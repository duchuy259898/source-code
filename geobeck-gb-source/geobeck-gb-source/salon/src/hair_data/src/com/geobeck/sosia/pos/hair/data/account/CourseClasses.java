/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author s_furukawa
 */
public class CourseClasses extends Vector<CourseClass> {

    protected	Integer		productDivision		=	0;

    public Integer getProductDivision() {
        return productDivision;
    }

    public void setProductDivision(Integer productDivision) {
        this.productDivision = productDivision;
    }

    	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		this.clear();

                ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());

		while(rs.next())
		{
			CourseClass	cc	=	new CourseClass();
			cc.setData(rs);
			this.add(cc);
		}

		rs.close();

		return	true;
	}

        public boolean loadCourseClass(ConnectionWrapper con) throws SQLException
	{
		this.clear();

                ResultSetWrapper	rs	=	con.executeQuery(this.getLoadCourseSQL());

		while(rs.next())
		{
			CourseClass	cc	=	new CourseClass();
			cc.setData(rs);
			this.add(cc);
		}

		rs.close();

		return	true;
	}

	/**
	 * �f�[�^��ǂݍ��ނr�p�k�����擾����B
	 * @return �f�[�^��ǂݍ��ނr�p�k��
	 */
	public String getLoadSQL()
	{
		String	sql = "select course_class_id as product_class_id, \n" +
                              "course_class_name as product_class_name, display_seq\n" +
                              // IVS_Thanh start add 2014/07/10 Mashu_����v�\��
                              ",shop_category_id\n" +
                              // IVS_Thanh end add 2014/07/10 Mashu_����v�\��
                              "from mst_course_class\n" +
                              "where delete_date is null\n" +
                              "order by display_seq, course_class_name, course_class_id";

		return	sql;
	}

        public String getLoadCourseSQL()
	{
		String	sql = "select * \n" +
                              "from mst_course_class\n" +
                              "where delete_date is null\n" +
                              "order by display_seq, course_class_name, course_class_id";

		return	sql;
	}

    	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, Integer shopID) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(this.getLoadCourseClassSQL(shopID));

            while (rs.next()) {
                CourseClass courseClass = new CourseClass();
                courseClass.setData(rs);
                this.add(courseClass);
            }

            rs.close();

            return true;
	}

      	/**
	 * �f�[�^��ǂݍ��ނr�p�k�����擾����B
	 * @return �f�[�^��ǂݍ��ނr�p�k��
	 */
	public String getLoadCourseClassSQL(Integer shopID)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mcc.course_class_id");
            sql.append("     ,mcc.course_class_name");
            sql.append("     ,mcc.display_seq");
            // Thanh start add 2014/07/10 Mashu_����v�\��
            sql.append("     ,mcc.shop_category_id");
            // Thanh start add 2014/07/10 Mashu_����v�\��
            sql.append(" from");
            sql.append("     (");
            sql.append("         select distinct");
            sql.append("             mc.course_class_id");
            sql.append("         from");
            sql.append("             mst_use_product mup");
            sql.append("                 inner join mst_course mc");
            sql.append("                         on mc.course_id = mup.product_id");
            sql.append("                        and mc.delete_date is null");
            sql.append("         where");
            sql.append("                 mup.delete_date is null");
            sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
            sql.append("             and mup.product_division = 3");
            sql.append("     ) mc");
            sql.append("     inner join mst_course_class mcc");
            sql.append("             on mcc.course_class_id = mc.course_class_id");
            sql.append("            and mcc.delete_date is null");
            sql.append(" order by");
            sql.append("     mcc.display_seq");

            return sql.toString();
	}
        // IVS_Thanh start add 2014/07/10 Mashu_����v�\��
        /**
	 * �f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
        public boolean load(ConnectionWrapper con, Integer shopID, String integrationId) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(this.getLoadCourseClassSQL(shopID,integrationId));

            while (rs.next()) {
                CourseClass courseClass = new CourseClass();
                courseClass.setData(rs);
                this.add(courseClass);
            }

            rs.close();

            return true;
	}
        /**
	 * �f�[�^��ǂݍ��ނr�p�k�����擾����B
	 * @return �f�[�^��ǂݍ��ނr�p�k��
	 */
        public String getLoadCourseClassSQL(Integer shopID,String integrationId)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mcc.course_class_id");
            sql.append("     ,mcc.course_class_name");
            sql.append("     ,mcc.display_seq");
            // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
            sql.append("     ,mcc.shop_category_id");
            // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
            sql.append(" from");
            sql.append("     (");
            sql.append("         select distinct");
            sql.append("             mc.course_class_id");
            sql.append("         from");
            sql.append("             mst_use_product mup");
            sql.append("                 inner join mst_course mc");
            sql.append("                         on mc.course_id = mup.product_id");
            sql.append("                        and mc.delete_date is null");
            sql.append("         where");
            sql.append("                 mup.delete_date is null");
            sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
            sql.append("             and mup.product_division = 3");
            sql.append("     ) mc");
            sql.append("     inner join mst_course_class mcc");
            sql.append("             on mcc.course_class_id = mc.course_class_id");
            sql.append("            and mcc.delete_date is null");
            if (!"".equals(integrationId)) {
                sql.append(" where");
                sql.append("     mcc.course_integration_id = ").append(integrationId);
            }
            sql.append(" order by");
            sql.append("     mcc.display_seq");

            return sql.toString();
	}
        // IVS_Thanh end add 2014/07/10 Mashu_����v�\��

    	public static final int TYPE_COURCE = 3;
    	private static HashMap<String, CourseClasses> cachedProducts = new HashMap<String, CourseClasses>();
    	public static CourseClasses getCachedProducts(ConnectionWrapper con, int type, Integer shop_id, String integrationId) throws SQLException {
    		if (!cachedProducts.containsKey(type+"_"+shop_id+"_"+integrationId)) {
    			CourseClasses productClasses = new CourseClasses();
    			productClasses.setProductDivision(TYPE_COURCE);
    			productClasses.load(con, shop_id, integrationId);
    			cachedProducts.put(type+"_"+shop_id+"_"+integrationId, productClasses);
    		}
    		return cachedProducts.get(type+"_"+shop_id+"_"+integrationId);
    	}

}
