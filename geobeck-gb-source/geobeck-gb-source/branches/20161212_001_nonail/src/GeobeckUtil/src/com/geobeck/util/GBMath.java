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
 * ���w�֐��N���X
 * @author katagiri
 */
public class GBMath
{
	
	/**
	 * �R���X�g���N�^
	 */
	public GBMath()
	{
	}
	
	
	/**
	 * �ő���񐔂��擾����B
	 * @param m ���l�P
	 * @param n ���l�Q
	 * @return �ő����
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
