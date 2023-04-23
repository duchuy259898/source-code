/*
 * MstAuthority.java
 *
 * Created on 2006/09/04, 10:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;

import java.sql.*;
import java.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;
import java.util.logging.*;
import java.util.Collections;

/**
 * �ڋq�����o�^�N���X
 * @author katagiri
 */
public class MstCustomerIntegration
{	
       /**
	* �R���X�g���N�^
	*/
	public MstCustomerIntegration()
	{
		
	}

       /**
	* �\��f�[�^�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
	* @return true - ����
	*/
	public boolean data_reservation_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                ResultSetWrapper rs = con.executeQuery(this.getUpdateKeySQL(intergration_id, 1));

                                Integer shop_id;
                                Integer reservation_no;
                                while(rs.next())
                                {
                                        shop_id=rs.getInt("shop_id");
                                        reservation_no=rs.getInt("reservation_no");

                                        //�\��f�[�^(data_reservation)
                                        sql =   this.getData_ReservationSQL(customer_id, shop_id, reservation_no);
                                        if(con.executeUpdate(sql) < 1)
                                        {
                                            return	false;
                                        }
                                }

                                rs.close();

                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}
	
       /**
	* �\��f�[�^(temp)�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
	* @return true - ����
	*/
	public boolean data_reservation_temp_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                ResultSetWrapper rs = con.executeQuery(this.getUpdateKeySQL(intergration_id, 2));

                                Integer temp_no;
                                while(rs.next())
                                {
                                        temp_no = rs.getInt("temp_no");

                                        //�\��f�[�^_temp(data_reservation_temp)
                                        sql =   this.getData_ReservationTempSQL(customer_id, temp_no);
                                        if(con.executeUpdate(sql) < 1)
                                        {
                                            return	false;
                                        }
                                }

                                rs.close();

                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}

       /**
	* �̔��f�[�^�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
	* @return true - ����
	*/
	public boolean data_sales_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                ResultSetWrapper rs = con.executeQuery(this.getUpdateKeySQL(intergration_id, 3));

                                Integer shop_id;
                                Integer slip_no;
                                while(rs.next())
                                {
                                        shop_id=rs.getInt("shop_id");
                                        slip_no=rs.getInt("slip_no");

                                        //�̔��f�[�^(data_sales)
                                        sql =   this.getData_SalesSQL(customer_id, shop_id, slip_no);
                                        if(con.executeUpdate(sql) < 1)
                                        {
                                            return	false;
                                        }
                                }

                                rs.close();

                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}

       /**
	* �ڋq�}�X�^�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
	* @return true - ����
	*/
	public boolean mst_customer_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                //�ڋq�}�X�^(mst_customer)
                                sql =   this.getMst_CustomerSQL(intergration_id);
                                if(con.executeUpdate(sql) < 1)
                                {
                                    return	false;
                                }
                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}

       /**
	* �|�C���g�����f�[�^�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
        * @return true - ����
	*/
	public boolean data_point_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                ResultSetWrapper rs = con.executeQuery(this.getUpdateKeySQL(intergration_id, 4));

                                Integer shop_id;
                                Integer point_id;
                                while(rs.next())
                                {
                                        shop_id=rs.getInt("shop_id");
                                        point_id=rs.getInt("point_id");

                                        //�|�C���g�����f�[�^(data_point)
                                        sql =   this.getData_PointSQL(customer_id, shop_id, point_id);
                                        if(con.executeUpdate(sql) < 1)
                                        {
                                            return	false;
                                        }
                                }

                                rs.close();
                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}

       /**
	* �C���[�W�J���e�f�[�^�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
	* @return true - ����
	*/
	public boolean data_image_karte_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    Integer main_cnt = 0;
                    Integer max_image_no = 0;
                    
                    ResultSetWrapper rs_main = con.executeQuery(this.getUpdateKeySQL(customer_id, 5));

                    while(rs_main.next())
                    {
                        main_cnt += 1;
                        max_image_no = rs_main.getInt("image_no")+1;
                    }
                    rs_main.close();
                    if (max_image_no == 0) max_image_no =1;
                    // sort
                    Collections.sort(ary_intergration);
                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                ResultSetWrapper rs = con.executeQuery(this.getUpdateKeySQL(intergration_id, 5));

                                Integer image_no;
                                Integer image_id;
                                String comment;

                                while(rs.next())
                                {
                                        image_no=rs.getInt("image_no");
                                        image_id=rs.getInt("image_id");
                                        comment=rs.getString("comment");
                                        if (comment.length() == 0)
                                        {
                                            comment = "";
                                        }

                                        if (main_cnt < 3)
                                        {
                                            //�C���[�W�J���e�f�[�^�o�^(data_image_karte)
                                            sql =   this.getData_Image_KarteSQL(customer_id, max_image_no, image_id, comment, 1);
                                            if(con.executeUpdate(sql) < 1)
                                            {
                                                return	false;
                                            }
                                            main_cnt += 1;
                                            max_image_no += 1;

                                            //�C���[�W�J���e�f�[�^�X�V(data_image_karte)��������ڋq�̃C���[�W�f�[�^���폜����B
                                            sql =   this.getData_Image_KarteSQL(intergration_id, image_no, image_id, comment, 2);
                                            if(con.executeUpdate(sql) < 1)
                                            {
                                                return	false;
                                            }
                                        }
                                        else
                                        {
                                            //�C���[�W�J���e�f�[�^�X�V(data_image_karte)��������ڋq�̃C���[�W�f�[�^���폜����B
                                            sql =   this.getData_Image_KarteSQL(intergration_id, image_no, image_id, comment, 2);
                                            if(con.executeUpdate(sql) < 1)
                                            {
                                                return	false;
                                            }
                                        }
                                }

                                rs.close();
                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}

       /**
	* DM�����ڍ׃f�[�^�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
	* @return true - ����
	*/
	public boolean data_dm_history_detail_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                ResultSetWrapper rs = con.executeQuery(this.getUpdateKeySQL(intergration_id, 6));

                                Integer shop_id;
                                Integer dm_type;
                                java.util.Date make_date;
                                while(rs.next())
                                {
                                        shop_id=rs.getInt("shop_id");
                                        dm_type=rs.getInt("dm_type");
                                        make_date=rs.getDate("make_date");


                                        int datacnt = 0;
                                        ResultSetWrapper rs_main = con.executeQuery(this.getHistoryKeySQL(shop_id,
                                                                                                            dm_type,
                                                                                                            make_date,
                                                                                                            customer_id));
                                        while(rs_main.next())
                                        {
                                            datacnt += 1;
                                        }
                                        rs_main.close();

                                        //DM�����ڍ׃f�[�^(data_dm_history_detail)
                                        sql =   this.getData_Dm_HistorySQL(shop_id,
                                                                            dm_type,
                                                                            make_date,
                                                                            customer_id,
                                                                            intergration_id,
                                                                            datacnt);
                                        con.executeUpdate(sql);
                                }

                                rs.close();

                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}

       /**
	* �t���[���ڃ��[�U�f�[�^�𓝍�����B
	* @param con ConnectionWrapper
	* @throws java.sql.SQLException SQLException
	* @return true - ����
	*/
	public boolean mst_customer_free_heading_regist(ConnectionWrapper con,
                                ArrayList<Integer> ary_intergration,
                                ArrayList<Integer> ary_main) throws SQLException
	{
                try {
                    String	sql = "";

                    Integer customer_id = ary_main.get(0);
                    Integer intergration_id;

                    for (Object obj : ary_intergration) {
                        if (obj instanceof Integer) {    //Integer�^�̏ꍇ
                            if (((Integer)obj).intValue() != customer_id)
                            {
                                intergration_id = ((Integer)obj).intValue();

                                ResultSetWrapper rs = con.executeQuery(this.getUpdateKeySQL(intergration_id, 7));

                                Integer free_heading_class_id;
                                while(rs.next())
                                {
                                        free_heading_class_id=rs.getInt("free_heading_class_id");

                                        int datacnt = 0;
                                        ResultSetWrapper rs_main = con.executeQuery(this.getFreeHeadingKeySQL(free_heading_class_id,
                                                                                                            customer_id));
                                        while(rs_main.next())
                                        {
                                            datacnt += 1;
                                        }
                                        rs_main.close();

                                        //�t���[���ڃ��[�U�f�[�^(mst_customer_free_heading)
                                        sql =   this.getMst_Customer_Free_HeadingSQL(free_heading_class_id,
                                                                            customer_id,
                                                                            intergration_id,
                                                                            datacnt);
                                        if(con.executeUpdate(sql) < 1)
                                        {
                                            return	false;
                                        }
                                }

                                rs.close();

                            }
                        }
                    }

                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return	false;
                }
                return	true;
	}

       /**
	*
	* @return
	*/
	public String getUpdateKeySQL(Integer id, Integer tablekbn)
	{
                String sql = "";

                if (tablekbn == 1)  //�\��f�[�^
                {
                    sql = "select shop_id,\n" +
				"reservation_no\n" +
				"from data_reservation\n" +
				"where customer_id = " + id + "\n";
                }
                else if (tablekbn == 2) //�\��f�[�^TEMP
                {
                     sql = "select temp_no\n" +
                            "from data_reservation_temp\n" +
                            "where customer_id = " + id + "\n";
                }
                else if (tablekbn == 3) //�̔��f�[�^
                {
                    sql = "select shop_id,\n" +
				"slip_no\n" +
				"from data_sales\n" +
				"where customer_id = " + id + "\n";
                }
                else if (tablekbn == 4) //�|�C���g�����f�[�^
                {
                    sql = "select shop_id,\n" +
				"point_id\n" +
				"from data_point\n" +
				"where customer_id = " + id + "\n";
                }
                else if (tablekbn == 5) //�C���[�W�J���e�f�[�^
                {
                    sql = "select image_no,\n" +
				"image_id,\n" +
				"comment\n" +
				"from data_image_karte\n" +
				"where customer_id = " + id + "\n" +
                                "order by image_no";
                }
                else if (tablekbn == 6) //DM�����ڍ׃f�[�^
                {
                    sql = "select shop_id,\n" +
				"dm_type,\n" +
				"make_date\n" +
				"from data_dm_history_detail\n" +
				"where customer_id = " + id + "\n";
                }
                else if (tablekbn == 7) //�t���[���ڃ��[�U�f�[�^
                {
                    sql = "select free_heading_class_id\n" +
				"from mst_customer_free_heading\n" +
				"where customer_id = " + id + "\n";
                }

		return	sql;
	}

       /**
	*DM�����ڍ׃f�[�^�̃L�[�����擾
	* @return
	*/
	public String getHistoryKeySQL(Integer shop_id,
                                            Integer dm_type,
                                            java.util.Date make_date,
                                            Integer intergration_id)
	{
                String sql = "";

                sql = "select shop_id,\n" +
                            "dm_type,\n" +
                            "make_date,\n" +
                            "customer_id\n" +
                            "from data_dm_history_detail\n" +
                            "where shop_id = " + shop_id + "\n" +
                            "and dm_type = " + dm_type + "\n" +
                            "and TO_CHAR(make_date,'YYYY-MM-DD') = '" + make_date.toString() + "'\n" +
                            "and customer_id = " + intergration_id + "\n";

		return	sql;
	}

       /**
	*�t���[���ڃ��[�U�f�[�^�̃L�[�����擾
	* @return
	*/
	public String getFreeHeadingKeySQL(Integer free_heading_class_id,
                                            Integer intergration_id)
	{
                String sql = "";

                sql = "select customer_id,\n" +
                            "free_heading_class_id\n" +
                            "from mst_customer_free_heading\n" +
                            "where customer_id = " + intergration_id + "\n" +
                            "and free_heading_class_id = " + free_heading_class_id + "\n";

		return	sql;
	}

        /**
	 * �\��f�[�^�𓝍�����B
	 * @return
	 */
	public String getData_ReservationSQL(Integer customer_id,
                                                Integer shop_id,
                                                Integer reservation_no)
	{
		String	sql	= "update data_reservation\n" +
				" set\n";
		sql	+=	" customer_id = " + customer_id + ",\n";
		sql	+=	" update_date = current_timestamp\n" +
                                " where shop_id = " + shop_id + "\n" +
                                " and reservation_no = " + reservation_no + "\n";

		return	sql;
	}

         /**
	 * �\��f�[�^(TEMP)�𓝍�����B
	 * @return
	 */
	public String getData_ReservationTempSQL(Integer customer_id,
                                                Integer temp_no)
	{
		String	sql	= "update data_reservation_temp\n" +
				" set\n" +
                                " customer_id = " + customer_id + "\n" +
                                " where temp_no = " + temp_no + "\n";
                               
		return	sql;
	}

        /**
	 * �̔��f�[�^�𓝍�����B
	 * @return
	 */
	public String getData_SalesSQL(Integer customer_id,
                                                Integer shop_id,
                                                Integer slip_no)
	{
		String	sql	= "update data_sales\n" +
				" set\n" +
                                " customer_id = " + customer_id + ",\n" +
                                " update_date = current_timestamp\n" +
                                " where shop_id = " + shop_id + "\n" +
                                " and slip_no = " + slip_no + "\n";
		return	sql;
	}

        /**
	 * �ڋq�}�X�^�𓝍�����B
	 * @return
	 */
	public String getMst_CustomerSQL(Integer intergration_id)
	{
		String	sql	= "update mst_customer\n" +
				" set\n";
		sql	+=	" update_date = current_timestamp,\n" +
                                " delete_date = current_timestamp\n" +
                                " where customer_id = " + intergration_id + "\n";
		return	sql;
	}

        /**
	 * �|�C���g�����f�[�^�𓝍�����B
	 * @return
	 */
	public String getData_PointSQL(Integer customer_id,
                                                Integer shop_id,
                                                Integer point_id)
	{
		String	sql	= "update data_point\n" +
				" set\n" +
                                " customer_id = " + customer_id + ",\n" +
                                " update_date = current_timestamp\n" +
                                " where shop_id = " + shop_id + "\n" +
                                " and point_id = " + point_id + "\n";
		return	sql;
	}

        /**
	 * �C���[�W�J���e�f�[�^�𓝍�����B
	 * @return
	 */
	public String getData_Image_KarteSQL(Integer customer_id,
                                                Integer image_no,
                                                Integer image_id,
                                                String comment,
                                                Integer kbn)
	{
                String	sql;

                if (kbn == 1)
                {
                    sql	= "insert into data_image_karte\n" +
                                    " values(\n" +
                                    customer_id + ",\n" +
                                    image_no + ",\n" +
                                    image_id + ",\n" +
                                    "'" + comment + "',\n" +
                                    "current_timestamp, current_timestamp, null)\n";
                }
                else
                {
                    sql	= "update data_image_karte\n" +
                        " set\n" +
                        " update_date = current_timestamp,\n" +
                        " delete_date = current_timestamp\n" +
                        " where customer_id = " + customer_id + "\n" +
                        " and image_no = " + image_no + "\n";

                }

		return	sql;
	}

        /**
	 * DM�����ڍ׃f�[�^�𓝍�����B
	 * @return
	 */
	public String getData_Dm_HistorySQL(Integer shop_id,
                                                        Integer dm_type,
                                                        java.util.Date make_date,
                                                        Integer customer_id,
                                                        Integer intergration_id,
                                                        Integer datacnt)
	{
                String	sql = "";
                if (datacnt == 0)
                {
                    sql	= "update data_dm_history_detail\n" +
                                    " set\n" +
                                    " customer_id = " + customer_id + "\n" +
                                    " where shop_id = " + shop_id + "\n" +
                                    " and dm_type = " + dm_type + "\n" +
                                    " and TO_CHAR(make_date, 'YYYY-MM-DD') = '" + make_date.toString() + "'\n" +
                                    " and customer_id = " + intergration_id + "\n";
                }
                else
                {
                    sql = "delete from data_dm_history_detail\n" +
                                    " where shop_id = " + shop_id + "\n" +
                                    " and dm_type = " + dm_type + "\n" +
                                    " and TO_CHAR(make_date, 'YYYY-MM-DD') = '" + make_date.toString() + "'\n" +
                                    " and customer_id = " + intergration_id + "\n";
                }
		return	sql;
	}

        /**
	 * �t���[���ڃ��[�U�f�[�^�𓝍�����B
	 * @return
	 */
	public String getMst_Customer_Free_HeadingSQL(Integer free_heading_class_id,
                                                        Integer customer_id,
                                                        Integer intergration_id,
                                                        Integer datacnt)
	{
                String	sql = "";
                if (datacnt == 0)
                {
                    sql	= "update mst_customer_free_heading\n" +
                                    " set\n" +
                                    " customer_id = " + customer_id + ",\n" +
                                    " update_date = current_timestamp\n" +
                                    " where customer_id = " + intergration_id + "\n" +
                                    " and free_heading_class_id = " + free_heading_class_id + "\n";
                }
                else
                {
                    sql	= "update mst_customer_free_heading\n" +
                                    " set\n" +
                                    " update_date = current_timestamp,\n" +
                                    " delete_date = current_timestamp\n" +
                                    " where customer_id = " + intergration_id + "\n" +
                                    " and free_heading_class_id = " + free_heading_class_id + "\n";
                }
		return	sql;
	}

}








