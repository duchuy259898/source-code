/*
 * MstTechnicClasses.java
 *
 * Created on 2006/05/24, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * �R�[�X���ރ}�X�^�f�[�^��ArrayList
 * @author katagiri
 */
public class MstCourseClasses extends ArrayList<MstCourseClass>
{
	
	/**
	 * �R���X�g���N�^
	 */
	public MstCourseClasses()
	{
	}
	
	
	/**
	 * �R�[�X���ރ}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstCourseClasses.getSelectSQL());

		while(rs.next())
		{
			MstCourseClass	mtc	=	new	MstCourseClass();
			mtc.setData(rs);
			this.add(mtc);
		}

		rs.close();
	}
	
        // IVS_LeTheHieu Start delete 20140709 GB_MASHU_�R�[�X���ޓo�^
        /**
	 * �R�[�X���ރ}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	/*public static String getSelectSQL()
	{
		return	"select *\n" +
			"from mst_course_class\n" +
			"where delete_date is null\n" +
			"order by display_seq, course_class_name\n";
	}*/
        // IVS_LeTheHieu End delete 20140709 GB_MASHU_�R�[�X���ޓo�^
        
        // IVS_LeTheHieu Start add 20140709 GB_MASHU_�R�[�X���ޓo�^
        /**
	 * �R�[�X���ރ}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
        public static String getSelectSQL()
	{
		return "select class.*, integration.course_integration_name, integration.display_seq, \n" +
                        "class.shop_category_id, category.shop_class_name, class.delete_date from mst_course_class class left join \n" +
                        "(select * from mst_course_integration where delete_date is null) integration\n" +
                        "using (course_integration_id)\n" +
                        "left join (select * from mst_shop_category where delete_date is null) category\n" +
                        "using (shop_category_id)\n" +
                        "where class.delete_date is null\n" +
                        "order by class.display_seq";
        }
        // IVS_LeTheHieu End add 20140709 GB_MASHU_�R�[�X���ޓo�^
}
