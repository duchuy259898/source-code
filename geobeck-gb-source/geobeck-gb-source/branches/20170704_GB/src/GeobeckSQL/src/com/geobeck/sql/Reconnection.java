/*
 * Reconnection.java
 *
 * Created on 2008/06/13, 13:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sql;

import java.util.logging.*;

/**
 *
 * @author iida
 */
public class Reconnection {
	//���s����̂ɑ҂��ԃ~���b�Ɖ�
	private	static final	int		WAIT_TIME	=	2000;
	private	static final	int		TRY_COUNT	=	10;
        
	private static ReconnectionHandler handler;
	
	/** Creates a new instance of Reconnection */
	public Reconnection() {
	}
	
	public ConnectionWrapper tryReconnection(ConnectionWrapper con, String driver
				               , String url, String database, String user
		                               , String pass, Logger logger){

            if (handler != null) {
                // SosiaPos.setMainFrameTitle("");
                handler.setTitle("");
            }

            ConnectionWrapper newcon = null;
            
            int i;
            for(i = 0;i < TRY_COUNT;i++){
                    try
                    {
                            //���Ă���ꍇ�͊J������
                            if(con.isClosed())
                            {
                                    con.open();
                            }

                            //�g�����U�N�V�������J�n���Ă���ꍇ�̓��[���o�b�N����
                            if(con.isBeginTran())
                            {
                                    con.rollback();
                                    con.commit();
                            }

                            boolean  b = con.execute("select current_timestamp as sysdate");
                            newcon = con;
                            break;

                    }
                    catch(Exception e)
                    {
                            try
                            {
                                    //������̎��s���Ă݂�
                                    con.close();
                            }catch(Exception ex)
                            {
                            }
                            con = new ConnectionWrapper(driver,
                                                            url + "/" + database,
                                                            user,
                                                            pass);
                            if(con.open()){
                                    newcon = con;
                                    logger.log(Level.INFO, "���l�b�g�Đڑ������I");
                                    break;
                            }
                    }

                    try
                    {
                            Thread.sleep(WAIT_TIME);
                    }
                    catch(InterruptedException e)
                    {
                    }
                    
                    if (handler != null) {
                        handler.setTitle("�@*** �f�[�^�x�[�X�ɐڑ����ł��B���΂炭���҂����������B �i" + (i + 1) + "�^" + TRY_COUNT + "�j");
                    }

                    logger.log(Level.INFO, "�ڑ����g���C�F" + i);
            }

            if(i >= TRY_COUNT){
                    logger.log(Level.INFO, "���l�b�g�ڑ����s�I");
                    newcon = null;
            }
                
            return newcon;
	}

     public static void setHandler(ReconnectionHandler newHandler) {
        handler = newHandler;
    }
}
