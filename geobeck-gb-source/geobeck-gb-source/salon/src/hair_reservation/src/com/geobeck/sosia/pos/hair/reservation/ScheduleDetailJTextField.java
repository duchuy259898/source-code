/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.basicinfo.company.DataScheduleDetail;
import com.geobeck.sosia.pos.master.company.MstShop;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import sun.util.calendar.Gregorian;

/**
 * @author IVS_ptquang start add 2016/09/07 New request #54112
 */
public class ScheduleDetailJTextField extends JLabel {

    /**
     * タイムスケジュール画面用予約ヘッダデータ
     */
    protected ReservationHeader header = null;
    /**
     * Variable Model dschedDetail
     */
    DataScheduleDetail dschedDetail = null;
    /**
     * shopName
     */
    String shopName = null;
    /**
     * shopNameMain
     */
    String shopNameMain = null;
    /**
     * shopNameHelp
     */
    String shopNameHelp = null;
    /**
     * jobStartTime
     */
    java.util.Date      jobStartTime      = null;
    /**
     * jobEndTime
     */
    java.util.Date      jobEndTime        = null;
     /**
     * Time of Staff 
     */
    private java.util.Date timeEndOfStaff = null;
    /**
     * @return
     */
    public String getShopName() {
        return shopName;
    }
    /**
     * @param shopName 
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    /**
     * @return 
     */
    public String getShopNameMain() {
        return shopNameMain;
    }
    /**
     * @param shopNameMain 
     */
    public void setShopNameMain(String shopNameMain) {
        this.shopNameMain = shopNameMain;
    }
    /**
     * @return 
     */
    public String getShopNameHelp() {
        return shopNameHelp;
    }
    /**
     * @param shopNameHelp 
     */
    public void setShopNameHelp(String shopNameHelp) {
        this.shopNameHelp = shopNameHelp;
    }
    /**
     * @return 
     */
    public Date getJobStartTime() {
        return jobStartTime;
    }
    /**
     * @param jobStartTime 
     */
    public void setJobStartTime(Date jobStartTime) {
        this.jobStartTime = jobStartTime;
    }
    /**
     * @return 
     */
    public Date getJobEndTime() {
        return jobEndTime;
    }
    /**
     * @param jobEndTime 
     */
    public void setJobEndTime(Date jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    /**
     * @return
     */
    public DataScheduleDetail getdschedDetail() {
        return dschedDetail;
    }

    /**
     * @param dschedDetail
     */
    public void setdschedDetail(DataScheduleDetail dschedDetail) {
        this.dschedDetail = dschedDetail;
    }

    /**
     * コンストラクタ
     */
    public ScheduleDetailJTextField() {
        super();
        this.setOpaque(true);
    }

    /**
     * コンストラクタ
     *
     * @param text 名称
     */
    public ScheduleDetailJTextField(String text) {
        super(text);
    }

    /**
     * タイムスケジュール画面用予約ヘッダデータを取得する。
     *
     * @return タイムスケジュール画面用予約ヘッダデータ
     */
    public ReservationHeader getHeader() {
        return header;
    }

    /**
     * タイムスケジュール画面用予約ヘッダデータをセットする。
     *
     * @param header タイムスケジュール画面用予約ヘッダデータ
     */
    public void setHeader(ReservationHeader header) {
        this.header = header;
    }

    /**
     * IVS_PTQUANG add 2016/09/07 New request #54112
     * @param term
     * @param colWidth
     * @param rowHeight
     * @param rtf
     * @param rh
     */
    public void setValue(int term, int colWidth, int rowHeight, Date datecurrent, int close_hour, int close_minute) {
        //long startTime = this.dschedDetail.getStartTime().getTime();
        long startTime = this.dschedDetail.getExtStartTime().getTime();
        //long extEndTime = this.dschedDetail.getEndTime().getTime();
        long extEndTime = this.dschedDetail.getExtEndTime().getTime();
        //Close Shop
        
        long dateCurrentSelect = datecurrent.getTime();
        long closeHour = TimeUnit.HOURS.toMillis(close_hour);
        long closeMinute = TimeUnit.MINUTES.toMillis(close_minute);
        
        long totalExtract = dateCurrentSelect + closeHour + closeMinute;
        
        long subTime;
        if (extEndTime > totalExtract) {
            subTime = (totalExtract - startTime);
            long totalTime = TimeUnit.MILLISECONDS.toMinutes(subTime);
            int cols = (int) totalTime / term;
            if (0 < totalTime % term) {
                cols++;
            }
            if (cols == 0) {
                cols = 1;
            }
            this.setSize(colWidth * cols - 1, rowHeight);
        }
        else
        {
            subTime = (extEndTime - startTime);
            long totalTime = TimeUnit.MILLISECONDS.toMinutes(subTime);
            int cols = (int) totalTime / term;
            if (0 < totalTime % term) {
                cols++;
            }
            if (cols == 0) {
                cols = 1;
            }
            this.setSize(colWidth * cols - 1, rowHeight);
        }
        
    }

    /**
     * IVS_ptquang add 2016/09/07 New request #54112
     *
     * @return hour
     */
    public int getHourForPartTime(GregorianCalendar current) {
        // Convert Date to long
        long date = this.dschedDetail.getExtStartTime().getTime();
        Date dateRegis = new Date(date);
        GregorianCalendar cd = new GregorianCalendar();
        cd.setTime(dateRegis);
        
        long dateCurrent = current.getTimeInMillis();
        Date dateRegiseCurrent = new Date(dateCurrent);
        GregorianCalendar cdeCurrent = new GregorianCalendar();
        cdeCurrent.setTime(dateRegiseCurrent);
        
        int hour = cd.get(Calendar.HOUR_OF_DAY);
        if (cd.get(cd.DAY_OF_YEAR) > cdeCurrent.get(Calendar.DAY_OF_YEAR) || cd.get(Calendar.YEAR) > cdeCurrent.get(Calendar.YEAR)) {
            hour += 24;
        }
        return hour;
    }

    /**
     * IVS_ptquang add 2016/09/07 New request #54112
     *
     * @return
     */
    public int getMinuteForPartTime() {
        long date = this.dschedDetail.getExtStartTime().getTime();
        Date dateRegis = new Date(date);
        GregorianCalendar cd = new GregorianCalendar();
        cd.setTime(dateRegis);
        return cd.get(Calendar.MINUTE);
    }

    /**
     * IVS_ptquang add 2016/09/07 New request #54112
     *
     * @param term
     * @param colWidth
     * @param rowHeight
     * @param rtf
     * @param rh
     */
    public void setValueDrawingPartime(int term, int colWidth, int rowHeight, GregorianCalendar datecurrent, int close_hour, int close_minute) {
        long startTime = this.jobStartTime.getTime();
        long extEndTime = this.jobEndTime.getTime();
        
        //Close Shop
        long dateCurrentSelect = datecurrent.getTimeInMillis();
        long closeHour = TimeUnit.HOURS.toMillis(close_hour);
        long closeMinute = TimeUnit.MINUTES.toMillis(close_minute);
        
        long totalExtract = dateCurrentSelect + closeHour + closeMinute;
        
        long subTime;
        if (extEndTime > totalExtract) {
            subTime = (totalExtract - startTime);
            long totalTime = TimeUnit.MILLISECONDS.toMinutes(subTime);
            int cols = (int) totalTime / term;
            if (0 < totalTime % term) {
                cols++;
            }
            if (cols == 0) {
                cols = 1;
            }
            this.setSize(colWidth * cols - 1, rowHeight);
        }
        else
        {
            subTime = (extEndTime - startTime);
            long totalTime = TimeUnit.MILLISECONDS.toMinutes(subTime);
            int cols = (int) totalTime / term;
            if (0 < totalTime % term) {
                cols++;
            }
            if (cols == 0) {
                cols = 1;
            }
            this.setSize(colWidth * cols - 1, rowHeight);
        }
    }

    /**
     * IVS_ptquang add 2016/09/07 New request #54112
     *
     * @return hour
     */
    public int getHourDrawingPartTime(GregorianCalendar currentDate) {
        long date = this.jobStartTime.getTime();
        Date dateRegis = new Date(date);
        GregorianCalendar cd = new GregorianCalendar();
        cd.setTime(dateRegis);
        //Date current time
        
        long dateCurrent = currentDate.getTimeInMillis();
        Date dateRegiseCurrent = new Date(dateCurrent);
        GregorianCalendar cdeCurrent = new GregorianCalendar();
        cdeCurrent.setTime(dateRegiseCurrent);
        
        int hour = cd.get(Calendar.HOUR_OF_DAY);

        if (cd.get(cd.DAY_OF_YEAR) > cdeCurrent.get(Calendar.DAY_OF_YEAR) || cd.get(Calendar.YEAR) > cdeCurrent.get(Calendar.YEAR)) {
            hour += 24;
        }
        return hour;
    }

    /**
     * IVS_ptquang add 2016/09/07 New request #54112
     *
     * @return
     */
    public int getMinuteDrawingPartTime() {
        long date = this.jobStartTime.getTime();
        Date dateRegis = new Date(date);
        GregorianCalendar cd = new GregorianCalendar();
        cd.setTime(dateRegis);
        return cd.get(Calendar.MINUTE);
    }

    /**
     * @return the timeEndOfStaff
     */
    public java.util.Date getTimeEndOfStaff() {
        return timeEndOfStaff;
    }

    /**
     * @param timeEndOfStaff the timeEndOfStaff to set
     */
    public void setTimeEndOfStaff(java.util.Date timeEndOfStaff) {
        this.timeEndOfStaff = timeEndOfStaff;
    }

}
