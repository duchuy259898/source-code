/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author s_furukawa
 */
public class CourseClass extends ArrayList<Course> {

    protected Integer courseClassId;

    protected String courseClassName;
    
    protected Integer displaySeq;

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public Integer getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }
    
    // Thanh start add 2014/07/11 Mashu_お会計表示
    private Integer shopCategoryID = null;

    public Integer getShopCategoryID() {
        return shopCategoryID;
    }

    public void setShopCategoryID(Integer shopCategoryID) {
        this.shopCategoryID = shopCategoryID;
    }
    // Thanh end add 2014/07/11 Mashu_お会計表示

        /**
     * 購入データを設定する。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
        this.setCourseClassId(rs.getInt("course_class_id"));
        this.setCourseClassName(rs.getString("course_class_name"));
        this.setDisplaySeq(rs.getInt("display_seq"));
        // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
        this.setShopCategoryID(rs.getInt("shop_category_id"));
        // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
    }

        /**
     * 商品を読み込む。
     * @param con ConnectionWrapper
     * @param productDivision 商品分類
     * @return true - 成功、false - 失敗
     * @throws java.sql.SQLException SQLException
     */
    public boolean loadCourse(ConnectionWrapper con, Integer shopID, Integer courseClassId) throws SQLException
    {
            this.clear();

            ResultSetWrapper	rs	=	con.executeQuery(
                            this.getLoadCourseSQL(shopID, courseClassId));

            while(rs.next())
            {
                    Course	cc	=	new Course();
                    cc.setData(rs);
                    cc.setCourseClass((CourseClass)this.clone());
                    this.add(cc);
            }

            rs.close();

            return	true;
    }

    public boolean loadCourse(ConnectionWrapper con, Integer courseClassId) throws SQLException
    {
            this.clear();

            ResultSetWrapper	rs	=	con.executeQuery(
                            this.getLoadCourseSQL(courseClassId));

            while(rs.next())
            {
                    Course	cc	=	new Course();
                    cc.setData(rs);
                    cc.setCourseClass((CourseClass)this.clone());
                    this.add(cc);
            }

            rs.close();

            return	true;
    }
        /**
     * 商品を読み込むＳＱＬ文を取得する。
     * @param productDivision 商品分類
     * @return 商品を読み込むＳＱＬ文
     */
    public String getLoadCourseSQL(Integer shopID, Integer courseClassId)
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
            sql.append("     and mup.product_division = 3");
            sql.append(" order by");
            sql.append("      mup.display_seq");
            sql.append("     ,mc.course_name");
            sql.append("     ,mc.course_id");

            return sql.toString();
    }
    
    public String getLoadCourseSQL(Integer courseClassId)
    {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select course_id");
            sql.append(" , course_name");
            sql.append(" ,num");
            sql.append(" ,price");
            sql.append(" ,price as base_price");
            sql.append(" ,display_seq as base_display_seq");
            sql.append(" ,display_seq");
            sql.append(" ,operation_time");
            sql.append(" ,praise_time_limit");
            sql.append(" ,is_praise_time \n");
            sql.append(" from mst_course \n");
            sql.append(" where delete_date is null");
            sql.append(" and course_class_id = " + SQLUtil.convertForSQL(courseClassId));
            sql.append(" order by display_seq, course_name, course_id");

            return sql.toString();
    }

    public String toString(){
        return this.courseClassName;
    }
}
