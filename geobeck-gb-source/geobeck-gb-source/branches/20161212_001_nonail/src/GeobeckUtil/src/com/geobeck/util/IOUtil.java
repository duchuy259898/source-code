/*
 * IOUtil.java
 *
 * Created on 2006/07/03, 20:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.io.*;

/**
 * ���o�͊֘A���[�e�B���e�B
 * @author katagiri
 */
public class IOUtil
{
	
	/**
	 * �R���X�g���N�^
	 */
	public IOUtil()
	{
	}
	
	
	/**
	 * �t�@�C�������݂��邩���`�F�b�N����B
	 * @param filePath �`�F�b�N����t�@�C���̃p�X
	 * @return ���݂���ꍇtrue
	 */
	public static boolean isExistFile(String filePath)
	{
		File	f	=	new File(filePath);
		
		if(f.exists())
		{
			return	true;
		}
		
		return	false;
	}
}
