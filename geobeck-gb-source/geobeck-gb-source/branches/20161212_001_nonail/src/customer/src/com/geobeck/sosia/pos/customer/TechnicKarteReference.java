/*
 * TechnicKarteReference.java
 *
 * Created on 2008/09/21, 13:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import com.geobeck.sosia.pos.customer.TechnicKarte;
import java.sql.*;

import com.geobeck.sql.*;

/**
 * 技術カルテ参照データ
 * @author saito
 */
public class TechnicKarteReference<T>
{
	/**
	 * 技術カルテ
	 */
	protected	TechnicKarte	technicKarte	=	new TechnicKarte();
	/**
	 * カルテ参照ＩＤ
	 */
	protected Integer		karteReferenceId		=	null;
	/**
	 * カルテ参照名
	 */
	protected	String		karteReferenceName         =	null;
	/**
	 * カルテ詳細ＩＤ
	 */
	protected Integer		karteDetailId		=	null;
	/**
	 * 表示順
	 */
	protected Integer		displaySeq              =	null;
	/**
	 * ソースオブジェクト
	 */
        protected  T sourceObject = null;

	/**
	 * ソースオブジェクトを取得する。
	 * @return ソースオブジェクト
	 */
        public T getSourceObject() {
            return sourceObject;
        }

	/**
	 * ソースオブジェクトを設定する。
	 * @param sourceObject ソースオブジェクト
	 */
        public void setSourceObject(T sourceObject) {
            this.sourceObject = sourceObject;
        }
	
	/**
	 * コンストラクタ
	 */
	public TechnicKarteReference()
	{
	}

	/**
	 * 技術カルテを取得する。
	 * @return 技術カルテ
	 */
	public TechnicKarte getTechnicKarte()
	{
		return technicKarte;
	}

	/**
	 * 技術カルテを設定する。
	 * @param technicKarte 技術カルテ
	 */
	public void setTechnicKarte(TechnicKarte technicKarte)
	{
		this.technicKarte = technicKarte;
	}
	
	/**
	 * 文字列に変換する。（カルテ参照名）
	 * @return カルテ参照名
	 */
	public String toString()
	{
		return karteReferenceName;
	}

	/**
	 * カルテ参照ＩＤを取得する。
	 * @return カルテ参照ＩＤ
	 */
	public Integer getKarteReferenceId()
	{
		return karteReferenceId;
	}

	/**
	* カルテ参照ＩＤをセットする。
	* @param karteReferenceId カルテ参照ＩＤ
	*/
	public void setKarteReferenceId(Integer karteReferenceId)
	{
		this.karteReferenceId = karteReferenceId;
	}

	/**
	 * カルテ参照名を取得する。
	 * @return カルテ参照名
	 */
	public String getKarteReferenceName()
	{
		return karteReferenceName;
	}

	/**
	* カルテ参照名をセットする。
	* @param karteReferenceName カルテ参照名
	*/
	public void setKarteReferenceName(String karteReferenceName)
	{
                this.karteReferenceName	=	karteReferenceName;
	}

	/**
	 * カルテ詳細ＩＤを取得する。
	 * @return カルテ詳細ＩＤ
	 */
	public Integer getKarteDetailId()
	{
		return karteDetailId;
	}

	/**
	* カルテ詳細ＩＤをセットする。
	* @param karteDetailId カルテ詳細ＩＤ
	*/
	public void setKarteDetailId(Integer karteDetailId)
	{
		this.karteDetailId = karteDetailId;
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
                this.displaySeq	=	null;
	}
	
	/**
	 * データを設定する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteReferenceId(rs.getInt("karte_reference_id"));
		this.setKarteReferenceName(rs.getString("karte_reference_name"));
		this.setKarteDetailId(rs.getInt("karte_detail_id"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
}
