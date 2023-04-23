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
 * �v�����}�X�^�f�[�^
 *
 * @author lvtu
 */
public class MstPlan {

    /**
     * �R�[�X����
     */
    protected MstCourseClass courseClass = new MstCourseClass();
    /**
     * �R�[�X
     */
    protected MstCourse course = new MstCourse();
    /**
     * �v����ID
     */
    protected Integer planId = null;
    /**
     * �v������
     */
    protected String planName = "";
    /**
     * �\����
     */
    protected Integer displaySeq = null;

    /**
     * Creates a new instance of MstPlan
     */
    public MstPlan() {
    }

    /**
     * �R�[�X���ނ��擾����B
     *
     * @return �R�[�X����
     */
    public MstCourseClass getCourseClass() {
        return courseClass;
    }

    /**
     * �R�[�X���ނ��Z�b�g����B
     *
     * @param courseClass �R�[�X����
     */
    public void setCourseClass(MstCourseClass courseClass) {
        this.courseClass = courseClass;
    }

    /**
     * �R�[�X���擾����B
     *
     * @return �R�[�X
     */
    public MstCourse getCourse() {
        return course;
    }

    /**
     * �R�[�X���ނ��Z�b�g����B
     *
     * @param course �R�[�X
     */
    public void setCourse(MstCourse course) {
        this.course = course;
    }

    /**
     * �v����ID���擾����B
     *
     * @return �v����ID
     */
    public Integer getPlanId() {
        return planId;
    }

    /**
     * �v����ID���Z�b�g����B
     *
     * @param planId �v����ID
     */
    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    /**
     * �v���������擾����B
     *
     * @return �v������
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * �v���������Z�b�g����B
     *
     * @param planName �v������
     */
    public void setPlanName(String planName) {
        this.planName = planName;
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

    @Override
    public String toString() {
        return planName;
    }

    /**
     * �v�����}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
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
     * �v�����}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
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
     * �v�����}�X�^�f�[�^����f�[�^���Z�b�g����B
     *
     * @param mp �v�����}�X�^�f�[�^
     */
    public void setData(MstPlan mp) {
        this.setCourseClass(mp.getCourseClass());
        this.setCourse(mp.getCourse());
        this.setPlanId(mp.getPlanId());
        this.setPlanName(mp.getPlanName());
        this.setDisplaySeq(mp.getDisplaySeq());
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����
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
     * �v�����}�X�^�Ƀf�[�^��o�^����B
     *
     * @return true - ����
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
     * �v�����}�X�^����f�[�^���폜����B�i�_���폜�j
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
     * �v�����}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
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
     * �v������ݒ肵�Ă���ڋq������ꍇ�͍폜�s��
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
     * Select�����擾����B
     *
     * @return Select��
     */
    private String getSelectSQL() {
        return "select *\n"
                + "from mst_plan\n"
                + "where	plan_id = " + SQLUtil.convertForSQL(this.getPlanId()) + "\n";
    }

    /**
     * Select�����擾����B
     *
     * @return Select��
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
     * Select�����擾����B
     *
     * @return Select��
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
     * Insert�����擾����B
     *
     * @return Insert��
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
     * Update�����擾����B
     *
     * @return Update��
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
     * �\���������炷�r�p�k�����擾����
     *
     * @param seq ���̕\����
     * @param isIncrement true - ���Z�Afalse - ���Z
     * @return �\���������炷�r�p�k��
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
     * �폜�pUpdate�����擾����B
     *
     * @return �폜�pUpdate��
     */
    private String getDeleteSQL() {
        return "update mst_plan\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	plan_id = " + SQLUtil.convertForSQL(this.getPlanId()) + "\n";
    }

}
