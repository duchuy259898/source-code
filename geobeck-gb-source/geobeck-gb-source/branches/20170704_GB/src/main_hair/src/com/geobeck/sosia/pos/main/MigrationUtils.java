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
 * �V�X�e���ڍs�Ɋւ���c�[���B
 * 
 * @author mahara
 */
public final class MigrationUtils {
	private static final Logger logger = Logger.getLogger(MigrationUtils.class.getName());
	
	private MigrationUtils() {
	}

	/**
	 * Java 7 ���`�F�b�N���s���Ă��悢���B
	 * @throws Exception 
	 */
	public static boolean supportCheck() {
		// MAC �A�h���X���`�F�b�N�����S�Ă̒[���Ŋ��`�F�b�N���s�����B
		if (EnvCheckConfiguration.alwaysMigrationCheck) {
			return true;
		}
		
		try {
			String macAddress = Login.getMacAddress();
		
			ConnectionWrapper con = SystemInfo.getBaseConnection();
			
			boolean ret = isSupportJavaUpdate(con, macAddress);
			logger.info("Java 7 �A�b�v�f�[�g�\��: mac=" + macAddress + ", java_update=" + ret);
			
			return ret;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "���`�F�b�N�Ώۂ��`�F�b�N���ɃG���[", e);
			
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
	 * ��������`�F�b�N����B
	 */
	public static void checkEnvironment() throws Exception {
		boolean needRepair = false;
		
		if (!new InstallCheckLogic().needInstall()) {
			// Java �C���X�g�[�����͖��Ȃ��B
			return;
		}
		
		// ���`�F�b�J���N������B
		IOUtils.downloadFile(EnvCheckConfiguration.setupFilename);
		
		// �N������B
		{
			// �C���X�g�[��������� SPOS ���N���ł���ꍇ�͋N���ł���悤�Ajnlp �t�@�C���� URL ���C���X�g�[���ɓn���B
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
						
						logger.info("jnlp �t�@�C������̋N�����o: jnlpx.origFilenameArg=" + origFilenameArg + ", jnlpURL=" + jnlpURL);
					} else {
						logger.info("jnlp �t�@�C�������o�������A�t�@�C����������Ȃ��Bjnlpx.origFilenameArg=" + origFilenameArg);
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, "jnlp �t�@�C���̓ǂݍ��݂Ɏ��s: origFilenameArg=" + origFilenameArg, e);
				}
			} else {
				logger.info("jnlp �t�@�C�������o�����Bjnlpx.origFilenameArg=" + origFilenameArg);
			}
				
			String commandArgs = null;

			if (jnlpURL != null) {
				commandArgs = "/jnlp " + jnlpURL;
			}

			IOUtils.start(EnvCheckConfiguration.setupFilename, commandArgs);
		}
		
		// Sosia POS �͏I������B
		System.exit(0);
	}
}
