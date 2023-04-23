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
 * CTIイベントクラス
 * @author murakami
 */
public class CTIEvent extends EventObject
{
    
    /** Creates a new instance of CTIEvent */
	/**
	 * CTI
	 */
	protected	String		cti		=	"";
 
	//CTI　顧客情報
	private String			ctiCustomerData				=   "";			
	
	/**
	 * コンストラクタ
	 * @param source 
	 * @param barcode CTI
	 */
	public CTIEvent(Object source, String cti)
	{
		super(source);
		this.setCTI(cti);
	}

	/**
	 * CTIを取得する
	 * @return CTI
	 */
	public String getCTI()
	{
		return cti;
	}

	/**
	 * CTIを設定する
	 * @param barcode CTI
	 */
	public void setCTI(String cti)
	{
		this.cti = cti;
	}    
	
	/**
	 * 電話をかけてきた方の情報を取得
	 * @param ctiData CTIからの情報
	 * @return 電話をかけてきた方情報
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
