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
 * タイムスケジュール画面用予約ヘッダデータ
 * @author katagiri
 */
public class ReservationHeader extends JTextField
{
	/**
	 * 店舗ＩＤ
	 */
	private	Object  shopId		=	null;
    
	/**
	 * ＩＤ
	 */
	protected	Object	id		=	null;
	/**
	 * 名称
	 */
	protected	String	name	=	"";
	/**
	 * スタッフ区分名
	 */
	private	String	className	=	"";
	/**
	 * タイムスケジュール画面用JTextFieldのリスト
	 */
	protected	ArrayList<ReservationJTextField>	reservations
			=	new ArrayList<ReservationJTextField>();
	
        /*
         * 稼動時間フラグ
         */
        private   Vector<Boolean> openTime = new Vector<Boolean>();

        /*
         * 日付
         */
        private     Date    reserveDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        public String getDateString(){
            return sdf.format(getReserveDate());
        }
        
	/**
	 * コンストラクタ
	 */
	public ReservationHeader()
	{
		super();
	}
	
	/**
	 * コンストラクタ
	 * @param text 名称
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
	 * ＩＤを取得する。
	 * @return ＩＤ
	 */
	public Object getId()
	{
		return id;
	}

	/**
	 * ＩＤをセットする。
	 * @param id ＩＤ
	 */
	public void setId(Object id)
	{
		this.id = id;
	}

	/**
	 * 名称を取得する。
	 * @return 名称
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 名称をセットする。
	 * @param name 名称
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
	 * タイムスケジュール画面用JTextFieldのリストを取得する。
	 * @return 予約データのリスト
	 */
	public ArrayList<ReservationJTextField> getReservations()
	{
		return reservations;
	}

	/**
	 * タイムスケジュール画面用JTextFieldのリストをセットする。
	 * @param reservations 予約データのリスト
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
