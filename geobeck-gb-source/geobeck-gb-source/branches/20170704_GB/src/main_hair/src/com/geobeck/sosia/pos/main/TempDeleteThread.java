/*
 * TempDeleteThread.java
 *
 * Created on 2009/03/19, 17:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.main;

import java.util.logging.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.io.File;

/**
 * temp�t�@�C���̍폜�p�o�b�`���쐬�����s����
 * @author iida
 */
public class TempDeleteThread extends Thread 
{
	/**
	 * Creates a new instance of TempDeleteThread
	 */
	public TempDeleteThread()
	{
	}
	
	
	public void run(){
        
            SystemInfo.getLogger().log(Level.INFO, "temp�t�@�C���폜�J�n");

            try {

                File dir = new File(System.getenv(SystemInfo.getTempDirStr()));
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory() && files[i].getName().startsWith("swing-mozilla")) {
                        deleteFolder(files[i]);
                    }
                }

                if (SystemInfo.isWindows()) {

                    String s = "cmd /c del /q "+ System.getenv(SystemInfo.getTempDirStr()) + "/*.* \n";
                    System.out.println(s);
                    Process process = Runtime.getRuntime().exec(s);
                    process.waitFor();

                    s = "cmd /c del /q "+ System.getenv(SystemInfo.getTempDirStr()) + "/ \n";
                    System.out.println(s);
                    process = Runtime.getRuntime().exec(s);
                    process.waitFor();

                } else {

                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec( "cd " + System.getenv(SystemInfo.getTempDirStr()));
                    runtime.exec( "rm  *.pdf *.xls *.xlt" );

                    /*
                    String s = ;
                    Process process = Runtime.getRuntime().exec(s);
                    process.waitFor();

                    s = "rm "+ System.getenv(SystemInfo.getTempDirStr()) + "/*.xls";
                    process = Runtime.getRuntime().exec(s);
                    process.waitFor();

                    s = "rm "+ System.getenv(SystemInfo.getTempDirStr()) + "/*.xlt";
                    process = Runtime.getRuntime().exec(s);
                    process.waitFor();
                     *
                     */

                }

            } catch(Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            SystemInfo.getLogger().log(Level.INFO, "temp�t�@�C���폜�I��");
        }
        
        private void deleteFolder(File f) {
           
            if (!f.exists() ) return ;
            if (f.isFile()) f.delete();

            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolder(files[i]);
                }
                f.delete();
            }
        }

}
