/*
 * CTIEvent.java
 *
 * Created on 2008/02/18, 19:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.cti;

import java.util.EventObject;

/**
 * CTI�C�x���g�N���X
 * @author murakami
 */
public class CTIEvent extends EventObject
{
    
    /** Creates a new instance of CTIEvent */
	/**
	 * CTI
	 */
	protected	String		cti		=	"";
 
	//CTI�@�ڋq���
	private String			ctiCustomerData				=   "";			
	
	/**
	 * �R���X�g���N�^
	 * @param source 
	 * @param barcode CTI
	 */
	public CTIEvent(Object source, String cti)
	{
		super(source);
		this.setCTI(cti);
	}

	/**
	 * CTI���擾����
	 * @return CTI
	 */
	public String getCTI()
	{
		return cti;
	}

	/**
	 * CTI��ݒ肷��
	 * @param barcode CTI
	 */
	public void setCTI(String cti)
	{
		this.cti = cti;
	}    
	
	/**
	 * �d�b�������Ă������̏����擾
	 * @param ctiData CTI����̏��
	 * @return �d�b�������Ă��������
	 */
	public String takeCustomerData(){

	    int countNum	   =0;
	    for(int i=0 ; i < cti.length() ; i++){
		    char num =cti.charAt(i);
		    
		    if( num =='\n'){
			
		    }else if( Character.isWhitespace(num) ){
			countNum++;
			if(countNum==2)	    break;    
		    }else{
			ctiCustomerData += num;
		    }
	    }
	    return ctiCustomerData;
	}
	
}
