/*
 * StaffWorkTime.java
 *
 * Created on 2009/12/02, 15:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.company;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author geobeck
 */
public class StaffWorkTime {
    
	private	MstStaff            staff		= null;	// �X�^�b�t
        private String              shiftTime           = null; // �V�t�g����
	private GregorianCalendar   workingDate         = null;	// �o�Γ�
        private Integer             workingShopId       = null; // �o�ΓX��
	private GregorianCalendar   workingStartTime	= null;	// �o�Ύ���
	private GregorianCalendar   workingFinishTime	= null;	// �ދΎ���
	private GregorianCalendar   recessStartTime1	= null;	// �x�e�J�n����1
	private GregorianCalendar   recessFinishTime1	= null;	// �x�e�I������1
	private GregorianCalendar   recessStartTime2	= null;	// �x�e�J�n����2
	private GregorianCalendar   recessFinishTime2	= null;	// �x�e�I������2
	private GregorianCalendar   recessStartTime3	= null;	// �x�e�J�n����3
	private GregorianCalendar   recessFinishTime3	= null;	// �x�e�I������3
	private GregorianCalendar   recessStartTime4	= null;	// �x�e�J�n����4
	private GregorianCalendar   recessFinishTime4	= null;	// �x�e�I������4
	private GregorianCalendar   recessStartTime5	= null;	// �x�e�J�n����5
	private GregorianCalendar   recessFinishTime5	= null;	// �x�e�I������5
	private Integer             recessType          = null; // �x�e�敪
        private Integer             staffShopId         = null; // �X�^�b�t�����X��
        //IVS_LVTu start 2016/08/25 New request #54109
        private String              scheduleTime        = null; 
        //IVS_LVTu end 2016/08/25 New request #54109

	/** Creates a new instance of StaffWorkTime */
	public StaffWorkTime() {
	}
	
        public MstStaff getStaff() {
            return staff;
        }

        public void setStaff(MstStaff staff) {
            this.staff = staff;
        }

        public String getShiftTime() {
            return shiftTime;
        }

        public void setShiftTime(String shiftTime) {
            this.shiftTime = shiftTime;
        }

        public GregorianCalendar getWorkingDate() {
            return workingDate;
        }

        public void setWorkingDate(GregorianCalendar workingDate) {
            this.workingDate = workingDate;
        }

        public Integer getWorkingShopId() {
            return workingShopId;
        }

        public void setWorkingShopId(Integer workingShopId) {
            this.workingShopId = workingShopId;
        }

        public GregorianCalendar getWorkingStartTime() {
            return workingStartTime;
        }

        public void setWorkingStartTime(GregorianCalendar workingStartTime) {
            this.workingStartTime = workingStartTime;
        }

        public GregorianCalendar getWorkingFinishTime() {
            return workingFinishTime;
        }

        public void setWorkingFinishTime(GregorianCalendar workingFinishTime) {
            this.workingFinishTime = workingFinishTime;
        }

        public GregorianCalendar getRecessStartTime1() {
            return recessStartTime1;
        }

        public void setRecessStartTime1(GregorianCalendar recessStartTime1) {
            this.recessStartTime1 = recessStartTime1;
        }

        public GregorianCalendar getRecessFinishTime1() {
            return recessFinishTime1;
        }

        public void setRecessFinishTime1(GregorianCalendar recessFinishTime1) {
            this.recessFinishTime1 = recessFinishTime1;
        }

        public GregorianCalendar getRecessStartTime2() {
            return recessStartTime2;
        }

        public void setRecessStartTime2(GregorianCalendar recessStartTime2) {
            this.recessStartTime2 = recessStartTime2;
        }

        public GregorianCalendar getRecessFinishTime2() {
            return recessFinishTime2;
        }

        public void setRecessFinishTime2(GregorianCalendar recessFinishTime2) {
            this.recessFinishTime2 = recessFinishTime2;
        }

        public GregorianCalendar getRecessStartTime3() {
            return recessStartTime3;
        }

        public void setRecessStartTime3(GregorianCalendar recessStartTime3) {
            this.recessStartTime3 = recessStartTime3;
        }

        public GregorianCalendar getRecessFinishTime3() {
            return recessFinishTime3;
        }

        public void setRecessFinishTime3(GregorianCalendar recessFinishTime3) {
            this.recessFinishTime3 = recessFinishTime3;
        }

        public GregorianCalendar getRecessStartTime4() {
            return recessStartTime4;
        }

        public void setRecessStartTime4(GregorianCalendar recessStartTime4) {
            this.recessStartTime4 = recessStartTime4;
        }

        public GregorianCalendar getRecessFinishTime4() {
            return recessFinishTime4;
        }

        public void setRecessFinishTime4(GregorianCalendar recessFinishTime4) {
            this.recessFinishTime4 = recessFinishTime4;
        }

        public GregorianCalendar getRecessStartTime5() {
            return recessStartTime5;
        }

        public void setRecessStartTime5(GregorianCalendar recessStartTime5) {
            this.recessStartTime5 = recessStartTime5;
        }

        public GregorianCalendar getRecessFinishTime5() {
            return recessFinishTime5;
        }

        public void setRecessFinishTime5(GregorianCalendar recessFinishTime5) {
            this.recessFinishTime5 = recessFinishTime5;
        }

        public Integer getRecessType() {
            return recessType;
        }

        public void setRecessType(Integer recessType) {
            this.recessType = recessType;
        }

        public Integer getStaffShopId() {
            return staffShopId;
        }

        public void setStaffShopId(Integer staffShopId) {
            this.staffShopId = staffShopId;
        }
        
        //IVS_LVTu start 2016/08/25 New request #54109
        public String getScheduleTime() {
            return scheduleTime;
        }

        public void setScheduleTime(java.util.Date dtStart, java.util.Date dtEnd) {
            Calendar calendarStart = GregorianCalendar.getInstance();
            Calendar calendarEnd = GregorianCalendar.getInstance();
            calendarStart.setTime(dtStart);
            calendarEnd.setTime(dtEnd);
            int hourTimeStart = calendarStart.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
            int hourTimeEnd = calendarEnd.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
            int minuteTimeStart = calendarStart.get(Calendar.MINUTE); 
            int minuteTimeEnd = calendarEnd.get(Calendar.MINUTE); 
            
            String hourStart   = hourTimeStart < 10 ? ("0" + hourTimeStart) : ("" + hourTimeStart);
            String minuteStaff = minuteTimeStart < 10 ? ("0" + minuteTimeStart) : ("" + minuteTimeStart);
            
            String hourEnd   = hourTimeEnd < 10 ? ("0" + hourTimeEnd) : ("" + hourTimeEnd);
            String minuteEnd = minuteTimeEnd < 10 ? ("0" + minuteTimeEnd) : ("" + minuteTimeEnd);
            
            this.scheduleTime = hourStart + "�F" + minuteStaff + "�`" + hourEnd + "�F" + minuteEnd ;
        }
        //IVS_LVTu end 2016/08/25 New request #54109

        /**
	 * �f�[�^���Z�b�g����
	 */
        public void setData( ResultSetWrapper rs ) throws SQLException
        {
            MstStaff ms = new MstStaff();
            ms.setData(rs);
            this.setStaff(ms);

            this.setShiftTime(rs.getString("shift_time"));
            
            this.setWorkingDate(rs.getGregorianCalendar( "working_date" ));
            this.setWorkingShopId(rs.getInt("working_shop_id"));
            this.setWorkingStartTime(rs.getGregorianCalendar( "working_start_time" ));
            this.setWorkingFinishTime(rs.getGregorianCalendar( "working_finish_time" ));
            this.setRecessStartTime1(rs.getGregorianCalendar("recess_start_time1"));
            this.setRecessFinishTime1(rs.getGregorianCalendar("recess_finish_time1"));
            this.setRecessStartTime2(rs.getGregorianCalendar("recess_start_time2"));
            this.setRecessFinishTime2(rs.getGregorianCalendar("recess_finish_time2"));
            this.setRecessStartTime3(rs.getGregorianCalendar("recess_start_time3"));
            this.setRecessFinishTime3(rs.getGregorianCalendar("recess_finish_time3"));
            this.setRecessStartTime4(rs.getGregorianCalendar("recess_start_time4"));
            this.setRecessFinishTime4(rs.getGregorianCalendar("recess_finish_time4"));
            this.setRecessStartTime5(rs.getGregorianCalendar("recess_start_time5"));
            this.setRecessFinishTime5(rs.getGregorianCalendar("recess_finish_time5"));
            this.setRecessType(rs.getInt("recess_type"));
            this.setStaffShopId(rs.getInt("staff_shop_id"));
	 }
	 
	/**
	 * �o�ދ΃f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
            String sql = "";

            if (isExists(con)) {
                sql = this.getUpdateSQL();
            } else {
                sql = this.getInsertSQL();
            }

            return (con.executeUpdate(sql) == 1);

	}
	
	/**
	 * �o�ދ΃f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
            // ���R�[�h�����݂��Ȃ��ꍇ�ɂ͏������s��Ȃ�
            if (!isExists(con)) return false;
		
            // �폜�pSQL���擾����
            return (con.executeUpdate(this.getDeleteSQL()) == 1);
	}
	
	/**
	 * �X�^�b�t�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
            if (con == null) return false;
		
            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
            
            return rs.next();
	}
	 
	/**
	 * �o�ދ΃f�[�^�擾SQL
	 */
	public String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select 1 from data_staff_work_time");
            sql.append(" where");
            sql.append("         staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()));
            sql.append("     and working_date = " + SQLUtil.convertForSQLDateOnly(this.getWorkingDate()));

            return sql.toString();
	}
	
	/**
	 * �o�ދ΃f�[�^�}��SQL���擾����
	 */
	public String getInsertSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" insert into data_staff_work_time");
            sql.append(" (");
            sql.append("      staff_id");
            sql.append("     ,working_date");
            sql.append("     ,shop_id");
            sql.append("     ,working_start_time");
            sql.append("     ,working_finish_time");
            sql.append("     ,recess_start_time1");
            sql.append("     ,recess_finish_time1");
            sql.append("     ,recess_start_time2");
            sql.append("     ,recess_finish_time2");
            sql.append("     ,recess_start_time3");
            sql.append("     ,recess_finish_time3");
            sql.append("     ,recess_start_time4");
            sql.append("     ,recess_finish_time4");
            sql.append("     ,recess_start_time5");
            sql.append("     ,recess_finish_time5");
            sql.append("     ,insert_date");
            sql.append("     ,update_date");
            sql.append(" ) values (");
            sql.append("      " + SQLUtil.convertForSQL(this.getStaff().getStaffID()));
            sql.append("     ," + SQLUtil.convertForSQLDateOnly(this.getWorkingDate()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getWorkingShopId()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getWorkingStartTime()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getWorkingFinishTime()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessStartTime1()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessFinishTime1()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessStartTime2()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessFinishTime2()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessStartTime3()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessFinishTime3()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessStartTime4()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessFinishTime4()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessStartTime5()));
            sql.append("     ," + SQLUtil.convertForSQL(this.getRecessFinishTime5()));
            sql.append("     ,current_timestamp");
            sql.append("     ,current_timestamp");
            sql.append(" )");

            return sql.toString();
	}
	
	/**
	 * �o�ދ΃f�[�^�X�VSQL���擾����
	 */
	public String getUpdateSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" update data_staff_work_time");
            sql.append(" set");
            sql.append("      shop_id = " + SQLUtil.convertForSQL(this.getWorkingShopId()));
            sql.append("     ,working_start_time = " + SQLUtil.convertForSQL(this.getWorkingStartTime()));
            sql.append("     ,working_finish_time = " + SQLUtil.convertForSQL(this.getWorkingFinishTime()));
            sql.append("     ,recess_start_time1 = " + SQLUtil.convertForSQL(this.getRecessStartTime1()));
            sql.append("     ,recess_finish_time1 = " + SQLUtil.convertForSQL(this.getRecessFinishTime1()));
            sql.append("     ,recess_start_time2 = " + SQLUtil.convertForSQL(this.getRecessStartTime2()));
            sql.append("     ,recess_finish_time2 = " + SQLUtil.convertForSQL(this.getRecessFinishTime2()));
            sql.append("     ,recess_start_time3 = " + SQLUtil.convertForSQL(this.getRecessStartTime3()));
            sql.append("     ,recess_finish_time3 = " + SQLUtil.convertForSQL(this.getRecessFinishTime3()));
            sql.append("     ,recess_start_time4 = " + SQLUtil.convertForSQL(this.getRecessStartTime4()));
            sql.append("     ,recess_finish_time4 = " + SQLUtil.convertForSQL(this.getRecessFinishTime4()));
            sql.append("     ,recess_start_time5 = " + SQLUtil.convertForSQL(this.getRecessStartTime5()));
            sql.append("     ,recess_finish_time5 = " + SQLUtil.convertForSQL(this.getRecessFinishTime5()));
            sql.append("     ,update_date = current_timestamp");
            sql.append("     ,delete_date = null");
            sql.append(" where");
            sql.append("         staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()));
            sql.append("     and working_date = " + SQLUtil.convertForSQLDateOnly(this.getWorkingDate()));

            return sql.toString();
	}
	
	/**
	 * �o�ދ΃f�[�^�폜SQL���擾����
	 */
	public String getDeleteSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" update data_staff_work_time");
            sql.append(" set");
            sql.append("      update_date = current_timestamp");
            sql.append("     ,delete_date = current_timestamp");
            sql.append(" where");
            sql.append("         staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()));
            sql.append("     and working_date = " + SQLUtil.convertForSQLDateOnly(this.getWorkingDate()));

            return sql.toString();
	}
}
