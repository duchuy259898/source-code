/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.main;

import com.geobeck.sosia.pos.envCheck.EnvCheckConfiguration;
import com.geobeck.sosia.pos.envCheck.logic.InstallCheckLogic;
import com.geobeck.sosia.pos.login.Login;
import com.geobeck.sosia.pos.util.IOUtils;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * システム移行に関するツール。
 * 
 * @author mahara
 */
public final class MigrationUtils {
	private static final Logger logger = Logger.getLogger(MigrationUtils.class.getName());
	
	private MigrationUtils() {
	}

	/**
	 * Java 7 環境チェックを行ってもよいか。
	 * @throws Exception 
	 */
	public static boolean supportCheck() {
		// MAC アドレスをチェックせず全ての端末で環境チェックを行うか。
		if (EnvCheckConfiguration.alwaysMigrationCheck) {
			return true;
		}
		
		try {
			String macAddress = Login.getMacAddress();
		
			ConnectionWrapper con = SystemInfo.getBaseConnection();
			
			boolean ret = isSupportJavaUpdate(con, macAddress);
			logger.info("Java 7 アップデート可能か: mac=" + macAddress + ", java_update=" + ret);
			
			return ret;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "環境チェック対象かチェック中にエラー", e);
			
			return false;
		}
	}	

	private static boolean isSupportJavaUpdate(ConnectionWrapper con, String macAddress) throws SQLException {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select java_update from mst_mac");
		sql.append(" where mac_address in (" + macAddress + ")");
		sql.append(" and java_update = true");
		
		ResultSetWrapper rs = con.executeQuery(sql.toString());
		try {
			if (rs.next()) {
				return true;
			}
			
			return false;
		} finally {
			rs.close();
		}
	}

	/**
	 * 動作環境をチェックする。
	 */
	public static void checkEnvironment() throws Exception {
		boolean needRepair = false;
		
		if (!new InstallCheckLogic().needInstall()) {
			// Java インストール環境は問題なし。
			return;
		}
		
		// 環境チェッカを起動する。
		IOUtils.downloadFile(EnvCheckConfiguration.setupFilename);
		
		// 起動する。
		{
			// インストール完了後に SPOS を起動できる場合は起動できるよう、jnlp ファイルの URL をインストーラに渡す。
			String jnlpURL = null;
			
			String origFilenameArg = System.getProperty("jnlpx.origFilenameArg");
			if (origFilenameArg != null) {
				try {
					File file = new File(origFilenameArg);

					if (file.exists()) {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document document = builder.parse(file);
						NodeList jnlps = document.getElementsByTagName("jnlp");
						Node jnlp = jnlps.item(0);
						NamedNodeMap attributes = jnlp.getAttributes();
						
						Node codebaseNode = attributes.getNamedItem("codebase");
						String codebase = codebaseNode.getNodeValue();
						
						Node hrefNode = attributes.getNamedItem("href");
						String href = hrefNode.getNodeValue();
						
						jnlpURL = codebase + href;
						
						logger.info("jnlp ファイルからの起動検出: jnlpx.origFilenameArg=" + origFilenameArg + ", jnlpURL=" + jnlpURL);
					} else {
						logger.info("jnlp ファイルを検出したが、ファイルが見つからない。jnlpx.origFilenameArg=" + origFilenameArg);
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, "jnlp ファイルの読み込みに失敗: origFilenameArg=" + origFilenameArg, e);
				}
			} else {
				logger.info("jnlp ファイルを検出せず。jnlpx.origFilenameArg=" + origFilenameArg);
			}
				
			String commandArgs = null;

			if (jnlpURL != null) {
				commandArgs = "/jnlp " + jnlpURL;
			}

			IOUtils.start(EnvCheckConfiguration.setupFilename, commandArgs);
		}
		
		// Sosia POS は終了する。
		System.exit(0);
	}
}
