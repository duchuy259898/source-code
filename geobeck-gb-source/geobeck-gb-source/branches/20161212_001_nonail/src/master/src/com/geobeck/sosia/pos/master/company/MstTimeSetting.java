/*
 * MstTimeSetting.java
 *
 * Created on 2010/07/15, 11:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author geobeck
 */
public class MstTimeSetting {
    
    private Integer shopId;     /* �X��ID       key */
    private Integer timeId;    /* �V�t�gID     key */
    private String timeName;   /* �V�t�g��         */
    private String startTime;   /* �J�n����         */
    private String endTime;     /* �I������         */
    
    public MstTimeSetting() {
    }
    
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(timeName);
        
        return sb.toString();
    }
    
    public boolean equals(Object o) {
        if(o != null && o instanceof MstTimeSetting) {
            MstTimeSetting time = (MstTimeSetting)o;
            if( time.getShopId() == this.getShopId() &&
                    time.getTimeId() == this.getTimeId() ) {
                
                return	true;
            } else {
                return	false;
            }
        } else {
            return	false;
        }
    }
    
    // �w�肵�����Ԃ��A�Ζ����ԓ����𒲍�
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
             (Integer.parseInt(time) <= Integer.parseInt(endTime)) ){
            return true;
        }
        return false;
    }
    
    
    public Integer getShopId() {
        return shopId;
    }
    
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
    
    public Integer getTimeId() {
        return timeId;
    }
    
    public void setTimeId(Integer timeId) {
        this.timeId = timeId;
    }
    
    public String getTimeName() {
        return timeName;
    }
    
    public void setTimeName(String timeName) {
        this.timeName = timeName;
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
    
    /**
     * �f�[�^���N���A����B
     */
    public void clear() {
        this.setShopId(null);
        this.setTimeId(null);
        this.setTimeName("");
        this.setStartTime("");
        this.setEndTime("");
    }
    
    /**
     * �V�t�g�}�X�^����A�ݒ肳��Ă���X��ID�A�V�t�gID�̃f�[�^��ǂݍ��ށB
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if(this.getShopId() == null || this.getShopId() < 0)	return	true;
        if(this.getTimeId() == null || this.getTimeId() < 0)	return	true;
        
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
     * �V�t�g�}�X�^����A�ݒ肳��Ă���X��ID�A�V�t�g���̃f�[�^��ǂݍ��ށB
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean loadByTimeName(ConnectionWrapper con) throws SQLException {
        if(this.getShopId() == null || this.getShopId() < 0)	return	true;
        if(this.getTimeName() == null || this.getTimeName().length() <= 0)	return	true;
        
        if(con == null)	return	false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectByTimeNameSQL());
        
        if(rs.next()) {
            this.setData(rs);
        } else {
            return false;
        }
        
        return	true;
    }
    
    /**
     * �V�t�g�}�X�^����A�ݒ肳��Ă���X��ID�A�V�t�g���̃f�[�^��ǂݍ��� �Ή����Ă���V�t�gID ��Ԃ��B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return Integer �V�t�gID
     */
    public static Integer figureTimeIDByName(ConnectionWrapper con, Integer intShopID, String strTimeName) throws SQLException {
        if (intShopID == null  ||  intShopID < 0)	return	null;
        if (strTimeName == null  ||  strTimeName.length() <= 0)	return	null;
        
        if(con == null)	return	null;
        
        MstTimeSetting time = new MstTimeSetting();
        time.setShopId(intShopID);
        time.setTimeName(strTimeName);
        ResultSetWrapper rs = con.executeQuery(time.getSelectByTimeNameSQL());
        
        if(rs.next()) {
            return (Integer)rs.getObject("time_id");
        } else {
            return null;
        }
    }
    
    /**
     * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setShopId(rs.getInt("shop_id"));
        this.setTimeId(rs.getInt("time_id"));
        this.setTimeName(rs.getString("time_name"));
        this.setStartTime(rs.getString("start_time"));
        this.setEndTime(rs.getString("end_time"));
    }
    
    
    /**
     * �V�t�g�}�X�^�Ƀf�[�^��o�^����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        if(isExists(con)) {
            return (con.executeUpdate(this.getUpdateSQL()) == 1);
        } else {
            if(con.executeUpdate(this.getInsertSQL()) != 1) {
                return false;
            }
            return true;
        }
    }
    
    
    /**
     * �V�t�g�}�X�^����f�[�^���폜����B�i�_���폜�j
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
    
    /**
     * �V�t�g�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if(this.getShopId() == null || this.getShopId() < 0)	return	false;
        if(this.getTimeId() == null || this.getTimeId() < 0)	return	false;
        
        if(con == null)	return	false;
        
        ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
        
        if(rs.next())	return	true;
        else	return	false;
    }
    
    /**
     * �u�x�v�̃C���X�^���X���쐬���ĕԂ�
     * @param Integer intShopID
     * @return Recess Instance
     */

    public static MstTimeSetting createRecessTimeInstance(Integer intShopID)
    {
        MstTimeSetting timeBreak = new MstTimeSetting();

        timeBreak.setShopId(intShopID);
        timeBreak.setTimeId(0);
        timeBreak.setTimeName("��");
        timeBreak.setStartTime("");
        timeBreak.setEndTime("");
        
        return timeBreak;
    }

    /**
     * Select�����擾����B
     * @return Select��
     */
    private String getSelectSQL() {
        return	"select *\n" +
                "from mst_time_setting\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    time_id = " + SQLUtil.convertForSQL(this.getTimeId()) + "\n" +
                "order by	time_id\n";
    }
    
    /**
     * �V�t�g������� Select�����擾����B
     * @return Select��
     */
    private String getSelectByTimeNameSQL() {
        return	"select *\n" +
                "from mst_time_setting\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    time_name = " + SQLUtil.convertForSQL(this.getTimeName()) + "\n" +
                "order by	time_id\n";
    }
    
    /**
     * Insert�����擾����B
     * @return Insert��
     */
    private String	getInsertSQL() {
        return	"insert into mst_time_setting\n" +
                "(shop_id, time_id, time_name,\n" +
                "start_time, end_time,\n" +
                "insert_date, update_date, delete_date)\n" +
                "select\n" +
                SQLUtil.convertForSQL(this.getShopId()) + ",\n" +
                "coalesce(max(time_id), 0) + 1,\n" +
                SQLUtil.convertForSQL(this.getTimeName()) + ",\n" +
                SQLUtil.convertForSQL(this.getStartTime()) + ",\n" +
                SQLUtil.convertForSQL(this.getEndTime()) + ",\n" +
                "current_timestamp, current_timestamp, null\n" +
                "from mst_time_setting\n" +
                "where	shop_id = " + SQLUtil.convertForSQL(this.getShopId());
    }
    
    /**
     * Update�����擾����B
     * @return Update��
     */
    private String	getUpdateSQL() {
        return	"update mst_time_setting\n" +
                "set\n" +
                "time_name = " + SQLUtil.convertForSQL(this.getTimeName()) + ",\n" +
                "start_time = " + SQLUtil.convertForSQL(this.getStartTime()) + ",\n" +
                "end_time   = " + SQLUtil.convertForSQL(this.getEndTime()) + ",\n" +
                "update_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    time_id = " + SQLUtil.convertForSQL(this.getTimeId()) + "\n";
    }
    
    /**
     * �폜�pUpdate�����擾����B
     * @return �폜�pUpdate��
     */
    private String	getDeleteSQL() {
        return	"update mst_time_setting\n" +
                "set\n" +
                "update_date = current_timestamp,\n" +
                "delete_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    time_id = " + SQLUtil.convertForSQL(this.getTimeId()) + "\n";
    }
    
 
}
