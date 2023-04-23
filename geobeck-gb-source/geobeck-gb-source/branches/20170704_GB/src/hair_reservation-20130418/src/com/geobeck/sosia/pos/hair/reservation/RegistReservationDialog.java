/*
 * RegistReservationDialog.java
 *
 * Created on 2007/10/05, 11:50
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.ibm.icu.text.SimpleDateFormat;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.basicinfo.*;
import com.geobeck.sosia.pos.basicinfo.company.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.products.*;
import com.geobeck.sosia.pos.search.customer.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.hair.data.product.*;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.data.account.DataSales;
import com.geobeck.sosia.pos.hair.master.company.*;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.hair.search.product.*;
import com.geobeck.sosia.pos.hair.customer.*;
import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.Course;

/**
 *
 * @author  kanemoto
 */
public class RegistReservationDialog extends AbstractImagePanelEx implements SearchHairProductOpener {
	private	GregorianCalendar   currentDate = new GregorianCalendar();	/** ���t */
	private	RegistReservation   rr = new RegistReservation();	/** �\��o�^�����N���X */
//	private MstStaffs				staffs			=	new MstStaffs();			/** �X�^�b�t���X�g */
	private MstBeds					beds			=	new MstBeds();				/** �{�p�䃊�X�g */
	private ArrayList<DataProportionally> proportionallys	= new ArrayList<DataProportionally>();	/** �� */
	private	int						techIndex			= 0;					/** �o�^�Z�p�C���f�b�N�X */
	
	private	boolean reserved		=	false;
        private int                             mobileFlag               =0;
        private String ctiNo = "";
        private AbstractImagePanelEx parent;
        
	//�w��:true or �t���[�Ffalse �w���t���[��ԕێ�	
	private boolean shimeiFreeFlag = false;	
        
	//�\�񎞊Ԃ�ύX�������Ƀ��b�Z�[�W��\�����邩�ǂ���	
	private boolean noMsgFlag = false;

	private	DataSales lastSales = new DataSales(SystemInfo.getTypeID());
	
	//��ʈʒu��ݒ肷�鎞�̒����l
        private final int MINUS_X = 4;
        private final int MINUS_Y = 30;
		
        private ReservationTimeTablePanel timeTable = null;
        
        private boolean isCheckout = false;
        
        private Integer selectedChargeStaffID = null;

        private Integer selectedBedID = null;
        
        // �V�t�g�}�X�^
        private MstShifts shifts = new MstShifts();
        // �X�^�b�t�V�t�g
        private DataSchedules staffShifts = new DataSchedules();
        
        private JTextField reservationStartTimeField = null;
        
        public void setReservationTimeTablePanel(ReservationTimeTablePanel p){
            timeTable = p;
        }
        
//	private boolean                        isReserveSuccess                 = false ;
        public int getMobileFlag(){
               return mobileFlag ;
       }
        public void setMobileFlag(int mobileFlag){
               this.mobileFlag=mobileFlag;
        }
	
	/**
	 * ���t��ݒ肷��B
	 * @param date ���t
	 */
	public void setDate(java.util.Date date){
            this.date.setDate( date );
            currentDate.setTime( date );
        }
	
	/**
	 * ��ʈʒu��ݒ肷��B
	 * @param point ��ʈʒu���
	 */
	public void setPoint(Point point){
            int posX = point.x - MINUS_X;
            int posY = point.y - MINUS_Y;

            if(this.isDialog()) {
                    ((JDialog)this.getParent().getParent().getParent().getParent()).setLocation(posX, posY);
            } else {
                    this.setLocation(posX, posY);
            }                    
        }

	/** Creates new form RegistReservationDialog */
	public RegistReservationDialog( MstShop shop, java.util.Date date ) {
            this(shop, date, null);
        }
        public RegistReservationDialog( MstShop shop, java.util.Date date, AbstractImagePanelEx p, GregorianCalendar clickTime) {
            this(shop, date, p);

            //�\�񎞊Ԃ�ύX�������Ƀ��b�Z�[�W��\�����邩�ǂ���
            if (clickTime != null) {
                reservationStartTimeField.setText(getFormatTime(date, clickTime));
            }
	}
        public RegistReservationDialog( MstShop shop, java.util.Date date, AbstractImagePanelEx p, GregorianCalendar clickTime, MstCustomer cus) {
            this(shop, date, p, clickTime);
            if(cus != null){
                this.setCustomer(cus);
		// �O��̗\��f�[�^���Z�b�g����
		this.setLastTimeReservationData( cus );                
            }
        }
        public RegistReservationDialog( MstShop shop, java.util.Date date, AbstractImagePanelEx p, GregorianCalendar clickTime, MstCustomer cus, boolean isNextReservation) {
            this(shop, date, p, clickTime);
            if(cus != null){
                this.setCustomer(cus);
		// �O��̗\��f�[�^���Z�b�g����
		this.setLastTimeReservationData( cus );
            }

            this.checkNext.setSelected(isNextReservation);

            // �ޓX��ʂ���J���ꂽ�ꍇ�͍ݓX�{�^���𖳌��ɂ���
            if (isNextReservation) {
                this.receiptButton.setEnabled(false);
            }
        }
        public void setIsCheckOut(boolean b){
            this.isCheckout = b;
            if(b){
                this.customerNo.setEnabled(false);
            }
        }
        public RegistReservationDialog( MstShop shop, java.util.Date date, AbstractImagePanelEx p) {
		parent = p;
                initComponents();
                reservationStartTimeField = (JTextField)reservationStartTime.getEditor().getEditorComponent();
                reservationStartTimeField.setHorizontalAlignment(JTextField.CENTER);
		this.date.setDate( date );
		currentDate.setTime( date );
		this.shop.addItem( shop );
		this.setPath("�\��Ǘ�");

                this.initReservationsColumn();

                if (SystemInfo.getDatabase().startsWith("pos_hair_lim")) {
                    pnlLim.setVisible(true);
                    this.setSize(595, 580);
                } else {
                    pnlLim.setVisible(false);
                    this.setSize(595, 550);
                }
                
                this.init();
                
                reservations.getColumn("�J�n����").setCellEditor(new DefaultCellEditor(getStartTimeComboBox()));
                
                if (SystemInfo.isReservationOnly()) {
                    receiptButton.setEnabled(false);
                }

                if (!SystemInfo.getSetteing().isItoAnalysis()) {
                    checkNext.setVisible(false);
                    checkPreorder.setVisible(false);
                }

                if (shop.getShopID().equals(shop.getShopID())) {
                    lblOtherShop.setVisible(false);
                }
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        charge = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        registPanel = new javax.swing.JPanel();
        searchTechnicButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        shopLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        customerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        jLabel1 = new javax.swing.JLabel();
        date = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        todayButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        customerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        customerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        searchCustomerButton = new javax.swing.JButton();
        chargeStaffLabel = new javax.swing.JLabel();
        chargeStaff = new javax.swing.JComboBox();
        newCustomerButton = new javax.swing.JButton();
        chargeStaffNo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        redButton = new javax.swing.JButton();
        customerInfoButton = new javax.swing.JButton();
        reservationEndTime = reservationEndTime	=	new JFormattedTextField(
            FormatterCreator.createMaskFormatter( "##:##", null, null ) );
        reservationStartTimeLabel = new javax.swing.JLabel();
        reservationEndTimeLabel = new javax.swing.JLabel();
        previewButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        salesInfoPane = new javax.swing.JScrollPane();
        salesInfo = new javax.swing.JTextArea();
        memoLabel3 = new javax.swing.JLabel();
        checkPreorder = new javax.swing.JCheckBox();
        checkNext = new javax.swing.JCheckBox();
        reservationStartTime = new javax.swing.JComboBox();
        Component c = reservationStartTime.getEditor().getEditorComponent();
        PlainDocument doc = (PlainDocument)((JTextComponent)c).getDocument();
        doc.setDocumentFilter(new CustomFilter(5, "0-9:"));
        lblOtherShop = new javax.swing.JLabel();
        reservationTechnicsScrollPane = new javax.swing.JScrollPane();
        reservations = new com.geobeck.swing.JTableEx();
        memoLabel1 = new javax.swing.JLabel();
        noteScrollPane1 = new javax.swing.JScrollPane();
        memoBody1 = new com.geobeck.swing.JTextAreaEx();
        memoLabel2 = new javax.swing.JLabel();
        noteScrollPane2 = new javax.swing.JScrollPane();
        memoBody2 = new com.geobeck.swing.JTextAreaEx();
        reserveButton = new javax.swing.JButton();
        receiptButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        pnlLim = new javax.swing.JPanel();
        chargeStaffLabel1 = new javax.swing.JLabel();
        regStaffNo = new javax.swing.JTextField();
        regStaff = new javax.swing.JComboBox();
        chargeStaffLabel2 = new javax.swing.JLabel();
        insertDateLabel = new javax.swing.JLabel();
        chargeStaffLabel4 = new javax.swing.JLabel();
        updateDateLabel = new javax.swing.JLabel();

        setFocusCycleRoot(true);
        setFocusTraversalPolicyProvider(true);
        setOpaque(false);

        jPanel1.setOpaque(false);

        registPanel.setOpaque(false);

        searchTechnicButton.setIcon(SystemInfo.getImageIcon("/button/master/regist_reserve_menu_off.jpg"));
        searchTechnicButton.setBorderPainted(false);
        searchTechnicButton.setContentAreaFilled(false);
        searchTechnicButton.setPressedIcon(SystemInfo.getImageIcon("/button/master/regist_reserve_menu_on.jpg"));
        searchTechnicButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTechnicButtonActionPerformed(evt);
            }
        });

        clearButton.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg"));
        clearButton.setBorderPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg"));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout registPanelLayout = new javax.swing.GroupLayout(registPanel);
        registPanel.setLayout(registPanelLayout);
        registPanelLayout.setHorizontalGroup(
            registPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registPanelLayout.createSequentialGroup()
                .addComponent(searchTechnicButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(138, Short.MAX_VALUE))
        );
        registPanelLayout.setVerticalGroup(
            registPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(registPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clearButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(searchTechnicButton, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)))
        );

        shopLabel.setText("�X��");

        jLabel2.setText("�ڋqNo.");

        customerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerNo.setColumns(15);
        customerNo.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        customerNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNoFocusLost(evt);
            }
        });

        jLabel1.setText("�\���");

        date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        date.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        date.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dateItemStateChanged(evt);
            }
        });
        date.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dateFocusGained(evt);
            }
        });

        todayButton.setIcon(SystemInfo.getImageIcon("/button/common/today_off.jpg"));
        todayButton.setBorderPainted(false);
        todayButton.setContentAreaFilled(false);
        todayButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/today_on.jpg"));
        todayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todayButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("�ڋq��");

        customerName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerName1.setEditable(false);
        customerName1.setFocusable(false);
        customerName1.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        customerName1.setInputKanji(true);

        customerName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerName2.setEditable(false);
        customerName2.setFocusable(false);
        customerName2.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        customerName2.setInputKanji(true);

        searchCustomerButton.setIcon(SystemInfo.getImageIcon("/button/search/search_customer_off.jpg"));
        searchCustomerButton.setBorderPainted(false);
        searchCustomerButton.setContentAreaFilled(false);
        searchCustomerButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_customer_on.jpg"));
        searchCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCustomerButtonActionPerformed(evt);
            }
        });

        chargeStaffLabel.setText("��S����");

        chargeStaff.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        chargeStaff.setMaximumRowCount(20);
        chargeStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeStaffActionPerformed(evt);
            }
        });

        newCustomerButton.setIcon(SystemInfo.getImageIcon("/button/common/new_registration_off.jpg"));
        newCustomerButton.setBorderPainted(false);
        newCustomerButton.setContentAreaFilled(false);
        newCustomerButton.setFocusable(false);
        newCustomerButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/new_registration_on.jpg"));
        newCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCustomerButtonActionPerformed(evt);
            }
        });

        chargeStaffNo.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        chargeStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chargeStaffNoFocusLost(evt);
            }
        });

        jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        redButton.setIcon(SystemInfo.getImageIcon("/button/reservation/Minimization_off.jpg"));
        redButton.setBorderPainted(false);
        redButton.setContentAreaFilled(false);
        redButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/Minimization_on.jpg"));
        redButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButtonActionPerformed(evt);
            }
        });

        customerInfoButton.setIcon(SystemInfo.getImageIcon("/button/common/customer_off.jpg"));
        customerInfoButton.setBorderPainted(false);
        customerInfoButton.setContentAreaFilled(false);
        customerInfoButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/customer_on.jpg"));
        customerInfoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerInfoButtonActionPerformed(evt);
            }
        });

        reservationEndTime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        reservationEndTime.setEditable(false);
        reservationEndTime.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        reservationEndTime.setFont(new java.awt.Font("MS UI Gothic", 0, 14));

        reservationStartTimeLabel.setText("�\�񎞊�");

        reservationEndTimeLabel.setText("�I������");

        previewButton.setIcon(SystemInfo.getImageIcon("/button/print/preview_off.jpg"));
        previewButton.setBorderPainted(false);
        previewButton.setContentAreaFilled(false);
        previewButton.setPressedIcon(SystemInfo.getImageIcon("/button/print/preview_on.jpg"));
        previewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewButtonActionPerformed(evt);
            }
        });

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        salesInfoPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        salesInfo.setBackground(new java.awt.Color(255, 255, 204));
        salesInfo.setColumns(20);
        salesInfo.setEditable(false);
        salesInfo.setFont(new java.awt.Font("�l�r �o�S�V�b�N", 0, 12));
        salesInfo.setLineWrap(true);
        salesInfo.setRows(5);
        salesInfoPane.setViewportView(salesInfo);

        memoLabel3.setText("�y�O�񐸎Z���z");

        checkPreorder.setText("�d�b�E���O�\��");
        checkPreorder.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkPreorder.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkPreorder.setOpaque(false);
        checkPreorder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPreorderActionPerformed(evt);
            }
        });

        checkNext.setText("���X������\��");
        checkNext.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkNext.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkNext.setOpaque(false);
        checkNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNextActionPerformed(evt);
            }
        });

        reservationStartTime.setEditable(true);
        reservationStartTime.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        reservationStartTime.setMaximumRowCount(10);
        reservationStartTime.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                reservationStartTimePopupMenuWillBecomeVisible(evt);
            }
        });
        reservationStartTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                reservationStartTimeItemStateChanged(evt);
            }
        });

        lblOtherShop.setFont(new java.awt.Font("MS UI Gothic", 1, 15));
        lblOtherShop.setForeground(java.awt.Color.red);
        lblOtherShop.setText("���X�܂̗\��ł�");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chargeStaffLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chargeStaffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chargeStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(reservationEndTimeLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(reservationEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(reservationStartTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(reservationStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(11, 11, 11))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(269, 269, 269))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(shopLabel))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                                .addComponent(lblOtherShop)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(customerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(redButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(todayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(previewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addComponent(memoLabel3))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(searchCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(newCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(checkPreorder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(checkNext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(35, 35, 35)))
                                        .addComponent(salesInfoPane, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))
                                .addGap(0, 0, 0)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(customerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(customerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customerInfoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(registPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(218, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(salesInfoPane, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shopLabel)
                    .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOtherShop, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(previewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(redButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(todayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(memoLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(searchCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(customerNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(11, 11, 11)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(customerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(customerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(customerInfoButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(chargeStaffLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chargeStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chargeStaffNo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(reservationStartTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(reservationStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkNext))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkPreorder)
                    .addComponent(reservationEndTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reservationEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(registPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        reservationTechnicsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));

        reservations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "�Z�p����", "�Z�p��", "<html>�{�p<br>����</html>", "�J�n����", "�{�p�S��", "�w��", "�{�p��", "�폜"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reservations.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        reservations.setColumnSelectionAllowed(false);
        reservations.setRowSelectionAllowed(false);
        reservations.setSelectionBackground(new java.awt.Color(220, 220, 220));
        reservations.setSelectionForeground(new java.awt.Color(0, 0, 0));
        reservations.getTableHeader().setReorderingAllowed(false);
        reservations.getTableHeader().setResizingAllowed(false);
        //this.initReservationsColumn();
        reservations.setDefaultRenderer(String.class, new RegistReservationTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(reservations, SystemInfo.getTableHeaderRenderer());
        reservations.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        TableColumnModel model = reservations.getColumnModel();
        model.getColumn(2).setCellEditor(new IntegerCellEditor(new JTextField()));
        reservations.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                reservationsPropertyChange(evt);
            }
        });
        reservationTechnicsScrollPane.setViewportView(reservations);

        memoLabel1.setText("�\�񃁃�");

        noteScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        memoBody1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        memoBody1.setColumns(20);
        memoBody1.setLineWrap(true);
        memoBody1.setRows(5);
        memoBody1.setTabSize(4);
        memoBody1.setInputKanji(true);
        noteScrollPane1.setViewportView(memoBody1);

        memoLabel2.setText("�O�񗈓X����");

        noteScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        memoBody2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        memoBody2.setColumns(20);
        memoBody2.setEditable(false);
        memoBody2.setLineWrap(true);
        memoBody2.setRows(5);
        memoBody2.setTabSize(4);
        memoBody2.setInputKanji(true);
        noteScrollPane2.setViewportView(memoBody2);

        reserveButton.setIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_off.jpg"));
        reserveButton.setBorderPainted(false);
        reserveButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_on.jpg"));
        reserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserveButtonActionPerformed(evt);
            }
        });

        receiptButton.setIcon(SystemInfo.getImageIcon("/button/reservation/stay_off.jpg"));
        receiptButton.setBorderPainted(false);
        receiptButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/stay_on.jpg"));
        receiptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receiptButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/reservation/delete_reservation_off.jpg"));
        deleteButton.setBorderPainted(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/delete_reservation_on.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        pnlLim.setOpaque(false);

        chargeStaffLabel1.setText("�o�^��");

        regStaffNo.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        regStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        regStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                regStaffNoFocusLost(evt);
            }
        });

        regStaff.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        regStaff.setMaximumRowCount(20);
        regStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        regStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regStaffActionPerformed(evt);
            }
        });

        chargeStaffLabel2.setText("�o�^����");

        insertDateLabel.setText("2011/12/31 21:45");

        chargeStaffLabel4.setText("�X�V����");

        updateDateLabel.setText("2011/12/31 21:45");

        javax.swing.GroupLayout pnlLimLayout = new javax.swing.GroupLayout(pnlLim);
        pnlLim.setLayout(pnlLimLayout);
        pnlLimLayout.setHorizontalGroup(
            pnlLimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLimLayout.createSequentialGroup()
                .addComponent(chargeStaffLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regStaffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regStaff, 0, 153, Short.MAX_VALUE))
            .addGroup(pnlLimLayout.createSequentialGroup()
                .addComponent(chargeStaffLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insertDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(pnlLimLayout.createSequentialGroup()
                .addComponent(chargeStaffLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlLimLayout.setVerticalGroup(
            pnlLimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLimLayout.createSequentialGroup()
                .addGroup(pnlLimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(regStaff, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(chargeStaffLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regStaffNo, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chargeStaffLabel2)
                    .addComponent(insertDateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chargeStaffLabel4)
                    .addComponent(updateDateLabel)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reservationTechnicsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(memoLabel1)
                                    .addComponent(noteScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noteScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                                    .addComponent(memoLabel2)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(pnlLim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(reserveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(receiptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reservationTechnicsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(memoLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noteScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(memoLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noteScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(receiptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reserveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlLim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void reservationStartTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_reservationStartTimeItemStateChanged
        //�\�񎞊Ԃ����t���b�V������
        //refreshReservationTime();
        this.dateItemStateChanged(evt);
    }//GEN-LAST:event_reservationStartTimeItemStateChanged

    private void reservationStartTimePopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_reservationStartTimePopupMenuWillBecomeVisible

        for (int i = 0; i < reservationStartTime.getItemCount(); i++) {
            if (reservationStartTime.getItemAt(i).toString().equals(reservationStartTimeField.getText())) {
                reservationStartTime.setSelectedIndex(i);
                break;
            }
        }
        
    }//GEN-LAST:event_reservationStartTimePopupMenuWillBecomeVisible

    private void checkPreorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPreorderActionPerformed
        if (checkPreorder.isSelected()) {
            checkNext.setSelected(false);
        }
    }//GEN-LAST:event_checkPreorderActionPerformed

    private void checkNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNextActionPerformed
        if (checkNext.isSelected()) {
            checkPreorder.setSelected(false);
        }
    }//GEN-LAST:event_checkNextActionPerformed

    private void reservationsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_reservationsPropertyChange

        int row = reservations.getSelectedRow();
        int col = reservations.getSelectedColumn();

        if(row < 0 || col != 2) return;

        Long value = Double.valueOf(reservations.getValueAt(row, col).toString()).longValue();

        if (value != null) {
            DataReservationDetail drd = (DataReservationDetail)rr.getReservation().get(row);
            if (drd.getCourseFlg() == null) {
                //�Z�p�̏ꍇ
                drd.getTechnic().setOperationTime((Integer)reservations.getValueAt(row, 2));
            } else if (drd.getCourseFlg() == 1) {
                //�R�[�X�_��̏ꍇ
                drd.getCourse().setOperationTime((Integer)reservations.getValueAt(row, 2));
            } else if (drd.getCourseFlg() == 2) {
                //�����R�[�X�̏ꍇ
                drd.getConsumptionCourse().setOperationTime((Integer)reservations.getValueAt(row, 2));
            }
            this.resetReservationTime(row);
        }

    }//GEN-LAST:event_reservationsPropertyChange

    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewButtonActionPerformed
//�\��ꗗ��ʂ��Ăяo��
	    ReservationTimeTableWholeDisplayPanel rtt	=	new ReservationTimeTableWholeDisplayPanel(date.getDate());
	    SwingUtil.openAnchorDialog( parentFrame, true, rtt, "�\��ꗗ", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
	    rtt	=	null;
    }//GEN-LAST:event_previewButtonActionPerformed

    private void customerInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerInfoButtonActionPerformed

        MstCustomer customer = rr.getCustomer();
        if(customer.getCustomerID() != null){
            
            MstCustomerPanel mcp = null;

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                mcp = new MstCustomerPanel(customer.getCustomerID() , false, false, true);                
                SwingUtil.openAnchorDialog( this.parentFrame, true, mcp, "�ڋq���", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            rr.setCustomer(mcp.getCustomer());

            customerName1.setText(mcp.getCustomer().getCustomerName(0));
            customerName2.setText(mcp.getCustomer().getCustomerName(1));
        }
    }//GEN-LAST:event_customerInfoButtonActionPerformed

    private void redButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButtonActionPerformed

                if( this.getSelectedShop() == null ) return;

                //�_�C�A���O���k������B
                RegistReservationSmallDialog rrp = new RegistReservationSmallDialog( this.getSelectedShop(), date.getDate(), this);
		openAnchorDialog( this.parentFrame, true, rrp, this.getTitle());

		((JDialog)rrp.getParent().getParent().getParent().getParent()).dispose();
                rrp	=	null;
		System.gc();

                //�_�C�A���O�����ɖ߂��B
                if(this.isDialog()) {
                        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(true);
                } else {
                        this.setVisible(true);
                }                    

    }//GEN-LAST:event_redButtonActionPerformed

    private void dateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dateItemStateChanged

        noMsgFlag = true;
        //�\�񎞊Ԃ����t���b�V������
        refreshReservationTime();
        noMsgFlag = false;
        
        if (parent != null) {
            if (parent instanceof ReservationStatusPanel) {
                ((ReservationStatusPanel)parent).setDate(date.getDate());
            } else if (parent instanceof ReservationTimeTablePanel) {
                ((ReservationTimeTablePanel)parent).setDate(date.getDate());
                if (reservationStartTimeField.getText().length() > 0 && !reservationStartTimeField.getText().equals("09:00")) {
                    int hour = Integer.parseInt(reservationStartTimeField.getText().replaceAll(":.+", ""));
                    int minute = Integer.parseInt(reservationStartTimeField.getText().replaceAll(".+:", ""));
                    ((ReservationTimeTablePanel)parent).setAdjustScrollToTime(hour, minute);
                }
            }
        }
            
    }//GEN-LAST:event_dateItemStateChanged

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

            if (!this.shimeiFreeFlag) {
                jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
                jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
                this.shimeiFreeFlag=true;
                setChargeFlagToReserve( true );
            } else {
                jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
                jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));
                this.shimeiFreeFlag=false;
                setChargeFlagToReserve(false);
            }

	}//GEN-LAST:event_jButton1ActionPerformed

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
		this.delete();
	}//GEN-LAST:event_deleteButtonActionPerformed

	private void receiptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receiptButtonActionPerformed
		this.receipt();
	}//GEN-LAST:event_receiptButtonActionPerformed

	private void reserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveButtonActionPerformed
		this.reserve();
	}//GEN-LAST:event_reserveButtonActionPerformed

        private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chargeStaffNoFocusLost

	    if (!chargeStaffNo.getText().equals("")) {

                chargeStaffNo.setName("1");
                this.setStaff(chargeStaff, chargeStaffNo.getText());
                chargeStaffNo.setName(null);
                
                rr.getReservation().setStaff( (MstStaff)chargeStaff.getSelectedItem() );

		setStaffDataToReserve( true );
                
		//�w���t���[�Ή�
                if (!this.getTitle().equals("�\��m�F")) {
                    this.shimeiFreeFlag=true;
                    this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
                    this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
                }

	    } else {
		
		chargeStaff.setSelectedIndex(0);
	    }
        }//GEN-LAST:event_chargeStaffNoFocusLost

        private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed

            if (chargeStaffNo.getName() != null) return;

	    MstStaff ms= (MstStaff)chargeStaff.getSelectedItem();
	    
	    if (chargeStaff.getSelectedIndex() == 0) {
		chargeStaffNo.setText("");
	    }
	    
            if (ms.getStaffID() != null) {
		chargeStaffNo.setText(ms.getStaffNo());
	    }
                
	    if(!this.isShowing()) {
                return;
	    }

	    rr.getReservation().setStaff( (MstStaff)chargeStaff.getSelectedItem() );

            if( chargeStaff.getSelectedIndex() == 0 ) {

                if (!this.getTitle().equals("�\��m�F")) {
                    //�w���t���[�Ή�
                    this.shimeiFreeFlag = false;
                    this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
                    this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));
                }

                setStaffDataToReserve( false );

            } else {

                if (!this.getTitle().equals("�\��m�F")) {
                    //�w���t���[�Ή�
                    this.shimeiFreeFlag = true;
                    this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
                    this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
                }

                setStaffDataToReserve( true );
            }

        }//GEN-LAST:event_chargeStaffActionPerformed

	
	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
		this.closeReservationPanel();
	}//GEN-LAST:event_closeButtonActionPerformed
	
	private void newCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCustomerButtonActionPerformed

                rr.customer.clear();
                customerNo.setText("0");
		this.setCustomer();
		customerName1.requestFocusInWindow();
                
                // �����̔Ԃ��L���ȏꍇ
                if (((MstShop)shop.getSelectedItem()).getAutoNumber() == 1) {
                    
                    int ret = MessageDialog.showYesNoDialog(
                                this,
                                "�ڋqNo.�̎����̔Ԃ��s���܂����H",
                                this.getTitle(),
                                JOptionPane.QUESTION_MESSAGE,
                                JOptionPane.NO_OPTION);
                    
                    if (ret == JOptionPane.YES_OPTION) {
                        
                        try {

                            ConnectionWrapper con = SystemInfo.getConnection();
                            String prefix = ((MstShop)shop.getSelectedItem()).getPrefixString();
                            int length = ((MstShop)shop.getSelectedItem()).getSeqLength().intValue();
                            
                            customerNo.setText(MstCustomer.getNewCustomerNo(con, prefix, length));
                            
                        } catch (Exception e) {
                            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                        }
                    }
                }

	}//GEN-LAST:event_newCustomerButtonActionPerformed
				
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
		this.clearReservation();
	}//GEN-LAST:event_clearButtonActionPerformed
	
	private void searchTechnicButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTechnicButtonActionPerformed
		SystemInfo.getLogger().log(Level.INFO, "�Z�p����");
		SearchHairProductDialog	spd = new SearchHairProductDialog( parentFrame, true, this, 1, (MstShop)shop.getSelectedItem(), rr.getCustomer());
		spd.setVisible(true);

		spd.dispose();
		spd = null;
	}//GEN-LAST:event_searchTechnicButtonActionPerformed
		
	private void searchCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerButtonActionPerformed
		SystemInfo.getLogger().log(Level.INFO, "�ڋq����");
		SearchCustomerDialog sc = new SearchCustomerDialog( parentFrame, true);
		sc.setShopID( ( SystemInfo.getSetteing().isShareCustomer() ? 0 : ( (MstShop)shop.getSelectedItem() ).getShopID() ) );
		sc.setVisible(true);

		if ( sc.getSelectedCustomer() != null &&
		      !sc.getSelectedCustomer().getCustomerID().equals(""))
		{
		    customerNo.setText(sc.getSelectedCustomer().getCustomerNo());
		    this.setCustomer(sc.getSelectedCustomer().getCustomerID());
		    customerInfoButton.setEnabled(true);
                    chargeStaff.requestFocusInWindow();
		}
		
		sc.dispose();
		sc = null;
	}//GEN-LAST:event_searchCustomerButtonActionPerformed
	
	private void todayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todayButtonActionPerformed
		date.setDate(((MstShop)shop.getSelectedItem()).getSystemDate().getTime());
	}//GEN-LAST:event_todayButtonActionPerformed
	
	private void dateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateFocusGained
		date.getInputContext().setCharacterSubsets(null);
	}//GEN-LAST:event_dateFocusGained
	
	private void customerNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNoFocusLost
            if(!this.isCheckout){
                this.setCustomer();
            }
	}//GEN-LAST:event_customerNoFocusLost

        private void regStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_regStaffNoFocusLost

            if (!regStaffNo.getText().equals("")) {

                regStaffNo.setName("1");
                this.setStaff(regStaff, regStaffNo.getText());
                regStaffNo.setName(null);

                rr.getReservation().setRegStaff( (MstStaff)regStaff.getSelectedItem() );

	    } else {

		regStaff.setSelectedIndex(0);
	    }

        }//GEN-LAST:event_regStaffNoFocusLost

        private void regStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regStaffActionPerformed

            if (regStaffNo.getName() != null) return;

	    MstStaff ms= (MstStaff)regStaff.getSelectedItem();

	    if (regStaff.getSelectedIndex() == 0) {
		regStaffNo.setText("");
	    }

            if (ms.getStaffID() != null) {
		regStaffNo.setText(ms.getStaffNo());
	    }

	    if(!this.isShowing()) {
                return;
	    }

	    rr.getReservation().setRegStaff( (MstStaff)regStaff.getSelectedItem() );
            
        }//GEN-LAST:event_regStaffActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup charge;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JLabel chargeStaffLabel;
    private javax.swing.JLabel chargeStaffLabel1;
    private javax.swing.JLabel chargeStaffLabel2;
    private javax.swing.JLabel chargeStaffLabel4;
    private javax.swing.JTextField chargeStaffNo;
    private javax.swing.JCheckBox checkNext;
    private javax.swing.JCheckBox checkPreorder;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton customerInfoButton;
    private com.geobeck.swing.JFormattedTextFieldEx customerName1;
    private com.geobeck.swing.JFormattedTextFieldEx customerName2;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo date;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel insertDateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblOtherShop;
    private com.geobeck.swing.JTextAreaEx memoBody1;
    private com.geobeck.swing.JTextAreaEx memoBody2;
    private javax.swing.JLabel memoLabel1;
    private javax.swing.JLabel memoLabel2;
    private javax.swing.JLabel memoLabel3;
    private javax.swing.JButton newCustomerButton;
    private javax.swing.JScrollPane noteScrollPane1;
    private javax.swing.JScrollPane noteScrollPane2;
    private javax.swing.JPanel pnlLim;
    private javax.swing.JButton previewButton;
    private javax.swing.JButton receiptButton;
    private javax.swing.JButton redButton;
    private javax.swing.JComboBox regStaff;
    private javax.swing.JTextField regStaffNo;
    private javax.swing.JPanel registPanel;
    private javax.swing.JFormattedTextField reservationEndTime;
    private javax.swing.JLabel reservationEndTimeLabel;
    private javax.swing.JComboBox reservationStartTime;
    private javax.swing.JLabel reservationStartTimeLabel;
    private javax.swing.JScrollPane reservationTechnicsScrollPane;
    private com.geobeck.swing.JTableEx reservations;
    private javax.swing.JButton reserveButton;
    private javax.swing.JTextArea salesInfo;
    private javax.swing.JScrollPane salesInfoPane;
    private javax.swing.JButton searchCustomerButton;
    private javax.swing.JButton searchTechnicButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    private javax.swing.JButton todayButton;
    private javax.swing.JLabel updateDateLabel;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * �\��o�^��ʗpFocusTraversalPolicy
	 */
	private	RegistReservationFocusTraversalPolicy	ftp	=
		new RegistReservationFocusTraversalPolicy();
	
	/**
	 * �\��o�^��ʗpFocusTraversalPolicy���擾����B
	 * @return �\��o�^��ʗpFocusTraversalPolicy
	 */
	public RegistReservationFocusTraversalPolicy getFocusTraversalPolicy() {
		return	ftp;
	}
	
	public boolean isReserved() {
		return reserved;
	}
	
	private void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
	/**
	 * ���ׂ̗������������B
	 */
	private void initReservationsColumn() {
                if (((MstShop)shop.getSelectedItem()).isBed()) {
                    //�{�p��L
                    reservations.getColumnModel().getColumn(0).setPreferredWidth(80);	// �Z�p����
                    reservations.getColumnModel().getColumn(1).setPreferredWidth(110);  // �Z�p��
                    reservations.getColumnModel().getColumn(2).setPreferredWidth(40);   // �{�p����
                    reservations.getColumnModel().getColumn(3).setPreferredWidth(70);   // �J�n����
                    reservations.getColumnModel().getColumn(4).setPreferredWidth(80);	// �{�p�S��
                    reservations.getColumnModel().getColumn(5).setPreferredWidth(30);	// �w��
                    reservations.getColumnModel().getColumn(6).setPreferredWidth(80);	// �{�p��
                    reservations.getColumnModel().getColumn(7).setPreferredWidth(48);	// �폜
                } else {
                    //�{�p�䖳
                    reservations.getColumnModel().getColumn(0).setPreferredWidth(100);	// �Z�p����
                    reservations.getColumnModel().getColumn(1).setPreferredWidth(150);  // �Z�p��
                    reservations.getColumnModel().getColumn(2).setPreferredWidth(40);   // �{�p����
                    reservations.getColumnModel().getColumn(3).setPreferredWidth(70);   // �J�n����
                    reservations.getColumnModel().getColumn(4).setPreferredWidth(110);	// �{�p�S��
                    reservations.getColumnModel().getColumn(5).setPreferredWidth(30);	// �w��
                    reservations.getColumnModel().getColumn(7).setPreferredWidth(48);	// �폜

                    //�{�p���̍폜
                    TableColumn column = reservations.getColumn("�{�p��");
                    reservations.removeColumn(column);
                }
		
		SwingUtil.clearTable(reservations);
	}
	
	/**
	 * �������������s��
	 */
	private void init() {
                // �{�p�䃊�X�g���擾����
		this.getMstBeds();
		// �����X�g���擾����
		this.getProportionallys();

                // �V�t�g�}�X�^���擾����
                this.getMstShifts();

                // �X�^�b�t�V�t�g���擾����
                this.getStaffShifts();
                
                addMouseCursorChange();
		setListener();

                rr.setShop(this.getSelectedShop());
		rr.init();
                this.setStaffs();

//		registPanel.setVisible(SystemInfo.getCurrentShop().getShopID() != 0);
//		searchCustomerButton.setVisible(SystemInfo.getCurrentShop().getShopID() != 0);
//		searchTechnicButton.setVisible(SystemInfo.getCurrentShop().getShopID() != 0);
//		customerNo.setEnabled(SystemInfo.getCurrentShop().getShopID() != 0);

                deleteButton.setEnabled( false );
		customerInfoButton.setEnabled(false);
                
                
                reservationStartTime.removeAllItems();
                
                Integer term        = ((MstShop)shop.getSelectedItem()).getTerm();            // ���ԒP��
                Integer openHour    = ((MstShop)shop.getSelectedItem()).getOpenHour();       // �J�X��
                Integer openMinute  = ((MstShop)shop.getSelectedItem()).getOpenMinute();     // �J�X��
                Integer closeHour   = ((MstShop)shop.getSelectedItem()).getCloseHour();      // �X��
                Integer closeMinute = ((MstShop)shop.getSelectedItem()).getCloseMinute();    // �X��
                
                if (openHour != null && closeHour != null && term != null) {

                    for (int h = openHour; h <= closeHour; h++) {
                        for (int m = 0; m < 60; m += term) {
                            // �J�X���Ԃ��O�̏ꍇ
                            if (h == openHour && m < openMinute) continue;
                            // �X���Ԃ���̏ꍇ
                            if (h == closeHour && closeMinute <= m) break;
                            // ���ԃZ�b�g
                            reservationStartTime.addItem(String.format("%1$02d", h) + ":" + String.format("%1$02d", m));
                        }
                    }
                }

                reservationStartTime.setSelectedIndex(-1);
                reservationStartTime.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        reservationStartTimeField.selectAll();
                    }
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        int row = reservations.getSelectedRow();
                        int col = reservations.getSelectedColumn();
                        
                        //�\�񎞊Ԃ����t���b�V������
                        refreshReservationTime();
                        
                        if (row >= 0) reservations.setRowSelectionInterval(row,row);
                        if (col >= 0) reservations.setColumnSelectionInterval(col, col);
                    }
                });
                
                reservationStartTime.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        if (evt.getKeyCode() == evt.VK_ENTER) {
                            //�\�񎞊Ԃ����t���b�V������
                            refreshReservationTime();
                        }
                    }
                });

                insertDateLabel.setText("");
                updateDateLabel.setText("");
	}

        private String getFormatTime(String time) {

            String s = time;
            
            if (s.matches("\\d+:\\d+")) {

                int h = Integer.parseInt(s.replaceAll(":.+", ""));
                int m = Integer.parseInt(s.replaceAll(".+:", ""));
                s = String.format("%1$02d", h) + ":" + String.format("%1$02d", m);

            } else {

                s = s.replace(":", "");

                if (s.length() < 5 && CheckUtil.isNumber(s)) {

                    if (s.length() == 1) {
                        s = "0" + s + ":00";
                    } else if (s.length() == 2) {
                        s = s + ":00";
                    } else if (s.length() == 3) {
                        s = "0" + s.substring(0, 1) + ":" + s.substring(1);
                    } else {
                        s = s.substring(0, 2) + ":" + s.substring(2);
                    }

                } else {
                    s = "";
                }
            }
            
            return s;
        }
        
	private void setStaffs()
	{
            MstStaffs staffs = new MstStaffs();
			
            staffs.setShopIDList(((MstShop)shop.getSelectedItem()).getShopID().toString());

            try {
                
                staffs.load(SystemInfo.getConnection(), true);

		for (MstStaff ms : staffs) {
                    if (ms.isDisplay()) {
                        chargeStaff.addItem(ms);
                        regStaff.addItem(ms);
                    }
		}
                
		chargeStaff.setSelectedIndex(0);
		regStaff.setSelectedIndex(0);
                
            } catch(SQLException e) {
                
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
	}
        
	/**
	 * �\��f�[�^��ǂݍ���
	 */
	public void LoadReservation( DataReservation dr ) {
	    
		if (dr == null) {
		    
		    this.setTitle("�\��o�^");
                    
                    // �V�K�o�^���\������������t�̏ꍇ�͓d�b�E���O�\��Ƀ`�F�b�N����
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    int reservationDay = Integer.parseInt(sdf.format(date.getDate()));
                    int today = Integer.parseInt(sdf.format(Calendar.getInstance().getTime()));
                    if (today < reservationDay && !checkNext.isSelected()) {
                        checkPreorder.setSelected(true);
                    }

                    memoBody1.setText(this.getCtiNo());

		    return;
		    
		} else {
		    
		    this.setTitle("�\��m�F");


		}
		              
                rr.setCustomer( dr.getCustomer() );
		rr.setReservation(dr);
		rr.loadReservation(dr.getReservationNo());
		showReservations();

//		reservations.updateUI();
		
                TimeSchedule ts=new TimeSchedule();
//		searchCustomerButton.setEnabled( false );
		searchCustomerButton.setEnabled( dr.getStatus().equals(1) );
		newCustomerButton.setEnabled( false );
//		customerNo.setEditable( false );
		customerNo.setEditable( dr.getStatus().equals(1) );
//		date.setEditable( false );
		todayButton.setEnabled( false );
//		clearButton.setVisible( false );
		deleteButton.setEnabled( true );

                if (this.getTitle().equals("�\��m�F")
                        && rr.getReservation().getReservationNo() != null
                        && 0 < rr.getReservation().getReservationNo()
                        && rr.getReservation().getStatus() != null
                        && 1 < rr.getReservation().getStatus())
                {
                    reserveButton.setEnabled(false);
                    deleteButton.setEnabled(false);

                    if (2 < rr.getReservation().getStatus()) {
                        receiptButton.setEnabled(false);
                    } else {
			receiptButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
			receiptButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
                        receiptButton.setName("�X�V");
                    }
                }

                if (dr.getMobileFlag() == ts.getMobileFlag() || dr.getMobileFlag() == ts.getMobileCancelFlag()) {
                    try {
                        ConnectionWrapper con = SystemInfo.getConnection();
                        dr.upDateMobileFlag(con);
                    } catch(SQLException e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    }

                    if (dr.getMobileFlag() == ts.getMobileFlag()) {
                        lblOtherShop.setText("WEB�\�񂪓���܂���");
                        lblOtherShop.setVisible(true);
                        lblOtherShop.setForeground(new Color(0,153,255));
                    } else if (dr.getMobileFlag() == ts.getMobileCancelFlag()) {
                        lblOtherShop.setText("�L�����Z������܂���");
                        lblOtherShop.setVisible(true);
                        reserveButton.setEnabled(false);
                        receiptButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                    }
                 }
	}
	
	/**
	 * �\��f�[�^��\������B
	 */
	private void showReservations() {
            
		if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();
		SwingUtil.clearTable(reservations);
		
		customerNo.setText(rr.getCustomer().getCustomerNo());
		customerName1.setText(rr.getCustomer().getCustomerName(0));
		customerName2.setText(rr.getCustomer().getCustomerName(1));

                customerInfoButton.setEnabled(rr.getCustomer().getCustomerID() != null);

		try {
		    MstCustomer cus = new MstCustomer(rr.getCustomer().getCustomerID());
		    cus.load(SystemInfo.getConnection());
		    showCustomerSalesInfo(cus);
		} catch(SQLException e) {
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		//chargeNamed.setSelected( rr.getReservation().getDesignated() );
		//chargeFree.setSelected( !rr.getReservation().getDesignated() );
		//�w���t���[�Ή�
		this.shimeiFreeFlag=rr.getReservation().getDesignated();
		if(this.shimeiFreeFlag){
			this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
			this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));		
		}else{
			this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
			this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));			
		}
		
		chargeStaff.setSelectedIndex(0);
		regStaff.setSelectedIndex(0);
                
                if (rr.getReservation().getStaff() != null) {
                    setStaff(chargeStaff , rr.getReservation().getStaff().getStaffID());
                }
                if (rr.getReservation().getRegStaff() != null) {
                    setStaff(regStaff , rr.getReservation().getRegStaff().getStaffID());
                }

//		for( int i = 1; i < chargeStaff.getItemCount(); i++ )
//		{
//			if( ( (MstStaff)chargeStaff.getItemAt( i ) ).getStaffID().intValue() == rr.getReservation().getStaff().getStaffID().intValue() )
//			{
//				chargeStaff.setSelectedIndex( i );
//			}
//		}

		GregorianCalendar temp = new GregorianCalendar();
                
		if (0 < rr.getReservation().size()) {
                    
                    temp.setTime(rr.getReservation().get(0).getReservationDatetime().getTime());

                    if(temp.get(Calendar.HOUR_OF_DAY) < rr.getShop().getOpenHour() ||
                            (temp.get(Calendar.HOUR_OF_DAY) == rr.getShop().getOpenHour() &&
                            temp.get(Calendar.MINUTE) < rr.getShop().getOpenMinute()))
                    {
                        
                        temp.add(Calendar.DAY_OF_MONTH, -1);
                    }
                    
                    // �\�񃁃�
                    memoBody1.setText(rr.getReservation().getComment());

                    // ���X������\��
                    checkNext.setSelected(rr.getReservation().getNextFlag() == 1);
                    
                    // �d�b�E���O�\��
                    checkPreorder.setSelected(rr.getReservation().getPreorderFlag() == 1);
                    
                    // �O��̔̔��f�[�^(�O�񗈓X����)���Z�b�g����
                    setLastTimeSalesData(rr.getReservation().getCustomer());

                    // �o�^�A�X�V����
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    insertDateLabel.setText(sdf.format(rr.getReservation().getInsertDate().getTime()));
                    updateDateLabel.setText(sdf.format(rr.getReservation().getUpdateDate().getTime()));
                    
		} else {

                    temp.setTime(rr.getDate().getTime());
                }

                //�\�񎞊ԁA�I������
                setReservationTime(temp.getTime());

                //�\���
		date.setDate(temp.getTime());
		
		for(DataReservationDetail drd : rr.getReservation()) {
                    if (drd.getCourseFlg() == null) {
                        this.addReservationRow(drd);
                    } else if (drd.getCourseFlg() == 1) {
                        this.addReservationCourseRow(drd);
                    } else if (drd.getCourseFlg() == 2) {
                        this.addReservationConsumptionCourseRow(drd);
                    }
		}
		
	}
	
	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange() {
		SystemInfo.addMouseCursorChange(searchCustomerButton);
		SystemInfo.addMouseCursorChange(todayButton);
		SystemInfo.addMouseCursorChange(jButton1);
		SystemInfo.addMouseCursorChange(searchTechnicButton);
		SystemInfo.addMouseCursorChange(clearButton);
		SystemInfo.addMouseCursorChange(reserveButton);
		SystemInfo.addMouseCursorChange(receiptButton);
		SystemInfo.addMouseCursorChange(deleteButton);
		SystemInfo.addMouseCursorChange(previewButton);
		SystemInfo.addMouseCursorChange(newCustomerButton);
		SystemInfo.addMouseCursorChange(redButton);
		SystemInfo.addMouseCursorChange(closeButton);
		SystemInfo.addMouseCursorChange(customerInfoButton);
	}
	
	/**
	 * �R���|�[�l���g�̊e���X�i�[���Z�b�g����B
	 */
	private void setListener() {
		customerNo.addKeyListener(SystemInfo.getMoveNextField());
		customerNo.addFocusListener(SystemInfo.getSelectText());
		customerName1.addKeyListener(SystemInfo.getMoveNextField());
		customerName1.addFocusListener(SystemInfo.getSelectText());
		customerName2.addKeyListener(SystemInfo.getMoveNextField());
		customerName2.addFocusListener(SystemInfo.getSelectText());
                date.addKeyListener(SystemInfo.getMoveNextField());
		date.addFocusListener(SystemInfo.getSelectText());
		chargeStaffNo.addKeyListener(SystemInfo.getMoveNextField());
		chargeStaffNo.addFocusListener(SystemInfo.getSelectText());
		regStaffNo.addKeyListener(SystemInfo.getMoveNextField());
		regStaffNo.addFocusListener(SystemInfo.getSelectText());
	}
	
	/**
	 * �I���s�̋Z�p�C���f�b�N�X���擾����
	 * @param technicPattern 0:�Z�p�C���f�b�N�X 1:���C���f�b�N�X
	 * @return index�l�i���݂��Ȃ��ꍇ�ɂ�null�j
	 */
	private Integer getTableTechIndex( int technicPattern ) {
	
            Integer retInteger = null;
            JComboBox combo;
		
            if ( reservations.getSelectedRow() < 0 ) {
                return null;
            }
		
            combo = (JComboBox)reservations.getValueAt( reservations.getSelectedRow(), 0 );
		
            if ( combo.getItemAt( technicPattern + 1 ) != null ) {
                retInteger = (Integer)combo.getItemAt( technicPattern + 1 );
            }
		
            return retInteger;
	}
	
	/**
	 * �w��s�̋Z�p�C���f�b�N�X���f�N�������g����
	 */
	private void decTableTechIndex( int row ) {
		Integer		setInteger = null;
		JComboBox	combo;
		if( row < 0 ) return ;
		combo = (JComboBox)reservations.getValueAt( row, 0 );
		setInteger = (Integer)combo.getItemAt( 1 ) - 1;
		combo.removeItemAt( 1 );
		combo.insertItemAt( setInteger, 1 );
	}
	
	/**
	 * �{�p�䃊�X�g���擾����
	 */
	private void getMstBeds() {
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		beds.setShop( (MstShop)shop.getSelectedItem() );
		
		try {
			beds.load( con );
		} catch(SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �����X�g���擾����
	 */
	private void getProportionallys() {
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		proportionallys.clear();
		try {
			ResultSetWrapper	rs	=	con.executeQuery( this.getProportionallyListSelectSQL() );
			while( rs.next() ) {
				DataProportionally drp = new DataProportionally();
				drp.setDataProportionallyID( rs.getInt( "data_proportionally_id" ) );
				MstProportionally proportionally = new MstProportionally();
				proportionally.setProportionallyID( rs.getInt( "proportionally_id" ) );
				proportionally.setProportionallyName( rs.getString( "proportionally_name" ) );
				proportionally.setProportionallyPoint( rs.getInt( "proportionally_point" ) );
				drp.setProportionally( proportionally );
				MstTechnic mt = new MstTechnic();
				mt.setData( rs );
				drp.setTechnic( mt );
				drp.setRatio( rs.getInt( "proportionally_ratio" ) );
				proportionallys.add( drp );
			}
		} catch(SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �����X�g�擾SQL���擾����
	 */
	private String getProportionallyListSelectSQL() {
		return
			"select\n" +
			"dp.data_proportionally_id,\n" +
			"mp.proportionally_id,\n" +
			"mp.proportionally_name,\n" +
			"mp.proportionally_point,\n" +
			"mt.*,\n" +
			"dp.proportionally_ratio\n" +
			"from\n" +
			"data_proportionally as dp\n" +
			"inner join mst_proportionally mp on\n" +
			"mp.proportionally_id = dp.proportionally_id\n" +
			"inner join mst_technic as mt on\n" +
			"mt.technic_id = dp.technic_id\n" +
			"where\n" +
			"dp.delete_date is null\n" +
			"order by\n" +
			"dp.technic_id, mp.display_seq, mp.proportionally_id\n" +
			";\n";
	}
	
       /**
         * �V�t�g�}�X�^���擾����
         */
        private void getMstShifts() {
            ConnectionWrapper	con	=	SystemInfo.getConnection();
            MstShop ms = (MstShop)shop.getSelectedItem();
            shifts.setShopId(ms.getShopID());

            try {
                shifts.loadAll(con);
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }    

        /*
         * �X�^�b�t�V�t�g���擾����
         */
        private void getStaffShifts() {
            ConnectionWrapper con = SystemInfo.getConnection();
            staffShifts.setShop((MstShop)shop.getSelectedItem());
            staffShifts.setScheduleDate(currentDate.getTime());

            try {
                staffShifts.load( con, false );
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        
	/**
	 * �ڋq����Editable��ύX����B
	 * @param isEditable
	 */
	private void changeCustomerNameEditable(boolean isEditable) {
		customerName1.setEditable(isEditable);
		customerName2.setEditable(isEditable);
		customerName1.setFocusable(isEditable);
		customerName2.setFocusable(isEditable);
	}
	
	/**
	 * �I������Ă���X�܂��擾����B
	 * @return �I������Ă���X��
	 */
	private MstShop getSelectedShop() {

            MstShop result = null;
            
            if (0 <= shop.getSelectedIndex()) {
                result = (MstShop)shop.getSelectedItem();
            }
            
            return result;
	}
	
	/**
	 * �I����t���擾����
	 */
	private GregorianCalendar getSelectedDate() {
            
            if (date.isSelected() && reservationStartTimeField.getText().length() > 0) {
                int hour = Integer.parseInt(reservationStartTimeField.getText().replaceAll(":.+", ""));
		int minute = Integer.parseInt(reservationStartTimeField.getText().replaceAll(".+:", ""));
			
                GregorianCalendar manageDate = new GregorianCalendar();
                manageDate.setTime(date.getDate());

                if (24 <= hour) {
                    manageDate.add(manageDate.DAY_OF_MONTH, 1);
                    hour -= 24;
                }
			
                manageDate.set(manageDate.HOUR_OF_DAY, hour);
                manageDate.set(manageDate.MINUTE, minute);
                return manageDate;
            }
		
            return new GregorianCalendar();
	}
	
        public void setCustomerNo(String no){
            this.customerNo.setText(no);
            System.out.println(this.customerNo.getText() + ":" + this.customerNo.isEditable());
            this.setCustomer();
        }
        
        public void setCustomer(MstCustomer cus){
            this.changeCustomerNameEditable(false);

            // �O��̐��Z����\������
            showCustomerSalesInfo(cus);
            
            // �O��̔̔��f�[�^(�O�񗈓X����)���Z�b�g����
            setLastTimeSalesData(cus);

            customerNo.setText(cus.getCustomerNo());
            customerName1.setText(cus.getCustomerName(0));
            customerName2.setText(cus.getCustomerName(1));

            // �O��̗\��f�[�^���Z�b�g����
            this.setLastTimeReservationData( cus );

            rr.setCustomer(cus);
        }
	
	/**
	 * �O��̓`�[�f�[�^��ǂݍ��ށB
	 * @param cus �ڋq�f�[�^
	 */
	private void loadLastTimeSalesData(MstCustomer cus) {
	    
	    ResultSetWrapper rs;
	    
	    try {
		
		//�R�l�N�V�������擾
		ConnectionWrapper con = SystemInfo.getConnection();

		StringBuilder sql = new StringBuilder(1000);
		sql.append(" select");
		sql.append("      ds.shop_id");
		sql.append("     ,ds.slip_no");
		sql.append(" from");
		sql.append("     data_sales as ds");
		sql.append(" where");
		sql.append("         ds.delete_date is null");
		sql.append("     and ds.sales_date is not null");
		sql.append("     and ds.customer_id = " + SQLUtil.convertForSQL(cus.getCustomerID()));
		sql.append(" order by");
		sql.append("      ds.sales_date desc");
		sql.append("     ,ds.slip_no desc");
		sql.append(" limit 1");
		
		//�O��̓`�[NO���擾
		rs = con.executeQuery(sql.toString());
		Integer lastShopID = null;
		Integer lastSlipNo = null;
		while (rs.next()) {
		    lastShopID = rs.getInt("shop_id");
		    lastSlipNo = rs.getInt("slip_no");
		}
		rs.close();

		//�O��`�[�f�[�^���擾
		lastSales = new DataSales(SystemInfo.getTypeID());
		if (lastShopID != null && lastSlipNo != null) {
		    lastSales.clear();
		    MstShop shop = new MstShop();
		    shop.setShopID(lastShopID);
		    lastSales.setShop(shop);
		    lastSales.setSlipNo(lastSlipNo);
		    lastSales.loadAll(con);
		}
		
	    } catch(SQLException e) {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	}
        
	/**
	 * �O��̐��Z����\������B
	 * @param cus �ڋq�f�[�^
	 */
	private void showCustomerSalesInfo(MstCustomer cus) {

            salesInfo.setText("");
            
            if (cus.getCustomerNo() == null || cus.getCustomerNo().length() == 0) return;
            
	    salesInfo.append("���y�a�����z���@");
	    if (cus.getBirthday() != null) {
		Integer	ageTemp	=	DateUtil.calcAge(
			    new com.ibm.icu.util.GregorianCalendar(),
			    cus.getBirthday());
		salesInfo.append(cus.getBirthdayString("yyyy�NM��d��") + " (" + ageTemp + "��)\n");
	    } else {
		salesInfo.append("\n");
	    }

	    loadLastTimeSalesData(cus);

	    if (lastSales != null) {
		salesInfo.append("�y�O�񗈓X���z�@" + (lastSales.getSalesDate() == null ? "" :DateUtil.format(lastSales.getSalesDate(), "yyyy�NM��d�� (E)")));

		salesInfo.append("\n");
		String staffStr = (lastSales.getStaff() == null ? "" :lastSales.getStaff().getFullStaffName() + (lastSales.getDesignated() ? " (�w)" : " (F)"));
		salesInfo.append("�y�O��S���ҁz�@" + staffStr);

		salesInfo.append("\n\n");
		salesInfo.append("�y�O��Z�p���e�z");
		for (int i = 0; i < lastSales.size(); i ++) {
		    if (lastSales.get(i).getProductDivision() == 1) {
			salesInfo.append("\n");
			salesInfo.append("��" + lastSales.get(i).getProductName());
		    }
		}

		salesInfo.append("\n\n");
		salesInfo.append("�y�O��w�����i�z");
		for (int i = 0; i < lastSales.size(); i ++) {
		    if (lastSales.get(i).getProductDivision() == 2) {
			salesInfo.append("\n");
			salesInfo.append("��" + lastSales.get(i).getProductName());
		    }
		}
	    }
	    
	    salesInfo.setCaretPosition(0);
	    
	}
	/**
	 * �ڋq���Z�b�g����B
	 */
	private void setCustomer() {
            
		if (!customerNo.isEditable()) return;

		customerInfoButton.setEnabled(false);
		salesInfo.setText("");
		
		MstCustomer cus = rr.getCustomer();
		
		cus.setCustomerNo(customerNo.getText());
		
		//�ڋq�R�[�h��0�̏ꍇ�A����
		if (cus.getCustomerNo().equals("0")) {
                    
			cus = new MstCustomer();
			
			this.changeCustomerNameEditable(true);
			
			customerName1.requestFocusInWindow();
		
		} else {
			
			try {
			    
			    cus = SelectSameNoData.getMstCustomerByNo(
				    parentFrame,
				    SystemInfo.getConnection(),
				    this.customerNo.getText(),
				    (SystemInfo.getSetteing().isShareCustomer() ? 0 : ((MstShop)shop.getSelectedItem()).getShopID()));

			    customerInfoButton.setEnabled(cus.getCustomerID() != null);

			    showCustomerSalesInfo(cus);

			} catch(Exception e) {
			    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			this.changeCustomerNameEditable(false);
			
		}
		
                // �O��̔̔��f�[�^(�O�񗈓X����)���Z�b�g����
                setLastTimeSalesData(cus);
			
		customerName1.setText(cus.getCustomerName(0));
		customerName2.setText(cus.getCustomerName(1));
		
		// �O��̗\��f�[�^���Z�b�g����i�V�K�o�^���̂݁j
                if (rr.getReservation().getReservationNo() == null) {
                    this.setLastTimeReservationData( cus );
                }
		
		rr.setCustomer(cus);
	}
	/**
	 * �ڋq���Z�b�g����B
	 */
	private void setCustomer(Integer customerID) {
            
		if (!customerNo.isEditable()) return;
		
		MstCustomer cus = rr.getCustomer();
		
		cus.setCustomerNo(customerNo.getText());
		
		//�ڋq�R�[�h��0�̏ꍇ�A����
		if (cus.getCustomerNo().equals("0")) {
                    
			cus = new MstCustomer();
			this.changeCustomerNameEditable(true);
			customerName1.requestFocusInWindow();
                        
		} else {
			
			try {
				cus =  new MstCustomer(customerID);
                                cus.load(SystemInfo.getConnection());
                                showCustomerSalesInfo(cus);
                                
			} catch(SQLException e) {
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			this.changeCustomerNameEditable(false);
			
		}
		
                // �O��̔̔��f�[�^(�O�񗈓X����)���Z�b�g����
                setLastTimeSalesData(cus);
			
		customerName1.setText(cus.getCustomerName(0));
		customerName2.setText(cus.getCustomerName(1));
		
		// �O��̗\��f�[�^���Z�b�g����
		this.setLastTimeReservationData( cus );
		
		rr.setCustomer(cus);
	}
	/**
	 * �O��̗\��f�[�^���Z�b�g����
	 */
	private void setLastTimeReservationData( MstCustomer cus ) {

                // �\��\����X�^�b�t���̃^�C���e�[�u�����I������Ă���ꍇ�͏����𔲂���
                if (getSelectedChargeStaffID() != null) return;
                    
		chargeStaff.setSelectedIndex(0);
		regStaff.setSelectedIndex(0);
                
		// �ڋq�̑O��̗��X��Ԃ���w���X�^�b�t���擾���A�Z�b�g����
		ResultSetWrapper rs;
		DataReservation ldr = null;
                
		try {

                    rs = SystemInfo.getConnection().executeQuery(this.getLastTimeReservationDataSelectSQL( cus ));
                    
                    while ( rs.next() ) {
                        
                        if ( ldr == null ) {
                            ldr = new DataReservation();
                            ldr.setReservationNo( rs.getInt( "reservation_no" ) );
                            ldr.setCustomer( cus );
                            ldr.setDesignated( rs.getBoolean( "designated_flag" ) );
                            MstStaff mstChargeStaff = new MstStaff();
                            mstChargeStaff.setStaffID( rs.getInt( "charge_staff_id" ) );
                            ldr.setStaff( mstChargeStaff );
                            // �O�񗈓X���w���̏ꍇ�ɂ͏����l�Ɏw���y�ю�S����o�^���Ă���
                            if( ldr.getDesignated() ) {
                                this.setChargeStaff(ldr.getStaff().getStaffID().intValue());
                                if (ldr.getRegStaff() != null) {
                                    this.setRegStaff(ldr.getRegStaff().getStaffID().intValue());
                                }
                                rr.getReservation().setStaff( mstChargeStaff );
                                this.shimeiFreeFlag=true;
                                this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
                                this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
                            }

                        }
                    }

                    rs.close();
                        
		} catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

        private void setChargeStaff(int staffID) {
            for (int i = 1; i < chargeStaff.getItemCount(); i++) {
                if ( ( (MstStaff)chargeStaff.getItemAt( i ) ).getStaffID().intValue() == staffID ) {
                    chargeStaff.setSelectedIndex(i);
                }
            }
        }

        private void setRegStaff(int staffID) {
            for (int i = 1; i < regStaff.getItemCount(); i++) {
                if ( ( (MstStaff)regStaff.getItemAt( i ) ).getStaffID().intValue() == staffID ) {
                    regStaff.setSelectedIndex(i);
                }
            }
        }
        
	/**
	 * �O��̗\��f�[�^���擾����SQL���擾
	 */
	private String getLastTimeReservationDataSelectSQL( MstCustomer cus ) {
		return
			"select\n" +
			"dr.reservation_no,\n" +
			"dr.designated_flag,\n" +
			"dr.staff_id as charge_staff_id\n" +
			"from\n" +
			"data_reservation as dr\n" +
			"where\n" +
			"dr.delete_date is null\n" +
//�X�܂͏�������Ȃ�
//			"and dr.shop_id = " + SQLUtil.convertForSQL( this.getSelectedShop().getShopID() ) + "\n" +
			"and dr.customer_id = " + SQLUtil.convertForSQL( cus.getCustomerID() ) + "\n" +
			"order by\n" +
			"dr.reservation_no desc\n" +
			"limit 1\n" +
			";\n";
	}
	
	/**
	 * �O��̔̔��f�[�^(�O�񗈓X����)���Z�b�g����
	 */
	private void setLastTimeSalesData( MstCustomer cus ) {
		memoBody2.setText(null);
		
		if(cus == null) return;
		
		ResultSetWrapper	rs;
		try {
			rs	=	SystemInfo.getConnection().executeQuery(this.getLastTimeSalesDataSelectSQL( cus ));
			while( rs.next() ) {
                            memoBody2.setText(rs.getString("visited_memo"));
			}
			rs.close();
		} catch(SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �O��̔̔��f�[�^(�O�񗈓X����)���擾����SQL���擾
	 */
	private String getLastTimeSalesDataSelectSQL( MstCustomer cus ) {
            System.out.println("cus =" + cus);
            System.out.println("shop = " + this.getSelectedShop());
		return String.format(
			"select\n" +
			"ds.visited_memo\n" +
			"from\n" +
			"data_sales as ds\n" +
			"where\n" +
			"ds.delete_date is null\n" +
			"and ds.shop_id = " + SQLUtil.convertForSQL( this.getSelectedShop().getShopID() ) + "\n" +
			"and ds.customer_id = " + SQLUtil.convertForSQL( cus.getCustomerID() ) + "\n" +
                        "and ds.sales_date < '%1$tY/%1$tm/%1$td'\n" +
			"order by\n" +
			"ds.sales_date desc,\n" +
			"ds.slip_no desc\n" +
			"limit 1\n" +
			";\n", currentDate);
	}
	
	private boolean checkTimeFormat(String time) {

            if (!time.matches("[0-9]{1,2}:[0-9]{2}")) return false;

            int	hour	= Integer.parseInt(time.replaceAll(":.+", ""));
            int	minute	= Integer.parseInt(time.replaceAll(".+:", ""));

            if (hour < 0 || 36 < hour) return false;
            if (minute < 0 || 59 < minute) return false;

            return true;
	}
	
	private boolean checkTimeTerm(String timeStr) {

            boolean result = false;
            
            MstShop shop = this.getSelectedShop();

            int	time = Integer.parseInt(timeStr.replace(":", ""));
            
            String openH = String.format("%1$02d", shop.getOpenHour());
            String openM = String.format("%1$02d", shop.getOpenMinute());
            int open = Integer.parseInt(openH.concat(openM));
            
            String closeH = String.format("%1$02d", shop.getCloseHour());
            String closeM = String.format("%1$02d", shop.getCloseMinute());
            int close = Integer.parseInt(closeH.concat(closeM));

            if (open <= time && time < close) {
                result = true;
            }

            return result;
            
	}
	
	private void resetReservationTime( Integer index ) {
            
            DataReservationDetail bdrd = rr.getReservation().get(index);
            GregorianCalendar time = (GregorianCalendar)bdrd.getReservationDatetime().clone();
            
            int wRow = 0;

            reservations.setValueAt(getFormatTime(date.getDate(), time), index, 3);
            if (bdrd.getCourse() != null) {
                time.add(Calendar.MINUTE, bdrd.getCourse().getOperationTime());
            } else if (bdrd.getConsumptionCourse() != null) {
                time.add(Calendar.MINUTE, bdrd.getConsumptionCourse().getOperationTime());
            } else {
                time.add(Calendar.MINUTE, bdrd.getTechnic().getOperationTime());
            }
            
            for (int i = 0; i < rr.getReservation().size(); i ++) {
                DataReservationDetail drd = rr.getReservation().get(i);
                if ( index < i ) {
                    reservations.setValueAt(getFormatTime(date.getDate(), time), i, 3);
                    drd.setReservationDatetime((GregorianCalendar)time.clone());
                    if (drd.getCourseFlg() == null) {
                        //�Z�p�̏ꍇ
                        time.add(Calendar.MINUTE, drd.getTechnic().getOperationTime());
                    } else if (drd.getCourseFlg() == 1) {
                        //�R�[�X�_��̏ꍇ
                        time.add(Calendar.MINUTE, drd.getCourse().getOperationTime());
                    } else if (drd.getCourseFlg() == 2) {
                        //�����R�[�X�̏ꍇ
                        time.add(Calendar.MINUTE, drd.getConsumptionCourse().getOperationTime());
                    }
                }
                wRow += ( drd.size() == 0 ? 1 : drd.size() );
            }

            //�\�񎞊ԁA�I������
            setReservationTime();
	}
	
	/**
	 * �\�񎞊Ԃ��ύX���ꂽ�Ƃ��̏������s���B
	 */
	private void changeReservationTime() {
            
            Integer index = this.getTableTechIndex( 0 );
            if (index == null) return;

            int row = reservations.getSelectedRow();

            DataReservationDetail drd = rr.getReservation().get(index);
            String time = reservationStartTimeField.getText();

            if (drd == null)	return;
            if (reservationStartTimeField.getText().length() == 0) return;

            int hour	= Integer.parseInt(time.replaceAll(":.+", ""));
            int minute	= Integer.parseInt(time.replaceAll(".+:", ""));

            GregorianCalendar datetime = new GregorianCalendar();
            datetime.setTime(date.getDate());

            if (24 <= hour) {
                datetime.add(datetime.DAY_OF_MONTH, 1);
                hour -= 24;
            }

            datetime.set(datetime.HOUR_OF_DAY, hour);
            datetime.set(datetime.MINUTE, minute);
            drd.setReservationDatetime(datetime);

            if (row == reservations.getSelectedRow()) {
                this.resetReservationTime(index);
            }
            
	}
	
	/**
	 * �X�^�b�t���ύX���ꂽ�Ƃ��̏������s���B
	 */
	private void changeStaff() {
		DataReservationDetail	drd	=	this.getCurrentReservation();
		if(drd == null)	return;
		JComboBox		staffCombo	=	(JComboBox)this.getCurrentCellObject();
		
			drd.setStaff((MstStaff)staffCombo.getSelectedItem());
		// �������݂���ꍇ�ɂ͈��w���X�^�b�t�ɐݒ肷��
//		if( 0 < drd.size() ) {
//                        for( int preIndex = 0; ( preIndex == 0 || preIndex < drd.size() ); preIndex++ ) {
//        			drd.get( preIndex ).setStaff( (MstStaff)staffCombo.getSelectedItem() );
//                        }
//                }
	}

        /**
	 * ��S�����ύX���ꂽ�Ƃ��߃��j���[�̃X�^�b�t�ɔ��f���鏈�����s���B
	 */
	private void changeStaffs() {
                
		DataReservationDetail	drd	=	this.getCurrentReservation();
		if(drd == null)	return;
		JComboBox		staffCombo	=	(JComboBox)this.getCurrentCellObject();
		
			drd.setStaff((MstStaff)staffCombo.getSelectedItem());
		// �������݂���ꍇ�ɂ͈��w���X�^�b�t�ɐݒ肷��
//		if( 0 < drd.size() ) {
//                        for( int preIndex = 0; ( preIndex == 0 || preIndex < drd.size() ); preIndex++ ) {
//        			drd.get( preIndex ).setStaff( (MstStaff)staffCombo.getSelectedItem() );
//                        }
//                }
}
	
	/**
	 * �x�b�h���ύX���ꂽ�Ƃ��̏������s���B
	 */
	private void changeBed() {
		DataReservationDetail	drd	=	this.getCurrentReservation();
		if(drd == null)	return;
		JComboBox		bedCombo	=	(JComboBox)this.getCurrentCellObject();
		
		drd.setBed((MstBed)bedCombo.getSelectedItem());
	}
	
	/**
	 * �I������Ă���Z���̃I�u�W�F�N�g���擾����B
	 * @return �I������Ă���Z���̃I�u�W�F�N�g
	 */
	private Object getCurrentCellObject() {
		return	reservations.getValueAt(reservations.getSelectedRow(),
			reservations.getSelectedColumn());
	}
	
	/**
	 * �I������Ă���s�̗\��f�[�^���擾����B
	 * @return �I������Ă���s�̗\��f�[�^
	 */
	private DataReservationDetail getCurrentReservation() {
            
            DataReservationDetail drd = null;
		
            if (0 <= reservations.getSelectedRow() && 0 <= reservations.getSelectedColumn()) {
                drd = rr.getReservation().get( this.getTableTechIndex( 0 ) );
            }
		
            return drd;
	}
	
	/**
	 * �폜�{�^���������ꂽ�Ƃ��̏������s���B
	 */
	private void deleteRow() {
            
            DefaultTableModel model = (DefaultTableModel)reservations.getModel();
            int row = reservations.getSelectedRow();
            if (row < 0) return;
            
            // �I�����ꂽ�Z�p���j���[�擾
            int index = this.getTableTechIndex( 0 );
            
            int size = rr.getReservation().get( index ).size();
		
            if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();
            rr.deleteTechnic(index);
            reservations.clearSelection();

            model.removeRow(row);
            for( ; row < reservations.getRowCount(); row++ ) {
                this.decTableTechIndex( row );
            }
            this.techIndex--;
            
            //�\�񎞊Ԃ����t���b�V������
            refreshReservationTime();
	}
	
	/**
	 * �\�񎞊Ԃ����t���b�V������B
	 */
	private void refreshReservationTime() {

            if (reservationStartTimeField.getText().length() > 0){
                reservationStartTimeField.setText(getFormatTime(reservationStartTimeField.getText()));
                
                if(!this.checkTimeFormat(reservationStartTimeField.getText())) {
                    reservationStartTimeField.setText("");
                    return;
                }
                
                if( reservations.getRowCount() >= 1 ) {
                    if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();
                    reservations.addRowSelectionInterval(0,0);
                    changeReservationTime();
                    reservations.clearSelection();
                }
            }
	}
	
	/**
	 * �\�񎞊ԁA�I�����Ԃ�ݒ肷��B
	 */
	private void setReservationTime()
        {
            setReservationTime(date.getDate());
        }
        
	private void setReservationTime(java.util.Date dt)
	{
            if(0 < rr.getReservation().size()) {
                
                GregorianCalendar temp = rr.getReservation().get(0).getReservationDatetime();
                GregorianCalendar temp2 = (GregorianCalendar)rr.getReservation().get(rr.getReservation().size() - 1).getReservationDatetime().clone();

                // �\�񎞊�
                reservationStartTimeField.setText(getFormatTime(dt, temp));
                
                // �I������
                if (rr.getReservation().get(rr.getReservation().size() - 1).getCourse() != null) {
                    temp2.add(Calendar.MINUTE, rr.getReservation().get(rr.getReservation().size() - 1).getCourse().getOperationTime());
                } else if (rr.getReservation().get(rr.getReservation().size() - 1).getConsumptionCourse() != null) {
                    temp2.add(Calendar.MINUTE, rr.getReservation().get(rr.getReservation().size() - 1).getConsumptionCourse().getOperationTime());
                } else {
                    temp2.add(Calendar.MINUTE, rr.getReservation().get(rr.getReservation().size() - 1).getTechnic().getOperationTime());
                }
                reservationEndTime.setText(getFormatTime(dt, temp2));
            }
	}
	
        private String getFormatTime(java.util.Date dt, Calendar cal) {

                int h = cal.get(Calendar.HOUR_OF_DAY);
                int m = cal.get(Calendar.MINUTE);
                if (Integer.parseInt(DateUtil.format(dt, "dd")) + 1 == cal.get(Calendar.DAY_OF_MONTH)) {
                    h += 24;
                }

                return String.format("%1$02d", h) + ":" + String.format("%1$02d", m);
        }

	/**
	 * �J�n���ԑI��pJComboBox���擾����B
	 * @return �J�n���ԑI��pJComboBox
	 */
	private JComboBox getStartTimeComboBox() {
            
            final JComboBox combo = new JComboBox();
            
            combo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            combo.setEditable(true);
            combo.setMaximumRowCount(10);

            Component component = combo.getEditor().getEditorComponent();
            PlainDocument doc = (PlainDocument)((JTextComponent)component).getDocument();
            doc.setDocumentFilter(new CustomFilter(5, "0-9:"));

            final JTextField comboField = (JTextField)component;
            comboField.setHorizontalAlignment(JTextField.CENTER);

            combo.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    comboField.selectAll();
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    int row = reservations.getSelectedRow();
                    if (row >= 0) {
                        reservations.setValueAt(getFormatTime(comboField.getText()), row, 3);
                        updateReservationDatetime();
                        setReservationTime();
                    }
                }
            });

            Integer term        = ((MstShop)shop.getSelectedItem()).getTerm();            // ���ԒP��
            Integer openHour    = ((MstShop)shop.getSelectedItem()).getOpenHour();        // �J�X��
            Integer openMinute  = ((MstShop)shop.getSelectedItem()).getOpenMinute();      // �J�X��
            Integer closeHour   = ((MstShop)shop.getSelectedItem()).getCloseHour();       // �X��
            Integer closeMinute = ((MstShop)shop.getSelectedItem()).getCloseMinute();     // �X��

            if (openHour != null && closeHour != null && term != null) {

                for (int h = openHour; h <= closeHour; h++) {
                    for (int m = 0; m < 60; m += term) {
                        // �J�X���Ԃ��O�̏ꍇ
                        if (h == openHour && m < openMinute) continue;
                        // �X���Ԃ���̏ꍇ
                        if (h == closeHour && closeMinute <= m) break;
                        // ���ԃZ�b�g
                        combo.addItem(String.format("%1$02d", h) + ":" + String.format("%1$02d", m));
                    }
                }
            }
            
            return combo;
	}
        
        private void updateReservationDatetime() {
            
            for (int i = 0; i < rr.getReservation().size(); i++) {
                DataReservationDetail drd = rr.getReservation().get(i);
                GregorianCalendar cal = drd.getReservationDatetime();
                
                String s = reservations.getValueAt(i, 3).toString();
                if (s.matches("\\d+:\\d+")) {
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.replaceAll(":.+", "")));
                    cal.set(Calendar.MINUTE, Integer.parseInt(s.replaceAll(".+:", "")));
                    drd.setReservationDatetime(cal);
                }
            }
        }
        
        
        
	/**
	 * �X�^�b�t�I��pJComboBox���擾����B
	 * @param staffID �I����Ԃɂ���X�^�b�t�h�c
	 * @return �X�^�b�t�I��pJComboBox
	 */
	private JComboBox getStaffComboBox(Integer staffID) {
            
		JComboBox staffCombo = new JComboBox();
		staffCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
		staffCombo.addItem(new MstStaff());
		SystemInfo.initStaffComponent(staffCombo);
		staffCombo.setSelectedIndex(0);
		
		this.setStaff(staffCombo, staffID);
		
		staffCombo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                            if (reservations.getSelectedColumn() == 4) {
				changeStaff();
                            }
			}
		});
		
		return	staffCombo;
	}
	
	/**
	 * �w�肵���X�^�b�t�h�c���X�^�b�t�I��pJComboBox�őI����Ԃɂ���B
	 * @param staffCombo �X�^�b�t�I��pJComboBox
	 * @param staffID �X�^�b�t�h�c
	 */
	private void setStaff(JComboBox staffCombo, Integer staffID) {
                
	    for (int i = 0; i < staffCombo.getItemCount(); i++) {

		 MstStaff ms  = (MstStaff)staffCombo.getItemAt(i);

		 //�󔒂��Z�b�g
		if (ms.getStaffID() == null) {
		     
		   staffCombo.setSelectedIndex(0);
		   
		} else if ( ms.getStaffID().equals(staffID)) {
		     
		    staffCombo.setSelectedIndex(i);
		    return;
		}
	    }
	}

	private void setStaff(JComboBox staffCombo, String staffNo) {
                
	    for (int i = 0; i < staffCombo.getItemCount(); i++) {

		 MstStaff ms  = (MstStaff)staffCombo.getItemAt(i);

		 //�󔒂��Z�b�g
		if (ms.getStaffID() == null) {
		     
		    staffCombo.setSelectedIndex(0);
		   
		} else if ( ms.getStaffNo().equals(staffNo)) {
		     
		    staffCombo.setSelectedIndex(i);
		    return;
		}
	    }
	}
	
	/**
	 * �x�b�h�I��pJComboBox���擾����B
	 * @param bedID �I����Ԃɂ���x�b�h�h�c
	 * @return �x�b�h�I��pJComboBox
	 */
	private JComboBox getBedComboBox(Integer bedID) {
            
            JComboBox bedCombo = new JComboBox();
            bedCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            bedCombo.addItem(new MstBed());
            bedCombo.setSelectedIndex(0);
		
            for (MstBed bed : rr.getBeds()) {
                bedCombo.addItem(bed);
            }

            this.setBed(bedCombo, bedID);

            bedCombo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    changeBed();
                }
            });

            return bedCombo;
	}
	
	/**
	 * �w�肵���x�b�h�h�c���x�b�h�I��pJComboBox�őI����Ԃɂ���B
	 * @param bedCombo �x�b�h�I��pJComboBox
	 * @param bedID �x�b�h�h�c
	 */
	private void setBed(JComboBox bedCombo, Integer bedID) {
		for(int i = 0; i < bedCombo.getItemCount(); i ++) {
			if(((MstBed)bedCombo.getItemAt(i)).getBedID() == bedID) {
				bedCombo.setSelectedIndex(i);
				return;
			}
		}
	}
	
	/**
	 * �폜�{�^�����擾����
	 */
	private JButton getDeleteButton() {
		JButton		deleteButton	=	new JButton();
		deleteButton.setBorderPainted(false);
		deleteButton.setContentAreaFilled(false);
		deleteButton.setIcon( new javax.swing.ImageIcon( getClass().getResource( "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg") ) );
		deleteButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource( "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg") ) );
		deleteButton.setSize(48, 25);
		deleteButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteRow();
			}
		});
		
		return	deleteButton;
	}
	
	/**
	 * �I���Z�p���N���A����
	 */
	private void clearReservation() {
		if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();
		SwingUtil.clearTable(reservations);	// �Z�p�e�[�u�����N���A
		rr.getReservation().initTechnics();
		rr.clear();
		techIndex = 0;
                reservationStartTimeField.setText("");
                reservationEndTime.setText("");
	}
	
	/**
	 * �Z�p���R���{�{�b�N�X���쐬����
	 */
	private JComboBox getTechnicRecodeName( String technicName, Integer ProportionallyIndex ) {
		JComboBox techName = new JComboBox();
		techName.removeAllItems();
		techName.addItem( technicName );
		techName.addItem( techIndex );
		techName.addItem( ProportionallyIndex );
		techName.setSelectedIndex( 0 );
		techName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

		techName.setOpaque( false );
		return techName;
	}

	/**
	 * �R�[�X�_�񖼃R���{�{�b�N�X���쐬����
	 */
	private JComboBox getCourseRecodeName( String name, Integer ProportionallyIndex ) {
		JComboBox courseName = new JComboBox();
		courseName.removeAllItems();
		courseName.addItem( name );
		courseName.addItem( techIndex );
		courseName.addItem( ProportionallyIndex );
		courseName.setSelectedIndex( 0 );
		courseName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

		courseName.setOpaque( false );
		return courseName;
	}

	/**
	 * �����R�[�X���R���{�{�b�N�X���쐬����
	 */
	private JComboBox getConsumptionCourseRecodeName( String name, Integer ProportionallyIndex ) {
		JComboBox consumptionCourseName = new JComboBox();
		consumptionCourseName.removeAllItems();
		consumptionCourseName.addItem( name );
		consumptionCourseName.addItem( techIndex );
		consumptionCourseName.addItem( ProportionallyIndex );
		consumptionCourseName.setSelectedIndex( 0 );
		consumptionCourseName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

		consumptionCourseName.setOpaque( false );
		return consumptionCourseName;
	}

	/**
	 * �w���t���O���擾����
	 */
	private JCheckBox getStaffDesignatedFlag( boolean designated ) {
		JCheckBox designatedFlg = new JCheckBox();
		designatedFlg.setSelected( designated );
//		designatedFlg.setSelected( true );
		designatedFlg.setOpaque( false );
		designatedFlg.setHorizontalAlignment(SwingConstants.CENTER);
		designatedFlg.addActionListener(
			new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changeTechnicStaffDesignated();
			}
		});
		return designatedFlg;
	}
	
	/**
	 * �w���f�[�^��ύX����
	 */
	private void changeTechnicStaffDesignated() {
            Integer index = this.getTableTechIndex( 0 );
            if (index != null) {
                int row   = reservations.getSelectedRow();
                DataReservationDetail drd = (DataReservationDetail)rr.getReservation().get( index );		
                drd.setDesignated( ( (JCheckBox)reservations.getValueAt( row, 5 ) ).isSelected() );
            }
	}
	
	/**
	 * �\����e�̍s��ǉ�����B
	 * @param dr �\��f�[�^
	 */
	private void addReservationRow(DataReservationDetail drd) {

            if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();

            DefaultTableModel model = (DefaultTableModel)reservations.getModel();

            int preIndex;

            model.addRow(
                new Object[] {
                    getTechnicRecodeName( drd.getTechnic().getTechnicClass().getTechnicClassName(), null ),
                    drd.getTechnic().getTechnicName(),
                    drd.getTechnic().getOperationTime(),
                    getFormatTime(date.getDate(), drd.getReservationDatetime()),
                    this.getStaffComboBox( drd.getStaff().getStaffID() ),
                    this.getStaffDesignatedFlag( drd.getDesignated() ),
                    this.getBedComboBox(drd.getBed().getBedID() ),
                    this.getDeleteButton()
                }
            );

            techIndex++;

	}

	/**
	 * �R�[�X�_��̗\����e�̍s��ǉ�����B
	 * @param dr �\��f�[�^
	 */
	private void addReservationCourseRow(DataReservationDetail drd) {

            if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();

            DefaultTableModel model = (DefaultTableModel)reservations.getModel();

            int preIndex;

            model.addRow(
                new Object[] {
                    getCourseRecodeName( drd.getCourse().getCourseClass().getCourseClassName(), null ),
                    drd.getCourse().getCourseName(),
                    drd.getCourse().getOperationTime(),
                    getFormatTime(date.getDate(), drd.getReservationDatetime()),
                    this.getStaffComboBox( drd.getStaff().getStaffID() ),
                    this.getStaffDesignatedFlag( drd.getDesignated() ),
                    this.getBedComboBox(drd.getBed().getBedID() ),
                    this.getDeleteButton()
                }
            );

            techIndex++;

	}

	/**
	 * �����R�[�X�̗\����e�̍s��ǉ�����B
	 * @param dr �\��f�[�^
	 */
	private void addReservationConsumptionCourseRow(DataReservationDetail drd) {

            if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();

            DefaultTableModel model = (DefaultTableModel)reservations.getModel();

            int preIndex;

            model.addRow(
                new Object[] {
                    getConsumptionCourseRecodeName( drd.getConsumptionCourse().getConsumptionCourseClass().getCourseClassName(), null ),
                    drd.getConsumptionCourse().getCourseName(),
                    drd.getConsumptionCourse().getOperationTime(),
                    getFormatTime(date.getDate(), drd.getReservationDatetime()),
                    this.getStaffComboBox( drd.getStaff().getStaffID() ),
                    this.getStaffDesignatedFlag( drd.getDesignated() ),
                    this.getBedComboBox(drd.getBed().getBedID() ),
                    this.getDeleteButton()
                }
            );

            techIndex++;

	}

	/**
	 * �Z�p�I���_�C�A���O�̋Z�p�I�������擾����
	 * @param productDivision
	 * @param product
	 */
	public void addSelectedProduct(Integer productDivision, Product product) {
            
            DataReservationDetail drd = new DataReservationDetail();
            MstTechnicClass mtc = new MstTechnicClass();
            MstTechnic mt = new MstTechnic();
		
            mtc.setTechnicClassID(product.getProductClass().getProductClassID());
            mtc.setTechnicClassName(product.getProductClass().getProductClassName());
            mt.setTechnicClass(mtc);
            mt.setTechnicID(product.getProductID());
            mt.setTechnicNo(product.getProductNo());
            mt.setTechnicName(product.getProductName());
            mt.setOperationTime(product.getOperationTime());
            drd.setTechnic(mt);
		
            drd.setReservationDatetime(this.getSelectedDate());
		
            if (0 < rr.getReservation().size()) {
                drd.setReservationDatetime(
                        (GregorianCalendar)rr.getReservation().get(
                        rr.getReservation().size() - 1
                        ).getReservationDatetime().clone());

                Integer operationTime = null;
                if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == null) {
                    //�Z�p�̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getTechnic().getOperationTime();
                } else if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == 1) {
                    //�R�[�X�_��̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getCourse().getOperationTime();
                } else if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == 2) {
                    //�����R�[�X�̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getConsumptionCourse().getOperationTime();
                }
                drd.getReservationDatetime().add(Calendar.MINUTE, operationTime);
            }
		
            if (0 < chargeStaff.getSelectedIndex()) {
                drd.setStaff((MstStaff)chargeStaff.getSelectedItem());
            }
		
            //�w���t���[�Ή�
            if (((MstShop)shop.getSelectedItem()).isDesignatedAssist()) {
                drd.setDesignated(this.shimeiFreeFlag);
            }

            rr.getReservation().add(drd);
            rr.getReservation().setTotalTime(rr.getReservation().getTotalTime() + drd.getTechnic().getOperationTime());

            //�\�񎞊ԁA�I������
            setReservationTime();
		
            // ����ǉ�����
            for ( DataProportionally dp : proportionallys ) {
                if (drd.getCourseFlg() == null) {
                    //�Z�p�̏ꍇ
                    if ( drd.getTechnic().getTechnicID().intValue() == dp.getTechnic().getTechnicID().intValue() ) {
                        DataReservationProportionally drp = new DataReservationProportionally();
                        drp.setReservationDetail( drd );
                        drp.setProportionally( dp );
                        drp.setDesignated( false );
                        drd.add( drp );
                    }
                } else if (drd.getCourseFlg() == 1) {
                    //�R�[�X�_��̏ꍇ
                } else if (drd.getCourseFlg() == 2) {
                    //�����R�[�X�̏ꍇ
                }
            }

            if (this.selectedBedID != null) {
                try {
                    MstBed b = new MstBed();
                    b.setShop((MstShop)shop.getSelectedItem());
                    b.setBedID(this.selectedBedID);
                    if (b.load(SystemInfo.getConnection())) {
                        drd.setBed(b);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            this.addReservationRow(drd);
	}
	
	/**
	 * �R�[�X�I���_�C�A���O�̃R�[�X�����擾����
	 * @param productDivision
	 * @param product
	 */
	public void addSelectedCourse(Integer productDivision, Course course) {

            DataReservationDetail drd = new DataReservationDetail();
            drd.setCourse(course);
            drd.setCourseFlg(1); //�R�[�X�_��̏ꍇ��1

            drd.setReservationDatetime(this.getSelectedDate());

            if (0 < rr.getReservation().size()) {
                drd.setReservationDatetime(
                        (GregorianCalendar)rr.getReservation().get(
                        rr.getReservation().size() - 1
                        ).getReservationDatetime().clone());

                Integer operationTime = null;
                if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == null) {
                    //�Z�p�̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getTechnic().getOperationTime();
                } else if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == 1) {
                    //�R�[�X�_��̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getCourse().getOperationTime();
                } else if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == 2) {
                    //�����R�[�X�̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getConsumptionCourse().getOperationTime();
                }
                drd.getReservationDatetime().add(Calendar.MINUTE, operationTime);
            }

            if (0 < chargeStaff.getSelectedIndex()) {
                drd.setStaff((MstStaff)chargeStaff.getSelectedItem());
            }

            //�w���t���[�Ή�
            if (((MstShop)shop.getSelectedItem()).isDesignatedAssist()) {
                drd.setDesignated(this.shimeiFreeFlag);
            }

            rr.getReservation().add(drd);
            rr.getReservation().setTotalTime(rr.getReservation().getTotalTime() + drd.getCourse().getOperationTime());

            //�\�񎞊ԁA�I������
            setReservationTime();

            // ����ǉ�����
            for ( DataProportionally dp : proportionallys ) {
                if (drd.getCourseFlg() == null) {
                    //�Z�p�̏ꍇ
                    if ( drd.getTechnic().getTechnicID().intValue() == dp.getTechnic().getTechnicID().intValue() ) {
                        DataReservationProportionally drp = new DataReservationProportionally();
                        drp.setReservationDetail( drd );
                        drp.setProportionally( dp );
                        drp.setDesignated( false );
                        drd.add( drp );
                    }
                } else if (drd.getCourseFlg() == 1) {
                    //�R�[�X�_��̏ꍇ
                    continue;
                } else if (drd.getCourseFlg() == 2) {
                    //�����R�[�X�̏ꍇ
                    continue;
                }
            }

            if (this.selectedBedID != null) {
                try {
                    MstBed b = new MstBed();
                    b.setShop((MstShop)shop.getSelectedItem());
                    b.setBedID(this.selectedBedID);
                    if (b.load(SystemInfo.getConnection())) {
                        drd.setBed(b);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            this.addReservationCourseRow(drd);
	}

	/**
	 * �����R�[�X�I���_�C�A���O�̏����R�[�X�����擾����
	 * @param productDivision
	 * @param product
	 */
	public void addSelectedConsumptionCourse(Integer productDivision, ConsumptionCourse consumptionCourse) {

            DataReservationDetail drd = new DataReservationDetail();
            drd.setConsumptionCourse(consumptionCourse);
            drd.setCourseFlg(2); //�R�[�X�����̏ꍇ��2

            drd.setReservationDatetime(this.getSelectedDate());

            if (0 < rr.getReservation().size()) {
                drd.setReservationDatetime(
                        (GregorianCalendar)rr.getReservation().get(
                        rr.getReservation().size() - 1
                        ).getReservationDatetime().clone());

                Integer operationTime = null;
                if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == null) {
                    //�Z�p�̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getTechnic().getOperationTime();
                } else if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == 1) {
                    //�R�[�X�_��̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getCourse().getOperationTime();
                } else if (rr.getReservation().get(rr.getReservation().size() - 1).getCourseFlg() == 2) {
                    //�����R�[�X�̏ꍇ
                    operationTime = rr.getReservation().get(rr.getReservation().size() - 1).getConsumptionCourse().getOperationTime();
                }
                drd.getReservationDatetime().add(Calendar.MINUTE, operationTime);
            }

            if (0 < chargeStaff.getSelectedIndex()) {
                drd.setStaff((MstStaff)chargeStaff.getSelectedItem());
            }

            //�w���t���[�Ή�
            if (((MstShop)shop.getSelectedItem()).isDesignatedAssist()) {
                drd.setDesignated(this.shimeiFreeFlag);
            }

            rr.getReservation().add(drd);
            rr.getReservation().setTotalTime(rr.getReservation().getTotalTime() + drd.getConsumptionCourse().getOperationTime());

            //�\�񎞊ԁA�I������
            setReservationTime();

            // ����ǉ�����
            for ( DataProportionally dp : proportionallys ) {
                if (drd.getCourseFlg() == null) {
                    //�Z�p�̏ꍇ
                    if ( drd.getTechnic().getTechnicID().intValue() == dp.getTechnic().getTechnicID().intValue() ) {
                        DataReservationProportionally drp = new DataReservationProportionally();
                        drp.setReservationDetail( drd );
                        drp.setProportionally( dp );
                        drp.setDesignated( false );
                        drd.add( drp );
                    }
                } else if (drd.getCourseFlg() == 1) {
                    //�R�[�X�_��̏ꍇ
                    continue;
                } else if (drd.getCourseFlg() == 2) {
                    //�����R�[�X�̏ꍇ
                    continue;
                }
            }

            if (this.selectedBedID != null) {
                try {
                    MstBed b = new MstBed();
                    b.setShop((MstShop)shop.getSelectedItem());
                    b.setBedID(this.selectedBedID);
                    if (b.load(SystemInfo.getConnection())) {
                        drd.setBed(b);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            this.addReservationConsumptionCourseRow(drd);
	}

        /**
	 * ���̓`�F�b�N���s���B
	 * @return true - �n�j
	 */
	private boolean checkInput() {
		//
		if(rr.getReservation().getReservationNo() != null) {
			if(0 < rr.getReservation().getReservationNo() && 2 < rr.getReservation().getStatus()) {
				MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(5115),
					this.getTitle(), JOptionPane.ERROR_MESSAGE);
				return	false;
			}
		}
		
		//�ڋq�R�[�h
		if(customerNo.getText().equals("")) {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
				"�ڋq�R�[�h"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
			customerNo.requestFocusInWindow();
			return	false;
		}
		
		//����
		if(customerNo.getText().equals("0") && rr.getCustomer().getCustomerID() == null) {

			rr.getCustomer().setCustomerNo("0");
			rr.getCustomer().setCustomerName(0, customerName1.getText());
			rr.getCustomer().setCustomerName(1, customerName2.getText());
                        
		} else {
                    
			MstCustomer mc = new MstCustomer();
                        
                        if (!customerName1.isFocusable()) {

                            mc.setCustomerID(rr.getCustomer().getCustomerID());

                            SystemInfo.getLogger().log(Level.INFO, "getCustomerID:" + rr.getCustomer().getCustomerID());

                            try {
                                    ConnectionWrapper con = SystemInfo.getConnection();

                                    if(!mc.isExists(con)) {
                                            MessageDialog.showMessageDialog(this,
                                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_NOT_EXIST,
                                                    "�ڋq�R�[�h" + customerNo.getText()), this.getTitle(),
                                                    JOptionPane.ERROR_MESSAGE);
                                            customerNo.requestFocusInWindow();
                                            return	false;
                                    }
                            } catch(SQLException e) {
                                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                                    return	false;
                            }
                        } else {
                            
                            // �����̔Ԃ̏ꍇ
                            rr.getCustomer().setCustomerNo(customerNo.getText());
                            rr.getCustomer().setCustomerName(0, customerName1.getText());
                            rr.getCustomer().setCustomerName(1, customerName2.getText());
                            
                        }
		}
		
		// ��S��
		//rr.getReservation().setDesignated( chargeNamed.isSelected() );
		//�w���t���[�Ή�
		rr.getReservation().setDesignated( this.shimeiFreeFlag );

		
		if( rr.getReservation().getDesignated() && rr.getReservation().getStaff() == null ) {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
				"��S��"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
			date.requestFocusInWindow();
			return	false;
		}
		
		//�\���
		if(date.getDate() == null) {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
				"�\���"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
			date.requestFocusInWindow();
			return	false;
		}
		
		//�\����e
		if(rr.getReservation().size() == 0) {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(5125),
                                this.getTitle(), JOptionPane.ERROR_MESSAGE);
			searchTechnicButton.requestFocusInWindow();
			return	false;
		}
		
		//�\�񎞊�
		if (reservationStartTimeField.getText().length() == 0) {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                                "�\�񎞊�"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                        reservationStartTime.requestFocusInWindow();
                        return	false;
		}
		
                //�J�n����
                for (int i = 0; i < reservations.getRowCount(); i++) {
                    if (reservations.getValueAt(i, 3).toString().length() == 0) {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                                "�J�n����"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                        reservations.requestFocusInWindow();
                        return	false;
                    }
                }

                //�o�^��
                if (SystemInfo.getDatabase().startsWith("pos_hair_lim")) {
                    if (regStaff.getSelectedIndex() == 0) {
                        MessageDialog.showMessageDialog(
                                this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�o�^��"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                            regStaff.requestFocusInWindow();
                        return false;
                    }
                }
                
		// �\�񃌃R�[�h
		for(DataReservationDetail drd : rr.getReservation()) {
//			//�\�񎞊�
//			if(drd.getReservationDatetime() == null) {
//				MessageDialog.showMessageDialog(this,
//					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
//					"�\�񎞊�"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
//				reservations.requestFocusInWindow();
//				return	false;
//			}
			// �w��
			if( ( drd.getDesignated() )&&( ( drd.getStaff().getStaffID() == null )||( drd.getStaff().getStaffID() == 0 ) ) ) {
				MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(5123),
					this.getTitle(), JOptionPane.ERROR_MESSAGE);
				reservations.requestFocusInWindow();
				return	false;
			}
			for( DataReservationProportionally drp : drd ) {
				// ���w��
				if( ( drp.getDesignated() )&&( ( drp.getStaff().getStaffID() == null )||( drp.getStaff().getStaffID() == 0 ) ) ) {
					MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(5124),
						this.getTitle(), JOptionPane.ERROR_MESSAGE);
					reservations.requestFocusInWindow();
					return	false;
				}
			}
		}

                // �\����̉ߋ����t�`�F�b�N
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(this.date.getDate());
                cal1.set(Calendar.HOUR_OF_DAY, 0);
                cal1.set(Calendar.MINUTE, 0);
                cal1.set(Calendar.SECOND, 0);
                cal1.set(Calendar.MILLISECOND, 0);

                Calendar cal2 = ((MstShop)shop.getSelectedItem()).getSystemDate();
                cal2.set(Calendar.HOUR_OF_DAY, 0);
                cal2.set(Calendar.MINUTE, 0);
                cal2.set(Calendar.SECOND, 0);
                cal2.set(Calendar.MILLISECOND, 0);
                
                if (cal1.before(cal2)) {
                    if (MessageDialog.showYesNoDialog(
                            this,
                            "�\������ߋ��̓��t�ɂȂ��Ă��܂���\n�o�^���Ă�낵���ł��傤���B",
                            this.getTitle(),
                            JOptionPane.WARNING_MESSAGE,
                            JOptionPane.NO_OPTION) != JOptionPane.YES_OPTION)
                    {
                            return false;
                    }
                }

                //�\�񎞊Ԃ����t���b�V������
                //refreshReservationTime();

                // �\�񎞊Ԃ̕s���`�F�b�N
                if(!this.checkTimeFormat(reservationStartTimeField.getText())) {

                    MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�\�񎞊�"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);

                    reservationStartTime.requestFocusInWindow();
                    return false;
                }

                // �J�n���Ԃ̕s���`�F�b�N
                for (int i = 0; i < reservations.getRowCount(); i++) {
                    if(!this.checkTimeFormat(reservations.getValueAt(i, 3).toString())) {

                        MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�J�n����"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);

                        reservations.requestFocusInWindow();
                        return false;
                    }
                }
                
                // �c�Ǝ��ԊO�`�F�b�N
                if(!this.checkTimeTerm(reservationStartTimeField.getText())) {
                    int ret = MessageDialog.showYesNoDialog(
                                    this,
                                    MessageUtil.getMessage(5004),
                                    this.getTitle(),
                                    JOptionPane.QUESTION_MESSAGE);

                    if (ret == JOptionPane.NO_OPTION) {
                        if( reservations.getRowCount() >= 1 ) {
                            reservations.addRowSelectionInterval(0,0);
                            rr.getReservation().get(this.getTableTechIndex(0)).setReservationDatetime(null);
                        }
                        reservationStartTime.requestFocusInWindow();
                        return false;
                    }
                }
                
                // �X�^�b�t�V�t�g�̔��f�L��
                if (this.getSelectedShop().getReservationStaffShift() == 1) {
                    // �Ζ����ԓ��`�F�b�N���s��
                    if( !checkReserveTimeInStaffWorkingTime() ){
                        if( MessageDialog.showYesNoDialog(
                                this,
                                MessageUtil.getMessage(5127, "�\��f�[�^"),
                                this.getTitle(),
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION ){
                            return false;
                        }
                    }
                }
                
                // �{�p�䐔�̒��߃`�F�b�N
		if(rr.getReservation().getReservationNo() == null) {
                    if (!isBedCountOverCheck()) {
                        return false;
                    }
                }

                // �{�p��L�̏ꍇ
                if (((MstShop)shop.getSelectedItem()).isBed()) {
                    for (DataReservationDetail drd : rr.getReservation()) {
                        if (drd.getBed().getBedID() == null || drd.getBed().getBedID().equals(0)) {
                            if( MessageDialog.showYesNoDialog(
                                    this,
                                    "�{�p�䂪�I������Ă��܂��񂪁A��낵���ł����H",
                                    this.getTitle(),
                                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION ) {
                                return false;
                            }
                            break;
                        }
                    }
                }

		return true;
	}
	
        private boolean isBedCountOverCheck() {
            
            boolean result = true;
            
            ArrayList<Integer> list = new ArrayList<Integer>();
            
            ConnectionWrapper con = SystemInfo.getConnection();
            
            for (int i = 0; i < rr.getReservation().size(); i++) {
                
                if (rr.getReservation().get(i).getBed().getBedID() == null) continue;
                
                try {
                    ResultSetWrapper rs = con.executeQuery(getBedCountOverCheckSQL(i));
                    if (rs.next()) {

                        if (rs.getInt("add_num") < 1) continue;
                        
                        Integer bedID = rr.getReservation().get(i).getBed().getBedID();
                        if (list.contains(bedID)) continue;
                        list.add(bedID);
                        
                        if (rs.getInt("bed_num") < rs.getInt("use_num") + rs.getInt("add_num")) {    
                            
                            if (MessageDialog.showYesNoDialog(
                                    this,
                                    rr.getReservation().get(i).getBed().getBedName() + "�̏������z���܂����\��o�^���s���Ă�낵���ł����H",
                                    this.getTitle(),
                                    JOptionPane.WARNING_MESSAGE,
                                    JOptionPane.NO_OPTION) != JOptionPane.YES_OPTION)
                            {
                                result = false;
                                break;
                            }
                        }
                    }
		} catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
            }
            
            return result;
        }
        

        private String getBedCountOverCheckSQL(int index) {

            String bedID = SQLUtil.convertForSQL(rr.getReservation().get(index).getBed().getBedID());
            String techID = SQLUtil.convertForSQL(rr.getReservation().get(index).getTechnic().getTechnicID());
            String reservationDateTime = SQLUtil.convertForSQL(rr.getReservation().get(index).getReservationDatetime());
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      (select bed_num from mst_bed where bed_id = " + bedID + ") as bed_num");
            sql.append("     ,coalesce(max((");
            sql.append("         select");
            sql.append("             count(*)");
            sql.append("         from");
            sql.append("             (" + getBedReservationTimeSQL(index) + ") a");
            sql.append("         where");
            sql.append("                 bed_id = t.bed_id");
            sql.append("             and reservation_no <> t.reservation_no");
            sql.append("             and (");
            sql.append("                      (start_time >= t.start_time and start_time < t.end_time)");
            sql.append("                   or (end_time > t.start_time and end_time <= t.end_time)");
            sql.append("                 )");
            sql.append("      )), -1) + 1 as use_num");
            sql.append("     ,(");
            sql.append("         select");
            sql.append("             case when count(*) > 0 then 1 else 0 end");
            sql.append("         from");
            sql.append("             (" + getBedReservationTimeSQL(index) + ") a");
            sql.append("         where");
            sql.append("                 a.bed_id = " + bedID);
            sql.append("             and (");
            sql.append("                     (");
            sql.append("                             " + reservationDateTime + "::timestamp >= a.start_time");
            sql.append("                         and " + reservationDateTime + "::timestamp < a.end_time");
            sql.append("                     )");
            sql.append("                  or (");
            sql.append("                             (select " + reservationDateTime + "::timestamp + (a.operation_time || 'minute')::interval) > a.start_time");
            sql.append("                         and (select " + reservationDateTime + "::timestamp + (a.operation_time || 'minute')::interval) <= a.end_time");
            sql.append("                     )");
            sql.append("                 )");
            sql.append("      ) as add_num");
            sql.append(" from");
            sql.append("     (" + getBedReservationTimeSQL(index) + ") t");

            return sql.toString();
        }
        
        private String getBedReservationTimeSQL(int index) {
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      shop_id");
            sql.append("     ,reservation_no");
            sql.append("     ,bed_id");
            sql.append("     ,reservation_datetime as start_time");
            sql.append("     ,reservation_datetime + (coalesce(t.operation_time, (select operation_time from mst_technic where technic_id = t.technic_id)) || 'minute')::interval as end_time");
            sql.append("     ,coalesce(t.operation_time, (select operation_time from mst_technic where technic_id = t.technic_id)) as operation_time");
            sql.append(" from");
            sql.append("     data_reservation_detail t");
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(rr.getShop().getShopID()));
            sql.append("     and date_trunc('day', reservation_datetime) = " + SQLUtil.convertForSQLDateOnly(rr.getReservation().get(index).getReservationDatetime()));
            sql.append("     and bed_id = " + SQLUtil.convertForSQL(rr.getReservation().get(index).getBed().getBedID()));
            sql.append("     and delete_date is null");

            return sql.toString();
        }
        
        
        
        
	private void setData() {
            
            if( reservations.getCellEditor() != null ) reservations.getCellEditor().stopCellEditing();
            
            rr.getReservation().setShop(this.getSelectedShop());
            GregorianCalendar selectedDate = new GregorianCalendar();
            selectedDate.setTime(date.getDate());
            rr.setDate(selectedDate);
            rr.getReservation().setComment(this.memoBody1.getText());
            rr.getReservation().setNextFlag(this.checkNext.isSelected() ? 1 : 0);
            rr.getReservation().setPreorderFlag(this.checkPreorder.isSelected() ? 1 : 0);
            
            for (int i = 0; i < reservations.getRowCount(); i++) {
                DataReservationDetail drd = rr.getReservation().get(i);
                if (drd.getCourseFlg() == null) {
                    drd.getTechnic().setOperationTime((Integer)reservations.getValueAt(i, 2));
                } else if (drd.getCourseFlg() == 1) {
                    drd.getCourse().setOperationTime((Integer)reservations.getValueAt(i, 2));
                } else if (drd.getCourseFlg() == 2) {
                    drd.getConsumptionCourse().setOperationTime((Integer)reservations.getValueAt(i, 2));
                }
                
                GregorianCalendar cal = drd.getReservationDatetime();
                cal.setTime(date.getDate());
                String s = reservations.getValueAt(i, 3).toString();
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.replaceAll(":.+", "")));
                cal.set(Calendar.MINUTE, Integer.parseInt(s.replaceAll(".+:", "")));
                drd.setReservationDatetime(cal);
            }
            
	}
	
	private void clearComponents() {
            
            date.setDefaultDate(date.getDefaultDate(true));
            this.clearReservation();
	}
	
	/**
	 * �\�񎞊Ԃ��S���X�^�b�t�̋Ζ����Ԃł��邱�Ƃ��m�F����
	 */
	private boolean checkReserveTimeInStaffWorkingTime() {

            // �J�n���ԏI�����Ԃ����߂�
            String startTime = reservationStartTimeField.getText().replaceAll(":", "");
            String endTime   = reservationEndTime.getText().replaceAll(":", "");

            MstStaff staff = (MstStaff)chargeStaff.getSelectedItem();
            if( staff.getStaffID() == null ){
                // �X�^�b�t���I��
                return true;
            }

            DataSchedule schedule = staffShifts.getSchedule(staff.getStaffID());
            if( schedule == null  ){
                // �X�^�b�t�V�t�g���o�^
                return false;
            }

            MstShift shift = shifts.getShift(schedule.getShiftId(), staff.getShopID());
            if( shift == null  ){
                // �ΏۃV�t�g��񖢓o�^
                return false;
            }

            // �{�p�J�n�I�����Ԃ��A�Ζ����Ԃł��邱�Ƃ��m�F����
            if (!shift.inRange(startTime) || !shift.inRangeEndTime(endTime)) {
                return false;
            }

            // �{�p���ԓ��ɋx�e���Ԃ��܂܂��Ƃ�
            if (schedule.getRecesses().inRange(startTime, endTime)){
                return false;
            }

            return true;
	}
        
	/**
	 * �\�񏈗����s���B
	 */
	private void reserve() {
            
            //���̓`�F�b�N
            if (!this.checkInput()) {
                return;
            }

            //�ݓX�ς݃`�F�b�N
            if (!this.getTitle().equals("�\��m�F")) {
                if (ReservationStatusPanel.isVisited(((MstShop)shop.getSelectedItem() ).getShopID(), rr.getCustomer(), date.getDate(), "�\��o�^")) return;
            }

            this.setData();

            //�\�񏈗�
            if(rr.reserve()) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(5001),
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);
                    this.setReserved(true);
                    this.clearComponents();
                    this.closeReservationPanel();
                    this.setReserved( true );
            } else {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�\��f�[�^"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            }
	}
	
	/**
	 * ��t�������s���B
	 */
	private void receipt() {
		//���̓`�F�b�N
		if(!this.checkInput()) {
                    return;
		}

                //�ݓX�ς݃`�F�b�N
                if (!this.getTitle().equals("�\��m�F")) {
                    if (ReservationStatusPanel.isVisited(((MstShop)shop.getSelectedItem() ).getShopID(), rr.getCustomer(), date.getDate(), "��t����")) return;                }

                ReservationStatusPanel p = new ReservationStatusPanel();
                p.introducerInfo(rr.getReservation());

		rr.getReservation().setComment(this.memoBody1.getText());
                rr.getReservation().setNextFlag(this.checkNext.isSelected() ? 1 : 0);
                rr.getReservation().setPreorderFlag(this.checkPreorder.isSelected() ? 1 : 0);

                //��t����
		if(rr.receipt()) {
                    String msg = MessageUtil.getMessage(5002);
                    if (receiptButton.getName() != null &&
                            receiptButton.getName().equals("�X�V")) {
                        msg = "�X�V���܂����B";
                    }
			MessageDialog.showMessageDialog(
                                this,
				msg,
				this.getTitle(),
				JOptionPane.INFORMATION_MESSAGE);
			this.setReserved(true);
			this.clearComponents();
			this.closeReservationPanel();
		} else {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�\��f�[�^"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void delete() {
		if( rr.getReservation().getReservationNo() == null ) {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(1112),
				this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(0 < rr.getReservation().getReservationNo() && 1 < rr.getReservation().getStatus() ) {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(5116),
				this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//�폜�m�F
		if(MessageDialog.showYesNoDialog(this,
			MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE,
			rr.getReservation().getCustomer().getFullCustomerName() + "�l�̗\��"),
			this.getTitle(),
			JOptionPane.WARNING_MESSAGE) != 0) {
			return;
		}
		
		if(rr.delete()) {
                        //�폜�����ďo����ʂ����t���b�V��
                        this.setReserved(true);
			this.clearReservation();
			this.closeReservationPanel();
//			this.load();
		} else {
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED,
				"�\��f�[�^"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

    /**
     * @return the ctiNo
     */
    public String getCtiNo() {
        if (ctiNo.length() > 0) {
            return "���M�ԍ��F" + ctiNo;
        } else {
            return ctiNo;
        }
    }

    /**
     * @param ctiNo the ctiNo to set
     */
    public void setCtiNo(String ctiNo) {
        this.ctiNo = ctiNo;
    }
	
	/**
	 * �\��o�^��ʗpFocusTraversalPolicy
	 */
	private class RegistReservationFocusTraversalPolicy
		extends FocusTraversalPolicy {
		/**
		 * aComponent �̂��ƂŃt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentAfter(Container aContainer,
			Component aComponent) {
			
			if (aComponent.equals(date)) {
			    if(customerNo.isEditable()){
				return customerNo;
			    }else if(customerName1.isFocusable()){
				return customerName1;
			    } else{
				return chargeStaff;
			    }
			} else if (aComponent.equals(customerNo)) {
				if(customerName1.isFocusable())
					return customerName1;
				else 
					return chargeStaff;
			} else if (aComponent.equals(searchCustomerButton)) {
				return date;
			} else if (aComponent.equals(customerName1)) {
				return customerName2;
			} else if (aComponent.equals(customerName2)) {
				return searchTechnicButton;
			} else if ( aComponent.equals(chargeStaffNo) ) {
				return chargeStaff;
			} else if ( aComponent.equals(chargeStaff) ) {
				return reservationStartTime;
			} else if (aComponent.equals(searchTechnicButton)) {
				return reservationStartTime;
			} else if (aComponent.equals(reservationStartTime)) {
				return reservations;
			} else if (aComponent.equals(reservations)) {
				return receiptButton;
			} else if (aComponent.equals(receiptButton)) {
				return deleteButton;
			} else if (aComponent.equals(deleteButton)) {
				return closeButton;
			} else if ( aComponent.equals(regStaffNo) ) {
				return regStaff;
			} else if (aComponent.equals(closeButton)) {
				return date;
			}
			
			return aComponent;
		}
		
		/**
		 * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentBefore(Container aContainer,
			Component aComponent) {
			if (aComponent.equals(customerNo)) {
				return date;
			} else if (aComponent.equals(searchCustomerButton)) {
				return customerNo;
			} else if (aComponent.equals(customerName1)) {
				return searchCustomerButton;
			} else if (aComponent.equals(customerName2)) {
				return customerName1;
			} else if ( aComponent.equals(date) ) {
				if( searchCustomerButton.isEnabled() )
					return searchCustomerButton;
				else 
					return closeButton;
			} else if (aComponent.equals(chargeStaff)) {
				return chargeStaffNo;
			} else if ( aComponent.equals(chargeStaffNo) ) {
				if( date.isEnabled() )
					return date;
				else if( searchCustomerButton.isEnabled() )
					return searchCustomerButton;
				else if( customerNo.isEnabled() )
					return customerNo;
				else
					return closeButton;
			} else if (aComponent.equals(searchTechnicButton)) {
				return chargeStaff;
			} else if (aComponent.equals(reservationStartTime)) {
				return chargeStaff;
			} else if (aComponent.equals(reservations)) {
				return reservationStartTime;
			} else if (aComponent.equals(receiptButton)) {
				return reservations;
			} else if (aComponent.equals(deleteButton)) {
				return receiptButton;
			} else if (aComponent.equals(closeButton)) {
				return deleteButton;
			}
			
			return aComponent;
		}
		
		/**
		 * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�������̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer �擪�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̐擪�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getFirstComponent(Container aContainer) {
                        return date;
		}
		
		/**
		 * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�t�����̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer aContainer - �Ō�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̍Ō�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getLastComponent(Container aContainer) {
                        return date;
		}
		
		/**
		 * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
		 * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getDefaultComponent(Container aContainer) {
                        return customerNo.isEditable() && customerNo.getText().length() == 0 ? customerNo : memoBody1;
		}
		
		/**
		 * �E�B���h�E���ŏ��ɕ\�����ꂽ�Ƃ��Ƀt�H�[�J�X���ݒ肳���R���|�[�l���g��Ԃ��܂��B
		 * show() �܂��� setVisible(true) �̌Ăяo���ň�x�E�B���h�E���\�������ƁA
		 * �������R���|�[�l���g�͂���ȍ~�g�p����܂���B
		 * ��x�ʂ̃E�B���h�E�Ɉڂ����t�H�[�J�X���Ăѐݒ肳�ꂽ�ꍇ�A
		 * �܂��́A��x��\����ԂɂȂ����E�B���h�E���Ăѕ\�����ꂽ�ꍇ�́A
		 * ���̃E�B���h�E�̍Ō�Ƀt�H�[�J�X���ݒ肳�ꂽ�R���|�[�l���g���t�H�[�J�X���L�҂ɂȂ�܂��B
		 * ���̃��\�b�h�̃f�t�H���g�����ł̓f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * @param window �����R���|�[�l���g���Ԃ����E�B���h�E
		 * @return �ŏ��ɃE�B���h�E���\�������Ƃ��Ƀt�H�[�J�X�ݒ肳���R���|�[�l���g�B�K�؂ȃR���|�[�l���g���Ȃ��ꍇ�� null
		 */
		public Component getInitialComponent(Window window) {
                        return date;
		}
	}
	
	/**
	 * �\��e�[�u���p��TableCellRenderer
	 */
	public class RegistReservationTableCellRenderer extends DefaultTableCellRenderer {
		/** Creates a new instance of ReservationTableCellRenderer */
		public RegistReservationTableCellRenderer() {
			super();
		}
		
		/**
		 * �e�[�u���Z�������_�����O��Ԃ��܂��B
		 * @param table JTable
		 * @param value �Z���Ɋ��蓖�Ă�l
		 * @param isSelected �Z�����I������Ă���ꍇ�� true
		 * @param hasFocus �t�H�[�J�X������ꍇ�� true
		 * @param row �s
		 * @param column ��
		 * @return �e�[�u���Z�������_�����O
		 */
		public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
			
			switch(column) {
				case 2:
				case 3:
					super.setHorizontalAlignment(SwingConstants.CENTER);
					break;
				default:
					super.setHorizontalAlignment(SwingConstants.LEFT);
					break;
			}
			
			return this;
		}
	}
	
	/**
	 * �_�C�A���O�����
	 */
	private void closeReservationPanel() {                
                if(this.isDialog()) {
                        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
                } else {
                        this.setVisible(false);
                }                    
	}
	
	public void releaceMemory() {
		rr.clear();
		beds.clear();
		proportionallys.clear();
	}
        
	/**
	 * �ڋq����\������iCTI���J�ڎ��j
	 */
        public void setCustomerInfo(MstCustomer mc, VisitRecord vr)
        {
		rr.setCustomer( mc );
                customerNo.setText(mc.getCustomerNo());
                customerName1.setText(mc.getCustomerName(0));
                customerName2.setText(mc.getCustomerName(1));
                memoBody1.setText(this.getCtiNo());
                if(vr.size() != 0)
                {        
                        chargeStaffNo.setText(vr.getStaff().getStaffNo());
                        chargeStaff.setSelectedItem(vr.getStaff());
                        //chargeNamed.setSelected(vr.getDesignated());
                        //chargeFree.setSelected(! vr.getDesignated());
						//�w���t���[�Ή�
						this.shimeiFreeFlag=vr.getDesignated();
						if(this.shimeiFreeFlag){
							this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
							this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));		
						}else{
							this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
							this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));			
						}

                        rr.getReservation().setStaff( vr.getStaff() ); 
                        rr.setCustomer( vr.getCustomer() );

                }
                if( mc.getCustomerID()!=null)
                {
                        searchCustomerButton.setEnabled( false );
                        newCustomerButton.setEnabled( false );
                        customerNo.setEditable( false );                        
                }
        }
	/**
	 * ��S���҂�ύX���������j���[���X�^�b�t�ɔ��f�Z�b�g������
	 * @param chargeFlag
	 */        
        public void setStaffDataToReserve(boolean chargeFlag){

            if (this.getTitle().equals("�\��m�F")) {
                if (!isReplaceStaff(((MstStaff)chargeStaff.getSelectedItem()).getStaffID())) {
                    return;
                }
            }
            
            for (int i = 0; i < rr.getReservation().size(); i++ ) {

                DataReservationDetail drd = rr.getReservation().get(i);
                if (drd == null) return;
                                
                //�X�^�b�t�p
                drd.setStaff((MstStaff)chargeStaff.getSelectedItem());

                //�w���t���O�p
                if (!this.getTitle().equals("�\��m�F")) {
                    drd.setDesignated( chargeFlag );
                }

                //���j���[���Ɏ�S���ҁC�w���t���O�̃f�[�^���f��\��
                setShowData(i,true, chargeFlag);
            }
        }
 	/**
	 * �w���t���O��ύX���������j���[���w���t���O�ɔ��f�Z�b�g������
	 * @param chargeFlag
	 */        
        public void  setChargeFlagToReserve(boolean chargeFlag){

            if (((MstShop)shop.getSelectedItem()).isDesignatedAssist()) {
                for (int i = 0; i < rr.getReservation().size(); i++ ) {
                    DataReservationDetail drd = rr.getReservation().get(i);
                    if (drd == null) return;

                    //�w���t���O�p
                    drd.setDesignated( chargeFlag );
                    //���j���[���Ɏ�S���ҁC�w���t���O�̃f�[�^���f��\��
                    setShowData(i,false, true);
                }
            }
        }
 	/**
	 * ���j���[���Ɏ�S���ҁC�w���t���O�̃f�[�^���f��\��
	 * @param reservationDataCount
         * @param isChargeStaff
	 */        
        public void setShowData(int reservationDataCount , boolean isChargeStaff, boolean isChangeDesignated){

            if (isChargeStaff) {
                setStaff( ((JComboBox)reservations.getValueAt(reservationDataCount,4)) ,
                ((MstStaff)chargeStaff.getSelectedItem()).getStaffID() );
            }

            ((JCheckBox)reservations.getValueAt(reservationDataCount,5)).setSelected(this.shimeiFreeFlag); //�w���t���[�Ή�

            reservations.updateUI();
        }
	
 	/**
	 * �_�C�A���O��ʂ��J��
	 * @param owner �e�t���[��
	 * @param modal ���[�_��
         * @param panel �_�C�A���O�p�p�l��
         * @param title �_�C�A���O�^�C�g��
	 */        
	private void openAnchorDialog(Frame owner, boolean modal, JPanel panel, String title )
	{
		JDialog dialog = new JDialog(owner, modal);
		
		dialog.setTitle(title);
		setPanelToDialog(dialog, panel);
		moveAnchor( dialog );
                closeReservationPanel();
		dialog.setVisible(true);
	}
	
 	/**
	 * �_�C�A���O�p�p�l�����_�C�A���O�ɐݒ肷��
	 * @param dialog �_�C�A���O
         * @param panel �_�C�A���O�p�l��
	 */        
	private void setPanelToDialog(JDialog dialog, JPanel panel)
	{
		dialog.setSize(panel.getWidth() + 4, panel.getHeight() + 32);
		
                org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(dialog.getContentPane());
                dialog.getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
	}
	
	/**
	 * JDialog��e�t���[���Ɠ�����ʈʒu�ֈړ�����B
	 * @param dialog JDialog
	 */
	private void moveAnchor( JDialog dialog )
	{
		int posX = this.getLocationOnScreen().x - MINUS_X;
		int posY = this.getLocationOnScreen().y - MINUS_Y;
		
		dialog.setLocation( posX, posY);
	}

        public Integer getSelectedChargeStaffID() {
            return selectedChargeStaffID;
        }

        public void setSelectedChargeStaffID(Integer selectedChargeStaffID) {
            this.selectedChargeStaffID = selectedChargeStaffID;
            if (selectedChargeStaffID != null) {
                this.setChargeStaff(selectedChargeStaffID.intValue());
                rr.getReservation().setStaff( (MstStaff)chargeStaff.getSelectedItem() );
                this.shimeiFreeFlag=true;
                this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
                this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
            }
        }
        
        private boolean isReplaceStaff(Integer chargeStaffId) {
        
            boolean result = true;
            
            if (chargeStaffId != null) {
                for (int i = 0; i < rr.getReservation().size(); i++ ) {
                    DataReservationDetail drd = rr.getReservation().get(i);
                    if (drd == null || drd.getStaff().getStaffID() == null || drd.getStaff().getStaffID().equals(0)) continue;

                    if (!drd.getStaff().getStaffID().equals(chargeStaffId)) {
                        if(MessageDialog.showYesNoDialog(
                                this,
                                "�{�p�S�������ɓ��͂���Ă��܂��B\n�ύX���Ă���낵���ł����H",
                                this.getTitle(),
                                JOptionPane.INFORMATION_MESSAGE,
                                JOptionPane.NO_OPTION) == JOptionPane.NO_OPTION)
                        {
                            result = false;
                        }
                        break;
                    }
                }
            }
            
            return result;
        }

        public void setSelectedBedID(Integer selectedBedID) {
            this.selectedBedID = selectedBedID;
        }
}
