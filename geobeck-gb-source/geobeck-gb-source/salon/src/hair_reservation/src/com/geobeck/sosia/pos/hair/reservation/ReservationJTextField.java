/*
 * ReservationJTextField.java
 *
 * Created on 2006/06/16, 19:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.basicinfo.company.DataScheduleDetail;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.product.MstCustomerFreeHeading;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.DateUtil;
import com.geobeck.util.SQLUtil;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
        
        //IVS_LVTu start add 2015/09/07 New request #42428
        private	GregorianCalendar	currentDate	=	new GregorianCalendar();
        //IVS_ptquang start add 2016/09/05 New request #54112
        DataScheduleDetail dschedDetail = null;
        
        String shopName             = null; 
        
        //IVS_LVTu start add 2018/04/06 GB_Studio
        // スタジオ型予約枠管理
        protected       boolean staffHeader = false;
        //IVS_LVTu end add 2018/04/06 GB_Studio
               
        /**
         * IVS_ptquang start add 2016/09/05 New request #54112
         * @return 
         */
        public DataScheduleDetail getArrdschedDetail() {
            return dschedDetail;
        }
       /**
        * IVS_ptquang start add 2016/09/05 New request #54112
        * @param dschedDetail 
        */
        public void setArrdschedDetail(DataScheduleDetail dschedDetail) {
            this.dschedDetail = dschedDetail;
        }
        
        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public GregorianCalendar getCurrentDate() {
            return currentDate;
        }

        public void setCurrentDate(GregorianCalendar currentDate) {
            this.currentDate = currentDate;
        }
        //IVS_LVTu end add 2015/09/07 New request #42428
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
        
        //IVS_LVTu start add 2018/04/06 GB_Studio
        public boolean isStaffHeader() {
            return staffHeader;
        }

        public void setStaffHeader(boolean staffHeader) {
            this.staffHeader = staffHeader;
        }
        //IVS_LVTu end add 2018/04/06 GB_Studio
        	
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
     * IVS_ptquang add 2016/09/05 New request #54112
     * @param term
     * @param colWidth
     * @param rowHeight
     * @param rtf
     * @param rh 
     */  
    public void setValue(int term, int colWidth, int rowHeight, ReservationJTextField rtf, ReservationHeader rh )
    {
        long startTime = this.dschedDetail.getStartTime().getTime();
        long extEndTime = this.dschedDetail.getEndTime().getTime();

        long subTime = (extEndTime - startTime);
        long totalTime = TimeUnit.MILLISECONDS.toMinutes(subTime);
        int cols = (int) totalTime / term;
        if(0 < totalTime % term){
            cols++;
        }
        if (cols == 0) {
            cols = 1;
        }
        this.setSize(colWidth * cols - 1, rowHeight);
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
         * IVS_ptquang add 2016/09/05 New request #54112
         * @return hour
         */
        public int getHourForPartTime()
	{
            long date = this.dschedDetail.getStartTime().getTime();
            Date dateRegis = new Date(date);
            GregorianCalendar	cd	=	new GregorianCalendar();
            cd.setTime(dateRegis);

                    int hour = cd.get(Calendar.HOUR_OF_DAY);

                    if(cd.get(cd.DAY_OF_MONTH) != cd.get(Calendar.DAY_OF_MONTH))
                                    hour += 24;

            return hour;
		
	}
        /**
         * IVS_ptquang add 2016/09/05 New request #54112
         * @return 
         */
	public int getMinuteForPartTime()
	{
            long date = this.dschedDetail.getStartTime().getTime();
            Date dateRegis = new Date(date);
            GregorianCalendar	cd	=	new GregorianCalendar();
            cd.setTime(dateRegis);
		
            return cd.get(Calendar.MINUTE);
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
	public void setTexts(MstShop shop, boolean isOutStaff, HashMap<String, MstCustomerFreeHeading> mcFHs)
	{
            //IVS_LVTu start edit 2018/04/06 GB_Studio
            if(shop.isStudioFlag() && this.getReservation().getReservationStudioList().size() > 1) {
                StringBuilder text = new StringBuilder();
                
                text.append("<html>");
                text.append(reservation.getDRDTechnicName());
                text.append("[");
                if(this.staffHeader) {
                    text.append(reservation.getDRDBedName());
                }else {
                    text.append(reservation.getDRDFullStaffName());
                }
                text.append("]");
                text.append("<br>&nbsp;");
                text.append("参加人数 ").append(reservation.getReservationStudioList().size()).append("人");
                text.append("</html>");
                this.setText(text.toString());
            }else {
                String s =
                    "<html>&nbsp;" + reservation.getTechnicClassContractedName() +
                    (isOutStaff ? "（" + reservation.getDRDStaffName() + "）" : "") +
                    //"<br>&nbsp;" + (reservation.getDesignated() ? "[指]" : "[F]") +
                    "<br>&nbsp;" + (reservation.get(0).getDesignated() ? "[指]" : "[F]") +
                    (reservation.getComment().length() > 0 ? "[メモ]" : "") +
                    reservation.getCustomer().getFullCustomerName() + "</html>";
                this.setText(s);
            }
			this.setToolTipText("now loading...");
			this.shop = shop;
			this.mcFHs = mcFHs;
            //IVS_LVTu end edit 2018/04/06 GB_Studio
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
	
	/**
	 * toolTipTextCache
	 */
	private String toolTipTextCache;	
	/**
	 * shop
	 */
	private MstShop shop;	
	/**
	 * mcFHs
	 */
	private HashMap<String, MstCustomerFreeHeading> mcFHs;
	
	/**
	 * ツールチップテキストを取得する.
	 * @return ツールチップテキスト
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		if (toolTipTextCache==null) {

            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("<html><table>");
            //IVS_LVTu start edit 2018/04/06 GB_Studio
            if(shop.isStudioFlag() && this.getReservation().getReservationStudioList().size() > 1) {
                stringbuffer.append("<tr><th>開始時間</th><td>").append(DateUtil.format(reservation.getDRDStartReservationDatetime().getTime(), "HH:mm～")).append("</td></tr>");
                stringbuffer.append("<tr><th>担当者</th><td>").append(reservation.getDRDFullStaffName().replace("、", "</td></tr><tr><th></th><td>")).append("</td></tr>");
                stringbuffer.append("<tr><th>施術台</th><td>").append(reservation.getDRDBedName().replace("、", "</td></tr><tr><th></th><td>")).append("</td></tr>");
                stringbuffer.append("<tr><th>メニュー名</th><td>").append(reservation.getDRDTechnicName().replace("、", "</td></tr><tr><th></th><td>")).append("</td></tr>");
                
                boolean firstFlg = true;
                for(DataReservation dr : reservation.getReservationStudioList()) {
                    if(firstFlg) {
                        stringbuffer.append("<tr><th>参加者</th><td>").append(dr.getCustomer().getFullCustomerName()).append("</td></tr>");
                        firstFlg = false;
                    }else {
                        stringbuffer.append("<tr><th></th><td>").append(dr.getCustomer().getFullCustomerName()).append("</td></tr>");
                    }
                }
                //IVS_LVTu end edit 2018/04/06 GB_Studio
            }else {
                stringbuffer.append("<tr><th>顧客No.</th><td>" + reservation.getCustomer().getCustomerNo() + "</td></tr>");
                stringbuffer.append("<tr><th>顧客名</th><td>" + reservation.getCustomer().getFullCustomerName() + "（" 
                                                                    + reservation.getCustomer().getFullCustomerKana() + "）</td></tr>");

                String birth = "";
                if (reservation.getCustomer().getBirthday() != null) {
                    reservation.getCustomer().getBirthYear();
                    Integer ageTemp = DateUtil.calcAge(
                        new com.ibm.icu.util.GregorianCalendar(),
                        reservation.getCustomer().getBirthday());
                    //IVS_TMTrong start edit 2015/10/19 New request #43511
                    //IVS_LVTu start edit 2015/11/13 New request #44270
                    if(ageTemp>0){
                        //birth = reservation.getCustomer().getBirthdayString("yyyy年M月d日") + " (" + ageTemp + "歳)";
                        birth = reservation.getCustomer().getBirthdayString2() + " (" + ageTemp + "歳)";
                    }else{
                        //birth = reservation.getCustomer().getBirthdayString("yyyy年M月d日");
                        birth = reservation.getCustomer().getBirthdayString2();
                    }
                    //IVS_LVTu end edit 2015/11/13 New request #44270
                    //IVS_TMTrong end edit 2015/10/19 New request #43511
                }
                stringbuffer.append("<tr><th>誕生日</th><td>" + birth + "</td></tr>");

                stringbuffer.append("<tr><th>スタッフ</th><td>" + reservation.getDRDFullStaffName() + "</td></tr>");
                if (shop != null && shop.isBed()) {
                    //施術台有
                    stringbuffer.append("<tr><th>施術台</th><td>" + reservation.getDRDBedName() + "</td></tr>");
                }
                stringbuffer.append("<tr><th>技術</th><td>" + reservation.getDRDTechnicName() + "</td></tr>");
                stringbuffer.append("<tr><th>時間</th><td>" + this.getTermString() + "</td></tr>");
                stringbuffer.append("<tr><th valign='top'>予約メモ</th><td>" + ReservationStatusPanel.formatHTMLString
                                                                    (ReservationStatusPanel.insertNewLineString(reservation.getComment())) + "</td></tr>");

                //IVS_LVTu start add 2015/09/03 New request #42428
                //TEL
                String strPhoneNumber = reservation.getCustomer().getCellularNumber();
                if (strPhoneNumber == null || strPhoneNumber.equals("")) {
                    if ( reservation.getCustomer().getPhoneNumber() != null) {
                        strPhoneNumber = reservation.getCustomer().getPhoneNumber().equals("") ? "": reservation.getCustomer().getPhoneNumber() ;
                    } else {
                        strPhoneNumber = "";
                    }
                }
                stringbuffer.append("<tr><th>TEL</th><td>" + strPhoneNumber + "</td></tr>");

                String strVisitDate = "";
                String strNextDate = "";
                //前回来店日
                Date lastVisitDate = reservation.getLastVisitDate();

                if (lastVisitDate != null ) {
                    strVisitDate = DateUtil.format(lastVisitDate, "yyyy年M月d日 (E)");
                }

                stringbuffer.append("<tr><th>前回来店日</th><td>" + strVisitDate + "</td></tr>");
                stringbuffer.append("<tr><th>次回予約日</th><td>" + reservation.getNextReservationDateStrArray() + "</td></tr>");

                boolean flag=true;
                //フリー項目は10件固定
                for( int i = 1 ; i <= 10 ; i++ ){
                   String key = reservation.getCustomer().getCustomerID()  + "-"+ Integer.toString(i);
                   if(mcFHs.containsKey(key)){
                       MstCustomerFreeHeading mcFH = mcFHs.get(key);

                       String freeHeadingClassName = mcFH.getFreeHeadingClassName();
                       String freeHeadingName = mcFH.getFreeHeadingName();

                        if(freeHeadingName != null)
                        {
                           if(flag==true)
                           {
                                stringbuffer.append("<tr><th>フリー項目</th><td> </td></tr>");
                                flag=false;
                           }
                            stringbuffer.append("<tr><th colspan='2' align='left' valign='top'>" + freeHeadingClassName + "</th></tr>");
                            stringbuffer.append("<tr><td colspan='2' align='left'>" + freeHeadingName + "</td></tr>");
                        }
                   }
               }
                // IVS start add 20220907 カスタマイズ箇所・１：フロント業務＞＞予約表
                if (SystemInfo.getDatabase().equals("pos_hair_sunaocl")) {
                    TimeSchedule ts = new TimeSchedule();
                    String sql = ts.getNextReservationSql(shop.getShopID(), reservation.getCustomer().getCustomerID());
                    try
                    {
                        ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql);
                        int dataCount = 0;
                        while (rs.next()) {
                            if (dataCount == 0) {
                                stringbuffer.append("<tr><th colspan='2' align='left'>【将来の予約情報】</th></tr>");
                            }
                            stringbuffer.append("<tr><th align='left'>[予約日]</th><td>" + DateUtil.format(rs.getDate("reservation_date"), "M月d日") + "</td></tr>");
                            stringbuffer.append("<tr><th align='left'>[予約時間]</th><td>" + rs.getString("reservation_time") + "</td></tr>");
                            stringbuffer.append("<tr><th align='left' valign='top'>[予約メモ]</th><td>" + ReservationStatusPanel.formatHTMLString
                                                                    (ReservationStatusPanel.insertNewLineString(rs.getString("comment"))) + "</td></tr>");
                            stringbuffer.append("<tr><th colspan='2' align='left'></th></tr>");

                            dataCount++;
                        }

                        rs.close();
                    }
                    catch(SQLException e)
                    {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    }
                }
                // IVS end add 20220907 カスタマイズ箇所・１：フロント業務＞＞予約表
            }

            stringbuffer.append("</table></html>");
            //this.setToolTipText(stringbuffer.toString());
			toolTipTextCache = stringbuffer.toString();
		}
		return toolTipTextCache;
	}
}
