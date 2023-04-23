/*
 * ReservationTimeTablePanel.java
 *
 * Created on 2007/10/05, 10:38
 */

package com.geobeck.sosia.pos.hair.reservation;

//import com.geobeck.sosia.pos.basicinfo.company.RegistRecessPanel;
import com.geobeck.sosia.pos.basicinfo.company.RegistPersonalRecessPanel;
import com.geobeck.sosia.pos.hair.customer.OpenCustomerPopupMenu;
import com.geobeck.sosia.pos.hair.pointcard.InsertCardDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import java.text.SimpleDateFormat;
import jxl.HeaderFooter;
import com.geobeck.sosia.pos.swing.table.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

/**
 *
 * @author  kanemoto
 */
public class ReservationTimeTablePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx implements Printable {
	
	private final Integer	HEADER_WIDTH	=	122;				/** �w�b�_�̕�  */	
	private	Integer         ROW_HEIGHT      =	SystemInfo.TABLE_ROW_HEIGHT;	/** �s�̍��� */
	private	final Integer	COLUMN_WIDTH	=	24;				/** ��̕� */
	
	private final Color	COLOR_HEADER		=	new Color(0xffffff);	/** �w�b�_�̐F  */
//	private final Color	COLOR_RESERVATION	=	new Color(0xffffcc);	/** �\��̐F */
	private final Color	COLOR_RESERVATION	=	new Color(0xffff00);	/** �\��̐F */
//	private final Color	COLOR_STAY			=	new Color(0x33ff99);	/** �ݓX�̐F */
	private final Color	COLOR_STAY			=	new Color(0xccff66);	/** �ݓX�̐F */
//	private final Color	COLOR_FINISH		=	new Color(0x00ccff);	/** �ޓX�̐F */
	private final Color	COLOR_FINISH		=	new Color(0xc4e1ff);	/** �ޓX�̐F */
//	private final Color	COLOR_MOBILE		=	new Color(0x000000);    /** ���o�C���\��̐F */
	private final Color	COLOR_MOBILE		=	new Color(0xff9999);    /** ���o�C���\��̐F */
        
	private Integer	term		=	SystemInfo.getCurrentShop().getTerm();			/** ���ԒP�� */
	private	Integer	openHour	=	SystemInfo.getCurrentShop().getOpenHour();		/** �J�X�� */
	private	Integer	openMinute	=	SystemInfo.getCurrentShop().getOpenMinute();	/** �J�X�� */
	private	Integer	closeHour	=	SystemInfo.getCurrentShop().getCloseHour();		/** �X�� */
	private	Integer	closeMinute	=	SystemInfo.getCurrentShop().getCloseMinute();	/** �X�� */
	
	private	GregorianCalendar	currentDate	=	new GregorianCalendar();	/** ���t */
	
	private	RegistReservationDialog		rrp		=	null;						/** �\��o�^�p�l�� */
	private	TimeSchedule	ts		=	new TimeSchedule();						/** �^�C���X�P�W���[����ʗp�����N���X */
	private	RegistPersonalRecessPanel	rpr		=	null;						/** �x�e�o�^�p�l�� */

	private	Integer		posX		=	null;	/** �ړ�����JTextField�̂����W */
	private	Integer		posY		=	null;	/** �ړ�����JTextField�̂����W */
	
	private	boolean		isLoading	=	false;
        private boolean         isShopLoading   =       false;
	
        private ReservationStatusPanel parent;
        
        private MstCustomer customer  = null;
        
        private boolean isOpenByCheckout = false;
        
	private String m_strTxtWorkOffGotFocus;
	private String m_strTxtTrainingGotFocus;
	private String m_strTxtEventGotFocus;

        private boolean nextReservation = false;
        
        private boolean autoOpenDialog = false;
        private Integer nextReserveShopID = null;
        private Integer nextReserveNo = null;

	private static enum eWORKINGTYPE {
		Off(0)
	,	Training(2)
	,	Event(-1)
	;

		private int m_nValue;

		private eWORKINGTYPE(int nVal)
		{
			m_nValue = nVal;
		}

		public int toInt()
		{
			return m_nValue;
		}

	};

        public void setOpenByCheckout(boolean b){
            isOpenByCheckout = b;
        }
        
        public boolean isOpenByCheckout(){
            return this.isOpenByCheckout;
        }
	/** Creates new form ReservationTimeTablePanel */
	public ReservationTimeTablePanel()
	{
            this(null, SystemInfo.getCurrentShop());
        }
        public ReservationTimeTablePanel( ReservationStatusPanel rp)
	{
            this(rp, SystemInfo.getCurrentShop());
        }
        public ReservationTimeTablePanel( ReservationStatusPanel rp, MstShop sp)
	{
                super();
		isLoading = true;
                parent = rp;

                if (SystemInfo.isGroup() == false && SystemInfo.getCurrentShop().getTerm().equals(30)) {
                    ROW_HEIGHT = ROW_HEIGHT + 10;
                }

		initComponents();
		this.setSize(833, 680);
		this.setPath("�\��Ǘ�");
		this.setTitle("�\��o�^");
		addMouseCursorChange();
		this.setListener();

                //�X�ܑI��p�R���{�{�b�N�X�̐ݒ�
                if(SystemInfo.getCurrentShop().getShopID() == 0){
                    SystemInfo.initGroupShopComponents(shop, 2);
                }else{
                    // �����O���[�v�̓X�܂�ݒ�
                    isShopLoading = true;
                    for (int i = 0; i < SystemInfo.getGroup().getShops().size(); i++) {
                        shop.addItem(SystemInfo.getGroup().getShops().get(i));
                    }
                    isShopLoading = false;
                    shop.setSelectedItem(sp);
                }

		isLinkScroll.setSelected(true);
                isLinkScroll.setVisible(false);

		isLoading = false;
                
		loadWorkingInfo();
                
                if( !SystemInfo.isUsePointcard() ){
                    cardInButton.setVisible(false);
                }
                
                recessButton.setVisible(SystemInfo.getCurrentShop().isReservationStaffShift());

                // �c�[���`�b�v�\���b���ݒ�
		ToolTipManager tp = ToolTipManager.sharedInstance();
		tp.setDismissDelay(60000);
                
                if (SystemInfo.isReservationOnly()) {
                    stayColorLabel.setVisible(false);
                    stayLabel.setVisible(false);
                    finishColorLabel.setVisible(false);
                    finishLabel.setVisible(false);
                }
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        previewButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        isLinkScroll = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        reserveLabel = new javax.swing.JLabel();
        reserveColorLabel = new javax.swing.JLabel();
        stayColorLabel = new javax.swing.JLabel();
        stayLabel = new javax.swing.JLabel();
        finishColorLabel = new javax.swing.JLabel();
        finishLabel = new javax.swing.JLabel();
        mobileLabel = new javax.swing.JLabel();
        mobileColorLabel = new javax.swing.JLabel();
        recessColorLabel = new javax.swing.JLabel();
        recessLabel = new javax.swing.JLabel();
        reserveButton = new javax.swing.JButton();
        cardInButton = new javax.swing.JButton();
        recessButton = new javax.swing.JButton();
        printScreen = new javax.swing.JButton();
        pnlWorkingInfo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEvent = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtTraining = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtWorkOff = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        staffPanel = new javax.swing.JPanel();
        staffScrollPane = new javax.swing.JScrollPane();
        staffTable = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        staffNameScrollPane = new javax.swing.JScrollPane();
        staffNameTable = new javax.swing.JTable();
        bedPanel = new javax.swing.JPanel();
        bedScrollPane = new javax.swing.JScrollPane();
        bedTable = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        bedNameScrollPane = new javax.swing.JScrollPane();
        bedNameTable = new javax.swing.JTable();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        date = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        todayButton = new javax.swing.JButton();
        searchCustomerButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPanel2.setOpaque(false);

        previewButton.setIcon(SystemInfo.getImageIcon("/button/print/preview_off.jpg"));
        previewButton.setBorderPainted(false);
        previewButton.setPressedIcon(SystemInfo.getImageIcon("/button/print/preview_on.jpg"));
        previewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewButtonActionPerformed(evt);
            }
        });

        printButton.setIcon(SystemInfo.getImageIcon("/button/common/print_off.jpg"));
        printButton.setBorderPainted(false);
        printButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/print_on.jpg"));
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        isLinkScroll.setText("�X�N���[����A������");
        isLinkScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        isLinkScroll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        isLinkScroll.setOpaque(false);
        isLinkScroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isLinkScrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(previewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(isLinkScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(isLinkScroll)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(previewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel3.setOpaque(false);
        jPanel3.setLayout(null);

        infoPanel.setOpaque(false);

        reserveLabel.setText("�\��");

        reserveColorLabel.setBackground(this.COLOR_RESERVATION);
        reserveColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        reserveColorLabel.setOpaque(true);

        stayColorLabel.setBackground(this.COLOR_STAY);
        stayColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        stayColorLabel.setOpaque(true);

        stayLabel.setText("�ݓX");

        finishColorLabel.setBackground(this.COLOR_FINISH);
        finishColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        finishColorLabel.setOpaque(true);

        finishLabel.setText("�ޓX");

        mobileLabel.setText("WEB�\��");

        mobileColorLabel.setBackground(this.COLOR_MOBILE);
        mobileColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mobileColorLabel.setOpaque(true);

        recessColorLabel.setBackground(new java.awt.Color(111, 0, 111));
        recessColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        recessColorLabel.setOpaque(true);

        recessLabel.setText("�x�e");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addComponent(reserveColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reserveLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stayColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(finishColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(finishLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mobileColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mobileLabel)
                .addGap(18, 18, 18)
                .addComponent(recessColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recessLabel)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mobileColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(finishLabel)
                    .addComponent(finishColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stayLabel)
                    .addComponent(stayColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(reserveColorLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(reserveLabel))
                    .addComponent(mobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recessColorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 13, Short.MAX_VALUE)
                    .addComponent(recessLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 13, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.add(infoPanel);
        infoPanel.setBounds(350, 10, 350, 15);

        reserveButton.setIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_off.jpg"));
        reserveButton.setBorderPainted(false);
        reserveButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_on.jpg"));
        reserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserveButtonActionPerformed(evt);
            }
        });
        jPanel3.add(reserveButton);
        reserveButton.setBounds(0, 0, 92, 25);

        cardInButton.setIcon(SystemInfo.getImageIcon("/button/common/card_in_off.jpg"));
        cardInButton.setBorderPainted(false);
        cardInButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/card_in_on.jpg"));
        cardInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardInButtonActionPerformed(evt);
            }
        });
        jPanel3.add(cardInButton);
        cardInButton.setBounds(141, 0, 92, 25);

        recessButton.setIcon(SystemInfo.getImageIcon("/button/reservation/recess_off.jpg"));
        recessButton.setBorderPainted(false);
        recessButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/recess_on.jpg"));
        recessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recessButtonActionPerformed(evt);
            }
        });
        jPanel3.add(recessButton);
        recessButton.setBounds(245, 0, 92, 25);

        printScreen.setIcon(SystemInfo.getImageIcon("/button/common/reserve_print_off.jpg"));
        printScreen.setBorderPainted(false);
        printScreen.setPressedIcon(SystemInfo.getImageIcon("/button/common/reserve_print_on.jpg"));
        printScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printScreenActionPerformed(evt);
            }
        });
        jPanel3.add(printScreen);
        printScreen.setBounds(698, 2, 92, 25);

        pnlWorkingInfo.setOpaque(false);
        pnlWorkingInfo.setPreferredSize(new java.awt.Dimension(100, 50));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtEvent.setColumns(20);
        txtEvent.setFont(new java.awt.Font("Monospaced", 0, 12));
        txtEvent.setLineWrap(true);
        txtEvent.setRows(6);
        txtEvent.setTabSize(4);
        txtEvent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEventFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEventFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(txtEvent);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtTraining.setColumns(20);
        txtTraining.setFont(new java.awt.Font("Monospaced", 0, 12));
        txtTraining.setLineWrap(true);
        txtTraining.setRows(6);
        txtTraining.setTabSize(4);
        txtTraining.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTrainingFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTrainingFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(txtTraining);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtWorkOff.setColumns(20);
        txtWorkOff.setFont(new java.awt.Font("Monospaced", 0, 12));
        txtWorkOff.setLineWrap(true);
        txtWorkOff.setRows(6);
        txtWorkOff.setTabSize(4);
        txtWorkOff.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtWorkOffFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtWorkOffFocusLost(evt);
            }
        });
        jScrollPane3.setViewportView(txtWorkOff);

        jLabel2.setFont(new java.awt.Font("MS UI Gothic", 1, 12));
        jLabel2.setText("���x");

        jLabel3.setFont(new java.awt.Font("MS UI Gothic", 1, 12));
        jLabel3.setText("���C");

        jLabel4.setFont(new java.awt.Font("MS UI Gothic", 1, 12));
        jLabel4.setText("�s���E���̑�");

        javax.swing.GroupLayout pnlWorkingInfoLayout = new javax.swing.GroupLayout(pnlWorkingInfo);
        pnlWorkingInfo.setLayout(pnlWorkingInfoLayout);
        pnlWorkingInfoLayout.setHorizontalGroup(
            pnlWorkingInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWorkingInfoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlWorkingInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(pnlWorkingInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(pnlWorkingInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );
        pnlWorkingInfoLayout.setVerticalGroup(
            pnlWorkingInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWorkingInfoLayout.createSequentialGroup()
                .addGroup(pnlWorkingInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(0, 0, 0)
                .addGroup(pnlWorkingInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlWorkingInfoLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0))
                    .addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)))
        );

        jLabel2.getAccessibleContext().setAccessibleName("aa");

        staffPanel.setOpaque(false);

        staffScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        staffScrollPane.getVerticalScrollBar().addAdjustmentListener(
            new AdjustmentListener()
            {
                public void adjustmentValueChanged(AdjustmentEvent e)
                {
                    staffNameScrollPane.getVerticalScrollBar().setValue(e.getValue());
                }
            });

            staffScrollPane.getHorizontalScrollBar().addAdjustmentListener(
                new AdjustmentListener()
                {
                    public void adjustmentValueChanged(AdjustmentEvent e)
                    {
                        staffNameScrollPane.getHorizontalScrollBar().setValue(e.getValue());
                    }
                });

                staffTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                staffTable.setRowSelectionAllowed(false);
                staffTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
                staffTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
                //this.initTable(staffTable);
                staffTable.setDefaultRenderer(Object.class, new StripeTableRenderer());
                staffTable.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        staffTableMouseClicked(evt);
                    }
                });
                staffScrollPane.setViewportView(staffTable);

                staffNameScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                staffNameScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                staffNameScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

                staffNameTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "�X�^�b�t��"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
                staffNameTable.setRequestFocusEnabled(false);
                staffNameTable.setRowSelectionAllowed(false);
                staffNameTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
                staffNameTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
                staffNameTable.getTableHeader().setReorderingAllowed(false);
                staffNameTable.getTableHeader().setSize(staffNameTable.getTableHeader().getWidth(), ROW_HEIGHT);
                staffNameTable.setRowHeight(ROW_HEIGHT);
                SwingUtil.setJTableHeaderRenderer(staffNameTable, SystemInfo.getTableHeaderRenderer());
                staffNameScrollPane.setViewportView(staffNameTable);

                javax.swing.GroupLayout staffPanelLayout = new javax.swing.GroupLayout(staffPanel);
                staffPanel.setLayout(staffPanelLayout);
                staffPanelLayout.setHorizontalGroup(
                    staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(staffPanelLayout.createSequentialGroup()
                        .addComponent(staffNameScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staffScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                        .addContainerGap())
                );
                staffPanelLayout.setVerticalGroup(
                    staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, staffPanelLayout.createSequentialGroup()
                        .addGroup(staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(staffNameScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(staffScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                        .addGap(10, 10, 10))
                );

                bedPanel.setOpaque(false);

                bedScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                bedScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                bedScrollPane.getVerticalScrollBar().addAdjustmentListener(
                    new AdjustmentListener()
                    {
                        public void adjustmentValueChanged(AdjustmentEvent e)
                        {
                            bedNameScrollPane.getVerticalScrollBar().setValue(e.getValue());
                        }
                    });

                    bedTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                    bedTable.setRowSelectionAllowed(false);
                    bedTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
                    bedTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
                    //this.initTable(bedTable);
                    bedTable.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            bedTableMouseClicked(evt);
                        }
                    });
                    bedScrollPane.setViewportView(bedTable);

                    bedNameScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                    bedNameScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    bedNameScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

                    bedNameTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                            "�{�p�䖼"
                        }
                    ) {
                        Class[] types = new Class [] {
                            java.lang.String.class
                        };
                        boolean[] canEdit = new boolean [] {
                            false
                        };

                        public Class getColumnClass(int columnIndex) {
                            return types [columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return canEdit [columnIndex];
                        }
                    });
                    bedNameTable.setRequestFocusEnabled(false);
                    bedNameTable.setRowSelectionAllowed(false);
                    bedNameTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
                    bedNameTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
                    bedNameTable.getTableHeader().setReorderingAllowed(false);
                    bedNameTable.getTableHeader().setSize(bedNameTable.getTableHeader().getWidth(), ROW_HEIGHT);
                    bedNameTable.setRowHeight(ROW_HEIGHT);
                    SwingUtil.setJTableHeaderRenderer(bedNameTable, SystemInfo.getTableHeaderRenderer());
                    bedNameScrollPane.setViewportView(bedNameTable);

                    javax.swing.GroupLayout bedPanelLayout = new javax.swing.GroupLayout(bedPanel);
                    bedPanel.setLayout(bedPanelLayout);
                    bedPanelLayout.setHorizontalGroup(
                        bedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(bedPanelLayout.createSequentialGroup()
                            .addComponent(bedNameScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bedScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                            .addContainerGap())
                    );
                    bedPanelLayout.setVerticalGroup(
                        bedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bedPanelLayout.createSequentialGroup()
                            .addGroup(bedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(bedScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                                .addComponent(bedNameScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap())
                    );

                    shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                    shop.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            shopActionPerformed(evt);
                        }
                    });

                    date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                    date.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
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

                    todayButton.setIcon(SystemInfo.getImageIcon("/button/common/today_m_off.jpg"));
                    todayButton.setBorderPainted(false);
                    todayButton.setEnabled(false);
                    todayButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/today_m_on.jpg"));
                    todayButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            todayButtonActionPerformed(evt);
                        }
                    });

                    searchCustomerButton.setIcon(SystemInfo.getImageIcon("/button/search/search_customer_off.jpg"));
                    searchCustomerButton.setBorderPainted(false);
                    searchCustomerButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_customer_on.jpg"));
                    searchCustomerButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            searchCustomerButtonActionPerformed(evt);
                        }
                    });

                    updateButton.setIcon(SystemInfo.getImageIcon("/button/common/update_m_off.jpg"));
                    updateButton.setBorderPainted(false);
                    updateButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_m_on.jpg"));
                    updateButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            updateButtonActionPerformed(evt);
                        }
                    });

                    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                    this.setLayout(layout);
                    layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(staffPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(bedPanel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap())
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(todayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                                    .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(41, 41, 41)
                                                    .addComponent(searchCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addComponent(pnlWorkingInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGap(26, 26, 26))))
                    );
                    layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pnlWorkingInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(searchCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(todayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(3, 3, 3)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(staffPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );
                }// </editor-fold>//GEN-END:initComponents

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed

        printButton.setCursor(null);
        
        try {
            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.printReservationList();
            
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_printButtonActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
        if (isAutoOpenDialog() && this.isVisible()) {
            for (ReservationHeader rh: ts.getStaffs()) {
                for (ReservationJTextField rtf : rh.getReservations()) {
                    if (rtf.getReservation().getShop().getShopID().equals(getNextReserveShopID()) &&
                        rtf.getReservation().getReservationNo().equals(getNextReserveNo()))
                    {
                        setAutoOpenDialog(false);                        
                        this.registReserve( rtf.getReservation(), null );
                        return;
                    }
                }
            }
        }
        
    }//GEN-LAST:event_formComponentResized

        private void cardInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardInButtonActionPerformed
// �J�[�h�}���҂���ʂ�\��
            Integer customer_id = InsertCardDialog.ShowReadDialog(parentFrame);
            if( customer_id != null ){
                DataReservation seldr = null;
                ReservationStatus	rs	=	new ReservationStatus();
                rs.loadReservation(currentDate, 1);
                
                for(DataReservation dr : rs.getDatas()) {
                    if(dr.getCustomer().getCustomerID().equals(customer_id)){
                        seldr = dr;
                        break;
                    }
                }
                if( seldr == null ){
                    // �ڋq���������p���ŗ\���ʂ��J��
                    customer = new MstCustomer(customer_id);
                    try {
                        customer.load(SystemInfo.getConnection());
                    } catch (SQLException ex) {
                        customer = null;
                    }
                    this.registReserve( null, null );
                }else{
                    //// ���������\��ŗ\���ʂ��J��
                    //this.registReserve( seldr, null );
                    // �ݓX�ɂ��邩���m�F����
                    
                    //�m�F���b�Z�[�W
                    if(MessageDialog.showYesNoDialog(this,
                                            MessageUtil.getMessage(5126),
                                            this.getTitle(),
                                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                    {
                        // �ݓX�ɂ���
			ConnectionWrapper	con	=	SystemInfo.getConnection();
                        
                        seldr.setStatus(2);
                        seldr.setSubStatus(1);
                        
                        try {
                            if(seldr.updateStatus(con))
                            {
        			this.load();
                                
                                MessageDialog.showMessageDialog(this,
                                        MessageUtil.getMessage(5002),
                                        this.getTitle(),
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                MessageDialog.showMessageDialog(this,
                                        MessageUtil.getMessage(5110),
                                        this.getTitle(),
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(5110),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }                    
                }
            }
        }//GEN-LAST:event_cardInButtonActionPerformed

	private void txtEventFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEventFocusLost
		String strLostFocus;
		ConnectionWrapper	con	=	SystemInfo.getConnection();

		strLostFocus = txtEvent.getText();

		if (strLostFocus != m_strTxtEventGotFocus)
		{
			registerWorkingInfoRoutine(con, eWORKINGTYPE.Event, strLostFocus);
		}
	}//GEN-LAST:event_txtEventFocusLost

	private void txtTrainingFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTrainingFocusLost
		String strLostFocus;
		ConnectionWrapper	con	=	SystemInfo.getConnection();

		strLostFocus = txtTraining.getText();

		if (strLostFocus != m_strTxtTrainingGotFocus)
		{
			registerWorkingInfoRoutine(con, eWORKINGTYPE.Training, strLostFocus);
		}
	}//GEN-LAST:event_txtTrainingFocusLost

	private void txtWorkOffFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWorkOffFocusLost
		String strLostFocus;
		ConnectionWrapper	con	=	SystemInfo.getConnection();

		strLostFocus = txtWorkOff.getText();

		if (strLostFocus != m_strTxtWorkOffGotFocus)
		{
			registerWorkingInfoRoutine(con, eWORKINGTYPE.Off, strLostFocus);
		}
	}//GEN-LAST:event_txtWorkOffFocusLost

	private void txtEventFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEventFocusGained
		m_strTxtEventGotFocus = txtEvent.getText();
	}//GEN-LAST:event_txtEventFocusGained

	private void txtTrainingFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTrainingFocusGained
		m_strTxtTrainingGotFocus = txtTraining.getText();
	}//GEN-LAST:event_txtTrainingFocusGained

	private void txtWorkOffFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWorkOffFocusGained
		m_strTxtWorkOffGotFocus = txtWorkOff.getText();
	}//GEN-LAST:event_txtWorkOffFocusGained

    private void bedTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bedTableMouseClicked

        if (reserveButton.isVisible() && evt.getClickCount() == 2) {
            //�_�u���N���b�N���A�N���b�N�������Ԃ������p���ŗ\��o�^��ʂ��Ăяo��
            this.registReserve(null, getCalendar(evt.getX()), true);
        }
    }//GEN-LAST:event_bedTableMouseClicked

    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewButtonActionPerformed
            //�\��ꗗ��ʂ��Ăяo��
            ReservationTimeTableWholeDisplayPanel rtt = new ReservationTimeTableWholeDisplayPanel(date.getDate(),(MstShop)shop.getSelectedItem());
            SwingUtil.openAnchorDialog( parentFrame, true, rtt, "�\��ꗗ", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
            rtt = null;
    }//GEN-LAST:event_previewButtonActionPerformed

    private void staffTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_staffTableMouseClicked
        int btn = evt.getButton();
        if (reserveButton.isVisible() && evt.getClickCount() == 2 && btn == MouseEvent.BUTTON1) {
            //�_�u���N���b�N���A�N���b�N�������Ԃ������p���ŗ\��o�^��ʂ��Ăяo��
            this.registReserve(null, getCalendar(evt.getX()));
        }else if(recessButton.isVisible() &&evt.getClickCount() == 2 &&  btn == MouseEvent.BUTTON3){
            DefaultTableModel hModel = (DefaultTableModel)staffNameTable.getModel();
            ReservationHeader rh = (ReservationHeader)hModel.getValueAt((evt.getY() / ROW_HEIGHT), 0);
            Integer staffID = null;
            if (rh.getId() != null) {
                staffID = Integer.valueOf(rh.getId().toString());
            }
            RegistPersonalRecessPanel.ShowDialog(parentFrame, (MstShop)shop.getSelectedItem(), date.getDate(), getCalendar(evt.getX()), staffID);

// TODO�@STAFFID�擾
//            SystemInfo.getLogger().log(Level.INFO, "NEW�O");
//            rpr = new RegistPersonalRecessPanel(parentFrame, (MstShop)shop.getSelectedItem(), date.getDate(), getCalendar(evt.getX()));
////            RegistPersonalRecessPanel.ShowDialog(parentFrame, (MstShop)shop.getSelectedItem(), date.getDate(), getCalendar(evt.getX()));
//            SystemInfo.getLogger().log(Level.INFO, "NEW��");
//            if (getCalendar(evt.getX()) != null && staffTable.getSelectedRow() >= 0) {
//                            SystemInfo.getLogger().log(Level.INFO, "ID�v�Z");
//                DefaultTableModel hModel = (DefaultTableModel)staffNameTable.getModel();
//                ReservationHeader rh = (ReservationHeader)hModel.getValueAt(staffTable.getSelectedRow(), 0);
//                if (rh.getId() != null) {
//                    SystemInfo.getLogger().log(Level.INFO, "�X�^�b�tID�Z�b�g");
//                    rpr.setSelectedChargeStaffID(Integer.valueOf(rh.getId().toString()));
//                 }
//            }
//            SwingUtil.openAnchorDialog(parentFrame, true, rpr, "�x�e���ԓo�^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);
            this.load();
            loadWorkingInfo();
        }

    }//GEN-LAST:event_staffTableMouseClicked

	private void isLinkScrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isLinkScrollActionPerformed
		this.linkScroll(staffScrollPane, bedScrollPane);
	}//GEN-LAST:event_isLinkScrollActionPerformed

	private void reserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveButtonActionPerformed
		this.registReserve( null, null );
	}//GEN-LAST:event_reserveButtonActionPerformed

	private void todayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todayButtonActionPerformed
		date.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
	}//GEN-LAST:event_todayButtonActionPerformed

	private void dateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateFocusGained
            if (date.getInputContext() != null) {
                date.getInputContext().setCharacterSubsets(null);
            }
	}//GEN-LAST:event_dateFocusGained

	private void dateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dateItemStateChanged
            
            if (date.getDate() != null && String.format("%1$tY/%1$tm/%1$td", date.getDate()) != String.format("%1$tY/%1$tm/%1$td", currentDate)) {
                this.load();
            }

            loadWorkingInfo();

            if (date.getDate().equals(SystemInfo.getCurrentShop().getSystemDate().getTime())) {
                todayButton.setEnabled(false);
            } else {
                todayButton.setEnabled(true);
            }

	}//GEN-LAST:event_dateItemStateChanged

	private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed

            if (isShopLoading) return;
            
            this.load();
            loadWorkingInfo();

	}//GEN-LAST:event_shopActionPerformed

        private void searchCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerButtonActionPerformed

            SearchCustomerDialog sc = new SearchCustomerDialog( parentFrame, true, true);
            sc.setShopID( ( SystemInfo.getSetteing().isShareCustomer() ? 0 : ( (MstShop)shop.getSelectedItem() ).getShopID() ) );
            sc.setVisible(true);
            sc.dispose();
            sc = null;
        }//GEN-LAST:event_searchCustomerButtonActionPerformed

        private void recessButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recessButtonActionPerformed
                //RegistRecessPanel.ShowDialog(parentFrame, (MstShop)shop.getSelectedItem(), date.getDate() );
                RegistPersonalRecessPanel.ShowDialog(parentFrame, (MstShop)shop.getSelectedItem(), date.getDate() );
                //rpr = new RegistPersonalRecessPanel(parentFrame, (MstShop)shop.getSelectedItem(), date.getDate());
                this.load();
                loadWorkingInfo();
        }//GEN-LAST:event_recessButtonActionPerformed

        private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
                this.load();
                loadWorkingInfo();
        }//GEN-LAST:event_updateButtonActionPerformed

        private void printScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printScreenActionPerformed

            printScreen.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                //�p���T�C�Y�A�p�������̐ݒ�
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(MediaSizeName.ISO_A4);
                aset.add(OrientationRequested.LANDSCAPE);
                PrinterJob printJob = PrinterJob.getPrinterJob();
                printJob.setPrintable(this);
                try {
                    //�������
                    printJob.print(aset);
                } catch (Exception e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

        }//GEN-LAST:event_printScreenActionPerformed
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane bedNameScrollPane;
    private javax.swing.JTable bedNameTable;
    private javax.swing.JPanel bedPanel;
    private javax.swing.JScrollPane bedScrollPane;
    private com.geobeck.swing.JTableEx bedTable;
    private javax.swing.JButton cardInButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo date;
    private javax.swing.JLabel finishColorLabel;
    private javax.swing.JLabel finishLabel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JCheckBox isLinkScroll;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel mobileColorLabel;
    private javax.swing.JLabel mobileLabel;
    private javax.swing.JPanel pnlWorkingInfo;
    private javax.swing.JButton previewButton;
    private javax.swing.JButton printButton;
    private javax.swing.JButton printScreen;
    private javax.swing.JButton recessButton;
    private javax.swing.JLabel recessColorLabel;
    private javax.swing.JLabel recessLabel;
    private javax.swing.JButton reserveButton;
    private javax.swing.JLabel reserveColorLabel;
    private javax.swing.JLabel reserveLabel;
    private javax.swing.JButton searchCustomerButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JScrollPane staffNameScrollPane;
    private javax.swing.JTable staffNameTable;
    private javax.swing.JPanel staffPanel;
    private javax.swing.JScrollPane staffScrollPane;
    private com.geobeck.swing.JTableEx staffTable;
    private javax.swing.JLabel stayColorLabel;
    private javax.swing.JLabel stayLabel;
    private javax.swing.JButton todayButton;
    private javax.swing.JTextArea txtEvent;
    private javax.swing.JTextArea txtTraining;
    private javax.swing.JTextArea txtWorkOff;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables

        public void setDate(java.util.Date reserveDate){
            date.setDate(reserveDate);
        }

        public void setAdjustScrollToTime(Integer hour, Integer minute) {
            this.adjustScrollToCurrentTime(staffScrollPane, staffTable, hour, minute);
            this.adjustScrollToCurrentTime(bedScrollPane, bedTable, hour, minute);
        }
        
 	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(todayButton);
		SystemInfo.addMouseCursorChange(updateButton);
		SystemInfo.addMouseCursorChange(previewButton);
		SystemInfo.addMouseCursorChange(reserveButton);
		SystemInfo.addMouseCursorChange(isLinkScroll);
		SystemInfo.addMouseCursorChange(cardInButton);
		SystemInfo.addMouseCursorChange(printButton);
		SystemInfo.addMouseCursorChange(searchCustomerButton);
		SystemInfo.addMouseCursorChange(recessButton);
		SystemInfo.addMouseCursorChange(printScreen);
	}
	
	/**
	 * �R���|�[�l���g�̊e���X�i�[���Z�b�g����B
	 */
	private void setListener()
	{
		date.addKeyListener(SystemInfo.getMoveNextField());
		date.addFocusListener(SystemInfo.getSelectText());
		
		staffScrollPane.getViewport().addChangeListener(
				new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						linkScroll(staffScrollPane, bedScrollPane);
					}
				});
		
		bedScrollPane.getViewport().addChangeListener(
				new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
                                                linkScroll(bedScrollPane, staffScrollPane);
					}
				});
	}
	
        public void setCustomer(MstCustomer customer){
            this.customer = customer;
        }
        
	/**
	 * �\��o�^�_�C�A���O���J��
	 */
	public void registReserve( DataReservation dr, GregorianCalendar clickTime )
        {
            this.registReserve(dr, clickTime, false);
        }
	public void registReserve( DataReservation dr, GregorianCalendar clickTime, boolean isBedSelected)
	{
		if( this.getSelectedShop() == null ) return;

		rrp = new RegistReservationDialog( this.getSelectedShop(), date.getDate(), (this.getOpener() == null ? this : parent) , clickTime, customer, nextReservation );
                rrp.setIsCheckOut(this.isOpenByCheckout());
		rrp.LoadReservation( dr );

                if (isBedSelected) {

                    if (clickTime != null && bedTable.getSelectedRow() >= 0) {
                        DefaultTableModel hModel = (DefaultTableModel)bedNameTable.getModel();
                        ReservationHeader rh = (ReservationHeader)hModel.getValueAt(bedTable.getSelectedRow(), 0);
                        if (rh.getId() != null) {
                            rrp.setSelectedBedID(Integer.valueOf(rh.getId().toString()));
                        }
                    }
                    
                } else {
                    
                    if (clickTime != null && staffTable.getSelectedRow() >= 0) {
                        DefaultTableModel hModel = (DefaultTableModel)staffNameTable.getModel();
                        ReservationHeader rh = (ReservationHeader)hModel.getValueAt(staffTable.getSelectedRow(), 0);
                        if (rh.getId() != null) {
                            rrp.setSelectedChargeStaffID(Integer.valueOf(rh.getId().toString()));
                        }
                    }
                }

                /*
                if(customer != null){
                    rrp.setCustomer(customer);
                }
                */
		SwingUtil.openAnchorDialog( this.parentFrame, true, rrp, (dr == null ? "�\��o�^" : "�\��m�F"), SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                boolean isReserved = false;
                if (rrp != null) {
                    isReserved = rrp.isReserved();
                    rrp.releaceMemory();
                    rrp = null;
                }

		System.gc();
		
                if(this.isOpenByCheckout()){
                    ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
                }
                
                
		if(isReserved) {
		    this.load();
		}
	}
	public void registReserve( RegistReservationDialog rrd )
        {
		if( this.getSelectedShop() == null ) return;

		rrp = rrd;

                // ParentFrame���Z�b�g����Ă���ꍇ�͗\��\��\������
                if (this.getParentFrame() != null) {
                    this.getParentFrame().changeContents(this);
                }

		SwingUtil.openAnchorDialog( this.parentFrame, true, rrp, "�\��o�^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

		boolean isReserved = rrp.isReserved();

		rrp.releaceMemory();
		rrp = null;
		System.gc();

                if(this.isOpenByCheckout()){
                    ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
                }

		if(isReserved) {
		    this.load();
		}
        }

	/**
	 * �I������Ă���X�܂��擾����B
	 * @return �I������Ă���X��
	 */
	private MstShop getSelectedShop()
	{
            MstShop result = null;
            
            if (0 <= shop.getSelectedIndex()) {
                result = (MstShop)shop.getSelectedItem();
            }
            
            return result;
	}
	
	/**
	 * �I������Ă���X�܂�ID���擾����B
	 * @return �I������Ă���X�܂�ID
	 */
	private Integer getSelectedShopID()
	{
            Integer result = 0;
            
            MstShop ms = this.getSelectedShop();
		
            if (ms != null) {
                result = ms.getShopID();
            }
            
            return result;
	}
	
	/**
	 * �����N�X�N���[������
	 */
	private void linkScroll(JScrollPane sp0, JScrollPane sp1)
	{
            if(isLinkScroll.isSelected() && !isLoading)
            {
                Double temp = (double)sp0.getHorizontalScrollBar().getValue() / (double)sp0.getHorizontalScrollBar().getMaximum();
                temp *= (double)sp0.getHorizontalScrollBar().getMaximum();
                sp1.getHorizontalScrollBar().setValue(temp.intValue());
            }
	}

	/**
	 * �w��X�܂̊J�X�X���Ԃ��擾����
	 */
	private void setOpenCloseTime()
	{
            if (currentDate == null) {
                term = ts.getShop().getTerm();
                openHour = ts.getShop().getOpenHour();
                openMinute = ts.getShop().getOpenMinute();
                closeHour = ts.getShop().getCloseHour();
                closeMinute = ts.getShop().getCloseMinute();

                // �^�C���X�P�W���[���ɊJ�X�`�X���ԏ���ݒ肷��
                ts.setOpenHour(openHour);
                ts.setOpenMinute(openMinute);
                ts.setCloseHour(closeHour);
                ts.setCloseMinute(closeMinute);
                
            } else {
                
                ts.setOpenCloseTimeByReservation(currentDate);
                openHour = ts.getOpenHour();
                openMinute = ts.getOpenMinute();
                closeHour = ts.getCloseHour();
                closeMinute = ts.getCloseMinute();
            }

            ts.setTerm(term);
	}
	
	/**
	 * ��w�b�_�p�̔z����擾����B
	 * @return ��w�b�_�p�̔z��
	 */
	private	Vector getHeaderTextArray()
	{
            Vector<String> ht = new Vector<String>();

            if (openHour == null || closeHour == null || term == null ) return ht;

            for (int h = openHour; h <= closeHour; h++) {

                for (int m = 0; m < 60; m += term) {

                    //�J�X���Ԃ��O�̏ꍇ
                    if (h == openHour && m < openMinute) continue;
                    
                    //�X���Ԃ���̏ꍇ
                    if (h == closeHour && closeMinute <= m) break;

                    ht.add(Integer.toString(m));
                    
/*                    
                    //�O���̎��A���Ԃ��Z�b�g
                    if (m == 0) {
                        ht.add(Integer.toString(h));
                    } else {
                        ht.add("");
                    }
*/
                }
            }

            return ht;
	}
	
	/**
	 * 
	 * @param table 
	 */
	private void initTable(JTable table)
	{
            table.setModel(
                new DefaultTableModel(this.getHeaderTextArray(), 0) {
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                });

            table.getTableHeader().setReorderingAllowed(false);
            table.getTableHeader().setSize(staffTable.getTableHeader().getWidth(), ROW_HEIGHT);
            table.setRowHeight(ROW_HEIGHT);

            for (int col = 0; col < table.getColumnModel().getColumnCount(); col ++) {
                table.getColumnModel().getColumn(col).setPreferredWidth(COLUMN_WIDTH);
                table.getColumnModel().getColumn(col).setResizable(false);
            }

            SwingUtil.setJTableHeaderRenderer(table, SystemInfo.getTableHeaderRenderer());
	}
	
	/**
	 * �f�[�^��ǂݍ��ށB
	 */
	public void load()
	{
            // �{�p��̎g�p�L���ŗv��Ȃ��������
            this.setBedStaffPanelVisible();

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // �{���̏ꍇ�͗\��o�^�{�^�����\���ɂ���
                if (SystemInfo.getCurrentShop().getShopID() == 0) {

                    //reserveButton.setVisible(false);
                    cardInButton.setVisible(false);
                    term = this.getSelectedShop().getTerm();
                    openHour = this.getSelectedShop().getOpenHour();
                    openMinute = this.getSelectedShop().getOpenMinute();
                    closeHour = this.getSelectedShop().getCloseHour();
                    closeMinute = this.getSelectedShop().getCloseMinute();

                } else {

                    boolean mode = SystemInfo.getCurrentShop().equals(this.getSelectedShop());
                    //reserveButton.setVisible(mode);
                    if( !SystemInfo.isUsePointcard() ){
                        cardInButton.setVisible(false);
                    }
                }

                currentDate.setTime(date.getDate());

                ts.setShop(this.getSelectedShop());
                this.setOpenCloseTime();

                //�e�[�u����������
                if ( 0 < this.getSelectedShopID() ) {
                    this.initTable(staffTable);
                    this.initTable(bedTable);
                }

                // ���̃e�[�u���̃w�b�_��������
                this.setNameTableHeaderHeight(staffNameTable);
                this.setNameTableHeaderHeight(bedNameTable);
                // �^�C���e�[�u���̎��ԕ\��
                this.groupingTimeHeader(staffTable);
                this.groupingTimeHeader(bedTable);
                
                //�f�[�^��ǂݍ���
                if (!ts.load(currentDate.getTime())) {
                    return;
                }

                //�f�[�^��\��
                this.showData();

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}
	
        private void setNameTableHeaderHeight(JTable table) {
            JTableHeader header = table.getTableHeader();
            Dimension dim = header.getPreferredSize();
            dim.height = 34;
            header.setPreferredSize(dim);
        }
        
        private void groupingTimeHeader(JTable table) {
        
            GroupableTableHeader header = (GroupableTableHeader)table.getTableHeader();
            header.setBackground(new Color(204,204,204));
            TableColumnModel cm = table.getColumnModel();
            
            ColumnGroup name = null;

            int i = 0;
            for (int h = openHour; h < closeHour; h++) {
                name = new ColumnGroup(h + ":00");
                
                for (int m = 0; m < 60; m += term) {
                    //�J�X���Ԃ��O�̏ꍇ
                    if (h == openHour && m < openMinute) continue;
                    //�X���Ԃ���̏ꍇ
                    if (h == closeHour && closeMinute <= m) break;

                    name.add(cm.getColumn(i++));
                }

                header.addColumnGroup(name);
            }
            
            if (closeMinute > 0) {
                name = new ColumnGroup(closeHour + ":00");
                while (i < cm.getColumnCount()) {
                    name.add(cm.getColumn(i++));
                }
                header.addColumnGroup(name);
            }
        }

	/**
	 * �f�[�^��\������B
	 */
	private void showData()
	{
                this.setIgnoreRepaint(true);    //�`����~�߂�

                this.showData(ts.getStaffs(), staffScrollPane, staffNameTable, staffTable);
		this.showData(ts.getBeds(), bedScrollPane, bedNameTable, bedTable);
		
		//���ݎ����|�P���ԂɃX�N���[���o�[�����킹��
		this.adjustScrollToCurrentTime(staffScrollPane, staffTable);
		this.adjustScrollToCurrentTime(bedScrollPane, bedTable);

                this.setIgnoreRepaint(false);   //�`����ĊJ����
        }
	
	/**
	 * �f�[�^��\������B
	 */
	private void showData(ArrayList<ReservationHeader> headers,
			JScrollPane scrollPane,
			JTable headerTable, JTable table)
	{
		SwingUtil.clearTable(headerTable);
		SwingUtil.clearTable(table);
		headerTable.removeAll();
		table.removeAll();
		
		int headerRow = 0;
		int headerRows = 0;
		
                staffTable.setName(null);
                
		for(ReservationHeader rh : headers)
		{
                    //�w�b�_�[�f�[�^��ǉ�
                    this.addHeader(headerTable, table, rh, headerRow);
                    headerRows = 1;

                    boolean isAddNewRow = false;
                    
                    //�\��f�[�^��ǉ�
                    for (ReservationJTextField rtf: rh.getReservations()) {
                        
                        // �\�񂪑��݂��Ȃ��ꍇ
                        if (rtf.getReservation().getReservationNo() == 0) continue;
                        
                        if (table.equals(staffTable)) {
                            if (this.getSelectedShop().getReservationNullLine() == 1) {
                                if (!isAddNewRow) {
                                    this.addHeaderHeight(headers, headerTable, table, rtf.getHeader(), rh);
                                    isAddNewRow = true;
                                }
                            }
                        }
                        
                        headerRows = this.addReservation(
                                                headers,
                                                scrollPane,
                                                headerTable,
                                                table,
                                                rtf,
                                                headerRow,
                                                headerRows);
                    }
                    
                    if (this.getSelectedShop().getReservationNullLine() == 1) {
                        if (isAddNewRow) {
                            headerRows++;
                        }
                    }
                    
                    headerRow += headerRows;
		}
	}

	/**
	 * �w�b�_���w�b�_�p�e�[�u���ɒǉ�����B
	 * @param headerTable �w�b�_�p�e�[�u��
	 * @param table �f�[�^�p�e�[�u��
	 * @param rh �\��w�b�_�f�[�^
	 * @param headerRow �ǉ�����s
	 */
	private void addHeader(JTable headerTable, JTable table, ReservationHeader rh, int headerRow)
	{
		rh.setLocation(0, ROW_HEIGHT * headerRow);
		rh.setSize(HEADER_WIDTH, ROW_HEIGHT);
		rh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                
                
                if (table.equals(staffTable)) {

                    if (staffTable.getName() != null) {
                        rh.setBackground(new Color(230,230,204));
                        staffTable.setName(null);
                    } else {
                        rh.setBackground(new Color(255,255,255));
                        staffTable.setName("1");
                    }

                } else {
                    
                    rh.setBackground(COLOR_HEADER);
                }

                // �^�C���e�[�u���ɍs��ǉ�
                TimeTableDrawer.makeNewRow(this.getSelectedShop(), ts, rh, table);
                
		rh.setEditable(false);
                
		DefaultTableModel hModel = (DefaultTableModel)headerTable.getModel();
		Vector<Object> v = new Vector<Object>();
		v.add(rh);
		hModel.addRow(v);

		headerTable.add(rh);
	}
	
        private class StripeTableRenderer extends DefaultTableCellRenderer {
            
            private final Color evenColor = new Color(240, 240, 255);
            
            public Component getTableCellRendererComponent(
                                    JTable table,
                                    Object value,
                                    boolean isSelected,
                                    boolean hasFocus,
                                   int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (! isSelected) {
                    DefaultTableModel model = (DefaultTableModel)staffNameTable.getModel();
                    setBackground(((ReservationHeader)model.getValueAt(row,0)).getBackground());
                }

                setHorizontalAlignment((value instanceof Number)?RIGHT:LEFT);
                return this;
            }
        }

        
        
	/**
	 * ���Ԃ��炘���W���擾����B
	 * @param hour ��
	 * @param minute ��
	 * @return ���ԂɑΉ����邘���W
	 */
	private int getLeft(int hour, int minute)
	{
		int	left	=	0;
		
		left	+=	(hour - openHour) * 60 / term * COLUMN_WIDTH;
		left	+=	(minute - openMinute) / term * COLUMN_WIDTH;
		
		return	left;
	}
	
	/**
	 * JTextField�̐F���擾����B
	 * @param status ���
	 * @return JTextField�̐F
	 */
	private Color getColor(int status)
	{
		switch(status)
		{
			//�\��
			case 1:
				return	COLOR_RESERVATION;
			//�ݓX
			case 2:
				return	COLOR_STAY;
			//�ޓX
			case 3:
				return	COLOR_FINISH;
			default:
				//return	COLOR_MOBILE;
				return	new Color(0x000000);
		}
	}
	
	/**
	 * �w�b�_�̍������P�s�����₷�B
	 * @param headerTable �w�b�_�p�e�[�u��
	 * @param table �f�[�^�p�e�[�u��
	 * @param header �w�b�_�f�[�^
	 */
	private void addHeaderHeight(
                        ArrayList<ReservationHeader> headers,
			JTable headerTable,
                        JTable table,
			ReservationHeader header,
                        ReservationHeader current_rh)
	{
		header.setSize(header.getWidth(), header.getHeight() + ROW_HEIGHT);
		
		//�w�b�_��1�s�ǉ�
		DefaultTableModel hModel = (DefaultTableModel)headerTable.getModel();
		Vector<Object> v = new Vector<Object>();
		v.add(header);
		hModel.addRow(v);

                // �^�C���e�[�u���ɍs��ǉ�
                if (header.getText().length() > 0 && table.equals(staffTable)) {

                    TimeTableDrawer.makeNewRow(this.getSelectedShop(), null, current_rh, table);

                } else {
                    
                    DefaultTableModel model = (DefaultTableModel)table.getModel();
                    v = new Vector<Object>();
                    for (int i = 0; i < model.getColumnCount(); i ++) {
                        JLabel label = new JLabel();
                        label.setBackground(Color.WHITE);
                        label.setOpaque(false);
                        label.setBorder(null);
                        v.add(label);
                    }
                    model.addRow(v);
                }
                
		//���̃f�[�^�̈ʒu��S�Ă��炷
		for(ReservationHeader rh : headers)
		{
			if(header.getY() < rh.getY())
			{
				rh.setLocation(rh.getX(), rh.getY() + ROW_HEIGHT);
				
				for(ReservationJTextField rtf : rh.getReservations())
				{
					rtf.setLocation(rtf.getX(), rtf.getY() + ROW_HEIGHT);
					rtf.repaint();
				}
			}
		}
	}
	
	/**
	 * �\����e�[�u���ɒǉ�����B
	 * @param headerTable �w�b�_�p�e�[�u��
	 * @param table �f�[�^�p�e�[�u��
	 * @param rtf �^�C���X�P�W���[����ʗpJTextField
	 * @param headerRow �w�b�_�̍s
	 * @param headerRows �w�b�_�̍s��
	 * @return �w�b�_�̍s��
	 */
	private int addReservation(
                        final ArrayList<ReservationHeader> headers,
			final JScrollPane scrollPane,
			final JTable headerTable,
                        final JTable table,
			ReservationJTextField rtf,
                        int headerRow,
                        int headerRows)
	{
            if (rtf.getReservation().getCustomer().getVisitCount() == 0) {
                //�V�K�ڋq�̏ꍇ�A�ԕ����\��
                rtf.setForeground(new Color(255, 60, 60));
            }
            //������S�̓I�ɏ����߂ɐݒ�
            rtf.setFont(new Font("MS UI Gothic", Font.PLAIN, 10));
            //�T�C�Y��ݒ�
            rtf.setSize(term, COLUMN_WIDTH, ROW_HEIGHT - 1);
            //�ʒu��ݒ�
            rtf.setLocation(this.getLeft(rtf.getHour(currentDate.getTime()), rtf.getMinute()), ROW_HEIGHT * headerRow);

            //�f�[�^���d�����Ȃ��悤�ɔz�u����
            for (int i = 1; i <= headerRows; i ++) {

                //�d������f�[�^�����݂���ꍇ
                if (this.isExistOverlapData(rtf, false)) {

                    if (i == headerRows) {
                        //�s��1�s�ǉ�
                        this.addHeaderHeight(headers, headerTable, table, rtf.getHeader(), rtf.getHeader());
                        headerRows ++;
                    }

                    rtf.setLocation(rtf.getX(), rtf.getY() + ROW_HEIGHT);

                } else {
                    break;
                }
            }

            rtf.setBackground(this.getColor(rtf.getReservation().getStatus()));
            rtf.setTexts(this.getSelectedShop(), table.equals(bedTable));

            if (rtf.getReservation().getMobileFlag() != 0) {
                rtf.setBackground(COLOR_MOBILE);//�w�i�F�ݒ�
                rtf.setBorder(null);
                //Border border = BorderFactory.createMatteBorder(3, 3, 3, 3, COLOR_MOBILE);
                //rtf.setBorder(border);
            } else {
                rtf.setBorder(null);
            }

            //�}�E�X�Ńh���b�O�����Ƃ��̏�����ǉ�
            /*
            rtf.addMouseMotionListener(
                new java.awt.event.MouseMotionAdapter() {
                    public void mouseDragged (java.awt.event.MouseEvent evt) {
                        //if(SystemInfo.isGroup())	return;
                        //textMouseDragged(headers, scrollPane, table, evt);
                    }
                });
            */

            //�}�E�X�̃{�^����������Ƃ��A�_�u���N���b�N�̏�����ǉ�
            rtf.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    public void mouseClicked (java.awt.event.MouseEvent evt) {                                         
                        if (reserveButton.isVisible() && evt.getClickCount() == 2) {
                            try {
                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                registReserve( ((ReservationJTextField)evt.getComponent()).getReservation(), null );
                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
                    }                                    
                });

            rtf.setComponentPopupMenu(
                new OpenCustomerPopupMenu(
                            this.getParentFrame(), true,
                            rtf.getReservation().getCustomer().getCustomerID()));

            table.add(rtf);

            return headerRows;
	}
	
	/**
	 * �X�N���[���o�[�̈ʒu���œK������B
	 * @param text �h���b�O����JTextField
	 */
	private void adjustScrollBar(final JScrollPane sp, JTextArea text)
	{
		JScrollBar	sb	=	sp.getHorizontalScrollBar();

		if(sb.getValue() + sp.getWidth() < text.getX() + text.getWidth())
		{
			sb.setValue(text.getX() + text.getWidth() - sp.getWidth());
		}
		else if(text.getX() < sb.getValue())
				sb.setValue(text.getX());
	}
	
	/**
	 * �����W����Ή����鎞�Ԃ�Calendar���擾����B
	 * @param x �����W
	 * @return Calendar
	 */
	private GregorianCalendar getCalendar(int x)
	{
		GregorianCalendar	temp	=	(GregorianCalendar)currentDate.clone();
		
//		Integer a = x / COLUMN_WIDTH;
//		Integer b = (60 - (openMinute == 0 ? 60 :openMinute)) / term;

		
//		Integer	hour	=	openHour + (x / COLUMN_WIDTH + (60 - (openMinute == 0 ? 60 :openMinute)) / term) / (60 / term);
		Integer	hour	=	openHour + (x / COLUMN_WIDTH + openMinute / term) / (60 / term);
//		Integer	minute	=	(((60 - openMinute) / term + x / COLUMN_WIDTH) % (60 / term)) * term;
		Integer	minute	=	((x / COLUMN_WIDTH + (openMinute / term)) % (60 / term)) * term;
		temp.set(temp.HOUR_OF_DAY, hour);
		temp.set(temp.MINUTE, minute);
		
		return	temp;
	}
	
	/**
	 * ���Ԃ��d������f�[�^�����݂��邩��Ԃ��B
	 * @param reservation �\��f�[�^
	 * @param isCheckOtherRow true - ���̍s���`�F�b�N����B
	 * @return true - ���Ԃ��d������f�[�^�����݂���
	 */
	private boolean isExistOverlapData(ReservationJTextField reservation,
			boolean isCheckOtherRow)
	{
		ReservationHeader	rh	=	reservation.getHeader();
		
		for(ReservationJTextField rtf : rh.getReservations())
		{
			if(!rtf.equals(reservation))
			{
				if(!isCheckOtherRow && rtf.getY() != reservation.getY())
				{
					continue;
				}
				
				if((rtf.getX() < reservation.getX() &&
							reservation.getX() < rtf.getX() + rtf.getWidth()) ||
						(rtf.getX() < reservation.getX() + reservation.getWidth() &&
							reservation.getX() < rtf.getX() + rtf.getWidth()))
				{
					return	true;
				}
			}
		}
		
		return	false;
	}
	
	/**
	 * ���ݎ����|�P���ԂɃX�N���[���o�[�����킹��B
	 * @param scrollPane ���߂���JScrollPane
	 */
	private void adjustScrollToCurrentTime(JScrollPane scrollPane, JTable table)
        {
                this.adjustScrollToCurrentTime(scrollPane, table, null, null);
        }
	private void adjustScrollToCurrentTime(JScrollPane scrollPane, JTable table, Integer hour, Integer minute)
	{
                if (hour == null) {
                    GregorianCalendar now = new GregorianCalendar();
                    hour = now.get(now.HOUR_OF_DAY) - 1;
                    minute = now.get(now.MINUTE) + 0;
                }

		Integer value	= COLUMN_WIDTH * ((hour - openHour) * 60 / term) + minute / term;
		
		if(value < 0 ||
				hour < openHour ||
				(hour == openHour && minute < openMinute) ||
				(24 <= closeHour && hour + 24 <= 36 &&
				((closeHour == hour + 24 && closeMinute < minute) ||
				closeHour < hour + 24)))
		{
			value = 0;
		}

		scrollPane.getHorizontalScrollBar().setValue(value);
		scrollPane.getHorizontalScrollBar().setValue(value);
	}

	/*
	 *
	 */
	private void loadWorkingInfo()
	{
		String strWorkOff_info;
		int nWorkOff_changed;
		String strTraining_info;
		int nTraining_changed;
		String strEvent_info;

		ConnectionWrapper con = SystemInfo.getConnection();
		String strSQL;
		String strText;
		ResultSetWrapper rsWorkInfo;

		
		try
		{

			rsWorkInfo = con.executeQuery(getSQLSelectWorkingInfo());

			if (rsWorkInfo.next())
			{
				nWorkOff_changed = rsWorkInfo.getInt("workoff_info_changed");
				strWorkOff_info = rsWorkInfo.getString("workoff_info");
				nTraining_changed = rsWorkInfo.getInt("trainy_info_changed");
				strTraining_info = rsWorkInfo.getString("trainy_info");
				strEvent_info = rsWorkInfo.getString("event_info");
			}
			else
			{
				nWorkOff_changed = 0;
				strWorkOff_info = "";
				nTraining_changed = 0;
				strTraining_info = "";
				strEvent_info = "";
			}

			rsWorkInfo.close();

		//-- ���x
			switch (nWorkOff_changed)
			{
			case 0:
				strText = composeWorkingStaffStr(con, eWORKINGTYPE.Off);
				break;

			case 1:
				strText = strWorkOff_info;
				break;

			case 2:
				strText = composeWorkingStaffStr(con, eWORKINGTYPE.Off);	
				strText += strWorkOff_info;

				break;

			default:
				strText = "";
				break;
			}

			txtWorkOff.setText(strText);

		//-- ���C
			switch (nTraining_changed)
			{
			case 0:
				strText = composeWorkingStaffStr(con, eWORKINGTYPE.Training);
				break;

			case 1:
				strText = strTraining_info;
				break;

			case 2:
				strText = composeWorkingStaffStr(con, eWORKINGTYPE.Training);	
				strText += strTraining_info;

				break;

			default:
				strText = "";
				break;
			}

			txtTraining.setText(strText);

		//-- ���x
			txtEvent.setText(strEvent_info);

		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

	}

	private String composeWorkingStaffStr(ConnectionWrapper con, eWORKINGTYPE eWorkingType) throws SQLException
	{
		ResultSetWrapper rsStaffData;
		String strText;

		strText = "";

		rsStaffData = con.executeQuery(getSQLSelectOffStaff(eWorkingType));
		while (rsStaffData.next())
		{
			strText  +=  rsStaffData.getString("staff_name1") + "(" + rsStaffData.getString("disp_name") + ")\n";
		}

		rsStaffData.close();

		return strText;
	}

	private void registerWorkingInfoRoutine(ConnectionWrapper con, eWORKINGTYPE eWorkingType, String strLostFocus)
	{
		String strWorkingStaff;

		try
		{
			strWorkingStaff = composeWorkingStaffStr(con, eWorkingType);

			if (strLostFocus.regionMatches(0, strWorkingStaff, 0, strWorkingStaff.length()))
			{
				// �S�̂Ƃ��Ă̕�����͕s��v�����Astaff DB �̓��e���� append �̂ݍs���Ă���̂� �ύX�R�[�h 2 �w��
				registerWorkingInfo(con, eWorkingType, 2, strLostFocus.substring(strWorkingStaff.length()));
			}
			else
			{
				// �ύX�R�[�h 1 �w��
				registerWorkingInfo(con, eWorkingType, 1, strLostFocus);
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	private void registerWorkingInfo(ConnectionWrapper con, eWORKINGTYPE eWorkingType, int nChgCode, String strRegister) throws SQLException
	{
		ResultSetWrapper rsWorkInfo;
		String strExecute;

		rsWorkInfo = con.executeQuery(getSQLSelectWorkingInfo());

		if (rsWorkInfo.next())
		{
			strExecute = getSQLUpdateWorkingInfo(eWorkingType, nChgCode, strRegister);
		}
		else
		{
			strExecute = getSQLInsertWorkingInfo(eWorkingType, nChgCode, strRegister);
		}

		rsWorkInfo.close();

		con.executeUpdate(strExecute);
	}


	/**
	 * �w��X�܂̊J�X�X���Ԃ��擾����
	 */
	private static String enclose(String str)
	{
		return "'" + str + "'";
	}

	private String getSQLSelectWorkingInfo()
	{
                StringBuilder sql = new StringBuilder(1000);
		sql.append(" select");
		sql.append(" 	workoff_info");
		sql.append(" ,	workoff_info_changed");
		sql.append(" ,	trainy_info");
		sql.append(" ,	trainy_info_changed");
		sql.append(" ,	event_info");
		sql.append(" from data_working_info");
		sql.append(" where");
		sql.append(" 	delete_date is null");
		sql.append(" and	to_char( working_date, 'yyyymmdd' ) = " + enclose(date.getDateStr()));
		sql.append(" and	shop_id = " + getSelectedShopID());

		return sql.toString();
	}

	/**
	 * �w��X�܂̊J�X�X���Ԃ��擾����
	 */
	private String getSQLSelectOffStaff(eWORKINGTYPE eWorkingType)
	{
                StringBuilder sql = new StringBuilder(1000);

		sql.append(" select");
		sql.append(" 	mst_staff.staff_name1");
		sql.append(" ,	mst_work_status.disp_name");
		sql.append(" from mst_staff");

		sql.append(" left join data_working_staff on");
		sql.append(" 	data_working_staff.delete_date is null");
		sql.append(" and	mst_staff.staff_id = data_working_staff.staff_id");

		sql.append(" left join mst_work_status on");
		sql.append(" 	data_working_staff.delete_date is null");
		sql.append(" and	mst_work_status.work_id = data_working_staff.work_id");


		sql.append(" where");
		sql.append(" 	mst_staff.delete_date is null");
		sql.append(" and	data_working_staff.shop_id = " + getSelectedShopID());
		sql.append(" and	to_char(data_working_staff.working_date, 'yyyymmdd') = " + enclose(date.getDateStr()));
		sql.append(" and	coalesce(mst_work_status.working, 0) = " + eWorkingType.toInt());

		return sql.toString();
	}

	private String getSQLInsertWorkingInfo(eWORKINGTYPE eWorkingType, int nChgCode, String strRegister) throws SQLException
	{
		String strSQL;

		strSQL = "";
		strSQL += " insert into data_working_info (";
		strSQL += " 	working_date";
		strSQL += " ,	shop_id";

		if (eWorkingType == eWORKINGTYPE.Off)
		{
			strSQL += " ,	workoff_info";
			strSQL += " ,	workoff_info_changed";
		}
		else if (eWorkingType == eWORKINGTYPE.Training)
		{
			strSQL += " ,	trainy_info";
			strSQL += " ,	trainy_info_changed";
		}
		else if (eWorkingType == eWORKINGTYPE.Event)
		{
			strSQL += " ,	event_info";
		}
		else
		{
			throw new SQLException("Not assumed eWORKINGTYPE");
		}

		strSQL += " ,	insert_date";
		strSQL += " ,	update_date";
		strSQL += " )";
		strSQL += " values (";
		strSQL += " 	" + enclose(date.getDateStr());
		strSQL += " ,	" + getSelectedShopID();

		if (eWorkingType == eWORKINGTYPE.Off)
		{
			strSQL += " ,	" + enclose(strRegister);
			strSQL += " ,	" + nChgCode;
		}
		else if (eWorkingType == eWORKINGTYPE.Training)
		{
			strSQL += " ,	" + enclose(strRegister);
			strSQL += " ,	" + nChgCode;
		}
		else if (eWorkingType == eWORKINGTYPE.Event)
		{
			strSQL += " ,	" + enclose(strRegister);
		}
		else
		{
			throw new SQLException("Not assumed eWORKINGTYPE");
		}

		strSQL += " ,	now()";
		strSQL += " ,	now()";
		strSQL += " )";

		return strSQL;
	}

	private String getSQLUpdateWorkingInfo(eWORKINGTYPE eWorkingType, int nChgCode, String strRegister) throws SQLException
	{
		String strSQL;

		strSQL = "";
		strSQL += " update data_working_info set";

		if (eWorkingType == eWORKINGTYPE.Off)
		{
			strSQL += " 	workoff_info = " + enclose(strRegister);
			strSQL += " ,	workoff_info_changed = " + nChgCode;
		}
		else if (eWorkingType == eWORKINGTYPE.Training)
		{
			strSQL += " 	trainy_info = " + enclose(strRegister);
			strSQL += " ,	trainy_info_changed = " + nChgCode;
		}
		else if (eWorkingType == eWORKINGTYPE.Event)
		{
			strSQL += " 	event_info = " + enclose(strRegister);
		}
		else
		{
			throw new SQLException("Not assumed eWORKINGTYPE");
		}


		strSQL += " ,	update_date = now()";

		strSQL += " where";
		strSQL += " 	delete_date is null";
		strSQL += " and	to_char( working_date, 'yyyymmdd' ) = " + enclose(date.getDateStr());
		strSQL += " and	shop_id = " + getSelectedShopID();



		
		return strSQL;
	}
        
        private void setBedStaffPanelVisible() {

            boolean mode = true;
            double offset = bedPanel.getPreferredSize().getHeight() + 6;
            
            // �{�p��̎g�p�L���ŗv��Ȃ��������
            MstShop ms = this.getSelectedShop();
            if (ms != null && !ms.isBed()) {
                mode = false;
                offset *= -1;
            }
            
            // ���݂̕\����ԂƈقȂ�ꍇ�̂݁i�r���I�_���a�Ŕ���j
            if (mode ^ bedPanel.isVisible()) {
                bedPanel.setVisible(mode);

                System.out.println(staffPanel.getPreferredSize().getHeight());

                staffPanel.setPreferredSize(new Dimension((int)(staffPanel.getPreferredSize().getWidth()), (int)(staffPanel.getPreferredSize().getHeight() - offset)));
                staffScrollPane.setPreferredSize(new Dimension((int)(staffScrollPane.getPreferredSize().getWidth()), (int)(staffScrollPane.getPreferredSize().getHeight() - offset)));
                staffNameScrollPane.setPreferredSize(new Dimension((int)(staffNameScrollPane.getPreferredSize().getWidth()), (int)(staffNameScrollPane.getPreferredSize().getHeight() - offset)));

                System.out.println(staffPanel.getPreferredSize().getHeight());
            }
        }

        public boolean isNextReservation() {
            return nextReservation;
        }

        public void setNextReservation(boolean nextReservation) {
            this.nextReservation = nextReservation;
        }

        public boolean isAutoOpenDialog() {
            return autoOpenDialog;
        }

        public void setAutoOpenDialog(boolean autoOpenDialog) {
            this.autoOpenDialog = autoOpenDialog;
        }

        public Integer getNextReserveShopID() {
            return nextReserveShopID;
        }

        public void setNextReserveShopID(Integer nextReserveShopID) {
            this.nextReserveShopID = nextReserveShopID;
        }

        public Integer getNextReserveNo() {
            return nextReserveNo;
        }

        public void setNextReserveNo(Integer nextReserveNo) {
            this.nextReserveNo = nextReserveNo;
        }
        
        private void printReservationList() {

            JExcelApi jx = new JExcelApi("�\��\");
            jx.setTemplateFile("/reports/�\��\.xls");
            
            // �w�b�_
            HeaderFooter header = jx.getTargetSheet().getSettings().getHeader();
            header.getCentre().clear();
            header.getCentre().setFontSize(14);
            header.getCentre().toggleBold();
            SimpleDateFormat format = new SimpleDateFormat("yyyy�NM��d��");
            header.getCentre().append(format.format(date.getDate()) + "�@�\��\");

            final int MAX_STAFF = 80;
            final int MAX_TIME = 24;

            // �X�^�b�t��
            int offsetCol = ts.getStaffs().size();
            for (int i = 0; i < (MAX_STAFF - offsetCol); i++) {
                jx.removeColumn(2 + (offsetCol * 3));
                jx.removeColumn(2 + (offsetCol * 3));
                jx.removeColumn(2 + (offsetCol * 3));
            }

            // ���Ԏ�
            MstShop ms = SystemInfo.getCurrentShop();
            int openHour = ms.getOpenHour();
            int closeHour = ms.getCloseHour() - (ms.getCloseMinute() > 0 ? 0 : 1);            
            for (int i = 0; i < (MAX_TIME - (closeHour - openHour + 1)); i++) {
                jx.removeRow(3);
                jx.removeRow(3);
                jx.removeRow(3);
                jx.removeRow(3);
            }
            
            SortedMap<Integer, ArrayList<Integer>> map = new TreeMap<Integer, ArrayList<Integer>>(new ExmComparator<Integer>());
            for (int i = 0; i < (closeHour - openHour + 1); i++) {
                jx.setValue(1, 3 + (i * 4), openHour + i);
                
                ArrayList<Integer> list = new ArrayList<Integer>(); 
                map.put(openHour + i, list);
            }
            
            // �P���ԒP�ʂ̗\�񐔂��`�F�b�N
            for (ReservationHeader rh : ts.getStaffs()) {

                for (int i = 0; i < (closeHour - openHour + 1); i++) {
                    ArrayList<Integer> list = map.get(openHour + i);
                    list.add(0);
                }
                
                for (ReservationJTextField rtf: rh.getReservations()) {
                    // �\�񂪑��݂��Ȃ��ꍇ
                    if (rtf.getReservation().getReservationNo() == 0) continue;
                    
                    int reservationHour = rtf.getHour(currentDate.getTime());
                    ArrayList<Integer> list = map.get(reservationHour);
                    list.set(list.size() - 1, ((Integer)list.get(list.size() - 1)) + 1);
                }
            }
            // �e���v���[�g�̍ő�s���𒴂����ꍇ�͍s�ǉ�
            for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry)it.next();
                Integer key = (Integer)entry.getKey();
                ArrayList value = (ArrayList)entry.getValue();
                
                int maxRow = 0;
                for (Object rows : value) {
                    if (maxRow < (Integer)rows) {
                        maxRow = (Integer)rows;
                    }
                }

                if (maxRow > 4) {
                    int row = 3;
                    while (!jx.getValue(1, row).equals(key.toString())) {
                        row++;
                    }
                    row++;
                    jx.insertRow(row, maxRow - 4);
                }
            }        
            
            offsetCol = 0;
            
            for (ReservationHeader rh : ts.getStaffs()) {
                
                int col = 2 + (offsetCol * 3);
                
                // �X�^�b�t���o��
                jx.setValue(col, 1, rh.getName());
                
                // ���ԏ��Ƀ\�[�g
                Collections.sort(rh.getReservations(), new Comparator<ReservationJTextField>() {
                    public int compare(ReservationJTextField r1, ReservationJTextField r2) {
                        int t1 = (r1.getHour(currentDate.getTime()) * 100) + r1.getMinute();
                        int t2 = (r2.getHour(currentDate.getTime()) * 100) + r2.getMinute();
                        return t1 - t2;
                    }		
                });		

                // �\����o��
                for (ReservationJTextField rtf: rh.getReservations()) {
                    // �\�񂪑��݂��Ȃ��ꍇ
                    if (rtf.getReservation().getReservationNo() == 0) continue;
                    
                    int reservationHour = rtf.getHour(currentDate.getTime());
                    
                    if (openHour <= reservationHour && reservationHour <= closeHour) {
                        int row = 3;
                        while (!jx.getValue(1, row).equals(String.valueOf(reservationHour))) {
                            row++;
                        }
                        while (jx.getValue(col, row).length() > 0) {
                            row++;
                        }
                        jx.setValue(col, row, rtf.getMinute());
                        jx.setValue(col + 1, row, (rtf.getReservation().getDesignated() ? "[�w]" : "[F]") + rtf.getReservation().getCustomer().getCustomerName(0));
                        jx.setValue(col + 2, row, rtf.getReservation().getTechnicClassContractedName());
                    }
                }
                
                offsetCol++;
            }
            
            jx.openWorkbook();
        }
        
        private class ExmComparator<T> implements Comparator<T> {
            public int compare(Object obj1, Object obj2) {
                return ((Integer)obj1).compareTo((Integer)obj2) * -1;
            }
        }     


	/**
	 * ��ʂ��������
	 * @param g �`��N���X
	 * @param pf �y�[�W�t�H�[�}�b�g
	 * @param pi �y�[�W�C���f�b�N�X
	 * @return int 1�FNO_SUCH_PAGE 0�FPAGE_EXISTS
	 */
        public int print( Graphics g, PageFormat pf, int pi ) throws PrinterException {

            final double SCALE = 0.8;   /** �n�[�h�R�s�[�̔{�� */

            if ( pi >= 1 ) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2 = (Graphics2D)g;

            //���E��̃}�[�W���������炵�ĕ`�悷��
            g2.translate( (int)pf.getImageableX(), (int)pf.getImageableY() );
            g2.scale(1, 0.85);

            this.printAll(g2);
            return Printable.PAGE_EXISTS;
        }

}
