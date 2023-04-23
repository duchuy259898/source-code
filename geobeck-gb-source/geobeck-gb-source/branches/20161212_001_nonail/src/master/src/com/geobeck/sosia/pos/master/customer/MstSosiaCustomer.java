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
 * SOSIA�A���p�ڋq�f�[�^
 * @author kanemoto
 */
public class MstSosiaCustomer {
	/** SOSIA �h�c */
	protected	Integer		sosiaID			=	null;
	/** ���O */
	protected	String		sosiaName		=	null;
	/** ���� */
	protected	Integer		sex				=	null;
	/** �a���� */
	protected	String		birthday		=	null;
	/** �|�C���g */
	protected	Integer		point			=	null;
	/** �A���t���O */
	protected	boolean		connectingFlag	=	false;
	
	/** Creates a new instance of MstSosiaCustomer */
	public MstSosiaCustomer() {
	}
	
	/**
	 * SOSIA�������Ԃ�
	 */
	public boolean isSosiaCustomer()
	{
		return ( sosiaID != null && 0 < sosiaID );
	}
	
	/**
	 * SOSIA�h�c���擾����B
	 * @return SOSIA�h�c
	 */
	public Integer getSosiaID()
	{
		return sosiaID;
	}

	/**
	 * SOSIA�h�c���Z�b�g����B
	 * @param sosiaID SOSIA�h�c
	 */
	public void setSosiaID( Integer sosiaID )
	{
		this.sosiaID = sosiaID;
	}
	
	/**
	 * ���O���擾����B
	 * @return ���O
	 */
	public String getName()
	{
		return sosiaName;
	}

	/**
	 * ���O���Z�b�g����B
	 * @param sosiaName ���O
	 */
	public void setName( String sosiaName )
	{
		this.sosiaName = sosiaName;
	}
	
	/**
	 * ���ʂ��擾����B
	 * @return ����
	 */
	public Integer getSex()
	{
		return sex;
	}

	/**
	 * ���ʂ��Z�b�g����B
	 * @param sex ����
	 */
	public void setSex( Integer sex )
	{
		this.sex = sex;
	}
	
	/**
	 * �a�������擾����B
	 * @return �a����
	 */
	public String getBirthday()
	{
		return birthday;
	}

	/**
	 * �a�������Z�b�g����B
	 * @param birthday �a����
	 */
	public void setBirthday( String birthday )
	{
		this.birthday = birthday;
	}
	
	/**
	 * �|�C���g���擾����B
	 * @return �|�C���g
	 */
	public Integer getPoint()
	{
		return point;
	}

	/**
	 * �|�C���g���Z�b�g����B
	 * @param point �|�C���g
	 */
	public void setPoint( Integer point )
	{
		this.point = point;
	}
	
	/**
	 * �A���̗L�����擾����
	 * @return  �ڑ����ɂ�True��Ԃ�
	 */
	public boolean isConnecting()
	{
		return connectingFlag;
	}
	
	/**
	 * �A���̗L�����Z�b�g����
	 * @param connectingFlag �A���t���O
	 */
	private	void setConnectingFlag( boolean connectingFlag )
	{
		this.connectingFlag = connectingFlag;
	}
	
	/**
	 * �f�[�^���Z�b�g����
	 * @param msc SOSIA�ڋq�f�[�^
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
	 * �f�[�^���N���A����B
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
	 * �ݒ肳��Ă���SOSIAID�̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean load( String url, String X_WSSE_Header ) throws Exception
	{
		System.out.println("���R�l�N�V�����쐬�J�n");
		System.out.println("��url" + url);
		System.out.println("��X_WSSE_Header" + X_WSSE_Header);
		GetConnection gCon = new GetConnection();
		if( gCon.connection( url, X_WSSE_Header ) )
		{
			System.out.println("���R�l�N�V�����쐬����");
			setData( gCon.getConnectionReceiveData() );
			return true;
		}
		System.out.println("���R�l�N�V�����J��");
		this.clear();
		System.out.println("���R�l�N�V�����J������");
		return false;
	}
	
	/**
	 * �擾�f�[�^����SOSIA�ڋq�����擾����
	 */
	private void setData( byte[] setData )
	{
		System.out.println("���f�[�^�擾�J�n");
		XmlAnalyzer xa = new XmlAnalyzer();
		xa.setXML( new ByteArrayInputStream( setData ) );
		
		System.out.println("��xa.getElementNodeName( name/family-name )"+ xa.getElementNodeName( "name/family-name" ));
		System.out.println("��xa.getElementNodeName( name/four-name )"+ xa.getElementNodeName( "name/four-name" ));
		System.out.println("��xa.getElementNodeName( gender )"+ xa.getElementNodeName( "gender" ));
		System.out.println("��xa.getElementNodeName( birthday )"+ xa.getElementNodeName( "birthday" ));
		
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
