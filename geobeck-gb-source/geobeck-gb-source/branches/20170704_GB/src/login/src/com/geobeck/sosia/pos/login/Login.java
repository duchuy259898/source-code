/*
 * Login.java
 *
 * Created on 2006/05/02, 9:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.login;

import java.io.*;
import java.sql.*;
import javax.swing.*;
import java.util.regex.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.text.DateFormat;

/**
 * ログイン処理
 * @author katagiri
 */
public class Login
{
        public static String registeredMacAddress = null;
        
	/**
	 * コンストラクタ
	 */
	public Login()
	{
	}
	
	
	/**
	 * ログイン処理を行う。
	 * @param parent ログインFrame
	 * @param loginID ログインＩＤ
	 * @param password パスワード
	 * @return true - ログイン成功
	 */
	public static boolean login(JFrame parent, String loginID, String password)
	{
		SystemInfo.closeConnection();
		
		SystemInfo.setDatabase("");
		SystemInfo.setLoginID("");
		
		ConnectionWrapper con = SystemInfo.getBaseConnection();
		
		if(con == null)
		{
			MessageDialog.showMessageDialog(parent,
					MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
					JOptionPane.ERROR_MESSAGE);
			return	false;
		}
		
		try
		{
			//データベース名を取得
			if(!Login.getUserInfo(con, loginID, password))
			{
				MessageDialog.showMessageDialog(parent,
						MessageUtil.getMessage(2000),
						JOptionPane.ERROR_MESSAGE);
				return	false;
			}
			
			SystemInfo.setLoginID(loginID);
			SystemInfo.setLoginPass(password);
			
			//ＭＡＣアドレスを取得
			String macAddress = Login.getMacAddress();
			System.out.println("MAC Address : " + macAddress);
			
			//ＭＡＣアドレス毎の情報を取得
			if(!Login.getMacID(con, macAddress))
			{
				MessageDialog.showMessageDialog(parent,
						MessageUtil.getMessage(2001),
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
                        
                        if (registeredMacAddress.startsWith("tlimit")) {
                            try {
                                java.util.Date dt = DateFormat.getDateInstance().parse(registeredMacAddress.substring(7).replace("-", "/"));
                                if (dt.before(SystemInfo.getSystemDate())) {
                                    MessageDialog.showMessageDialog(
                                        parent,
                                        "使用期限が過ぎています。",
                                        JOptionPane.ERROR_MESSAGE);
                                    return false;
                                }
                            } catch (Exception e) {
                                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                                return false;
                            }
                        }
			
			//ログイン可能な場合
			if(Login.checkLoginNum(con))
			{
				con.begin();
				
				//ログインデータを削除
				Login.deleteLoginData(con);
				
				//ログインデータを追加
				if(!Login.insertLoginData(con))
				{
					con.rollback();
					MessageDialog.showMessageDialog(parent,
							MessageUtil.getMessage(2003),
							JOptionPane.ERROR_MESSAGE);
					return	false;
				}
				
				con.commit();
			}
			else
			{
				MessageDialog.showMessageDialog(parent,
						MessageUtil.getMessage(2002),
						JOptionPane.ERROR_MESSAGE);
				return	false;
			}
                        
			SystemInfo.refleshCurrentShop();
                        
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		try
		{
			//グループを取得する
			if(!SystemInfo.getGroup().loadData(SystemInfo.getConnection()))
			{
				MessageDialog.showMessageDialog(parent,
						MessageUtil.getMessage(2004),
						JOptionPane.ERROR_MESSAGE);
				return	false;
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
	
	
	/**
	 * ＭＡＣアドレスを取得する。
	 * @return ＭＡＣアドレス
	 */
	public static String getMacAddress()
	{
	    String result = "";
	    
	    try
	    {
                // ipconfigコマンドの出力からMACアドレス部分とマッチさせる正規表現を定義する
                String regex = "";
                if (SystemInfo.isWindows()) {
                    regex = "(Physical Address|物理アドレス)[^:]+: ([0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2})";
                } else {
                    regex = "(ether)\\s+([0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2})";
                }
                Pattern pattern = Pattern.compile(regex);

                // プロセスオブジェクトを生成
                String command = "";
                if (SystemInfo.isWindows()) {
                    command = "ipconfig /all";
                } else {
                    command = "ifconfig -a";
                }
                Process process = Runtime.getRuntime().exec(command);

                // 外部コマンドの標準出力を取得するための入力ストリームを取得
                InputStream is = process.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                // 標準出力を1行づつ取り出します
                String line;
                while((line=br.readLine())!=null)
                {
                    Matcher matcher=pattern.matcher(line.trim());
                    if(matcher.matches())
                    {
                        if(!matcher.group(2).equals("00-00-00-00-00-00-00-E0"))
                        {
                            // マッチした場合MACアドレス部分を戻り値として返す
                            if (result.length() > 0) result += ",";
                            result += "'" + matcher.group(2) + "'";
                        }
                    }
                }
	    }
	    catch (Exception e)
	    {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return result;
	}
	
	/**
	 * mst_userテーブルからユーザー情報を取得する。
	 * @param con コネクション
	 * @param loginID ログインＩＤ
	 * @param password パスワード
	 * @return true - 取得成功
	 */
	private static boolean getUserInfo(ConnectionWrapper con,
			String loginID, String password)
	{
		SystemInfo.setDatabase("");
		SystemInfo.setTypeID(0);
		
		// ユーザマスタテーブルからデータベース名等を取得する
		// ログインフォームで入力されたユーザID、パスワードを抽出条件とする
		StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      md.database_name");
                sql.append("     ,md.database_ip");
                sql.append("     ,md.database_port");
                sql.append("     ,mu.type_id");
                sql.append("     ,mu.group_id");
                sql.append("     ,mu.shop_id");
                sql.append("     ,mu.skin_id");
                sql.append("     ,mu.sosia_gear_base_url");
                sql.append("     ,mu.sosia_gear_id");
                sql.append("     ,mu.sosia_gear_password");
                sql.append("     ,mu.sosia_code");
                // vtbphuong add start 20131212 
                sql.append("     ,mu.custom_id");
                sql.append("     ,mu.database_id");
                // vtbphuong add End 20131212 
                //Luc start add 20140226
                 sql.append("     ,mu.pos_id,mu.pos_password,mu.pos_salon_id");     
                //Luc end addd 20140226
                sql.append("     ,mu.receipt_printer_type");
                //IVS_LVTu start edit 2016/02/24 New request #48455
                //sql.append("     ,mu.reservation_only");
                sql.append("     ,mu.system_type");
                sql.append("     ,coalesce(mc.directory_name, '') as directory_name");
                //IVS_LVTu start add 2016/05/27 New request #50223
                sql.append("     ,coalesce(use_api, 0) as use_api");
                sql.append("     ,coalesce(api_salon_id, 0) as api_salon_id");
                //IVS_LVTu end add 2016/05/27 New request #50223
                sql.append("     ,mu.use_visit_karte"); // 20170413 add #61376
                sql.append(" from");
                sql.append("     mst_user mu");
                sql.append("         inner join mst_database md");
                sql.append("                 on mu.database_id = md.database_id");
                sql.append("         left join mst_web_connection mc");
                sql.append("                 on md.database_id = mc.database_id");
                sql.append(" where");
                sql.append("         mu.login_id = " + SQLUtil.convertForSQL(loginID));
                sql.append("     and mu.password = " + SQLUtil.convertForSQL(password));

		try
		{
			ResultSetWrapper rs = con.executeQuery(sql.toString());
			
			if(rs.next())
			{
				SystemInfo.setDatabase(rs.getString("database_name"));
				SystemInfo.setServerIP(rs.getString("database_ip"));
				SystemInfo.setServerPort(rs.getString("database_port"));
				SystemInfo.setTypeID(rs.getInt("type_id"));
				SystemInfo.getGroup().setGroupID(rs.getInt("group_id"));
				SystemInfo.getCurrentShop().setShopID(rs.getInt("shop_id"));
				SystemInfo.getSkin().setSkinID(rs.getInt("skin_id"));
				SystemInfo.setSosiaGearBaseURL( rs.getString( "sosia_gear_base_url" ) );
				SystemInfo.setSosiaGearID( rs.getString( "sosia_gear_id" ) );
				SystemInfo.setSosiaGearPassWord( rs.getString( "sosia_gear_password" ) );
				SystemInfo.setSosiaCode( rs.getString( "sosia_code" ) );
				SystemInfo.setReceiptPrinterType( rs.getInt( "receipt_printer_type" ) );
				//SystemInfo.setReservationOnly( rs.getInt( "reservation_only" ) );
                                SystemInfo.setReservationOnly( rs.getInt( "system_type" ) );
                                //IVS_LVTu end edit 2016/02/24 New request #48455
				SystemInfo.setDirectoryName(rs.getString("directory_name"));
                                
                                //vtbphuong start add 20131212
                                SystemInfo.setNSystem(rs.getInt("custom_id"));
                                SystemInfo.setDatabaseID(rs.getInt("database_id"));
                                //vtbphuong end add 20131212
                                //Luc start add 20140226
                                SystemInfo.setPosId(rs.getString("pos_id"));
                                SystemInfo.setPosPassWord(rs.getString("pos_password"));
                                SystemInfo.setPossSalonId(rs.getString("pos_salon_id"));
                                //Luc end add 20140226
                                //IVS_LVTu start add 2016/05/27 New request #50223
                                SystemInfo.setUserAPI(rs.getInt("use_api"));
                                SystemInfo.setApiSalonID(rs.getInt("api_salon_id"));
                                //IVS_LVTu end add 2016/05/27 New request #50223
                                SystemInfo.setUseVisitKarte(rs.getBoolean("use_visit_karte")); // 20170413 add #61376
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	!SystemInfo.getDatabase().equals("");
	}
	
	/**
	 * mst_macからmac_idを取得する。
	 * @param con コネクション
	 * @param macAddress MACアドレス
	 * @return true - 取得成功
	 */
	private static boolean getMacID(ConnectionWrapper con, String macAddress)
	{
		SystemInfo.setMacID(null);
		registeredMacAddress = "";
                
		StringBuilder sql = new StringBuilder(1000);
                sql.append(" select * from mst_mac");
                sql.append(" where");
                sql.append("     login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()));
                sql.append(" and (mac_address like 'tlimit%' or mac_address in (" + macAddress + "))");
		
		try
		{
			ResultSetWrapper rs = con.executeQuery(sql.toString());
			
			if(rs.next())
			{
                            SystemInfo.setMacID(rs.getInt("mac_id"));
                            registeredMacAddress = rs.getString("mac_address");
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	(SystemInfo.getMacID() != null);
	}
	
	/**
	 * mst_userのライセンス数と、ログインしているユーザー数を比較して、
	 * ログイン可能かチェックする。
	 * @param con コネクション
	 * @return true - ログイン可能
	 */
	private static boolean checkLoginNum(ConnectionWrapper con)
	{
		boolean		result		=	false;
		
		String	sql	=	"select mu.login_id, mu.license_num,\n" +
						"sum(case when dl.login_id is null then 0 else 1 end) as login_cnt\n" +
						"from mst_user mu\n" +
						"left outer join data_login dl\n" +
						"on dl.login_id = mu.login_id\n" +
						"and mac_id != " + SQLUtil.convertForSQL(SystemInfo.getMacID()) + "\n" +
						"where mu.login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n" +
						"group by mu.login_id, mu.license_num\n";
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(sql);
			
			if(rs.next())
			{
				result	=	(rs.getInt("login_cnt") < rs.getInt("license_num"));
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	result;
	}
	
	/**
	 * data_loginテーブルにレコードが存在するかチェックする。
	 * @param con コネクション
	 * @return true - 存在する
	 */
	private static boolean isExistLoginData(ConnectionWrapper con)
	{
		boolean		result		=	false;
		
		String	sql	=	"select count(*) as login_cnt\n" +
						"from data_login dl\n" +
						"where dl.login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n" +
						"and dl.mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID());
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(sql);
			
			if(rs.next())
			{
				result	=	(rs.getInt("login_cnt") == 1);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	result;
	}
	
	/**
	 * data_loginテーブルにレコードを追加する。
	 * @param con コネクション
	 * @return true - 追加成功
	 */
	private static boolean insertLoginData(ConnectionWrapper con)
	{
		boolean		result		=	false;
		
		String	sql	=	"insert into data_login(login_id, mac_id, insert_date)\n" +
						"values(\n" +
						SQLUtil.convertForSQL(SystemInfo.getLoginID()) + ",\n" +
						SQLUtil.convertForSQL(SystemInfo.getMacID()) + ",\n" +
						"current_timestamp)\n";
		
		try
		{
			if(con.executeUpdate(sql) == 1)
			{
				result	=	true;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	result;
	}
	
	/**
	 * ログアウト処理を行う。
	 */
	public static void logout()
	{
		if(SystemInfo.getMacID() == null)	return;
		
		ConnectionWrapper	con	=	SystemInfo.getBaseConnection();
		
		Login.deleteLoginData(con);
	}
	
	/**
	 * data_loginテーブルからレコードを削除する。
	 * @param con コネクション
	 * @return true - 削除成功
	 */
	private static boolean deleteLoginData(ConnectionWrapper con)
	{
		boolean		result	=	false;
		
		try
		{
			String	sql	=	"delete from data_login\n" +
							"where login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n" +
							"and mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID());
			
			if(con.executeUpdate(sql) == 1)
			{
				result	=	true;
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
}
