/*
 * DateUtil.java
 *
 * Created on 2006/05/01, 11:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.util.*;
import java.text.*;

import com.ibm.icu.util.*;


/**
 * ���t�p���[�e�B���e�B
 * @author katagiri
 */
public class DateUtil
{
	
	/**
	 * �R���X�g���N�^
	 */
	public DateUtil()
	{
	}
	
	/**
	 * ���t���w�肵�������̕�����ŕԂ��B
	 * @param date ���t
	 * @param pattern ����
	 * @return ������ɕϊ��������t
	 */
	public static String format(Date date, String pattern)
	{
		if(date == null)	return	null;
		
		SimpleDateFormat	sdf	=	new SimpleDateFormat(pattern);
		
		return	sdf.format(date);
	}
	
	
	/**
	 * �N����擾����B
	 * @param base ��ƂȂ���t
	 * @param birthday �a����
	 * @return �N��
	 */
	public static Integer calcAge(java.util.Calendar base,
			java.util.Calendar birthday)
	{
		Integer	age		=	null;
		
		if(base != null && birthday != null && base.before(birthday))
		{
			age	=	birthday.get(base.YEAR) - birthday.get(birthday.YEAR);
			
			if(birthday.get(birthday.DAY_OF_YEAR) <= base.get(base.DAY_OF_YEAR))
			{
				age += 1;
			}
		}
		
		return	age;
	}
	
	
	/**
	 * �N����擾����B
	 * @param base ��ƂȂ���t
	 * @param birthday �a����
	 * @return �N��
	 */
	public static Integer calcAge(com.ibm.icu.util.Calendar base,
			com.ibm.icu.util.Calendar birthday)
	{
//            begin edit TMTrong 20150706 Bug #39342
//		Integer	age		=	null;
//		
//		if(base != null && birthday != null && base.after(birthday))
//		{
//			age	=	base.get(base.EXTENDED_YEAR) - birthday.get(birthday.EXTENDED_YEAR);
//			
//			if(base.get(base.DAY_OF_YEAR) <= birthday.get(birthday.DAY_OF_YEAR))
//			{
//				age -= 1;
//			}
//		}
//		
//		return	age;
            //IVS_TMTrong start add 2015/10/19 New request #43511
             Integer age = 0;
            // enter year 1900 = 33
            // enter year 0000 => birthday = null
            if(birthday != null && base != null && base.after(birthday)){
                //if(((Integer)birthday.get(com.ibm.icu.util.Calendar.YEAR)!=33)&&  birthday.get(birthday.EXTENDED_YEAR)>1900){
                if(birthday.get(birthday.EXTENDED_YEAR)>1900){
                    age = base.get(base.EXTENDED_YEAR) - birthday.get(birthday.EXTENDED_YEAR);

                    if ((birthday.get(com.ibm.icu.util.Calendar.MONTH) > base.get(com.ibm.icu.util.Calendar.MONTH))
                        || (birthday.get(com.ibm.icu.util.Calendar.MONTH) == base.get(com.ibm.icu.util.Calendar.MONTH) && birthday.get(com.ibm.icu.util.Calendar.DAY_OF_MONTH) > base
                            .get(com.ibm.icu.util.Calendar.DAY_OF_MONTH))) {
                      age--;
                    }
                }
                //IVS_TMTrong end add 2015/10/19 New request #43511
            }
            return	age;
            //end edit TMTrong 20150706 Bug #39342
	}
        
	public static Date toDate(String dateString)
	{
		SimpleDateFormat	sdf	=	new SimpleDateFormat("yyyy/MM/dd");
		
		return	sdf.parse(dateString, new ParsePosition(0));
	}
	
	
	public static java.util.GregorianCalendar dateToGregorianCalendar(Date date)
	{
		if(date == null)	return	null;
		
		java.util.GregorianCalendar		temp	=	new java.util.GregorianCalendar();
		temp.setTime(date);
		return	temp;
	}
	
	public static java.util.Calendar getLastDate(java.util.Calendar cal)
	{
		if(cal == null)
		{
			return	null;
		}
		
		java.util.Calendar	temp	=	java.util.Calendar.getInstance();
		temp.setTime(cal.getTime());
		
		temp.add(java.util.Calendar.MONTH, 1);
		temp.set(java.util.Calendar.DAY_OF_MONTH, 1);
		temp.add(java.util.Calendar.DAY_OF_MONTH, -1);
		
		return	temp;
	}
	
	public static Long getDateDifference(int field,
			java.util.Calendar base,
			java.util.Calendar target)
	{
		return	null;
	}
}
