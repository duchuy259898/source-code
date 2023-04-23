/*
 * MstTechnic.java
 *
 * Created on 2006/05/24, 11:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �Z�p�}�X�^�f�[�^
 *
 * @author katagiri
 */
public class MstTechnic {

    /**
     * �Z�p����
     */
    protected MstTechnicClass technicClass = new MstTechnicClass();
    /**
     * �Z�p�h�c
     */
    protected Integer technicID = null;
    /**
     * �Z�p�R�[�h
     */
    protected String technicNo = "";
    /**
     * �Z�p��
     */
    protected String technicName = "";
    /**
     * �P��
     */
    protected Long price = null;
    /**
     * �{�p����
     */
    protected Integer operationTime = null;
    /**
     * �\����
     */
    protected Integer displaySeq = null;
    protected Integer status = null;
    /**
     * ���o�C���\��p�Z�pNo(�ύX�s��)
     */
    protected String mobileTechnicNo = new String("mo-rsv-");
    protected Integer praiseTimeLimit = new Integer(0);
    protected Boolean isPraiseTime = false;
    protected Integer mobileFlag = 0;

    public Integer getMobileFlag() {
        return mobileFlag;
    }

    public void setMobileFlag(Integer mobileFlag) {
        this.mobileFlag = mobileFlag;
    }

   

    /**
     * Creates a new instance of MstTechnic
     */
    public MstTechnic() {
    }

    /**
     * �Z�p���ނ��擾����B
     *
     * @return �Z�p����
     */
    public MstTechnicClass getTechnicClass() {
        return technicClass;
    }

    /**
     * �Z�p���ނ��Z�b�g����B
     *
     * @param technicClass �Z�p����
     */
    public void setTechnicClass(MstTechnicClass technicClass) {
        this.technicClass = technicClass;
    }

    /**
     * �Z�p�h�c���擾����B
     *
     * @return �Z�p�h�c
     */
    public Integer getTechnicID() {
        return technicID;
    }

    /**
     * �Z�p�h�c���Z�b�g����B
     *
     * @param technicID �Z�p�h�c
     */
    public void setTechnicID(Integer technicID) {
        this.technicID = technicID;
    }

    /**
     * �Z�p�R�[�h���擾����B
     *
     * @return �Z�p�R�[�h
     */
    public String getTechnicNo() {
        return technicNo;
    }

    /**
     * �Z�p�R�[�h���Z�b�g����B
     *
     * @param technicNo �Z�p�R�[�h
     */
    public void setTechnicNo(String technicNo) {
        this.technicNo = technicNo;
    }

    /**
     * �Z�p�����擾����B
     *
     * @return �Z�p��
     */
    public String getTechnicName() {
        return technicName;
    }

    /**
     * �Z�p�����Z�b�g����B
     *
     * @param technicName �Z�p��
     */
    public void setTechnicName(String technicName) {
        this.technicName = technicName;
    }

    /**
     * �P�����擾����B
     *
     * @return �P��
     */
    public Long getPrice() {
        return price;
    }

    /**
     * �P�����Z�b�g����B
     *
     * @param price �P��
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * �{�p���Ԃ��擾����B
     *
     * @return �{�p����
     */
    public Integer getOperationTime() {
        return operationTime;
    }

    /**
     * �{�p���Ԃ��Z�b�g����B
     *
     * @param operationTime �{�p����
     */
    public void setOperationTime(Integer operationTime) {
        this.operationTime = operationTime;
    }

    /**
     * �\�������擾����B
     *
     * @return �\����
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * �\�������Z�b�g����B
     *
     * @param displaySeq �\����
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String toString() {
        return this.getTechnicName();
    }

    /**
     * ���o�C���\��p�@�Z�pNo���Z�b�g����B
     */
    public void setMobileTechnicNo(String mobileTechnicNo) {
        this.mobileTechnicNo = mobileTechnicNo;
    }

    /**
     * ���o�C���\��p�@�Z�pNo���擾����B
     *
     * @param mobileTechnicNo
     */
    public String getMobileTechnicNo() {
        return mobileTechnicNo;
    }

    public void setPraiseTimeLimit(Integer praiseTime) {
        this.praiseTimeLimit = praiseTime;
    }

    public Integer getPraiseTimeLimit() {
        return this.praiseTimeLimit;
    }

    public void setPraiseTime(Boolean b) {
        this.isPraiseTime = b;
    }

    public Boolean isPraiseTime() {
        return this.isPraiseTime;
    }

    /**
     * �Z�p�}�X�^�f�[�^����f�[�^���Z�b�g����B
     *
     * @param mt �Z�p�}�X�^�f�[�^
     */
    public void setData(MstTechnic mt) {
        this.setTechnicClass(new MstTechnicClass(mt.getTechnicClass().getTechnicClassID()));
        this.setTechnicID(mt.getTechnicID());
        this.setTechnicNo(mt.getTechnicNo());
        this.setTechnicName(mt.getTechnicName());
        this.setPrice(mt.getPrice());
        this.setOperationTime(mt.getOperationTime());
        this.setDisplaySeq(mt.getDisplaySeq());
        this.setPraiseTimeLimit(mt.getPraiseTimeLimit());
        this.setPraiseTime(mt.isPraiseTime());
        //this.setStatus(mt.getStatus());
        this.setMobileFlag(mt.getMobileFlag());
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setTechnicClass(new MstTechnicClass(rs.getInt("technic_class_id")));
        this.setTechnicID(rs.getInt("technic_id"));
        this.setTechnicNo(rs.getString("technic_no"));
        this.setTechnicName(rs.getString("technic_name"));
        this.setPrice(rs.getLong("price"));
        this.setOperationTime(rs.getInt("operation_time"));
        this.setDisplaySeq(rs.getInt("display_seq"));
        this.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        this.setPraiseTime(rs.getBoolean("is_praise_time"));
        this.setMobileFlag(rs.getInt("mobile_flag"));
        
        //this.setStatus(rs.getInt("status"));
    }

    /**
     * �Z�p�}�X�^�Ƀf�[�^��o�^����B
     *
     * @return true - ����
     * @param lastSeq �\�����̍ő�l
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean regist(ConnectionWrapper con,
            Integer lastSeq) throws SQLException {
        if (isExists(con)) {
            if (lastSeq != this.getDisplaySeq()) {
                if (0 < lastSeq) {
                    if (con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0) {
                        return false;
                    }
                }

                if (0 <= this.getDisplaySeq()) {
                    if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }

            if (con.executeUpdate(this.getUpdateSQL()) != 1) {
                return false;
            }
        } else {
            if (0 <= this.getDisplaySeq()) {
                if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }

            if (con.executeUpdate(this.getInsertSQL()) != 1) {
                return false;
            }

            this.setMaxTechnicID(con);
        }

        return true;
    }

    private void setMaxTechnicID(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getMaxTechnicIDSQL());

        if (rs.next()) {
            
            this.setTechnicID(rs.getInt("max_id"));
        }

        rs.close();
    }

    /**
     * �Z�p�}�X�^����f�[�^���폜����B�i�_���폜�j
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getDeleteSQL();
        } else {
            return false;
        }

        if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0) {
            return false;
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �Z�p�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicID() == null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
    
    //IVS_LVTu start add 2016/05/27 New request #50223
    public boolean loadByTechnicID(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicID()== null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            this.setData(rs);
            rs.close();
            return true;
        }

        rs.close();
        return false;
    }
    //IVS_LVTu end add 2016/05/27 New request #50223

    /**
     * �Z�p�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean loadByTechnicNo(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicNo() == null || this.getTechnicNo().equals("")) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectByTechnicNoSQL());

        if (rs.next()) {
            this.setData(rs);
            rs.close();
            return true;
        }

        rs.close();
        return false;
    }

    /**
     * �Z�p�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExistsByTechnicNo(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicNo() == null || this.getTechnicNo().equals("")) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectByTechnicNoSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Select�����擾����B
     *
     * @return Select��
     */
    private String getSelectSQL() {
        return "select *\n"
                + "from mst_technic\n"
                + "where	technic_id = " + SQLUtil.convertForSQL(this.getTechnicID()) + "\n";
    }

    /**
     * Select�����擾����B
     *
     * @return Select��
     */
    private String getSelectByTechnicNoSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      mtc.technic_class_name");
        sql.append("     ,mt.*");
        sql.append(" from");
        sql.append("     mst_technic mt");
        sql.append("         join mst_technic_class mtc");
        sql.append("         using (technic_class_id)");
        sql.append(" where");
        sql.append("         mt.delete_date is null");
        sql.append("     and mtc.delete_date is null");
        sql.append("     and technic_no = " + SQLUtil.convertForSQL(this.getTechnicNo()));

        return sql.toString();
    }

    /**
     * �\���������炷�r�p�k�����擾����
     *
     * @param seq ���̕\����
     * @param isIncrement true - ���Z�Afalse - ���Z
     * @return �\���������炷�r�p�k��
     */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_technic");
        sql.append(" set");
        sql.append("     display_seq = display_seq " + (isIncrement ? "+" : "-") + " 1");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()));
        sql.append("     and display_seq >= " + SQLUtil.convertForSQL(seq));
        sql.append("     and display_seq " + (isIncrement ? "+" : "-") + " 1 >= 0");

        if (!isIncrement) {
            sql.append("     and not exists");
            sql.append("            (");
            sql.append("                 select 1");
            sql.append("                 from");
            sql.append("                     (");
            sql.append("                         select");
            sql.append("                             count(*) as cnt");
            sql.append("                         from");
            sql.append("                             mst_technic");
            sql.append("                         where");
            sql.append("                                 technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()));
            sql.append("                             and delete_date is null");
            sql.append("                         group by");
            sql.append("                             display_seq");
            sql.append("                     ) t");
            sql.append("                 where");
            sql.append("                     cnt > 1");
            sql.append("            )");
        }

        return sql.toString();
    }

    /**
     * Insert�����擾����B
     *
     * @return Insert��
     */
    private String getInsertSQL() {
        return "insert into mst_technic\n"
                + "(technic_class_id, technic_id, technic_no, technic_name,\n"
                + "price, operation_time, display_seq, praise_time_limit, is_praise_time, \n"
                + "mobile_flag,insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + ",\n"
                + "(select coalesce(max(technic_id), 0) + 1\n"
                //IVS_LVTu start edit 2016/12/13 New request #58893
                + "from mst_technic\n"
                + "where technic_id > 0),\n"
                //IVS_LVTu end edit 2016/12/13 New request #58893
                + SQLUtil.convertForSQL(this.getTechnicNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getTechnicName()) + ",\n"
                + SQLUtil.convertForSQL(this.getPrice()) + ",\n"
                + SQLUtil.convertForSQL(this.getOperationTime()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "), 0) + 1 end,\n"
                + SQLUtil.convertForSQL(this.getPraiseTimeLimit()) + ",\n"
                + SQLUtil.convertForSQL(this.isPraiseTime()) + ",\n"
                + SQLUtil.convertForSQL(this.getMobileFlag()) + ",\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update�����擾����B
     *
     * @return Update��
     */
    private String getUpdateSQL() {
        return "update mst_technic\n"
                + "set\n"
                + "technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + ",\n"
                + "technic_no = " + SQLUtil.convertForSQL(this.getTechnicNo()) + ",\n"
                + "technic_name = " + SQLUtil.convertForSQL(this.getTechnicName()) + ",\n"
                + "price = " + SQLUtil.convertForSQL(this.getPrice()) + ",\n"
                + "operation_time = " + SQLUtil.convertForSQL(this.getOperationTime()) + ",\n"
                + "praise_time_limit = " + SQLUtil.convertForSQL(this.getPraiseTimeLimit()) + ",\n"
                + "is_praise_time = " + SQLUtil.convertForSQL(this.isPraiseTime()) + ",\n"
                + "mobile_flag = " + SQLUtil.convertForSQL(this.getMobileFlag()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "\n"
                + "and technic_id != "
                + SQLUtil.convertForSQL(this.getTechnicID()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "\n"
                + "and technic_id != "
                + SQLUtil.convertForSQL(this.getTechnicID()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp\n"
                + "where	technic_id = " + SQLUtil.convertForSQL(this.getTechnicID()) + "\n";
    }

    /**
     * �폜�pUpdate�����擾����B
     *
     * @return �폜�pUpdate��
     */
    private String getDeleteSQL() {
        return "update mst_technic\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	technic_id = " + SQLUtil.convertForSQL(this.getTechnicID()) + "\n";
    }

    private static String getMaxTechnicIDSQL() {
        return "select max(technic_id) as max_id\n"
                + "from mst_technic\n";
    }

    //�}�X�^�ꊇ�o�^ add start 2017/01/07
    /**
     * �ꊇ�o�^�p�̓o�^���������s
     * @param con �R�l�N�V����
     * @return true:���� / false:�ُ�
     * @throws SQLException 
     */
    public boolean registForBulk(ConnectionWrapper con) throws SQLException {
        
        if (con.executeUpdate(this.getInsertSqlForBulk()) != 1) {
            return false;
        }

        //�Z�pID�������̔Ԃ̂��ߎ擾���ĕێ�
        this.setMaxTechnicID(con);

        return true;
    }

    /**
     * �ꊇ�o�^�pSQL��Ԃ�
     * @return SQL��
     */
    private String getInsertSqlForBulk() {
        
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into mst_technic\n");
        sql.append("(\n");
        sql.append("technic_class_id,\n");
        sql.append("technic_id,\n");
        sql.append("technic_no,\n");
        sql.append("technic_name,\n");
        sql.append("price,\n");
        sql.append("operation_time,\n");
        sql.append("display_seq,\n");
        sql.append("insert_date,\n");
        sql.append("update_date,\n");
        sql.append("delete_date,\n");
        sql.append("praise_time_limit,\n");
        sql.append("is_praise_time,\n");
        sql.append("sex_flg,\n");
        sql.append("credit_flg,\n");
        sql.append("mobile_flag \n");
        sql.append(") values(\n");
        sql.append(SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID())).append(",\n");
        sql.append("(select coalesce(max(technic_id), 0) + 1 from mst_technic), \n");
        sql.append(SQLUtil.convertForSQL(this.getTechnicNo())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getTechnicName())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getPrice())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getOperationTime())).append(",\n");
        sql.append("(select coalesce(max(display_seq), 0) + 1 from mst_technic where technic_class_id =");
        sql.append(SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID())).append(" and delete_date is null), \n");
        sql.append("current_timestamp,\n");
        sql.append("current_timestamp,\n");
        sql.append("null,\n");
        sql.append(SQLUtil.convertForSQL(this.getPraiseTimeLimit())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.isPraiseTime())).append(",\n");
        sql.append("0,\n");
        sql.append(SQLUtil.convertForSQL(Boolean.TRUE)).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getMobileFlag())).append(" \n");
        sql.append(")");

        return sql.toString();
    }
    //�}�X�^�ꊇ�o�^ add end 2017/01/07
}
