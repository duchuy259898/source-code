/*
 * TimeTableDrawer.java
 *
 * Created on 2009/05/07, 12:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.basicinfo.company.DataRecess;
import java.awt.Color;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.geobeck.sosia.pos.master.company.MstShop;

/**
 *
 * @author takeda
 */
public class TimeTableDrawer {
    
    public static final Color	COLOR_HEADER		=	new Color(0xffffff);	/** �w�b�_�̐F  */
    public static final Color	COLOR_RESERVATION	=	new Color(0xffff00);	/** �\��̐F */
    public static final Color	COLOR_STAY			=	new Color(0xccff66);	/** �ݓX�̐F */
    public static final Color	COLOR_FINISH		=	new Color(0xc4e1ff);	/** �ޓX�̐F */
    public static final Color	COLOR_MOBILE		=	new Color(0xff9999);    /** ���o�C���\��̐F */
    
    public static final Color	COLOR_OFFTIME		=	new Color(180,180,180);    /** �Ζ��O�F */

    public static final Color	COLOR_RECESSTIME	=	new Color(111,0,111);    /** �x�e�F */

    public static final Color	COLOR_BORDER		=	new Color(0xC0C0C0);    /** �r���J���[ */
    
    private static Color backColor = new Color(255,255,255);
    
    /**
     * JTextField�̐F���擾����B
     * @param status ���
     * @return JTextField�̐F
     */
    public static Color getColor(int status) {
        switch(status) {
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
    
    /*
     * �V�����s���쐬����
     */
    public static void makeNewRow(MstShop shop, TimeSchedule ts, ReservationHeader rh, JTable table){
    
        // �Ζ�����z����擾����
        Vector openTime = null;
        Vector<Integer> timeTable = null;

        if (rh != null) openTime = rh.getOpenTime();
        if (ts != null) timeTable = ts.getTimeTable();

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        Vector<Object> v = new Vector<Object>();
        
        for (int i = 0; i < model.getColumnCount(); i++) {
            JLabel label = new JLabel();
            label.setOpaque(true);

            if ( openTime != null && openTime.get(i) != null && (openTime.get(i) instanceof Boolean) && (Boolean)openTime.get(i) ) {

                label.setBackground(rh.getBackground());

            } else {

                // �X�^�b�t�V�t�g�̔��f�L��
                if (shop.getReservationStaffShift() == 1) {

                    if (openTime.get(i) instanceof DataRecess) {
                        label.setBackground(COLOR_RECESSTIME);
                        //label.setForeground(Color.WHITE);
                        //label.setText(" �x");
                        label.setToolTipText("<html><strong>�x�e�R�����g</strong><br>" + ReservationStatusPanel.formatHTMLString(((DataRecess)openTime.get(i)).getNote()) + "</html>");
                    } else {
                        label.setBackground(COLOR_OFFTIME);
                    }

                } else {
                    if (rh != null) backColor =  rh.getBackground();
                    label.setBackground(backColor);
                }

            }
            label.setBorder(null);
            v.add(label);
        }
        model.addRow(v);    
    }
    
    /*
     * �ǉ��s���쐬����
     */
    public static void makeAddRow(MstShop shop, TimeSchedule ts, ReservationHeader rh, JTable table){

        // �Ζ�����z����擾����
        Vector<Boolean> openTime = rh.getOpenTime();
        Vector<Integer> timeTable = ts.getTimeTable();
        
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        Vector<Object> v = new Vector<Object>();
        
        for (int i = 0; i < model.getColumnCount(); i++) {
            JLabel label = new JLabel();
            if ( openTime.get(i) ) {
                label.setBackground(Color.WHITE);
                label.setOpaque(false);
            } else {
                label.setBackground(COLOR_OFFTIME);
                // �X�^�b�t�V�t�g�̔��f�L��
                if (shop.getReservationStaffShift() == 1) {
                    label.setOpaque(true);
                } else {
                    label.setOpaque(false);
                }                
            }
            label.setBorder(null);
            v.add(label);
        }
        model.addRow(v);
    }
    
    /**
     * ���Ԃ��d������f�[�^�����݂��邩��Ԃ��B
     * @param reservation �\��f�[�^
     * @param isCheckOtherRow true - ���̍s���`�F�b�N����B
     * @return true - ���Ԃ��d������f�[�^�����݂���
     */
    public static boolean isExistOverlapData(ReservationJTextField reservation,
            boolean isCheckOtherRow) {
        ReservationHeader	rh	=	reservation.getHeader();
        
        for(ReservationJTextField rtf : rh.getReservations()) {
            if(!rtf.equals(reservation)) {
                if(!isCheckOtherRow && rtf.getY() != reservation.getY()) {
                    continue;
                }
                
                if((rtf.getX() < reservation.getX() &&
                        reservation.getX() < rtf.getX() + rtf.getWidth()) ||
                        (rtf.getX() < reservation.getX() + reservation.getWidth() &&
                        reservation.getX() < rtf.getX() + rtf.getWidth())) {
                    return	true;
                }
            }
        }
        
        return	false;
    }    
    
}
