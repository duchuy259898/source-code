/*
 * SanwaA31.java
 *
 * Created on 2008/09/02, 11:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.pointcard;

import com.geobeck.sosia.pos.hair.pointcard.InsertCardDialog;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.barcode.SerialParameters;
import com.geobeck.sosia.pos.system.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.io.*;

/**
 * 
 * @author takeda
 */
public class NatecCRWPOS extends AbstractCardCommunication {
    
    // ����f�[�^
    private StringBuffer _PrintData = new StringBuffer();
    
    private String _controlFile = "/NatecCRWPOS.exe";
    private ExecThread _execThread = null;
    
    /**
     * �O���R�}���h���s�N���X
     */
    private class ExecThread extends Thread {

        private boolean isProcessing = false;
        private Process process = null;
        private String result = null;
        
        public ExecThread(String command) {
            this.process = exec(command);
        }
        
        public void run(){
            isProcessing = true;
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                result = br.readLine();
                process.waitFor();
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            isProcessing = false;
        }

        public void close(){
            process.destroy();
            isProcessing = false;
        }
    }
    
    private Process exec(String command) {

        Process result = null;
        
        try {

            result = Runtime.getRuntime().exec(
                        ReportManager.getExportPath()
                        + _controlFile + " "
                        + SystemInfo.getPointcardParameters().getPortName() + " "
                        + command);

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }
    
    private void copyFile(String file) throws Exception
    {
        InputStream in = getClass().getResource(file).openStream();
        OutputStream out = new FileOutputStream(ReportManager.getExportPath() + file);

        try {   
            byte[] buf = new byte[1024];   
            int len = 0;   

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);   
            }   

            out.flush();   
        } finally {   
            out.close();
            in.close();   
        }
    }
    
    /**
     * �R���X�g���N�^
     * �e�̃R���X�g���N�^���ďo��
     * @param para �ʐM�p�����[�^�[
     */
    public NatecCRWPOS(SerialParameters para)
    {
        super(para);
        
        try {
            this.copyFile("/msvcp60.dll");
            this.copyFile("/POSCRW.dll");
            this.copyFile(_controlFile);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    // �@�\�֐�
    public int getLastError()
    {
        return _LastError;
    }
    // </editor-fold>                        
    
    public boolean Status()
    {
        return true;
    }
    
    /**** ���s�n���� ***/
    /**
     * ���������s
     * @return �����^���s
     */
    public boolean Initialize()
    {
        return true;
    }
    
    /**
     * �f�[�^�ǂݍ��ݎ��s
     * @return �����^���s
     */
    public boolean Read()
    {
        return true;
    }

    public boolean Read(Integer tracParam, Integer formatParam){
        return Read();
    }
    
    /**
     * 
     * @param RecvData 
     * @return �����^���s
     */
    public int getReadData(StringBuffer RecvData) {

        int ret = -1;    // �}���҂�
        _LastError = STAT_NORMAL;

        switch (this.getMode()) {

            case InsertCardDialog.MODE_CLEAR:
                
                if (_execThread == null) {
                    try {
                        _execThread = new ExecThread("CLEAR");
                        _execThread.start();
                        _execThread.sleep(1000);
                    } catch(Exception e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    }
                }
                
                if (_execThread.isProcessing) {
                    return -1;
                } else {
                    
                    if (_execThread.result != null && _execThread.result.equals("NG")) {
                        _LastError = STAT_NON_STRIPE;
                    }
                    
                    return 0;
                }
                
            case InsertCardDialog.MODE_READ:

                if (_execThread == null) {
                    try {
                        _execThread = new ExecThread("READ");
                        _execThread.start();
                        _execThread.sleep(1000);
                    } catch(Exception e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    }
                }
                
                if (_execThread.isProcessing) {
                    return -1;
                } else {
                    if (_execThread.result != null) {
                        if (_execThread.result.equals("NG")) {
                            _LastError = STAT_NON_STRIPE;
                        } else {
                            RecvData.append(_execThread.result);
                            RecvData.setLength(RecvData.toString().trim().length());
                        }
                    }
                    return RecvData.length();
                }

            case InsertCardDialog.MODE_PRINT:

                if (_execThread == null) {
                    try {
                        _execThread = new ExecThread("READ_NO_EJECT");
                        _execThread.start();
                        _execThread.sleep(1000);
                    } catch(Exception e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    }
                }
                
                if (_execThread.isProcessing) {
                    return -1;
                } else {
                    if (_execThread.result != null) {
                        RecvData.append(_execThread.result);
                        RecvData.setLength(RecvData.toString().trim().length());
                    }
                    return RecvData.length();
                }

        }

        clearPrintBuffer();
        
        return -1;
    }
    
    /**
     * �J�[�h�}���҂��L�����Z�����s
     * @return �����^���s
     */
    public boolean Cancel()
    {
        return true;
    }
    
    /**
     * �f�[�^�������ݎ��s
     * @param msg �������ݕ�����
     * @return �����^���s
     */
    public boolean Write(String msg)
    {
        String status = null;
        
        Process p = exec("WRITE " + msg);
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            status = br.readLine();
            p.waitFor();
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        if (status != null && status.equals("NG")) {
            _LastError = STAT_NON_STRIPE;
            return false;
        }
        
        return true;
    }
    
    /**
     * �f�[�^������s
     * @return �����^���s
     */
    public boolean Print()
    {
        String status = null;
        
        Process p = exec("PRINT " + _PrintData.toString());
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            status = br.readLine();
            p.waitFor();
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        if (status != null && status.equals("NG")) {
            _LastError = STAT_NON_STRIPE;
            return false;
        }

        return true;
    }
    
    /**
     * �J�[�h�r�o���s
     * @return �����^���s
     */
    public boolean Eject()
    {
        if (_execThread != null) {
            _execThread.close();
            _execThread = null;
        }
        
        Process p = exec("EJECT");
        try {
            p.waitFor();
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return true;
    }
    
    /**
     * ����o�b�t�@���N���A
     * @return �����^���s
     */
    public boolean clearPrintBuffer()
    {
        _PrintData = new StringBuffer();
        return true;
    }
    
    /**
     * ����o�b�t�@�փf�[�^��ǉ�
     * @param Line ����s
     * @param Pos ����ʒu  �����̃o�[�W�����ł͖���
     * @param Width ���{�p(1-4)
     * @param Height �c�{�p(1-4)
     * @param data ����f�[�^
     * @return �����^���s
     */
    public boolean addPrintBuffer(int Line, int Pos, int Width, int Height, String data)
    {
        final String VB_CR_LF = "VB_CR_LF";
        
        if (Line == 1) data = "���No. " + data;
        if (Line == 8) data = "�S���F " + data;
        
        
        if (Line < 17) {

            if (Height > 1) _PrintData.append("[VE]");
            if (Line == 5) _PrintData.append("�@[SS]");
            
            _PrintData.append(data);
            
            if (Line != 4) {
                _PrintData.append(VB_CR_LF);
            }
            
            if (Line == 2 || Line == 8) {
                _PrintData.append(VB_CR_LF);
            }
        }
        
        return true;
   }
    
    /**
     * ����ʂ��N���A
     * @return �����^���s
     */
    public boolean Clear()
    {
        return _LastError == STAT_NORMAL;
    }
    
    /**
     * �N���[��
     * @return �����^���s
     */
    public boolean Clean()
    {
        try {
            _execThread = new ExecThread("CLEAN");
            _execThread.start();
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return true;
    }
    
    public boolean isCleannig()
    {
        return _execThread.isProcessing;
    }
    
    /**
     * ��s�̍ő啶������Ԃ�
     *
     */
    @Override
    public int getMaxChars()
    {
        return 23;
    }

    /**
     * �|�[�g�̃I�[�v������
     * @return �����^���s
     */
    @Override
    public boolean Open( )
    {
        return true;
    }
    
    /**
     * �|�[�g�N���[�Y
     * @return �����^���s
     */
    @Override
    public boolean Close()
    {
        return true;
    }

}

