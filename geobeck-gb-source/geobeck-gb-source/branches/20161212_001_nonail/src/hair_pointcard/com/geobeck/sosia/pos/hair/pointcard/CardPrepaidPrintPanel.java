/*
 * CardPrepaidPanel.java
 *
 * Created on 2008/09/02, 13:36
 */

package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.sosia.pos.swing.AbstractMainFrame;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.pointcard.AbstractCardCommunication;
import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.hair.data.account.DataSales;
import com.geobeck.sosia.pos.basicinfo.SelectSameNoData;
import com.geobeck.sosia.pos.hair.account.HairInputAccountDetailDialog;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.*;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.DateUtil;
import com.geobeck.util.StringUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.logging.*;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class CardPrepaidPrintPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    private ArrayList<PointCardLayoutBean> layoutDataList;
    private boolean hasValidEntries;
    private int currentIndex = 0;
    
    private Integer shopId     = null;
    private Integer customerId = null;
    private Integer staffId    = null;
    private Date salesDate = null;
    private Date nextReserveDate = null;
    private Date limitDate = null;
    private Date nextDateTo    = null;
    private Date nextDateFrom  = null;

    private PrepaidData prepaid = null;
    
    // 支払い情報
    private Integer slipNo    = null;
    
    // 印刷完了フラグ
    private boolean isPrinted = false;
    
    private LocalFocusTraversalPolicy	ftp;
    private DataSales sales   = null;
    
    private long useValue = 0l;
    private HairInputAccountDetailDialog hairDlg = null;
    
    // エラー種別
    private final int FAILED_CUSTOMER = 1;
    private final int FAILED_STAFF = 2;
    private final int FAILED_SALES_VALUE = 3;
    private final int FAILED_USE_VALUE = 4;
    private final int FAILED_INSERT_PREPAID = 5;
    private final int FAILED_UPDATE_PREPAID = 6;
    private final int FAILED_NEXT_VISIT = 7;
    public static final int NEXT_TYPE_RESERVE_DATE = 0;
    public static final int NEXT_TYPE_NEXT_DATE = 1;
    
     private JTextField jTextNextTime = null; 
    static public boolean ShowDialog(JDialog owner, DataSales sales, long useValue )
    {
        SystemInfo.getLogger().log(Level.INFO, "プリペイド印刷");
        
        CardPrepaidPrintPanel	dlg = new CardPrepaidPrintPanel(sales.getShop().getShopID());
        dlg.ShowCloseBtn();
        
        dlg.hairDlg = (HairInputAccountDetailDialog)owner;
        
        dlg.sales = sales;
        dlg.customerId = sales.getCustomer().getCustomerID();
        dlg.staffId    = sales.getStaff().getStaffID();
        dlg.slipNo     = sales.getSlipNo();
        dlg.salesDate = sales.getSalesDate();
        dlg.setUseValue(useValue);
        dlg.loadDispData();
        
        SwingUtil.openDialog(owner, true, dlg, "プリペイド印刷");
        
        if( dlg.isPrinted ){
            dlg.dispose();
            return true;
        }else{
            dlg.dispose();
            return false;
        }
    }
    
    private void ShowCloseBtn() {
        btnClose.setVisible(true);
    }
    
    private void loadDispData() {
        ConnectionWrapper con = SystemInfo.getConnection();
        
        // 顧客Noを設定
        MstCustomer      mst  = new MstCustomer();
        mst.setCustomerID(customerId);
        try {
            mst.loadForPrepaid(con);
            
            this.txtCustomerNo.setText(mst.getCustomerNo());
            this.txtCustomerName1.setText(mst.getCustomerName(0));
            this.txtCustomerName2.setText(mst.getCustomerName(1));
           
            updatePreviewCustomer();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // 担当者
        MstStaff staff = new MstStaff();
        staff.setStaffID(staffId);
        try {
            staff.load(con);
            this.setInputStaff(staff.getStaffNo());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        txtCustomerNo.setEditable(false);
        txtCustomerNo.setBackground(new Color(212, 208, 200));
        
        // 発生ポイントを設定
        if( sales != null ){
            if( sales.getSlipNo() != null ){
                prepaid = PrepaidData.getPrepaidData(sales.getShop().getShopID(), sales.getSlipNo() );
            }
            if( prepaid == null ){
                txtSalesValue.setText("0");
                txtUseValue.setText(String.valueOf(this.useValue));
            }else{
                txtSalesValue.setText(String.valueOf(prepaid.getSalesValue()));
                txtUseValue.setText(String.valueOf(prepaid.getUseValue()));
            }
            
            updatePreviewPrepaid();
        }else{
            txtSalesValue.setText("0");
            txtUseValue.setText("0");
        }
        if((mst.getNextVisitDate()!=null && !mst.getNextVisitDate().isEmpty()))
        nextDate.setDate(mst.getNextVisitDate());
        if((mst.getNextVisitTime() != null && !mst.getNextVisitTime().isEmpty())) {
            boolean flag = false;
            for(int i=0;i<nextTime.getItemCount();i++) {
                if(nextTime.getItemAt(i).toString().trim().equals(mst.getNextVisitTime())) {
                     nextTime.setSelectedIndex(i);
                    flag = true;
                }
            }
            if(!flag) {
                nextTime.addItem(mst.getNextVisitTime());
                nextTime.setSelectedItem(mst.getNextVisitTime());
            }
        }
        
        String nextString = "次回：";
        nextString += mst.getNextVisitDate() != null ? mst.getNextVisitDate() :"";
        nextString += " ";
        nextString += mst.getNextVisitTime() != null ?  mst.getNextVisitTime(): "";
        nextvisitTime.setText(nextString);
        if(checkNext.isSelected()) {
            nextDate.setEditable(true);
            nextTime.setEditable(true);
            nextTime.setEnabled(true);
            nextvisitTime.setVisible(true);
        }else {
            nextDate.setEditable(false);
            nextTime.setEditable(false);
            nextTime.setEnabled(false);
            nextvisitTime.setVisible(false);
        }
                
        ShowNextDate();
        initComboBoxItems();
    }
    
    private void changeNextVisit() {
        String nextDateStr = "次回：";
        try {
           nextDateStr  +=  nextDate.getDateStr("/")!= null ? nextDate.getDateStr("/"):"";
        }catch(Exception e) {}
        
        nextDateStr += " ";
        
        try {
            nextDateStr  += nextTime.getSelectedItem().toString();
        }catch (Exception e) {}
        if(checkNext.isSelected()) {
           nextvisitTime.setText(nextDateStr);
        }
    }
    
    /** Creates new form CardPrepaidPrintPanel */
    public CardPrepaidPrintPanel(Integer shopId) {
        
        this.shopId = shopId;
        
        initComponents();
        
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        this.setSize(800,600);
        this.setTitle("プリペイド印字");
        
        initComboBoxItems();
        shopId = SystemInfo.getCurrentShop().getShopID();
        
        if( hasValidEntries == true ) {
            populateComments();
        }
        
        txtSalesValue.setText("0");
        txtUseValue.setText("0");
        
        this.initStaff(inputStaff);
        this.addMouseCursorChange();
        this.setKeyListener();
        
        UpdateButtonEnabled();
        jTextNextTime = (JTextField) nextTime.getEditor().getEditorComponent();
        jTextNextTime.setHorizontalAlignment(JTextField.CENTER);
        
        nextTime.removeAllItems();

        Integer term =  SystemInfo.getCurrentShop().getTerm();            // 時間単位
        Integer openHour = SystemInfo.getCurrentShop().getOpenHour();       // 開店時
        Integer openMinute = SystemInfo.getCurrentShop().getOpenMinute();     // 開店分
        Integer closeHour = SystemInfo.getCurrentShop().getCloseHour();      // 閉店時
        Integer closeMinute = SystemInfo.getCurrentShop().getCloseMinute();    // 閉店分

        if (openHour != null && closeHour != null && term != null) {

            for (int h = openHour; h <= closeHour; h++) {
                for (int m = 0; m < 60; m += term) {
                    // 開店時間より前の場合
                    if (h == openHour && m < openMinute) {
                        continue;
                    }
                    // 閉店時間より後の場合
                    if (h == closeHour && closeMinute <= m) {
                        break;
                    }
                    // 時間セット
                    nextTime.addItem(String.format("%1$02d", h) + ":" + String.format("%1$02d", m));
                }
            }
        }

        nextTime.setSelectedIndex(-1);
    }
    
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnCardPrint);
        SystemInfo.addMouseCursorChange(btnClose);
        SystemInfo.addMouseCursorChange(btnCustomerSearch);
        SystemInfo.addMouseCursorChange(btnPrintClear);
    }
    
    private void setKeyListener() {
        txtCustomerNo.addKeyListener(SystemInfo.getMoveNextField());
        txtCustomerName1.addKeyListener(SystemInfo.getMoveNextField());
        txtCustomerName2.addKeyListener(SystemInfo.getMoveNextField());
        txtStaffNo.addKeyListener(SystemInfo.getMoveNextField());
        inputStaff.addKeyListener(SystemInfo.getMoveNextField());
        txtSalesValue.addKeyListener(SystemInfo.getMoveNextField());
        txtUseValue.addKeyListener(SystemInfo.getMoveNextField());
        
        txtCustomerNo.addFocusListener(SystemInfo.getSelectText());
        txtCustomerName1.addFocusListener(SystemInfo.getSelectText());
        txtCustomerName2.addFocusListener(SystemInfo.getSelectText());
        txtStaffNo.addFocusListener(SystemInfo.getSelectText());
        inputStaff.addFocusListener(SystemInfo.getSelectText());
        txtSalesValue.addFocusListener(SystemInfo.getSelectText());
        txtUseValue.addFocusListener(SystemInfo.getSelectText());
    }
    
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy() {
        return	ftp;
    }
    
    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
    
    public int getShopId() {
        return this.shopId;
    }
    
    //タイトル
    private void initComboBoxItems() {
        
        //@shiera - add filter here to include templates that cover targetDay
        PointCardLayoutData layoutData = new PointCardLayoutData();
        layoutDataList = layoutData.getPointCardLayouts(shopId);
        String[] listItems = null;
        
        Calendar targetDay = Calendar.getInstance();
        if( sales == null){
            targetDay.setTime(new Date());
            targetDay.set(Calendar.HOUR, 0);
        }else{
            targetDay.setTime(sales.getSalesDate());
        }
        
        System.out.println( "対象日付" + targetDay.getTime().toString());
        if( layoutDataList.size() > 0 ) {
            PointCardLayoutBean bean = null;
            listItems = new String[ layoutDataList.size() ];
            
            for( int index = 0; index < layoutDataList.size() ; index++ ) {
                bean = this.layoutDataList.get( index );
                listItems[index] = new String( bean.getTemplateTitle() );
                if( currentIndex == 0 ) {
                    if( bean.getFromDate() != null && bean.getToDate() != null ) {
                        Calendar fromDate = Calendar.getInstance();
                        fromDate.setTime(bean.getFromDate());
                        fromDate.add(Calendar.MILLISECOND, -1);
                        Calendar toDate  = Calendar.getInstance();
                        toDate.setTime(bean.getToDate());
                        toDate.add(Calendar.DATE, 1);
                        
                        System.out.println( "比較日付" + fromDate.getTime().toString() + "～" + toDate.getTime().toString());
                        if( targetDay.compareTo(fromDate) > 0  &&  targetDay.compareTo(toDate) < 0) {
                            currentIndex =  index;
                            break;
                        }
                    }
                }
            }
            hasValidEntries = true;
        } else {
            listItems = new String[1];
            listItems[0] = new String( "標準" );
            hasValidEntries = false;
        }
        
    }
    
    /**
     * 担当者を初期化する。
     */
    protected void initStaff( JComboBox cb ) {
        
        MstStaffs staffs = new MstStaffs();
        staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
        try
        {
            staffs.load(SystemInfo.getConnection(), true);
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        cb.removeAllItems();
        
        for(MstStaff ms : staffs)
        {
            cb.addItem(ms);
        }

        cb.setSelectedIndex(0);
    }
    
    private void populateComments() {
    }
    
    /**
     * 入力スタッフをセットする。
     */
    private void setInputStaff(String staffNo) {
        inputStaff.setSelectedIndex(0);
        this.staffId = null;
        
        for(int i = 1; i < inputStaff.getItemCount(); i ++) {
            MstStaff	ms	=	(MstStaff)inputStaff.getItemAt(i);
            if(ms.getStaffNo().equals(staffNo)) {
                inputStaff.setSelectedIndex(i);
                staffNamePreview.setText("担当：" + ms.getStaffName()[0] + "　" + ms.getStaffName()[1]);
                this.staffId = ms.getStaffID();
                break;
            }
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupNextTime = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cardPanel = new com.geobeck.swing.ImagePanel();
        shopNamePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        shopTelPreview = new com.geobeck.swing.JFormattedTextFieldEx();
        value1Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        value3Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        value4Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        customerNoPreview = new com.geobeck.swing.JFormattedTextFieldEx();
        customerNamePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        datePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        staffNamePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        value2Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel26 = new javax.swing.JLabel();
        nextvisitTime = new com.geobeck.swing.JFormattedTextFieldEx();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnCustomerSearch = new javax.swing.JButton();
        txtCustomerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        txtStaffNo = new com.geobeck.swing.JFormattedTextFieldEx();
        txtCustomerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        txtCustomerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        inputStaff = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtUseValue = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtUseValue.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.NUMERIC));
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtSalesValue = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtSalesValue.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.NUMERIC));
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtPrevValue = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel17 = new javax.swing.JLabel();
        txtTotalValue = new com.geobeck.swing.JFormattedTextFieldEx();
        nextDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        nextTime = new javax.swing.JComboBox();
        Component c = nextTime.getEditor().getEditorComponent();
        PlainDocument doc = (PlainDocument)((JTextComponent)c).getDocument();
        doc.setDocumentFilter(new CustomFilter(5, "0-9:"));
        jLabel2 = new javax.swing.JLabel();
        checkNext = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnClose.setVisible(false);
        btnClose.setContentAreaFilled(false);
        btnCardPrint = new javax.swing.JButton();
        btnPrintClear = new javax.swing.JButton();

        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel1.setText("表示レイアウト");

        cardPanel.setBackground(new java.awt.Color(255, 255, 255));
        cardPanel.setImage(SystemInfo.getImageIcon("/card/background.png"));
        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);

        shopNamePreview.setEditable(false);
        shopNamePreview.setBackground(Color.WHITE);
        shopNamePreview.setBorder(null);
        shopNamePreview.setForeground(new java.awt.Color(0, 0, 102));
        shopNamePreview.setAutoscrolls(false);
        shopNamePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        shopNamePreview.setFocusable(false);
        shopNamePreview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        shopNamePreview.setOpaque(false);
        shopNamePreview.setPreferredSize(new java.awt.Dimension(150, 13));
        shopNamePreview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(shopNamePreview);
        shopNamePreview.setBounds(60, 254, 135, 20);

        shopTelPreview.setEditable(false);
        shopTelPreview.setBackground(Color.WHITE);
        shopTelPreview.setBorder(null);
        shopTelPreview.setForeground(new java.awt.Color(0, 0, 102));
        shopTelPreview.setAutoscrolls(false);
        shopTelPreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        shopTelPreview.setFocusable(false);
        shopTelPreview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        shopTelPreview.setOpaque(false);
        shopTelPreview.setPreferredSize(new java.awt.Dimension(150, 13));
        shopTelPreview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(shopTelPreview);
        shopTelPreview.setBounds(60, 274, 135, 20);

        value1Preview.setEditable(false);
        value1Preview.setBackground(Color.WHITE);
        value1Preview.setBorder(null);
        value1Preview.setForeground(new java.awt.Color(0, 0, 102));
        value1Preview.setAutoscrolls(false);
        value1Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        value1Preview.setFocusable(false);
        value1Preview.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
        value1Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        value1Preview.setOpaque(false);
        value1Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        value1Preview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(value1Preview);
        value1Preview.setBounds(60, 144, 135, 20);

        value3Preview.setEditable(false);
        value3Preview.setBackground(Color.WHITE);
        value3Preview.setBorder(null);
        value3Preview.setForeground(new java.awt.Color(0, 0, 102));
        value3Preview.setAutoscrolls(false);
        value3Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        value3Preview.setFocusable(false);
        value3Preview.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
        value3Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        value3Preview.setOpaque(false);
        value3Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        value3Preview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(value3Preview);
        value3Preview.setBounds(60, 184, 135, 20);

        value4Preview.setEditable(false);
        value4Preview.setBackground(Color.WHITE);
        value4Preview.setBorder(null);
        value4Preview.setForeground(new java.awt.Color(0, 0, 102));
        value4Preview.setAutoscrolls(false);
        value4Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        value4Preview.setFocusable(false);
        value4Preview.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
        value4Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        value4Preview.setOpaque(false);
        value4Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        value4Preview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(value4Preview);
        value4Preview.setBounds(60, 204, 135, 20);

        customerNoPreview.setBackground(Color.WHITE);
        customerNoPreview.setBorder(null);
        customerNoPreview.setEditable(false);
        customerNoPreview.setForeground(new java.awt.Color(0, 0, 102));
        customerNoPreview.setAutoscrolls(false);
        customerNoPreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        customerNoPreview.setFocusable(false);
        customerNoPreview.setOpaque(false);
        customerNoPreview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(customerNoPreview);
        customerNoPreview.setBounds(62, 47, 135, 13);

        customerNamePreview.setBackground(Color.WHITE);
        customerNamePreview.setBorder(null);
        customerNamePreview.setEditable(false);
        customerNamePreview.setForeground(new java.awt.Color(0, 0, 102));
        customerNamePreview.setAutoscrolls(false);
        customerNamePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        customerNamePreview.setFocusable(false);
        customerNamePreview.setFont(new java.awt.Font("MS UI Gothic", 1, 18)); // NOI18N
        customerNamePreview.setOpaque(false);
        customerNamePreview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(customerNamePreview);
        customerNamePreview.setBounds(62, 68, 182, 19);

        datePreview.setBackground(Color.WHITE);
        datePreview.setBorder(null);
        datePreview.setEditable(false);
        datePreview.setForeground(new java.awt.Color(0, 0, 102));
        datePreview.setAutoscrolls(false);
        datePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        datePreview.setFocusable(false);
        datePreview.setOpaque(false);
        datePreview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(datePreview);
        datePreview.setBounds(60, 95, 135, 13);

        staffNamePreview.setBackground(Color.WHITE);
        staffNamePreview.setBorder(null);
        staffNamePreview.setEditable(false);
        staffNamePreview.setForeground(new java.awt.Color(0, 0, 102));
        staffNamePreview.setAutoscrolls(false);
        staffNamePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        staffNamePreview.setFocusable(false);
        staffNamePreview.setOpaque(false);
        staffNamePreview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(staffNamePreview);
        staffNamePreview.setBounds(60, 115, 135, 13);

        jLabel24.setText("今回のご利用店舗は");
        cardPanel.add(jLabel24);
        jLabel24.setBounds(60, 232, 160, 20);

        jLabel25.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel25.setText("－－－－－－－－－－");
        cardPanel.add(jLabel25);
        jLabel25.setBounds(60, 130, 160, 19);

        value2Preview.setEditable(false);
        value2Preview.setBackground(Color.WHITE);
        value2Preview.setBorder(null);
        value2Preview.setForeground(new java.awt.Color(0, 0, 102));
        value2Preview.setAutoscrolls(false);
        value2Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        value2Preview.setFocusable(false);
        value2Preview.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
        value2Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        value2Preview.setOpaque(false);
        value2Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        value2Preview.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(value2Preview);
        value2Preview.setBounds(60, 164, 135, 20);

        jLabel26.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel26.setText("－－－－－－－－－－");
        cardPanel.add(jLabel26);
        jLabel26.setBounds(60, 220, 160, 20);

        nextvisitTime.setEditable(false);
        nextvisitTime.setBackground(Color.WHITE);
        nextvisitTime.setBorder(null);
        nextvisitTime.setForeground(new java.awt.Color(0, 0, 102));
        nextvisitTime.setAutoscrolls(false);
        nextvisitTime.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nextvisitTime.setFocusable(false);
        nextvisitTime.setMargin(new java.awt.Insets(0, 0, 0, 0));
        nextvisitTime.setOpaque(false);
        nextvisitTime.setPreferredSize(new java.awt.Dimension(150, 13));
        nextvisitTime.setSelectionColor(new java.awt.Color(255, 255, 255));
        cardPanel.add(nextvisitTime);
        nextvisitTime.setBounds(60, 294, 135, 20);

        jPanel7.setOpaque(false);

        jLabel15.setText("実際の印字とは異なります。");

        jLabel4.setText("※上記の表示はイメージとなります。");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(210, Short.MAX_VALUE))
        );

        jPanel3.setForeground(new java.awt.Color(131, 153, 177));
        jPanel3.setOpaque(false);

        btnCustomerSearch.setIcon(SystemInfo.getImageIcon("/button/search/customer_b_on.jpg"));
        btnCustomerSearch.setBorderPainted(false);
        btnCustomerSearch.setContentAreaFilled(false);
        btnCustomerSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerSearchActionPerformed(evt);
            }
        });

        txtCustomerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCustomerNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerNoFocusLost(evt);
            }
        });

        txtStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStaffNoFocusLost(evt);
            }
        });

        txtCustomerName1.setEditable(false);
        txtCustomerName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCustomerName1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerName1FocusLost(evt);
            }
        });

        txtCustomerName2.setEditable(false);
        txtCustomerName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCustomerName2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerName2FocusLost(evt);
            }
        });

        inputStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        inputStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputStaffActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel5.setText("担当者");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCustomerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStaffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputStaff, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCustomerName1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustomerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCustomerSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustomerNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustomerName1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustomerName2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStaffNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputStaff, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setOpaque(false);
        jPanel5.setLayout(null);

        jLabel20.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel20.setText("円");
        jPanel5.add(jLabel20);
        jLabel20.setBounds(180, 14, 50, 19);

        txtUseValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtUseValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUseValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUseValueFocusLost(evt);
            }
        });
        jPanel5.add(txtUseValue);
        txtUseValue.setBounds(73, 58, 96, 19);

        jLabel23.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel23.setText("円");
        jPanel5.add(jLabel23);
        jLabel23.setBounds(180, 80, 50, 19);

        jLabel21.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel21.setText("円");
        jPanel5.add(jLabel21);
        jLabel21.setBounds(180, 36, 50, 19);

        jLabel18.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel18.setText("利用額");
        jPanel5.add(jLabel18);
        jLabel18.setBounds(12, 58, 56, 19);

        jLabel19.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel19.setText("現在残");
        jPanel5.add(jLabel19);
        jLabel19.setBounds(12, 80, 56, 19);

        txtSalesValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtSalesValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSalesValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesValueFocusLost(evt);
            }
        });
        jPanel5.add(txtSalesValue);
        txtSalesValue.setBounds(73, 36, 96, 19);

        jLabel16.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel16.setText("前回残");
        jPanel5.add(jLabel16);
        jLabel16.setBounds(12, 14, 56, 19);

        jLabel22.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel22.setText("円");
        jPanel5.add(jLabel22);
        jLabel22.setBounds(180, 58, 50, 19);

        txtPrevValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtPrevValue.setEditable(false);
        txtPrevValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPrevValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrevValueActionPerformed(evt);
            }
        });
        jPanel5.add(txtPrevValue);
        txtPrevValue.setBounds(73, 14, 96, 19);

        jLabel17.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel17.setText("販売額");
        jPanel5.add(jLabel17);
        jLabel17.setBounds(12, 36, 56, 19);

        txtTotalValue.setEditable(false);
        txtTotalValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTotalValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel5.add(txtTotalValue);
        txtTotalValue.setBounds(73, 80, 96, 19);

        nextDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        nextDate.setEditable(false);
        nextDate.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        nextDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nextDateItemStateChanged(evt);
            }
        });
        nextDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nextDateFocusGained(evt);
            }
        });
        jPanel5.add(nextDate);
        nextDate.setBounds(73, 110, 80, 23);

        nextTime.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        nextTime.setMaximumRowCount(10);
        nextTime.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                nextTimePopupMenuWillBecomeVisible(evt);
            }
        });
        nextTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nextTimeItemStateChanged(evt);
            }
        });
        jPanel5.add(nextTime);
        nextTime.setBounds(170, 110, 80, 23);

        jLabel2.setText("次回予約 ");
        jPanel5.add(jLabel2);
        jLabel2.setBounds(10, 110, 60, 20);

        checkNext.setText("印字");
        checkNext.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkNext.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkNext.setOpaque(false);
        checkNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNextActionPerformed(evt);
            }
        });
        jPanel5.add(checkNext);
        checkNext.setBounds(73, 140, 41, 13);

        jPanel6.setOpaque(false);

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btnClose.setBorderPainted(false);
        btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnCardPrint.setIcon(SystemInfo.getImageIcon("/button/common/card_note_off.jpg"));
        btnCardPrint.setBorder(null);
        btnCardPrint.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCardPrint.setPressedIcon(SystemInfo.getImageIcon("/button/common/card_note_on.jpg"));
        btnCardPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCardPrintActionPerformed(evt);
            }
        });

        btnPrintClear.setIcon(SystemInfo.getImageIcon("/button/common/notes_clear_off.jpg"));
        btnPrintClear.setBorder(null);
        btnPrintClear.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnPrintClear.setPressedIcon(SystemInfo.getImageIcon("/button/common/notes_clear_on.jpg"));
        btnPrintClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCardPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnPrintClear, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPrintClear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCardPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(110, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
	private void btnPrintClearActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintClearActionPerformed
	{//GEN-HEADEREND:event_btnPrintClearActionPerformed
            InsertCardDialog.ShowDialog(parentFrame, InsertCardDialog.MODE_CLEAR);
	}//GEN-LAST:event_btnPrintClearActionPerformed
        
	private void btnCardPrintActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCardPrintActionPerformed
	{//GEN-HEADEREND:event_btnCardPrintActionPerformed
            // 印刷前チェックを行う
            if(!chkeckBeforePrint()) return;
            
            AbstractCardCommunication card = SystemInfo.getPointcardConnection();
            if( card != null ){
                // This is a Temporary Data for Customer
                // ユーザ情報
                if( card.clearPrintBuffer() ){
                    card.addPrintBuffer( 1, 1, 1, this.customerNoPreview.getText());
                    card.addPrintBuffer( 2, 1, 2, StringUtil.replaceInvalidString(this.customerNamePreview.getText()));
                    card.addPrintBuffer( 5, 1, 1, this.datePreview.getText());
                    card.addPrintBuffer( 6, 1, 1, StringUtil.replaceInvalidString(this.staffNamePreview.getText()));
                    
                    card.addPrintBuffer( 7, 1, 1, "----------------------");

                    int prevValue = Integer.valueOf(txtPrevValue.getText());
                    int salesValue = Integer.valueOf(txtSalesValue.getText());
                    int useValue = Integer.valueOf(txtUseValue.getText());
                    int totalValue = Integer.valueOf(txtTotalValue.getText());

                    card.addPrintBuffer( 8, 1, 1, value1Preview.getText());
                    card.addPrintBuffer( 9, 1, 1, value2Preview.getText());
                    card.addPrintBuffer( 10, 1, 1, value3Preview.getText());
                    card.addPrintBuffer( 11, 1, 1, value4Preview.getText());
                    card.addPrintBuffer( 12, 1, 1, "----------------------");
                    
                    //コメント欄
                    card.addPrintBuffer(13, 1, 1, "今回のご利用店舗は");
                    card.addPrintBuffer(14, 1, 1, shopNamePreview.getText());
                    card.addPrintBuffer(15, 1, 1, shopTelPreview.getText());
                    if(checkNext.isSelected()) {
                        card.addPrintBuffer(16, 1, 1, nextvisitTime.getText());
                    }
                   if( (InsertCardDialog.ShowDialog(parentFrame,
                            InsertCardDialog.MODE_PRINT,
                            String.valueOf(customerId).trim())) != InsertCardDialog.STAT_SUCCESS ){
                        return;
                    }
                }
            }
            
            // プリペイドの登録処理を行う
            long Sales = 0;
            long Use = 0;
            if( CheckUtil.isNumber(txtSalesValue.getText()) ){
                Sales = Integer.valueOf(txtSalesValue.getText());
            }
            if( CheckUtil.isNumber(txtUseValue.getText()) ){
                Use = Integer.valueOf(txtUseValue.getText());
            }
            
            if( prepaid != null ){
                // 既存データの更新を行う
                this.prepaid.setUseValue(Use);
                this.prepaid.setSalesValue(Sales);
                this.prepaid.setCustomerId(customerId);
                if( !this.prepaid.update() ){
                    displayMessage(FAILED_UPDATE_PREPAID);
                    return;
                }
            }else{
                // プリペイド増減があるときのみ、プリペイドを登録
                if (PrepaidData.getPrepaidData(shopId, slipNo) == null) {
                    if( Sales > 0 || Use > 0 ) {
                        // 新規登録
                        PrepaidData data = new PrepaidData( shopId, customerId, slipNo, Use, Sales );
                        if( !data.insertPrepaidCalculation() ) {
                            displayMessage(FAILED_INSERT_PREPAID);
                            return;
                        }
                    }
                } else {
                    // 既存データの更新を行う
                    PrepaidData data = new PrepaidData( shopId, customerId, slipNo, Use, Sales );
                    if( !data.update() ) {
                        displayMessage(FAILED_UPDATE_PREPAID);
                        return;
                    }
                }

                txtSalesValue.setText("0");
                txtUseValue.setText("0");
            }

            // 画面を更新する
            loadDispData();
	}//GEN-LAST:event_btnCardPrintActionPerformed
                    
    
    private void UpdateButtonEnabled() {


        ShowNextDate();
    }
    
    private void ShowNextDate() {
        
    }
                    
    private void txtStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStaffNoFocusLost
        this.setInputStaff(txtStaffNo.getText());
        this.getFocusTraversalPolicy().getComponentAfter(this,txtStaffNo);
    }//GEN-LAST:event_txtStaffNoFocusLost
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCloseActionPerformed
                                                                            
    private void txtUseValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUseValueFocusLost
        updatePreviewPrepaid();
    }//GEN-LAST:event_txtUseValueFocusLost
    
    private void txtSalesValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesValueFocusLost
        updatePreviewPrepaid();
    }//GEN-LAST:event_txtSalesValueFocusLost
    
    private void txtCustomerName2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerName2FocusLost
        updatePreviewCustomer();
    }//GEN-LAST:event_txtCustomerName2FocusLost
    
    private void txtCustomerName1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerName1FocusLost
        updatePreviewCustomer();
    }//GEN-LAST:event_txtCustomerName1FocusLost
        
    private void updatePreviewCustomer() {
        Long NowValue = null;
        if( customerId != null ){
            customerNoPreview.setText( txtCustomerNo.getText() );
            customerNamePreview.setText( txtCustomerName1.getText() + "　" + txtCustomerName2.getText() + "　様" );
            NowValue = PrepaidData.getNowValue(customerId);
        }

        datePreview.setText("日付：" + DateUtil.format(sales.getSalesDate(), "yyyy/MM/dd") );

        shopNamePreview.setText(sales.getShop().getShopName());
        shopTelPreview.setText("TEL：" + sales.getShop().getPhoneNumber());
        
        // 現在額を表示する
        if( NowValue == null ) {
            txtPrevValue.setText( "" );
        } else {
            if( sales != null ){
                prepaid = PrepaidData.getPrepaidData(sales.getShop().getShopID(), sales.getSlipNo() );
                if( prepaid != null ) {
                    // 伝票登録済みデータの場合は、その時の発行ポイントをのぞいたものを計算
                    NowValue -= (prepaid.getSalesValue() - prepaid.getUseValue());
                }
            }
            txtPrevValue.setText( String.valueOf(NowValue) );
        }
        updatePreviewPrepaid();
    }
    
    private void updatePreviewPrepaid() {
        Integer NowPrepaid = 0;
        Integer AddPrepaid = 0;
        Integer UsePrepaid = 0;
        
        if( CheckUtil.isNumber(txtPrevValue.getText()) ){
            NowPrepaid = Integer.valueOf(txtPrevValue.getText());
        }
        if( CheckUtil.isNumber(txtSalesValue.getText()) ){
            AddPrepaid = Integer.valueOf(txtSalesValue.getText());
        }
        if( CheckUtil.isNumber(txtUseValue.getText()) ){
            UsePrepaid = Integer.valueOf(txtUseValue.getText());
        }
        Integer TotalPrepaid = NowPrepaid + AddPrepaid - UsePrepaid;
        if( TotalPrepaid == null ){
            txtTotalValue.setText( "" );
            value1Preview.setText( "" );
            value2Preview.setText( "" );
            value3Preview.setText( "" );
            value4Preview.setText( "" );
        }else{
            int prevValue = 0;
            int salesValue = 0;
            int useValue = 0;
            int totalValue = 0;

            try {prevValue = Integer.valueOf(txtPrevValue.getText());} catch (Exception e) {}
            try {salesValue = Integer.valueOf(txtSalesValue.getText());} catch (Exception e) {}
            try {useValue = Integer.valueOf(txtUseValue.getText());} catch (Exception e) {}
            try {totalValue = Integer.valueOf(txtTotalValue.getText());} catch (Exception e) {}

            totalValue = prevValue + salesValue - useValue;
            txtTotalValue.setText(String.valueOf(totalValue));

            value1Preview.setText("　前回残" + String.format("%1$,11d", prevValue) + " 円");
            value2Preview.setText("　販売額" + String.format("%1$,11d", salesValue) + " 円");
            value3Preview.setText("　利用額" + String.format("%1$,11d", useValue) + " 円");
            value4Preview.setText("　現在残" + String.format("%1$,11d", totalValue) + " 円");
        }
    }
    
    
    private void txtCustomerNoFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtCustomerNoFocusLost
    {//GEN-HEADEREND:event_txtCustomerNoFocusLost
//        MstCustomer      mst  = new MstCustomer();
//        mst.setCustomerNo(this.txtCustomerNo.getText());
//        
//        if( this.txtCustomerNo.getText().equals("0")) {
//            this.txtChangeEditable(true);
//        } else {
//            try {
//                mst = SelectSameNoData.getMstCustomerByNo(
//                        parentFrame,
//                        SystemInfo.getConnection(),
//                        this.txtCustomerNo.getText(),
//                        (SystemInfo.getSetteing().isShareCustomer() ? 0 : shopId));
//                
//                this.txtChangeEditable(false);
//            } catch(Exception e) {
//                SystemInfo.getLogger().log(Level.SEVERE,e.getLocalizedMessage(), e);
//            }
//        }
//        
//        if( mst == null )
//            mst = new MstCustomer();
//        
//        this.txtCustomerNo.setText(mst.getCustomerNo());
//        this.txtCustomerName1.setText(mst.getCustomerName(0));
//        this.txtCustomerName2.setText(mst.getCustomerName(1));
//        customerId = mst.getCustomerID();
//        
//        updatePreviewCustomer();
    }//GEN-LAST:event_txtCustomerNoFocusLost
    
    private void txtChangeEditable(boolean isEdit) {
        this.txtCustomerName1.setEditable(isEdit);
        this.txtCustomerName2.setEditable(isEdit);
    }
    
    
    private void inputStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputStaffActionPerformed
        MstStaff ms = (MstStaff)this.inputStaff.getSelectedItem();
        
        if( ms != null && ms.getStaffID() != null ) {
            this.txtStaffNo.setText(ms.getStaffNo());
            this.staffId = ms.getStaffID();
            staffNamePreview.setText("担当：" + ms.getStaffName()[0] + "　" + ms.getStaffName()[1]);
        } else {
            this.txtStaffNo.setText("");
            this.staffId = null;
        	/* GEOBECK start edit 20160905 #54425 */
            staffNamePreview.setText(" ");
            /* GEOBECK end edit 20160905 #54425 */

        }
    }//GEN-LAST:event_inputStaffActionPerformed
    
    private void btnCustomerSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCustomerSearchActionPerformed
    {//GEN-HEADEREND:event_btnCustomerSearchActionPerformed
        SystemInfo.getLogger().log(Level.INFO, "顧客検索");
        SearchCustomerDialog sd = new SearchCustomerDialog(parentFrame, true);
        sd.setShopID(shopId);
        sd.setVisible(true);
        
        if( sd.getSelectedCustomer() != null &&
                !sd.getSelectedCustomer().getCustomerID().equals("")) {
            this.txtCustomerNo.setText(sd.getSelectedCustomer().getCustomerNo());
            this.txtCustomerName1.setText(sd.getSelectedCustomer().getCustomerName(0));
            this.txtCustomerName2.setText(sd.getSelectedCustomer().getCustomerName(1));
            
            this.customerId = sd.getSelectedCustomer().getCustomerID();
            this.txtSalesValue.setText("0");
            this.txtUseValue.setText("0");
            
            updatePreviewCustomer();
        }
    }//GEN-LAST:event_btnCustomerSearchActionPerformed
    
    private void txtPrevValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrevValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrevValueActionPerformed

    private void nextDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nextDateItemStateChanged
        changeNextVisit();
    }//GEN-LAST:event_nextDateItemStateChanged

    private void nextDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nextDateFocusGained
        nextDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_nextDateFocusGained

    private void nextTimePopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_nextTimePopupMenuWillBecomeVisible

       
    }//GEN-LAST:event_nextTimePopupMenuWillBecomeVisible

    private void nextTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nextTimeItemStateChanged
        changeNextVisit();
    }//GEN-LAST:event_nextTimeItemStateChanged

    private void checkNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNextActionPerformed
       
        if(checkNext.isSelected()) {
            nextDate.setEditable(true);
            nextTime.setEditable(true);
            nextTime.setEnabled(true);
            nextvisitTime.setVisible(true);
        }else {
            nextDate.setEditable(false);
            nextTime.setEditable(false);
            nextTime.setEnabled(false);
            nextvisitTime.setVisible(false);
        }
    }//GEN-LAST:event_checkNextActionPerformed
    
    //Clear Data in the Card
    /**
     * 印刷前チェック処理
     * @return
     */
    private boolean chkeckBeforePrint() {
        // 入力必須チェック
        
        // 顧客未設定
        if( customerId == null  ){
            displayMessage(FAILED_CUSTOMER);
            return false;
        }
        
        /* GEOBECK start edit 20160905 #54425 */
        // 担当者未設定
        if( staffId == null  ){
            //displayMessage(FAILED_STAFF);
            //return false;
            int dialogResult = JOptionPane.showConfirmDialog(this, "担当者の入力がありません。\n" +
                    "担当者名が印字されませんが、よろしいですか？",
                    "プリペイド印刷", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (dialogResult == 0) {
                    //Yes
                    return true;                    
                }else if(dialogResult == 1){
                    //No
                    return false;
                }
        }
        /* GEOBECK end edit 20160905 #54425 */
        
        // 販売額未設定
        if( txtSalesValue.getText().trim().length() == 0 ){
            displayMessage(FAILED_SALES_VALUE);
            return false;
        }
        
        // 利用額未設定
        if( txtUseValue.getText().trim().length() == 0 ){
            displayMessage(FAILED_USE_VALUE);
            return false;
        }
        if(checkNext.isSelected()) {
            if(nextvisitTime.getText().replace("次回：", "").trim().length() == 0) {
                displayMessage(FAILED_NEXT_VISIT);
                return false;
            }
        }
        
        return true;
    }
    
    // Print Data to Card
    /**
     * Displays a message depending on the message type
     * @param msgType   its value could be one of the following constants:
     *                  FAILED_INSERT, FAILED_UPDATE, FAILED_DELETE,
     *                  MISSING_DISPLAY_SEQUENCE, MISSING_DATE, INVALID_DATE_INPUT
     */
    private void displayMessage( int msgType ) {
        
        Component parent;
        String title;
        if(this.isDialog()) {
            parent = (Component)this.getParent().getParent().getParent().getParent();
            title = ((JDialog)parent).getTitle();
        }else{
            parent = (Component)this.getParentFrame();
            title = ((AbstractMainFrame)parent).getTitle();
        }
        
        switch( msgType ) {
            case FAILED_CUSTOMER:
                MessageDialog.showMessageDialog( parent,
                        "顧客を選択してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            /* GEOBECK start edit 20160905 #54425 */
            /*case FAILED_STAFF:
                MessageDialog.showMessageDialog( parent,
                        "スタッフを選択してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break; */
            /* GEOBECK end edit 20160905 #54425 */
            case FAILED_SALES_VALUE:
                MessageDialog.showMessageDialog( parent,
                        "販売額を入力してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_USE_VALUE:
                MessageDialog.showMessageDialog( parent,
                        "利用額を入力してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_INSERT_PREPAID:
                MessageDialog.showMessageDialog( parent,
                        "プリペイドの登録に失敗しました", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_UPDATE_PREPAID:
                MessageDialog.showMessageDialog( parent,
                        "プリペイドの更新に失敗しました", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_NEXT_VISIT:
                MessageDialog.showMessageDialog( parent,
                        "次回予約情報を正しく入力してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            default:
                break;
        }
    }
    
    private void dispose() {
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).dispose();
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Component Declarations">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCardPrint;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnCustomerSearch;
    private javax.swing.JButton btnPrintClear;
    private com.geobeck.swing.ImagePanel cardPanel;
    private javax.swing.JCheckBox checkNext;
    private com.geobeck.swing.JFormattedTextFieldEx customerNamePreview;
    private com.geobeck.swing.JFormattedTextFieldEx customerNoPreview;
    private com.geobeck.swing.JFormattedTextFieldEx datePreview;
    private javax.swing.ButtonGroup groupNextTime;
    private javax.swing.JComboBox inputStaff;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo nextDate;
    private javax.swing.JComboBox nextTime;
    private com.geobeck.swing.JFormattedTextFieldEx nextvisitTime;
    private com.geobeck.swing.JFormattedTextFieldEx shopNamePreview;
    private com.geobeck.swing.JFormattedTextFieldEx shopTelPreview;
    private com.geobeck.swing.JFormattedTextFieldEx staffNamePreview;
    private com.geobeck.swing.JFormattedTextFieldEx txtCustomerName1;
    private com.geobeck.swing.JFormattedTextFieldEx txtCustomerName2;
    private com.geobeck.swing.JFormattedTextFieldEx txtCustomerNo;
    private com.geobeck.swing.JFormattedTextFieldEx txtPrevValue;
    private com.geobeck.swing.JFormattedTextFieldEx txtSalesValue;
    private com.geobeck.swing.JFormattedTextFieldEx txtStaffNo;
    private com.geobeck.swing.JFormattedTextFieldEx txtTotalValue;
    private com.geobeck.swing.JFormattedTextFieldEx txtUseValue;
    private com.geobeck.swing.JFormattedTextFieldEx value1Preview;
    private com.geobeck.swing.JFormattedTextFieldEx value2Preview;
    private com.geobeck.swing.JFormattedTextFieldEx value3Preview;
    private com.geobeck.swing.JFormattedTextFieldEx value4Preview;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    
    /**
     * FocusTraversalPolicy
     */
    private class LocalFocusTraversalPolicy
            extends FocusTraversalPolicy {
        ArrayList<Component> controls = new ArrayList<Component>();
        public LocalFocusTraversalPolicy() {
            controls.add(txtCustomerNo);
//            controls.add(txtCustomerName1);
//            controls.add(txtCustomerName2);
            controls.add(txtStaffNo);
//            controls.add(inputStaff);
            controls.add(txtSalesValue);
            controls.add(txtUseValue);
            
            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(txtCustomerNo);
            controls.add(txtStaffNo);
        };
        
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for(Component co : controls){
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }
        
        /**
         * aComponent の前にフォーカスを受け取る Component を返します。
         */
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for( int ii = controls.size(); ii>0; ii-- ){
                Component co = controls.get(ii-1);
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }
        
        /**
         * トラバーサルサイクルの最初の Component を返します。
         */
        public Component getFirstComponent(Container aContainer) {
            return getDefaultComponent(aContainer);
        }
        
        /**
         * トラバーサルサイクルの最後の Component を返します。
         */
        public Component getLastComponent(Container aContainer) {
            return getComponentBefore(aContainer, controls.get(0));
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer) {
            for(Component co : controls){
                if( co.isEnabled() ){
                    return co;
                }
            }
            return controls.get(0);
        }
        
        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
         */
        public Component getInitialComponent(Window window) {
            for(Component co : controls){
                if( co.isEnabled() ){
                    return co;
                }
            }
            return controls.get(0);
        }
    }

    public void setUseValue(long useValue) {
        this.useValue = useValue;
    }
}

