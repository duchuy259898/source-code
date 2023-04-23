/*
 * MstPurchaseClass.java
 *
 * Created on 2007/04/18, 16:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.product.*;

/**
 *
 * @author katagiri
 */
public class MstPurchaseClass extends ArrayList<MstPurchase>
{
	private	MstItemClass	itemClass	=	new MstItemClass();
	
	/** Creates a new instance of MstPurchaseClass */
	public MstPurchaseClass()
	{
		super();
	}
	
	/**
	 * ������ɕϊ�����B�i���i���ޖ��j
	 * @return ���i���ޖ�
	 */
	public String toString()
	{
		return	this.getItemClassName();
	}

	/**
	 * ���i���ނh�c���擾����B
	 * @return ���i���ނh�c
	 */
	public Integer getItemClassID()
	{
		return itemClass.getItemClassID();
	}

	/**
	 * ���i���ނh�c���Z�b�g����B
	 * @param itemClassID ���i���ނh�c
	 */
	public void setItemClassID(Integer itemClassID)
	{
		itemClass.setItemClassID(itemClassID);
	}

	/**
	 * ���i���ޖ����擾����B
	 * @return ���i���ޖ�
	 */
	public String getItemClassName()
	{
		return itemClass.getItemClassName();
	}

	/**
	 * ���i���ޖ����Z�b�g����B
	 * @param itemClassName ���i���ޖ�
	 */
	public void setItemClassName(String itemClassName)
	{
		itemClass.setItemClassName(itemClassName);
	}

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return itemClass.getDisplaySeq();
	}

	/**
	 * �\�������Z�b�g����B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		itemClass.setDisplaySeq(displaySeq);
	}

	public MstItemClass getItemClass()
	{
		return itemClass;
	}

	public void setItemClass(MstItemClass itemClass)
	{
		this.itemClass = itemClass;
	}
	
}
