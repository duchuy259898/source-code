/*
 * DmHistory.java
 *
 * Created on 2010/01/22, 12:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.mail;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.util.SQLUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import com.geobeck.swing.MessageDialog;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.util.DateUtil;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;

/**
 *
 * @author geobeck
 */

public class DmHistory
{
    //IVS_LVTu start edit 2016/02/26 Bug #48832
    public static final int DM_MAIL     = 1;
    public static final int DM_LABEL    = 2;
    public static final int DM_POSTCARD = 3;
    //IVS_LVTu end edit 2016/02/26 Bug #48832
    
    private Component parent = null;
    private ArrayList list = null;
    private String dmTitle = null;
    private Integer shopID = null;
    private Integer type = null;

    public DmHistory(Component parent, ArrayList list, String dmTitle, Integer type) {
        this.parent = parent;
        this.list = list;
        this.dmTitle = dmTitle;
        this.shopID = SystemInfo.getCurrentShop().getShopID();
        this.type = type;
    }
    
    public static boolean deleteOld() {

        boolean result = false;
        
        ConnectionWrapper con = SystemInfo.getConnection();
        
        try {

            con.begin();
            
            try {
                
                con.executeUpdate("delete from data_dm_history where make_date < current_timestamp + '1 year ago'");
                con.executeUpdate("delete from data_dm_history_detail where make_date < current_timestamp + '1 year ago'");

                con.commit();
                result = true;
                
            } catch (Exception e) {
                con.rollback();
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return result;
    }

    public boolean checkInput() {

        boolean result = true;
        
        if (dmTitle.replace("@","").trim().length() < 1) {
            MessageDialog.showMessageDialog(
                parent,
                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,"DMƒ^ƒCƒgƒ‹"),
                "—š—ð“o˜^",
                JOptionPane.ERROR_MESSAGE);

            result = false;
        }
        
        return result;
    }

    public boolean showRegistDialog() {

        boolean result = false;
        
        StringBuilder msg = new StringBuilder(1000);
        msg.append("‘ÎÛŒÚ‹q‚ðDMì¬—š—ð‚É“o˜^‚µ‚Ü‚·B‚æ‚ë‚µ‚¢‚Å‚·‚©H\n\n");
        msg.append("    DMƒ^ƒCƒgƒ‹F\n");
        msg.append("          u " + dmTitle + " v\n\n");
        
        int ret = MessageDialog.showYesNoDialog(
                    parent,
                    msg.toString(),
                    "—š—ð“o˜^",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.NO_OPTION);

        if (ret == JOptionPane.YES_OPTION) {
            result = true;
        }
        
        return result;
    }

    public boolean regist() {
        
        boolean result = false;
        
        ConnectionWrapper con = SystemInfo.getConnection();
        
        try {

            con.begin();
            
            try {
                String makeDate = DateUtil.format(Calendar.getInstance().getTime(), "yyyy/MM/dd HH:mm:ss");
                con.executeUpdate(getCreateDmHistorySQL(makeDate));

                for (Object obj: list) {
                    if(!isExists(con,makeDate,obj)){
                        con.executeUpdate(getCreateDmHistoryDetailSQL(makeDate, obj));
                    }
                }
                
                con.commit();
                result = true;
                
            } catch (Exception e) {
                con.rollback();
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        // Œ‹‰Ê”»’è
        if (result) {
            MessageDialog.showMessageDialog(
                parent,
                MessageUtil.getMessage(201),
                "—š—ð“o˜^",
                JOptionPane.INFORMATION_MESSAGE);

        } else {

            MessageDialog.showMessageDialog(
                parent,
                MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "DMì¬—š—ð"),
                "—š—ð“o˜^",
                JOptionPane.ERROR_MESSAGE);
        }
        
        return result;
    }
    
    private String getCreateDmHistorySQL(String makeDate) {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into data_dm_history");
        sql.append(" (");
        sql.append("      shop_id");
        sql.append("     ,dm_type");
        sql.append("     ,make_date");
        sql.append("     ,dm_title");
        sql.append("     ,dm_count");
        sql.append(" ) values");
        sql.append(" (");
        sql.append("      " + SQLUtil.convertForSQL(shopID));
        sql.append("     ," + SQLUtil.convertForSQL(type));
        sql.append("     ," + SQLUtil.convertForSQL(makeDate));
        sql.append("     ," + SQLUtil.convertForSQL(dmTitle));
        sql.append("     ," + SQLUtil.convertForSQL(list.size()));
        sql.append(" )");

        return sql.toString();
    }
    
    
    private String getCreateDmHistoryDetailSQL(String makeDate, Object obj) {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into data_dm_history_detail");
        sql.append(" (");
        sql.append("      shop_id");
        sql.append("     ,dm_type");
        
        sql.append("     ,make_date");
        sql.append("     ,customer_id");
        sql.append("     ,mail_address");
        sql.append("     ,mail_title");
        sql.append("     ,mail_body");
        sql.append(" ) values");
        sql.append(" (");
        sql.append("      " + SQLUtil.convertForSQL(shopID));
        sql.append("     ," + SQLUtil.convertForSQL(type));
        sql.append("     ," + SQLUtil.convertForSQL(makeDate));
        sql.append("     ," + SQLUtil.convertForSQL(((MstCustomer)obj).getCustomerID()));

        if (type.equals(DM_MAIL)) {
            sql.append(" ," + SQLUtil.convertForSQL(((DataMail)obj).getSendMailAddress()));
            sql.append(" ," + SQLUtil.convertForSQL(((DataMail)obj).getMailTitle()));
            sql.append(" ," + SQLUtil.convertForSQL(((DataMail)obj).getMailBody()));
        } else {
            sql.append(" ,null");
            sql.append(" ,null");
            sql.append(" ,null");
        }

        sql.append(" )");

        return sql.toString();
    }
    
    //nhanvt start
    /**
	 * “`•[ƒwƒbƒ_ƒf[ƒ^‚ª‘¶Ý‚·‚é‚©‚ðŽæ“¾‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ‘¶Ý‚·‚é
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExists(ConnectionWrapper con, String makeDate, Object obj) throws SQLException
	{
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.isExistsSQL(makeDate,obj));

		if(rs.next())	return	true;
		else	return	false;
	}
        
        public String isExistsSQL(String makeDate, Object obj){
            StringBuilder sql = new StringBuilder();
            
            sql.append("select *\n" );
            sql.append(		"from data_dm_history_detail \n" );
            sql.append(		" where shop_id = " + SQLUtil.convertForSQL(shopID) + "\n" );
            sql.append(		"and make_date = " + SQLUtil.convertForSQL(makeDate) + "\n");
            sql.append(		"and customer_id = " + SQLUtil.convertForSQL(((MstCustomer)obj).getCustomerID()) + "\n");
            return sql.toString();
        }
    //nhanvt end
}
