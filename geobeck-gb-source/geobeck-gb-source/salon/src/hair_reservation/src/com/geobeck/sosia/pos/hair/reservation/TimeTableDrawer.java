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
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author takeda
 */
public class TimeTableDrawer {
    
    public static final Color	COLOR_HEADER		=	new Color(0xffffff);	/** ヘッダの色  */
    public static final Color	COLOR_RESERVATION	=	new Color(0xffff00);	/** 予約の色 */
    public static final Color	COLOR_STAY			=	new Color(0xccff66);	/** 在店の色 */
    public static final Color	COLOR_FINISH		=	new Color(0xc4e1ff);	/** 退店の色 */
    public static final Color	COLOR_MOBILE		=	new Color(0xff9999);    /** モバイル予約の色 */
    
    public static final Color	COLOR_OFFTIME		=	new Color(180,180,180);    /** 勤務外色 */

    public static final Color	COLOR_RECESSTIME	=	new Color(111,0,111);    /** 休憩色 */

    public static final Color	COLOR_BORDER		=	new Color(0xC0C0C0);    /** 罫線カラー */
    
    private static Color backColor = new Color(255,255,255);
    
    /**
     * JTextFieldの色を取得する。
     * @param status 状態
     * @return JTextFieldの色
     */
    public static Color getColor(int status) {
        switch(status) {
            //予約
            case 1:
                return	COLOR_RESERVATION;
                //在店
            case 2:
                return	COLOR_STAY;
                //退店
            case 3:
                return	COLOR_FINISH;
            default:
                //return	COLOR_MOBILE;
                return	new Color(0x000000);
        }
    }
    
    /*
     * 新しい行を作成する
     */
    public static void makeNewRow(MstShop shop, TimeSchedule ts, ReservationHeader rh, JTable table){
    
        // 勤務判定配列を取得する
        Vector openTime = null;
        Vector<Integer> timeTable = null;

        if (rh != null) openTime = rh.getOpenTime();
        if (ts != null) timeTable = ts.getTimeTable();

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        Vector<Object> v = new Vector<Object>();
        
        for (int i = 0; i < model.getColumnCount(); i++) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            if(i<openTime.size()){
                if ( openTime != null && openTime.get(i) != null && (openTime.get(i) instanceof Boolean) && (Boolean)openTime.get(i) ) {

                    label.setBackground(rh.getBackground());

                } else {

                    // スタッフシフトの反映有無
                    if (shop.getReservationStaffShift() == 1) {

                        if (openTime.get(i) instanceof DataRecess) {
                            label.setBackground(COLOR_RECESSTIME);
                            //LVTu start edit 2016/11/08 New request #58395
                            DataRecess dr = (DataRecess)openTime.get(i);
                            label.setName(dr.getStaffId() + ":" +dr.getRecessId());
                            //label.setForeground(Color.WHITE);
                            //label.setText(" 休");
                            // 201712 GB edit start #33784 [GB内対応][gb]休憩帯のツールチップに休憩の開始・終了時間を表示させる
                            //label.setToolTipText("<html><strong>休憩コメント</strong><br>" + ReservationStatusPanel.formatHTMLString(((DataRecess)openTime.get(i)).getNote()) + "</html>");
                            StringBuilder tmpSb = new StringBuilder(dr.getStartTime());
                            tmpSb.insert(2, ":");
                            String startTime = tmpSb.toString(); // 休憩開始時間
                            tmpSb.setLength(0);
                            tmpSb.append(dr.getEndTime());
                            tmpSb.insert(2, ":");
                            String endTime = tmpSb.toString(); // 休憩終了時間
                            String toolTipText = "<html><strong>休憩コメント</strong><br>" + ReservationStatusPanel.formatHTMLString(((DataRecess)openTime.get(i)).getNote());
                            toolTipText += "<br><strong>休憩時間</strong><br>" + startTime + "～" + endTime + "</html>";
                            label.setToolTipText(toolTipText);
                            // 201712 GB add edit #33784 [GB内対応][gb]休憩帯のツールチップに休憩の開始・終了時間を表示させる

                        } else {
                            label.setBackground(COLOR_OFFTIME);
                        }

                    } else {
                        if (rh != null) backColor =  rh.getBackground();
                        label.setBackground(backColor);
                    }

                }
            }else{
                label.setBackground(rh.getBackground());
            }
            //label.setBorder(null);
            //LVTu end edit 2016/11/08 New request #58395
            v.add(label);
        }
        model.addRow(v);    
    }
    
    /*
     * 追加行を作成する
     */
    public static void makeAddRow(MstShop shop, TimeSchedule ts, ReservationHeader rh, JTable table){

        // 勤務判定配列を取得する
        Vector<Boolean> openTime = rh.getOpenTime();
        Vector<Integer> timeTable = ts.getTimeTable();
        
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        Vector<Object> v = new Vector<Object>();
        
        for (int i = 0; i < model.getColumnCount(); i++) {
            JLabel label = new JLabel();
            //LVTu start edit 2015/03/17 Bug #35622
            //if ( openTime.get(i) ) {
            if ( openTime != null && openTime.get(i) != null && (openTime.get(i) instanceof Boolean) && (Boolean)openTime.get(i) ) {
            //LVTu end edit 2015/03/17 Bug #35622
                label.setBackground(Color.WHITE);
                label.setOpaque(false);
            } else {
                label.setBackground(COLOR_OFFTIME);
                // スタッフシフトの反映有無
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
     * 時間が重複するデータが存在するかを返す。
     * @param reservation 予約データ
     * @param isCheckOtherRow true - 他の行もチェックする。
     * @return true - 時間が重複するデータが存在する
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
