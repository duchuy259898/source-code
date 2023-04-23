/*
 * MstTechnicClass.java
 *
 * Created on 2006/05/24, 11:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * コース分類マスタデータ
 * @author furukawa
 */
public class MstCourseClass extends ArrayList<MstCourse>
{
	/**
	 * コース分類ＩＤ
	 */
	protected	Integer		courseClassID		=	null;
	/**
	 * コース分類名
	 */
	protected	String		courseClassName	=	"";
	/**
	 * コース分類名略称
	 */
	protected	String		courseClassContractedName	=	"";
	/**
	 * 表示順
	 */
	protected	Integer		displaySeq			=	null;
        // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
        protected	Integer		shopcategoryid          =	null;
        
        protected	String		shopClassName           =	"";
        
        protected	MstData         business                =       null;
        
        protected	MstData         courseintegration       =       null;
        
        protected	Integer		courseintegrationid     =	null;
        
        protected	String		courseintegrationname   =	"";
        
        private         Integer         useShopCategory =       null;
        
        private         Integer         shopId          =       null;
        
        private         MstShop         mstShop          = new MstShop();
        // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
	
	/**
	 * コンストラクタ
	 */
	public MstCourseClass()
	{
	}
	/**
	 * コンストラクタ
	 * @param technicClassID コース分類ＩＤ
	 */
	public MstCourseClass(Integer courseClassID)
	{
		this.setCourseClassID(courseClassID);
	}
	
	/**
	 * 文字列に変換する。（コース分類名）
	 * @return コース分類名
	 */
	public String toString()
	{
		return	this.getCourseClassName();
	}

	/**
	 * コース分類ＩＤを取得する。
	 * @return コース分類ＩＤ
	 */
	public Integer getCourseClassID()
	{
		return courseClassID;
	}

	/**
	 * コース分類ＩＤをセットする。
	 * @param technicClassID コース分類ＩＤ
	 */
	public void setCourseClassID(Integer courseClassID)
	{
		this.courseClassID = courseClassID;
	}

	/**
	 * コース分類名を取得する。
	 * @return コース分類名
	 */
	public String getCourseClassName()
	{
		return courseClassName;
	}

	/**
	 * コース分類名をセットする。
	 * @param courseClassName コース分類名
	 */
	public void setCourseClassName(String courseClassName)
	{
		this.courseClassName = courseClassName;
	}

	/**
	 * コース分類名略称を取得する。
	 * @return コース分類名略称
	 */
	public String getCourseClassContractedName()
	{
		if(courseClassContractedName == null){
			return "";
		}
		return courseClassContractedName;
	}

	/**
	 * コース分類名略称をセットする。
	 * @param technicClassName コース分類名略称
	 */
	public void setCourseClassContractedName(String courseClassContractedName)
	{
		this.courseClassContractedName = courseClassContractedName;
	}
	
	/**
	 * 表示順を取得する。
	 * @return 表示順
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * 表示順をセットする。
	 * @param displaySeq 表示順
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}
	
        // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
        public Integer getShopcategoryid() {
            return shopcategoryid;
        }

        public void setShopcategoryid(Integer shopcategoryid) {
            this.shopcategoryid = shopcategoryid;
        }
        
        public String getShopClassName() {
            return shopClassName;
        }

        public void setShopClassName(String shopClassName) {
            this.shopClassName = shopClassName;
        }
        
        public MstData getBusiness() {
            return business;
        }

        public void setBusiness(MstData business) {
            this.business = business;
        }

        public MstData getCourseintegration() {
            return courseintegration;
        }

        public void setCourseintegration(MstData courseintegration) {
            this.courseintegration = courseintegration;
        }

        public Integer getCourseintegrationid() {
            return courseintegrationid;
        }

        public void setCourseintegrationid(Integer courseintegrationid) {
            this.courseintegrationid = courseintegrationid;
        }

        public String getCourseintegrationname() {
            return courseintegrationname;
        }

        public void setCourseintegrationname(String courseintegrationname) {
            this.courseintegrationname = courseintegrationname;
        }

        public MstShop getMstShop() {
            return mstShop;
        }

        public void setMstShop(MstShop mstShop) {
            this.mstShop = mstShop;
        }
	// IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
        
	public boolean equals(Object o)
	{
		if(o instanceof MstCourseClass)
		{
			MstCourseClass	mtc	=	(MstCourseClass)o;
			
			if(mtc.getCourseClassID() == courseClassID &&
					mtc.getCourseClassName().equals(courseClassName) &&
					mtc.getCourseClassContractedName().equals( courseClassContractedName ) &&
					mtc.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * コース分類マスタデータからデータをセットする。
	 * @param mtc 技術分類マスタデータ
	 */
	public void setData(MstCourseClass mtc)
	{
		this.setCourseClassID(mtc.getCourseClassID());
		this.setCourseClassName(mtc.getCourseClassName());
		this.setCourseClassContractedName( mtc.getCourseClassContractedName() );
		this.setDisplaySeq(mtc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCourseClassID(rs.getInt("course_class_id"));
		this.setCourseClassName(rs.getString("course_class_name"));
		this.setCourseClassContractedName( rs.getString( "course_class_contracted_name" ) );
		this.setDisplaySeq(rs.getInt("display_seq"));
                // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
                MstData businessdata = new MstData(rs.getInt("shop_category_id"), rs.getString("shop_class_name"), rs.getInt("display_seq"));
                this.setBusiness(businessdata);
                MstData integrationdata = new MstData(rs.getInt("course_integration_id"), rs.getString("course_integration_name"), rs.getInt("display_seq"));
                this.setCourseintegration(integrationdata);
                // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
	}
	
	
	/**
	 * コース分類マスタにデータを登録する。
	 * @return true - 成功
	 * @param lastSeq 表示順の最大値
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	
	/**
	 * コース分類マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getDeleteSQL();
		}
		else
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * コース分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getCourseClassID() == null || this.getCourseClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_course_class\n"
			+	"where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_course_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
                // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
                Integer cateId;
                Integer courseId;
                if(this.getBusiness()== null){
                    cateId = null;
                }else{
                    cateId = this.getBusiness().getID();
                }
                
                if(this.getCourseintegration()== null){
                    courseId = null;
                }else{
                    courseId = this.getCourseintegration().getID();
                }
                // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
		return	"insert into mst_course_class\n" +
				"(course_class_id, course_class_name, course_class_contracted_name, display_seq, "
                                // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
                                + "shop_category_id, course_integration_id,\n" +
                                // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(course_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getCourseClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getCourseClassContractedName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce(max(display_seq), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_course_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
                                // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
                                SQLUtil.convertForSQL(cateId) + ",\n"+
                                SQLUtil.convertForSQL(courseId) + ",\n"+
                                // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
				"current_timestamp, current_timestamp, null\n" +
				"from mst_course_class\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
                // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
                Integer cateId = null;
                Integer courseId = null;
                shopId = mstShop.getShopID();
                useShopCategory = mstShop.getUseShopCategory();
                boolean isUpdate = false;
                
                // get courseId
                if(this.getCourseintegration()== null){
                    courseId = null;
                    }else{
                        courseId = this.getCourseintegration().getID();
                }
                
                // Only update cateId incase shopId=0 or useShopCategory = 1
                if (shopId.equals(0)){
                    isUpdate = true;
                   // get cateId
                   if(this.getBusiness()== null){
                    cateId = null;
                    }else{
                        cateId = this.getBusiness().getID();
                    }
                } else{
                    if (useShopCategory.equals(1)){
                        isUpdate = true;
                        // get cateId
                        if(this.getBusiness()== null){
                            cateId = null;
                        }else{
                            cateId = this.getBusiness().getID();
                        }
                    }
                    else{
                        isUpdate = false;
                    } 
                }
                if (isUpdate == true){
                    return	"update mst_course_class\n" +
                                    "set\n" +
                                    "course_class_name = " + SQLUtil.convertForSQL(this.getCourseClassName()) + ",\n" +
                                    "course_class_contracted_name = " + SQLUtil.convertForSQL(this.getCourseClassContractedName()) + ",\n" +
                                    // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
                                    "shop_category_id = " + SQLUtil.convertForSQL(cateId) + ",\n" +
                                    "course_integration_id = " + SQLUtil.convertForSQL(courseId) + ",\n" +
                                    // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
                                    "display_seq = case\n" +
                                    "when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
                                    " between 0 and coalesce((select max(display_seq)\n" +
                                    "from mst_course_class\n" +
                                    "where delete_date is null\n" +
                                    "and course_class_id != " +
                                    SQLUtil.convertForSQL(this.getCourseClassID()) + "), 0) then " +
                                    SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
                                    "else coalesce((select max(display_seq)\n" +
                                    "from mst_course_class\n" +
                                    "where delete_date is null\n" +
                                    "and course_class_id != " +
                                    SQLUtil.convertForSQL(this.getCourseClassID()) + "), 0) + 1 end,\n" +
                                    "update_date = current_timestamp\n" +
                                    "where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
                }
                // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
                return	"update mst_course_class\n" +
                                    "set\n" +
                                    "course_class_name = " + SQLUtil.convertForSQL(this.getCourseClassName()) + ",\n" +
                                    "course_class_contracted_name = " + SQLUtil.convertForSQL(this.getCourseClassContractedName()) + ",\n" +
                                    // IVS_LeTheHieu Start add 20140709 GB_MASHU_コース分類登録
                                    "course_integration_id = " + SQLUtil.convertForSQL(courseId) + ",\n" +
                                    // IVS_LeTheHieu End add 20140709 GB_MASHU_コース分類登録
                                    "display_seq = case\n" +
                                    "when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
                                    " between 0 and coalesce((select max(display_seq)\n" +
                                    "from mst_course_class\n" +
                                    "where delete_date is null\n" +
                                    "and course_class_id != " +
                                    SQLUtil.convertForSQL(this.getCourseClassID()) + "), 0) then " +
                                    SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
                                    "else coalesce((select max(display_seq)\n" +
                                    "from mst_course_class\n" +
                                    "where delete_date is null\n" +
                                    "and course_class_id != " +
                                    SQLUtil.convertForSQL(this.getCourseClassID()) + "), 0) + 1 end,\n" +
                                    "update_date = current_timestamp\n" +
                                    "where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_course_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * コース分類マスタデータをArrayListに読み込む。
	 * @param technicClassID 技術分類ＩＤ
	 */
	public void loadCourse(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectCourseSQL());

		while(rs.next())
		{
			MstCourse	mc	=	new	MstCourse();
			mc.setData(rs);
			this.add(mc);
		}

		rs.close();
	}
	
	/**
	 * コースマスタデータを全て取得するＳＱＬ文を取得する
	 * @param technicClassID コース分類ＩＤ
	 * @return コース分類マスタデータを全て取得するＳＱＬ文
	 */
	public String getSelectCourseSQL()
	{
		return	"select *\n" +
			"from mst_course\n" +
			"where delete_date is null\n" +
					"and course_class_id = " +
					SQLUtil.convertForSQL(this.getCourseClassID()) + "\n" +
			"order by display_seq, course_id\n";
	}
        
        //VUINV start add 20140717 MASHU_予約登録画面
        
        public int getShopCategoryIdByCourseId(ConnectionWrapper con, int courseId) throws SQLException {
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" SELECT mtc.shop_category_id ");
            sql.append(" FROM mst_course msc ");
            sql.append(" INNER JOIN mst_course_class mtc ON mtc.course_class_id = msc.course_class_id ");
            sql.append(" WHERE msc.course_id = ").append(SQLUtil.convertForSQL(courseId));
            sql.append(" AND msc.delete_date IS NULL ");
            sql.append(" AND mtc.delete_date IS NULL ");
            
            ResultSetWrapper	rs	=	con.executeQuery(sql.toString());
            
            if (rs.next()) {
                return rs.getInt("shop_category_id");
            }
            
            return 0;
        }
        
        //VUINV end add 20140717 MASHU_予約登録画面
        
}
