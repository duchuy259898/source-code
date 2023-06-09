/*
 * GetConnection.java
 *
 * Created on 2007/10/23, 18:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import	java.io.*;
import	java.net.*;
import	javax.net.ssl.*;
/**
 *
 * @author kanemoto
 */
public class GetConnection {
	public	final int		CONNECTION_TYPE_NULL	=	0;
	public	final int		CONNECTION_TYPE_HTTP	=	1;
	public	final int		CONNECTION_TYPE_HTTPS	=	2;
	
	private			byte[]		dataBuff		= null;
	
	/**
	 * 通信取得データを取得する
	 */
	public byte[]	getConnectionReceiveData()
	{
		return dataBuff;
	}
	
	/**
	 * GET通信を行う
	 * @param	url				接続先URL
	 * @param	X_WSSE_Header	X-WSSEヘッダ
	 * @return	通信成功時にTrueを返す
	 */
	public boolean connection( String url, String X_WSSE_Header ) throws Exception {
		// 通信タイプで処理を分ける
		switch( getConnectionType( url ) )
		{
			// HTTP通信
			case CONNECTION_TYPE_HTTP:
				return httpConnection( url, X_WSSE_Header );
			// HTTPS通信
			case CONNECTION_TYPE_HTTPS:
				return httpsConnection( url, X_WSSE_Header );
			default:
				return false;
		}
	}
	
	/**
	 * 接続URLから通信タイプを取得する
	 * @param url 接続URL
	 * @return 接続タイプ
	 */
	private int getConnectionType( String url )
	{
		String	cType;
		int		retType = CONNECTION_TYPE_NULL;
		
		int fPos = url.indexOf( "://" );
		if( 0 < fPos )
		{
			cType = url.substring( 0, fPos );
			if( cType.compareTo( "http"  ) == 0 )	retType = CONNECTION_TYPE_HTTP;
			if( cType.compareTo( "https" ) == 0 )	retType = CONNECTION_TYPE_HTTPS;
		}
		return retType;
	}
	
	/**
	 * HTTP POST通信を行う
	 * @param	url				接続URL
	 * @param	X_WSSE_Header	X-WSSEヘッダ 
	 * @return	成功時にTrueを返す
	 */
	private boolean httpConnection( String sendUrl, String X_WSSE_Header )
	{
//		InputStreamReader		in		=	null;
		InputStream				in		=	null;
		int						data;	
		ByteArrayOutputStream	bout;
		
		try {
            // URLクラスのインスタンスを生成
            URL url = new URL( sendUrl );
            
            // 接続します
            HttpURLConnection conection = (HttpURLConnection)url.openConnection();

			// X-WSSEヘッダを登録する
			if( X_WSSE_Header != null && 0 < X_WSSE_Header.length() ) {
				conection.addRequestProperty( "X-WSSE", X_WSSE_Header );
			}

			// 通信メソッドをPOSTに設定
			conection.setRequestMethod( "GET" );
			
            // 出力を行うように設定します
            conection.setDoOutput( false );

			// 通信実行
			conection.connect();
			
            // 入力ストリームを取得
//			in = new InputStreamReader( conection.getInputStream() );
			in = conection.getInputStream();
			bout = new ByteArrayOutputStream();
			try
			{
				while( ( data = in.read() ) != -1 )
				{
					bout.write( data );
				}
			}
			catch( Exception e ) {
				return false;
			}
			finally {
				try{ in.close(); } catch( Exception exception ) {}
			}
			dataBuff = bout.toByteArray();
		}
		catch( IOException e ) {
			return false;
		}
		catch( Exception e ) {
			return false;
		}
		finally {
            // 入力ストリームを閉じます
            try{ in.close(); } catch( Exception e ) {}
		}
		return true;
	}
	
	/**
	 * HTTPS POST通信を行う
	 * @param	url			接続URL
	 * @param	X_WSSE_Header	X-WSSEヘッダ 
	 * @return	成功時にTrueを返す
	 */
	private boolean httpsConnection( String sendUrl, String X_WSSE_Header )
	{
//		InputStreamReader		in		=	null;
		InputStream				in		=	null;
		int						data;	
		ByteArrayOutputStream	bout;
		
		try {
            // URLクラスのインスタンスを生成
            URL url = new URL( sendUrl );
            
            // 接続します
            HttpsURLConnection conection = (HttpsURLConnection)url.openConnection();

			// 通信メソッドをPOSTに設定
			conection.setRequestMethod( "GET" );
			
			// X-WSSEヘッダを登録する
			if( X_WSSE_Header != null && 0 < X_WSSE_Header.length() ) {
				conection.addRequestProperty( "X-WSSE", X_WSSE_Header );
			}
			
            // 出力を行うように設定します
            conection.setDoOutput( false );

			// 通信実行
			conection.connect();
			
            // 入力ストリームを取得
//			in = new InputStreamReader( conection.getInputStream() );
			in = conection.getInputStream();
			bout = new ByteArrayOutputStream();
			try
			{
				while( ( data = in.read() ) != -1 )
				{
					bout.write( data );
				}
			}
			catch( Exception e ) {
				return false;
			}
			finally {
				try{ in.close(); } catch( Exception exception ) {}
			}
			dataBuff = bout.toByteArray();
		}
		catch( IOException e ) {
			return false;
		}
		catch( Exception e ) {
			return false;
		}
		finally {
            // 入力ストリームを閉じます
            try{ in.close(); } catch( Exception e ) {}
		}
		return true;
	}
}
