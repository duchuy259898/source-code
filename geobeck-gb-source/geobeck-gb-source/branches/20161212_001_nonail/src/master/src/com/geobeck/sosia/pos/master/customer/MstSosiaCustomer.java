/*
 * MstSosiaCustomer.java
 *
 * Created on 2007/10/18, 10:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import	java.io.*;
import	java.net.*;
import	com.geobeck.util.*;

/**
 * SOSIA連動用顧客データ
 * @author kanemoto
 */
public class MstSosiaCustomer {
	/** SOSIA ＩＤ */
	protected	Integer		sosiaID			=	null;
	/** 名前 */
	protected	String		sosiaName		=	null;
	/** 性別 */
	protected	Integer		sex				=	null;
	/** 誕生日 */
	protected	String		birthday		=	null;
	/** ポイント */
	protected	Integer		point			=	null;
	/** 連動フラグ */
	protected	boolean		connectingFlag	=	false;
	
	/** Creates a new instance of MstSosiaCustomer */
	public MstSosiaCustomer() {
	}
	
	/**
	 * SOSIA会員かを返す
	 */
	public boolean isSosiaCustomer()
	{
		return ( sosiaID != null && 0 < sosiaID );
	}
	
	/**
	 * SOSIAＩＤを取得する。
	 * @return SOSIAＩＤ
	 */
	public Integer getSosiaID()
	{
		return sosiaID;
	}

	/**
	 * SOSIAＩＤをセットする。
	 * @param sosiaID SOSIAＩＤ
	 */
	public void setSosiaID( Integer sosiaID )
	{
		this.sosiaID = sosiaID;
	}
	
	/**
	 * 名前を取得する。
	 * @return 名前
	 */
	public String getName()
	{
		return sosiaName;
	}

	/**
	 * 名前をセットする。
	 * @param sosiaName 名前
	 */
	public void setName( String sosiaName )
	{
		this.sosiaName = sosiaName;
	}
	
	/**
	 * 性別を取得する。
	 * @return 性別
	 */
	public Integer getSex()
	{
		return sex;
	}

	/**
	 * 性別をセットする。
	 * @param sex 性別
	 */
	public void setSex( Integer sex )
	{
		this.sex = sex;
	}
	
	/**
	 * 誕生日を取得する。
	 * @return 誕生日
	 */
	public String getBirthday()
	{
		return birthday;
	}

	/**
	 * 誕生日をセットする。
	 * @param birthday 誕生日
	 */
	public void setBirthday( String birthday )
	{
		this.birthday = birthday;
	}
	
	/**
	 * ポイントを取得する。
	 * @return ポイント
	 */
	public Integer getPoint()
	{
		return point;
	}

	/**
	 * ポイントをセットする。
	 * @param point ポイント
	 */
	public void setPoint( Integer point )
	{
		this.point = point;
	}
	
	/**
	 * 連動の有無を取得する
	 * @return  接続時にはTrueを返す
	 */
	public boolean isConnecting()
	{
		return connectingFlag;
	}
	
	/**
	 * 連動の有無をセットする
	 * @param connectingFlag 連動フラグ
	 */
	private	void setConnectingFlag( boolean connectingFlag )
	{
		this.connectingFlag = connectingFlag;
	}
	
	/**
	 * データをセットする
	 * @param msc SOSIA顧客データ
	 */
	public void setData( MstSosiaCustomer msc )
	{
		this.setSosiaID( msc.getSosiaID() );
		this.setName( msc.getName() );
		this.setSex( msc.getSex() );
		this.setBirthday( msc.getBirthday() );
		this.setPoint( msc.getPoint() );
		this.setConnectingFlag( msc.isConnecting() );
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setSosiaID( null );
		this.setName( null );
		this.setSex( null );
		this.setBirthday( null );
		this.setPoint( null );
		this.setConnectingFlag( false );
	}
	
	/**
	 * 設定されているSOSIAIDのデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean load( String url, String X_WSSE_Header ) throws Exception
	{
		System.out.println("★コネクション作成開始");
		System.out.println("★url" + url);
		System.out.println("★X_WSSE_Header" + X_WSSE_Header);
		GetConnection gCon = new GetConnection();
		if( gCon.connection( url, X_WSSE_Header ) )
		{
			System.out.println("★コネクション作成成功");
			setData( gCon.getConnectionReceiveData() );
			return true;
		}
		System.out.println("★コネクション開放");
		this.clear();
		System.out.println("★コネクション開放成功");
		return false;
	}
	
	/**
	 * 取得データからSOSIA顧客情報を取得する
	 */
	private void setData( byte[] setData )
	{
		System.out.println("★データ取得開始");
		XmlAnalyzer xa = new XmlAnalyzer();
		xa.setXML( new ByteArrayInputStream( setData ) );
		
		System.out.println("★xa.getElementNodeName( name/family-name )"+ xa.getElementNodeName( "name/family-name" ));
		System.out.println("★xa.getElementNodeName( name/four-name )"+ xa.getElementNodeName( "name/four-name" ));
		System.out.println("★xa.getElementNodeName( gender )"+ xa.getElementNodeName( "gender" ));
		System.out.println("★xa.getElementNodeName( birthday )"+ xa.getElementNodeName( "birthday" ));
		
		this.setName( xa.getElementNodeName( "name/family-name" ) + " " + xa.getElementNodeName( "name/four-name" ) );
		this.setSex(
			( ( xa.getElementNodeName( "gender" ) ).compareTo( "male" ) == 0 ? 1 : 2 )
		);
		// 1985-03-16
		this.setBirthday( xa.getElementNodeName( "birthday" ).substring( 5 ) );
		this.setPoint( 0 );
		this.setConnectingFlag( true );
	}
}
