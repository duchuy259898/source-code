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
    
    // 印刷データ
    private StringBuffer _PrintData = new StringBuffer();
    
    private String _controlFile = "/NatecCRWPOS.exe";
    private ExecThread _execThread = null;
    
    /**
     * 外部コマンド実行クラス
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
     * コンストラクタ
     * 親のコンストラクタを呼出す
     * @param para 通信パラメーター
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
    
    // 機能関数
    public int getLastError()
    {
        return _LastError;
    }
    // </editor-fold>                        
    
    public boolean Status()
    {
        return true;
    }
    
    /**** 実行系命令 ***/
    /**
     * 初期化実行
     * @return 成功／失敗
     */
    public boolean Initialize()
    {
        return true;
    }
    
    /**
     * データ読み込み実行
     * @return 成功／失敗
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
     * @return 成功／失敗
     */
    public int getReadData(StringBuffer RecvData) {

        int ret = -1;    // 挿入待ち
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
     * カード挿入待ちキャンセル実行
     * @return 成功／失敗
     */
    public boolean Cancel()
    {
        return true;
    }
    
    /**
     * データ書き込み実行
     * @param msg 書き込み文字列
     * @return 成功／失敗
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
     * データ印刷実行
     * @return 成功／失敗
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
     * カード排出実行
     * @return 成功／失敗
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
     * 印刷バッファをクリア
     * @return 成功／失敗
     */
    public boolean clearPrintBuffer()
    {
        _PrintData = new StringBuffer();
        return true;
    }
    
    /**
     * 印刷バッファへデータを追加
     * @param Line 印刷行
     * @param Pos 印刷位置  ※このバージョンでは無効
     * @param Width 横倍角(1-4)
     * @param Height 縦倍角(1-4)
     * @param data 印刷データ
     * @return 成功／失敗
     */
    public boolean addPrintBuffer(int Line, int Pos, int Width, int Height, String data)
    {
        final String VB_CR_LF = "VB_CR_LF";
        
        if (Line == 1) data = "会員No. " + data;
        if (Line == 8) data = "担当： " + data;
        
        
        if (Line < 17) {

            if (Height > 1) _PrintData.append("[VE]");
            if (Line == 5) _PrintData.append("　[SS]");
            
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
     * 印刷面をクリア
     * @return 成功／失敗
     */
    public boolean Clear()
    {
        return _LastError == STAT_NORMAL;
    }
    
    /**
     * クリーン
     * @return 成功／失敗
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
     * 一行の最大文字数を返す
     *
     */
    @Override
    public int getMaxChars()
    {
        return 23;
    }

    /**
     * ポートのオープン処理
     * @return 成功／失敗
     */
    @Override
    public boolean Open( )
    {
        return true;
    }
    
    /**
     * ポートクローズ
     * @return 成功／失敗
     */
    @Override
    public boolean Close()
    {
        return true;
    }

}

