/*
 * DataCustomerMemo.java
 *
 * 顧客メモ機能対応
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
 * data_customer_memoテーブルへのアクセス、取得結果保持を行う。
 */
public class DataCustomerMemo {
    /**
     * 評価：良い
     */
    public static final int EVALUATION_GOOD = 1;
    /**
     * 評価：悪い
     */
    public static final int EVALUATION_BAD = 2;
    /**
     * 評価：バトン
     */
    public static final int EVALUATION_BATON = 0;
    
    /**
     * 評価文字列
     */
    private static final List<String> EVALUATION_STR = Arrays.asList("バトン", "良い", "悪い");
    
    /**
     * メモID。プライマリーキー。
     */
    protected Integer memoId = null;
    
    /**
     * 顧客ID
     */
    protected Integer customerId = null;
    
    /**
     * 画面上で設定した登録日
     */
    protected java.util.Date memoDate = null;
    
    /**
     * 画面上で選択した店舗の店舗ID
     */
    protected Integer shopId = null;
    
    /**
     * 画面上で選択したスタッフのスタッフID
    */
    protected Integer staffId = null;
    
    /**
     * 画面上で選択した評価（1：良い、2：悪い）
     */
    protected Integer evaluation = null;
    
    /**
     * メモ内容
     */
    protected String memo = null;
    
    /**
     * 登録日（データ挿入日時）
     */
    protected Date insertDate = null;
    
    /**
     * 更新日（データ更新日時）
     */
    protected Date updateDate = null;
    
    /**
     * 削除日（データ削除日時）
     */
    protected Date deleteDate = null;
    
    /**
     * メモデータに登録されている店舗
     */
    protected MstShop shop = null;

    /**
     * メモデータに登録されている担当者
     */
    protected MstStaff staff = null;
    
    /**
     * 評価に対応する文字列を返す。
     * @param evaluation 文字列を取得する評価値
     * @return 評価に対応する文字列
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
    * ResultSetからデータをセットする。
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
    * 登録処理を行う。
    * @param con ConnectionWrapper
    * @return true - 成功
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
    * 挿入処理を行う。
    * @param con ConnectionWrapper
    * @return true - 成功
    * @throws java.sql.SQLException SQLException
    */
   public boolean insert(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getInsertPreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * 挿入処理を行う。memo_idは事前に設定しておく。
    * @param con ConnectionWrapper
    * @return true - 成功
    * @throws java.sql.SQLException SQLException
    */
   public boolean insertWithMemoId(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getInsertWithMemoIdPreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * 更新処理を行う。
    * @param con ConnectionWrapper
    * @return true - 成功
    * @throws java.sql.SQLException SQLException
    */
   public boolean update(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getUpdatePreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * 削除処理を行う。
    * @param con ConnectionWrapper
    * @return true - 成功
    * @throws java.sql.SQLException SQLException
    */
   public boolean delete(ConnectionWrapper con) throws SQLException {
        try(PreparedStatement ps = this.getDeletePreparedStatement(con)) {
            return (ps.executeUpdate() == 1);
        }
   }
   
   /**
    * メモデータが存在するかを取得する。
    * @param con ConnectionWrapper
    * @return true - 存在する
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
    * 新しいメモIDシーケンス番号を取得する。
    * @param con ConnectionWrapper
    * @return 新しいシーケンス番号。取得できなかった場合は-1。
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
    * customerIdに紐づくメモデータのリストを取得する。
    * @param con ConnectionWrapper
    * @return List 顧客IDに紐づくメモデータのリスト
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
    * customerIdに紐づくメモデータとそのメモに登録されている店舗データ、担当者データのリストを取得する。
    * @param con ConnectionWrapper
    * @return List 顧客IDに紐づくメモデータのリスト
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
    * memoIDに紐づくメモデータを取得する。
    * @param con ConnectionWrapper
    * @return DataCustomerMemo メモIDに紐づくメモデータ。見つからなかった場合はnull
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
    * memoIDに紐づくメモデータを取得する。
    * @param con ConnectionWrapper
    * @return DataCustomerMemo メモIDに紐づくメモデータ。見つからなかった場合はnull
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
     * 新しいメモIDシーケンス番号を取得するＳＱＬ文を取得する。
     * @return 新しいメモIDシーケンス番号を取得するＳＱＬ文
     */
    protected String getSelectNewMemoIdSeqSQL() {
        return	"SELECT NEXTVAL('data_customer_memo_id_seq') ";
    }
    
    /**
     * getSelectNewMemoIdSeqSQL()のPreparedStatementを取得する。
     * @param con コネクション
     * @return getSelectNewMemoIdSeqSQL()のPreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectMemoIdSeqPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectNewMemoIdSeqSQL());
        
        return ps;
    }
           
    /**
     * memoIdに紐づくメモデータ数を取得するＳＱＬ文を取得する。
     * @return メモデータを取得するＳＱＬ文
     */
    protected String getSelectCountSQL() {
        return	"SELECT COUNT(*) AS COUNT " 
              + "FROM data_customer_memo dcm " 
              + "WHERE dcm.memo_id = ?";
    }
    
    /**
     * getSelectCountSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return getSelectCountSQL()のプレースホルダに値を設定したPreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectCountPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectCountSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
    
    /**
     * memoIdに紐づくメモデータを取得するＳＱＬ文を取得する。
     * @return メモデータを取得するＳＱＬ文
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
     * getSelectUniqueSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return getSelectUniqueSQL()のプレースホルダに値を設定したPreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectUniquePreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectUniqueSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
    
        /**
     * memoIdに紐づくメモデータを取得するＳＱＬ文を取得する。
     * @return メモデータを取得するＳＱＬ文
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
     * getSelectUniqueSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return getSelectUniqueSQL()のプレースホルダに値を設定したPreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectMemoDataWithShopDataAndStaffDataPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectUniqueMemoDataWithShopDataAndStaffDataSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
    
    /**
     * 未削除のcustomerIdに紐づくメモデータを取得するＳＱＬ文を取得する。
     * @return 未削除メモデータを取得するＳＱＬ文
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
     * getSelectByCustomerIdSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return getSelectByCustomerIdSQL()のプレースホルダに値を設定したPreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectByCustomerIdPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectByCustomerIdSQL());
        ps.setInt(1, this.getCustomerId());
        
        return ps;
    }
    
    /**
     * 未削除のcustomerIdに紐づくメモデータとそのメモデータに登録されている店舗、担当者データを取得するＳＱＬ文を取得する。
     * @return 未削除メモデータを取得するＳＱＬ文
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
     * getSelectMemoDataWithShopDataAndStaffDataByCustomerIdSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return getSelectMemoDataWithShopDataAndStaffDataByCustomerIdSQL()のプレースホルダに値を設定したPreparedStatement
     * @throws java.sql.SQLException
     * 
     */
    protected PreparedStatement getSelectMemoDataWithShopDataAndStaffDataByCustomerIdPreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getSelectMemoDataWithShopDataAndStaffDataByCustomerIdSQL());
        ps.setInt(1, this.getCustomerId());
        
        return ps;
    }
    
    /**
     * memoIdに紐づくメモデータを更新するＳＱＬ文を取得する。
     * @return メモデータを更新するＳＱＬ文
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
     * getUpdateSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return メモデータを更新するＳＱＬ文に値を設定したStatement
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
     * メモデータを挿入するＳＱＬ文を取得する。
     * @return メモデータを挿入するＳＱＬ文
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
     * getInsertSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return メモデータを挿入するＳＱＬに値を設定したStatement
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
     * メモデータを挿入するＳＱＬ文を取得する。
     * @return メモデータを挿入するＳＱＬ文
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
     * getInsertWithMemoIdSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return メモデータを挿入するＳＱＬに値を設定したStatement
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
     * memoIdに紐づくメモデータを論理削除するＳＱＬ文を取得する。
     * @return メモデータを論理削除するＳＱＬ文
     */
    protected String getDeleteSQL() {
        return "UPDATE "
             + "data_customer_memo SET "
             + "  delete_date = CURRENT_TIMESTAMP " 
             + "WHERE memo_id = ?";
    }
    
    /**
     * getDeleteSQL()のプレースホルダに値を設定したPreparedStatementを取得する。
     * @param con コネクション
     * @return メモデータを論理削除するＳＱＬ文に値を設定したStatement
     * @throws java.sql.SQLException
     */
    protected PreparedStatement getDeletePreparedStatement(ConnectionWrapper con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(this.getDeleteSQL());
        ps.setInt(1, this.getMemoId());
        
        return ps;
    }
}
