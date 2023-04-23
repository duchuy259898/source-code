/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author s_furukawa
 */
public class Course {
    /**
     * コース分類
     */
    protected CourseClass courseClass;
    /**
     * コースID
     */
    protected	Integer courseId = null;
    /**
     * コース名
     */
    protected String courseName = "";
    /**
     * 価格
     */
    protected Long price = null;
    /**
     * 元の価格
     */
    private Long basePrice = null;
    /**
     * 消化回数
     */
    protected Integer num;
    /**
     * 施術時間
     */
    protected Integer operationTime;
    /**
     * 有効期限使用有無
     */
    protected boolean isPraiseTime;
    /**
     * 有効期限
     */
    protected Integer praiseTimeLimit;
    /**
     * 表示順
     */
    protected Integer displaySeq = null;
    
    // Start add 20131202 lvut
    protected Integer   securityTimeLimit   = 0;
    // End add 20131202 lvut

    public Course(){
    }

    public Long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Long basePrice) {
        this.basePrice = basePrice;
    }

    public CourseClass getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(CourseClass courseClass) {
        this.courseClass = courseClass;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    public boolean isIsPraiseTime() {
        return isPraiseTime;
    }

    public void setIsPraiseTime(boolean isPraiseTime) {
        this.isPraiseTime = isPraiseTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Integer operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getPraiseTimeLimit() {
        return praiseTimeLimit;
    }

    public void setPraiseTimeLimit(Integer praiseTimeLimit) {
        this.praiseTimeLimit = praiseTimeLimit;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
     // Start add 20131202 lvut
    public void setSecurityTimeLimit(Integer securityTimeLimit){
        this.securityTimeLimit = securityTimeLimit;
    }
    
    public Integer getSecurityTimeLimit(){
        return this.securityTimeLimit;
    }
    // End add 20131202 lvut

    /**
     * コースデータを設定する。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setCourseId(rs.getInt("course_id"));
        this.setCourseName(rs.getString("course_name"));
        this.setPrice(rs.getLong("price"));
        this.setBasePrice(rs.getLong("base_price"));
        this.setNum(rs.getInt("num"));
        this.setOperationTime(rs.getInt("operation_time"));
        this.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        this.setIsPraiseTime(rs.getBoolean("is_praise_time"));
        this.setDisplaySeq(rs.getInt("display_seq"));
    }

    public boolean loadCourse(ConnectionWrapper con, Integer shopID, Integer courseClassId, Integer courseId) throws SQLException
    {
            ResultSetWrapper	rs	=	con.executeQuery(
                            this.getLoadCourseSQL(shopID, courseClassId, courseId));

            if(rs.next())
            {
                    this.setData(rs);
            }

            rs.close();

            return	true;
    }
    
    // IVS Hoa Start Add 20150619
    // [gb]解約伝票でコース名が表示されない
    // マスタにコース削除した場合または店舗使用技術・商品マスタが存在しない場合
    public boolean loadCourse(ConnectionWrapper con, Integer shopID, Integer courseClassId, Integer courseId, Integer slipNo, Integer slipDetailNo) throws SQLException
    {
            ResultSetWrapper	rs	=	con.executeQuery(
                            this.getLoadCourseSQL(shopID, courseClassId, courseId, slipNo, slipDetailNo));

            if(rs.next())
            {
                    this.setData(rs);
            }

            rs.close();

            return	true;
    }
    // IVS Hoa End Add 20150619

    /**
     * 商品を読み込むＳＱＬ文を取得する。
     * @param productDivision 商品分類
     * @return 商品を読み込むＳＱＬ文
     */
    public String getLoadCourseSQL(Integer shopID, Integer courseClassId, Integer courseId)
    {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mc.course_id");
            sql.append("     ,mc.course_name");
            sql.append("     ,mc.num");
            sql.append("     ,mup.price");
            sql.append("     ,mc.price as base_price");
            sql.append("     ,mc.display_seq as base_display_seq");
            sql.append("     ,mup.display_seq");
            sql.append("     ,mc.operation_time");
            sql.append("     ,mc.praise_time_limit");
            sql.append("     ,mc.is_praise_time");
            sql.append(" from");
            sql.append("     mst_use_product mup");
            sql.append("         inner join mst_course mc");
            sql.append("                 on mc.course_id = mup.product_id");
            sql.append("                and mc.course_class_id = " + SQLUtil.convertForSQL(courseClassId));
            sql.append("                and mc.delete_date is null");
            sql.append(" where");
            sql.append("         mup.delete_date is null");
            sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
            sql.append("     and mc.course_id = " + SQLUtil.convertForSQL(courseId));
            sql.append("     and mup.product_division = 3");
            sql.append(" order by");
            sql.append("      mup.display_seq");
            sql.append("     ,mc.course_name");
            sql.append("     ,mc.course_id");

            return sql.toString();
    }
    
    // IVS Hoa Start Add 20150619
    // [gb]解約伝票でコース名が表示されない
    // マスタにコース削除した場合または店舗使用技術・商品マスタが存在しない場合
        /**
     * 商品を読み込むＳＱＬ文を取得する。
     * @param productDivision 商品分類
     * @return 商品を読み込むＳＱＬ文
     */
    public String getLoadCourseSQL(Integer shopID, Integer courseClassId, Integer courseId, Integer slipNo, Integer slipDetailNo)
    {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mc.course_id");
            sql.append("     ,mc.course_name");
            sql.append("     ,mc.num");
            sql.append("     ,dsd.product_value as price");
            sql.append("     ,mc.price as base_price");
            sql.append("     ,mc.display_seq as base_display_seq");
            sql.append("     ,mc.display_seq");
            sql.append("     ,mc.operation_time");
            sql.append("     ,mc.praise_time_limit");
            sql.append("     ,mc.is_praise_time");
            sql.append(" from");
            sql.append("         data_sales_detail dsd  ");
            sql.append("         inner join mst_course mc");
            sql.append("                on mc.course_id = dsd.product_id and dsd.product_division = 8");
            sql.append("                and mc.course_class_id = " + SQLUtil.convertForSQL(courseClassId));
            sql.append(" where");
            sql.append("         dsd.delete_date is null");
            sql.append("     and dsd.shop_id = " + SQLUtil.convertForSQL(shopID));
            sql.append("     and dsd.slip_no = " + SQLUtil.convertForSQL(slipNo));
            sql.append("     and dsd.slip_detail_no = " + SQLUtil.convertForSQL(slipDetailNo));
            sql.append("     and mc.course_id = " + SQLUtil.convertForSQL(courseId));
            sql.append("     and dsd.product_division = 8");
            sql.append(" order by");
            sql.append("      mc.display_seq");
            sql.append("     ,mc.course_name");
            sql.append("     ,mc.course_id");

            return sql.toString();
    }
    // IVS Hoa End Add 20150619
    
    public boolean loadCourseByID(ConnectionWrapper con) throws SQLException
    {
            ResultSetWrapper	rs	=	con.executeQuery(
            this.getLoadCourseSQL(courseId));

            if(rs.next())
            {
                    this.setData(rs);
            }

            rs.close();

            return	true;
    }
    public String getLoadCourseSQL(Integer courseClassId)
    {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mc.course_id");
            sql.append("     ,mc.course_name");
            sql.append("     ,mc.num");
            sql.append("     ,mc.price");
            sql.append("     ,mc.price as base_price");
            sql.append("     ,mc.display_seq as base_display_seq");
            sql.append("     ,mc.display_seq");
            sql.append("     ,mc.operation_time");
            sql.append("     ,mc.praise_time_limit");
            sql.append("     ,mc.is_praise_time");
            sql.append(" from");
            sql.append("      mst_course mc ");
            sql.append(" where");
            sql.append("     mc.course_id = " + SQLUtil.convertForSQL(courseId));
            sql.append(" order by");
            sql.append("      mc.display_seq");
            sql.append("     ,mc.course_name");
            sql.append("     ,mc.course_id");

            return sql.toString();
    }

    public String toString(){
        return this.courseName;
    }
}
