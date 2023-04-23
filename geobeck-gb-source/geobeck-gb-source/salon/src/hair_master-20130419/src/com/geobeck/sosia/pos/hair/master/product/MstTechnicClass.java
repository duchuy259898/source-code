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
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * ‹Zp•ª—Şƒ}ƒXƒ^ƒf[ƒ^
 * @author katagiri
 */
public class MstTechnicClass extends ArrayList<MstTechnic>
{
	/**
	 * ‹Zp•ª—Ş‚h‚c
	 */
	protected	Integer		technicClassID		=	null;
	/**
	 * ‹Zp•ª—Ş–¼
	 */
	protected	String		technicClassName	=	"";
	/**
	 * ‹Zp•ª—Ş–¼—ªÌ
	 */
	protected	String		technicClassContractedName	=	"";
	/**
	 * •\¦‡
	 */
	protected	Integer		displaySeq			=	null;
	/**
	 * ƒvƒŠƒyƒCƒh
	 */
	protected	Integer		prepaid			=	null;
	/**
	 * “‡î•ñ
	 */
	protected	MstData		integration      =	null;
	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 */
	public MstTechnicClass()
	{
	}
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 * @param technicClassID ‹Zp•ª—Ş‚h‚c
	 */
	public MstTechnicClass(Integer technicClassID)
	{
		this.setTechnicClassID(technicClassID);
	}
	
	/**
	 * •¶š—ñ‚É•ÏŠ·‚·‚éBi‹Zp–¼j
	 * @return ‹Zp–¼
	 */
	public String toString()
	{
		return	this.getTechnicClassName();
	}

	/**
	 * ‹Zp•ª—Ş‚h‚c‚ğæ“¾‚·‚éB
	 * @return ‹Zp•ª—Ş‚h‚c
	 */
	public Integer getTechnicClassID()
	{
		return technicClassID;
	}

	/**
	 * ‹Zp•ª—Ş‚h‚c‚ğƒZƒbƒg‚·‚éB
	 * @param technicClassID ‹Zp•ª—Ş‚h‚c
	 */
	public void setTechnicClassID(Integer technicClassID)
	{
		this.technicClassID = technicClassID;
	}

	/**
	 * ‹Zp–¼‚ğæ“¾‚·‚éB
	 * @return ‹Zp–¼
	 */
	public String getTechnicClassName()
	{
		return technicClassName;
	}

	/**
	 * ‹Zp–¼‚ğƒZƒbƒg‚·‚éB
	 * @param technicClassName ‹Zp–¼
	 */
	public void setTechnicClassName(String technicClassName)
	{
		this.technicClassName = technicClassName;
	}

	/**
	 * ‹Zp•ª—Ş–¼—ªÌ‚ğæ“¾‚·‚éB
	 * @return ‹Zp•ª—Ş–¼—ªÌ
	 */
	public String getTechnicClassContractedName()
	{
		if(technicClassContractedName == null){
			return "";
		}
		return technicClassContractedName;
	}

	/**
	 * ‹Zp•ª—Ş–¼—ªÌ‚ğƒZƒbƒg‚·‚éB
	 * @param technicClassName ‹Zp•ª—Ş–¼—ªÌ
	 */
	public void setTechnicClassContractedName(String technicClassContractedName)
	{
		this.technicClassContractedName = technicClassContractedName;
	}
	
	/**
	 * •\¦‡‚ğæ“¾‚·‚éB
	 * @return •\¦‡
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * •\¦‡‚ğƒZƒbƒg‚·‚éB
	 * @param displaySeq •\¦‡
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}

        /**
         * @return the prepaid
         */
        public Integer getPrepaid() {
            return prepaid;
        }

        /**
         * @param prepaid the prepaid to set
         */
        public void setPrepaid(Integer prepaid) {
            this.prepaid = prepaid;
        }

        public Boolean isPrepaid() {
            return prepaid == 1;
        }

        /**
         * @return the integration
         */
        public MstData getIntegration() {
            return integration;
        }

        /**
         * @param integration the integration to set
         */
        public void setIntegration(MstData integration) {
            this.integration = integration;
        }
	
	public boolean equals(Object o)
	{
            if (o instanceof MstTechnicClass) {
                MstTechnicClass mtc = (MstTechnicClass)o;
                if (mtc.getTechnicClassID() == technicClassID &&
                    mtc.getTechnicClassName().equals(technicClassName) &&
                    mtc.getTechnicClassContractedName().equals( technicClassContractedName ) &&
                    mtc.getDisplaySeq() == displaySeq &&
                    mtc.getPrepaid() == prepaid &&
                    mtc.getIntegration() == integration)
                {
                    return true;
                }
            }

            return false;
	}
	
	/**
	 * ‹Zp•ª—Şƒ}ƒXƒ^ƒf[ƒ^‚©‚çƒf[ƒ^‚ğƒZƒbƒg‚·‚éB
	 * @param mtc ‹Zp•ª—Şƒ}ƒXƒ^ƒf[ƒ^
	 */
	public void setData(MstTechnicClass mtc)
	{
		this.setTechnicClassID(mtc.getTechnicClassID());
		this.setTechnicClassName(mtc.getTechnicClassName());
		this.setTechnicClassContractedName( mtc.getTechnicClassContractedName() );
		this.setDisplaySeq(mtc.getDisplaySeq());
		this.setPrepaid(mtc.getPrepaid());
                this.setIntegration(mtc.getIntegration());
	}
	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğƒZƒbƒg‚·‚é
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setTechnicClassID(rs.getInt("technic_class_id"));
		this.setTechnicClassName(rs.getString("technic_class_name"));
		this.setTechnicClassContractedName( rs.getString( "technic_class_contracted_name" ) );
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setPrepaid(rs.getInt("prepaid"));

                MstData data = new MstData(rs.getInt("technic_integration_id"), rs.getString("technic_integration_name"), rs.getInt("display_seq"));
                this.setIntegration(data);
	}
	
	
	/**
	 * ‹Zp•ª—Şƒ}ƒXƒ^‚Éƒf[ƒ^‚ğ“o˜^‚·‚éB
	 * @return true - ¬Œ÷
	 * @param lastSeq •\¦‡‚ÌÅ‘å’l
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
	 * ‹Zp•ª—Şƒ}ƒXƒ^‚©‚çƒf[ƒ^‚ğíœ‚·‚éBi˜_—íœj
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ¬Œ÷
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
	 * ‹Zp•ª—Şƒ}ƒXƒ^‚Éƒf[ƒ^‚ª‘¶İ‚·‚é‚©ƒ`ƒFƒbƒN‚·‚éB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ‘¶İ‚·‚é
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getTechnicClassID() == null || this.getTechnicClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * Select•¶‚ğæ“¾‚·‚éB
	 * @return Select•¶
	 */
	private String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      a.*");
            sql.append("     ,b.technic_integration_name");
            sql.append("     ,b.display_seq");
            sql.append(" from");
            sql.append("     mst_technic_class a");
            sql.append("         left join (select * from mst_technic_integration where delete_date is null) b");
            sql.append("         using (technic_integration_id)");
            sql.append(" where");
            sql.append("     technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()));

            return sql.toString();
	}
	
	/**
	 * •\¦‡‚ğ‚¸‚ç‚·‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @param seq Œ³‚Ì•\¦‡
	 * @param isIncrement true - ‰ÁZAfalse - Œ¸Z
	 * @return •\¦‡‚ğ‚¸‚ç‚·‚r‚p‚k•¶
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_technic_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert•¶‚ğæ“¾‚·‚éB
	 * @return Insert•¶
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_technic_class\n" +
				"(technic_class_id, technic_class_name, technic_class_contracted_name, display_seq, prepaid, technic_integration_id,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(technic_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getTechnicClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getTechnicClassContractedName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce(max(display_seq), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_technic_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				SQLUtil.convertForSQL(this.getPrepaid()) + ",\n" +
                                (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_technic_class\n";
	}
	
	/**
	 * Update•¶‚ğæ“¾‚·‚éB
	 * @return Update•¶
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_technic_class\n" +
				"set\n" +
				"technic_class_name = " + SQLUtil.convertForSQL(this.getTechnicClassName()) + ",\n" +
				"technic_class_contracted_name = " + SQLUtil.convertForSQL(this.getTechnicClassContractedName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_technic_class\n" +
				"where delete_date is null\n" +
				"and technic_class_id != " +
				SQLUtil.convertForSQL(this.getTechnicClassID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_technic_class\n" +
				"where delete_date is null\n" +
				"and technic_class_id != " +
				SQLUtil.convertForSQL(this.getTechnicClassID()) + "), 0) + 1 end,\n" +
				"prepaid = " + SQLUtil.convertForSQL(this.getPrepaid()) + ",\n" +
				"technic_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n";
	}
	
	/**
	 * íœ—pUpdate•¶‚ğæ“¾‚·‚éB
	 * @return íœ—pUpdate•¶
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_technic_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n";
	}
	
	/**
	 * ‹Zpƒ}ƒXƒ^ƒf[ƒ^‚ğArrayList‚É“Ç‚İ‚ŞB
	 * @param technicClassID ‹Zp•ª—Ş‚h‚c
	 */
	public void loadTechnic(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectTechnicSQL());

		while(rs.next())
		{
			MstTechnic	mt	=	new	MstTechnic();
			mt.setData(rs);
			this.add(mt);
		}

		rs.close();
	}
	
	/**
	 * ‹Zpƒ}ƒXƒ^ƒf[ƒ^‚ğ‘S‚Äæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚é
	 * @param technicClassID ‹Zp•ª—Ş‚h‚c
	 * @return ‹Zpƒ}ƒXƒ^ƒf[ƒ^‚ğ‘S‚Äæ“¾‚·‚é‚r‚p‚k•¶
	 */
	public String getSelectTechnicSQL()
	{
		return	"select *\n" +
			"from mst_technic\n" +
			"where delete_date is null\n" +
					"and technic_class_id = " +
					SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n" +
			"order by display_seq, technic_id\n";
	}
}
