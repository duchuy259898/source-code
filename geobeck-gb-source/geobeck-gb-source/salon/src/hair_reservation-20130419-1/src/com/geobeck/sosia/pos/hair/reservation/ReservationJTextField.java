/*
 * ReservationJTextField.java
 *
 * Created on 2006/06/16, 19:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.product.MstCustomerFreeHeading;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.DateUtil;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import javax.swing.*;

/**
 * タイムスケジュール画面用JTextField
 * @author katagiri
 */
public class ReservationJTextField extends JLabel
{
	/**
	 * タイムスケジュール画面用予約ヘッダデータ
	 */
	protected	ReservationHeader header = null;
	/**
	 * 予約データ
	 */
	protected	DataReservation	reservation = new DataReservation();

        /**
         * 予約日
         */
        protected       java.util.Date reservationDate = null;

	/**
	 * コンストラクタ
	 */
	public ReservationJTextField()
	{
            super();
            this.setOpaque(true);
	}

	/**
	 * コンストラクタ
	 * @param text 名称
	 */
	public ReservationJTextField(String text)
	{
		super(text);
	}

	/**
	 * タイムスケジュール画面用予約ヘッダデータを取得する。
	 * @return タイムスケジュール画面用予約ヘッダデータ
	 */
	public ReservationHeader getHeader()
	{
		return header;
	}

	/**
	 * タイムスケジュール画面用予約ヘッダデータをセットする。
	 * @param header タイムスケジュール画面用予約ヘッダデータ
	 */
	public void setHeader(ReservationHeader header)
	{
		this.header = header;
	}

	/**
	 * 予約データを取得する。
	 * @return 予約データ
	 */
	public DataReservation getReservation()
	{
		return reservation;
	}

	/**
	 * 予約データをセットする。
	 * @param reservation 予約データ
	 */
	public void setReservation(DataReservation reservation)
	{
		this.reservation = reservation;
	}
	
        public java.util.Date getReservationDate() {
            return reservationDate;
        }

        public void setReservationDate(java.util.Date reservationDate) {
            this.reservationDate = reservationDate;
        }
	
	/**
	 * サイズを設定する。
	 * @param term 表示単位
	 * @param colWidth 列の幅
	 * @param rowHeight 行の高さ
	 */
	public void setSize(int term, int colWidth, int rowHeight)
	{
		//施術時間が設定されていない場合１列分の幅にする
		if(reservation.getTotalTime() == null)
		{
			this.setSize(colWidth - 1, rowHeight);
		}
		else
		{
			//列幅を（施術時間÷表示単位）の切り上げで取得する
			int	cols	=	reservation.getTotalTime() / term;
			if(0 < reservation.getTotalTime() % term)
					cols ++;
			if(cols == 0)	cols = 1;
			this.setSize(colWidth * cols - 1, rowHeight);
		}
	}
	
	/**
	 * 時間を取得する。
	 * @param currentDate 基準となる日付
	 * @return 時間
	 */
	public int getHour(Date currentDate)
	{
		GregorianCalendar	cd	=	new GregorianCalendar();
		cd.setTime(currentDate);
		
		if(reservation.getDRDStartReservationDatetime() == null)
		{
			return	0;
		}
		else
		{
			int hour = reservation.getDRDStartReservationDatetime().get(Calendar.HOUR_OF_DAY);
			
			if(cd.get(cd.DAY_OF_MONTH) != reservation.getDRDStartReservationDatetime().get(Calendar.DAY_OF_MONTH))
					hour += 24;
			
			return hour;
		}
	}
	
	/**
	 * 分を取得する。
	 * @return 分
	 */
	public int getMinute()
	{
		if(reservation.getDRDStartReservationDatetime() == null)
		{
			return	0;
		}
		else
		{
			return	reservation.getDRDStartReservationDatetime().get(Calendar.MINUTE);
		}
	}
	
	public String getTermString()
	{
		GregorianCalendar endTime = new GregorianCalendar();
		endTime.setTime(reservation.getDRDStartReservationDatetime().getTime());
		endTime.add(endTime.MINUTE, reservation.getTotalTime());
		return getFormatTime(reservation.getDRDStartReservationDatetime()) + "～" + getFormatTime(endTime);
	}
	
        private String getFormatTime(Calendar cal) {

                int h = cal.get(Calendar.HOUR_OF_DAY);
                int m = cal.get(Calendar.MINUTE);
                if (Integer.parseInt(DateUtil.format(reservationDate, "dd")) + 1 == cal.get(Calendar.DAY_OF_MONTH)) {
                    h += 24;
                }

                return String.format("%1$02d", h) + ":" + String.format("%1$02d", m);
        }
	
	/**
	 * TextとToolTipTextをセットする。
	 */
	public void setTexts(MstShop shop, boolean isOutStaff)
	{
            String s =
                "<html>&nbsp;" + reservation.getTechnicClassContractedName() +
                (isOutStaff ? "（" + reservation.getDRDStaffName() + "）" : "") +
                "<br>&nbsp;" + (reservation.getDesignated() ? "[指]" : "[F]") +
                (reservation.getComment().length() > 0 ? "[メモ]" : "") +
                reservation.getCustomer().getFullCustomerName() + "</html>";

            this.setText(s);

            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("<html><table>");
            stringbuffer.append("<tr><th>顧客No.</th><td>" + reservation.getCustomer().getCustomerNo() + "</td></tr>");
            stringbuffer.append("<tr><th>顧客名</th><td>" + reservation.getCustomer().getFullCustomerName() + "（" + reservation.getCustomer().getFullCustomerKana() + "）</td></tr>");

            String birth = "";
            if (reservation.getCustomer().getBirthday() != null) {
                Integer ageTemp = DateUtil.calcAge(
                    new com.ibm.icu.util.GregorianCalendar(),
                    reservation.getCustomer().getBirthday());
                birth = reservation.getCustomer().getBirthdayString("yyyy年M月d日") + " (" + ageTemp + "歳)";
            }
            stringbuffer.append("<tr><th>誕生日</th><td>" + birth + "</td></tr>");

            stringbuffer.append("<tr><th>スタッフ</th><td>" + reservation.getDRDFullStaffName() + "</td></tr>");
            if (shop != null && shop.isBed()) {
                //施術台有
                stringbuffer.append("<tr><th>施術台</th><td>" + reservation.getDRDBedName() + "</td></tr>");
            }
            stringbuffer.append("<tr><th>技術</th><td>" + reservation.getDRDTechnicName() + "</td></tr>");
            stringbuffer.append("<tr><th>時間</th><td>" + this.getTermString() + "</td></tr>");
            stringbuffer.append("<tr><th valign='top'>予約メモ</th><td>" + ReservationStatusPanel.formatHTMLString(ReservationStatusPanel.insertNewLineString(reservation.getComment())) + "</td></tr>");

/**
            // フリー項目
            ArrayList<MstCustomerFreeHeading> mcfhs = new ArrayList<MstCustomerFreeHeading>();
	    try {
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getFreeHeadingSelectSQL());
		while (rs.next()) {
                    MstCustomerFreeHeading mt = new MstCustomerFreeHeading();
                    mt.setData(rs);
                    mcfhs.add(mt);
		}
		rs.close();
	    } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

            for ( MstCustomerFreeHeading mcfh : mcfhs ) {
                String freeHeadingClassName = mcfh.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassName();
                String freeHeadingName = mcfh.getMstFreeHeading().getFreeHeadingName();
                if (freeHeadingName != null) {
                    stringbuffer.append("<tr><th colspan='2' align='left' valign='top'>" + freeHeadingClassName + "</th></tr>");
                    stringbuffer.append("<tr><td colspan='2' align='left'>" + freeHeadingName + "</td></tr>");
                }
            }
**/
            stringbuffer.append("</table></html>");
            this.setToolTipText(stringbuffer.toString());
	}

	/**
	 * フリー項目取得
	 * @return Select文
	 */
	private String getFreeHeadingSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mfhc.*");
            sql.append("     ,mc.*");
            sql.append("     ,mfh.*");
            sql.append("     ,coalesce( mfh.free_heading_id, -1 )");
            sql.append("     ,mfh.free_heading_name");
            sql.append("     ,mfh.display_seq");
            sql.append(" from");
            sql.append("     mst_free_heading_class as mfhc");
            sql.append("         left join mst_customer_free_heading as mcfh");
            sql.append("                on mcfh.delete_date is null");
            sql.append("               and mcfh.customer_id = " + SQLUtil.convertForSQL(reservation.getCustomer().getCustomerID()));
            sql.append("               and mcfh.free_heading_class_id = mfhc.free_heading_class_id");
            sql.append("         left join mst_customer as mc");
            sql.append("                on mc.delete_date is null");
            sql.append("               and mc.customer_id = " + SQLUtil.convertForSQL(reservation.getCustomer().getCustomerID()));
            sql.append("         left join mst_free_heading as mfh");
            sql.append("                on mfh.delete_date is null");
            sql.append("               and mfh.free_heading_class_id = mcfh.free_heading_class_id");
            sql.append("               and mfh.free_heading_id = mcfh.free_heading_id");
            sql.append(" where");
            sql.append("         mfhc.delete_date is null");
            sql.append("     and mfhc.use_type = 't'");
            sql.append(" order by");
            sql.append("     mfhc.free_heading_class_id");

            return sql.toString();
	}

}
