/*
 * MstManager.java
 *
 * Created on 2007/12/13, 15:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;



/**
 * �Ǘ��҃}�X�^�f�[�^
 * @author uchiyama
 */
public class MstManager {
    /**
     * �Ǘ��҂h�c
     */
    private	Integer			managerID			=	null;
    /**
     * �V���b�v�h�c
     */
    private	Integer			shopID			=	null;
    /**
     * �Ǘ��Җ�
     */
    private     String[]                managerName             =       {"", ""};
    /**
     * ���[���A�h���X
     */
    private     String                  mailAddress              =       "";
    
    /** Creates a new instance of MstManager */
    
    /**
     * �R���X�g���N�^
     */
    public MstManager() {
    }
    public MstManager(MstManager mm) {
        this.setData(mm);
    }
    
    /**
     * �R���X�g���N�^
     * @param managerID �Ǘ��҂h�c
     */
    public MstManager(Integer managerID) {
        this.setManagerID(managerID);
    }
    
    /**
     * ������ɕϊ�����B�i�Ǘ��Җ��j
     * @return �Ǘ��Җ�
     */
    public String toString() {
        return	this.getManagerName(0) + "�@" + this.getManagerName(1);
    }
    
    /**
     * �Ǘ��҂h�c���擾����B
     * @return �Ǘ��҂h�c
     */
    public Integer getManagerID() {
        return managerID;
    }
    
    /**
     * �Ǘ��҂h�c���Z�b�g����B
     * @param managerID �Ǘ��҂h�c
     */
    public void setManagerID(Integer managerID) {
        this.managerID = managerID;
    }
    
    /**
     * �Ǘ��Җ����擾����B
     * @return �Ǘ��Җ�
     */
    public String[] getManagerName() {
        return managerName;
    }
    
    /**
     * �Ǘ��Җ����擾����B
     * @param index �C���f�b�N�X
     * @return �Ǘ��Җ�
     */
    public String getManagerName(int index) {
        return managerName[index];
    }
    
    /**
     * �Ǘ��Җ����Z�b�g����B
     * @param staffName �Ǘ��Җ�
     */
    public void setManagerName(String[] managerName) {
        this.managerName = managerName;
    }
    
    /**
     * �Ǘ��Җ����Z�b�g����B
     * @param index �C���f�b�N�X
     * @param staffName �Ǘ��Җ�
     */
    public void setManagerName(int index, String managerName) {
        this.managerName[index] = managerName;
    }
    
    /**
     * �Ǘ��҂̃t���l�[�����擾����B
     * @return �t���l�[��
     */
    public String getFullManagerName() {
        return	(managerName[0] == null ? "" : managerName[0])
        +	(managerName[1] == null || managerName[1].equals("") ? "" : "�@" + managerName[1]);
    }
    
    /**
     * ���[���A�h���X���擾����B
     * @return ���[���A�h���X
     */
    public String getMailAddress() {
        return mailAddress;
    }
    
    /**
     * ���[���A�h���X���Z�b�g����B
     * @param mailAddress ���[���A�h���X
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    
    /**
     * �ݐГX�܂h�c���Z�b�g����B
     * @param shopID �ݐГX�܂h�c
     */
    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
    
    /**
     * �ݐГX�܂h�c���擾����B
     * @return �ݐГX�܂h�c
     */
    public Integer getShopID() {
        return this.shopID;
    }
    
    /**
     * �f�[�^���N���A����B
     */
    public void clear() {
        this.setManagerID(null);
        this.setManagerName(0, "");
        this.setManagerName(1, "");
        this.setMailAddress("");
        this.setShopID(null);
    }
    
    /**
     * �Ǘ��҃}�X�^����A�ݒ肳��Ă���Ǘ���ID�̃f�[�^��ǂݍ��ށB
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if(con == null)	return	false;
        
        ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setData(rs);
        }
        
        return	true;
    }
    
    /**
     * MstManager�̃f�[�^��ǂݍ��ށB
     * @param manager MstManager
     */
    public void setData( MstManager manager ) {
        this.setManagerID(manager.getManagerID() );
        this.setManagerName(0, manager.getManagerName(0) );
        this.setManagerName(1, manager.getManagerName(1) );
        this.setMailAddress(manager.getMailAddress() );
        this.setShopID(manager.getShopID() );
    }
    
    /**
     * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setManagerID(rs.getInt("manager_id"));
        this.setManagerName(0, rs.getString("manager_name1"));
        this.setManagerName(1, rs.getString("manager_name2"));
        this.setMailAddress(rs.getString("mail_address"));
        this.setShopID(rs.getInt("shop_id"));
    }
    
    /**
     * �Ǘ��҃}�X�^�Ƀf�[�^��o�^����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        
        String sql = "";
        
        if (isExists(con)) {
            sql	= this.getUpdateSQL();
        } else {
            sql	= this.getInsertSQL();
        }
        
        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * �Ǘ��҃}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        
        if (this.getManagerID() == null) return false;
        
        if (con == null) return false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        return rs.next();
    }
    
    /**
     * Select�����擾����B
     * @return Select��
     */
    private String getSelectSQL() {
        return	"select *\n" +
                "from mst_manager\n" +
                "where	manager_id = " + SQLUtil.convertForSQL(this.getManagerID()) + "\n";
    }
    /**
     * Insert�����擾����B
     * @return Insert��
     */
    private String	getInsertSQL() {
        return	"insert into mst_manager\n" +
                "(manager_id, shop_id, manager_name1, manager_name2, mail_address,\n" +
                "insert_date, update_date, delete_date)\n" +
                "select\n" +
//---- 2013/04/24 GB MOD START
//                "coalesce(max(mst_manager.manager_id), 0) + 1,\n" +
                "coalesce(max(mm.manager_id), '0') + 1,\n" +
//---- 2013/04/24 GB MOD END
                SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
                SQLUtil.convertForSQL(this.getManagerName(0)) + ",\n" +
                SQLUtil.convertForSQL(this.getManagerName(1)) + ",\n" +
                SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +  
                "current_timestamp, current_timestamp, null\n" +
                "from mst_manager mm";
    }
    
    /**
     * Update�����擾����B
     * @return Update��
     */
    private String	getUpdateSQL() {
        return	"update mst_manager\n" +
                "set\n" +
                "manager_name1 = " + SQLUtil.convertForSQL(this.getManagerName(0)) + ",\n" +
                "manager_name2 = " + SQLUtil.convertForSQL(this.getManagerName(1)) + ",\n" +
                "mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
                "shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
                "update_date = current_timestamp\n" +
                "where	manager_id = " + SQLUtil.convertForSQL(this.getManagerID()) + "\n";
    }
    
    public boolean delete(ConnectionWrapper con) throws SQLException {
        
        String sql = "delete from mst_manager where manager_id = " + SQLUtil.convertForSQL(this.getManagerID());
        
        if(con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }
    
}
