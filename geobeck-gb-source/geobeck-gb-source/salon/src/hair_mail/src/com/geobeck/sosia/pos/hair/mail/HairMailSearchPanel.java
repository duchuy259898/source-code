/*
 * HairMailSearchPanel.java
 *
 * Created on 2006/10/23, 11:24
 */
package com.geobeck.sosia.pos.hair.mail;

import com.geobeck.sosia.pos.customer.MobileMemberData;
import com.geobeck.sosia.pos.customer.MobileMemberList;
import com.geobeck.sosia.pos.hair.data.mail.DataCondition;
import com.geobeck.sosia.pos.hair.data.mail.DataConditionDetail;
import com.geobeck.sosia.pos.hair.master.company.MstResponse;
import com.geobeck.sosia.pos.hair.master.company.MstResponses;
import com.geobeck.util.SQLUtil;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.mail.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.master.company.*;

import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.hair.report.util.*;
import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import com.geobeck.sosia.pos.master.system.MstSetting;
import com.geobeck.sosia.pos.products.Product;
import com.geobeck.sosia.pos.products.ProductClass;
import com.geobeck.sosia.pos.products.ProductClasses;
import com.geobeck.sosia.pos.search.mail.SearchConditionDialog;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.swing.filechooser.WildcardFileFilter;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.DateUtil;
import com.ibm.icu.util.JapaneseCalendar;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.FormSubmitEvent;
import javax.swing.text.html.HTMLEditorKit;
/**
 *
 * @author katagiri
 */
public class HairMailSearchPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx implements SelectMailAddressDialogOpener {

    public final int SEND_TYPE_E_MAIL = 0;
    public final int SEND_TYPE_DIRECT_MAIL = 1;
    public final int SEND_TYPE_EXCEL = 2;
    Map<Integer, MapProduct> condTechClass = new HashMap<Integer, MapProduct>();
    Map<Integer, MapProduct> condItemClass = new HashMap<Integer, MapProduct>();
    //IVS_LVTu start add 2016/03/16 getCondTechClassCondition
    Map<Integer, MapProduct> condCourseClass = new HashMap<Integer, MapProduct>();
    Map<Integer, MapProduct> condTech = new HashMap<Integer, MapProduct>();
    Map<Integer, MapProduct> condItem = new HashMap<Integer, MapProduct>();
    Map<Integer, MapProduct> condCourse = new HashMap<Integer, MapProduct>();
    //IVS_LVTu end add 2016/03/16 getCondTechClassCondition
    String nowString;
     private ArrayList<MstFreeHeadingSelectUnitPanel> mfhsups = new ArrayList<MstFreeHeadingSelectUnitPanel>();        
    private ArrayList<MstFreeHeadingSelectTextFiled> mfhsuptext = new ArrayList<MstFreeHeadingSelectTextFiled>();  
    private ArrayList<MstCustomerFreeHeading> mcfhs = new ArrayList<MstCustomerFreeHeading>();
    private ArrayList<String[]> arrOrderByFreeHeading = new ArrayList<String[]>();        
    private int numFreeHeadingClass = 0;
    //IVS start add 2022/07/12 0624_出力項目の追加
    private String DB_POS_HAIR_BIP = "pos_hair_bip";
    //IVS end add 2022/07/12 0624_出力項目の追加


    private MstFreeHeadingClasses mfhcs;
    //IVS_LVTU start add 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
    private boolean isCustomerNo_0 = false;
    //IVS_LVTU end add 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
    /**
     * Creates new form HairMailSearchPanel
     */
    public HairMailSearchPanel() {
        super();
        initComponents();
        addMouseCursorChange();
        this.setSize(830, 680);
        this.setPath("メール機能");
        this.setTitle("条件検索");
        SystemInfo.initGroupShopComponents(target, 3);
        initComponentData();
        this.getFreeHeadingDatas(); // フリー項目
        // SOSIA連動不可ならワイルドカードメール送信ボタンを非表示にする
        //wildMailButton.setVisible( SystemInfo.isSosiaGearEnable() );
        //wildMailButton.setVisible( false );
        //機能実装がまだなので非表示に
        itemOnly.setVisible(false);

        // EXCEL出力ボタン表示設定
        this.btnOutputExcel.setVisible(SystemInfo.checkAuthority(56));
        // CSV出力 vtbphuong start add 20140702  Request #26403
        this.btnOutputCsv.setVisible(SystemInfo.checkAuthority(56));
         // CSV出力 vtbphuong end add 20140702   Request #26403
        // メール作成ボタン表示設定
        this.mailButton.setVisible(SystemInfo.checkAuthority(53));
        // 宛名ラベル作成ボタン表示設定
        this.sealButton.setVisible(SystemInfo.checkAuthority(55));
        // ハガキ作成ボタン表示設定
        this.postcardButton.setVisible(SystemInfo.checkAuthority(54));

        // SOSIA連動不可の場合には連動パネルを非表示にする
        if (!SystemInfo.isSosiaGearEnable()) {
            sosiaGearPanel.setVisible(false);
        }

        condTemplateButton.setEnabled(DataCondition.getList(SystemInfo.getCurrentShop()).size() > 0);

        this.initProductClasses();
        
        //IVS_LVTu start add 2016/03/16 getCondTechClassCondition
        CheckCourseFlag();
        initCourseClasses();
        //IVS_LVTu end add 2016/03/16 getCondTechClassCondition
        //IVS_LVTu start add 2016/02/24 New request #48455
        if (SystemInfo.getSystemType().equals(2)) {
            techItemPanel.setVisible(false);
        }
        //IVS_LVTu end add 2016/02/24 New request #48455

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateButtonGroup = new javax.swing.ButtonGroup();
        sexButtonGroup = new javax.swing.ButtonGroup();
        mailButtonGroup = new javax.swing.ButtonGroup();
        addButtonGroup = new javax.swing.ButtonGroup();
        visitCountButtonGroup = new javax.swing.ButtonGroup();
        chargeStaffButtonGroup = new javax.swing.ButtonGroup();
        sendMailButtonGroup = new javax.swing.ButtonGroup();
        sendDmButtonGroup = new javax.swing.ButtonGroup();
        callFlagButtonGroup = new javax.swing.ButtonGroup();
        pcMailButtonGroup = new javax.swing.ButtonGroup();
        cellularMailButtonGroup = new javax.swing.ButtonGroup();
        sosiaGearButtonGroup = new javax.swing.ButtonGroup();
        techItemGroup = new javax.swing.ButtonGroup();
        postcardButton = new javax.swing.JButton();
        sealButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        chargeStaffNo = new javax.swing.JTextField();
        ((PlainDocument)chargeStaffNo.getDocument()).setDocumentFilter(
            new CustomFilter(10, CustomFilter.ALPHAMERIC));
        jLabel5 = new javax.swing.JLabel();
        chargeStaff = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        firstComingMotive = new javax.swing.JComboBox();
        chargeStaffAll = new javax.swing.JRadioButton();
        chargeStaffNamed = new javax.swing.JRadioButton();
        chargeStaffFree = new javax.swing.JRadioButton();
        jLabel40 = new javax.swing.JLabel();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            mrs.load(con);

            con.close();

            MstResponse mr = new MstResponse();
            mr.setResponseID( -1 );
            mr.setResponseName( "" );
            mr.setDisplaySeq( -1 );
            mrs.add( 0, mr );

        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        responseItem = new JComboBox( mrs.toArray() );
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        pcMailExists = new javax.swing.JRadioButton();
        pcMailNotExists = new javax.swing.JRadioButton();
        pcMailAll = new javax.swing.JRadioButton();
        cellularMailExists = new javax.swing.JRadioButton();
        cellularMailNotExists = new javax.swing.JRadioButton();
        cellularMailAll = new javax.swing.JRadioButton();
        sosiaGearPanel = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        sosiaGearExists = new javax.swing.JRadioButton();
        sosiaGearNotExists = new javax.swing.JRadioButton();
        sosiaGearAll = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        freeHeadingPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        targetLabel = new javax.swing.JLabel();
        itemOnly = new javax.swing.JCheckBox();
        target = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        visitCountFrom = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)visitCountFrom.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        jLabel20 = new javax.swing.JLabel();
        visitCountTo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)visitCountTo.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        jLabel21 = new javax.swing.JLabel();
        dateType1 = new javax.swing.JRadioButton();
        dateType0 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        visitDateFrom = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel3 = new javax.swing.JLabel();
        visitDateTo = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        dateType2 = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        female = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        male = new javax.swing.JRadioButton();
        both = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        ageFrom = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)ageFrom.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        jLabel12 = new javax.swing.JLabel();
        ageTo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)ageTo.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        jLabel13 = new javax.swing.JLabel();
        birthMonth = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        birthdayFrom = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel16 = new javax.swing.JLabel();
        birthdayTo = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        visitCountType1 = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        isIntroducer = new javax.swing.JCheckBox();
        isIntroduced = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        customerKana1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerKana1.getDocument()).setDocumentFilter(
            new CustomFilter(40));
        customerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerName1.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addAll = new javax.swing.JRadioButton();
        addEnd = new javax.swing.JRadioButton();
        addYet = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        postalCode = postalCode = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createPostalCodeFormatter());
        jLabel11 = new javax.swing.JLabel();
        prefecture = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        city = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        job = new javax.swing.JComboBox();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        memo = new com.geobeck.swing.JFormattedTextFieldEx();
        priceFrom = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)priceFrom.getDocument()).setDocumentFilter(
            new CustomFilter(8, CustomFilter.NUMERIC));
        priceTo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)priceTo.getDocument()).setDocumentFilter(
            new CustomFilter(8, CustomFilter.NUMERIC));
        visitCountType0 = new javax.swing.JRadioButton();
        customerKana2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerKana2.getDocument()).setDocumentFilter(
            new CustomFilter(40));
        customerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerName2.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        jLabel22 = new javax.swing.JLabel();
        sendMailOK = new javax.swing.JRadioButton();
        sendMailNG = new javax.swing.JRadioButton();
        sendMailAll = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        sendDmOK = new javax.swing.JRadioButton();
        sendDmNG = new javax.swing.JRadioButton();
        sendDmAll = new javax.swing.JRadioButton();
        callFlagOK = new javax.swing.JRadioButton();
        callFlagNG = new javax.swing.JRadioButton();
        callFlagAll = new javax.swing.JRadioButton();
        jLabel44 = new javax.swing.JLabel();
        customerNo1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo1.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        customerNo2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo2.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        jLabel45 = new javax.swing.JLabel();
        dateType3 = new javax.swing.JRadioButton();
        clearButton = new javax.swing.JButton();
        btnOutputExcel = new javax.swing.JButton();
        condTemplateButton = new javax.swing.JButton();
        condRegistButton = new javax.swing.JButton();
        condName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerName1.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        jLabel47 = new javax.swing.JLabel();
        techItemPanel = new javax.swing.JPanel();
        productDivision = new javax.swing.JTabbedPane();
        technicPanel = new javax.swing.JPanel();
        technicScrollPane = new javax.swing.JScrollPane();
        technic = new javax.swing.JTable();
        technicClassScrollPane = new javax.swing.JScrollPane();
        technicClass = new javax.swing.JTable();
        itemPanel = new javax.swing.JPanel();
        itemScrollPane = new javax.swing.JScrollPane();
        item = new javax.swing.JTable();
        itemClassScrollPane = new javax.swing.JScrollPane();
        itemClass = new javax.swing.JTable();
        coursePanel = new javax.swing.JPanel();
        courseScrollPane = new javax.swing.JScrollPane();
        tbCourse = new javax.swing.JTable();
        courseClassScrollPane = new javax.swing.JScrollPane();
        tbCourseClass = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        hint = new javax.swing.JTextArea();
        techItemOR = new javax.swing.JRadioButton();
        techItemAND = new javax.swing.JRadioButton();
        jLabel50 = new javax.swing.JLabel();
        techItemNOT = new javax.swing.JRadioButton();
        btnOutputCsv = new javax.swing.JButton();
        mailButton = new javax.swing.JButton();
        DigitalPost = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();

        setFocusCycleRoot(true);
        setLayout(null);

        postcardButton.setIcon(SystemInfo.getImageIcon("/button/mail/create_postcard_off.jpg"));
        postcardButton.setActionCommand("ハガキ印刷");
        postcardButton.setBorderPainted(false);
        postcardButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/create_postcard_on.jpg"));
        postcardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postcardButtonActionPerformed(evt);
            }
        });
        add(postcardButton);
        postcardButton.setBounds(630, 10, 92, 25);

        sealButton.setIcon(SystemInfo.getImageIcon("/button/mail/create_seal_off.jpg"));
        sealButton.setBorderPainted(false);
        sealButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/create_seal_on.jpg"));
        sealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sealButtonActionPerformed(evt);
            }
        });
        add(sealButton);
        sealButton.setBounds(530, 10, 92, 25);

        jPanel1.setOpaque(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("主担当者");

        chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chargeStaffNoFocusLost(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("指名区分");

        chargeStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeStaffActionPerformed(evt);
            }
        });

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("初回来店動機");

        chargeStaffButtonGroup.add(chargeStaffAll);
        chargeStaffAll.setSelected(true);
        chargeStaffAll.setText("すべて");
        chargeStaffAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chargeStaffAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chargeStaffAll.setOpaque(false);

        chargeStaffButtonGroup.add(chargeStaffNamed);
        chargeStaffNamed.setText("指名");
        chargeStaffNamed.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chargeStaffNamed.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chargeStaffNamed.setOpaque(false);

        chargeStaffButtonGroup.add(chargeStaffFree);
        chargeStaffFree.setText("フリー");
        chargeStaffFree.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chargeStaffFree.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chargeStaffFree.setOpaque(false);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("反響項目");

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("PCメール");

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("携帯メール");

        pcMailButtonGroup.add(pcMailExists);
        pcMailExists.setText("有り");
        pcMailExists.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pcMailExists.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pcMailExists.setOpaque(false);

        pcMailButtonGroup.add(pcMailNotExists);
        pcMailNotExists.setText("無し");
        pcMailNotExists.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pcMailNotExists.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pcMailNotExists.setOpaque(false);

        pcMailButtonGroup.add(pcMailAll);
        pcMailAll.setSelected(true);
        pcMailAll.setText("すべて");
        pcMailAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pcMailAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pcMailAll.setOpaque(false);

        cellularMailButtonGroup.add(cellularMailExists);
        cellularMailExists.setText("有り");
        cellularMailExists.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cellularMailExists.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cellularMailExists.setOpaque(false);

        cellularMailButtonGroup.add(cellularMailNotExists);
        cellularMailNotExists.setText("無し");
        cellularMailNotExists.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cellularMailNotExists.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cellularMailNotExists.setOpaque(false);

        cellularMailButtonGroup.add(cellularMailAll);
        cellularMailAll.setSelected(true);
        cellularMailAll.setText("すべて");
        cellularMailAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cellularMailAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cellularMailAll.setOpaque(false);

        sosiaGearPanel.setOpaque(false);
        sosiaGearPanel.setLayout(null);

        jLabel46.setText("WEB会員連動");
        sosiaGearPanel.add(jLabel46);
        jLabel46.setBounds(0, 0, 90, 13);

        sosiaGearButtonGroup.add(sosiaGearExists);
        sosiaGearExists.setText("有り");
        sosiaGearExists.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sosiaGearExists.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sosiaGearExists.setOpaque(false);
        sosiaGearPanel.add(sosiaGearExists);
        sosiaGearExists.setBounds(90, 0, 50, 13);

        sosiaGearButtonGroup.add(sosiaGearNotExists);
        sosiaGearNotExists.setText("無し");
        sosiaGearNotExists.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sosiaGearNotExists.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sosiaGearNotExists.setOpaque(false);
        sosiaGearPanel.add(sosiaGearNotExists);
        sosiaGearNotExists.setBounds(145, 0, 60, 13);

        sosiaGearButtonGroup.add(sosiaGearAll);
        sosiaGearAll.setSelected(true);
        sosiaGearAll.setText("すべて");
        sosiaGearAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sosiaGearAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sosiaGearAll.setOpaque(false);
        sosiaGearPanel.add(sosiaGearAll);
        sosiaGearAll.setBounds(211, 0, 70, 13);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(339, 270));

        freeHeadingPanel.setPreferredSize(new java.awt.Dimension(320, 270));

        org.jdesktop.layout.GroupLayout freeHeadingPanelLayout = new org.jdesktop.layout.GroupLayout(freeHeadingPanel);
        freeHeadingPanel.setLayout(freeHeadingPanelLayout);
        freeHeadingPanelLayout.setHorizontalGroup(
            freeHeadingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 451, Short.MAX_VALUE)
        );
        freeHeadingPanelLayout.setVerticalGroup(
            freeHeadingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 270, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(freeHeadingPanel);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(sosiaGearPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 367, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jLabel42)
                                .add(jLabel43))
                            .add(jLabel40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(17, 17, 17)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(chargeStaffAll))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(10, 10, 10)
                                        .add(chargeStaffNamed)
                                        .add(23, 23, 23)
                                        .add(chargeStaffFree))))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(18, 18, 18)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(responseItem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                            .add(jPanel1Layout.createSequentialGroup()
                                                .add(cellularMailExists, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(cellularMailNotExists, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .add(jPanel1Layout.createSequentialGroup()
                                                .add(pcMailExists, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(pcMailNotExists, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                        .add(18, 18, 18)
                                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(cellularMailAll)
                                            .add(pcMailAll))))))))
                .addContainerGap(91, Short.MAX_VALUE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(firstComingMotive, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(130, 130, 130))
            .add(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel17)
                    .add(firstComingMotive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .add(8, 8, 8)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(chargeStaffAll)
                    .add(chargeStaffNamed)
                    .add(chargeStaffFree))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel40, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(responseItem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(14, 14, 14)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(pcMailExists)
                            .add(jLabel42)
                            .add(pcMailNotExists))
                        .add(6, 6, 6)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel43)
                            .add(cellularMailExists)
                            .add(cellularMailNotExists)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(pcMailAll)
                        .add(6, 6, 6)
                        .add(cellularMailAll)))
                .add(6, 6, 6)
                .add(sosiaGearPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0))
        );

        add(jPanel1);
        jPanel1.setBounds(350, 100, 470, 300);

        jPanel3.setOpaque(false);

        targetLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel.setText("対象");

        itemOnly.setText("商品購入のみの顧客は含まない");
        itemOnly.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        itemOnly.setMargin(new java.awt.Insets(0, 0, 0, 0));
        itemOnly.setOpaque(false);

        target.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(targetLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 28, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(itemOnly)
                    .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 182, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(targetLabel))
                .add(4, 4, 4)
                .add(itemOnly)
                .add(65, 65, 65))
        );

        add(jPanel3);
        jPanel3.setBounds(0, 0, 300, 50);

        jPanel7.setOpaque(false);

        jLabel19.setText("来店回数");

        visitCountFrom.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel20.setText("回目　～　");

        visitCountTo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel21.setText("回目");

        dateButtonGroup.add(dateType1);
        dateType1.setText("最終来店日");
        dateType1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateType1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateType1.setOpaque(false);
        dateType1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateType1ActionPerformed(evt);
            }
        });

        dateButtonGroup.add(dateType0);
        dateType0.setSelected(true);
        dateType0.setText("来店日付");
        dateType0.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateType0.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateType0.setOpaque(false);
        dateType0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateType0ActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("来店日付");

        jLabel3.setText("～");

        dateButtonGroup.add(dateType2);
        dateType2.setText("次回予約日");
        dateType2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateType2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateType2.setOpaque(false);
        dateType2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateType2ActionPerformed(evt);
            }
        });

        jLabel10.setText("日付条件");

        sexButtonGroup.add(female);
        female.setText("女");
        female.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        female.setMargin(new java.awt.Insets(0, 0, 0, 0));
        female.setOpaque(false);

        jLabel8.setText("性別");

        sexButtonGroup.add(male);
        male.setText("男");
        male.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        male.setMargin(new java.awt.Insets(0, 0, 0, 0));
        male.setOpaque(false);

        sexButtonGroup.add(both);
        both.setSelected(true);
        both.setText("すべて");
        both.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        both.setMargin(new java.awt.Insets(0, 0, 0, 0));
        both.setOpaque(false);

        jLabel14.setText("生年月日");

        ageFrom.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setText("歳　～　");

        ageTo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel13.setText("歳");

        birthMonth.setMaximumRowCount(20);
        birthMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        birthMonth.addItem("");
        for(Integer i = 1; i <= 12; i ++)
        birthMonth.addItem(i.toString());

        jLabel15.setText("月生まれ");

        jLabel16.setText("～");

        visitCountButtonGroup.add(visitCountType1);
        visitCountType1.setText("来店日付期間内の来店回数");
        visitCountType1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        visitCountType1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        visitCountType1.setOpaque(false);

        jLabel24.setText("売上金額");

        jLabel26.setText("円　～　");

        jLabel28.setText("円");

        jLabel30.setText("紹介");

        jLabel32.setText("年齢");

        isIntroducer.setText("紹介した人");
        isIntroducer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        isIntroducer.setMargin(new java.awt.Insets(0, 0, 0, 0));
        isIntroducer.setOpaque(false);

        isIntroduced.setText("紹介された人");
        isIntroduced.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        isIntroduced.setMargin(new java.awt.Insets(0, 0, 0, 0));
        isIntroduced.setOpaque(false);

        jLabel6.setText("ふりがな");

        customerKana1.setInputKanji(true);

        customerName1.setInputKanji(true);

        jLabel7.setText("氏名");

        jLabel4.setText("住所対象");

        addButtonGroup.add(addAll);
        addAll.setSelected(true);
        addAll.setText("すべて");
        addAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        addAll.setOpaque(false);

        addButtonGroup.add(addEnd);
        addEnd.setText("住所入力済");
        addEnd.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addEnd.setMargin(new java.awt.Insets(0, 0, 0, 0));
        addEnd.setOpaque(false);

        addButtonGroup.add(addYet);
        addYet.setText("住所未入力");
        addYet.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addYet.setMargin(new java.awt.Insets(0, 0, 0, 0));
        addYet.setOpaque(false);

        jLabel9.setText("郵便番号");

        jLabel11.setText("都道府県");

        prefecture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefectureActionPerformed(evt);
            }
        });

        jLabel34.setText("市区町村");

        jLabel18.setText("職業");

        jLabel33.setText("備考");

        jLabel35.setText("※ 備考内の文字を検索します");

        memo.setInputKanji(true);

        priceFrom.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        priceTo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        visitCountButtonGroup.add(visitCountType0);
        visitCountType0.setSelected(true);
        visitCountType0.setText("累計来店回数");
        visitCountType0.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        visitCountType0.setMargin(new java.awt.Insets(0, 0, 0, 0));
        visitCountType0.setOpaque(false);

        customerKana2.setInputKanji(true);

        customerName2.setInputKanji(true);

        jLabel22.setText("メール配信");

        sendMailButtonGroup.add(sendMailOK);
        sendMailOK.setText("可");
        sendMailOK.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sendMailOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sendMailOK.setOpaque(false);

        sendMailButtonGroup.add(sendMailNG);
        sendMailNG.setText("不可");
        sendMailNG.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sendMailNG.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sendMailNG.setOpaque(false);

        sendMailButtonGroup.add(sendMailAll);
        sendMailAll.setSelected(true);
        sendMailAll.setText("すべて");
        sendMailAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sendMailAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sendMailAll.setOpaque(false);

        jLabel23.setText("ＤＭ配信");

        jLabel41.setText("電話連絡");

        sendDmButtonGroup.add(sendDmOK);
        sendDmOK.setText("可");
        sendDmOK.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sendDmOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sendDmOK.setOpaque(false);

        sendDmButtonGroup.add(sendDmNG);
        sendDmNG.setText("不可");
        sendDmNG.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sendDmNG.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sendDmNG.setOpaque(false);

        sendDmButtonGroup.add(sendDmAll);
        sendDmAll.setSelected(true);
        sendDmAll.setText("すべて");
        sendDmAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sendDmAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        sendDmAll.setOpaque(false);

        callFlagButtonGroup.add(callFlagOK);
        callFlagOK.setText("可");
        callFlagOK.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        callFlagOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        callFlagOK.setOpaque(false);

        callFlagButtonGroup.add(callFlagNG);
        callFlagNG.setText("不可");
        callFlagNG.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        callFlagNG.setMargin(new java.awt.Insets(0, 0, 0, 0));
        callFlagNG.setOpaque(false);

        callFlagButtonGroup.add(callFlagAll);
        callFlagAll.setSelected(true);
        callFlagAll.setText("すべて");
        callFlagAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        callFlagAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        callFlagAll.setOpaque(false);

        jLabel44.setText("顧客No.");

        customerNo1.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("inactiveCaption")));
        customerNo1.setColumns(15);
        customerNo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNo1FocusLost(evt);
            }
        });
        customerNo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customerNo1KeyPressed(evt);
            }
        });

        customerNo2.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("inactiveCaption")));
        customerNo2.setColumns(15);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("～");

        dateButtonGroup.add(dateType3);
        dateType3.setText("初回来店日");
        dateType3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateType3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateType3.setOpaque(false);
        dateType3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateType3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(12, 12, 12)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                                .add(19, 19, 19)
                                .add(jLabel10)
                                .add(25, 25, 25))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
                                        .add(15, 15, 15)
                                        .add(jLabel24))
                                    .add(jLabel30)
                                    .add(jLabel4)
                                    .add(jLabel7)
                                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel44)
                                        .add(jLabel6))
                                    .add(jLabel9)
                                    .add(jLabel11)
                                    .add(jLabel34)
                                    .add(jLabel18)
                                    .add(jLabel33))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(isIntroducer)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(isIntroduced))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(priceFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel26)
                                .add(4, 4, 4)
                                .add(priceTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel28))
                            .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jLabel35)
                                .add(city, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(prefecture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(postalCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jPanel7Layout.createSequentialGroup()
                                    .add(addAll)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(addEnd)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(addYet))
                                .add(jPanel7Layout.createSequentialGroup()
                                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, customerName1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, customerKana1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(customerName2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(customerKana2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)))
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                                    .add(customerNo1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jLabel45, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(customerNo2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(job, 0, 225, Short.MAX_VALUE)
                                .add(memo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(dateType0)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(dateType1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(dateType2))
                            .add(dateType3)))
                    .add(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel41)
                            .add(jLabel23)
                            .add(jLabel22)
                            .add(jLabel8)
                            .add(jLabel32)
                            .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel19, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel14))
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(25, 25, 25)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(visitCountType0)
                            .add(visitCountType1)
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(ageFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel12)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(ageTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel13))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(female, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(male)
                                .add(15, 15, 15)
                                .add(both))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(visitDateFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(visitDateTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(visitCountFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel20)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(visitCountTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel21))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel7Layout.createSequentialGroup()
                                        .add(birthMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel15))
                                    .add(jPanel7Layout.createSequentialGroup()
                                        .add(sendMailOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(sendMailNG))
                                    .add(jPanel7Layout.createSequentialGroup()
                                        .add(sendDmOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(sendDmNG))
                                    .add(jPanel7Layout.createSequentialGroup()
                                        .add(callFlagOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(callFlagNG)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(callFlagAll)
                                    .add(sendDmAll)
                                    .add(sendMailAll)))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(birthdayFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel16)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(birthdayTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel10)
                            .add(dateType0)
                            .add(dateType1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(dateType2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(30, 30, 30)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(visitDateFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, visitDateTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(5, 5, 5)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(female, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(male, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel8)
                            .add(both, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(ageFrom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(ageTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel32))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(birthMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel15)
                            .add(jLabel14))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(birthdayFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, Short.MAX_VALUE))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(birthdayTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel16))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 13, Short.MAX_VALUE))))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                        .add(dateType3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(138, 138, 138)))
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel22)
                    .add(sendMailOK)
                    .add(sendMailNG)
                    .add(sendMailAll))
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel23)
                    .add(sendDmOK)
                    .add(sendDmNG)
                    .add(sendDmAll))
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel41)
                    .add(callFlagOK)
                    .add(callFlagNG)
                    .add(callFlagAll))
                .add(12, 12, 12)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(visitCountFrom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel20, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(visitCountTo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel21, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel19))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(visitCountType0)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(visitCountType1)
                .add(6, 6, 6)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel24)
                    .add(jLabel26)
                    .add(jLabel28)
                    .add(priceFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(priceTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(isIntroducer)
                    .add(isIntroduced)
                    .add(jLabel30))
                .add(8, 8, 8)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel45, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .add(customerNo1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(customerNo2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel44))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(customerKana1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(customerKana2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addAll)
                    .add(addEnd)
                    .add(addYet)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(postalCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(prefecture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel11))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(city, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel34))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(job, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel18))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(memo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel33))
                .add(7, 7, 7)
                .add(jLabel35)
                .add(5, 5, 5))
        );

        add(jPanel7);
        jPanel7.setBounds(0, 55, 340, 630);

        clearButton.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg"));
        clearButton.setBorderPainted(false);
        clearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg"));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        add(clearButton);
        clearButton.setBounds(730, 40, 92, 25);

        btnOutputExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutputExcel.setBorderPainted(false);
        btnOutputExcel.setFocusCycleRoot(true);
        btnOutputExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutputExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputExcelActionPerformed(evt);
            }
        });
        add(btnOutputExcel);
        btnOutputExcel.setBounds(330, 10, 92, 25);

        condTemplateButton.setIcon(SystemInfo.getImageIcon("/button/mail/template_off.jpg"));
        condTemplateButton.setBorderPainted(false);
        condTemplateButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/template_on.jpg"));
        condTemplateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                condTemplateButtonActionPerformed(evt);
            }
        });
        add(condTemplateButton);
        condTemplateButton.setBounds(630, 40, 92, 25);

        condRegistButton.setIcon(SystemInfo.getImageIcon("/button/mail/cond_regist_off.jpg"));
        condRegistButton.setBorderPainted(false);
        condRegistButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/cond_regist_on.jpg"));
        condRegistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                condRegistButtonActionPerformed(evt);
            }
        });
        add(condRegistButton);
        condRegistButton.setBounds(530, 40, 92, 25);

        condName.setInputKanji(true);
        add(condName);
        condName.setBounds(350, 40, 170, 25);

        jLabel47.setText("条件名");
        add(jLabel47);
        jLabel47.setBounds(300, 40, 40, 25);

        techItemPanel.setOpaque(false);

        productDivision.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                productDivisionStateChanged(evt);
            }
        });

        technicPanel.setOpaque(false);

        technicScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        technic.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "技術名", "選択"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technic.setSelectionBackground(new java.awt.Color(255, 210, 142));
        technic.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technic.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technic.getTableHeader().setReorderingAllowed(false);
        technic.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(technic, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(technic);
        technic.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                technicPropertyChange(evt);
            }
        });
        technicScrollPane.setViewportView(technic);

        technicClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        technicClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "技術分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technicClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        technicClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technicClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicClass.getTableHeader().setReorderingAllowed(false);
        technicClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(technicClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(technicClass);
        technicClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                technicClassMouseReleased(evt);
            }
        });
        technicClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                technicClassKeyReleased(evt);
            }
        });
        technicClassScrollPane.setViewportView(technicClass);

        org.jdesktop.layout.GroupLayout technicPanelLayout = new org.jdesktop.layout.GroupLayout(technicPanel);
        technicPanel.setLayout(technicPanelLayout);
        technicPanelLayout.setHorizontalGroup(
            technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicPanelLayout.createSequentialGroup()
                .add(technicClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(technicScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        technicPanelLayout.setVerticalGroup(
            technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicClassScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
            .add(technicScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );

        productDivision.addTab("　　技術　　", technicPanel);

        itemPanel.setOpaque(false);

        itemScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        item.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "商品名", "選択"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        item.setSelectionBackground(new java.awt.Color(255, 210, 142));
        item.setSelectionForeground(new java.awt.Color(0, 0, 0));
        item.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        item.getTableHeader().setReorderingAllowed(false);
        item.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(item, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(item);
        item.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                itemPropertyChange(evt);
            }
        });
        itemScrollPane.setViewportView(item);

        itemClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        itemClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "商品分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        itemClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        itemClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemClass.getTableHeader().setReorderingAllowed(false);
        itemClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(itemClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(itemClass);
        itemClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                itemClassMouseReleased(evt);
            }
        });
        itemClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemClassKeyReleased(evt);
            }
        });
        itemClassScrollPane.setViewportView(itemClass);

        org.jdesktop.layout.GroupLayout itemPanelLayout = new org.jdesktop.layout.GroupLayout(itemPanel);
        itemPanel.setLayout(itemPanelLayout);
        itemPanelLayout.setHorizontalGroup(
            itemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemPanelLayout.createSequentialGroup()
                .add(itemClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(itemScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        itemPanelLayout.setVerticalGroup(
            itemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemClassScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
            .add(itemScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );

        productDivision.addTab("　　商品　　", itemPanel);

        coursePanel.setOpaque(false);

        courseScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbCourse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "コース名", "選択"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCourse.setSelectionBackground(new java.awt.Color(255, 210, 142));
        tbCourse.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbCourse.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbCourse.getTableHeader().setReorderingAllowed(false);
        tbCourse.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(tbCourse, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(tbCourse);
        tbCourse.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbCoursePropertyChange(evt);
            }
        });
        courseScrollPane.setViewportView(tbCourse);

        courseClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbCourseClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "コース分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCourseClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        tbCourseClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbCourseClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbCourseClass.getTableHeader().setReorderingAllowed(false);
        tbCourseClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(tbCourseClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(tbCourseClass);
        tbCourseClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbCourseClassMouseReleased(evt);
            }
        });
        tbCourseClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbCourseClassKeyReleased(evt);
            }
        });
        courseClassScrollPane.setViewportView(tbCourseClass);

        org.jdesktop.layout.GroupLayout coursePanelLayout = new org.jdesktop.layout.GroupLayout(coursePanel);
        coursePanel.setLayout(coursePanelLayout);
        coursePanelLayout.setHorizontalGroup(
            coursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(coursePanelLayout.createSequentialGroup()
                .add(courseClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(courseScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        coursePanelLayout.setVerticalGroup(
            coursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(courseClassScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
            .add(courseScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        productDivision.addTab("　コース　", coursePanel);

        hint.setBackground(new java.awt.Color(255, 255, 204));
        hint.setColumns(20);
        hint.setEditable(false);
        hint.setFont(new java.awt.Font("ＭＳ Ｐゴシック", 0, 12)); // NOI18N
        hint.setLineWrap(true);
        hint.setRows(1);
        hint.setTabSize(1);
        jScrollPane1.setViewportView(hint);

        techItemGroup.add(techItemOR);
        techItemOR.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        techItemOR.setSelected(true);
        techItemOR.setText("OR");
        techItemOR.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        techItemOR.setMargin(new java.awt.Insets(0, 0, 0, 0));
        techItemOR.setOpaque(false);

        techItemGroup.add(techItemAND);
        techItemAND.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        techItemAND.setText("AND");
        techItemAND.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        techItemAND.setMargin(new java.awt.Insets(0, 0, 0, 0));
        techItemAND.setOpaque(false);

        jLabel50.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("⇒ ");

        techItemGroup.add(techItemNOT);
        techItemNOT.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        techItemNOT.setText("NOT");
        techItemNOT.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        techItemNOT.setMargin(new java.awt.Insets(0, 0, 0, 0));
        techItemNOT.setOpaque(false);

        org.jdesktop.layout.GroupLayout techItemPanelLayout = new org.jdesktop.layout.GroupLayout(techItemPanel);
        techItemPanel.setLayout(techItemPanelLayout);
        techItemPanelLayout.setHorizontalGroup(
            techItemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(techItemPanelLayout.createSequentialGroup()
                .add(productDivision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 342, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(techItemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(techItemPanelLayout.createSequentialGroup()
                        .add(techItemOR)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(techItemAND))
                    .add(techItemNOT))
                .addContainerGap())
            .add(techItemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, techItemPanelLayout.createSequentialGroup()
                    .add(0, 343, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        techItemPanelLayout.setVerticalGroup(
            techItemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(techItemPanelLayout.createSequentialGroup()
                .add(techItemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(productDivision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 252, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(techItemPanelLayout.createSequentialGroup()
                        .add(techItemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(techItemAND)
                            .add(techItemOR)
                            .add(jLabel50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(4, 4, 4)
                        .add(techItemNOT)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(techItemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(techItemPanelLayout.createSequentialGroup()
                    .add(37, 37, 37)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 211, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(15, Short.MAX_VALUE)))
        );

        add(techItemPanel);
        techItemPanel.setBounds(350, 410, 480, 250);

        btnOutputCsv.setIcon(SystemInfo.getImageIcon("/button/print/csv_off.jpg"));
        btnOutputCsv.setBorderPainted(false);
        btnOutputCsv.setFocusCycleRoot(true);
        btnOutputCsv.setPressedIcon(SystemInfo.getImageIcon("/button/print/csv_on.jpg"));
        btnOutputCsv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputCsvActionPerformed(evt);
            }
        });
        add(btnOutputCsv);
        btnOutputCsv.setBounds(430, 10, 92, 25);

        mailButton.setIcon(SystemInfo.getImageIcon("/button/mail/create_mail_off.jpg"));
        mailButton.setBorderPainted(false);
        mailButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/create_mail_on.jpg"));
        mailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mailButtonActionPerformed(evt);
            }
        });
        add(mailButton);
        mailButton.setBounds(730, 10, 92, 25);

        DigitalPost.setIcon(SystemInfo.getImageIcon("/button/mail/sosia_dm_off.jpg"));
        DigitalPost.setBorderPainted(false);
        DigitalPost.setPressedIcon(SystemInfo.getImageIcon("/button/mail/sosia_dm_on.jpg"));
        DigitalPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DigitalPostActionPerformed(evt);
            }
        });
        add(DigitalPost);
        DigitalPost.setBounds(730, 70, 92, 25);

        jLabel49.setForeground(java.awt.Color.red);
        jLabel49.setText("※選択された条件すべてに該当するものが検索対象となります。");
        add(jLabel49);
        jLabel49.setBounds(350, 660, 301, 20);

        jLabel48.setText("＜絞り込み条件＞");
        add(jLabel48);
        jLabel48.setBounds(700, 390, 120, 20);
    }// </editor-fold>//GEN-END:initComponents

    private String createFunctionSql(String shopIDList) {
        // 経過日数を取ってくる関数を定義
        nowString = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        String sql = "CREATE OR REPLACE FUNCTION tmp_get_progress_day" + nowString + "(integer, timestamp without time zone) \n"
                + "RETURNS integer AS \n"
                + "  'SELECT cast(date_part(''day'', $2 - max(sales_date)) as integer) FROM data_sales  \n"
                + "           INNER JOIN mst_customer USING(customer_id) \n"
                + "           WHERE customer_id = $1  \n"
                + "                  AND sales_date < $2  \n"
                + "                  AND customer_no != ''0''  \n"
                + "                  AND data_sales.delete_date IS NULL \n"
                + //                     "                  AND mst_customer.delete_date IS NULL  \n" +
                "                  AND data_sales.shop_id in (" + shopIDList + ") \n"
                + "  ' \n"
                + "    LANGUAGE 'sql' VOLATILE";

        return sql;
    }
    
    //IVS_LVTu start add 2016/03/16 getCondTechClassCondition
    private String listShopCourseFlag() {
        String listShopID = "";
        if(target.getSelectedItem() instanceof MstGroup) {
            MstGroup group = new MstGroup();
            group = (MstGroup) target.getSelectedItem();
            if (!checkShopCourseFlag(group)) {
                listShopID = getShopIDCourseFlag(group);
            }
        }else if(target.getSelectedItem() instanceof MstShop) {
            MstShop shop = new MstShop();
            shop = (MstShop) target.getSelectedItem();
            if(shop.getCourseFlag() == 1) {
                listShopID = shop.getShopID().toString();
            }
        }
        return listShopID;
    }
    
     private boolean CheckCourseFlag() {
         
        if(target.getSelectedItem() instanceof MstGroup) {
            MstGroup group = new MstGroup();
            group = (MstGroup) target.getSelectedItem();
            if (!checkShopCourseFlag(group)) {
                productDivision.remove(coursePanel);
                return false;
            }else {
                productDivision.remove(coursePanel);
                productDivision.add("　コース　",coursePanel);
            }
        }else if(target.getSelectedItem() instanceof MstShop) {
            MstShop shop = new MstShop();
            shop = (MstShop) target.getSelectedItem();
            if(shop.getCourseFlag() != 1) {
                productDivision.remove(coursePanel);
                return false;
            }else {
                productDivision.remove(coursePanel);
                productDivision.add("　コース　",coursePanel);
            }
        }
        return true;
    }
    
    private boolean checkShopCourseFlag(MstGroup mg) {
            //グループ
        if(mg.getShops().size()> 0) {
            for (MstShop mshop : mg.getShops()) {
                if ( mshop.getCourseFlag().equals(1)) {
                    return true;
                }
            }
        } else if ( mg.getGroups().size() > 0) {
            for ( int i = 0;i < mg.getGroups().size() ;i ++) {
                return checkShopCourseFlag(mg.getGroups().get(i));
            }
        }
        return false;
    }
    
    private String getShopIDCourseFlag(MstGroup mg) {
            //グループ
        String listShopID = "";    
        if(mg.getShops().size()> 0) {
            for (MstShop mshop : mg.getShops()) {
                if ( mshop.getCourseFlag().equals(1)) {
                    if(listShopID.equals("")) {
                        listShopID = mshop.getShopID().toString();
                    }else {
                        listShopID = listShopID + "," + mshop.getShopID().toString();
                    }
                    return listShopID;
                }
            }
        } else if ( mg.getGroups().size() > 0) {
            for ( int i = 0;i < mg.getGroups().size() ;i ++) {
                return getShopIDCourseFlag(mg.getGroups().get(i));
            }
        }
        return listShopID;
    }
    //IVS_LVTu end add 2016/03/16 getCondTechClassCondition

    /**
     * ******************************
     */
    /* 精算情報　Excel出力 */
    /**
     * ******************************
     */
    private ResultSetWrapper getInfoForAccountOutput(ConnectionWrapper con) {
        
        
        //nhanvt start add  20141009 Bug #30993
        MstSetting setting = SystemInfo.getSetteing();
        MstAccountSetting accountSetting = SystemInfo.getAccountSetting(); //201805 add #35807
        
        boolean isOld = true;
        try {
            if(setting.getVersionSql(con).contains("PostgreSQL 7.4")){
                isOld = true;
            }else{
                isOld = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //nhanvt end add  20141009 Bug #30993

        ResultSetWrapper rs = new ResultSetWrapper();

        // 按分使用有無
        boolean isProportionally = true;

        String shopIDList = "";
        //グループ
        if (target.getSelectedItem() instanceof MstGroup) {
            MstGroup mg = (MstGroup) target.getSelectedItem();
            shopIDList = mg.getShopIDListAll();
        } else {
            MstShop ms = (MstShop) target.getSelectedItem();
            shopIDList = ms.getShopID().toString();
            isProportionally = ms.getProportionally() == 1 ? true : false;
        }

        // 経過日数を取ってくる関数を定義
        String sqlTmp = createFunctionSql(shopIDList);

        try {
            con.execute(sqlTmp);
        } catch (Exception e) {
            System.err.println(sqlTmp);
            e.printStackTrace();
            return rs;//rs is empty
        }

        StringBuilder dsQuery = new StringBuilder(1000);

        if ((dateType0.isSelected() || dateType3.isSelected())) {

            /**
             * *****************************************
             */
            /*  来店日付 */
            /**
             * *****************************************
             */
            dsQuery.append("(");

            dsQuery.append("select * \n");
            dsQuery.append("  from data_sales ds \n");
            dsQuery.append(" where ds.delete_date is null and ds.shop_id in (" + shopIDList + ") \n");

            // 来店日付の始まりを条件に含める
            if (visitDateFrom.getDateStr() != null) {
                dsQuery.append(" and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'\n");
            }
            // 来店日付の終わりを条件に含める
            if (visitDateTo.getDateStr() != null) {
                dsQuery.append(" and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'\n");
            }

            dsQuery.append(")");

        } else if (dateType1.isSelected()) {

            /**
             * *****************************************
             */
            /*  最終来店日 */
            /**
             * *****************************************
             */
            if (visitDateFrom.getDateStr() != null || visitDateTo.getDateStr() != null) {

                dsQuery.append("(");

                dsQuery.append(" select * \n");
                dsQuery.append(" from data_sales ds \n");
                dsQuery.append("           inner join \n");
                dsQuery.append("               ( \n");
                dsQuery.append("                   select \n");
                dsQuery.append("                        customer_id as c_id \n");
                dsQuery.append("                       ,max(sales_date) as last_date \n");
                dsQuery.append("                     from data_sales \n");
                dsQuery.append("                    where delete_date IS NULL \n");
                dsQuery.append("                      and shop_id in (" + shopIDList + ") \n");
                dsQuery.append("                   group by \n");
                dsQuery.append("                       customer_id \n");
                dsQuery.append("               ) ds_last \n");
                dsQuery.append("               on ds_last.c_id = ds.customer_id \n");
                dsQuery.append("              and ds.sales_date = last_date \n");
                dsQuery.append(" where ds.delete_date is null \n");

                // 最終来店日の始まりを条件に含める
                if (visitDateFrom.getDateStr() != null) {
                    dsQuery.append(" and ds_last.last_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'\n");
                }
                // 最終来店日の終わりを条件に含める
                if (visitDateTo.getDateStr() != null) {
                    dsQuery.append(" and ds_last.last_date <= '" + visitDateTo.getDateStrWithLastTime() + "'\n");
                }

                //主担当者
                if (chargeStaff.getSelectedIndex() > 0) {
                    dsQuery.append(" and ds.staff_id = " + ((MstStaff) chargeStaff.getSelectedItem()).getStaffID());
                }

                // 主担当指名区分
                if (chargeStaffNamed.isSelected()) {
                    dsQuery.append(" and ds.designated_flag = true");
                }
                if (chargeStaffFree.isSelected()) {
                    dsQuery.append(" and ds.designated_flag = false");
                }
                //IVS_LVTu start edit 2016/03/16 getCondTechClassCondition
                if (this.isCondTechClassSelected() || this.isCondTechSelected() 
                        || this.isCondItemClassSelected() || this.isCondItemSelected()
                        ||this.isCondCourseClassSelected() || this.isCondCourseSelected()) {
                    dsQuery.append(" and exists");
                    dsQuery.append(" (");
                    dsQuery.append("     select 1");
                    dsQuery.append("     from");
                    dsQuery.append("         view_data_sales_detail_valid_with_prepaid dsd");
                    dsQuery.append("             left join mst_technic mt");
                    dsQuery.append("                     on dsd.product_division = 1");
                    dsQuery.append("                    and dsd.product_id = mt.technic_id");
                    dsQuery.append("             left outer join mst_item mi");
		    dsQuery.append("                          on dsd.product_division = 2");
		    dsQuery.append("                         and dsd.product_id = mi.item_id");
                    dsQuery.append("             left outer join mst_course mc");
		    dsQuery.append("                          on dsd.product_division = 5");
		    dsQuery.append("                         and dsd.product_id = mc.course_id");
                    dsQuery.append("     where");
                    dsQuery.append("             dsd.shop_id = ds.shop_id");
                    dsQuery.append("         and dsd.slip_no = ds.slip_no");

                    if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected()
                            || this.isCondTechClassSelected() || this.isCondTechSelected()
                            || this.isCondCourseClassSelected() || this.isCondCourseSelected())) {
                        dsQuery.append("         and (");
                    }
                    if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
                        if (techItemOR.isSelected()) {
                            if (this.isCondTechClassSelected()) {
                                dsQuery.append(getCondTechClassCondition("dsd"));
                            }
                            // 技術
                            if (this.isCondTechSelected()) {
                                if (this.isCondTechClassSelected()) {
                                    dsQuery.append("         or ");
                                }
                                dsQuery.append(getCondTechCondition("dsd"));
                            }
                        } else {
                            // 技術分類
                            if (this.isCondTechClassSelected()) {
                                dsQuery.append(getCondTechClassCondition("dsd"));
                            }
                            // 技術
                            if (this.isCondTechSelected()) {
                                dsQuery.append(getCondTechCondition("dsd"));
                            }
                        }
                    }

                    if (this.isCondItemClassSelected() || this.isCondItemSelected()) {

                        if (techItemOR.isSelected()) {
                            if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected())) {
                                dsQuery.append(" or ");
                            } else {
                                dsQuery.append("        (");
                            }
                            // 商品分類
                            if (this.isCondItemClassSelected()) {
                                dsQuery.append(getCondItemClassCondition("dsd"));
                            }
                            // 商品
                            if (this.isCondItemSelected()) {
                                if (this.isCondItemClassSelected()) {
                                    dsQuery.append("         or ");
                                }
                                dsQuery.append(getCondItemCondition("dsd"));
                            }

                            //dsQuery.append("         )");
                        } else {
                            // 商品分類
                            if (this.isCondItemClassSelected()) {
                                dsQuery.append(getCondItemClassCondition("dsd"));
                            }
                            // 商品
                            if (this.isCondItemSelected()) {
                                dsQuery.append(getCondItemCondition("dsd"));
                            }
                        }
                        //dsQuery.append("         )");
                    }
                    
                    //コース
                    if (this.isCondCourseClassSelected() || this.isCondCourseSelected()) {
                        
                        if (techItemOR.isSelected()) {
                            if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected()
                                    ||this.isCondItemClassSelected() || this.isCondItemSelected())) {
                                dsQuery.append(" or (");
                            }else {
                                dsQuery.append("        (");
                            }
                            // コース分類
                            if (this.isCondCourseClassSelected()) {
                                    dsQuery.append(getCondCourseClassCondition("dsd"));
                            }
                            // コース
                            if (this.isCondCourseSelected()) {
                                if(this.isCondCourseClassSelected()) {
                                    dsQuery.append("         or ");
                                }
                                dsQuery.append(getCondCourseCondition("dsd"));
                            }
                            dsQuery.append("         )");
                            
                        }else{
                            // コース分類
                            if (this.isCondCourseClassSelected()) {
                                dsQuery.append(getCondCourseClassCondition("dsd"));
                            }
                            // コース
                            if (this.isCondCourseSelected()) {
                                dsQuery.append(getCondCourseCondition("d"));
                            }
                        }
		    }

                    if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected()
                            || this.isCondTechClassSelected() || this.isCondTechSelected()
                            || this.isCondCourseClassSelected() || this.isCondCourseSelected())) {
                        dsQuery.append("         ) ");
                    }
                    dsQuery.append("         )");
                }
                
                // 反響項目
                if (0 < responseItem.getSelectedIndex()) {
                    dsQuery.append("             and exists");
                    dsQuery.append("             (");
                    dsQuery.append("                 select 1");
                    dsQuery.append("                 from");
                    dsQuery.append("                     data_response_effect");
                    dsQuery.append("                 where");
                    dsQuery.append("                         delete_date is null");
                    dsQuery.append("                     and shop_id = ds.shop_id");
                    dsQuery.append("                     and slip_no = ds.slip_no");
                    dsQuery.append("                     and response_id = " + SQLUtil.convertForSQL(((MstResponse) responseItem.getSelectedItem()).getResponseID()));
                    dsQuery.append("            )");
                }

                dsQuery.append(")");
            }

        } else {

            dsQuery.append("(select * from data_sales ds where ds.delete_date is null)");
        }
        //Luc start add 20151225 
        StringBuilder visitNumSql = new StringBuilder(1000);
        visitNumSql.append(" (");
        visitNumSql.append("     select");
        visitNumSql.append("         count(a.slip_no)");
        //日付条件が来店日付以外、または来店日付かつ累計来店回数が
        //選択されている場合のみ累計来店回数を取得する。
        if (!(dateType0.isSelected() || dateType3.isSelected()) || visitCountType0.isSelected()) {
            visitNumSql.append("     + coalesce(max(b.before_visit_num),0)");
        }
        visitNumSql.append("     from");
        visitNumSql.append("         view_data_sales_valid a");
        visitNumSql.append("             inner join mst_customer b");
        visitNumSql.append("                 using(customer_id)");
        visitNumSql.append("     where");
        visitNumSql.append("             b.customer_id = t.customer_id");
        visitNumSql.append("         and a.sales_date is not null");
        visitNumSql.append("         and a.shop_id in (" + shopIDList + ")");
        // 来店日付かつ期間指定がある場合のみ
        if (dateType0.isSelected() || dateType3.isSelected()) {
            if(visitDateFrom.getDateStr() != null){
                //累計来店回数以外が選択されている場合のみ開始日を指定する
                if (!visitCountType0.isSelected()) {
                    visitNumSql.append(" and a.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
            }
            if(visitDateTo.getDateStr() != null) {
                visitNumSql.append(" and a.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
        }
        visitNumSql.append(" )");
        
        /*******************************/
        /* 売上金額 取得ＳＱＬ */
        /*******************************/
        StringBuilder sumTotalSql = new StringBuilder(1000);
        sumTotalSql.append(" (");
        sumTotalSql.append("     select");
        //IVS_LVTu start edit 2015/07/16 Bug #40581
        //sumTotalSql.append("         coalesce(sum(discount_sales_value_in_tax), 0)");
        sumTotalSql.append("         coalesce(sum(discount_detail_value_in_tax), 0)");
        sumTotalSql.append("     from");
        //sumTotalSql.append("         view_data_sales_valid");
        sumTotalSql.append("         view_data_sales_detail_valid");
        sumTotalSql.append("     where");
        sumTotalSql.append("             shop_id in (" + shopIDList + ")");
        sumTotalSql.append("         and customer_id = t.customer_id");
        sumTotalSql.append("         and sales_date is not null");
        sumTotalSql.append("         and product_division <> 6 ");
        //IVS_LVTu end edit 2015/07/16 Bug #40581
        // 来店日付かつ期間指定がある場合のみ
        if (dateType0.isSelected() || dateType3.isSelected()) {
            if(visitDateFrom.getDateStr() != null){
                sumTotalSql.append(" and sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if(visitDateTo.getDateStr() != null) {
                sumTotalSql.append(" and sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
        }
        sumTotalSql.append(" )");
        /*******************************/
        /* 初回来店日 取得ＳＱＬ */
        /*******************************/
        StringBuilder firstVisitDateSql = new StringBuilder(1000);
        firstVisitDateSql.append(" (");
        firstVisitDateSql.append("     select");
        firstVisitDateSql.append("         coalesce(first_visit_date,");
        firstVisitDateSql.append("             (");
        firstVisitDateSql.append("                 select");
        firstVisitDateSql.append("                     min(sales_date)");
        firstVisitDateSql.append("                 from");
        firstVisitDateSql.append("                     data_sales");
        firstVisitDateSql.append("                 where");
        firstVisitDateSql.append("                         delete_date is null");
        firstVisitDateSql.append("                     and sales_date is not null");
        firstVisitDateSql.append("                     and customer_id = t.customer_id");
        firstVisitDateSql.append("                     and shop_id in (" + shopIDList + ")");
        firstVisitDateSql.append("             ))");
        firstVisitDateSql.append("     from");
        firstVisitDateSql.append("         mst_customer");
        firstVisitDateSql.append("     where");
        firstVisitDateSql.append("         customer_id = t.customer_id");
        firstVisitDateSql.append(" )");
        
         boolean isSalesCondition = false;
        // 来店日付
        isSalesCondition = isSalesCondition || (visitDateFrom.getDateStr() != null);
        isSalesCondition = isSalesCondition || (visitDateTo.getDateStr() != null);
        //主担当者
        isSalesCondition = isSalesCondition || (!chargeStaffNo.getText().equals(""));
        // 主担当指名区分
        isSalesCondition = isSalesCondition || (chargeStaffNamed.isSelected());
        isSalesCondition = isSalesCondition || (chargeStaffFree.isSelected());
        // 技術分類・技術
        isSalesCondition = isSalesCondition || (this.isCondTechClassSelected());
        isSalesCondition = isSalesCondition || (this.isCondTechSelected());
        // 商品分類・商品
        isSalesCondition = isSalesCondition || (this.isCondItemClassSelected());
        isSalesCondition = isSalesCondition || (this.isCondItemSelected());
        // コース　
        isSalesCondition = isSalesCondition || (this.isCondCourseClassSelected());
        isSalesCondition = isSalesCondition || (this.isCondCourseSelected());
        // 反響項目
        isSalesCondition = isSalesCondition || (0 < responseItem.getSelectedIndex());
        // 来店回数
        isSalesCondition = isSalesCondition || (!visitCountFrom.getText().equals(""));
        isSalesCondition = isSalesCondition || (!visitCountTo.getText().equals(""));
        // 売上金額
        isSalesCondition = isSalesCondition || (!priceFrom.getText().equals(""));
        isSalesCondition = isSalesCondition || (!priceTo.getText().equals(""));
            
	//Luc end add 20151225        
        /**
         * *****************************************
         */
        /*  メインＳＱＬ作成 */
        /**
         * *****************************************
         */
        StringBuilder sql = new StringBuilder(1000);
        // IVS - Sang begin edit 20130909
        //sql.append("select ");
        sql.append("select distinct \n");
        // IVS - Sang end edit 20130909



        sql.append("  sales.*");

        //IVS NNTUAN START EDIT 20131112
        /*
         sql.append(" ,payment.money_value");
         sql.append(" ,payment.card_name");
         sql.append(" ,payment.card_value");
         sql.append(" ,payment.ecash_name");
         sql.append(" ,payment.ecash_value");
         sql.append(" ,payment.gift_name");
         sql.append(" ,payment.gift_value");
         sql.append(" ,payment.bill_value");
         */
        sql.append(" ,sum(payment.money_value) as money_value");
        sql.append(" ,array_to_string(array( select (case when b.payment_class_id = 2 then b.payment_method_name end) \n");
        sql.append("			from data_payment_detail dpd \n");
        sql.append("			inner join mst_payment_method b using(payment_method_id)\n");
        sql.append("			where dpd.payment_no = 0 \n");
        sql.append("			      and dpd.shop_id = sales.shop_id\n");
        sql.append("			      and dpd.slip_no = sales.slip_no\n");
        sql.append("			      and b.payment_class_id = 2),','  )    as card_name \n");
        sql.append(" ,sum(payment.card_value) as card_value \n");
        sql.append(" ,array_to_string(array( select  (case when b.payment_class_id = 3 then b.payment_method_name end) \n");
        sql.append("			from data_payment_detail dpd \n");
        sql.append("			inner join mst_payment_method b using(payment_method_id)\n");
        sql.append("			where dpd.payment_no = 0 \n");
        sql.append("			      and dpd.shop_id = sales.shop_id\n");
        sql.append("			      and dpd.slip_no = sales.slip_no\n");
        sql.append("			      and b.payment_class_id = 3),','  )    as ecash_name \n");
        sql.append(" ,sum(payment.ecash_value) as ecash_value \n");
        sql.append(" ,array_to_string(array( select  (case when b.payment_class_id = 4 then b.payment_method_name end)\n");
        sql.append("			from data_payment_detail dpd \n");
        sql.append("			inner join mst_payment_method b using(payment_method_id)\n");
        sql.append("			where dpd.payment_no = 0 \n");
        sql.append("			      and dpd.shop_id = sales.shop_id\n");
        sql.append("			      and dpd.slip_no = sales.slip_no\n");
        sql.append("			      and b.payment_class_id = 4),','  )    as gift_name \n");
        sql.append(" ,sum(payment.gift_value) as gift_value \n");
        // IVS SANG START EDIT 20131128 [gbソース]顧客情報画面⇒来店情報タブ、条件検索EXCEL出力
        // sql.append(" ,sum(payment.bill_value) as bill_value");
        sql.append(" ,payment.bill_value as bill_value");
        // IVS SANG END EDIT 20131128 [gbソース]顧客情報画面⇒来店情報タブ、条件検索EXCEL出力
        //IVS NNTUAN END EDIT 20131112

        sql.append(" ,case \n");
        sql.append("   when product_division = 0 then product_value \n");
        //nhanvt start edit 20141127 Bug #33192
        //sql.append("   when product_division = 1 and is_proportionally = true then product_value \n");
        sql.append("   when product_division = 1 AND is_proportionally = TRUE THEN cast((product_value * product_num*0.01) as double precision) \n");
        //nhanvt end edit 20141127 Bug #33192
        sql.append("   when product_division = 8 then product_value \n");
         sql.append("   when product_division = 9 then product_value \n");
         //nhanvt start edit 20141106 
        //sql.append("   else discount_detail_value_in_tax \n");
        // 201805 edit start #35807
        //sql.append("   else product_value*product_num - discount_value \n");
         if(accountSetting.getDisplayPriceType()==1 && accountSetting.getDiscountType()==1) {
             // 外税＆税抜き額から割引
             sql.append(" when product_division in (1,2,3,4,5) then ");
             sql.append("    ( case when  discount_value != 0 then ( ceil(product_value / (1 + get_tax_rate(sales_date))) * product_num) - discount_value + trunc( ( ( ceil(product_value / (1 + get_tax_rate(sales_date))) * product_num) - discount_value) * get_tax_rate(sales_date)) else detail_value_in_tax end ) ");
         }else {
             sql.append("   else product_value*product_num - discount_value \n");
         }
        // 201805 edit end #35807
        //nhanvt end edit 20141106
        sql.append(" end as total_price, \n");
        sql.append(" (select \n");
        sql.append("    min(reservation_datetime) as reserve_date \n");
        sql.append(" from \n");
        sql.append("    data_reservation as dr \n");
        sql.append("        inner join data_reservation_detail drd \n");
        sql.append("            using(shop_id, reservation_no) \n");
        sql.append(" where \n");
        sql.append("        dr.delete_date IS NULL \n");
        sql.append("    and drd.delete_date IS NULL \n");
        sql.append("    and reservation_datetime > now() \n");
        sql.append("    and status < 3 \n");
        sql.append("    and shop_id = sales.shop_id \n");
        sql.append("    and customer_id = sales.customer_id) as next_reserve_date \n");
        sql.append("from \n");
        // ThuanNK start edit 20140318
        sql.append("(	select  distinct \n");
        // ThuanNK end edit 20140318
        //IVS NNTUAN START EDIT 20131008
        //sql.append("              ms.shop_id, mc.customer_id, shop_name, ds.slip_no, dsd.slip_detail_no, mc.customer_no, customer_name1 || ' ' || customer_name2 as customer_name, \n");
        //get extra information update date from table data_sales
        // ThuanNK start edit 20140307
        //nhanvt start edit 20141024 Bug #31726
        sql.append(" dsd.slip_detail_no,mt.staff_id,mt.data_proportionally_id, ");
        //nhanvt end edit 20141024 Bug #31726
        //nhanvt start add 20141127 Bug #33192
        sql.append(" mt.seq_num, ");
        //nhanvt end edit 20141127 Bug #33192
        sql.append("              ds.update_date, ms.shop_id, mc.customer_id, shop_name, ds.slip_no, mc.customer_no, customer_name1 || ' ' || customer_name2 as customer_name, \n");
        // ThuanNK end edit 20140307
        //IVS NNTUAN END EDIT 20131008
        sql.append("              ds.sales_date, coalesce(staff1.staff_name1,'') || ' ' || coalesce(staff1.staff_name2,'') as charge_staff, ds.designated_flag as charge_designated_flag,dsd.product_division, \n");
        // 201805 GB add start #35807
        sql.append("              dsd.detail_value_in_tax, ");
        // 201805 GB add end #35807
        //IVS_LVTu edit 2019/09/20  SPOS増税対応
        sql.append("              dsd.tax_rate, ");
        sql.append("              case \n");
        sql.append("               when dsd.product_division in (0) then '<全体割引>'\n");
        sql.append("               when dsd.product_division in (1,3) then mt.technic_class_name\n");
        sql.append("               when dsd.product_division in (2,4) then mic.item_class_name\n");
        sql.append("               when dsd.product_division in (5,6) then cc.course_class_name\n");
        //Luc start edit 20151215 #45313
        //sql.append("               when dsd.product_division in (8) then cc.course_class_name\n");
        sql.append("               when dsd.product_division in (7,8) then cc.course_class_name\n");
        //Luc end edit 20151215 #45313
        sql.append("               when dsd.product_division in (9) then '手数料'\n");
        sql.append("              end as category_name, \n");
        sql.append("              case \n");
        sql.append("               when dsd.product_division in (0) then (select discount_name from mst_discount where discount_id = dsd.product_id) \n");
        sql.append("               when dsd.product_division in (1,3) then mt.technic_name \n");
        sql.append("               when dsd.product_division in (2,4) then mi.item_name \n");
        sql.append("               when dsd.product_division in (5,6) then c.course_name \n");
        //Luc start edit 20151215 #45313
        //sql.append("               when dsd.product_division in (8) then c.course_name \n");
        sql.append("               when dsd.product_division in (7,8) then c.course_name \n");
        //Luc end edit 20151215 #45313
        sql.append("               when dsd.product_division in (9) then '変更手数料' \n");
        sql.append("              end as product_name, \n");
        sql.append("              case \n");
        sql.append("               when dsd.product_division in (0) then (select discount_value from view_data_sales_valid where shop_id = dsd.shop_id and slip_no = dsd.slip_no) * -1 \n");
        sql.append("               else dsd.product_value \n");
        sql.append("              end as product_value, \n");
        //IVS start add 2022/07/12 0624_出力項目の追加
        if (SystemInfo.getDatabase().equals(this.DB_POS_HAIR_BIP)) {
            sql.append("          dsd.discount_value as discount_value_no_tax, ");
        }
        //IVS end add 2022/07/12 0624_出力項目の追加
        sql.append("              coalesce(case \n");
        sql.append("               when dsd.product_division in (0) then 1 \n");
        sql.append("               when dsd.product_division in (1,3) then \n");
        sql.append("                      case is_proportionally when true then \n");
        sql.append("                              proportionally_ratio \n");
        sql.append("                      else \n");
        
        // ThuanNK start edit 20140311
        sql.append("                    (select case when dsd.detail_staff_id is null then\n");
        sql.append("                        (select sum(product_num) from data_sales_detail where slip_no = dsd.slip_no \n");
        sql.append("                        and shop_id = dsd.shop_id and product_id = dsd.product_id \n");
        sql.append("                        and product_division = dsd.product_division \n");
        sql.append("                        and product_value = dsd.product_value \n");
        sql.append("                        AND designated_flag = dsd.detail_designated_flag \n");
        //nhanvt start edit 20141106 Bug #32145
        sql.append("                        and slip_detail_no = dsd.slip_detail_no \n");
        //nhanvt end edit 20141106 Bug #32145
        // vtbphuong start add 20140422 Bug #22512
        sql.append("                        AND delete_date is null  \n");
        // vtbphuong start add 20140422 Bug #22512
        //sql.append("                        --AND staff_id = dsd.detail_staff_id \n");
        //sql.append("                        group by slip_no,shop_id,product_id,product_division, product_value,designated_flag) --,staff_id) \n");
        sql.append("                        group by slip_no,shop_id,product_id,product_division, product_value,designated_flag)  \n");
        sql.append("                    when dsd.detail_staff_id is not null then\n");
        sql.append("                        (select sum(product_num) from data_sales_detail where slip_no = dsd.slip_no \n");
        sql.append("                        and shop_id = dsd.shop_id and product_id = dsd.product_id \n");
        sql.append("                        and product_division = dsd.product_division \n");
        sql.append("                        and product_value = dsd.product_value \n");
        sql.append("                        AND designated_flag = dsd.detail_designated_flag \n");
        sql.append("                        AND staff_id = dsd.detail_staff_id \n");
         //nhanvt start edit 20141106 Bug #32145
        sql.append("                        and slip_detail_no = dsd.slip_detail_no \n");
        //nhanvt end edit 20141106 Bug #32145
        // vtbphuong start add 20140422 Bug #22512
        sql.append("                        AND delete_date is null  \n");
        // vtbphuong start add 20140422 Bug #22512
        sql.append("                        group by slip_no,shop_id,product_id,product_division, product_value,designated_flag,staff_id) \n");
        sql.append("                    end)\n");
        sql.append("                end \n");
        sql.append("                when dsd.product_division in (2,4,5) then \n");
        sql.append("                    (select case when dsd.detail_staff_id is null then\n");
        sql.append("                        (select sum(product_num) from data_sales_detail where slip_no = dsd.slip_no \n");
        sql.append("                        and shop_id = dsd.shop_id and product_id = dsd.product_id \n");
        sql.append("                        and product_division = dsd.product_division \n");
        sql.append("                        and product_value = dsd.product_value \n");
        //Luc start add 20140407
        sql.append("                        AND designated_flag = dsd.detail_designated_flag \n");
        //sql.append("                        --AND staff_id = dsd.detail_staff_id \n");
        //Luc end add 20140407
        // vtbphuong start add 20140422 Bug #22512
        sql.append("                        AND delete_date is null  \n");
         //nhanvt start edit 20141106 Bug #32145
        sql.append("                        and slip_detail_no = dsd.slip_detail_no \n");
        //nhanvt end edit 20141106 Bug #32145
        // vtbphuong start add 20140422 Bug #22512
        //sql.append("                        group by slip_no,shop_id,product_id,product_division, product_value,designated_flag)--,staff_id) \n");
        sql.append("                        group by slip_no,shop_id,product_id,product_division, product_value,designated_flag) \n");
        sql.append("                    when dsd.detail_staff_id is not null then\n");
        sql.append("                        (select sum(product_num) from data_sales_detail where slip_no = dsd.slip_no \n");
        sql.append("                        and shop_id = dsd.shop_id and product_id = dsd.product_id \n");
        sql.append("                        and product_division = dsd.product_division \n");
        sql.append("                        and product_value = dsd.product_value \n");
        //Luc start add 20140407
        sql.append("                        AND designated_flag = dsd.detail_designated_flag \n");
        sql.append("                        AND staff_id = dsd.detail_staff_id \n");
        //Luc end add 20140407
        // vtbphuong start add 20140422 Bug #22512
        sql.append("                        AND delete_date is null  \n");
         //nhanvt start edit 20141106 Bug #32145
        sql.append("                        and slip_detail_no = dsd.slip_detail_no \n");
        //nhanvt end edit 20141106 Bug #32145
        // vtbphuong start add 20140422 Bug #22512
        sql.append("                        group by slip_no,shop_id,product_id,product_division, product_value,designated_flag,staff_id) \n");
        sql.append("                        end )\n");
        // ThuanNK start edit 20140304
        //sql.append("               when dsd.product_division in (6) then dsd.product_num \n");
        //コース解約
        sql.append("                when dsd.product_division in (8) then 1 \n");
        sql.append("                when dsd.product_division in (9) then 1 \n");

        //IVS_LVTu start edit 2016/06/03 New request #50990
        sql.append("            when dsd.product_division in (6) then (select CAST(sum(dcd.product_num) AS float) as product_num\n");
        //IVS_LVTu end edit 2016/06/03 New request #50990

        //nhanvt start add 20141120 Bug #32638
        sql.append(" FROM data_contract_digestion dcd ");
        sql.append("                      INNER JOIN data_contract dc ON dc.shop_id = dcd.contract_shop_id ");
        sql.append("                      AND dc.contract_no = dcd.contract_no ");
        sql.append("                      AND dc.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                      inner join data_sales_detail dsd1 on dsd1.shop_id = dcd.shop_id  ");
        sql.append("                                  and dsd1.slip_no = dcd.slip_no ");
        sql.append("                                and dsd1.contract_shop_id = dcd.contract_shop_id ");
        sql.append("                                and dsd1.contract_no = dcd.contract_no  ");
        sql.append("                                and dsd1.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                     WHERE dsd1.shop_id = dsd.shop_id ");
        sql.append("                       AND dsd1.slip_no = dsd.slip_no ");
        sql.append("                       and dsd1.slip_detail_no = dsd.slip_detail_no ");
        sql.append("                       AND dc.product_id = dsd.product_id ");
        sql.append("                       AND dcd.delete_date IS NULL ");
        //nhanvt start edit Bug #33715
        //sql.append("                       AND dc.delete_date IS NULL ");
        //nhanvt end edit Bug #33715
        sql.append("                       and dsd1.product_division = 6 ");
        sql.append("                     ) END, 0) AS product_num, ");
        //nhanvt end add 20141120 Bug #32638
        sql.append("              case \n");
        sql.append("             when dsd.product_division in (0) then 0 \n");
        sql.append("             when dsd.product_division in (1,3) then \n");
        sql.append("                      case is_proportionally when true then \n");
        sql.append("                              proportionally_point \n");
        sql.append("                      else \n");
        // 201805 edit start #35807
        //sql.append("                              (	select coalesce(sum(detail_value_in_tax - discount_detail_value_in_tax),0)  \n");
        sql.append("  (select ");
        if(accountSetting.getDisplayPriceType()==1 && accountSetting.getDiscountType()==1) {
            // 外税＆税抜き額から割引
            sql.append(" discount_value ");
        }else {
            sql.append(" coalesce(sum(detail_value_in_tax - discount_detail_value_in_tax),0)  \n");
        }
        // 201805 edit end #35807
        sql.append("                                    from view_data_sales_detail_valid  \n");
        sql.append("                                    where slip_no = dsd.slip_no and shop_id = dsd.shop_id \n");
        sql.append("                                    and product_id = dsd.product_id and product_value = dsd.product_value \n");
        sql.append("                                    and product_division = dsd.product_division \n");
        // vtbphuong start edit 20140422 Bug #22512
        //sql.append("                                    AND designated_flag = dsd.detail_designated_flag  \n");
        sql.append("                                    AND detail_designated_flag = dsd.detail_designated_flag  \n");
        //sql.append("                                    AND staff_id = dsd.detail_staff_id  \n");
        sql.append("                                    AND detail_staff_id = dsd.detail_staff_id  \n");
        //nhanvt start edit 20141106 Bug #32145
        sql.append("                                    and slip_detail_no = dsd.slip_detail_no  \n");
        //nhanvt end edit 20141106 Bug #32145
        // vtbphuong end edit 20140422 Bug #22512

        sql.append("                              )  \n");
        sql.append("                      end \n");
        sql.append("            when dsd.product_division in (2,4,5) then \n");
        // 201805 edit start #35807
        //sql.append("  (select coalesce(sum(detail_value_in_tax - discount_detail_value_in_tax),0)  \n");
        sql.append("  (select ");
        if(accountSetting.getDisplayPriceType()==1 && accountSetting.getDiscountType()==1) {
            // 外税＆税抜き額から割引
            sql.append(" discount_value ");
        }else {
            sql.append(" coalesce(sum(detail_value_in_tax - discount_detail_value_in_tax),0)  \n");
        }
        // 201805 edit end #35807
        sql.append("                from view_data_sales_detail_valid \n");
        sql.append("                where slip_no = dsd.slip_no and shop_id = dsd.shop_id \n");
        sql.append("                and product_id = dsd.product_id and product_value = dsd.product_value \n");
        sql.append("                and product_division = dsd.product_division and slip_detail_no = dsd.slip_detail_no) ");
        sql.append("            when dsd.product_division in (6) then \n");
        sql.append("                (dsd.detail_value_in_tax - dsd.discount_detail_value_in_tax) \n");
        //IVS_LVTu start edit 2016/06/03 New request #50990
        sql.append("                * (select CAST(sum(dcd.product_num) AS float) as product_num\n");
        //IVS_LVTu end edit 2016/06/03 New request #50990
        //nhanvt start edit 20141125 Bug #32982
        /*sql.append("                 from data_contract_digestion dcd\n");
        sql.append("                 inner join data_contract dc on dc.shop_id = dcd.contract_shop_id \n");
        sql.append("                and dc.contract_no = dcd.contract_no \n");
        sql.append("                and dc.contract_detail_no = dcd.contract_detail_no\n");
        sql.append("                where  dcd.shop_id = dsd.shop_id  and dcd.slip_no = dsd.slip_no  and dc.product_id = dsd.product_id \n");
        sql.append("                group by dcd.slip_no,dc.product_id,dc.shop_id) ");*/
        sql.append(" FROM data_contract_digestion dcd ");
        sql.append("                      INNER JOIN data_contract dc ON dc.shop_id = dcd.contract_shop_id ");
        sql.append("                      AND dc.contract_no = dcd.contract_no ");
        sql.append("                      AND dc.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                      inner join data_sales_detail dsd1 on dsd1.shop_id = dcd.shop_id  ");
        sql.append("                                  and dsd1.slip_no = dcd.slip_no ");
        sql.append("                                and dsd1.contract_shop_id = dcd.contract_shop_id ");
        sql.append("                                and dsd1.contract_no = dcd.contract_no  ");
        sql.append("                                and dsd1.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                     WHERE dsd1.shop_id = dsd.shop_id ");
        sql.append("                       AND dsd1.slip_no = dsd.slip_no ");
        sql.append("                       and dsd1.slip_detail_no = dsd.slip_detail_no ");
        sql.append("                       AND dc.product_id = dsd.product_id ");
        sql.append("                       AND dcd.delete_date IS NULL ");
        //nhanvt edit start 20141209 Bug #33788
        //sql.append("                       AND dc.delete_date IS NULL ");
        //nhanvt edit start 20141209 Bug #33788
        sql.append("                       and dsd1.product_division = 6 ) ");
        //nhanvt start edit 20141125 Bug #32982
        sql.append("              end as discount_value, \n");
        // 201805 GB add start #35807
        sql.append("              case \n");
        sql.append("             when dsd.product_division in (0) then 0 \n");
        sql.append("             when dsd.product_division in (1,3) then \n");
        sql.append("                      case is_proportionally when true then \n");
        sql.append("                              proportionally_point \n");
        sql.append("                      else \n");
        sql.append("  (select ");
        //IVS start edit 20220626 1円の差異が発生
        //sql.append(" trunc(discount_value * (1 + get_tax_rate(sales_date)) ) ");
        sql.append(" trunc( detail_value_in_tax - \n");
        sql.append(getTechItemCourseValueQuery());
        sql.append(" ) \n");
        //IVS end edit 20220626 1円の差異が発生
        sql.append("                                    from view_data_sales_detail_valid  \n");
        sql.append("                                    where slip_no = dsd.slip_no and shop_id = dsd.shop_id \n");
        sql.append("                                    and product_id = dsd.product_id and product_value = dsd.product_value \n");
        sql.append("                                    and product_division = dsd.product_division \n");
        sql.append("                                    AND detail_designated_flag = dsd.detail_designated_flag  \n");
        sql.append("                                    AND detail_staff_id = dsd.detail_staff_id  \n");
        sql.append("                                    and slip_detail_no = dsd.slip_detail_no  \n");
        sql.append("                              )  \n");
        sql.append("                      end \n");
        sql.append("            when dsd.product_division in (2,4,5) then \n");
        sql.append("  (select ");
        //IVS start edit 20220626 1円の差異が発生
        //sql.append(" trunc(discount_value * (1 + get_tax_rate(sales_date)) ) ");
        sql.append(" trunc( detail_value_in_tax - \n");
        sql.append(getTechItemCourseValueQuery());
        sql.append(" ) \n");
        //IVS end edit 20220626 1円の差異が発生
        sql.append("                from view_data_sales_detail_valid \n");
        sql.append("                where slip_no = dsd.slip_no and shop_id = dsd.shop_id \n");
        sql.append("                and product_id = dsd.product_id and product_value = dsd.product_value \n");
        sql.append("                and product_division = dsd.product_division and slip_detail_no = dsd.slip_detail_no) ");
        sql.append("            when dsd.product_division in (6) then \n");
        sql.append("                (dsd.detail_value_in_tax - dsd.discount_detail_value_in_tax) \n");
        sql.append("                * (select CAST(sum(dcd.product_num) AS float) as product_num\n");
        sql.append(" FROM data_contract_digestion dcd ");
        sql.append("                      INNER JOIN data_contract dc ON dc.shop_id = dcd.contract_shop_id ");
        sql.append("                      AND dc.contract_no = dcd.contract_no ");
        sql.append("                      AND dc.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                      inner join data_sales_detail dsd1 on dsd1.shop_id = dcd.shop_id  ");
        sql.append("                                  and dsd1.slip_no = dcd.slip_no ");
        sql.append("                                and dsd1.contract_shop_id = dcd.contract_shop_id ");
        sql.append("                                and dsd1.contract_no = dcd.contract_no  ");
        sql.append("                                and dsd1.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                     WHERE dsd1.shop_id = dsd.shop_id ");
        sql.append("                       AND dsd1.slip_no = dsd.slip_no ");
        sql.append("                       and dsd1.slip_detail_no = dsd.slip_detail_no ");
        sql.append("                       AND dc.product_id = dsd.product_id ");
        sql.append("                       AND dcd.delete_date IS NULL ");
        sql.append("                       and dsd1.product_division = 6 ) ");
        sql.append("              end as discount_value_in_tax , \n");
        // 201805 GB add end #35807
        sql.append("              case when dsd.product_division = 0 \n");
        sql.append("                  then dsd.detail_value_in_tax * -1 \n");
        sql.append("                  else \n");
        sql.append("            case when dsd.product_division in (1,2,3,4,5) \n");
        sql.append("            then ");
        sql.append("            (select case when dsd.detail_staff_id is null then\n");
        sql.append("                (dsd.product_value * (select sum(product_num) from data_sales_detail \n");
        sql.append("                where slip_no = dsd.slip_no and shop_id = dsd.shop_id and product_id = dsd.product_id \n");
        sql.append("                and product_division = dsd.product_division \n");
        sql.append("                and product_value = dsd.product_value \n");
        sql.append("                AND designated_flag = dsd.detail_designated_flag \n");
        //sql.append("                --AND staff_id = dsd.detail_staff_id \n");
        //sql.append("                group by slip_no,shop_id,product_id,product_division, product_value,designated_flag)) --,staff_id)) \n");
        sql.append("                group by slip_no,shop_id,product_id,product_division, product_value,designated_flag))  \n");
        sql.append("                - (select coalesce(sum(detail_value_in_tax - discount_detail_value_in_tax),0)  \n");
        sql.append("                from view_data_sales_detail_valid \n");
        sql.append("                where slip_no = dsd.slip_no and shop_id = dsd.shop_id \n");
        sql.append("                and product_id = dsd.product_id and product_value = dsd.product_value \n");
        sql.append("                and product_division = dsd.product_division");
        // vtbphuong start edit 20140422 Bug #22512
        //sql.append("                AND designated_flag = dsd.detail_designated_flag)  \n");
        sql.append("                AND detail_designated_flag = dsd.detail_designated_flag)  \n");
        // vtbphuong end edit 20140422 Bug #22512
        sql.append("            when dsd.detail_staff_id is not null then\n");
        sql.append("                (dsd.product_value * (select sum(product_num) from data_sales_detail \n");
        sql.append("                where slip_no = dsd.slip_no and shop_id = dsd.shop_id and product_id = dsd.product_id \n");
        sql.append("                and product_division = dsd.product_division \n");
        sql.append("                and product_value = dsd.product_value \n");
        sql.append("                AND designated_flag = dsd.detail_designated_flag \n");
        sql.append("                AND staff_id = dsd.detail_staff_id \n");
        sql.append("                group by slip_no,shop_id,product_id,product_division, product_value,designated_flag,staff_id)) \n");
        sql.append("                - (select coalesce(sum(detail_value_in_tax - discount_detail_value_in_tax),0)  \n");
        sql.append("                from view_data_sales_detail_valid \n");
        sql.append("                where slip_no = dsd.slip_no and shop_id = dsd.shop_id \n");
        sql.append("                and product_id = dsd.product_id and product_value = dsd.product_value \n");
        sql.append("                and product_division = dsd.product_division ");
        // vtbphuong start edit 20140422 Bug #22512
        //sql.append("                AND designated_flag = dsd.detail_designated_flag  \n");
        sql.append("                AND detail_designated_flag = dsd.detail_designated_flag  \n");
        //sql.append("                AND staff_id = dsd.detail_staff_id)  \n");
        sql.append("                AND detail_staff_id = dsd.detail_staff_id)  \n");
        // vtbphuong end edit 20140422 Bug #22512
        sql.append("            end )\n");
        sql.append(" when dsd.product_division in (6) then \n");
        //IVS_LVTu start edit 2016/06/03 New request #50990
        sql.append(" ((dsd.product_value * (select CAST(sum(dcd.product_num) AS float) as product_num \n");
        //IVS_LVTu end edit 2016/06/03 New request #50990
        //nhanvt start edit 20141125 Bug #32982
        /*sql.append(" from data_contract_digestion dcd \n");
        sql.append(" inner join data_contract dc on dc.shop_id = dcd.contract_shop_id \n");
        sql.append(" and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no \n");
        sql.append(" where  dcd.shop_id = dsd.shop_id  and dcd.slip_no = dsd.slip_no  and dc.product_id = dsd.product_id \n");
        sql.append(" group by dcd.slip_no,dc.product_id,dc.shop_id)) \n");*/
        
        sql.append(" FROM data_contract_digestion dcd ");
        sql.append("                      INNER JOIN data_contract dc ON dc.shop_id = dcd.contract_shop_id ");
        sql.append("                      AND dc.contract_no = dcd.contract_no ");
        sql.append("                      AND dc.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                      inner join data_sales_detail dsd1 on dsd1.shop_id = dcd.shop_id  ");
        sql.append("                                  and dsd1.slip_no = dcd.slip_no ");
        sql.append("                                and dsd1.contract_shop_id = dcd.contract_shop_id ");
        sql.append("                                and dsd1.contract_no = dcd.contract_no  ");
        sql.append("                                and dsd1.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                     WHERE dsd1.shop_id = dsd.shop_id ");
        sql.append("                       AND dsd1.slip_no = dsd.slip_no ");
        sql.append("                       and dsd1.slip_detail_no = dsd.slip_detail_no ");
        sql.append("                       AND dc.product_id = dsd.product_id ");
        sql.append("                       AND dcd.delete_date IS NULL ");
        //nhanvt edit start 20141209 Bug #33788
        //sql.append("                       AND dc.delete_date IS NULL ");
        //nhanvt edit end 20141209 Bug #33788
        sql.append("                       and dsd1.product_division = 6 )) ");
        //nhanvt end edit 20141125 Bug #32982
        sql.append(" - (dsd.detail_value_in_tax - dsd.discount_detail_value_in_tax) \n");
        //IVS_LVTu start edit 2016/06/03 New request #50990
        sql.append(" * (select CAST(sum(dcd.product_num) AS float) as product_num\n");
        //IVS_LVTu end edit 2016/06/03 New request #50990
        //nhanvt start edit 20141125 Bug #32982
        /*sql.append("		from data_contract_digestion dcd\n");
        sql.append("		inner join data_contract dc on dc.shop_id = dcd.contract_shop_id \n");
        sql.append("		and dc.contract_no = dcd.contract_no \n");
        sql.append("		and dc.contract_detail_no = dcd.contract_detail_no\n");
        sql.append("		where  dcd.shop_id = dsd.shop_id  and dcd.slip_no = dsd.slip_no  and dc.product_id = dsd.product_id \n");
        sql.append("            group by dcd.slip_no,dc.product_id,dc.shop_id) ");*/
        sql.append(" FROM data_contract_digestion dcd ");
        sql.append("                      INNER JOIN data_contract dc ON dc.shop_id = dcd.contract_shop_id ");
        sql.append("                      AND dc.contract_no = dcd.contract_no ");
        sql.append("                      AND dc.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                      inner join data_sales_detail dsd1 on dsd1.shop_id = dcd.shop_id  ");
        sql.append("                                  and dsd1.slip_no = dcd.slip_no ");
        sql.append("                                and dsd1.contract_shop_id = dcd.contract_shop_id ");
        sql.append("                                and dsd1.contract_no = dcd.contract_no  ");
        sql.append("                                and dsd1.contract_detail_no = dcd.contract_detail_no ");
        sql.append("                     WHERE dsd1.shop_id = dsd.shop_id ");
        sql.append("                       AND dsd1.slip_no = dsd.slip_no ");
        sql.append("                       and dsd1.slip_detail_no = dsd.slip_detail_no ");
        sql.append("                       AND dc.product_id = dsd.product_id ");
        sql.append("                       AND dcd.delete_date IS NULL ");
        //nhanvt edit start 20141209 Bug #33788
        //sql.append("                       AND dc.delete_date IS NULL ");
        //nhanvt edit end 20141209 Bug #33788
        sql.append("                       and dsd1.product_division = 6 ) ");
        //nhanvt end edit 20141125 Bug #32982
        sql.append(" ) end \n");
        // ThuanNK end edit 20140311
        sql.append("              end as discount_detail_value_in_tax, \n");
        sql.append("              case when is_proportionally \n");
        sql.append("                  then \n");
        sql.append("                      ( \n");
        sql.append("                          select \n");
        //Luc start edit 20151215 #45313
        //sql.append("                              a.staff_name1 || ' ' || a.staff_name2 \n");
        sql.append("                              coalesce(a.staff_name1,'') || ' ' || coalesce(a.staff_name2,'') \n");
        //Luc end edit 20151215 #45313
        sql.append("                          from \n");
        sql.append("                              mst_staff a \n");
        sql.append("                                  join data_sales_proportionally b \n");
        sql.append("                                  using (staff_id) \n");
        sql.append("                          where \n");
        sql.append("                                  b.shop_id = ds.shop_id \n");
        sql.append("                              and b.slip_no = ds.slip_no \n");
        sql.append("                              and b.slip_detail_no = dsd.slip_detail_no \n");
        sql.append("                              and b.data_proportionally_id = mt.data_proportionally_id \n");
        //nhanvt start add 20141029 Bug #31726
        sql.append("                              and b.staff_id = mt.staff_id \n");
        //nhanvt end add 20141029 Bug #31726
        sql.append("                          limit 1 \n");
        sql.append("                      ) \n");
        sql.append("                  else \n");
        //Luc start edit 20151215 #45313
        //sql.append("                      staff2.staff_name1 || ' ' || staff2.staff_name2 \n");
        sql.append("                      coalesce(staff2.staff_name1,'') || ' ' ||coalesce(staff2.staff_name2,'') \n");
        //Luc start edit 20151215 #45313
        sql.append("              end as technic_staff, \n");
        sql.append("              case when is_proportionally \n");
        sql.append("                  then \n");
        sql.append("                      ( \n");
        sql.append("                          select \n");
        sql.append("                              designated_flag \n");
        sql.append("                          from \n");
        sql.append("                              data_sales_proportionally \n");
        sql.append("                          where \n");
        sql.append("                                  shop_id = ds.shop_id \n");
        sql.append("                              and slip_no = ds.slip_no \n");
        sql.append("                              and slip_detail_no = dsd.slip_detail_no \n");
        sql.append("                              and data_proportionally_id = mt.data_proportionally_id \n");
        sql.append("                          limit 1 \n");
        sql.append("                      ) \n");
        sql.append("                  else \n");
        sql.append("                      dsd.detail_designated_flag \n");
        sql.append("              end as technic_designated_flag, \n");
        sql.append("              case when ds.reservation_datetime is null then '' else to_char(ds.reservation_datetime, 'HH24:MI') end as reservation_datetime, \n");
        //nhanvt start edit  20141008 Bug #30993
        if(isOld){
            sql.append("              case when ds.reservation_datetime is null then '' else to_char(visit_time, 'HH24:MI') end as visit_time, \n");
            sql.append("              case when ds.reservation_datetime is null then '' else to_char(start_time, 'HH24:MI') end as start_time, \n");
            sql.append("              case when ds.reservation_datetime is null then '' else to_char(leave_time, 'HH24:MI') end as leave_time, \n");
            sql.append("              case when ds.reservation_datetime is null then '' else  \n");
            sql.append("                      date_part('hour', date_trunc('second',start_time) - date_trunc('second',visit_time)) || ':' ||  \n");
            sql.append("                      date_part('minute', date_trunc('second',start_time) - date_trunc('second',visit_time)) || ':' ||  \n");
            sql.append("                      date_part('second', date_trunc('second',start_time) - date_trunc('second',visit_time)) end as wait_time, \n");
            sql.append("              case when ds.reservation_datetime is null then '' else  \n");
            sql.append("                      date_part('hour', date_trunc('second',leave_time) - date_trunc('second',visit_time)) || ':' ||  \n");
            sql.append("                      date_part('minute', date_trunc('second',leave_time) - date_trunc('second',visit_time)) || ':' ||  \n");
            sql.append("                      date_part('second', date_trunc('second',leave_time) - date_trunc('second',visit_time)) end as stay_time, \n");
        }else{
          
            sql.append(" (select to_char(visit_time, 'HH24:MI')");
            sql.append(" from  data_reservation dr  ");
            sql.append(" where ds.shop_id = dr.shop_id  ");
            sql.append(" and ds.slip_no = dr.slip_no ");
            sql.append(" and delete_date is null and dr.customer_id = ds.customer_id ");
            sql.append(" ) as visit_time, ");

            sql.append(" (select to_char(start_time, 'HH24:MI') ");
            sql.append(" from  data_reservation dr  ");
            sql.append(" where ds.shop_id = dr.shop_id  ");
            sql.append(" and ds.slip_no = dr.slip_no ");
            sql.append(" and delete_date is null and dr.customer_id = ds.customer_id ");
            sql.append(" ) as start_time, ");


            sql.append(" (select to_char(leave_time, 'HH24:MI') ");
            sql.append(" from  data_reservation dr  ");
            sql.append(" where ds.shop_id = dr.shop_id  ");
            sql.append(" and ds.slip_no = dr.slip_no ");
            sql.append(" and delete_date is null and dr.customer_id = ds.customer_id ");
            sql.append(" ) as leave_time, ");

            sql.append(" (select date_part('hour', date_trunc('second',start_time) - date_trunc('second',visit_time)) || ':' || date_part('minute', date_trunc('second',start_time) - date_trunc('second',visit_time)) || ':' || date_part('second', date_trunc('second',start_time) - date_trunc('second',visit_time))");
            sql.append(" from  data_reservation dr  ");
            sql.append(" where ds.shop_id = dr.shop_id  ");
            sql.append(" and ds.slip_no = dr.slip_no ");
            sql.append(" and delete_date is null and dr.customer_id = ds.customer_id ");
            sql.append(") as wait_time, ");

            sql.append(" (select date_part('hour', date_trunc('second',leave_time) - date_trunc('second',visit_time)) || ':' || date_part('minute', date_trunc('second',leave_time) - date_trunc('second',visit_time)) || ':' || date_part('second', date_trunc('second',leave_time) - date_trunc('second',visit_time)) ");
            sql.append(" from  data_reservation dr  ");
            sql.append(" where ds.shop_id = dr.shop_id ");
            sql.append(" and ds.slip_no = dr.slip_no ");
            sql.append(" and delete_date is null and dr.customer_id = ds.customer_id ");
            sql.append(" ) as stay_time, ");
        }
        
        //nhanvt end edit  20141008 Bug #30993
        sql.append("              tmp_get_progress_day" + nowString + "(ds.customer_id, ds.sales_date) as progress_day, \n");

        sql.append("              array(\n");
        sql.append("                  select");
        sql.append("                      coalesce(mr.response_name, '')\n");
        sql.append("                  from\n");
        sql.append("                      data_response_effect dre\n");
        sql.append("                          join mst_response mr\n");
        sql.append("                          using (response_id)\n");
        sql.append("                  where\n");
        sql.append("                          mr.delete_date is null\n");
        sql.append("                      and dre.delete_date is null\n");
        sql.append("                      and dre.shop_id = ds.shop_id\n");
        sql.append("                      and dre.slip_no = ds.slip_no\n");
        sql.append("              ) as response_name, \n");

        sql.append("              dsd.visited_memo, is_proportionally, \n");
        //IVS NNTUAN START EDIT 20131112 
        //  sql.append("              case product_division \n");
        sql.append("              case dsd.product_division \n");
        //IVS NNTUAN END EDIT 20131112
        sql.append("               when 1 then '技術' \n");
        sql.append("               when 2 then '商品' \n");
        sql.append("               when 3 then '技術クレーム' \n");
        sql.append("               when 4 then '商品返品' \n");
        sql.append("               when 5 then 'コース契約' \n");
        sql.append("               when 6 then 'コース消化' \n");
        sql.append("               when 7 then 'コース変更' \n");
        sql.append("               when 8 then 'コース解約' \n");
        sql.append("               when 9 then 'コース解約' \n");
        sql.append("              end as product_division_name \n");
        //IVS start add 2022/07/12 0624_出力項目の追加
        if (SystemInfo.getDatabase().equals(this.DB_POS_HAIR_BIP)) {
            sql.append(" ,ds.sex ");
            sql.append(" ,ds.age ");
            sql.append(" ,ds.job_name ");
            sql.append(" ,ds.address1 ");
            sql.append(" ,ds.address2 ");
            for (int l = 1; l <= numFreeHeadingClass; l++) {
                sql.append(" ,ds.free_heading_name").append(l);
            }
        }
        //IVS end add 2022/07/12 0624_出力項目の追加
        sql.append("      from  \n");
        sql.append("        (\n");
        sql.append("         select\n");
        //IVS NNTUAN START EDIT 20131008
        //sql.append("              ds.shop_id");
        // get extra information update date from table data sales
        sql.append("              ds.update_date\n");
        sql.append("             ,ds.shop_id\n");
        //IVS NNTUAN END EDIT 20131008
        sql.append("             ,ds.slip_no\n");
        sql.append("             ,ds.sales_date\n");
        sql.append("             ,ds.customer_id\n");
        sql.append("             ,ds.staff_id\n");
        sql.append("             ,ds.designated_flag\n");
        sql.append("             ,(\n");
        sql.append("                 select\n");
        sql.append("                     min(drd.reservation_datetime)\n");
        sql.append("                 from\n");
        sql.append("                     data_reservation dr\n");
        sql.append("                         join data_reservation_detail drd\n");
        sql.append("                             using(shop_id, reservation_no)\n");
        sql.append("                 where\n");
        sql.append("                     drd.delete_date is null\n");
        sql.append("                 and dr.slip_no = ds.slip_no\n");
        sql.append("                 and dr.shop_id = ds.shop_id\n");
        sql.append("                 and dr.customer_id = ds.customer_id\n");
        sql.append("             ) as reservation_datetime\n");
        //Luc start add 20151225 #45536
        sql.append("     ,(SELECT min(sales_date) ");
        sql.append("     FROM data_sales ");
        sql.append("     WHERE delete_date IS NULL ");
        sql.append("     AND sales_date IS NOT NULL ");
        sql.append("     AND customer_id = t.customer_id ");
        sql.append("     AND shop_id IN (" + shopIDList + ")) as date_sales ");
        //IVS_LVTu end add 2015/07/16 Bug #40581
        for (int i = 0; i < arrOrderByFreeHeading.size(); i++) {
            String[] free = arrOrderByFreeHeading.get(i);              
            sql.append("     ,(");
            sql.append("         select");
            sql.append("             free_heading_class_name");
            sql.append("         from");
            sql.append("             mst_customer_free_heading a join mst_free_heading_class b using (free_heading_class_id)");
            sql.append("         where");
            sql.append("                 b.free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
            sql.append("             and a.customer_id = t.customer_id");
            sql.append("      ) as free_heading_class_name" + SQLUtil.convertForSQL(i + 1));
            sql.append("     , Case when (select free_heading_id from mst_customer_free_heading where ");
            sql.append("     customer_id = t.customer_id and free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
            sql.append("         ) = 0 then  (select free_heading_text ");
            sql.append("         from mst_customer_free_heading a");
            sql.append("         where");
            sql.append("           a.customer_id = t.customer_id and free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
            sql.append("         ) else  (");
            sql.append("         select");
            sql.append("             free_heading_name");
            sql.append("         from");
            sql.append("             mst_customer_free_heading a join mst_free_heading b using (free_heading_class_id, free_heading_id)");
            sql.append("         where");
            sql.append("                 b.free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
            sql.append("             and a.customer_id = t.customer_id");
            sql.append("      ) end as free_heading_name" + SQLUtil.convertForSQL(i + 1));
        }

        //IVS_TMTrong start edit 2015/10/19 New request #43511
        //sql.append("     ,date_part('year', age(current_timestamp, t.birthday)) as age");
        sql.append("     ,case when EXTRACT(YEAR FROM t.birthday)::int <= 1900 then 0 else date_part('year', age(current_timestamp, t.birthday)) end as age"); 
        //IVS_TMTrong end edit 2015/10/19 New request #43511
        sql.append("     ,(select job_name from mst_job where job_id = t.job_id and delete_date is null) as job_name");
        //IVS start add 2022/07/12 0624_出力項目の追加
        if (SystemInfo.getDatabase().equals(this.DB_POS_HAIR_BIP)) {
            sql.append("     ,case when t.sex = 1 then '男' ELSE '女' END AS sex");
            sql.append("     ,t.address1, t.address2");
        }
        //IVS end add 2022/07/12 0624_出力項目の追加
        sql.append("     ,coalesce(sf.staff_name1,'') || ' ' || coalesce(sf.staff_name2,'') as staff_name");
        sql.append("     ,(select customer_no from mst_customer where customer_id = t.introducer_id) as introducer_no");
        sql.append("     ,(select customer_name1 || customer_name2 from mst_customer where customer_id = t.introducer_id) as introducer_name");
        sql.append("     ,(select first_coming_motive_name from mst_first_coming_motive where first_coming_motive_class_id = t.first_coming_motive_class_id and delete_date is null) as first_coming_motive_name");
        sql.append("     ," + visitNumSql.toString() + " as visit_num");
        sql.append("     ," + sumTotalSql.toString() + " as sumTotal");

        // 紹介した人の顧客No.の配列を返す
        sql.append("     ,array(");
        sql.append("         select");
        sql.append("             coalesce(customer_no, '')");
        sql.append("         from");
        sql.append("             mst_customer mc");
        sql.append("         where");
        sql.append("             introducer_id = t.customer_id");
        sql.append("      ) as introducer_no_array");

        // 紹介した人の顧客名の配列を返す
        sql.append("     ,array(");
        sql.append("         select");
        sql.append("             coalesce(customer_name1, '') || ' ' || coalesce(customer_name2, '')");
        sql.append("         from");
        sql.append("             mst_customer mc");
        sql.append("         where");
        sql.append("             introducer_id = t.customer_id");
        sql.append("      ) as introducer_name_array");

        sql.append("     ,(");
        sql.append("         select");
        sql.append("             to_char(drd.reservation_datetime, 'yyyy/mm/dd hh24:mi') as next_reserve_date");
        sql.append("         from");
        sql.append("             data_reservation dr");
        sql.append("                 inner join data_reservation_detail drd");
        sql.append("                     using(shop_id, reservation_no)");
        sql.append("         where");
        sql.append("                 dr.delete_date is null");
        sql.append("             and drd.delete_date is null");
        sql.append("             and dr.shop_id in (" + shopIDList + ")");
        sql.append("             and dr.customer_id = t.customer_id");
        sql.append("             and drd.reservation_datetime > current_timestamp");
        sql.append("         order by");
        sql.append("             drd.reservation_datetime");
        sql.append("         limit 1");
        sql.append("      ) as next_reserve_date");
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             case");
        sql.append("                 when dr.mobile_flag is not null then 'WEB予約'");
        sql.append("                 when dr.next_flag is not null and dr.next_flag = 1 then '前回来店'");
        sql.append("                 when dr.preorder_flag is not null and dr.preorder_flag = 1 then '事前予約'");
        sql.append("             end");
        sql.append("         from");
        sql.append("             data_reservation dr");
        sql.append("                 inner join data_reservation_detail drd");
        sql.append("                     using(shop_id, reservation_no)");
        sql.append("         where");
        sql.append("                 dr.delete_date is null");
        sql.append("             and drd.delete_date is null");
        sql.append("             and dr.shop_id in (" + shopIDList + ")");
        sql.append("             and dr.customer_id = t.customer_id");
        sql.append("             and drd.reservation_datetime > current_timestamp");
        sql.append("         order by");
        sql.append("             drd.reservation_datetime");
        sql.append("         limit 1");
        sql.append("      ) as next_reserve_type");
        //Luc end add 20151225 #45536
        sql.append("         from \n");
       sql.append("         " + dsQuery.toString() + " ds");
        //Luc start edit 20151225 #45536
        //sql.append("               join (" + this.sqlBuild(SEND_TYPE_EXCEL, shopIDList, OutputExcelDialog.OUTPUT_ACCOUNT) + ") t");
        //sql.append("               using(customer_id)");
        sql.append("              JOIN mst_customer t on ds.customer_id = t.customer_id\n");
        sql.append("              LEFT JOIN mst_staff sf on ds.staff_id = sf.staff_id\n");
        int i = 0;       
        for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
            //mfhsup.setSelectedInit();            
            if (mfhsup.getFreeHeading().getFreeHeadingID() > 0) { 
                i += 1;
                sql.append("     left outer join mst_customer_free_heading mcfh" + i  +" on (t.customer_id = mcfh" + i + ".customer_id)");
            } 
        }

        for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
            if (!fhPanelText.getFreeHeadingText().isEmpty()) {
                i += 1;
                sql.append("     left outer join mst_customer_free_heading mcfh" + i  +" on (t.customer_id = mcfh" + i + ".customer_id)");
            }
        }
        
        sql.append(" where");
        //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        if (!this.isCustomerNo_0) {
            sql.append("     t.customer_no != '0' and ");
        }
        sql.append("      t.shop_id in (" +(SystemInfo.getSetteing().isShareCustomer() ? "0" : shopIDList )  + ")");
        //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        i = 0;
        for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
            //mfhsup.setSelectedInit();            
            if (mfhsup.getFreeHeading().getFreeHeadingID() > 0) { 
                i += 1;
                sql.append(" and mcfh" + i + ".free_heading_class_id = " + SQLUtil.convertForSQL(mfhsup.getFreeHeadingClassID()));
                sql.append(" and mcfh" + i + ".free_heading_id = " + SQLUtil.convertForSQL(mfhsup.getFreeHeading().getFreeHeadingID()));
            }           

        }

        for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
            if (!fhPanelText.getFreeHeadingText().isEmpty()) {
                i += 1;
                sql.append(" and mcfh" + i + ".free_heading_class_id = " + SQLUtil.convertForSQL(fhPanelText.getFreeHeadingClassID()));
                //sql.append(" and mcfh" + i + ".free_heading_text = " + SQLUtil.convertForSQL(fhPanelText.getFreeHeadingText()));
                sql.append(" and mcfh" + i + ".free_heading_text like '%" + fhPanelText.getFreeHeadingText() + "%'");
            }
        }
        // 紹介した人
        if (isIntroducer.isSelected()) {
            sql.append(" and get_introduce_count(t.customer_id) > 0");
        }

        // 紹介された人
        if (isIntroduced.isSelected()) {
            sql.append(" and t.introducer_id is not null");
        }

        sql.append("     and t.customer_id in");
        sql.append("     (");
        sql.append("         select");
        sql.append("             customer_id");
        sql.append("         from");
        sql.append("             mst_customer");
        sql.append("         where");
        //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        if (!this.isCustomerNo_0) {
            sql.append("                 customer_no != '0' and ");
        }
//	    sql.append("             and delete_date is null");
        sql.append("             shop_id in (" +(SystemInfo.getSetteing().isShareCustomer() ? "0" : shopIDList )  + ")");
        //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい

            // 性別
            if (male.isSelected()){
                sql.append("     and sex = 1");
            }
            if (female.isSelected()){
                sql.append("     and sex = 2");
            }

            // 年齢
            if (!ageFrom.getText().equals("")) {
                sql.append("     and extract(year from age(current_timestamp, birthday)) >= " + ageFrom.getText());
            }
            if (!ageTo.getText().equals("")) {
                sql.append("     and extract(year from age(current_timestamp, birthday)) <= " + ageTo.getText());
            }

            // 誕生月
            if (0 < birthMonth.getSelectedIndex()) {
                sql.append("     and extract(month from birthday) = " + birthMonth.getSelectedItem().toString());
            }

            // メール配信
            if (!sendMailAll.isSelected()){
                sql.append("     and send_mail = "  + (sendMailOK.isSelected() ? 1 : 0));
            }
            // DM配信
            if (!sendDmAll.isSelected()){
                sql.append("     and send_dm = "  + (sendDmOK.isSelected() ? 1 : 0));
            }
            // 電話連絡
            if (!callFlagAll.isSelected()){
                sql.append("     and call_flag = "  + (callFlagOK.isSelected() ? 1 : 0));
            }
            // PCメールアドレス
            if (!pcMailAll.isSelected()){
                if (pcMailExists.isSelected()) {
                    sql.append(" and coalesce(pc_mail_address, '') != ''");
                } else {
                    sql.append(" and coalesce(pc_mail_address, '') = ''");
                }
            }
            // 携帯メールアドレス
            if (!cellularMailAll.isSelected()){
                if (cellularMailExists.isSelected()) {
                    sql.append(" and coalesce(cellular_mail_address, '') != ''");
                } else {
                    sql.append(" and coalesce(cellular_mail_address, '') = ''");
                }
            }
            // SOSIA連動
        if( SystemInfo.isSosiaGearEnable() ) {
            if (!sosiaGearAll.isSelected()){
                if (sosiaGearExists.isSelected()) {
                    MobileMemberList mobileMemberList = new MobileMemberList();
                    mobileMemberList.setSosiaCode(null);
                    mobileMemberList.setAddDateFrom(null);
                    mobileMemberList.setAddDateTo(null);
                    mobileMemberList.setGearCondition(null);
                    mobileMemberList.setSosiaCode(SystemInfo.getSosiaCode());
                    if (sosiaGearExists.isSelected()) {
                        mobileMemberList.setGearCondition(1);
                    }
                    mobileMemberList.load();
                    StringBuilder customerIdList = new StringBuilder(1000);
                    for (MobileMemberData mmd : mobileMemberList) {
                        customerIdList.append(",");
                        customerIdList.append(mmd.getCustomerID());
                    }
                    if (customerIdList.length() > 0) {
                        sql.append(" and t.customer_id in (");
                        sql.append(customerIdList.toString().substring(1));
                        sql.append(" )");
                    }

                    sql.append(" and coalesce(sosia_id, 0) > 0");

                } else {
                    sql.append(" and coalesce(sosia_id, 0) = 0");
                }
            }
        }

        // 誕生日の始まりを条件に含める
        if (birthdayFrom.getDateStr() != null) {
            sql.append("     and birthday >= '" + birthdayFrom.getDateStrWithFirstTime() + "'");
        }
        // 誕生日の終わりを条件に含める
        if (birthdayTo.getDateStr() != null) {
            sql.append("     and birthday <= '" + birthdayTo.getDateStrWithLastTime() + "'");
        }

        // 顧客No.
        if (!customerNo1.getText().equals("")) {
            String s = customerNo1.getText();
            if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                sql.append(" and translate(customer_no, '0123456789', '') = ''");
                sql.append(" and customer_no::text::numeric >= " + s);
            } else {
                sql.append(" and customer_no >= '" + s + "'");
            }
        }
        if (!customerNo2.getText().equals("")) {
            String s = customerNo2.getText();
            if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                sql.append(" and translate(customer_no, '0123456789', '') = ''");
                sql.append(" and customer_no::text::numeric <= " + s);
            } else {
                sql.append(" and customer_no <= '" + s + "'");
            }
        }

        // ふりがな１
        if (!customerKana1.getText().equals("")) {
            sql.append("     and customer_kana1 like '%" + customerKana1.getText() + "%'");
        }

        // ふりがな２
        if (!customerKana2.getText().equals("")) {
            sql.append("     and customer_kana2 like '%" + customerKana2.getText() + "%'");
        }

        // 氏名１
        if (!customerName1.getText().equals("")) {
            sql.append("     and customer_name1 like '%" + customerName1.getText() + "%'");
        }

        // 氏名２
        if (!customerName2.getText().equals("")) {
            sql.append("     and customer_name2 like '%" + customerName2.getText() + "%'");
        }

        // 住所対象
        if (addEnd.isSelected()){
            sql.append("     and");
            sql.append("     (");
            sql.append("          coalesce(postal_code, '') != ''");
            sql.append("       or (coalesce(address1, '') || coalesce(address2, '') || coalesce(address3, '') != '')");
            sql.append("     )");
        }
        if (addYet.isSelected()){
            sql.append("     and");
            sql.append("     (");
            sql.append("           coalesce(postal_code, '') = ''");
            sql.append("       and (coalesce(address1, '') || coalesce(address2, '') || coalesce(address3, '') = '')");
            sql.append("     )");
        }

        // 郵便番号
        if (!this.getPostalCode().equals("")) {
            sql.append("     and postal_code like '" + this.getPostalCode() + "%'");
        }

        // 都道府県
        if (0 < prefecture.getSelectedIndex()) {
            sql.append("     and address1 = '" + prefecture.getSelectedItem().toString() + "'");
        }

        // 市区町村
        if (0 < city.getSelectedIndex()) {
            sql.append("     and address2 = '" + city.getSelectedItem().toString() + "'");
        }

        // 職業
        if (0 < job.getSelectedIndex()) {
            MstJob mj = (MstJob)job.getSelectedItem();
            sql.append("     and job_id = " + mj.getJobID().toString());
        }

        // 備考
        if (!memo.getText().equals("")) {
            sql.append("     and note like '%" + memo.getText()+"%'");
        }

        // 初回来店動機
        if (firstComingMotive.getSelectedIndex() > 0){
            MstFirstComingMotive motive = (MstFirstComingMotive)firstComingMotive.getSelectedItem();
            sql.append("     and first_coming_motive_class_id = " + SQLUtil.convertForSQL(motive.getFirstComingMotiveClassId()));
        }

        // 初回来店日
        if (dateType3.isSelected()) {
            if (visitDateFrom.getDateStr() != null) {
                sql.append(" and " + firstVisitDateSql.toString() + " >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                sql.append(" and coalesce(t.before_visit_num, 0) < 1");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append(" and " + firstVisitDateSql.toString() + " <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                sql.append(" and coalesce(t.before_visit_num, 0) < 1");
            }
        }

        sql.append("     )");

        /**********************/
        /* 来店日付 */
        /**********************/

        // 初回来店日かつ以下の条件が指定されている場合は売上情報も検索する
        boolean isFirstVisitDateAndSalesCondition = false;
        if (dateType3.isSelected()) {
            //主担当者
            if (chargeStaff.getSelectedIndex() > 0) {
                isFirstVisitDateAndSalesCondition = true;
            }
            // 主担当指名区分
            if (chargeStaffNamed.isSelected()) {
                isFirstVisitDateAndSalesCondition = true;
            }
            if (chargeStaffFree.isSelected()) {
                isFirstVisitDateAndSalesCondition = true;
            }
            if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
                // 技術分類
                if (this.isCondTechClassSelected()) {
                    isFirstVisitDateAndSalesCondition = true;
                }
                // 技術
                if (this.isCondTechSelected()) {
                    isFirstVisitDateAndSalesCondition = true;
                }
            }
            if (this.isCondItemClassSelected() || this.isCondItemSelected()) {
                // 商品分類
                if (this.isCondItemClassSelected()) {
                    isFirstVisitDateAndSalesCondition = true;
                }
                // 商品
                if (this.isCondItemSelected()) {
                    isFirstVisitDateAndSalesCondition = true;
                }
            }
            // 反響項目
            if (0 < responseItem.getSelectedIndex()) {
                isFirstVisitDateAndSalesCondition = true;
            }
        }

        if(isSalesCondition && (dateType0.isSelected() || isFirstVisitDateAndSalesCondition)) {

            sql.append("     and exists");
            sql.append("     (");

                sql.append("     select 1");
                sql.append("     from");
                sql.append("         view_data_sales_detail_valid_with_prepaid d");
                sql.append("             left outer join mst_technic mt");
                sql.append("                          on d.product_division = 1");
                sql.append("                         and d.product_id = mt.technic_id");
                sql.append("             left outer join mst_item mi");
                sql.append("                          on d.product_division = 2");
                sql.append("                         and d.product_id = mi.item_id");
                sql.append("             left outer join mst_course mc");
                sql.append("                          on d.product_division = 5");
                sql.append("                         and d.product_id = mc.course_id");
                sql.append("     where");
                sql.append("             d.shop_id in (" + shopIDList + ")");
                sql.append("         and d.customer_id = t.customer_id");

                // 来店日付
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and d.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                    // 初回来店日を含む場合
                    if (dateType3.isSelected()) {
                        sql.append(" and " + firstVisitDateSql.toString() + " >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                        sql.append(" and coalesce(t.before_visit_num, 0) < 1");
                    }
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and d.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                    // 初回来店日を含む場合
                    if (dateType3.isSelected()) {
                        sql.append(" and " + firstVisitDateSql.toString() + " <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                        sql.append(" and coalesce(t.before_visit_num, 0) < 1");
                    }
                }

                //主担当者
                if (chargeStaff.getSelectedIndex() > 0) {
                    sql.append("     and d.staff_id = " + ((MstStaff)chargeStaff.getSelectedItem()).getStaffID());
                }

                // 主担当指名区分
                if (chargeStaffNamed.isSelected()) {
                    sql.append("     and d.designated_flag = true");
                }
                if (chargeStaffFree.isSelected()) {
                    sql.append("     and d.designated_flag = false");
                }

                if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected() 
				|| this.isCondTechClassSelected() || this.isCondTechSelected()
				|| this.isCondCourseClassSelected() || this.isCondCourseSelected()
				)) {
			sql.append("         and (");
		}
                if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
                    if (techItemOR.isSelected()) {
//                        sql.append("         and (");
//                        if (this.isCondItemClassSelected() || this.isCondItemSelected()) {
//                            sql.append("         (");
//                        }
                        // 技術分類
                        if (this.isCondTechClassSelected()) {
                            sql.append(getCondTechClassCondition("d"));
                        }
                        // 技術
                        if (this.isCondTechSelected()) {
                            if(this.isCondTechClassSelected()) {
                                sql.append("         or ");
                            }
                            sql.append(getCondTechCondition("d"));
                        }
                        //sql.append("         )");
                    }else {
                        // 技術分類
                        if (this.isCondTechClassSelected()) {
                            sql.append(getCondTechClassCondition("d"));
                        }
                        // 技術
                        if (this.isCondTechSelected()) {
                            sql.append(getCondTechCondition("d"));
                        }
                    }
                }

                if (this.isCondItemClassSelected() || this.isCondItemSelected()) {
                    
                    if (techItemOR.isSelected()) {
                        if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected())) {
                            sql.append("         or (");
                        }else {
                            sql.append("         (");
                        }
			// 商品分類
			if (this.isCondItemClassSelected()) {
				sql.append(getCondItemClassCondition("d"));
			}
			// 商品
			if (this.isCondItemSelected()) {
                            if(this.isCondItemClassSelected()) {
                                sql.append("         or ");
                            }
                            sql.append(getCondItemCondition("d"));
			}
			
			sql.append("         )");
//                        if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
//                            sql.append("     )");
//                        }
                    }else{
                        // 商品分類
                        if (this.isCondItemClassSelected()) {
                            sql.append(getCondItemClassCondition("d"));
                        }
                        // 商品
                        if (this.isCondItemSelected()) {
                            sql.append(getCondItemCondition("d"));
                        }
                    }
                }
                
                //コース
                if (this.isCondCourseClassSelected() || this.isCondCourseSelected()) {

                    if (techItemOR.isSelected()) {
                        if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected()
                                        ||this.isCondItemClassSelected() || this.isCondItemSelected())) {
                                sql.append(" or (");
                        }else {
                                sql.append("     (");
                        }
                        // コース分類
                        if (this.isCondCourseClassSelected()) {
                                        sql.append(getCondCourseClassCondition("d"));
                        }
                        // コース
                        if (this.isCondCourseSelected()) {
                                if(this.isCondCourseClassSelected()) {
                                        sql.append("         or ");
                                }
                                sql.append(getCondCourseCondition("d"));
                        }
                        sql.append("         )");

                    }else{
                        // コース分類
                        if (this.isCondCourseClassSelected()) {
                                sql.append(getCondCourseClassCondition("d"));
                        }
                        // コース
                        if (this.isCondCourseSelected()) {
                                sql.append(getCondCourseCondition("d"));
                        }
                    }
                }
                
                if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected() 
				|| this.isCondTechClassSelected() || this.isCondTechSelected()
				|| this.isCondCourseClassSelected() || this.isCondCourseSelected()
				)) {
			sql.append("         ) ");
		}

                // 反響項目
                if (0 < responseItem.getSelectedIndex()) {
                    sql.append("            and exists");
                    sql.append("            (");
                    sql.append("                select 1");
                    sql.append("                from");
                    sql.append("                    data_response_effect");
                    sql.append("                where");
                    sql.append("                        delete_date is null");
                    sql.append("                    and shop_id = d.shop_id");
                    sql.append("                    and slip_no = d.slip_no");
                    sql.append("                    and response_id = " + SQLUtil.convertForSQL(((MstResponse)responseItem.getSelectedItem()).getResponseID()));
                    sql.append("            )");
                }

            sql.append("     )");
        }

        // 来店回数
        if (!visitCountFrom.getText().equals("")) {
            sql.append("     and " + visitNumSql.toString() + " >= " + visitCountFrom.getText());
        }
        if (!visitCountTo.getText().equals("")) {
            sql.append("     and " + visitNumSql.toString() + " <= " + visitCountTo.getText());
        }

        // 売上金額
        if (!priceFrom.getText().equals("")) {
            sql.append("     and " + sumTotalSql.toString() + " >= " + priceFrom.getText());
        }
        if (!priceTo.getText().equals("")) {
            sql.append("     and " + sumTotalSql.toString() + " <= " + priceTo.getText());
        }

        /**********************/
        /* 次回予約日 */
        /**********************/
        if(dateType2.isSelected()) {
            sql.append("     and exists");
            sql.append("     (");
            sql.append("         select 1");
            sql.append("         from");
            sql.append("             data_reservation as dr");
            sql.append("                 inner join data_reservation_detail drd");
            sql.append("                     using(shop_id, reservation_no)");
            sql.append("         where");
            sql.append("                 dr.delete_date is null");
            sql.append("             and drd.delete_date is null");
            sql.append("             and drd.reservation_datetime > now()");
            sql.append("             and dr.shop_id in (" + shopIDList + ")");
            sql.append("             and dr.customer_id = t.customer_id");

            //主担当者
            if (chargeStaff.getSelectedIndex() > 0) {
                sql.append("         and dr.staff_id = " + ((MstStaff)chargeStaff.getSelectedItem()).getStaffID());
            }

            // 主担当指名区分
            if (chargeStaffNamed.isSelected()) {
                sql.append("         and dr.designated_flag = true");
            }
            if (chargeStaffFree.isSelected()) {
                sql.append("         and dr.designated_flag = false");
            }

            sql.append("         having true");

            if (visitDateFrom.getDateStr() != null) {
                sql.append("         and min(reservation_datetime) >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append("         and min(reservation_datetime) <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
            sql.append("     )");
        }	    

        // ふりがな順で出力（未設定は最後に出力）
        sql.append(" order by");
        sql.append("      case when length(coalesce(t.customer_kana1, '')) > 0 then 0 else 1 end");
        sql.append("     ,t.customer_kana1");
        sql.append("     ,t.customer_kana2");
        //Luc start edit 20151225 #45536
        
        sql.append("        ) ds");
        sql.append("      inner join view_data_sales_detail_valid_with_total_discount dsd\n");
        sql.append("          using(shop_id, slip_no) \n");
        sql.append("      inner join \n");
        sql.append("              mst_customer mc \n");
        sql.append("      on ds.customer_id = mc.customer_id \n");
        sql.append("      inner join  \n");
        sql.append("              mst_shop ms \n");
        sql.append("      on ds.shop_id = ms.shop_id \n");
        sql.append("      left outer join  \n");
        sql.append("              mst_staff staff1 \n");
        sql.append("      on ds.staff_id = staff1.staff_id \n");
        sql.append("      left outer join  \n");
        sql.append("              mst_staff staff2 \n");
        sql.append("      on dsd.detail_staff_id = staff2.staff_id \n");
       
        sql.append("      left outer join \n");
        sql.append("      ( \n");
        sql.append("      	select  \n");
        sql.append(" a.slip_no,a.shop_id, ");
        sql.append("      	     mt.technic_id  \n");
        sql.append("      	    ,technic_class_name  \n");
        sql.append("      	    ,technic_name  \n");
        sql.append("      	    ,false as is_proportionally  \n");
        sql.append("      	    ,technic_no  \n");
        sql.append("      	    ,mt.price  \n");
        sql.append("      	    ,null as proportionally_name  \n");
        sql.append("      	    ,0 as proportionally_ratio  \n");
        sql.append("      	    ,0 as proportionally_point  \n");
        sql.append("      	    ,0 as display_seq  \n");
        sql.append("              ,0 as data_proportionally_id  \n");
        //nhanvt start add 20141024 Bug #31726
        sql.append("                  ,0 as staff_id  \n");
        //nhanvt start add 20141127 Bug #33192
        sql.append("                  ,0 as seq_num  \n");
        //nhanvt end add 20141127 Bug #33192
        //nhanvt end edit 20141024 Bug #31726
        //nhanvt start edit 20141024 Bug #31726
        sql.append("      	from  \n");
//        sql.append("      	    mst_technic mt  \n");
//        sql.append("      	        inner join mst_technic_class  \n");
//        sql.append("      	            using(technic_class_id)  \n");
        
        
        
        sql.append(" data_sales a ");
        sql.append(" inner join data_sales_detail b on a.shop_id = b.shop_id and a.slip_no = b.slip_no ");
        sql.append(" inner join mst_technic mt on mt.technic_id = b.product_id  ");
        sql.append(" INNER JOIN mst_technic_class USING(technic_class_id) ");
        sql.append(" where b.product_division in (1,3) ");
        sql.append(" and a.shop_id in ( " + shopIDList + ") ");
        if (visitDateFrom.getDateStr() != null) {
               sql.append(" and a.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'\n");
           }
           // 最終来店日の終わりを条件に含める
           if (visitDateTo.getDateStr() != null) {
               sql.append(" and a.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'\n");
           }
        
        //nhanvt end edit 20141024 Bug #31726

        // 按分使用有の場合
        if (isProportionally) {
           
            sql.append("      	union all  \n");
            sql.append("      	  \n");
            sql.append("      	select  \n");
            sql.append(" a.slip_no,a.shop_id, ");
            sql.append("      	     dp.technic_id  \n");
            sql.append("      	    ,case when mp.proportionally_name is null  \n");
            sql.append("      	        then mtc.technic_class_name  \n");
            sql.append("      	        else '<按分>'  \n");
            sql.append("      	     end as technic_class_name  \n");
            sql.append("      	  \n");
            sql.append("      	    ,case when mp.proportionally_name is null  \n");
            sql.append("      	        then mt.technic_name  \n");
            sql.append("      	        else mp.proportionally_name  \n");
            sql.append("      	     end as technic_name  \n");
            sql.append("      	  \n");
            sql.append("      	    ,case when mp.proportionally_name is null  \n");
            sql.append("      	        then false  \n");
            sql.append("      	        else true  \n");
            sql.append("      	     end as is_proportionally  \n");
            sql.append("      	  \n");
            sql.append("      	    ,mt.technic_no  \n");
            sql.append("      	    ,mt.price  \n");
            sql.append("      	    ,mp.proportionally_name  \n");
            //nhanvt start edit 20141127 Bug #33192
            sql.append("      	    ,a.ratio as proportionally_ratio  \n");
            sql.append("      	    ,a.point as proportionally_point  \n");
            //nhanvt end edit 20141127 Bug #33192
            sql.append("      	    ,mp.display_seq  \n");
            //nhanvt start add 20141024 Bug #31726
            sql.append("                  ,a.data_proportionally_id  \n");
            sql.append("                  ,a.staff_id  \n");
            //nhanvt start edit 20141127 Bug #33192
            sql.append("                  ,a.seq_num  \n");
            //nhanvt end edit 20141127 Bug #33192
            //nhanvt end add 20141024 Bug #31726
            sql.append("      	from  \n");
            //nhanvt start edit 20141024 Bug #31726
//            sql.append("      	    data_proportionally dp  \n");
//            sql.append("      	        left outer join mst_proportionally mp  \n");
//            sql.append("      	                     on dp.proportionally_id = mp.proportionally_id  \n");
//            sql.append("      	                    and mp.delete_date is null  \n");
//            sql.append("      	        left outer join mst_technic mt  \n");
//            sql.append("      	                     on mt.technic_id = dp.technic_id  \n");
//            sql.append("      	        inner join mst_technic_class mtc  \n");
//            sql.append("      	                on mt.technic_class_id = mtc.technic_class_id  \n");
//            sql.append("      	where  \n");
//            sql.append("      	    dp.delete_date is null  \n");
            
            sql.append("      	     data_sales_proportionally a   \n");
            sql.append("      	        join  data_proportionally dp on a.data_proportionally_id = dp.data_proportionally_id  \n");
            sql.append("      	                     JOIN mst_proportionally mp on mp.proportionally_id = dp.proportionally_id  \n");
            sql.append("      	                    LEFT OUTER JOIN mst_technic mt ON mt.technic_id = dp.technic_id  \n");
            sql.append("      	        INNER JOIN mst_technic_class mtc ON mt.technic_class_id = mtc.technic_class_id  \n");
            sql.append("      	                     where a.delete_date IS NULL  \n");
            sql.append(" and a.shop_id in ( " + shopIDList + ")");
            //nhanvt end edit 20141024 Bug #31726
        }

        sql.append("      ) as mt \n");
        sql.append("      on mt.technic_id = dsd.product_id and product_division in (1,3)\n");
        //nhanvt start add 20141024 Bug #31726
        sql.append(" and dsd.slip_no = mt.slip_no ");
        //nhanvt end edit 20141024 Bug #31726

        sql.append("      left join \n");
        sql.append("              mst_item mi \n");
        sql.append("      on dsd.product_id = mi.item_id and product_division in (2,4) \n");
        sql.append("      left join \n");
        sql.append("              mst_item_class mic \n");
        sql.append("      on mi.item_class_id = mic.item_class_id \n");

        sql.append("      left join mst_course c\n");
        sql.append("              on dsd.product_id = c.course_id\n");
        //Luc start edit 20151215 #45313
        //sql.append("             and product_division in (5,6,8,9) \n");
        sql.append("             and product_division in (5,6,7,8,9) \n");
        //Luc end edit 20151215 #45313
        sql.append("      left join mst_course_class cc\n");
        sql.append("              on c.course_class_id = cc.course_class_id \n");
        //nhanvt start edit  20141008 Bug #30993
        if(isOld){
            sql.append("      left join \n");
            sql.append("              data_reservation dr \n");
            sql.append("       on ds.slip_no = dr.slip_no \n");
            sql.append("      and ds.shop_id = dr.shop_id \n");
            sql.append("      and ds.customer_id = dr.customer_id \n");

            sql.append("      left join data_reservation_detail drd\n");
            sql.append("             on drd.delete_date is null\n");
            sql.append("            and drd.contract_no is not null\n");
            sql.append("            and drd.contract_detail_no is not null\n");
            sql.append("            and dr.shop_id = drd.shop_id \n");
            sql.append("            and dr.reservation_no = drd.reservation_no\n");
            sql.append("            and (dsd.product_division in (5) and dsd.product_id = drd.technic_id)\n");

            sql.append("      left join data_contract_digestion dcd\n");
            sql.append("             on ds.shop_id = dcd.shop_id\n");
            sql.append("            and ds.slip_no = dcd.slip_no\n");
            sql.append("            and drd.contract_no = dcd.contract_no\n");
            sql.append("            and drd.contract_detail_no = dcd.contract_detail_no\n");
        }
        
        sql.append(") sales\n");
        // ThuanNK end edit 20140318
        sql.append(" left join\n");
        sql.append(" (\n");
        sql.append("     select\n");
        sql.append("          dp.shop_id\n");
        sql.append("         ,dp.slip_no\n");
        // IVS SANG START INSERT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        sql.append("         ,dp.payment_date\n");
        // IVS SANG END INSERT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        sql.append("         ,max(case when b.payment_class_id = 1 then a.payment_value-dp.change_value end)          as money_value\n");
        //IVS NNTUAN START EDIT 20131112
        /*
         sql.append("         ,max(case when b.payment_class_id = 2 then b.payment_method_name end)    as card_name");
         sql.append("         ,max(case when b.payment_class_id = 2 then a.payment_value end)          as card_value");
         sql.append("         ,max(case when b.payment_class_id = 3 then b.payment_method_name end)    as ecash_name");
         sql.append("         ,max(case when b.payment_class_id = 3 then a.payment_value end)          as ecash_value");
         sql.append("         ,max(case when b.payment_class_id = 4 then b.payment_method_name end)    as gift_name");
         sql.append("         ,max(case when b.payment_class_id = 4 then a.payment_value end)          as gift_value");
         */
        sql.append("         ,(case when b.payment_class_id = 2 then a.payment_value end)          as card_value\n");
        sql.append("         ,(case when b.payment_class_id = 3 then a.payment_value end)          as ecash_value\n");
        sql.append("         ,(case when b.payment_class_id = 4 then a.payment_value end)          as gift_value\n");
        //IVS NNTUAN END EDIT 20131112

        sql.append("         ,max(dp.bill_value) as bill_value\n");
        sql.append("     from\n");
        sql.append("         data_payment dp\n");
        sql.append("             left join data_payment_detail a\n");
        sql.append("                 using (shop_id, slip_no, payment_no)\n");
        sql.append("             left join mst_payment_method b\n");
        sql.append("                 using (payment_method_id)\n");
        sql.append("     where\n");
        sql.append("         dp.shop_id in (" + shopIDList + ")\n");
        // IVS SANG START EDIT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        sql.append("         and dp.payment_no = ( \n");
        sql.append("                  select min(dp1.payment_no) \n");
        sql.append("                  from data_payment dp1 \n");
        sql.append("                  where dp1.shop_id = dp.shop_id and dp1.slip_no = dp.slip_no) \n");
        // IVS SANG START EDIT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        sql.append("     group by\n");
        //IVS NNTUAN START ADD 20131112
        sql.append("           a.payment_detail_no ,  b.payment_class_id, a.payment_value,\n");
        //IVS NNTUAN END ADD 20131112
        sql.append("          dp.shop_id\n");
        sql.append("         ,dp.slip_no\n");
        // IVS SANG START EDIT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        sql.append("         ,dp.payment_date\n");
        // IVS SANG START EDIT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        sql.append(" ) payment\n");
        // IVS SANG START EDIT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        // sql.append(" using (shop_id, slip_no)");
        sql.append(" on sales.shop_id = payment.shop_id and sales.slip_no =  payment.slip_no and sales.sales_date = payment.payment_date\n");
        // IVS SANG END EDIT 20131108 [gbソース]来店促進⇒条件検索⇒EXCEL出力⇒精算情報
        sql.append(" where\n");
        sql.append("     not (product_division = 3 and category_name = '<按分>')\n");
        //IVS NNTUAN START ADD 20131112
        // ThuanNK start edit 20140307
        sql.append("group by  sales.update_date  , sales.shop_id   ,sales.slip_no  ,sales.sales_date  , sales.customer_id ,sales.shop_name, sales.customer_no ,sales.customer_name \n"
                // ThuanNK end edit 20140307
                + "	,sales.charge_staff , sales.charge_designated_flag , sales.product_division, sales.category_name , sales.product_name , sales.product_value , sales.product_num\n"
                + "	,sales.discount_value , sales.discount_detail_value_in_tax , sales.technic_staff, sales.technic_designated_flag , sales.reservation_datetime , sales.visit_time, sales.start_time\n"
                + "	,sales.leave_time , sales.wait_time , sales.stay_time, sales.progress_day , sales.response_name, sales.visited_memo, sales.is_proportionally, sales.product_division_name \n");
        
        
        //nhanvt start edit 20141024 Bug #31726
        sql.append(" ,sales.slip_detail_no, sales.staff_id , sales.data_proportionally_id \n ");
        //nhanvt end edit 20141024 Bug #31726
        //IVS NNTUAN END ADD 20131112
        // IVS SANG START INSERT [gbソース]顧客情報画面⇒来店情報タブ、条件検索EXCEL出力
        //nhanvt start add 20141127 Bug #33192
        sql.append(" , sales.seq_num \n ");
        //nhanvt end add 20141127 Bug #33192
        sql.append(",payment.bill_value\n");
        // IVS SANG END INSERT [gbソース]顧客情報画面⇒来店情報タブ、条件検索EXCEL出力
        // 201805 GB add start #35807
        sql.append(" ,sales.detail_value_in_tax ");
        sql.append(" ,sales.discount_value_in_tax ");
        //IVS_LVTu edit 2019/09/20  SPOS増税対応
        sql.append(" ,sales.tax_rate ");
        // 201805 GB add end #35807
        // ThuanNK start add 20140318
        //IVS start add 2022/07/12 0624_出力項目の追加
        if (SystemInfo.getDatabase().equals(this.DB_POS_HAIR_BIP)) {
            sql.append(" ,sales.sex ");
            sql.append(" ,sales.age ");
            sql.append(" ,sales.job_name ");
            sql.append(" ,sales.address1 ");
            sql.append(" ,sales.address2 ");
            for (int l = 1; l <= numFreeHeadingClass; l++) {
                sql.append(" ,sales.free_heading_name").append(l);
            }
            sql.append(" ,sales.discount_value_no_tax ");
        }
        //IVS end add 2022/07/12 0624_出力項目の追加
        sql.append(" order by sales.slip_no \n");
        // ThuanNK end add 20140318
        //IVS_LVTu end edit 2016/03/16 getCondTechClassCondition
        try {
            rs = con.executeQuery(sql.toString());
//             while(rs.next()){
//                 SearchResultAccountInfo info = new SearchResultAccountInfo();
//                 info.setData(rs);
//                 list.add(info);
//             }
            con.execute("drop function tmp_get_progress_day" + nowString + "(integer, timestamp without time zone)");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql.toString());
        }

        return rs;
    }

    private void btnOutputExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutputExcelActionPerformed

        
        // 次回予約日で検索する場合は日付入力必須
        if (dateType2.isSelected()) {
            if (visitDateFrom.getDateStr() == null || visitDateTo.getDateStr() == null) {
                MessageDialog.showMessageDialog(this,
                        "次回予約日で検索する場合は、日付を設定してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        OutputExcelDialog dialg = new OutputExcelDialog(this.parentFrame, true);
        SwingUtil.moveCenter(dialg);
        dialg.setVisible(true);
        //IVS_LVTU start add 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        this.isCustomerNo_0 = dialg.getIsCustomerNo_0();
        //IVS_LVTU end add 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        int outputType = dialg.getOutputType();
        if (outputType < 0) {
            return;
        }

        try {

            if (outputType == OutputExcelDialog.OUTPUT_CUSTOMER) {
                if (!this.setCustomerArrayList(SEND_TYPE_EXCEL, outputType)) {
                    return;
                }
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (!outputExcel(outputType)) {
                return;
            }

        } catch (Exception e) {

            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_btnOutputExcelActionPerformed

    private boolean outputExcel(int outputType) {
        if (outputType == OutputExcelDialog.OUTPUT_ACCOUNT) {
            ResultSetWrapper rs = getInfoForAccountOutput(SystemInfo.getConnection());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            //------------------
            // 精算情報
            //------------------
            try {


                JExcelApi jx = new JExcelApi("条件検索_精算情報");
                //IVS start edit 2022/07/12 0624_出力項目の追加
                //jx.setTemplateFile("/reports/条件検索_精算情報.xls");
                if (SystemInfo.getDatabase().equals(this.DB_POS_HAIR_BIP)) {
                    jx.setTemplateFile("/reports/条件検索_精算情報_pos_hair_bip.xls");
                } else {
                    jx.setTemplateFile("/reports/条件検索_精算情報.xls");
                }
                //IVS start edit 2022/07/12 0624_出力項目の追加

                int row = 2;
                //IVS start add 2022/07/12 0624_出力項目の追加
                if (SystemInfo.getDatabase().equals(this.DB_POS_HAIR_BIP)) {
                    if (numFreeHeadingClass > 0) {
                        jx.insertColumn(9, numFreeHeadingClass);
                    }
                    
                    int columnFreeHeading = 10;
                    for (int i = 0; i < arrOrderByFreeHeading.size(); i++) {
                            String[] freeHeading = arrOrderByFreeHeading.get(i);
                            jx.setValue(columnFreeHeading, 1, freeHeading[1]);
                            columnFreeHeading += 1;
                    }
                }
                //IVS end add 2022/07/12 0624_出力項目の追加

                MstAccountSetting ma = SystemInfo.getAccountSetting(); //201805 add #35807

                // データセット
                //Iterator iter = list.iterator();

                while (rs.next()) {
                    // 追加行数セット
                    if (row > 2) {
                        jx.insertRow(row - 1, 1);
                    }

                    //		    SearchResultAccountInfo info = (SearchResultAccountInfo)iter.next();
                    //		    jx.setValue(1, row, info.getShopName());
                    //		    jx.setValue(2, row, info.getSlipNo().replaceAll("[ |　]",""));
                    //		    jx.setValue(3, row, info.getCustomerNo().replaceAll("[ |　]",""));
                    //		    jx.setValue(4, row, info.getCustomerName());
                    //		    jx.setValue(5, row, info.getSalesDate());
                    //		    jx.setValue(6, row, info.getChargeStaff());
                    //		    jx.setValue(7, row, info.getFree());
                    //		    jx.setValue(8, row, info.getProductDivisionName());
                    //		    jx.setValue(9, row, info.getCategoryName());
                    //		    jx.setValue(10, row, info.getProductName());
                    //		    jx.setValue(11, row, info.getProductValue());
                    //		    jx.setValue(12, row, info.getProductNum());
                    //		    jx.setValue(13, row, info.getDiscountValue());
                    //		    jx.setValue(14, row, info.getTotalPrice());
                    //                    jx.setValue(15, row, info.getMoneyValue());
                    //		    jx.setValue(16, row, info.getCardName());
                    //		    jx.setValue(17, row, info.getCardValue());
                    //		    jx.setValue(18, row, info.getEcashName());
                    //		    jx.setValue(19, row, info.getEcashValue());
                    //		    jx.setValue(20, row, info.getGiftName());
                    //		    jx.setValue(21, row, info.getGiftValue());
                    //                    jx.setValue(22, row, info.getBillValue());
                    //                    jx.setValue(23, row, info.getTechnicStaff());
                    //		    jx.setValue(24, row, info.getFreeTechnic());
                    //		    jx.setValue(25, row, info.getReserveTime());
                    //		    jx.setValue(26, row, info.getVisitTime());
                    //		    jx.setValue(27, row, info.getStartTime());
                    //		    jx.setValue(28, row, info.getWaitTime());
                    //		    jx.setValue(29, row, info.getStayTime());
                    //		    jx.setValue(30, row, info.getLeaveTime());
                    //		    jx.setValue(31, row, info.getProgressDay());
                    //		    jx.setValue(32, row, info.getResponseName());
                    //		    jx.setValue(33, row, info.getNextReserveDate());
                    //		    jx.setValue(34, row, info.getVisitedMemo());
                    //                    //IVS NNTUAN START ADD 20131008
                    //                    jx.setValue(35, row, info.getUpdateDate());
                    //                    //IVS NNTUAN END ADD 20131008
                    //                    


                    //SearchResultAccountInfo info = (SearchResultAccountInfo) iter.next();
                    jx.setValue(1, row, rs.getString("shop_name") != null ? rs.getString("shop_name") : "");
                    jx.setValue(2, row, rs.getString("slip_no") != null ? rs.getString("slip_no") : "");
                    jx.setValue(3, row, rs.getString("customer_no") != null ? rs.getString("customer_no") : "");
                    jx.setValue(4, row, rs.getString("customer_name") != null ? rs.getString("customer_name") : "");
                    //IVS start add 2022/07/12 0624_出力項目の追加
                    //反響項目
                    Array arrResponseMame = rs.getArray("response_name");
                    String ResponseMame1 = "";
                    String ResponseMame2 = "";
                    if (arrResponseMame != null) {

                        String[] ResponseMame = (String[])arrResponseMame.getArray();

                        for (int i = 0;arrResponseMame != null && i < ResponseMame.length;i ++) {
                            if (i == 0) {
                                ResponseMame1 = ResponseMame[i];
                            } else {
                                ResponseMame2 = ResponseMame2.equals("") == true ? ResponseMame[i] : ("," + ResponseMame[i]);
                            }
                        }
                    }
                    if (SystemInfo.getDatabase().equals(this.DB_POS_HAIR_BIP)) {
                        jx.setValue(5, row, rs.getString("sex") != null ? rs.getString("sex") : "");
                        jx.setValue(6, row, (int) rs.getDouble("age") > 0 ? (int) rs.getDouble("age") : "");
                        jx.setValue(7, row, rs.getString("job_name") != null ? rs.getString("job_name") : "");
                        jx.setValue(8, row, rs.getString("address1") != null ? rs.getString("address1") : "");
                        jx.setValue(9, row, rs.getString("address2") != null ? rs.getString("address2") : "");
                        int stepCol = 9;
                        for (int i = 1; i <= numFreeHeadingClass; i++) {
                            String free_heading_name = rs.getString("free_heading_name" + i) != null && !rs.getString("free_heading_name" + i).equals("") ? rs.getString("free_heading_name" + i) : "";
                            jx.setValue(stepCol+1, row, free_heading_name);
                            stepCol += 1;
                        }
                        jx.setValue(1 + stepCol, row, rs.getDate("sales_date") != null ? dateFormat.format(rs.getDate("sales_date")) : "");
                        jx.setValue(2 + stepCol, row, rs.getString("charge_staff") != null ? rs.getString("charge_staff") : "");
                        jx.setValue(3 + stepCol, row, rs.getBoolean("charge_designated_flag") == true ? "指名" : "フリー");
                        jx.setValue(4 + stepCol, row, rs.getString("product_division_name") != null ? rs.getString("product_division_name") : "");
                        jx.setValue(5 + stepCol, row, rs.getString("category_name") != null ? rs.getString("category_name") : "");
                        jx.setValue(6 + stepCol, row, rs.getString("product_name") != null ? rs.getString("product_name") : "");
                        jx.setValue(7 + stepCol, row, (int) rs.getDouble("product_value"));
                        jx.setValue(8 + stepCol, row, rs.getString("product_num") + (rs.getBoolean("is_proportionally") ? "%" : ""));
                        if (ma.getDisplayPriceType()==1 && ma.getDiscountType()==1) {
                            //外税＆税抜き額から割引
                            jx.setValue(9 + stepCol, row, (int) rs.getDouble("discount_value_in_tax"));
                        } else {
                            jx.setValue(9 + stepCol, row, (int) rs.getDouble("discount_value"));
                        }
                        jx.setValue(10 + stepCol, row, (int) rs.getDouble("total_price"));
                        jx.setValue(11 + stepCol, row, rs.getDouble("tax_rate"));
                        double taxExcluded;
                        double taxValue;
                        BigDecimal tempValue = new BigDecimal(0);
                        if (rs.getInt("product_division") == 0) {
                            tempValue = new BigDecimal(rs.getDouble("product_value") / (1 + rs.getDouble("tax_rate")));
                        } else {
                            tempValue = new BigDecimal(((rs.getDouble("product_value") * rs.getDouble("product_num")) / (1 + rs.getDouble("tax_rate"))) - rs.getDouble("discount_value_no_tax"));
                        }
                        tempValue = tempValue.setScale(3, RoundingMode.HALF_UP);

                        if (tempValue.doubleValue() > 0) {
                            taxExcluded = ((Double) Math.ceil(tempValue.doubleValue())).longValue();
                        } else {
                            taxExcluded = ((Double) Math.floor(tempValue.doubleValue())).longValue();
                        }
                        taxValue = tempValue.doubleValue() * rs.getDouble("tax_rate");
                        if (taxValue > 0) {
                            taxValue = Math.floor(taxExcluded * rs.getDouble("tax_rate"));
                        } else {
                            taxValue = Math.ceil(taxExcluded * rs.getDouble("tax_rate"));
                        }
                        jx.setValue(12 + stepCol, row, (int) taxExcluded);
                        jx.setValue(13 + stepCol, row, (int) taxValue);
                        jx.setValue(14 + stepCol, row, (int) rs.getDouble("money_value"));
                        jx.setValue(15 + stepCol, row, rs.getString("card_name"));
                        jx.setValue(16 + stepCol, row, (int) rs.getDouble("card_value"));
                        jx.setValue(17 + stepCol, row, rs.getString("ecash_name"));
                        jx.setValue(18 + stepCol, row, (int) rs.getDouble("ecash_value"));
                        jx.setValue(19 + stepCol, row, rs.getString("gift_name"));
                        jx.setValue(20 + stepCol, row, (int) rs.getDouble("gift_value"));
                        if (rs.getString("product_division_name") != null) {
                            if (rs.getString("product_division_name").equals("コース解約")) {
                                jx.setValue(21 + stepCol, row, 0);
                            } else {
                                 jx.setValue(21 + stepCol, row, (int) rs.getDouble("bill_value"));
                            }
                        } else {
                             jx.setValue(21 + stepCol, row, (int) rs.getDouble("bill_value"));
                        }

                        jx.setValue(22 + stepCol, row, rs.getString("technic_staff") != null ? rs.getString("technic_staff") : "");
                        jx.setValue(23 + stepCol, row, rs.getInt("product_division") == 0 ? "" : rs.getBoolean("technic_designated_flag") == true ? "指名" : "フリー");
                        jx.setValue(24 + stepCol, row, rs.getString("reservation_datetime"));
                        jx.setValue(25 + stepCol, row, rs.getString("visit_time"));
                        jx.setValue(26 + stepCol, row, rs.getString("start_time"));
                        jx.setValue(27 + stepCol, row, rs.getString("wait_time") != null ? rs.getString("wait_time") : "");
                        jx.setValue(28 + stepCol, row, rs.getString("stay_time") != null ? rs.getString("stay_time") : "");
                        jx.setValue(29 + stepCol, row, rs.getString("leave_time"));
                        jx.setValue(30 + stepCol, row, rs.getString("progress_day") != null ? rs.getString("progress_day") : "");
                        jx.setValue(31 + stepCol, row, ResponseMame1);
                        jx.setValue(32 + stepCol, row, ResponseMame2);
                        jx.setValue(33 + stepCol, row, rs.getDate("next_reserve_date") != null ? dateFormat.format(rs.getDate("next_reserve_date")) : "");
                        jx.setValue(34 + stepCol, row, rs.getString("visited_memo") != null ? rs.getString("visited_memo") : "");
                        jx.setValue(35 + stepCol, row, rs.getDate("update_date").toString());
                    } else {
                    //IVS end add 2022/07/12 0624_出力項目の追加
                        jx.setValue(5, row, rs.getDate("sales_date") != null ? dateFormat.format(rs.getDate("sales_date")) : "");
                        jx.setValue(6, row, rs.getString("charge_staff") != null ? rs.getString("charge_staff") : "");

                        //IVS_LVTu start edit 2017/09/21 #26374 [gb]精算情報：主担当を指名していても「フリー」と表示される
                        jx.setValue(7, row, rs.getBoolean("charge_designated_flag") == true ? "指名" : "フリー");
                        //IVS_LVTu end edit 2017/09/21 #26374 [gb]精算情報：主担当を指名していても「フリー」と表示される
                        jx.setValue(8, row, rs.getString("product_division_name") != null ? rs.getString("product_division_name") : "");
                        jx.setValue(9, row, rs.getString("category_name") != null ? rs.getString("category_name") : "");
                        jx.setValue(10, row, rs.getString("product_name") != null ? rs.getString("product_name") : "");
                        jx.setValue(11, row, (int) rs.getDouble("product_value"));
                        jx.setValue(12, row, rs.getString("product_num") + (rs.getBoolean("is_proportionally") ? "%" : ""));
                        // 201805 GB edit start #35807
                        //jx.setValue(13, row, (int) rs.getDouble("discount_value"));
                        if (ma.getDisplayPriceType()==1 && ma.getDiscountType()==1) {
                            //外税＆税抜き額から割引
                            jx.setValue(13, row, (int) rs.getDouble("discount_value_in_tax"));
                        }else {
                            jx.setValue(13, row, (int) rs.getDouble("discount_value"));
                        }
                        // 201805 GB edit end #35807
                        jx.setValue(14, row, (int) rs.getDouble("total_price"));
    //                    if (rs.getString("product_division_name") != null) {
    //                        if (rs.getString("product_division_name").equals("コース解約")) {
    //                            jx.setValue(15, row, -(int) rs.getDouble("money_value"));
    //                        } else {
    //                            jx.setValue(15, row, (int) rs.getDouble("money_value"));
    //                        }
    //                    } else {
                        //IVS_LVTu edit 2019/09/20  SPOS増税対応
                        jx.setValue(15, row, rs.getDouble("tax_rate"));
                            jx.setValue(16, row, (int) rs.getDouble("money_value"));
                        //}
                        jx.setValue(17, row, rs.getString("card_name"));
    //                    if (rs.getString("product_division_name") != null) {
    //                        if (rs.getString("product_division_name").equals("コース解約")) {
    //                            jx.setValue(17, row, -(int) rs.getDouble("card_value"));
    //                        } else {
    //                            jx.setValue(17, row, (int) rs.getDouble("card_value"));
    //                        }
    //                    } else {
                            jx.setValue(18, row, (int) rs.getDouble("card_value"));
                        //}
                        jx.setValue(19, row, rs.getString("ecash_name"));
    //                    if (rs.getString("product_division_name") != null) {
    //                        if (rs.getString("product_division_name").equals("コース解約")) {
    //                            jx.setValue(19, row, -(int) rs.getDouble("ecash_value"));
    //                        } else {
    //                            jx.setValue(19, row, (int) rs.getDouble("ecash_value"));
    //                        }
    //                    } else {
                            jx.setValue(20, row, (int) rs.getDouble("ecash_value"));
                        //}
                        jx.setValue(21, row, rs.getString("gift_name"));
    //                    if (rs.getString("product_division_name") != null) {
    //                        if (rs.getString("product_division_name").equals("コース解約")) {
    //                            jx.setValue(21, row, -(int) rs.getDouble("gift_value"));
    //                        } else {
    //                            jx.setValue(21, row, (int) rs.getDouble("gift_value"));
    //                        }
    //                    } else {
                            jx.setValue(22, row, (int) rs.getDouble("gift_value"));
                        //}

                        if (rs.getString("product_division_name") != null) {
                            if (rs.getString("product_division_name").equals("コース解約")) {
                                jx.setValue(23, row, 0);
                            } else {
                                 jx.setValue(23, row, (int) rs.getDouble("bill_value"));
                            }
                        } else {
                             jx.setValue(23, row, (int) rs.getDouble("bill_value"));
                        }

                        jx.setValue(24, row, rs.getString("technic_staff") != null ? rs.getString("technic_staff") : "");
                        jx.setValue(25, row, rs.getInt("product_division") == 0 ? "" : rs.getBoolean("technic_designated_flag") == true ? "指名" : "フリー");
                        jx.setValue(26, row, rs.getString("reservation_datetime"));
                        jx.setValue(27, row, rs.getString("visit_time"));
                        jx.setValue(28, row, rs.getString("start_time"));
                        jx.setValue(29, row, rs.getString("wait_time") != null ? rs.getString("wait_time") : "");
                        jx.setValue(30, row, rs.getString("stay_time") != null ? rs.getString("stay_time") : "");
                        jx.setValue(31, row, rs.getString("leave_time"));
                        jx.setValue(32, row, rs.getString("progress_day") != null ? rs.getString("progress_day") : "");
                        //IVS_LVTu start edit 2016/06/03 New request #50990
                        //反響項目
                        //IVS start edit 2022/07/12 0624_出力項目の追加
                        //Array arrResponseMame = rs.getArray("response_name");
                        //String ResponseMame1 = "";
                        //String ResponseMame2 = "";
                        //if(arrResponseMame != null) {

                            //String[] ResponseMame = (String[])arrResponseMame.getArray();

                            //for(int i = 0;arrResponseMame != null && i < ResponseMame.length;i ++) {
                                //if(i == 0) {
                                    //ResponseMame1 = ResponseMame[i];
                                //}else {
                                    //ResponseMame2 = ResponseMame2.equals("") == true ? ResponseMame[i] : ("," + ResponseMame[i]);
                                //}
                            //}
                        //}
                        //IVS end edit 2022/07/12 0624_出力項目の追加
                        jx.setValue(33, row, ResponseMame1);
                        jx.setValue(34, row, ResponseMame2);
                        //jx.setValue(32, row, rs.getString("response_name") != null ? rs.getString("response_name").replace("{", "").replace("}", "").replace("\"", "") : "");
                        jx.setValue(35, row, rs.getDate("next_reserve_date") != null ? dateFormat.format(rs.getDate("next_reserve_date")) : "");
                        jx.setValue(36, row, rs.getString("visited_memo") != null ? rs.getString("visited_memo") : "");
                        //IVS NNTUAN START ADD 20131008
                        jx.setValue(37, row, rs.getDate("update_date").toString());
                        //IVS_LVTu end edit 2016/06/03 New request #50990
                    //IVS start add 2022/07/12 0624_出力項目の追加
                    }
                    //IVS end add 2022/07/12 0624_出力項目の追加
                    row++;

                }

                jx.openWorkbook();

            } catch (SQLException e) {
            }

        } else if (outputType == OutputExcelDialog.OUTPUT_CUSTOMER) {
            ArrayList list = new ArrayList();
            list = cusArray;

            //------------------
            // 顧客情報
            //------------------
            if (list.size() > 0) {

                JExcelApi jx = new JExcelApi("条件検索_顧客情報");
                jx.setTemplateFile("/reports/条件検索_顧客情報.xls");

                // ヘッダ
                /*
                jx.setValue(32, 1, jLabel36.getText());
                jx.setValue(33, 1, jLabel37.getText());
                jx.setValue(34, 1, jLabel38.getText());
                jx.setValue(35, 1, jLabel39.getText());*/

                int row = 2;

                // 追加行数セット
                jx.insertRow(row, list.size() - 1);
                //IVS_LVTu start edit 2017/09/01 #24475 [gb]条件検索 使用するフリー項目区分が０の状態で顧客情報のExcelファイルを出力すると、項目がずれて表示される
                if (numFreeHeadingClass > 1) {
                    jx.insertColumn(31, numFreeHeadingClass - 1);
                } else if(numFreeHeadingClass <= 0) {
                    jx.removeColumn(32);
                }
                //IVS_LVTu end edit 2017/09/01 #24475 [gb]条件検索 使用するフリー項目区分が０の状態で顧客情報のExcelファイルを出力すると、項目がずれて表示される
                int columnFreeHeading;
//                if (SystemInfo.getNSystem() == 2) {
//                	columnFreeHeading = 34;
//                } else {
//                	columnFreeHeading = 32;
//                }
                columnFreeHeading = 32;
                for (int i = 0; i < arrOrderByFreeHeading.size(); i++) {
                    String[] freeHeading = arrOrderByFreeHeading.get(i);
                    jx.setValue(columnFreeHeading, 1, freeHeading[1]);
                    columnFreeHeading += 1;
                }
                
                // データセット
                Iterator iter = list.iterator();
                //IVS_LVTu start edit 2015/11/13 New request #44270
                String strBirthday = "";
                while (iter.hasNext()) {
                    SearchResultInfo info = (SearchResultInfo) iter.next();
                    if (info.getBirthday() == null) {
                        strBirthday = "";
                    } else {
                        if (1900 < info.getBirthday().get(JapaneseCalendar.EXTENDED_YEAR)) {
                            strBirthday = DateUtil.format(info.getBirthday().getTime(), "yyyy.MM.dd");
                        } else {
                            strBirthday = DateUtil.format(info.getBirthday().getTime(), "MM.dd");
                        }
                    }
                    
                    jx.setValue(1, row, info.getLastShopName());
                    jx.setValue(2, row, info.getCustomerNo().replaceAll("[ |　]", ""));
                    jx.setValue(3, row, info.getFullCustomerName());
                    jx.setValue(4, row, info.getFullCustomerKana());
                    jx.setValue(5, row, info.getSexString());
                    //jx.setValue(6, row, info.getBirthdayString());
                    jx.setValue(6, row, strBirthday);
                    //IVS_LVTu end edit 2015/11/13 New request #44270
                    jx.setValue(7, row, info.getAge());
                    jx.setValue(8, row, info.getPostalCode());
                    jx.setValue(9, row, info.getAddress1());
                    jx.setValue(10, row, info.getAddress2());
                    jx.setValue(11, row, info.getAddress3());
                    jx.setValue(12, row, info.getAddress4());
                    jx.setValue(13, row, info.getPhoneNumber());
                    jx.setValue(14, row, info.getCellularNumber());
                    jx.setValue(15, row, info.getFaxNumber());
                    jx.setValue(16, row, info.getPcMailAddress());
                    jx.setValue(17, row, info.getCellularMailAddress());
                    jx.setValue(18, row, info.getJobName());
                    jx.setValue(19, row, info.getVisitCount());
                    jx.setValue(20, row, info.getSumTotal());
                    jx.setValue(21, row, info.getLastVisitDate());
                    jx.setValue(22, row, info.getLastStaffName());
                    jx.setValue(23, row, info.getFree());
                    jx.setValue(24, row, info.getNextReserveDate());
                    jx.setValue(25, row, info.getNextReserveType());
                    jx.setValue(26, row, info.getIntroducerNo());
                    jx.setValue(27, row, info.getIntroducerName());
                    jx.setValue(28, row, info.getIntroduceNo());
                    jx.setValue(29, row, info.getIntroduceName());
                    jx.setValue(30, row, info.getFirstComingMotiveName());
                    jx.setValue(31, row, info.getFirstComingMotiveNote());
//                    jx.setValue(32, row, info.getFree1());
//                    jx.setValue(33, row, info.getFree2());
//                    jx.setValue(34, row, info.getFree3());
//                    jx.setValue(35, row, info.getFree4());
                    // IVS change 20140925 
                   columnFreeHeading = 32;
                    for (int i = 0; i < info.getArraryFree().size(); i++) {                        
                        jx.setValue(columnFreeHeading, row, info.getArraryFree().get(i));
                        columnFreeHeading += 1;
                    }
                    String note = "";
                    if (!info.getNote().isEmpty()) {
                        note = info.getNote();
                       // jx.setValue(36, row, note.length() > 32767 ? note.substring(0, 32766) : note);
                         jx.setValue(33+info.getArraryFree().size() -1  , row, note.length() > 32767 ? note.substring(0, 32766) : note);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                   // jx.setValue(37, row, info.getFirstVisitDate() == null ? null : sdf.format(info.getFirstVisitDate().getTime()));
                     jx.setValue(34 +info.getArraryFree().size() -1 , row , info.getFirstVisitDate() == null ? null : sdf.format(info.getFirstVisitDate().getTime()));

                    row++;
                }

                jx.openWorkbook();

            } else {

                return false;
            }

        } else {
            return false;
        }

        return true;

    }

    private boolean outputCSV(String fileName,int outputType) {

         


        if (outputType == OutputExcelDialog.OUTPUT_ACCOUNT) {
            //------------------
            // 精算情報
            //------------------
           
            if (fileName != null) {
                boolean compliteFlg = true;
                BufferedWriter bw = null;
                try {

                    ResultSetWrapper rs = getInfoForAccountOutput(SystemInfo.getConnection());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    //VUINV START EDIT 20140422 Bug #22482
//                  bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Shift_JIS"));
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
                    //VUINV END EDIT 20140422 Bug #22482
                    String line;
                    String header = "";
                    header = header += "\"" + "来店店舗" + "\"" + ",";
                    header = header += "\"" + "伝票No." + "\"" + ",";
                    header = header += "\"" + "会員No." + "\"" + ",";
                    header = header += "\"" + "氏名" + "\"" + ",";
                    header = header += "\"" + "来店日" + "\"" + ",";
                    header = header += "\"" + "主担当" + "\"" + ",";
                    header = header += "\"" + "指名区分" + "\"" + ",";
                    header = header += "\"" + "技術商品区分" + "\"" + ",";
                    header = header += "\"" + "分類/按分" + "\"" + ",";
                    header = header += "\"" + "品名/按分名" + "\"" + ",";
                    header = header += "\"" + "単価/技術料" + "\"" + ",";
                    header = header += "\"" + "数量/パーセント" + "\"" + ",";
                    header = header += "\"" + "明細割引/ポイント" + "\"" + ",";
                    header = header += "\"" + "合計" + "\"" + ",";
                    //IVS_LVTu edit 2019/09/20  SPOS増税対応
                    header = header += "\"" + "税率" + "\"" + ",";
                    header = header += "\"" + "現金" + "\"" + ",";
                    header = header += "\"" + "カード種別" + "\"" + ",";
                    header = header += "\"" + "金額" + "\"" + ",";
                    header = header += "\"" + "電子マネー種別" + "\"" + ",";
                    header = header += "\"" + "金額" + "\"" + ",";
                    header = header += "\"" + "金券・その他種別" + "\"" + ",";
                    header = header += "\"" + "金額" + "\"" + ",";
                    header = header += "\"" + "売掛金" + "\"" + ",";
                    header = header += "\"" + "施術担当・按分担当" + "\"" + ",";
                    header = header += "\"" + "指名区分" + "\"" + ",";
                    header = header += "\"" + "予約時間" + "\"" + ",";
                    header = header += "\"" + "受付時間" + "\"" + ",";
                    header = header += "\"" + "開始時間" + "\"" + ",";
                    header = header += "\"" + "待ち時間" + "\"" + ",";
                    header = header += "\"" + "滞在時間" + "\"" + ",";
                    header = header += "\"" + "退店時間" + "\"" + ",";
                    header = header += "\"" + "前回からの経過日数" + "\"" + ",";
                    //IVS_LVTu start edit 2016/06/03 New request #50990
                    //header = header += "\"" + "反響項目" + "\"" + ",";
                    header = header += "\"" + "反響項目①" + "\"" + ",";
                    header = header += "\"" + "反響項目②" + "\"" + ",";
                    //IVS_LVTu end edit 2016/06/03 New request #50990
                    header = header += "\"" + "次回予約日" + "\"" + ",";
                    header = header += "\"" + "来店メモ" + "\"" + ",";
                    header = header += "\"" + "最終更新日" + "\"" + "";
                    bw.write(header);
                    bw.newLine();
                    while (rs.next()) {
                        line = "";
                        line += "\"" + (rs.getString("shop_name") != null ? rs.getString("shop_name") : "") + "\"" + ",";
                        line += "\"" + (rs.getString("slip_no") != null ? rs.getString("slip_no") : "") + "\"" + ",";
                        line += "\"" + (rs.getString("customer_no") != null ? rs.getString("customer_no") : "") + "\"" + ",";
                        line += "\"" + (rs.getString("customer_name") != null ? rs.getString("customer_name") : "") + "\"" + ",";
                        line += "\"" + (rs.getDate("sales_date") != null ? dateFormat.format(rs.getDate("sales_date")) : "") + "\"" + ",";
                        line += "\"" + (rs.getString("charge_staff") != null ? rs.getString("charge_staff") : "") + "\"" + ",";
                        //IVS_LVTu start edit 2017/09/22 #26374 [gb]精算情報：主担当を指名していても「フリー」と表示される
                        line += "\"" + (rs.getBoolean("charge_designated_flag") == true ? "指名" : "フリー") + "\"" + ",";
                        //IVS_LVTu end edit 2017/09/22 #26374 [gb]精算情報：主担当を指名していても「フリー」と表示される
                        line += "\"" + (rs.getString("product_division_name") != null ? rs.getString("product_division_name") : "") + "\"" + ",";
                        line += "\"" + (rs.getString("category_name") != null ? rs.getString("category_name") : "") + "\"" + ",";
                        line += "\"" + (rs.getString("product_name") != null ? rs.getString("product_name") : "") + "\"" + ",";
                        line += "\"" + (int) rs.getDouble("product_value") + "\"" + ",";
                        line += "\"" + rs.getString("product_num") + (rs.getBoolean("is_proportionally") ? "%" : "") + "\"" + ",";
                        line += "\"" + (int) rs.getDouble("discount_value") + "\"" + ",";
                        line += "\"" + (int) rs.getDouble("total_price") + "\"" + ",";
                        //IVS_LVTu edit 2019/09/20  SPOS増税対応
                        line += "\"" + rs.getDouble("tax_rate") + "\"" + ",";
                        line += "\"" + (int) rs.getDouble("money_value") + "\"" + ",";
                        line += "\"" + (rs.getString("card_name") == null ? "" : rs.getString("card_name")) + "\"" + ",";
                        line += "\"" + (int) rs.getDouble("card_value") + "\"" + ",";
                        line += "\"" + (rs.getString("ecash_name") == null ? "" : rs.getString("ecash_name")) + "\"" + ",";
                        line += "\"" + (int) rs.getDouble("ecash_value") + "\"" + ",";
                        line += "\"" + (rs.getString("gift_name") == null ? "" : rs.getString("gift_name")) + "\"" + ",";
                        line += "\"" + (int) rs.getDouble("gift_value") + "\"" + ",";
                        if (rs.getString("product_division_name") != null) {
                            if (rs.getString("product_division_name").equals("コース解約")) {
                                line += "\"" + (int) rs.getDouble("product_value") + "\"" + ",";
                            } else {
                                line += "\"" + (int) rs.getDouble("bill_value") + "\"" + ",";
                            }
                        } else {
                            line += "\"" + (int) rs.getDouble("bill_value") + "\"" + ",";
                        }
                        line += "\"" + (rs.getString("technic_staff") != null ? rs.getString("technic_staff") : "") + "\"" + ",";
                        line += "\"" + (rs.getInt("product_division") == 0 ? "" : rs.getBoolean("technic_designated_flag") == true ? "指名" : "フリー") + "\"" + ",";
                        line += "\"" + rs.getString("reservation_datetime") + "\"" + ",";
                        line += "\"" + rs.getString("visit_time") + "\"" + ",";
                        line += "\"" + rs.getString("start_time") + "\"" + ",";
                        line += "\"" + (rs.getString("wait_time") != null ? rs.getString("wait_time") : "") + "\"" + ",";
                        line += "\"" + (rs.getString("stay_time") != null ? rs.getString("stay_time") : "") + "\"" + ",";
                        line += "\"" + rs.getString("leave_time") + "\"" + ",";
                        line += "\"" + (rs.getString("progress_day") != null ? rs.getString("progress_day") : "") + "\"" + ",";
                        //IVS_LVTu start edit 2016/06/03 New request #50990
                        //反響項目
                        Array arrResponseMame = rs.getArray("response_name");
                        String ResponseMame1 = "";
                        String ResponseMame2 = "";
                        if(arrResponseMame != null) {

                            String[] ResponseMame = (String[])arrResponseMame.getArray();

                            for(int i = 0;arrResponseMame != null && i < ResponseMame.length;i ++) {
                                if(i == 0) {
                                    ResponseMame1 = ResponseMame[i];
                                }else {
                                    ResponseMame2 = ResponseMame2.equals("") == true ? ResponseMame[i] : ("," + ResponseMame[i]);
                                }
                            }
                        }
                        line += "\"" + (ResponseMame1) + "\"" + ",";
                        line += "\"" + (ResponseMame2) + "\"" + ",";
                        //line += "\"" + (rs.getString("response_name") != null ? rs.getString("response_name").replace("{", "").replace("}", "").replace("\"", "") : "") + "\"" + ",";
                        //IVS_LVTu end edit 2016/06/03 New request #50990
                        
                        line += "\"" + (rs.getDate("next_reserve_date") != null ? dateFormat.format(rs.getDate("next_reserve_date")) : "") + "\"" + ",";
                        line += "\"" + (rs.getString("visited_memo") != null ? rs.getString("visited_memo") : "") + "\"" + ",";
                        line += "\"" + rs.getDate("update_date").toString() + "\"";
                        bw.write(line);
                        bw.newLine();
                    }
                    MessageDialog.showMessageDialog(this,
                        "出力しました",
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);

                } catch (FileNotFoundException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    compliteFlg = false;
                } catch (IOException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    compliteFlg = false;
                } catch (SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    compliteFlg = false;
                } finally {
                    try {
                        if (bw != null) {
                            bw.flush();
                            bw.close();
                        }
                    } catch (Exception e) {
                    }
                }
                return compliteFlg;
            }
        } else if (outputType == OutputExcelDialog.OUTPUT_CUSTOMER) {

            //------------------
            // 顧客情報
            //------------------
            //fileName = getSaveCSVFileName("条件検索_顧客情報");
            if (fileName != null) {
                if (outputType == OutputExcelDialog.OUTPUT_CUSTOMER) {
                    if (!this.setCustomerArrayList(SEND_TYPE_EXCEL, outputType)) {
                        return false;
                    }
                }
                ArrayList list = new ArrayList();
                list = cusArray;
                if (list.size() > 0) {
                    boolean compliteFlg = true;
                    BufferedWriter bw = null;
                    try {
                        //VUINV START EDIT 20140422 Bug #22482
//                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Shift_JIS"));
                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
                        //VUINV END EDIT 20140422 Bug #22482
                        String header = "";
                        String line;
                        Iterator iter = list.iterator();
                        SearchResultInfo info = new SearchResultInfo();
                        header = header += "\"" + "最終来店店舗" + "\"" + ",";
                        header = header += "\"" + "会員No." + "\"" + ",";
                        header = header += "\"" + "氏名" + "\"" + ",";
                        header = header += "\"" + "ふりがな" + "\"" + ",";
                        header = header += "\"" + "性別" + "\"" + ",";
                        header = header += "\"" + "誕生日" + "\"" + ",";
                        header = header += "\"" + "年齢" + "\"" + ",";
                        header = header += "\"" + "郵便番号" + "\"" + ",";
                        header = header += "\"" + "都道府県" + "\"" + ",";
                        header = header += "\"" + "市町区村" + "\"" + ",";
                        header = header += "\"" + "町域・番地" + "\"" + ",";
                        header = header += "\"" + "マンション名等" + "\"" + ",";
                        header = header += "\"" + "電話番号" + "\"" + ",";
                        header = header += "\"" + "携帯番号" + "\"" + ",";
                        header = header += "\"" + "FAX番号" + "\"" + ",";
                        header = header += "\"" + "PCメール" + "\"" + ",";
                        header = header += "\"" + "携帯メール" + "\"" + ",";
                        header = header += "\"" + "職業" + "\"" + ",";
                        header = header += "\"" + "来店回数" + "\"" + ",";
                        header = header += "\"" + "累計売上金額" + "\"" + ",";
                        header = header += "\"" + "前回来店日" + "\"" + ",";
                        header = header += "\"" + "前回主担当" + "\"" + ",";
                        header = header += "\"" + "指名区分" + "\"" + ",";
                        header = header += "\"" + "次回予約日" + "\"" + ",";
                        header = header += "\"" + "予約種別" + "\"" + ",";
                        header = header += "\"" + "紹介元会員No." + "\"" + ",";
                        header = header += "\"" + "紹介元会員名" + "\"" + ",";
                        header = header += "\"" + "紹介先会員No." + "\"" + ",";
                        header = header += "\"" + "紹介先会員名" + "\"" + ",";
                        header = header += "\"" + "初回来店動機" + "\"" + ",";
                        header = header += "\"" + "初回来店動機メモ" + "\"" + ",";
                        //IVS_LVTu start edit 2016/03/04 Bug #48985
//                        header = header += "\"" + "xxx来店動機x" + "\"" + ",";
//                        header = header += "\"" + "アクセス方法" + "\"" + ",";
//                        header = header += "\"" + "髪質" + "\"" + ",";
//                        header = header += "\"" + "アレルギー" + "\"" + ",";
                        for (int i = 0; i < arrOrderByFreeHeading.size(); i++) {
                            String[] freeHeading = arrOrderByFreeHeading.get(i);
                            header = header += "\"" + freeHeading[1] + "\"" + ",";
                        }
                        header = header += "\"" + "備考" + "\"" + ",";
                        header = header += "\"" + "初回来店日" + "\"" + "";
                        bw.write(header);
                        bw.newLine();
                        String strBirthday = "";
                        while (iter.hasNext()) {
                            info = (SearchResultInfo) iter.next();
                            if (info.getBirthday() == null) {
                                strBirthday = "";
                            } else {
                                if (1900 < info.getBirthday().get(JapaneseCalendar.EXTENDED_YEAR)) {
                                    strBirthday = DateUtil.format(info.getBirthday().getTime(), "yyyy.MM.dd");
                                } else {
                                    strBirthday = DateUtil.format(info.getBirthday().getTime(), "MM.dd");
                                }
                            }
                            
                            line = "";
                            line += "\"" + (info.getLastShopName()) + "\"" + ",";
                            line += "\"" + (info.getCustomerNo().replaceAll("[ |　]", "")) + "\"" + ",";
                            line += "\"" + (info.getFullCustomerName()) + "\"" + ",";
                            line += "\"" + (info.getFullCustomerKana()) + "\"" + ",";
                            line += "\"" + (info.getSexString()) + "\"" + ",";
                            //line += "\"" + (info.getBirthdayString()) + "\"" + ",";
                            line += "\"" + (strBirthday) + "\"" + ",";
                            line += "\"" + (info.getAge()) + "\"" + ",";
                            line += "\"" + (info.getPostalCode()) + "\"" + ",";
                            line += "\"" + (info.getAddress1()) + "\"" + ",";
                            line += "\"" + (info.getAddress2()) + "\"" + ",";
                            line += "\"" + (info.getAddress3()) + "\"" + ",";
                            line += "\"" + (info.getAddress4()) + "\"" + ",";
                            line += "\"" + (info.getPhoneNumber()) + "\"" + ",";
                            line += "\"" + (info.getCellularNumber()) + "\"" + ",";
                            line += "\"" + (info.getFaxNumber()) + "\"" + ",";
                            line += "\"" + (info.getPcMailAddress()) + "\"" + ",";
                            line += "\"" + (info.getCellularMailAddress()) + "\"" + ",";
                            line += "\"" + (info.getJobName()) + "\"" + ",";
                            line += "\"" + (info.getVisitCount()) + "\"" + ",";
                            line += "\"" + (info.getSumTotal()) + "\"" + ",";
                            line += "\"" + (info.getLastVisitDate()) + "\"" + ",";
                            line += "\"" + (info.getLastStaffName()) + "\"" + ",";
                            line += "\"" + (info.getFree()) + "\"" + ",";
                            line += "\"" + (info.getNextReserveDate()) + "\"" + ",";
                            line += "\"" + (info.getNextReserveType()) + "\"" + ",";
                            line += "\"" + (info.getIntroducerNo()) + "\"" + ",";
                            line += "\"" + (info.getIntroducerName()) + "\"" + ",";
                            line += "\"" + (info.getIntroduceNo()) + "\"" + ",";
                            line += "\"" + (info.getIntroduceName() == null ? "" : info.getIntroduceName()) + "\"" + ",";
                            line += "\"" + (info.getFirstComingMotiveName() == null ? "" : info.getFirstComingMotiveName()) + "\"" + ",";
                            line += "\"" + (info.getFirstComingMotiveNote() == null ? "" : info.getFirstComingMotiveNote()) + "\"" + ",";
    //                            line += "\"" + (info.getFree1() == null ? "" : info.getFree1()) + "\"" + ",";
    //                            line += "\"" + (info.getFree2() == null ? "" : info.getFree2()) + "\"" + ",";
    //                            line += "\"" + (info.getFree3() == null ? "" : info.getFree3()) + "\"" + ",";
    //                            line += "\"" + (info.getFree4() == null ? "" : info.getFree4()) + "\"" + ",";
                            for (int i = 0; i < info.getArraryFree().size(); i++) {                        
                                line += "\"" +  info.getArraryFree().get(i) + "\"" + ",";
                            }
                            //IVS_LVTu end edit 2016/03/04 Bug #48985
                            String note = "";
                            if (!info.getNote().isEmpty()) {
                                note = info.getNote();
                                note = note.replace("\n", "");
                                
                            }
                            line += "\"" + (note.length() > 32767 ? note.substring(0, 32766) : note) + "\"" + ",";
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                            line += "\"" + (info.getFirstVisitDate() == null ? "" : sdf.format(info.getFirstVisitDate().getTime())) + "\"";

                            bw.write(line);
                            bw.newLine();
                        }
                        MessageDialog.showMessageDialog(this,
                        "出力しました",
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);

                    } catch (FileNotFoundException e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                        compliteFlg = false;
                    } catch (IOException e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                        compliteFlg = false;

                    } finally {
                        try {
                            if (bw != null) {
                                bw.flush();
                                bw.close();
                            }
                        } catch (Exception e) {
                        }
                    }
                    return compliteFlg;

                }
            }

        } else {
            return false;
        }

        return true;

    }
    
     private boolean outputDigitalPostCSV(String fileName) {
            //------------------
            // 顧客情報
            //------------------
            //fileName = getSaveCSVFileName("条件検索_顧客情報");
            if (fileName != null) {
                
                ArrayList list = new ArrayList();
                list = cusArray;
                if (list.size() > 0) {
                    boolean compliteFlg = true;
                    BufferedWriter bw = null;
                    try {
                        
                        try {
                            FileOutputStream fos  = new FileOutputStream(fileName);
                            bw = new BufferedWriter(new OutputStreamWriter(fos));
                        }catch(Exception e) {
                              MessageDialog.showMessageDialog(this,
                                     "CSVファイルが使用中です。ＣＳＶファイルを閉じてください。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                              return false;
                        }
                        
                        String header = "";
                        String line;
                        Iterator iter = list.iterator();
                        SearchResultInfo info = new SearchResultInfo();
                        header = header += "\"" + "姓" + "\"" + ",";
                        header = header += "\"" + "名" + "\"" + ",";
                        header = header += "\"" + "敬称" + "\"" + ",";
                        header = header += "\"" + "郵便番号" + "\"" + ",";
                        header = header += "\"" + "都道府県" + "\"" + ",";
                        header = header += "\"" + "市区町村" + "\"" + ",";
                        header = header += "\"" + "町名番地" + "\"" + ",";
                        header = header += "\"" + "ビル・マンション名" + "\"" + "";
                       
                        
                        bw.write(header);
                        bw.newLine();
                        int count = 0;
                        while (iter.hasNext()) {
                            info = (SearchResultInfo) iter.next();
                            if((!info.getCustomerName(0).equals("") && info.getCustomerName(0)!=null)
                            && (!info.getCustomerName(1).equals("") && info.getCustomerName(1)!=null) 
                            && (!info.getPostalCode().equals("") && info.getPostalCode()!=null)
                            && (!info.getAddress1().equals("") && info.getAddress1()!=null)    
                            && (!info.getAddress2().equals("") && info.getAddress2()!=null)    
                            && (!info.getAddress3().equals("") && info.getAddress3()!=null) 
                                    ) {
                                    line = "";
                                    line += "\"" + (info.getCustomerName(0)) + "\"" + ",";
                                    line += "\"" + (info.getCustomerName(1)) + "\"" + ",";
                                    line += "\"" + ("様") + "\"" + ",";
                                    line += "\"" + (info.getPostalCode()) + "\"" + ",";
                                    line += "\"" + (info.getAddress1()) + "\"" + ",";

                                    line += "\"" + (info.getAddress2()) + "\"" + ",";

                                    line += "\"" + (info.getAddress3()) + "\"" + ",";
                                    line += "\"" + (info.getAddress4()) + "\"" + "";

                                    bw.write(line);
                                    bw.newLine();
                                    count ++;
                            }
                        }
                        if(count == 0) {
                            MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(8000),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        MessageDialog.showMessageDialog(this,
                       "CSVファイルを出力いたしました。\n"
                        +"ブラウザが開きますので引き続きウェブページで処理を進めてください。",
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);

                    } catch (FileNotFoundException e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                        compliteFlg = false;
                    } catch (IOException e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                        compliteFlg = false;
                        
                    } finally {
                        try {
                            if (bw != null) {
                                bw.flush();
                                bw.close();
                            }
                        } catch (Exception e) {
                        }
                    }
                    return compliteFlg;

                }
            }

       

        return true;

    }
   
    private void dateType0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateType0ActionPerformed
        jLabel2.setText("来店日付");
        this.visitCountFrom.setEnabled(true);
        this.visitCountTo.setEnabled(true);
        this.visitCountType0.setEnabled(true);
        this.visitCountType1.setEnabled(true);
        this.setItemEnabled(true);
    }//GEN-LAST:event_dateType0ActionPerformed

    private void dateType1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateType1ActionPerformed
        jLabel2.setText("最終来店日");
        this.visitCountFrom.setEnabled(true);
        this.visitCountTo.setEnabled(true);
        this.visitCountType0.setEnabled(true);
        this.visitCountType0.setSelected(true);
        this.visitCountType1.setEnabled(false);
        this.setItemEnabled(true);
    }//GEN-LAST:event_dateType1ActionPerformed

        private void dateType2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateType2ActionPerformed
            jLabel2.setText("次回予約日");
            this.visitCountFrom.setEnabled(true);
            this.visitCountTo.setEnabled(true);
            this.visitCountType0.setEnabled(true);
            this.visitCountType0.setSelected(true);
            this.visitCountType1.setEnabled(false);
            this.setItemEnabled(false);
        }//GEN-LAST:event_dateType2ActionPerformed

    /**
     * 主担当者をセットする。
     */
    private void setChargeStaff(String staffNo) {
        chargeStaff.setSelectedIndex(0);

        for (int i = 1; i < chargeStaff.getItemCount(); i++) {
            if (((MstStaff) chargeStaff.getItemAt(i)).getStaffNo().equals(staffNo)) {
                chargeStaff.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * 指定したスタッフＩＤをスタッフ選択用JComboBoxで選択状態にする。
     *
     * @param staffCombo スタッフ選択用JComboBox
     * @param staffID スタッフＩＤ
     */
    private void setStaff(JComboBox staffCombo, Integer staffID) {
        for (int i = 0; i < staffCombo.getItemCount(); i++) {
            if (((MstStaff) staffCombo.getItemAt(i)).getStaffID() == staffID) {
                staffCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * ワイルドカードメール送信ボタン押下イベント
     */
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearButtonActionPerformed
	{//GEN-HEADEREND:event_clearButtonActionPerformed
            this.clear();
	}//GEN-LAST:event_clearButtonActionPerformed

	private void mailButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mailButtonActionPerformed
	{//GEN-HEADEREND:event_mailButtonActionPerformed

            // メール作成をクリック
            if (this.setCustomerArrayList(SEND_TYPE_E_MAIL)) {
                
                if (!this.optimizeSelectedArray(-1)) {
                    return;
                }

                SystemInfo.getLogger().log(Level.INFO, "メール作成");

                HairCommonMailPanel rcmp = new HairCommonMailPanel(target.getSelectedItem(), selectedArray);

                rcmp.setOpener(this);
                this.setVisible(false);
                parentFrame.changeContents(rcmp);
            }
	}//GEN-LAST:event_mailButtonActionPerformed

	private void sealButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sealButtonActionPerformed
	{//GEN-HEADEREND:event_sealButtonActionPerformed
            // 宛名ラベル印刷をクリック
            if (this.setCustomerArrayList(SEND_TYPE_DIRECT_MAIL)) {

                if (!this.optimizeSelectedArray(4)) {
                    return;
                }

                SystemInfo.getLogger().log(Level.INFO, "宛名ラベル印刷");

                // 連名印刷の事前処理
                this.jointNamePrint();

                PrintLabelPanel plp = new PrintLabelPanel(target.getSelectedItem(), selectedArray);
                plp.setOpener(this);
                this.setVisible(false);
                parentFrame.changeContents(plp);
            }
	}//GEN-LAST:event_sealButtonActionPerformed

	private void postcardButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_postcardButtonActionPerformed
	{//GEN-HEADEREND:event_postcardButtonActionPerformed
            // ハガキ印刷をクリック
            if (this.setCustomerArrayList(SEND_TYPE_DIRECT_MAIL)) {

                if (!this.optimizeSelectedArray(4)) {
                    return;
                }

                SystemInfo.getLogger().log(Level.INFO, "はがき印刷");

                // 連名印刷の事前処理
                this.jointNamePrint();

                PrintPostcardPanel ppp = new PrintPostcardPanel(target.getSelectedItem(), selectedArray);
                ppp.setOpener(this);
                this.setVisible(false);
                parentFrame.changeContents(ppp);
            }
	}//GEN-LAST:event_postcardButtonActionPerformed

    private void jointNamePrint() {

        String msg = "連名印刷を行いますか？\n\n";
        msg += "※ 連名印刷を行う場合は、家族登録されている方が1人でも該当すると\n";
        msg += "　　全ての家族が連名となります。\n\n";
        msg += "※ 家族登録されていても住所が未登録の場合は、\n";
        msg += "　　連名印刷から除外されますのでご注意ください。\n\n";

        if (MessageDialog.showYesNoDialog(
                this,
                msg,
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }

        // 顧客IDリスト取得
        StringBuilder customerIdList = new StringBuilder(1000);
        for (MstCustomer mc : selectedArray) {
            customerIdList.append(",");
            customerIdList.append(mc.getCustomerID().toString());
        }

        // 除外する顧客IDリスト
        ArrayList<Integer> removeIdList = new ArrayList<Integer>();

        for (MstCustomer mc : selectedArray) {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     mc.*");
            sql.append(" from");
            sql.append("     mst_family mf");
            sql.append("         inner join mst_customer mc");
            sql.append("                 on mf.family_id = mc.customer_id");
            sql.append(" where");
            sql.append("         mc.delete_date is null");
            sql.append("     and mf.customer_id = " + SQLUtil.convertForSQL(mc.getCustomerID()));
            sql.append("     and mf.family_id in (" + customerIdList.substring(1).toString() + ")");
            try {
                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    MstCustomer family = new MstCustomer();
                    family.setData(rs);
                    mc.getFamilyList().add(family);
                    removeIdList.add(family.getCustomerID());
                }
                rs.close();
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        Iterator<MstCustomer> iter = selectedArray.iterator();
        while (iter.hasNext()) {
            MstCustomer mc = iter.next();
            if (removeIdList.contains(mc.getCustomerID())) {
                iter.remove();
            }
        }
    }

	private void prefectureActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_prefectureActionPerformed
	{//GEN-HEADEREND:event_prefectureActionPerformed
            this.setCity();
	}//GEN-LAST:event_prefectureActionPerformed

        private void customerNo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNo1FocusLost
            if (customerNo2.getText().length() == 0) {
                customerNo2.setText(customerNo1.getText());
            }
        }//GEN-LAST:event_customerNo1FocusLost

        private void customerNo1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerNo1KeyPressed
            if (evt.getKeyCode() == evt.VK_ENTER) {
                customerNo2.requestFocusInWindow();
            }
        }//GEN-LAST:event_customerNo1KeyPressed

        private void dateType3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateType3ActionPerformed
            jLabel2.setText("初回来店日");
            this.visitCountFrom.setText("");
            this.visitCountFrom.setEnabled(false);
            this.visitCountTo.setText("");
            this.visitCountTo.setEnabled(false);
            this.visitCountType0.setEnabled(false);
            this.visitCountType1.setEnabled(false);
            this.setItemEnabled(true);
        }//GEN-LAST:event_dateType3ActionPerformed

        private void condRegistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_condRegistButtonActionPerformed

            condRegistButton.setCursor(null);
            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                registCondition();
                condTemplateButton.setEnabled(DataCondition.getList(SystemInfo.getCurrentShop()).size() > 0);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

        }//GEN-LAST:event_condRegistButtonActionPerformed

        private void condTemplateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_condTemplateButtonActionPerformed

            // 条件検索テンプレート画面
            condTemplateButton.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                SystemInfo.getLogger().log(Level.INFO, "条件検索テンプレート");

                SearchConditionDialog sc = new SearchConditionDialog(parentFrame);
                sc.setVisible(true);

                if (sc.getSelectedCondition() != null) {

                    // 技術・商品の設定初期化
                    hint.setText("");
                    this.initProductClasses();

                    for (DataConditionDetail dd : DataConditionDetail.getList(sc.getSelectedCondition())) {

                        switch (dd.getItemNo()) {

                            case 100:
                                if (dd.getItemValue().equals("0")) {
                                    dateType0.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    dateType1.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    dateType2.setSelected(true);
                                }
                                if (dd.getItemValue().equals("3")) {
                                    dateType3.setSelected(true);
                                }
                                break;

                            case 110:
                                visitDateFrom.setDate(dd.getItemValue());
                                break;
                            case 112:
                                visitDateTo.setDate(dd.getItemValue());
                                break;

                            case 120:
                                if (dd.getItemValue().equals("0")) {
                                    female.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    male.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    both.setSelected(true);
                                }
                                break;

                            case 130:
                                ageFrom.setText(dd.getItemValue());
                                break;
                            case 132:
                                ageTo.setText(dd.getItemValue());
                                break;

                            case 140:
                                setSelectedComboBox(birthMonth, dd.getItemValue());
                                break;

                            case 142:
                                birthdayFrom.setDate(dd.getItemValue());
                                break;
                            case 144:
                                birthdayTo.setDate(dd.getItemValue());
                                break;

                            case 150:
                                if (dd.getItemValue().equals("0")) {
                                    sendMailOK.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    sendMailNG.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    sendMailAll.setSelected(true);
                                }
                                break;

                            case 160:
                                if (dd.getItemValue().equals("0")) {
                                    sendDmOK.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    sendDmNG.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    sendDmAll.setSelected(true);
                                }
                                break;

                            case 170:
                                if (dd.getItemValue().equals("0")) {
                                    callFlagOK.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    callFlagNG.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    callFlagAll.setSelected(true);
                                }
                                break;

                            case 180:
                                visitCountFrom.setText(dd.getItemValue());
                                break;
                            case 182:
                                visitCountTo.setText(dd.getItemValue());
                                break;

                            case 190:
                                if (dd.getItemValue().equals("0")) {
                                    visitCountType0.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    visitCountType1.setSelected(true);
                                }
                                break;

                            case 200:
                                priceFrom.setText(dd.getItemValue());
                                break;
                            case 202:
                                priceTo.setText(dd.getItemValue());
                                break;

                            case 210:
                                isIntroducer.setSelected(dd.getItemValue().equals("1"));
                                break;
                            case 212:
                                isIntroduced.setSelected(dd.getItemValue().equals("1"));
                                break;

                            case 220:
                                customerNo1.setText(dd.getItemValue());
                                break;
                            case 222:
                                customerNo2.setText(dd.getItemValue());
                                break;

                            case 230:
                                customerKana1.setText(dd.getItemValue());
                                break;
                            case 232:
                                customerKana2.setText(dd.getItemValue());
                                break;

                            case 240:
                                customerName1.setText(dd.getItemValue());
                                break;
                            case 242:
                                customerName2.setText(dd.getItemValue());
                                break;

                            case 250:
                                if (dd.getItemValue().equals("0")) {
                                    addAll.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    addEnd.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    addYet.setSelected(true);
                                }
                                break;

                            case 260:
                                postalCode.setText(dd.getItemValue());
                                break;

                            case 270:
                                setSelectedComboBox(prefecture, dd.getItemValue());
                                break;
                            case 280:
                                setSelectedComboBox(city, dd.getItemValue());
                                break;

                            case 290:
                                setSelectedComboBox(job, dd.getItemValue());
                                break;

                            case 300:
                                memo.setText(dd.getItemValue());
                                break;

                            case 310:
                                setSelectedComboBox(firstComingMotive, dd.getItemValue());
                                break;

                           /* case 320:
                                setSelectedComboBox(free1, dd.getItemValue());
                                break;
                            case 330:
                                setSelectedComboBox(free2, dd.getItemValue());
                                break;
                            case 340:
                                setSelectedComboBox(free3, dd.getItemValue());
                                break;
                            case 350:
                                setSelectedComboBox(free4, dd.getItemValue());
                                break;*/
                             //vtbphuong start edit 20140627     
                            case 320: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 1) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 1) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                }
                            case 330: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 2) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 2) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                } 
                            case 340: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 3) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 3) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                } 
                            case 350: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 4) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 4) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                }   
                             case 460: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 5) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 5) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                } 
                             case 470: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 6) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 6) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                }   
                             case 480: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 7) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 7) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                }
                              case 490: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 8) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 8) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                }  
                            case 500: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 9) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 9) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                } 
                            case 510: 
                                for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                                    //mfhsup.setSelectedInit();            
                                    if (mfhsup.getFreeHeadingClassID() == 10) { 
                                        mfhsup.setSelectedComboBox(dd.getItemValue()); break;
                                    }
                                }
                                for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                                    if (fhPanelText.getFreeHeadingClassID() == 10) {
                                         fhPanelText.setFreeHeadingText(dd.getItemValue()); break;
                                    }
                                } 
                            //vtbphuong end edit 20140627 

                            case 360:
                                setSelectedComboBox(chargeStaff, dd.getItemValue());
                                break;

                            case 370:
                                if (dd.getItemValue().equals("0")) {
                                    chargeStaffAll.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    chargeStaffNamed.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    chargeStaffFree.setSelected(true);
                                }
                                break;

                            case 420:
                                setSelectedComboBox(responseItem, dd.getItemValue());
                                break;

                            case 430:
                                if (dd.getItemValue().equals("0")) {
                                    pcMailExists.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    pcMailNotExists.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    pcMailAll.setSelected(true);
                                }
                                break;

                            case 440:
                                if (dd.getItemValue().equals("0")) {
                                    cellularMailExists.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    cellularMailNotExists.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    cellularMailAll.setSelected(true);
                                }
                                break;

                            case 450:
                                if (dd.getItemValue().equals("0")) {
                                    sosiaGearExists.setSelected(true);
                                }
                                if (dd.getItemValue().equals("1")) {
                                    sosiaGearNotExists.setSelected(true);
                                }
                                if (dd.getItemValue().equals("2")) {
                                    sosiaGearAll.setSelected(true);
                                }
                                break;
                        }

                        if (dd.getItemNo() == 10000) {
                            if (dd.getItemValue().equals("OR")) {
                                techItemOR.setSelected(true);
                            } else {
                                techItemAND.setSelected(true);
                            }
                        }

                        // 技術
                        if (11000 <= dd.getItemNo() && dd.getItemNo() < 12000) {
                            String[] strAry = dd.getItemValue().split("\\$\\$");
                            if (strAry[0].equals("0")) {
                                condTechClass.put(Integer.parseInt(strAry[1]), new MapProduct(strAry[2], true, 0));
                            } else {
                                condTech.put(Integer.parseInt(strAry[1]), new MapProduct(strAry[2], true, Integer.parseInt(strAry[0])));
                            }
                            hint.append(strAry[2]);
                            hint.append("\n");
                        }

                        // 商品
                        if (12000 <= dd.getItemNo() && dd.getItemNo() < 13000) {
                            String[] strAry = dd.getItemValue().split("\\$\\$");
                            if (strAry[0].equals("0")) {
                                condItemClass.put(Integer.parseInt(strAry[1]), new MapProduct(strAry[2], true, 0));
                            } else {
                                condItem.put(Integer.parseInt(strAry[1]), new MapProduct(strAry[2], true, Integer.parseInt(strAry[0])));
                            }
                            hint.append(strAry[2]);
                            hint.append("\n");
                        }

                    }

                    // 技術・商品の設定を反映する
                    this.productDivisionStateChanged(null);

                }
                sc = null;

                condTemplateButton.setEnabled(DataCondition.getList(SystemInfo.getCurrentShop()).size() > 0);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

        }//GEN-LAST:event_condTemplateButtonActionPerformed

        private void technicClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_technicClassMouseReleased
            this.showProducts(technicClass, technic);
}//GEN-LAST:event_technicClassMouseReleased

        private void technicClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_technicClassKeyReleased
            this.showProducts(technicClass, technic);
}//GEN-LAST:event_technicClassKeyReleased

        private void itemClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemClassMouseReleased
            this.showProducts(itemClass, item);
}//GEN-LAST:event_itemClassMouseReleased

        private void itemClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemClassKeyReleased
            this.showProducts(itemClass, item);
}//GEN-LAST:event_itemClassKeyReleased

        private void productDivisionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_productDivisionStateChanged
            if (productDivision.getSelectedIndex() == 0) {
                this.showProducts(technicClass, technic);
            } else if (productDivision.getSelectedIndex() == 1) {
                this.showProducts(itemClass, item);
            }
}//GEN-LAST:event_productDivisionStateChanged

        private void technicPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_technicPropertyChange
            this.setTechItemChecked(technic);
        }//GEN-LAST:event_technicPropertyChange

        private void itemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_itemPropertyChange
            this.setTechItemChecked(item);
        }//GEN-LAST:event_itemPropertyChange

    private void btnOutputCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutputCsvActionPerformed


        // 次回予約日で検索する場合は日付入力必須
        if (dateType2.isSelected()) {
            if (visitDateFrom.getDateStr() == null || visitDateTo.getDateStr() == null) {
                MessageDialog.showMessageDialog(this,
                        "次回予約日で検索する場合は、日付を設定してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        OutputExcelDialog dialg = new OutputExcelDialog(this.parentFrame, true, "CSV出力",true);
        SwingUtil.moveCenter(dialg);
        dialg.setVisible(true);
        //IVS_LVTU start add 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        this.isCustomerNo_0 = dialg.getIsCustomerNo_0();
        //IVS_LVTU end add 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        int outputType = dialg.getOutputType();
        if (outputType < 0) {
            return;
        }

        try {

             String fileName = dialg.getOutPutFileString();
             if(fileName==null){
                 
                 return ;
             }
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (!outputCSV(fileName,outputType)) {
                return;
            }
             
			

        } catch (Exception e) {

            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           
            
        }

    }//GEN-LAST:event_btnOutputCsvActionPerformed

    private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed

        if (chargeStaff.getSelectedIndex() > 0) {
            chargeStaffNo.setText(((MstStaff) chargeStaff.getSelectedItem()).getStaffNo());
        } else {
            chargeStaffNo.setText("");
        }

        if (!this.isShowing()) {
            return;
        }
    }//GEN-LAST:event_chargeStaffActionPerformed

    private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chargeStaffNoFocusLost

        if (!chargeStaffNo.getText().equals("")) {
            this.setChargeStaff(chargeStaffNo.getText());
        } else {
            this.chargeStaff.setSelectedIndex(0);
        }
    }//GEN-LAST:event_chargeStaffNoFocusLost

    private void DigitalPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DigitalPostActionPerformed
        // TODO add your handling code here:
        //CSVファイルを出力する
        try {
            File f1 = new File("C:\\sosia\\csv\\customer.csv");
            if(f1.exists()) {
                f1.delete();
            }
            File f2 = new File("C:\\sosia\\digitalpost\\index.html");
            if(f2.exists()) {
                f2.delete();
            }
        }catch(Exception ex) {
            
        }
        boolean flg = this.setCustomerDigitalPost(SEND_TYPE_EXCEL, 0);
        if(flg) {
            if (dateType2.isSelected()) {
                if (visitDateFrom.getDateStr() == null || visitDateTo.getDateStr() == null) {
                    MessageDialog.showMessageDialog(this,
                            "次回予約日で検索する場合は、日付を設定してください。",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            //CSVファイル名
            String fileName = "C:\\sosia\\csv";//System.getProperty("user.home") + "\\Desktop\\";
            File dir = new File(fileName);
            if(!dir.exists()) {
               dir.mkdir();
            }


            fileName += "\\customer.csv";
            //fileName += "_" + String.format("%1$tY%1$tm%1$td%2$ts", new GregorianCalendar(), new java.util.Date()) + ".csv";

            try {
                 if(fileName==null){
                     return ;
                 }
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (!outputDigitalPostCSV(fileName)) {
                    return;
                }

            } catch (Exception e) {

                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


            }

            //ｈｔｍｌファイル名
            //String htmlFileName = "C:\\sosia\\csv\\pmasp"+String.format("%1$tY%1$tm%1$td%2$ts", new GregorianCalendar(), new java.util.Date()) +".html";
            String htmlDirPath = "C:\\sosia\\digitalpost";
            File htmlDir = new File(htmlDirPath);
            if(!htmlDir.exists()) {
                htmlDir.mkdir();
            }
            String htmlFileName = "C:\\sosia\\digitalpost\\index.html";

            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFileName),"UTF-8"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(HairMailSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(HairMailSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

            String companyId = SystemInfo.getDatabase();
            companyId = companyId.substring(9);//pos_hair_○○
            String authSrc = "oqz9cdy7m0006GW3ivAvI9xZ";
            String authHashPlantext = "";
            try {

                /*bw.write(" <!DOCTYPE HTML PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'>");
                bw.newLine();
                bw.write(" <html xml:lang='ja' xmlns='http://www.w3.org/1999/xhtml' lang='ja'> ");
                bw.newLine();
                bw.write(" <head> ");
                bw.newLine();
                bw.write("     <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /> ");
                bw.newLine();
                bw.write( "     <title>連携:ログイン連携</title> ");
                bw.newLine();
                bw.write( " </head> ");
                bw.newLine();
                bw.write(" <body> ");
                bw.newLine();
                bw.write( "     <H1>ＣＳＶファイルはC:\\sosia\\csvに選択してください。</H1> ");
                bw.newLine();
                bw.write( "     <form method='post' enctype='multipart/form-data' action='http://sosiadm.digitalpost.jp/pmasp' accept-charset='UTF-8'  > ");
                bw.newLine();
                bw.write( "         <input type='hidden' name='userId' value='"+SystemInfo.getLoginID()+"' /><br/> ");
                bw.newLine();
                bw.write("         <input type='hidden' name='companyId' value='"+companyId+"'  /><br/> ");
                bw.newLine();
                bw.write( "        <input type='hidden' name='authSrc' value='"+authSrc+"' /><br/> ");
                bw.newLine();
                authHashPlantext = authSrc + companyId + SystemInfo.getLoginID();
                String authHash =  Md5EndCrypt(authHashPlantext);
                bw.write( "         <input type='hidden' name='authHash' value='"+authHash+"' /><br/> ");
                bw.newLine();
                bw.write( "        <input type='hidden'  name='eMail' value='"+SystemInfo.getCurrentShop().getMailAddress()+"' /><br/> ");
                bw.newLine();
                bw.write("         CSVファイル：<input type='file' value='./'  name='csvFile' /><br/> ");
                bw.newLine();
                bw.write( "         <button type='submit'>サイト遷移</button> ");
                bw.newLine();
                bw.write("     </form> ");
                bw.newLine();
                bw.write(" </body> ");
                bw.newLine();
                bw.write(" </html> ");
                bw.newLine();
                */
                bw.write(" <!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'> ");
                bw.newLine();
                bw.write(" <html xmlns='http://www.w3.org/1999/xhtml'> ");
                bw.newLine();
                bw.write(" <head> ");
                bw.newLine();
                bw.write(" <meta http-equiv='Content-Type' content='text/html; charset=utf-8' /> ");
                bw.newLine();
                bw.write(" <meta http-equiv='Content-Style-Type' content='text/css' /> ");
                bw.newLine();
                bw.write(" <meta http-equiv='Content-Script-Type' content='text/javascript' /> ");
                bw.newLine();
                bw.write(" <meta http-equiv='imagetoolbar' content='no' /> ");
                bw.newLine();
                bw.write(" <meta name='viewport' content='width=device-width, initial-scale = 1.0, user-scalable = no'> ");
                bw.newLine();
                bw.write(" <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700,600' rel='stylesheet' type='text/css'> ");
                bw.newLine();
                bw.write(" <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css' /> ");
                bw.newLine();
                bw.write(" <link href='http://api.sosia.jp/digitalpost/common.css' media='screen' rel='stylesheet' type='text/css' /> ");
                bw.newLine();
                bw.write(" <link href='http://api.sosia.jp/digitalpost/style.css' media='screen' rel='stylesheet' type='text/css' /> ");
                bw.newLine();
                bw.write(" <script src='https://code.jquery.com/jquery-1.11.1.min.js'></script> ");
                bw.newLine();
                bw.write(" <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js'></script> ");
                bw.newLine();
                bw.write(" <title>SOSIA DM Powered by DigitalPOST</title> ");
                bw.newLine();
                bw.write(" <meta name='description' content='' /> ");
                bw.newLine();
                bw.write(" <meta name='keywords' content='' /> ");
                bw.newLine();
                bw.write(" <!-- <link rel='stylesheet' href='css/style.min.css' type='text/css' media='screen'> --> ");
                bw.newLine();
                bw.write(" <!--[if lt IE 9]> ");
                bw.newLine();
                bw.write("   <script src='http://html5shiv.googlecode.com/svn/trunk/html5.js'></script> ");
                bw.newLine();
                bw.write("   <script src='http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js'><script> ");
                bw.newLine();
                bw.write(" <![endif]--> ");
                bw.newLine();
                bw.write(" <script> ");
                bw.write("     $( document ).ready(function() { ");
                bw.write("         $('#submit_button').click(function(){ ");
                bw.write("                         var file = $('#csvFile').val(); ");
                bw.write("                         if(file) { ");
                bw.write("                                 $('#digiForm').submit(); ");
                bw.write("                         } else { ");
                bw.write("                                 alert('ファイルを選択してください'); ");
                bw.write("                         } ");
                bw.write("                 }); ");
                bw.write("     }); ");
                bw.write(" </script> ");
                bw.write(" </head> ");
                bw.newLine();

                bw.write(" <body> ");
                bw.newLine();
                bw.write(" <!-- header--> ");
                bw.newLine();
                bw.write(" <div id='main'> ");
                bw.newLine();
                bw.write("   <div class='navbar navbar-fixed-top nav-menu' role='navigation'> ");
                bw.newLine();
                bw.write("     <div class='container-fluid'> ");
                bw.newLine();
                bw.write("       <div class='container menu'> ");
                bw.newLine();
                bw.write("         <div class='menuL'> ");
                bw.newLine();
                bw.write("           <div id='logoH'><img alt='Logo' src='http://api.sosia.jp/digitalpost/logo.png' /></div> ");
                bw.newLine();
                bw.write("         </div> ");
                bw.newLine();
                bw.write("       </div> ");
                bw.newLine();
                bw.write("     </div> ");
                bw.newLine();
                bw.write(" <!--end #container clearfix--> ");
                bw.newLine();
                bw.write("   </div> ");
                bw.newLine();
                bw.write(" <!-- header end --> ");
                bw.newLine();
                bw.write(" <!-- ▽メインコンテンツ▽ --> ");
                bw.newLine();
                bw.write(" <div id='topImges'></div> ");
                bw.newLine();
                bw.write(" <div id='servicetopArea'> ");
                bw.newLine();
                bw.write("     <div class='container clearfix'> ");
                bw.newLine();
                bw.write("         <div id='topImgesMsg'> ");
                bw.newLine();
                bw.write("             <div class='timbox_w'><p><o>SOSIA DMとは…</o><o>「デジタルポスト株式会社」との</o><br><o>提携サービスでSOSIA POSで</o><o>抽出したお客様に向けてハガキ作成</o><o>から送付まで行える画期的なサービスです。</o></p></div> ");
                bw.newLine();
                bw.write("         </div> ");
                bw.newLine();
                bw.write("     </div> ");
                bw.newLine();
                bw.write(" </div> ");
                bw.newLine();
                bw.write(" <div id='newsArea'> ");
                bw.newLine();
                bw.write("     <div class='container clearfix newsWaku'> ");
                bw.newLine();
                bw.write("         <div id='newsL'> ");
                bw.newLine();
                bw.write("             <p>ハガキ作成</p> ");
                bw.newLine();
                bw.write("         </div> ");
                bw.newLine();
                bw.write("         <div id='newsR'> ");
                bw.newLine();
                bw.write("         	<form id='digiForm' method='post' action='http://sosiadm.digitalpost.jp/pmasp' enctype='multipart/form-data'> ");
                bw.newLine();

                bw.write(" 	        	<p class='fl'>ファイルを選択</p><input id='csvFile' type='file' name='csvFile'> ");
                bw.newLine();
                bw.write("                 <p class='attention'>※「参照」もしくは「ファイルを選択」ボタンをクリックして </br> C:\\sosia\\csv\\内のCSVファイルを選択してください。</br>");
                bw.newLine();
                bw.write("                 InternetExplorerをご利用の場合は、画面下部に表示される </br>「ブロックされているコンテンツを許可」をクリックしてからご利用ください。</p>");
                bw.newLine();
                bw.write("  		       	<div class='submit_btn'><input id='submit_button' type='button' value='ハガキを作成する ＞'></div> ");
                bw.newLine();
                bw.write("        			<p class='attention'>※これより先は「デジタルポスト株式会社」のサービスとなります。 ");
                bw.newLine();
                bw.write(" </p> ");
                bw.newLine();
                bw.write( "         <input type='hidden' name='userId' value='"+SystemInfo.getLoginID()+"' /><br/> ");
                bw.newLine();
                bw.write("         <input type='hidden' name='companyId' value='"+companyId+"'  /><br/> ");
                bw.newLine();
                bw.write( "        <input type='hidden' name='authSrc' value='"+authSrc+"' /><br/> ");
                bw.newLine();
                authHashPlantext = authSrc + companyId + SystemInfo.getLoginID();
                String authHash =  Md5EndCrypt(authHashPlantext);
                bw.write( "         <input type='hidden' name='authHash' value='"+authHash+"' /><br/> ");
                bw.newLine();
                if(!SystemInfo.getCurrentShop().getMailAddress().equals("")) {
                    bw.write( "        <input type='hidden'  name='eMail' value='"+SystemInfo.getCurrentShop().getMailAddress()+"' /><br/> ");
                }else {
                    bw.write( "        <input type='hidden'  name='eMail' value='"+SystemInfo.getGroup().getMailAddress()+"' /><br/> ");
                }
                bw.newLine();
                bw.write("         	</form> ");
                bw.newLine();
                bw.write("         </div> ");
                bw.newLine();
                bw.write("     </div> ");
                bw.newLine();
                bw.write(" </div> ");
                bw.newLine();
                bw.write(" <div id='footerBefore'></div> ");
                bw.newLine();
                bw.write(" <!-- △メインコンテンツ△ --> ");
                bw.newLine();
                bw.write(" <!-- ▽フッター▽ --> ");
                bw.newLine();
                bw.write("   <!-- footer --> ");
                bw.newLine();
                bw.write(" <div id='footer'> ");
                bw.newLine();
                bw.write(" </div><!--end #footer--> ");
                bw.newLine();
                bw.write(" <!-- △フッター△ --> ");
                bw.newLine();
                bw.write(" </div> ");
                bw.newLine();
                bw.write(" <!--end #main--> ");
                bw.newLine();
                bw.write(" </body> ");
                bw.newLine();
                bw.write(" </html> ");
                bw.newLine();
            } catch (IOException ex) {
                Logger.getLogger(HairMailSearchPanel.class.getName()).log(Level.SEVERE, null, ex);           
            }finally{
                if (bw != null) {
                    try {
                        bw.flush();
                        bw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(HairMailSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

               }
            } 

            /*
            //APIコールを処理
            String charset = "UTF-8";
            //File csvFile = new File( System.getenv(SystemInfo.getTempDirStr()) + "/send_address.csv");
            File csvFile = new File( "D:/send_address.csv");
            String requestURL = "https://stg2.digitalpost.jp/pmasp";
            String authHashPlantext = "";
            String authSrc = "oqz9cdy7m0006GW3ivAvI9xZ";
            StringBuffer content = new StringBuffer();
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                String companyId = SystemInfo.getDatabase();
                companyId = companyId.substring(9);//pos_hair_○○

                multipart.addFormField("userId", SystemInfo.getLoginID());
                multipart.addFormField("companyId", companyId);
                multipart.addFormField("authSrc", authSrc);
                multipart.addFormField("eMail", SystemInfo.getCurrentShop().getMailAddress());
                authHashPlantext = authSrc + companyId + SystemInfo.getLoginID();
                String authHash =  Md5EndCrypt(authHashPlantext);
                multipart.addFormField("authHash", authHash);
                multipart.addFilePart("csvFile", csvFile);
                List<String> response = multipart.finish();

                for (String line : response) {
                     System.out.println(line);
                     content.append(line);
                     bw.write(line);
                     bw.newLine();
                }


           } catch (IOException ex) {
                System.err.println(ex);
            }
            finally {
                try {
                    if (bw != null) {
                        bw.flush();
                        bw.close();
                    }
                } catch (Exception e) {
                }
            }
            //DigitalPostPanel digitalPanel = new DigitalPostPanel(parentFrame,true,content.toString());
            //digitalPanel.setLocation(this.getLocationOnScreen().x, this.getLocationOnScreen().y);
            //digitalPanel.setVisible(true);
            */
            Runtime rTime = Runtime.getRuntime();
            try {
                Runtime runtime = Runtime.getRuntime();
                    runtime.exec( "cmd /c \"" + htmlFileName + "\"");
            } catch (IOException ex) {
                Logger.getLogger(HairMailSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
            }   
            
        }
        
       
       
    }//GEN-LAST:event_DigitalPostActionPerformed

    private void tbCoursePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbCoursePropertyChange
        this.setTechItemChecked(tbCourse);
    }//GEN-LAST:event_tbCoursePropertyChange

    private void tbCourseClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCourseClassMouseReleased
        this.showCourse(tbCourseClass , tbCourse);
    }//GEN-LAST:event_tbCourseClassMouseReleased

    private void tbCourseClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCourseClassKeyReleased
        this.showCourse(tbCourseClass , tbCourse);
    }//GEN-LAST:event_tbCourseClassKeyReleased

    private void targetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetActionPerformed
        CheckCourseFlag();
        initCourseClasses();
    }//GEN-LAST:event_targetActionPerformed

    public static String escapeForJava( String value )
    {
        StringBuilder builder = new StringBuilder();
        
        for( char c : value.toCharArray() )
        {

            if ( c == '\"' )
                builder.append( "\\\"" );          
            else
                builder.append( c );
        }
       
        return builder.toString();
    }
    public static String Md5EndCrypt(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] hash = md.digest(message.getBytes("UTF-8"));
            //converting byte array to Hexadecimal String 
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HairMailSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HairMailSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return digest;
    }

    /**
     * CSVファイルを開く
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DigitalPost;
    private javax.swing.JRadioButton addAll;
    private javax.swing.ButtonGroup addButtonGroup;
    private javax.swing.JRadioButton addEnd;
    private javax.swing.JRadioButton addYet;
    private com.geobeck.swing.JFormattedTextFieldEx ageFrom;
    private com.geobeck.swing.JFormattedTextFieldEx ageTo;
    private javax.swing.JComboBox birthMonth;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo birthdayFrom;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo birthdayTo;
    private javax.swing.JRadioButton both;
    private javax.swing.JButton btnOutputCsv;
    private javax.swing.JButton btnOutputExcel;
    private javax.swing.JRadioButton callFlagAll;
    private javax.swing.ButtonGroup callFlagButtonGroup;
    private javax.swing.JRadioButton callFlagNG;
    private javax.swing.JRadioButton callFlagOK;
    private javax.swing.JRadioButton cellularMailAll;
    private javax.swing.ButtonGroup cellularMailButtonGroup;
    private javax.swing.JRadioButton cellularMailExists;
    private javax.swing.JRadioButton cellularMailNotExists;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JRadioButton chargeStaffAll;
    private javax.swing.ButtonGroup chargeStaffButtonGroup;
    private javax.swing.JRadioButton chargeStaffFree;
    private javax.swing.JRadioButton chargeStaffNamed;
    private javax.swing.JTextField chargeStaffNo;
    private javax.swing.JComboBox city;
    private javax.swing.JButton clearButton;
    private com.geobeck.swing.JFormattedTextFieldEx condName;
    private javax.swing.JButton condRegistButton;
    private javax.swing.JButton condTemplateButton;
    private javax.swing.JScrollPane courseClassScrollPane;
    private javax.swing.JPanel coursePanel;
    private javax.swing.JScrollPane courseScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx customerKana1;
    private com.geobeck.swing.JFormattedTextFieldEx customerKana2;
    private com.geobeck.swing.JFormattedTextFieldEx customerName1;
    private com.geobeck.swing.JFormattedTextFieldEx customerName2;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo1;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo2;
    private javax.swing.ButtonGroup dateButtonGroup;
    private javax.swing.JRadioButton dateType0;
    private javax.swing.JRadioButton dateType1;
    private javax.swing.JRadioButton dateType2;
    private javax.swing.JRadioButton dateType3;
    private javax.swing.JRadioButton female;
    private javax.swing.JComboBox firstComingMotive;
    private javax.swing.JPanel freeHeadingPanel;
    private javax.swing.JTextArea hint;
    private javax.swing.JCheckBox isIntroduced;
    private javax.swing.JCheckBox isIntroducer;
    private javax.swing.JTable item;
    private javax.swing.JTable itemClass;
    private javax.swing.JScrollPane itemClassScrollPane;
    private javax.swing.JCheckBox itemOnly;
    private javax.swing.JPanel itemPanel;
    private javax.swing.JScrollPane itemScrollPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox job;
    private javax.swing.JButton mailButton;
    private javax.swing.ButtonGroup mailButtonGroup;
    private javax.swing.JRadioButton male;
    private com.geobeck.swing.JFormattedTextFieldEx memo;
    private javax.swing.JRadioButton pcMailAll;
    private javax.swing.ButtonGroup pcMailButtonGroup;
    private javax.swing.JRadioButton pcMailExists;
    private javax.swing.JRadioButton pcMailNotExists;
    private com.geobeck.swing.JFormattedTextFieldEx postalCode;
    private javax.swing.JButton postcardButton;
    private javax.swing.JComboBox prefecture;
    private com.geobeck.swing.JFormattedTextFieldEx priceFrom;
    private com.geobeck.swing.JFormattedTextFieldEx priceTo;
    private javax.swing.JTabbedPane productDivision;
    private javax.swing.JComboBox responseItem;
    private javax.swing.JButton sealButton;
    private javax.swing.JRadioButton sendDmAll;
    private javax.swing.ButtonGroup sendDmButtonGroup;
    private javax.swing.JRadioButton sendDmNG;
    private javax.swing.JRadioButton sendDmOK;
    private javax.swing.JRadioButton sendMailAll;
    private javax.swing.ButtonGroup sendMailButtonGroup;
    private javax.swing.JRadioButton sendMailNG;
    private javax.swing.JRadioButton sendMailOK;
    private javax.swing.ButtonGroup sexButtonGroup;
    private javax.swing.JRadioButton sosiaGearAll;
    private javax.swing.ButtonGroup sosiaGearButtonGroup;
    private javax.swing.JRadioButton sosiaGearExists;
    private javax.swing.JRadioButton sosiaGearNotExists;
    private javax.swing.JPanel sosiaGearPanel;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel target;
    private javax.swing.JLabel targetLabel;
    private javax.swing.JTable tbCourse;
    private javax.swing.JTable tbCourseClass;
    private javax.swing.JRadioButton techItemAND;
    private javax.swing.ButtonGroup techItemGroup;
    private javax.swing.JRadioButton techItemNOT;
    private javax.swing.JRadioButton techItemOR;
    private javax.swing.JPanel techItemPanel;
    private javax.swing.JTable technic;
    private javax.swing.JTable technicClass;
    private javax.swing.JScrollPane technicClassScrollPane;
    private javax.swing.JPanel technicPanel;
    private javax.swing.JScrollPane technicScrollPane;
    private javax.swing.ButtonGroup visitCountButtonGroup;
    private com.geobeck.swing.JFormattedTextFieldEx visitCountFrom;
    private com.geobeck.swing.JFormattedTextFieldEx visitCountTo;
    private javax.swing.JRadioButton visitCountType0;
    private javax.swing.JRadioButton visitCountType1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo visitDateFrom;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo visitDateTo;
    // End of variables declaration//GEN-END:variables
    private ArrayList<SearchResultInfo> cusArray = new ArrayList<SearchResultInfo>();
    private ArrayList<MstCustomer> selectedArray = new ArrayList<MstCustomer>();
    private ProductClasses technicClasses = new ProductClasses();
    private ProductClasses itemClasses = new ProductClasses();
    private ProductClasses courseClasses = new ProductClasses();
    private MstResponses mrs = new MstResponses();
    private Integer selectedAddress = -1;
    /**
     * 非会員一覧画面用FocusTraversalPolicy
     */
    private HairMailSearchFocusTraversalPolicy ftp = new HairMailSearchFocusTraversalPolicy();

    /**
     * 非会員一覧画面用FocusTraversalPolicyを取得する。
     *
     * @return 非会員一覧画面用FocusTraversalPolicy
     */
    public HairMailSearchFocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    public void setSelectedAddress(Integer selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnOutputExcel);
        SystemInfo.addMouseCursorChange(mailButton);
        SystemInfo.addMouseCursorChange(postcardButton);
        SystemInfo.addMouseCursorChange(sealButton);
        SystemInfo.addMouseCursorChange(clearButton);
        SystemInfo.addMouseCursorChange(condRegistButton);
        SystemInfo.addMouseCursorChange(condTemplateButton);
        SystemInfo.addMouseCursorChange(DigitalPost);
        SystemInfo.addMouseCursorChange(btnOutputCsv);
        
    }

    private void clear() {
        //target.setSelectedIndex(0);
        itemOnly.setSelected(false);
        both.setSelected(true);
//		mailAll.setSelected(true);
        this.dateType0ActionPerformed(null);
        visitCountFrom.setText("");
        visitCountTo.setText("");
        dateType0.setSelected(true);
        visitDateFrom.setDate(visitDateFrom.getDefaultDate(false));
        visitDateTo.setDate(visitDateTo.getDefaultDate(false));
        customerNo1.setText("");
        customerNo2.setText("");
        customerKana1.setText("");
        customerName1.setText("");
        customerKana2.setText("");
        customerName2.setText("");
        chargeStaffNo.setText("");
        chargeStaff.setSelectedIndex(0);
        chargeStaffAll.setSelected(true);
        addAll.setSelected(true);
        postalCode.setText("");
        prefecture.setSelectedIndex(0);
        city.setSelectedIndex(0);
        ageFrom.setText("");
        ageTo.setText("");
        birthMonth.setSelectedIndex(0);
        birthdayFrom.setDate(birthdayFrom.getDefaultDate(false));
        birthdayTo.setDate(birthdayTo.getDefaultDate(false));
        job.setSelectedIndex(0);
        memo.setText("");
        responseItem.setSelectedIndex(0);
        /*
        if (free1.isVisible()) {
            free1.setSelectedIndex(0);
        }
        if (free2.isVisible()) {
            free2.setSelectedIndex(0);
        }
        if (free3.isVisible()) {
            free3.setSelectedIndex(0);
        }
        if (free4.isVisible()) {
            free4.setSelectedIndex(0);
        }*/
        for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
               mfhsup.setSelectedInit(); 
        }

        for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
            fhPanelText.setNullFreeHeadingText();
         }
         
        firstComingMotive.setSelectedIndex(0);
        priceFrom.setText("");
        priceTo.setText("");

        sendMailAll.setSelected(true);
        sendDmAll.setSelected(true);
        callFlagAll.setSelected(true);
        pcMailAll.setSelected(true);
        cellularMailAll.setSelected(true);
        sosiaGearAll.setSelected(true);

        this.initProductClasses();

    }

    private String sqlBuild(int sendType, String shopIDList, int outputType) {
        return sqlBuild(sendType, shopIDList, false, outputType);
    }

    public String sqlBuild( int sendType, String shopIDList, boolean isInNonSalesCustomer, int outputType ) {
	    
	    /*******************************/
	    /* 来店回数 取得ＳＱＬ */
	    /*******************************/
	    StringBuilder visitNumSql = new StringBuilder(1000);
	    visitNumSql.append(" (");
	    visitNumSql.append("     select");
	    visitNumSql.append("         count(a.slip_no)");
            //日付条件が来店日付以外、または来店日付かつ累計来店回数が
            //選択されている場合のみ累計来店回数を取得する。
	    if (!(dateType0.isSelected() || dateType3.isSelected()) || visitCountType0.isSelected()) {
		visitNumSql.append("     + coalesce(max(b.before_visit_num),0)");
	    }
	    visitNumSql.append("     from");
	    visitNumSql.append("         view_data_sales_valid a");
	    visitNumSql.append("             inner join mst_customer b");
	    visitNumSql.append("                 using(customer_id)");
	    visitNumSql.append("     where");
	    visitNumSql.append("             b.customer_id = t.customer_id");
	    visitNumSql.append("         and a.sales_date is not null");
	    visitNumSql.append("         and a.shop_id in (" + shopIDList + ")");
	    // 来店日付かつ期間指定がある場合のみ
	    if (dateType0.isSelected() || dateType3.isSelected()) {
		if(visitDateFrom.getDateStr() != null){
                    //累計来店回数以外が選択されている場合のみ開始日を指定する
                    if (!visitCountType0.isSelected()) {
                        visitNumSql.append(" and a.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                    }
		}
		if(visitDateTo.getDateStr() != null) {
		    visitNumSql.append(" and a.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
		}
	    }
	    visitNumSql.append(" )");
	    
	    /*******************************/
	    /* 売上金額 取得ＳＱＬ */
	    /*******************************/
	    StringBuilder sumTotalSql = new StringBuilder(1000);
	    sumTotalSql.append(" (");
	    sumTotalSql.append("     select");
	    //IVS_LVTu start edit 2015/07/16 Bug #40581
            //sumTotalSql.append("         coalesce(sum(discount_sales_value_in_tax), 0)");
            sumTotalSql.append("         coalesce(sum(discount_detail_value_in_tax), 0)");
	    sumTotalSql.append("     from");
            //sumTotalSql.append("         view_data_sales_valid");
            sumTotalSql.append("         view_data_sales_detail_valid");
	    sumTotalSql.append("     where");
	    sumTotalSql.append("             shop_id in (" + shopIDList + ")");
	    sumTotalSql.append("         and customer_id = t.customer_id");
	    sumTotalSql.append("         and sales_date is not null");
            sumTotalSql.append("         and product_division <> 6 ");
            //IVS_LVTu end edit 2015/07/16 Bug #40581
	    // 来店日付かつ期間指定がある場合のみ
	    if (dateType0.isSelected() || dateType3.isSelected()) {
		if(visitDateFrom.getDateStr() != null){
		    sumTotalSql.append(" and sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
		}
		if(visitDateTo.getDateStr() != null) {
		    sumTotalSql.append(" and sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
		}
	    }
	    sumTotalSql.append(" )");

	    /*******************************/
	    /* 初回来店日 取得ＳＱＬ */
	    /*******************************/
	    StringBuilder firstVisitDateSql = new StringBuilder(1000);
	    firstVisitDateSql.append(" (");
            firstVisitDateSql.append("     select");
            firstVisitDateSql.append("         coalesce(first_visit_date,");
            firstVisitDateSql.append("             (");
            firstVisitDateSql.append("                 select");
            firstVisitDateSql.append("                     min(sales_date)");
            firstVisitDateSql.append("                 from");
            firstVisitDateSql.append("                     data_sales");
            firstVisitDateSql.append("                 where");
            firstVisitDateSql.append("                         delete_date is null");
            firstVisitDateSql.append("                     and sales_date is not null");
            firstVisitDateSql.append("                     and customer_id = t.customer_id");
            firstVisitDateSql.append("                     and shop_id in (" + shopIDList + ")");
            firstVisitDateSql.append("             ))");
            firstVisitDateSql.append("     from");
            firstVisitDateSql.append("         mst_customer");
            firstVisitDateSql.append("     where");
            firstVisitDateSql.append("         customer_id = t.customer_id");
	    firstVisitDateSql.append(" )");
	    
	    /**************************************/
	    /* 精算情報に関連する抽出条件 */
	    /**************************************/
	    boolean isSalesCondition = false;
	    // 来店日付
	    isSalesCondition = isSalesCondition || (visitDateFrom.getDateStr() != null);
	    isSalesCondition = isSalesCondition || (visitDateTo.getDateStr() != null);
	    //主担当者
	    isSalesCondition = isSalesCondition || (!chargeStaffNo.getText().equals(""));
	    // 主担当指名区分
	    isSalesCondition = isSalesCondition || (chargeStaffNamed.isSelected());
	    isSalesCondition = isSalesCondition || (chargeStaffFree.isSelected());
	    // 技術分類・技術
	    isSalesCondition = isSalesCondition || (this.isCondTechClassSelected());
	    isSalesCondition = isSalesCondition || (this.isCondTechSelected());
	    // 商品分類・商品
	    isSalesCondition = isSalesCondition || (this.isCondItemClassSelected());
	    isSalesCondition = isSalesCondition || (this.isCondItemSelected());
            // コース　
	    isSalesCondition = isSalesCondition || (this.isCondCourseClassSelected());
	    isSalesCondition = isSalesCondition || (this.isCondCourseSelected());
            // 反響項目
	    isSalesCondition = isSalesCondition || (0 < responseItem.getSelectedIndex());
	    // 来店回数
	    isSalesCondition = isSalesCondition || (!visitCountFrom.getText().equals(""));
	    isSalesCondition = isSalesCondition || (!visitCountTo.getText().equals(""));
	    // 売上金額
	    isSalesCondition = isSalesCondition || (!priceFrom.getText().equals(""));
	    isSalesCondition = isSalesCondition || (!priceTo.getText().equals(""));
	    
	    /*******************************/
	    /* メインＳＱＬ */
	    /*******************************/
	    StringBuilder sql = new StringBuilder(1000);
	    sql.append(" select");
	    sql.append("     t.*");
            //IVS_LVTu start add 2015/07/16 Bug #40581
                sql.append("     ,(SELECT min(sales_date) ");
                sql.append("     FROM data_sales ");
                sql.append("     WHERE delete_date IS NULL ");
                sql.append("     AND sales_date IS NOT NULL ");
                sql.append("     AND customer_id = t.customer_id ");
                sql.append("     AND shop_id IN (" + shopIDList + ")) as date_sales ");
            //IVS_LVTu end add 2015/07/16 Bug #40581
	    for (int i = 0; i < arrOrderByFreeHeading.size(); i++) {
                String[] free = arrOrderByFreeHeading.get(i);              
                sql.append("     ,(");
                sql.append("         select");
                sql.append("             free_heading_class_name");
                sql.append("         from");
                sql.append("             mst_customer_free_heading a join mst_free_heading_class b using (free_heading_class_id)");
                sql.append("         where");
                sql.append("                 b.free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
                sql.append("             and a.customer_id = t.customer_id");
                sql.append("      ) as free_heading_class_name" + SQLUtil.convertForSQL(i + 1));
                sql.append("     , Case when (select free_heading_id from mst_customer_free_heading where ");
                sql.append("     customer_id = t.customer_id and free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
                sql.append("         ) = 0 then  (select free_heading_text ");
                sql.append("         from mst_customer_free_heading a");
                sql.append("         where");
                sql.append("           a.customer_id = t.customer_id and free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
                sql.append("         ) else  (");
                sql.append("         select");
                sql.append("             free_heading_name");
                sql.append("         from");
                sql.append("             mst_customer_free_heading a join mst_free_heading b using (free_heading_class_id, free_heading_id)");
                sql.append("         where");
                sql.append("                 b.free_heading_class_id = " + SQLUtil.convertForSQL(free[0]));
                sql.append("             and a.customer_id = t.customer_id");
                sql.append("      ) end as free_heading_name" + SQLUtil.convertForSQL(i + 1));
            }
            /*
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_class_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading_class b using (free_heading_class_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 1");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_class_name1");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading b using (free_heading_class_id, free_heading_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 1");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_name1");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_class_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading_class b using (free_heading_class_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 2");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_class_name2");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading b using (free_heading_class_id, free_heading_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 2");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_name2");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_class_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading_class b using (free_heading_class_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 3");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_class_name3");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading b using (free_heading_class_id, free_heading_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 3");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_name3");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_class_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading_class b using (free_heading_class_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 4");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_class_name4");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             free_heading_name");
	    sql.append("         from");
	    sql.append("             mst_customer_free_heading a join mst_free_heading b using (free_heading_class_id, free_heading_id)");
	    sql.append("         where");
	    sql.append("                 b.free_heading_class_id = 4");
	    sql.append("             and a.customer_id = t.customer_id");
	    sql.append("      ) as free_heading_name4");
	    */
            //IVS_TMTrong start edit 2015/10/19 New request #43511
	    //sql.append("     ,date_part('year', age(current_timestamp, t.birthday)) as age");
            sql.append("     ,case when EXTRACT(YEAR FROM t.birthday)::int <= 1900 then 0 else date_part('year', age(current_timestamp, t.birthday)) end as age"); 
            //IVS_TMTrong end edit 2015/10/19 New request #43511
	    sql.append("     ,(select job_name from mst_job where job_id = t.job_id and delete_date is null) as job_name");
	    sql.append("     ,ds.sales_date");
	    sql.append("     ,ds.shop_id         as last_shop_id");
	    sql.append("     ,ds.shop_name       as shop_name");
	    sql.append("     ,ds.staff_name      as staff_name");
	    sql.append("     ,ds.designated_flag as designated_flag");
	    sql.append("     ,(select customer_no from mst_customer where customer_id = t.introducer_id) as introducer_no");
	    sql.append("     ,(select customer_name1 || customer_name2 from mst_customer where customer_id = t.introducer_id) as introducer_name");
	    sql.append("     ,(select first_coming_motive_name from mst_first_coming_motive where first_coming_motive_class_id = t.first_coming_motive_class_id and delete_date is null) as first_coming_motive_name");
	    sql.append("     ," + visitNumSql.toString() + " as visit_num");
	    sql.append("     ," + sumTotalSql.toString() + " as sumTotal");

	    // 紹介した人の顧客No.の配列を返す
	    sql.append("     ,array(");
	    sql.append("         select");
	    sql.append("             coalesce(customer_no, '')");
	    sql.append("         from");
	    sql.append("             mst_customer mc");
	    sql.append("         where");
	    sql.append("             introducer_id = t.customer_id");
	    sql.append("      ) as introducer_no_array");

	    // 紹介した人の顧客名の配列を返す
	    sql.append("     ,array(");
	    sql.append("         select");
	    sql.append("             coalesce(customer_name1, '') || ' ' || coalesce(customer_name2, '')");
	    sql.append("         from");
	    sql.append("             mst_customer mc");
	    sql.append("         where");
	    sql.append("             introducer_id = t.customer_id");
	    sql.append("      ) as introducer_name_array");
	    
            sql.append("     ,(");
            sql.append("         select");
            sql.append("             to_char(drd.reservation_datetime, 'yyyy/mm/dd hh24:mi') as next_reserve_date");
            sql.append("         from");
            sql.append("             data_reservation dr");
            sql.append("                 inner join data_reservation_detail drd");
            sql.append("                     using(shop_id, reservation_no)");
            sql.append("         where");
            sql.append("                 dr.delete_date is null");
            sql.append("             and drd.delete_date is null");
            sql.append("             and dr.shop_id in (" + shopIDList + ")");
            sql.append("             and dr.customer_id = t.customer_id");
            sql.append("             and drd.reservation_datetime > current_timestamp");
            sql.append("         order by");
            sql.append("             drd.reservation_datetime");
            sql.append("         limit 1");
            sql.append("      ) as next_reserve_date");
            sql.append("     ,(");
            sql.append("         select");
            sql.append("             case");
            sql.append("                 when dr.mobile_flag is not null then 'WEB予約'");
            sql.append("                 when dr.next_flag is not null and dr.next_flag = 1 then '前回来店'");
            sql.append("                 when dr.preorder_flag is not null and dr.preorder_flag = 1 then '事前予約'");
            sql.append("             end");
            sql.append("         from");
            sql.append("             data_reservation dr");
            sql.append("                 inner join data_reservation_detail drd");
            sql.append("                     using(shop_id, reservation_no)");
            sql.append("         where");
            sql.append("                 dr.delete_date is null");
            sql.append("             and drd.delete_date is null");
            sql.append("             and dr.shop_id in (" + shopIDList + ")");
            sql.append("             and dr.customer_id = t.customer_id");
            sql.append("             and drd.reservation_datetime > current_timestamp");
            sql.append("         order by");
            sql.append("             drd.reservation_datetime");
            sql.append("         limit 1");
            sql.append("      ) as next_reserve_type");

	    sql.append(" from");
	    sql.append("     mst_customer t");
//	    sql.append("         left outer join mst_customer introducer");
//	    sql.append("                      on t.introducer_id = introducer.customer_id");
//	    sql.append("         left outer join mst_first_coming_motive mfcm");
//	    sql.append("                      on t.first_coming_motive_class_id = mfcm.first_coming_motive_class_id");
//	    sql.append("                     and mfcm.delete_date is null");

	    // 最終来店日で期間指定なし、または、次回予約日の場合は、精算情報が生成されていない場合があるため、Left Joinとする。
	    boolean isLeftJoin = false;
	    isLeftJoin = isLeftJoin || (dateType1.isSelected() && visitDateFrom.getDateStr() == null && visitDateTo.getDateStr() == null);
	    isLeftJoin = isLeftJoin || dateType2.isSelected();
            
	    // 来店日付で期間指定なしの場合は、確認メッセージを表示し、Left Joinとする。
            if (isInNonSalesCustomer) isLeftJoin = true;
            
	    if (isLeftJoin) {
		sql.append("     left outer join");
	    } else {
		sql.append("     inner join");
	    }
	    
	    sql.append("             (");
	    sql.append("                 select");
	    sql.append("                      a.*");
	    sql.append("                     ,shop_name");
	    sql.append("                     ,staff_name1 || ' ' || staff_name2 as staff_name");
	    sql.append("                 from");
	    sql.append("                     data_sales a");
	    sql.append("                         join");
	    sql.append("                         (");
	    sql.append("                             select");
	    sql.append("                                  a.shop_id");
	    sql.append("                                 ,a.sales_date");
	    sql.append("                                 ,a.customer_id");
	    sql.append("                                 ,max(a.slip_no) as slip_no ");
	    sql.append("                             from");
	    sql.append("                                 data_sales a");
	    sql.append("                                     join");
	    sql.append("                                     (");
	    sql.append("                                         select");
	    sql.append("                                              customer_id");
	    sql.append("                                             ,max(sales_date) as sales_date");
	    sql.append("                                         from");
	    sql.append("                                             data_sales");
	    sql.append("                                         where");
	    sql.append("                                                 shop_id in (" + shopIDList + ")");
	    sql.append("                                             and sales_date is not null");
	    sql.append("                                             and delete_date is null");
	    sql.append("                                         group by");
	    sql.append("                                             customer_id");
            
	    sql.append("                                     ) b");
	    sql.append("                                     using(customer_id, sales_date)");
	    sql.append("                             where");
	    sql.append("                                 a.delete_date is null");
	    sql.append("                             group by");
	    sql.append("                                  shop_id");
	    sql.append("                                 ,sales_date");
	    sql.append("                                 ,customer_id");
	    sql.append("                         ) b");
	    sql.append("                         using(shop_id, customer_id, sales_date, slip_no)");
	    sql.append("                         join mst_shop");
	    sql.append("                             using(shop_id)");
	    sql.append("                         left outer join mst_staff");
	    sql.append("                             using(staff_id)");
	    sql.append("                 where");
	    sql.append("                     a.delete_date is null");

	    // 本部ログインで会社名が選択された場合
	    if (target.getSelectedItem() instanceof MstGroup && ((MstGroup)target.getSelectedItem()).getLevel() == 0) {
		sql.append(" and (a.shop_id in (" + shopIDList + ") or a.shop_id is null)");
	    } else {
		sql.append(" and a.shop_id in (" + shopIDList + ")");
	    }

	    /**********************/
	    //最終来店日
	    /**********************/
	    if(isSalesCondition && dateType1.isSelected()) {

		// 最終来店日
		if (visitDateFrom.getDateStr() != null) {
		    sql.append("     and a.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
		}
		if (visitDateTo.getDateStr() != null) {
		    sql.append("     and a.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
		}
		
		//主担当者
		if (chargeStaff.getSelectedIndex() > 0) {
		    sql.append("     and a.staff_id = " + ((MstStaff)chargeStaff.getSelectedItem()).getStaffID());
		}

		// 主担当指名区分
		if (chargeStaffNamed.isSelected()) {
		    sql.append("     and a.designated_flag = true");
		}
		if (chargeStaffFree.isSelected()) {
		    sql.append("     and a.designated_flag = false");
		}
                //IVS_LVTu start edit 2016/03/16 getCondTechClassCondition
		if (this.isCondTechClassSelected() || this.isCondTechSelected() 
                        || this.isCondItemClassSelected() || this.isCondItemSelected()
                        ||this.isCondCourseClassSelected() || this.isCondCourseSelected()) {
                    sql.append(" and exists");
                    sql.append(" (");
                    sql.append("     select 1");
                    sql.append("     from");
                    sql.append("         view_data_sales_detail_valid_with_prepaid dsd");
                    sql.append("             left join mst_technic mt");
                    sql.append("                     on dsd.product_division = 1");
                    sql.append("                    and dsd.product_id = mt.technic_id");
                    sql.append("             left outer join mst_item mi");
		    sql.append("                          on dsd.product_division = 2");
		    sql.append("                         and dsd.product_id = mi.item_id");
                    sql.append("             left outer join mst_course mc");
		    sql.append("                          on dsd.product_division = 5");
		    sql.append("                         and dsd.product_id = mc.course_id");
                    sql.append("     where");
                    sql.append("             dsd.shop_id = a.shop_id");
                    sql.append("         and dsd.slip_no = a.slip_no");

                    if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected()
                            || this.isCondTechClassSelected() || this.isCondTechSelected()
                            || this.isCondCourseClassSelected() || this.isCondCourseSelected())) {
                        sql.append("         and (");
                    }
                    if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
                        if (techItemOR.isSelected()) {
                            if (this.isCondTechClassSelected()) {
                                sql.append(getCondTechClassCondition("dsd"));
                            }
                            // 技術
                            if (this.isCondTechSelected()) {
                                if (this.isCondTechClassSelected()) {
                                    sql.append("         or ");
                                }
                                sql.append(getCondTechCondition("dsd"));
                            }
                        } else {
                            // 技術分類
                            if (this.isCondTechClassSelected()) {
                                sql.append(getCondTechClassCondition("dsd"));
                            }
                            // 技術
                            if (this.isCondTechSelected()) {
                                sql.append(getCondTechCondition("dsd"));
                            }
                        }
                    }

                    if (this.isCondItemClassSelected() || this.isCondItemSelected()) {

                        if (techItemOR.isSelected()) {
                            if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected())) {
                                sql.append(" or ");
                            } else {
                                sql.append("    (");
                            }
                            // 商品分類
                            if (this.isCondItemClassSelected()) {
                                sql.append(getCondItemClassCondition("dsd"));
                            }
                            // 商品
                            if (this.isCondItemSelected()) {
                                if (this.isCondItemClassSelected()) {
                                    sql.append("         or ");
                                }
                                sql.append(getCondItemCondition("dsd"));
                            }

                        } else {
                            // 商品分類
                            if (this.isCondItemClassSelected()) {
                                sql.append(getCondItemClassCondition("dsd"));
                            }
                            // 商品
                            if (this.isCondItemSelected()) {
                                sql.append(getCondItemCondition("dsd"));
                            }
                        }
                    }
                    
                    //コース
                    if (this.isCondCourseClassSelected() || this.isCondCourseSelected()) {
                        
                        if (techItemOR.isSelected()) {
                            if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected())
                                    ||this.isCondItemClassSelected() || this.isCondItemSelected()) {
                                sql.append(" or (");
                            }else {
                                sql.append("         (");
                            }
                            // コース分類
                            if (this.isCondCourseClassSelected()) {
                                sql.append(getCondCourseClassCondition("dsd"));
                            }
                            // コース
                            if (this.isCondCourseSelected()) {
                                if(this.isCondCourseClassSelected()) {
                                    sql.append("         or ");
                                }
                                sql.append(getCondCourseCondition("dsd"));
                            }
                            sql.append("         )");
                            
                        }else{
                            // コース分類
                            if (this.isCondCourseClassSelected()) {
                                sql.append(getCondCourseClassCondition("dsd"));
                            }
                            // コース
                            if (this.isCondCourseSelected()) {
                                sql.append(getCondCourseCondition("d"));
                            }
                        }
		    }

                    if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected()
                            || this.isCondTechClassSelected() || this.isCondTechSelected()
                            || this.isCondCourseClassSelected() || this.isCondCourseSelected())) {
                        sql.append("         ) ");
                    }
                    sql.append("         )");
                }
                
                // 反響項目
                if (0 < responseItem.getSelectedIndex()) {
                    sql.append("     and exists");
                    sql.append("     (");
                    sql.append("         select 1");
                    sql.append("         from");
                    sql.append("             data_response_effect");
                    sql.append("         where");
                    sql.append("                 delete_date is null");
                    sql.append("             and shop_id = a.shop_id");
                    sql.append("             and slip_no = a.slip_no");
                    sql.append("             and response_id = " + SQLUtil.convertForSQL(((MstResponse)responseItem.getSelectedItem()).getResponseID()));
                    sql.append("     )");
                }
	    }

	    sql.append("             ) ds");
	    sql.append("             on t.customer_id = ds.customer_id");
            
            int i = 0;       
            for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                //mfhsup.setSelectedInit();            
                if (mfhsup.getFreeHeading().getFreeHeadingID() > 0) { 
                    i += 1;
                    sql.append("     left outer join mst_customer_free_heading mcfh" + i  +" on (t.customer_id = mcfh" + i + ".customer_id)");
                } 
            }
            
            for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                if (!fhPanelText.getFreeHeadingText().isEmpty()) {
                    i += 1;
                    sql.append("     left outer join mst_customer_free_heading mcfh" + i  +" on (t.customer_id = mcfh" + i + ".customer_id)");
                }
            }
           
	    /* comment
	    // フリー項目
	    if (free1.getSelectedIndex() > 0) {   
		sql.append("     left outer join mst_customer_free_heading mcfh1 on (t.customer_id = mcfh1.customer_id)");
	    }
	    if (free2.getSelectedIndex() > 0) {   
		sql.append("     left outer join mst_customer_free_heading mcfh2 on (t.customer_id = mcfh2.customer_id)");
	    }
	    if (free3.getSelectedIndex() > 0) {   
		sql.append("     left outer join mst_customer_free_heading mcfh3 on (t.customer_id = mcfh3.customer_id)");
	    }
	    if (free4.getSelectedIndex() > 0) {   
		sql.append("     left outer join mst_customer_free_heading mcfh4 on (t.customer_id = mcfh4.customer_id)");
	    }
            */ 
	    sql.append(" where");
	    sql.append("     t.customer_no != '0'");
            
            // 顧客情報出力時は削除データを含めない（精算情報出力時は含める）
            if (outputType == OutputExcelDialog.OUTPUT_CUSTOMER) {
                sql.append(" and t.delete_date is null");
            }
            
	    sql.append("     and t.shop_id in (" +(SystemInfo.getSetteing().isShareCustomer() ? "0" : shopIDList )  + ")");
            
            i = 0;
            for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
                //mfhsup.setSelectedInit();            
                if (mfhsup.getFreeHeading().getFreeHeadingID() > 0) { 
                    i += 1;
                    sql.append(" and mcfh" + i + ".free_heading_class_id = " + SQLUtil.convertForSQL(mfhsup.getFreeHeadingClassID()));
                    sql.append(" and mcfh" + i + ".free_heading_id = " + SQLUtil.convertForSQL(mfhsup.getFreeHeading().getFreeHeadingID()));
                }           
          
            }
            
            for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                if (!fhPanelText.getFreeHeadingText().isEmpty()) {
                    i += 1;
                    sql.append(" and mcfh" + i + ".free_heading_class_id = " + SQLUtil.convertForSQL(fhPanelText.getFreeHeadingClassID()));
                    //sql.append(" and mcfh" + i + ".free_heading_text = " + SQLUtil.convertForSQL(fhPanelText.getFreeHeadingText()));
                    sql.append(" and mcfh" + i + ".free_heading_text like '%" + fhPanelText.getFreeHeadingText() + "%'");
                }
            }
            
            /* comment
	    // フリー項目
	    if (free1.getSelectedIndex() > 0) {
		MstFreeHeading msfh = (MstFreeHeading)free1.getSelectedItem();
		sql.append(" and mcfh1.free_heading_class_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingClass().getFreeHeadingClassID()));
		sql.append(" and mcfh1.free_heading_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingID()));
	    }
	    if (free2.getSelectedIndex() > 0) {
		MstFreeHeading msfh = (MstFreeHeading)free2.getSelectedItem();
		sql.append(" and mcfh2.free_heading_class_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingClass().getFreeHeadingClassID()));
		sql.append(" and mcfh2.free_heading_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingID()));
	    }
	    if (free3.getSelectedIndex() > 0) {
		MstFreeHeading msfh = (MstFreeHeading)free3.getSelectedItem();
		sql.append(" and mcfh3.free_heading_class_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingClass().getFreeHeadingClassID()));
		sql.append(" and mcfh3.free_heading_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingID()));
	    }
	    if (free4.getSelectedIndex() > 0) {
		MstFreeHeading msfh = (MstFreeHeading)free4.getSelectedItem();
		sql.append(" and mcfh4.free_heading_class_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingClass().getFreeHeadingClassID()));
		sql.append(" and mcfh4.free_heading_id = " + SQLUtil.convertForSQL(msfh.getFreeHeadingID()));
	    }
	    */
	    // 紹介した人
	    if (isIntroducer.isSelected()) {
		sql.append(" and get_introduce_count(t.customer_id) > 0");
	    }

	    // 紹介された人
	    if (isIntroduced.isSelected()) {
		sql.append(" and t.introducer_id is not null");
	    }
	    
	    /**********************/
	    /* メール */
	    /**********************/
	    if (sendType == SEND_TYPE_E_MAIL) {
		sql.append(" and (coalesce(t.pc_mail_address, '') != '' or coalesce(t.cellular_mail_address, '') != '')");
	    }
	    /**********************/
	    /* 住所 */
	    /**********************/
	    else if (sendType == SEND_TYPE_DIRECT_MAIL) {
		sql.append(" and coalesce(t.address1, '') || coalesce(t.address2, '') || coalesce(t.address3, '') != ''");
	    }
	    
	    sql.append("     and t.customer_id in");
	    sql.append("     (");
	    sql.append("         select");
	    sql.append("             customer_id");
	    sql.append("         from");
	    sql.append("             mst_customer");
	    sql.append("         where");
	    sql.append("                 customer_no != '0'");
//	    sql.append("             and delete_date is null");
	    sql.append("             and shop_id in (" +(SystemInfo.getSetteing().isShareCustomer() ? "0" : shopIDList )  + ")");

		// 性別
		if (male.isSelected()){
		    sql.append("     and sex = 1");
		}
		if (female.isSelected()){
		    sql.append("     and sex = 2");
		}

		// 年齢
		if (!ageFrom.getText().equals("")) {
		    sql.append("     and extract(year from age(current_timestamp, birthday)) >= " + ageFrom.getText());
		}
		if (!ageTo.getText().equals("")) {
		    sql.append("     and extract(year from age(current_timestamp, birthday)) <= " + ageTo.getText());
		}

		// 誕生月
		if (0 < birthMonth.getSelectedIndex()) {
		    sql.append("     and extract(month from birthday) = " + birthMonth.getSelectedItem().toString());
		}
            
		// メール配信
		if (!sendMailAll.isSelected()){
		    sql.append("     and send_mail = "  + (sendMailOK.isSelected() ? 1 : 0));
		}
		// DM配信
		if (!sendDmAll.isSelected()){
		    sql.append("     and send_dm = "  + (sendDmOK.isSelected() ? 1 : 0));
		}
		// 電話連絡
		if (!callFlagAll.isSelected()){
		    sql.append("     and call_flag = "  + (callFlagOK.isSelected() ? 1 : 0));
		}
		// PCメールアドレス
		if (!pcMailAll.isSelected()){
                    if (pcMailExists.isSelected()) {
                        sql.append(" and coalesce(pc_mail_address, '') != ''");
                    } else {
                        sql.append(" and coalesce(pc_mail_address, '') = ''");
                    }
		}
		// 携帯メールアドレス
		if (!cellularMailAll.isSelected()){
                    if (cellularMailExists.isSelected()) {
                        sql.append(" and coalesce(cellular_mail_address, '') != ''");
                    } else {
                        sql.append(" and coalesce(cellular_mail_address, '') = ''");
                    }
		}
		// SOSIA連動
		if( SystemInfo.isSosiaGearEnable() ) {
                    if (!sosiaGearAll.isSelected()){
                        if (sosiaGearExists.isSelected()) {
                            MobileMemberList mobileMemberList = new MobileMemberList();
                            mobileMemberList.setSosiaCode(null);
                            mobileMemberList.setAddDateFrom(null);
                            mobileMemberList.setAddDateTo(null);
                            mobileMemberList.setGearCondition(null);
                            mobileMemberList.setSosiaCode(SystemInfo.getSosiaCode());
                            if (sosiaGearExists.isSelected()) {
                                mobileMemberList.setGearCondition(1);
                            }
                            mobileMemberList.load();
                            StringBuilder customerIdList = new StringBuilder(1000);
                            for (MobileMemberData mmd : mobileMemberList) {
                                customerIdList.append(",");
                                customerIdList.append(mmd.getCustomerID());
                            }
                            if (customerIdList.length() > 0) {
                                sql.append(" and t.customer_id in (");
                                sql.append(customerIdList.toString().substring(1));
                                sql.append(" )");
                            }

                            sql.append(" and coalesce(sosia_id, 0) > 0");

                        } else {
                            sql.append(" and coalesce(sosia_id, 0) = 0");
                        }
                    }
                }

		// 誕生日の始まりを条件に含める
		if (birthdayFrom.getDateStr() != null) {
		    sql.append("     and birthday >= '" + birthdayFrom.getDateStrWithFirstTime() + "'");
		}
		// 誕生日の終わりを条件に含める
		if (birthdayTo.getDateStr() != null) {
		    sql.append("     and birthday <= '" + birthdayTo.getDateStrWithLastTime() + "'");
		}

		// 顧客No.
                if (!customerNo1.getText().equals("")) {
                    String s = customerNo1.getText();
                    if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                        sql.append(" and translate(customer_no, '0123456789', '') = ''");
                        sql.append(" and customer_no::text::numeric >= " + s);
                    } else {
                        sql.append(" and customer_no >= '" + s + "'");
                    }
                }
                if (!customerNo2.getText().equals("")) {
                    String s = customerNo2.getText();
                    if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                        sql.append(" and translate(customer_no, '0123456789', '') = ''");
                        sql.append(" and customer_no::text::numeric <= " + s);
                    } else {
                        sql.append(" and customer_no <= '" + s + "'");
                    }
                }
            
		// ふりがな１
		if (!customerKana1.getText().equals("")) {
		    sql.append("     and customer_kana1 like '%" + customerKana1.getText() + "%'");
		}

		// ふりがな２
		if (!customerKana2.getText().equals("")) {
		    sql.append("     and customer_kana2 like '%" + customerKana2.getText() + "%'");
		}
            
		// 氏名１
		if (!customerName1.getText().equals("")) {
		    sql.append("     and customer_name1 like '%" + customerName1.getText() + "%'");
		}

		// 氏名２
		if (!customerName2.getText().equals("")) {
		    sql.append("     and customer_name2 like '%" + customerName2.getText() + "%'");
		}
            
		// 住所対象
		if (addEnd.isSelected()){
		    sql.append("     and");
		    sql.append("     (");
		    sql.append("          coalesce(postal_code, '') != ''");
		    sql.append("       or (coalesce(address1, '') || coalesce(address2, '') || coalesce(address3, '') != '')");
		    sql.append("     )");
		}
		if (addYet.isSelected()){
		    sql.append("     and");
		    sql.append("     (");
		    sql.append("           coalesce(postal_code, '') = ''");
		    sql.append("       and (coalesce(address1, '') || coalesce(address2, '') || coalesce(address3, '') = '')");
		    sql.append("     )");
		}

		// 郵便番号
		if (!this.getPostalCode().equals("")) {
		    sql.append("     and postal_code like '" + this.getPostalCode() + "%'");
		}

		// 都道府県
		if (0 < prefecture.getSelectedIndex()) {
		    sql.append("     and address1 = '" + prefecture.getSelectedItem().toString() + "'");
		}

		// 市区町村
		if (0 < city.getSelectedIndex()) {
		    sql.append("     and address2 = '" + city.getSelectedItem().toString() + "'");
		}

		// 職業
		if (0 < job.getSelectedIndex()) {
		    MstJob mj = (MstJob)job.getSelectedItem();
		    sql.append("     and job_id = " + mj.getJobID().toString());
		}

		// 備考
		if (!memo.getText().equals("")) {
		    sql.append("     and note like '%" + memo.getText()+"%'");
		}

		// 初回来店動機
		if (firstComingMotive.getSelectedIndex() > 0){
		    MstFirstComingMotive motive = (MstFirstComingMotive)firstComingMotive.getSelectedItem();
		    sql.append("     and first_coming_motive_class_id = " + SQLUtil.convertForSQL(motive.getFirstComingMotiveClassId()));
		}

                // 初回来店日
                if (dateType3.isSelected()) {
                    if (visitDateFrom.getDateStr() != null) {
                        sql.append(" and " + firstVisitDateSql.toString() + " >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                        sql.append(" and coalesce(t.before_visit_num, 0) < 1");
                    }
                    if (visitDateTo.getDateStr() != null) {
                        sql.append(" and " + firstVisitDateSql.toString() + " <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                        sql.append(" and coalesce(t.before_visit_num, 0) < 1");
                    }
                }

	    sql.append("     )");

	    /**********************/
	    /* 来店日付 */
	    /**********************/
            
            // 初回来店日かつ以下の条件が指定されている場合は売上情報も検索する
            boolean isFirstVisitDateAndSalesCondition = false;
            if (dateType3.isSelected()) {
                //主担当者
                if (chargeStaff.getSelectedIndex() > 0) {
                    isFirstVisitDateAndSalesCondition = true;
                }
                // 主担当指名区分
                if (chargeStaffNamed.isSelected()) {
                    isFirstVisitDateAndSalesCondition = true;
                }
                if (chargeStaffFree.isSelected()) {
                    isFirstVisitDateAndSalesCondition = true;
                }
                if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
                    // 技術分類
                    if (this.isCondTechClassSelected()) {
                        isFirstVisitDateAndSalesCondition = true;
                    }
                    // 技術
                    if (this.isCondTechSelected()) {
                        isFirstVisitDateAndSalesCondition = true;
                    }
                }
                if (this.isCondItemClassSelected() || this.isCondItemSelected()) {
                    // 商品分類
                    if (this.isCondItemClassSelected()) {
                        isFirstVisitDateAndSalesCondition = true;
                    }
                    // 商品
                    if (this.isCondItemSelected()) {
                        isFirstVisitDateAndSalesCondition = true;
                    }
                }
                // 反響項目
                if (0 < responseItem.getSelectedIndex()) {
                    isFirstVisitDateAndSalesCondition = true;
                }
            }

	    if(isSalesCondition && (dateType0.isSelected() || isFirstVisitDateAndSalesCondition)) {
		
		sql.append("     and exists");
		sql.append("     (");

		    sql.append("     select 1");
		    sql.append("     from");
		    sql.append("         view_data_sales_detail_valid_with_prepaid d");
		    sql.append("             left outer join mst_technic mt");
		    sql.append("                          on d.product_division = 1");
		    sql.append("                         and d.product_id = mt.technic_id");
		    sql.append("             left outer join mst_item mi");
		    sql.append("                          on d.product_division = 2");
		    sql.append("                         and d.product_id = mi.item_id");
                    sql.append("             left outer join mst_course mc");
		    sql.append("                          on d.product_division = 5");
		    sql.append("                         and d.product_id = mc.course_id");
		    sql.append("     where");
		    sql.append("             d.shop_id in (" + shopIDList + ")");
		    sql.append("         and d.customer_id = t.customer_id");
		    
		    // 来店日付
		    if (visitDateFrom.getDateStr() != null) {
			sql.append("     and d.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
			// 初回来店日を含む場合
			if (dateType3.isSelected()) {
			    sql.append(" and " + firstVisitDateSql.toString() + " >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
			    sql.append(" and coalesce(t.before_visit_num, 0) < 1");
			}
		    }
		    if (visitDateTo.getDateStr() != null) {
			sql.append("     and d.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
			// 初回来店日を含む場合
			if (dateType3.isSelected()) {
			    sql.append(" and " + firstVisitDateSql.toString() + " <= '" + visitDateTo.getDateStrWithLastTime() + "'");
			    sql.append(" and coalesce(t.before_visit_num, 0) < 1");
			}
		    }

		    //主担当者
		    if (chargeStaff.getSelectedIndex() > 0) {
			sql.append("     and d.staff_id = " + ((MstStaff)chargeStaff.getSelectedItem()).getStaffID());
		    }

		    // 主担当指名区分
		    if (chargeStaffNamed.isSelected()) {
			sql.append("     and d.designated_flag = true");
		    }
		    if (chargeStaffFree.isSelected()) {
			sql.append("     and d.designated_flag = false");
		    }

                    if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected() 
                            || this.isCondTechClassSelected() || this.isCondTechSelected()
                            || this.isCondCourseClassSelected() || this.isCondCourseSelected()
                            )) {
                        sql.append("         and (");
                    }
		    if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
                        if (techItemOR.isSelected()) {
                            //sql.append("         and (");
                            //if (this.isCondItemClassSelected() || this.isCondItemSelected()) {
                            //    sql.append("         (");
                            //}
                            // 技術分類
                            if (this.isCondTechClassSelected()) {
                                    sql.append(getCondTechClassCondition("d"));
                            }
                            // 技術
                            if (this.isCondTechSelected()) {
                                    if(this.isCondTechClassSelected()) {
                                            sql.append("         or ");
                                    }
                                    sql.append(getCondTechCondition("d"));
                            }
                            //sql.append("         )");
                        }else {
                            // 技術分類
                            if (this.isCondTechClassSelected()) {
                                sql.append(getCondTechClassCondition("d"));
                            }
                            // 技術
                            if (this.isCondTechSelected()) {
                                sql.append(getCondTechCondition("d"));
                            }
                        }
		    }

		    if (this.isCondItemClassSelected() || this.isCondItemSelected()) {
                        
                        if (techItemOR.isSelected()) {
                            if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected())) {
                                sql.append(" or (");
                            }else {
                                sql.append("    (");
                            }
                            // 商品分類
                            if (this.isCondItemClassSelected()) {
                                    sql.append(getCondItemClassCondition("d"));
                            }
                            // 商品
                            if (this.isCondItemSelected()) {
                                if(this.isCondItemClassSelected()) {
                                    sql.append("         or ");
                                }
                                sql.append(getCondItemCondition("d"));
                            }

                            sql.append("         )");
                            //if (this.isCondTechClassSelected() || this.isCondTechSelected()) {
                            //    sql.append("     )");
                            //}
                        }else{
                            // 商品分類
                            if (this.isCondItemClassSelected()) {
                                sql.append(getCondItemClassCondition("d"));
                            }
                            // 商品
                            if (this.isCondItemSelected()) {
                                sql.append(getCondItemCondition("d"));
                            }
                        }
		    }
                    
                    //コース
                    if (this.isCondCourseClassSelected() || this.isCondCourseSelected()) {
                        
                        if (techItemOR.isSelected()) {
                            if (techItemOR.isSelected() && (this.isCondTechClassSelected() || this.isCondTechSelected()
                                    ||this.isCondItemClassSelected() || this.isCondItemSelected())) {
                                sql.append(" or (");
                            }else {
                                sql.append("         (");
                            }
                            // コース分類
                            if (this.isCondCourseClassSelected()) {
                                    sql.append(getCondCourseClassCondition("d"));
                            }
                            // コース
                            if (this.isCondCourseSelected()) {
                                if(this.isCondCourseClassSelected()) {
                                    sql.append("         or ");
                                }
                                sql.append(getCondCourseCondition("d"));
                            }
                            sql.append("         )");
                            
                        }else{
                            // コース分類
                            if (this.isCondCourseClassSelected()) {
                                sql.append(getCondCourseClassCondition("d"));
                            }
                            // コース
                            if (this.isCondCourseSelected()) {
                                sql.append(getCondCourseCondition("d"));
                            }
                        }
		    }
                    
                    if (techItemOR.isSelected() && (this.isCondItemClassSelected() || this.isCondItemSelected() 
                            || this.isCondTechClassSelected() || this.isCondTechSelected()
                            || this.isCondCourseClassSelected() || this.isCondCourseSelected()
                            )) {
                        sql.append("         ) ");
                    }
                    //IVS_LVTu end add 2016/03/16 getCondTechClassCondition
                    
                    // 反響項目
                    if (0 < responseItem.getSelectedIndex()) {
                        sql.append("            and exists");
                        sql.append("            (");
                        sql.append("                select 1");
                        sql.append("                from");
                        sql.append("                    data_response_effect");
                        sql.append("                where");
                        sql.append("                        delete_date is null");
                        sql.append("                    and shop_id = d.shop_id");
                        sql.append("                    and slip_no = d.slip_no");
                        sql.append("                    and response_id = " + SQLUtil.convertForSQL(((MstResponse)responseItem.getSelectedItem()).getResponseID()));
                        sql.append("            )");
                    }

		sql.append("     )");
	    }
	    
	    // 来店回数
	    if (!visitCountFrom.getText().equals("")) {
		sql.append("     and " + visitNumSql.toString() + " >= " + visitCountFrom.getText());
	    }
	    if (!visitCountTo.getText().equals("")) {
		sql.append("     and " + visitNumSql.toString() + " <= " + visitCountTo.getText());
	    }

	    // 売上金額
	    if (!priceFrom.getText().equals("")) {
		sql.append("     and " + sumTotalSql.toString() + " >= " + priceFrom.getText());
	    }
	    if (!priceTo.getText().equals("")) {
		sql.append("     and " + sumTotalSql.toString() + " <= " + priceTo.getText());
	    }
	    
	    /**********************/
	    /* 次回予約日 */
	    /**********************/
	    if(dateType2.isSelected()) {
		sql.append("     and exists");
		sql.append("     (");
		sql.append("         select 1");
		sql.append("         from");
		sql.append("             data_reservation as dr");
		sql.append("                 inner join data_reservation_detail drd");
		sql.append("                     using(shop_id, reservation_no)");
		sql.append("         where");
		sql.append("                 dr.delete_date is null");
		sql.append("             and drd.delete_date is null");
		sql.append("             and drd.reservation_datetime > now()");
		sql.append("             and dr.shop_id in (" + shopIDList + ")");
		sql.append("             and dr.customer_id = t.customer_id");
		
		//主担当者
		if (chargeStaff.getSelectedIndex() > 0) {
		    sql.append("         and dr.staff_id = " + ((MstStaff)chargeStaff.getSelectedItem()).getStaffID());
		}

		// 主担当指名区分
		if (chargeStaffNamed.isSelected()) {
		    sql.append("         and dr.designated_flag = true");
		}
		if (chargeStaffFree.isSelected()) {
		    sql.append("         and dr.designated_flag = false");
		}

		sql.append("         having true");

		if (visitDateFrom.getDateStr() != null) {
		    sql.append("         and min(reservation_datetime) >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
		}
		if (visitDateTo.getDateStr() != null) {
		    sql.append("         and min(reservation_datetime) <= '" + visitDateTo.getDateStrWithLastTime() + "'");
		}
		sql.append("     )");
	    }	    

            // ふりがな順で出力（未設定は最後に出力）
            sql.append(" order by");
            sql.append("      case when length(coalesce(t.customer_kana1, '')) > 0 then 0 else 1 end");
            sql.append("     ,t.customer_kana1");
            sql.append("     ,t.customer_kana2");
            
	    System.out.println(sql);
	    
	    return sql.toString();
	}

    /**
     * 入力されている郵便番号を取得する。
     *
     * @return 入力されている郵便番号
     */
    private String getPostalCode() {
        return this.postalCode.getText().replaceAll("[-_]", "");
    }

    private void initComponentData() {
        this.initPrefecture();
        this.setCity();
        this.initJob();
        chargeStaff.addItem(new MstStaff());
        SystemInfo.initStaffComponent(chargeStaff);
        chargeStaff.setSelectedIndex(0);
        setListener();

        // 初回来店動機の設定
        firstComingMotive.addItem("");
        java.util.List<MstFirstComingMotive> motiveList = MstFirstComingMotive.getAll(SystemInfo.getConnection());
        for (int i = 0; i < motiveList.size(); i++) {
            firstComingMotive.addItem(motiveList.get(i));
        }

        // フリー項目の設定
        MstFreeHeadingClasses freeHeading = new MstFreeHeadingClasses();
        freeHeading.load();
        /*
        free1.addItem("");
        free2.addItem("");
        free3.addItem("");
        free4.addItem("");

        free1.setVisible(false);
        free2.setVisible(false);
        free3.setVisible(false);
        free4.setVisible(false);

        jLabel36.setText("");
        jLabel37.setText("");
        jLabel38.setText("");
        jLabel39.setText("");

        JComboBox[] freeArray = {free1, free2, free3, free4};
        JLabel[] freeLabel = {jLabel36, jLabel37, jLabel38, jLabel39};

        int tmp = 0;
        for (int j = 0; j < 4; j++) {
            try {
                MstFreeHeadingClass mfhc = freeHeading.get(j);
                if (mfhc.getUseFlg()) {
                    freeLabel[tmp].setText(mfhc.getFreeHeadingClassName());
                    MstFreeHeadingUnit mfhu = new MstFreeHeadingUnit(mfhc.getFreeHeadingClassID(), mfhc.getFreeHeadingClassName());
                    mfhu.load();
                    freeArray[tmp].setVisible(true);
                    for (int i = 0; i < mfhu.size(); i++) {
                        freeArray[tmp].addItem(mfhu.get(i));
                    }
                    tmp++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // ４つ以下のときは強制的に終了
                break;
            }
        }*/
    }

    /**
     * コンポーネントの各リスナーをセットする。
     */
    private void setListener() {
        chargeStaffNo.addKeyListener(SystemInfo.getMoveNextField());
        chargeStaffNo.addFocusListener(SystemInfo.getSelectText());
    }

    private void initPrefecture() {
        prefecture.addItem("");

        try {
            ConnectionWrapper con = SystemInfo.getBaseConnection();

            ResultSetWrapper rs = con.executeQuery("select *\n"
                    + "from mst_prefecture\n"
                    + "order by prefecture_code\n");

            while (rs.next()) {
                prefecture.addItem(rs.getString("prefecture_name"));
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void setCity() {
        city.removeAllItems();
        city.addItem("");

        if (0 < prefecture.getSelectedIndex()) {
            try {
                ConnectionWrapper con = SystemInfo.getBaseConnection();

                ResultSetWrapper rs = con.executeQuery("select distinct code, city_name\n"
                        + "from mst_postal_code\n"
                        + "where prefecture_name = '" + prefecture.getSelectedItem().toString() + "'\n"
                        + "order by code\n");

                while (rs.next()) {
                    city.addItem(rs.getString("city_name"));
                }

                rs.close();
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }

    private void initJob() {
        job.addItem("");

        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            ResultSetWrapper rs = con.executeQuery(MstJob.getSelectAllSQL());

            while (rs.next()) {
                MstJob mj = new MstJob();

                mj.setData(rs);

                job.addItem(mj);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private boolean setCustomerArrayList(int sendType) {
        return setCustomerArrayList(sendType, 0);
    }

    private boolean setCustomerArrayList(int sendType, int outputType) {
        cusArray.clear();

        int age1 = 0;
        int age2 = 0;

        try {
            if (!"".equals(ageFrom.getText())) {
                age1 = Integer.parseInt(ageFrom.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "年齢"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            if (!"".equals(ageTo.getText())) {
                age2 = Integer.parseInt(ageTo.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "年齢"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!ageFrom.getText().equals("") && !ageTo.getText().equals("") && age1 > age2) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1101, "年齢の範囲"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int count1 = 0;
        int count2 = 0;
        try {
            if (!"".equals(visitCountFrom.getText())) {
                count1 = Integer.parseInt(visitCountFrom.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "来店回数"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            if (!"".equals(visitCountTo.getText())) {
                count2 = Integer.parseInt(visitCountTo.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "来店回数"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!visitCountFrom.getText().equals("") && !visitCountTo.getText().equals("") && count1 > count2) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1101, "来店回数の範囲"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int price1 = 0;
        int price2 = 0;
        try {
            if (!"".equals(priceFrom.getText())) {
                price1 = Integer.parseInt(priceFrom.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "売上金額"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            if (!"".equals(priceTo.getText())) {
                price2 = Integer.parseInt(priceTo.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "売上金額"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!priceFrom.getText().equals("") && !priceTo.getText().equals("") && price1 > price2) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1101, "売上金額の範囲"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            String shopIDList = "";
            if (target.getSelectedItem() instanceof MstGroup) {
                MstGroup mg = (MstGroup) target.getSelectedItem();
                shopIDList = mg.getShopIDListAll();
            } else {
                MstShop ms = (MstShop) target.getSelectedItem();
                shopIDList = ms.getShopID().toString();
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            /**
             * *************************
             */
            /* 顧客情報抽出SQL生成 */
            /**
             * *************************
             */
            // 来店日付で期間指定なしの場合は、確認メッセージを表示する。
            boolean isInNonSalesCustomer = false;
            if (outputType == OutputExcelDialog.OUTPUT_CUSTOMER) {
                if ((dateType0.isSelected() || dateType3.isSelected()) && visitDateFrom.getDateStr() == null && visitDateTo.getDateStr() == null) {
                    if (MessageDialog.showYesNoDialog(this,
                            "来店の無い顧客も含めますか？",
                            this.getTitle(),
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        isInNonSalesCustomer = true;
                    }
                }
            }

            ResultSetWrapper rs = con.executeQuery(this.sqlBuild(sendType, shopIDList, isInNonSalesCustomer, outputType));

            while (rs.next()) {
                SearchResultInfo result = new SearchResultInfo();
               // result.setData(rs, con, (target.getSelectedItem() instanceof MstGroup));
                //setRadioDatetype
                result.setData(rs, con, (target.getSelectedItem() instanceof MstGroup),numFreeHeadingClass);
                cusArray.add(result);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        if (0 < cusArray.size()) {
            return true;
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(8000),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private boolean setCustomerDigitalPost(int sendType, int outputType) {
        cusArray.clear();
        /*
        int age1 = 0;
        int age2 = 0;

        try {
            if (!"".equals(ageFrom.getText())) {
                age1 = Integer.parseInt(ageFrom.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "年齢"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            if (!"".equals(ageTo.getText())) {
                age2 = Integer.parseInt(ageTo.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "年齢"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!ageFrom.getText().equals("") && !ageTo.getText().equals("") && age1 > age2) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1101, "年齢の範囲"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int count1 = 0;
        int count2 = 0;
        try {
            if (!"".equals(visitCountFrom.getText())) {
                count1 = Integer.parseInt(visitCountFrom.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "来店回数"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            if (!"".equals(visitCountTo.getText())) {
                count2 = Integer.parseInt(visitCountTo.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "来店回数"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!visitCountFrom.getText().equals("") && !visitCountTo.getText().equals("") && count1 > count2) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1101, "来店回数の範囲"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int price1 = 0;
        int price2 = 0;
        try {
            if (!"".equals(priceFrom.getText())) {
                price1 = Integer.parseInt(priceFrom.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "売上金額"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            if (!"".equals(priceTo.getText())) {
                price2 = Integer.parseInt(priceTo.getText());
            }
        } catch (NumberFormatException e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1103, "売上金額"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!priceFrom.getText().equals("") && !priceTo.getText().equals("") && price1 > price2) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1101, "売上金額の範囲"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        */
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            String shopIDList = "";
            if (target.getSelectedItem() instanceof MstGroup) {
                MstGroup mg = (MstGroup) target.getSelectedItem();
                shopIDList = mg.getShopIDListAll();
            } else {
                MstShop ms = (MstShop) target.getSelectedItem();
                shopIDList = ms.getShopID().toString();
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            /**
             * *************************
             */
            /* 顧客情報抽出SQL生成 */
            /**
             * *************************
             */
            // 来店日付で期間指定なしの場合は、確認メッセージを表示する。
            /*
            boolean isInNonSalesCustomer = false;
            if (outputType == OutputExcelDialog.OUTPUT_CUSTOMER) {
                if ((dateType0.isSelected() || dateType3.isSelected()) && visitDateFrom.getDateStr() == null && visitDateTo.getDateStr() == null) {
                    if (MessageDialog.showYesNoDialog(this,
                            "来店の無い顧客も含めますか？",
                            this.getTitle(),
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        isInNonSalesCustomer = true;
                    }
                }
            }
            */
            ResultSetWrapper rs = con.executeQuery(this.sqlBuild(sendType, shopIDList, false, outputType));

            while (rs.next()) {
                SearchResultInfo result = new SearchResultInfo();
               // result.setData(rs, con, (target.getSelectedItem() instanceof MstGroup));
                //setRadioDatetype
                result.setData(rs, con, (target.getSelectedItem() instanceof MstGroup),numFreeHeadingClass);
                cusArray.add(result);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        if (0 < cusArray.size()) {
            return true;
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(8000),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean optimizeSelectedArray(Integer optimizeType) {
        selectedArray.clear();
        Integer cnt = 0;

        for (MstCustomer mc : cusArray) {

            boolean isAdd = true;

            switch (optimizeType) {
                case 0:
                case 1:
                    if ((mc.getPCMailAddress() == null || mc.getPCMailAddress().equals(""))
                            && mc.getCellularMailAddress().equals("")) {
                        isAdd = false;
                    }
                    break;
                case 2:
                    if (mc.getPCMailAddress() == null || mc.getPCMailAddress().equals("")) {
                        isAdd = false;
                    }
                    break;
                case 3:
                    if (mc.getCellularMailAddress() == null || mc.getCellularMailAddress().equals("")) {
                        isAdd = false;
                    }
                    break;
                case 4:
                    if (mc.getFullAddress() == null || mc.getFullAddress().equals("")) {
                        isAdd = false;
                    }
                    break;
            }

            if (isAdd) {
                selectedArray.add(mc);
            }
        }

        if ((0 < selectedArray.size())) {
            return true;
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(8000),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * シンプルマスタ登録画面用FocusTraversalPolicy
     */
    private class HairMailSearchFocusTraversalPolicy
            extends FocusTraversalPolicy {

        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。 aContainer は aComponent
         * のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         *
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は
         * null
         */
      public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
                              if (aComponent.equals(target)) {
                                    return getSelectedDateType();
                                } else if (aComponent.equals(dateType0)) {
                                    return visitDateFrom;
                                } else if (aComponent.equals(dateType1)) {
                                    return visitDateFrom;
                                } else if (aComponent.equals(dateType2)) {
                                    return visitDateFrom;
                                } else if (aComponent.equals(dateType3)) {
                                    return visitDateFrom;
                                } else if (aComponent.equals(visitDateFrom)) {
                                    return visitDateTo;
                                } else if (aComponent.equals(visitDateTo)) {
                                    return this.getSelectedSex();
                                } else if (aComponent.equals(male)) {
                                    return ageFrom;
                                } else if (aComponent.equals(female)) {
                                    return ageFrom;
                                } else if (aComponent.equals(both)) {
                                    return ageFrom;
                                } else if (aComponent.equals(ageFrom)) {
                                    return ageTo;
                                } else if (aComponent.equals(ageTo)) {
                                    return birthMonth;
                                } else if (aComponent.equals(birthMonth)) {
                                    return birthdayFrom;
                                } else if (aComponent.equals(birthdayFrom)) {
                                    return birthdayTo;
                                } else if (aComponent.equals(birthdayTo)) {
                                    return this.getSelectedSendMail();
                                } else if (aComponent.equals(sendMailOK)) {
                                    return this.getSelectedSendDM();
                                } else if (aComponent.equals(sendMailNG)) {
                                    return this.getSelectedSendDM();
                                } else if (aComponent.equals(sendMailAll)) {
                                    return this.getSelectedSendDM();
                                } else if (aComponent.equals(sendDmOK)) {
                                    return getSelectedCallFlag();
                                } else if (aComponent.equals(sendDmNG)) {
                                    return getSelectedCallFlag();
                                } else if (aComponent.equals(sendDmAll)) {
                                    return getSelectedCallFlag();
                                } else if (aComponent.equals(callFlagOK) && dateType3.isSelected()) {
                                    return priceFrom;
                                } else if (aComponent.equals(callFlagNG) && dateType3.isSelected()) {
                                    return priceFrom;
                                } else if (aComponent.equals(callFlagAll) && dateType3.isSelected()) {
                                    return priceFrom;
                                } else if (aComponent.equals(callFlagOK)) {
                                    return visitCountFrom;
                                } else if (aComponent.equals(callFlagNG)) {
                                    return visitCountFrom;
                                } else if (aComponent.equals(callFlagAll)) {
                                    return visitCountFrom;
                                } else if (aComponent.equals(visitCountFrom)) {
                                    return visitCountTo;
                                } else if (aComponent.equals(visitCountTo)) {
                                    return getSelectedVisitCount();
                                } else if (aComponent.equals(visitCountType0) && dateType2.isSelected()) {
                                    return isIntroducer;
                                } else if (aComponent.equals(visitCountType0)) {
                                    return priceFrom;
                                } else if (aComponent.equals(visitCountType1)) {
                                    return priceFrom;
                                } else if (aComponent.equals(priceFrom)) {
                                    return priceTo;
                                } else if (aComponent.equals(priceTo)) {
                                    return isIntroducer;
                                } else if (aComponent.equals(isIntroducer)) {
                                    return isIntroduced;
                                } else if (aComponent.equals(isIntroduced)) {
                                    return customerNo1;

                                } else if (aComponent.equals(customerNo1)) {
                                    return customerNo2;
                                } else if (aComponent.equals(customerNo2)) {
                                    return customerKana1;
                                } else if (aComponent.equals(customerKana1)) {
                                    return customerKana2;
                                } else if (aComponent.equals(customerKana2)) {
                                    return customerName1;
                                } else if (aComponent.equals(customerName1)) {
                                    return customerName2;
                                } else if (aComponent.equals(customerName2)) {
                                    return getSelectedadd();
                                } else if (aComponent.equals(addAll)) {
                                    return postalCode;
                                } else if (aComponent.equals(addEnd)) {
                                    return postalCode;
                                } else if (aComponent.equals(addYet)) {
                                    return postalCode;
                                } else if (aComponent.equals(postalCode)) {
                                    return prefecture;
                                } else if (aComponent.equals(prefecture)) {
                                    return city;
                                } else if (aComponent.equals(city)) {
                                    return job;
                                } else if (aComponent.equals(job)) {
                                    return memo;
                                } else if (aComponent.equals(memo)) {
                                    return condName;
                                } else if (aComponent.equals(condName)) {
                                    return firstComingMotive;
                                } else if (aComponent.equals(firstComingMotive)) {
                                    for (int i = 0; i < mfhcs.size(); i++) {
                                        MstFreeHeadingClass mfhc = mfhcs.get(i);
                                        if (mfhc.getUseFlg()) {
                                            if (mfhc.getInput_type() == 0) {
                                                return mfhsups.get(0).getComponent(1);
                                            } else {
                                                return mfhsuptext.get(0).getComponent(1);
                                            }
                                        }
                                    }

                                    return chargeStaffNo;


                                } else if (mfhcs.size() > 0 && !aComponent.equals(chargeStaffNo) && !aComponent.equals(chargeStaff) && !aComponent.equals(getSelectedchargeStaff()) && !aComponent.equals(responseItem)  && !aComponent.equals(getSelectedCellularMail()) && !aComponent.equals(getSelectedMailExists()) && !aComponent.equals(getSelectedSosiaGear()) && !aComponent.equals(target)) {
                                    int iC = 0;
                                    int iT = 0;
                                    for (int i = 0; i < mfhcs.size(); i++) {
                                        MstFreeHeadingClass mfhc = mfhcs.get(i);
                                        if (i == 9) {
                                            continue;
                                        }
                                        if (mfhc.getUseFlg()) {
                                            if (mfhc.getInput_type() == 0) {
                                                if (aComponent.equals(mfhsups.get(iC).getComponent(1))) {
                                                    if (mfhcs.get(i + 1).getInput_type() == 0 && iC + 1 < mfhsups.size()) {
                                                        return mfhsups.get(iC + 1).getComponent(1);
                                                    } else if (mfhcs.get(i + 1).getInput_type() == 1 && iT < mfhsups.size()) {
                                                        iT++;
                                                        return mfhsuptext.get(iT - 1).getComponent(1);
                                                    }
                                                }
                                                iC++;
                                            } else {
                                                if (aComponent.equals(mfhsuptext.get(iT).getComponent(1))) {
                                                    if (mfhcs.get(i + 1).getInput_type() == 0 && iC < mfhsups.size()) {
                                                        iC++;
                                                        return mfhsups.get(iC - 1).getComponent(1);
                                                    } else if (mfhcs.get(i + 1).getInput_type() == 1 && iT + 1 < mfhsuptext.size()) {
                                                        return mfhsuptext.get(iT + 1).getComponent(1);
                                                    }
                                                }
                                                iT++;
                                            }
                                        }

                                    }

                                    return chargeStaffNo;
                                } else if (aComponent.equals(chargeStaffNo)) {
                                    return chargeStaff;
                                } else if (aComponent.equals(chargeStaff)) {
                                    return getSelectedchargeStaff();
                                } else if (aComponent.equals(chargeStaffAll)) {
                                    return responseItem;
                                } else if (aComponent.equals(chargeStaffNamed)) {
                                    return responseItem;
                                } else if (aComponent.equals(chargeStaffFree)) {
                                    return responseItem;
                                } else if (aComponent.equals(responseItem)) {
                                    return  getSelectedMailExists();                  
                                } else if (aComponent.equals(pcMailExists)) {
                                    return getSelectedCellularMail();
                                } else if (aComponent.equals(pcMailNotExists)) {
                                    return getSelectedCellularMail();
                                } else if (aComponent.equals(pcMailAll)) {
                                    return getSelectedCellularMail();
                                } else if (aComponent.equals(cellularMailExists)) {
                                    if (sosiaGearPanel.isVisible()) {
                                        return getSelectedSosiaGear();
                                    }
                                    return getDefaultComponent();
                                } else if (aComponent.equals(cellularMailNotExists)) {
                                    if (sosiaGearPanel.isVisible()) {
                                        return getSelectedSosiaGear();
                                    }
                                    return getDefaultComponent();
                                } else if (aComponent.equals(cellularMailAll)) {
                                    if (sosiaGearPanel.isVisible()) {
                                        return getSelectedSosiaGear();
                                    }
                                    return getDefaultComponent();
                                } else if (aComponent.equals(sosiaGearExists)) {
                                    return getDefaultComponent();
                                } else if (aComponent.equals(sosiaGearNotExists)) {
                                    return getDefaultComponent();
                                } else if (aComponent.equals(sosiaGearAll)) {
                                    return getDefaultComponent();
                                }
                                return getDefaultComponent();
		}

		/**
		 * aComponent の前にフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentBefore(Container aContainer,
											Component aComponent)
		{
			if (aComponent.equals(getDefaultComponent()))
			{
				return responseItem;
			}
			if (aComponent.equals(target))
			{
				return responseItem;
			}
			else if (aComponent.equals(dateType0))
			{
				return target;
			}
			else if (aComponent.equals(dateType1))
			{
				return target;
			}
			else if (aComponent.equals(dateType2))
			{
				return target;
			}
			else if (aComponent.equals(dateType3))
			{
				return target;
			}
			else if (aComponent.equals(visitDateFrom))
			{
				return getSelectedDateType();
			}
			else if (aComponent.equals(visitDateTo))
			{
				return visitDateFrom;
			}
			else if (aComponent.equals(male))
			{
				return visitDateTo;
			}
			else if (aComponent.equals(female))
			{
				return visitDateTo;
			}
			else if (aComponent.equals(both))
			{
				return visitDateTo;
			}
			else if (aComponent.equals(ageFrom))
			{
				return getSelectedSex();
			}
			else if (aComponent.equals(ageTo))
			{
				return ageFrom;
			}
			else if (aComponent.equals(birthMonth))
			{
				return ageTo;
			}
			else if (aComponent.equals(birthdayFrom))
			{
				return birthMonth;
			}
			else if (aComponent.equals(birthdayTo))
			{
				return birthdayFrom;
			}
			else if (aComponent.equals(visitCountFrom))
			{
				return birthdayTo;
			}
                        else if (aComponent.equals(visitCountTo))
                        {
                                return visitCountFrom;
                        }
                        else if (aComponent.equals(visitCountType1))
                        {
                                return visitCountTo;
                        }
                        else if (aComponent.equals(priceFrom))
                        {
                                return this.getSelectedVisitCount();
                        }
                        else if (aComponent.equals(priceTo))
                        {
                                return priceFrom;
                        }
                        else if (aComponent.equals(isIntroducer))
                        {
                                return priceTo;
                        }
                        else if (aComponent.equals(isIntroduced))
                        {
                                return isIntroducer;
                        }
                        else if (aComponent.equals(customerKana2))
			{
				return customerKana1;
			}
                        else if (aComponent.equals(customerKana1))
			{
				return customerNo2;
                        }
                        else if (aComponent.equals(customerNo2))
			{
				return customerNo1;
			}
                        else if (aComponent.equals(customerNo1))
			{
				return isIntroduced;
			}
			else if (aComponent.equals(customerName2))
			{
				return customerName1;
			}
			else if (aComponent.equals(customerName1))
			{
				return customerKana2;
			}
                        else if (aComponent.equals(addAll))
                        {
                            return customerName2;
                        }
                        else if (aComponent.equals(addEnd))
                        {
                            return customerName2;
                        }
                        else if (aComponent.equals(addYet))
                        {
                            return customerName2;
                        }
			else if (aComponent.equals(postalCode))
			{
				return getSelectedadd();
			}
			else if (aComponent.equals(prefecture))
			{
				return postalCode;
			}
			else if (aComponent.equals(city))
			{
				return prefecture;
			}
			else if (aComponent.equals(job))
			{
				return city;
			}
			else if (aComponent.equals(memo))
			{
				return job;
			}
			else if (aComponent.equals(firstComingMotive))
			{
				return memo;
			}
                        /* comment
                        else if (aComponent.equals(free1))
			{
				return getBeforeFree(free1);
			}
                        else if (aComponent.equals(free2))
			{
				return getBeforeFree(free2);
			}
                        else if (aComponent.equals(free3))
			{
				return getBeforeFree(free3);
			}
                        else if (aComponent.equals(free4))
			{
				return getBeforeFree(free4);
			}
                        */ 
                        else if (aComponent.equals(chargeStaffNo))
			{
				return getBeforeFree(chargeStaffNo);
			}
                        else if (aComponent.equals(chargeStaff))
			{
				return chargeStaffNo;
			}
			
			return getDefaultComponent();
		}

                private Component getNextFree(Component c){
                    /* comment
                    if(c.equals(firstComingMotive) && free1.isVisible()){
                        return free1;
                    }else if( c.equals(free1) && free2.isVisible()){
                        return free2;
                    } else if(c.equals(free2) && free3.isVisible()){
                        return free3;
                    } else if(c.equals(free3) && free4.isVisible()){
                        return free4;
                    } else {
                        return chargeStaffNo;
                    }
                    */
                    return chargeStaffNo;
                }
                
                private Component getBeforeFree(Component c){
                    /* comment
                    if(c.equals(chargeStaffNo)){
                        if(free4.isVisible()){
                            return free4;
                        } else {
                            return getBeforeFree(free4);
                        }
                    }else if( c.equals(free4)){
                        if(free3.isVisible()){
                            return free3;
                        } else {
                            return getBeforeFree(free3);
                        }
                    } else if(c.equals(free3)){
                        if(free2.isVisible()){
                            return free2;
                        } else {
                            return getBeforeFree(free2);
                        }
                    } else if(c.equals(free2)){
                        if(free1.isVisible()){
                            return free1;
                        } else {
                            return getBeforeFree(free1);
                        }
                    } else {
                        return firstComingMotive;
                    }
                    * */
                    return firstComingMotive;
                }
                
		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return getDefaultComponent();
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return sosiaGearAll;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return getDefaultComponent();
		}
		
		/**
		 * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
		 * show() または setVisible(true) の呼び出しで一度ウィンドウが表示されると、
		 * 初期化コンポーネントはそれ以降使用されません。
		 * 一度別のウィンドウに移ったフォーカスが再び設定された場合、
		 * または、一度非表示状態になったウィンドウが再び表示された場合は、
		 * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
		 * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
		 * @param window 初期コンポーネントが返されるウィンドウ
		 * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
		 */
		public Component getInitialComponent(Window window)
		{
			return getDefaultComponent();
		}
		
		private Component getDefaultComponent()
		{
			if(1 < target.getItemCount())
			{
				return	target;
			}
			else
			{
				return this.getSelectedDateType();
			}
		}
                
                        private Component getSelectedVisitCount() {
                            if (visitCountType0.isSelected()) {
                                return visitCountType0;
                            } else {
                                return visitCountType1;
                            }
                        }

                        private Component getSelectedSex() {
                            if (male.isSelected()) {
                                return male;
                            } else if (female.isSelected()) {
                                return female;
                            }

                            return both;
                        }

                        private Component getSelectedDateType() {
                            if (dateType0.isSelected()) {
                                return dateType0;
                            } else if (dateType1.isSelected()) {
                                return dateType1;
                            } else if (dateType2.isSelected()) {
                                return dateType2;
                            }
                            return dateType3;
                        }

                        private Component getSelectedSendMail() {
                            if (sendMailOK.isSelected()) {
                                return sendMailOK;
                            } else if (sendMailNG.isSelected()) {
                                return sendMailNG;
                            }
                            return sendMailAll;
                        }

                        private Component getSelectedSendDM() {
                            if (sendDmOK.isSelected()) {
                                return sendDmOK;
                            } else if (sendDmNG.isSelected()) {
                                return sendDmNG;
                            }
                            return sendDmAll;
                        }

                        private Component getSelectedCallFlag() {
                            if (callFlagOK.isSelected()) {
                                return callFlagOK;
                            } else if (callFlagNG.isSelected()) {
                                return callFlagNG;
                            }
                            return callFlagAll;
                        }

                        private Component getSelectedchargeStaff() {
                            if (chargeStaffAll.isSelected()) {
                                return chargeStaffAll;
                            } else if (chargeStaffNamed.isSelected()) {
                                return chargeStaffNamed;
                            }
                            return chargeStaffFree;
                        }

                        private Component getSelectedMailExists() {
                            if (pcMailExists.isSelected()) {
                                return pcMailExists;
                            } else if (pcMailNotExists.isSelected()) {
                                return pcMailNotExists;
                            }
                            return pcMailAll;
                        }

                        private Component getSelectedCellularMail() {
                            if (cellularMailExists.isSelected()) {
                                return cellularMailExists;
                            } else if (cellularMailNotExists.isSelected()) {
                                return cellularMailNotExists;
                            }
                            return cellularMailAll;
                        }

                        private Component getSelectedSosiaGear() {
                            if (sosiaGearExists.isSelected()) {
                                return sosiaGearExists;
                            } else if (sosiaGearNotExists.isSelected()) {
                                return sosiaGearNotExists;
                            }
                            return sosiaGearAll;
                        }

                        private Component getSelectedadd() {
                            if (addAll.isSelected()) {
                                return addAll;
                            } else if (addEnd.isSelected()) {
                                return addEnd;
                            }
                            return addYet;
                        }
    }

    private void setItemEnabled(boolean mode) {

        if (this.priceFrom.isEnabled() && mode) {
            return;
        }

        this.priceFrom.setText("");
        this.priceTo.setText("");

        this.initProductClasses();

        this.responseItem.setSelectedIndex(0);

        this.jLabel24.setEnabled(mode);
        this.priceFrom.setEnabled(mode);
        this.jLabel26.setEnabled(mode);
        this.priceTo.setEnabled(mode);
        this.jLabel28.setEnabled(mode);

        this.technicClass.setEnabled(mode);
        this.technic.setEnabled(mode);

        this.itemClass.setEnabled(mode);
        this.item.setEnabled(mode);
    }

    private void registCondition() {

        if (condName.getText().replace("　", "").trim().length() < 1) {
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "条件名"),
                    "条件登録",
                    JOptionPane.ERROR_MESSAGE);

            condName.requestFocusInWindow();
            return;
        }

        ConnectionWrapper con = SystemInfo.getConnection();

        try {

            con.begin();

            StringBuilder sql = new StringBuilder(1000);

            // 新規条件ID取得
            int condId = 0;
            // Thanh start edit 2014/03/11
            sql.append("select coalesce(max(cond_id), 0) + 1 as cond_id from data_condition where shop_id = " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
            // Thanh end edit 2014/03/11
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                condId = rs.getInt("cond_id");
            }

            // 条件名登録
            sql.setLength(0);
            sql.append(" insert into data_condition(shop_id, cond_id, cond_name, insert_date)");
            sql.append(" values(");
            sql.append(SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
            sql.append(",");
            sql.append(SQLUtil.convertForSQL(condId));
            sql.append(",");
            sql.append(SQLUtil.convertForSQL(condName.getText()));
            sql.append(",current_timestamp");
            sql.append(");");
            con.execute(sql.toString());

            // 条件登録
            registConditionDetail(con, condId);

            con.commit();

            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(201),
                    "条件登録",
                    JOptionPane.INFORMATION_MESSAGE);

            condName.setText("");

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ignore) {
            }
            e.printStackTrace();
        }

        condName.requestFocusInWindow();
    }

    private void registConditionDetail(ConnectionWrapper con, int condId) throws SQLException {

        if (dateType0.isSelected()) {
            con.execute(getRegistCondSQL(condId, 100, "0"));
        }
        if (dateType1.isSelected()) {
            con.execute(getRegistCondSQL(condId, 100, "1"));
        }
        if (dateType2.isSelected()) {
            con.execute(getRegistCondSQL(condId, 100, "2"));
        }
        if (dateType3.isSelected()) {
            con.execute(getRegistCondSQL(condId, 100, "3"));
        }

        con.execute(getRegistCondSQL(condId, 110, visitDateFrom.getDateStr()));
        con.execute(getRegistCondSQL(condId, 112, visitDateTo.getDateStr()));

        if (female.isSelected()) {
            con.execute(getRegistCondSQL(condId, 120, "0"));
        }
        if (male.isSelected()) {
            con.execute(getRegistCondSQL(condId, 120, "1"));
        }
        if (both.isSelected()) {
            con.execute(getRegistCondSQL(condId, 120, "2"));
        }

        con.execute(getRegistCondSQL(condId, 130, ageFrom.getText()));
        con.execute(getRegistCondSQL(condId, 132, ageTo.getText()));

        con.execute(getRegistCondSQL(condId, 140, birthMonth.getSelectedItem().toString()));
        con.execute(getRegistCondSQL(condId, 142, birthdayFrom.getDateStr()));
        con.execute(getRegistCondSQL(condId, 144, birthdayTo.getDateStr()));

        if (sendMailOK.isSelected()) {
            con.execute(getRegistCondSQL(condId, 150, "0"));
        }
        if (sendMailNG.isSelected()) {
            con.execute(getRegistCondSQL(condId, 150, "1"));
        }
        if (sendMailAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 150, "2"));
        }

        if (sendDmOK.isSelected()) {
            con.execute(getRegistCondSQL(condId, 160, "0"));
        }
        if (sendDmNG.isSelected()) {
            con.execute(getRegistCondSQL(condId, 160, "1"));
        }
        if (sendDmAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 160, "2"));
        }

        if (callFlagOK.isSelected()) {
            con.execute(getRegistCondSQL(condId, 170, "0"));
        }
        if (callFlagNG.isSelected()) {
            con.execute(getRegistCondSQL(condId, 170, "1"));
        }
        if (callFlagAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 170, "2"));
        }

        con.execute(getRegistCondSQL(condId, 180, visitCountFrom.getText()));
        con.execute(getRegistCondSQL(condId, 182, visitCountTo.getText()));

        if (visitCountType0.isSelected()) {
            con.execute(getRegistCondSQL(condId, 190, "0"));
        }
        if (visitCountType1.isSelected()) {
            con.execute(getRegistCondSQL(condId, 190, "1"));
        }

        con.execute(getRegistCondSQL(condId, 200, priceFrom.getText()));
        con.execute(getRegistCondSQL(condId, 202, priceTo.getText()));

        con.execute(getRegistCondSQL(condId, 210, isIntroducer.isSelected() ? "1" : "0"));
        con.execute(getRegistCondSQL(condId, 212, isIntroduced.isSelected() ? "1" : "0"));

        con.execute(getRegistCondSQL(condId, 220, customerNo1.getText()));
        con.execute(getRegistCondSQL(condId, 222, customerNo2.getText()));

        con.execute(getRegistCondSQL(condId, 230, customerKana1.getText()));
        con.execute(getRegistCondSQL(condId, 232, customerKana2.getText()));

        con.execute(getRegistCondSQL(condId, 240, customerName1.getText()));
        con.execute(getRegistCondSQL(condId, 242, customerName2.getText()));

        if (addAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 250, "0"));
        }
        if (addEnd.isSelected()) {
            con.execute(getRegistCondSQL(condId, 250, "1"));
        }
        if (addYet.isSelected()) {
            con.execute(getRegistCondSQL(condId, 250, "2"));
        }

        con.execute(getRegistCondSQL(condId, 260, getPostalCode()));

        con.execute(getRegistCondSQL(condId, 270, prefecture.getSelectedItem().toString()));
        con.execute(getRegistCondSQL(condId, 280, city.getSelectedItem().toString()));

        con.execute(getRegistCondSQL(condId, 290, job.getSelectedItem().toString()));

        con.execute(getRegistCondSQL(condId, 300, memo.getText()));

        con.execute(getRegistCondSQL(condId, 310, firstComingMotive.getSelectedItem().toString()));

        /*con.execute(getRegistCondSQL(condId, 320, free1.getSelectedItem().toString()));
        con.execute(getRegistCondSQL(condId, 330, free2.getSelectedItem().toString()));
        con.execute(getRegistCondSQL(condId, 340, free3.getSelectedItem().toString()));
        con.execute(getRegistCondSQL(condId, 350, free4.getSelectedItem().toString()));*/
        for (MstFreeHeadingSelectTextFiled fhPanelText : mfhsuptext) {                
                switch (fhPanelText.getFreeHeadingClassID()){
                    case 1: 
                       con.execute(getRegistCondSQL(condId, 320, fhPanelText.getFreeHeadingText()));
                        break;
                    case 2: 
                       con.execute(getRegistCondSQL(condId, 330, fhPanelText.getFreeHeadingText()));
                        break;
                    case 3: 
                       con.execute(getRegistCondSQL(condId, 340, fhPanelText.getFreeHeadingText()));
                        break;
                    case 4: 
                       con.execute(getRegistCondSQL(condId, 350, fhPanelText.getFreeHeadingText()));
                        break;   
                    case 5: 
                       con.execute(getRegistCondSQL(condId, 460, fhPanelText.getFreeHeadingText()));
                        break;
                    case 6: 
                       con.execute(getRegistCondSQL(condId, 470, fhPanelText.getFreeHeadingText()));
                        break;
                    case 7: 
                       con.execute(getRegistCondSQL(condId, 480, fhPanelText.getFreeHeadingText()));
                        break;
                    case 8: 
                       con.execute(getRegistCondSQL(condId, 490, fhPanelText.getFreeHeadingText()));
                        break;     
                    case 9: 
                       con.execute(getRegistCondSQL(condId, 500, fhPanelText.getFreeHeadingText()));
                        break;
                    case 10: 
                       con.execute(getRegistCondSQL(condId, 510, fhPanelText.getFreeHeadingText()));
                        break;    
                }
            }

        con.execute(getRegistCondSQL(condId, 360, chargeStaff.getSelectedItem().toString()));

        if (chargeStaffAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 370, "0"));
        }
        if (chargeStaffNamed.isSelected()) {
            con.execute(getRegistCondSQL(condId, 370, "1"));
        }
        if (chargeStaffFree.isSelected()) {
            con.execute(getRegistCondSQL(condId, 370, "2"));
        }

        con.execute(getRegistCondSQL(condId, 420, responseItem.getSelectedItem().toString()));

        if (pcMailExists.isSelected()) {
            con.execute(getRegistCondSQL(condId, 430, "0"));
        }
        if (pcMailNotExists.isSelected()) {
            con.execute(getRegistCondSQL(condId, 430, "1"));
        }
        if (pcMailAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 430, "2"));
        }

        if (cellularMailExists.isSelected()) {
            con.execute(getRegistCondSQL(condId, 440, "0"));
        }
        if (cellularMailNotExists.isSelected()) {
            con.execute(getRegistCondSQL(condId, 440, "1"));
        }
        if (cellularMailAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 440, "2"));
        }

        if (sosiaGearExists.isSelected()) {
            con.execute(getRegistCondSQL(condId, 450, "0"));
        }
        if (sosiaGearNotExists.isSelected()) {
            con.execute(getRegistCondSQL(condId, 450, "1"));
        }
        if (sosiaGearAll.isSelected()) {
            con.execute(getRegistCondSQL(condId, 450, "2"));
        }


        // 技術・商品
        con.execute(getRegistCondSQL(condId, 10000, techItemOR.isSelected() ? "OR" : "AND"));

        int seqNo = 11000;
        for (Map.Entry<Integer, MapProduct> pc : condTechClass.entrySet()) {
            if (pc.getValue().checked) {
                con.execute(getRegistCondSQL(condId, seqNo, pc.getValue().getClassID() + "$$" + pc.getKey() + "$$" + pc.getValue().getName()));
                seqNo++;
            }

            for (Map.Entry<Integer, MapProduct> p : condTech.entrySet()) {
                if (p.getValue().getClassID().equals(pc.getKey()) && p.getValue().checked) {
                    con.execute(getRegistCondSQL(condId, seqNo, p.getValue().getClassID() + "$$" + p.getKey() + "$$" + p.getValue().getName()));
                    seqNo++;
                }
            }
        }

        seqNo = 12000;
        for (Map.Entry<Integer, MapProduct> pc : condItemClass.entrySet()) {
            if (pc.getValue().checked) {
                con.execute(getRegistCondSQL(condId, seqNo, pc.getValue().getClassID() + "$$" + pc.getKey() + "$$" + pc.getValue().getName()));
                seqNo++;
            }

            for (Map.Entry<Integer, MapProduct> p : condItem.entrySet()) {
                if (p.getValue().getClassID().equals(pc.getKey()) && p.getValue().checked) {
                    con.execute(getRegistCondSQL(condId, seqNo, p.getValue().getClassID() + "$$" + p.getKey() + "$$" + p.getValue().getName()));
                    seqNo++;
                }
            }
        }

    }

    private String getRegistCondSQL(int condId, int itemNo, String itemValue) {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into data_condition_detail(shop_id, cond_id, item_no, item_value)");
        sql.append(" values(");
        sql.append(SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
        sql.append(",");
        sql.append(SQLUtil.convertForSQL(condId));
        sql.append(",");
        sql.append(SQLUtil.convertForSQL(itemNo));
        sql.append(",");
        sql.append(SQLUtil.convertForSQL(itemValue));
        sql.append(");");

        return sql.toString();
    }

    private void setSelectedComboBox(JComboBox combo, String value) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).toString().equals(value)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void initProductClasses() {
        condTechClass.clear();
        condItemClass.clear();
        condTech.clear();
        condItem.clear();
        hint.setText("");

        try {

            ConnectionWrapper con = SystemInfo.getConnection();

            technicClasses.setProductDivision(1);
            technicClasses.load(con, SystemInfo.getCurrentShop().getShopID());

            itemClasses.setProductDivision(2);
            itemClasses.load(con, SystemInfo.getCurrentShop().getShopID());
            
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        this.setProductClasses(technicClasses, technicClass);
        this.setProductClasses(itemClasses, itemClass);

        this.showProducts(technicClass, technic);

        technic.getColumnModel().getColumn(0).setPreferredWidth(300);
        item.getColumnModel().getColumn(0).setPreferredWidth(300);

    }
    
    //IVS_LVTu start add 2016/03/16 getCondTechClassCondition
    //　コース　
    private void initCourseClasses() {

        condCourseClass.clear();
        condCourse.clear();

        String listShopID = this.listShopCourseFlag();
        try {

            ConnectionWrapper con = SystemInfo.getConnection();
            
            courseClasses.loadCourse(con, listShopID);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        this.setProductClasses(courseClasses, tbCourseClass);

        this.showCourse(tbCourseClass, tbCourse);

        tbCourse.getColumnModel().getColumn(0).setPreferredWidth(300);

    }
    
    /**
     * 　コース　のリストを表示する。
     */
    private void showCourse(JTable productClassesTable, JTable productsTable) {
        //全行削除
        SwingUtil.clearTable(productsTable);

        //選択されている分類を取得
        ProductClass pc = this.getSelectedProductClass(productClassesTable);
        String listShopID = this.listShopCourseFlag();
        
        if (pc == null) {
            return;
        }

        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            pc.loadProducts(con, 3, SystemInfo.getCurrentShop().getShopID());
            pc.loadCourse(con, listShopID);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        Map<Integer, MapProduct> condProductClass = null;
        Map<Integer, MapProduct> condProduct = null;

            condProductClass    = condCourseClass;
            condProduct         = condCourse;

        Product topProduct = new Product();
        topProduct.setProductClass(pc);
        topProduct.setProductName(pc.toString() + "のいずれか");

        if (!condProductClass.containsKey(pc.getProductClassID())) {
            condProductClass.put(pc.getProductClassID(), new MapProduct(topProduct.getProductName(), false));
        }

        //テーブルに技術・商品を追加
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.addRow(new Object[]{topProduct, condProductClass.get(pc.getProductClassID()).checked});

        for (Product mj : pc) {
            if (!condProduct.containsKey(mj.getProductID())) {
                condProduct.put(mj.getProductID(), new MapProduct(mj.getProductName(), false, mj.getProductClass().getProductClassID()));
            }
            model.addRow(new Object[]{mj, condProduct.get(mj.getProductID()).checked});
        }
    }

    /**
     * 選択されている分類を取得する。
     *
     * @return 選択されている分類
     */
    public ProductClass getSelectedProductClass(JTable productClassesTable) {
        if (productClassesTable.getSelectedRow() < 0) {
            return null;
        }

        return (ProductClass) productClassesTable.getValueAt(productClassesTable.getSelectedRow(), 0);
    }

    private void setProductClasses(ProductClasses productClasses, JTable classTable) {
        SwingUtil.clearTable(classTable);
        DefaultTableModel model = (DefaultTableModel) classTable.getModel();

        for (ProductClass pc : productClasses) {
            model.addRow(new Object[]{pc});
        }

        if (0 < classTable.getRowCount()) {
            classTable.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * 技術・商品のリストを表示する。
     */
    private void showProducts(JTable productClassesTable, JTable productsTable) {
        //全行削除
        SwingUtil.clearTable(productsTable);

        //選択されている分類を取得
        ProductClass pc = this.getSelectedProductClass(productClassesTable);

        if (pc == null) {
            return;
        }

        // 選択中のタブ（技術・商品）を取得
        int division = productDivision.getSelectedIndex() + 1;

        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            pc.loadProducts(con, division, SystemInfo.getCurrentShop().getShopID());
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        Map<Integer, MapProduct> condProductClass = null;
        Map<Integer, MapProduct> condProduct = null;

        if (division == 1) {
            condProductClass = condTechClass;
            condProduct = condTech;
        } else {
            condProductClass = condItemClass;
            condProduct = condItem;
        }

        Product topProduct = new Product();
        topProduct.setProductClass(pc);
        topProduct.setProductName(pc.toString() + "のいずれか");

        if (!condProductClass.containsKey(pc.getProductClassID())) {
            condProductClass.put(pc.getProductClassID(), new MapProduct(topProduct.getProductName(), false));
        }

        //テーブルに技術・商品を追加
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.addRow(new Object[]{topProduct, condProductClass.get(pc.getProductClassID()).checked});

        for (Product mj : pc) {
            if (!condProduct.containsKey(mj.getProductID())) {
                condProduct.put(mj.getProductID(), new MapProduct(mj.getProductName(), false, mj.getProductClass().getProductClassID()));
            }
            model.addRow(new Object[]{mj, condProduct.get(mj.getProductID()).checked});
        }
    }

    private void setTechItemChecked(JTable table) {

        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();

        if (row < 0 || col < 1) {
            return;
        }

        Boolean checked = (Boolean) table.getValueAt(row, col);

        if (row == 0) {
            if (checked) {
                for (int i = 1; i < table.getRowCount(); i++) {
                    table.setValueAt(false, i, col);
                }
            }
        } else {
            if (checked) {
                table.setValueAt(false, 0, col);
            }
        }

        table.changeSelection(row, col - 1, false, false);

        Map<Integer, MapProduct> condProductClass = null;
        Map<Integer, MapProduct> condProduct = null;

        if (productDivision.getSelectedIndex() == 0) {
            condProductClass = condTechClass;
            condProduct = condTech;
        } else if (productDivision.getSelectedIndex() == 1) {
            condProductClass = condItemClass;
            condProduct = condItem;
        } else if (productDivision.getSelectedIndex() == 2) {
            condProductClass = condCourseClass;
            condProduct = condCourse;
        }

        for (int i = 0; i < table.getRowCount(); i++) {
            Product p = (Product) table.getValueAt(i, 0);
            Boolean chk = (Boolean) table.getValueAt(i, 1);
            if (i == 0) {
                condProductClass.get(p.getProductClass().getProductClassID()).checked = chk;
            } else {
                condProduct.get(p.getProductID()).checked = chk;
            }
        }

        this.showSelectedProduct();
    }

    private void showSelectedProduct() {

        hint.setText("");

        for (Map.Entry<Integer, MapProduct> pc : condTechClass.entrySet()) {
            if (pc.getValue().checked) {
                hint.append(pc.getValue().getName());
                hint.append("\n");
            }

            for (Map.Entry<Integer, MapProduct> p : condTech.entrySet()) {
                if (p.getValue().getClassID().equals(pc.getKey()) && p.getValue().checked) {
                    hint.append(p.getValue().getName());
                    hint.append("\n");
                }
            }
        }

        for (Map.Entry<Integer, MapProduct> pc : condItemClass.entrySet()) {
            if (pc.getValue().checked) {
                hint.append(pc.getValue().getName());
                hint.append("\n");
            }

            for (Map.Entry<Integer, MapProduct> p : condItem.entrySet()) {
                if (p.getValue().getClassID().equals(pc.getKey()) && p.getValue().checked) {
                    hint.append(p.getValue().getName());
                    hint.append("\n");
                }
            }
        }
        
        for (Map.Entry<Integer, MapProduct> pc : condCourseClass.entrySet()) {
            if (pc.getValue().checked) {
                hint.append(pc.getValue().getName());
                hint.append("\n");
            }

            for (Map.Entry<Integer, MapProduct> p : condCourse.entrySet()) {
                if (p.getValue().getClassID().equals(pc.getKey()) && p.getValue().checked) {
                    hint.append(p.getValue().getName());
                    hint.append("\n");
                }
            }
        }

    }

    private boolean isCondTechClassSelected() {
        boolean result = false;
        for (Map.Entry<Integer, MapProduct> p : condTechClass.entrySet()) {
            if (p.getValue().checked) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean isCondTechSelected() {
        boolean result = false;
        for (Map.Entry<Integer, MapProduct> p : condTech.entrySet()) {
            if (p.getValue().checked) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean isCondItemClassSelected() {
        boolean result = false;
        for (Map.Entry<Integer, MapProduct> p : condItemClass.entrySet()) {
            if (p.getValue().checked) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean isCondItemSelected() {
        boolean result = false;
        for (Map.Entry<Integer, MapProduct> p : condItem.entrySet()) {
            if (p.getValue().checked) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    private boolean isCondCourseClassSelected() {
        boolean result = false;
        for (Map.Entry<Integer, MapProduct> p : condCourseClass.entrySet()) {
            if (p.getValue().checked) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean isCondCourseSelected() {
        boolean result = false;
        for (Map.Entry<Integer, MapProduct> p : condCourse.entrySet()) {
            if (p.getValue().checked) {
                result = true;
                break;
            }
        }
        return result;
    }

    //IVS_LVTu end add 2016/03/16 getCondTechClassCondition
    private String getCondTechClassCondition(String parentTable) {

        List<Integer> list = new ArrayList<Integer>();

        for (Map.Entry<Integer, MapProduct> p : condTechClass.entrySet()) {
            if (p.getValue().checked) {
                list.add(p.getKey());
            }
        }

        Collections.sort(list);
        String idList = list.toString().replace("[", "").replace("]", "");

        StringBuilder sql = new StringBuilder(1000);
        if (techItemOR.isSelected()) {

            sql.append("     exists");
            sql.append("         (");
            sql.append("             select 1");
            sql.append("             from");
            //sql.append("                 data_sales_detail a");
            sql.append("    data_sales ds");
            sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
            sql.append("                     inner join mst_technic b");
            sql.append("                             on a.product_id = b.technic_id");
            sql.append("                            and a.product_division = 1");
            sql.append("             where");
            sql.append("                     a.delete_date is null");
            sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
            //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
            sql.append("                 and b.technic_class_id in (" + idList + ")");
            sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
            if (visitDateFrom.getDateStr() != null) {
                sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
            sql.append("         )");

        } else {
            //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
            if (techItemNOT.isSelected()) {
                //IVS_LVTU start edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("     and NOT EXISTS");
                sql.append("         (");
                sql.append("             select 1 ");
                //IVS_LVTU end edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_technic b");
                sql.append("                             on a.product_id = b.technic_id");
                sql.append("                            and a.product_division = 1");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.technic_class_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            } else {
                sql.append("     and array[" + idList + "] =");
                sql.append("         array(");
                sql.append("             select distinct");
                sql.append("                 b.technic_class_id");
                sql.append("             from");
                //sql.append("                 data_sales_detail a");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_technic b");
                sql.append("                             on a.product_id = b.technic_id");
                sql.append("                            and a.product_division = 1");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
                sql.append("                 and b.technic_class_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            }
            //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        }

        return sql.toString();
    }

    private String getCondTechCondition(String parentTable) {

        List<Integer> list = new ArrayList<Integer>();

        for (Map.Entry<Integer, MapProduct> p : condTech.entrySet()) {
            if (p.getValue().checked) {
                list.add(p.getKey());
            }
        }

        Collections.sort(list);
        String idList = list.toString().replace("[", "").replace("]", "");

        StringBuilder sql = new StringBuilder(1000);
        if (techItemOR.isSelected()) {

            sql.append("     exists");
            sql.append("         (");
            sql.append("             select 1");
            sql.append("             from");
            //sql.append("                 data_sales_detail a");
            sql.append("    data_sales ds");
            sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
            sql.append("                     inner join mst_technic b");
            sql.append("                             on a.product_id = b.technic_id");
            sql.append("                            and a.product_division = 1");
            sql.append("             where");
            sql.append("                     a.delete_date is null");
            sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
            //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
            sql.append("                 and b.technic_id in (" + idList + ")");
            sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
            if (visitDateFrom.getDateStr() != null) {
                sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
            sql.append("         )");

        } else {
            //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
            if (techItemNOT.isSelected()) {
                //IVS_LVTU start edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("     and NOT EXISTS ");
                sql.append("         (");
                sql.append("             select 1 ");
                //IVS_LVTU end edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_technic b");
                sql.append("                             on a.product_id = b.technic_id");
                sql.append("                            and a.product_division = 1");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.technic_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            } else {
                sql.append("     and array[" + idList + "] =");
                sql.append("         array(");
                sql.append("             select distinct");
                sql.append("                 b.technic_id");
                sql.append("             from");
                //sql.append("                 data_sales_detail a");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_technic b");
                sql.append("                             on a.product_id = b.technic_id");
                sql.append("                            and a.product_division = 1");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
                sql.append("                 and b.technic_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            }
            //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        }

        return sql.toString();
    }

    private String getCondItemClassCondition(String parentTable) {

        List<Integer> list = new ArrayList<Integer>();

        for (Map.Entry<Integer, MapProduct> p : condItemClass.entrySet()) {
            if (p.getValue().checked) {
                list.add(p.getKey());
            }
        }

        Collections.sort(list);
        String idList = list.toString().replace("[", "").replace("]", "");

        StringBuilder sql = new StringBuilder(1000);
        if (techItemOR.isSelected()) {

            sql.append("     exists");
            sql.append("         (");
            sql.append("             select 1");
            sql.append("             from");
            //sql.append("                 data_sales_detail a");
            sql.append("    data_sales ds");
            sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
            sql.append("                     inner join mst_item b");
            sql.append("                             on a.product_id = b.item_id");
            sql.append("                            and a.product_division = 2");
            sql.append("             where");
            sql.append("                     a.delete_date is null");
            sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
            //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
            sql.append("                 and b.item_class_id in (" + idList + ")");
            sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
            if (visitDateFrom.getDateStr() != null) {
                sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
            sql.append("         )");

        } else {
            //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
            if (techItemNOT.isSelected()) {
                //IVS_LVTU start edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("     and NOT EXISTS ");
                sql.append("         (");
                sql.append("             select 1 ");
                //IVS_LVTU end edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_item b");
                sql.append("                             on a.product_id = b.item_id");
                sql.append("                            and a.product_division = 2");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.item_class_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            } else {
                sql.append("     and array[" + idList + "] =");
                sql.append("         array(");
                sql.append("             select distinct");
                sql.append("                 b.item_class_id");
                sql.append("             from");
                //sql.append("                 data_sales_detail a");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_item b");
                sql.append("                             on a.product_id = b.item_id");
                sql.append("                            and a.product_division = 2");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
                sql.append("                 and b.item_class_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");
            }
            //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        }

        return sql.toString();
    }

       /**
     * フリー項目データを取得する
     */
    private void getFreeHeadingDatas() {
       
        
        numFreeHeadingClass = 0;
        int Num = 0;
        mfhcs = new MstFreeHeadingClasses();

        // フリー項目区分を取得する
        for (MstFreeHeadingClass mfhc : mfhcs) {
            // 有効項目判定
            if (mfhc.getUseFlg()) {
                if (mfhc.getInput_type() == 0) {
                     numFreeHeadingClass += 1;
                    MstFreeHeadingSelectUnitPanel mfhsup = new MstFreeHeadingSelectUnitPanel(mfhc);
                    mfhsups.add(mfhsup);
                    freeHeadingPanel.add(mfhsup);
                    mfhsup.setBounds(0, (Num++ * 25) + 5, 420, 20);
                    arrOrderByFreeHeading.add(numFreeHeadingClass - 1,new String[]{mfhsup.getFreeHeadingClassID().toString(), mfhc.getFreeHeadingClassName()});
                }
               else
                {
                    numFreeHeadingClass += 1;
                    MstFreeHeadingSelectTextFiled mfhsup = new MstFreeHeadingSelectTextFiled(mfhc);
                    mfhsuptext.add(mfhsup);
                    freeHeadingPanel.add(mfhsup);
                    mfhsup.setBounds(0, (Num++ * 25) + 5, 420, 20);
                    
                    arrOrderByFreeHeading.add(numFreeHeadingClass - 1,new String[]{mfhsup.getFreeHeadingClassID().toString(), mfhc.getFreeHeadingClassName()});
                }
            }
            
        }
    }
    
    private String getCondItemCondition(String parentTable) {

        List<Integer> list = new ArrayList<Integer>();

        for (Map.Entry<Integer, MapProduct> p : condItem.entrySet()) {
            if (p.getValue().checked) {
                list.add(p.getKey());
            }
        }

        Collections.sort(list);
        String idList = list.toString().replace("[", "").replace("]", "");

        StringBuilder sql = new StringBuilder(1000);
        if (techItemOR.isSelected()) {

            sql.append("     exists");
            sql.append("         (");
            sql.append("             select 1");
            sql.append("             from");
            //sql.append("                 data_sales_detail a");
            sql.append("    data_sales ds");
            sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
            sql.append("                     inner join mst_item b");
            sql.append("                             on a.product_id = b.item_id");
            sql.append("                            and a.product_division = 2");
            sql.append("             where");
            sql.append("                     a.delete_date is null");
            sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
            //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
            sql.append("                 and b.item_id in (" + idList + ")");
            sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
            if (visitDateFrom.getDateStr() != null) {
                sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
            sql.append("         )");

        } else {
            //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
            if (techItemNOT.isSelected()) {
                //IVS_LVTU start edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("     and NOT EXISTS ");
                sql.append("         (");
                sql.append("             select 1 ");
                //IVS_LVTU end edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_item b");
                sql.append("                             on a.product_id = b.item_id");
                sql.append("                            and a.product_division = 2");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.item_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            } else {
                sql.append("     and array[" + idList + "] =");
                sql.append("         array(");
                sql.append("             select distinct");
                sql.append("                 b.item_id");
                sql.append("             from");
                //sql.append("                 data_sales_detail a");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_item b");
                sql.append("                             on a.product_id = b.item_id");
                sql.append("                            and a.product_division = 2");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                //sql.append("                 and a.slip_no = " + parentTable + ".slip_no");
                sql.append("                 and b.item_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            }
            //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        }

        return sql.toString();
    }
    
    //IVS_LVTu start add 2016/03/16 getCondTechClassCondition
    private String getCondCourseClassCondition(String parentTable) {

        List<Integer> list = new ArrayList<Integer>();

        for (Map.Entry<Integer, MapProduct> p : condCourseClass.entrySet()) {
            if (p.getValue().checked) {
                list.add(p.getKey());
            }
        }

        Collections.sort(list);
        String idList = list.toString().replace("[", "").replace("]", "");

        StringBuilder sql = new StringBuilder(1000);
        if (techItemOR.isSelected()) {

            sql.append("     exists");
            sql.append("         (");
            sql.append("             select 1");
            sql.append("             from");
            sql.append("    data_sales ds");
            sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
            sql.append("                     inner join mst_course b");
            sql.append("                             on a.product_id = b.course_id");
            sql.append("                            and a.product_division = 5");
            sql.append("             where");
            sql.append("                     a.delete_date is null");
            sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
            sql.append("                 and b.course_class_id in (" + idList + ")");
            sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
            if (visitDateFrom.getDateStr() != null) {
                sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
            sql.append("         )");

        } else {
            //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
            if (techItemNOT.isSelected()) {
                //IVS_LVTU start edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("     and NOT EXISTS ");
                sql.append("         (");
                sql.append("             select 1 ");
                //IVS_LVTU end edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_course b");
                sql.append("                             on a.product_id = b.course_id");
                sql.append("                            and a.product_division = 5");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.course_class_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            } else {
                sql.append("     and array[" + idList + "] =");
                sql.append("         array(");
                sql.append("             select distinct");
                sql.append("                 b.course_class_id");
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_course b");
                sql.append("                             on a.product_id = b.course_id");
                sql.append("                            and a.product_division = 5");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.course_class_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            }
            //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        }

        return sql.toString();
    }
    
    
    private String getCondCourseCondition(String parentTable) {

        List<Integer> list = new ArrayList<Integer>();

        for (Map.Entry<Integer, MapProduct> p : condCourse.entrySet()) {
            if (p.getValue().checked) {
                list.add(p.getKey());
            }
        }

        Collections.sort(list);
        String idList = list.toString().replace("[", "").replace("]", "");

        StringBuilder sql = new StringBuilder(1000);
        if (techItemOR.isSelected()) {

            sql.append("     exists");
            sql.append("         (");
            sql.append("             select 1");
            sql.append("             from");
            sql.append("    data_sales ds");
            sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
            sql.append("                     inner join mst_course b");
            sql.append("                             on a.product_id = b.course_id");
            sql.append("                            and a.product_division = 5");
            sql.append("             where");
            sql.append("                     a.delete_date is null");
            sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
            sql.append("                 and b.course_id in (" + idList + ")");
            sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
            if (visitDateFrom.getDateStr() != null) {
                sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
            }
            if (visitDateTo.getDateStr() != null) {
                sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
            }
            sql.append("         )");

        } else {
            //IVS_LVTU start edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
            if (techItemNOT.isSelected()) {
                //IVS_LVTU start edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("     and NOT EXISTS ");
                sql.append("         (");
                sql.append("             select 1 ");
                //IVS_LVTU end edit 2017/05/22 #9908 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_course b");
                sql.append("                            on a.product_id = b.course_id");
                sql.append("                            and a.product_division = 5");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.course_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            } else {
                sql.append("     and array[" + idList + "] =");
                sql.append("         array(");
                sql.append("             select distinct");
                sql.append("                 b.course_id");
                sql.append("             from");
                sql.append("    data_sales ds");
                sql.append("    INNER JOIN data_sales_detail a ON ds.shop_id = a.shop_id and ds.slip_no = a.slip_no");
                sql.append("                     inner join mst_course b");
                sql.append("                            on a.product_id = b.course_id");
                sql.append("                            and a.product_division = 5");
                sql.append("             where");
                sql.append("                     a.delete_date is null");
                sql.append("                 and a.shop_id = " + parentTable + ".shop_id");
                sql.append("                 and b.course_id in (" + idList + ")");
                sql.append("                 AND ds.customer_id = " + parentTable + ".customer_id");
                if (visitDateFrom.getDateStr() != null) {
                    sql.append("     and ds.sales_date >= '" + visitDateFrom.getDateStrWithFirstTime() + "'");
                }
                if (visitDateTo.getDateStr() != null) {
                    sql.append("     and ds.sales_date <= '" + visitDateTo.getDateStrWithLastTime() + "'");
                }
                sql.append("             order by");
                sql.append("                 1");
                sql.append("         )");

            }
            //IVS_LVTU end edit 2017/04/22 #61616 [gb]条件検索でNOT検索の追加及び非会員の精算情報を出力したい
        }

        return sql.toString();
    }
    //IVS_LVTu end add 2016/03/16 getCondTechClassCondition
    
    /**
     * 外税＆姓抜き額から割引設定の場合と、それ以外の設定とで使用するSQLを変えます
     * @param paramBean
     * @return
     */
    private String getTechItemCourseValueQuery() {
        String sql = "";

        if(SystemInfo.getAccountSetting().getDisplayPriceType()==1 && SystemInfo.getAccountSetting().getDiscountType()==1) {
            // 外税＆税抜き額から割引
            sql = "case when  discount_value != 0 then ( ceil(product_value / (1 + get_tax_rate(tax_rate, sales_date))) * product_num) - discount_value + trunc( ( ( ceil(product_value / (1 + get_tax_rate(tax_rate, sales_date))) * product_num) - discount_value) * get_tax_rate(tax_rate, sales_date)) else detail_value_in_tax end ";
        }else {
            sql = " discount_detail_value_in_tax ";
        }

        return sql;
    }
    
    class MapProduct {

        private String name = "";
        private Boolean checked = false;
        private Integer classID = 0;

        public MapProduct(String name, Boolean checked) {
            this(name, checked, 0);
        }

        public MapProduct(String name, Boolean checked, Integer classID) {
            this.name = name;
            this.checked = checked;
            this.classID = classID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public Integer getClassID() {
            return classID;
        }

        public void setClassID(Integer classID) {
            this.classID = classID;
        }
    }
}
