/*
 * DataRecess.java
 *
 * Created on 2009/04/30, 12:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.SQLUtil;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author takeda
 */
public class DataRecess {
    
    private Integer staffId;        /* �X�^�b�tID       Key */
    private Date    scheduleDate;   /* ���t             Key */
    private Integer recessId;       /* �x�eID           Key */
    private String  startTime;      /* �J�n����             */
    private String  endTime;        /* �I������             */
    private String  note; 
    //IVS_NHTVINH start add 2016/09/07 New request #54380
    private Integer allianceRecessId;/* ����             */
    //IVS_NHTVINH end add 2016/09/07 New request #54380
    //IVS_LVTu start add 2016/11/30 Bug #58699
    private Integer staffIdNew;     // start_id new
    private Date    scheduleDateNew; // schudule_date new
    private Integer recessIdNew;    // recess_id new

    public Integer getStaffIdNew() {
        return staffIdNew;
    }

    public void setStaffIdNew(Integer staffIdNew) {
        this.staffIdNew = staffIdNew;
    }

    public Date getScheduleDateNew() {
        return scheduleDateNew;
    }

    public void setScheduleDateNew(Date scheduleDateNew) {
        this.scheduleDateNew = scheduleDateNew;
    }

    public Integer getRecessIdNew() {
        return recessIdNew;
    }

    public void setRecessIdNew(Integer recessIdNew) {
        this.recessIdNew = recessIdNew;
    }
    //IVS_LVTu end add 2016/11/30 Bug #58699
    /** Creates a new instance of DataRecess */
    public DataRecess() {
    }
    
    public boolean equals(Object o) {
        if(o != null && o instanceof DataRecess) {
            DataRecess reces = (DataRecess)o;
            if( reces.getStaffId().equals(this.getStaffId()) &&
                reces.getScheduleDate().equals(this.getScheduleDate()) &&
                reces.getRecessId().equals(this.getRecessId()) ) {
                
                return	true;
            } else {
                return	false;
            }
        } else {
            return	false;
        }
    }
    
    // �w�肵�����Ԃ��A�x�e���ԓ����𒲍�
    public boolean inRange(String time)
    {
        // ���Ԗ��ݒ�́A�Ȃ�
        if( startTime == null || endTime == null || 
            (startTime.equals("0000") && endTime.equals("0000")) ||
            CheckUtil.isNumber(startTime) == false ||
            CheckUtil.isNumber(endTime)   == false ){
            return false;
        }
                
        // �w�莞�Ԃ��A�͈͓�
        if( (Integer.parseInt(time) >= Integer.parseInt(startTime)) &&
             (Integer.parseInt(time) < Integer.parseInt(endTime)) ){
            return true;
        }
        return false;
    }
    
    // �w�肵�����Ԕ͈͓��ɋx�e���Ԃ��܂܂�邩�𒲍�
    boolean inRange(String startTime, String endTime) {
        // ���Ԗ��ݒ�́A�Ȃ�
        if( startTime == null || endTime == null ||  this.startTime == null || this.endTime == null)
        {
            return false;

        }
        if((startTime.equals("0000") && endTime.equals("0000")) ||  
            CheckUtil.isNumber(this.startTime) == false ||
            CheckUtil.isNumber(this.endTime)   == false ){
            return false;
        }
        
        Integer targetStart = Integer.parseInt(startTime);
        Integer targetEnd = Integer.parseInt(endTime);
        Integer start = Integer.parseInt(this.startTime);
        Integer end = Integer.parseInt(this.endTime);
        
        // �w�莞�Ԕ͈͂̈ꕔ���A�x�e���Ԃɂ��Ԃ��Ă��鎞
        if( targetStart > start && targetStart < end || 
            targetEnd > start && targetEnd < end ){
            return true;
        }
        
        // �w�莞�Ԕ͈͓��ɋx�e���Ԃ����݂���Ƃ�
        if( targetStart < start && targetEnd > end ){
            return true;
        }
        
        return false;
    }
    
    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Integer getRecessId() {
        return recessId;
    }

    public void setRecessId(Integer recessId) {
        this.recessId = recessId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    //IVS_NHTVINH start add 2016/09/07 New request #54380
    public Integer getAllianceRecessId() {
        return allianceRecessId;
    }

    public void setAllianceRecessId(Integer allianceRecessId) {
        this.allianceRecessId = allianceRecessId;
    }
    //IVS_NHTVINH end add 2016/09/07 New request #54380
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = (note != null ? note : "");
    }

    /**
     * �f�[�^���N���A����B
     */
    public void clear() {
        this.setStaffId(null);
        this.setScheduleDate(null);
        this.setRecessId(null);
        this.setStartTime(null);
        this.setEndTime(null);
        this.setNote(null);
    }
    
    /**
     * �x�e�f�[�^����A�ݒ肳��Ă���X�^�b�tID�A�X�P�W���[�����t�A�x�eID�̃f�[�^��ǂݍ��ށB
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if(this.getStaffId() == null || this.getStaffId() < 0)      return  true;
        if(this.getScheduleDate() == null)                          return  true;
        if(this.getRecessId() == null)                              return  true;
        
        if(con == null)	return	false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setData(rs);
        } else {
            return false;
        }
        
        return	true;
    }
    
    /**
     * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean setData(ResultSetWrapper rs) throws SQLException {
        // staff_id��null�Ȃ�΁A�f�[�^�Ȃ�
        if(rs.getString("recess_id") == null ) return false;
            
        this.setStaffId(rs.getInt("staff_id"));
        this.setScheduleDate(rs.getDate("schedule_date"));
        this.setRecessId(rs.getInt("recess_id"));
        this.setStartTime(rs.getString("start_time"));
        this.setEndTime(rs.getString("end_time"));
        this.setNote(rs.getString("note"));
        //IVS_NHTVINH start add 2016/09/07 New request #54380
        try{
            this.setAllianceRecessId(rs.getInt("alliance_recess_id"));
        }catch(Exception e){}
        //IVS_NHTVINH end add 2016/09/07 New request #54380
        return true;
    }
    
    //IVS_NHTVINH start add 2016/10/19 New request #55977
    public Integer getAllianceRecessIdFromDb(ConnectionWrapper con) throws SQLException{
        try{
            if( null != this.getStaffId() 
            && this.getStaffId() >= 0 
            && null != this.getScheduleDate() 
            && null != this.getStartTime() 
            && null != this.getEndTime() 
            && null != con) {
            
                ResultSetWrapper rs = con.executeQuery(this.getSqlSelectAlianceRecessId());
                if (rs.next()) {
                    this.setAllianceRecessId(rs.getInt("alliance_recess_id"));
                }
            }
        }catch(Exception e){
            throw new SQLException(e.getMessage());
        }
        return allianceRecessId;
    }
    //IVS_NHTVINH end add 2016/10/19 New request #55977
   
    /**
     * �x�e�f�[�^�Ƀf�[�^��o�^����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        if (isExists(con)) {
            //IVS_LVTu start edit 2016/11/30 Bug #58699
            if(this.staffIdNew != null && this.scheduleDateNew != null && this.recessIdNew != null) {
                return (con.executeUpdate(this.getUpdateSQL2()) == 1);
            }else {
                return (con.executeUpdate(this.getUpdateSQL()) == 1);
            }
            //IVS_LVTu end edit 2016/11/30 Bug #58699
        } else {
            if(con.executeUpdate(this.getInsertSQL()) != 1) {
                return false;
            }
            return true;
        }
    }
       
    
    /**
     * �x�e�f�[�^����f�[�^���폜����B�i�_���폜�j
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        if(isExists(con)) {
            if(con.executeUpdate(this.getDeleteSQL()) == 1) {
                return	true;
            }
        }
        
        return	false;
    }
    
    
    //IVS_LVTu start add 2016/03/24 Bug #49232
    /**
     * delete data_recess
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean deleteDataRecess(ConnectionWrapper con) throws SQLException {
        if(isExists(con)) {
            if(con.executeUpdate(this.getDeleteDataRecessSQL()) == 1) {
                return	true;
            }
        }
        
        return	false;
    }
    
    //IVS_LVTu end add 2016/03/24 Bug #49232
    /**
     * �x�e�f�[�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if(this.getStaffId() == null || this.getStaffId() < 0)      return  true;
        if(this.getScheduleDate() == null)                          return  true;
        if(this.getRecessId() == null)                              return  true;
        
        if(con == null)	return	false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }   
     //IVS_NHTVINH start add 2016/10/04 New request #54487 [gb]���񂴂�API�p_�@�\�ǉ��i�x�e�o�^��ʁj
    /**
     * check regist recess, 
     * if time regist is overlap then return true
     * else return false
     */
    public boolean checkTimeRegistOverlap(ConnectionWrapper con) throws SQLException {
        if( null != this.getStaffId() 
            && this.getStaffId() >= 0 
            && null != this.getScheduleDate() 
            && null != this.getStartTime() 
            && null != this.getEndTime() 
            && null != con) {
            
            ResultSetWrapper rs = con.executeQuery(this.getSelectWithTimeSQL());
            if (rs.next()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * get sql data recess with time less than end time and greate than start time
     * @return 
     */
    private String getSelectWithTimeSQL(){
        StringBuffer sql = new StringBuffer(
                "select *\n" +
                "from data_recess\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n" +
                "and    (((0 || start_time)::integer >= " + SQLUtil.convertForSQL(Integer.parseInt(this.getStartTime())) + "\n" +
                "and    (0 || start_time)::integer < " + SQLUtil.convertForSQL(Integer.parseInt(this.getEndTime())) + ")\n" +
                "or    ((0 || start_time)::integer <= " + SQLUtil.convertForSQL(Integer.parseInt(this.getStartTime())) + "\n" +
                "and    (0 || end_time)::integer > " + SQLUtil.convertForSQL(Integer.parseInt(this.getStartTime())) + "))\n"
        );
        
        if(null != this.getRecessId() && this.getRecessId() >= 0){
            sql.append(" and recess_id != " + SQLUtil.convertForSQL(this.getRecessId()));
        }
        return sql.toString();
    }
    //IVS_NHTVINH end add 2016/10/04 New request #54487 [gb]���񂴂�API�p_�@�\�ǉ��i�x�e�o�^��ʁj
    
    private String getSqlSelectAlianceRecessId(){
        return	"select alliance_recess_id \n" +
                "from data_recess\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n" +
                "and    recess_id= " + SQLUtil.convertForSQL(this.getRecessId()) + "\n";
    }
    
    /**
     * Select�����擾����B
     * @return Select��
     */
    private String getSelectSQL() {
        return	"select *\n" +
                "from data_recess\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n" +
                "and    recess_id= " + SQLUtil.convertForSQL(this.getRecessId()) + "\n";
    }
    
    /**
     * Insert�����擾����B
     * @return Insert��
     */
    private String	getInsertSQL() {
        return	"insert into data_recess\n" +
                "(staff_id, schedule_date, recess_id,\n" +
                "start_time, end_time,\n" +
                "insert_date, update_date, delete_date,note)\n" +
                "values\n" +
                "(\n" +
                SQLUtil.convertForSQL(this.getStaffId()) + ",\n" +
                SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + ",\n" +
                SQLUtil.convertForSQL(this.getRecessId()) + ",\n" +
                SQLUtil.convertForSQL(this.getStartTime()) + ",\n" +
                SQLUtil.convertForSQL(this.getEndTime()) + ",\n" +
                "current_timestamp, current_timestamp, null,\n" +
                SQLUtil.convertForSQL(this.getNote()) + "\n" +
                ")\n";
    }
    
    /**
     * Update�����擾����B
     * @return Update��
     */
    private String	getUpdateSQL() {
        return	"update data_recess\n" +
                "set\n" +
                "start_time   = " + SQLUtil.convertForSQL(this.getStartTime()) + ",\n" +
                "end_time     = " + SQLUtil.convertForSQL(this.getEndTime()) + ",\n" +
                "note     = " + SQLUtil.convertForSQL(this.getNote()) + ",\n" +
                "update_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n" +
                "and    recess_id= " + SQLUtil.convertForSQL(this.getRecessId()) + "\n";
    }
    
    //IVS_LVTu start add 2016/11/30 Bug #58699
    /**
     * �X�V�pUpdate�����擾����B
     * @return Update��
     */
    private String	getUpdateSQL2() {
        return	"update data_recess\n" +
                "set\n" +
                "start_time   = " + SQLUtil.convertForSQL(this.getStartTime()) + ",\n" +
                "end_time     = " + SQLUtil.convertForSQL(this.getEndTime()) + ",\n" +
                "note     = " + SQLUtil.convertForSQL(this.getNote()) + ",\n" +
                "staff_id      = " + SQLUtil.convertForSQL(this.getStaffIdNew()) + ",\n" +
                "schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDateNew()) + ",\n" +
                "recess_id= " + SQLUtil.convertForSQL(this.getRecessIdNew()) + ",\n" +
                "update_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n" +
                "and    recess_id= " + SQLUtil.convertForSQL(this.getRecessId()) + "\n";
    }
    //IVS_LVTu end add 2016/11/30 Bug #58699
    /**
     * �폜�pUpdate�����擾����B
     * @return �폜�pUpdate��
     */
    private String	getDeleteSQL() {
        return	"update data_recess\n" +
                "set\n" +
                "update_date = current_timestamp,\n" +
                "delete_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n" +
                "and    recess_id= " + SQLUtil.convertForSQL(this.getRecessId()) + "\n";
    }    

    //IVS_LVTu start add 2016/03/24 Bug #49232
    /**
     * �폜�pUpdate�����擾����B
     * @return �폜�pUpdate��
     */
    private String	getDeleteDataRecessSQL() {
        return	"delete from data_recess\n" +
                "where  staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n" +
                "and    recess_id= " + SQLUtil.convertForSQL(this.getRecessId()) + "\n";
    }    
    //IVS_LVTu end add 2016/03/24 Bug #49232
    
    // REGION IVS_PTQUANG New request #57851 [gb]���񂴂�API�p_�@�\�ǉ��i�w���v�X�^�b�t�o�^��ʁj_�w���v�Ƌx�e�̏d���s�Ή�
    /**
     * getSelectSQLCompareRecess
     * @param staff_id, schedule_date
     * @return String SQL
     */
    private String getSelectSQLCompareRecess(int staff_id, Date schedule_date) {
        return "select *\n"
                + "from data_recess\n"
                + "where  delete_date is null\n"
                + "and    staff_id      = " + SQLUtil.convertForSQL(staff_id) + "\n"
                + "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(schedule_date) + "\n";
    }
    
    /**
     * @param staff_id
     * @param schedule_date
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return ArrayList
     */
    public ArrayList loadCompareRecess(int staff_id, Date schedule_date) {
        
        ArrayList<DataRecess> list = new ArrayList<DataRecess>();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            if (con != null) {
                ResultSetWrapper rs = con.executeQuery(this.getSelectSQLCompareRecess(staff_id, schedule_date));
                DataRecess dtRecess = null;
                while (rs.next()) {
                    dtRecess = new DataRecess();
                    dtRecess.setStaffId(rs.getInt("staff_id"));
                    dtRecess.setScheduleDate(rs.getDate("schedule_date"));
                    dtRecess.setStartTime(rs.getString("start_time"));
                    dtRecess.setEndTime(rs.getString("end_time"));
                   list.add(dtRecess);
                }
                con.close();
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return list;
    }
    //END REGION IVS_PTQUANG New request #57851 [gb]���񂴂�API�p_�@�\�ǉ��i�w���v�X�^�b�t�o�^��ʁj_�w���v�Ƌx�e�̏d���s�Ή�
}
