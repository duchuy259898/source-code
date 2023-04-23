/*
 * WildcardFileFilter.java
 *
 * Created on 2006/05/30, 11:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing.filechooser;

import java.io.*;

/**
 * JFileChooser�p��FileFilter
 * @author katagiri
 */
public class WildcardFileFilter extends javax.swing.filechooser.FileFilter
{
	/**
	 * �g���q�̃t�B���^������
	 */
	private	String	extension		=	".*\\.exe";
	/**
	 * ����
	 */
	private String	description		=	"exe file";
	
	/**
	 * �R���X�g���N�^
	 * @param wildcard �g���q�̃t�B���^������
	 * @param description ����
	 */
	public WildcardFileFilter(String wildcard, String description)
	{
		this.extension		=	this.convertWildCard(wildcard);
		this.description	=	description;
	}
	
	/**
	 * �g���q�̃t�B���^�������ϊ�����B�i�G�X�P�[�v�������j
	 * @param wildcard �g���q�̃t�B���^������
	 * @return �ϊ����ꂽ�g���q�̃t�B���^������
	 */
	private String convertWildCard(String wildcard)
	{
		wildcard	=	wildcard.replace(';', '|');
		wildcard	=	wildcard.replace('*', '/');
		wildcard	=	wildcard.replaceAll("\\.", "\\\\.");
		wildcard	=	wildcard.replaceAll("/.", ".*");
		wildcard	=	wildcard.replace("?", ".");
		return	wildcard;
	}
	
	/**
	 * �t�@�C����\������K�v������ꍇ�� true ��Ԃ��܂��B
	 * @param f �t�@�C��
	 * @return true - �t�@�C����\������K�v������ꍇ
	 */
	public boolean accept(File f)
	{
		if(f.isDirectory())	return	true;
		
		String	name	=	f.getName().toLowerCase();
		return	name.matches(extension);
	}
	
	/**
	 * �������擾����B
	 * @return ����
	 */
	public String getDescription()
	{
		return	description;
	}
}
