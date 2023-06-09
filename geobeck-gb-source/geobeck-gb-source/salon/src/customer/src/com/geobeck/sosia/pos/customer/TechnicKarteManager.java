/*
 * TechnicKarteManager.java
 *
 * Created on 2008/09/08, 23:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;
import java.util.logging.Level;

import com.geobeck.sosia.pos.system.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 * 技術カルテ管理クラス
 * @author saito
 */
public class TechnicKarteManager
{
	/**
	 * 店舗
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * 伝票No.
	 */
	protected	Integer			slipNo		=	null;
	/**
	 * 技術カルテ分類一覧
	 */
	private	TechnicKarteClasses		technicKarteClasses	=	new TechnicKarteClasses();
	
	/**
	 * コンストラクタ
	 */
	public TechnicKarteManager()
	{
	}

	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * 店舗をセットする。
	 * @param shop 店舗
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * 伝票No.を取得する。
	 * @return 伝票No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * 伝票No.をセットする。
	 * @param slipNo 伝票No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * 技術カルテ分類一覧を取得する。
	 * @return 技術カルテ分類一覧
	 */
	public TechnicKarteClasses getTechnicKarteClasses()
	{
		return technicKarteClasses;
	}

	/**
	 * 技術カルテ分類一覧を設定する。
	 * @param technicKarteClasses 技術カルテ分類一覧
	 */
	public void setTechnicKarteClasses(TechnicKarteClasses technicKarteClasses)
	{
		this.technicKarteClasses = technicKarteClasses;
	}
	
	/**
	 * データを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, boolean farst) throws SQLException
	{
		if(shop.getShopID() == null || slipNo == null)
		{
			return	false;
		}
		
		if(farst){
//		    if(!technicKarteClasses.load(con))
//		    {
//			    return	false;
//		    }
//
//		    for(TechnicKarteClass tkc : technicKarteClasses)
//		    {
//			    if(!tkc.loadTechnicKartes(con))
//			    {
//				    return	false;
//			    } else {
//				for(TechnicKarte tk : tkc)
//				{
//					if(!tk.loadTechnicKarteReferences(con))
//					{
//						return	false;
//					}
//				}
//			    }
//		    }
		    
//		    for(TechnicKarteClass tkc : technicKarteClasses)
//		    {
//			for(TechnicKarte tk : tkc)
//			{
//			    System.out.println("" + tkc.getKarteClassId() + " : " + tk.getKarteDetailId());
//			    for(TechnicKarteReference tkr : tk)
//			    {
//				System.out.println("" + tkc.getKarteClassId() + " : " + tk.getKarteDetailId() + " : " + tkr.getKarteReferenceId());
//			    }
//			}
//		    }
		    
		    if(!this.loadMstKarteAll(con)){
			
		    }
		    
//		    System.out.println("new");
//		    
//		    for(TechnicKarteClass tkc : technicKarteClasses)
//		    {
//			for(TechnicKarte tk : tkc)
//			{
//			    System.out.println("" + tkc.getKarteClassId() + " : " + tk.getKarteDetailId());
//			    for(TechnicKarteReference tkr : tk)
//			    {
//				System.out.println("" + tkc.getKarteClassId() + " : " + tk.getKarteDetailId() + " : " + tkr.getKarteReferenceId());
//			    }
//			}
//		    }		    
		}
		
		if(!this.loadDataTechnicKarte(con))
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * 技術カルテデータを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean loadDataTechnicKarte(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getDataTechnicKarteSQL());
		
		while(rs.next())
		{
			if(technicKarteClasses.getTechnicKarteClass(rs.getInt("karte_class_id")) != null &&
				technicKarteClasses.getTechnicKarteClass(rs.getInt("karte_class_id")).getTechnicKarte(rs.getInt("karte_class_id"), rs.getInt("karte_detail_id")) != null){
				
			    technicKarteClasses.getTechnicKarteClass(rs.getInt("karte_class_id"))
				.getTechnicKarte(rs.getInt("karte_class_id"), rs.getInt("karte_detail_id"))
				    .setContents(rs.getString("contents"));
			}
		}
		
		rs.close();
		rs	=	null;
		
		return	true;
	}
	
	/**
	 * 技術カルテデータを読み込むＳＱＬ文を取得する。
	 * @return 技術カルテデータを読み込むＳＱＬ文
	 */
	private String getDataTechnicKarteSQL()
	{
//		return
//			"select\n" +
//			"dtk.shop_id,\n" +
//			"dtk.slip_no,\n" +
//			"dtk.karte_class_id,\n" +
//			"dtk.karte_detail_id,\n" +
//			"dtk.contents\n" +
//			"from data_technic_karte dtk\n" +
//			"inner join mst_karte_detail mkd\n" +
//			"on dtk.karte_class_id = mkd.karte_class_id\n" +
//                        "and dtk.karte_detail_id = mkd.karte_detail_id\n" +
//                        "and mkd.delete_date is null\n" +
//			"where dtk.delete_date is null\n" +
//			"and dtk.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
//			"and dtk.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
//                        "order by mkd.display_seq\n";
		return
			"select\n" +
			"dtk.shop_id,\n" +
			"dtk.slip_no,\n" +
			"mkd.karte_class_id,\n" +
			"mkd.karte_detail_id,\n" +
			"dtk.contents\n" +
			"from mst_karte_class mkc\n" +
			"inner join mst_karte_detail mkd\n" +
			"on mkc.karte_class_id = mkd.karte_class_id\n" +
                        "and mkd.delete_date is null\n" +
			"left join data_technic_karte dtk\n" +
			"on dtk.karte_class_id = mkd.karte_class_id\n" +
                        "and dtk.karte_detail_id = mkd.karte_detail_id\n" +
                        "and dtk.delete_date is null\n" +
			"and dtk.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and dtk.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
			"where mkc.delete_date is null\n" +
                        "order by mkd.display_seq\n";
	}
	
	/**
	 * 技術マスタを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean loadMstKarteAll(ConnectionWrapper con) throws SQLException
	{
	    technicKarteClasses.clear();
	    
	    ResultSetWrapper	rs	=	con.executeQuery(this.getMstKarteAllSQL());

	    while(rs.next())
	    {
		if(technicKarteClasses.size() == 0 
			|| technicKarteClasses.get(technicKarteClasses.size()-1).getKarteClassId().intValue() != rs.getInt("karte_class_id")){
		    TechnicKarteClass	tkc	=	new TechnicKarteClass();
		    tkc.setData(rs);
		    technicKarteClasses.add(tkc);
		}

		TechnicKarteClass karteClass = technicKarteClasses.get(technicKarteClasses.size() -1);

		if(karteClass.size() == 0 || karteClass.get(karteClass.size() - 1).getKarteDetailId().intValue() != rs.getInt("karte_detail_id")){
		    TechnicKarte	tk	=	new TechnicKarte();
		    tk.setData(rs);
		    tk.setTechnicKarteClass(karteClass);
		    karteClass.add(tk);
		}

		TechnicKarte karte = karteClass.get(karteClass.size()-1);
		if(karte.size() ==0 || karte.get(karte.size()-1).getKarteReferenceId().intValue() != rs.getInt("karte_reference_id")){
		    TechnicKarteReference	tkr	=	new TechnicKarteReference();
		    tkr.setData(rs);
		    tkr.setTechnicKarte(karte);
		    karte.add(tkr);
		}
	    }

	    rs.close();
	    rs	=	null;

	    return	true;
	}
	
	/**
	 * 技術カルテデータを読み込むＳＱＬ文を取得する。
	 * @return 技術カルテデータを読み込むＳＱＬ文
	 */
	private String getMstKarteAllSQL()
	{
	    return "select * \n" + 
		    "from mst_karte_class mkc \n" + 
		    "inner join mst_karte_detail mkd \n" + 
		    "on mkc.karte_class_id = mkd.karte_class_id \n" + 
		    "and mkd.delete_date is null \n" + 
		    "" + 
		    "left join mst_karte_reference mkr \n" + 
		    "on mkr.karte_detail_id = mkd.karte_detail_id \n" + 
		    "and mkr.delete_date is null \n" + 
		    "where mkc.delete_date is null \n" + 
		    "order by mkc.display_seq ,mkd.display_seq ,mkr.display_seq \n";
	}
	
	/**
	 * 登録処理を行う。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//元データを削除
		SystemInfo.getLogger().log(Level.INFO, "技術カルテ元データを削除 " + this.getShop().getShopID() + "slip_no=" + this.getSlipNo());
		if(!this.deleteData(con))
		{
			return	false;
		}
		//データを登録
		SystemInfo.getLogger().log(Level.INFO, "技術カルテデータを登録");
		if(!this.registData(con))
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * 元のデータを削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean deleteData(ConnectionWrapper con) throws SQLException
	{
		return (0 <= con.executeUpdate(this.getDeleteDataTechnicKarteSQL()));
	}
	
	/**
	 * 元のデータを削除するＳＱＬ文を取得する。
	 * @return 元のデータを削除するＳＱＬ文
	 */
	private String getDeleteDataTechnicKarteSQL()
	{
		return	"delete from data_technic_karte\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and  slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * データを登録する。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean registData(ConnectionWrapper con) throws SQLException
	{
		for(TechnicKarteClass tkc : technicKarteClasses)
		{
			for(TechnicKarte tk : tkc)
			{
				if(con.executeUpdate(this.getInsertDataTechnicKarteSQL(tk)) != 1)
				{
					return	false;
				}
			}
		}
		
		return	true;
	}
	
    /**
     * １件分のデータを登録するＳＱＬ文を取得する。
     * 
     * @param tk 登録する技術カルテ
     * @return １件分のデータを登録するＳＱＬ文
     */
	private String getInsertDataTechnicKarteSQL(TechnicKarte tk)
	{
		return	"insert into data_technic_karte \n" +
                                "(shop_id, slip_no, karte_class_id, karte_detail_id, contents,\n" +
                                "insert_date, update_date, delete_date)\n" +
                                "values(\n" +
                                SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
                                SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
                                SQLUtil.convertForSQL(tk.getKarteClassId()) + ",\n" +
                                SQLUtil.convertForSQL(tk.getKarteDetailId()) + ",\n" +
                                SQLUtil.convertForSQL(tk.getContents()) + ",\n" +
                                "current_timestamp, current_timestamp, null)\n";
	}
}
