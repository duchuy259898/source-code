/*
 * StringUtil.java
 *
 * Created on 2007/08/27, 16:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

/**
 *
 * @author katagiri
 */
public class StringUtil
{
	/**
	 * ������\�����̉������擾����
	 * @param font �����t�H���g
	 * @param str  �\��������
	 * @return �\��������̉�����Ԃ��܂�
	 */
	public static int getStringWidth( Font font, String str ) {
		Rectangle2D rectangle = font.getStringBounds( str, new FontRenderContext( new AffineTransform(), false, false ) );
		return (int)rectangle.getWidth();
	}
	
	/**
	 * ������\�����̏c�����擾����
	 * @param font �����t�H���g
	 * @return �\��������̏c����Ԃ��܂�
	 */
	public static int getStringHeight( Font font ) {
		Rectangle2D rectangle = font.getStringBounds( "a", new FontRenderContext( new AffineTransform(), false, false ) );
		return (int)rectangle.getHeight();
	}

        public static String replaceInvalidString(String s) {
            String result = "";

            result = s.replace("��", "��")
                      .replace("��", "��")
                      .replace("�", "��")
                      .replace("�", "��")
                      .replace("�M", "�_");

            return result;
        }
}
