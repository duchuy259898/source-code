 /* RegistShopEmployeePanel.java
 *
 * Created on 2016/08/18, 10:44
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.master.company.MstShift;
import com.geobeck.sosia.pos.master.company.MstShifts;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.SQLUtil;
import com.google.gson.Gson;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.collections.map.LinkedMap;

/**
 *
 * @author  ptquang
 */
public class RegistShopEmployeePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    private     MstShop         shop;
    private     MstShifts       shifts;
    private     Date            scheduleDate;
    private     DataSchedules   scheds;
    private     DataSchedules   schedsTemp;
    //IVS_PTQUANG start add 2016/10/25 New request #57851 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_ヘルプと休憩の重複不可対応
    private final String CONSTANT_MESSAGE_ERROR = "ヘルプ時間と休憩時間を重複して\n登録することはできません。";
    private final String CONSTANT_MESSAGE_ERROR_EXTERNAL = "ヘルプ時間がシフト時間外です";
    private final String CONSTANT_TITLE_ERROR = "ヘルプ登録";
    //IVS_PTQUANG end add 2016/10/25 New request #57851 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_ヘルプと休憩の重複不可対応
    //IVS_PTQUANG start add 2016/09/05 New request #54490
    //IVS_LVTu start add 2016/12/08 New request #58864
    private final String CONSTANT_MESSAGE_ERROR_SHIFT_SHOP = "ヘルプ時間が営業時間外です";
    //IVS_LVTu end add 2016/12/08 New request #58864
    protected MstAPI mstApi = new MstAPI();
    private  Integer scheduleDetailNo = null;
    //DefaultTableModel model;
    static public boolean ShowDialog(Frame owner, MstShop shop, Date targetDate ) {

        SystemInfo.getLogger().log(Level.INFO, "ヘルプ登録");
        
        RegistShopEmployeePanel dlg = new RegistShopEmployeePanel();
        dlg.ShowCloseBtn();
        dlg.shop = shop;
        dlg.scheduleDate = targetDate;
        dlg.init();
        
        SwingUtil.openAnchorDialog(owner, true, dlg, "ヘルプ登録", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);
        dlg.dispose();
        return true;
    }
    
    /*
     * ダイアログとして表示された場合は、閉じるボタンを有効化
     */
    private void ShowCloseBtn() {
        btnClose.setVisible(true);
    }
    
    /*
     * 後始末
     */
    private void dispose() {
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).dispose();
        }
    }
    
    /** Creates new form RegistShopEmployeePanel */
    private RegistShopEmployeePanel() {
        initComponents();
        this.setTitle("ヘルプ登録");
        int nBasePanelWidth = new ShiftPanelBase().getPreferredSize().width;
        this.setSize(80 + nBasePanelWidth * 3 + 100, 600);
    }
    
    private void init() {
        this.initTableColumnWidth();
        addMouseCursorChange();
        this.loadData();
        initData();
        //IVS_PTQUANG start New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
        Integer shopID = this.shop.getShopID();
        SystemInfo.getMstUser().setShopID(shopID);
        //IVS_PTQUANG end New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
    }
    
    private void initData() {
        DefaultComboBoxModel fromModel = (DefaultComboBoxModel)fromHour.getModel();
        DefaultComboBoxModel toModel = (DefaultComboBoxModel)toHour.getModel();
        fromModel.addElement(new String());
        toModel.addElement(new String());
        for( int ii = 0; ii < 37; ii++ ){
            String str = String.format( "%02d", ii );
            fromModel.addElement(str);
            toModel.addElement(str);
        }
        
        fromModel = (DefaultComboBoxModel)fromMinute.getModel();
        toModel = (DefaultComboBoxModel)toMinute.getModel();
        fromModel.addElement(new String());
        toModel.addElement(new String());
        for( int ii = 0; ii < 60; ii = ii + this.shop.getTerm() ){
            String str = String.format( "%02d", ii );
            fromModel.addElement(str);
            toModel.addElement(str);
        }  
        
        DefaultComboBoxModel travel1 = (DefaultComboBoxModel)cmbTravel1.getModel();
        DefaultComboBoxModel travel2 = (DefaultComboBoxModel)cmbTravel2.getModel();
        travel1.addElement("");
       
        for( int ii = 0; ii <= 300; ii = ii + this.shop.getTerm() ){
            String str = String.format( "%02d", ii );
            travel1.addElement(str);
        }
       
        travel2.addElement("");
        for( int ii = 0; ii <= 300; ii = ii + this.shop.getTerm() ){
            String str = String.format( "%02d", ii );
            travel2.addElement(str);
        }
        
        initShopName();
        loadStaff();
        renewButton.setEnabled(false);
	deleteButton.setEnabled(false);
    }
    
    public void initShopName() {
        if (SystemInfo.getCurrentShop().getShopID() == 0) {
            SystemInfo.initGroupShopComponents(cmbShop, 2);
        } else {
            for (int i = 0; i < SystemInfo.getGroup().getShops().size(); i++) {
                cmbShop.addItem(SystemInfo.getGroup().getShops().get(i));
            }
            cmbShop.setSelectedItem(SystemInfo.getCurrentShop());
        }
        // remove shop current
        for ( int i = 0;i < cmbShop.getItemCount(); i ++) {
            if(((MstShop)cmbShop.getItemAt(i)).getShopID().equals(this.shop.getShopID())) {
                cmbShop.removeItemAt(i);
            }
        }
    }
    
    private void loadStaff() {
        MstStaffs staffs = new MstStaffs();
        staffs.setShopIDList(this.shop.getShopID().toString());
        try{
            staffs.loadOnlyShop(SystemInfo.getConnection(), false);
        }catch(SQLException e){
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        cmbStaff.addItem(new MstStaff());
        for ( MstStaff ms : staffs) {
            cmbStaff.addItem(ms);
        }
        if(cmbStaff.getItemCount() > 0) {
            cmbStaff.setSelectedIndex(0);
        }   
    }
    
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnNewRow);
        SystemInfo.addMouseCursorChange(btnClose);
        SystemInfo.addMouseCursorChange(renewButton);
        SystemInfo.addMouseCursorChange(deleteButton);
    }
	
    private void initTableColumnWidth()
    {
        
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        scheduleTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        scheduleTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        SwingUtil.setJTableHeaderRenderer(scheduleTable, SystemInfo.getTableHeaderRenderer());
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        scheduleTable.getTableHeader().setResizingAllowed(false);
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(scheduleTable);
        scheduleTable.setDefaultRenderer(String.class, new TableCellAlignRenderer());
    }
    
    
    private boolean loadData() {
        scheds = new DataSchedules();
        schedsTemp = new DataSchedules();
        shifts = new MstShifts();
        
        // スケジュールの検索キーを登録
        scheds.setScheduleDate(scheduleDate);
        scheds.setShop(shop);
        
        schedsTemp.setScheduleDate(scheduleDate);
        schedsTemp.setShop(shop);
        // 店舗シフトの検索
        shifts.setShopId(shop.getShopID());
        
        // スケジュールデータを読み込む、あわせてシフトマスタを読み込んでおく（１度だけ）
        scheds.clear();
        schedsTemp.clear();
        shifts.clear();
        this.scheduleTable.removeAll();
        try{
            scheds.loadCheduleDetail(SystemInfo.getConnection());
            schedsTemp.loadCheduleDetail(SystemInfo.getConnection());
            shifts.load(SystemInfo.getConnection(), false);
            
        DataSchedules removeSched = new DataSchedules();
        DataSchedules removeSchedTemp = new DataSchedules();
        for( DataSchedule sched : schedsTemp ){
            if( sched.getShiftId() == 0 ){
                removeSchedTemp.add(sched);
            }
        }
        for( DataSchedule sched : removeSchedTemp ){
            schedsTemp.remove(sched);
        }
        
        removeSched.clear();
        for( DataSchedule sched : scheds ){
            if( sched.getScheduleDetails().getScheduleDetailNo() == null ){
                removeSched.add(sched);
            }
        }
        for( DataSchedule sched : removeSched ){
            scheds.remove(sched);
        }
        }catch(SQLException e){
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        
        // 対象日付を表示
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
        targetDate.setText(sdf.format(scheduleDate));
        lblShopName.setText(scheds.getShop().getShopName());
        
        
        DefaultTableModel model = (DefaultTableModel)scheduleTable.getModel();
        
        model.setNumRows(0);
        int rowCount = scheds.size();
        for( int row = 0; row < rowCount; row ++ ){
            DataSchedule sched = scheds.get(row);
            
            MstShop shop = new MstShop();
            shop.setShopID(sched.getScheduleDetails().getShopID());
            try{
                shop.load(SystemInfo.getConnection());
            }catch(SQLException e){
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return false;
            }
            
            
            MstShift shift = shifts.getShift(sched.getShiftId());
            StringBuilder sb = new StringBuilder();
            if((!shift.getStartTime().trim().equals("") && (!shift.getEndTime().trim().equals("")))){
                sb.append(shift.getStartTime().substring(0, 2));
                sb.append(":");
                sb.append(shift.getStartTime().substring(2, 4));
                sb.append("～" );
                sb.append(shift.getEndTime().substring(0, 2));
                sb.append(":");
                sb.append(shift.getEndTime().substring(2, 4));
                sb.append("");
            }
            String strShift = sb.toString();
            
            Object[] rowData = { scheds.get(row).getScheduleDetails(),
                                 strShift,
                                 shop,
                                 getTime(scheds.get(row).getScheduleDetails().getStartTime(), scheds.get(row).getScheduleDetails().getEndTime()),
                                 getTravelTime(scheds.get(row).getScheduleDetails().getTravelTime1(), scheds.get(row).getScheduleDetails().getTravelTime2()),
                               };
            
            model.addRow(rowData);
        }
        return true;
    }
    
    //時間 
    private String getTime(Date startTime, Date endTime) {
        Calendar calendarCurrent = GregorianCalendar.getInstance();
        Calendar calendarStart = GregorianCalendar.getInstance();
        Calendar calendarEnd = GregorianCalendar.getInstance();
        calendarStart.setTime(startTime);
        calendarEnd.setTime(endTime);
        calendarCurrent.setTime(this.scheduleDate);
        int hourTimeStart = calendarStart.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        if(calendarCurrent.get(Calendar.DAY_OF_YEAR) < calendarStart.get(Calendar.DAY_OF_YEAR) 
                || calendarCurrent.get(Calendar.YEAR) < calendarStart.get(Calendar.YEAR)) {
            hourTimeStart = 24 + calendarStart.get(Calendar.HOUR_OF_DAY);
        }
        int hourTimeEnd = calendarEnd.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        if(calendarCurrent.get(Calendar.DAY_OF_YEAR) < calendarEnd.get(Calendar.DAY_OF_YEAR)
                || calendarCurrent.get(Calendar.YEAR) < calendarEnd.get(Calendar.YEAR)) {
            hourTimeEnd = 24 + calendarEnd.get(Calendar.HOUR_OF_DAY);
        }
        int minuteTimeStart = calendarStart.get(Calendar.MINUTE); 
        int minuteTimeEnd = calendarEnd.get(Calendar.MINUTE); 
        
        String result = "";
        if( startTime != null && endTime != null){
            String hourStart   = hourTimeStart < 10 ? ("0" + hourTimeStart) : ("" + hourTimeStart);
            String minuteStaff = minuteTimeStart < 10 ? ("0" + minuteTimeStart) : ("" + minuteTimeStart);
            
            String hourEnd   = hourTimeEnd < 10 ? ("0" + hourTimeEnd) : ("" + hourTimeEnd);
            String minuteEnd = minuteTimeEnd < 10 ? ("0" + minuteTimeEnd) : ("" + minuteTimeEnd);
            
            result =  hourStart + "時" + minuteStaff + "分 ～ " + hourEnd + "時" + minuteEnd + "分";
        }
        return result;
    }
    
    //移動時間
    private String getTravelTime(int travel1, int travel2 ) {
        return "前" + travel1 + "分/後" + travel2 + "分";
    }
    
    // REGION IVS_PTQUANG start New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
    /**
     * Check Register help outside time of staff
     * @return boolean
     */
    private boolean verifyRegisHelpExternal()
    {
        boolean _flag = true;
        try {
            DataScheduleDetail dsd = new DataScheduleDetail();
            //開始時間
            dsd.setStartTime(this.getFromTime());
            //終了時間
            dsd.setEndTime(this.getToTime());
            //移動時間（前）
            dsd.setTravelTime1(Integer.parseInt(this.cmbTravel1.getSelectedItem().toString().equals("") ? "0" : this.cmbTravel1.getSelectedItem().toString()));
            //移動時間（後）
            dsd.setTravelTime2(Integer.parseInt(this.cmbTravel2.getSelectedItem().toString().equals("") ? "0" : this.cmbTravel2.getSelectedItem().toString()));
            //開始時間
            dsd.setExtStartTime(this.getExtStartTime(dsd.getStartTime(), dsd.getTravelTime1()));
            //終了時間
            dsd.setExtEndTime(this.getExtEndTime(dsd.getEndTime(), dsd.getTravelTime2()));

            Integer staffID = ((MstStaff) cmbStaff.getSelectedItem()).getStaffID();
            DataSchedule ds = this.schedsTemp.getSchedule(staffID);
            MstShift shift = shifts.getShift(ds.getShiftId());
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            // Start Time of Shift
            long startHoursOfShift = Long.parseLong(shift.getStartTime().substring(0, 2));
            long startMinuteOfShift = Long.parseLong(shift.getStartTime().substring(2, 4));
            long convertStartHourOfShift = TimeUnit.HOURS.toMillis(startHoursOfShift);
            long convertStartMinuteOfShift = TimeUnit.MINUTES.toMillis(startMinuteOfShift);
            long joinStartTimeOfShift = (convertStartHourOfShift + convertStartMinuteOfShift);
            // End Time of Shift
            long endHourOfShift = Long.parseLong(shift.getEndTime().substring(0, 2));
            long endMinuteOfShift = Long.parseLong(shift.getEndTime().substring(2, 4));
            long convertEndHourOfShift = TimeUnit.HOURS.toMillis(endHourOfShift);
            long convertEndMinuteOfShift = TimeUnit.MINUTES.toMillis(endMinuteOfShift);
            long joinEndTimeOfShift = (convertEndHourOfShift + convertEndMinuteOfShift);

            // Start Hour
            Date sTime = dsd.getExtStartTime();
            Calendar calendarStartHours = Calendar.getInstance();
            calendarStartHours.setTime(sTime);
            int startHours = calendarStartHours.get(Calendar.HOUR_OF_DAY);
            if(!fmt.format(calendarStartHours.getTime()).equals(fmt.format(scheduleDate)))
            {
                startHours += 24;
            }
            int startMinute = calendarStartHours.get(Calendar.MINUTE);
            // Conver to long
            long lStartHour = TimeUnit.HOURS.toMillis(startHours);
            long lStartminute = TimeUnit.MINUTES.toMillis(startMinute);
            long joinStartTimeOfHelp = (lStartHour + lStartminute);

            // End Hours
            Date eTime = dsd.getExtEndTime();
            Calendar calendarEndHours = Calendar.getInstance();
            calendarEndHours.setTime(eTime);
            int endHours = calendarEndHours.get(Calendar.HOUR_OF_DAY);
            if(!fmt.format(calendarEndHours.getTime()).equals(fmt.format(scheduleDate)))
            {
                endHours += 24;
            }
            int endMinute = calendarEndHours.get(Calendar.MINUTE);
            // Convert to long
            long lendHours = TimeUnit.HOURS.toMillis(endHours);
            long lendMinute = TimeUnit.MINUTES.toMillis(endMinute);
            long joinEndTimeOfHelp = (lendHours + lendMinute);

            // check error
            if(joinStartTimeOfHelp < joinStartTimeOfShift) {
                _flag = false;
            }
            
            if(joinEndTimeOfHelp > joinEndTimeOfShift)
            {
                _flag = false;
            }
            
        } catch (Exception ex) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, ex.getMessage());    
        }
        return _flag;
    }
    // REGION IVS_PTQUANG end New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
    
    // REGION IVS_QUANG New request #57851 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_ヘルプと休憩の重複不可対応
    /**
     * verifyRegisRecess
     * @param isAdd
     * @return 
     */
    private boolean verifyRegisRecess(boolean isAdd) {
        
        boolean _check = true;
        try {
            DataScheduleDetail dsd;
            if (isAdd) {
                dsd = new DataScheduleDetail();
            } else {
                int indexRow = this.scheduleTable.getSelectedRow();
                dsd = this.scheds.get(indexRow).getScheduleDetails();
            }
            //担当者ID
            dsd.setStaffID(((MstStaff) cmbStaff.getSelectedItem()).getStaffID());
            //ショップID
            dsd.setShopID(((MstShop) cmbShop.getSelectedItem()).getShopID());
            //シフト日付
            dsd.setScheduleDate(this.scheduleDate);
            if (isAdd) {
                //シフト詳細番号
                dsd.setMaxScheduleDetailNo(SystemInfo.getConnection());
                scheduleDetailNo = dsd.getScheduleDetailNo();
            }
            //開始時間
            dsd.setStartTime(this.getFromTime());
            //終了時間
            dsd.setEndTime(this.getToTime());
            //移動時間（前）
            dsd.setTravelTime1(Integer.parseInt(this.cmbTravel1.getSelectedItem().toString().equals("")? "0" : this.cmbTravel1.getSelectedItem().toString()));
            //移動時間（後）
            dsd.setTravelTime2(Integer.parseInt(this.cmbTravel2.getSelectedItem().toString().equals("")? "0" : this.cmbTravel2.getSelectedItem().toString()));
            //開始時間
            dsd.setExtStartTime(this.getExtStartTime(dsd.getStartTime(), dsd.getTravelTime1()));
            //終了時間
            dsd.setExtEndTime(this.getExtEndTime(dsd.getEndTime(), dsd.getTravelTime2()));

            // Get staffId from DataScheduleDetail
            int _staffID = ((MstStaff) cmbStaff.getSelectedItem()).getStaffID();
            // Get scheduleDate from DataScheduleDetail
            Date _scheduleDate = this.scheduleDate;
            // Get DataRecess
            DataRecess dtRecess = new DataRecess();
            // Load Recess from Data_Recess
            ArrayList<DataRecess> arrRecess = dtRecess.loadCompareRecess(_staffID, _scheduleDate);
            // Get Start Hour from Form
            long startHourToForm = dsd.getExtStartTime().getTime();
            // Get End Hour from Form
            long endHourToForm = dsd.getExtEndTime().getTime();
            // use for fill read value
            for (int i = 0; i < arrRecess.size(); i++) {
                // Start Hour DataRecess
                String startHourToDataRecess = arrRecess.get(i).getStartTime().substring(0, 2);
                String startMinuteToDataRecess = arrRecess.get(i).getStartTime().substring(2, 4);
                // End Hour DataRecess
                String endHourToDataRecess = arrRecess.get(i).getEndTime().substring(0, 2);
                String endMinuteToDataRecess = arrRecess.get(i).getEndTime().substring(2, 4);
                //Join Start Hour
                String joinStartHour = startHourToDataRecess + ":" + startMinuteToDataRecess;
                String joinEndHour = endHourToDataRecess + ":" + endMinuteToDataRecess;
                // Format date
                DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                // Current Date
                String currentDate = dfDate.format(this.scheduleDate);
                // Format Start Hour
                Date dtStartHour = df.parse(currentDate + " " + joinStartHour);
                Date dtEndHour = df.parse(currentDate + " " + joinEndHour);
                // Get data from Data Recess
                long startHourToConvert = dtStartHour.getTime();
                // Get data from Data Recess
                long endHourToConvert = dtEndHour.getTime();
                
                // Check Recess
                if ((startHourToForm <= startHourToConvert && endHourToForm > startHourToConvert)) {
                    _check = false;
                    break;
                } else if ((startHourToForm >= startHourToConvert && endHourToForm <= endHourToConvert)) {
                    _check = false;
                    break;
                } else if ((startHourToForm < endHourToConvert && endHourToForm >= endHourToConvert)) {
                    _check = false;
                    break;
                } else if ((startHourToForm < startHourToConvert && endHourToForm > endHourToConvert)) {
                    _check = false;
                    break;
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (ParseException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return _check;

    }
// END REGION IVS_QUANG New request #57851 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_ヘルプと休憩の重複不可対応
    
    private boolean addRowData(boolean isAdd) {
        try {
            
            DataScheduleDetail dsd ;
            if(isAdd) {
                dsd = new DataScheduleDetail();
            }else {
                int indexRow = this.scheduleTable.getSelectedRow();
                dsd = this.scheds.get(indexRow).getScheduleDetails();
            }
            //担当者ID
            dsd.setStaffID(((MstStaff)cmbStaff.getSelectedItem()).getStaffID());
            //ショップID
            dsd.setShopID(((MstShop)cmbShop.getSelectedItem()).getShopID());
            //シフト日付
            dsd.setScheduleDate(this.scheduleDate);
            if(isAdd) {
                //シフト詳細番号
                dsd.setMaxScheduleDetailNo(SystemInfo.getConnection());
                scheduleDetailNo = dsd.getScheduleDetailNo();
            }
            //開始時間
            dsd.setStartTime(this.getFromTime());
            //終了時間
            dsd.setEndTime(this.getToTime());
            //移動時間（前）
            dsd.setTravelTime1(Integer.parseInt(this.cmbTravel1.getSelectedItem().toString().equals("")? "0" : this.cmbTravel1.getSelectedItem().toString()));
            //移動時間（後）
            dsd.setTravelTime2(Integer.parseInt(this.cmbTravel2.getSelectedItem().toString().equals("")? "0" : this.cmbTravel2.getSelectedItem().toString()));
            //開始時間
            dsd.setExtStartTime(this.getExtStartTime(dsd.getStartTime(), dsd.getTravelTime1()));
            //終了時間
            dsd.setExtEndTime(this.getExtEndTime(dsd.getEndTime(), dsd.getTravelTime2()));
            dsd.regist(SystemInfo.getConnection()); 
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RegistShopEmployeePanel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private boolean checkInput(boolean isAdd) {
        if(cmbStaff.getSelectedIndex() <= 0)
        {
                MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "スタッフ名"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                cmbStaff.requestFocusInWindow();
                return	false;
        }
        if(cmbShop.getSelectedIndex() < 0)
        {
                MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "ヘルプ店舗"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                cmbShop.requestFocusInWindow();
                return	false;
        }
        if(fromHour.getSelectedIndex() <= 0 || fromMinute.getSelectedIndex() <= 0
            || toHour.getSelectedIndex() <= 0 || toMinute.getSelectedIndex() <= 0)
        {
                MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "時間"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                if(fromHour.getSelectedIndex() <= 0){
                    fromHour.requestFocusInWindow();
                }else if(fromMinute.getSelectedIndex() <= 0) {
                    fromMinute.requestFocusInWindow();
                }else if(toHour.getSelectedIndex() <= 0) {
                    toHour.requestFocusInWindow();
                }else {
                    toMinute.requestFocusInWindow();
                }
            
                return	false;
        }
        long fHour = extraLongHour(Integer.parseInt(fromHour.getSelectedItem().toString()), Integer.parseInt(fromMinute.getSelectedItem().toString()));
        long tHour = extraLongHour(Integer.parseInt(toHour.getSelectedItem().toString()), Integer.parseInt(toMinute.getSelectedItem().toString()));
        if(fHour >= tHour)
        {
            MessageDialog.showMessageDialog(this, "時間の設定が正しくありません。", this.getTitle(), JOptionPane.ERROR_MESSAGE);
            if(fromHour.getSelectedIndex() <= 0){
                fromHour.requestFocusInWindow();
            }else if(fromMinute.getSelectedIndex() <= 0) {
                fromMinute.requestFocusInWindow();
            }else if(toHour.getSelectedIndex() <= 0) {
                toHour.requestFocusInWindow();
            }else {
                toMinute.requestFocusInWindow();
            }
            return false;
        }
        
        // check between date
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calFrom.setTime(scheduleDate);
        calTo.setTime(scheduleDate);
        int idxRow = this.scheduleTable.getSelectedRow();
        
        Integer staffID = ((MstStaff)this.cmbStaff.getSelectedItem()).getStaffID();
        //TravelTime1
        int travelTime1 = Integer.parseInt(this.cmbTravel1.getSelectedItem().toString().equals("")? "0" : this.cmbTravel1.getSelectedItem().toString());
        //TravelTime2
        int travelTime2 = Integer.parseInt(this.cmbTravel2.getSelectedItem().toString().equals("")? "0" : this.cmbTravel2.getSelectedItem().toString());
        
        calFrom.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.fromHour.getSelectedItem().toString()));
        calFrom.set(Calendar.MINUTE, Integer.parseInt(this.fromMinute.getSelectedItem().toString()));
        calFrom.setTime(this.getExtStartTime(calFrom.getTime(), travelTime1));
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);
        calTo.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.toHour.getSelectedItem().toString()));
        calTo.set(Calendar.MINUTE, Integer.parseInt(this.toMinute.getSelectedItem().toString()));
        calTo.setTime(this.getExtEndTime(calTo.getTime(), travelTime2));
        calTo.set(Calendar.SECOND, 0);
        calTo.set(Calendar.MILLISECOND, 0);
        
        int row = 0;
        for ( DataSchedule ds : scheds) {
            if(idxRow >= 0 && row == idxRow && (!isAdd)) {
                row ++;
                continue;
            }
            if(staffID != null && staffID.equals(ds.getStaffId())) {
                calStart.setTime(ds.getScheduleDetails().getExtStartTime());
                calStart.set(Calendar.MINUTE, calStart.get(Calendar.MINUTE) + (+1));
                calStart.set(Calendar.SECOND, 0);
                calStart.set(Calendar.MILLISECOND, 0);
                calEnd.setTime(ds.getScheduleDetails().getExtEndTime());
                //calEnd.set(Calendar.MINUTE, calEnd.get(Calendar.MINUTE) + (ds.getScheduleDetails().getTravelTime2()-1));
                calEnd.set(Calendar.MINUTE, calEnd.get(Calendar.MINUTE) -1);
                calEnd.set(Calendar.SECOND, 0);
                calEnd.set(Calendar.MILLISECOND, 0);
                if((calFrom.getTime().before(calStart.getTime()) 
                        &&calTo.getTime().before(calStart.getTime()))
                        || (calFrom.getTime().after(calEnd.getTime()) 
                            && calTo.getTime().after(calEnd.getTime()))) {
                    row ++;
                    continue;
                }else {
                    MessageDialog.showMessageDialog(this,
                                "時間が重複しています",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                    this.fromHour.requestFocusInWindow();
                    return false;
                }
            }
            row ++;
        }
        //IVS_LVTu start edit 2016/10/11 New request #53644
//        if(isAdd) {
//            //insert 予約が設定されています。
//            if(checkReservation(((MstStaff)cmbStaff.getSelectedItem()).getStaffID(), this.getFromTime(), this.getToTime(), isAdd)) {
//                MessageDialog.showMessageDialog(this,
//                                    "予約が設定されています。",
//                                    this.getTitle(),
//                                    JOptionPane.ERROR_MESSAGE);
//                this.fromHour.requestFocusInWindow();
//                return false;
//            }
//        }else {
//            //update, delete 予約が設定されています。
//            if(checkReservation(((MstStaff)cmbStaff.getSelectedItem()).getStaffID(), this.getFromTime(), this.getToTime(), isAdd)) {
//                MessageDialog.showMessageDialog(this,
//                                    "予約が設定されています。",
//                                    this.getTitle(),
//                                    JOptionPane.ERROR_MESSAGE);
//                this.fromHour.requestFocusInWindow();
//                return false;
//            }
//        }
        //reservation staff
        ArrayList<ReservationStaff> listReservation = this.getRerservationStaff(((MstStaff)cmbStaff.getSelectedItem()).getStaffID());
        // check between date
        calFrom = Calendar.getInstance();
        calTo = Calendar.getInstance();
        calFrom.setTime(scheduleDate);
        calTo.setTime(scheduleDate);
        calFrom.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.fromHour.getSelectedItem().toString()));
        calFrom.set(Calendar.MINUTE, Integer.parseInt(this.fromMinute.getSelectedItem().toString()));
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);
        calTo.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.toHour.getSelectedItem().toString()));
        calTo.set(Calendar.MINUTE, Integer.parseInt(this.toMinute.getSelectedItem().toString()));
        calTo.set(Calendar.SECOND, 0);
        calTo.set(Calendar.MILLISECOND, 0);

        boolean flag = false;
        //shop help
        //reservation staff help
        if(!isAdd) {
            int indexRow = this.scheduleTable.getSelectedRow();
            DataScheduleDetail dsd = this.scheds.get(indexRow).getScheduleDetails();
            ArrayList<ReservationStaff> listReservationHelp = this.getRerservationInTimeHelp(((MstStaff)cmbStaff.getSelectedItem()).getStaffID(), dsd.getStartTime(), dsd.getEndTime());
            
            for ( ReservationStaff rs : listReservationHelp) {
                if(rs.getReservationShopID().equals(((MstShop)cmbShop.getSelectedItem()).getShopID())) {
                    if(!(rs.getReservationStartTime().getTime() >= calFrom.getTimeInMillis()&& rs.getReservationEndTime().getTime() <= calTo.getTimeInMillis())){
                        flag = true;
                        break;
                    }
                }
            }
            if(flag) {
                MessageDialog.showMessageDialog(this,
                                    "予約が設定されています。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                this.fromHour.requestFocusInWindow();
                return false;
            }
        }
        
        //main shop
        flag = false;
        calFrom.setTime(this.getExtStartTime(calFrom.getTime(), travelTime1));
        calTo.setTime(this.getExtEndTime(calTo.getTime(), travelTime2));
        for ( ReservationStaff rs : listReservation) {
            if(!rs.getReservationShopID().equals(((MstShop)cmbShop.getSelectedItem()).getShopID())) {
                if( (rs.getReservationStartTime().getTime() >= calFrom.getTimeInMillis()
                        && rs.getReservationStartTime().getTime() < calTo.getTimeInMillis())
                     || (rs.getReservationStartTime().getTime() <= calFrom.getTimeInMillis()
                        && rs.getReservationEndTime().getTime() > calFrom.getTimeInMillis())   
                    ) {
                    flag = true;
                    break;
                }
            }else if(rs.getReservationShopID().equals(((MstShop)cmbShop.getSelectedItem()).getShopID())) {
                // reservation in time help
                Calendar calStartTime = Calendar.getInstance();
                Calendar calEndTime = Calendar.getInstance();
                calStartTime.setTime(scheduleDate);
                calEndTime.setTime(scheduleDate);
                calStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.fromHour.getSelectedItem().toString()));
                calStartTime.set(Calendar.MINUTE, Integer.parseInt(this.fromMinute.getSelectedItem().toString()));
                calStartTime.set(Calendar.SECOND, 0);
                calStartTime.set(Calendar.MILLISECOND, 0);
                calEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.toHour.getSelectedItem().toString()));
                calEndTime.set(Calendar.MINUTE, Integer.parseInt(this.toMinute.getSelectedItem().toString()));
                calEndTime.set(Calendar.SECOND, 0);
                calEndTime.set(Calendar.MILLISECOND, 0);
                if(rs.getReservationStartTime().getTime() >= calStartTime.getTimeInMillis()&& rs.getReservationEndTime().getTime() <= calEndTime.getTimeInMillis()) {
                    continue;
                }
                // check time travel time
                if( (rs.getReservationStartTime().getTime() < calFrom.getTimeInMillis()
                        && rs.getReservationEndTime().getTime() > calFrom.getTimeInMillis())
                     || (rs.getReservationStartTime().getTime() < calTo.getTimeInMillis()
                        && rs.getReservationEndTime().getTime() > calTo.getTimeInMillis())   
                     || (rs.getReservationStartTime().getTime() < calFrom.getTimeInMillis()
                        && rs.getReservationEndTime().getTime() > calTo.getTimeInMillis())
                     || (rs.getReservationStartTime().getTime() >= calFrom.getTimeInMillis()
                        && rs.getReservationEndTime().getTime() <= calTo.getTimeInMillis())   
                    ) {
                    flag = true;
                    break;
                }
            }
        }
        if(flag) {
            MessageDialog.showMessageDialog(this,
                                "予約が設定されています。",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
            this.fromHour.requestFocusInWindow();
            return false;
        }
        //IVS_LVTu end edit 2016/10/11 New request #53644
        //check shift shop
        MstShop shop = ((MstShop)cmbShop.getSelectedItem());
        //IVS_LVTu start edit 2016/12/08 New request #58864
        if (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
            long openHour = extraLongHour(shop.getOpenHour(), shop.getOpenMinute());
            long closetHour = extraLongHour(shop.getCloseHour(), shop.getCloseMinute());
            //time staff help
            long startTimeHour = extraLongHour(Integer.parseInt(fromHour.getSelectedItem().toString()), Integer.parseInt(fromMinute.getSelectedItem().toString()) - travelTime1);
            long endTimeHour = extraLongHour(Integer.parseInt(toHour.getSelectedItem().toString()), Integer.parseInt(toMinute.getSelectedItem().toString()) + travelTime2);

            //        if((fHour < openHour || tHour > closetHour)) {
            //            MessageDialog.showMessageDialog(this,
            //                                    "ヘルプ先営業時間外です",
            //                                    this.getTitle(),
            //                                    JOptionPane.ERROR_MESSAGE);
            //                this.fromHour.requestFocusInWindow();
            //                return false;
            //        }
            if ((startTimeHour < openHour || endTimeHour > closetHour)) {
                MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_ERROR_SHIFT_SHOP,
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                this.fromHour.requestFocusInWindow();
                return false;
            }
        }
        //IVS_LVTu end edit 2016/12/08 New request #58864
        
        return true;
    }
    private long extraLongHour(int hour, int minute)
    {
        long longConvertFromHour = 1000*60*60*(Long.valueOf(hour));
        long longConvertFromMinute = 1000*60*(Long.valueOf(minute));
        
        return (longConvertFromHour+longConvertFromMinute);
    }
    
    private void selectedRowIndex() {
        int indexRow = this.scheduleTable.getSelectedRow();
        
        DataScheduleDetail dsd = this.scheds.get(indexRow).getScheduleDetails();
        
        //スタッフ名
        for(int i = 0;i < cmbStaff.getItemCount();i ++) {
            if(((MstStaff)cmbStaff.getItemAt(i)).getStaffID() == null) {
                continue;
            }
            if(((MstStaff)cmbStaff.getItemAt(i)).getStaffID().equals(dsd.getStaffID())) {
                cmbStaff.setSelectedIndex(i);
            }
        }
        //シフト
        lbShiftValue.setText(this.scheduleTable.getValueAt(indexRow, 1).toString());
        //ヘルプ店舗
        for ( int i = 0;i < cmbShop.getItemCount();i ++) {
            if(((MstShop)cmbShop.getItemAt(i)).getShopID().equals(dsd.getShopID())) {
                cmbShop.setSelectedIndex(i);
            }
        }
        //時間
        setFromTime(this.scheds.get(indexRow).getScheduleDetails().getStartTime());
        setToTime(this.scheds.get(indexRow).getScheduleDetails().getEndTime());
        //移動時間
        String travel1  = this.scheds.get(indexRow).getScheduleDetails().getTravelTime1() < 10 
                ? "0" + this.scheds.get(indexRow).getScheduleDetails().getTravelTime1().toString() : this.scheds.get(indexRow).getScheduleDetails().getTravelTime1().toString();
        String travel2  = this.scheds.get(indexRow).getScheduleDetails().getTravelTime2() < 10
                ? "0" + this.scheds.get(indexRow).getScheduleDetails().getTravelTime2().toString() : this.scheds.get(indexRow).getScheduleDetails().getTravelTime2().toString();
        
        cmbTravel1.setSelectedItem(travel1);
        cmbTravel2.setSelectedItem(travel2);
        
        renewButton.setEnabled(0 <= indexRow);
	deleteButton.setEnabled(0 <= indexRow);
        
    }
    
    public void setFromTime(Date time)
    {
        Calendar calendarCurrent = GregorianCalendar.getInstance();
        Calendar calendarStart = GregorianCalendar.getInstance();
        calendarStart.setTime(time);
        calendarCurrent.setTime(this.scheduleDate);
        int hourTimeStart = calendarStart.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        if(calendarCurrent.get(Calendar.DAY_OF_YEAR) < calendarStart.get(Calendar.DAY_OF_YEAR)
                || calendarCurrent.get(Calendar.YEAR) < calendarStart.get(Calendar.YEAR)) {
            hourTimeStart = 24 + calendarStart.get(Calendar.HOUR_OF_DAY);
        }
        int minuteTimeStart = calendarStart.get(Calendar.MINUTE);
        if( time != null){
            String hour   = hourTimeStart < 10 ? ("0" + hourTimeStart) : ("" + hourTimeStart);
            String minute = minuteTimeStart < 10 ? ("0" + minuteTimeStart) : ("" + minuteTimeStart);
            fromHour.setSelectedItem(hour);
            fromMinute.setSelectedItem(minute);
        }else{
            fromHour.setSelectedItem("");
            fromMinute.setSelectedItem("");
        }
    }
    
    public void setToTime(Date time)
    {
        Calendar calendarCurrent = GregorianCalendar.getInstance();
        Calendar calendarEnd = GregorianCalendar.getInstance();
        calendarEnd.setTime(time);
        calendarCurrent.setTime(this.scheduleDate);
        int hourTimeEnd = calendarEnd.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        if(calendarCurrent.get(Calendar.DAY_OF_YEAR) < calendarEnd.get(Calendar.DAY_OF_YEAR)
                || calendarCurrent.get(Calendar.YEAR) < calendarEnd.get(Calendar.YEAR)) {
            hourTimeEnd = 24 + calendarEnd.get(Calendar.HOUR_OF_DAY);
        }
        int minuteTimeEnd = calendarEnd.get(Calendar.MINUTE);
        if( time != null){
            String hour   = hourTimeEnd < 10 ? ("0" + hourTimeEnd) : ("" + hourTimeEnd);
            String minute = minuteTimeEnd < 10 ? ("0" + minuteTimeEnd) : ("" + minuteTimeEnd);
            toHour.setSelectedItem(hour);
            toMinute.setSelectedItem(minute);
        }else{
            toHour.setSelectedItem("");
            toMinute.setSelectedItem("");
        }
    }
    public Date getFromTime()
    {
        if( fromHour.getSelectedIndex() <= 0  ){
            // 未設定
            return this.scheduleDate;
        }else{
            Calendar calendarStart = GregorianCalendar.getInstance();
            calendarStart.setTime(this.scheduleDate);
            calendarStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fromHour.getSelectedItem().toString()));
            if(fromMinute.getSelectedItem().equals(""))
            {
                calendarStart.set(Calendar.MINUTE, 0);
            }
            else
            {
                calendarStart.set(Calendar.MINUTE, Integer.parseInt(fromMinute.getSelectedItem().toString()));
            }
            calendarStart.set(Calendar.SECOND, 0);
            calendarStart.set(Calendar.MILLISECOND, 0);
            return calendarStart.getTime();
        }
    }
        
    public Date getToTime()
    {
        if( toHour.getSelectedIndex() <= 0  ){
            // 未設定
            return this.scheduleDate;
        }else{
            Calendar calendarEnd = GregorianCalendar.getInstance();
            calendarEnd.setTime(this.scheduleDate);
            calendarEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toHour.getSelectedItem().toString()));
            if(toMinute.getSelectedItem().equals(""))
            {
                calendarEnd.set(Calendar.MINUTE, 0);
            }
            else
            {
                calendarEnd.set(Calendar.MINUTE, Integer.parseInt(toMinute.getSelectedItem().toString()));
            }
            calendarEnd.set(Calendar.SECOND, 0);
            calendarEnd.set(Calendar.MILLISECOND, 0);
            return calendarEnd.getTime();
        }
    }

    private Date getExtStartTime(Date strTime, int travelTime) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(strTime);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - travelTime);
        
        return calendar.getTime();
    }
    
    private Date getExtEndTime(Date strTime, int travelTime) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(strTime);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + travelTime);
        
        return calendar.getTime();
    }
    
    protected MstStaffs loadStaff(String shopidlist) {
        
        MstStaffs staff = new MstStaffs();
        staff.setShopIDList(shopidlist);
            try{
                staff.load(SystemInfo.getConnection(), false);
            }catch(SQLException e){
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        return staff;
    }
    
    private void deleteData()
    {
        int indexRow = this.scheduleTable.getSelectedRow();
        DataSchedule sched = scheds.get(indexRow);
        DataScheduleDetail dsd = sched.getScheduleDetails();
        if(MessageDialog.showYesNoDialog(this,
                                MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, dsd.getFullStaffName()),
                                this.getTitle(),
                                JOptionPane.WARNING_MESSAGE) != 0)
        {
                return;
        }
        ConnectionWrapper con = SystemInfo.getConnection();
 
        try{
            con.begin();
            dsd.delete(con);
            this.scheds.remove(indexRow);
            con.commit();
            con.close();
            
        //IVS_PTQUANG Start New request #58800 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_ヘルプスタッフ連携APIの呼出順変更
        //IVS_PTQUANG Start add New request #54490
        if (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
                boolean flag;
                String loginId = SystemInfo.getLoginID();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                targetDate.setText(sdf.format(scheduleDate));
                Integer staffId = ((MstStaff) cmbStaff.getSelectedItem()).getStaffID();
                //IVS_PTQUANG Start add New request #55235
                int sched_no = sched.getScheduleDetails().getScheduleDetailNo();
                //IVS_PTQUANG End add New request #55235
                flag = sendScheduleAPI(loginId, staffId, targetDate.getText(), sched_no);
                
                if(flag == false)
                {
                    MessageDialog.showMessageDialog(this,"媒体との連動ができませんでした。",this.getTitle(),JOptionPane.ERROR_MESSAGE);
                }
        }
        //IVS_PTQUANG End add New request #54490
        //IVS_PTQUANG End New request #58800 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_ヘルプスタッフ連携APIの呼出順変更
        }catch(Exception e){
            try{
                con.rollback();
            }catch(SQLException ex){
                SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private boolean checkReservation(Integer staffID,Date dtStart, Date dtEnd, boolean isAdd)
    {
        ConnectionWrapper con = SystemInfo.getConnection();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" select mt.* from data_reservation_detail dsd\n" );
            sql.append(" inner join mst_staff mt on mt.staff_id = dsd.staff_id \n" );
            if(isAdd) {    
                sql.append(" and mt.shop_id = dsd.shop_id\n" );
                sql.append(" and dsd.shop_id = ").append(SQLUtil.convertForSQL(this.shop.getShopID()));
            }else {
                sql.append(" and dsd.shop_id = ").append(SQLUtil.convertForSQL(((MstShop)this.cmbShop.getSelectedItem()).getShopID()));
            }
            sql.append(" where mt.staff_id = ").append(SQLUtil.convertForSQL(staffID));
            sql.append(" and dsd.delete_date is null ");
            sql.append(" and date_trunc('day',dsd.reservation_datetime) = ").append(SQLUtil.convertForSQL(dtEnd)).append("::date");
            sql.append(" and ((( ").append(SQLUtil.convertForSQL(dtStart)).append(" ::TIMESTAMP  between dsd.reservation_datetime ::TIMESTAMP  - ' 1 minute'::interval and dsd.reservation_datetime ::timestamp + (dsd.operation_time || 'minute')::interval + ' 1 minute'::interval) ");
            sql.append(" or ( ").append(SQLUtil.convertForSQL(dtEnd)).append(" ::TIMESTAMP  between dsd.reservation_datetime ::TIMESTAMP  - ' 1 minute'::interval and dsd.reservation_datetime ::timestamp + (dsd.operation_time || 'minute')::interval + ' 1 minute'::interval)) ");
            sql.append(" or ");
            sql.append(" (( dsd.reservation_datetime ::TIMESTAMP  between ").append(SQLUtil.convertForSQL(dtStart)).append(" ::TIMESTAMP - ' 1 minute'::interval and ").append(SQLUtil.convertForSQL(dtEnd)).append(" ::TIMESTAMP + ' 1 minute'::interval ) ");
            sql.append(" or ( dsd.reservation_datetime ::timestamp + (dsd.operation_time || 'minute')::interval between  ").append(SQLUtil.convertForSQL(dtStart)).append(" ::TIMESTAMP - ' 1 minute'::interval and ").append(SQLUtil.convertForSQL(dtEnd)).append(" ::TIMESTAMP + ' 1 minute'::interval )) ");
            sql.append(" ) ");
            ResultSetWrapper rs = con.executeQuery(sql.toString());

            if (rs.next()) {
                return true;
            }
        }catch(SQLException ex){
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return false;
    }
    
    //IVS_LVTu start add 2016/10/11 New request #53644
    /**
     * get Reservation by id staff
     * @param staffID
     * @return 
     */
    private ArrayList<ReservationStaff> getRerservationStaff(Integer staffID) {
        ArrayList<ReservationStaff> listReservation = new ArrayList<ReservationStaff>();
        ConnectionWrapper con = SystemInfo.getConnection();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" select dsd.shop_id as reservation_shop_id, \n" );
            sql.append(" mt.shop_id, \n" );
            sql.append(" dsd.reservation_datetime as reservation_start_time, \n" );
            sql.append(" (dsd.reservation_datetime ::TIMESTAMP + (dsd.operation_time || 'minute')::interval) as reservation_end_time \n" );
            sql.append(" from data_reservation_detail dsd\n" );
            sql.append(" inner join mst_staff mt on mt.staff_id = dsd.staff_id \n" );
            sql.append(" where mt.staff_id = ").append(SQLUtil.convertForSQL(staffID));
            sql.append(" and dsd.delete_date is null ");
            sql.append(" and date_trunc('day',dsd.reservation_datetime) = ").append(SQLUtil.convertForSQL(this.scheduleDate)).append("::date");
            
            ResultSetWrapper rs = con.executeQuery(sql.toString());

            while (rs.next()) {
                ReservationStaff reserStaff = new ReservationStaff(rs.getInt("reservation_shop_id"),
                                                                    rs.getInt("shop_id"),
                                                                    rs.getTime("reservation_start_time"),
                                                                    rs.getTime("reservation_end_time")
                                                                    );
                listReservation.add(reserStaff);
            }
        }catch(SQLException ex){
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return listReservation;
    }
    
    /**
     * get Reservation by id and date.
     * @param staffID
     * @param oldStartTime
     * @param oldEndTime
     * @return 
     */
    private ArrayList<ReservationStaff> getRerservationInTimeHelp(Integer staffID, Date oldStartTime,Date oldEndTime ) {
        ArrayList<ReservationStaff> listReservation = new ArrayList<ReservationStaff>();
        ConnectionWrapper con = SystemInfo.getConnection();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" select dsd.shop_id as reservation_shop_id, \n" );
            sql.append(" mt.shop_id, \n" );
            sql.append(" dsd.reservation_datetime as reservation_start_time, \n" );
            sql.append(" (dsd.reservation_datetime ::TIMESTAMP + (dsd.operation_time || 'minute')::interval) as reservation_end_time \n" );
            sql.append(" from data_reservation_detail dsd\n" );
            sql.append(" inner join mst_staff mt on mt.staff_id = dsd.staff_id \n" );
            sql.append(" where mt.staff_id = ").append(SQLUtil.convertForSQL(staffID));
            sql.append(" and dsd.delete_date is null ");
            sql.append(" and ( dsd.reservation_datetime between ").append(SQLUtil.convertForSQL(oldStartTime)).append("::TIMESTAMP + '1 minute'::interval and ").append(SQLUtil.convertForSQL(oldEndTime)).append("::TIMESTAMP - '1 minute'::interval \n");
            sql.append(" or (dsd.reservation_datetime ::TIMESTAMP + (dsd.operation_time || 'minute')::interval) between ").append(SQLUtil.convertForSQL(oldStartTime)).append("::TIMESTAMP + '1 minute'::interval and ").append(SQLUtil.convertForSQL(oldEndTime)).append("::TIMESTAMP - '1 minute'::interval)\n");
            
            ResultSetWrapper rs = con.executeQuery(sql.toString());

            while (rs.next()) {
                ReservationStaff reserStaff = new ReservationStaff(rs.getInt("reservation_shop_id"),
                                                                    rs.getInt("shop_id"),
                                                                    rs.getTime("reservation_start_time"),
                                                                    rs.getTime("reservation_end_time")
                                                                    );
                listReservation.add(reserStaff);
            }
        }catch(SQLException ex){
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return listReservation;
    }
    //IVS_LVTu end add 2016/10/11 New request #53644
    private void clear() {
        cmbShop.setSelectedIndex(0);
        cmbStaff.setSelectedIndex(0);
        fromHour.setSelectedIndex(0);
        fromMinute.setSelectedIndex(0);
        toHour.setSelectedIndex(0);
        toMinute.setSelectedIndex(0);
        cmbTravel1.setSelectedIndex(0);
        cmbTravel2.setSelectedIndex(0);
        renewButton.setEnabled(false);
	deleteButton.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        targetDate = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnClose.setVisible(false);
        btnClose.setContentAreaFilled(false);
        btnNewRow = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblShopName = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        renewButton = new javax.swing.JButton();
        fromHour = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        fromMinute = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        toHour = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        toMinute = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        cmbStaff = new javax.swing.JComboBox();
        lbStaff = new javax.swing.JLabel();
        cmbShop = new javax.swing.JComboBox();
        lbShop = new javax.swing.JLabel();
        lbtime = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        lbShiftValue = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cmbTravel1 = new javax.swing.JComboBox();
        cmbTravel2 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        lbTravel = new javax.swing.JLabel();
        bedsScrollPane = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        lbShiftShop = new javax.swing.JLabel();
        lbShiftTimeShop = new javax.swing.JLabel();

        jLabel1.setText("対象日");

        targetDate.setText("yyyy年mm月dd日");

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btnClose.setAlignmentY(0.0F);
        btnClose.setBorderPainted(false);
        btnClose.setFocusable(false);
        btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnNewRow.setBackground(new java.awt.Color(255, 255, 255));
        btnNewRow.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        btnNewRow.setBorderPainted(false);
        btnNewRow.setContentAreaFilled(false);
        btnNewRow.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
        btnNewRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewRowActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("対象");

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.setBorderPainted(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        fromHour.setMaximumRowCount(20);
        fromHour.setOpaque(false);

        jLabel3.setText("時");

        fromMinute.setMaximumRowCount(20);
        fromMinute.setOpaque(false);

        jLabel4.setText("分");

        jLabel5.setText("～");

        toHour.setMaximumRowCount(20);
        toHour.setOpaque(false);

        jLabel6.setText("時");

        toMinute.setMaximumRowCount(20);
        toMinute.setOpaque(false);

        jLabel7.setText("分");

        cmbStaff.setMaximumRowCount(20);
        cmbStaff.setOpaque(false);
        cmbStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStaffActionPerformed(evt);
            }
        });

        lbStaff.setText("スタッフ名");

        cmbShop.setMaximumRowCount(20);
        cmbShop.setOpaque(false);
        cmbShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShopActionPerformed(evt);
            }
        });

        lbShop.setText("ヘルプ店舗");

        lbtime.setText("時間");

        lbTime.setText("シフト");

        jLabel13.setText("分");

        jLabel14.setText("分/後");

        cmbTravel1.setMaximumRowCount(20);
        cmbTravel1.setOpaque(false);

        cmbTravel2.setMaximumRowCount(20);
        cmbTravel2.setOpaque(false);

        jLabel15.setText("前");

        lbTravel.setText("移動時間");

        bedsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "スタッフ名", "シフト", "ヘルプ店舗", "時間", "移動時間"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scheduleTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
        scheduleTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(scheduleTable, SystemInfo.getTableHeaderRenderer());
        scheduleTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        SelectTableCellRenderer.setSelectTableCellRenderer(scheduleTable);
        scheduleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduleTableMouseClicked(evt);
            }
        });
        scheduleTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                scheduleTableKeyReleased(evt);
            }
        });
        bedsScrollPane.setViewportView(scheduleTable);

        lbShiftShop.setText("営業時間");

        lbShiftTimeShop.setText("xxxxx");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbStaff)
                                .addGap(18, 18, 18)
                                .addComponent(cmbStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbShop)
                                .addGap(18, 18, 18)
                                .addComponent(cmbShop, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbtime)
                                .addGap(18, 18, 18)
                                .addComponent(fromHour, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel6)
                                .addGap(7, 7, 7)
                                .addComponent(fromMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbTime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbShiftValue, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7)
                        .addComponent(jLabel5)
                        .addGap(9, 9, 9)
                        .addComponent(toHour, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel3)
                        .addGap(7, 7, 7)
                        .addComponent(toMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel4)
                        .addGap(35, 35, 35)
                        .addComponent(lbTravel)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTravel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTravel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addGap(157, 157, 157))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bedsScrollPane)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblShopName)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(targetDate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 726, Short.MAX_VALUE)
                                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lbShiftShop, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbShiftTimeShop, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNewRow, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(renewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(lblShopName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(targetDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbStaff))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbShop)
                            .addComponent(cmbShop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbTime)
                            .addComponent(lbShiftValue))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fromHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbtime))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fromMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(toHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(toMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbTravel))
                            .addComponent(cmbTravel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTravel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNewRow, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(renewButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbShiftShop)
                            .addComponent(lbShiftTimeShop))))
                .addGap(11, 11, 11)
                .addComponent(bedsScrollPane)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
         * IVS_PTQUANG start add 2016/09/08 New request #54490
         * @param login_id
         * @param reservation_no
         * @return 
         */
    private Boolean sendScheduleAPI(String login_id, int staff_id, String schedule_date, int schedule_detail_no) {
        try {
            MstAPI api = new MstAPI(0);
            String apiUrl = api.getApiUrl().trim();
            String url = apiUrl + "/s/send/helpSchedule.php";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");

            //set parameter
            Map mapParam = new LinkedMap();
            mapParam.put("login_id", login_id);
            mapParam.put("staff_id", staff_id);
            mapParam.put("schedule_date", schedule_date);
            //IVS_PTQUANG Start add New request #55235
            mapParam.put("schedule_detail_no", schedule_detail_no);
            //IVS_PTQUANG start New request #58793 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_スタッフシフト連携APIにパラメータ追加
            mapParam.put("shop_id", this.shop.getShopID());
            //IVS_PTQUANG end New request #58793 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_スタッフシフト連携APIにパラメータ追加
            //IVS_PTQUANG End add New request #55235
            Gson gson = new Gson();
            String jsonParam = gson.toJson(mapParam);
            String urlParameters = "param=" + jsonParam;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //get response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if (response.toString().contains("\"code\":200")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    private void btnNewRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewRowActionPerformed
        btnNewRow.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(!checkInput(true)) {
                return;
            }
            
            if (!verifyRegisRecess(true) && (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0)) {
                MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_ERROR,
                        CONSTANT_TITLE_ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            //IVS_PTQUANG start New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
            if (!verifyRegisHelpExternal() && (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0)) {
                MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_ERROR_EXTERNAL,
                        CONSTANT_TITLE_ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            //IVS_PTQUANG end New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
                if (this.addRowData(true)) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);

                    //IVS_PTQUANG Start add New request #54490 
                    if (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
                        boolean flag;
                        String loginId = SystemInfo.getLoginID();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        targetDate.setText(sdf.format(scheduleDate));
                        Integer staffId = ((MstStaff) cmbStaff.getSelectedItem()).getStaffID();
                        //IVS_PTQUANG Start add New request #55235
                        scheds.loadCheduleDetail(SystemInfo.getConnection());
                        int sched_no = this.scheduleDetailNo;
                        flag = sendScheduleAPI(loginId, staffId, targetDate.getText(), sched_no);
                        //IVS_PTQUANG End add New request #55235
                        if (flag == false) {
                            MessageDialog.showMessageDialog(this, "媒体との連動ができませんでした。", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    //IVS_PTQUANG End add New request #54490   
                    this.loadData();
                    this.clear();
                } else {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "ヘルプ登録"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
  
        } catch (Exception ex) {
            Logger.getLogger(RegistShopEmployeePanel.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            this.scheduleDetailNo = null;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnNewRowActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        int indexRow = this.scheduleTable.getSelectedRow();
        boolean flag = false;
        if(indexRow >= 0) {
            DataScheduleDetail dsd = this.scheds.get(indexRow).getScheduleDetails();

            //スタッフ名
            if(((MstStaff)cmbStaff.getSelectedItem()).getStaffID() != null
                    && !((MstStaff)cmbStaff.getSelectedItem()).getStaffID().equals(dsd.getStaffID())) {
                flag = true;
            }
            //ヘルプ店舗
            if(!((MstShop)cmbShop.getSelectedItem()).getShopID().equals(dsd.getShopID())) {
                flag = true;
            }
            //時間
            if(dsd.getStartTime().getTime() != this.getFromTime().getTime()) {
                flag = true;
            }

            if(dsd.getEndTime().getTime() != this.getToTime().getTime()) {
                flag = true;
            }
            //移動時間
            if(!dsd.getTravelTime1().equals(Integer.parseInt(this.cmbTravel1.getSelectedItem().toString().equals("")? "0" : this.cmbTravel1.getSelectedItem().toString()))) {
                flag = true;
            }
            if(!dsd.getTravelTime2().equals(Integer.parseInt(this.cmbTravel2.getSelectedItem().toString().equals("")? "0" : this.cmbTravel2.getSelectedItem().toString()))) {
                flag = true;
            }
        }
        
        if(flag) {
            if(MessageDialog.showYesNoDialog(this,
                "保存されていません",
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE) != 0)
            {
                return;
            }
        }
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        //IVS_LVTu start add 2016/10/11 New request #53644
        // check between date
        int indexRow = this.scheduleTable.getSelectedRow();
        
        DataScheduleDetail dsd = this.scheds.get(indexRow).getScheduleDetails();
        ArrayList<ReservationStaff> listReservation = this.getRerservationInTimeHelp(((MstStaff)cmbStaff.getSelectedItem()).getStaffID(), dsd.getStartTime(), dsd.getEndTime());
        for (ReservationStaff rs : listReservation) {
            if(rs.getReservationShopID().equals(((MstShop)cmbShop.getSelectedItem()).getShopID())) {
                MessageDialog.showMessageDialog(this,
                                    "予約が設定されています。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                this.fromHour.requestFocusInWindow();
                return;
            }
        }
        //IVS_LVTu end add 2016/10/11 New request #53644
        this.deleteData();
        this.loadData();
        this.clear();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void renewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewButtonActionPerformed
        renewButton.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(!checkInput(false)) {
                return;
            }
            int indexRow = this.scheduleTable.getSelectedRow();
            DataSchedule sched = scheds.get(indexRow);
            if(indexRow < 0) {
                return;
            }
            
            if (!verifyRegisRecess(true) && (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0)) {
                MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_ERROR,
                        CONSTANT_TITLE_ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // IVS_PTQUANG start New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
            if(!verifyRegisHelpExternal() && (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0))
            {
                MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_ERROR_EXTERNAL,
                        CONSTANT_TITLE_ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // IVS_PTQUANG end New request #58780 [gb]かんざしAPI用_機能追加（ヘルプスタッフ登録画面）_シフト時間外にヘルプ登録不可対応
                if (this.addRowData(false)) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);

                    //IVS_PTQUANG Start add New request #54490   
                    if (SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
                        boolean flag;
                        String loginId = SystemInfo.getLoginID();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        targetDate.setText(sdf.format(scheduleDate));
                        Integer staffId = ((MstStaff) cmbStaff.getSelectedItem()).getStaffID();
                        //IVS_PTQUANG Start add New request #55235
                        scheds.loadCheduleDetail(SystemInfo.getConnection());
                        int sched_no = sched.getScheduleDetails().getScheduleDetailNo();
                        flag = sendScheduleAPI(loginId, staffId, targetDate.getText(), sched_no);
                        //IVS_PTQUANG End add New request #55235
                        if (flag == false) {
                            MessageDialog.showMessageDialog(this, "媒体との連動ができませんでした。", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    //IVS_PTQUANG End add New request #54490
                    this.loadData();
                    this.clear();
                } else {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "ヘルプ登録"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
        } catch (Exception ex) {
            Logger.getLogger(RegistShopEmployeePanel.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_renewButtonActionPerformed

    private void scheduleTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduleTableMouseClicked
        selectedRowIndex();
    }//GEN-LAST:event_scheduleTableMouseClicked

    private void scheduleTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scheduleTableKeyReleased
        selectedRowIndex();
    }//GEN-LAST:event_scheduleTableKeyReleased

    private void cmbStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStaffActionPerformed
        boolean flag = false;
        StringBuilder sb = new StringBuilder();
        
        Integer staffID = ((MstStaff)cmbStaff.getSelectedItem()).getStaffID();
        for (DataSchedule ds : this.schedsTemp) {
            if(staffID == null) {
                break;
            }else if (ds.getStaffId().equals(staffID)) {
                MstShift shift = shifts.getShift(ds.getShiftId());
                if( shift != null ){
                    sb.append("<html>");
                    sb.append(shift.getStartTime().substring(0, 2));
                    sb.append(":");
                    sb.append(shift.getStartTime().substring(2, 4));
                    sb.append("～" );
                    sb.append(shift.getEndTime().substring(0, 2));
                    sb.append(":");
                    sb.append(shift.getEndTime().substring(2, 4));
                    sb.append("</html>");
                    flag = true;
                    break;
                }
            }
        }
        if(flag) {
            lbShiftValue.setText(sb.toString());
        }else {
            lbShiftValue.setText("休み");
        }
        btnNewRow.setEnabled(flag);
        renewButton.setEnabled(flag);
        // change combobox
        int idxRowTable = this.scheduleTable.getSelectedRow();
        if(staffID != null && idxRowTable >= 0) {
            if(!this.scheds.get(idxRowTable).getStaffId().equals(staffID)) {
                this.renewButton.setEnabled(false);
            }else{
                this.renewButton.setEnabled(true);
            }
        }else{
                this.renewButton.setEnabled(false);
            }
    }//GEN-LAST:event_cmbStaffActionPerformed

    private void cmbShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShopActionPerformed
        MstShop shop = ((MstShop)cmbShop.getSelectedItem());
        StringBuilder sb = new StringBuilder();
        if( shop != null ){
            sb.append("<html>");
            sb.append(this.getTimeStr(shop.getOpenHour(), shop.getOpenMinute()));
            sb.append("～" );
            sb.append(this.getTimeStr(shop.getCloseHour(), shop.getCloseMinute()));
            sb.append("</html>");
        }
        lbShiftTimeShop.setText(sb.toString());
    }//GEN-LAST:event_cmbShopActionPerformed
    
    private String getTimeStr(Integer hour, Integer minute) {
        String strHour = hour < 10 ? "0" + hour : "" + hour; 
        String strMinute = minute < 10 ? "0" + minute : "" + minute; 
        
        return strHour + ":" + strMinute;        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane bedsScrollPane;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNewRow;
    private javax.swing.JComboBox cmbShop;
    private javax.swing.JComboBox cmbStaff;
    private javax.swing.JComboBox cmbTravel1;
    private javax.swing.JComboBox cmbTravel2;
    private javax.swing.JButton deleteButton;
    private javax.swing.JComboBox fromHour;
    private javax.swing.JComboBox fromMinute;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lbShiftShop;
    private javax.swing.JLabel lbShiftTimeShop;
    private javax.swing.JLabel lbShiftValue;
    private javax.swing.JLabel lbShop;
    private javax.swing.JLabel lbStaff;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbTravel;
    private javax.swing.JLabel lblShopName;
    private javax.swing.JLabel lbtime;
    private javax.swing.JButton renewButton;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JLabel targetDate;
    private javax.swing.JComboBox toHour;
    private javax.swing.JComboBox toMinute;
    // End of variables declaration//GEN-END:variables
    
    
    //IVS_LVTu start add 2016/10/11 New request #53644
    class ReservationStaff {
        private Integer reservationShopID       = null;
        
        private Integer mainShopID              = null;
        
        private Date reservationStartTime       = null;
        
        private Date reservationEndTime         = null;

        public ReservationStaff(Integer reservationShopID, Integer mainShopID, Date reservationStartTime, Date reservationEndTime) {
            this.reservationShopID       = reservationShopID;
            this.mainShopID              = mainShopID;
            this.reservationStartTime    = reservationStartTime;
            this.reservationEndTime      = reservationEndTime;
        }

        public Integer getReservationShopID() {
            return reservationShopID;
        }

        public void setReservationShopID(Integer reservationShopID) {
            this.reservationShopID = reservationShopID;
        }

        public Integer getMainShopID() {
            return mainShopID;
        }

        public void setMainShopID(Integer mainShopID) {
            this.mainShopID = mainShopID;
        }

        public Date getReservationStartTime() {
            return reservationStartTime;
        }

        public void setReservationStartTime(Date reservationStartTime) {
            this.reservationStartTime = reservationStartTime;
        }

        public Date getReservationEndTime() {
            return reservationEndTime;
        }

        public void setReservationEndTime(Date reservationEndTime) {
            this.reservationEndTime = reservationEndTime;
        }
        
    }
    //IVS_LVTu end add 2016/10/11 New request #53644
    /**
        * 列の表示位置を設定するTableCellRenderer
        */
       private class TableCellAlignRenderer extends SelectTableCellRenderer {

           /**
            * Creates a new instance of ReservationTableCellRenderer
            */
           public TableCellAlignRenderer() {
               super();
           }

           /**
            * テーブルセルレンダリングを返します。
            *
            * @param table JTable
            * @param value セルに割り当てる値
            * @param isSelected セルが選択されている場合は true
            * @param hasFocus フォーカスがある場合は true
            * @param row 行
            * @param column 列
            * @return テーブルセルレンダリング
            */

           @Override
           public Component getTableCellRendererComponent(JTable table,
                   Object value, boolean isSelected, boolean hasFocus, int row, int column) {
               super.getTableCellRendererComponent(table, value,
                       isSelected, hasFocus, row, column);

               switch (column) {
                   case 0:
                   case 2:
                   case 4:
                       super.setHorizontalAlignment(SwingConstants.LEFT);        
                       break;
                   case 1:     
                   case 3:
                       super.setHorizontalAlignment(SwingConstants.CENTER);       
                       break;
                   default:
                       super.setHorizontalAlignment(SwingConstants.RIGHT);
                       break;
               }

               return this;
           }
       }
}


