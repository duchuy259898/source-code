/*
 * GBMath.java
 *
 * Created on 2006/06/20, 19:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

/**
 * 数学関数クラス
 * @author katagiri
 */
public class GBMath
{
	
	/**
	 * コンストラクタ
	 */
	public GBMath()
	{
	}
	
	
	/**
	 * 最大公約数を取得する。
	 * @param m 数値１
	 * @param n 数値２
	 * @return 最大公約数
	 */
	public static int gcd(int m, int n)
	{
		int	t	=	0;

		while(n != 0)
		{
			t	=	m % n;
			m	=	n;
			n	=	t;
		}

		return Math.abs(m);
	}
}
