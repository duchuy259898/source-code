/*
 * DataCustomerMemo.java
 *
 * �ڋq�����@�\�Ή�
 * Created on 2016/12/25
 */

package com.geobeck.sosia.pos.hair.customer;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.util.DateUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * DataCustomerMemo
 * data_customer_memo�e�[�u���ւ̃A�N�Z�X�A�擾���ʕێ����s���B
 */
public class DataCustomerMemo {
    /**
     * �]���F�ǂ�
     */
    public static final int EVALUATION_GOOD = 1;
    /**
     * �]���F����
     */
    public static final int EVALUATION_BAD = 2;
    /**
     * �]���F�o�g��
     */
    public static final int EVALUATION_BATON = 0;
    
    /**
     * �]��������
     */
    private static final List<String> EVALUATION_STR = Arrays.asList("�o�g��", "�ǂ�", "����");
    
    /**
     * ����ID�B�v���C�}���[�L�[�B
     */
    protected Integer memoId = null;
    
    /**
     * �ڋqID
     */
    protected Integer customerId = null;
    
    /**
     * ��ʏ�Őݒ肵���o�^��
     */
    protected java.util.Date memoDate = null;
    
    /**
     * ��ʏ�őI�������X�܂̓X��ID
     */
    protected Integer shopId = null;
    
    /**
     * ��ʏ�őI�������X�^�b�t�̃X�^�b�tID
    */
    protected Integer staffId = null;
    
    /**
     * ��ʏ�őI�������]���i1�F�ǂ��A2�F�����j
     */
    protected Integer evaluation = null;
    
    /**
     * �������e
     */
    protected String memo = null;
    
    /**
     * �o�^���i�f�[�^�}�������j
     */
    protected Date insertDate = null;
    
    /**
     * �X�V���i�f�[�^�X�V�����j
     */
    protected Date updateDate = null;
    
    /**
     * �폜���i�f�[�^�폜�����j
     */
    protected Date deleteDate = null;
    
    /**
     * �����f�[�^�ɓo�^����Ă���X��
     */
    protected MstShop shop = null;

    /**
     * �����f�[�^�ɓo�^����Ă���S����
     */
    protected MstStaff staff = null;
    
    /**
     * �]���ɑΉ����镶�����Ԃ��B
     * @param evaluation ��������擾����]���l
     * @return �]���ɑΉ����镶����
     */
    public static String getEvaluationString(int evaluation) {
        if(evaluation >= EVALUATION_STR.size()) {
            return null;
        }
        return EVALUATION_STR.get(evaluation);
    }
    
    public DataCustomerMemo() {
        
    }
    
    public Integer getMemoId() {
        return memoId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Date getMemoDate() {
        return memoDate;
    }

    public Integer getShopId() {
        return shopId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public String getMemo() {
        return memo;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setMemoId(Integer memoId) {
        this.memoId = memoId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setMemoDate(Date memoDate) {
        this.memoDate = memoDate;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
    
    public MstShop getShop() {
        return shop;
    }

    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    public MstStaff getStaff() {
        return staff;
    }

    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }
    
    /**
    * ResultSet����f�[�^���Z�b�g����B
    * @param rs ResultSet
    * @throws java.sql.SQLException SQLException
    */
   public void setData(ResultSet rs) throws SQLException {
       this.setMemoId(rs.getInt("memo_id"));
       this.setCustomerId(rs.getInt("customer_id"));
       this.setMemoDate(rs.getDate("memo_date"));
       this.setShopId(rs.getInt("shop_id"));
       this.setStaffId(rs.getInt("staff_id"));
       this.setEvaluation(rs.getInt("evaluation"));
       this.setMemo(rs.getString("memo"));
       this.setInsertDate(rs.getDate("insert_date"));
       this.setUpdateDate(rs.getDate("update_date"));
       this.setDeleteDate(rs.getDate("delete_date"));
   }
   
   /**
    * �o�^�������s���B
    * @param con ConnectionWrapper
    * @return true - ����
    * @throws java.sql.SQLException SQLException
    */
   public boolean regist(ConnectionWrapper con) throws SQLException {
        boolean isExists = isExists(con);
        try(PreparedStatement ps = isExists ? this.getUpdatePreparedStatement(con) :
                                              this.getInsertPreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * �}���������s���B
    * @param con ConnectionWrapper
    * @return true - ����
    * @throws java.sql.SQLException SQLException
    */
   public boolean insert(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getInsertPreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * �}���������s���Bmemo_id�͎��O�ɐݒ肵�Ă����B
    * @param con ConnectionWrapper
    * @return true - ����
    * @throws java.sql.SQLException SQLException
    */
   public boolean insertWithMemoId(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getInsertWithMemoIdPreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * �X�V�������s���B
    * @param con ConnectionWrapper
    * @return true - ����
    * @throws java.sql.SQLException SQLException
    */
   public boolean update(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getUpdatePreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * �폜�������s���B
    * @param con ConnectionWrapper
    * @return true - ����
    * @throws java.sql.SQLException SQLException
    */
   public boolean delete(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getDeletePreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * �����f�[�^�����݂��邩���擾����B
    * @param con ConnectionWrapper
    * @return true - ���݂���
    * @throws java.sql.SQLException SQLException
    */
   public boolean isExists(ConnectionWrapper con) throws SQLException {
        if(this.memoId == null || this.memoId < 1) {
            return false;
        }

        if(con == null) {
            return false;
        }

        try(PreparedStatement ps = this.getSelectCountPreparedStatement(con);
            ResultSet rs = ps.executeQuery();) {

            if(rs.next()) {
                if(rs.getInt(1) > 0) {
                    return true;
                }
            }
        }
        return false;
   }
   
   /**
    * �V��������ID�V�[�P���X�ԍ����擾����B
    * @param con ConnectionWrapper
    * @return �V�����V�[�P���X�ԍ��B�擾�ł��Ȃ������ꍇ��-1�B
    * @throws java.sql.SQLException SQLException
    */
   public int selectNewMemoIdSeq(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getSelectMemoIdSeqPreparedStatement(con);
            ResultSet rs = ps.executeQuery();) {
            if(rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
   }
   
   /**
    * customerId�ɕR�Â������f�[�^�̃��X�g���擾����B
    * @param con ConnectionWrapper
    * @return List �ڋqID�ɕR�Â������f�[�^�̃��X�g
    * @throws java.sql.SQLException SQLException
    */
   public List<DataCustomerMemo> selectByCustomerId(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getSelectByCustomerIdPreparedStatement(con);
            ResultSet rs = ps.executeQuery();) {
            
            List<DataCustomerMemo> list = new ArrayList<>();
            while(rs.next()) {
                DataCustomerMemo memoData = new DataCustomerMemo();
                memoData.setData(rs);
                list.add(memoData);
            }
            return list;
        }
   }
   
   /**
    * customerId�ɕR�Â������f�[�^�Ƃ��̃����ɓo�^����Ă���X�܃f�[�^�A�S���҃f�[�^�̃��X�g���擾����B
    * @param con ConnectionWrapper
    * @return List �ڋqID�ɕR�Â������f�[�^�̃��X�g
    * @throws java.sql.SQLException SQLException
    */
   public List<DataCustomerMemo> selectMemoDataWithShopDataAndStaffDataByCustomerId(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getSelectMemoDataWithShopDataAndStaffDataByCustomerIdPreparedStatement(con);
            ResultSet rs = ps.executeQuery();) {
            
            List<DataCustomerMemo> list = new ArrayList<>();
            while(rs.next()) {
                DataCustomerMemo memoData = new DataCustomerMemo();
                memoData.setData(rs);
                
                MstShop shopData = new MstShop();
                shopData.setData(rs);
                memoData.setShop(shopData);
                
                MstStaff staffData = new MstStaff();
                staffData.setData(rs);
                memoData.setStaff(staffData);
                
                list.add(memoData);
            }
            return list;
        }
   }
   
    /**
    * memoID�ɕR�Â������f�[�^���擾����B
    * @param con ConnectionWrapper
    * @return DataCustomerMemo ����ID�ɕR�Â������f�[�^�B������Ȃ������ꍇ��null
    * @throws java.sql.SQLException SQLException
    */
   public DataCustomerMemo selectByMemoId(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getSelectByCustomerIdPreparedStatement(con);
            ResultSet rs = ps.executeQuery();) {
            if(rs.next()) {
                DataCustomerMemo memoData = new DataCustomerMemo();
                memoData.setData(rs);
                return memoData;
            }
        }
        return null;
   }
   
   /**
    * memoID�ɕR�Â������f�[�^���擾����B
    * @param con ConnectionWrapper
    * @return DataCustomerMemo ����ID�ɕR�Â������f�[�^�B������Ȃ������ꍇ��null
    * @throws java.sql.SQLException SQLException
    */
   public DataCustomerMemo selectMemoDataWithShopDataAndStaffDataByMemoId(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getSelectMemoDataWithShopDataAndStaffDataPreparedStatement(con);
            ResultSet rs = ps.executeQuery();) {
            if(rs.next()) {
                DataCustomerMemo memoData = new DataCustomerMemo();
                memoData.setData(rs);
                
                MstShop shopData = new MstShop();
                shopData.setData(rs);
                memoData.setShop(shopData);
                
                MstStaff staffData = new MstStaff();
                staffData.setData(rs);
                memoData.setStaff(staffData);
                
                return memoData;
            }
        }
        return null;
   }
   
   /**
     * �V��������ID�V�[�P���X�ԍ����擾����r�p�k�����擾����B
     * @return �V��������ID�V�[�P���X�ԍ����擾����r�p�k��
     */
    protected String getSelectNewMemoIdSeqSQL() {
        return	"SELECT NEXTVAL('data_customer_memo_id_seq') ";
    }
    
    /**
     * getSelectNewMemoIdSeqSQL()��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return getSelectNewMemoIdSeqSQL()��PreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectMemoIdSeqPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectNewMemoIdSeqSQL());
        
        return ps;
    }
           
    /**
     * memoId�ɕR�Â������f�[�^�����擾����r�p�k�����擾����B
     * @return �����f�[�^���擾����r�p�k��
     */
    protected String getSelectCountSQL() {
        return	"SELECT COUNT(*) AS COUNT " 
              + "FROM data_customer_memo dcm " 
              + "WHERE dcm.memo_id = ?";
    }
    
    /**
     * getSelectCountSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return getSelectCountSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectCountPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectCountSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
    
    /**
     * memoId�ɕR�Â������f�[�^���擾����r�p�k�����擾����B
     * @return �����f�[�^���擾����r�p�k��
     */
    protected String getSelectUniqueSQL() {
        return "SELECT "  
             + "  dcm.memo_id, " 
             + "  dcm.customer_id, " 
             + "  dcm.memo_date, " 
             + "  dcm.shop_id, " 
             + "  dcm.staff_id, " 
             + "  dcm.evaluation, " 
             + "  dcm.memo, " 
             + "  dcm.insert_date, " 
             + "  dcm.update_date, " 
             + "  dcm.delete_date " 
             + "FROM data_customer_memo dcm " 
             + "WHERE dcm.memo_id = ?";
    }
    
    /**
     * getSelectUniqueSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return getSelectUniqueSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectUniquePreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectUniqueSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
    
        /**
     * memoId�ɕR�Â������f�[�^���擾����r�p�k�����擾����B
     * @return �����f�[�^���擾����r�p�k��
     */
    protected String getSelectUniqueMemoDataWithShopDataAndStaffDataSQL() {
        return "SELECT "  
             + "  dcm.memo_id, " 
             + "  dcm.customer_id, " 
             + "  dcm.memo_date, " 
             + "  dcm.shop_id, " 
             + "  dcm.staff_id, " 
             + "  dcm.evaluation, " 
             + "  dcm.memo, " 
             + "  dcm.insert_date, " 
             + "  dcm.update_date, " 
             + "  dcm.delete_date, " 
             + "  shop.*, "
             + "  staff.* "
             + "FROM data_customer_memo dcm "
             + "  LEFT OUTER JOIN mst_shop shop ON dcm.shop_id = shop.shop_id "
             + "  LEFT OUTER JOIN mst_staff staff ON dcm.staff_id = staff.staff_id "
             + "WHERE dcm.memo_id = ?";
    }
    
    /**
     * getSelectUniqueSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return getSelectUniqueSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectMemoDataWithShopDataAndStaffDataPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectUniqueMemoDataWithShopDataAndStaffDataSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
    
    /**
     * ���폜��customerId�ɕR�Â������f�[�^���擾����r�p�k�����擾����B
     * @return ���폜�����f�[�^���擾����r�p�k��
     */
    protected String getSelectByCustomerIdSQL() {
        return "SELECT "
             + "  dcm.memo_id, "
             + "  dcm.customer_id, "
             + "  dcm.memo_date, "
             + "  dcm.shop_id, "
             + "  dcm.staff_id, "
             + "  dcm.evaluation, "
             + "  dcm.memo, "
             + "  dcm.insert_date, "
             + "  dcm.update_date, "
             + "  dcm.delete_date "
             + "FROM data_customer_memo dcm "
             + "WHERE dcm.customer_id = ? "
             + "  AND dcm.delete_date IS NULL "
             + "ORDER BY dcm.memo_date DESC, dcm.memo_id DESC";
    }
    
    /**
     * getSelectByCustomerIdSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return getSelectByCustomerIdSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectByCustomerIdPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectByCustomerIdSQL());
        ps.setInt(1, this.getCustomerId());
        
        return ps;
    }
    
    /**
     * ���폜��customerId�ɕR�Â������f�[�^�Ƃ��̃����f�[�^�ɓo�^����Ă���X�܁A�S���҃f�[�^���擾����r�p�k�����擾����B
     * @return ���폜�����f�[�^���擾����r�p�k��
     */
    protected String getSelectMemoDataWithShopDataAndStaffDataByCustomerIdSQL() {
        return "SELECT "
             + "  dcm.memo_id, "
             + "  dcm.customer_id, "
             + "  dcm.memo_date, "
             + "  dcm.shop_id, "
             + "  dcm.staff_id, "
             + "  dcm.evaluation, "
             + "  dcm.memo, "
             + "  dcm.insert_date, "
             + "  dcm.update_date, "
             + "  dcm.delete_date, "
             + "  shop.*, "
             + "  staff.* "
             + "FROM data_customer_memo dcm "
             + "  LEFT OUTER JOIN mst_shop shop ON dcm.shop_id = shop.shop_id "
             + "  LEFT OUTER JOIN mst_staff staff ON dcm.staff_id = staff.staff_id "
             + "WHERE dcm.customer_id = ? "
             + "  AND dcm.delete_date IS NULL "
             + "ORDER BY dcm.memo_date DESC, dcm.memo_id DESC";
    }
    
    /**
     * getSelectMemoDataWithShopDataAndStaffDataByCustomerIdSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return getSelectMemoDataWithShopDataAndStaffDataByCustomerIdSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectMemoDataWithShopDataAndStaffDataByCustomerIdPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectMemoDataWithShopDataAndStaffDataByCustomerIdSQL());
        ps.setInt(1, this.getCustomerId());
        
        return ps;
    }
    
    /**
     * memoId�ɕR�Â������f�[�^���X�V����r�p�k�����擾����B
     * @return �����f�[�^���X�V����r�p�k��
     */
    protected String getUpdateSQL() {
        return "UPDATE "
             + "data_customer_memo SET "
             + "  memo_id = ?, " 
             + "  customer_id = ?, "
             + "  memo_date = TO_DATE(?, 'yyyy/mm/dd'), "
             + "  shop_id = ?, "
             + "  staff_id = ?, "
             + "  evaluation = ?, "
             + "  memo = ?, "
             + "  update_date = CURRENT_TIMESTAMP " 
             + "WHERE memo_id = ?";
    }
    
    /**
     * getUpdateSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return �����f�[�^���X�V����r�p�k���ɒl��ݒ肵��Statement
     * @throws java.sql.SQLException
     */
    protected PreparedStatement getUpdatePreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getUpdateSQL());
        ps.setInt(1, this.getMemoId());
        ps.setInt(2, this.getCustomerId());
        ps.setString(3, DateUtil.format(this.getMemoDate(), "yyyy/MM/dd"));
        ps.setInt(4, this.getShopId());
        ps.setInt(5, this.getStaffId());
        ps.setInt(6, this.getEvaluation());
        ps.setString(7, this.getMemo());
        ps.setInt(8, this.getMemoId());
        
        return ps;
    }
    
    /**
     * �����f�[�^��}������r�p�k�����擾����B
     * @return �����f�[�^��}������r�p�k��
     */
    protected String getInsertSQL() {
        return "INSERT INTO data_customer_memo "
             + "( "
             + "  memo_id, "
             + "  customer_id, "
             + "  memo_date, "
             + "  shop_id, "
             + "  staff_id, "
             + "  evaluation, "
             + "  memo, "
             + "  insert_date, "
             + "  update_date, "
             + "  delete_date "
             + ") "
             + "VALUES ("
             + "  nextval('data_customer_memo_id_seq'), "
             + "  ?, "
             + "  TO_DATE(?, 'yyyy/mm/dd'), "
             + "  ?, "
             + "  ?, "
             + "  ?, "
             + "  ?, "
             + "  CURRENT_TIMESTAMP, "
             + "  CURRENT_TIMESTAMP, "
             + "  NULL "
             + ")";
    }
    
    /**
     * getInsertSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return �����f�[�^��}������r�p�k�ɒl��ݒ肵��Statement
     * @throws java.sql.SQLException
     */
    protected PreparedStatement getInsertPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getInsertSQL());
        ps.setInt(1, this.getCustomerId());
        ps.setString(2, DateUtil.format(this.getMemoDate(), "yyyy/MM/dd"));
        ps.setInt(3, this.getShopId());
        ps.setInt(4, this.getStaffId());
        ps.setInt(5, this.getEvaluation());
        ps.setString(6, this.getMemo());
        
        return ps;
    }
    
    /**
     * �����f�[�^��}������r�p�k�����擾����B
     * @return �����f�[�^��}������r�p�k��
     */
    protected String getInsertWithMemoIdSQL() {
        return "INSERT INTO data_customer_memo "
             + "( "
             + "  memo_id, "
             + "  customer_id, "
             + "  memo_date, "
             + "  shop_id, "
             + "  staff_id, "
             + "  evaluation, "
             + "  memo, "
             + "  insert_date, "
             + "  update_date, "
             + "  delete_date "
             + ") "
             + "VALUES ("
             + "  ?, "
             + "  ?, "
             + "  TO_DATE(?, 'yyyy/mm/dd'), "
             + "  ?, "
             + "  ?, "
             + "  ?, "
             + "  ?, "
             + "  CURRENT_TIMESTAMP, "
             + "  CURRENT_TIMESTAMP, "
             + "  NULL "
             + ")";
    }
    
    /**
     * getInsertWithMemoIdSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return �����f�[�^��}������r�p�k�ɒl��ݒ肵��Statement
     * @throws java.sql.SQLException
     */
    protected PreparedStatement getInsertWithMemoIdPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getInsertWithMemoIdSQL());
        ps.setInt(1, this.getMemoId());
        ps.setInt(2, this.getCustomerId());
        ps.setString(3, DateUtil.format(this.getMemoDate(), "yyyy/MM/dd"));
        ps.setInt(4, this.getShopId());
        ps.setInt(5, this.getStaffId());
        ps.setInt(6, this.getEvaluation());
        ps.setString(7, this.getMemo());
        
        return ps;
    }
    
    /**
     * memoId�ɕR�Â������f�[�^��_���폜����r�p�k�����擾����B
     * @return �����f�[�^��_���폜����r�p�k��
     */
    protected String getDeleteSQL() {
        return "UPDATE "
             + "data_customer_memo SET "
             + "  delete_date = CURRENT_TIMESTAMP " 
             + "WHERE memo_id = ?";
    }
    
    /**
     * getDeleteSQL()�̃v���[�X�z���_�ɒl��ݒ肵��PreparedStatement���擾����B
     * @param con �R�l�N�V����
     * @return �����f�[�^��_���폜����r�p�k���ɒl��ݒ肵��Statement
     * @throws java.sql.SQLException
     */
    protected PreparedStatement getDeletePreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getDeleteSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
}
