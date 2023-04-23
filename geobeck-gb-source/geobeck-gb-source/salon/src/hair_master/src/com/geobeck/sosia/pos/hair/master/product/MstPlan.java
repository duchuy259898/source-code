/*
 * MstPlan.java
 *
 * Created on 2018/03/05, 11:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.ArrayList;

/**
 * プランマスタデータ
 *
 * @author lvtu
 */
public class MstPlan {

    /**
     * コース分類
     */
    protected MstCourseClass courseClass = new MstCourseClass();
    /**
     * コース
     */
    protected MstCourse course = new MstCourse();
    /**
     * プランID
     */
    protected Integer planId = null;
    /**
     * プラン名
     */
    protected String planName = "";
    /**
     * 表示順
     */
    protected Integer displaySeq = null;

    /**
     * Creates a new instance of MstPlan
     */
    public MstPlan() {
    }

    /**
     * コース分類を取得する。
     *
     * @return コース分類
     */
    public MstCourseClass getCourseClass() {
        return courseClass;
    }

    /**
     * コース分類をセットする。
     *
     * @param courseClass コース分類
     */
    public void setCourseClass(MstCourseClass courseClass) {
        this.courseClass = courseClass;
    }

    /**
     * コースを取得する。
     *
     * @return コース
     */
    public MstCourse getCourse() {
        return course;
    }

    /**
     * コース分類をセットする。
     *
     * @param course コース
     */
    public void setCourse(MstCourse course) {
        this.course = course;
    }

    /**
     * プランIDを取得する。
     *
     * @return プランID
     */
    public Integer getPlanId() {
        return planId;
    }

    /**
     * プランIDをセットする。
     *
     * @param planId プランID
     */
    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    /**
     * プラン名を取得する。
     *
     * @return プラン名
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * プラン名をセットする。
     *
     * @param planName プラン名
     */
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    /**
     * 表示順を取得する。
     *
     * @return 表示順
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * 表示順をセットする。
     *
     * @param displaySeq 表示順
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    @Override
    public String toString() {
        return planName;
    }

    /**
     * プランマスタデータをArrayListに読み込む。
     * @param con
     * @return 
     * @throws java.sql.SQLException
     */
    public ArrayList<MstPlan> load(ConnectionWrapper con) throws SQLException {
        ArrayList<MstPlan> listPlan = new ArrayList<MstPlan>();
        ResultSetWrapper rs = con.executeQuery(
                this.getSelectAllSQL());

        while (rs.next()) {
            MstPlan mp = new MstPlan();
            mp.setData(rs);
            listPlan.add(mp);
        }

        rs.close();

        return listPlan;
    }
    
    /**
     * プランマスタデータをArrayListに読み込む。
     * @param con
     * @param shopId
     * @return 
     * @throws java.sql.SQLException 
     */
    public ArrayList<MstPlan> loadByShop(ConnectionWrapper con, int shopId) throws SQLException {
        ArrayList<MstPlan> listPlan = new ArrayList<MstPlan>();
        ResultSetWrapper rs = con.executeQuery(
                this.getSelectByShopSQL(shopId));

        while (rs.next()) {
            MstPlan mp = new MstPlan();
            mp.setData(rs);
            listPlan.add(mp);
        }

        rs.close();

        return listPlan;
    }

    /**
     * プランマスタデータからデータをセットする。
     *
     * @param mp プランマスタデータ
     */
    public void setData(MstPlan mp) {
        this.setCourseClass(mp.getCourseClass());
        this.setCourse(mp.getCourse());
        this.setPlanId(mp.getPlanId());
        this.setPlanName(mp.getPlanName());
        this.setDisplaySeq(mp.getDisplaySeq());
    }

    /**
     * ResultSetWrapperからデータをセットする
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setPlanId(rs.getInt("plan_id"));
        this.setPlanName(rs.getString("plan_name"));

        MstCourseClass mCourseClass = new MstCourseClass();
        mCourseClass.setCourseClassID(rs.getInt("course_class_id"));
        mCourseClass.setCourseClassName(rs.getString("course_class_name"));
        this.setCourseClass(mCourseClass);

        MstCourse mCourse = new MstCourse();
        mCourse.setCourseID(rs.getInt("course_id"));
        mCourse.setCourseName(rs.getString("course_name"));
        mCourse.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        mCourse.setNum(rs.getInt("num"));
        mCourse.setPrice(rs.getLong("price"));
        this.setCourse(mCourse);

        this.setDisplaySeq(rs.getInt("display_seq"));
    }

    /**
     * プランマスタにデータを登録する。
     *
     * @return true - 成功
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
        }

        return true;
    }

    /**
     * プランマスタからデータを削除する。（論理削除）
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
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
     * プランマスタにデータが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getPlanId() == null) {
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
    
    /**
     * プランを設定している顧客がいる場合は削除不可
     * @param con
     * @return
     * @throws SQLException 
     */
    public boolean isExistsPlanSettingCustomer(ConnectionWrapper con) throws SQLException {

        if (con == null) {
            return false;
        }
        
        String sql = "select 1\n"
                + "from mst_plan mp\n"
                + "inner join data_month_member mm on mm.plan_id = mp.plan_id\n"
                + "where mp.plan_id = " + SQLUtil.convertForSQL(this.getPlanId()) + "\n"
                + "and mm.delete_date is null \n"
                + "and mm.status = 1\n";

        ResultSetWrapper rs = con.executeQuery(sql);

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL() {
        return "select *\n"
                + "from mst_plan\n"
                + "where	plan_id = " + SQLUtil.convertForSQL(this.getPlanId()) + "\n";
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectAllSQL() {
        return "select plan.*\n"
                + ", course.course_id\n"
                + ", course.course_name\n"
                + ", course.praise_time_limit\n"
                + ", course.num\n"
                + ", course.price\n"
                + ", cc.course_class_id\n"
                + ", cc.course_class_name\n"
                + "from mst_plan plan\n"
                + "inner join mst_course course on course.course_id = plan.course_id\n"
                + "inner join mst_course_class cc on cc.course_class_id = course.course_class_id\n"
                + "where plan.delete_date is null\n"
                + "order by display_seq, plan_id\n";
    }
    
    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectByShopSQL(int shopId) {
        return "select plan.*\n"
                + ", course.course_id\n"
                + ", course.course_name\n"
                + ", course.praise_time_limit\n"
                + ", course.num\n"
                + ", course.price\n"
                + ", cc.course_class_id\n"
                + ", cc.course_class_name\n"
                + "from mst_plan plan\n"
                + "inner join mst_course course on course.course_id = plan.course_id\n"
                + "inner join mst_use_product mup\n"
                + "on course.course_id = mup.product_id\n"
                + "and course.delete_date is null\n"
                + "inner join mst_course_class cc on cc.course_class_id = course.course_class_id\n"
                + "where plan.delete_date is null\n"
                + "and mup.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                + "and mup.product_division = 3\n"
                + "order by display_seq, plan_id\n";
    }

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        return "insert into mst_plan\n"
                + "(plan_id,\n"
                + "plan_name,\n"
                + "course_id,\n"
                + "display_seq,\n"
                + "insert_date, update_date, delete_date)\n"
                + "select\n"
                + "(select coalesce(max(plan_id), 0) + 1\n"
                + "from mst_plan),\n"
                + SQLUtil.convertForSQL(this.getPlanName()) + ",\n"
                + SQLUtil.convertForSQL(this.getCourse().getCourseID()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_plan\n"
                + "where delete_date is null\n"
                + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_plan\n"
                + "where delete_date is null\n"
                + "), 0) + 1 end,\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update文を取得する。
     *
     * @return Update文
     */
    private String getUpdateSQL() {
        return "update mst_plan\n"
                + "set\n"
                + "plan_name = " + SQLUtil.convertForSQL(this.getPlanName()) + ",\n"
                + "course_id = " + SQLUtil.convertForSQL(this.getCourse().getCourseID()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_plan\n"
                + "where delete_date is null\n"
                + "and plan_id != "
                + SQLUtil.convertForSQL(this.getPlanId()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_plan\n"
                + "where delete_date is null\n"
                + "and plan_id != "
                + SQLUtil.convertForSQL(this.getPlanId()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp\n"
                + "where	plan_id = " + SQLUtil.convertForSQL(this.getPlanId()) + "\n";
    }

    /**
     * 表示順をずらすＳＱＬ文を取得する
     *
     * @param seq 元の表示順
     * @param isIncrement true - 加算、false - 減算
     * @return 表示順をずらすＳＱＬ文
     */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_plan");
        sql.append(" set");
        sql.append("     display_seq = display_seq " + (isIncrement ? "+" : "-") + " 1");
        sql.append(" where");
        sql.append("         delete_date is null");
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
            sql.append("                             mst_plan");
            sql.append("                         where");
            sql.append("                             delete_date is null");
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
     * 削除用Update文を取得する。
     *
     * @return 削除用Update文
     */
    private String getDeleteSQL() {
        return "update mst_plan\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	plan_id = " + SQLUtil.convertForSQL(this.getPlanId()) + "\n";
    }

}
