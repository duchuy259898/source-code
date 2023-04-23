/*
 * MailUtil.java
 *
 * Created on 2006/11/06, 11:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.mail;

import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sosia.pos.hair.master.product.MstTechnic;
import com.geobeck.sosia.pos.hair.pointcard.PointData;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.mail.*;
import com.geobeck.sosia.pos.system.*;
import javax.mail.internet.MimeUtility;

/**
 * ƒ[ƒ‹ƒ†[ƒeƒBƒŠƒeƒB
 * @author katagiri
 */
public class MailUtil
{
	/**
	 * “X•Ü–¼‚ÌƒL[
	 */
	public static String	KEY_TARGET_NAME		=	"[“X•Ü–¼]";
	/**
	 * “X•ÜZŠ‚ÌƒL[
	 */
	public static String	KEY_TARGET_ADDRESS	=	"[“X•ÜZŠ]";
	/**
	 * “X•ÜTEL‚ÌƒL[
	 */
	public static String	KEY_TARGET_TEL		=	"[“X•ÜTEL]";
	/**
	 * “X•Üƒ[ƒ‹ƒAƒhƒŒƒX‚ÌƒL[
	 */
	public static String	KEY_TARGET_MAIL		=	"[“X•Üƒ[ƒ‹ƒAƒhƒŒƒX]";
	/**
	 * ŒÚ‹q–¼‚ÌƒL[
	 */
	public static String	KEY_CUSTOMER_NAME	=	"[ŒÚ‹q–¼]";
	/**
	 * ’S“–Ò–¼‚ÌƒL[
	 */
	public static String	KEY_STAFF_NAME		=	"[’S“–Ò–¼]";
	/**
	 * ‘O‰ñ—ˆ“X“ú‚ÌƒL[
	 */
	public static String	KEY_LAST_VISIT		=	"[‘O‰ñ—ˆ“X“ú]";
	/**
	 * ƒ|ƒCƒ“ƒg‚ÌƒL[
	 */
	public static String	KEY_POINT		=	"[ƒ|ƒCƒ“ƒg]";

	/**
	 * Ÿ‰ñ—\–ñ“ú‚ÌƒL[
	 */
	public static String	KEY_NEXT_RESERVATION	=	"[Ÿ‰ñ—\–ñ“ú]";
	/**
	 * —\–ñ’S“–Ò‚ÌƒL[
	 */
	public static String	KEY_RESERVATION_STAFF	=	"[—\–ñ’S“–Ò]";
	/**
	 * Ÿ‰ñ—\–ñ“à—e‚ÌƒL[
	 */
	public static String	KEY_NEXT_RESERVATION_MENU =     "[Ÿ‰ñ—\–ñ“à—e]";
        
	/**
	 * “X•Ü–¼‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_TARGET_NAME_D	=	"\\[“X•Ü–¼\\]";
	/**
	 * “X•ÜZŠ‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_TARGET_ADDRESS_D	=	"\\[“X•ÜZŠ\\]";
	/**
	 * “X•ÜTEL‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_TARGET_TEL_D	=	"\\[“X•ÜTEL\\]";
	/**
	 * “X•Üƒ[ƒ‹ƒAƒhƒŒƒX‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_TARGET_MAIL_D	=	"\\[“X•Üƒ[ƒ‹ƒAƒhƒŒƒX\\]";
	/**
	 * ŒÚ‹q–¼‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_CUSTOMER_NAME_D	=	"\\[ŒÚ‹q–¼\\]";
	/**
	 * ’S“–Ò–¼‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_STAFF_NAME_D	=	"\\[’S“–Ò–¼\\]";
	/**
	 * ‘O‰ñ—ˆ“X“ú‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_LAST_VISIT_D	=	"\\[‘O‰ñ—ˆ“X“ú\\]";
	/**
	 * ƒ|ƒCƒ“ƒg‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_POINT_D	=	"\\[ƒ|ƒCƒ“ƒg\\]";
	/**
	 * ¤•i‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_ITEM_D		=	"\\[¤•i[^\\]]*\\]";
	/**
	 * ‹Zp‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_TECHNIC_D		=	"\\[‹Zp[^\\]]*\\]";
        /**
	 * ‹Zp‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_COURSE_D		=	"\\[ƒR[ƒX[^\\]]*\\]";
	/**
	 * –¼‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_SIGNATURE_D		=	"\\[–¼[1-3]\\]";

	/**
	 * Ÿ‰ñ—\–ñ“ú‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_NEXT_RESERVATION_D	=	"\\[Ÿ‰ñ—\–ñ“ú\\]";
	/**
	 * —\–ñ’S“–Ò‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_RESERVATION_STAFF_D	=	"\\[—\–ñ’S“–Ò\\]";
	/**
	 * Ÿ‰ñ—\–ñ“à—e‚ÌƒL[‚Ì³‹K•\Œ»
	 */
	private static String	KEY_NEXT_RESERVATION_MENU_D	=	"\\[Ÿ‰ñ—\–ñ“à—e\\]";
        
        private static  DataMail mailPlantext;
        
        public  static  boolean isCcMail = false;

	/**
	 * ƒL[€–Ú‚ğƒfƒR[ƒh‚·‚éB
	 * @param dm ƒ[ƒ‹ƒf[ƒ^
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
       
	public static String decodeKey(DataMail dm, Object target)
	{
                return decodeKeyFunction(dm, target, false);
	}
	public static String decodeKeyTitle(DataMail dm, Object target)
	{
                return decodeKeyFunction(dm, target, true);
	}

	private static String decodeKeyFunction(DataMail dm, Object target, boolean isTitle)
	{
		String result = isTitle ? dm.getMailTitle() : dm.getMailBody();

		if(target instanceof MstGroup) {
                    // –{•”‘I‘ğ‚Ìê‡‚ÍÅI—ˆ“X“X•Ü‚ğ•\¦
                    result = MailUtil.decodeKeyTarget(result, dm.getShop());
                } else {
                    result = MailUtil.decodeKeyTarget(result, target);
                }

		result = MailUtil.decodeKeyCustomerName(result, dm.getFullCustomerName());

		result = MailUtil.decodeKeyStaffName(result, dm.getCustomerID());

		result = MailUtil.decodeKeyLastVisit(result, dm.getCustomerID());

		result = MailUtil.decodeKeyPoint(result, dm.getCustomerID());

		result = MailUtil.decodeKeyItem(result, null);

		result = MailUtil.decodeKeyTechnic(result, null);
                
                result = MailUtil.decodeKeyCourse(result, null);

		result = MailUtil.decodeKeySignature(result, target, null);

		result = MailUtil.decodeKeyNextReservation(result, dm.getCustomerID());

                result = MailUtil.decodeKeyReservationStaff(result, dm.getCustomerID());

		result = MailUtil.decodeKeyNextReservationMenu(result, dm.getCustomerID());
                
		return result;
	}

	private static String decodeKeyTarget(String value, Object target)
	{
		String result = value;
		
		if(target instanceof MstGroup)
		{
			MstGroup	mg	=	(MstGroup)target;
			
			result	=	result.replaceAll(KEY_TARGET_NAME_D, mg.getGroupName());
			result	=	result.replaceAll(KEY_TARGET_ADDRESS_D, mg.getFullAddress());
			result	=	result.replaceAll(KEY_TARGET_TEL_D, mg.getPhoneNumber());
			result	=	result.replaceAll(KEY_TARGET_MAIL_D, mg.getMailAddress());
		}
		else if(target instanceof MstShop)
		{
			MstShop		ms	=	(MstShop)target;
			
			result	=	result.replaceAll(KEY_TARGET_NAME_D, ms.getShopName());
			result	=	result.replaceAll(KEY_TARGET_ADDRESS_D, ms.getFullAddress());
			result	=	result.replaceAll(KEY_TARGET_TEL_D, ms.getPhoneNumber());
			result	=	result.replaceAll(KEY_TARGET_MAIL_D, ms.getMailAddress());
		}
		
		return	result;
	}
	
	/**
	 * ŒÚ‹q–¼‚ÌƒL[‚ğƒfƒR[ƒh‚·‚éB
	 * @param value ‘ÎÛ‚Æ‚È‚é•¶š—ñ
	 * @param customerName ŒÚ‹q–¼
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
	private static String decodeKeyCustomerName(String value, String customerName)
	{
		String	result = value;
		return	result.replaceAll(KEY_CUSTOMER_NAME_D, customerName);
	}

	/**
	 * ‘O‰ñ’S“–Ò–¼‚ÌƒL[‚ğƒfƒR[ƒh‚·‚éB
	 * @param value ‘ÎÛ‚Æ‚È‚é•¶š—ñ
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
	private static String decodeKeyStaffName(String value, Integer customerID)
	{
		String result = value;
                String replaceStr = MailUtil.getLastStaffName(customerID);
                if (replaceStr == null) replaceStr = "";
                return result.replaceAll(KEY_STAFF_NAME_D, replaceStr);
	}
	
	
	/**
	 * ‘O‰ñ’S“–Ò–¼‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ‘O‰ñ’S“–Ò–¼
	 */
	private static String getLastStaffName(Integer customerID)
	{
		String result = null;
		
		try
		{
			ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(
					MailUtil.getLastStaffNameSQL(customerID));
			
			if(rs.next()) {
			    result = rs.getString("staff_name");
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * ‘O‰ñ’S“–Ò–¼‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ‘O‰ñ’S“–Ò–¼‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	private static String getLastStaffNameSQL(Integer customerID)
	{
	    StringBuilder sql = new StringBuilder(1000);
	    sql.append(" select");
	    sql.append("     ms.staff_name1 || '@' || ms.staff_name2 as staff_name");
	    sql.append(" from");
	    sql.append("     data_sales ds");
	    sql.append("         inner join mst_staff ms");
	    sql.append("                 on ds.staff_id = ms.staff_id");
	    sql.append(" where");
	    sql.append("     ds.delete_date is null");
	    sql.append(" and ds.customer_id = " + customerID.toString());
	    sql.append(" and ds.sales_date = ");
	    sql.append("         (");
	    sql.append("             select");
	    sql.append("                 max(sales_date)");
	    sql.append("             from");
	    sql.append("                 data_sales ds");
	    sql.append("             where");
	    sql.append("                 ds.delete_date is null");
	    sql.append("             and ds.customer_id = " + customerID.toString());
	    sql.append("         )");
	    sql.append(" limit 1");

	    return sql.toString();
	}
	
	/**
	 * ‘O‰ñ—ˆ“X“ú‚ÌƒL[‚ğƒfƒR[ƒh‚·‚éB
	 * @param value ‘ÎÛ‚Æ‚È‚é•¶š—ñ
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
	private static String decodeKeyLastVisit(String value, Integer customerID)
	{
		String	result	=	value;
		java.util.Date	date	=	MailUtil.getLastVisitDate(customerID);
		
		if(date != null)
		{
			return	result.replaceAll(KEY_LAST_VISIT_D,
					String.format("%1$tY”N%1$tmŒ%1$td“ú", date));
		}
		else
		{
			return	result.replaceAll(KEY_LAST_VISIT_D, "");
		}
	}
	
	
	/**
	 * ‘O‰ñ—ˆ“X“ú‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ‘O‰ñ—ˆ“X“ú
	 */
	private static java.util.Date getLastVisitDate(Integer customerID)
	{
		java.util.Date	result	=	null;
		
		try
		{
			ResultSetWrapper	rs	=	SystemInfo.getConnection().executeQuery(
					MailUtil.getLastVisitDateSQL(customerID));
			
			if(rs.next())
			{
				result	=	rs.getDate("last_visit_date");
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * ‘O‰ñ—ˆ“X“ú‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ‘O‰ñ—ˆ“X“ú‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	private static String getLastVisitDateSQL(Integer customerID)
	{
		return	"select max(sales_date) as last_visit_date\n" +
				"from data_sales ds\n" +
				"where ds.delete_date is null\n" +
				"and ds.customer_id = " + customerID.toString() + "\n";
	}
	
	/**
	 * ƒ|ƒCƒ“ƒg‚ÌƒL[‚ğƒfƒR[ƒh‚·‚éB
	 * @param value ‘ÎÛ‚Æ‚È‚é•¶š—ñ
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
	private static String decodeKeyPoint(String value, Integer customerID)
	{
		String result = value;

                // Œ»İƒ|ƒCƒ“ƒg‚ğæ“¾‚·‚é
                Long nowPoint = PointData.getNowPoint(customerID);
                if( nowPoint != null ){
                    result = result.replaceAll(KEY_POINT_D, String.valueOf(nowPoint));
                }

		return result;
	}
	
	private static String decodeKeyItem(String value, Map mapItem)
	{
                String result = value;
		Pattern p = Pattern.compile(KEY_ITEM_D, Pattern.DOTALL);
		Matcher m = p.matcher(result);
                
                MstItem mg = null;
                if (mapItem == null) {
                    mg = new MstItem();
                }
                
		while(m.find())
		{
			p	=	Pattern.compile(".*\\[¤•i", Pattern.DOTALL);
			String	itemID	=	p.matcher(result).replaceFirst("");
			p	=	Pattern.compile("\\].*", Pattern.DOTALL);
			itemID	=	p.matcher(itemID).replaceFirst("");
			
			try
			{
                            String itemName = "";
                            if (mapItem == null) {

                                mg.setItemNo(itemID);
				mg.loadByItemNo(SystemInfo.getConnection());
                                itemName = mg.getItemName();

                            } else {
                                
                                itemName = (String)mapItem.get(itemID);
                            }
				result	=	result.replaceAll("\\[¤•i" + itemID + "\\]", itemName);
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			
			m.reset(result);
		}
		
		return	result;
	}
	
	
	private static String decodeKeyTechnic(String value, Map mapTech)
	{
		String result = value;
		Pattern p =	Pattern.compile(KEY_TECHNIC_D, Pattern.DOTALL);
		Matcher m =	p.matcher(result);

		MstTechnic mt = null;
                if (mapTech == null) {
                    mt = new MstTechnic();                    
                }

		while(m.find())
		{
			p	=	Pattern.compile(".*\\[‹Zp", Pattern.DOTALL);
			String	technicID	=	p.matcher(result).replaceFirst("");
			p	=	Pattern.compile("\\].*", Pattern.DOTALL);
			technicID	=	p.matcher(technicID).replaceFirst("");
			
			try
			{
                            String techName = "";
                            if (mapTech == null) {

                                mt.setTechnicNo(technicID);
				mt.loadByTechnicNo(SystemInfo.getConnection());
                                techName = mt.getTechnicName();
                                    
                            } else {

                                techName = (String)mapTech.get(technicID);
                            }
				
				result	=	result.replaceAll("\\[‹Zp" + technicID + "\\]", techName);
			}
			catch(Exception e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			
			m.reset(result);
		}
		
		return	result;
	}
        
        private static String decodeKeyCourse(String value, Map mapCourse)
	{
		String result = value;
		Pattern p =	Pattern.compile(KEY_COURSE_D, Pattern.DOTALL);
		Matcher m =	p.matcher(result);

		Course course = null;
                if (mapCourse == null) {
                    course = new Course();                    
                }

		while(m.find())
		{
			p	=	Pattern.compile(".*\\[ƒR[ƒX", Pattern.DOTALL);
			String	courseIDSr	=	p.matcher(result).replaceFirst("");
			p	=	Pattern.compile("\\].*", Pattern.DOTALL);
			courseIDSr	=	p.matcher(courseIDSr).replaceFirst("");
			Integer courseID = Integer.parseInt(courseIDSr);
			try
			{
                            String courseName = "";
                            if (mapCourse == null) {

                                course.setCourseId(courseID);
				course.loadCourseByID(SystemInfo.getConnection());
                                courseName = course.getCourseName();
                                    
                            } else {

                                courseName = (String)mapCourse.get(courseIDSr);
                            }
				
				result	=	result.replaceAll("\\[ƒR[ƒX" + courseIDSr + "\\]", courseName);
			}
			catch(Exception e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			
			m.reset(result);
		}
		
		return	result;
	}
	
	private static String decodeKeySignature(String value, Object target, Map mapSignature)
	{
		String result = value;
		
		MstMailSignature mms = null;
                
                if (mapSignature == null) {

                    mms = new MstMailSignature();

                    if(target instanceof MstGroup)
                    {
                        MstGroup	mg	=	(MstGroup)target;
                        mms.setGroupID(mg.getGroupID());
                        mms.setShopID(0);
                    }
                    else if(target instanceof MstShop)
                    {
                        MstShop		ms	=	(MstShop)target;
                        mms.setGroupID(ms.getGroupID());
                        mms.setShopID(ms.getShopID());
                    }
                }
		
		for(Integer	i = 1; i <= 3; i ++)
		{
			try
			{
                            String signatureName = "";
                            
                            if (mapSignature == null) {
                                
				mms.setMailSignaturID(i);
				mms.load(SystemInfo.getConnection());
                                signatureName = mms.getMailSignaturBody();
                                
                            } else {
                                
                                signatureName = (String)mapSignature.get(i);
                            }
				
				Pattern		p		=	Pattern.compile("\\[–¼" + i.toString() + "\\]", Pattern.DOTALL);
				Matcher		m		=	p.matcher(result);

				result	=	m.replaceAll(signatureName);
				
				m.reset(result);
			}
			catch(Exception e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		
		return	result;
	}
	
	private static boolean sendSosiaMails(Collection<DataMail> cdm) throws SQLException{
		boolean		result		=	true;
		String		fromName	=	SystemInfo.getName();
		String		fromAddress	=	SystemInfo.getMailAddress();

		ConnectionWrapper	con		=	SystemInfo.getMailConnection();
		
		try
		{
			con.begin();
			
			for(DataMail dm : cdm)
			{
				if(!MailUtil.sendSosiaMail(con, fromName, fromAddress, dm))
				{
					result	=	false;
					break;
				}
			}
			
			if(result)
			{
				con.commit();
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			con.rollback();
			throw(e);
		}
		
		return	result;            
        }
        
        private static boolean sendSosiaMail(ConnectionWrapper con,
			String fromName,
			String fromAddress,
			DataMail dm) throws SQLException
	{
               
                String mailHeader = "";
                try {
                    mailHeader = "From: " + MimeUtility.encodeText(fromName, "iso-2022-jp", "B") + " <" + fromAddress + ">\n";
                    mailHeader+= "Subject: " + MimeUtility.encodeText(dm.getMailTitle(), "iso-2022-jp", "B") + "\n";
                } catch(Exception e){}
           
                String mailBody = mailHeader.replaceAll("'", "''") + "\n" + dm.getMailBody().replaceAll("'", "''");
           
            	String sql = "insert into sosia_synchronization_mail_queue(sosia_id, body ) \n" + 
                             "values('" + dm.getSosiaCustomer().getSosiaID() + "', \n" +  
                                    "'" + mailBody + "')";
                System.out.println(mailBody);
                return	(con.executeUpdate(sql) == 1);
	}
     
        //nhanvt start edit 20141229 New request #34634
        public static boolean sendMails(Collection<DataMail> cdm, ConnectionWrapper con) throws SQLException
	{
            return sendMails(cdm, "", con);
        }
	
	public static boolean sendMails(Collection<DataMail> cdm, String sendDate, ConnectionWrapper con) throws SQLException
	{
		boolean result      = true;
		String   fromName    = SystemInfo.getName();
		String   fromAddress = SystemInfo.getMailAddress();

		//ConnectionWrapper con = SystemInfo.getMailConnection();
		
                System.out.println("connectionData :"+con.getDriver()+"  "+con.getUrl());
                System.out.println("fromName :"+SystemInfo.getName());
                System.out.println("fromAddress :"+SystemInfo.getMailAddress());
		
		try
		{
		    con.begin();

                   
		    for(DataMail dm : cdm) {
                        //Luc start edit 20151224 #45505
                        if(dm.getSendMail()!=0) {
                            if (!MailUtil.sendMail(con, fromName, fromAddress, dm, sendDate)) {
                                result = false;
                                break;
                            }
                        }
		    }
                    
                    //Luc start add 20160406 #49480
                    //“X•Ü‚Éƒ[ƒ‹‚ğCC‚·‚éB
                    if(isCcMail && mailPlantext != null) {
                        DataMail cc = mailPlantext;
                        if(cc!= null) {
                            cc.setCustomerName(0,SystemInfo.getName());
                            cc.setCustomerName(1,"");
                            cc.setSendMailAddress(SystemInfo.getMailAddress());
                            if (!MailUtil.sendMail(con, fromName, fromAddress,cc, sendDate)) {
                                result = false;
                            } 
                        } 
                    }
                     //Luc end add 20160406 #49480
		    if(result) {
			con.commit();
		    } else {
			con.rollback();
		    }

		}
		catch(SQLException e)
		{
		    con.rollback();
		    throw(e);
		}
                isCcMail = false;
		return	result;
	}
        //nhanvt end edit 20141229 New request #34634
	
	private static boolean sendMail(ConnectionWrapper con,
			String fromName,
			String fromAddress,
			DataMail dm,
                        String sendDate) throws SQLException
	{
            System.out.println("ConnectionWrapper :\n"+con.getDriver()+" "+con.getUrl());
		return	(con.executeUpdate(MailUtil.getInsertSendMailSQL(
				fromName, fromAddress, dm, sendDate)) == 1);
	}
	private static String getInsertSendMailSQL(String fromName,
			String fromAddress,
			DataMail dm,
                        String sendDate)
	{
           
		String	sql	=	"insert into mail_queue\n" +
						"(\n" +
						"from_name,\n" +
						"from_mail,\n" +
						"to_name,\n" +
						"to_mail,\n" +
						"subject,\n" +
						"body\n" +
						(sendDate.length() > 0 ? ",send_date" : "") +
						") values (\n" +
						"'" + fromName.replaceAll("'", "''") + "',\n" +
						"'" + fromAddress + "',\n" +
						"'" + dm.getFullCustomerName().replaceAll("'", "''") + "',\n" +
						"'" + dm.getSendMailAddress() + "',\n" +
						"'" + dm.getMailTitle().replaceAll("'", "''") + "',\n" +
						"'" + dm.getMailBody().replaceAll("'", "''") + "'\n" +
						(sendDate.length() > 0 ? ",'" + sendDate + "'" : "") + "\n" +
						");\n";
                System.out.println(sql);
		
		return	SQLUtil.encodeSpecialCharacters(sql);
	}

       
        public static void setMailBody(ArrayList<DataMail> targetList, ArrayList<DataMail> mailDatas, Object target) {

            Map mapStaffName = new HashMap();
            Map mapLastVisit = new HashMap();
            Map mapItem = new HashMap();
            Map mapTech = new HashMap();
            Map mapCourse = new HashMap();
            Map mapSignature = new HashMap();
            Map mapNextReservation = new HashMap();
            Map mapReservationStaff = new HashMap();
            Map mapNextReservationMenu = new HashMap();

            setLastVisitMap(targetList, mapStaffName, mapLastVisit);
            setTechMap(mapTech);
            setItemMap(mapItem);
            setCourseMap(mapCourse);
            setSignatureMap(mapSignature, target);
            setNextReservationMap(targetList, mapNextReservation, mapReservationStaff, mapNextReservationMenu);
            if(targetList!= null && targetList.size()>0) {
                   mailPlantext = new DataMail();
                   mailPlantext.setMailTitle(targetList.get(0).getMailTitle());
                   mailPlantext.setMailBody(targetList.get(0).getMailBody());
            }
	    for (DataMail dm : targetList) {

                for (int i = 0; i < 2; i++) {

                    String result = (i == 0 ? dm.getMailTitle() : dm.getMailBody());
                    result = decodeKeyTarget(result, target);
                    result = decodeKeyCustomerName(result, dm.getFullCustomerName());

                    // ‘O‰ñ’S“–Ò
                    String staffName = "";
                    if (mapStaffName.get(dm.getCustomerID()) != null) {
                        staffName = String.valueOf(mapStaffName.get(dm.getCustomerID()));
                    }
                    result = result.replaceAll(KEY_STAFF_NAME_D, staffName);

                    // ‘O‰ñ—ˆ“X“ú
                    java.util.Date date = (java.util.Date)mapLastVisit.get(dm.getCustomerID());
                    if (date != null) {
                        result = result.replaceAll(KEY_LAST_VISIT_D, String.format("%1$tY”N%1$tmŒ%1$td“ú", date));
                    } else {
                        result = result.replaceAll(KEY_LAST_VISIT_D, "");
                    }

                    // ƒ|ƒCƒ“ƒg
                    result = decodeKeyPoint(result, dm.getCustomerID());

                    // ¤•i–¼
                    result = decodeKeyItem(result, mapItem);

                    // ‹Zp–¼
                    result = decodeKeyTechnic(result, mapTech);
                    
                    // ƒR[ƒX
                    result = decodeKeyCourse(result, mapCourse);

                    // –¼
                    result = decodeKeySignature(result, target, mapSignature);

                    // Ÿ‰ñ—\–ñ“ú
                    java.util.Date nextDate = (java.util.Date)mapNextReservation.get(dm.getCustomerID());
                    if (nextDate != null) {
                        result = result.replaceAll(KEY_NEXT_RESERVATION_D, String.format("%1$tY”N%1$tmŒ%1$td“ú %1$tH%1$tM•ª", nextDate));
                    } else {
                        result = result.replaceAll(KEY_NEXT_RESERVATION_D, "");
                    }

                    // —\–ñ’S“–Ò
                    String reservationStaff = "";
                    if (mapReservationStaff.get(dm.getCustomerID()) != null) {
                        reservationStaff = String.valueOf(mapReservationStaff.get(dm.getCustomerID()));
                    }
                    result = result.replaceAll(KEY_RESERVATION_STAFF_D, reservationStaff);

                    // Ÿ‰ñ—\–ñ“à—e
                    String nextReservationMenu = "";
                    if (mapNextReservationMenu.get(dm.getCustomerID()) != null) {
                        nextReservationMenu = String.valueOf(mapNextReservationMenu.get(dm.getCustomerID()));
                    }
                    result = result.replaceAll(KEY_NEXT_RESERVATION_MENU_D, nextReservationMenu);

                    if (i == 0) {
                        dm.setMailTitle(result);
                    } else {
                        dm.setMailBody(result);
                    }
                }

                mailDatas.add(dm);
            }
        }
        
        
        private static void setLastVisitMap(final ArrayList<DataMail> targetList, Map mapStaffName, Map mapLastVisit) {
            
            StringBuilder idList = new StringBuilder(1000);
            for (DataMail dm : targetList) {
                idList.append(",");
                idList.append(dm.getCustomerID());
            }

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mc.customer_id");
            sql.append("     ,ds.sales_date");
            sql.append("     ,coalesce(ds.staff_name, '') as staff_name");
            sql.append(" from");
            sql.append("     mst_customer mc");
            sql.append("         left outer join");
            sql.append("             (");
            sql.append("                 SELECT");
            sql.append("                      a.customer_id");
            sql.append("                     ,a.sales_date");
            sql.append("                     ,a.staff_id");
            sql.append("                     ,staff_name1 || ' ' || staff_name2 as staff_name");
            sql.append("                 FROM");
            sql.append("                     data_sales a");
            sql.append("                         join");
            sql.append("                             (");
            sql.append("                                 SELECT");
            sql.append("                                      a.shop_id");
            sql.append("                                     ,a.sales_date");
            sql.append("                                     ,a.customer_id");
            sql.append("                                     ,max(a.slip_no) as slip_no");
            sql.append("                                 FROM");
            sql.append("                                     data_sales a");
            sql.append("                                         inner join");
            sql.append("                                         (");
            sql.append("                                             SELECT");
            sql.append("                                                  customer_id");
            sql.append("                                                 ,max(to_char(sales_date, 'yyyy.mm.dd hh24:mi:ss') || to_char(insert_date, 'yyyy.mm.dd hh24:mi:ss')) as sales_date");
            sql.append("                                             FROM");
            sql.append("                                                 data_sales");
            sql.append("                                             WHERE");
            sql.append("                                                     sales_date is not null");
            sql.append("                                                 AND delete_date is null");
            sql.append("                                                 AND customer_id in (" + idList.substring(1).toString() + ")");
            sql.append("                                             GROUP BY");
            sql.append("                                                 customer_id");
            sql.append("                                         ) b");
            sql.append("                                         on a.customer_id = b.customer_id");
            sql.append("                                        and (to_char(a.sales_date, 'yyyy.mm.dd hh24:mi:ss') || to_char(a.insert_date, 'yyyy.mm.dd hh24:mi:ss')) = b.sales_date");
            sql.append("                                     WHERE");
            sql.append("                                         a.delete_date is null");
            sql.append("                                     GROUP BY");
            sql.append("                                          a.shop_id");
            sql.append("                                         ,a.sales_date");
            sql.append("                                         ,a.customer_id");
            sql.append("                             ) b");
            sql.append("                             using(shop_id, customer_id, sales_date, slip_no)");
            sql.append("                         join mst_shop");
            sql.append("                             using(shop_id)");
            sql.append("                         left outer join mst_staff");
            sql.append("                                      on a.staff_id = mst_staff.staff_id");
            sql.append("                 WHERE");
            sql.append("                         a.delete_date is null");
            if(!SystemInfo.getCurrentShop().getShopID().equals(0)) {
                sql.append("                        and a.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")");
            }
            sql.append("             ) ds");
            sql.append("             on mc.customer_id = ds.customer_id");
            sql.append(" where");
            sql.append("     mc.customer_id in (" + idList.substring(1).toString() + ")");

            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    mapStaffName.put(rs.getInt("customer_id"), rs.getString("staff_name"));
                    mapLastVisit.put(rs.getInt("customer_id"), rs.getDate("sales_date"));
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        private static void setTechMap(Map mapTech) {
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      technic_no");
            sql.append("     ,max(technic_name) as technic_name");
            sql.append(" from");
            sql.append("     mst_technic");
            sql.append(" group by");
            sql.append("     technic_no");

            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    mapTech.put(rs.getString("technic_no"), rs.getString("technic_name"));
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        private static void setItemMap(Map mapItem) {
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      item_no");
            sql.append("     ,max(item_name) as item_name");
            sql.append(" from");
            sql.append("     mst_item");
            sql.append(" group by");
            sql.append("     item_no");

            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    mapItem.put(rs.getString("item_no"), rs.getString("item_name"));
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        
         private static void setCourseMap(Map mapItem) {
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      course_id");
            sql.append("     ,max(course_name) as course_name");
            sql.append(" from");
            sql.append("     mst_course");
            sql.append(" group by");
            sql.append("     course_id");

            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    mapItem.put(rs.getString("course_id"), rs.getString("course_name"));
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        private static void setSignatureMap(Map mapSignature, Object target) {

            Integer groupID = null;
            Integer shopID = null;
            
            if (target instanceof MstGroup) {
                
                MstGroup mg = (MstGroup)target;
                groupID = mg.getGroupID();
                shopID = 0;

            } else if(target instanceof MstShop) {
                
                MstShop ms = (MstShop)target;
                groupID = ms.getGroupID();
                shopID = ms.getShopID();
            }

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mail_signature_id");
            sql.append("     ,mail_signature_body");
            sql.append(" from");
            sql.append("     mst_mail_signature");
            sql.append(" where");
            sql.append("         group_id = " + SQLUtil.convertForSQL(groupID));
            sql.append("     and shop_id = " + SQLUtil.convertForSQL(shopID));

            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    mapSignature.put(rs.getInt("mail_signature_id"), rs.getString("mail_signature_body"));
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

	/**
	 * Ÿ‰ñ—\–ñ“ú‚ÌƒL[‚ğƒfƒR[ƒh‚·‚éB
	 * @param value ‘ÎÛ‚Æ‚È‚é•¶š—ñ
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
	private static String decodeKeyNextReservation(String value, Integer customerID)
	{
            String result = value;
            java.util.Date date = MailUtil.getNextReservationDateTime(customerID);

            if (date != null) {
                return result.replaceAll(KEY_NEXT_RESERVATION_D, String.format("%1$tY”N%1$tmŒ%1$td“ú %1$tH%1$tM•ª", date));
            } else {
                return result.replaceAll(KEY_NEXT_RESERVATION_D, "");
            }
	}
	
	
	/**
	 * Ÿ‰ñ—\–ñ“ú‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return Ÿ‰ñ—\–ñ“ú
	 */
	private static java.util.Date getNextReservationDateTime(Integer customerID)
	{
            java.util.Date result = null;
		
            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(
                                    MailUtil.getgetNextReservationDateTimeSQL(customerID));

                if (rs.next()) {
                    result = rs.getTimestamp("reservation_datetime");
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            return result;
	}
	
	/**
	 * Ÿ‰ñ—\–ñ“ú‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return Ÿ‰ñ—\–ñ“ú‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	private static String getgetNextReservationDateTimeSQL(Integer customerID)
	{
            StringBuilder sql = new StringBuilder(1000);
                
            sql.append(" select");
            sql.append("     min(drd.reservation_datetime) as reservation_datetime");
            sql.append(" from");
            sql.append("     data_reservation dr");
            sql.append("         join data_reservation_detail drd");
            sql.append("         using(shop_id, reservation_no)");
            sql.append(" where");
            sql.append("         dr.delete_date is null");
            sql.append("     and drd.delete_date is null");
            sql.append("     and drd.reservation_datetime >= now()");
            sql.append("     and dr.customer_id = " + customerID.toString());

            return sql.toString();
	}
        
	/**
	 * —\–ñ’S“–Ò‚ÌƒL[‚ğƒfƒR[ƒh‚·‚éB
	 * @param value ‘ÎÛ‚Æ‚È‚é•¶š—ñ
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
	private static String decodeKeyReservationStaff(String value, Integer customerID)
	{
            String result = value;
            String replaceStr = MailUtil.getReservationStaff(customerID);
            if (replaceStr == null) replaceStr = "";
            return result.replaceAll(KEY_RESERVATION_STAFF_D, replaceStr);
	}
	
	/**
	 * —\–ñ’S“–Ò‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return —\–ñ’S“–Ò
	 */
	private static String getReservationStaff(Integer customerID)
	{
            String result = null;
		
            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(MailUtil.getReservationStaffSQL(customerID));

                if (rs.next()) {
                    result = rs.getString("staff_name");
                }
                
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            return result;
	}
	
	/**
	 * —\–ñ’S“–Ò‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return —\–ñ’S“–Ò‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	private static String getReservationStaffSQL(Integer customerID)
	{
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     case when dr.designated_flag");
            sql.append("         then ms.staff_name1 || '@' || ms.staff_name2");
            sql.append("         else 'w–¼’S“–‚È‚µ'");
            sql.append("     end as staff_name");
            sql.append(" from");
            sql.append("     data_reservation dr");
            sql.append("         join data_reservation_detail drd");
            sql.append("             using(shop_id, reservation_no)");
            sql.append("         inner join mst_staff ms");
            sql.append("                 on dr.staff_id = ms.staff_id");
            sql.append(" where");
            sql.append("         dr.delete_date is null");
            sql.append("     and drd.delete_date is null");
            sql.append("     and dr.customer_id = " + customerID.toString());
            sql.append("     and drd.reservation_datetime =");
            sql.append("         (");
            sql.append("             select");
            sql.append("                 min(drd.reservation_datetime)");
            sql.append("             from");
            sql.append("                 data_reservation dr");
            sql.append("                     join data_reservation_detail drd");
            sql.append("                     using(shop_id, reservation_no)");
            sql.append("             where");
            sql.append("                     dr.delete_date is null");
            sql.append("                 and drd.delete_date is null");
            sql.append("                 and drd.reservation_datetime >= now()");
            sql.append("                 and dr.customer_id = " + customerID.toString());
            sql.append("         )");
            sql.append(" limit 1");

	    return sql.toString();
	}
        
        private static void setNextReservationMap(
                final ArrayList<DataMail> targetList,
                Map mapNextReservation,
                Map mapReservationStaff,
                Map mapNextReservationMenu) {
            
            StringBuilder idList = new StringBuilder(1000);
            for (DataMail dm : targetList) {
                idList.append(",");
                idList.append(dm.getCustomerID());
            }

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      dr.customer_id");
            sql.append("     ,drd.reservation_datetime");
            //Luc start edit 20151210 #44662
            //sql.append("     ,case when dr.designated_flag");
            sql.append("     ,case when  dr.designated_flag and (dr.staff_id is not null and    dr.staff_id >0)   ");
             //Luc end edit 20151210 #44662
            sql.append("         then ms.staff_name1 || '@' || ms.staff_name2");
            sql.append("         else '’S“–w–¼‚È‚µ'");
            sql.append("      end as staff_name");
            sql.append(" from");
            sql.append("     data_reservation dr");
            sql.append("         join data_reservation_detail drd");
            sql.append("             using(shop_id, reservation_no)");
            //Luc start edit 20151124 
            //sql.append("         inner join mst_staff ms");
            //sql.append("                 on dr.staff_id = ms.staff_id");
            sql.append("         left join mst_staff ms");
            sql.append("                 on dr.staff_id = ms.staff_id");
            //Luc end edit 20151124 
            sql.append("         inner join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      dr.customer_id");
            sql.append("                     ,min(drd.reservation_datetime) as reservation_datetime");
            sql.append("                 from");
            sql.append("                     data_reservation dr");
            sql.append("                         join data_reservation_detail drd");
            sql.append("                         using(shop_id, reservation_no)");
            sql.append("                 where");
            sql.append("                         dr.delete_date is null");
            sql.append("                     and drd.delete_date is null");
            //Luc start edit 20151124 #44662
            sql.append("                     and drd.reservation_datetime >= now()");
             //Luc start edit 20151124 #44662
            //Luc start add 20151204 #44687
            if(!SystemInfo.getCurrentShop().getShopID().equals(0)) {
                sql.append("                and dr.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")");
            }
            //Luc start add 20151204 #44687
            sql.append("                     and dr.customer_id in (" + idList.substring(1).toString() + ")");
            sql.append("                 group by");
            sql.append("                     dr.customer_id");
            sql.append("             ) t");
            sql.append("             on dr.customer_id = t.customer_id");
            sql.append("            and drd.reservation_datetime = t.reservation_datetime");
            sql.append(" where");
            sql.append("         dr.delete_date is null");
            //Luc start add 20151210 #44662
            sql.append("     and dr.status < 2");
            //Luc start add 20151210 #44662
            sql.append("     and drd.delete_date is null");
            //Luc start edit 20151124 #44686
            //sql.append("     and drd.reservation_datetime >= (current_date + 1)");
            sql.append("     and drd.reservation_datetime >= now()");
            //Luc end edit 20151124 #44686
            //Luc start add 20151204 #44687
            if(!SystemInfo.getCurrentShop().getShopID().equals(0)) {
                sql.append(" and dr.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")");
            }
            sql.append("                     and dr.customer_id in (" + idList.substring(1).toString() + ")");
            //Luc start add 20151204 #44687
            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    mapNextReservation.put(rs.getInt("customer_id"), rs.getTimestamp("reservation_datetime"));
                    mapReservationStaff.put(rs.getInt("customer_id"), rs.getString("staff_name"));
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            // Ÿ‰ñ—\–ñ“à—e
            //Luc start edit 20151124 #44662
            /*sql.setLength(0);
            sql.append(" select");
            sql.append("      dr.customer_id");
            sql.append("     ,mt.technic_name");
            sql.append(" from");
            sql.append("     data_reservation dr");
            sql.append("         join data_reservation_detail drd");
            sql.append("             using(shop_id, reservation_no)");
            sql.append("         join mst_technic mt");
            sql.append("             using(technic_id)");
            sql.append(" where");
            sql.append("         dr.delete_date is null");
            sql.append("     and drd.delete_date is null");
            //Luc start edit 2015115
            //sql.append("     and drd.reservation_datetime > (current_date + 1)");
            sql.append("     and drd.reservation_datetime >= (current_date )");
            //Luc start edit 2015115
            sql.append("     and dr.customer_id in (" + idList.substring(1).toString() + ")");
            sql.append(" order by");
            sql.append("      dr.customer_id");
            sql.append("     ,drd.reservation_datetime");
            */
            sql.setLength(0);
            sql.append(" select t.customer_id,\n");
            sql.append(" array_to_string(array((select mt.technic_name from data_reservation dr1 \n");
            sql.append(" inner join data_reservation_detail drd1 on dr1.reservation_no = drd1.reservation_no and dr1.shop_id = drd1.shop_id\n");
            sql.append(" inner join mst_technic mt on drd1.technic_id = mt.technic_id\n");
            sql.append(" where dr1.customer_id = t.customer_id \n");
            sql.append("                         and exists(select 1 from data_reservation dr2 \n");
            sql.append("                         inner join data_reservation_detail drd2 on dr2.reservation_no = drd2.reservation_no and dr2.shop_id = drd2.shop_id\n");
            sql.append("                         where dr2.customer_id = dr1.customer_id \n");
            sql.append("                         and drd2.reservation_datetime = t.min_date\n");
            sql.append("                         and dr2.reservation_no = dr1.reservation_no \n");
            sql.append("                         and dr2.shop_id = dr1.shop_id\n");
            sql.append("                         and dr2.delete_date is null and drd2.delete_date is null\n");
            sql.append("                         )\n");
            sql.append("       and dr1.delete_date is null and drd1.delete_date is null\n");
            sql.append("       and dr1.status < 2\n");
            sql.append(" )),',') as technic_name  \n");
            sql.append(" from \n");


            sql.append(" (SELECT dr.customer_id,(select min(reservation_datetime) from data_reservation dr1 \n");
            sql.append("                                  inner join data_reservation_detail drd1 on dr1.reservation_no = drd1.reservation_no and dr1.shop_id = drd1.shop_id\n");
            sql.append("                                 where drd1.reservation_datetime >= now()\n");
            sql.append("                                 and dr1.customer_id = dr.customer_id\n");
            if(!SystemInfo.getCurrentShop().getShopID().equals(0)) {
                sql.append("                                 and dr1.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")");
            }
            sql.append("                                 and dr1.delete_date is null and drd1.delete_date is null");
            sql.append("                                  )  as min_date\n");
            sql.append(" FROM data_reservation dr\n");
            sql.append(" JOIN data_reservation_detail drd using(shop_id, reservation_no)\n");
            sql.append(" JOIN mst_technic mt using(technic_id)\n");
            sql.append(" WHERE dr.delete_date IS NULL\n");
            sql.append("   AND drd.delete_date IS NULL\n");
            sql.append(" AND dr.customer_id IN (" + idList.substring(1).toString() + ")\n");
            if(!SystemInfo.getCurrentShop().getShopID().equals(0)) {
                sql.append("                                 and dr.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")");
            }
            sql.append(" and drd.reservation_datetime >= now()\n");
            sql.append("       and dr.status < 2\n");
            sql.append(" group by customer_id\n");
            sql.append(" )t\n");
            //Luc start edit 20151124 #44662
            
            try {
                int id = 0;
                String menu = "";
                
                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                while (rs.next()) {
                    if (id == 0) id = rs.getInt("customer_id");

                    if (id != rs.getInt("customer_id")) {
                        mapNextReservationMenu.put(id, menu);
                        id = rs.getInt("customer_id");
                        menu = "";
                    }

                    if (menu.length() > 0) menu += "A";
                    menu += rs.getString("technic_name");
                }

                if (id != 0) mapNextReservationMenu.put(id, menu);

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

        }

	/**
	 * Ÿ‰ñ—\–ñ“à—e‚ÌƒL[‚ğƒfƒR[ƒh‚·‚éB
	 * @param value ‘ÎÛ‚Æ‚È‚é•¶š—ñ
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return ƒfƒR[ƒhŒã‚Ì•¶š—ñ
	 */
	private static String decodeKeyNextReservationMenu(String value, Integer customerID)
	{
            String result = value;
            String replaceStr = MailUtil.getNextReservationMenu(customerID);
            if (replaceStr == null) replaceStr = "";
            return result.replaceAll(KEY_NEXT_RESERVATION_MENU_D, replaceStr);
	}

	/**
	 * Ÿ‰ñ—\–ñ“à—e‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return Ÿ‰ñ—\–ñ“à—e
	 */
	private static String getNextReservationMenu(Integer customerID)
	{
            String result = "";

            try {

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(
                                    MailUtil.getgetNextReservationMenuSQL(customerID));

                while (rs.next()) {
                    if (result.length() > 0) result += "A";
                    result += rs.getString("technic_name");
                }

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            return result;
	}

	/**
	 * Ÿ‰ñ—\–ñ“à—e‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @param customerID ŒÚ‹q‚h‚c
	 * @return Ÿ‰ñ—\–ñ“à—e‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	private static String getgetNextReservationMenuSQL(Integer customerID)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     coalesce(mc.course_name, mt.technic_name) as technic_name");
            sql.append(" from");
            sql.append("     data_reservation dr");
            sql.append("         join data_reservation_detail drd");
            sql.append("             using(shop_id, reservation_no)");
            sql.append("         left join mst_technic mt");
            sql.append("             on drd.technic_id = mt.technic_id");
            sql.append("         left join mst_course mc");
            sql.append("             on drd.technic_id = mc.course_id");
            sql.append("            and drd.course_flg in (1, 2)");
            sql.append(" where");
            sql.append("         dr.delete_date is null");
            sql.append("     and drd.delete_date is null");
            sql.append("     and dr.customer_id = " + customerID.toString());
            sql.append("     and drd.reservation_no =");
            sql.append("         (");
            sql.append("             select");
            sql.append("                 min(drd.reservation_no)");
            sql.append("             from");
            sql.append("                 data_reservation dr");
            sql.append("                     join data_reservation_detail drd");
            sql.append("                     using(shop_id, reservation_no)");
            sql.append("             where");
            sql.append("                     dr.delete_date is null");
            sql.append("                 and drd.delete_date is null");
            sql.append("                 and dr.customer_id = " + customerID.toString());
            sql.append("                 and drd.reservation_datetime =");
            sql.append("                     (");
            sql.append("                         select");
            sql.append("                             min(drd.reservation_datetime)");
            sql.append("                         from");
            sql.append("                             data_reservation dr");
            sql.append("                                 join data_reservation_detail drd");
            sql.append("                                 using(shop_id, reservation_no)");
            sql.append("                         where");
            sql.append("                                 dr.delete_date is null");
            sql.append("                             and drd.delete_date is null");
            sql.append("                             and dr.customer_id = " + customerID.toString());
            sql.append("                             and drd.reservation_datetime >= now()");
            sql.append("                     )");
            sql.append("         )");
            sql.append(" order by");
            sql.append("     drd.reservation_datetime");

            return sql.toString();
	}

}
