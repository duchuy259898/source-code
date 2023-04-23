/*
 * AbstractCardCommunication.java
 *
 * Created on 2008/09/02, 11:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.pointcard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import java.util.Vector;
import com.geobeck.barcode.SerialParameters;

/*
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
*/

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;


/**
 * リライトカードリーダライター用基底クラス
 * @author takeda
 */
//abstract public class AbstractCardCommunication implements SerialPortEventListener{
abstract public class AbstractCardCommunication{
    
    final public static int STAT_NORMAL       = 0;        // 正常
    final public static int STAT_NON_CARD     = 11;       // カードなし
    final public static int STAT_NON_STRIPE   = 12;       // データストライプなし
    final public static int STAT_CARD_REMAIN  = 13;       // 挿入口カード残留
    
    // エラー
    final public static int STAT_ERR_PARITY   = 51;       // モーター異常
    final public static int STAT_ERR_NOMARK   = 52;       // 開始終了符号なし
    final public static int STAT_ERR_LRC      = 53;       // ＬＲＣエラー
    final public static int STAT_ERR_CHAR     = 54;       // 異常キャラクター
    final public static int STAT_ERR_WRITE    = 55;       // 書き込み
    final public static int STAT_ERR_JAM      = 56;       // カードジャム
    final public static int STAT_ERR_OPEN     = 57;       // カバーオープン
    final public static int STAT_ERR_READ     = 58;       // 読み込み
    
    // 緊急エラー
    final public static int STAT_ERR_MOTOR    = 91;       // モーター異常
    final public static int STAT_ERR_HEAD     = 92;       // ヘッド異常
    final public static int STAT_ERR_EEPROM   = 93;       // EEPROM異常
    final public static int STAT_ERR_PRINT    = 94;       // プリント
    
    // その他エラー
    final public static int STAT_ERR_BUFFER   = 101;      // 展開バッファオーバーフロー
    
    
    final public static int STAT_ERR_OTHERS   = 255;      // 不明なエラー
    
    // 電文符号
    final protected static byte _STX = (byte)0x02;       // テキスト開始
    final protected static byte _ETX = (byte)0x03;       // テキスト終了
    final protected static byte _ENQ = (byte)0x05;       // 調査要求
    final protected static byte _ACK = (byte)0x06;       // 正常応答
    final protected static byte _NAK = (byte)0x15;       // 異常応答
    final protected static byte _DLE = (byte)0x10;       // 拒否応答
    final protected static byte _LF  = (byte)0x0A;       // 改行
    final protected static byte _ESC = (byte)0x1B;       // エスケープシーケンス
    
    private SerialPort      _serialPort;
    private InputStream     _in;
    private OutputStream    _out;
    private int             _mode;
    protected int          _nTimeout;       // タイムアウト
    protected int          _nRetry;         // リトライ回数
    
    private boolean        _bOpen = false;
    
    // 受信電文バッファ
    Vector<Byte> _vecRecvBuffer = new Vector<Byte>();
    
    // ポート設定
    private SerialParameters	_para;
    protected int _LastError = 0;
    
    
    /**
     * コンストラクタ
     * @param para 通信パラメータ
     */
    public AbstractCardCommunication(SerialParameters para)
    {
        this._para = para;
        _nTimeout = 1200;
        _nRetry   = 3;
        _bOpen = false;
    }
    
    /*
     * Vector配列を電文用配列に変換する
     * （Vector<Byte> => byte[]に変換する）
     */
    /**
     * Byte型のVector配列をbyte配列に変換する
     * @param vecArray Vector文字列
     * @return byte配列
     */
    protected byte[] VecToCommArray(Vector<Byte> vecArray)
    {
        byte[] retBuf;
        
        // byte[]へ変換
        retBuf = new byte[vecArray.size()];
        for(int ii=0; ii<vecArray.size(); ii++){
            retBuf[ii] = vecArray.get(ii);
        }
        
        return retBuf;
    }

    /**
     * ポートのオープン処理
     * @return 成功／失敗
     */
    public boolean Open( )
    {
        boolean ret = false;
        try{

            System.out.println("ポイントカード：ポートオープン：ポート名：" + _para.getPortName());
            
            CommPortIdentifier port = CommPortIdentifier.getPortIdentifier(_para.getPortName());
            _serialPort = (SerialPort)port.open("CardReaderWriter", 30000);
            // シリアルポートのパラメータを設定
            _serialPort.setSerialPortParams(_para.getBaudRate(),
                            _para.getDatabits(), _para.getStopbits(), _para.getParity());
            // Flow Control Mode
            _serialPort.setFlowControlMode(_para.getFlowControlIn() | 
                                              _para.getFlowControlOut());
            
            _serialPort.enableReceiveTimeout(100);
            
            // 入力ストリームを取得
            _in = _serialPort.getInputStream();
            // 出力ストリームを取得
            _out = _serialPort.getOutputStream();
            
            /*
            // イベントリスナー登録
            _serialPort.addEventListener(this);
            // シリアルポートがデータを受信した際に教えてねと設定
            _serialPort.notifyOnDataAvailable(true);
            */
            
            _serialPort.setRTS(true);
            _serialPort.setDTR(true);
            
            _bOpen = true;
            ret = true;
        }catch(NoSuchPortException ex){
            ex.printStackTrace();
            //System.err.println(ex);
        }catch(PortInUseException ex){
            System.err.println(ex);
        }catch(UnsupportedCommOperationException ex){
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        //} catch (TooManyListenersException ex){
        //    System.err.println(ex);
        }
        return ret;
    }
    
    /**
     * ポートクローズ
     * @return 成功／失敗
     */
    public boolean Close()
    {
        boolean ret = false;
        if( !_bOpen ) return true;
        try {
            System.out.println("ポイントカード：ポートクローズ");
            _in.close();
            _out.close();
            _serialPort.close();
                    
            _bOpen = false;
            ret = true;
        } catch (IOException ex) {
            System.err.println(ex);
        }        
        return ret;
    }

    /**
     * 受信バッファをクリアする
     */
    protected void claerRecvBuffer()
    {
        if( !_bOpen ) return;
        try {
            while(true)
            {
                int readBuf = _in.read();
                if( readBuf == -1 ){
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        /*
        synchronized(_vecRecvBuffer){
            _vecRecvBuffer.clear();
        }
        */
    }
    
    // ++++ ポーリング処理に変更したため不要 
    /**
     * シリアルイベント処理
     * データを受信バッファにプールする
     * @param arg0 
     * /
    public void serialEvent(SerialPortEvent arg0) {
        if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            int newData = 0;
    		while (newData != -1){
                try {
                    newData = _in.read();// 入力ストリームから読み込み
                    
                    // 受信があればバッファに追加していく
                    if (newData >=0 ){
                        synchronized(_vecRecvBuffer){
                            _vecRecvBuffer.add((byte)newData);
                        }
                    }
                    
                } catch (IOException ex) {
                    System.err.println(ex);
                    return;
                }
            }
        }
    }
     */
    
    /**
     * データ送信
     * @param msg 送信データ
     * @return 成功／失敗
     */
    protected boolean Send(byte[] msg)
    {
        boolean ret = false;
        if(!_bOpen) Open();
        
        try {
            //System.out.println("Length=" + msg.length);
            if( msg.length  > 98 ){
                for(int ii = 0; ii*50 < msg.length; ii++){
                    if( ii * 50 + 49 > msg.length ){
                        _out.write(msg, ii * 50, msg.length - ii * 50);
                    }else{
                        _out.write(msg, ii * 50, 50);
                    }
                }
            }else{
                // 出力ストリームにデータを書き込み、フラッシュ
                _out.write(msg, 0, msg.length);
            }
            
            StringBuffer buf = new StringBuffer();
            buf.append("send=");
            for(int ii = 0; ii < msg.length; ii++){
                buf.append(String.format("x%02x", msg[ii]));
            }
            System.out.println(buf.toString());
            //_out.write(msg);
            _out.flush();
            ret = true;
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return ret;
    }
    
    /**
     * データ受信
     * データを１バイト受信
     * @return -1:タイムアウト
     * 以外:受信バイト
     */
    protected int Recv()
    {
        int bufLen, waitTime = 0;
        int byteData = -1;
        final int C_WAIT = 100;
        
        // タイムアウトまで、受信を監視する
        while( true ){
            try {
                // 入力ストリームから読み込み
                int newData = _in.read();// 入力ストリームから読み込み
                
                if( newData >= 0 ){
                    // 受信データをバッファから取り出す
                    byteData = newData;
                    break;
                }
                
                if( waitTime > _nTimeout ){
                    // タイムアウト
                    break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try{
                waitTime += C_WAIT;
                Thread.sleep(C_WAIT);  // 100ms待ち
            }catch( InterruptedException ex){
                System.err.println(ex);
            }            
            
            /*
            try{
                synchronized(_vecRecvBuffer){
                     bufLen = _vecRecvBuffer.size();
                }
                if( waitTime > _nTimeout ){
                    // タイムアウト
                    break;
                }
                if( bufLen == 0 ){
                    waitTime += C_WAIT;
                    Thread.sleep(C_WAIT);  // 100ms待ち
                }else{
                    // 受信データをバッファから取り出す
                    byteData = _vecRecvBuffer.remove(0) & 0xFF;
                    break;
                }
            }catch( InterruptedException ex){
                System.err.println(ex);
            }
             */
        }
        
        return byteData;
    }
    
    /**
     * 半角数値を全角数値に変換する
     * @param num 半角数字文字列
     * @return 全角数字文字列
     */
    public static String NumberToWideNumber(String num) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            if (c >= '0' && c <= '9') {
                buf.append((char) (c - '0' + '０'));
            }else if (c == ',') {
                buf.append((char) ('，'));
            }else if (c == '-') {
                buf.append((char) ('－'));
            }else if (c == '+') {
                buf.append((char) ('＋'));
            }else{
                buf.append((char) (c));
            }
        }
        return buf.toString();
    }    
    
    // 機能関数
    /**
     * 発生したエラーを取得する
     */
    abstract public int getLastError();
    
    /**
     * 初期化処理
     * @return 成功／失敗
     */
    abstract public boolean Initialize();
    
    /**
     * 状態取得
     * @return 成功／失敗
     */
    abstract public boolean Status();
    
    /**
     * 磁気面読み込み
     * @return 成功／失敗
     */
    abstract public boolean Read();
    abstract public boolean Read(Integer tracParam, Integer formatParam);
    /**
     * 読み込みデータ取得
     * @param data 
     * @return 成功／失敗
     */
    abstract public int getReadData(StringBuffer data);
    /**
     * カード挿入待ちのキャンセル
     * @return 成功／失敗
     */
    abstract public boolean Cancel();
    /**
     * 磁気面書き込み
     * @param data 
     * @return 成功／失敗
     */
    abstract public boolean Write(String data);
    /**
     * カード排出
     * @return 成功／失敗
     */
    abstract public boolean Eject();
    /**
     * 印刷面プリント
     * @return 成功／失敗
     */
    abstract public boolean Print();
    /**
     * 印刷面クリア
     * @return 成功／失敗
     */
    abstract public boolean Clear();
    /**
     * クリーン
     * @return 成功／失敗
     */
    abstract public boolean Clean();

    // 印刷内容設定
    /**
     * 印刷バッファをクリア
     * @return 成功／失敗
     */
    abstract public boolean clearPrintBuffer();
    
    /**
     * 印刷バッファへデータを追加
     * @param Line 印刷行
     * @param Pos 印刷位置  ※A31のこのバージョンでは無効
     * @param Width 横倍角(1-4)
     * @param Height 縦倍角(1-4)
     * @param data 印刷データ
     * @return 成功／失敗
     */
    abstract public boolean addPrintBuffer(int Line, int pos, int Width, int Height, String data);
     
    /**
     * 印刷バッファへデータを追加
     * @param Line 印刷行
     * @param Width 横倍角(1-4)
     * @param Height 縦倍角(1-4)
     * @param data 印刷データ
     * @return 成功／失敗
     */
    public boolean addPrintBuffer(int Line, int Width, int Height, String data)
    {
        return addPrintBuffer(Line, 1, Width, Height, data);
    }

    /**
     * 印刷バッファへデータを追加
     * @param Line 印刷行
     * @param data 印刷データ
     * @return 成功／失敗
     */
    public boolean addPrintBuffer(int Line, String data)
    {
        return addPrintBuffer(Line, 1, 1, 1, data);
    }
    
    /**
     * 印刷バッファへデータを追加
     * @param Line 印刷行
     * @param Pos 印刷位置  ※A31のこのバージョンでは無効
     * @param data 印刷データ
     * @return 成功／失敗
     */
    public boolean addPrintBuffer(int Line, int Pos, String data)
    {
        return addPrintBuffer(Line, Pos, 1, 1, data);
    }
    
    /**
     * 一行の最大文字数を返す
     *
     */
    public int getMaxChars()
    {
        return 24;
    }
    
    /**
     * クリーニング実行中か？
     * @return true 実行中
     */
    abstract public boolean isCleannig();
    
    
    /**
     * 発生エラーメッセージを取得（各機種共通と出来る？）
     */
    public String getLastMessage()
    {
        String ret = "";
        switch( _LastError ) 
        {
        // 軽微なエラー
        case STAT_NON_CARD:
            ret = "カードなし";
            break;
        case STAT_NON_STRIPE:
            ret = "磁気面が読み取れません。";
            break;
        case STAT_CARD_REMAIN:
            ret = "挿入口にカードが残っています。";
            break;
           
            
        // 警告エラー系
        case STAT_ERR_PARITY:
            ret = "パリティエラーです。装置のクリーニングを行ってください。";
            break;
        case STAT_ERR_NOMARK:
            ret = "開始符号／終了符号なしエラーです。装置のクリーニングを行ってください。";
            break;
        case STAT_ERR_LRC:
            ret = "ＬＲＣエラーです。装置のクリーニングを行ってください。";
            break;
        case STAT_ERR_CHAR:
            ret = "異常キャラクタです。装置のクリーニングを行ってください。";
            break;
        case STAT_ERR_WRITE:
            ret = "書き込みに失敗しました。装置のクリーニングを行ってください。";
            break;
        case STAT_ERR_JAM:
            ret = "カード詰まりです。装置内のカードを取り除いてください。";
            break;
        case STAT_ERR_OPEN:
            ret = "カバーが開いています。";
            break;
        case STAT_ERR_READ:
            ret = "読み込みに失敗しました。装置のクリーニングを行ってください。";
            break;
            
        // 緊急エラー系
        case STAT_ERR_MOTOR:
            ret = "カムモーター異常です。サービスマンに連絡してください。";
            break;
        case STAT_ERR_HEAD:
            ret = "イレースヘッド温度異常です。サービスマンに連絡してください。";
            break;
        case STAT_ERR_EEPROM:
            ret = "ＥＥＰＲＯＭ異常です。サービスマンに連絡してください。";
            break;
        case STAT_ERR_BUFFER:
            ret = "展開バッファオーバーフローです。";
            break;
        case STAT_ERR_PRINT:
            ret = "プリンタエラーです。サービスマンに連絡してください。";
            break;
        default:
            ret = "不明なエラーです。";
            break;
        }
        return ret;
    }

    public int getMode() {
        return _mode;
    }

    public void setMode(int mode) {
        this._mode = mode;
    }
   
}
