/*
 * SearchCustomerDialog.java
 *
 * Created on 2006/04/20, 14:13
 */

package com.geobeck.sosia.pos.hair.account;

import com.geobeck.sosia.pos.system.SystemInfo;
import javax.swing.*;
import javax.swing.text.*;

import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.MessageUtil;

/**
 * 顧客検索画面
 * @author katagiri
 */
public class DiscountSettingDialog extends javax.swing.JDialog
{
        //割引対象
        public	static	final	int	DISCOUNT_DIVISION_ALL     = 0;
	public	static	final	int	DISCOUNT_DIVISION_TECHNIC = 1;
	public	static	final	int	DISCOUNT_DIVISION_PRODUCT = 2;
        // vtbphuong start add 20150511
        public	static	final	int	DISCOUNT_DIVISION_COURSE = 5;
        // vtbphuong end add 20150511
        //割引方法
	public	static	final	int	DISCOUNT_METHOD_VALUE = 1;
	public	static	final	int	DISCOUNT_METHOD_RATE  = 2;

        private boolean OK = false;
        
	/**
	 * コンストラクタ
	 * @param parent 
	 * @param modal 
	 */
	public DiscountSettingDialog(java.awt.Frame parent, boolean modal)
	{       
            super(parent, modal);
            init();
	}
    
        // 共通初期化処理
        private void init() {
            initComponents();
            addMouseCursorChange();
            this.setListener();
            SwingUtil.moveCenter(this);
            this.setDiscountMethodEnabled();
        }
        
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        monthGroup = new javax.swing.ButtonGroup();
        divisionGroup = new javax.swing.ButtonGroup();
        backPanel = new com.geobeck.swing.ImagePanel();
        discountMethodLabel1 = new javax.swing.JLabel();
        discountMethodLabel3 = new javax.swing.JLabel();
        discountMethodLabel2 = new javax.swing.JLabel();
        settingButton = new javax.swing.JButton();
        percentLabel = new javax.swing.JLabel();
        yenLabel = new javax.swing.JLabel();
        divisionTechnic = new javax.swing.JRadioButton();
        divisionAll = new javax.swing.JRadioButton();
        discountRate = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)discountRate.getDocument()).setDocumentFilter(
            new CustomFilter(2, CustomFilter.NUMBER));
        discountDivisionLabel = new javax.swing.JLabel();
        methodRate = new javax.swing.JRadioButton();
        methodValue = new javax.swing.JRadioButton();
        discountValue = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)discountValue.getDocument()).setDocumentFilter(
            new CustomFilter(9, CustomFilter.NUMERIC));
        divisionProduct = new javax.swing.JRadioButton();
        discountMethodLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        divisionCourse = new javax.swing.JRadioButton();

        setTitle("楽々割引");
        setName("searchCustomerFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        backPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        backPanel.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));
        backPanel.setRepeat(true);

        discountMethodLabel1.setText("金額");

        discountMethodLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        discountMethodLabel3.setText("※ 入力した値が全ての明細に反映します。");

        discountMethodLabel2.setText("パーセント");

        settingButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        settingButton.setBorderPainted(false);
        settingButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
        settingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingButtonActionPerformed(evt);
            }
        });

        percentLabel.setText("％");

        yenLabel.setText("円");

        divisionGroup.add(divisionTechnic);
        divisionTechnic.setSelected(true);
        divisionTechnic.setText("技術");
        divisionTechnic.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        divisionTechnic.setMargin(new java.awt.Insets(0, 0, 0, 0));
        divisionTechnic.setOpaque(false);

        divisionGroup.add(divisionAll);
        divisionAll.setText("すべて");
        divisionAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        divisionAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        divisionAll.setOpaque(false);

        discountRate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        discountRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        discountDivisionLabel.setText("割引対象");

        monthGroup.add(methodRate);
        methodRate.setText("パーセント");
        methodRate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        methodRate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        methodRate.setOpaque(false);
        methodRate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                methodRateStateChanged(evt);
            }
        });

        monthGroup.add(methodValue);
        methodValue.setSelected(true);
        methodValue.setText("金額");
        methodValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        methodValue.setMargin(new java.awt.Insets(0, 0, 0, 0));
        methodValue.setOpaque(false);
        methodValue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                methodValueStateChanged(evt);
            }
        });

        discountValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        discountValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        divisionGroup.add(divisionProduct);
        divisionProduct.setText("商品");
        divisionProduct.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        divisionProduct.setMargin(new java.awt.Insets(0, 0, 0, 0));
        divisionProduct.setOpaque(false);

        discountMethodLabel.setText("割引方法");

        jLabel1.setText("（１〜９９）");

        divisionGroup.add(divisionCourse);
        divisionCourse.setText("コース契約");
        divisionCourse.setOpaque(false);
        divisionCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                divisionCourseActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout backPanelLayout = new org.jdesktop.layout.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(backPanelLayout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(discountMethodLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(backPanelLayout.createSequentialGroup()
                                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(backPanelLayout.createSequentialGroup()
                                        .add(discountMethodLabel)
                                        .add(26, 26, 26))
                                    .add(backPanelLayout.createSequentialGroup()
                                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(discountMethodLabel1)
                                            .add(discountMethodLabel2))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)))
                                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(backPanelLayout.createSequentialGroup()
                                        .add(discountRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(percentLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel1))
                                    .add(backPanelLayout.createSequentialGroup()
                                        .add(methodValue)
                                        .add(18, 18, 18)
                                        .add(methodRate))
                                    .add(backPanelLayout.createSequentialGroup()
                                        .add(discountValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(yenLabel)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(backPanelLayout.createSequentialGroup()
                    .add(discountDivisionLabel)
                        .add(26, 26, 26)
                    .add(divisionTechnic)
                        .add(18, 18, 18)
                    .add(divisionProduct)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(divisionCourse)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(divisionAll)
                        .add(0, 38, Short.MAX_VALUE)))
                .addContainerGap())
            .add(backPanelLayout.createSequentialGroup()
                .add(134, 134, 134)
                .add(settingButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .add(16, 16, 16)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(discountDivisionLabel)
                    .add(divisionTechnic)
                    .add(divisionProduct)
                    .add(divisionAll)
                    .add(divisionCourse))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(discountMethodLabel)
                    .add(methodValue)
                    .add(methodRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(15, 15, 15)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(discountValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(yenLabel)
                    .add(discountMethodLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(discountRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(percentLabel)
                    .add(discountMethodLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(discountMethodLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(settingButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    }//GEN-LAST:event_formWindowOpened

    private void settingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingButtonActionPerformed

        //入力チェック
        if(this.checkInput())
        {
            //画面を閉じる
            this.OK = true;
            this.setVisible(false);
        }
}//GEN-LAST:event_settingButtonActionPerformed

    private void methodRateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_methodRateStateChanged
            this.setDiscountMethodEnabled();
}//GEN-LAST:event_methodRateStateChanged

    private void methodValueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_methodValueStateChanged
            this.setDiscountMethodEnabled();
}//GEN-LAST:event_methodValueStateChanged

    private void divisionCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_divisionCourseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_divisionCourseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JLabel discountDivisionLabel;
    private javax.swing.JLabel discountMethodLabel;
    private javax.swing.JLabel discountMethodLabel1;
    private javax.swing.JLabel discountMethodLabel2;
    private javax.swing.JLabel discountMethodLabel3;
    private com.geobeck.swing.JFormattedTextFieldEx discountRate;
    private com.geobeck.swing.JFormattedTextFieldEx discountValue;
    private javax.swing.JRadioButton divisionAll;
    private javax.swing.JRadioButton divisionCourse;
    private javax.swing.ButtonGroup divisionGroup;
    private javax.swing.JRadioButton divisionProduct;
    private javax.swing.JRadioButton divisionTechnic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton methodRate;
    private javax.swing.JRadioButton methodValue;
    private javax.swing.ButtonGroup monthGroup;
    private javax.swing.JLabel percentLabel;
    private javax.swing.JButton settingButton;
    private javax.swing.JLabel yenLabel;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{  
            SystemInfo.addMouseCursorChange(settingButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{
            divisionAll.addKeyListener(SystemInfo.getMoveNextField());
            divisionTechnic.addKeyListener(SystemInfo.getMoveNextField());
            divisionProduct.addKeyListener(SystemInfo.getMoveNextField());
            methodValue.addKeyListener(SystemInfo.getMoveNextField());
            discountValue.addKeyListener(SystemInfo.getMoveNextField());
            discountValue.addFocusListener(SystemInfo.getSelectText());
            methodRate.addKeyListener(SystemInfo.getMoveNextField());
            discountRate.addKeyListener(SystemInfo.getMoveNextField());
            discountRate.addFocusListener(SystemInfo.getSelectText());
            divisionCourse.addKeyListener(SystemInfo.getMoveNextField());
	}

	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setDiscountMethodEnabled()
	{
            this.discountValue.setEnabled(this.methodValue.isSelected());
            this.discountRate.setEnabled(this.methodRate.isSelected());
            if (this.methodValue.isSelected()) {
                this.discountRate.setText("");
                this.discountValue.requestFocusInWindow();
            } else {
                this.discountValue.setText("");
                this.discountRate.requestFocusInWindow();
            }
	}

        /**
	 * 入力チェックを行う。
	 * @return 入力エラーがなければtrueを返す。
	 */
	private boolean checkInput()
	{
            //金額
            if(!discountValue.getText().equals("") && !CheckUtil.isNumber(discountValue.getText()))
            {
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "金額"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                discountValue.requestFocusInWindow();
                return false;
            }

            //パーセント
            if(!discountRate.getText().equals("") && !CheckUtil.isNumber(discountRate.getText()))
            {
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "パーセント"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                discountRate.requestFocusInWindow();
                return false;
            }

            return true;
	}

	/**
	 * 割引対象を取得する。
	 * @return 割引区分
	 */
	public Integer getDiscountDivision()
	{
		if(this.divisionAll.isSelected())
				return	DISCOUNT_DIVISION_ALL;
		else if(this.divisionTechnic.isSelected())
				return	DISCOUNT_DIVISION_TECHNIC;
		else if(this.divisionProduct.isSelected())
				return	DISCOUNT_DIVISION_PRODUCT;
                else if(this.divisionCourse.isSelected())
				return	DISCOUNT_DIVISION_COURSE;
		else	return	null;
	}

	/**
	 * 割引方法を取得する。
	 * @return 割引方法
	 */
	public Integer getDiscountMethod()
	{
		if(this.methodValue.isSelected())
				return	DISCOUNT_METHOD_VALUE;
		else if(this.methodRate.isSelected())
				return	DISCOUNT_METHOD_RATE;
		else	return	null;
	}

        public Integer getDiscountValue()
        {
            Integer result = 0;
            try {
                result = Integer.parseInt(discountValue.getText());
            } catch (Exception e) {
            }
            return result;
        }

        public Integer getDiscountRate()
        {
            Integer result = 0;
            try {
                result = Integer.parseInt(discountRate.getText());
            } catch (Exception e) {
            }
            return result;
        }

        /**
         * @return the OK
         */
        public boolean isOK() {
            return OK;
        }

}
