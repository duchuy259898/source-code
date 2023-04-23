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
	 * 文字列表示時の横幅を取得する
	 * @param font 文字フォント
	 * @param str  表示文字列
	 * @return 表示文字列の横幅を返します
	 */
	public static int getStringWidth( Font font, String str ) {
		Rectangle2D rectangle = font.getStringBounds( str, new FontRenderContext( new AffineTransform(), false, false ) );
		return (int)rectangle.getWidth();
	}
	
	/**
	 * 文字列表示時の縦幅を取得する
	 * @param font 文字フォント
	 * @return 表示文字列の縦幅を返します
	 */
	public static int getStringHeight( Font font ) {
		Rectangle2D rectangle = font.getStringBounds( "a", new FontRenderContext( new AffineTransform(), false, false ) );
		return (int)rectangle.getHeight();
	}

        public static String replaceInvalidString(String s) {
            String result = "";

            result = s.replace("髙", "高")
                      .replace("﨑", "崎")
                      .replace("邊", "辺")
                      .replace("邉", "辺")
                      .replace("濵", "濱");

            return result;
        }
}
