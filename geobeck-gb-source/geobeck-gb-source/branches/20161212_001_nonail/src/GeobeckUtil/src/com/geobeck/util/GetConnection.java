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
	 * �ʐM�擾�f�[�^���擾����
	 */
	public byte[]	getConnectionReceiveData()
	{
		return dataBuff;
	}
	
	/**
	 * GET�ʐM���s��
	 * @param	url				�ڑ���URL
	 * @param	X_WSSE_Header	X-WSSE�w�b�_
	 * @return	�ʐM��������True��Ԃ�
	 */
	public boolean connection( String url, String X_WSSE_Header ) throws Exception {
		// �ʐM�^�C�v�ŏ����𕪂���
		switch( getConnectionType( url ) )
		{
			// HTTP�ʐM
			case CONNECTION_TYPE_HTTP:
				return httpConnection( url, X_WSSE_Header );
			// HTTPS�ʐM
			case CONNECTION_TYPE_HTTPS:
				return httpsConnection( url, X_WSSE_Header );
			default:
				return false;
		}
	}
	
	/**
	 * �ڑ�URL����ʐM�^�C�v���擾����
	 * @param url �ڑ�URL
	 * @return �ڑ��^�C�v
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
	 * HTTP POST�ʐM���s��
	 * @param	url				�ڑ�URL
	 * @param	X_WSSE_Header	X-WSSE�w�b�_ 
	 * @return	��������True��Ԃ�
	 */
	private boolean httpConnection( String sendUrl, String X_WSSE_Header )
	{
//		InputStreamReader		in		=	null;
		InputStream				in		=	null;
		int						data;	
		ByteArrayOutputStream	bout;
		
		try {
            // URL�N���X�̃C���X�^���X�𐶐�
            URL url = new URL( sendUrl );
            
            // �ڑ����܂�
            HttpURLConnection conection = (HttpURLConnection)url.openConnection();

			// X-WSSE�w�b�_��o�^����
			if( X_WSSE_Header != null && 0 < X_WSSE_Header.length() ) {
				conection.addRequestProperty( "X-WSSE", X_WSSE_Header );
			}

			// �ʐM���\�b�h��POST�ɐݒ�
			conection.setRequestMethod( "GET" );
			
            // �o�͂��s���悤�ɐݒ肵�܂�
            conection.setDoOutput( false );

			// �ʐM���s
			conection.connect();
			
            // ���̓X�g���[�����擾
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
            // ���̓X�g���[������܂�
            try{ in.close(); } catch( Exception e ) {}
		}
		return true;
	}
	
	/**
	 * HTTPS POST�ʐM���s��
	 * @param	url			�ڑ�URL
	 * @param	X_WSSE_Header	X-WSSE�w�b�_ 
	 * @return	��������True��Ԃ�
	 */
	private boolean httpsConnection( String sendUrl, String X_WSSE_Header )
	{
//		InputStreamReader		in		=	null;
		InputStream				in		=	null;
		int						data;	
		ByteArrayOutputStream	bout;
		
		try {
            // URL�N���X�̃C���X�^���X�𐶐�
            URL url = new URL( sendUrl );
            
            // �ڑ����܂�
            HttpsURLConnection conection = (HttpsURLConnection)url.openConnection();

			// �ʐM���\�b�h��POST�ɐݒ�
			conection.setRequestMethod( "GET" );
			
			// X-WSSE�w�b�_��o�^����
			if( X_WSSE_Header != null && 0 < X_WSSE_Header.length() ) {
				conection.addRequestProperty( "X-WSSE", X_WSSE_Header );
			}
			
            // �o�͂��s���悤�ɐݒ肵�܂�
            conection.setDoOutput( false );

			// �ʐM���s
			conection.connect();
			
            // ���̓X�g���[�����擾
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
            // ���̓X�g���[������܂�
            try{ in.close(); } catch( Exception e ) {}
		}
		return true;
	}
}
