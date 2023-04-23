/*
 * CardPointPanel.java
 *
 * Created on 2008/09/02, 13:36
 */

package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.sosia.pos.master.company.MstShop;
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
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.*;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.StringUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.Window;
import java.sql.SQLException;
import java.text.DecimalFormat;

import java.text.SimpleDateFormat;
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
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class CardPointPrintPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    private static int COMMENT_MAX_LENGTH;
    private static int TITLE_MAX_LENGTH;
    
    private ArrayList<PointCardLayoutBean> layoutDataList;
    private boolean hasValidEntries;
    private int currentIndex = 0;
    
    private Integer shopId     = null;
    private Integer customerId = null;
    private Integer staffId    = null;
    private Integer nextType   = null;
    private Date salesDate = null;
    private Date nextReserveDate = null;
    private Date limitDate = null;
    private Date nextDateTo    = null;
    private Date nextDateFrom  = null;
    
    private PointData point = null;
    
    // 支払い情報
    private Integer slipNo    = null;
    private Integer paymentNo = null;
    
    // 印刷完了フラグ
    private boolean isPrinted = false;
    
    private LocalFocusTraversalPolicy	ftp;
    private DataSales sales   = null;
    
    private long discountUsePoint = 0l;
    private HairInputAccountDetailDialog hairDlg = null;
    
    // エラー種別
    private final int FAILED_CUSTOMER = 1;
    private final int FAILED_STAFF = 2;
    private final int FAILED_ADDPOINT = 3;
    private final int FAILED_USEPOINT = 4;
    private final int FAILED_INSERTPOINT = 5;
    private final int FAILED_UPDATEPOINT = 6;
    
    public static final int NEXT_TYPE_RESERVE_DATE = 0;
    public static final int NEXT_TYPE_NEXT_DATE = 1;

    private boolean isAlaise = SystemInfo.getDatabase().startsWith("pos_hair_alaise");

    // 予約データにて、ダイアログとして、表示する
    static public boolean ShowDialog(
            JDialog owner,
            DataSales sales,
            Date reservDate,
            Date from,
            Date to,
            Date limitDate,
            int nextType,
            long discountUsePoint )
    {
        SystemInfo.getLogger().log(Level.INFO, "カード印刷");
        
        CardPointPrintPanel	dlg = new CardPointPrintPanel(sales.getShop().getShopID());
        dlg.ShowCloseBtn();
        
        dlg.hairDlg = (HairInputAccountDetailDialog)owner;
        
        dlg.sales = sales;
        dlg.customerId = sales.getCustomer().getCustomerID();
        dlg.staffId    = sales.getStaff().getStaffID();
        dlg.slipNo     = sales.getSlipNo();
        dlg.paymentNo  = sales.getPayment(0).getPaymentNo();
        dlg.nextType   = nextType; // 予約
        dlg.salesDate = sales.getSalesDate();
        dlg.nextReserveDate = reservDate;
        dlg.limitDate = limitDate;
        dlg.nextDateFrom = from;
        dlg.nextDateTo   = to;
        dlg.setDiscountUsePoint(discountUsePoint);
        dlg.loadDispData();
        
        SwingUtil.openDialog(owner, true, dlg, "カード印刷");
        
        if( dlg.isPrinted ){
            dlg.dispose();
            return true;
        }else{
            dlg.dispose();
            return false;
        }
    }
    
    // カード印刷ダイアログを表示（顧客・販売データなしで開く）
    static public boolean ShowDialog(Frame owner, MstShop shop, int nextType ) {
        
        SystemInfo.getLogger().log(Level.INFO, "カード印刷");
        
        CardPointPrintPanel	dlg = new CardPointPrintPanel(shop.getShopID());
        dlg.ShowCloseBtn();
        
        dlg.sales = null;
        dlg.customerId = null;
        dlg.staffId    = null;
        dlg.slipNo     = null;
        dlg.paymentNo  = null;
        dlg.nextType   = nextType; // 予約
        dlg.salesDate = null;
        dlg.nextReserveDate = null;
        dlg.limitDate = null;
        dlg.nextDateFrom = null;
        dlg.nextDateTo   = null;
        dlg.loadDispData();
        
        SwingUtil.openDialog(owner, true, dlg, "カード印刷");
        
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
            mst.load(con);
            
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
        
        currentSalesDate.setDate(salesDate);
        nextReservationDate.setDate(nextReserveDate);
        currentLimitDate.setDate(limitDate);
        nextTimeFrom.setDate(nextDateFrom);
        nextTimeTo.setDate(nextDateTo);

/*
        currentSales.setSelected(false);
        currentSalesDate.setEnabled(false);
        currentLimit.setSelected(false);
        currentLimitDate.setEnabled(false);
        
        // 次回来店日付、次回予約日を表示
        if (nextType == NEXT_TYPE_RESERVE_DATE) {
            nextReservation.setSelected(true);
            nextTime.setSelected(false);
            nextTimeTo.setEnabled(false);
            nextTimeFrom.setEnabled(false);
        } else {
            nextReservation.setSelected(false);
            nextReservationDate.setEnabled(false);
            nextTime.setSelected(true);
        }
*/

        // 発生ポイントを設定
        if( sales != null ){
            if( sales.getSlipNo() != null ){
                point = PointData.getPointData(sales.getShop().getShopID(), sales.getSlipNo() );
            }
            if( point == null ){
                long newPoint = CalculatePoint.calcPoint(sales);
                txtAddPoint.setText(String.valueOf(newPoint));
                txtUsePoint.setText(String.valueOf(this.discountUsePoint));
            }else{
                txtAddPoint.setText(String.valueOf(point.getSuppliedPoint()));
                txtUsePoint.setText(String.valueOf(point.getUsePoint() + this.discountUsePoint));
            }
            
            updatePreviewPoint();
        }else{
            txtAddPoint.setText("0");
            txtUsePoint.setText("0");
        }
        
        ShowNextDate();
        initComboBoxItems();
    }
    
    
    /** Creates new form CardPointPrintPanel */
    public CardPointPrintPanel(Integer shopId) {
       
    
        COMMENT_MAX_LENGTH = SystemInfo.getPointcardConnection().getMaxChars();
        TITLE_MAX_LENGTH = SystemInfo.getPointcardConnection().getMaxChars();
        
        this.shopId = shopId;
        
        initComponents();
        
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        this.setSize(800,600);
        this.setPath("基本設定 >> ポイントカード設定 >> カード印字");
        this.setTitle("カード印字");
        
        initComboBoxItems();
        shopId = SystemInfo.getCurrentShop().getShopID();
        
        if( hasValidEntries == true ) {
            this.titleList.setSelectedIndex( currentIndex );
            populateComments();
        }
        
        txtAddPoint.setText("0");
        txtUsePoint.setText("0");
        
        this.initStaff(inputStaff);
        this.addMouseCursorChange();
        this.setKeyListener();
        
        lblInputNum.setText("※1行につき全角" + (COMMENT_MAX_LENGTH / 2) + "文字まで登録できます。");
        
        if (SystemInfo.getPointcardProductId() == 2) {
            commentInputLabel_08.setVisible(false);
            commentInputLabel_09.setVisible(false);
            commentInput_08.setVisible(false);
            commentInput_09.setVisible(false);
            comment8Preview.setVisible(false);
            comment9Preview.setVisible(false);
        }

        switch (SystemInfo.getPointDefaultDateType()) {
            case 0:
                this.currentSales.setSelected(true);
                break;
            case 1:
                this.nextReservation.setSelected(true);
                break;
            case 2:
                this.currentLimit.setSelected(true);
                break;
            case 3:
                this.nextTime.setSelected(true);
                break;
        }
        
        UpdateButtonEnabled();

        if (isAlaise) {
            commentPanel.setVisible(false);
            nextTime.setVisible(false);
            nextTimeFrom.setVisible(false);
            nextTimeTo.setVisible(false);
            nextTimeKara.setVisible(false);
        }
    }
    
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnCalcPoint);
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
        txtAddPoint.addKeyListener(SystemInfo.getMoveNextField());
        txtUsePoint.addKeyListener(SystemInfo.getMoveNextField());
        currentSales.addKeyListener(SystemInfo.getMoveNextField());
        currentSalesDate.addKeyListener(SystemInfo.getMoveNextField());
        nextReservation.addKeyListener(SystemInfo.getMoveNextField());
        nextReservationDate.addKeyListener(SystemInfo.getMoveNextField());
        currentLimit.addKeyListener(SystemInfo.getMoveNextField());
        currentLimitDate.addKeyListener(SystemInfo.getMoveNextField());
        nextTime.addKeyListener(SystemInfo.getMoveNextField());
        nextTimeFrom.addKeyListener(SystemInfo.getMoveNextField());
        nextTimeTo.addKeyListener(SystemInfo.getMoveNextField());
        titleList.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_01.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_02.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_03.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_04.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_05.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_06.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_07.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_08.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_09.addKeyListener(SystemInfo.getMoveNextField());
        
        
        txtCustomerNo.addFocusListener(SystemInfo.getSelectText());
        txtCustomerName1.addFocusListener(SystemInfo.getSelectText());
        txtCustomerName2.addFocusListener(SystemInfo.getSelectText());
        txtStaffNo.addFocusListener(SystemInfo.getSelectText());
        inputStaff.addFocusListener(SystemInfo.getSelectText());
        txtAddPoint.addFocusListener(SystemInfo.getSelectText());
        txtUsePoint.addFocusListener(SystemInfo.getSelectText());
        currentSales.addFocusListener(SystemInfo.getSelectText());
        currentSalesDate.addFocusListener(SystemInfo.getSelectText());
        nextReservation.addFocusListener(SystemInfo.getSelectText());
        nextReservationDate.addFocusListener(SystemInfo.getSelectText());
        currentLimit.addFocusListener(SystemInfo.getSelectText());
        currentLimitDate.addFocusListener(SystemInfo.getSelectText());
        nextTime.addFocusListener(SystemInfo.getSelectText());
        nextTimeFrom.addFocusListener(SystemInfo.getSelectText());
        nextTimeTo.addFocusListener(SystemInfo.getSelectText());
        titleList.addFocusListener(SystemInfo.getSelectText());
        commentInput_01.addFocusListener(SystemInfo.getSelectText());
        commentInput_02.addFocusListener(SystemInfo.getSelectText());
        commentInput_03.addFocusListener(SystemInfo.getSelectText());
        commentInput_04.addFocusListener(SystemInfo.getSelectText());
        commentInput_05.addFocusListener(SystemInfo.getSelectText());
        commentInput_06.addFocusListener(SystemInfo.getSelectText());
        commentInput_07.addFocusListener(SystemInfo.getSelectText());
        commentInput_08.addFocusListener(SystemInfo.getSelectText());
        commentInput_09.addFocusListener(SystemInfo.getSelectText());
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
        
        this.titleList.setModel( new DefaultComboBoxModelEx( listItems ) );
        this.titleList.setSelectedIndex(currentIndex);
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

        if (isAlaise) return;

        int selectedIndex = this.titleList.getSelectedIndex();
        this.currentIndex = selectedIndex;
        
        if( selectedIndex > -1 ) {
            PointCardLayoutBean bean = this.layoutDataList.get( selectedIndex );
            this.commentInput_01.setText( bean.getComment1() );
            this.commentInput_02.setText( bean.getComment2() );
            this.commentInput_03.setText( bean.getComment3() );
            this.commentInput_04.setText( bean.getComment4() );
            this.commentInput_05.setText( bean.getComment5() );
            this.commentInput_06.setText( bean.getComment6() );
            this.commentInput_07.setText( bean.getComment7() );
            this.commentInput_08.setText( bean.getComment8() );
            this.commentInput_09.setText( bean.getComment9() );
        }
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
                staffNamePreview.setText(ms.getStaffName()[0] + "　" + ms.getStaffName()[1]);
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
        comment1Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment2Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment3Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment4Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment5Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment6Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment7Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment8Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment9Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        customerNoPreview = new com.geobeck.swing.JFormattedTextFieldEx();
        customerNamePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        datePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        staffNamePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        jScrollPane1 = new javax.swing.JScrollPane();
        pointsPreview = new javax.swing.JTextPane();
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
        commentPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        commentInputLabel_08 = new javax.swing.JLabel();
        commentInputLabel_09 = new javax.swing.JLabel();
        commentInput_01 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_01.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_02 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_02.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_03 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_03.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_04 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_04.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_05 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_05.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_06 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_06.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_07 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_07.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_08 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_08.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_09 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_09.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        titleList = new javax.swing.JComboBox();
        lblInputNum = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        nextReservationDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        nextTimeFrom = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        nextReservation = new javax.swing.JRadioButton();
        nextTime = new javax.swing.JRadioButton();
        nextTimeTo = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        nextTimeKara = new javax.swing.JLabel();
        currentSales = new javax.swing.JRadioButton();
        currentSalesDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        currentLimit = new javax.swing.JRadioButton();
        currentLimitDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtUsePoint = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtUsePoint.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.NUMERIC));
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtAddPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtAddPoint.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.NUMERIC));
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtPrevPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel17 = new javax.swing.JLabel();
        txtTotalPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        btnCalcPoint = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnClose.setVisible(false);
        btnClose.setContentAreaFilled(false);
        btnCardPrint = new javax.swing.JButton();
        btnPrintClear = new javax.swing.JButton();

        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel1.setText("表示レイアウト");

        cardPanel.setBackground(new java.awt.Color(255, 255, 255));
        cardPanel.setImage(SystemInfo.getImageIcon("/card/background.png"));
        cardPanel.setOpaque(false);

        comment1Preview.setBackground(Color.WHITE);
        comment1Preview.setBorder(null);
        comment1Preview.setEditable(false);
        comment1Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment1Preview.setAutoscrolls(false);
        comment1Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment1Preview.setFocusable(false);
        comment1Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment1Preview.setOpaque(false);
        comment1Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment1Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment2Preview.setBackground(Color.WHITE);
        comment2Preview.setBorder(null);
        comment2Preview.setEditable(false);
        comment2Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment2Preview.setAutoscrolls(false);
        comment2Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment2Preview.setFocusable(false);
        comment2Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment2Preview.setOpaque(false);
        comment2Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment2Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment3Preview.setBackground(Color.WHITE);
        comment3Preview.setBorder(null);
        comment3Preview.setEditable(false);
        comment3Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment3Preview.setAutoscrolls(false);
        comment3Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment3Preview.setFocusable(false);
        comment3Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment3Preview.setOpaque(false);
        comment3Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment3Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment4Preview.setBackground(Color.WHITE);
        comment4Preview.setBorder(null);
        comment4Preview.setEditable(false);
        comment4Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment4Preview.setAutoscrolls(false);
        comment4Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment4Preview.setFocusable(false);
        comment4Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment4Preview.setOpaque(false);
        comment4Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment4Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment5Preview.setBackground(Color.WHITE);
        comment5Preview.setBorder(null);
        comment5Preview.setEditable(false);
        comment5Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment5Preview.setAutoscrolls(false);
        comment5Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment5Preview.setFocusable(false);
        comment5Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment5Preview.setOpaque(false);
        comment5Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment5Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment6Preview.setBackground(Color.WHITE);
        comment6Preview.setBorder(null);
        comment6Preview.setEditable(false);
        comment6Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment6Preview.setAutoscrolls(false);
        comment6Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment6Preview.setFocusable(false);
        comment6Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment6Preview.setOpaque(false);
        comment6Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment6Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment7Preview.setBackground(Color.WHITE);
        comment7Preview.setBorder(null);
        comment7Preview.setEditable(false);
        comment7Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment7Preview.setAutoscrolls(false);
        comment7Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment7Preview.setFocusable(false);
        comment7Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment7Preview.setOpaque(false);
        comment7Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment7Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment8Preview.setBackground(Color.WHITE);
        comment8Preview.setBorder(null);
        comment8Preview.setEditable(false);
        comment8Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment8Preview.setAutoscrolls(false);
        comment8Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment8Preview.setFocusable(false);
        comment8Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment8Preview.setOpaque(false);
        comment8Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment8Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment9Preview.setBackground(Color.WHITE);
        comment9Preview.setBorder(null);
        comment9Preview.setEditable(false);
        comment9Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment9Preview.setAutoscrolls(false);
        comment9Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment9Preview.setFocusable(false);
        comment9Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment9Preview.setOpaque(false);
        comment9Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment9Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        customerNoPreview.setBackground(Color.WHITE);
        customerNoPreview.setBorder(null);
        customerNoPreview.setEditable(false);
        customerNoPreview.setForeground(new java.awt.Color(0, 0, 102));
        customerNoPreview.setAutoscrolls(false);
        customerNoPreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        customerNoPreview.setFocusable(false);
        customerNoPreview.setOpaque(false);
        customerNoPreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        customerNamePreview.setBackground(Color.WHITE);
        customerNamePreview.setBorder(null);
        customerNamePreview.setEditable(false);
        customerNamePreview.setForeground(new java.awt.Color(0, 0, 102));
        customerNamePreview.setAutoscrolls(false);
        customerNamePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        customerNamePreview.setFocusable(false);
        customerNamePreview.setFont(new java.awt.Font("MS UI Gothic", 1, 18));
        customerNamePreview.setOpaque(false);
        customerNamePreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        datePreview.setBackground(Color.WHITE);
        datePreview.setBorder(null);
        datePreview.setEditable(false);
        datePreview.setForeground(new java.awt.Color(0, 0, 102));
        datePreview.setAutoscrolls(false);
        datePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        datePreview.setFocusable(false);
        datePreview.setOpaque(false);
        datePreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        staffNamePreview.setBackground(Color.WHITE);
        staffNamePreview.setBorder(null);
        staffNamePreview.setEditable(false);
        staffNamePreview.setForeground(new java.awt.Color(0, 0, 102));
        staffNamePreview.setAutoscrolls(false);
        staffNamePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        staffNamePreview.setFocusable(false);
        staffNamePreview.setOpaque(false);
        staffNamePreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

        pointsPreview.setBorder(null);
        pointsPreview.setEditable(false);
        pointsPreview.setForeground(new java.awt.Color(0, 0, 102));
        pointsPreview.setDisabledTextColor(new java.awt.Color(0, 0, 102));
        pointsPreview.setFocusable(false);
        pointsPreview.setOpaque(false);
        jScrollPane1.setViewportView(pointsPreview);

        javax.swing.GroupLayout cardPanelLayout = new javax.swing.GroupLayout(cardPanel);
        cardPanel.setLayout(cardPanelLayout);
        cardPanelLayout.setHorizontalGroup(
            cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardPanelLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(customerNamePreview, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardPanelLayout.createSequentialGroup()
                            .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(customerNoPreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(datePreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(staffNamePreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment9Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment8Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment7Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment6Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment5Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment4Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment3Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment2Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addComponent(comment1Preview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                            .addGap(47, 47, 47))))
                .addGap(25, 25, 25))
        );
        cardPanelLayout.setVerticalGroup(
            cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(customerNoPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerNamePreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(datePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(staffNamePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(comment1Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment2Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment3Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment4Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment5Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment6Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment7Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment8Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment9Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

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
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
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

        txtCustomerName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCustomerName1.setEditable(false);
        txtCustomerName1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerName1FocusLost(evt);
            }
        });

        txtCustomerName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCustomerName2.setEditable(false);
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

        jLabel5.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
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
                    .addComponent(inputStaff, javax.swing.GroupLayout.Alignment.TRAILING, 0, 100, Short.MAX_VALUE)
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

        commentPanel.setOpaque(false);

        jLabel3.setText("タイトル");

        jLabel6.setText("コメント１");

        jLabel7.setText("コメント２");

        jLabel8.setText("コメント３");

        jLabel9.setText("コメント４");

        jLabel10.setText("コメント５");

        jLabel11.setText("コメント６");

        jLabel12.setText("コメント７");

        commentInputLabel_08.setText("コメント８");

        commentInputLabel_09.setText("コメント９");

        commentInput_01.setInputKanji(true);
        commentInput_01.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_01CaretUpdate(evt);
            }
        });
        commentInput_01.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_01FocusGained(evt);
            }
        });

        commentInput_02.setInputKanji(true);
        commentInput_02.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_02CaretUpdate(evt);
            }
        });
        commentInput_02.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_02FocusGained(evt);
            }
        });

        commentInput_03.setInputKanji(true);
        commentInput_03.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_03CaretUpdate(evt);
            }
        });
        commentInput_03.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_03FocusGained(evt);
            }
        });

        commentInput_04.setInputKanji(true);
        commentInput_04.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_04CaretUpdate(evt);
            }
        });
        commentInput_04.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_04FocusGained(evt);
            }
        });

        commentInput_05.setInputKanji(true);
        commentInput_05.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_05CaretUpdate(evt);
            }
        });
        commentInput_05.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_05FocusGained(evt);
            }
        });

        commentInput_06.setInputKanji(true);
        commentInput_06.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_06CaretUpdate(evt);
            }
        });
        commentInput_06.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_06FocusGained(evt);
            }
        });

        commentInput_07.setInputKanji(true);
        commentInput_07.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_07CaretUpdate(evt);
            }
        });
        commentInput_07.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_07FocusGained(evt);
            }
        });

        commentInput_08.setInputKanji(true);
        commentInput_08.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_08CaretUpdate(evt);
            }
        });
        commentInput_08.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_08FocusGained(evt);
            }
        });

        commentInput_09.setInputKanji(true);
        commentInput_09.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_09CaretUpdate(evt);
            }
        });
        commentInput_09.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_09FocusGained(evt);
            }
        });

        titleList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleListActionPerformed(evt);
            }
        });

        lblInputNum.setText("※");

        javax.swing.GroupLayout commentPanelLayout = new javax.swing.GroupLayout(commentPanel);
        commentPanel.setLayout(commentPanelLayout);
        commentPanelLayout.setHorizontalGroup(
            commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInputLabel_08, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInputLabel_09, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInputNum)
                    .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(titleList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_09, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_08, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_07, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_06, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_05, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_04, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_03, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(commentInput_01, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
                .addContainerGap(210, Short.MAX_VALUE))
        );
        commentPanelLayout.setVerticalGroup(
            commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_01, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_02, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_03, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_04, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_05, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_06, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_07, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commentInputLabel_08, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_08, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(commentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commentInputLabel_09, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_09, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInputNum)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setOpaque(false);

        nextReservationDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        nextReservationDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nextReservationDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nextReservationDateFocusLost(evt);
            }
        });

        nextTimeFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        nextTimeFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nextTimeFromFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nextTimeFromFocusLost(evt);
            }
        });

        groupNextTime.add(nextReservation);
        nextReservation.setText("次回予約日");
        nextReservation.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        nextReservation.setMargin(new java.awt.Insets(0, 0, 0, 0));
        nextReservation.setOpaque(false);
        nextReservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextReservationActionPerformed(evt);
            }
        });

        groupNextTime.add(nextTime);
        nextTime.setText("次回来店案内");
        nextTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        nextTime.setMargin(new java.awt.Insets(0, 0, 0, 0));
        nextTime.setOpaque(false);
        nextTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextTimeActionPerformed(evt);
            }
        });

        nextTimeTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        nextTimeTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nextTimeToFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nextTimeToFocusLost(evt);
            }
        });

        nextTimeKara.setText("～");

        groupNextTime.add(currentSales);
        currentSales.setText("来店日付");
        currentSales.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        currentSales.setMargin(new java.awt.Insets(0, 0, 0, 0));
        currentSales.setOpaque(false);
        currentSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentSalesActionPerformed(evt);
            }
        });

        currentSalesDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        currentSalesDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                currentSalesDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                currentSalesDateFocusLost(evt);
            }
        });

        groupNextTime.add(currentLimit);
        currentLimit.setText("賞美期限");
        currentLimit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        currentLimit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        currentLimit.setOpaque(false);
        currentLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentLimitActionPerformed(evt);
            }
        });

        currentLimitDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        currentLimitDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                currentLimitDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                currentLimitDateFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(nextTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextTimeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextTimeKara)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextTimeTo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(currentSales)
                        .addGap(31, 31, 31)
                        .addComponent(currentSalesDate, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nextReservation)
                            .addComponent(currentLimit))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentLimitDate, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nextReservationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(181, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(currentSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(currentSalesDate, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(nextReservation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextReservationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(currentLimit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(currentLimitDate, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(nextTimeTo, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(nextTimeFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(nextTimeKara, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5.setOpaque(false);

        jLabel20.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel20.setText("ポイント");

        txtUsePoint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtUsePoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUsePoint.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsePointFocusLost(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel23.setText("ポイント");

        jLabel21.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel21.setText("ポイント");

        jLabel18.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel18.setText("使用ポイント");

        jLabel19.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel19.setText("ポイント合計");

        txtAddPoint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtAddPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAddPoint.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAddPointFocusLost(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel16.setText("前回ポイント");

        jLabel22.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel22.setText("ポイント");

        txtPrevPoint.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtPrevPoint.setEditable(false);
        txtPrevPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel17.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel17.setText("加算ポイント");

        txtTotalPoint.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTotalPoint.setEditable(false);
        txtTotalPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btnCalcPoint.setIcon(SystemInfo.getImageIcon("/button/common/refresh_off.jpg"));
        btnCalcPoint.setBorder(null);
        btnCalcPoint.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCalcPoint.setPressedIcon(SystemInfo.getImageIcon("/button/common/refresh_on.jpg"));
        btnCalcPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcPointActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtUsePoint, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPrevPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAddPoint, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTotalPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addComponent(btnCalcPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrevPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAddPoint, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUsePoint, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTotalPoint, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnCalcPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

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
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(commentPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(commentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCalcPointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcPointActionPerformed

        String msg = "";
        msg += "ポイントの再計算を行いますか？\n\n";
        msg += "※ ポイント再計算を行うと手で入力されたポイントは　\n";
        msg += "　　リセットされますので、再度入力してください。\n\n";
        
        int ret = MessageDialog.showYesNoDialog(
                    this, msg, this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.NO_OPTION);

        if (ret == JOptionPane.YES_OPTION) {
            long newPoint = CalculatePoint.calcPoint(sales);
            txtAddPoint.setText(String.valueOf(newPoint));
            updatePreviewPoint();
        }
        
    }//GEN-LAST:event_btnCalcPointActionPerformed

    private void currentLimitDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_currentLimitDateFocusLost
        ShowNextDate();
    }//GEN-LAST:event_currentLimitDateFocusLost

    private void currentLimitDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_currentLimitDateFocusGained
        this.currentLimitDate.getInputContext().setCharacterSubsets(null);
        ShowNextDate();
    }//GEN-LAST:event_currentLimitDateFocusGained

    private void currentLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentLimitActionPerformed
        UpdateButtonEnabled();
    }//GEN-LAST:event_currentLimitActionPerformed

    private void currentSalesDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_currentSalesDateFocusLost
        ShowNextDate();
    }//GEN-LAST:event_currentSalesDateFocusLost

    private void currentSalesDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_currentSalesDateFocusGained
        this.currentSalesDate.getInputContext().setCharacterSubsets(null);
        ShowNextDate();
    }//GEN-LAST:event_currentSalesDateFocusGained

    private void currentSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentSalesActionPerformed
        UpdateButtonEnabled();
    }//GEN-LAST:event_currentSalesActionPerformed
    
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

                    if (isAlaise) {

                        String margin = " ";
                        card.addPrintBuffer( 3, 1, 1, margin + this.customerNoPreview.getText());
                        card.addPrintBuffer( 5, 1, 2, margin + StringUtil.replaceInvalidString(this.customerNamePreview.getText()));

                        int TotalPoint = Integer.valueOf(txtTotalPoint.getText());
                        DecimalFormat fmt = new DecimalFormat("##,##0");
                        String point = AbstractCardCommunication.NumberToWideNumber(
                                fmt.format(TotalPoint));
                        card.addPrintBuffer( 7, 1, 2, margin + point);
                        card.addPrintBuffer( 8, point.length()*2+3, margin + "POINT");
                        card.addPrintBuffer( 10, 1, 1, margin + this.datePreview.getText());
                        card.addPrintBuffer( 11, 1, 1, margin + StringUtil.replaceInvalidString(this.staffNamePreview.getText()));

                        if( (InsertCardDialog.ShowDialog(parentFrame,
                                InsertCardDialog.MODE_PRINT,
                                String.valueOf(customerId).trim())) != InsertCardDialog.STAT_SUCCESS ){
                            return;
                        }

                    } else {

                        card.addPrintBuffer( 1, 1, 1, this.customerNoPreview.getText());
                        card.addPrintBuffer( 2, 1, 2, StringUtil.replaceInvalidString(this.customerNamePreview.getText()));

                        int TotalPoint = Integer.valueOf(txtTotalPoint.getText());
                        DecimalFormat fmt = new DecimalFormat("##,##0");
                        String point = AbstractCardCommunication.NumberToWideNumber(
                                fmt.format(TotalPoint));
                        card.addPrintBuffer( 4, 1, 2, point);
                        card.addPrintBuffer( 5, point.length()*2+3, "POINT");
                        card.addPrintBuffer( 7, 1, 1, this.datePreview.getText());
                        card.addPrintBuffer( 8, 1, 1, StringUtil.replaceInvalidString(this.staffNamePreview.getText()));

                        //コメント欄
                        card.addPrintBuffer(10, 1, 1, StringUtil.replaceInvalidString(this.comment1Preview.getText()));
                        card.addPrintBuffer(11, 1, 1, StringUtil.replaceInvalidString(this.comment2Preview.getText()));
                        card.addPrintBuffer(12, 1, 1, StringUtil.replaceInvalidString(this.comment3Preview.getText()));
                        card.addPrintBuffer(13, 1, 1, StringUtil.replaceInvalidString(this.comment4Preview.getText()));
                        card.addPrintBuffer(14, 1, 1, StringUtil.replaceInvalidString(this.comment5Preview.getText()));
                        card.addPrintBuffer(15, 1, 1, StringUtil.replaceInvalidString(this.comment6Preview.getText()));
                        card.addPrintBuffer(16, 1, 1, StringUtil.replaceInvalidString(this.comment7Preview.getText()));
                        card.addPrintBuffer(17, 1, 1, StringUtil.replaceInvalidString(this.comment8Preview.getText()));
                        card.addPrintBuffer(18, 1, 1, StringUtil.replaceInvalidString(this.comment9Preview.getText()));
                        if( (InsertCardDialog.ShowDialog(parentFrame,
                                InsertCardDialog.MODE_PRINT,
                                String.valueOf(customerId).trim())) != InsertCardDialog.STAT_SUCCESS ){
                            return;
                        }

                    }
                }
            }
            
            // ポイントの登録処理を行う
            long Supplied = 0;
            long Use = 0;
            if( CheckUtil.isNumber(txtAddPoint.getText()) ){
                Supplied = Integer.valueOf(txtAddPoint.getText());
            }
            if( CheckUtil.isNumber(txtUsePoint.getText()) ){
                Use = Integer.valueOf(txtUsePoint.getText());
            }
            
            if( point != null ){
                // 既存データの更新を行う
                this.point.setUsePoint(Use);
                this.point.setSuppliedPoint(Supplied);
                this.point.setCustomerId(customerId);
                if( !this.point.update() ){
                    displayMessage(FAILED_UPDATEPOINT);
                    return;
                }
            }else{
                // ポイント増減があるときのみ、ポイントを登録
                if (PointData.getPointData(shopId, slipNo) == null) {
                    if( Supplied > 0 || Use > 0 ) {
                        // 新規登録
                        PointData data = new PointData( shopId, customerId,
                                slipNo, paymentNo,
                                Use, Supplied );
                        if( !data.insertPointCalculation() ) {
                            displayMessage(FAILED_INSERTPOINT);
                            return;
                        }
                    }
                } else {
                    // 既存データの更新を行う
                    PointData data = new PointData( shopId, customerId,
                            slipNo, paymentNo,
                            Use, Supplied );
                    if( !data.update() ) {
                        displayMessage(FAILED_UPDATEPOINT);
                        return;
                    }
                }
                
                txtAddPoint.setText("0");
                txtUsePoint.setText("0");
            }

            // 退店画面
            if (hairDlg != null) {
                // 前画面から引き継いだ使用ポイントをクリア
                setDiscountUsePoint(0);
                // 退店画面の使用ポイントをクリア
                this.hairDlg.setDiscountUsePoint(0);
            }
            
            // 画面を更新する
            loadDispData();
	}//GEN-LAST:event_btnCardPrintActionPerformed
        
    private void nextTimeToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nextTimeToFocusLost
        ShowNextDate();
    }//GEN-LAST:event_nextTimeToFocusLost
    
    private void nextTimeFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nextTimeFromFocusLost
        ShowNextDate();
    }//GEN-LAST:event_nextTimeFromFocusLost
    
    private void nextReservationDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nextReservationDateFocusLost
        ShowNextDate();
    }//GEN-LAST:event_nextReservationDateFocusLost
    
    
    private void UpdateButtonEnabled() {

        currentSalesDate.setEnabled(false);
        nextReservationDate.setEnabled(false);
        currentLimitDate.setEnabled(false);
        nextTimeTo.setEnabled(false);
        nextTimeFrom.setEnabled(false);

        if (currentSales.isSelected()) {
            currentSalesDate.setEnabled(true);
        } else if (nextReservation.isSelected()) {
            nextReservationDate.setEnabled(true);
        } else if (currentLimit.isSelected()) {
            currentLimitDate.setEnabled(true);
        } else {
            nextTimeTo.setEnabled(true);
            nextTimeFrom.setEnabled(true);
        }

        ShowNextDate();
    }
    
    private void ShowNextDate() {

        if (currentSales.isSelected()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
            
            if( currentSalesDate.getDate() != null ){
                datePreview.setText(sdf.format(currentSalesDate.getDate()));
            }else{
                datePreview.setText("");
            }
            
        } else if (nextReservation.isSelected()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
            
            if( nextReservationDate.getDate() != null ){
                datePreview.setText(sdf.format(nextReservationDate.getDate()));
            }else{
                datePreview.setText("");
            }

        } else if (currentLimit.isSelected()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
            
            if( currentLimitDate.getDate() != null ){
                datePreview.setText(sdf.format(currentLimitDate.getDate()));
            }else{
                datePreview.setText("");
            }

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String str = "";
            if( nextTimeFrom.getDate() != null ){
                str = sdf.format(nextTimeFrom.getDate());
            }
            str += "～";
            if( nextTimeTo.getDate() != null ){
                str += sdf.format(nextTimeTo.getDate());
            }
            datePreview.setText(str);
        }
    }
    
    private void nextTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextTimeActionPerformed
        UpdateButtonEnabled();
    }//GEN-LAST:event_nextTimeActionPerformed
    
    private void nextReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextReservationActionPerformed
        UpdateButtonEnabled();
    }//GEN-LAST:event_nextReservationActionPerformed
    
    private void nextTimeToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nextTimeToFocusGained
        this.nextTimeTo.getInputContext().setCharacterSubsets(null);
        ShowNextDate();
    }//GEN-LAST:event_nextTimeToFocusGained
    
    private void nextTimeFromFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nextTimeFromFocusGained
        this.nextTimeFrom.getInputContext().setCharacterSubsets(null);
        ShowNextDate();
    }//GEN-LAST:event_nextTimeFromFocusGained
    
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
    
    private void commentInput_09CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_09CaretUpdate
        comment9Preview.setText( commentInput_09.getText());
    }//GEN-LAST:event_commentInput_09CaretUpdate
    
    private void commentInput_09FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_09FocusGained
        comment9Preview.setText( commentInput_09.getText());
    }//GEN-LAST:event_commentInput_09FocusGained
    
    private void commentInput_08CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_08CaretUpdate
        comment8Preview.setText( commentInput_08.getText());
    }//GEN-LAST:event_commentInput_08CaretUpdate
    
    private void commentInput_08FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_08FocusGained
        comment8Preview.setText( commentInput_08.getText());
    }//GEN-LAST:event_commentInput_08FocusGained
    
    private void commentInput_07CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_07CaretUpdate
        comment7Preview.setText( commentInput_07.getText());
    }//GEN-LAST:event_commentInput_07CaretUpdate
    
    private void commentInput_07FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_07FocusGained
        comment7Preview.setText( commentInput_07.getText());
    }//GEN-LAST:event_commentInput_07FocusGained
    
    private void commentInput_06CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_06CaretUpdate
        comment6Preview.setText( commentInput_06.getText());
    }//GEN-LAST:event_commentInput_06CaretUpdate
    
    private void commentInput_06FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_06FocusGained
        comment6Preview.setText( commentInput_06.getText());
    }//GEN-LAST:event_commentInput_06FocusGained
    
    private void commentInput_05CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_05CaretUpdate
        comment5Preview.setText( commentInput_05.getText());
    }//GEN-LAST:event_commentInput_05CaretUpdate
    
    private void commentInput_05FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_05FocusGained
        comment5Preview.setText( commentInput_05.getText());
    }//GEN-LAST:event_commentInput_05FocusGained
    
    private void commentInput_04CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_04CaretUpdate
        comment4Preview.setText( commentInput_04.getText());
    }//GEN-LAST:event_commentInput_04CaretUpdate
    
    private void commentInput_04FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_04FocusGained
        comment4Preview.setText( commentInput_04.getText());
    }//GEN-LAST:event_commentInput_04FocusGained
    
    private void commentInput_03CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_03CaretUpdate
        comment3Preview.setText( commentInput_03.getText());
    }//GEN-LAST:event_commentInput_03CaretUpdate
    
    private void commentInput_03FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_03FocusGained
        comment3Preview.setText( commentInput_03.getText());
    }//GEN-LAST:event_commentInput_03FocusGained
    
    private void commentInput_02CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_02CaretUpdate
        comment2Preview.setText( commentInput_02.getText());
    }//GEN-LAST:event_commentInput_02CaretUpdate
    
    private void commentInput_02FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_02FocusGained
        comment2Preview.setText( commentInput_02.getText());
    }//GEN-LAST:event_commentInput_02FocusGained
    
    private void commentInput_01CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_commentInput_01CaretUpdate
        comment1Preview.setText( commentInput_01.getText());
    }//GEN-LAST:event_commentInput_01CaretUpdate
    
    private void commentInput_01FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentInput_01FocusGained
        comment1Preview.setText( commentInput_01.getText());
    }//GEN-LAST:event_commentInput_01FocusGained
    
    private void txtUsePointFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsePointFocusLost
        updatePreviewPoint();
    }//GEN-LAST:event_txtUsePointFocusLost
    
    private void txtAddPointFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddPointFocusLost
        updatePreviewPoint();
    }//GEN-LAST:event_txtAddPointFocusLost
    
    private void txtCustomerName2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerName2FocusLost
        updatePreviewCustomer();
    }//GEN-LAST:event_txtCustomerName2FocusLost
    
    private void txtCustomerName1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerName1FocusLost
        updatePreviewCustomer();
    }//GEN-LAST:event_txtCustomerName1FocusLost
    
    private void titleListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_titleListActionPerformed
    {//GEN-HEADEREND:event_titleListActionPerformed
        if( hasValidEntries == true ) {
            this.populateComments();
        }
    }//GEN-LAST:event_titleListActionPerformed
    
    private void updatePreviewCustomer() {
        Long NowPoint = null;
        if( customerId != null ){
            if (isAlaise) {
                customerNoPreview.setText( "会員No. " + txtCustomerNo.getText() );
            } else {
                customerNoPreview.setText( txtCustomerNo.getText() );
            }
            customerNamePreview.setText( txtCustomerName1.getText() + "　" + txtCustomerName2.getText() + "　様" );
            NowPoint = PointData.getNowPoint(customerId);
        }
        
        // 現在ポイントを表示する
        if( NowPoint == null ) {
            txtPrevPoint.setText( "" );
        } else {
            if( sales != null ){
                point = PointData.getPointData(sales.getShop().getShopID(), sales.getSlipNo() );
                if( point != null ) {
                    // 伝票登録済みデータの場合は、その時の発行ポイントをのぞいたものを計算
                    NowPoint -= (point.getSuppliedPoint() - point.getUsePoint());
                }
            }
            txtPrevPoint.setText( String.valueOf(NowPoint) );
        }
        updatePreviewPoint();
    }
    
    private void updatePreviewPoint() {
        Integer NowPoint = 0;
        Integer AddPoint = 0;
        Integer UsePoint = 0;
        
        if( CheckUtil.isNumber(txtPrevPoint.getText()) ){
            NowPoint = Integer.valueOf(txtPrevPoint.getText());
        }
        if( CheckUtil.isNumber(txtAddPoint.getText()) ){
            AddPoint = Integer.valueOf(txtAddPoint.getText());
        }
        if( CheckUtil.isNumber(txtUsePoint.getText()) ){
            UsePoint = Integer.valueOf(txtUsePoint.getText());
        }
        Integer TotalPoint = NowPoint + AddPoint - UsePoint;
        if( TotalPoint == null ){
            txtTotalPoint.setText( "" );
            pointsPreview.setText( "" );
        }else{
            txtTotalPoint.setText( String.valueOf(TotalPoint) );
            DecimalFormat fmt = new DecimalFormat("##,##0");
            String point = AbstractCardCommunication.NumberToWideNumber(
                    fmt.format(TotalPoint));
            pointsPreview.setText(point + " POINT");
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setFontSize(attr, 18);
            StyleConstants.setBold(attr, true);
            StyledDocument doc = pointsPreview.getStyledDocument();
            doc.setCharacterAttributes(0, point.length(), attr, true);
        }
    }
    
    
    private void txtCustomerNoFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtCustomerNoFocusLost
    {//GEN-HEADEREND:event_txtCustomerNoFocusLost
        MstCustomer      mst  = new MstCustomer();
        mst.setCustomerNo(this.txtCustomerNo.getText());
        //nhanvt start edit 20141014 Bug #31105
        if( this.txtCustomerNo.getText().equals("0")) {
            this.txtChangeEditable(true);
        } else {
            try {
                mst = SelectSameNoData.getMstCustomerByNo(
                        parentFrame,
                        SystemInfo.getConnection(),
                        this.txtCustomerNo.getText(),
                        (SystemInfo.getSetteing().isShareCustomer() ? 0 : shopId));
                
                this.txtChangeEditable(false);
            } catch(Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE,e.getLocalizedMessage(), e);
            }
            if( mst == null )
                mst = new MstCustomer();
            this.txtCustomerNo.setText(mst.getCustomerNo());
            this.txtCustomerName1.setText(mst.getCustomerName(0));
            this.txtCustomerName2.setText(mst.getCustomerName(1));
            customerId = mst.getCustomerID();
        }
    
        updatePreviewCustomer();
        
//        if( mst == null )
//            mst = new MstCustomer();
//        
//        this.txtCustomerNo.setText(mst.getCustomerNo());
//        this.txtCustomerName1.setText(mst.getCustomerName(0));
//        this.txtCustomerName2.setText(mst.getCustomerName(1));
//        customerId = mst.getCustomerID();
//        
//        updatePreviewCustomer();
        //nhanvt end edit 20141014 Bug #31105
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
            staffNamePreview.setText(ms.getStaffName()[0] + "　" + ms.getStaffName()[1]);
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
            this.txtAddPoint.setText("0");
            this.txtUsePoint.setText("0");
            
            updatePreviewCustomer();
        }
    }//GEN-LAST:event_btnCustomerSearchActionPerformed
    
    private void nextReservationDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nextReservationDateFocusGained
        this.nextReservationDate.getInputContext().setCharacterSubsets(null);
        ShowNextDate();
    }//GEN-LAST:event_nextReservationDateFocusGained
    
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
        // スタッフ未設定
        if( staffId == null  ){
            
            //displayMessage(FAILED_STAFF);
            //return false;
            int dialogResult = JOptionPane.showConfirmDialog(this, "担当者の入力がありません。\n" +
                    "担当者名が印字されませんが、よろしいですか？",
                    "カード印刷", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (dialogResult == 0) {
                    //Yes
                    return true;                    
                }else if(dialogResult == 1){
                    //No
                    return false;
                }
        }
        /* GEOBECK end edit 20160905 #54425 */
        
        // 追加ポイント未設定
        if( txtAddPoint.getText().trim().length() == 0 ){
            displayMessage(FAILED_ADDPOINT);
            return false;
        }
        
        // 使用ポイント未設定
        if( txtUsePoint.getText().trim().length() == 0 ){
            displayMessage(FAILED_USEPOINT);
            return false;
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
            //GEOBECK start edit 20160905 #54425
            /* case FAILED_STAFF:
                MessageDialog.showMessageDialog( parent,
                        "スタッフを選択してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break; */
            //GEOBECK end edit 20160905 #54425
            case FAILED_ADDPOINT:
                MessageDialog.showMessageDialog( parent,
                        "追加ポイントを入力してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_USEPOINT:
                MessageDialog.showMessageDialog( parent,
                        "使用ポイントを入力してください", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_INSERTPOINT:
                MessageDialog.showMessageDialog( parent,
                        "ポイントの登録に失敗しました", title,
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_UPDATEPOINT:
                MessageDialog.showMessageDialog( parent,
                        "ポイントの更新に失敗しました", title,
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
    private javax.swing.JButton btnCalcPoint;
    private javax.swing.JButton btnCardPrint;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnCustomerSearch;
    private javax.swing.JButton btnPrintClear;
    private com.geobeck.swing.ImagePanel cardPanel;
    private com.geobeck.swing.JFormattedTextFieldEx comment1Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment2Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment3Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment4Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment5Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment6Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment7Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment8Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment9Preview;
    private javax.swing.JLabel commentInputLabel_08;
    private javax.swing.JLabel commentInputLabel_09;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_01;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_02;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_03;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_04;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_05;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_06;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_07;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_08;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_09;
    private javax.swing.JPanel commentPanel;
    private javax.swing.JRadioButton currentLimit;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo currentLimitDate;
    private javax.swing.JRadioButton currentSales;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo currentSalesDate;
    private com.geobeck.swing.JFormattedTextFieldEx customerNamePreview;
    private com.geobeck.swing.JFormattedTextFieldEx customerNoPreview;
    private com.geobeck.swing.JFormattedTextFieldEx datePreview;
    private javax.swing.ButtonGroup groupNextTime;
    private javax.swing.JComboBox inputStaff;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInputNum;
    private javax.swing.JRadioButton nextReservation;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo nextReservationDate;
    private javax.swing.JRadioButton nextTime;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo nextTimeFrom;
    private javax.swing.JLabel nextTimeKara;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo nextTimeTo;
    private javax.swing.JTextPane pointsPreview;
    private com.geobeck.swing.JFormattedTextFieldEx staffNamePreview;
    private javax.swing.JComboBox titleList;
    private com.geobeck.swing.JFormattedTextFieldEx txtAddPoint;
    private com.geobeck.swing.JFormattedTextFieldEx txtCustomerName1;
    private com.geobeck.swing.JFormattedTextFieldEx txtCustomerName2;
    private com.geobeck.swing.JFormattedTextFieldEx txtCustomerNo;
    private com.geobeck.swing.JFormattedTextFieldEx txtPrevPoint;
    private com.geobeck.swing.JFormattedTextFieldEx txtStaffNo;
    private com.geobeck.swing.JFormattedTextFieldEx txtTotalPoint;
    private com.geobeck.swing.JFormattedTextFieldEx txtUsePoint;
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
            controls.add(txtAddPoint);
            controls.add(txtUsePoint);
            controls.add(currentSales);
            controls.add(currentSalesDate);
            controls.add(nextReservation);
            controls.add(nextReservationDate);
            controls.add(currentLimit);
            controls.add(currentLimitDate);
            controls.add(nextTime);
            controls.add(nextTimeFrom);
            controls.add(nextTimeTo);
            controls.add(titleList);
            controls.add(commentInput_01);
            controls.add(commentInput_02);
            controls.add(commentInput_03);
            controls.add(commentInput_04);
            controls.add(commentInput_05);
            controls.add(commentInput_06);
            controls.add(commentInput_07);
            controls.add(commentInput_08);
            controls.add(commentInput_09);
            
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

    public void setDiscountUsePoint(long discountUsePoint) {
        this.discountUsePoint = discountUsePoint;
    }
}

