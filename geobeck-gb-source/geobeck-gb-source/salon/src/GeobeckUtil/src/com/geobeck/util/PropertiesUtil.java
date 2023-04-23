package com.geobeck.util;

import	java.io.*;
import java.util.*;

import com.geobeck.sosia.pos.system.SystemInfo;


public class PropertiesUtil{
	private static String folderPath = System.getenv(SystemInfo.getTempDirStr());
	public PropertiesUtil(){}
	
	public static String readValue(String fileName,String key) {
		String propertiesPath =folderPath + "/" + fileName;
		File propertiesFile=new File(propertiesPath);
		if(propertiesFile.exists()){
			Properties props = new Properties();
			try {
				InputStream in = new BufferedInputStream (new FileInputStream(propertiesPath));
				props.load(in);
				String value = props.getProperty (key);
				if (value == null) {
					return "";
				} else {
					return value;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		} else {
			return "";
		}

	}
	
	public static void writeProperties(String fileName,String parameterName,String parameterValue) {
		Properties prop = new Properties();
		String propertiesPath =folderPath + "/" + fileName;
		File propertiesFile=new File(propertiesPath);
		if(!propertiesFile.exists()){
			createPropertiesFile(fileName);
		}
		try {
			InputStream fis = new BufferedInputStream (new FileInputStream(propertiesPath));
			prop.load(fis);
			OutputStream fos = new BufferedOutputStream (new FileOutputStream(propertiesPath));
			prop.setProperty(parameterName, parameterValue);
			prop.store(fos, "Update '" + parameterName + "' value");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createPropertiesFile(String fileName) {
		Properties prop = new Properties();
		String propertiesPath =folderPath + "/" + fileName;
		File propertiesFile=new File(propertiesPath);
		try {
			propertiesFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}