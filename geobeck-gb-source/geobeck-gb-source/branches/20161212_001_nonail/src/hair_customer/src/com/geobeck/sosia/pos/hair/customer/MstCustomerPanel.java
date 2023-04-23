/*
 * MstCustomerPanel.java
 *
 * Created on 2006/10/13, 15:56
 */
package com.geobeck.sosia.pos.hair.customer;

import com.geobeck.sosia.pos.basicinfo.customer.*;
import com.geobeck.sosia.pos.hair.pointcard.PointData;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.*;

import com.ibm.icu.util.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.basicinfo.*;
import com.geobeck.sosia.pos.customer.*;
import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.hair.account.HairInputAccount;
import com.geobeck.sosia.pos.hair.account.HairInputAccountPanel;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.search.*;
import com.geobeck.sosia.pos.search.customer.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.hair.data.account.*;
import com.geobeck.sosia.pos.hair.data.course.DataContract;
import com.geobeck.sosia.pos.hair.data.course.DataContractDigestion;
import com.geobeck.sosia.pos.hair.data.course.DataContractShare;
import com.geobeck.sosia.pos.hair.data.mail.DataDmHistoryDetail;
import com.geobeck.sosia.pos.hair.data.reservation.DataReservation;
import com.geobeck.sosia.pos.hair.mail.HairCommonMailPanel;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.hair.pointcard.PrepaidData;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.hair.reservation.ReservationTimeTablePanel;
import com.geobeck.sosia.pos.mail.DataMail;
import com.geobeck.sosia.pos.mail.DmHistory;
import com.geobeck.sosia.pos.mail.MailUtil;
import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import com.geobeck.sosia.pos.master.system.MstSetting;
import com.geobeck.sosia.pos.products.Product;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import jp.co.flatsoft.fscomponent.FSCalenderCombo;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author katagiri
 */
public class MstCustomerPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    /**
     * Creates new form MstCustomerPanel
     */
    private ArrayList<DataContract> dataContracts = null;
    // ThuanNK start add 2014/02/27
    private ArrayList<DataDmHistoryDetail> dataDmHistoryDetails = null;
    // ThuanNK end add 2014/02/27
    private Integer courseTableSelectedIndex = null;
    //nhanvt start
    MstAccountSetting ms = new MstAccountSetting();
    //nhanvt end
    //IVS_LVTu start add 2015/11/23 New request #44111
    ArrayList<DataProposal> arrProposal = new  ArrayList<DataProposal>();
    //IVS_LVTu end add 2015/11/23 New request #44111
    
    private boolean isLoaded = false;
    private boolean tabZero = false;
    private boolean tabOne = false;
    private boolean tabTwo = false;
    private boolean tabThree = false;
// 2017/01/08 �ڋq���� ADD START
    // �ڋq�����^�u�f�[�^��ǂݍ���Őݒ肵�����ǂ�����\���B
    // �Đݒ肪�K�v�ɂȂ����^�C�~���O��false��ݒ肷��B
    private boolean isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
    
    //��{���
    private int tabGenaralIndex = 0;
    
    //���X���
    private int tabAtShopIndex = 1;
    
// 2017/01/02 �ڋq���� MOD START
    //�ڋq����
    private int tabCustomerMemoIndex = 2;
    
    //�_�񗚗�
//    private int tabContractIndex = 2;
    private int tabContractIndex = 3;
    
    //��ď�
//    private int tabProposalIndex = 3;
    private int tabProposalIndex = 4;
    
    //DM�E���[������
//    private int tabDMMailIndex = 4;
    private int tabDMMailIndex = 5;
   
    // �ڋq�����@�\�L������
    private boolean isCustomerMemoEnabled;
    
    // �Ώیڋq�������X�g
    private List<DataCustomerMemo> customerMemoList;
    
    /**
     * �S���҃}�b�v�i�L���b�V���p�j
     * �X��ID�ɕR�Â��X�^�b�t�̃��X�g��ێ�����B
     */
    Map<Integer, MstStaffs> memoStaffsMap = null;
    
    /**
     * �R���{�{�b�N�X�󔒗p�X�܃f�[�^
     */
    private static final MstShop EMPTY_SHOP_DATA = new MstShop();
    
    /**
     * �R���{�{�b�N�X�󔒗p�S���҃f�[�^
     */
    private static final MstStaff EMPTY_STAFF_DATA = new MstStaff();
    
    /**
     * �����o�^���̏���
     */
    private static final String MEMO_DATE_FORMAT = "yyyy/MM/dd";
    
    /**
     * �폜�ΏۂɂȂ��Ă���S���҃R���{�{�b�N�X�̍��ڃC���f�b�N�X�̃��X�g
     */
    private List<Integer> deletedMemoStaffComboItemIndexList = null;
    
    /**
     * �폜�ΏۂɂȂ��Ă���X�܃R���{�{�b�N�X�̍��ڃC���f�b�N�X�̃��X�g
     */
    private List<Integer> deletedMemoShopComboItemIndexList = null;
       
// 2017/01/02 �ڋq���� MOD END
    
    public MstCustomerPanel() {
        super();
        this.init();
        initComponents();
        
// 2017/01/02 �ڋq���� ADD START
        isCustomerMemoEnabled = this.isCustomerMemoEnabled();
        if(!isCustomerMemoEnabled) {
            // �ڋq�����@�\�������́A�ڋq�����^�u���\���ɂ���B
            this.jTabbedPane1.remove(this.customerMemoPanel);
            
            // �ڋq�����^�u���Ȃ��Ȃ邱�Ƃɂ���ă^�u�̏���������邽�߁A���̂��̂��C������B
            --tabContractIndex;
            --tabProposalIndex;
            --tabDMMailIndex;
        }
// 2017/01/02 �ڋq���� ADD END
        
        isLoaded = true;
        addMouseCursorChange();
        //nhanvt start edit 20141010 Bug #31135
        this.setSize(848, 691);
        //nhanvt start edit 20141010 Bug #31135
        this.setPath("�ڋq�Ǘ�");
        this.setTitle("�ڋq���o�^");
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            chargeStaff.setVisible(true);
            chargeStaffNo.setVisible(true);
            lblMainStaff.setVisible(true);
            this.initStaff();
            this.customerID.setText("");
            customerID.setVisible(true);
        } else {
            chargeStaff.setVisible(false);
            chargeStaffNo.setVisible(false);
            lblMainStaff.setVisible(false);
            customerID.setVisible(false);
        }
        SystemInfo.initGropuShopComponentsForCustomer(shop, 2);
        if (SystemInfo.getSetteing().isShareCustomer()) {
            //�ڋq���L
            shopLabel.setText("");
        } else {
            shopLabel.setText("�o�^�X��");
        }

        this.setListener();
        this.initOptionComponent();
        this.getFreeHeadingDatas(); // �t���[����
        this.clear();
        //IVS_LVTu start add 2015/04/21 Bug #36369
        SwingUtil.clearTable(introducer);
        SwingUtil.clearTable(family);
        SwingUtil.clearTable(introduce);
        //IVS_LVTu end add 2015/04/21 Bug #36369
        this.customerNo.setText("");

        //�Љ��ŃG���[�ɂȂ��Ă����Ή�
        introduce.setAutoCreateRowSorter(true);

        // WEB����A���s�̏ꍇ�ɂ͘A���p�l�����\���ɂ���
        if (!SystemInfo.isSosiaGearEnable()) {
            sosiaGearPanel.setVisible(false);
        }

        // �|�C���g���g�p��
        if (!SystemInfo.isUsePointcard()) {
            // �|�C���g���g�p���̓|�C���g���\��
            pointPanel.setVisible(false);
            // �|�C���g���g�p�̏ꍇ�̓|�C���g�����^�u��\�����Ȃ�
            salesInfoTabbedPane.remove(pointInfoPanel);
        }

        // �v���y�C�h���g�p�̏ꍇ�̓v���y�C�h�����^�u��\�����Ȃ�
        if (!SystemInfo.getSetteing().isUsePrepaid()) {
            salesInfoTabbedPane.remove(prepaidInfoPanel);
        }

        deleteLabel.setVisible(false);

        initYearCombo(cmbTargetYear);

        invisiblePanel.setVisible(false);

        // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
        // start add 20130221 nakhoa Check ispot�X��
        if (!"".equals(SystemInfo.getIspotShopID())) {
            optionTab.remove(optionTab.getTabCount() - 1);
        }
        this.CustomerIspotIDjTextField.setText("");
        this.CustomerIspotIDjTextField.setEditable(false);
        if (!"".equals(SystemInfo.getIspotShopID())) {
            optionTab.remove(optionTab.getTabCount() - 1);
        } else {
            this.cusIspotIDLabel.setVisible(false);
            this.CustomerIspotIDjTextField.setVisible(false);
        }


        // end add 20130221 nakhoa Check ispot�X��

        //An start add set tabindex 20130327
        MstShop shop = SystemInfo.getCurrentShop();
        try {
            shop.load(SystemInfo.getConnection());
        } catch (Exception e) {
        }
        //IVS_ptthu start add 2015/04/25 New reques#49600
        if(!shop.getShopID().equals(0)) {
            if(shop.getCourseFlag() != null && shop.getCourseFlag().equals(0) )
            {
                jTabbedPane1.remove(jPanel8);
                tabProposalIndex = tabProposalIndex -1;
                tabDMMailIndex = tabDMMailIndex -1;
            }
        }else {
            MstGroup group = new MstGroup();
            group = SystemInfo.getGroup();

            if (!checkShopCourseFlag(group)) {
                jTabbedPane1.remove(jPanel8);//tab 2
                tabProposalIndex = tabProposalIndex -1;
                tabDMMailIndex = tabDMMailIndex -1;
            }
        }
        //IVS_ptthu end start add 2015/04/25 New reques#49600
        int index = shop.getTabIndex();
        jTabbedPane1.setSelectedIndex(index);
        tabZero = false;
        tabOne = false;
        tabTwo = false;
        tabThree = false;
        
// 2017/01/08 �ڋq���� ADD START
        isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
    
        //An end add set tabindex 20130327  
        // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
        // Luc start add 20140707 Request #26547 [gb][MF]�����ύX�y�шꕔ����ǉ�
        if(SystemInfo.getDatabase().equals("pos_hair_missionf")||
        SystemInfo.getDatabase().equals("pos_hair_missionf_dev")) {
              if (!SystemInfo.isUsePointcard()) {
                  //tab point is invisible
                 salesInfoTabbedPane.setTitleAt(5, "�߽�ޯ�"); 
              }else {
                  //tab poin is visible
                salesInfoTabbedPane.setTitleAt(6, "�߽�ޯ�");
              }
             
            
        }
        ms = SystemInfo.getAccountSetting(); 
        try {
            ms.load(SystemInfo.getConnection());
            ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Luc end add 20140707 Request #26547 [gb][MF]�����ύX�y�шꕔ����ǉ�
        //if shop = 0 then enable = false
        //IVS_LVTu start add 2015/11/23 New request #44111
        if ( (shop.getShopID() == 0) || (this.customer == null && this.customer.getCustomerID() == null) ) {
            btCallProposal.setEnabled(false);
        }else if (shop.getShopID() != 0 || (this.customer != null && this.customer.getCustomerID() != null)) {
            btCallProposal.setEnabled(true);
        }
        //��ď��^�u��ǉ� pos_hair_natura�Apos_hair_dev�̂�
        if (!(SystemInfo.getDatabase().startsWith("pos_hair_natura") || SystemInfo.getDatabase().startsWith("pos_hair_dev"))) {
            //IVS_LVTu start edit 2016/05/12 Bug #50315
            jTabbedPane1.remove(jPanel9);//tab 3
            tabDMMailIndex = tabDMMailIndex -1;
            //IVS_LVTu end edit 2016/05/12 Bug #50315
        }
        //IVS_LVTu end add 2015/11/23 New request #44111
        
        //Luc start add 20160112 #46365
        if (SystemInfo.getNSystem() == 1) {
            this.BufferTimeLabel.setVisible(true);
            this.Reserved_buffer_time.setVisible(true);
            this.timeLabel.setVisible(true);
        } else {
            this.BufferTimeLabel.setVisible(false);
            this.Reserved_buffer_time.setVisible(false);
            this.timeLabel.setVisible(false);
        }
        //Luc end add 20160112 #46365
        
       
    }

    public MstCustomerPanel(Integer customerID) {
        this(customerID, false, false);
         ms = SystemInfo.getAccountSetting(); 
        try {
            ms.load(SystemInfo.getConnection());
            ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }

    public MstCustomerPanel(Integer customerID, boolean isReadOnly) {
        this(customerID, isReadOnly, false);
         ms = SystemInfo.getAccountSetting(); 
        try {
            ms.load(SystemInfo.getConnection());
            ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    public MstCustomerPanel(Integer customerID, boolean isReadOnly, boolean dispRenew) {
        this(customerID, isReadOnly, dispRenew, false);
         ms = SystemInfo.getAccountSetting(); 
        try {
            ms.load(SystemInfo.getConnection());
            ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    public MstCustomerPanel(Object mobileMemberDataObj) {
        this(null, true, true, true);
        MobileMemberData mmd = (MobileMemberData) mobileMemberDataObj;
        this.customerNo.setText("0");
        this.customerName1.setText(mmd.getCusName1());
        this.customerName2.setText(mmd.getCusName2());
        this.cellularMail.setText(mmd.getEmail());

        if (mmd.getSex().equals(1)) {
            male.setSelected(true);
        } else {
            female.setSelected(true);
        }

        customer.setBirthdayDate(mmd.getBirthDate());
        this.setBirthday();

        customer.getSosiaCustomer().setSosiaID(mmd.getSosiaID());

        this.renewButton.setEnabled(true);
         ms = SystemInfo.getAccountSetting(); 
        try {
            ms.load(SystemInfo.getConnection());
            ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    public MstCustomerPanel(Integer customerID, boolean isReadOnly, boolean dispRenew, boolean isCloseButtonEnable) {
        this();
        customer.setCustomerID(customerID);
        this.loadCustomer();
        this.showData();
        this.setNotMemberButtonEnabled();
        deleteButton.setEnabled(false);
        this.isReadOnly = isReadOnly;
        this.dispRenew = dispRenew;
        this.isCloseButtonEnable = isCloseButtonEnable;

        if (isReadOnly) {

            this.isCloseButtonEnable = true;

            // ���̑��̃{�^�����\����
            prevButton.setVisible(false);
            nextButton.setVisible(false);
            searchCusButton.setVisible(false);
            clearButton.setVisible(false);
            addButton.setVisible(false);
            if (!dispRenew) {
                renewButton.setVisible(false);
            }
        }

        // �폜�{�^�������{�^���ɕύX
        if (this.isCloseButtonEnable) {
            deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
            deleteButton.setBorderPainted(false);
            deleteButton.setContentAreaFilled(false);
            deleteButton.setEnabled(true);
            deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        }


        if (customer.isDeleted()) {
            deleteLabel.setVisible(true);
            deleteLabel.setText("���̌ڋq�͊��ɍ폜����Ă��܂��B ����������ꍇ�́A�X�V�{�^�����N���b�N���Ă��������B��");

            setDeletedMode(customerParamPane);
            setDeletedMode(jPanel2);
            setDeletedMode(jPanel3);
            note.setEnabled(false);
            account.setEnabled(false);
        }
        initYearCombo(cmbTargetYear);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
         ms = SystemInfo.getAccountSetting(); 
        try {
            ms.load(SystemInfo.getConnection());
            ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    //IVS_ptthu start add 2015/04/25 New reques#49600
    private boolean checkShopCourseFlag(MstGroup mg) {
            //�O���[�v
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
    //IVS_ptthu end add 2015/04/25 New reques#49600

    private void setDeletedMode(JPanel c) {

        for (int i = 0; i < c.getComponentCount(); i++) {
            Component component = c.getComponent(i);
            if (component instanceof JPanel) {
                setDeletedMode((JPanel) component);
            } else if (!(component instanceof JLabel)) {
                component.setEnabled(false);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sexGroup = new javax.swing.ButtonGroup();
        sendMailGroup = new javax.swing.ButtonGroup();
        sendDmGroup = new javax.swing.ButtonGroup();
        callFlagGroup = new javax.swing.ButtonGroup();
        webCancelGroup = new javax.swing.ButtonGroup();
        evaluationButtonGroup = new javax.swing.ButtonGroup();
        controlPanel = new javax.swing.JPanel();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        searchCusButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        registPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        renewButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        deleteLabel = new javax.swing.JLabel();
        invisiblePanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        technicClameSalesTotal = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel13 = new javax.swing.JLabel();
        itemReturnedSalesTotal = new com.geobeck.swing.JFormattedTextFieldEx();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        customerParamPane = new javax.swing.JPanel();
        shopPanel = new javax.swing.JPanel();
        customerNoPanel = new javax.swing.JPanel();
        cusIDLabel = new javax.swing.JLabel();
        customerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        showButton = new javax.swing.JButton();
        customerID = new com.geobeck.swing.JFormattedTextFieldEx();
        customerNamePanel = new javax.swing.JPanel();
        customerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerName2.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        cusNameLabel = new javax.swing.JLabel();
        customerKana1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerKana1.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        customerKana2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerKana2.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        alertMarkLabel = new javax.swing.JLabel();
        cusKanaLabel = new javax.swing.JLabel();
        customerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerKana1.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        customerAddressPanel = new javax.swing.JPanel();
        postalCodeLabel = new javax.swing.JLabel();
        postalCode = postalCode = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createPostalCodeFormatter());
        searchAddressButton = new javax.swing.JButton();
        address1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address1.getDocument()).setDocumentFilter(
            new CustomFilter(16));
        addressLabel = new javax.swing.JLabel();
        addressLabel1 = new javax.swing.JLabel();
        address2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address2.getDocument()).setDocumentFilter(
            new CustomFilter(64));
        addressLabel2 = new javax.swing.JLabel();
        address3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address3.getDocument()).setDocumentFilter(
            new CustomFilter(128));
        addressLabel3 = new javax.swing.JLabel();
        address4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address4.getDocument()).setDocumentFilter(
            new CustomFilter(128));
        customerPhoneNumberPanel = new javax.swing.JPanel();
        phoneNumberLabel = new javax.swing.JLabel();
        phoneNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)phoneNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));
        faxNumberLabel = new javax.swing.JLabel();
        faxNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)faxNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));
        cellularNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)cellularNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));
        cellularNumberLabel = new javax.swing.JLabel();
        customerMailPanel = new javax.swing.JPanel();
        pcMailLabel = new javax.swing.JLabel();
        pcMail = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)pcMail.getDocument()).setDocumentFilter(
            new CustomFilter(64, CustomFilter.MAIL_ADDRESS));
        cellularMailLabel = new javax.swing.JLabel();
        cellularMail = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)cellularMail.getDocument()).setDocumentFilter(
            new CustomFilter(64, CustomFilter.MAIL_ADDRESS));
        customerSexPanel = new javax.swing.JPanel();
        sendDmOK = new javax.swing.JRadioButton();
        sendDmNG = new javax.swing.JRadioButton();
        sexLabel3 = new javax.swing.JLabel();
        sexLabel1 = new javax.swing.JLabel();
        sendMailOK = new javax.swing.JRadioButton();
        sendMailNG = new javax.swing.JRadioButton();
        sexLabel2 = new javax.swing.JLabel();
        callFlagOK = new javax.swing.JRadioButton();
        callFlagNG = new javax.swing.JRadioButton();
        creditContracLabel = new javax.swing.JLabel();
        credit_lock_chk = new javax.swing.JCheckBox();
        customerBirthdayPanel = new javax.swing.JPanel();
        birthdayLabel = new javax.swing.JLabel();
        yearUnit = new javax.swing.JComboBox();
        birthYear = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)birthYear.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMERIC));
        birthMonth = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        birthDay = new javax.swing.JComboBox();
        ageLabel = new javax.swing.JLabel();
        age = new com.geobeck.swing.JFormattedTextFieldEx();
        yearLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        female = new javax.swing.JRadioButton();
        male = new javax.swing.JRadioButton();
        sexLabel = new javax.swing.JLabel();
        sexLabel6 = new javax.swing.JLabel();
        Alert = new javax.swing.JComboBox();
        customerJobPanel = new javax.swing.JPanel();
        jobLabel = new javax.swing.JLabel();
        job = new JComboBox(jobList.toArray());
        customerNotePanel = new javax.swing.JPanel();
        noteLabel = new javax.swing.JLabel();
        noteScrollPane = new javax.swing.JScrollPane();
        note = new com.geobeck.swing.JTextAreaEx();
        shopLabel = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        BufferTimeLabel = new javax.swing.JLabel();
        Reserved_buffer_time = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)Reserved_buffer_time.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        timeLabel = new javax.swing.JLabel();
        question_2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)cellularNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));
        question_1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)cellularNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));
        shopLabel3 = new javax.swing.JLabel();
        shopLabel4 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        sosiaGearPanel = new javax.swing.JPanel();
        sosiaGearLabel = new javax.swing.JLabel();
        changeSosiaGearButton = new javax.swing.JButton();
        pointPanel = new javax.swing.JPanel();
        sosiaGearLabel1 = new javax.swing.JLabel();
        genzaiPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel14 = new javax.swing.JLabel();
        cusIspotIDLabel = new javax.swing.JLabel();
        CustomerIspotIDjTextField = new javax.swing.JTextField();
        optionTab = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        firstComingMotivePanel = new javax.swing.JPanel();
        firstComingMotiveLabel = new javax.swing.JLabel();
        firstComingMotive = new JComboBox(firstComingMotiveList.toArray());
        firstComingMotiveLabel1 = new javax.swing.JLabel();
        firstComingMotiveNote = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address1.getDocument()).setDocumentFilter(
            new CustomFilter(16));
        freeHeadingPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        introducerPanel = new javax.swing.JPanel();
        introducerScrollPane = new javax.swing.JScrollPane();
        introducer = new com.geobeck.swing.JTableEx();
        addIntroducerButton = new javax.swing.JButton();
        introducerPanel1 = new javax.swing.JPanel();
        introduceScrollPane = new javax.swing.JScrollPane();
        introduce = new com.geobeck.swing.JTableEx();
        introducerPanel2 = new javax.swing.JPanel();
        familyScrollPane = new javax.swing.JScrollPane();
        family = new com.geobeck.swing.JTableEx();
        addFamilyButton = new javax.swing.JButton();
        sexLabel4 = new javax.swing.JLabel();
        sexLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        salesInfoTabbedPane = new javax.swing.JTabbedPane();
        visitInfoPanel = new javax.swing.JPanel();
        accountScrollPane = new javax.swing.JScrollPane();
        account = new com.geobeck.swing.JTableEx();
        techItemTableScrollPane = new javax.swing.JScrollPane();
        techItemTable = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        discountName = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel11 = new javax.swing.JLabel();
        discountValue = new com.geobeck.swing.JFormattedTextFieldEx();
        paymentScrollPane = new javax.swing.JScrollPane();
        payment = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        cancel = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        WebOKCancel = new javax.swing.JRadioButton();
        WebNGCancel = new javax.swing.JRadioButton();
        cancelsScrollPane = new javax.swing.JScrollPane();
        cancels = new com.geobeck.swing.JTableEx();
        techInfoPanel = new javax.swing.JPanel();
        techTableScrollPane = new javax.swing.JScrollPane();
        techTable = new com.geobeck.swing.JTableEx();
        itemInfoPanel = new javax.swing.JPanel();
        itemTableScrollPane = new javax.swing.JScrollPane();
        itemTable = new com.geobeck.swing.JTableEx();
        visitedMemoPanel = new javax.swing.JPanel();
        visitedMemoTableScrollPane = new javax.swing.JScrollPane();
        visitedMemoTable = new com.geobeck.swing.JTableEx();
        visitedMemoUpdateButton = new javax.swing.JButton();
        visitedMemoScrollPane = new javax.swing.JScrollPane();
        visitedMemo = new com.geobeck.swing.JTextAreaEx();
        pointInfoPanel = new javax.swing.JPanel();
        pointTableScrollPane = new javax.swing.JScrollPane();
        pointTable = new com.geobeck.swing.JTableEx();
        pointUpdateButton = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        suppliedPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)suppliedPoint.getDocument()).setDocumentFilter(
            new CustomFilter(10, CustomFilter.NUMERIC));
        usePoint = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)usePoint.getDocument()).setDocumentFilter(
            new CustomFilter(10, CustomFilter.NUMERIC));
        shopPoint = new javax.swing.JLabel();
        datePoint = new javax.swing.JLabel();
        pointAddButton = new javax.swing.JButton();
        prepaidInfoPanel = new javax.swing.JPanel();
        prepaidTableScrollPane = new javax.swing.JScrollPane();
        prepaidTable = new com.geobeck.swing.JTableEx();
        jLabel26 = new javax.swing.JLabel();
        currentPrepaid = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel24 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        visitCount = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel7 = new javax.swing.JLabel();
        technicSalesTotal = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel8 = new javax.swing.JLabel();
        salesTotal = new com.geobeck.swing.JFormattedTextFieldEx();
        itemSalesTotal = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel16 = new javax.swing.JLabel();
        nextReserveChangeButton = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        beforeVisitNum = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)beforeVisitNum.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMBER));
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        firstVisitYear = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)firstVisitYear.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMERIC));
        jLabel25 = new javax.swing.JLabel();
        yearLabel1 = new javax.swing.JLabel();
        firstVisitMonth = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        firstVisitDay = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        customerRank = new javax.swing.JLabel();
        cusNameLabel1 = new javax.swing.JLabel();
        visitName = new javax.swing.JLabel();
        cusIDLabel1 = new javax.swing.JLabel();
        visitNo = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        salesTotal1 = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        salesTotal2 = new com.geobeck.swing.JFormattedTextFieldEx();
        cmbNextReserve = new JComboBox();
        jPanel6 = new javax.swing.JPanel();
        cmbTargetYear = new javax.swing.JComboBox();
        ((PlainDocument)((JTextComponent)
            cmbTargetYear.getEditor().getEditorComponent()).getDocument()).setDocumentFilter(
        new CustomFilter(4, CustomFilter.NUMBER));
    lblBaseMonth = new javax.swing.JLabel();
    cmbTargetMonth = new javax.swing.JComboBox();
    lblTargetPeriod8 = new javax.swing.JLabel();
    lblTargetPeriod9 = new javax.swing.JLabel();
    showVisitCycleButton = new javax.swing.JButton();
    visitCyclelScrollPane1 = new javax.swing.JScrollPane();
    visitCycleTable = new com.geobeck.swing.JTableEx();
    customerMemoPanel = new javax.swing.JPanel();
    memoPanel = new javax.swing.JPanel();
    memoRegistrationDateComboBox = new FSCalenderCombo(SystemInfo.getSystemDate());
    memoEvaluationLabel = new javax.swing.JLabel();
    memoShopLabel = new javax.swing.JLabel();
    memoEvaluationGoodRadioButton = new javax.swing.JRadioButton();
    memoRegistrationDateLabel = new javax.swing.JLabel();
    memoRegistButton = new javax.swing.JButton();
    memoEvaluationBadRadioButton = new javax.swing.JRadioButton();
    memoStaffLabel = new javax.swing.JLabel();
    memoShopComboBox = new javax.swing.JComboBox();
    jScrollPane1 = new javax.swing.JScrollPane();
    memoTextArea = new javax.swing.JTextArea();
    memoStaffComboBox = new javax.swing.JComboBox();
    memoEvaluationBatonRadioButton = new javax.swing.JRadioButton();
    memoCustomerInfoPanel = new javax.swing.JPanel();
    jLabel39 = new javax.swing.JLabel();
    memoCustomerNoLabel = new javax.swing.JLabel();
    jLabel40 = new javax.swing.JLabel();
    memoCustomerNameLabel = new javax.swing.JLabel();
    memoListPanel = new javax.swing.JPanel();
    searchResultScrollPane = new javax.swing.JScrollPane();
    memoTable = new com.geobeck.swing.JTableEx();
    feedListButton = new javax.swing.JButton();
    jLabel41 = new javax.swing.JLabel();
    jPanel8 = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    jLabel27 = new javax.swing.JLabel();
    jLabel28 = new javax.swing.JLabel();
    introducerPanel9 = new javax.swing.JPanel();
    introduceScrollPane2 = new javax.swing.JScrollPane();
    contractCourseTable = new com.geobeck.swing.JTableEx();
    introducerPanel7 = new javax.swing.JPanel();
    introduceScrollPane1 = new javax.swing.JScrollPane();
    consumptionCourseTable = new com.geobeck.swing.JTableEx();
    introduceScrollPane3 = new javax.swing.JScrollPane();
    jointlyUserTable = new com.geobeck.swing.JTableEx();
    jointlyUserAddButton = new javax.swing.JButton();
    lblMainStaff = new javax.swing.JLabel();
    chargeStaffNo = new javax.swing.JTextField();
    chargeStaff = new javax.swing.JComboBox();
    courseNoLabel = new javax.swing.JLabel();
    cusNameLabel3 = new javax.swing.JLabel();
    courseName = new javax.swing.JLabel();
    coursetNo = new javax.swing.JLabel();
    jLabel31 = new javax.swing.JLabel();
    lblMainStaff1 = new javax.swing.JLabel();
    lblMainStaff2 = new javax.swing.JLabel();
    jPanel9 = new javax.swing.JPanel();
    jLabel9 = new javax.swing.JLabel();
    jLabel36 = new javax.swing.JLabel();
    introducerPanel10 = new javax.swing.JPanel();
    introduceScrollPane4 = new javax.swing.JScrollPane();
    tbProposal = new com.geobeck.swing.JTableEx()
    {
        public String getToolTipText(MouseEvent e)
        {
            int col = columnAtPoint(e.getPoint());
            int row = rowAtPoint(e.getPoint());
            if ( col != 2 ){
                return null;
            }
            TableModel m = getModel();
            return getProposalMemoToolTip(row);
        }
    };
    introducerPanel11 = new javax.swing.JPanel();
    introduceScrollPane6 = new javax.swing.JScrollPane();
    tbProductDescription = new com.geobeck.swing.JTableEx();
    jLabel37 = new javax.swing.JLabel();
    jLabel38 = new javax.swing.JLabel();
    introduceScrollPane7 = new javax.swing.JScrollPane();
    tbCourseDescription = new com.geobeck.swing.JTableEx();
    lbCustomerNo = new javax.swing.JLabel();
    lbCustomerName = new javax.swing.JLabel();
    txtCustomerName = new javax.swing.JLabel();
    txtCustomerNo = new javax.swing.JLabel();
    btCallProposal = new javax.swing.JButton();
    jPanel7 = new javax.swing.JPanel();
    introducerPanel6 = new javax.swing.JPanel();
    dmmailScrollPane = new javax.swing.JScrollPane();
    dmmailTable = new com.geobeck.swing.JTableEx();
    cusNameLabel2 = new javax.swing.JLabel();
    introducerPanel8 = new javax.swing.JPanel();
    mailTitleLabel = new javax.swing.JLabel();
    mailTitleScrollPane = new javax.swing.JScrollPane();
    mailTitle = new javax.swing.JTextArea();
    mailBodyScrollPane = new javax.swing.JScrollPane();
    mailBody = new javax.swing.JTextArea();
    mailBodyLabel = new javax.swing.JLabel();
    cusIDLabel2 = new javax.swing.JLabel();
    mailButton = new javax.swing.JButton();
    DMNo = new javax.swing.JLabel();
    DMName = new javax.swing.JLabel();

    setFocusCycleRoot(true);
    setOpaque(false);

    controlPanel.setOpaque(false);

    prevButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left_off.jpg"));
    prevButton.setBorderPainted(false);
    prevButton.setContentAreaFilled(false);
    prevButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left_on.jpg"));
    prevButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            prevButtonActionPerformed(evt);
        }
    });

    nextButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right_off.jpg"));
    nextButton.setBorderPainted(false);
    nextButton.setContentAreaFilled(false);
    nextButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right_on.jpg"));
    nextButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            nextButtonActionPerformed(evt);
        }
    });

    searchCusButton.setIcon(SystemInfo.getImageIcon("/button/search/search_customer_off.jpg"));
    searchCusButton.setBorderPainted(false);
    searchCusButton.setContentAreaFilled(false);
    searchCusButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_customer_on.jpg"));
    searchCusButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            searchCusButtonActionPerformed(evt);
        }
    });

    clearButton.setIcon(SystemInfo.getImageIcon("/button/common/new_registration_off.jpg"));
    clearButton.setBorderPainted(false);
    clearButton.setContentAreaFilled(false);
    clearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/new_registration_on.jpg"));
    clearButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            clearButtonActionPerformed(evt);
        }
    });

    registPanel.setOpaque(false);

    addButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
    addButton.setBorderPainted(false);
    addButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
    addButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            addButtonActionPerformed(evt);
        }
    });

    renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
    renewButton.setBorderPainted(false);
    renewButton.setEnabled(false);
    renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
    renewButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            renewButtonActionPerformed(evt);
        }
    });

    deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
    deleteButton.setBorderPainted(false);
    deleteButton.setEnabled(false);
    deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
    deleteButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            deleteButtonActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout registPanelLayout = new org.jdesktop.layout.GroupLayout(registPanel);
    registPanel.setLayout(registPanelLayout);
    registPanelLayout.setHorizontalGroup(
        registPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, registPanelLayout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );
    registPanelLayout.setVerticalGroup(
        registPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(registPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );

    deleteLabel.setFont(new java.awt.Font("MS UI Gothic", 1, 16)); // NOI18N
    deleteLabel.setForeground(java.awt.Color.red);
    deleteLabel.setText("�y�폜�ڋq�z");

    org.jdesktop.layout.GroupLayout controlPanelLayout = new org.jdesktop.layout.GroupLayout(controlPanel);
    controlPanel.setLayout(controlPanelLayout);
    controlPanelLayout.setHorizontalGroup(
        controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(controlPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(prevButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(nextButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(searchCusButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(20, 20, 20)
            .add(deleteLabel)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(registPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(20, 20, 20))
    );
    controlPanelLayout.setVerticalGroup(
        controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, controlPanelLayout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(deleteLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(nextButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(prevButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(searchCusButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(clearButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(registPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
    );

    invisiblePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel12.setText("�݌v�Z�p�ڰ� ");

    technicClameSalesTotal.setEditable(false);
    technicClameSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    technicClameSalesTotal.setText("0");
    technicClameSalesTotal.setDisabledTextColor(new java.awt.Color(255, 255, 255));

    jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel13.setText("�݌v���i�ԕi ");

    itemReturnedSalesTotal.setEditable(false);
    itemReturnedSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    itemReturnedSalesTotal.setText("0");
    itemReturnedSalesTotal.setDisabledTextColor(new java.awt.Color(255, 255, 255));

    org.jdesktop.layout.GroupLayout invisiblePanelLayout = new org.jdesktop.layout.GroupLayout(invisiblePanel);
    invisiblePanel.setLayout(invisiblePanelLayout);
    invisiblePanelLayout.setHorizontalGroup(
        invisiblePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(invisiblePanelLayout.createSequentialGroup()
            .add(invisiblePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, invisiblePanelLayout.createSequentialGroup()
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(invisiblePanelLayout.createSequentialGroup()
                    .add(30, 30, 30)
                    .add(invisiblePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(itemReturnedSalesTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(technicClameSalesTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(0, 4, Short.MAX_VALUE)))
            .addContainerGap())
    );
    invisiblePanelLayout.setVerticalGroup(
        invisiblePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(invisiblePanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(technicClameSalesTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(32, 32, 32)
            .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(itemReturnedSalesTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(229, Short.MAX_VALUE))
    );

    jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            jTabbedPane1StateChanged(evt);
        }
    });

    customerParamPane.setOpaque(false);

    shopPanel.setOpaque(false);

    org.jdesktop.layout.GroupLayout shopPanelLayout = new org.jdesktop.layout.GroupLayout(shopPanel);
    shopPanel.setLayout(shopPanelLayout);
    shopPanelLayout.setHorizontalGroup(
        shopPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 7, Short.MAX_VALUE)
    );
    shopPanelLayout.setVerticalGroup(
        shopPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 0, Short.MAX_VALUE)
    );

    customerNoPanel.setOpaque(false);

    cusIDLabel.setText("�ڋqNo.");

    customerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    customerNo.setColumns(15);

    showButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
    showButton.setBorderPainted(false);
    showButton.setContentAreaFilled(false);
    showButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
    showButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            showButtonActionPerformed(evt);
        }
    });

    customerID.setEditable(false);
    customerID.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

    org.jdesktop.layout.GroupLayout customerNoPanelLayout = new org.jdesktop.layout.GroupLayout(customerNoPanel);
    customerNoPanel.setLayout(customerNoPanelLayout);
    customerNoPanelLayout.setHorizontalGroup(
        customerNoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerNoPanelLayout.createSequentialGroup()
            .add(cusIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(11, 11, 11)
            .add(customerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(showButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(18, 18, 18)
            .add(customerID, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
            .add(20, 20, 20))
    );
    customerNoPanelLayout.setVerticalGroup(
        customerNoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerNoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(cusIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(customerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(customerNoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
            .add(customerID, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.LEADING, showButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );

    customerNamePanel.setOpaque(false);

    customerName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    customerName2.setColumns(20);
    customerName2.setInputKanji(true);

    cusNameLabel.setText("����");

    customerKana1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    customerKana1.setColumns(20);
    customerKana1.setInputKanji(true);
    customerKana1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            customerKana1ActionPerformed(evt);
        }
    });

    customerKana2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    customerKana2.setColumns(20);
    customerKana2.setInputKanji(true);

    alertMarkLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    alertMarkLabel.setPreferredSize(new java.awt.Dimension(39, 20));

    cusKanaLabel.setText("�ӂ肪��");

    customerName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    customerName1.setColumns(20);
    customerName1.setInputKanji(true);
    customerName1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            customerName1ActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout customerNamePanelLayout = new org.jdesktop.layout.GroupLayout(customerNamePanel);
    customerNamePanel.setLayout(customerNamePanelLayout);
    customerNamePanelLayout.setHorizontalGroup(
        customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerNamePanelLayout.createSequentialGroup()
            .add(customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(customerNamePanelLayout.createSequentialGroup()
                    .add(cusNameLabel)
                    .add(7, 7, 7)
                    .add(alertMarkLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(customerNamePanelLayout.createSequentialGroup()
                    .add(cusKanaLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(11, 11, 11)
                    .add(customerKana1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                .add(customerKana2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
    );
    customerNamePanelLayout.setVerticalGroup(
        customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerNamePanelLayout.createSequentialGroup()
            .add(customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(cusNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, alertMarkLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
            .add(0, 0, 0)
            .add(customerNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(customerKana1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(customerKana2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cusKanaLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(0, 0, 0))
    );

    customerAddressPanel.setOpaque(false);

    postalCodeLabel.setText("�X�֔ԍ�");

    postalCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    postalCode.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
    postalCode.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            postalCodeFocusLost(evt);
        }
    });

    searchAddressButton.setIcon(SystemInfo.getImageIcon("/button/search/search_address_off.jpg"));
    searchAddressButton.setBorderPainted(false);
    searchAddressButton.setContentAreaFilled(false);
    searchAddressButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_address_on.jpg"));
    searchAddressButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            searchAddressButtonActionPerformed(evt);
        }
    });

    address1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    address1.setColumns(16);
    address1.setInputKanji(true);

    addressLabel.setText("�s���{��");

    addressLabel1.setText("�s�撬��");

    address2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    address2.setColumns(64);
    address2.setInputKanji(true);

    addressLabel2.setText("����E�Ԓn");

    address3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    address3.setColumns(128);
    address3.setInputKanji(true);

    addressLabel3.setText("�}���V��������");

    address4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    address4.setColumns(128);
    address4.setInputKanji(true);

    org.jdesktop.layout.GroupLayout customerAddressPanelLayout = new org.jdesktop.layout.GroupLayout(customerAddressPanel);
    customerAddressPanel.setLayout(customerAddressPanelLayout);
    customerAddressPanelLayout.setHorizontalGroup(
        customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerAddressPanelLayout.createSequentialGroup()
            .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(org.jdesktop.layout.GroupLayout.LEADING, customerAddressPanelLayout.createSequentialGroup()
                    .add(postalCodeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(5, 5, 5)
                    .add(postalCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(searchAddressButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(org.jdesktop.layout.GroupLayout.LEADING, customerAddressPanelLayout.createSequentialGroup()
                    .add(addressLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(5, 5, 5)
                    .add(address1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(0, 0, Short.MAX_VALUE))
        .add(org.jdesktop.layout.GroupLayout.TRAILING, customerAddressPanelLayout.createSequentialGroup()
            .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(org.jdesktop.layout.GroupLayout.LEADING, customerAddressPanelLayout.createSequentialGroup()
                    .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(addressLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(addressLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(5, 5, 5)
                    .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(address4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .add(address3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .add(org.jdesktop.layout.GroupLayout.LEADING, customerAddressPanelLayout.createSequentialGroup()
                    .add(addressLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(address2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(12, 12, 12))
    );
    customerAddressPanelLayout.setVerticalGroup(
        customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerAddressPanelLayout.createSequentialGroup()
            .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(postalCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(postalCodeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(searchAddressButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(3, 3, 3)
            .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(addressLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(address1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(3, 3, 3)
            .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(addressLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(address2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(3, 3, 3)
            .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(addressLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(address3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(3, 3, 3)
            .add(customerAddressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(addressLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(address4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
    );

    customerPhoneNumberPanel.setOpaque(false);

    phoneNumberLabel.setText("�d�b�ԍ�");

    phoneNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    phoneNumber.setColumns(20);

    faxNumberLabel.setText("FAX�ԍ�");

    faxNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    faxNumber.setColumns(20);

    cellularNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cellularNumber.setColumns(20);

    cellularNumberLabel.setText("�g�єԍ�");

    org.jdesktop.layout.GroupLayout customerPhoneNumberPanelLayout = new org.jdesktop.layout.GroupLayout(customerPhoneNumberPanel);
    customerPhoneNumberPanel.setLayout(customerPhoneNumberPanelLayout);
    customerPhoneNumberPanelLayout.setHorizontalGroup(
        customerPhoneNumberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerPhoneNumberPanelLayout.createSequentialGroup()
            .add(customerPhoneNumberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerPhoneNumberPanelLayout.createSequentialGroup()
                    .add(cellularNumberLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(5, 5, 5)
                    .add(cellularNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(customerPhoneNumberPanelLayout.createSequentialGroup()
                    .add(phoneNumberLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(5, 5, 5)
                    .add(phoneNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(faxNumberLabel)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(faxNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(33, Short.MAX_VALUE))
    );
    customerPhoneNumberPanelLayout.setVerticalGroup(
        customerPhoneNumberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerPhoneNumberPanelLayout.createSequentialGroup()
            .add(customerPhoneNumberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(phoneNumberLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(phoneNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(faxNumberLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(faxNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerPhoneNumberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(cellularNumberLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cellularNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
    );

    customerMailPanel.setOpaque(false);

    pcMailLabel.setText("PC���[��");
    pcMailLabel.setPreferredSize(new java.awt.Dimension(44, 14));

    pcMail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    pcMail.setColumns(64);

    cellularMailLabel.setText("�g�у��[��");
    cellularMailLabel.setPreferredSize(new java.awt.Dimension(44, 14));

    cellularMail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cellularMail.setColumns(64);

    org.jdesktop.layout.GroupLayout customerMailPanelLayout = new org.jdesktop.layout.GroupLayout(customerMailPanel);
    customerMailPanel.setLayout(customerMailPanelLayout);
    customerMailPanelLayout.setHorizontalGroup(
        customerMailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerMailPanelLayout.createSequentialGroup()
            .add(customerMailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(cellularMailLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(pcMailLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(9, 9, 9)
            .add(customerMailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(pcMail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                .add(cellularMail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
            .add(10, 10, 10))
    );
    customerMailPanelLayout.setVerticalGroup(
        customerMailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerMailPanelLayout.createSequentialGroup()
            .add(customerMailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(pcMailLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(pcMail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerMailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(cellularMail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cellularMailLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2))
    );

    customerSexPanel.setOpaque(false);

    sendDmGroup.add(sendDmOK);
    sendDmOK.setSelected(true);
    sendDmOK.setText("��");
    sendDmOK.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    sendDmOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
    sendDmOK.setOpaque(false);

    sendDmGroup.add(sendDmNG);
    sendDmNG.setText("�s��");
    sendDmNG.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    sendDmNG.setMargin(new java.awt.Insets(0, 0, 0, 0));
    sendDmNG.setOpaque(false);

    sexLabel3.setText("�d�b�A��");

    sexLabel1.setText("���[���z�M");

    sendMailGroup.add(sendMailOK);
    sendMailOK.setSelected(true);
    sendMailOK.setText("��");
    sendMailOK.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    sendMailOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
    sendMailOK.setOpaque(false);

    sendMailGroup.add(sendMailNG);
    sendMailNG.setText("�s��");
    sendMailNG.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    sendMailNG.setMargin(new java.awt.Insets(0, 0, 0, 0));
    sendMailNG.setOpaque(false);

    sexLabel2.setText("�c�l�z�M");

    callFlagGroup.add(callFlagOK);
    callFlagOK.setSelected(true);
    callFlagOK.setText("��");
    callFlagOK.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    callFlagOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
    callFlagOK.setOpaque(false);

    callFlagGroup.add(callFlagNG);
    callFlagNG.setText("�s��");
    callFlagNG.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    callFlagNG.setMargin(new java.awt.Insets(0, 0, 0, 0));
    callFlagNG.setOpaque(false);

    creditContracLabel.setText("�M�̌_��");

    credit_lock_chk.setText("�s��");
    credit_lock_chk.setOpaque(false);

    org.jdesktop.layout.GroupLayout customerSexPanelLayout = new org.jdesktop.layout.GroupLayout(customerSexPanel);
    customerSexPanel.setLayout(customerSexPanelLayout);
    customerSexPanelLayout.setHorizontalGroup(
        customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerSexPanelLayout.createSequentialGroup()
            .add(0, 0, 0)
            .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(sexLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .add(sexLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                .add(sendMailOK, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(sendDmOK, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(15, 15, 15)
            .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerSexPanelLayout.createSequentialGroup()
                    .add(sendMailNG, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(21, 21, 21))
                .add(customerSexPanelLayout.createSequentialGroup()
                    .add(sendDmNG)
                    .add(31, 31, 31)))
            .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(sexLabel3)
                .add(creditContracLabel))
            .add(14, 14, 14)
            .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerSexPanelLayout.createSequentialGroup()
                    .add(callFlagOK)
                    .add(15, 15, 15)
                    .add(callFlagNG))
                .add(credit_lock_chk))
            .add(17, 17, 17))
    );
    customerSexPanelLayout.setVerticalGroup(
        customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerSexPanelLayout.createSequentialGroup()
            .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(sendMailOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(sendMailNG, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(sexLabel3)
                .add(callFlagNG, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(callFlagOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(sexLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(creditContracLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(credit_lock_chk))
                .add(customerSexPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sendDmOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sendDmNG, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sexLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
    );

    customerBirthdayPanel.setOpaque(false);

    birthdayLabel.setText("���N����");

    yearUnit.setMaximumRowCount(5);
    yearUnit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "����", "�吳", "���a", "����", "����" }));
    yearUnit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    yearUnit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            yearUnitActionPerformed(evt);
        }
    });

    birthYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    birthYear.setColumns(4);
    birthYear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    birthYear.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            birthYearFocusLost(evt);
        }
    });

    birthMonth.setMaximumRowCount(15);
    birthMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    birthMonth.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            birthMonthActionPerformed(evt);
        }
    });

    jLabel1.setText("��");

    birthDay.setMaximumRowCount(15);
    birthDay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    birthDay.addItem("");
    for(Integer i = 1; i <= 31; i ++)
    birthDay.addItem(i);
    birthDay.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            birthDayActionPerformed(evt);
        }
    });

    ageLabel.setText("��");

    age.setEditable(false);
    age.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    age.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

    yearLabel.setText("�N");

    jLabel2.setText("��");

    sexGroup.add(female);
    female.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
    female.setSelected(true);
    female.setText("��");
    female.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    female.setMargin(new java.awt.Insets(0, 0, 0, 0));
    female.setOpaque(false);

    sexGroup.add(male);
    male.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
    male.setText("�j");
    male.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    male.setMargin(new java.awt.Insets(0, 0, 0, 0));
    male.setOpaque(false);
    male.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            maleActionPerformed(evt);
        }
    });

    sexLabel.setText("����");

    sexLabel6.setText("�A���[�g ");

    Alert.setMaximumRowCount(15);
    Alert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    birthMonth.addItem("");
    for(Integer i = 1; i <= 12; i ++)
    birthMonth.addItem(i);
    Alert.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            AlertActionPerformed(evt);
        }
    });

    customerJobPanel.setOpaque(false);

    jobLabel.setText("�E��");

    job.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    job.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jobActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout customerJobPanelLayout = new org.jdesktop.layout.GroupLayout(customerJobPanel);
    customerJobPanel.setLayout(customerJobPanelLayout);
    customerJobPanelLayout.setHorizontalGroup(
        customerJobPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerJobPanelLayout.createSequentialGroup()
            .add(jobLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(job, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    customerJobPanelLayout.setVerticalGroup(
        customerJobPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerJobPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(jobLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(job, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );

    customerNotePanel.setOpaque(false);

    noteLabel.setText("���l");

    noteScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

    note.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    note.setColumns(20);
    note.setLineWrap(true);
    note.setRows(10);
    note.setTabSize(4);
    note.setInputKanji(true);
    noteScrollPane.setViewportView(note);

    org.jdesktop.layout.GroupLayout customerNotePanelLayout = new org.jdesktop.layout.GroupLayout(customerNotePanel);
    customerNotePanel.setLayout(customerNotePanelLayout);
    customerNotePanelLayout.setHorizontalGroup(
        customerNotePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerNotePanelLayout.createSequentialGroup()
            .add(noteLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(noteScrollPane))
    );
    customerNotePanelLayout.setVerticalGroup(
        customerNotePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerNotePanelLayout.createSequentialGroup()
            .add(customerNotePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerNotePanelLayout.createSequentialGroup()
                    .add(noteLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 49, Short.MAX_VALUE))
                .add(noteScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .add(0, 0, 0))
    );

    shopLabel.setText("�o�^�X��");

    shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    shop.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            shopActionPerformed(evt);
        }
    });

    BufferTimeLabel.setText("�\��o�b�t�@���� ");

    Reserved_buffer_time.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    Reserved_buffer_time.setColumns(3);

    timeLabel.setText("��");

    question_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    question_2.setColumns(20);
    question_2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            question_2ActionPerformed(evt);
        }
    });

    question_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    question_1.setColumns(20);
    question_1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            question_1ActionPerformed(evt);
        }
    });

    shopLabel3.setText("�A���P�[�g ");

    jLabel30.setText("��");

    org.jdesktop.layout.GroupLayout customerBirthdayPanelLayout = new org.jdesktop.layout.GroupLayout(customerBirthdayPanel);
    customerBirthdayPanel.setLayout(customerBirthdayPanelLayout);
    customerBirthdayPanelLayout.setHorizontalGroup(
        customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerJobPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .add(customerBirthdayPanelLayout.createSequentialGroup()
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                .add(org.jdesktop.layout.GroupLayout.LEADING, customerBirthdayPanelLayout.createSequentialGroup()
                    .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(sexLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(sexLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(Alert, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(org.jdesktop.layout.GroupLayout.LEADING, customerBirthdayPanelLayout.createSequentialGroup()
                    .add(1, 1, 1)
                    .add(birthdayLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(yearUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(birthYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE)
            .add(yearLabel)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(birthMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jLabel1)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 19, Short.MAX_VALUE)
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerBirthdayPanelLayout.createSequentialGroup()
                    .add(age, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(ageLabel)
                    .add(55, 55, 55))
                .add(customerBirthdayPanelLayout.createSequentialGroup()
                    .add(birthDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jLabel30)
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .add(customerBirthdayPanelLayout.createSequentialGroup()
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerBirthdayPanelLayout.createSequentialGroup()
                    .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(customerBirthdayPanelLayout.createSequentialGroup()
                            .add(BufferTimeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(Reserved_buffer_time, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                            .add(timeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(customerBirthdayPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(customerBirthdayPanelLayout.createSequentialGroup()
                                    .add(79, 79, 79)
                                    .add(jLabel2))
                                .add(shopLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(customerBirthdayPanelLayout.createSequentialGroup()
                            .add(76, 76, 76)
                            .add(female, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(10, 10, 10)
                            .add(male)))
                    .add(0, 0, Short.MAX_VALUE))
                .add(customerNotePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
        .add(customerBirthdayPanelLayout.createSequentialGroup()
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerBirthdayPanelLayout.createSequentialGroup()
                    .add(shopLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(9, 9, 9)
                    .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(question_2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                        .add(question_1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(0, 0, Short.MAX_VALUE))
                .add(customerBirthdayPanelLayout.createSequentialGroup()
                    .add(shopLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(shop, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .add(10, 10, 10))
    );
    customerBirthdayPanelLayout.setVerticalGroup(
        customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerBirthdayPanelLayout.createSequentialGroup()
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(yearUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(birthYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(birthMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(birthDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(yearLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(birthdayLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(sexLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(Alert, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(female)
                .add(male, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(sexLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(age, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(ageLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerJobPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(2, 2, 2)
            .add(customerNotePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(4, 4, 4)
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(shopLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(BufferTimeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(Reserved_buffer_time, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(timeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(question_1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(shopLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(2, 2, 2)
            .add(customerBirthdayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(question_2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(shopLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(20, Short.MAX_VALUE))
    );

    sosiaGearPanel.setOpaque(false);

    sosiaGearLabel.setText("WEB����A��");

    changeSosiaGearButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    changeSosiaGearButton.setContentAreaFilled(false);
    changeSosiaGearButton.setEnabled(false);
    changeSosiaGearButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            changeSosiaGearButtonActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout sosiaGearPanelLayout = new org.jdesktop.layout.GroupLayout(sosiaGearPanel);
    sosiaGearPanel.setLayout(sosiaGearPanelLayout);
    sosiaGearPanelLayout.setHorizontalGroup(
        sosiaGearPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(sosiaGearPanelLayout.createSequentialGroup()
            .add(sosiaGearLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(0, 0, 0)
            .add(changeSosiaGearButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
            .addContainerGap())
    );
    sosiaGearPanelLayout.setVerticalGroup(
        sosiaGearPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(sosiaGearLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        .add(changeSosiaGearButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pointPanel.setOpaque(false);

    sosiaGearLabel1.setText("���݃|�C���g");

    genzaiPoint.setEditable(false);
    genzaiPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

    jLabel14.setText("�|�C���g");

    org.jdesktop.layout.GroupLayout pointPanelLayout = new org.jdesktop.layout.GroupLayout(pointPanel);
    pointPanel.setLayout(pointPanelLayout);
    pointPanelLayout.setHorizontalGroup(
        pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(pointPanelLayout.createSequentialGroup()
            .add(sosiaGearLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(5, 5, 5)
            .add(genzaiPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    pointPanelLayout.setVerticalGroup(
        pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(sosiaGearLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(genzaiPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jLabel14))
    );

    cusIspotIDLabel.setText("ispot���ID");

    jPanel2.setOpaque(false);

    firstComingMotivePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("���񗈓X���@"));
    firstComingMotivePanel.setOpaque(false);

    firstComingMotiveLabel.setText("���");

    firstComingMotive.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

    firstComingMotiveLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    firstComingMotiveLabel1.setText("����");

    firstComingMotiveNote.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    firstComingMotiveNote.setColumns(16);
    firstComingMotiveNote.setInputKanji(true);

    org.jdesktop.layout.GroupLayout firstComingMotivePanelLayout = new org.jdesktop.layout.GroupLayout(firstComingMotivePanel);
    firstComingMotivePanel.setLayout(firstComingMotivePanelLayout);
    firstComingMotivePanelLayout.setHorizontalGroup(
        firstComingMotivePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, firstComingMotivePanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(firstComingMotiveLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(firstComingMotive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(firstComingMotiveLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(firstComingMotiveNote, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(26, 26, 26))
    );
    firstComingMotivePanelLayout.setVerticalGroup(
        firstComingMotivePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, firstComingMotivePanelLayout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(firstComingMotivePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(firstComingMotiveLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(firstComingMotiveNote, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(firstComingMotiveLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(firstComingMotive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    freeHeadingPanel.setOpaque(false);
    freeHeadingPanel.setLayout(null);

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(freeHeadingPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 406, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(firstComingMotivePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 398, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(29, 29, 29))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .add(firstComingMotivePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(freeHeadingPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
            .addContainerGap())
    );

    optionTab.addTab("�t���[����", jPanel2);

    jPanel5.setOpaque(false);

    introducerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("�Љ"));
    introducerPanel.setOpaque(false);

    introducerScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introducerScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    introducer.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null}
        },
        new String [] {
            "�ڋqNo.", "�ڋq��", "�ŏI���X��", "�S����", "�ڍ�", "����", "�폜"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, true, true, true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    introducer.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    introducer.setAutoscrolls(false);
    introducer.setSelectionBackground(new java.awt.Color(220, 220, 220));
    introducer.setSelectionForeground(new java.awt.Color(0, 0, 0));
    introducer.getTableHeader().setReorderingAllowed(false);
    this.initIntroducerColumn();
    introducer.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SwingUtil.setJTableHeaderRenderer(introducer, SystemInfo.getTableHeaderRenderer());
    introducerScrollPane.setViewportView(introducer);

    addIntroducerButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
    addIntroducerButton.setBorderPainted(false);
    addIntroducerButton.setContentAreaFilled(false);
    addIntroducerButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
    addIntroducerButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            addIntroducerButtonActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout introducerPanelLayout = new org.jdesktop.layout.GroupLayout(introducerPanel);
    introducerPanel.setLayout(introducerPanelLayout);
    introducerPanelLayout.setHorizontalGroup(
        introducerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, introducerPanelLayout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(addIntroducerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(21, 21, 21))
        .add(introducerPanelLayout.createSequentialGroup()
            .add(introducerScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(0, 0, Short.MAX_VALUE))
    );
    introducerPanelLayout.setVerticalGroup(
        introducerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanelLayout.createSequentialGroup()
            .add(addIntroducerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(2, 2, 2)
            .add(introducerScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    introducerPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("�Љ�ꗗ"));
    introducerPanel1.setOpaque(false);

    introduceScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introduceScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    introduce.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null}
        },
        new String [] {
            "�ڋqNo.", "�ڋq��", "�ŏI���X��", "�S����", "�ڍ�", "����"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, true, true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    introduce.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    introduce.setSelectionBackground(new java.awt.Color(220, 220, 220));
    introduce.setSelectionForeground(new java.awt.Color(0, 0, 0));
    introduce.getTableHeader().setReorderingAllowed(false);
    this.initIntroduceColumn();
    introduce.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SwingUtil.setJTableHeaderRenderer(introduce, SystemInfo.getTableHeaderRenderer());
    introduceScrollPane.setViewportView(introduce);

    org.jdesktop.layout.GroupLayout introducerPanel1Layout = new org.jdesktop.layout.GroupLayout(introducerPanel1);
    introducerPanel1.setLayout(introducerPanel1Layout);
    introducerPanel1Layout.setHorizontalGroup(
        introducerPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introduceScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
    );
    introducerPanel1Layout.setVerticalGroup(
        introducerPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introduceScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
    );

    introducerPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("�Ƒ��\��"));
    introducerPanel2.setOpaque(false);

    familyScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    familyScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    family.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null}
        },
        new String [] {
            "�ڋqNo.", "�ڋq��", "�ŏI���X��", "�S����", "�ڍ�", "�폜"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, true, true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    family.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    family.setSelectionBackground(new java.awt.Color(220, 220, 220));
    family.setSelectionForeground(new java.awt.Color(0, 0, 0));
    family.getTableHeader().setReorderingAllowed(false);
    this.initFamilyColumn();
    family.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SwingUtil.setJTableHeaderRenderer(family, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(family);
    family.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    family.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            familyPropertyChange(evt);
        }
    });
    familyScrollPane.setViewportView(family);

    addFamilyButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
    addFamilyButton.setBorderPainted(false);
    addFamilyButton.setContentAreaFilled(false);
    addFamilyButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
    addFamilyButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            addFamilyButtonActionPerformed(evt);
        }
    });

    sexLabel4.setForeground(java.awt.Color.blue);
    sexLabel4.setText("�� �A������ɉe�����܂��̂ŁA�Ƒ��\���ɕύX������ꍇ�͍X�V�����肢���܂��B");

    sexLabel5.setText("���͉Ƒ��o�^�̃��C���ł��B");

    org.jdesktop.layout.GroupLayout introducerPanel2Layout = new org.jdesktop.layout.GroupLayout(introducerPanel2);
    introducerPanel2.setLayout(introducerPanel2Layout);
    introducerPanel2Layout.setHorizontalGroup(
        introducerPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanel2Layout.createSequentialGroup()
            .add(296, 296, 296)
            .add(addFamilyButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .add(org.jdesktop.layout.GroupLayout.TRAILING, introducerPanel2Layout.createSequentialGroup()
            .add(introducerPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(introducerPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(sexLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE))
                .add(org.jdesktop.layout.GroupLayout.LEADING, sexLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(8, 8, 8))
        .add(familyScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
    );
    introducerPanel2Layout.setVerticalGroup(
        introducerPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanel2Layout.createSequentialGroup()
            .add(introducerPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(sexLabel5)
                .add(addFamilyButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(familyScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 11, Short.MAX_VALUE)
            .add(sexLabel4))
    );

    org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel5Layout.createSequentialGroup()
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(introducerPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 415, Short.MAX_VALUE)
                .add(introducerPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(introducerPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 415, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel5Layout.createSequentialGroup()
            .add(introducerPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(3, 3, 3)
            .add(introducerPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(3, 3, 3)
            .add(introducerPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(0, 0, 0))
    );

    optionTab.addTab("�Љ�E�Ƒ��\��", jPanel5);

    org.jdesktop.layout.GroupLayout customerParamPaneLayout = new org.jdesktop.layout.GroupLayout(customerParamPane);
    customerParamPane.setLayout(customerParamPaneLayout);
    customerParamPaneLayout.setHorizontalGroup(
        customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerParamPaneLayout.createSequentialGroup()
            .add(0, 0, 0)
            .add(customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerParamPaneLayout.createSequentialGroup()
                    .add(customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(customerParamPaneLayout.createSequentialGroup()
                            .add(customerNoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(0, 7, Short.MAX_VALUE))
                        .add(customerNamePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(shopPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(customerParamPaneLayout.createSequentialGroup()
                    .add(customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, customerParamPaneLayout.createSequentialGroup()
                                .add(sosiaGearPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pointPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, customerSexPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, customerAddressPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, customerPhoneNumberPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, customerMailPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(customerParamPaneLayout.createSequentialGroup()
                            .add(cusIspotIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(CustomerIspotIDjTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(customerBirthdayPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(0, 0, Short.MAX_VALUE)))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(optionTab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 436, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(24, 24, 24))
    );
    customerParamPaneLayout.setVerticalGroup(
        customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerParamPaneLayout.createSequentialGroup()
            .add(customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(customerParamPaneLayout.createSequentialGroup()
                    .add(customerNoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(2, 2, 2)
                    .add(customerNamePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(2, 2, 2)
                    .add(shopPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, 0)
                    .add(customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(cusIspotIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(CustomerIspotIDjTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(4, 4, 4)
                    .add(customerParamPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(pointPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(sosiaGearPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(0, 0, 0)
                    .add(customerAddressPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(2, 2, 2)
                    .add(customerPhoneNumberPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(2, 2, 2)
                    .add(customerMailPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, 0)
                    .add(customerSexPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(1, 1, 1)
                    .add(customerBirthdayPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(optionTab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 631, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .add(customerParamPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(463, 463, 463))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel1Layout.createSequentialGroup()
            .add(customerParamPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(0, 0, 0))
    );

    jTabbedPane1.addTab("��{���", jPanel1);

    visitInfoPanel.setOpaque(false);

    accountScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    account.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null}
        },
        new String [] {
            "���X�X��", "���X��", "��S����", "�w", "����\���", "<html>����<br>���z</html>", "�J���e"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    account.setSelectionForeground(new java.awt.Color(0, 0, 0));
    account.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    account.getTableHeader().setReorderingAllowed(false);
    this.initAccountColumn();
    SwingUtil.setJTableHeaderRenderer(account, SystemInfo.getTableHeaderRenderer());
    account.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(account);
    account.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            accountMouseReleased(evt);
        }
    });
    account.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            accountKeyReleased(evt);
        }
    });
    accountScrollPane.setViewportView(account);
    account.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    techItemTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    techItemTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null}
        },
        new String [] {
            "�敪", "����", "�P��", "����", "����", "���z", "�S����"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    techItemTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    techItemTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    techItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    techItemTable.getTableHeader().setReorderingAllowed(false);
    SwingUtil.setJTableHeaderRenderer(techItemTable, SystemInfo.getTableHeaderRenderer());
    techItemTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(techItemTable);
    techItemTableScrollPane.setViewportView(techItemTable);

    jLabel10.setText("�S�̊�����");

    discountName.setEditable(false);

    jLabel11.setText("�S�̊����z");

    discountValue.setEditable(false);
    discountValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    discountValue.setDisabledTextColor(new java.awt.Color(255, 255, 255));

    payment.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "�x����", "��������", "�����ȊO", "���|��"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    payment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    payment.setSelectionForeground(new java.awt.Color(0, 0, 0));
    payment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    payment.getTableHeader().setReorderingAllowed(false);
    SwingUtil.setJTableHeaderRenderer(payment, SystemInfo.getTableHeaderRenderer());
    payment.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(payment);
    paymentScrollPane.setViewportView(payment);

    jLabel15.setText("�x�����");

    org.jdesktop.layout.GroupLayout visitInfoPanelLayout = new org.jdesktop.layout.GroupLayout(visitInfoPanel);
    visitInfoPanel.setLayout(visitInfoPanelLayout);
    visitInfoPanelLayout.setHorizontalGroup(
        visitInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(visitInfoPanelLayout.createSequentialGroup()
            .add(10, 10, 10)
            .add(jLabel15)
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .add(org.jdesktop.layout.GroupLayout.TRAILING, visitInfoPanelLayout.createSequentialGroup()
            .add(visitInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(visitInfoPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(paymentScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .add(visitInfoPanelLayout.createSequentialGroup()
                    .add(77, 77, 77)
                    .add(jLabel10)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(discountName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jLabel11)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(discountValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(20, 20, 20))
        .add(org.jdesktop.layout.GroupLayout.TRAILING, visitInfoPanelLayout.createSequentialGroup()
            .add(visitInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(org.jdesktop.layout.GroupLayout.LEADING, techItemTableScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .add(accountScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addContainerGap())
    );
    visitInfoPanelLayout.setVerticalGroup(
        visitInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, visitInfoPanelLayout.createSequentialGroup()
            .add(3, 3, 3)
            .add(accountScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(8, 8, 8)
            .add(techItemTableScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 177, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(visitInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(discountValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel11)
                .add(discountName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel10))
            .add(4, 4, 4)
            .add(jLabel15)
            .add(1, 1, 1)
            .add(paymentScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(26, 26, 26))
    );

    salesInfoTabbedPane.addTab(" ���X ", visitInfoPanel);

    cancel.setOpaque(false);

    jLabel29.setText("Web�\�� ");

    webCancelGroup.add(WebOKCancel);
    WebOKCancel.setSelected(true);
    WebOKCancel.setText("��");
    WebOKCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    WebOKCancel.setMargin(new java.awt.Insets(0, 0, 0, 0));
    WebOKCancel.setOpaque(false);

    webCancelGroup.add(WebNGCancel);
    WebNGCancel.setText("�s��");
    WebNGCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    WebNGCancel.setMargin(new java.awt.Insets(0, 0, 0, 0));
    WebNGCancel.setOpaque(false);

    cancelsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    cancels.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null}
        },
        new String [] {
            "�\��X��", "�\���", "�L���Z���敪"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Object.class, java.lang.String.class, java.lang.Object.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    cancels.setSelectionForeground(new java.awt.Color(0, 0, 0));
    cancels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    cancels.getTableHeader().setReorderingAllowed(false);
    this.initCancelColumn();
    SwingUtil.setJTableHeaderRenderer(cancels, SystemInfo.getTableHeaderRenderer());
    cancels.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(cancels);
    cancels.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            cancelsMouseReleased(evt);
        }
    });
    cancels.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            cancelsKeyReleased(evt);
        }
    });
    cancelsScrollPane.setViewportView(cancels);

    org.jdesktop.layout.GroupLayout cancelLayout = new org.jdesktop.layout.GroupLayout(cancel);
    cancel.setLayout(cancelLayout);
    cancelLayout.setHorizontalGroup(
        cancelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(cancelLayout.createSequentialGroup()
            .add(1, 1, 1)
            .add(cancelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(cancelLayout.createSequentialGroup()
                    .add(jLabel29)
                    .add(136, 136, 136)
                    .add(WebOKCancel)
                    .add(15, 15, 15)
                    .add(WebNGCancel)
                    .addContainerGap())
                .add(cancelsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
    );
    cancelLayout.setVerticalGroup(
        cancelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(cancelLayout.createSequentialGroup()
            .addContainerGap()
            .add(cancelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel29)
                .add(WebNGCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(WebOKCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(15, 15, 15)
            .add(cancelsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
            .addContainerGap())
    );

    salesInfoTabbedPane.addTab("��ݾ� ", cancel);

    techInfoPanel.setOpaque(false);

    techTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    techTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null}
        },
        new String [] {
            "���t", "����", "�P��", "����", "����", "���z", "�S����"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    techTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    techTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    techTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    techTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    techTable.getTableHeader().setReorderingAllowed(false);
    SwingUtil.setJTableHeaderRenderer(techTable, SystemInfo.getTableHeaderRenderer());
    techTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(techTable);
    techTableScrollPane.setViewportView(techTable);
    techTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    org.jdesktop.layout.GroupLayout techInfoPanelLayout = new org.jdesktop.layout.GroupLayout(techInfoPanel);
    techInfoPanel.setLayout(techInfoPanelLayout);
    techInfoPanelLayout.setHorizontalGroup(
        techInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(techTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
    );
    techInfoPanelLayout.setVerticalGroup(
        techInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(techInfoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(techTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
    );

    salesInfoTabbedPane.addTab(" �Z�p ", techInfoPanel);

    itemInfoPanel.setOpaque(false);

    itemTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    itemTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null}
        },
        new String [] {
            "���t", "����", "�P��", "����", "����", "���z", "�S����"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    itemTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    itemTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    itemTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    itemTable.getTableHeader().setReorderingAllowed(false);
    SwingUtil.setJTableHeaderRenderer(itemTable, SystemInfo.getTableHeaderRenderer());
    itemTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(itemTable);
    itemTableScrollPane.setViewportView(itemTable);

    org.jdesktop.layout.GroupLayout itemInfoPanelLayout = new org.jdesktop.layout.GroupLayout(itemInfoPanel);
    itemInfoPanel.setLayout(itemInfoPanelLayout);
    itemInfoPanelLayout.setHorizontalGroup(
        itemInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(itemTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
    );
    itemInfoPanelLayout.setVerticalGroup(
        itemInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(itemInfoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(itemTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
    );

    salesInfoTabbedPane.addTab(" ���i", itemInfoPanel);

    visitedMemoPanel.setOpaque(false);

    visitedMemoTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    visitedMemoTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null},
            {null, null},
            {null, null},
            {null, null}
        },
        new String [] {
            "���t", "���X����"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    visitedMemoTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    visitedMemoTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    visitedMemoTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    visitedMemoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    visitedMemoTable.getTableHeader().setReorderingAllowed(false);
    visitedMemoTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT + 15);
    visitedMemoTable.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
    SwingUtil.setJTableHeaderRenderer(visitedMemoTable, SystemInfo.getTableHeaderRenderer());
    visitedMemoTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            visitedMemoTableMouseReleased(evt);
        }
    });
    visitedMemoTable.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            visitedMemoTableKeyReleased(evt);
        }
    });
    visitedMemoTableScrollPane.setViewportView(visitedMemoTable);

    visitedMemoUpdateButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
    visitedMemoUpdateButton.setBorderPainted(false);
    visitedMemoUpdateButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
    visitedMemoUpdateButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            visitedMemoUpdateButtonActionPerformed(evt);
        }
    });

    visitedMemoScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

    visitedMemo.setBorder(null);
    visitedMemo.setColumns(20);
    visitedMemo.setLineWrap(true);
    visitedMemo.setRows(5);
    visitedMemo.setTabSize(0);
    visitedMemo.setInputKanji(true);
    visitedMemoScrollPane.setViewportView(visitedMemo);

    org.jdesktop.layout.GroupLayout visitedMemoPanelLayout = new org.jdesktop.layout.GroupLayout(visitedMemoPanel);
    visitedMemoPanel.setLayout(visitedMemoPanelLayout);
    visitedMemoPanelLayout.setHorizontalGroup(
        visitedMemoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, visitedMemoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(visitedMemoScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(visitedMemoUpdateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
        .add(visitedMemoTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
    );
    visitedMemoPanelLayout.setVerticalGroup(
        visitedMemoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, visitedMemoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(visitedMemoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(visitedMemoUpdateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(visitedMemoScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(visitedMemoTableScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 346, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );

    salesInfoTabbedPane.addTab(" ���X�� ", visitedMemoPanel);

    pointInfoPanel.setOpaque(false);
    pointInfoPanel.setLayout(null);

    pointTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    pointTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "���X�X��", "���X��", "�g�p�|�C���g", "���Z�|�C���g"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Object.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    pointTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    pointTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    pointTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    pointTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    pointTable.getTableHeader().setReorderingAllowed(false);
    SwingUtil.setJTableHeaderRenderer(pointTable, SystemInfo.getTableHeaderRenderer());
    pointTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(pointTable);
    pointTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            pointTableMouseReleased(evt);
        }
    });
    pointTable.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            pointTableKeyReleased(evt);
        }
    });
    pointTableScrollPane.setViewportView(pointTable);

    pointInfoPanel.add(pointTableScrollPane);
    pointTableScrollPane.setBounds(0, 67, 410, 387);

    pointUpdateButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
    pointUpdateButton.setBorderPainted(false);
    pointUpdateButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
    pointUpdateButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            pointUpdateButtonActionPerformed(evt);
        }
    });
    pointInfoPanel.add(pointUpdateButton);
    pointUpdateButton.setBounds(290, 40, 92, 25);

    jLabel20.setText("���X�X��");
    pointInfoPanel.add(jLabel20);
    jLabel20.setBounds(4, 10, 70, 16);

    jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel21.setText("���X��");
    pointInfoPanel.add(jLabel21);
    jLabel21.setBounds(130, 10, 56, 16);

    jLabel22.setText("�g�p�|�C���g");
    pointInfoPanel.add(jLabel22);
    jLabel22.setBounds(4, 38, 58, 13);

    jLabel23.setText("���Z�|�C���g");
    pointInfoPanel.add(jLabel23);
    jLabel23.setBounds(146, 32, 58, 25);

    suppliedPoint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    suppliedPoint.setColumns(4);
    suppliedPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    suppliedPoint.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            suppliedPointFocusLost(evt);
        }
    });
    pointInfoPanel.add(suppliedPoint);
    suppliedPoint.setBounds(210, 35, 65, 20);

    usePoint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    usePoint.setColumns(4);
    usePoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    usePoint.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            usePointFocusLost(evt);
        }
    });
    pointInfoPanel.add(usePoint);
    usePoint.setBounds(69, 35, 65, 20);

    shopPoint.setText("XXXXX");
    pointInfoPanel.add(shopPoint);
    shopPoint.setBounds(80, 10, 60, 16);

    datePoint.setText("yyyy�NMM��dd��");
    pointInfoPanel.add(datePoint);
    datePoint.setBounds(190, 10, 86, 16);

    pointAddButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
    pointAddButton.setBorderPainted(false);
    pointAddButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
    pointAddButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            pointAddButtonActionPerformed(evt);
        }
    });
    pointInfoPanel.add(pointAddButton);
    pointAddButton.setBounds(290, 10, 92, 26);

    salesInfoTabbedPane.addTab("�߲�� ", pointInfoPanel);

    prepaidInfoPanel.setOpaque(false);
    prepaidInfoPanel.setLayout(null);

    prepaidTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    prepaidTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "���X�X��", "���X��", "���p�z", "�̔��z"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Object.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    prepaidTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    prepaidTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    prepaidTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    prepaidTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    prepaidTable.getTableHeader().setReorderingAllowed(false);
    SwingUtil.setJTableHeaderRenderer(prepaidTable, SystemInfo.getTableHeaderRenderer());
    prepaidTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SelectTableCellRenderer.setSelectTableCellRenderer(prepaidTable);
    prepaidTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            prepaidTableMouseReleased(evt);
        }
    });
    prepaidTable.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            prepaidTableKeyReleased(evt);
        }
    });
    prepaidTableScrollPane.setViewportView(prepaidTable);

    prepaidInfoPanel.add(prepaidTableScrollPane);
    prepaidTableScrollPane.setBounds(0, 34, 410, 420);

    jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel26.setText("���ݎc");
    prepaidInfoPanel.add(jLabel26);
    jLabel26.setBounds(0, 5, 50, 20);

    currentPrepaid.setEditable(false);
    currentPrepaid.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    currentPrepaid.setText("0");
    currentPrepaid.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    prepaidInfoPanel.add(currentPrepaid);
    currentPrepaid.setBounds(50, 5, 60, 20);

    jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel24.setText("�~");
    prepaidInfoPanel.add(jLabel24);
    jLabel24.setBounds(110, 5, 20, 20);

    salesInfoTabbedPane.addTab("����߲�� ", prepaidInfoPanel);

    jPanel4.setOpaque(false);
    jPanel4.setLayout(null);

    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel6.setText("���񗈓X�� ");
    jPanel4.add(jLabel6);
    jLabel6.setBounds(0, 80, 70, 20);

    visitCount.setEditable(false);
    visitCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    visitCount.setText("0");
    visitCount.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    jPanel4.add(visitCount);
    visitCount.setBounds(380, 30, 60, 20);

    jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel7.setText("���i���� ");
    jPanel4.add(jLabel7);
    jLabel7.setBounds(310, 90, 70, 20);

    technicSalesTotal.setEditable(false);
    technicSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    technicSalesTotal.setText("0");
    technicSalesTotal.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    jPanel4.add(technicSalesTotal);
    technicSalesTotal.setBounds(380, 60, 60, 20);

    jLabel8.setText("���Z�p����{���i����  ");
    jPanel4.add(jLabel8);
    jLabel8.setBounds(470, 80, 150, 20);

    salesTotal.setEditable(false);
    salesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    salesTotal.setText("0");
    salesTotal.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    jPanel4.add(salesTotal);
    salesTotal.setBounds(530, 60, 75, 20);

    itemSalesTotal.setEditable(false);
    itemSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    itemSalesTotal.setText("0");
    itemSalesTotal.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    jPanel4.add(itemSalesTotal);
    itemSalesTotal.setBounds(380, 90, 60, 20);

    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel16.setText("�����\���");
    jPanel4.add(jLabel16);
    jLabel16.setBounds(0, 50, 60, 20);

    nextReserveChangeButton.setIcon(SystemInfo.getImageIcon("/button/reservation/change_reservation_off.jpg"));
    nextReserveChangeButton.setBorderPainted(false);
    nextReserveChangeButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/change_reservation_on.jpg"));
    nextReserveChangeButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            nextReserveChangeButtonActionPerformed(evt);
        }
    });
    jPanel4.add(nextReserveChangeButton);
    nextReserveChangeButton.setBounds(190, 47, 92, 25);

    jLabel17.setText("  �i �����O");
    jPanel4.add(jLabel17);
    jLabel17.setBounds(460, 30, 60, 20);

    beforeVisitNum.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    beforeVisitNum.setText("0");
    beforeVisitNum.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    jPanel4.add(beforeVisitNum);
    beforeVisitNum.setBounds(530, 30, 40, 20);

    jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel18.setText("��");
    jPanel4.add(jLabel18);
    jLabel18.setBounds(440, 30, 20, 20);

    jLabel19.setText("  �� �j");
    jPanel4.add(jLabel19);
    jLabel19.setBounds(580, 30, 40, 20);

    firstVisitYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    firstVisitYear.setColumns(4);
    firstVisitYear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    firstVisitYear.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            firstVisitYearFocusLost(evt);
        }
    });
    jPanel4.add(firstVisitYear);
    firstVisitYear.setBounds(70, 80, 60, 20);

    jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel25.setText("���X�� ");
    jPanel4.add(jLabel25);
    jLabel25.setBounds(310, 30, 70, 20);

    yearLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    yearLabel1.setText("�N");
    jPanel4.add(yearLabel1);
    yearLabel1.setBounds(140, 80, 20, 20);

    firstVisitMonth.setMaximumRowCount(15);
    firstVisitMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    firstVisitMonth.addItem("");
    for(Integer i = 1; i <= 12; i ++)
    firstVisitMonth.addItem(i);
    firstVisitMonth.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            firstVisitMonthActionPerformed(evt);
        }
    });
    jPanel4.add(firstVisitMonth);
    firstVisitMonth.setBounds(170, 80, 40, 20);

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel3.setText("��");
    jPanel4.add(jLabel3);
    jLabel3.setBounds(210, 80, 20, 20);

    firstVisitDay.setMaximumRowCount(15);
    firstVisitDay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    firstVisitDay.addItem("");
    for(Integer i = 1; i <= 31; i ++)
    firstVisitDay.addItem(i);
    firstVisitDay.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            firstVisitDayActionPerformed(evt);
        }
    });
    jPanel4.add(firstVisitDay);
    firstVisitDay.setBounds(230, 80, 40, 19);

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("��");
    jPanel4.add(jLabel4);
    jLabel4.setBounds(270, 80, 20, 20);

    customerRank.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    customerRank.setText("�ڋq�����N �y �` �z");
    jPanel4.add(customerRank);
    customerRank.setBounds(610, 30, 100, 26);

    cusNameLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    cusNameLabel1.setText("����");
    jPanel4.add(cusNameLabel1);
    cusNameLabel1.setBounds(0, 16, 70, 30);

    visitName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    visitName.setPreferredSize(new java.awt.Dimension(39, 20));
    jPanel4.add(visitName);
    visitName.setBounds(70, 20, 130, 20);

    cusIDLabel1.setText("�ڋqNo.");
    jPanel4.add(cusIDLabel1);
    cusIDLabel1.setBounds(0, 0, 70, 20);

    visitNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    visitNo.setPreferredSize(new java.awt.Dimension(39, 20));
    jPanel4.add(visitNo);
    visitNo.setBounds(70, 0, 150, 20);

    jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel32.setText("�Z�p���� ");
    jPanel4.add(jLabel32);
    jLabel32.setBounds(310, 60, 70, 20);

    jLabel33.setText("������z");
    jPanel4.add(jLabel33);
    jLabel33.setBounds(470, 60, 60, 20);

    salesTotal1.setEditable(false);
    salesTotal1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    salesTotal1.setText("0");
    salesTotal1.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    jPanel4.add(salesTotal1);
    salesTotal1.setBounds(690, 60, 75, 20);

    jLabel34.setText("�R�[�X�_��");
    jPanel4.add(jLabel34);
    jLabel34.setBounds(630, 60, 60, 20);

    jLabel35.setText("�������z");
    jPanel4.add(jLabel35);
    jLabel35.setBounds(630, 90, 60, 20);

    salesTotal2.setEditable(false);
    salesTotal2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    salesTotal2.setText("0");
    salesTotal2.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    jPanel4.add(salesTotal2);
    salesTotal2.setBounds(690, 90, 75, 20);

    cmbNextReserve.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cmbNextReserve.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbNextReserveActionPerformed(evt);
        }
    });
    jPanel4.add(cmbNextReserve);
    cmbNextReserve.setBounds(70, 50, 115, 20);

    jPanel6.setOpaque(false);
    jPanel6.setLayout(null);

    cmbTargetYear.setEditable(true);
    cmbTargetYear.setMaximumRowCount(12);
    cmbTargetYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    jPanel6.add(cmbTargetYear);
    cmbTargetYear.setBounds(80, 12, 90, 19);

    lblBaseMonth.setText("�\�����");
    jPanel6.add(lblBaseMonth);
    lblBaseMonth.setBounds(0, 12, 80, 20);

    cmbTargetMonth.setMaximumRowCount(12);
    cmbTargetMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
    jPanel6.add(cmbTargetMonth);
    cmbTargetMonth.setBounds(190, 12, 36, 19);

    lblTargetPeriod8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblTargetPeriod8.setText("��");
    jPanel6.add(lblTargetPeriod8);
    lblTargetPeriod8.setBounds(230, 12, 20, 20);

    lblTargetPeriod9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblTargetPeriod9.setText("�N");
    jPanel6.add(lblTargetPeriod9);
    lblTargetPeriod9.setBounds(170, 12, 20, 20);

    showVisitCycleButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
    showVisitCycleButton.setBorderPainted(false);
    showVisitCycleButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
    showVisitCycleButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            showVisitCycleButtonActionPerformed(evt);
        }
    });
    jPanel6.add(showVisitCycleButton);
    showVisitCycleButton.setBounds(270, 10, 92, 25);

    visitCyclelScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    visitCycleTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "�Z�p����", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "��", "����"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    visitCycleTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    visitCycleTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    visitCycleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    visitCycleTable.getTableHeader().setReorderingAllowed(false);
    visitCycleTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SwingUtil.setJTableHeaderRenderer(visitCycleTable, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(visitCycleTable);
    visitCyclelScrollPane1.setViewportView(visitCycleTable);

    jPanel6.add(visitCyclelScrollPane1);
    visitCyclelScrollPane1.setBounds(0, 40, 420, 460);

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel3Layout.createSequentialGroup()
                    .add(salesInfoTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 387, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 424, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 791, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel3Layout.createSequentialGroup()
            .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(3, 3, 3)
            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 509, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(salesInfoTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 517, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(77, 77, 77))
    );

    jTabbedPane1.addTab("���X���", jPanel3);

    memoRegistrationDateComboBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    memoRegistrationDateComboBox.setToolTipText("");

    memoEvaluationLabel.setText("�]��");

    memoShopLabel.setText("�X��");

    evaluationButtonGroup.add(memoEvaluationGoodRadioButton);
    memoEvaluationGoodRadioButton.setText("�ǂ�");

    memoRegistrationDateLabel.setText("�o�^��");
    memoRegistrationDateLabel.setAutoscrolls(true);

    memoRegistButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
    memoRegistButton.setBorderPainted(false);
    memoRegistButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    memoRegistButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
    memoRegistButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            memoRegistButtonActionPerformed(evt);
        }
    });

    evaluationButtonGroup.add(memoEvaluationBadRadioButton);
    memoEvaluationBadRadioButton.setText("����");

    memoStaffLabel.setText("�S����");

    memoTextArea.setColumns(20);
    memoTextArea.setRows(5);
    jScrollPane1.setViewportView(memoTextArea);

    evaluationButtonGroup.add(memoEvaluationBatonRadioButton);
    memoEvaluationBatonRadioButton.setText("�o�g��");

    org.jdesktop.layout.GroupLayout memoPanelLayout = new org.jdesktop.layout.GroupLayout(memoPanel);
    memoPanel.setLayout(memoPanelLayout);
    memoPanelLayout.setHorizontalGroup(
        memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(memoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .add(memoPanelLayout.createSequentialGroup()
                    .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(memoShopLabel)
                        .add(memoStaffLabel)
                        .add(memoRegistrationDateLabel))
                    .add(26, 26, 26)
                    .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(memoRegistrationDateComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(memoStaffComboBox, 0, 103, Short.MAX_VALUE)
                        .add(memoShopComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(27, 27, 27)
                    .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(memoRegistButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(memoEvaluationLabel)
                        .add(memoPanelLayout.createSequentialGroup()
                            .add(4, 4, 4)
                            .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(memoEvaluationBatonRadioButton)
                                .add(memoPanelLayout.createSequentialGroup()
                                    .add(memoEvaluationGoodRadioButton)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                    .add(memoEvaluationBadRadioButton)))))))
            .addContainerGap())
    );
    memoPanelLayout.setVerticalGroup(
        memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(memoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(memoRegistButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(memoPanelLayout.createSequentialGroup()
                    .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                        .add(memoRegistrationDateLabel)
                        .add(memoRegistrationDateComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(memoShopLabel)
                        .add(memoShopComboBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(memoEvaluationLabel))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(memoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(memoStaffLabel)
                        .add(memoStaffComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(memoEvaluationGoodRadioButton)
                        .add(memoEvaluationBadRadioButton))))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(memoEvaluationBatonRadioButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 495, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(34, Short.MAX_VALUE))
    );

    jLabel39.setText("�ڋqNo.");
    jLabel39.setName(""); // NOI18N

    memoCustomerNoLabel.setText("101-1");
    memoCustomerNoLabel.setToolTipText("");
    memoCustomerNoLabel.setName(""); // NOI18N

    jLabel40.setText("�ڋq��");

    memoCustomerNameLabel.setText("�R�c�@���Y");

    org.jdesktop.layout.GroupLayout memoCustomerInfoPanelLayout = new org.jdesktop.layout.GroupLayout(memoCustomerInfoPanel);
    memoCustomerInfoPanel.setLayout(memoCustomerInfoPanelLayout);
    memoCustomerInfoPanelLayout.setHorizontalGroup(
        memoCustomerInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(memoCustomerInfoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(memoCustomerInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel40)
                .add(jLabel39))
            .add(26, 26, 26)
            .add(memoCustomerInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(memoCustomerNoLabel)
                .add(memoCustomerNameLabel))
            .addContainerGap(280, Short.MAX_VALUE))
    );
    memoCustomerInfoPanelLayout.setVerticalGroup(
        memoCustomerInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(memoCustomerInfoPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(memoCustomerInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel39)
                .add(memoCustomerNoLabel))
            .add(11, 11, 11)
            .add(memoCustomerInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel40)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, memoCustomerNameLabel))
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    searchResultScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    searchResultScrollPane.setFocusable(false);

    memoTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "�o�^��", "�X��", "�S����", "�]��", "�폜"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    memoTable.setToolTipText("");
    memoTable.setColumnSelectionAllowed(false);
    memoTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    memoTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    memoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    memoTable.getTableHeader().setReorderingAllowed(false);
    SwingUtil.setJTableHeaderRenderer(memoTable, SystemInfo.getTableHeaderRenderer());
    memoTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    this.initTableColumnWidth();
    memoTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            memoTableMousePressed(evt);
        }
    });
    memoTable.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            memoTableKeyReleased(evt);
        }
    });
    searchResultScrollPane.setViewportView(memoTable);

    feedListButton.setIcon(SystemInfo.getImageIcon("/button/common/rank_list_off.jpg"));
    feedListButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    feedListButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/rank_list_on.jpg"));
    feedListButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            feedListButtonActionPerformed(evt);
        }
    });

    jLabel41.setText("���t�B�[�h��");

    org.jdesktop.layout.GroupLayout memoListPanelLayout = new org.jdesktop.layout.GroupLayout(memoListPanel);
    memoListPanel.setLayout(memoListPanelLayout);
    memoListPanelLayout.setHorizontalGroup(
        memoListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(memoListPanelLayout.createSequentialGroup()
            .addContainerGap()
            .add(memoListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .add(memoListPanelLayout.createSequentialGroup()
                    .add(jLabel41)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(feedListButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
    );
    memoListPanelLayout.setVerticalGroup(
        memoListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(memoListPanelLayout.createSequentialGroup()
            .add(memoListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(feedListButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel41))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 496, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    org.jdesktop.layout.GroupLayout customerMemoPanelLayout = new org.jdesktop.layout.GroupLayout(customerMemoPanel);
    customerMemoPanel.setLayout(customerMemoPanelLayout);
    customerMemoPanelLayout.setHorizontalGroup(
        customerMemoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerMemoPanelLayout.createSequentialGroup()
            .add(customerMemoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(memoCustomerInfoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(memoListPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(memoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    customerMemoPanelLayout.setVerticalGroup(
        customerMemoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(customerMemoPanelLayout.createSequentialGroup()
            .add(customerMemoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                .add(customerMemoPanelLayout.createSequentialGroup()
                    .add(memoCustomerInfoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(23, 23, 23)
                    .add(memoListPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(memoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(0, 27, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("�t�B�[�h", customerMemoPanel);

    jLabel5.setText("���R�[�X����������");

    jLabel27.setText("���R�[�X�_�񗚗���");

    jLabel28.setText("����őI�������R�[�X�̏���������\�����܂��B");

    introducerPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    introducerPanel9.setOpaque(false);

    introduceScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introduceScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    contractCourseTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null,null,null,null,null,null,null,null},
            {null, null, null, null, null,null,null,null,null,null,null,null},
            {null, null, null, null, null,null,null,null,null,null,null,null},
            {null, null, null, null, null,null,null,null,null,null,null,null}
        },
        new String [] {
            "���t", "����", "�_����z", "������","�𖱎c","�X�e�[�^�X","�S����","�L������","�ۏ؊���","����","���","�ύX"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.Object.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false,false,false,false,false,true,true,true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    contractCourseTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    contractCourseTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
    contractCourseTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    contractCourseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    contractCourseTable.getTableHeader().setReorderingAllowed(false);
    this.initContractCourseColumn();
    contractCourseTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SwingUtil.setJTableHeaderRenderer(contractCourseTable, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(contractCourseTable);
    contractCourseTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            contractCourseTableMouseReleased(evt);
        }
    });
    contractCourseTable.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            contractCourseTableKeyReleased(evt);
        }
    });
    introduceScrollPane2.setViewportView(contractCourseTable);
    contractCourseTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    org.jdesktop.layout.GroupLayout introducerPanel9Layout = new org.jdesktop.layout.GroupLayout(introducerPanel9);
    introducerPanel9.setLayout(introducerPanel9Layout);
    introducerPanel9Layout.setHorizontalGroup(
        introducerPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introduceScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
    );
    introducerPanel9Layout.setVerticalGroup(
        introducerPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introduceScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
    );

    introducerPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    introducerPanel7.setOpaque(false);

    introduceScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introduceScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    consumptionCourseTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null}
        },
        new String [] {
            "���t", "����", "�������z", "������", "�S����", "���p��"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    consumptionCourseTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    consumptionCourseTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
    consumptionCourseTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    consumptionCourseTable.getTableHeader().setReorderingAllowed(false);
    consumptionCourseTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    this.initConsumptionCourseColumn();
    SwingUtil.setJTableHeaderRenderer(consumptionCourseTable, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(consumptionCourseTable);
    introduceScrollPane1.setViewportView(consumptionCourseTable);
    consumptionCourseTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    if (consumptionCourseTable.getColumnModel().getColumnCount() > 0) {
        consumptionCourseTable.getColumnModel().getColumn(5).setHeaderValue("���p��");
    }

    introduceScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introduceScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    jointlyUserTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
        },
        new String [] {
            "�ڋqNO", "�ڋq��", "�ŏI���X��", "�ڍ�", "�폜"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, true, true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    initjointlyUserColumn();
    jointlyUserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jointlyUserTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
    jointlyUserTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    SwingUtil.setJTableHeaderRenderer(jointlyUserTable, SystemInfo.getTableHeaderRenderer());
    introduceScrollPane3.setViewportView(jointlyUserTable);
    jointlyUserTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    jointlyUserAddButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
    jointlyUserAddButton.setBorderPainted(false);
    jointlyUserAddButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
    jointlyUserAddButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jointlyUserAddButtonActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout introducerPanel7Layout = new org.jdesktop.layout.GroupLayout(introducerPanel7);
    introducerPanel7.setLayout(introducerPanel7Layout);
    introducerPanel7Layout.setHorizontalGroup(
        introducerPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanel7Layout.createSequentialGroup()
            .add(introduceScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 479, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(introducerPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(introduceScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 332, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, introducerPanel7Layout.createSequentialGroup()
                    .add(jointlyUserAddButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(23, 23, 23)))
            .add(21, 21, 21))
    );
    introducerPanel7Layout.setVerticalGroup(
        introducerPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
        .add(introducerPanel7Layout.createSequentialGroup()
            .add(0, 0, Short.MAX_VALUE)
            .add(introduceScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 270, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(introducerPanel7Layout.createSequentialGroup()
            .add(jointlyUserAddButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(introduceScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 244, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );

    lblMainStaff.setText("���C���S����");

    chargeStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            chargeStaffNoFocusLost(evt);
        }
    });

    chargeStaff.setMaximumRowCount(20);
    chargeStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    chargeStaff.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            chargeStaffActionPerformed(evt);
        }
    });

    courseNoLabel.setText("�ڋqNo.");

    cusNameLabel3.setText("����");

    courseName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    courseName.setPreferredSize(new java.awt.Dimension(39, 20));

    coursetNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    coursetNo.setPreferredSize(new java.awt.Dimension(39, 20));

    jLabel31.setText("���������p�ҁ�");

    lblMainStaff1.setText("�_����z�y�я������z�͐ō����i�ƂȂ�܂��B ");

    lblMainStaff2.setText("�_��ύX�̎���͕ύX��̌_��̏��������Ă���ꍇ�͍s���܂���");

    org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel8Layout.createSequentialGroup()
            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(org.jdesktop.layout.GroupLayout.LEADING, introducerPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel8Layout.createSequentialGroup()
                            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(courseNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(cusNameLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(courseName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(coursetNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblMainStaff1))
                        .add(jPanel8Layout.createSequentialGroup()
                            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jPanel8Layout.createSequentialGroup()
                                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(10, 10, 10)
                                    .add(jLabel28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 257, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(112, 112, 112)
                                    .add(jLabel31))
                                .add(jLabel27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jPanel8Layout.createSequentialGroup()
                                    .add(118, 118, 118)
                                    .add(lblMainStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(6, 6, 6)
                                    .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(5, 5, 5)
                                    .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(0, 0, Short.MAX_VALUE))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel8Layout.createSequentialGroup()
                            .add(0, 0, Short.MAX_VALUE)
                            .add(lblMainStaff2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 338, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(org.jdesktop.layout.GroupLayout.LEADING, introducerPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 819, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel8Layout.createSequentialGroup()
            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(coursetNo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(courseNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(5, 5, 5)
            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(cusNameLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(courseName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(lblMainStaff1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel8Layout.createSequentialGroup()
                    .add(1, 1, 1)
                    .add(jLabel27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(lblMainStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMainStaff2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(3, 3, 3)
            .add(introducerPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel5)
                .add(jLabel28)
                .add(jLabel31))
            .add(0, 0, 0)
            .add(introducerPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(65, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("�_�񗚗�", jPanel8);

    jLabel9.setText("����ē��e��");

    jLabel36.setText("����ď��ꗗ��");

    introducerPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    introducerPanel10.setOpaque(false);

    introduceScrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introduceScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    tbProposal.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "�쐬��", "�쐬��", "��ď���", "�R�[�X���z","���i���z","���v���z","�L������","�_��X��","�`�[NO","���","�ҏW","����","�폜"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.Object.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false,false,false,false,false,true,true,true,true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tbProposal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    tbProposal.setSelectionBackground(new java.awt.Color(220, 220, 220));
    tbProposal.setSelectionForeground(new java.awt.Color(0, 0, 0));
    tbProposal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tbProposal.getTableHeader().setReorderingAllowed(false);
    this.initProposalColumn();
    tbProposal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SwingUtil.setJTableHeaderRenderer(tbProposal, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(tbProposal);
    tbProposal.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            tbProposalMouseReleased(evt);
        }
    });
    tbProposal.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            tbProposalKeyReleased(evt);
        }
    });
    introduceScrollPane4.setViewportView(tbProposal);
    tbProposal.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    org.jdesktop.layout.GroupLayout introducerPanel10Layout = new org.jdesktop.layout.GroupLayout(introducerPanel10);
    introducerPanel10.setLayout(introducerPanel10Layout);
    introducerPanel10Layout.setHorizontalGroup(
        introducerPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introduceScrollPane4)
    );
    introducerPanel10Layout.setVerticalGroup(
        introducerPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introduceScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
    );

    introducerPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    introducerPanel11.setOpaque(false);

    introduceScrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introduceScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    tbProductDescription.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "����", "���i��", "����", "���z"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tbProductDescription.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    tbProductDescription.setSelectionBackground(new java.awt.Color(220, 220, 220));
    tbProductDescription.setSelectionForeground(new java.awt.Color(0, 0, 0));
    tbProductDescription.getTableHeader().setReorderingAllowed(false);
    tbProductDescription.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    this.initProposalProductColumn();
    SwingUtil.setJTableHeaderRenderer(tbProductDescription, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(tbProductDescription);
    introduceScrollPane6.setViewportView(tbProductDescription);
    tbProductDescription.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    jLabel37.setText("���R�[�X���e��");

    jLabel38.setText("�����i���e��");

    introduceScrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    introduceScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    tbCourseDescription.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "����", "�R�[�X��", "�_���", "�_����z"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tbCourseDescription.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    tbCourseDescription.setSelectionBackground(new java.awt.Color(220, 220, 220));
    tbCourseDescription.setSelectionForeground(new java.awt.Color(0, 0, 0));
    tbCourseDescription.getTableHeader().setReorderingAllowed(false);
    tbCourseDescription.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    this.initProposalCourseColumn();
    SwingUtil.setJTableHeaderRenderer(tbCourseDescription, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(tbCourseDescription);
    introduceScrollPane7.setViewportView(tbCourseDescription);
    tbCourseDescription.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    org.jdesktop.layout.GroupLayout introducerPanel11Layout = new org.jdesktop.layout.GroupLayout(introducerPanel11);
    introducerPanel11.setLayout(introducerPanel11Layout);
    introducerPanel11Layout.setHorizontalGroup(
        introducerPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanel11Layout.createSequentialGroup()
            .add(18, 18, 18)
            .add(introducerPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(introducerPanel11Layout.createSequentialGroup()
                    .add(jLabel37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(299, 299, 299))
                .add(introducerPanel11Layout.createSequentialGroup()
                    .add(introduceScrollPane7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(18, 18, 18)))
            .add(introducerPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(introduceScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 380, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(18, 18, 18))
    );
    introducerPanel11Layout.setVerticalGroup(
        introducerPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .add(introducerPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel37)
                .add(jLabel38))
            .add(0, 0, 0)
            .add(introducerPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(introduceScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(introduceScrollPane7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    lbCustomerNo.setText("�ڋqNo.");

    lbCustomerName.setText("����");

    txtCustomerName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    txtCustomerName.setPreferredSize(new java.awt.Dimension(39, 20));

    txtCustomerNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    txtCustomerNo.setPreferredSize(new java.awt.Dimension(39, 20));

    btCallProposal.setIcon(SystemInfo.getImageIcon("/button/common/new_registration_off.jpg"));
    btCallProposal.setBorderPainted(false);
    btCallProposal.setPressedIcon(SystemInfo.getImageIcon("/button/common/new_registration_on.jpg"));
    btCallProposal.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btCallProposalActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(org.jdesktop.layout.GroupLayout.LEADING, introducerPanel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel9Layout.createSequentialGroup()
                            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(0, 0, Short.MAX_VALUE))
                        .add(jPanel9Layout.createSequentialGroup()
                            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(lbCustomerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(lbCustomerName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(txtCustomerName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(txtCustomerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btCallProposal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(29, 29, 29))))
                .add(org.jdesktop.layout.GroupLayout.LEADING, introducerPanel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel9Layout.createSequentialGroup()
            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(txtCustomerNo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(lbCustomerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(5, 5, 5)
            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(lbCustomerName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtCustomerName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btCallProposal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jLabel36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(3, 3, 3)
            .add(introducerPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jLabel9)
            .add(0, 0, 0)
            .add(introducerPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(60, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("��ď�", jPanel9);

    introducerPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    introducerPanel6.setOpaque(false);

    dmmailScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    dmmailTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null}
        },
        new String [] {
            "����", "���t", "�^�C�g��"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.Object.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    dmmailTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
    dmmailTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
    dmmailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    dmmailTable.getTableHeader().setReorderingAllowed(false);
    dmmailTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
    SwingUtil.setJTableHeaderRenderer(dmmailTable, SystemInfo.getTableHeaderRenderer());
    SelectTableCellRenderer.setSelectTableCellRenderer(dmmailTable);
    dmmailTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            dmmailTableMouseReleased(evt);
        }
    });
    dmmailTable.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            dmmailTableKeyReleased(evt);
        }
    });
    dmmailScrollPane.setViewportView(dmmailTable);

    org.jdesktop.layout.GroupLayout introducerPanel6Layout = new org.jdesktop.layout.GroupLayout(introducerPanel6);
    introducerPanel6.setLayout(introducerPanel6Layout);
    introducerPanel6Layout.setHorizontalGroup(
        introducerPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(dmmailScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
    );
    introducerPanel6Layout.setVerticalGroup(
        introducerPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(dmmailScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
    );

    cusNameLabel2.setText("����");

    introducerPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    introducerPanel8.setOpaque(false);

    mailTitleLabel.setText("�^�C�g��");

    mailTitleScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    mailTitleScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    mailTitleScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    mailTitle.setColumns(20);
    mailTitle.setEditable(false);
    mailTitle.setRows(5);
    mailTitleScrollPane.setViewportView(mailTitle);

    mailBodyScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

    mailBody.setEditable(false);
    mailBody.setColumns(20);
    mailBody.setRows(5);
    mailBodyScrollPane.setViewportView(mailBody);

    mailBodyLabel.setText("�{��");

    org.jdesktop.layout.GroupLayout introducerPanel8Layout = new org.jdesktop.layout.GroupLayout(introducerPanel8);
    introducerPanel8.setLayout(introducerPanel8Layout);
    introducerPanel8Layout.setHorizontalGroup(
        introducerPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanel8Layout.createSequentialGroup()
            .add(6, 6, 6)
            .add(introducerPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(mailBodyScrollPane)
                .add(mailTitleLabel)
                .add(mailBodyLabel)
                .add(mailTitleScrollPane))
            .add(6, 6, 6))
    );
    introducerPanel8Layout.setVerticalGroup(
        introducerPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(introducerPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .add(mailTitleLabel)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(mailTitleScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(mailBodyLabel)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(mailBodyScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
            .addContainerGap())
    );

    cusIDLabel2.setText("�ڋqNo.");

    mailButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
    mailButton.setBorderPainted(false);
    mailButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
    mailButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mailButtonActionPerformed(evt);
        }
    });

    DMNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    DMNo.setPreferredSize(new java.awt.Dimension(39, 20));

    DMName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    DMName.setPreferredSize(new java.awt.Dimension(39, 20));

    org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
            .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(jPanel7Layout.createSequentialGroup()
                    .add(9, 9, 9)
                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(cusIDLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(cusNameLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(DMNo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .add(DMName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(mailButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(introducerPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(introducerPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(299, 299, 299))
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel7Layout.createSequentialGroup()
            .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel7Layout.createSequentialGroup()
                    .add(1, 1, 1)
                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(cusIDLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(DMNo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 25, Short.MAX_VALUE)
                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(cusNameLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(DMName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(1, 1, 1))
                .add(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(mailButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .add(introducerPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(20, 20, 20)
            .add(introducerPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(30, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("DM�E���[������", jPanel7);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(controlPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createSequentialGroup()
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 830, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(invisiblePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(controlPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(83, 83, 83)
                    .add(invisiblePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 691, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
    );
    }// </editor-fold>//GEN-END:initComponents

	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearButtonActionPerformed
	{//GEN-HEADEREND:event_clearButtonActionPerformed
            jTabbedPane1.setSelectedIndex(0);
            this.clear();
            this.showData();
            // �����̔Ԃ��L���ȏꍇ
            if (SystemInfo.getCurrentShop().getAutoNumber() == 1) {
                try {

                    ConnectionWrapper con = SystemInfo.getConnection();
                    String prefix = SystemInfo.getCurrentShop().getPrefixString();
                    int length = SystemInfo.getCurrentShop().getSeqLength().intValue();

                    customer.setCustomerNo(MstCustomer.getNewCustomerNo(con, prefix, length));
                    customerNo.setText(customer.getCustomerNo());
                    customerName1.requestFocusInWindow();

                } catch (Exception e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            } else {
                customerNo.requestFocusInWindow();
            }
	}//GEN-LAST:event_clearButtonActionPerformed

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
	{//GEN-HEADEREND:event_deleteButtonActionPerformed
            if (!isReadOnly && !isCloseButtonEnable) {
                this.delete();
            } else {
                getRootPane().getParent().setVisible(false);
            }
	}//GEN-LAST:event_deleteButtonActionPerformed

	private void renewButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renewButtonActionPerformed
	{//GEN-HEADEREND:event_renewButtonActionPerformed
            this.registData(false);
	}//GEN-LAST:event_renewButtonActionPerformed

	private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
	{//GEN-HEADEREND:event_addButtonActionPerformed
            this.registData(true);
            //IVS_LVTu start add 2015/11/30 New request #44111
            if (this.customer != null && this.customer.getCustomerID() != null && SystemInfo.getCurrentShop().getShopID() != 0) {
                btCallProposal.setEnabled(true);
            }else {
                btCallProposal.setEnabled(false);
            }
            //IVS_LVTu end add 2015/11/30 New request #44111
	}//GEN-LAST:event_addButtonActionPerformed

	private void searchCusButtonActionPerformed(java.awt.event.ActionEvent evt)                                                
	{                                                    
            SystemInfo.getLogger().log(Level.INFO, "�ڋq����");
            SearchCustomerDialog sc = new SearchCustomerDialog(parentFrame, true);
            sc.setShopID(this.getSelectedShopID());
            sc.setVisible(true);

            if (sc.getSelectedCustomer() != null
                    && !sc.getSelectedCustomer().getCustomerID().equals("")) {
                customer.setCustomerID(sc.getSelectedCustomer().getCustomerID());
                this.loadCustomer();
                tabZero = false;
                tabOne = false;
                tabTwo = false;
                tabThree = false;
                
// 2017/01/08 �ڋq���� ADD START
                isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
                
                this.showData();

                customerName1.requestFocusInWindow();
            }

            sc.dispose();
            sc = null;
            //IVS_LVTu start add 2015/11/30 New request #44111
            if (this.customer != null && this.customer.getCustomerID() != null && SystemInfo.getCurrentShop().getShopID() != 0) {
                btCallProposal.setEnabled(true);
            }else {
                btCallProposal.setEnabled(false);
            }
            //IVS_LVTu end add 2015/11/30 New request #44111
	}                                               
	
	private void searchJointlyUserButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_searchCusButtonActionPerformed
	{//GEN-HEADEREND:event_searchCusButtonActionPerformed
        int index = contractCourseTable.getSelectedRow() / 2;
        Object selectedCell = dataContracts.get(index);
        if (selectedCell instanceof JTextPane) {
            return;
        }
        DataContract dc = (DataContract) selectedCell;
        SystemInfo.getLogger().log(Level.INFO, "�ڋq����");
        SearchCustomerDialog sc = new SearchCustomerDialog(parentFrame, true,customer.getCustomerID(),dc.getShop().getShopID(),dc.getContractNo(),dc.getContractDetailNo());
        sc.setVisible(true);
        if (sc.getSelectedCustomer() != null
                && !sc.getSelectedCustomer().getCustomerID().equals("")) {
                try {
                    DataContractShare.registDataContractShare(SystemInfo.getConnection(), dc.getShop().getShopID(),sc.getSelectedCustomer().getCustomerID(),dc.getContractNo(),dc.getContractDetailNo());
                    this.showJointlyUserHistory(dc);
                } catch (Exception e) {}
        }
        sc.dispose();
        sc = null;
	}//GEN-LAST:event_searchCusButtonActionPerformed
	
	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nextButtonActionPerformed
	{//GEN-HEADEREND:event_nextButtonActionPerformed
            this.moveCustomer(true);
	}//GEN-LAST:event_nextButtonActionPerformed

	private void prevButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_prevButtonActionPerformed
	{//GEN-HEADEREND:event_prevButtonActionPerformed
            this.moveCustomer(false);
	}//GEN-LAST:event_prevButtonActionPerformed

    private void showButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showButtonActionPerformed

        if (showButton.getName() != null) {

            // ���̓`�F�b�N
            if (customerNo.getText().length() > 0 && !customerNo.getText().equals("0")) {

                int ret = MessageDialog.showYesNoDialog(
                        this,
                        "�ڋqNo.�����ɓ��͂���Ă��܂��B\n�㏑�����Ă���낵���ł����H",
                        this.getTitle(),
                        JOptionPane.WARNING_MESSAGE,
                        JOptionPane.NO_OPTION);

                if (ret == JOptionPane.NO_OPTION) {
                    customerNo.requestFocusInWindow();
                    return;
                }
            }

            // �����̔�
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                String prefix = SystemInfo.getCurrentShop().getPrefixString();
                int length = SystemInfo.getCurrentShop().getSeqLength().intValue();

                customerNo.setText(MstCustomer.getNewCustomerNo(con, prefix, length));
                customerNo.requestFocusInWindow();

            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

        } else {
            this.setCustomerByNo();
            this.loadCustomer();
            tabZero = false;
            tabOne = false;
            tabTwo = false;
            tabThree = false;
            
// 2017/01/08 �ڋq���� ADD START
            isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
            
            this.showData();
        }
        //IVS_LVTu start add 2015/11/30 New request #44111
        if (this.customer != null && this.customer.getCustomerID() != null && SystemInfo.getCurrentShop().getShopID() != 0) {
            btCallProposal.setEnabled(true);
        }else {
            btCallProposal.setEnabled(false);
        }
        //IVS_LVTu end add 2015/11/30 New request #44111
    }//GEN-LAST:event_showButtonActionPerformed

    private void customerKana1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerKana1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerKana1ActionPerformed

    private void customerName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerName1ActionPerformed

    private void postalCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_postalCodeFocusLost
        String temp = postalCode.getText().replaceAll("[-_]", "");

        //�X�֔ԍ������^����Ă��Ȃ��ꍇ
        if (temp.equals("") || temp.length() != 7) {
            address1.setText("");
            address2.setText("");
            address3.setText("");
        } //�X�֔ԍ������^����Ă���ꍇ
        else {
            AddressUtil au = new AddressUtil();

            au.setPostalCode(temp);

            try {
                ConnectionWrapper con = SystemInfo.getBaseConnection();

                //�X�֔ԍ�����Z�����擾
                if (au.getDataByPostalCode(con)) {
                    address1.setText(au.getPrefectureName());
                    address2.setText(au.getCityName());
                    address3.setText(au.getTownName().replaceAll("�i.+", ""));
                } else {
                    address1.setText("");
                    address2.setText("");
                    address3.setText("");
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }//GEN-LAST:event_postalCodeFocusLost

    private void searchAddressButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchAddressButtonActionPerformed
        SystemInfo.getLogger().log(Level.INFO, "�Z������");
        SearchAddressDialog sa = new SearchAddressDialog(parentFrame, true);
        sa.setVisible(true);

        if (!sa.getSelectedPrefecture().equals("")) {
            postalCode.setText(sa.getSelectedPostalCode());
            address1.setText(sa.getSelectedPrefecture());
            address2.setText(sa.getSelectedCity());
            address3.setText(sa.getSelectedTown().replaceAll("�i.+", ""));
        }

        sa.dispose();
        sa = null;
    }//GEN-LAST:event_searchAddressButtonActionPerformed

    private void yearUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearUnitActionPerformed
        this.setAge();
    }//GEN-LAST:event_yearUnitActionPerformed

    private void birthYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_birthYearFocusLost
        this.setAge();
    }//GEN-LAST:event_birthYearFocusLost

    private void birthMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_birthMonthActionPerformed
        this.setAge();
    }//GEN-LAST:event_birthMonthActionPerformed

    private void birthDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_birthDayActionPerformed
        this.setAge();
    }//GEN-LAST:event_birthDayActionPerformed

    private void maleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maleActionPerformed

    private void AlertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AlertActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AlertActionPerformed

    private void jobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jobActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jobActionPerformed

    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
        //this.clear();
    }//GEN-LAST:event_shopActionPerformed

    private void question_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question_2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_question_2ActionPerformed

    private void question_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question_1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_question_1ActionPerformed

    private void changeSosiaGearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSosiaGearButtonActionPerformed
        changeSosiaGear();
    }//GEN-LAST:event_changeSosiaGearButtonActionPerformed

    private void addIntroducerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIntroducerButtonActionPerformed
        MstCustomer mc = getMstCustomer();

        if (mc != null && !mc.getCustomerID().equals("")) {
            addIntoroducer(mc);
        }
    }//GEN-LAST:event_addIntroducerButtonActionPerformed

    private void familyPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_familyPropertyChange
        if (evt.getPropertyName().equals("tableCellEditor")) {
            isModifyFamily = true;
        }
    }//GEN-LAST:event_familyPropertyChange

    private void addFamilyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFamilyButtonActionPerformed
        MstCustomer mc = getMstCustomer();
        
        //Luc start add 20141216 New request #34101 [gb]�ڋq���̉Ƒ��\���̒ǉ��ɂ����Ė{�l�̒ǉ�����

        String message = "�{�l���I������Ă��܂��B\n";
        message+= "�{�l�͉Ƒ��o�^�ł��܂���B";
        if(mc.getCustomerID().equals(customer.getCustomerID())) {
             MessageDialog.showMessageDialog(this,
               message,
               this.getTitle(),
               JOptionPane.ERROR_MESSAGE);
            return ;
        }
        //Luc start add 20141216 New request #34101 [gb]�ڋq���̉Ƒ��\���̒ǉ��ɂ����Ė{�l�̒ǉ�����
        if (mc != null && !mc.getCustomerID().equals("")) {

            boolean isAddFamily = false;

            String sql = "select * from mst_family where " + SQLUtil.convertForSQL(mc.getCustomerID()) + " in (customer_id, family_id)";
            try {
                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql);
                if (rs.next()) {
                    isAddFamily = true;
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            
            
            if (!isAddFamily) {
                for (int i = 0; i < family.getRowCount(); i++) {
                    if (((MstCustomer) family.getValueAt(i, 1)).getCustomerID().equals(mc.getCustomerID())) {
                        isAddFamily = true;
                        break;
                    }
                }
            }

            if (isAddFamily) {
                String msg = "[ " + mc.getCustomerNo() + " ]  " + mc.getFullCustomerName() + "\n" + "���̌ڋq�͊��ɉƑ��o�^����Ă��܂��B";
                MessageDialog.showMessageDialog(
                        this,
                        msg,
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


            addFamily(mc);
            isModifyFamily = true;
        }
    }//GEN-LAST:event_addFamilyButtonActionPerformed

    private void accountMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountMouseReleased
        this.showSelectedAccountDetail();
    }//GEN-LAST:event_accountMouseReleased

    private void accountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_accountKeyReleased
        this.showSelectedAccountDetail();
    }//GEN-LAST:event_accountKeyReleased

    private void cancelsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelsMouseReleased
   }//GEN-LAST:event_cancelsMouseReleased

    private void cancelsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cancelsKeyReleased
   }//GEN-LAST:event_cancelsKeyReleased

    private void visitedMemoTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visitedMemoTableMouseReleased
        this.showVisitedMemo();
    }//GEN-LAST:event_visitedMemoTableMouseReleased

    private void visitedMemoTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_visitedMemoTableKeyReleased
        this.showVisitedMemo();
    }//GEN-LAST:event_visitedMemoTableKeyReleased

    private void visitedMemoUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitedMemoUpdateButtonActionPerformed
        this.updateVisitedMemo();
    }//GEN-LAST:event_visitedMemoUpdateButtonActionPerformed

    private void pointTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pointTableMouseReleased
        this.showPointData();
    }//GEN-LAST:event_pointTableMouseReleased

    private void pointTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pointTableKeyReleased
        this.showPointData();
    }//GEN-LAST:event_pointTableKeyReleased

    private void pointUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointUpdateButtonActionPerformed
        this.updatePointData();
    }//GEN-LAST:event_pointUpdateButtonActionPerformed

    private void suppliedPointFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_suppliedPointFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_suppliedPointFocusLost

    private void usePointFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usePointFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_usePointFocusLost

    private void pointAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointAddButtonActionPerformed
        this.insertPointData();
    }//GEN-LAST:event_pointAddButtonActionPerformed

    private void prepaidTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prepaidTableMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_prepaidTableMouseReleased

    private void prepaidTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prepaidTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_prepaidTableKeyReleased

    private void nextReserveChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextReserveChangeButtonActionPerformed

        // ���X�\��̏ꍇ
        if (!this.nextReserveShopID.equals(SystemInfo.getCurrentShop().getShopID())) {

            MessageDialog.showMessageDialog(
                    this,
                    "���X�̗\��ƂȂ�܂��̂ŕύX�͂ł��܂���B",
                    this.getTitle(),
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        nextReserveChangeButton.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            ReservationTimeTablePanel rtt = new ReservationTimeTablePanel();

            rtt.setCustomer(customer);

            try {
                //IVS_LVTu start edit 2015/10/20 New request #43508
                //rtt.setDate(DateFormat.getDateInstance().parse(nextReserveDate.getText()));
                NextReserve selectedRow = (NextReserve)cmbNextReserve.getSelectedItem();
                rtt.setDate(DateFormat.getDateInstance().parse(selectedRow.getNextReserveDate().trim()));
                //IVS_LVTu end edit 2015/10/20 New request #43508
            } catch (Exception e) {
            }

            rtt.load();

            rtt.setNextReservation(true);
            rtt.setAutoOpenDialog(true);
            rtt.setNextReserveShopID(this.nextReserveShopID);
            rtt.setNextReserveNo(this.nextReserveNo);

            /*
             * �_�C�A���O�ŊJ��
             */
            SwingUtil.openAnchorDialog(null, true, rtt, "�\��\", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);

            rtt = null;

            this.showNextReserveDate();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_nextReserveChangeButtonActionPerformed

    private void firstVisitYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_firstVisitYearFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_firstVisitYearFocusLost

    private void firstVisitMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstVisitMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstVisitMonthActionPerformed

    private void firstVisitDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstVisitDayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstVisitDayActionPerformed

    private void showVisitCycleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showVisitCycleButtonActionPerformed

        showVisitCycleButton.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.showVisitCycle();
            //jTabbedPane1.setSelectedIndex(2);
            //jTabbedPane1.setSelectedIndex(1);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_showVisitCycleButtonActionPerformed

    private void contractCourseTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contractCourseTableMouseReleased

        if (contractCourseTable.getSelectedRow() == -1) {
            //IVS NNTUAN START EDIT 20131104
            //row = 0;
            row = -1;
            return;
            //IVS NNTUAN END EDIT 20131104
        } else {
            row = contractCourseTable.getSelectedRow();
        }
        this.showConsumptionCourseHistory();
    }//GEN-LAST:event_contractCourseTableMouseReleased

    private void contractCourseTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contractCourseTableKeyReleased
        this.showConsumptionCourseHistory();
    }//GEN-LAST:event_contractCourseTableKeyReleased

    private void dmmailTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dmmailTableMouseReleased
        this.showDmMailData();
    }//GEN-LAST:event_dmmailTableMouseReleased

    private void dmmailTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dmmailTableKeyReleased
        this.showDmMailData();
    }//GEN-LAST:event_dmmailTableKeyReleased

    private void mailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mailButtonActionPerformed

        ArrayList<MstCustomer> selectedArray = new ArrayList<MstCustomer>();
        selectedArray.add(customer);
        HairCommonMailPanel rcmp = new HairCommonMailPanel(SystemInfo.getCurrentTarget(), selectedArray);
        rcmp.setOpener(this);
        SwingUtil.openAnchorDialog(null, true, rcmp, "���[���쐬", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
    }//GEN-LAST:event_mailButtonActionPerformed

    private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chargeStaffNoFocusLost

        if (!chargeStaffNo.getText().equals("")) {
            this.setChargeStaff(chargeStaffNo.getText());
        } else {
            chargeStaff.setSelectedIndex(0);
        }
    }//GEN-LAST:event_chargeStaffNoFocusLost

    private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed

        MstStaff ms = (MstStaff) chargeStaff.getSelectedItem();

        if (ms != null) {
            if (ms.getStaffID() != null) {
                chargeStaffNo.setText(ms.getStaffNo());
            }

            if (chargeStaff.getSelectedIndex() == tabGenaralIndex) {
                chargeStaffNo.setText("");
            }
        }
    }//GEN-LAST:event_chargeStaffActionPerformed

    private void jointlyUserAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jointlyUserAddButtonActionPerformed
    	int index = this.courseTableSelectedIndex;
        Object selectedCell = dataContracts.get(index);
        if (selectedCell instanceof JTextPane) {
            return;
        }
        DataContract dc = (DataContract) selectedCell;
        SystemInfo.getLogger().log(Level.INFO, "�ڋq����");
        SearchCustomerDialog sc = new SearchCustomerDialog(parentFrame, true,customer.getCustomerID(),dc.getShop().getShopID(),dc.getContractNo(),dc.getContractDetailNo());
        sc.setVisible(true);
        if (sc.getSelectedCustomer() != null
                && !sc.getSelectedCustomer().getCustomerID().equals("")) {
                try {
                    DataContractShare.registDataContractShare(SystemInfo.getConnection(), dc.getShop().getShopID(),sc.getSelectedCustomer().getCustomerID(),dc.getContractNo(),dc.getContractDetailNo());
                    this.showJointlyUserHistory(dc);
                } catch (Exception e) {}
        }
        sc.dispose();
        sc = null;
    }//GEN-LAST:event_jointlyUserAddButtonActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        if(isLoaded ) {
            if(jTabbedPane1.getSelectedIndex() != -1 && customer.getCustomerNo() != null ){
            showData();
            }else{
               if(jTabbedPane1.getSelectedIndex() != -1){
                    this.setCustomerByNo();
                    this.loadCustomer();
                    tabZero = true;
                    tabOne = false;
                    tabTwo = false;
                    tabThree = false;
                    this.showData();
                } 
            }
        }
         
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void cmbNextReserveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNextReserveActionPerformed
        //IVS_LVTu start add 2015/10/20 New request #43508
        if ( cmbNextReserve.getSelectedItem() != null ) {
            nextReserveShopID = ((NextReserve)cmbNextReserve.getSelectedItem()).getShopId();
            nextReserveNo = ((NextReserve)cmbNextReserve.getSelectedItem()).getReservationNo();
        }
        //IVS_LVTu end add 2015/10/20 New request #43508
    }//GEN-LAST:event_cmbNextReserveActionPerformed

    private void tbProposalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProposalMouseReleased
        selectedProposalIndex();
    }//GEN-LAST:event_tbProposalMouseReleased

    private void tbProposalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbProposalKeyReleased
        selectedProposalIndex();
    }//GEN-LAST:event_tbProposalKeyReleased

    private void btCallProposalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCallProposalActionPerformed
        ProposalPanel pp = new ProposalPanel(customer, null, -1);
        SwingUtil.openAnchorDialog( this.parentFrame, true, pp, "��ď��쐬", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
        System.gc();
        pp.setVisible(true);
        
        showProposal();
    }//GEN-LAST:event_btCallProposalActionPerformed

// 2017/01/08 �ڋq���� ADD START
    private void memoRegistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoRegistButtonActionPerformed
        // �o�^�{�^������
        
        if(customer == null || customer.getCustomerID() == null) {
            return;
        }

        // �o�^�����̓`�F�b�N
        Integer errorMessageCode = this.checkInputRegistrationDate(this.memoRegistrationDateComboBox);
        if(errorMessageCode != null) {
            MessageDialog.showMessageDialog(
                this,
                MessageUtil.getMessage(errorMessageCode, memoRegistrationDateLabel.getText()),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // �X�ܓ��̓`�F�b�N
        errorMessageCode = this.checkInputShop(this.memoShopComboBox);
        if(errorMessageCode != null) {
            MessageDialog.showMessageDialog(
                this,
                MessageUtil.getMessage(errorMessageCode, memoShopLabel.getText()),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        } 
        
        // �S���ғ��̓`�F�b�N
        errorMessageCode = this.checkInputStaff(this.memoStaffComboBox);
        if(errorMessageCode != null) {
            MessageDialog.showMessageDialog(
                this,
                MessageUtil.getMessage(errorMessageCode, memoStaffLabel.getText()),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // �]�����̓`�F�b�N
        errorMessageCode = this.checkInputEvaluation(this.evaluationButtonGroup);
        if(errorMessageCode != null) {
            MessageDialog.showMessageDialog(
                this,
                MessageUtil.getMessage(errorMessageCode, memoEvaluationLabel.getText()),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // �������̓`�F�b�N
        errorMessageCode = this.checkInputMemo(this.memoTextArea);
        if(errorMessageCode != null) {
            MessageDialog.showMessageDialog(
                this,
                MessageUtil.getMessage(errorMessageCode, "����"),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            // �����f�[�^�o�^
            DataCustomerMemo dataCustomerMemo = new DataCustomerMemo();
            
            dataCustomerMemo.setCustomerId(customer.getCustomerID());
            dataCustomerMemo.setMemoDate(memoRegistrationDateComboBox.getDate());
            
            MstShop selectedShop = (MstShop)memoShopComboBox.getSelectedItem();
            dataCustomerMemo.setShopId(selectedShop.getShopID());
            
            MstStaff selectedStaff = (MstStaff)memoStaffComboBox.getSelectedItem();
            dataCustomerMemo.setStaffId(selectedStaff.getStaffID());

            int evaluation;
            if(memoEvaluationGoodRadioButton.isSelected()) {
                evaluation = DataCustomerMemo.EVALUATION_GOOD;
            } else if (memoEvaluationBadRadioButton.isSelected()) {
                evaluation = DataCustomerMemo.EVALUATION_BAD;
            } else {
                evaluation = DataCustomerMemo.EVALUATION_BATON;
            }
            dataCustomerMemo.setEvaluation(evaluation);
            
            dataCustomerMemo.setMemo(memoTextArea.getText());

            if(memoTable.getSelectedRow() != -1) {
                // ���׍s���I������Ă���ꍇ�́A���̍s�̃f�[�^���X�V����B
                DataCustomerMemo memoData = customerMemoList.get(memoTable.getSelectedRow());
                dataCustomerMemo.setMemoId(memoData.getMemoId());
                
                dataCustomerMemo.update(SystemInfo.getConnection());
                
                customerMemoList.set(memoTable.getSelectedRow(), 
                        dataCustomerMemo.selectMemoDataWithShopDataAndStaffDataByMemoId(SystemInfo.getConnection()));
            } else {
                // ���׍s���I������Ă��Ȃ��ꍇ�́A�f�[�^��}������B
                int memoId = dataCustomerMemo.selectNewMemoIdSeq(SystemInfo.getConnection());
                dataCustomerMemo.setMemoId(memoId);
                
                dataCustomerMemo.insertWithMemoId(SystemInfo.getConnection());
                
                customerMemoList.add(dataCustomerMemo.selectMemoDataWithShopDataAndStaffDataByMemoId(SystemInfo.getConnection()));
            }
            
            // �V���ɓo�^���ꂽ�f�[�^��g�ݍ��񂾃��X�g���\�[�g���čĕ\������B
            this.updateCustomerMemoTable();
            
            // �o�^�����s��I��
//            int targetRow = this.getRowIndex(dataCustomerMemo.getMemoId());
//            memoTable.setRowSelectionInterval(targetRow, targetRow);
            
            // �ڋq�����o�^�֘A�R���|�[�l���g������������B
            clearCustomerMemoComponents();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }//GEN-LAST:event_memoRegistButtonActionPerformed
  
    private void memoTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_memoTableKeyReleased
        this.updateCustomerMemoComponents();
    }//GEN-LAST:event_memoTableKeyReleased

    private void memoTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_memoTableMousePressed
        this.updateCustomerMemoComponents();
    }//GEN-LAST:event_memoTableMousePressed

    private void feedListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feedListButtonActionPerformed
        if(customer == null || customer.getCustomerID() == null) {
            return;
        }
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            CustomerMemoListDialog cmld = new CustomerMemoListDialog(null, true, customer);
            
            // ���C����ʂɂ��Ԃ���悤�ɕ\������
            cmld.setSize(1042, 780);
            cmld.setLocationRelativeTo(null);
            cmld.setVisible(true);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_feedListButtonActionPerformed
// 2017/01/08 �ڋq���� ADD END
    
    private void updateVisitedMemo() {

        Integer index = visitedMemoTable.getSelectedRow();

        if (0 <= index && index < customer.getAccounts().size()) {

            if (MessageDialog.showYesNoDialog(
                    this,
                    "���X�������X�V���܂��B��낵���ł����H",
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.NO_OPTION) == JOptionPane.NO_OPTION) {
                return;
            }

            VisitRecord vr = customer.getAccounts().get(index);

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" update data_sales");
            sql.append(" set");
            sql.append("     visited_memo = " + SQLUtil.convertForSQL(visitedMemo.getText()));
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(vr.getShop().getShopID()));
            sql.append("     and slip_no = " + SQLUtil.convertForSQL(vr.getSlipNo()));

            try {
                if (SystemInfo.getConnection().executeUpdate(sql.toString()) == 1) {
                    visitedMemoTable.setValueAt(visitedMemo.getText(), index, 1);
                    visitedMemoTable.changeSelection(index, 1, false, false);
                    vr.setVisitedMemo(visitedMemo.getText());

                } else {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "���X����"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean inputPointCheck() {

        // �g�p�|�C���g
        if (usePoint.getText().length() == 0) {
            MessageDialog.showMessageDialog(
                    this,
                    "�g�p�|�C���g����͂��Ă�������",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            usePoint.requestFocusInWindow();
            return false;
        }

        // ���Z�|�C���g
        if (suppliedPoint.getText().length() == 0) {
            MessageDialog.showMessageDialog(
                    this,
                    "���Z�|�C���g����͂��Ă�������",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            suppliedPoint.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void insertPointData() {

        if (!inputPointCheck()) {
            return;
        }

        StringBuilder msg = new StringBuilder(1000);
        msg.append("�ȉ��̏����|�C���g�����ɒǉ����܂��B\n");
        msg.append("��낵���ł����H\n\n");
        msg.append("�m�g�p�|�C���g�n " + usePoint.getText() + "\n");
        msg.append("�m���Z�|�C���g�n " + suppliedPoint.getText() + "\n\n");

        if (MessageDialog.showYesNoDialog(
                this,
                msg.toString(),
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.NO_OPTION) == JOptionPane.NO_OPTION) {
            return;
        }

        int shopId = 0;
        if (!SystemInfo.isGroup()) {
            shopId = SystemInfo.getCurrentShop().getShopID();
        }

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into data_point");
        sql.append(" (shop_id, point_id, customer_id, use_point, supplied_point, insert_date, update_date)");
        sql.append(" values");
        sql.append(" (");
        sql.append("      " + SQLUtil.convertForSQL(shopId));
        sql.append("     ,(select coalesce(max(point_id), 0) + 1 from data_point where shop_id = " + SQLUtil.convertForSQL(shopId) + ")");
        sql.append("     ," + SQLUtil.convertForSQL(customer.getCustomerID()));
        sql.append("     ," + SQLUtil.convertForSQL(usePoint.getText()));
        sql.append("     ," + SQLUtil.convertForSQL(suppliedPoint.getText()));
        sql.append("     ,current_timestamp");
        sql.append("     ,current_timestamp");
        sql.append(" )");

        try {
            if (SystemInfo.getConnection().executeUpdate(sql.toString()) == 1) {
                this.showPointList();

                // ���݃|�C���g�̍X�V
                this.showGenzaiPoint();

            } else {
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�|�C���g����"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updatePointData() {

        Integer index = pointTable.getSelectedRow();

        if (0 <= index && shopPoint.getText().length() > 0) {

            if (!inputPointCheck()) {
                return;
            }

            if (MessageDialog.showYesNoDialog(
                    this,
                    "�|�C���g�������X�V���܂��B��낵���ł����H",
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.NO_OPTION) == JOptionPane.NO_OPTION) {
                return;
            }


            MstShop ms = (MstShop) pointTable.getValueAt(index, 0);

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" update data_point");
            sql.append(" set");
            sql.append("      use_point = " + SQLUtil.convertForSQL(usePoint.getText()));
            sql.append("     ,supplied_point = " + SQLUtil.convertForSQL(suppliedPoint.getText()));
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(ms.getShopID()));
            // point_id�̕ۑ��p�ɃO���[�vID���g�p���Ă���
            sql.append("     and point_id = " + SQLUtil.convertForSQL(ms.getGroupID()));

            try {
                if (SystemInfo.getConnection().executeUpdate(sql.toString()) == 1) {
                    pointTable.setValueAt(Long.valueOf(usePoint.getText()), index, 2);
                    pointTable.setValueAt(Long.valueOf(suppliedPoint.getText()), index, 3);
                    pointTable.changeSelection(index, 0, false, false);

                    // ���݃|�C���g�̍X�V
                    this.showGenzaiPoint();

                } else {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�|�C���g����"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ��S���҂��Z�b�g����B
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

    private void initStaff() {
        chargeStaff.removeAllItems();
        HairInputAccount ia = new HairInputAccount();
        for (MstStaff ms : ia.getStaffs(getSelectedShop())) {
            if (ms.isDisplay()) {
                chargeStaff.addItem(ms);
            }
        }
        if (chargeStaff.getItemCount() > 0) {
            chargeStaff.setSelectedIndex(0);
        }
    }

    private boolean inputCheck() {
        if (customer == null || customer.getCustomerID() == null) {
            return false;
        }

        NumberUtils numUtil = new NumberUtils();
        if (!numUtil.isNumber(this.cmbTargetYear.getSelectedItem().toString())) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, lblBaseMonth.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            Calendar cdr = Calendar.getInstance();
            this.cmbTargetYear.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void showDmMailData() {
        mailTitle.setText("");
        mailBody.setText("");

        // ThuanNK start edit 2014/02/27
        DataDmHistoryDetail dhd = dataDmHistoryDetails.get(dmmailTable.getSelectedRow());
        // ThuanNK end edit 2014/02/27
        if (dhd.getDmType() != DmHistory.DM_MAIL) {
            return;
        }

        // ���[���^�C�g��
        mailTitle.setText(dhd.getMailTitle());

        // ���[���{��
        DataMail dm = new DataMail();
        dm.setData(dhd);
        dm.setMailBody(dhd.getMailBody());
        mailBody.setText(MailUtil.decodeKey(dm, shop.getSelectedItem()));

        // �J�[�\����擪�ɃZ�b�g
        mailTitle.setCaretPosition(0);
        mailBody.setCaretPosition(0);
    }

    private void showVisitedMemo() {
        int row = visitedMemoTable.getSelectedRow();
        visitedMemo.setText(visitedMemoTable.getValueAt(row, 1).toString());
        visitedMemo.setCaretPosition(0);
    }

    private void showPointData() {
        int row = pointTable.getSelectedRow();
        shopPoint.setText(pointTable.getValueAt(row, 0).toString());
        datePoint.setText(pointTable.getValueAt(row, 1).toString());
        usePoint.setText(pointTable.getValueAt(row, 2).toString());
        suppliedPoint.setText(pointTable.getValueAt(row, 3).toString());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox Alert;
    private javax.swing.JLabel BufferTimeLabel;
    private javax.swing.JTextField CustomerIspotIDjTextField;
    private javax.swing.JLabel DMName;
    private javax.swing.JLabel DMNo;
    private com.geobeck.swing.JFormattedTextFieldEx Reserved_buffer_time;
    private javax.swing.JRadioButton WebNGCancel;
    private javax.swing.JRadioButton WebOKCancel;
    private com.geobeck.swing.JTableEx account;
    private javax.swing.JScrollPane accountScrollPane;
    private javax.swing.JButton addButton;
    private javax.swing.JButton addFamilyButton;
    private javax.swing.JButton addIntroducerButton;
    private com.geobeck.swing.JFormattedTextFieldEx address1;
    private com.geobeck.swing.JFormattedTextFieldEx address2;
    private com.geobeck.swing.JFormattedTextFieldEx address3;
    private com.geobeck.swing.JFormattedTextFieldEx address4;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JLabel addressLabel1;
    private javax.swing.JLabel addressLabel2;
    private javax.swing.JLabel addressLabel3;
    private com.geobeck.swing.JFormattedTextFieldEx age;
    private javax.swing.JLabel ageLabel;
    private javax.swing.JLabel alertMarkLabel;
    private com.geobeck.swing.JFormattedTextFieldEx beforeVisitNum;
    private javax.swing.JComboBox birthDay;
    private javax.swing.JComboBox birthMonth;
    private com.geobeck.swing.JFormattedTextFieldEx birthYear;
    private javax.swing.JLabel birthdayLabel;
    private javax.swing.JButton btCallProposal;
    private javax.swing.ButtonGroup callFlagGroup;
    private javax.swing.JRadioButton callFlagNG;
    private javax.swing.JRadioButton callFlagOK;
    private javax.swing.JPanel cancel;
    private com.geobeck.swing.JTableEx cancels;
    private javax.swing.JScrollPane cancelsScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx cellularMail;
    private javax.swing.JLabel cellularMailLabel;
    private com.geobeck.swing.JFormattedTextFieldEx cellularNumber;
    private javax.swing.JLabel cellularNumberLabel;
    private javax.swing.JButton changeSosiaGearButton;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JTextField chargeStaffNo;
    private javax.swing.JButton clearButton;
    private javax.swing.JComboBox cmbNextReserve;
    private javax.swing.JComboBox cmbTargetMonth;
    private javax.swing.JComboBox cmbTargetYear;
    private com.geobeck.swing.JTableEx consumptionCourseTable;
    private com.geobeck.swing.JTableEx contractCourseTable;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel courseName;
    private javax.swing.JLabel courseNoLabel;
    private javax.swing.JLabel coursetNo;
    private javax.swing.JLabel creditContracLabel;
    private javax.swing.JCheckBox credit_lock_chk;
    private com.geobeck.swing.JFormattedTextFieldEx currentPrepaid;
    private javax.swing.JLabel cusIDLabel;
    private javax.swing.JLabel cusIDLabel1;
    private javax.swing.JLabel cusIDLabel2;
    private javax.swing.JLabel cusIspotIDLabel;
    private javax.swing.JLabel cusKanaLabel;
    private javax.swing.JLabel cusNameLabel;
    private javax.swing.JLabel cusNameLabel1;
    private javax.swing.JLabel cusNameLabel2;
    private javax.swing.JLabel cusNameLabel3;
    private javax.swing.JPanel customerAddressPanel;
    private javax.swing.JPanel customerBirthdayPanel;
    private com.geobeck.swing.JFormattedTextFieldEx customerID;
    private javax.swing.JPanel customerJobPanel;
    private com.geobeck.swing.JFormattedTextFieldEx customerKana1;
    private com.geobeck.swing.JFormattedTextFieldEx customerKana2;
    private javax.swing.JPanel customerMailPanel;
    private javax.swing.JPanel customerMemoPanel;
    private com.geobeck.swing.JFormattedTextFieldEx customerName1;
    private com.geobeck.swing.JFormattedTextFieldEx customerName2;
    private javax.swing.JPanel customerNamePanel;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo;
    private javax.swing.JPanel customerNoPanel;
    private javax.swing.JPanel customerNotePanel;
    private javax.swing.JPanel customerParamPane;
    private javax.swing.JPanel customerPhoneNumberPanel;
    private javax.swing.JLabel customerRank;
    private javax.swing.JPanel customerSexPanel;
    private javax.swing.JLabel datePoint;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel deleteLabel;
    private com.geobeck.swing.JFormattedTextFieldEx discountName;
    private com.geobeck.swing.JFormattedTextFieldEx discountValue;
    private javax.swing.JScrollPane dmmailScrollPane;
    private com.geobeck.swing.JTableEx dmmailTable;
    private javax.swing.ButtonGroup evaluationButtonGroup;
    private com.geobeck.swing.JTableEx family;
    private javax.swing.JScrollPane familyScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx faxNumber;
    private javax.swing.JLabel faxNumberLabel;
    private javax.swing.JButton feedListButton;
    private javax.swing.JRadioButton female;
    private javax.swing.JComboBox firstComingMotive;
    private javax.swing.JLabel firstComingMotiveLabel;
    private javax.swing.JLabel firstComingMotiveLabel1;
    private com.geobeck.swing.JFormattedTextFieldEx firstComingMotiveNote;
    private javax.swing.JPanel firstComingMotivePanel;
    private javax.swing.JComboBox firstVisitDay;
    private javax.swing.JComboBox firstVisitMonth;
    private com.geobeck.swing.JFormattedTextFieldEx firstVisitYear;
    private javax.swing.JPanel freeHeadingPanel;
    private com.geobeck.swing.JFormattedTextFieldEx genzaiPoint;
    private com.geobeck.swing.JTableEx introduce;
    private javax.swing.JScrollPane introduceScrollPane;
    private javax.swing.JScrollPane introduceScrollPane1;
    private javax.swing.JScrollPane introduceScrollPane2;
    private javax.swing.JScrollPane introduceScrollPane3;
    private javax.swing.JScrollPane introduceScrollPane4;
    private javax.swing.JScrollPane introduceScrollPane6;
    private javax.swing.JScrollPane introduceScrollPane7;
    private com.geobeck.swing.JTableEx introducer;
    private javax.swing.JPanel introducerPanel;
    private javax.swing.JPanel introducerPanel1;
    private javax.swing.JPanel introducerPanel10;
    private javax.swing.JPanel introducerPanel11;
    private javax.swing.JPanel introducerPanel2;
    private javax.swing.JPanel introducerPanel6;
    private javax.swing.JPanel introducerPanel7;
    private javax.swing.JPanel introducerPanel8;
    private javax.swing.JPanel introducerPanel9;
    private javax.swing.JScrollPane introducerScrollPane;
    private javax.swing.JPanel invisiblePanel;
    private javax.swing.JPanel itemInfoPanel;
    private com.geobeck.swing.JFormattedTextFieldEx itemReturnedSalesTotal;
    private com.geobeck.swing.JFormattedTextFieldEx itemSalesTotal;
    private com.geobeck.swing.JTableEx itemTable;
    private javax.swing.JScrollPane itemTableScrollPane;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox job;
    private javax.swing.JLabel jobLabel;
    private javax.swing.JButton jointlyUserAddButton;
    private com.geobeck.swing.JTableEx jointlyUserTable;
    private javax.swing.JLabel lbCustomerName;
    private javax.swing.JLabel lbCustomerNo;
    private javax.swing.JLabel lblBaseMonth;
    private javax.swing.JLabel lblMainStaff;
    private javax.swing.JLabel lblMainStaff1;
    private javax.swing.JLabel lblMainStaff2;
    private javax.swing.JLabel lblTargetPeriod8;
    private javax.swing.JLabel lblTargetPeriod9;
    private javax.swing.JTextArea mailBody;
    private javax.swing.JLabel mailBodyLabel;
    private javax.swing.JScrollPane mailBodyScrollPane;
    private javax.swing.JButton mailButton;
    private javax.swing.JTextArea mailTitle;
    private javax.swing.JLabel mailTitleLabel;
    private javax.swing.JScrollPane mailTitleScrollPane;
    private javax.swing.JRadioButton male;
    private javax.swing.JPanel memoCustomerInfoPanel;
    private javax.swing.JLabel memoCustomerNameLabel;
    private javax.swing.JLabel memoCustomerNoLabel;
    private javax.swing.JRadioButton memoEvaluationBadRadioButton;
    private javax.swing.JRadioButton memoEvaluationBatonRadioButton;
    private javax.swing.JRadioButton memoEvaluationGoodRadioButton;
    private javax.swing.JLabel memoEvaluationLabel;
    private javax.swing.JPanel memoListPanel;
    private javax.swing.JPanel memoPanel;
    private javax.swing.JButton memoRegistButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo memoRegistrationDateComboBox;
    private javax.swing.JLabel memoRegistrationDateLabel;
    private javax.swing.JComboBox memoShopComboBox;
    private javax.swing.JLabel memoShopLabel;
    private javax.swing.JComboBox memoStaffComboBox;
    private javax.swing.JLabel memoStaffLabel;
    private com.geobeck.swing.JTableEx memoTable;
    private javax.swing.JTextArea memoTextArea;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton nextReserveChangeButton;
    private com.geobeck.swing.JTextAreaEx note;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JScrollPane noteScrollPane;
    private javax.swing.JTabbedPane optionTab;
    private javax.swing.JTable payment;
    private javax.swing.JScrollPane paymentScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx pcMail;
    private javax.swing.JLabel pcMailLabel;
    private com.geobeck.swing.JFormattedTextFieldEx phoneNumber;
    private javax.swing.JLabel phoneNumberLabel;
    private javax.swing.JButton pointAddButton;
    private javax.swing.JPanel pointInfoPanel;
    private javax.swing.JPanel pointPanel;
    private com.geobeck.swing.JTableEx pointTable;
    private javax.swing.JScrollPane pointTableScrollPane;
    private javax.swing.JButton pointUpdateButton;
    private com.geobeck.swing.JFormattedTextFieldEx postalCode;
    private javax.swing.JLabel postalCodeLabel;
    private javax.swing.JPanel prepaidInfoPanel;
    private com.geobeck.swing.JTableEx prepaidTable;
    private javax.swing.JScrollPane prepaidTableScrollPane;
    private javax.swing.JButton prevButton;
    private com.geobeck.swing.JFormattedTextFieldEx question_1;
    private com.geobeck.swing.JFormattedTextFieldEx question_2;
    private javax.swing.JPanel registPanel;
    private javax.swing.JButton renewButton;
    private javax.swing.JTabbedPane salesInfoTabbedPane;
    private com.geobeck.swing.JFormattedTextFieldEx salesTotal;
    private com.geobeck.swing.JFormattedTextFieldEx salesTotal1;
    private com.geobeck.swing.JFormattedTextFieldEx salesTotal2;
    private javax.swing.JButton searchAddressButton;
    private javax.swing.JButton searchCusButton;
    private javax.swing.JScrollPane searchResultScrollPane;
    private javax.swing.ButtonGroup sendDmGroup;
    private javax.swing.JRadioButton sendDmNG;
    private javax.swing.JRadioButton sendDmOK;
    private javax.swing.ButtonGroup sendMailGroup;
    private javax.swing.JRadioButton sendMailNG;
    private javax.swing.JRadioButton sendMailOK;
    private javax.swing.ButtonGroup sexGroup;
    private javax.swing.JLabel sexLabel;
    private javax.swing.JLabel sexLabel1;
    private javax.swing.JLabel sexLabel2;
    private javax.swing.JLabel sexLabel3;
    private javax.swing.JLabel sexLabel4;
    private javax.swing.JLabel sexLabel5;
    private javax.swing.JLabel sexLabel6;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    private javax.swing.JLabel shopLabel3;
    private javax.swing.JLabel shopLabel4;
    private javax.swing.JPanel shopPanel;
    private javax.swing.JLabel shopPoint;
    private javax.swing.JButton showButton;
    private javax.swing.JButton showVisitCycleButton;
    private javax.swing.JLabel sosiaGearLabel;
    private javax.swing.JLabel sosiaGearLabel1;
    private javax.swing.JPanel sosiaGearPanel;
    private com.geobeck.swing.JFormattedTextFieldEx suppliedPoint;
    private com.geobeck.swing.JTableEx tbCourseDescription;
    private com.geobeck.swing.JTableEx tbProductDescription;
    private com.geobeck.swing.JTableEx tbProposal;
    private javax.swing.JPanel techInfoPanel;
    private javax.swing.JTable techItemTable;
    private javax.swing.JScrollPane techItemTableScrollPane;
    private com.geobeck.swing.JTableEx techTable;
    private javax.swing.JScrollPane techTableScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx technicClameSalesTotal;
    private com.geobeck.swing.JFormattedTextFieldEx technicSalesTotal;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel txtCustomerName;
    private javax.swing.JLabel txtCustomerNo;
    private com.geobeck.swing.JFormattedTextFieldEx usePoint;
    private com.geobeck.swing.JFormattedTextFieldEx visitCount;
    private com.geobeck.swing.JTableEx visitCycleTable;
    private javax.swing.JScrollPane visitCyclelScrollPane1;
    private javax.swing.JPanel visitInfoPanel;
    private javax.swing.JLabel visitName;
    private javax.swing.JLabel visitNo;
    private com.geobeck.swing.JTextAreaEx visitedMemo;
    private javax.swing.JPanel visitedMemoPanel;
    private javax.swing.JScrollPane visitedMemoScrollPane;
    private com.geobeck.swing.JTableEx visitedMemoTable;
    private javax.swing.JScrollPane visitedMemoTableScrollPane;
    private javax.swing.JButton visitedMemoUpdateButton;
    private javax.swing.ButtonGroup webCancelGroup;
    private javax.swing.JLabel yearLabel;
    private javax.swing.JLabel yearLabel1;
    private javax.swing.JComboBox yearUnit;
    // End of variables declaration//GEN-END:variables
    //�R�����g�ҏW�{�^���p
    private final String MEMO_EDIT_INTRODUCER = "1";	//�Љ
    private final String MEMO_EDIT_INTRODUCE = "2";	//�Љ�ꗗ
    private MstCustomerManager customer = new MstCustomerManager();
    //�Љ�ꗗ
    private ArrayList<MstCustomer> introduceList = new ArrayList<MstCustomer>();
    private ArrayList<MstJob> jobList = new ArrayList<MstJob>();
    //���񗈓X���@�ꗗ
    private ArrayList<MstFirstComingMotive> firstComingMotiveList = new ArrayList<MstFirstComingMotive>();
    private ArrayList<MstFreeHeadingSelectUnitPanel> mfhsups = new ArrayList<MstFreeHeadingSelectUnitPanel>();
    private ArrayList<MstFreeHeadingSelectTextFiled> mfTexts = new ArrayList<MstFreeHeadingSelectTextFiled>();
    private ArrayList<MstCustomerFreeHeading> mcfhs = new ArrayList<MstCustomerFreeHeading>();
    private boolean isReadOnly;
    //��L�uisReadOnly�v��true�̏ꍇ�A�X�V�{�^����\�����邩�ǂ����̃t���O
    private boolean dispRenew;
    private boolean isCloseButtonEnable;
    //�X�V�ς݃t���O
    private boolean renewed;
    private Integer introducerID;
    //�Ƒ����X�V�t���O
    private boolean isModifyFamily = false;
    private Integer nextReserveShopID = null;
    private Integer nextReserveNo = null;
    private Integer row = 0;

    public void setCustomer(MstShop ms, MstCustomer mc) {
        shop.setSelectedItem(ms);
        customer.setData(mc);
        tabZero = false;
        tabOne = false;
        tabTwo = false;
        tabThree = false;
        
// 2017/01/08 �ڋq���� ADD START
        isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
        
        this.showData();
    }

    public MstCustomerManager getCustomer() {
        return customer;
    }

    public boolean isRenewed() {
        return renewed;
    }

    private void setRenewed(boolean renewed) {
        this.renewed = renewed;
    }
    /**
     * �ڋq���o�^��ʗpFocusTraversalPolicy
     */
    private MstCustomerFocusTraversalPolicy ftp = new MstCustomerFocusTraversalPolicy();

    /**
     * �ڋq���o�^��ʗpFocusTraversalPolicy���擾����B
     *
     * @return �ڋq���o�^��ʗpFocusTraversalPolicy
     */
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    /**
     * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(prevButton);
        SystemInfo.addMouseCursorChange(nextButton);
        SystemInfo.addMouseCursorChange(searchCusButton);
        SystemInfo.addMouseCursorChange(clearButton);
        SystemInfo.addMouseCursorChange(showButton);
        SystemInfo.addMouseCursorChange(searchAddressButton);
        SystemInfo.addMouseCursorChange(addButton);
        SystemInfo.addMouseCursorChange(renewButton);
        SystemInfo.addMouseCursorChange(deleteButton);
        SystemInfo.addMouseCursorChange(nextReserveChangeButton);
        SystemInfo.addMouseCursorChange(visitedMemoUpdateButton);
        SystemInfo.addMouseCursorChange(pointAddButton);
        SystemInfo.addMouseCursorChange(pointUpdateButton);
        SystemInfo.addMouseCursorChange(addIntroducerButton);
        SystemInfo.addMouseCursorChange(addFamilyButton);
        SystemInfo.addMouseCursorChange(showVisitCycleButton);
        SystemInfo.addMouseCursorChange(mailButton);
        SystemInfo.addMouseCursorChange(btCallProposal);
    }

    /**
     * �R���|�[�l���g�̊e���X�i�[���Z�b�g����B
     */
    private void setListener() {
        address1.addKeyListener(SystemInfo.getMoveNextField());
        address1.addFocusListener(SystemInfo.getSelectText());
        address2.addKeyListener(SystemInfo.getMoveNextField());
        address2.addFocusListener(SystemInfo.getSelectText());
        address3.addKeyListener(SystemInfo.getMoveNextField());
        address3.addFocusListener(SystemInfo.getSelectText());
        address4.addKeyListener(SystemInfo.getMoveNextField());
        address4.addFocusListener(SystemInfo.getSelectText());
        age.addKeyListener(SystemInfo.getMoveNextField());
        birthDay.addKeyListener(SystemInfo.getMoveNextField());
        birthMonth.addKeyListener(SystemInfo.getMoveNextField());
        birthYear.addKeyListener(SystemInfo.getMoveNextField());
        birthYear.addFocusListener(SystemInfo.getSelectText());
        cellularMail.addKeyListener(SystemInfo.getMoveNextField());
        cellularMail.addFocusListener(SystemInfo.getSelectText());
        cellularNumber.addKeyListener(SystemInfo.getMoveNextField());
        cellularNumber.addFocusListener(SystemInfo.getSelectText());
        customerNo.addKeyListener(SystemInfo.getMoveNextField());
        customerNo.addFocusListener(SystemInfo.getSelectText());
        customerKana1.addKeyListener(SystemInfo.getMoveNextField());
        customerKana1.addFocusListener(SystemInfo.getSelectText());
        customerKana2.addKeyListener(SystemInfo.getMoveNextField());
        customerKana2.addFocusListener(SystemInfo.getSelectText());
        customerName1.addKeyListener(SystemInfo.getMoveNextField());
        customerName1.addFocusListener(SystemInfo.getSelectText());
        customerName2.addKeyListener(SystemInfo.getMoveNextField());
        customerName2.addFocusListener(SystemInfo.getSelectText());
        faxNumber.addKeyListener(SystemInfo.getMoveNextField());
        faxNumber.addFocusListener(SystemInfo.getSelectText());
        female.addKeyListener(SystemInfo.getMoveNextField());
        male.addKeyListener(SystemInfo.getMoveNextField());
        job.addKeyListener(SystemInfo.getMoveNextField());
        // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
        //An start add 20130125
        Alert.addKeyListener(SystemInfo.getMoveNextField());
        Reserved_buffer_time.addKeyListener(SystemInfo.getMoveNextField());
        Reserved_buffer_time.addFocusListener(SystemInfo.getSelectText());
        question_1.addKeyListener(SystemInfo.getMoveNextField());
        question_1.addFocusListener(SystemInfo.getSelectText());
        question_2.addFocusListener(SystemInfo.getSelectText());
        question_2.addFocusListener(SystemInfo.getSelectText());
        //An end add 20130125
        // IVS SANG END INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
        note.addFocusListener(SystemInfo.getSelectText());
        pcMail.addKeyListener(SystemInfo.getMoveNextField());
        pcMail.addFocusListener(SystemInfo.getSelectText());
        phoneNumber.addKeyListener(SystemInfo.getMoveNextField());
        phoneNumber.addFocusListener(SystemInfo.getSelectText());
        postalCode.addKeyListener(SystemInfo.getMoveNextField());
        postalCode.addFocusListener(SystemInfo.getSelectText());
        yearUnit.addKeyListener(SystemInfo.getMoveNextField());
        beforeVisitNum.addKeyListener(SystemInfo.getMoveNextField());
        beforeVisitNum.addFocusListener(SystemInfo.getSelectText());
        firstVisitDay.addKeyListener(SystemInfo.getMoveNextField());
        firstVisitMonth.addKeyListener(SystemInfo.getMoveNextField());
        firstVisitYear.addKeyListener(SystemInfo.getMoveNextField());
        firstVisitYear.addFocusListener(SystemInfo.getSelectText());
        chargeStaffNo.addKeyListener(SystemInfo.getMoveNextField());
        chargeStaffNo.addFocusListener(SystemInfo.getSelectText());
        chargeStaff.addKeyListener(SystemInfo.getMoveNextField());
        chargeStaff.addFocusListener(SystemInfo.getSelectText());
    }

    /**
     * �������������s���B
     */
    private void init() {
        this.loadJobs();
        this.loadFirstComingMotives();
        //2016/08/23 GB MOD #54158 Start
        this.loadCustomerDummy();
        //2016/08/23 GB MOD #54158 End
    }

    /**
     * �I������Ă���X�܂��擾����B
     *
     * @return �I������Ă���X��
     */
    private MstShop getSelectedShop() {
        if (0 <= shop.getSelectedIndex()) {
            return (MstShop) shop.getSelectedItem();
        } else {
            return null;
        }
    }

    /**
     * �I������Ă���X�܂�ID���擾����B
     *
     * @return �I������Ă���X�܂�ID
     */
    private Integer getSelectedShopID() {
        MstShop ms = this.getSelectedShop();

        if (ms != null) {
            return ms.getShopID();
        } else {
            return 0;
        }
    }

    /**
     * �E�ƃ}�X�^���f�[�^�x�[�X����ǂݍ��ށB
     *
     * @return true - ����
     */
    private boolean loadJobs() {
        jobList.clear();

        jobList.add(null);

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(MstJob.getSelectAllSQL());

            while (rs.next()) {
                MstJob mj = new MstJob();

                mj.setData(rs);

                jobList.add(mj);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * ���񗈓X���@�}�X�^���f�[�^�x�[�X����ǂݍ��ށB
     *
     * @return true - ����
     */
    private boolean loadFirstComingMotives() {
        firstComingMotiveList.clear();

        firstComingMotiveList.add(null);

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(MstFirstComingMotive.getSelectAllSQL());

            while (rs.next()) {
                MstFirstComingMotive mfcm = new MstFirstComingMotive();

                mfcm.setData(rs);

                firstComingMotiveList.add(mfcm);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * ���͍��ڂ��N���A����B
     */
    private void clear() {
        customer.setData(new MstCustomer());
        changeSosiaGearButton.setEnabled(false);
        changeSosiaGearButton.setIcon(null);
        changeSosiaGearButton.setBorderPainted(true);
        tabZero = false;
        tabOne = false;
        tabTwo = false;
        tabThree = false;

// 2017/01/08 �ڋq���� ADD START
        isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
        
    }

    /**
     * �ڋq�R�[�h����ڋq���Z�b�g����B
     *
     * @return
     */
    private boolean setCustomerByNo() {
        customer.setData(new MstCustomer());

        //�ڋqNo�����͂���Ă��Ȃ��ꍇ�A�����𔲂���
        if (this.customerNo.getText().equals("")) {
            return false;
        }

        //�ڋqNo���Z�b�g����B
        customer.setCustomerNo(this.customerNo.getText());
        try {
            customer.setData(SelectSameNoData.getMstCustomerByNo(
                    parentFrame,
                    SystemInfo.getConnection(),
                    this.customerNo.getText(),
                    this.getSelectedShopID()));
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * �ڋq����ǂݍ��ށB
     *
     * @return true - ����
     */
    private boolean loadCustomer() {
        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            customer.load(con);
            this.loadCustomerFreeHeading(con);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }

    //2016/08/23 GB MOD #54158 Start
    /**
     * �_�~�[�̌ڋq����ǂݍ��ށB
     *
     * @return true - ����
     */
    private boolean loadCustomerDummy() {
        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        this.loadCustomerFreeHeadingForAdd(con);

        return true;
    }
    //2016/08/23 GB MOD #54158 End
    
    /**
     * customer�̃f�[�^��\������B
     */
    private void showData() {
        if (customer == null) {
            return;
        }
        if (jTabbedPane1.getSelectedIndex() == tabGenaralIndex && !tabZero) {

        customerNo.setText(customer.getCustomerNo());
        customerName1.setText(customer.getCustomerName(0));
        customerName2.setText(customer.getCustomerName(1));
        customerKana1.setText(customer.getCustomerKana(0));
        customerKana2.setText(customer.getCustomerKana(1));
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            this.setChargeStaff(customer.getStaff().getStaffNo());
            customerID.setText(customer.getCustomerID() == null ? "" : customer.getCustomerID().toString());
        }
        postalCode.setText((customer.getPostalCode() == null ? "" : customer.getPostalCode()));
        address1.setText((customer.getAddress(0) == null ? "" : customer.getAddress(0)));
        address2.setText((customer.getAddress(1) == null ? "" : customer.getAddress(1)));
        address3.setText((customer.getAddress(2) == null ? "" : customer.getAddress(2)));
        address4.setText((customer.getAddress(3) == null ? "" : customer.getAddress(3)));
        phoneNumber.setText((customer.getPhoneNumber() == null ? "" : customer.getPhoneNumber()));
        faxNumber.setText((customer.getFaxNumber() == null ? "" : customer.getFaxNumber()));
        cellularNumber.setText((customer.getCellularNumber() == null ? "" : customer.getCellularNumber()));
        pcMail.setText((customer.getPCMailAddress() == null ? "" : customer.getPCMailAddress()));
        cellularMail.setText((customer.getCellularMailAddress() == null ? "" : customer.getCellularMailAddress()));
        this.setSelectedSendMail();
        this.setSelectedSendDm();
        this.setSelectedCallFlag();
        this.setSelectedSex();
        this.setBirthday();
        this.setSelectedJob();
        // IVS SANG START INSERT [gb�\�[�X]�\�[�X�}�[�W
        //An start add 20130125
        this.setSelectedWebReservationFlag();
        this.setSelectedCreditLock();
        if (customer.getAlertMark() == null) {
            this.alertMarkLabel.setText("");
            this.Alert.setSelectedItem("");
            this.alertMarkLabel.setOpaque(false);
        } else {
            //  alertMarkLabel.setText(Alert.getSelectedItem().toString());
            this.alertMarkLabel.setText("�y" + customer.getAlertMark() + "�z");
            this.Alert.setSelectedItem(customer.getAlertMark());
            //this.alertMarkLabel.setOpaque(true);
        }

        if (customer.getAlertMark() == null) {
            this.cusNameLabel1.setText("����");
            this.Alert.setSelectedItem("");
            this.cusNameLabel1.setOpaque(false);

        } else {
            //  alertMarkLabel.setText(Alert.getSelectedItem().toString());
            this.cusNameLabel1.setText("����  " + "�y" + customer.getAlertMark() + "�z");
            this.Alert.setSelectedItem(customer.getAlertMark());
            //this.alertMarkLabel.setOpaque(true);
        }

        if (customer.getAlertMark() == null) {
            this.cusNameLabel3.setText("����");
            this.Alert.setSelectedItem("");
            this.cusNameLabel3.setOpaque(false);
        } else {
            //  alertMarkLabel.setText(Alert.getSelectedItem().toString());
            this.cusNameLabel3.setText("����  " + "�y" + customer.getAlertMark() + "�z");
            this.Alert.setSelectedItem(customer.getAlertMark());
            //this.alertMarkLabel.setOpaque(true);
        }
            
        if (customer.getAlertMark() == null) {
            this.cusNameLabel2.setText("����");
            this.Alert.setSelectedItem("");
            this.cusNameLabel2.setOpaque(false);
        } else {
            //  alertMarkLabel.setText(Alert.getSelectedItem().toString());
            this.cusNameLabel2.setText("����  " + "�y" + customer.getAlertMark() + "�z");
            this.Alert.setSelectedItem(customer.getAlertMark());
            //this.alertMarkLabel.setOpaque(true);
        }

        if (SystemInfo.getNSystem() == 1) {

            if (customer.getReservationBufferTime() == null) {
                this.Reserved_buffer_time.setText("0");

            } else {
                Reserved_buffer_time.setText(customer.getReservationBufferTime().toString());
            }
            this.BufferTimeLabel.setVisible(true);
            this.Reserved_buffer_time.setVisible(true);
            this.timeLabel.setVisible(true);
        } else {
            this.BufferTimeLabel.setVisible(false);
            this.Reserved_buffer_time.setVisible(false);
            this.timeLabel.setVisible(false);
        }


        question_1.setText((customer.getQuestion_1() == null ? "" : customer.getQuestion_1()));
        question_2.setText((customer.getQuestion_2() == null ? "" : customer.getQuestion_2()));
        //An end add 20130125
        // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        this.setSelectedFirstComingMotive();
        firstComingMotiveNote.setText(customer.getFirstComingMotiveNote());
        note.setText(customer.getNote());
        note.setCaretPosition(0);
        this.setAge();
        this.changeButtonEnabled(customer.getCustomerID() != null && customer.getCustomerID() != 0);

        if (customer.getCustomerID() == null) {
            //2016/08/23 GB MOD #54158 Start
            //mcfhs.clear();
            loadCustomerDummy();
            //2016/08/23 GB MOD #54158 End
        }

        for (MstFreeHeadingSelectUnitPanel mfhsup : mfhsups) {
            mfhsup.setSelectedInit();
            for (MstCustomerFreeHeading mcfh : mcfhs) {
                if (mcfh.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() == mfhsup.getFreeHeadingClassID()) {
                    mfhsup.setFreeHeading(mcfh.getMstFreeHeading());
                }
            }
        }

        // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
        //An start add 20130406
        for (MstFreeHeadingSelectTextFiled mfText : mfTexts) {
                //IVS_LVTu start add 2015/04/23 Bug #36369
                mfText.setFreeHeadingText("");
                //IVS_LVTu end add 2015/04/23 Bug #36369
            for (MstCustomerFreeHeading cFH : mcfhs) {

                //   getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID()

                if (cFH.getMstCustomerFreeHeadings().getFreeHeadingClassID() == mfText.getFreeHeadingClassID()) {
                    mfText.setFreeHeadingText(cFH.getFreeHeadingText());
                }
            }

        }
        }
        //IVS_LVTu start edit 2015/07/16 Bug #40581
        visitName.setText(customer.getFullCustomerName());
        visitNo.setText(customer.getCustomerNo());
        beforeVisitNum.setText(customer.getBeforeVisitNum().toString());
        this.setFirstVisitDate();
        //An end add 20130406
        // IVS SANG END INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
        //this.loadOption();
        
         if (jTabbedPane1.getSelectedIndex() == tabAtShopIndex && !tabOne) {
               //visitName.setText(customer.getFullCustomerName());
               //visitNo.setText(customer.getCustomerNo());
               //beforeVisitNum.setText(customer.getBeforeVisitNum().toString());
               //this.setFirstVisitDate();
             //IVS_LVTu end edit 2015/07/16 Bug #40581
               
        customer.loadAccounts();
        this.showAccounts();
        this.showVisitedMemoList();
        this.showPointList();
        this.showPrepaidList();
        // IVS SANG START INERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        //An start add 20130125
        this.showCancel();
           }

        /* WEB����A���{�^�����X�V���� */
        if (customer.getCustomerNo() != null) {
            updateSosiaGearButton();
        }

        // �|�C���g�g�p���̓|�C���g��\��
        //IVS_TMTrong start edit 2015/10/19 Bug #43584
        if ((jTabbedPane1.getSelectedIndex() == tabGenaralIndex) || (jTabbedPane1.getSelectedIndex() == tabAtShopIndex && !tabOne)) {
             //tabOne = true;
            this.showGenzaiPoint();
        }
        //IVS_TMTrong end edit 2015/10/19 Bug #43584
            
        // �Љ��\������
        if (jTabbedPane1.getSelectedIndex() == tabGenaralIndex && !tabZero) {
            
        this.introducerID = customer.getIntroducerID();

        if (introducerID != null && introducerID.intValue() > 0) {
            MstCustomer introducer = new MstCustomer(this.introducerID);
            ConnectionWrapper con = SystemInfo.getConnection();
            if (con == null) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                introducer.load(con);
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            this.addIntoroducer(introducer);
        } else {
            // ��̍s���c��̂ŏ���
            this.removeIntroducer();
        }

        // �Љ�ꗗ��\������
        SwingUtil.clearTable(introduce);
        introduceList.clear();
        introduceList = getIntroduceList();
        for (MstCustomer mc : introduceList) {
            this.addIntoroduce(mc);
        }
        }
        if (jTabbedPane1.getSelectedIndex() == tabAtShopIndex && !tabOne) {
        this.showNextReserveDate();
            // start detele 20130705 IVS
            SwingUtil.clearTable(visitCycleTable);
            // ���X�T�C�N���\��
            // this.showVisitCycle();
            // end detele 20130705 IVS
        }
        // �����N�\��
        if (jTabbedPane1.getSelectedIndex() == 0 && !tabZero) {
        customerRank.setText("");
        MstSetting setting = SystemInfo.getSetteing();
        if (customer.getCustomerID() != null && setting.getRankStartDate() != null && setting.getRankEndDate() != null) {
            try {
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("     (");
                sql.append("         select");
                sql.append("             rank_name");
                sql.append("         from");
                sql.append("             mst_customer_rank_setting");
                sql.append("         where");
                sql.append("             id =");
                sql.append("                 (");
                sql.append("                     select");
                sql.append("                         min(id)");
                sql.append("                     from");
                sql.append("                         mst_customer_rank_setting");
                sql.append("                     where");
                sql.append("                             t.count >= count_from");
                sql.append("                         and t.value >= value_from");
                sql.append("                 )");
                sql.append("     ) as rank");
                sql.append(" from");
                sql.append("     (");
                sql.append("         select");
                sql.append("              count(*) as count");
                if (SystemInfo.getSetteing().getRankTaxType() == 0) {
                    // �ō���
                    sql.append("         ,sum(discount_sales_value_in_tax) as value");
                } else {
                    // �Ŕ���
                    sql.append("         ,sum(discount_sales_value_no_tax) as value");
                }
                sql.append("         from");
                sql.append("             view_data_sales_valid");
                sql.append("         where");
                sql.append("                 sales_date between " + SQLUtil.convertForSQLDateOnly(setting.getRankStartDate()));
                sql.append("                                and " + SQLUtil.convertForSQLDateOnly(setting.getRankEndDate()));
                sql.append("             and customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
                sql.append("     ) t");

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                if (rs.next()) {
                    if (rs.getString("rank") != null) {
                        customerRank.setText("�ڋq�����N �y " + rs.getString("rank") + " �z");
                    }
                }
                rs.close();
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        }

        // �Ƒ��f�[�^�\��
        if(jTabbedPane1.getSelectedIndex() == tabGenaralIndex && !tabZero){
        this.showFamily();
        }
        
        //IVS_LVTu start edit 2015/11/23 New request #44111
        // DM�E���[������
        //if(jTabbedPane1.getSelectedIndex() == 3 && !tabThree){
        if((jTabbedPane1.getSelectedIndex() == tabProposalIndex || jTabbedPane1.getSelectedIndex() ==tabDMMailIndex ) && !tabThree){
        //IVS_LVTu end edit 2015/11/23 New request #44111
            DMName.setText(customer.getFullCustomerName());
            DMNo.setText(customer.getCustomerNo());
        this.showDmMailHistory();
        mailButton.setEnabled(customer.getCustomerID() != null);
        }
        //�R�[�X�_��E��������
        if (jTabbedPane1.getSelectedIndex() == tabContractIndex && !tabTwo) {
            courseName.setText(customer.getFullCustomerName());
            coursetNo.setText(customer.getCustomerNo());
        this.showCourseHistory();
        if (contractCourseTable.getRowCount() > 0) {
            //�R�[�X�_��1���ȏ㑶�݂���ꍇ��1�s�ڂ������I�ɑI�������ɂ���
            contractCourseTable.setRowSelectionInterval(0, 0);
            //�R�[�X����������\��
            this.showConsumptionCourseHistory();
        }
        }

        //Luc start add 20130308
        if (SystemInfo.getIspotShopID() != null) {
            if (!SystemInfo.getIspotShopID().equals("")) {
                //IVS_LVTu start edit 2016/02/24 New request #48455
                //if (!SystemInfo.isReservationOnly()) {
                if (!SystemInfo.getSystemType().equals(1)) {
                //IVS_LVTu end edit 2016/02/24 New request #48455
                    jLabel6.setVisible(false);
                    firstVisitYear.setVisible(false);
                    yearLabel1.setVisible(false);
                    firstVisitMonth.setVisible(false);
                    jLabel3.setVisible(false);
                    firstVisitDay.setVisible(false);
                    jLabel4.setVisible(false);
                    jLabel25.setVisible(false);
                    visitCount.setVisible(false);
                    jLabel18.setVisible(false);
                    jLabel17.setVisible(false);
                    beforeVisitNum.setVisible(false);
                    jLabel19.setVisible(false);
                    jLabel8.setVisible(false);
                    jLabel7.setVisible(false);
                    technicSalesTotal.setVisible(false);
                    jLabel32.setVisible(false);
                    itemSalesTotal.setVisible(false);
                    salesTotal.setVisible(false);
                    //nhanvt start add
                    jLabel8.setVisible(false);
                    jLabel34.setVisible(false);
                    jLabel35.setVisible(false);
                    salesTotal1.setVisible(false);
                    salesTotal2.setVisible(false);
                    //nhanvt end add
                    salesInfoTabbedPane.remove(visitInfoPanel);
                    salesInfoTabbedPane.remove(techInfoPanel);
                    salesInfoTabbedPane.remove(itemInfoPanel);
                    salesInfoTabbedPane.remove(visitedMemoPanel);
                    optionTab.remove(jPanel6);
                    optionTab.remove(jPanel7);
                }
               
            }        
        }
        
// 2017/01/08 �ڋq���� ADD START
        // �I�����ꂽ�^�u���ڋq���� ���� �ڋq�����^�u�̓��e�����ݒ�ł���Ώ���������B
        if(jTabbedPane1.getSelectedComponent().equals(customerMemoPanel) && !isCustomerMemoTabLoaded) {
            initCustomerMemoTab();
            isCustomerMemoTabLoaded = true;
        }
// 2017/01/08 �ڋq���� ADD END
        
        if(jTabbedPane1.getSelectedIndex() == tabGenaralIndex && !tabZero){
            tabZero = true;
        }else if(jTabbedPane1.getSelectedIndex() == tabAtShopIndex && !tabOne){
            tabOne = true;
        }else if(jTabbedPane1.getSelectedIndex() == tabContractIndex && !tabTwo){
            tabTwo = true;
        }else if(jTabbedPane1.getSelectedIndex() ==tabProposalIndex  && !tabThree){
            tabThree = true;
        }         
       //nhanvt start add 20150212 New request #35199
        try {
            SystemInfo.getCurrentShop().load(SystemInfo.getConnection());
        }catch(Exception e){}
        if(SystemInfo.getCurrentShop().getShopID() != 0 && SystemInfo.getCurrentShop().getCourseFlag()!=1) {
            jLabel34.setVisible(false);
            salesTotal1.setVisible(false);
            jLabel35.setVisible(false);
            salesTotal2.setVisible(false);
        }
        //nhanvt end add 20150212 New request #35199
        //Luc end add 20130308
        //Luc start add 20130528
        if (SystemInfo.getAccountSetting().getCreditLockManage() != null) {
            if (SystemInfo.getAccountSetting().getCreditLockManage() == 1) {
                credit_lock_chk.setEnabled(false);
            } else {
                credit_lock_chk.setEnabled(true);
            }
        }

        //Luc end add 20130528
        //IVS_LVTu start add 2015/11/23 New request #44111
        showProposal();
        //IVS_LVTu end add 2015/11/23 New request #44111
    }

    // IVS SANG START 20131102 [gb�\�[�X]�\�[�X�}�[�W
    //An start add 20130125
    private void setSelectedWebReservationFlag() {
        WebOKCancel.setSelected(false);
        WebNGCancel.setSelected(false);

        //�I������Ă��Ȃ��ꍇ
        if (customer.getWebReservationFlag() == null) {
            WebOKCancel.setSelected(true);
        } else if (customer.getWebReservationFlag() == 0) {
            WebNGCancel.setSelected(true);
        } else if (customer.getWebReservationFlag() == 1) {
            WebOKCancel.setSelected(true);
        }
    }
    // An end add 20130125
    //An start add 20130227 

    private void setSelectedCreditLock() {
        credit_lock_chk.setSelected(false);

        //�I������Ă��Ȃ��ꍇ
        if (customer.getSelectedCreditLock() == null) {
            credit_lock_chk.setSelected(false);
        } else if (customer.getSelectedCreditLock() == 0) {
            credit_lock_chk.setSelected(false);
        } else if (customer.getSelectedCreditLock() == 1) {
            credit_lock_chk.setSelected(true);
        }
    }
    // IVS SANG END 20131102 [gb�\�[�X]�\�[�X�}�[�W

    /**
     * ���X�T�C�N����\������B
     */
    private void showVisitCycle() {
        if (!inputCheck()) {
            return;
        }

        SwingUtil.clearTable(visitCycleTable);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(cmbTargetYear.getSelectedItem().toString()));
        cal.set(Calendar.MONTH, Integer.parseInt(cmbTargetMonth.getSelectedItem().toString()) - 1);

        Calendar calTmp = (Calendar) cal.clone();
        for (int i = 1; i <= 12; i++) {
            visitCycleTable.getColumnModel().getColumn(i).setHeaderValue(calTmp.get(Calendar.MONTH) + 1);
            calTmp.add(Calendar.MONTH, 1);
        }

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      mtc.technic_class_id");
        sql.append("     ,max(mtc.technic_class_name) as technic_class_name");
        sql.append("     ,max(mtc.display_seq)");

        calTmp.setTime(cal.getTime());
        for (int i = 1; i <= 12; i++) {
            sql.append(" ,max(case when date_part('year', ds.sales_date) = " + calTmp.get(Calendar.YEAR) + " and date_part('month', ds.sales_date) = " + (calTmp.get(Calendar.MONTH) + 1) + " then 1 end) as c_" + i);
            calTmp.add(Calendar.MONTH, 1);
        }

        sql.append("     ,count(distinct ds.slip_no) as cnt");
        sql.append("     ,extract(epoch from max(ds.sales_date) - min(ds.sales_date)) / 86400 as days");

        sql.append(" from");
        sql.append("     mst_technic_class mtc");
        sql.append("         join mst_technic mt");
        sql.append("             using (technic_class_id)");
        // start edit 20130710 IVS
        // sql.append("         left join data_sales_detail dsd");
        sql.append("         inner join data_sales_detail dsd");
        // end edit 20130710 IVS
        sql.append("                on dsd.product_id = mt.technic_id");
        sql.append("               and dsd.delete_date is null");
        sql.append("               and dsd.product_division = 1");
        sql.append("         left join data_sales ds");
        sql.append("                on ds.shop_id = dsd.shop_id");
        sql.append("               and ds.slip_no = dsd.slip_no");
        sql.append("               and ds.delete_date is null");

        sql.append("               and ds.sales_date >= " + SQLUtil.convertForSQLDateOnly(cal));

        calTmp.setTime(cal.getTime());
        calTmp.add(Calendar.YEAR, 1);
        sql.append("               and ds.sales_date < " + SQLUtil.convertForSQLDateOnly(calTmp));

        sql.append("               and ds.customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));

        sql.append(" where");
        sql.append("         mt.delete_date is null");
        sql.append("     and mtc.delete_date is null");
        sql.append(" group by");
        sql.append("     mtc.technic_class_id");
        sql.append(" order by");
        sql.append("     max(mtc.display_seq)");

        try {

            DefaultTableModel model = (DefaultTableModel) visitCycleTable.getModel();
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

            while (rs.next()) {

                int cycle = 0;
                try {
                    cycle = rs.getInt("days") / rs.getInt("cnt");
                } catch (Exception e) {
                }

                model.addRow(new Object[]{
                            rs.getString("technic_class_name"),
                            rs.getInt("c_1") == 1 ? "��" : "",
                            rs.getInt("c_2") == 1 ? "��" : "",
                            rs.getInt("c_3") == 1 ? "��" : "",
                            rs.getInt("c_4") == 1 ? "��" : "",
                            rs.getInt("c_5") == 1 ? "��" : "",
                            rs.getInt("c_6") == 1 ? "��" : "",
                            rs.getInt("c_7") == 1 ? "��" : "",
                            rs.getInt("c_8") == 1 ? "��" : "",
                            rs.getInt("c_9") == 1 ? "��" : "",
                            rs.getInt("c_10") == 1 ? "��" : "",
                            rs.getInt("c_11") == 1 ? "��" : "",
                            rs.getInt("c_12") == 1 ? "��" : "",
                            rs.getInt("cnt"),
                            cycle
                        });
            }
            rs.close();
            // IVS SANG START DELETE 20131103 [gb�\�[�X]�\�[�X�}�[�W
            //optionTab.setSelectedIndex(4);
            //optionTab.setSelectedIndex(3);
            // IVS SANG START DEKETE 20131103 [gb�\�[�X]�\�[�X�}�[�W
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

    }

    /**
     * DM�E���[��������\������B
     */
    private void showDmMailHistory() {
        SwingUtil.clearTable(dmmailTable);
        mailTitle.setText("");
        mailBody.setText("");

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      a.dm_title");
        sql.append("     ,b.*");
        sql.append(" from");
        sql.append("     data_dm_history a");
        sql.append("         join data_dm_history_detail b");
        sql.append("         using(shop_id, dm_type, make_date)");
        sql.append(" where");
        sql.append("     b.customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
        sql.append(" order by");
        sql.append("     a.make_date desc");

        try {

            dataDmHistoryDetails = new ArrayList<DataDmHistoryDetail>();

            DefaultTableModel model = (DefaultTableModel) dmmailTable.getModel();
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

            while (rs.next()) {
                DataDmHistoryDetail dhd = new DataDmHistoryDetail();
                dhd.setCustomerID(rs.getInt("customer_id"));
                dhd.setDmType(rs.getInt("dm_type"));
                dhd.setMakeDate(rs.getDate("make_date"));
                dhd.setMailTitle(rs.getString("mail_title"));
                dhd.setMailBody(rs.getString("mail_body"));
                // ThuanNK start edit 2014/02/27
                dhd.setTitle(rs.getString("dm_title"));
                dhd.setCustomerName(0, customer.getCustomerName(0) + " " + customer.getCustomerName(1));
                // ThuanNK end edit 2014/02/27

                model.addRow(new Object[]{
                            dhd.getDmType().equals(DmHistory.DM_MAIL) ? "���[��"
                            : dhd.getDmType().equals(DmHistory.DM_LABEL) ? "�������x��"
                            : dhd.getDmType().equals(DmHistory.DM_POSTCARD) ? "�n�K�L" : "",
                            String.format("%1$tY/%1$tm/%1$td", dhd.getMakeDate()),
                            // ThuanNK start edit 2014/02/27
                            dhd.getTitle()
                        // ThuanNK end edit 2014/02/27
                        });
                // ThuanNK start add 2014/02/27
                dataDmHistoryDetails.add(dhd);
                // ThuanNK end add 2014/02/27
            }
            rs.close();

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * �R�[�X�_��E����������\������
     */
    private void showCourseHistory() {
        if (contractCourseTable.getCellEditor() != null) {
            contractCourseTable.getCellEditor().stopCellEditing();
        }
        //IVS_LVTu start edit 2015/12/28 Bug #45225
        //���ݕ\������Ă�������N���A����
        SwingUtil.clearTable(contractCourseTable);
        SwingUtil.clearTable(consumptionCourseTable);
        SwingUtil.clearTable(jointlyUserTable);
        //�R�[�X�_�񗚗��擾
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ds.shop_id");
        sql.append("     ,ds.slip_no");
        sql.append("     ,dc.contract_no");
        sql.append("     ,dc.contract_detail_no");
        sql.append("     ,dc.product_id");
        // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        //An start edit 20130227
        sql.append("     ,dc.valid_date");
        sql.append("     ,dc.limit_date");
        sql.append("     ,dc.contract_status");
        sql.append("     ,dc.delete_date");
        //An end edit 20130227
        // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        sql.append("     ,ds.sales_date");
        sql.append("     ,mc.course_name");
        sql.append("     ,dc.product_value");
        sql.append("     ,dc.product_num");
        sql.append("     ,coalesce(ms.staff_name1, '') as staff_name1");
        sql.append("     ,coalesce(ms.staff_name2, '') as staff_name2");
        sql.append("     ,sum(coalesce(dcd.product_num,0)) as consumption_num");
        sql.append("     ,before_contract_shop_id");
        sql.append("     ,before_contract_no");
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         inner join data_contract dc");
        sql.append("                 on ds.shop_id = dc.shop_id");
        sql.append("                and ds.slip_no = dc.slip_no");
        sql.append("         inner join mst_course mc");
        sql.append("                 on mc.course_id = dc.product_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = dc.staff_id");
        sql.append("         left join data_contract_digestion dcd");
        sql.append("                on dc.shop_id = dcd.contract_shop_id");
        sql.append("               and dc.contract_no = dcd.contract_no");
        sql.append("               and dc.contract_detail_no = dcd.contract_detail_no");
        sql.append(" where");
        sql.append("     ds.customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
        //nhanvt start add 20150112 Bug #34883
        sql.append("     and ds.sales_date is not null ");
        //nhanvt end add 20150112 Bug #34883
        sql.append(" group by");
        sql.append("      ds.shop_id");
        sql.append("     ,ds.slip_no");
        sql.append("     ,dc.contract_no");
        sql.append("     ,dc.contract_detail_no");
        sql.append("     ,dc.product_id");
        sql.append("     ,ds.sales_date");
        sql.append("     ,mc.course_name");
        sql.append("     ,dc.product_value");
        sql.append("     ,dc.product_num");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,mc.course_id");
        // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        //An start edit 20130227
        sql.append("     ,dc.valid_date");
        sql.append("     ,dc.limit_date");
        sql.append("     ,dc.contract_status"); 
        sql.append("     ,dc.delete_date");
        sql.append("     ,before_contract_shop_id");
        sql.append("     ,before_contract_no");
        //An end edit 20130227
        // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        sql.append(" order by");
        //IVS_LVTu start edit 2015/05/28 New request #37068
        //sql.append("      ds.sales_date");
        sql.append("      ds.sales_date DESC");
        //IVS_LVTu end edit 2015/05/28 New request #37068
        sql.append("     ,dc.contract_no DESC");
        sql.append("     ,dc.contract_detail_no");

        try {

            DefaultTableModel model = (DefaultTableModel) contractCourseTable.getModel();
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

            NumberFormat nf = NumberFormat.getInstance();

            dataContracts = new ArrayList<DataContract>();
            int row = 0;
            while (rs.next()) {
                DataContract dc = new DataContract();
                dc.setContractNo(rs.getInt("contract_no"));
                dc.setContractDetailNo(rs.getInt("contract_detail_no"));
                dc.setSlipNo(rs.getInt("slip_no"));
                MstShop s = new MstShop();
                s.setShopID(rs.getInt("shop_id"));
                dc.setShop(s);

                // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
                //An start add 20130227

                dc.setValidDate(rs.getDate("valid_date"));
                dc.setLimitDate(rs.getDate("limit_date"));
                dc.setContractStatus(rs.getInt("contract_status"));
                dc.setDeleteDate(rs.getDate("delete_date"));
                //An end add 20130227
                // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W

                Product p = new Product();
                p.setProductID(rs.getInt("product_id"));
                p.setProductName(rs.getString("course_name"));
                p.setPrice(rs.getLong("product_value"));
                dc.setProduct(p);

                dc.setProductNum(rs.getInt("product_num"));
                dc.setProductValue(rs.getLong("product_value"));
                dc.setRemainingNum(dc.getProductNum() - (rs.getDouble("consumption_num")));
                
                dc.setBeforeContractShopID(rs.getInt("before_contract_shop_id"));
                dc.setBeforeContractNo(rs.getInt("before_contract_no"));

                String strStatus = "";
                // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calDate = Calendar.getInstance();
                Date date = calDate.getTime();
                try {
                    date = sdf.parse(String.valueOf(calDate.get(Calendar.YEAR)) + "-" + String.valueOf(calDate.get(Calendar.MONTH) + 1) + "-" + String.valueOf(calDate.get(Calendar.DAY_OF_MONTH)));
                } catch (Exception e) {
                }
                calDate.setTime(date);
                Calendar calValidDate = Calendar.getInstance();


                if (dc.getDeleteDate() != null || dc.getContractStatus() == 1) {
                    strStatus = "���";
                    //Luc start edit 20140619 bug  #25575 [gb][rizap]�L�������ύX���{���̓��t�ɕύX�����ꍇ�A���{�^�����o�Ă��Ȃ��B
                    //} else if (dc.getValidDate() != null && dc.getValidDate().compareTo(date) < 0 || dc.getLimitDate() != null && dc.getLimitDate().compareTo(date) < 0) {
                } else if (dc.getContractStatus() == 2) {
                    strStatus = "�ύX��";

                } else {
                    //defaut status contract is running
                    strStatus = "�_��";
                    if (dc.getValidDate() != null) {
                        calValidDate.setTime(dc.getValidDate());
                        if (calValidDate.before(calDate) == true) {
                            //Luc end edit 20140619 bug  #25575 [gb][rizap]�L�������ύX���{���̓��t�ɕύX�����ꍇ�A���{�^�����o�Ă��Ȃ��B
                            strStatus = "������";
                        }
                    }
                }



                
                //An start add 20130227 

                model.addRow(new Object[]{
                            String.format("%1$tY/%1$tm/%1$td", rs.getDate("sales_date")), //���t               
                            //                        rs.getString("course_name"), //����
                            dc.getContractStatus() == 1 ? "(�ύX�O)" + dc : dc.getContractStatus() == 2 ? "(�ύX��)" + dc : dc,
                            //       dc,
                            FormatUtil.decimalFormat(dc.getProductValue()), //�_����z
                            nf.format(rs.getDouble("consumption_num")) + " / " + dc.getProductNum(), //������
                            nf.format(dc.getProductNum() - (rs.getDouble("consumption_num"))),
                            dc.getProductNum() - (rs.getDouble("consumption_num")) == 0.0 ? "�I��" : strStatus,
                            rs.getString("staff_name1") + " " + rs.getString("staff_name2"), //�S����
                            dc.getValidDate() == null ? "" : String.format("%1$tY/%1$tm/%1$td", (dc.getValidDate())),//���t
                            dc.getLimitDate() == null ? "" : String.format("%1$tY/%1$tm/%1$td", (dc.getLimitDate())),//����    

                            ((!SystemInfo.getAuthority().getAuthoryty(28)) || (dc.getProductNum() - (rs.getDouble("consumption_num")) == 0.0)) ? getLable() : getChangeButton(dc.getShop().getShopID(), dc.getSlipNo(), dc.getContractNo(), dc.getContractDetailNo(), strStatus),
                            //((!SystemInfo.getAuthority().getAuthoryty(29)) ||  (dc.getProductNum() - (rs.getDouble("consumption_num")) == 0.0) )? "" : getCancelButton(customer.getCustomerID(), dc.getSlipNo(), dc.getContractNo(), dc.getContractDetailNo(), dc.getShop().getShopID(), strStatus),
                            //nhanvt edit start 20141007 Request #31292 
                            ((!SystemInfo.getAuthority().getAuthoryty(29)) || (dc.getProductNum() - (rs.getDouble("consumption_num")) == 0.0)) ? getLable() : getCancelButton(customer, dc, strStatus,rs.getDate("delete_date")),
                            //nhanvt edit end 20141007 Request #31292 
                            ((!SystemInfo.getAuthority().getAuthoryty(290)) || (dc.getProductNum() - (rs.getDouble("consumption_num")) == 0.0)) ? getLable() : getDeadlineButton(customer.getCustomerID(), dc.getSlipNo(), dc.getContractNo(), dc.getContractDetailNo(), dc.getShop().getShopID(), strStatus),
                            ((strStatus.equals("������") || strStatus.equals("���") || dc.getProductNum() - (rs.getDouble("consumption_num")) == 0.0)) ? getLable() : getJointlyUserButton()
                        });
                dataContracts.add(dc);

//                    model.addRow(new Object[]{
//                        String.format("%1$tY/%1$tm/%1$td", rs.getDate("sales_date")), //���t
////                        rs.getString("course_name"), //����
//                        dc,
//                        FormatUtil.decimalFormat(dc.getProductValue()), //�_����z
//                        nf.format(rs.getDouble("consumption_num")) + " / " + dc.getProductNum(), //������
//                        rs.getString("staff_name1") + " " + rs.getString("staff_name2") //�S����
//                    });
                // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
                addBlankCourseHistoryTable(contractCourseTable);
            }
            
            //check data
            int index = 0;
            for ( int i = 0;i < this.dataContracts.size() ;i ++ ) {
                boolean flag = true;
                if ( i!= 0) {
                    index = index + 2;
                }
                JButton btCancel ;
                if (contractCourseTable.getValueAt(index, 10) instanceof JButton) {
                    btCancel = (JButton)contractCourseTable.getValueAt(index, 10);
                    for ( int j = 0;j < this.dataContracts.size();j ++) {
                        if(j != i) {
                            if (this.dataContracts.get(i).getDeleteDate() != null
                                    && this.dataContracts.get(j).getBeforeContractShopID() != null 
                                    && this.dataContracts.get(j).getBeforeContractShopID().equals(this.dataContracts.get(i).getShop().getShopID())
                                    && this.dataContracts.get(j).getBeforeContractNo() != null
                                    && this.dataContracts.get(j).getBeforeContractNo().equals(this.dataContracts.get(i).getContractNo())
                                    && this.dataContracts.get(j).getDeleteDate() != null) {
                                flag = false;
                            } else if (this.dataContracts.get(i).getDeleteDate() != null
                                    && this.dataContracts.get(j).getBeforeContractShopID() != null 
                                    && this.dataContracts.get(j).getBeforeContractShopID().equals(this.dataContracts.get(i).getShop().getShopID())
                                    && this.dataContracts.get(j).getBeforeContractNo() != null
                                    && this.dataContracts.get(j).getBeforeContractNo().equals(this.dataContracts.get(i).getContractNo())
                                    && this.dataContracts.get(j).getDeleteDate() == null
                                    && (this.dataContracts.get(j).getProductNum() - this.dataContracts.get(j).getRemainingNum()) != 0) {
                                flag = false;
                            }
                        }
                    }
                    btCancel.setEnabled(flag);
                }
            }
            //IVS_LVTu end edit 2015/12/28 Bug #45225    

            if (contractCourseTable.getRowCount() > 0) {
                DefaultTableModel dtm = (DefaultTableModel) contractCourseTable.getModel();
                dtm.removeRow(contractCourseTable.getRowCount() - 1);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

    }
    
    private JLabel getLable() {
        JLabel label = new JLabel(" ");
        label.setEnabled(false);
        label.setOpaque(false);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // IVS SANG START INSERT 201301102 [gb�\�[�X]�\�[�X�}�[�W
    //An start add 20130227 �ڋq���
    private JButton getChangeButton(final Integer shopID,
            final Integer slipNO,
            final Integer contractNO,
            final Integer contractDetailNO,
            String strStatus) {
        JButton contractCourseButton = null;
        final String title = this.getTitle();
        final Container parent = this;
        if (strStatus.equals("������") || strStatus.equals("�ύX��") || strStatus.equals("�_��")) {
            contractCourseButton = new JButton();
            
            contractCourseButton.setBorderPainted(false);
            contractCourseButton.setContentAreaFilled(false);
            contractCourseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/images/" + SystemInfo.getSkinPackage() + "/button/common/change_off.jpg")));
            contractCourseButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/images/" + SystemInfo.getSkinPackage() + "/button/common/change_on.jpg")));
            contractCourseButton.setSize(48, 25);
            contractCourseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    DefaultTableModel model = (DefaultTableModel) contractCourseTable.getModel();
                    MstChangeLimitDate mcld = null;
                    mcld = new MstChangeLimitDate(shopID, slipNO, contractNO, contractDetailNO);
                    SwingUtil.openAnchorDialog(parentFrame, true, mcld, "�L�������E�ۏ؊����ύX", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
                    ((JDialog) mcld.getParent().getParent().getParent().getParent()).dispose();
                    mcld = null;
                    System.gc();
                    showCourseHistory();
                }
            });
          

        } else {
            contractCourseButton = new JButton();
            contractCourseButton.setBorderPainted(false);
            contractCourseButton.setContentAreaFilled(false);
            contractCourseButton.setEnabled(false);
        }
        return contractCourseButton;
    }
    //nhanvt edit start 20141007 Request #31292 
    /**
     * 
     * @param cus
     * @param dc
     * @param strStatus
     * @param sale_delete
     * @return 
     */
    private JButton getCancelButton(final MstCustomer cus, final DataContract dc, String strStatus, Date sale_delete) {

        //final DataContract dc

        JButton delButton = null;

        if (strStatus.equals("�ύX��") || strStatus.equals("�_��")) {
            
                delButton = new JButton();
 				delButton.setContentAreaFilled(false);
                delButton.setContentAreaFilled(false);
                delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/images/" + SystemInfo.getSkinPackage() + "/button/common/surrender_off.jpg")));
                delButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/images/" + SystemInfo.getSkinPackage() + "/button/common/surrender_on.jpg")));
                
                delButton.setSize(48, 25);
                String title = this.getTitle();

                delButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {

                        //showDialog ContractCourse(dc);
                        ContractCanCelPanel ccp = null;
                        try {
                            if (getBillValue(dc.getShop().getShopID(), dc.getSlipNo()) > 0) {
                                MessageDialog.showMessageDialog(new JDialog(), "���|���̂���_��̉��y�ь_��ύX�͍s���܂���B", "", JOptionPane.WARNING_MESSAGE);
                                return;
                            }


                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            // vtbphuong start change 20140617 Request #25499
                            // ccp = new ContractCanCelPanel(customerID, contractNo, contractDetailNo, ShopID);
                            ccp = new ContractCanCelPanel(cus, dc);
                            // vtbphuong start change 20140617 Request #25499
                            SwingUtil.openAnchorDialog((JFrame) null, true, ccp, "�����", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
                        } finally {
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }

                        ccp = null;
                        System.gc();
                        showCourseHistory();

                    }
                });
            
            

        }else if(strStatus.equals("���") ){
            if(sale_delete != null){
                delButton = new JButton();
                delButton.setBorderPainted(false);
                delButton.setContentAreaFilled(false);
                
                delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/images/" + SystemInfo.getSkinPackage() + "/button/common/remove_off.jpg")));
                delButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/images/" + SystemInfo.getSkinPackage() + "/button/common/remove_on.jpg")));
                
                delButton.setSize(48, 25);
                String title = this.getTitle();
                delButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {                       
                        try {
                            
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            if(cancelCourcse(dc)){
                                System.gc();
                                showCourseHistory();
                            }
                            
                        } finally {
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }

                    }
                });
            }else {
                delButton = new JButton();
                delButton.setBorderPainted(false);
                delButton.setContentAreaFilled(false);
                delButton.setEnabled(false);
            }
        }else {
            delButton = new JButton();
            delButton.setBorderPainted(false);
            delButton.setContentAreaFilled(false);
            delButton.setEnabled(false);
        }

        return delButton;
    }
    //nhanvt edit end 20141007 Request #31292 
    
    //nhanvt add start 20141007 Request #31292 
    public boolean cancelCourcse(DataContract dc){
        if(MessageDialog.showYesNoDialog(this,
							"���̎�����s���܂��B\n��낵���ł����H",
							this.getTitle(),
							JOptionPane.QUESTION_MESSAGE) != 0)
        {
                return false;
        }
        ConnectionWrapper	con	=	SystemInfo.getConnection();
        boolean result = false;
        //IVS_LVTu start edit 2015/12/28 Bug #45225
        Integer contractShopID = null;
        Integer slipNo     = null;
        try
        {
                con.begin();
                //find children 
                ResultSetWrapper rs = con.executeQuery(getSelectDataContractSQL(dc));
                while (rs.next()) {
                    contractShopID = rs.getInt("shop_id");
                    slipNo = rs.getInt("slip_no");
                }
                //delete all children 
                if (contractShopID != null && slipNo != null) {
                    if (deleteContractData(dc.getShop().getShopID(), dc.getContractNo(), con)){
                        if(deleteAll(con, contractShopID,slipNo)) {
                            result = true; 
                        }
                    }
                    //update contract_status
                    if(isExists(dc, con)) {
                        getUpdateDataContractStatusSQL(dc, con);
                    }
                }else if(deleteAll(con, dc.getShop().getShopID(),getSlipNoCancel(dc,con))){
                    result = true;
                }else{
                    result = false;
                }              
                 
               if(result){
                   if(con.executeUpdate(this.getReUpdateDataContractLogicallySQL(dc)) >=0){
                    con.commit();
                   }else{
                         con.rollback();
                   }   
               }else{
                   con.rollback();
               }
             
        }
        //IVS_LVTu end edit 2015/12/28 Bug #45225
        catch(SQLException e)
        {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                
            }
            
        }
        return true;
        
    }
    
    //nhanvt add start 20141007 Request #31292 
   public String getReUpdateDataContractLogicallySQL(DataContract dc)
   {
       StringBuilder sql = new StringBuilder();
       sql.append(	"update data_contract\n" );
       sql.append(        "set delete_date = null " );
       sql.append(        "where shop_id = " + SQLUtil.convertForSQL(dc.getShop().getShopID()) + "\n" );
       sql.append(        "and slip_no = " + SQLUtil.convertForSQL(dc.getSlipNo()) + "\n" );
       sql.append(        "and contract_no = " + SQLUtil.convertForSQL(dc.getContractNo()) + "\n" );
       return sql.toString();

   }
        //nhanvt add end 20141007 Request #31292 
    
    private Integer getSlipNoCancel(DataContract dc, ConnectionWrapper con){
        StringBuilder sql = new StringBuilder(200);
        
        sql.append("SELECT ds.slip_no ");
        sql.append(" FROM data_sales ds ");
        sql.append(" inner join data_sales_detail dsd ");
        sql.append(" on ds.slip_no = dsd.slip_no and ds.shop_id = dsd.shop_id and dsd.contract_no = " + dc.getContractNo() + " and dsd.contract_detail_no = " + dc.getContractDetailNo() +" and dsd.product_id = " + dc.getProduct().getProductID() + " and dsd.product_division in (7, 8 ) and dsd.delete_date is null ");
        sql.append("WHERE ds.delete_date is null");
        
        Integer slip_no = 0;
        try{
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            while (rs.next()) {
                slip_no = rs.getInt("slip_no");
            }
        }catch(SQLException ex){
            
        }
        return slip_no;
    }
    
    //IVS_LVTu start add 2015/12/28 Bug #45225
    public boolean isExists(DataContract dc, ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getExistsDataContractSQL(dc));

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean deleteContractData(Integer beforeContractShopID, Integer beforeContractNO, ConnectionWrapper con){
        try{
            if (beforeContractShopID != null && beforeContractNO != null) {
                return 0 <= con.executeUpdate(getDeleteDataContractSQL(beforeContractShopID, beforeContractNO));
            }
            
        }catch(SQLException ex){
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return false;
    } 
    public String getSelectDataContractSQL(DataContract dc)
    {
        StringBuilder sql = new StringBuilder();
        sql.append(	"SELECT shop_id, slip_no FROM data_contract\n" );
        sql.append(        "where before_contract_shop_id = " + SQLUtil.convertForSQL(dc.getShop().getShopID()) + "\n" );
        sql.append(        "and before_contract_no = " + SQLUtil.convertForSQL(dc.getContractNo()) + "\n" );
        sql.append(        "limit 1 ");

        return sql.toString();

    }
    
    public String getDeleteDataContractSQL(Integer beforeContractShopID,Integer beforeContractNO)
    {
        StringBuilder sql = new StringBuilder();
        sql.append(	"DELETE FROM data_contract\n" );
        sql.append(        "where before_contract_shop_id = " + SQLUtil.convertForSQL(beforeContractShopID) + "\n" );
        sql.append(        "and before_contract_no = " + SQLUtil.convertForSQL(beforeContractNO) + "\n" );
        return sql.toString();

    }
    
    public String getExistsDataContractSQL(DataContract dc)
    {
        StringBuilder sql = new StringBuilder();
        sql.append(	"SELECT shop_id, slip_no FROM data_contract\n" );
        sql.append(        "where shop_id = " + SQLUtil.convertForSQL(dc.getShop().getShopID()) + "\n" );
        sql.append(        "and contract_no = " + SQLUtil.convertForSQL(dc.getContractNo()) + "\n" );
        sql.append(        "limit 1 ");

        return sql.toString();

    }
    
    public boolean getUpdateDataContractStatusSQL(DataContract dc, ConnectionWrapper con) throws SQLException
   {
       StringBuilder sql = new StringBuilder();
       sql.append(	"update data_contract\n" );
       sql.append(        "set contract_status = 2 " );
       sql.append(        "where shop_id = " + SQLUtil.convertForSQL(dc.getShop().getShopID()) + "\n" );
       sql.append(        "and contract_no = " + SQLUtil.convertForSQL(dc.getContractNo()) + "\n" );
       
        return 0 <= con.executeUpdate(sql.toString());
   }
    //IVS_LVTu end add 2015/12/28 Bug #45225
    
    /**
	 * ����w�b�_�f�[�^�A�`�[���׃f�[�^�A�x���f�[�^�A�x�����׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deleteAll(ConnectionWrapper con, Integer shopId, Integer slipNo ) throws SQLException
	{
		//���㖾�ׁA�x���A�x������
		if(!this.deleteAllChildren(con,shopId,slipNo ))
				return	false;
		//����w�b�_
		if(!this.delete(con,shopId,slipNo))
				return	false;
		
		return	true;
	}
        
        /**
	 * �`�[�w�b�_�f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con,  Integer shopId, Integer slipNo) throws SQLException
	{
		boolean	result	=	false;
		
		result	=	(con.executeUpdate(this.getDeleteSQL(shopId,slipNo)) == 1);
		
		return	result;
	}
    
    /**
	 * �`�[���׃f�[�^�A�x���f�[�^�A�x�����׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deleteAllChildren(ConnectionWrapper con, Integer shopId, Integer slipNo) throws SQLException
	{
		if(!this.deleteDetail(con, shopId,slipNo))
				return	false;
		
		if(!this.deletePayment(con, shopId, slipNo))
				return	false;
		
		if(!this.deletePaymentDetail(con, shopId,  slipNo))
				return	false;
		
		return	true;
	}
    
    /**
	 * �`�[���׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deleteDetail(ConnectionWrapper con, Integer shopId, Integer slipNo) throws SQLException
	{
		con.execute(this.getDeleteDetailSQL(shopId, slipNo));
		
		return	true;
	}
	
	
	/**
	 * �x���f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deletePayment(ConnectionWrapper con,  Integer shopId, Integer slipNo) throws SQLException
	{
		con.execute(this.getDeletePaymentSQL(shopId,slipNo));
		
		return	true;
	}
	
	
	/**
	 * �x�����׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deletePaymentDetail(ConnectionWrapper con,  Integer shopId, Integer slipNo) throws SQLException
	{
		con.execute(this.getDeletePaymentDetailSQL(shopId, slipNo));
		
		return	true;
	}
    
    /**
	 * ����w�b�_�f�[�^���폜����r�p�k�����擾����B
	 * @return ����w�b�_�f�[�^���폜����r�p�k��
	 */
	public String getDeleteSQL(Integer shopId,Integer slipNo)
	{
		return	"update data_sales\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(shopId) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
	}
	
	/**
	 * �`�[���׃f�[�^��_���폜����r�p�k�����擾����B
	 * @return �`�[���׃f�[�^��_���폜����r�p�k��
	 */
	public String getDeleteDetailSQL(Integer shopId,Integer slipNo)
	{
		return	"update data_sales_detail\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(shopId) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
	}
	
	/**
	 * �x���f�[�^��_���폜����r�p�k�����擾����B
	 * @return �x���f�[�^��_���폜����r�p�k��
	 */
	public String getDeletePaymentSQL(Integer shopId,Integer slipNo)
	{
		return	"update data_payment\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + shopId + "\n" +
				"and slip_no = " + slipNo + "\n" +
				"and payment_no >= 0\n";
	}
	
	/**
	 * �x�����׃f�[�^��_���폜����r�p�k�����擾����B
	 * @return �x�����׃f�[�^��_���폜����r�p�k��
	 */
	public String getDeletePaymentDetailSQL(Integer shopId,Integer slipNo)
	{
		return	"update data_payment_detail\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + shopId + "\n" +
				"and slip_no = " + slipNo + "\n";
	}
    
    
    
    //nhanvt add end 20141007 Request #31292 

    private int getBillValue(int shopId, int slipNo) {

        String statementSQL = "select dp.bill_value from data_payment dp \n"
                + "where dp.delete_date is null\n"
                + "and dp.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                + "and dp.slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n"
                + "and dp.payment_no = (select max(payment_no) from data_payment where delete_date is null" + "\n"
                + "and shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(slipNo) + ")\n";
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(statementSQL);

            if (rs.next()) {
                int a = rs.getInt("bill_value");
                rs.close();
                return a;
            }
            rs.close();
        } catch (SQLException ex) {
        }
        return 0;
    }

    private JButton getDeadlineButton(final Integer customerID, final Integer SlipNo, final Integer contractNo, final Integer contractDetailNo, final Integer ShopID, String strStatus) {
        JButton searchButton = null;
        if (strStatus.equals("�ύX��") || strStatus.equals("�_��")) {

            searchButton = new JButton();
            searchButton.setBorderPainted(false);
            searchButton.setContentAreaFilled(false);
            searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/images/" + SystemInfo.getSkinPackage() + "/button/common/change_off.jpg")));
            searchButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/images/" + SystemInfo.getSkinPackage() + "/button/common/change_on.jpg")));
            searchButton.setSize(48, 25);
            searchButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ContractChangePanel ccp = null;

                    try {

                        if (getBillValue(ShopID, SlipNo) > 0) {
                            MessageDialog.showMessageDialog(new JDialog(), "���|���̂���_��̉��y�ь_��ύX�͍s���܂���B", "", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        ccp = new ContractChangePanel(customerID, contractNo, contractDetailNo, ShopID);
                        SwingUtil.openAnchorDialog((JFrame) null, true, ccp, "�_��ύX����", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);

                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                    ccp = null;
                    System.gc();
                    showCourseHistory();
                }
            });
            //IVS_LVTu start add 2015/10/06 New request #43159
            if ( SystemInfo.getCurrentShop().getShopID() == 0 ) {
                searchButton.setEnabled(false);
                
            }
            //IVS_LVTu end add 2015/10/06 New request #43159
        }else {
            searchButton = new JButton();
            searchButton.setBorderPainted(false);
            searchButton.setContentAreaFilled(false);
            searchButton.setEnabled(false);
        }
        
        return searchButton;

    }
    
    /**
     * 2014-09-15 added by ennyu
     * �R�[�X�_��Ƌ����w���҂̕R�t
     * @return button
     */
    private JButton getJointlyUserButton() {
        JButton searchButton = null;
        searchButton = new JButton();
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_off.jpg")));
        searchButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_on.jpg")));
        searchButton.setSize(48, 25);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	//searchCusButtonActionPerformed(evt);
            	searchJointlyUserButtonActionPerformed(evt);
            }
        });
        return searchButton;
    }
    
    
    // IVS SANG END INSERT 201301102 [gb�\�[�X]�\�[�X�}�[�W

    private void addBlankCourseHistoryTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        JTextPane sep = new JTextPane();
        sep.setBackground(new Color(150, 150, 150));
        sep.setFont(new Font(null, 0, 0));

        model.addRow(new Object[]{
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep
                });

        table.setRowHeight(model.getRowCount() - 1, 3);
    }

    /**
     * �R�[�X������z��
     */
    private void showConsumptionCourseHistory() {
        //Object selectedCell = contractCourseTable.getValueAt(contractCourseTable.getSelectedRow(), 1);
        if (contractCourseTable.getSelectedRow() >= 0 && (contractCourseTable.getSelectedRow() % 2) == 0) {
            int index = contractCourseTable.getSelectedRow() / 2;
            this.courseTableSelectedIndex = index;
            Object selectedCell = dataContracts.get(index);
            if (selectedCell instanceof JTextPane) {
                return;
            }

            DefaultTableModel model = (DefaultTableModel) consumptionCourseTable.getModel();
            //�S�s�폜
            SwingUtil.clearTable(consumptionCourseTable);
            
            //�I�����ꂽ�R�[�X�_�񗚗����擾
            DataContract dc = (DataContract) selectedCell;
            
            try {
                if (!dc.loadDataContactDigestion(SystemInfo.getConnection())) {
                    return;
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            NumberFormat nf = NumberFormat.getInstance();
            //�e�[�u���ɏ��i��ǉ�
            for (DataContractDigestion dcd : dc) {
                long unitPrice = new BigDecimal(dc.getProductValue()).divide(new BigDecimal(dc.getProductNum()), 0, RoundingMode.UP).longValue();
                Object[] rowData = {
                    //                    String.format("%1$tY/%1$tm/%1$td", dcd.getInsertDate()),

                    dcd.getSalesDate() == null ? "" : String.format("%1$tY/%1$tm/%1$td", dcd.getSalesDate()),
                    //  String.format("%1$tY/%1$tm/%1$td", dcd.getSalesDate()),
                    dc.getProduct().getProductName(),
                    nf.format(new BigDecimal(unitPrice).multiply(new BigDecimal(dcd.getProductNum().doubleValue())).setScale(0, RoundingMode.UP).doubleValue()),
                    nf.format(dcd.getProductNum().doubleValue()),
                    dcd.getStaff().getStaffName(0) + " " + dcd.getStaff().getStaffName(1),
                    dcd.getCustomer().getCustomerName(0) + " " + dcd.getCustomer().getCustomerName(1)
                };
                model.addRow(rowData);
            }
            
            //�������p�Ҏ擾
            this.showJointlyUserHistory(dc);
        }
    }
    
    /**
     * �������p�Ҏ擾
     */
    private void showJointlyUserHistory(DataContract dc){
        String strStatus = "";
        Calendar calValidDate = Calendar.getInstance();
        Calendar calDate = Calendar.getInstance();
        if (dc.getDeleteDate() != null || dc.getContractStatus() == 1) {
            strStatus = "���";
        } else if (dc.getContractStatus() == 2) {
            strStatus = "�ύX��";
        } else {
            strStatus = "�_��";
            if (dc.getValidDate() != null) {
                calValidDate.setTime(dc.getValidDate());
                if (calValidDate.before(calDate) == true) {
                    strStatus = "������";
                }
            }
        }
        if(strStatus.equals("������") || strStatus.equals("���") || dc.getRemainingNum() == 0.0){
        	jointlyUserAddButton.setEnabled(false);
        } else {
        	jointlyUserAddButton.setEnabled(true);
        }
        DefaultTableModel model = (DefaultTableModel) jointlyUserTable.getModel();
        //�S�s�폜
        SwingUtil.clearTable(jointlyUserTable);
        
        java.util.List<DataContractShare> dataContractShareList = null;
        try {
			dataContractShareList = DataContractShare.getDataContractShareList(SystemInfo.getConnection(), dc.getShop().getShopID(),dc.getSlipNo(),dc.getContractNo(),dc.getContractDetailNo());
		} catch (SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
        NumberFormat nf = NumberFormat.getInstance();
        if (dataContractShareList != null) {
			for (DataContractShare dcs : dataContractShareList) {
	            Object[] rowData = {
	                dcs.getCustomer().getCustomerNo(),
	                dcs.getCustomer().getFullCustomerName(),
	                (dcs.getSalesDate() == null) ? "" : String.format("%1$tY/%1$tm/%1$td", dcs.getSalesDate()),
	                //IVS_LVTu start delete 2015/09/21 Bug #42778
                        //nf.format(dcs.getProductNum().doubleValue()),
                        //IVS_LVTu end delete 2015/09/21 Bug #42778
	                getUserSearchButton(dcs.getCustomer().getCustomerID()),
	                getJointlyUserDeleteButton(dcs.getCustomer().getCustomerID(),dc)
	            };
	            model.addRow(rowData);
	        }
        }
    }
    
    /**
     * �������p�ҍ폜�{�^�����擾����
     */
    private JButton getJointlyUserDeleteButton(final Integer customerID,final DataContract dc) {
        JButton deleteButton = new JButton();
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
        deleteButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
        deleteButton.setSize(48, 25);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try {
					DataContractShare.delete(SystemInfo.getConnection(),dc.getShop().getShopID(),customerID,dc.getContractNo(),dc.getContractDetailNo());
					DefaultTableModel model = (DefaultTableModel) jointlyUserTable.getModel();
					int row = jointlyUserTable.getSelectedRow();
					if (jointlyUserTable.getCellEditor() != null) {
						jointlyUserTable.getCellEditor().stopCellEditing();
					}
					model.removeRow(row);
				} catch (SQLException e) {
					SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
            }
        });
        return deleteButton;
    }
    

    private void showGenzaiPoint() {
        // �|�C���g�g�p���̓|�C���g��\��
        if (SystemInfo.isUsePointcard()) {
            // ���݃|�C���g��\������
            Long NowPoint = PointData.getNowPoint(customer.getCustomerID());
            if (NowPoint == null) {
                genzaiPoint.setText("");
            } else {
                genzaiPoint.setText(String.valueOf(NowPoint));
            }
        }
    }

    /**
     * �{�^����Enabled��ݒ肷��B
     *
     * @param enabled �{�^����Enabled
     */
    private void changeButtonEnabled(boolean enabled) {
        renewButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    public void setNotMemberButtonEnabled() {
        prevButton.setVisible(false);
        nextButton.setVisible(false);
        searchCusButton.setVisible(false);
        clearButton.setVisible(false);
        addButton.setVisible(false);

        if (SystemInfo.getCurrentShop().getAutoNumber() == 1) {

            showButton.setName("1");
            showButton.setIcon(SystemInfo.getImageIcon("/button/common/auto_number_off.jpg"));
            showButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/auto_number_on.jpg"));

        } else {

            showButton.setVisible(false);
        }
    }

    /**
     * �I������Ă��郁�[���z�M�ۂ��Z�b�g����B
     */
    private void setSelectedSendMail() {
        sendMailOK.setSelected(false);
        sendMailNG.setSelected(false);

        //�I������Ă��Ȃ��ꍇ
        if (customer.getSendMail() == null) {
            sendMailOK.setSelected(true);
        } else if (customer.getSendMail() == 0) {
            sendMailNG.setSelected(true);
        } else if (customer.getSendMail() == 1) {
            sendMailOK.setSelected(true);
        }
    }

    /**
     * �I������Ă���DM�z�M�ۂ��Z�b�g����B
     */
    private void setSelectedSendDm() {
        sendDmOK.setSelected(false);
        sendDmNG.setSelected(false);

        //�I������Ă��Ȃ��ꍇ
        if (customer.getSendDm() == null) {
            sendDmOK.setSelected(true);
        } else if (customer.getSendDm() == 0) {
            sendDmNG.setSelected(true);
        } else if (customer.getSendDm() == 1) {
            sendDmOK.setSelected(true);
        }
    }

    /**
     * �I������Ă���d�b�A���ۂ��Z�b�g����B
     */
    private void setSelectedCallFlag() {
        callFlagOK.setSelected(false);
        callFlagNG.setSelected(false);

        //�I������Ă��Ȃ��ꍇ
        if (customer.getCallFlag() == null) {
            callFlagOK.setSelected(true);
        } else if (customer.getCallFlag() == 0) {
            callFlagNG.setSelected(true);
        } else if (customer.getCallFlag() == 1) {
            callFlagOK.setSelected(true);
        }
    }

    /**
     * �I������Ă��鐫�ʂ��Z�b�g����B
     */
    private void setSelectedSex() {
        male.setSelected(false);
        female.setSelected(false);

        //�I������Ă��Ȃ��ꍇ
        if (customer.getSex() == null) {
//			return;
            female.setSelected(true);
        } //�j
        else if (customer.getSex() == 1) {
            male.setSelected(true);
        } //��
        else if (customer.getSex() == 2) {
            female.setSelected(true);
        }
    }

    /**
     * �a�������Z�b�g����B
     */
    private void setBirthday() {
        yearUnit.setSelectedIndex(4);
        //�N
        birthYear.setText((customer.getBirthYear() == null || customer.getBirthYear().intValue() == 1
                ? "" : customer.getBirthYear().toString()));
        birthYear.getText();
        //��
        if (customer.getBirthMonth() == null) {
            birthMonth.setSelectedIndex(0);
        } else {
            birthMonth.setSelectedItem(customer.getBirthMonth());
        }
        //��
        if (customer.getBirthMonth() == null) {
            birthDay.setSelectedIndex(0);
        } else {
            birthDay.setSelectedItem(customer.getBirthDay());
        }
    }

    /**
     * �N����Z�b�g����B
     */
    private void setAge() {
        //start edit TMTrong on 20150703 Bug #39342
        Integer ageTemp = DateUtil.calcAge(
                new com.ibm.icu.util.GregorianCalendar(),
                this.getBirthday());
        //end edit TMTrong on 20150703 Bug #39342
        //IVS_TMTrong start edit 2015/10/19 New request #43511
        //IVS_TMTrong start edit 2015/10/21 New request #43511
        age.setText("");
        //IVS_TMTrong end edit 2015/10/21 New request #43511
        if(ageTemp>0){
            age.setText((ageTemp == null || birthYear.getText().equals("") ? "" : ageTemp.toString()));
        }
        //IVS_TMTrong end edit 2015/10/19 New request #43511
    }

    /**
     * ���񗈓X�����Z�b�g����B
     */
    private void setFirstVisitDate() {
        if (customer.getCustomerID() != null && customer.getFirstVisitDate() == null) {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     min(sales_date) as sales_date");
            sql.append(" from");
            sql.append("     view_data_sales_valid");
            sql.append(" where");
            sql.append("         sales_date is not null");
            sql.append("     and customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));

            try {
                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                if (rs.next()) {
                    customer.setFirstVisitDate(rs.getDate("sales_date"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //�N
        firstVisitYear.setText((customer.getFirstVisitYear() == null || customer.getFirstVisitYear().intValue() == 1
                ? "" : customer.getFirstVisitYear().toString()));
        firstVisitYear.getText();
        //��
        if (customer.getFirstVisitMonth() == null) {
            firstVisitMonth.setSelectedIndex(0);
        } else {
            firstVisitMonth.setSelectedItem(customer.getFirstVisitMonth());
        }
        //��
        if (customer.getFirstVisitMonth() == null) {
            firstVisitDay.setSelectedIndex(0);
        } else {
            firstVisitDay.setSelectedItem(customer.getFirstVisitDay());
        }
    }

    /**
     * �I������Ă���E�Ƃ��Z�b�g����B
     */
    private void setSelectedJob() {
        //�E�Ƃ��ݒ肳��Ă��Ȃ��ꍇ
        if (customer.getJob() == null
                || customer.getJob().getJobID() == null
                || customer.getJob().getJobID() <= 0) {
            job.setSelectedIndex(-1);
            return;
        }

        for (MstJob mj : jobList) {
            if (mj != null && customer != null && customer.getJob() != null
                    && mj.getJobID() == customer.getJobID()) {
                job.setSelectedItem(mj);
            }
        }
    }

    /**
     * �I������Ă��鏉�񗈓X���@���Z�b�g����B
     */
    private void setSelectedFirstComingMotive() {
        //���񗈓X���@���ݒ肳��Ă��Ȃ��ꍇ
        if (customer.getFirstComingMotive() == null
                || customer.getFirstComingMotive().getFirstComingMotiveClassId() == null
                || customer.getFirstComingMotive().getFirstComingMotiveClassId() <= 0) {
            firstComingMotive.setSelectedIndex(-1);
            return;
        }

        for (MstFirstComingMotive mfcm : firstComingMotiveList) {
            if (mfcm != null && customer != null && customer.getFirstComingMotive() != null
                    //IVS_LVTu start edit 2015/03/19 Bug #35657
                    //&& mfcm.getFirstComingMotiveClassId() == customer.getFirstComingMotiveClassId()) {
                    && mfcm.getFirstComingMotiveClassId().equals(customer.getFirstComingMotiveClassId())) {
                    //IVS_LVTu end edit 2015/03/19 Bug #35657
                firstComingMotive.setSelectedItem(mfcm);
            }
        }
    }

    /**
     * ���́i�O�́j�ڋq����\������B
     *
     * @param isNext true - ���̌ڋq
     */
    private void moveCustomer(boolean isNext) {
        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ResultSetWrapper rs = con.executeQuery(this.getNextCustomerID(isNext));

            if (rs.next()) {
                customer.setData(new MstCustomer());
                customer.setCustomerID(rs.getInt("customer_id"));
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        this.loadCustomer();
        tabZero = false;
        tabOne = false;
        tabTwo = false;
        tabThree = false;
        
// 2017/01/08 �ڋq���� ADD START
        isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
        
        this.showData();
    }

    /**
     * ���i�O�j�̌ڋq�h�c���擾����B
     *
     * @param isNext true - ���Afalse - �O
     * @return ���i�O�j�̌ڋq�h�c
     */
    private String getNextCustomerID(boolean isNext) {
        //�ڋq���\������Ă��Ȃ��ꍇ
        if (customer == null || customer.getCustomerID() == null) {
            return "select min(customer_id) as customer_id\n"
                    + "from mst_customer\n"
                    + "where delete_date is null\n"
                    + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                    + "and customer_no = (\n"
                    + "select min(customer_no) as customer_no\n"
                    + "from mst_customer\n"
                    + "where delete_date is null\n"
                    + "and customer_no != '0')\n"
                    + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n";
        } else {
            //���̌ڋq
            if (isNext) {
                return "select coalesce((\n"
                        + "select min(customer_id)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_id > " + SQLUtil.convertForSQL(customer.getCustomerID()) + "\n"
                        + "and customer_no != '0'\n"
                        + "and customer_no = (\n"
                        + "select min(customer_no)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no != '0'\n"
                        + "and customer_no = " + SQLUtil.convertForSQL(customer.getCustomerNo()) + "\n"
                        + "and customer_id > " + SQLUtil.convertForSQL(customer.getCustomerID()) + ")), (\n"
                        + "select min(customer_id)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no != '0'\n"
                        + "and customer_no = coalesce((\n"
                        + "select min(customer_no)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no > " + SQLUtil.convertForSQL(customer.getCustomerNo()) + "\n"
                        + "and customer_no != '0'), (\n"
                        + "select min(customer_no)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no != '0'\n"
                        + ")))) as customer_id\n";
            } //�O�̌ڋq
            else {
                return "select coalesce((\n"
                        + "select max(customer_id)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_id < " + SQLUtil.convertForSQL(customer.getCustomerID()) + "\n"
                        + "and customer_no != '0'\n"
                        + "and customer_no = (\n"
                        + "select max(customer_no)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no != '0'\n"
                        + "and customer_no = " + SQLUtil.convertForSQL(customer.getCustomerNo()) + "\n"
                        + "and customer_id < " + SQLUtil.convertForSQL(customer.getCustomerID()) + ")), (\n"
                        + "select max(customer_id)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no != '0'\n"
                        + "and customer_no = coalesce((\n"
                        + "select max(customer_no)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no != '0'\n"
                        + "and customer_no < " + SQLUtil.convertForSQL(customer.getCustomerNo()) + "), (\n"
                        + "select max(customer_no)\n"
                        + "from mst_customer\n"
                        + "where delete_date is null\n"
                        + "and shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()) + "\n"
                        + "and customer_no != '0'\n"
                        + ")))) as customer_id\n";
            }
        }
    }

    /**
     * ���̓`�F�b�N���s���B
     *
     * @return ������true��Ԃ��B
     */
    private boolean checkInput() {
        this.setData();

        //�ڋq�h�c
        if (customerNo.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�ڋqNo."),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            customerNo.requestFocusInWindow();
            return false;
        }

        //����
        if (customerName1.getText().equals("")
                && customerName2.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "����"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            customerName1.requestFocusInWindow();
            return false;
        }

        //�a����
        if (customer.getBirthday() != null
                && (new java.util.GregorianCalendar()).getTimeInMillis()
                < customer.getBirthday().getTimeInMillis()) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(6000),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            birthYear.requestFocusInWindow();
            return false;
        }
        

        return true;
    }

    /**
     * customer�ɓ��͂��ꂽ�f�[�^���Z�b�g����B
     */
    private void setData() {
        customer.setShop(this.getSelectedShop());
        customer.setCustomerNo(customerNo.getText());
        //����
        customer.setCustomerName(0, customerName1.getText());
        customer.setCustomerName(1, customerName2.getText());

        try {
            customer.setAlertMark(Alert.getSelectedItem().toString());
        }catch (Exception e) {

        }

        if (!this.isShowing()) {
            return;
        }

        //An end add 20130125

        // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W

        //�t���K�i
        customer.setCustomerKana(0, customerKana1.getText());
        customer.setCustomerKana(1, customerKana2.getText());
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            int staffID = -1;
            if (this.chargeStaff.getSelectedIndex() > 0) {
                staffID = ((MstStaff) this.chargeStaff.getSelectedItem()).getStaffID();
                customer.setMainStaffId(staffID);
            }
        }

        customer.setPostalCode(this.getPostalCode());
        customer.setAddress(0, address1.getText());
        customer.setAddress(1, address2.getText());
        customer.setAddress(2, address3.getText());
        customer.setAddress(3, address4.getText());
        customer.setPhoneNumber(phoneNumber.getText());
        customer.setCellularNumber(cellularNumber.getText());
        customer.setFaxNumber(faxNumber.getText());
        customer.setPCMailAddress(pcMail.getText());
        customer.setCellularMailAddress(cellularMail.getText());
        customer.setSendMail(this.getSendMail());
        customer.setSendDm(this.getSendDm());
        customer.setCallFlag(this.getCallFlag());
        customer.setSex(this.getSex());
        customer.setBirthday(this.getBirthday());

        // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
        //An start add 20130125               
        customer.setWebReservationFlag(this.getWebReservationFlag());
        customer.setSelectedCreditLock(this.getSelectedCreditLock());

        try {
            if (Reserved_buffer_time.getText() == null) {
                //  this.Reserved_buffer_time.setText("0");
                customer.setReservationBufferTime(0);

            } else {
                customer.setReservationBufferTime(Integer.parseInt(Reserved_buffer_time.getText()));
            }
        } catch (Exception e) {
            this.Reserved_buffer_time.setText("0");
        }

        customer.setQuestion_1(question_1.getText());
        customer.setQuestion_2(question_2.getText());
        // An end add 20130125
        // IVS SANG END INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W

        customer.setJob((MstJob) job.getSelectedItem());
        customer.setFirstComingMotive((MstFirstComingMotive) firstComingMotive.getSelectedItem());
        customer.setFirstComingMotiveNote(firstComingMotiveNote.getText());
        customer.setNote(note.getText());
        customer.setFirstVisitDay(this.getFirstVisitDate());

        if (!CheckUtil.isNumber(beforeVisitNum.getText())) {
            beforeVisitNum.setText("0");
        }
        customer.setBeforeVisitNum(Integer.valueOf(beforeVisitNum.getText()));
    }

    /**
     * ���͂���Ă���X�֔ԍ����擾����B
     *
     * @return ���͂���Ă���X�֔ԍ�
     */
    private String getPostalCode() {
        return this.postalCode.getText().replaceAll("[-_]", "");
    }

    /**
     * ���[���z�M�ۂ��擾����B
     *
     * @return ���[���z�M��
     */
    private Integer getSendMail() {
        if (this.sendMailOK.isSelected()) {

            return 1;

        } else if (this.sendMailNG.isSelected()) {

            return 0;

        } else {
            return null;
        }
    }

    // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W
    //An start add 20130125
    private Integer getWebReservationFlag() {
        if (this.WebOKCancel.isSelected()) {

            return 1;

        } else if (this.WebNGCancel.isSelected()) {

            return 0;

        } else {
            return null;
        }
    }

    private Integer getSelectedCreditLock() {
        if (this.credit_lock_chk.isSelected()) {
            return 1;

        } else {
            return 0;
        }
    }
    // An end add 20130125
    // IVS SANG START INSERT 20131103 [gb�\�[�X]�\�[�X�}�[�W

    /**
     * DM�z�M�ۂ��擾����B
     *
     * @return DM�z�M��
     */
    private Integer getSendDm() {
        if (this.sendDmOK.isSelected()) {

            return 1;

        } else if (this.sendDmNG.isSelected()) {

            return 0;

        } else {
            return null;
        }
    }

    /**
     * �d�b�A���ۂ��擾����B
     *
     * @return �d�b�A����
     */
    private Integer getCallFlag() {
        if (this.callFlagOK.isSelected()) {

            return 1;

        } else if (this.callFlagNG.isSelected()) {

            return 0;

        } else {
            return null;
        }
    }

    /**
     * ���ʂ��擾����B
     *
     * @return ����
     */
    private Integer getSex() {
        //�j��
        if (this.male.isSelected()) {
            return 1;
        } //����
        else if (this.female.isSelected()) {
            return 2;
        } else {
            return null;
        }
    }

    /**
     * �a�������擾����B
     *
     * @return �a����
     */
    private JapaneseCalendar getBirthday() {
        String year = birthYear.getText();
        String month = (this.birthMonth.getSelectedItem() == null
                ? "" : this.birthMonth.getSelectedItem().toString());
        String day = (this.birthDay.getSelectedItem() == null
                ? "" : this.birthDay.getSelectedItem().toString());

        //���͂���Ă��Ȃ����ڂ�����ꍇ
        if (!((CheckUtil.isNumeric(year) || year.equals(""))
                && CheckUtil.isNumeric(month)
                && CheckUtil.isNumeric(day))) {
            return null;
        }

        JapaneseCalendar temp = new JapaneseCalendar();
        temp.setLenient(true);

        switch (yearUnit.getSelectedIndex()) {
            //����
            case 0:
                temp.set(JapaneseCalendar.ERA, JapaneseCalendar.MEIJI);
                break;
            //�吳
            case 1:
                temp.set(JapaneseCalendar.ERA, JapaneseCalendar.TAISHO);
                break;
            //���a
            case 2:
                temp.set(JapaneseCalendar.ERA, JapaneseCalendar.SHOWA);
                break;
            //����
            case 3:
                temp.set(JapaneseCalendar.ERA, JapaneseCalendar.HEISEI);
                break;
            //����
            case 4:
                temp.set(JapaneseCalendar.ERA, JapaneseCalendar.AD);
                break;
        }

        //����I������Ă���ꍇ
        if (yearUnit.getSelectedIndex() == 4 || year.equals("")) {
            if (year.equals("")) {
                year = "1";
            }
            temp.set(JapaneseCalendar.EXTENDED_YEAR, Integer.parseInt(year));
        } else {
            temp.set(JapaneseCalendar.YEAR, Integer.parseInt(year));
        }

        //���������t�����`�F�b�N����
        if (CheckUtil.isDate(temp.get(JapaneseCalendar.EXTENDED_YEAR),
                Integer.parseInt(month) + 1,
                Integer.parseInt(day))) {
            temp.set(JapaneseCalendar.MONTH, Integer.parseInt(month) - 1);
            temp.set(JapaneseCalendar.DAY_OF_MONTH, Integer.parseInt(day));
        } else {
            return null;
        }

        return temp;
    }

    /**
     * ���񗈓X�����擾����B
     *
     * @return ���񗈓X��
     */
    private Calendar getFirstVisitDate() {
        String year = firstVisitYear.getText();
        String month = (this.firstVisitMonth.getSelectedItem() == null ? "" : this.firstVisitMonth.getSelectedItem().toString());
        String day = (this.firstVisitDay.getSelectedItem() == null ? "" : this.firstVisitDay.getSelectedItem().toString());

        //���͂���Ă��Ȃ����ڂ�����ꍇ
        if (!(CheckUtil.isNumeric(year) && CheckUtil.isNumeric(month) && CheckUtil.isNumeric(day))) {
            return null;
        }

        //���������t�����`�F�b�N����
        if (!CheckUtil.isDate(
                Integer.parseInt(year),
                Integer.parseInt(month) + 1,
                Integer.parseInt(day))) {
            return null;
        }

        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.YEAR, Integer.parseInt(year));
        temp.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        temp.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

        return temp;
    }

    /**
     * �f�[�^��o�^����B
     */
    private void registData(boolean isAdd) {
        //���̓`�F�b�N
        if (!this.checkInput()) {
            return;
        }

        if (!this.checkCustomerID(isAdd)) {
            return;
        }

        if (!checkCustomerNo(isAdd)) {
            return;
        }

        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // ���������`�F�b�N
            if (isAdd) {
                StringBuffer msg = new StringBuffer();
                if (checkSameCustomerName(msg)) {
                    if (MessageDialog.showYesNoDialog(
                            this,
                            msg.toString(),
                            this.getTitle(),
                            JOptionPane.QUESTION_MESSAGE,
                            JOptionPane.NO_OPTION) == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
            }

            if (isAdd) {
                customer.setCustomerID(null);
            }

            con.begin();

            //�o�^����
            if (registCustomer(con, isAdd)) {
                con.commit();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
                // WEB����A���{�^�����X�V����
                updateSosiaGearButton();

                // �X�V
                if (!isAdd) {
                    this.setRenewed(true);
                    this.showAccounts();
                    this.showVisitedMemoList();
                    // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
                    //An start add 20130125
                    this.showCancel();

                    //An end add 20130125
                    // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
                }
            } //�o�^���s
            else {
                con.rollback();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�ڋq�f�[�^"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        if (this.getOpener() instanceof NotMemberListPanel) {
            NotMemberListPanel nmlp = (NotMemberListPanel) this.getOpener();
            nmlp.refresh();
            this.parentFrame.changeContents(nmlp);
        }

        // �|�b�v�A�b�v�\������Ă���ꍇ�͓o�^��ɉ�ʂ����
        if (isReadOnly || isCloseButtonEnable) {
            getRootPane().getParent().setVisible(false);
        }

    }

    private boolean checkSameCustomerName(StringBuffer msg) {

        boolean result = false;

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_customer");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and customer_no <> '0'");
        sql.append("     and customer_name1 = " + SQLUtil.convertForSQL(customer.getCustomerName(0)));
        sql.append("     and customer_name2 = " + SQLUtil.convertForSQL(customer.getCustomerName(1)));
        sql.append(" order by");
        sql.append("     insert_date desc");

        int cnt = 0;
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            if (rs.next()) {
                msg.append("��������v���Ă���ڋq��񂪊��ɑ��݂��܂����A\n�o�^���Ă���낵���ł����H\n\n");
                msg.append("[ �ڋqNo. ]�@" + rs.getString("customer_no") + "\n");
                msg.append("[ ���� ]\n");
                msg.append("�@�@" + rs.getString("customer_name1") + "�@" + rs.getString("customer_name2") + " �i " + replaceNullString(rs.getString("customer_kana1")) + "�@" + replaceNullString(rs.getString("customer_kana2")) + " �j\n");
                msg.append("[ �X�֔ԍ� ]\n");
                msg.append("�@�@" + replaceNullString(rs.getString("postal_code")) + "\n");
                msg.append("[ �Z�� ]\n");
                msg.append("�@�@" + replaceNullString(rs.getString("address1")) + replaceNullString(rs.getString("address2")) + "\n");
                msg.append("�@�@" + replaceNullString(rs.getString("address3")) + replaceNullString(rs.getString("address4")) + "\n");
                msg.append("[ �a���� ]\n");
                msg.append("�@�@" + (rs.getDate("birthday") == null ? "" : DateUtil.format(rs.getDate("birthday"), "yyyy�NMM��dd��")) + "\n\n");

                result = true;
            }

            while (rs.next()) {
                cnt++;
            }

            if (cnt > 0) {
                msg.append("�� " + cnt + " ��\n\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String replaceNullString(String s) {
        return s == null ? "" : s;
    }

    private boolean checkCustomerID(boolean isAdd) {
        if (isAdd) {
            try {
                if (0 < MstCustomer.getMstCustomerArrayByNo(
                        SystemInfo.getConnection(),
                        customerNo.getText(),
                        this.getSelectedShopID()).size()) {
                    if (MessageDialog.showYesNoDialog(this,
                            MessageUtil.getMessage(105, "�ڋqNo."),
                            this.getTitle(),
                            JOptionPane.QUESTION_MESSAGE,
                            JOptionPane.NO_OPTION) == JOptionPane.NO_OPTION) {
                        return false;
                    }
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        } else {
            if (MessageDialog.showYesNoDialog(this,
                    MessageUtil.getMessage(103),
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
                return false;
            }
        }

        customer.setCustomerNo(customerNo.getText());

        return true;
    }

    private boolean checkCustomerNo(boolean isAdd) {
        if (customerNo.getText().equals("0")) {
            if (MessageDialog.showYesNoDialog(this,
                    MessageUtil.getMessage((isAdd ? 6001 : 6002)),
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        return true;
    }

    /**
     * �ڋq�}�X�^�Ƀf�[�^��o�^����B
     *
     * @param con �R�l�N�V����
     * @return ������true��Ԃ��B
     */
    private boolean registCustomer(ConnectionWrapper con, boolean isAdd) {
        boolean result = true;

        try {
            result = result && this.customer.regist(con);

            //2016/08/23 GB MOD #54158 Start
            if (isAdd) {
                this.setCustmerInfoFreeHeading();
            }
            //2016/08/23 GB MOD #54158 End
            
            // �t���[���ڂ̑I����e�𔽉f
            setFreeHeadingSelectedData();
            

            result = result && this.registFreeHeading(con);
            if (isAdd) {
                this.loadCustomerFreeHeading(con);
            }
            result = result && this.registFreeHeadingText(con);
            result = result && this.registIntroduce(con);
            result = result && this.registFamily(con);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            result = false;
        }

        return result;
    }

    /**
     * �ڋq�f�[�^�̍폜���s���B
     */
    private void delete() {
        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        MstCustomer temp = new MstCustomer();
        temp.setCustomerID(customer.getCustomerID());

        try {
            if (!temp.isExists(con)) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_NOT_EXIST,
                        "�ڋqNo." + customerNo.getText()),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return;
        }

        //�m�F���b�Z�[�W
        if (MessageDialog.showYesNoDialog(this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE,
                "�ڋqNo." + customerNo.getText()),
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }

        customer.setCustomerNo(customerNo.getText());

        try {
            con.begin();

            //�폜����
            if (customer.delete(con)) {
                //�Љ�ꗗ�̌ڋq�f�[�^����Љ�ڋqID�A�Љ�ڋq�������N���A����
                for (MstCustomer mc : introduceList) {
                    mc.setIntroducerID(null);
                    mc.setIntroducerNote(null);
                }
                this.registIntroduce(con);

                //�R�~�b�g
                con.commit();
                this.clear();
                tabZero = false;
                tabOne = false;
                tabTwo = false;
                tabThree = false;
                
// 2017/01/08 �ڋq���� ADD START
                isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
                
                this.showData();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_DELETE_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
            } //�폜���s
            else {
                con.rollback();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void loadOption() {
        customer.loadAccounts();
        this.showAccounts();
        this.showVisitedMemoList();
        this.showPointList();
        this.showPrepaidList();
        // IVS SANG START INERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        //An start add 20130125
        this.showCancel();
        //An end add 20130125
        // IVS SANG END INERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
    }

    private void initOptionComponent() {
        this.initAccountTable();
        this.initAccountTable(techTable);
        this.initAccountTable(itemTable);
        // IVS SANG START INERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        //An start add 20130125       
        this.initAlertCombox(Alert);
        //An end add 20130125
        // IVS SANG END INERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        this.initVisitedMemoTableWidth();
        this.initPointTableWidth();
        this.initPrepaidTableWidth();
        this.initDmMailTableWidth();
        this.initVisitCycleTableWidth();
    }

    private void initAccountTable() {
        techItemTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        techItemTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        techItemTable.getColumnModel().getColumn(2).setPreferredWidth(45);
        techItemTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        techItemTable.getColumnModel().getColumn(4).setPreferredWidth(45);
        techItemTable.getColumnModel().getColumn(5).setPreferredWidth(52);
        techItemTable.getColumnModel().getColumn(6).setPreferredWidth(67);
    }

    private void initAccountTable(JTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth(65);
        table.getColumnModel().getColumn(1).setPreferredWidth(82);
        table.getColumnModel().getColumn(2).setPreferredWidth(45);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(45);
        table.getColumnModel().getColumn(5).setPreferredWidth(52);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
    }

    private void initVisitedMemoTableWidth() {
        visitedMemoTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        visitedMemoTable.getColumnModel().getColumn(1).setPreferredWidth(324);
    }

    private void initPointTableWidth() {
        pointTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        pointTable.getColumnModel().getColumn(1).setPreferredWidth(79);
        pointTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        pointTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    }

    private void initPrepaidTableWidth() {
        prepaidTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        prepaidTable.getColumnModel().getColumn(1).setPreferredWidth(79);
        prepaidTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        prepaidTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    }

    private void initDmMailTableWidth() {
        dmmailTable.getColumnModel().getColumn(2).setPreferredWidth(240);
    }

    private void initVisitCycleTableWidth() {
        visitCycleTable.getColumnModel().getColumn(0).setPreferredWidth(750);
        visitCycleTable.getColumnModel().getColumn(13).setPreferredWidth(200);
        visitCycleTable.getColumnModel().getColumn(14).setPreferredWidth(200);
    }

    private void showAccounts() {
        //nhanvt start edit
//        salesTotal.setText(FormatUtil.decimalFormat(customer.getSalesTotal()));
        salesTotal.setText(FormatUtil.decimalFormat(customer.getTechnicTotal()+ customer.getItemTotal()));
        salesTotal1.setText(FormatUtil.decimalFormat(customer.getCourseTotal()));
        salesTotal2.setText(FormatUtil.decimalFormat(customer.getComputionCourseTotal()));
        //nhanvt end edit
        technicSalesTotal.setText(FormatUtil.decimalFormat(customer.getTechnicTotal()));
        itemSalesTotal.setText(FormatUtil.decimalFormat(customer.getItemTotal()));
        technicClameSalesTotal.setText(FormatUtil.decimalFormat(customer.getTechnicClameTotal()));
        itemReturnedSalesTotal.setText(FormatUtil.decimalFormat(customer.getItemReturnedTotal()));

        DefaultTableModel model = (DefaultTableModel) account.getModel();
        if (account.getCellEditor() != null) {
            account.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(account);

        int i = 0;
        for (VisitRecord vr : customer.getAccounts()) {
            model.addRow(new Object[]{
                        vr.getShop().getShopName(),
                        String.format("%1$tY/%1$tm/%1$td", vr.getSalesDate()),
                        vr.getStaff().getFullStaffName(),
                        vr.getDesignated() ? "��" : "",
                        vr.getNextReserveDate() == null ? "" : String.format("%1$tY/%1$tm/%1$td", (vr.getNextReserveDate())),
                        vr.getSalesTotal(),
                        getKarteButton(vr.getShop().getShopID(), vr.getSlipNo())
                    });

            //�s�̍������Œ�ɂ���
            account.setRowHeightFix(i++);
        }

        // ���X�����ꗗ��1�s�ڂ�I����Ԃɂ���
        if (account.getRowCount() > 0) {
            account.addRowSelectionInterval(0, 0);
        }
        this.showSelectedAccountDetail();

        // ���X��
        customer.setVisitCount(new Long(account.getRowCount()) + customer.getBeforeVisitNum());
        visitCount.setText(FormatUtil.decimalFormat(customer.getVisitCount()));

        SwingUtil.clearTable(techTable);
        SwingUtil.clearTable(itemTable);

        if (customer.getAccounts().size() > 0) {
            VisitRecord vr = customer.getAccounts().get(0);
            vr.setMsSetting(ms);
            // �Z�p�����E���i�����̎擾
            try {
                //nhanvt start edit  20150327 Bug #35748
                if (!vr.loadCustomerAll2(SystemInfo.getConnection())) {
                    return;
                }
                //nhanvt end edit  20150327 Bug #35748
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            Integer techSlipNo = null;
            Integer itemSlipNo = null;

            for (DataSalesDetail dsd : vr) {
                if (dsd.getProductDivision().equals(1) || dsd.getProductDivision().equals(3)) {

                    // �Z�p����
                    if (techSlipNo == null) {
                        techSlipNo = dsd.getSlipNo();
                    } else {
                        if (!techSlipNo.equals(dsd.getSlipNo())) {
                            this.addBlankTechItemTable(techTable);
                            techSlipNo = dsd.getSlipNo();
                        }
                    }

                    this.addSalesDetailDataTechItemTable(techTable, dsd);

                } else if (dsd.getProductDivision().equals(2) || dsd.getProductDivision().equals(4)) {

                    // ���i����
                    if (itemSlipNo == null) {
                        itemSlipNo = dsd.getSlipNo();
                    } else {
                        if (!itemSlipNo.equals(dsd.getSlipNo())) {
                            this.addBlankTechItemTable(itemTable);
                            itemSlipNo = dsd.getSlipNo();
                        }
                    }

                    this.addSalesDetailDataTechItemTable(itemTable, dsd);
                }
            }
        }
    }

    private void showVisitedMemoList() {
        DefaultTableModel model = (DefaultTableModel) visitedMemoTable.getModel();
        if (visitedMemoTable.getCellEditor() != null) {
            visitedMemoTable.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(visitedMemoTable);
        visitedMemoTable.clearSelection();
        visitedMemo.setText("");

        int i = 0;
        for (VisitRecord vr : customer.getAccounts()) {
            model.addRow(new Object[]{
                        "\n" + String.format("%1$tY/%1$tm/%1$td", vr.getSalesDate()),
                        vr.getVisitedMemo()
                    });

            //�s�̍������Œ�ɂ���
            visitedMemoTable.setRowHeightFix(i++);
        }

        // 1�s�ڂ�I����Ԃɂ���
        if (visitedMemoTable.getRowCount() > 0) {
            visitedMemoTable.addRowSelectionInterval(0, 0);
        }
    }

    private void showPointList() {
        DefaultTableModel model = (DefaultTableModel) pointTable.getModel();
        if (pointTable.getCellEditor() != null) {
            pointTable.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(pointTable);
        pointTable.clearSelection();
        shopPoint.setText("");
        datePoint.setText("");
        usePoint.setText("");
        suppliedPoint.setText("");

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ms.*");
        sql.append("     ,ds.sales_date");
        sql.append("     ,dp.point_id");
        sql.append("     ,dp.use_point");
        sql.append("     ,dp.supplied_point");
        sql.append(" from");
        sql.append("     data_point dp");
        sql.append("         left join mst_shop ms");
        sql.append("                 on dp.shop_id = ms.shop_id");
        sql.append("         left join data_sales ds");
        sql.append("                 on dp.shop_id = ds.shop_id");
        sql.append("                and dp.slip_no = ds.slip_no");
        sql.append(" where");
        sql.append("         dp.customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
        sql.append("     and dp.delete_date is null");
        sql.append(" order by");
        sql.append("      coalesce(ds.sales_date, ds.insert_date) desc");
        sql.append("     ,dp.insert_date desc");
        sql.append("     ,dp.point_id desc");

        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                MstShop ms = new MstShop();
                ms.setData(rs);
                // point_id�̕ۑ��p�Ƃ��ăO���[�vID�̍��ڂ��g�p����
                ms.setGroupID(rs.getInt("point_id"));

                model.addRow(new Object[]{
                            ms,
                            rs.getDate("sales_date") != null ? String.format("%1$tY/%1$tm/%1$td", rs.getDate("sales_date")) : "",
                            rs.getLong("use_point"),
                            rs.getLong("supplied_point")
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1�s�ڂ�I����Ԃɂ���
        if (pointTable.getRowCount() > 0) {
            pointTable.addRowSelectionInterval(0, 0);
        }
    }

    private void showPrepaidList() {
        DefaultTableModel model = (DefaultTableModel) prepaidTable.getModel();
        if (prepaidTable.getCellEditor() != null) {
            prepaidTable.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(prepaidTable);
        prepaidTable.clearSelection();
        currentPrepaid.setText("");

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ms.*");
        sql.append("     ,ds.sales_date");
        sql.append("     ,dp.prepaid_id");
        sql.append("     ,dp.use_value");
        sql.append("     ,dp.sales_value");
        sql.append(" from");
        sql.append("     data_prepaid dp");
        sql.append("         left join mst_shop ms");
        sql.append("                 on dp.shop_id = ms.shop_id");
        sql.append("         left join data_sales ds");
        sql.append("                 on dp.shop_id = ds.shop_id");
        sql.append("                and dp.slip_no = ds.slip_no");
        sql.append(" where");
        sql.append("         dp.customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
        sql.append("     and dp.delete_date is null");
        sql.append(" order by");
        sql.append("      coalesce(ds.sales_date, ds.insert_date) desc");
        sql.append("     ,dp.insert_date desc");
        sql.append("     ,dp.prepaid_id desc");

        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                MstShop ms = new MstShop();
                ms.setData(rs);
                // prepaid_id�̕ۑ��p�Ƃ��ăO���[�vID�̍��ڂ��g�p����
                ms.setGroupID(rs.getInt("prepaid_id"));

                model.addRow(new Object[]{
                            ms,
                            rs.getDate("sales_date") != null ? String.format("%1$tY/%1$tm/%1$td", rs.getDate("sales_date")) : "",
                            rs.getLong("use_value"),
                            rs.getLong("sales_value")
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1�s�ڂ�I����Ԃɂ���
        if (prepaidTable.getRowCount() > 0) {
            prepaidTable.addRowSelectionInterval(0, 0);
        }

        if (SystemInfo.getSetteing().isUsePrepaid()) {
            // ���݃v���y�C�h��\������
            Long NowValue = PrepaidData.getNowValue(customer.getCustomerID());
            if (NowValue == null) {
                currentPrepaid.setText("");
            } else {
                currentPrepaid.setText(String.valueOf(NowValue));
            }
        }

    }

    private java.util.Date getNextReserveDate(Integer customerID, java.util.Date visitDate) {
        ConnectionWrapper con = SystemInfo.getConnection();
        java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        cal.setTime(visitDate);
        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);  // �����ȍ~�̗\�����邽��
        String sql = "select min(reservation_datetime) as reserve_date from data_reservation_detail inner join data_reservation using(reservation_no) \n"
                + "where customer_id = " + SQLUtil.convertForSQL(customerID) + "\n"
                + " and reservation_datetime >= " + SQLUtil.convertForSQL(cal.getTime());

        try {
            ResultSetWrapper rs = con.executeQuery(sql);

            if (rs.next() && rs.getDate("reserve_date") != null) {
                return rs.getDate("reserve_date");
            }
        } catch (Exception e) {
            System.err.println("SQL = " + sql);
            e.printStackTrace();
        }

        return null;
    }

    private void showSelectedAccountDetail() {
        SwingUtil.clearTable(techItemTable);
        discountName.setText("");
        discountValue.setText("0");
        SwingUtil.clearTable(payment);

        Integer index = account.getSelectedRow();

        if (0 <= index && index < customer.getAccounts().size()) {
            VisitRecord vr = customer.getAccounts().get(index);

            try {
                if (!vr.loadAll(SystemInfo.getConnection())) {
                    return;
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            long totalDiscount = vr.getDiscountTotal();

            for (DataSalesDetail dsd : vr) {
                this.addSalesDetailData(techItemTable, dsd, vr.getSalesDate());
            }

            if (vr.getDiscount() != null) {
                discountName.setText(vr.getDiscount().getProduct().getProductName());
                discountValue.setText(FormatUtil.decimalFormat(vr.getDiscount().getDiscountValue()));
            }

            DefaultTableModel model = (DefaultTableModel) payment.getModel();

            for (DataPayment dp : vr.getPayments()) {
                Vector<Object> temp = new Vector<Object>();

                temp.add(String.format("%1$tY/%1$tm/%1$td", dp.getPaymentDate()));

                long total = 0;
                //IVS_LVTu start edit 2015/12/09 Bug #44148
                //��������
                temp.add(dp.getPaymentTotal(1));
                for (int i = 2; i <= 4; i++) {
                    //temp.add(dp.getPaymentTotal(i));
                    total += dp.getPaymentTotal(i);
                }
                //�����ȊO
                temp.add(total);
                //���|��
                temp.add(dp.getBillValue());
                //total += dp.getBillValue();
                //IVS_LVTu end edit 2015/12/09 Bug #44148
                // vtbphuong start delete 20140623 Bug #25667
//
//                // ���ނ�
//                long changePrice = total - vr.getSalesTotal();
//
//                long newPrice = ((Long) temp.get(1)).longValue() - changePrice;
//                if (newPrice > 0) {
//                    newPrice -= vr.getDiscount().getDiscountValue();
//                }
//                temp.remove(1);
//                temp.add(1, newPrice);
                long changePrice = dp.getChangeValue();
                changePrice = (Long) temp.get(1) - changePrice;
                temp.remove(1);
                temp.add(1, changePrice);
                // vtbphuong end delete 20140623 Bug #25667
                model.addRow(temp);
            }
        }
    }

    private void addSalesDetailData(JTable table, DataSalesDetail dsd, java.util.Date salesDate) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        if (dsd.getProductDivision() == 5 || dsd.getProductDivision() == 7
                || dsd.getProductDivision() == 9) {
            //�R�[�X�_��̏ꍇ
            model.addRow(new Object[]{
                        dsd.getProductDivisionName(),
                        dsd.getCourse().getCourseName(),
                        SystemInfo.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                        //                            dsd.getProductNum(),
                        1, //�R�[�X�_��̏ꍇ�A1�R�[�X1�_�񂵂��ł��Ȃ��i�����R�[�X�𕡐��_�񂷂�ꍇ�͕����̖��ׂ��ł��邱�ƂɂȂ�j
                        dsd.getDiscountValue(),
                        SystemInfo.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), dsd.getTaxRate()),
                        dsd.getStaff().getFullStaffName()
                    });
        } else if (dsd.getProductDivision() == 6) {
            //�����R�[�X�̏ꍇ
            model.addRow(new Object[]{
                        dsd.getProductDivisionName(),
                        dsd.getConsumptionCourse().getCourseName(),
                        SystemInfo.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                        dsd.getConsumptionCourse().getConsumptionNum(),
                        dsd.getDiscountValue(),
                        SystemInfo.getAccountSetting().getDisplayValue(dsd.getValueForConsumption(), dsd.getDiscountValue(), dsd.getTaxRate()).longValue(),
                        dsd.getStaff().getFullStaffName()
                    });
        } else {
            //��L�ȊO�̏ꍇ
            model.addRow(new Object[]{
                        dsd.getProductDivisionName(),
                        dsd.getProduct().getProductName(),
                        SystemInfo.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                        dsd.getProductNum(),
                        dsd.getDiscountValue(),
                        SystemInfo.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), dsd.getTaxRate()),
                        dsd.getStaff().getFullStaffName()
                    });
        }
    }

    private void addSalesDetailDataTechItemTable(JTable table, DataSalesDetail dsd) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.addRow(new Object[]{
                    String.format("%1$tY/%1$tm/%1$td", dsd.getSalesDate()),
                    dsd.getProduct().getProductName(),
                    SystemInfo.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                    dsd.getProductNum(),
                    dsd.getDiscountValue(),
                    SystemInfo.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), dsd.getTaxRate()),
                    dsd.getStaff().getFullStaffName()
                });
    }

    private void addBlankTechItemTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        JTextPane sep = new JTextPane();
        sep.setBackground(new Color(150, 150, 150));
        sep.setFont(new Font(null, 0, 0));

        model.addRow(new Object[]{
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep,
                    sep
                });

        table.setRowHeight(model.getRowCount() - 1, 3);
    }

    /**
     * �ڋq���o�^��ʗpFocusTraversalPolicy
     */
    private class MstCustomerFocusTraversalPolicy
            extends FocusTraversalPolicy {

        /**
         *
         * @param focusCycleRoot
         * @param aComponent
         * @return
         */
        public Component getComponentAfter(Container focusCycleRoot,
                Component aComponent) {
            if (aComponent.equals(customerNo)) {
                return customerName1;
            } else if (aComponent.equals(customerName1)) {
                return customerName2;
            } else if (aComponent.equals(customerName2)) {
                return customerKana1;
            } else if (aComponent.equals(customerKana1)) {
                return customerKana2;
            } else if (aComponent.equals(customerKana2)) {
                return postalCode;
            } else if (aComponent.equals(postalCode)) {
                return address1;
            } else if (aComponent.equals(address1)) {
                return address2;
            } else if (aComponent.equals(address2)) {
                return address3;
            } else if (aComponent.equals(address3)) {
                return address4;
            } else if (aComponent.equals(address4)) {
                return phoneNumber;
            } else if (aComponent.equals(phoneNumber)) {
                return faxNumber;
            } else if (aComponent.equals(faxNumber)) {
                return cellularNumber;
            } else if (aComponent.equals(cellularNumber)) {
                return pcMail;
            } else if (aComponent.equals(pcMail)) {
                return cellularMail;
            } else if (aComponent.equals(cellularMail)) {
                if (sendMailOK.isSelected()) {
                    return sendMailOK;
                } else {
                    return sendMailNG;
                }
            } else if (aComponent.equals(sendMailOK)) {
                if (sendDmOK.isSelected()) {
                    return sendDmOK;
                } else {
                    return sendDmNG;
                }
            } else if (aComponent.equals(sendMailNG)) {
                if (sendDmOK.isSelected()) {
                    return sendDmOK;
                } else {
                    return sendDmNG;
                }
            } else if (aComponent.equals(sendDmOK)) {
                if (callFlagOK.isSelected()) {
                    return callFlagOK;
                } else {
                    return callFlagNG;
                }
            } else if (aComponent.equals(sendDmNG)) {
                if (callFlagOK.isSelected()) {
                    return callFlagOK;
                } else {
                    return callFlagNG;
                }
            } else if (aComponent.equals(callFlagOK)) {
                return yearUnit;
            } else if (aComponent.equals(callFlagNG)) {
                return yearUnit;
            } else if (aComponent.equals(yearUnit)) {
                return birthYear;
            } else if (aComponent.equals(birthYear)) {
                return birthMonth;
            } else if (aComponent.equals(birthMonth)) {
                return birthDay;
            } else if (aComponent.equals(birthDay)) {
                return Alert;
            } else if (aComponent.equals(Alert)) {
                if (male.isSelected()) {
                    return male;
                } else {
                    return female;
                }
            } else if (aComponent.equals(male)) {
                return job;
            } else if (aComponent.equals(female)) {
                return job;
            } else if (aComponent.equals(job)) {
                return note;
            } else if (aComponent.equals(note)) {
                return shop;
            } else if (aComponent.equals(Reserved_buffer_time)) {
                return question_1;
            } else if (aComponent.equals(question_1)) {
                return question_2;
            } else if (aComponent.equals(question_2)) {
                return firstComingMotive;
            } else if (aComponent.equals(usePoint)) {
                return suppliedPoint;
            } else if (aComponent.equals(firstVisitYear)) {
                return firstVisitMonth;
            } else if (aComponent.equals(firstVisitMonth)) {
                return firstVisitDay;
            } else if (aComponent.equals(firstComingMotive)) {
                return firstComingMotiveNote;
            } else if (aComponent.equals(firstComingMotiveNote)) {
                if (mfhsups.size() > 0) {
                    return mfhsups.get(0).getComponent(1);
                }
                if (mfTexts.size() > 0) {
                    return mfTexts.get(0).getComponent(1);
                }
            } else if (mfhsups.size() > 1) {
                for (int i = 0; i < mfhsups.size() - 1; i++) {
                    if (aComponent.equals(mfhsups.get(i).getComponent(1))) {
                        return mfhsups.get(i + 1).getComponent(1);
                    }
                }
            } else if (mfTexts.size() > 1) {
                for (int i = 0; i < mfTexts.size() - 1; i++) {
                    if (aComponent.equals(mfTexts.get(i).getComponent(1))) {
                        return mfTexts.get(i + 1).getComponent(1);
                    }
                }
            }
            if (aComponent.equals(beforeVisitNum)) {
                return firstVisitYear;
            } else if (aComponent.equals(firstVisitYear)) {
                return firstVisitMonth;
            } else if (aComponent.equals(firstVisitMonth)) {
                return firstVisitDay;
            } else if (aComponent.equals(firstVisitDay)) {
                return cmbTargetYear;
            } else if (aComponent.equals(cmbTargetYear.getComponent(2))) {
                return cmbTargetMonth;
            } else if (aComponent.equals(cmbTargetMonth)) {
                return beforeVisitNum;
            }
            return customerNo;
        }

        /**
         *
         * @param focusCycleRoot
         * @param aComponent
         * @return
         */
        public Component getComponentBefore(Container focusCycleRoot,
                Component aComponent) {
            if (aComponent.equals(customerNo)) {
                return customerNo;
            } else if (aComponent.equals(customerName1)) {
                return customerNo;
            } else if (aComponent.equals(customerName2)) {
                return customerName1;
            } else if (aComponent.equals(customerKana1)) {
                return customerName2;
            } else if (aComponent.equals(customerKana2)) {
                return customerKana1;
            } else if (aComponent.equals(postalCode)) {
                return customerKana2;
            } else if (aComponent.equals(address1)) {
                return postalCode;
            } else if (aComponent.equals(address2)) {
                return address1;
            } else if (aComponent.equals(address3)) {
                return address2;
            } else if (aComponent.equals(address4)) {
                return address3;
            } else if (aComponent.equals(phoneNumber)) {
                return address4;
            } else if (aComponent.equals(faxNumber)) {
                return phoneNumber;
            } else if (aComponent.equals(cellularNumber)) {
                return faxNumber;
            } else if (aComponent.equals(pcMail)) {
                return cellularNumber;
            } else if (aComponent.equals(cellularMail)) {
                return pcMail;
            } else if (aComponent.equals(male)) {
                return birthDay;
            } else if (aComponent.equals(female)) {
                return birthDay;
            } else if (aComponent.equals(sendMailOK)) {
                return cellularMail;
            } else if (aComponent.equals(sendMailNG)) {
                return cellularMail;
            } else if (aComponent.equals(sendDmOK)) {
                if (sendMailOK.isSelected()) {
                    return sendMailOK;
                } else {
                    return sendMailNG;
                }
            } else if (aComponent.equals(sendDmNG)) {
                if (sendMailOK.isSelected()) {
                    return sendMailOK;
                } else {
                    return sendMailNG;
                }
            } else if (aComponent.equals(callFlagOK)) {
                if (sendDmOK.isSelected()) {
                    return sendDmOK;
                } else {
                    return sendDmNG;
                }
            } else if (aComponent.equals(callFlagNG)) {
                if (sendDmOK.isSelected()) {
                    return sendDmOK;
                } else {
                    return sendDmNG;
                }
            } else if (aComponent.equals(yearUnit)) {
                if (callFlagOK.isSelected()) {
                    return callFlagOK;
                } else {
                    return callFlagNG;
                }
            } else if (aComponent.equals(birthYear)) {
                return yearUnit;
            } else if (aComponent.equals(birthMonth)) {
                return birthYear;
            } else if (aComponent.equals(birthDay)) {
                return birthMonth;
            } else if (aComponent.equals(job)) {
                if (male.isSelected()) {
                    return male;
                } else {
                    return female;
                }
            } else if (aComponent.equals(note)) {
                return job;
            } else if (aComponent.equals(shop)) {
                return note;
            } else if (aComponent.equals(Reserved_buffer_time)) {
                return shop;
            } else if (aComponent.equals(question_1)) {
                return Reserved_buffer_time;
            } else if (aComponent.equals(question_2)) {
                return question_1;
            } else if (aComponent.equals(firstVisitMonth)) {
                return firstVisitYear;
            } else if (aComponent.equals(firstVisitDay)) {
                return firstVisitMonth;
            } else if (aComponent.equals(firstComingMotiveNote)) {
                return firstComingMotive;
            }
            return customerNo;
        }

        /**
         *
         * @param focusCycleRoot
         * @return
         */
        public Component getDefaultComponent(Container focusCycleRoot) {
            return customerNo;
        }

        /**
         *
         * @param focusCycleRoot
         * @return
         */
        public Component getLastComponent(Container focusCycleRoot) {
            return question_2;
        }

        /**
         *
         * @param focusCycleRoot
         * @return
         */
        public Component getFirstComponent(Container focusCycleRoot) {
            return customerNo;
        }
    }

    /**
     * �t���[���ڃf�[�^���擾����
     */
    private void getFreeHeadingDatas() {
        int Num = 0;
        MstFreeHeadingClasses mfhcs = new MstFreeHeadingClasses();

        // �t���[���ڋ敪���擾����
        for (MstFreeHeadingClass mfhc : mfhcs) {
            // �L�����ڔ���
            if (mfhc.getUseFlg()) {
                if(mfhc.getInput_type()==0)
                {
                    MstFreeHeadingSelectUnitPanel mfhsup = new MstFreeHeadingSelectUnitPanel(mfhc);
                    mfhsups.add(mfhsup);
                    freeHeadingPanel.add(mfhsup);
                    mfhsup.setBounds(-10, (Num++ * 25) + 5, 400, 20);
                }
            
            else
            {
                MstFreeHeadingSelectTextFiled mfText = new MstFreeHeadingSelectTextFiled(mfhc);
                mfTexts.add(mfText);
                freeHeadingPanel.add(mfText);
                mfText.setBounds(-10, (Num++ * 25) + 5, 400, 20);
            }
                }
        }
    }

    /**
     * �t���[���ڃf�[�^���擾����
     */
    private void loadCustomerFreeHeading(ConnectionWrapper con) {
        mcfhs.clear();

        if (customer == null || customer.getCustomerID() == null) {
            return;
        }

        try {
            ResultSetWrapper rs = con.executeQuery(
                    this.getFreeHeadingSelectSQL());

            while (rs.next()) {
                MstCustomerFreeHeading mt = new MstCustomerFreeHeading();
                mt.setData(rs);
                mt.setFreeHeadingText(rs.getString("free_heading_text"));
                mcfhs.add(mt);
            }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }


    }
    //2016/08/23 GB MOD #54158 Start
    /**
     * �t���[���ڃf�[�^(�V�K�o�^�p)���擾����
     */
    private void loadCustomerFreeHeadingForAdd(ConnectionWrapper con) {
        mcfhs.clear();

        try {
            ResultSetWrapper rs = con.executeQuery(
                    this.getFreeHeadingForAddSelectSQL());

            while (rs.next()) {
                MstCustomerFreeHeading mt = new MstCustomerFreeHeading();
                mt.setData(rs);
                mt.setFreeHeadingText(rs.getString("free_heading_text"));
                mcfhs.add(mt);
            }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

    }
    //2016/08/23 GB MOD #54158 End

    /**
     * Select�����擾����B
     *
     * @return Select��
     */
    private String getFreeHeadingSelectSQL() {
        return "select\n"
                + "mfhc.*,\n"
                + "mc.*,\n"
                + "mfh.*,\n"
                + "coalesce( mfh.free_heading_id, -1 ),\n"
                + "mfh.free_heading_name,\n"
                + "mfh.display_seq\n"
                + " , mcfh.free_heading_text \n "
                + "from\n"
                + "mst_free_heading_class as mfhc\n"
                + "left join mst_customer_free_heading as mcfh on\n"
                + "mcfh.delete_date is null\n"
                + "and mcfh.customer_id = " + SQLUtil.convertForSQL(this.customer.getCustomerID()) + "\n"
                + "and mcfh.free_heading_class_id = mfhc.free_heading_class_id\n"
                + "left join mst_customer as mc on\n"
                + "mc.delete_date is null\n"
                + "and mc.customer_id = " + SQLUtil.convertForSQL(this.customer.getCustomerID()) + "\n"
                + "left join mst_free_heading as mfh on\n"
                + "mfh.delete_date is null\n"
                + "and mfh.free_heading_class_id = mcfh.free_heading_class_id\n"
                + "and mfh.free_heading_id = mcfh.free_heading_id\n"
                + "where\n"
                + "mfhc.delete_date is null\n"
                + "and mfhc.use_type = 't'\n"
                + "order by\n"
                + "mfhc.free_heading_class_id\n"
                + ";\n";
    }
    
    //2016/08/23 GB MOD #54158 Start
    /**
     * �t���[���ڃf�[�^(�V�K�o�^�p)�擾�pSelect�����擾����B
     *
     * @return Select��
     */
    private String getFreeHeadingForAddSelectSQL() {
        return "select\n"
                + "mfhc.*,\n"
                + "mc.*,\n"
                + "mfh.*,\n"
                + "coalesce( mfh.free_heading_id, -1 ),\n"
                + "mfh.free_heading_name,\n"
                + "mfh.display_seq\n"
                + " , mcfh.free_heading_text \n "
                + "from\n"
                + "mst_free_heading_class as mfhc\n"
                + "left join mst_customer_free_heading as mcfh on\n"
                + "mcfh.delete_date is null\n"
                + "and mcfh.customer_id IS NULL \n"
                + "and mcfh.free_heading_class_id = mfhc.free_heading_class_id\n"
                + "left join mst_customer as mc on\n"
                + "mc.delete_date is null\n"
                + "and mc.customer_id IS NULL \n"
                + "left join mst_free_heading as mfh on\n"
                + "mfh.delete_date is null\n"
                + "and mfh.free_heading_class_id = mcfh.free_heading_class_id\n"
                + "and mfh.free_heading_id = mcfh.free_heading_id\n"
                + "where\n"
                + "mfhc.delete_date is null\n"
                + "and mfhc.use_type = 't'\n"
                + "order by\n"
                + "mfhc.free_heading_class_id\n"
                + ";\n";
    }
    //2016/08/23 GB MOD #54158 End
    
    /**
     * �t���[���ڂ̑I����e�𔽉f
     */
    private void setFreeHeadingSelectedData() {
        for (MstCustomerFreeHeading cFH : mcfhs) {
            for (MstFreeHeadingSelectUnitPanel fhPanel : mfhsups) {
                if (cFH.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() == fhPanel.getFreeHeadingClassID()) {
                    cFH.setMstFreeHeading(fhPanel.getFreeHeading());
                }
            }
        }
    }

    //2016/08/19 GB MOD Start
    /**
     * �t���[���ڂɌڋq����ǉ�
     */
    private void setCustmerInfoFreeHeading() {
        for (MstCustomerFreeHeading cFH : mcfhs) {
            cFH.setMstCustomer(this.customer);
        }
    }
    //2016/08/19 GB MOD End
    
    /**
     * �t���[���ڃf�[�^��ۑ�����
     */
    private boolean registFreeHeading(ConnectionWrapper con) throws SQLException {
        for (MstCustomerFreeHeading cFH : mcfhs) {
            if (!cFH.regist(con)) {
                return false;
            }
        }
        return true;
    }

    private boolean registFreeHeadingText(ConnectionWrapper con) throws SQLException {

        for (MstFreeHeadingSelectTextFiled mfText : mfTexts) {                
                //if (!mfText.getFreeHeadingText().isEmpty()) {
                   MstCustomerFreeHeading cFH = new MstCustomerFreeHeading(); 
                   cFH.setFreeHeadingText(mfText.getFreeHeadingText());
                   MstCustomer mc= new MstCustomer();
                   mc.setCustomerID(customer.getCustomerID());
                   cFH.setMstCustomer(mc);

                   MstFreeHeadingClass mfc= new MstFreeHeadingClass();
                   mfc.setFreeHeadingClassID(mfText.getFreeHeadingClassID());                   
                   MstFreeHeading mf= new MstFreeHeading();
                   mf.setFreeHeadingClass(mfc);
                   cFH.setMstFreeHeading(mf);
                   cFH.registText(con);
             //   }
            }

        
        return true;
    }

    /**
     * �Љ�ꗗ�f�[�^��ۑ�����
     */
    private boolean registIntroduce(ConnectionWrapper con) throws SQLException {
        for (MstCustomer mc : introduceList) {
            // �������Љ�̏ꍇ�͓o�^���Ȃ�
            if (mc.getCustomerID().equals(mc.getIntroducerID())) {
                continue;
            }

            if (!mc.regist(con)) {
                return false;
            }
        }
        return true;
    }

    /**
     * �Ƒ��f�[�^��ۑ�����
     */
    private boolean registFamily(ConnectionWrapper con) throws SQLException {
        if (isModifyFamily) {

            try {
                String sql = "";

                // �Ƒ��f�[�^�폜
                sql = "delete from mst_family where customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID());
                con.executeUpdate(sql);

                // �Ƒ��f�[�^�ǉ�
                for (int row = 0; row < this.family.getRowCount(); row++) {
                    sql = "insert into mst_family(customer_id, family_id) values (";
                    sql += SQLUtil.convertForSQL(customer.getCustomerID()) + ",";
                    sql += SQLUtil.convertForSQL(((MstCustomer) this.family.getValueAt(row, 1)).getCustomerID());
                    sql += ");";

                    if (con.executeUpdate(sql) != 1) {
                        return false;
                    }
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            isModifyFamily = false;
        }

        return true;
    }

    /**
     * �d�b�ԍ��A�g�єԍ����Z�b�g������
     */
    public void setTextData(String phoneNo, String cellularNo) {
        phoneNumber.setText(phoneNo);
        cellularNumber.setText(cellularNo);
    }

    /**
     * WEB����A���_�C�A���O���J��
     */
    private void changeSosiaGear() {
        SosiaGearDialog sgd = new SosiaGearDialog(customer);
        SwingUtil.openAnchorDialog(this.parentFrame, true, sgd, "WEB����A��", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        ((JDialog) sgd.getParent().getParent().getParent().getParent()).dispose();
        sgd = null;
        tabZero = false;
        tabOne = false;
        tabTwo = false;
        tabThree = false;
        
// 2017/01/08 �ڋq���� ADD START
        isCustomerMemoTabLoaded = false;
// 2017/01/08 �ڋq���� ADD END
        
        this.showData();
    }

    /**
     * WEB����A���{�^�����X�V����
     */
    private void updateSosiaGearButton() {
        // WEB����A���s�̏ꍇ�ɂ͘A���������s��Ȃ�
        if (!SystemInfo.isSosiaGearEnable()) {
            return;
        }

        // WEB����A���ɂ��킹�ĘA���{�^����ύX����
        if (customer.getSosiaCustomer().isSosiaCustomer()) {
            // �A��ON
            changeSosiaGearButton.setIcon(SystemInfo.getImageIcon("/button/common/SOSIA_mail_off.png"));
            changeSosiaGearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/SOSIA_mail_on.png"));
        } else {
            // �A��OFF
            changeSosiaGearButton.setIcon(SystemInfo.getImageIcon("/button/common/SOSIA_regist_off.png"));
            changeSosiaGearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/SOSIA_regist_on.png"));
        }

        changeSosiaGearButton.setBorderPainted(false);
        changeSosiaGearButton.setOpaque(false);
        changeSosiaGearButton.setBackground(new Color(236, 233, 216));
        changeSosiaGearButton.setEnabled(true);
    }

    /**
     * �Љ��ǉ�����B
     */
    private void addIntoroducer(MstCustomer mc) {
        SwingUtil.clearTable(introducer);

        MstCustomerManager mcm = new MstCustomerManager();
        mcm.setData(mc);
        mcm.loadAccounts();

        String lastVisitDate = "";
        String staffName = "";
        if (mcm.getAccounts().size() > 0) {
            VisitRecord vr = mcm.getAccounts().get(0);
            lastVisitDate = String.format("%1$tY/%1$tm/%1$td", vr.getSalesDate());
            MstStaff staff = vr.getStaff();
            staffName = (staff != null ? staff.getFullStaffName() : "");
        }

        DefaultTableModel model = (DefaultTableModel) introducer.getModel();
        if (introducer.getCellEditor() != null) {
            introducer.getCellEditor().stopCellEditing();
        }
        model.setRowCount(0);
        SwingUtil.clearTable(introducer);

        model.addRow(new Object[]{
                    mc.getCustomerNo(),
                    mc.getFullCustomerName(),
                    lastVisitDate,
                    staffName,
                    getUserSearchButton(introducerID),
                    getMemoEditButton(MEMO_EDIT_INTRODUCER),
                    getDeleteButton()
                });
        this.introducerID = mc.getCustomerID();


        // �}�X�^�[�̃I�u�W�F�N�g�ɒǉ�����
        customer.setIntroducerID(mc.getCustomerID());

        // �{�^���̃��x����ύX����
        //addIntroducerButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        //addIntroducerButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));

        // �{�^��������
        addIntroducerButton.setVisible(false);

    }

    /**
     * �Ƒ��f�[�^��\������B
     */
    private void showFamily() {
        isModifyFamily = false;
        addFamilyButton.setEnabled(true);

        SwingUtil.clearTable(family);

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            Integer parentID = null;
            ResultSetWrapper rs = con.executeQuery("select * from mst_family where family_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
            if (rs.next()) {
                parentID = rs.getInt("customer_id");
            }
            rs.close();

            StringBuilder sql = new StringBuilder(1000);

            if (parentID == null) {
                sql.append(" select");
                sql.append("      mc.*");
                sql.append("     ,0 as family_main");
                sql.append(" from");
                sql.append("     mst_family mf");
                sql.append("         inner join mst_customer mc");
                sql.append("                 on mf.family_id = mc.customer_id");
                sql.append(" where");
                sql.append("         mc.delete_date is null");
                sql.append("     and mf.customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
                sql.append(" order by");
                sql.append("     mc.customer_no");

            } else {
                sql.append(" select");
                sql.append("      t.*");
                sql.append("     ,case when exists (select 1 from mst_family where customer_id = t.customer_id) then 1 else 0 end as family_main");
                sql.append(" from");
                sql.append(" (");
                sql.append(" select * from mst_customer where customer_id = " + SQLUtil.convertForSQL(parentID) + " union all ");
                sql.append(" select");
                sql.append("      mc.*");
                sql.append(" from");
                sql.append("     mst_family mf");
                sql.append("         inner join mst_customer mc");
                sql.append("                 on mf.family_id = mc.customer_id");
                sql.append(" where");
                sql.append("         mc.delete_date is null");
                sql.append("     and mf.customer_id = " + SQLUtil.convertForSQL(parentID));
                sql.append("     and mf.family_id <> " + SQLUtil.convertForSQL(customer.getCustomerID()));
                sql.append(" order by");
                sql.append("     customer_no");
                sql.append(" ) t");
            }

            rs = con.executeQuery(sql.toString());
            while (rs.next()) {
                MstCustomer mc = new MstCustomer();
                mc.setData(rs);
                addFamily(mc);
            }
            rs.close();

            if (parentID != null) {
                addFamilyButton.setEnabled(false);
                for (int i = 0; i < family.getRowCount(); i++) {
                    ((JButton) family.getValueAt(i, family.getColumnCount() - 1)).setEnabled(false);
                }
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * �Ƒ���ǉ�����B
     */
    private void addFamily(MstCustomer mc) {
        MstCustomerManager mcm = new MstCustomerManager();
        mcm.setData(mc);
        mcm.loadAccounts();

        String lastVisitDate = "";
        String staffName = "";
        if (mcm.getAccounts().size() > 0) {
            VisitRecord vr = mcm.getAccounts().get(0);
            lastVisitDate = String.format("%1$tY/%1$tm/%1$td", vr.getSalesDate());
            MstStaff staff = vr.getStaff();
            staffName = (staff != null ? staff.getFullStaffName() : "");
        }

        DefaultTableModel model = (DefaultTableModel) family.getModel();
        if (family.getCellEditor() != null) {
            family.getCellEditor().stopCellEditing();
        }

        model.addRow(new Object[]{
                    (mc.isFamilyMain() ? "�� " : "") + mc.getCustomerNo().toString(),
                    mc,
                    lastVisitDate,
                    staffName,
                    getUserSearchButton(mc.getCustomerID()),
                    getFamilyDeleteButton()
                });

    }

    /**
     * �Љ�e�[�u���̗񕝂𒲐�
     */
    private void initIntroducerColumn() {
        introducer.getColumnModel().getColumn(0).setPreferredWidth(59);     // �ڋq�ԍ�
        introducer.getColumnModel().getColumn(1).setPreferredWidth(72);     // �ڋq��
        introducer.getColumnModel().getColumn(2).setPreferredWidth(68);     // �ŏI���X��
        introducer.getColumnModel().getColumn(3).setPreferredWidth(68);     // �S����
        introducer.getColumnModel().getColumn(4).setPreferredWidth(46);     // ���[�U����
        introducer.getColumnModel().getColumn(4).setResizable(false);
        introducer.getColumnModel().getColumn(5).setPreferredWidth(46);     // �R�����g�ҏW
        introducer.getColumnModel().getColumn(5).setResizable(false);
        introducer.getColumnModel().getColumn(6).setPreferredWidth(46);     // �폜
        introducer.getColumnModel().getColumn(6).setResizable(false);
    }

    /**
     * �Љ�ꗗ�e�[�u���̗񕝂𒲐�
     */
    private void initIntroduceColumn() {
        introduce.getColumnModel().getColumn(0).setPreferredWidth(59);     // �ڋq�ԍ�
        introduce.getColumnModel().getColumn(1).setPreferredWidth(79);     // �ڋq��
        introduce.getColumnModel().getColumn(2).setPreferredWidth(80);     // �ŏI���X��
        introduce.getColumnModel().getColumn(3).setPreferredWidth(79);     // �S����
        introduce.getColumnModel().getColumn(4).setPreferredWidth(48);     // ���[�U����
        introduce.getColumnModel().getColumn(4).setResizable(false);
        introduce.getColumnModel().getColumn(5).setPreferredWidth(48);     // �R�����g�ҏW
        introduce.getColumnModel().getColumn(5).setResizable(false);
    }

    /**
     * �Ƒ��e�[�u���̗񕝂𒲐�
     */
    private void initFamilyColumn() {
        family.getColumnModel().getColumn(0).setPreferredWidth(59);     // �ڋq�ԍ�
        family.getColumnModel().getColumn(1).setPreferredWidth(79);     // �ڋq��
        family.getColumnModel().getColumn(2).setPreferredWidth(80);     // �ŏI���X��
        family.getColumnModel().getColumn(3).setPreferredWidth(79);     // �S����
        family.getColumnModel().getColumn(4).setPreferredWidth(48);     // ���[�U����
        family.getColumnModel().getColumn(4).setResizable(false);
        family.getColumnModel().getColumn(5).setPreferredWidth(48);     // �폜
        family.getColumnModel().getColumn(5).setResizable(false);
    }

    /**
     * ���X�����e�[�u���̗񕝂𒲐�
     */
    private void initAccountColumn() {
        account.getColumnModel().getColumn(0).setPreferredWidth(60);     // ���X�X��
        account.getColumnModel().getColumn(1).setPreferredWidth(70);      // ���X��
        account.getColumnModel().getColumn(2).setPreferredWidth(70);      // ��S����
        account.getColumnModel().getColumn(3).setPreferredWidth(20);      // ��S���w��
        account.getColumnModel().getColumn(4).setPreferredWidth(72);      // ����\���
        account.getColumnModel().getColumn(5).setPreferredWidth(57);      // �������z
        account.getColumnModel().getColumn(6).setPreferredWidth(43);      // �J���e
        account.getColumnModel().getColumn(6).setResizable(false);
    }

    //An start add 20130125
    private void initCancelColumn() {
        cancels.getColumnModel().getColumn(0).setPreferredWidth(80);     // ���X�X��
        cancels.getColumnModel().getColumn(1).setPreferredWidth(70);      // ���X��
        cancels.getColumnModel().getColumn(2).setPreferredWidth(70);      // ��S����

    }
    //An end add 20130125

    /**
     * �R�[�X�_�񗚗��e�[�u���̗񕝂𒲐�
     */
    private void initContractCourseColumn() {
        contractCourseTable.getColumnModel().getColumn(0).setPreferredWidth(68);     // ���t
        SelectTableCellRenderer cell0Render = new SelectTableCellRenderer();
        cell0Render.setHorizontalAlignment(SwingConstants.CENTER); //�Z���^�����O
        contractCourseTable.getColumnModel().getColumn(0).setCellRenderer(cell0Render);

        contractCourseTable.getColumnModel().getColumn(1).setPreferredWidth(130);     // ����
        contractCourseTable.getColumnModel().getColumn(2).setPreferredWidth(59);     // �_����z
        SelectTableCellRenderer cell2Render = new SelectTableCellRenderer();
        cell2Render.setHorizontalAlignment(SwingConstants.RIGHT); //�E��
        contractCourseTable.getColumnModel().getColumn(2).setCellRenderer(cell2Render);

        contractCourseTable.getColumnModel().getColumn(3).setPreferredWidth(59);     // ������
        SelectTableCellRenderer cell3Render = new SelectTableCellRenderer();
        cell3Render.setHorizontalAlignment(SwingConstants.CENTER); //�Z���^�����O
        contractCourseTable.getColumnModel().getColumn(3).setCellRenderer(cell3Render);

        contractCourseTable.getColumnModel().getColumn(4).setPreferredWidth(45);
        SelectTableCellRenderer cell4Render = new SelectTableCellRenderer();// �S����
        cell4Render.setHorizontalAlignment(SwingConstants.CENTER);
        contractCourseTable.getColumnModel().getColumn(4).setCellRenderer(cell4Render);
        contractCourseTable.getColumnModel().getColumn(5).setPreferredWidth(50);     // �S����
        contractCourseTable.getColumnModel().getColumn(6).setPreferredWidth(80);     // �S����
        contractCourseTable.getColumnModel().getColumn(7).setPreferredWidth(68);     // �S����
        contractCourseTable.getColumnModel().getColumn(8).setPreferredWidth(68);     // �S����
        contractCourseTable.getColumnModel().getColumn(9).setPreferredWidth(55);     // �S����
        contractCourseTable.getColumnModel().getColumn(10).setPreferredWidth(55);     // �S����
        contractCourseTable.getColumnModel().getColumn(11).setPreferredWidth(55);     // �S����
//        contractCourseTable.getColumnModel().getColumn(12).setPreferredWidth(55);	//���L
    }

    /**
     * �R�[�X���������e�[�u���̗񕝂𒲐�
     */
    private void initConsumptionCourseColumn() {
        consumptionCourseTable.getColumnModel().getColumn(0).setPreferredWidth(68);     // ���t
        SelectTableCellRenderer cell0Render = new SelectTableCellRenderer();
        cell0Render.setHorizontalAlignment(SwingConstants.CENTER); //�Z���^�����O
        consumptionCourseTable.getColumnModel().getColumn(0).setCellRenderer(cell0Render);

        consumptionCourseTable.getColumnModel().getColumn(1).setPreferredWidth(115);     // ����
        consumptionCourseTable.getColumnModel().getColumn(2).setPreferredWidth(59);     // �������z
        SelectTableCellRenderer cell2Render = new SelectTableCellRenderer();
        cell2Render.setHorizontalAlignment(SwingConstants.RIGHT); //�E��
        consumptionCourseTable.getColumnModel().getColumn(2).setCellRenderer(cell2Render);

        consumptionCourseTable.getColumnModel().getColumn(3).setPreferredWidth(59);     // ������
        SelectTableCellRenderer cell3Render = new SelectTableCellRenderer();
        cell3Render.setHorizontalAlignment(SwingConstants.CENTER); //�Z���^�����O
        consumptionCourseTable.getColumnModel().getColumn(3).setCellRenderer(cell3Render);

        consumptionCourseTable.getColumnModel().getColumn(4).setPreferredWidth(79);     // �S����
        
        consumptionCourseTable.getColumnModel().getColumn(5).setPreferredWidth(79);     // ���p��
    }
    
    /**
     * �������p�҃e�[�u���̗񕝂𒲐�
     */
    private void initjointlyUserColumn() {
    	jointlyUserTable.getColumnModel().getColumn(0).setPreferredWidth(55);     // �ڋqNO
        SelectTableCellRenderer cell0Render = new SelectTableCellRenderer();
        cell0Render.setHorizontalAlignment(SwingConstants.CENTER); //�Z���^�����O
        jointlyUserTable.getColumnModel().getColumn(0).setCellRenderer(cell0Render);
        jointlyUserTable.getColumnModel().getColumn(1).setPreferredWidth(80);     // �ڋq��
        jointlyUserTable.getColumnModel().getColumn(2).setPreferredWidth(70);     // �ŏI���X��
        //jointlyUserTable.getColumnModel().getColumn(3).setPreferredWidth(69);     // ������
        //SelectTableCellRenderer cell3Render = new SelectTableCellRenderer();
        //cell3Render.setHorizontalAlignment(SwingConstants.CENTER); //�Z���^�����O
        //jointlyUserTable.getColumnModel().getColumn(3).setCellRenderer(cell3Render);
        jointlyUserTable.getColumnModel().getColumn(3).setPreferredWidth(52);     // �ڍ�
        jointlyUserTable.getColumnModel().getColumn(4).setPreferredWidth(52);     // �폜
    }
    
    //IVS_LVTu start add 2015/11/23 New request #44111
    private void initProposalColumn() {
        SelectTableCellRenderer cell0Render = new SelectTableCellRenderer();
        cell0Render.setHorizontalAlignment(SwingConstants.CENTER); 
        
    	tbProposal.getColumnModel().getColumn(0).setPreferredWidth(69);  
        tbProposal.getColumnModel().getColumn(1).setPreferredWidth(67); 
        tbProposal.getColumnModel().getColumn(2).setPreferredWidth(67);
        tbProposal.getColumnModel().getColumn(3).setPreferredWidth(67);
        tbProposal.getColumnModel().getColumn(4).setPreferredWidth(67);
        tbProposal.getColumnModel().getColumn(5).setPreferredWidth(67);
        tbProposal.getColumnModel().getColumn(6).setPreferredWidth(68);
        tbProposal.getColumnModel().getColumn(7).setPreferredWidth(68);
        tbProposal.getColumnModel().getColumn(8).setPreferredWidth(60);
        tbProposal.getColumnModel().getColumn(9).setPreferredWidth(45);
        tbProposal.getColumnModel().getColumn(10).setPreferredWidth(50);
        tbProposal.getColumnModel().getColumn(11).setPreferredWidth(50);
        tbProposal.getColumnModel().getColumn(12).setPreferredWidth(50);
        
        tbProposal.getColumnModel().getColumn(8).setCellRenderer(cell0Render);
    }

    private void initProposalCourseColumn() {
        
    	tbCourseDescription.getColumnModel().getColumn(0).setPreferredWidth(80);  
        tbCourseDescription.getColumnModel().getColumn(1).setPreferredWidth(110); 
        tbCourseDescription.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbCourseDescription.getColumnModel().getColumn(3).setPreferredWidth(80);

    }
    
    private void initProposalProductColumn() {
        
    	tbProductDescription.getColumnModel().getColumn(0).setPreferredWidth(80);  
        tbProductDescription.getColumnModel().getColumn(1).setPreferredWidth(110); 
        tbProductDescription.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbProductDescription.getColumnModel().getColumn(3).setPreferredWidth(80);

    }
    //IVS_LVTu end add 2015/11/23 New request #44111
    /**
     * ���[�U�����{�^�����擾����
     */
    private JButton getUserSearchButton(final Integer customerID) {
        JButton searchButton = new JButton();
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_off.jpg")));
        searchButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_on.jpg")));
        searchButton.setSize(48, 25);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel model = (DefaultTableModel) introducer.getModel();

                MstCustomerPanel mcp = new MstCustomerPanel(customerID, true, false);

                JDialog dialog = new JDialog(parentFrame, true);
                dialog.setSize(849, 754);
                dialog.getContentPane().add(mcp);
                SwingUtil.moveCenter(dialog);

                dialog.setVisible(true);

                dialog.dispose();
            }
        });
        return searchButton;
    }

    /**
     * �R�����g�ҏW�{�^�����擾���� �敪�@�P�F�Љ�@�Q�F�Љ�ꗗ
     */
    private JButton getMemoEditButton(final String kbn) {
        JButton memoEditButton = new JButton();
        memoEditButton.setBorderPainted(false);
        memoEditButton.setContentAreaFilled(false);
        memoEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/memo_off.jpg")));
        memoEditButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/memo_on.jpg")));
        memoEditButton.setSize(48, 25);
        memoEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //�R�����g�ҏW��ʂ��Ăяo��
                MemoEditDialog med = null;
                if (MEMO_EDIT_INTRODUCER.equals(kbn)) {
                    //�Љ�̏ꍇ
                    med = new MemoEditDialog(
                            MEMO_EDIT_INTRODUCER, customer,
                            customerNo.getText(), customerName1.getText(), customerName2.getText());
                } else {
                    //�Љ�ꗗ�̏ꍇ
                    MstCustomer mc = null;
                    if (0 <= introduce.getSelectedRow()) {
                        mc = introduceList.get(introduce.getSelectedRow());
                    }
                    med = new MemoEditDialog(MEMO_EDIT_INTRODUCE, mc, null, null, null);
                }
                SwingUtil.openAnchorDialog(parentFrame, true, med, "�R�����g�ҏW", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
                ((JDialog) med.getParent().getParent().getParent().getParent()).dispose();
                med = null;
            }
        });
        return memoEditButton;
    }

    /**
     * �폜�{�^�����擾����
     */
    private JButton getDeleteButton() {
        JButton delButton = new JButton();
        delButton.setBorderPainted(false);
        delButton.setContentAreaFilled(false);
        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
        delButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
        delButton.setSize(48, 25);
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeIntroducer();
            }
        });
        return delButton;
    }

    /**
     * �Ƒ� �폜�{�^�����擾����
     */
    private JButton getFamilyDeleteButton() {
        JButton delButton = new JButton();
        delButton.setBorderPainted(false);
        delButton.setContentAreaFilled(false);
        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
        delButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
        delButton.setSize(48, 25);
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFamily();
                isModifyFamily = true;
            }
        });
        return delButton;
    }

    /**
     * �J���e�{�^�����擾����
     */
    private JButton getKarteButton(final Integer shopId, final Integer slipNo) {
        JButton karteButton = new JButton();
        karteButton.setBorderPainted(false);
        karteButton.setContentAreaFilled(false);
        karteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/memo_off.jpg")));
        karteButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/memo_on.jpg")));
        karteButton.setSize(48, 25);
        karteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //�J���e����ʂ��Ăяo��
                KarteDialog kd = null;
                kd = new KarteDialog(
                        shopId, slipNo, customer);
                SwingUtil.openAnchorDialog(parentFrame, true, kd, "�J���e���", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
                ((JDialog) kd.getParent().getParent().getParent().getParent()).dispose();
                kd = null;
                System.gc();
                showVisitedMemoList();
            }
        });
        return karteButton;
    }

    /**
     * �Љ������
     */
    private void removeIntroducer() {
        if (introducer.getCellEditor() != null) {
            introducer.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(introducer);
        introducerID = null;


        // �}�X�^�[�̃I�u�W�F�N�g�������
        customer.setIntroducerID(null);
        customer.setIntroducerNote(null);

        // �{�^���̃��x����ύX����
        //addIntroducerButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        //addIntroducerButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));

        // �{�^����\������
        addIntroducerButton.setVisible(true);
    }

    /**
     * �Ƒ�������
     */
    private void removeFamily() {

        DefaultTableModel model = (DefaultTableModel) family.getModel();
        int row = family.getSelectedRow();
        if (family.getCellEditor() != null) {
            family.getCellEditor().stopCellEditing();
        }
        model.removeRow(row);
    }

    /**
     * NULL�t�B�[���h���擾����
     */
    private JFormattedTextField getNullField() {
        JFormattedTextField ftf = new JFormattedTextField(
                FormatterCreator.createMaskFormatter("", null, ""));
        ftf.setHorizontalAlignment(JTextField.CENTER);
        ftf.addFocusListener(SystemInfo.getSelectText());
        ftf.setBorder(null);
        ftf.setText("");
        return ftf;
    }

    /**
     * �ڋq�����E�B���h�E��\������
     */
    private MstCustomer getMstCustomer() {
        SystemInfo.getLogger().log(Level.INFO, "�ڋq����");
        SearchCustomerDialog sc = new SearchCustomerDialog(parentFrame, true);
        sc.setShopID(this.getSelectedShopID());
        sc.setVisible(true);

        MstCustomer mc = sc.getSelectedCustomer();

        sc.dispose();

        return mc;
    }

    /**
     * �Љ�ꗗ���擾����
     */
    private ArrayList<MstCustomer> getIntroduceList() {
        ConnectionWrapper con = SystemInfo.getConnection();
        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {

            return MstCustomer.getIntroduceList(con, customer.getCustomerID(), this.getSelectedShopID());
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return null;
    }

    /**
     * �Љ�ꗗ��ǉ�����B
     */
    private void addIntoroduce(MstCustomer mc) {
        MstCustomerManager mcm = new MstCustomerManager();
        mcm.setData(mc);
        mcm.loadAccounts();

        String lastVisitDate = "";
        String staffName = "";
        if (mcm.getAccounts().size() > 0) {
            VisitRecord vr = mcm.getAccounts().get(0);
            lastVisitDate = String.format("%1$tY/%1$tm/%1$td", vr.getSalesDate());
            MstStaff staff = vr.getStaff();
            staffName = (staff != null ? staff.getFullStaffName() : "");
        }

        DefaultTableModel model = (DefaultTableModel) introduce.getModel();
        if (introduce.getCellEditor() != null) {
            introduce.getCellEditor().stopCellEditing();
        }
        model.addRow(new Object[]{
                    mc.getCustomerNo(),
                    mc.getCustomerName(0) + " " + mc.getCustomerName(1),
                    lastVisitDate,
                    staffName,
                    getUserSearchButton(mc.getCustomerID()),
                    getMemoEditButton(MEMO_EDIT_INTRODUCE)
                });

    }

    private void showNextReserveDate() {

        //IVS_LVTu start edit 2015/10/20 New request #43508
        nextReserveShopID = null;
        nextReserveNo = null;
        //nextReserveDate.setText("");
        cmbNextReserve.removeAllItems();
        nextReserveChangeButton.setEnabled(false);

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select shop_id, ");
	sql.append(" reservation_no, ");
	sql.append(" to_char(reservation_datetime, 'yyyy/mm/dd hh24:mi') AS next_reserve_date ");
        sql.append(" from (");
        sql.append(" select");
        sql.append("      dr.shop_id");
        sql.append("     ,dr.reservation_no");
        //sql.append("     ,to_char(drd.reservation_datetime, 'yyyy/mm/dd hh24:mi') as next_reserve_date");
        sql.append("     ,min(drd.reservation_datetime) as reservation_datetime ");
        sql.append(" from");
        sql.append("     data_reservation dr");
        sql.append("         inner join data_reservation_detail drd");
        sql.append("             using(shop_id, reservation_no)");
        sql.append(" where");
        sql.append("         dr.delete_date is null");
        sql.append("     and drd.delete_date is null");
        sql.append("     and dr.customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
        sql.append("     and date_trunc('d', drd.reservation_datetime) > date_trunc('d', current_timestamp)");
        //sql.append(" order by");
        //sql.append("     drd.reservation_datetime");
        //sql.append(" limit 1");
        sql.append("     group by dr.shop_id, ");
	sql.append("    	 dr.reservation_no) as data ");
        sql.append("     ORDER BY reservation_datetime ");

        try {

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

            //if (rs.next()) {
            while(rs.next()) {
                NextReserve row = new NextReserve();
                row.setShopId(rs.getInt("shop_id"));
                row.setReservationNo(rs.getInt("reservation_no"));
                row.setNextReserveDate(rs.getString("next_reserve_date"));
                
                //nextReserveShopID = rs.getInt("shop_id");
                //nextReserveNo = rs.getInt("reservation_no");
                //nextReserveDate.setText(rs.getString("next_reserve_date"));
                cmbNextReserve.addItem(row);
            }

            //nextReserveChangeButton.setEnabled(nextReserveDate.getText().length() > 0);
            nextReserveChangeButton.setEnabled(cmbNextReserve.getSelectedItem()!= null);
            
            //IVS_LVTu end edit 2015/10/20 New request #43508
            rs.close();

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

        private Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        public MultiLineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                super.setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                super.setForeground(table.getForeground());
                super.setBackground(table.getBackground());
            }

            setFont(table.getFont());

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(noFocusBorder);
            }

            setText((value == null) ? "" : value.toString());

            return this;
        }
    }

    private void initYearCombo(final JComboBox cmb) {

        cmb.removeAllItems();

        int y = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 5; i++) {
            cmb.addItem(String.valueOf(y - i));
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, -11);
        cmb.setSelectedItem(cal.get(Calendar.YEAR));
        cmbTargetMonth.setSelectedIndex(cal.get(Calendar.MONTH));

        cmb.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ((JTextField) cmb.getEditor().getEditorComponent()).selectAll();
            }
        });

        SwingUtil.clearTable(visitCycleTable);
        for (int i = 1; i <= 12; i++) {
            visitCycleTable.getColumnModel().getColumn(i).setHeaderValue("");
        }

    }

    // IVS SANG START 20131102 [gb�\�[�X]�\�[�X�}�[�W
    //An start add 20130125
    private void showCancel() {
        //salesTotal.setText(FormatUtil.decimalFormat(customer.getSalesTotal()));
        technicSalesTotal.setText(FormatUtil.decimalFormat(customer.getTechnicTotal()));
        itemSalesTotal.setText(FormatUtil.decimalFormat(customer.getItemTotal()));
        technicClameSalesTotal.setText(FormatUtil.decimalFormat(customer.getTechnicClameTotal()));
        itemReturnedSalesTotal.setText(FormatUtil.decimalFormat(customer.getItemReturnedTotal()));

        DefaultTableModel model = (DefaultTableModel) cancels.getModel();
        if (cancels.getCellEditor() != null) {
            cancels.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(cancels);

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      a.shop_id ");
        sql.append("     ,b.shop_name");
        sql.append("     ,c.reservation_datetime");
        sql.append("     ,a.cancel_flg");
        // IVS SANG START INSERT 20131103
        sql.append("     ,a.delete_date ");
        // IVS SANG START INSERT 20131103
        sql.append(" from data_reservation a");
        sql.append("        left join mst_shop b");
        sql.append("            on b.shop_id = a.shop_id");
        sql.append("        left join data_reservation_detail c");
        // IVS SANG START EDIT 20131103
        // sql.append("            on a.reservation_no = c.reservation_no");
        sql.append("            on a.reservation_no = c.reservation_no and a.shop_id = c.shop_id ");
        // IVS SANG START EDIT 20131103
        sql.append(" where a.customer_id =" + SQLUtil.convertForSQL(customer.getCustomerID()));
        sql.append(" and c.reservation_detail_no =1");
        // IVS SANG START INSERT 20131103
        sql.append(" and a.delete_date is not null");
        // IVS SANG START INSERT 20131103
        sql.append(" order by a.reservation_no ");
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            NumberFormat nf = NumberFormat.getInstance();
            while (rs.next()) {
                DataReservation dr = new DataReservation();
                dr.setReserveDate(rs.getDate("reservation_datetime"));
                dr.setCancelFlag(rs.getInt("cancel_flg"));
                MstShop ms = new MstShop();
                ms.setShopID(rs.getInt("shop_id"));
                ms.setShopName(rs.getString("shop_name"));
                dr.setShop(ms);
                String strCancelFlag = "�L�����Z��";
                // IVS SANG START EDIT 20131103 
//                if (dr.getCancelFlag() == 1) {
//                    strCancelFlag = "���f�L�����Z��";
//                } else if (dr.getCancelFlag() == 0) {
//                    strCancelFlag = "�A���L��̃L�����Z��";
//                }
                // IVS SANG START EDIT 20131103
                model.addRow(new Object[]{
                            dr.getShop().getShopName(),
                            // String.format("%1$tY/%1$tm/%1$td", dr.getReserveDate()),
                            dr.getReserveDate() == null ? "" : String.format("%1$tY/%1$tm/%1$td", (dr.getReserveDate())),
                            strCancelFlag
                        });
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    //IVS_LVTu start add 2015/11/23 New request #44111
    private void showProposal() {
        //�ڋqNo.
        txtCustomerNo.setText(this.customer.getCustomerNo());
        //����
        txtCustomerName.setText(this.customer.getFullCustomerName()); 
        //����ď��ꗗ��
        DefaultTableModel model = (DefaultTableModel) tbProposal.getModel();
        if (tbProposal.getCellEditor() != null) {
            tbProposal.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(tbProposal);
        SwingUtil.clearTable(tbCourseDescription);
        SwingUtil.clearTable(tbProductDescription);
        this.arrProposal.clear();

        StringBuilder sql = new StringBuilder(1000);
        sql.append("	SELECT dp.proposal_date,  \n"); 
        sql.append("		   dp.shop_id,  \n");
        sql.append("		   staff.staff_id,  \n");
        sql.append("		   staff.staff_name1,  \n");
        sql.append("		   staff.staff_name2,  \n");
        sql.append("		   dp.proposal_id,  \n");
        sql.append("		   dp.proposal_name,  \n");
        sql.append("		   sum(CASE WHEN dpd.product_division = 2 THEN (dpd.product_value * dpd.product_num) ELSE 0 END) AS item_value,  \n");
        sql.append("		   sum(CASE WHEN dpd.product_division = 5 THEN (dpd.product_value) ELSE 0 END) AS course_value,  \n");
        sql.append("		   dp.proposal_valid_date,  \n");
        sql.append("		   dp.contract_shop_id,  \n");
        sql.append("		   ms.shop_name,  \n");
        sql.append("		   dp.slip_no,  \n");
        sql.append("		   customer_id ,  \n");
        sql.append("		   proposal_memo  \n");
        sql.append("	FROM data_proposal dp  \n");
        sql.append("	INNER JOIN data_proposal_detail dpd ON dpd.shop_id = dp.shop_id  \n");
        sql.append("	AND dpd.proposal_id = dp.proposal_id  \n");
        sql.append("	INNER JOIN mst_staff staff ON staff.staff_id = dp.staff_id  \n");
        sql.append("	LEFT JOIN mst_item mi ON mi.item_id = dpd.product_id  \n");
        sql.append("	LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id  \n");
        sql.append("	LEFT JOIN mst_course mc ON dpd.product_id = mc.course_id  \n");
        sql.append("	LEFT JOIN mst_course_class mcc ON mcc.course_class_id = mc.course_class_id  \n");
        sql.append("	LEFT JOIN mst_shop ms ON ms.shop_id = dp.contract_shop_id  \n");
        sql.append("	WHERE dp.customer_id =" + SQLUtil.convertForSQL(customer.getCustomerID()));
        sql.append("	  AND dp.delete_date IS NULL  \n");
        sql.append("	  AND dpd.delete_date IS NULL  \n");
        sql.append("	GROUP BY dp.proposal_date,  \n");
        sql.append("			 dp.shop_id, \n");
        sql.append("			 staff.staff_id,  \n");
        sql.append("			 staff.staff_name1,  \n");
        sql.append("			 staff.staff_name2,  \n");
        sql.append("			 dp.proposal_id,  \n");
        sql.append("			 dp.proposal_name,  \n");
        sql.append("			 dp.proposal_valid_date,  \n");
        sql.append("                     dp.contract_shop_id,  \n");
        sql.append("			 ms.shop_name,  \n");
        sql.append("			 dp.slip_no,  \n");
        sql.append("                     customer_id ,  \n");
        sql.append("                     proposal_memo  \n");
        sql.append("	ORDER BY dp.proposal_date DESC \n");
        
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            NumberFormat nf = NumberFormat.getInstance();
            Double itemValue    = 0d;
            Double courseValue  = 0d;
            Double totalValue   = 0d;
            DataProposal dtProposal = null;
            while (rs.next()) {
                dtProposal = new DataProposal();
                dtProposal.setData(rs);
                this.arrProposal.add(dtProposal);
                
                itemValue = rs.getDouble("item_value");
                courseValue = rs.getDouble("course_value");
                totalValue = itemValue + courseValue;
                
                model.addRow(new Object[]{
                            String.format("%1$tY/%1$tm/%1$td", rs.getDate("proposal_date")),
                            //rs.getString("staff_name1") + " " + rs.getString("staff_name2"),
                            (rs.getString("staff_name1") == null ? "": rs.getString("staff_name1") + " ") + (rs.getString("staff_name2") == null ? "" :rs.getString("staff_name2")), 
                            rs.getString("proposal_name"),
                            courseValue.longValue(),
                            itemValue.longValue(),
                            totalValue.longValue(),
                            String.format("%1$tY/%1$tm/%1$td", rs.getDate("proposal_valid_date")),
                            rs.getString("shop_name"),
                            //rs.getInt("slip_no") == 0 ? "": rs.getInt("slip_no"),
                            rs.getInt("slip_no") == 0 ? "": (String.valueOf(rs.getInt("slip_no"))).toString(),
                            getPrintProposalButton(),
                            getEditProposalButton(rs.getInt("slip_no")), 
                            getReplicaProposalButton(),
                            getDeleteProposalButton()
                        });
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    /**
     * ���
     */
    private JButton getPrintProposalButton() {
        JButton printButton = new JButton();
        printButton.setBorderPainted(false);
        printButton.setContentAreaFilled(false);
        printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/print_small_off.jpg")));
        printButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/print_small_on.jpg")));
        printButton.setSize(48, 25);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printProposal();
            }
        });

        return printButton;
    }
    /**
     * �ҏW
     */
    private JButton getEditProposalButton(Integer slipNo) {
        JButton editButton = new JButton();
        editButton.setBorderPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/edit_small_off.jpg")));
        editButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/edit_small_on.jpg")));
        editButton.setSize(48, 25);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProposal();
                showProposal();
            }
        });
        //������v�ɕR���Ă����ď��ɂ��Ă͕ҏW�{�^���������ɂȂ�܂��B
        if (slipNo > 0) {
            editButton.setEnabled(false);
        }
        return editButton;
    }
    
    /**
     * ����
     */
    private JButton getReplicaProposalButton() {
        JButton replicaButton = new JButton();
        replicaButton.setBorderPainted(false);
        replicaButton.setContentAreaFilled(false);
        replicaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/copy_small_off.jpg")));
        replicaButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/copy_small_on.jpg")));
        replicaButton.setSize(48, 25);
        replicaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replicaProposal();
                showProposal();
            }
        });
        //�{�����O�C���̏ꍇ�͐V�K�쐬�ƕ����͔񊈐��Ƃ���
        if (SystemInfo.getCurrentShop().getShopID().equals(0)) {
            replicaButton.setEnabled(false);
        }
        return replicaButton;
    }

    /**
     * �폜
     */
    private JButton getDeleteProposalButton() {
        JButton deleteButton = new JButton();
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
        deleteButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
        deleteButton.setSize(48, 25);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProposal();
                showProposal();
            }
        });

        return deleteButton;
    }
    
    private void printProposal() {
        int row = tbProposal.getSelectedRow();
        
        ConnectionWrapper cw = SystemInfo.getConnection();
        ResultSetWrapper rs = new ResultSetWrapper();
        int shopID      = this.arrProposal.get(row).getShopID();
        int ProposalID    = this.arrProposal.get(row).getProposalID();    
        
        String sql = "select ms.shop_name, mstaff.staff_name1, mstaff.staff_name2, dp.*, dpd.*,\n" +
                        "ms.address1, ms.address2, ms.address3, ms.address4 , ms.phone_number,\n" +
                        "case when dpd.product_division = 2 then mic.item_class_id else mcc.course_class_id end as product_class_id,\n" +
                        "case when dpd.product_division = 2 then mic.item_class_name else mcc.course_class_name end as product_class_name,\n" +
                        "case when dpd.product_division = 2 then mi.item_name else mc.course_name end as product_name\n" +
                        "from data_proposal dp\n" +
                        "inner join data_proposal_detail dpd on dpd.shop_id = dp.shop_id and dpd.proposal_id = dp.proposal_id\n" +
                        "inner join mst_shop ms on ms.shop_id = dp.shop_id\n" +
                	"inner join mst_staff mstaff on mstaff.staff_id = dp.staff_id\n" +
                        "left join mst_item mi on mi.item_id = dpd.product_id\n" +
                        "left join mst_item_class mic on mic.item_class_id = mi.item_class_id\n" +
                        "left join mst_course mc on dpd.product_id = mc.course_id\n" +
                        "left join mst_course_class mcc on mcc.course_class_id = mc.course_class_id\n" +
                        "where dp.shop_id = " + SQLUtil.convertForSQL(shopID) + "\n" +
                        "and dp.proposal_id = " + SQLUtil.convertForSQL(ProposalID) + "\n" +
                        "and dp.delete_date is null\n" +
                        "and dpd.delete_date is null\n" +
                        "order by dpd.product_id" ;
        
        try {
            
            rs = cw.executeQuery(sql);
            
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy/MM/dd");
            JExcelApi jx = new JExcelApi("��ď�");
            jx.setTemplateFile("/reports/��ď�.xls");
            
            MstStaff staff ;
            MstShop shop ;
            if ( rs.next()) {
                staff = new MstStaff();
                shop = new MstShop();
                staff.setStaffName(0, rs.getString("staff_name1"));
                staff.setStaffName(1, rs.getString("staff_name2"));
                
                shop.setAddress(0, rs.getString("address1"));
                shop.setAddress(1, rs.getString("address2"));
                shop.setAddress(2, rs.getString("address3"));
                shop.setAddress(3, rs.getString("address4"));
                shop.setPhoneNumber(rs.getString("phone_number"));
                
                jx.setValue(11, 3, format.format(rs.getDate("proposal_date")));
                jx.setValue(11, 4, format.format(rs.getDate("proposal_valid_date")));

                jx.setValue(3, 6, this.customer.getFullCustomerName() + "�l�i����ԍ��F" + this.customer.getCustomerNo() + ")" );
                jx.setValue(10, 7, rs.getString("shop_name"));
                jx.setValue(10, 8, (staff.getFullStaffName()));
                jx.setValue(10, 9,  (shop.getAddress(0).equals("") ? "" : (shop.getAddress(0))) + shop.getAddress(1));
                jx.setValue(10, 10, shop.getAddress(2));
                jx.setValue(10, 11, shop.getAddress(3));
                jx.setValue(10, 12, shop.getPhoneNumber());
            }
            int indexRow = 15;
            
            rs.beforeFirst();
             while(rs.next()) {
                Double productValue  = 0d;
                productValue = rs.getDouble("product_value");
                 
                jx.setValue(2, indexRow, rs.getString("product_name"));
                jx.setValue(7, indexRow, rs.getInt("product_num"));
                jx.setValue(8, indexRow, productValue.longValue());
                if ( rs.getInt("product_division") == 2) {
                    jx.setValue(9, indexRow, rs.getInt("product_num")*productValue );
                }else if(rs.getInt("product_division") == 5) {
                    jx.setValue(9, indexRow, productValue );
                }
                
                indexRow ++ ;
             }

            jx.openWorkbook();
  
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(1099),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
       
    } 
    
    private void editProposal() {
        int row = tbProposal.getSelectedRow();
        
        ProposalPanel pp = new ProposalPanel(customer, this.arrProposal.get(row), 1);
        SwingUtil.openAnchorDialog( this.parentFrame, true, pp, "��ď��쐬", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
        System.gc();
        pp.setVisible(true);
    }
    
    private void replicaProposal() {
        int row = tbProposal.getSelectedRow();
        
        ProposalPanel pp = new ProposalPanel(customer, this.arrProposal.get(row), 2);
        SwingUtil.openAnchorDialog( this.parentFrame, true, pp, "��ď��쐬", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
        System.gc();
        pp.setVisible(true);
    }
    
    private void deleteProposal() {
        int row = tbProposal.getSelectedRow();     
        
        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (MessageDialog.showYesNoDialog(this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE,
                "��ď��� : " + this.arrProposal.get(row).getProposalName().trim()),
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }

        try {


            if (this.arrProposal.get(row).delete(SystemInfo.getConnection())) {
                this.arrProposal.remove(row);
                //�R�~�b�g
                con.commit();

                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_DELETE_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
            } //�폜���s
            else {
                con.rollback();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private void selectedProposalIndex() {
        DataProposal dtProposal = new DataProposal();
        int row = tbProposal.getSelectedRow();
        int col = tbProposal.getSelectedColumn();
        if ( col > 8) {
            return;
        }
        String sql =	"select dpd.*,\n" +
                        "case when dpd.product_division = 2 then mic.item_class_id else mcc.course_class_id end as product_class_id,\n" +
                        "case when dpd.product_division = 2 then mic.item_class_name else mcc.course_class_name end as product_class_name,\n" +
                        "case when dpd.product_division = 2 then mi.item_name else mc.course_name end as product_name\n" +
                        "from data_proposal_detail dpd \n" +
                        "left join mst_item mi on mi.item_id = dpd.product_id\n" +
                        "left join mst_item_class mic on mic.item_class_id = mi.item_class_id\n" +
                        "left join mst_course mc on dpd.product_id = mc.course_id\n" +
                        "left join mst_course_class mcc on mcc.course_class_id = mc.course_class_id\n" +
                        "where dpd.shop_id = " + SQLUtil.convertForSQL(this.arrProposal.get(row).getShopID()) + "\n" +
                        "and dpd.proposal_id = " + SQLUtil.convertForSQL(this.arrProposal.get(row).getProposalID()) + "\n" +
                        "and dpd.delete_date is null" ;
        
        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
         
        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            
            DataProposalDetail dtDetail = null;
            while (rs.next()) {
                dtDetail = new DataProposalDetail();
                dtDetail.setData(rs);
                
                dtProposal.add(dtDetail);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        // set table ���R�[�X���e��
        DefaultTableModel modelCourse = (DefaultTableModel) tbCourseDescription.getModel();
        SwingUtil.clearTable(tbCourseDescription);
        // set table �����i���e��
        DefaultTableModel modelItem = (DefaultTableModel) tbProductDescription.getModel();
        SwingUtil.clearTable(tbProductDescription);
        
        for (DataProposalDetail dpd : dtProposal) {
            if ( dpd.getProductDivision() == 5) {
            modelCourse.addRow(
                new Object[]{
                    dpd.getClassName(),
                    dpd.getProductName(),
                    dpd.getProductNum(),
                    dpd.getProductValue()
                });
            }else if (dpd.getProductDivision() == 2) {
                modelItem.addRow(
                new Object[]{
                    dpd.getClassName(),
                    dpd.getProductName(),
                    dpd.getProductNum(),
                    dpd.getProductValue()*dpd.getProductNum()
                });
            }
        }
        
    }
    
    
    private String getProposalMemoToolTip(int row)
	{
            String str[] = this.arrProposal.get(row).getProposalMemo().split("\n");
            StringBuffer memo = new StringBuffer();
            for(String s : str) {
                memo.append(s + "<br>");
            }
                StringBuffer sb = new StringBuffer();
                sb.append("<html><table>");
                sb.append("<tr align=left><td><b>�y��ă����z</b></td></tr>");
                sb.append("<tr align=left><td>" + this.arrProposal.get(row).getProposalMemo() == null ? "": memo  + "</td></tr>�@");
                sb.append("</table></html>");
		return sb.toString();
	}
    
    //IVS_LVTu end add 2015/11/23 New request #44111
    
    //An end add 20130125
    //An start add 20130125    
    private void initAlertCombox(final JComboBox Alert) {
        Alert.removeAllItems();
        Alert.addItem("");
        Alert.addItem("!");
        Alert.addItem("��");
        Alert.addItem("��");
        Alert.addItem("��");
        Alert.addItem("��");
        Alert.addItem("��");
        Alert.addItem("��");
        Alert.addItem("��");
        Alert.addItem("��");
        Alert.setSelectedIndex(-1);

    }

    private void refresh() {
        this.showCourseHistory();
        this.clear();
        this.showData();
    }
    // An end add 20130125
    // IVS SANG END 20131102 [gb�\�[�X]�\�[�X�}�[�W
    //IVS_LVTu start add 2015/10/20 New request #43508
    private class NextReserve{   
        private Integer shopId          =  null;
        private Integer reservationNo   =  null;
        private String  nextReserveDate =  "";
        
        
        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public Integer getReservationNo() {
            return reservationNo;
        }

        public void setReservationNo(Integer reservationNo) {
            this.reservationNo = reservationNo;
        }

        public String getNextReserveDate() {
            return nextReserveDate;
        }

        public void setNextReserveDate(String nextReserveDate) {
            this.nextReserveDate = nextReserveDate;
        }
        
        public String toString()
	{
		return nextReserveDate;
	}
    }
    //IVS_LVTu end add 2015/10/20 New request #43508
    

// 2017/01/02 �ڋq���� ADD START
    /**
    * JTable�̗񕝂�����������B
    */
    private void initTableColumnWidth()
    {
        //��̕���ݒ肷��B
        memoTable.getColumn("�]��").setPreferredWidth(10);
        memoTable.getColumn("�폜").setPreferredWidth(10);
    }
    
    /**
     * ����ID�ɕR�Â�memoTable�̍s�C���f�b�N�X��Ԃ��B
     * @param memoId
     * @return ����ID�ɕR�Â�memoTable�̍s�C���f�b�N�X�B������Ȃ������ꍇ��-1
     */
    private int getRowIndex(int memoId) {
        for(int i = 0; i < customerMemoList.size(); ++i) {
            if(customerMemoList.get(i).getMemoId().equals(memoId)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * �ڋq�����^�u�����ڐݒ菈��
     */
    private void initCustomerMemoTab() {
        
        memoStaffsMap = new HashMap<>();
        deletedMemoShopComboItemIndexList = new ArrayList<>();
        deletedMemoStaffComboItemIndexList = new ArrayList<>();
        
        memoCustomerNoLabel.setText(this.customer.getCustomerNo());
        
        memoCustomerNameLabel.setText(this.customer.getFullCustomerName());

        this.initShopComboList(memoShopComboBox);

        this.initStaffComboList(memoStaffComboBox, SystemInfo.getCurrentShop());
        
        this.initCustomerMemoTable();
    }
    
    /**
     * �ڋq�����e�[�u��������
     */
    private void initCustomerMemoTable() {
        // �ڋq�����e�[�u���ݒ�
        customerMemoList = this.getMemoList();
        this.setMemoTable(customerMemoList);
        memoTable.repaint();
    }
    
    /**
     * �ڋq�����e�[�u���X�V
     */
    private void updateCustomerMemoTable() {
        // �ڋq�����e�[�u���ݒ�
        
        //�����o�^���A����ID�̏����ō~���\�[�g���s���B
        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("memoDate", new ReverseComparator()));
        comparator.addComparator(new BeanComparator("memoId", new ReverseComparator()));
        Collections.sort(customerMemoList, comparator);
        
        this.setMemoTable(customerMemoList);
        memoTable.repaint();
    }
    
    /**
     * data_customer_memo�e�[�u������ڋqID�ɕR�Â����폜�f�[�^���擾����B
     * @return DataCustomerMemo�̃��X�g�B�G���[��������null
     */
    private List<DataCustomerMemo> getMemoList() {
        if(customer == null || customer.getCustomerID() == null) {
            return null;
        }
        
        List<DataCustomerMemo> memoList = null;
        try {
            // �Ώیڋq�̖��폜�����f�[�^�擾
            DataCustomerMemo dataCustomerMemo = new DataCustomerMemo();
            dataCustomerMemo.setCustomerId(customer.getCustomerID());
            
            memoList = dataCustomerMemo.selectMemoDataWithShopDataAndStaffDataByCustomerId(SystemInfo.getConnection());
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return memoList;
    }
    
    /**
     * �����e�[�u���ɒl��ݒ肷��B
     * @param memoList �ݒ肷�郁���f�[�^�̃��X�g�Bnull����Ȃ�e�[�u������ɂ���B
     */
    private void setMemoTable(List<DataCustomerMemo> memoList) {
        DefaultTableModel model = (DefaultTableModel)memoTable.getModel();
        //�S�s�폜
        model.setRowCount(0);
        if(memoList == null || memoList.isEmpty()) {    
            return;
        }
       
        for(DataCustomerMemo memo : memoList) {
            model.addRow(
                new Object[] {
                    DateUtil.format(memo.getMemoDate(), MEMO_DATE_FORMAT),
                    memo.getShop(),
                    memo.getStaff(),
                    DataCustomerMemo.getEvaluationString(memo.getEvaluation()),
                    getDeleteMemoButton()
                }
            );
        }
    }
    
    /**
    * �����폜�{�^�����擾����
    * @param memo �{�^���������ɍ폜����郁���f�[�^
    */
    private JButton getDeleteMemoButton() {
        JButton	registMemoButton = new JButton();
        registMemoButton.setBorderPainted(false);
        registMemoButton.setContentAreaFilled(false);
        registMemoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_small_off.jpg")));
        registMemoButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                        "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_small_on.jpg")));
        registMemoButton.setSize(43, 23);
        registMemoButton.addActionListener(new java.awt.event.ActionListener()
        {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    try {
                         int selectedRow = memoTable.getSelectedRow();
                         // �폜�{�^���������ꂽ���ɁA�s���I������Ă��Ȃ���Ԃ̏ꍇ������̂ŁA����ɑΉ�����B
                         if(selectedRow == -1) {
                             // �����s���I������Ă��Ȃ���΁A�}�E�X�J�[�\���̈ʒu���牟���ꂽ�s���擾����B
                             Point point = memoTable.getMousePosition(true);
                             if(point != null) {
                                selectedRow = memoTable.rowAtPoint(point);
                             }
                             if(selectedRow == -1) {
                                return;
                             }
                         }

                         if(MessageDialog.showYesNoDialog(
                                null,
                                MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE_FIXED),
                                "�t�B�[�h",
                                JOptionPane.QUESTION_MESSAGE
                            ) == JOptionPane.NO_OPTION) {
                             return;
                         }
                         
                         setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                         
                         // �����f�[�^�폜
                         DataCustomerMemo dataCustomerMemo = new DataCustomerMemo();
                         DefaultTableModel model = (DefaultTableModel)memoTable.getModel();
                         dataCustomerMemo.setMemoId(customerMemoList.get(selectedRow).getMemoId());

                         if(dataCustomerMemo.delete(SystemInfo.getConnection())) {
                             // �폜�����������ꍇ�́A�e�[�u���ƃL���b�V���f�[�^����Ώۃf�[�^����������B
                             clearCustomerMemoComponents();
                             model.removeRow(selectedRow);
                             memoTable.repaint();
                             
                             customerMemoList.remove(selectedRow);
                             
                             // �폜�ΏۂɂȂ��Ă��鍀�ڂ��폜
                             removeDeletedComboBoxItems(memoShopComboBox, deletedMemoShopComboItemIndexList);
                             removeDeletedComboBoxItems(memoStaffComboBox, deletedMemoStaffComboItemIndexList);
                         }
                    } catch (SQLException ex) {
                        Logger.getLogger(MstCustomerPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
        });
        return registMemoButton;
    }
   
    /**
    * �������֘A�R���|�[�l���g���N���A����B
    */
    private void clearCustomerMemoComponents() {
        memoTable.clearSelection();
        
        memoRegistrationDateComboBox.setDate(memoRegistrationDateComboBox.getDefaultDate(true));

        if(memoShopComboBox.getItemCount() > 0) {
            memoShopComboBox.setSelectedItem(SystemInfo.getCurrentShop());
        }
        
        if(memoStaffComboBox.getItemCount() > 0) {
            memoStaffComboBox.setSelectedIndex(0);
        }

        evaluationButtonGroup.clearSelection();
        
        memoTextArea.setText("");
    }
    
   /**
    * �I�����ꂽ���������֘A�R���|�[�l���g�ɐݒ肷��B
    */
    private void updateCustomerMemoComponents() {
        int selectedRow = memoTable.getSelectedRow();
        if(selectedRow == -1 || customerMemoList == null || customerMemoList.isEmpty()) {
            return;
        }
        
        DataCustomerMemo memoData = customerMemoList.get(selectedRow);
        
        memoRegistrationDateComboBox.setDate(memoData.getMemoDate());

        MstShop shopData = memoData.getShop();
        this.selectShopComboBox(shopData);
        
        MstStaff staffData = memoData.getStaff();
        this.selectStaffComboBox(staffData);
        
        if(memoData.getEvaluation() == DataCustomerMemo.EVALUATION_GOOD) {
            memoEvaluationGoodRadioButton.setSelected(true);
        } else if(memoData.getEvaluation() == DataCustomerMemo.EVALUATION_BAD) {
            memoEvaluationBadRadioButton.setSelected(true);
        } else {
            memoEvaluationBatonRadioButton.setSelected(true);
        }
        
        memoTextArea.setText(memoData.getMemo());
        memoTextArea.setCaretPosition(0);
    }
    
    /**
     * �X�܃R���{�{�b�N�X��targetShop�̍��ڂ�I����Ԃɂ���B
     * �Y�����ڂ��X�܃R���{�{�b�N�X�ɑ��݂��Ȃ��ꍇ�A�����ɒǉ����A�I������B
     * �ǉ��������ڂ́A���̌Ăяo���ō폜�����B
     * @param targetShop �I������X�܃f�[�^
     */
    private void selectShopComboBox(MstShop targetShop) {
        this.selectComboBox(targetShop, memoShopComboBox, deletedMemoShopComboItemIndexList);
    }
    
    /**
     * �S���҃R���{�{�b�N�X��targetStaff�̍��ڂ�I����Ԃɂ���B
     * �Y�����ڂ��S���҃R���{�{�b�N�X�ɑ��݂��Ȃ��ꍇ�A�����ɒǉ����A�I������B
     * �ǉ��������ڂ́A���̌Ăяo���ō폜�����B
     * @param targetStaff �I������S���҃f�[�^
     */
    private void selectStaffComboBox(MstStaff targetStaff) {
        this.selectComboBox(targetStaff, memoStaffComboBox, deletedMemoStaffComboItemIndexList);
    }
    
    /**
     * �폜�ΏۂɂȂ��Ă��鍀�ڂ��폜����B
     * @param ComboBox ���ڍ폜����R���{�{�b�N�X
     * @param deletedMemoComboItemIndexList �폜�Ώۂ̃��X�g 
     */
    private void removeDeletedComboBoxItems(JComboBox ComboBox, List<Integer> deletedMemoComboItemIndexList) {
        for(Integer targetIndex : deletedMemoComboItemIndexList) {
            ComboBox.removeItemAt(targetIndex);
        }
        deletedMemoComboItemIndexList.clear();
    }
    
    /**
     * �X�܁E�S���҃R���{�{�b�N�X��target�̍��ڂ�I����Ԃɂ���B
     * �Y�����ڂ��R���{�{�b�N�X�ɑ��݂��Ȃ��ꍇ�A�����ɒǉ����A�I������B
     * �ǉ��������ڂ́A���̌Ăяo���ō폜�����B
     * @param targetShop �I������X�܁E�S���҃f�[�^
     * @param ComboBox ���삷��R���{�{�b�N�X
     * @param deletedMemoComboItemIndexList �폜�Ώۂ�ێ����邽�߂�List
     */
    private void selectComboBox(Object target, JComboBox ComboBox, List<Integer> deletedMemoComboItemIndexList) {
        // �폜�ΏۂɂȂ��Ă��鍀�ڂ��폜
        this.removeDeletedComboBoxItems(ComboBox, deletedMemoComboItemIndexList);
        
        // target�̍��ڂ����݂���index��T���B
        int targetIndex = -1;
        if(target instanceof MstShop) {
            MstShop targetShop = (MstShop)target;
            for(int i = 0; i < ComboBox.getItemCount(); ++i) {
                MstShop selectedShop = (MstShop)ComboBox.getItemAt(i);
                if(selectedShop.getShopID() != null && selectedShop.getShopID().equals(targetShop.getShopID())) {
                    targetIndex = i;
                    break;
                }
            }
        } else if (target instanceof MstStaff) {
            MstStaff targetStaff = (MstStaff)target;
            for(int i = 0; i < ComboBox.getItemCount(); ++i) {
                MstStaff selectedStaff = (MstStaff)ComboBox.getItemAt(i);
                if(selectedStaff.getStaffID() != null && selectedStaff.getStaffID().equals(targetStaff.getStaffID())) {
                    targetIndex = i;
                    break;
                }
            }
        }
       
        // ������Ȃ������ꍇ�͒ǉ�����B
        if(targetIndex == -1) {
            ComboBox.addItem(target);
            targetIndex = ComboBox.getItemCount() - 1;
            
            // ComboBox�ɑ��݂��Ȃ��͍̂폜���ꂽ�f�[�^�B
            // �폜���ꂽ�f�[�^�̖��̂͑��̍s���I������Ă��鎞�ɂ͕\�����Ȃ����߁A�폜�Ώۃ��X�g�ɒǉ����Ă����B
            // ���̍s���I�����ꂽ���ɍ폜�����B
            deletedMemoComboItemIndexList.add(targetIndex);
        }
        ComboBox.setSelectedIndex(targetIndex);
    }
    
    /**
     * ���̓`�F�b�N(�o�^��)
     * 
     * @param registrationDateComboBox �`�F�b�N�Ώۓo�^���R���{�{�b�N�X
     * @return �`�F�b�NOK�Ȃ�null, NG�Ȃ�Ή�����G���[���b�Z�[�W�R�[�h
     */
    private Integer checkInputRegistrationDate(FSCalenderCombo registrationDateComboBox) {
        if(registrationDateComboBox.getDate() == null) {
            return MessageUtil.ERROR_INPUT_EMPTY;
        }
        return null;
    }
    
    /**
     * ���̓`�F�b�N(�X��)
     * 
     * @param shopComboBox �`�F�b�N�ΏۓX�܃R���{�{�b�N�X
     * @return �`�F�b�NOK�Ȃ�null, NG�Ȃ�Ή�����G���[���b�Z�[�W�R�[�h
     */
    private Integer checkInputShop(JComboBox shopComboBox) {
        // �󔒑I����
        if(EMPTY_SHOP_DATA.equals(shopComboBox.getSelectedItem())) {
            return MessageUtil.ERROR_INPUT_EMPTY;
        }
        
        // �폜�σf�[�^�I����
        for(Integer targetIndex : deletedMemoShopComboItemIndexList) {
            if(targetIndex.equals(shopComboBox.getSelectedIndex())) {
                return MessageUtil.ERROR_INPUT_NOT_EXIST;
            }
        }
        return null;
    }
    
    /**
     * ���̓`�F�b�N(�S����)
     * 
     * @param staffComboBox �`�F�b�N�ΏےS���҃R���{�{�b�N�X
     * @return �`�F�b�NOK�Ȃ�null, NG�Ȃ�Ή�����G���[���b�Z�[�W�R�[�h
     */
    private Integer checkInputStaff(JComboBox staffComboBox) {
        // �󔒑I����
        if(EMPTY_STAFF_DATA.equals(staffComboBox.getSelectedItem())) {
            return MessageUtil.ERROR_INPUT_EMPTY;
        }
        
        // �폜�σf�[�^�I����
        for(Integer targetIndex : deletedMemoStaffComboItemIndexList) {
            if(targetIndex.equals(staffComboBox.getSelectedIndex())) {
                return MessageUtil.ERROR_INPUT_NOT_EXIST;
            }
        }
        return null;
    }
    
    /**
     * ���̓`�F�b�N(�]��)
     * 
     * @param evaluationButtonGroup �`�F�b�N�Ώۃ��W�I�{�^���O���[�v
     * @return �`�F�b�NOK�Ȃ�null, NG�Ȃ�Ή�����G���[���b�Z�[�W�R�[�h
     */
    private Integer checkInputEvaluation(ButtonGroup evaluationButtonGroup) {
        if(evaluationButtonGroup.getSelection() == null) {
            return MessageUtil.ERROR_NOT_SELECTED;
        }
        return null;
    }
    
    /**
     * ���̓`�F�b�N(����)
     * 
     * @param memoTextArea �`�F�b�N�Ώۃ����e�L�X�g�G���A
     * @return �`�F�b�NOK�Ȃ�null, NG�Ȃ�Ή�����G���[���b�Z�[�W�R�[�h
     */
    private Integer checkInputMemo(JTextArea memoTextArea) {
        if(memoTextArea.getText().isEmpty()) {
            return MessageUtil.ERROR_INPUT_EMPTY;
        }
        return null;
    }
    
    /** 
    * �X�܃R���{�{�b�N�X�l�ݒ�
    * ���O�C���X�܂Ɠ���O���[�v�̓X�܂��ׂĂ��R���{�{�b�N�X�ɐݒ肷��B
    * 
    * @param shopComboBox �ݒ�ΏۓX�܃R���{�{�b�N�X
    */
    private void initShopComboList(JComboBox shopComboBox) {
        shopComboBox.removeAllItems();
        
        // �{�����O�C���̏ꍇ�A�R���{�擪�ɋ󔒂�ݒ�B
        if (SystemInfo.getCurrentShop().getShopID() == 0) {
            shopComboBox.addItem(EMPTY_SHOP_DATA);
        }
        for (MstShop shopData : SystemInfo.getGroup().getShops()) {
            shopComboBox.addItem(shopData);
        }
        shopComboBox.setSelectedItem(SystemInfo.getCurrentShop());
    }
    
    /** 
    * �S���҃R���{�{�b�N�X�l�ݒ�
    * mst_staff�ɑ��݂��関�폜�f�[�^���R���{�{�b�N�X�ɐݒ肷��B
    * �����͓X�܂ɏ�������X�^�b�t���D�悳���B
    * 
    * @param staffComboBox �ݒ�ΏےS���҃R���{�{�b�N�X
    * @param shop �X�܏��
    */
    private void initStaffComboList(JComboBox staffComboBox, MstShop shop) {
        staffComboBox.removeAllItems();
        MstStaffs staffs = new MstStaffs();
        
        // Staffs�̓X�܂ɑI������Ă���X�܁i���I���̏ꍇ�̓��O�C���X�܁j��ݒ肷��B
        // �ݒ�X�܂͈ȉ��ɗ��p�����B
        // �P�D�\�������ݒ�
        // �@�@�ΏۃX�^�b�t���ݒ�X�܂ɏ������Ă���ꍇ�\���������ŏ�ʂɂ���B
        // �Q�Dmst_staff_nondisplay�e�[�u���ɂ���\���X�܁E�X�^�b�t�̑g�ݍ��킹����
        Integer shopId;
        if (shop != null && shop.equals(EMPTY_SHOP_DATA)) {
            staffs.setShopIDList(shop.getShopID().toString());
            shopId = shop.getShopID();
        } else {
            staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
            shopId = SystemInfo.getCurrentShop().getShopID();
        }

        // �L���b�V��������΂���𗘗p����B
        if(memoStaffsMap.containsKey(shopId)) {
            staffs = memoStaffsMap.get(shopId);
        } else {
            try {
                // �X�^�b�t�f�[�^�擾
                staffs.load(SystemInfo.getConnection(), true);
                memoStaffsMap.put(shopId, staffs);
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        
        for (MstStaff staff : staffs) {
            staffComboBox.addItem(staff);
        }
    }
    
    /**
     * �ڋq�����@�\�L������
     * 
     * @return �ڋq�����L�����Atrue�B�������Afalse
     */
    private boolean isCustomerMemoEnabled() {
        // ���O�C��DB��pos_hair_nonailX�ȊO�ł���Όڋq�����@�\����
        return SystemInfo.getDatabase().startsWith("pos_hair_nonail");
    }
// 2017/01/02 �ڋq���� ADD END
}
