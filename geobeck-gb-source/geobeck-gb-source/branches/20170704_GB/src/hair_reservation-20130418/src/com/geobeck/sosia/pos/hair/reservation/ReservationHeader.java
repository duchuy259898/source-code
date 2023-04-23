/*
 * ReservationHeader.java
 *
 * Created on 2006/06/16, 19:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

/**
 * �^�C���X�P�W���[����ʗp�\��w�b�_�f�[�^
 * @author katagiri
 */
public class ReservationHeader extends JTextField
{
	/**
	 * �X�܂h�c
	 */
	private	Object  shopId		=	null;
    
	/**
	 * �h�c
	 */
	protected	Object	id		=	null;
	/**
	 * ����
	 */
	protected	String	name	=	"";
	/**
	 * �X�^�b�t�敪��
	 */
	private	String	className	=	"";
	/**
	 * �^�C���X�P�W���[����ʗpJTextField�̃��X�g
	 */
	protected	ArrayList<ReservationJTextField>	reservations
			=	new ArrayList<ReservationJTextField>();
	
        /*
         * �ғ����ԃt���O
         */
        private   Vector<Boolean> openTime = new Vector<Boolean>();

        /*
         * ���t
         */
        private     Date    reserveDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        public String getDateString(){
            return sdf.format(getReserveDate());
        }
        
	/**
	 * �R���X�g���N�^
	 */
	public ReservationHeader()
	{
		super();
	}
	
	/**
	 * �R���X�g���N�^
	 * @param text ����
	 */
	public ReservationHeader(String text)
	{
		super(text);
	}
	
	public String toString()
	{
		return	this.getName();
	}

	/**
	 * �h�c���擾����B
	 * @return �h�c
	 */
	public Object getId()
	{
		return id;
	}

	/**
	 * �h�c���Z�b�g����B
	 * @param id �h�c
	 */
	public void setId(Object id)
	{
		this.id = id;
	}

	/**
	 * ���̂��擾����B
	 * @return ����
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * ���̂��Z�b�g����B
	 * @param name ����
	 */
	public void setName(String name)
	{
		this.name = name;
                
                if (className != null && className.length() > 0) {
                    
                    this.setText(name + "(" + className + ")");

                } else {

                    this.setText(name);
                }
	}

	/**
	 * �^�C���X�P�W���[����ʗpJTextField�̃��X�g���擾����B
	 * @return �\��f�[�^�̃��X�g
	 */
	public ArrayList<ReservationJTextField> getReservations()
	{
		return reservations;
	}

	/**
	 * �^�C���X�P�W���[����ʗpJTextField�̃��X�g���Z�b�g����B
	 * @param reservations �\��f�[�^�̃��X�g
	 */
	public void setReservations(ArrayList<ReservationJTextField> reservations)
	{
		this.reservations = reservations;
	}

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Vector<Boolean> getOpenTime() {
            return openTime;
        }

        public void setOpenTime(Vector<Boolean> openTime) {
            this.openTime = openTime;
        }

        public Date getReserveDate() {
            return reserveDate;
        }

        public void setReserveDate(Date reserveDate) {
            this.reserveDate = reserveDate;
        }

        public Object getShopId() {
            return shopId;
        }

        public void setShopId(Object shopId) {
            this.shopId = shopId;
        }

}
