/*
 * XmlAnalyzer.java
 *
 * Created on 2007/10/19, 9:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import	java.io.*;
import	java.util.*;
import	javax.xml.parsers.*;
import	org.w3c.dom.*;

/**
 *
 * @author kanemoto
 */
public class XmlAnalyzer {
	/** �h�L�������g�r���_�[�t�@�N�g�� */
	DocumentBuilderFactory dbfactory = null;
	/** �h�L�������g�r���_�[ */
	DocumentBuilder builder = null;
	/** Document�I�u�W�F�N�g */
	Document doc = null;

	/** Creates a new instance of XmlAnalyzer */
	public XmlAnalyzer() {
	}
	
	/**
	 * �h�L�������g�r���_�[�̏��������s��
	 * @return ��������True��Ԃ�
	 */
	private boolean initDocumentBuilder() {
		try {
			// �h�L�������g�r���_�[�t�@�N�g���𐶐�
			dbfactory = DocumentBuilderFactory.newInstance();
			// �h�L�������g�r���_�[�𐶐�
			builder = dbfactory.newDocumentBuilder();
		} catch( Exception e ) {
			return false;
		}
		return true;
	}
	
	/**
	 * XML�f�[�^���Z�b�g����
	 * @param file XML�f�[�^
	 * @return ��������True��Ԃ�
	 */
	public boolean setXML( Document doc ) {
		if( !initDocumentBuilder() ) return false;
		// Document�I�u�W�F�N�g���擾
		this.doc = doc;
		return true;
	}
	
	/**
	 * XML�f�[�^���Z�b�g����
	 * @param file XML�f�[�^
	 * @return ��������True��Ԃ�
	 */
	public boolean setXML( File file ) {
		if( !initDocumentBuilder() ) return false;
		try {
			// �p�[�X�����s����Document�I�u�W�F�N�g���擾
			doc = builder.parse( file );
		} catch( Exception e ) {
			return false;
		}
		return true;
	}
	
	/**
	 * XML�f�[�^���Z�b�g����
	 * @param in XML�f�[�^
	 * @return ��������True��Ԃ�
	 */
	public boolean setXML( InputStream in ) {
		if( !initDocumentBuilder() ) return false;
		try {
			// �p�[�X�����s����Document�I�u�W�F�N�g���擾
			doc = builder.parse( in );
		} catch( Exception e ) {
			return false;
		}
		return true;
	}
	
	/**
	 * �w��G�������g���擾����
	 */
//	public NodeList getNodeList( String path ) {
//		try {
//			XObject xobj = XPathAPI.eval( doc, path );
//			return xobj.nodelist();
//		}
//		catch( Exception e ) {
//		}
//		return null;
//		
//	}
	
	public Element getElement( String path )
	{
		return getElement( doc.getDocumentElement(), path );
	}
	
	public String	getElementAttribute( String path, String attName )
	{
		return getElement( doc.getDocumentElement(), path ).getAttribute( attName );
	}

	
	public String	getElementNodeName( String path )
	{
		return getElement( doc.getDocumentElement(), path ).getFirstChild().getNodeValue();
	}
	
	public Element getElement( Element element, String path )
	{
		if( path == null ) return null;
		if( path.compareTo( "" ) == 0 ) return element;
		
		return getElement( getAnalyzeElement( element, splitPath( path, false ) ), splitPath( path, true ) );
	}
	
	private String splitPath( String path, boolean nextFlg ) {
		int sPos = path.indexOf( "/" );
		int fPos = 0;
		
		if( sPos == -1 ) {
			if( nextFlg ) return "";
			return        path;
		}
		
		if( sPos == 0 ) {
			fPos = path.indexOf( fPos );
		}
		else {
			fPos = sPos;
			sPos = 0;
		}
		
		if( nextFlg ) return path.substring( fPos + 1 );
		return path.substring( sPos, fPos );
	}
	
	private NodeList getAnalyzeNodeList( Element element, String path )
	{
		Element		ele;
		String		nodeName;
		
		if( 0 < path.indexOf( "[" ) ) {
			nodeName = path.substring( 0, path.indexOf( "[" ) );
		}
		else {
			nodeName = new String( path );
		}
		
		return element.getElementsByTagName( nodeName );
	}
	
	@SuppressWarnings("unchecked")
	private Element getAnalyzeElement( Element element, String path )
	{
		NodeList	nodeList;
		Element		ele;
		String		nodeName;
		Map			map = null;
		Set			set;
		String[]	keys;
		boolean		f;
		
		if( 0 < path.indexOf( "[" ) ) map = getHashMapParam( path.substring( path.indexOf( "[" ) + 1, path.lastIndexOf( "]" ) ) );
		nodeList = getAnalyzeNodeList( element, path );
		
		for( int i = 0; i < nodeList.getLength(); i++ ) {
			f = true;
			ele = (Element)nodeList.item( i );
			if( map != null )
			{
				set = map.keySet();
				keys = (String[])set.toArray(new String[set.size()]);
				for( String key : keys )
				{
					if( ele.getAttribute( key ).compareTo( (String)map.get( key ) ) != 0 ) f = false;
				}
			}
			if( f ) return ele;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Map getHashMapParam( String strParams ) {
		Map			retMap = new HashMap();
		String[]	params = strParams.split( "," );
		String[]	p;
		for( int i = 0; i < params.length; i++ ) {
			p = params[ i ].split( "=" );
			retMap.put( p[ 0 ], p[ 1 ] );
		}
		return retMap;
	}
}
