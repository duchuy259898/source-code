/*
 * StarTCP300II.java
 *
 * Created on 2008/09/07, 11:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.pointcard;

import com.geobeck.barcode.SerialParameters;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Star精密製リライトカードリーダーライター制御用クラス
 * @author takeda
 */
public class StarTCP300 extends AbstractCardCommunication {
    
    // <editor-fold defaultstate="collapsed" desc="通信電文用定数">                          
    // コマンド
    final private static byte _CMD_INITIAL     = (byte)0x5F;   // 初期化コマンド
    final private static byte _CMD_READ        = (byte)0x26;   // リードコマンド
    final private static byte _CMD_PARAM_READ  = (byte)0x28;   // リードコマンド（パラメーター付き）
    final private static byte _CMD_SET_WDATA_R7 = (byte)0x36;   // ライトトラックデータ設定コマンド
    final private static byte _CMD_SET_WDATA_7  = (byte)0x39;   // ライトトラックデータ設定コマンド
    final private static byte _CMD_SET_WDATA    = (byte)0x3C;   // ライトトラックデータ設定コマンド
    final private static byte _CMD_WRITE        = (byte)0x32;   // ライトコマンド
    
    final private static byte _CMD_CLR_BUF     = (byte)0x49;   // プリントバッファクリア
    final private static byte _CMD_SET_BUF     = (byte)0x41;   // プリントバッファ設定
    final private static byte _CMD_PRINT       = (byte)0x46;   // プリントコマンド(消去／印字／排出)
    final private static byte _CMD_EJECT       = (byte)0x50;   // カード排出コマンド
    final private static byte _CMD_CANCEL      = (byte)0x54;   // キャンセルコマンド
    final private static byte _CMD_CLEAN       = (byte)0x52;   // クリーニングコマンド
    final private static byte _CMD_STAT        = (byte)0x59;   // ステータス要求コマンド
    
    // ステータス
    final private static byte _STAT_NORMAL     = (byte)0x20;   // 正常
    final private static byte _STAT_NO_CARD    = (byte)0x22;   // カードなし
    final private static byte _STAT_ERR_CARD   = (byte)0x23;   // カード異常
    
    final private static byte _STAT_ERR_PARITY = (byte)0x31;   // パリティーエラー
    final private static byte _STAT_ERR_MARK   = (byte)0x32;   // 開始符号／終了符号なし
    final private static byte _STAT_ERR_LRC    = (byte)0x33;   // ＬＲＣエラー
    final private static byte _STAT_ERR_CHAR   = (byte)0x34;   // 異常キャラクタ
    final private static byte _STAT_ERR_WRITE  = (byte)0x37;   // 書き込みエラー
    final private static byte _STAT_ERR_JAM    = (byte)0x38;   // カードジャム
    final private static byte _STAT_ERR_COVER  = (byte)0x40;   // カバーオープン
    final private static byte _STAT_ERR_MOTOR  = (byte)0x42;   // カムモータ異常
    final private static byte _STAT_ERR_TEMP   = (byte)0x43;   // イレースヘッド異常
    final private static byte _STAT_ERR_EEPROM = (byte)0x44;   // ＥＥＰＲＯＭエラー
    final private static byte _STAT_ERR_BUFFER = (byte)0x51;   // ＥＥＰＲＯＭエラー
    
        
    // 電文位置情報
    final private static int _POS_STX  = 0;
    final private static int _POS_CMD  = 1;
    final private static int _POS_STAT = 2;
    final private static int _POS_DATA = 3;
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="メンバ変数">                          
    // 取得ステータス情報
    private Integer _sensor1 = 0;
    private Integer _sensor2 = 0;
    private Integer _sensor3 = 0;
    private Integer _sensor4 = 0;
    private Integer _cover   = 0;
    
    // 磁気面パラメーター
    private boolean _setParam = false;
    private String _trac = "";
    private String _format = "";
    
    // クリーニン取得用
    RecvString cleanRecv;
   
    // 印刷プールデータ
    ArrayList<PrintBuffer> printBuffer = new ArrayList<PrintBuffer>();
    // </editor-fold>                        
    
    /** Creates a new instance of StarTCP300 */
    public StarTCP300(SerialParameters para)
    {
        super(para);
    }

    // <editor-fold defaultstate="collapsed" desc="送信電文作成処理">                          
    /**
     * 通信電文を作成する
     */
    private Vector<Byte> makeCommString(byte cmd, Vector<byte[]> subcmd)
    {
        Vector<Byte> veclsbyte = new Vector<Byte>();
        
        veclsbyte.add(_STX);
        veclsbyte.add((byte)cmd);
        for(byte[] scmd : subcmd){
            for( int ii = 0; ii < scmd.length; ii++ ){
                veclsbyte.add((byte)scmd[ii]);
            }
            veclsbyte.add((byte)',');
        }
        if( subcmd.size() >0 ){ // 最後カンマを削除
            veclsbyte.setSize(veclsbyte.size()-1);
        }
        veclsbyte.add(_ETX);
        
        // BCCの計算
        byte byteBCC = 0;
        byteBCC = 0;
        
        // CMD～ETXまでのBCCを計算
        for(int ii=_POS_CMD; ii< veclsbyte.size(); ii++){
            byteBCC ^= veclsbyte.get(ii);
        }
        veclsbyte.add((byte)byteBCC);

        return veclsbyte;
    }
    
    /**
     * 実行コマンドを取得
     */
    private byte getCmd(byte[] cmd)
    {
        return cmd[_POS_CMD];
    }    
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="通信処理">                          
    /**
     * コマンドを実行
     */
    private boolean runCmd(byte[] runcmd)
    {
        boolean ret = false;
        int recvBuf;
        for(int ii = 0; ii< _nRetry; ii++){
            // 受信バッファを消去
            claerRecvBuffer();
            
            // 送信エラー
            if( !Send(runcmd) ){
                break;
            }
            
            // 結果を受信
            while( true ){
                recvBuf = Recv();
                System.out.println(String.format("recv=x%02x", recvBuf));
                
                if( recvBuf < 0 ||
                    recvBuf == _ACK ||
                    recvBuf == _NAK ){
                    break;
                }
            }
            
            // ACK以外受信ならば、再送
            if( recvBuf == _ACK ) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    
    /**
     * 実行結果を取得する
     */
    private RecvString getResponse(byte cmd)
    {
        int recvBuf;
        RecvString recv = new RecvString();
        
        // 結果を受信
        for(int ii = 0; ii< _nRetry; ii++){
            recv.init();
   
            // 結果を受信
            while( true ){
                recvBuf = Recv();
                if( recvBuf < 0 ){
                    break;
                }
                
                // 受信データを設定
                recv.put((byte)recvBuf);
                
                // 受信電文完成ならば、処理を抜ける
                if( recv.IsComplete() == true ){
                    recv.debugPrint();
                    
                    // 自分のコマンドで無ければ、処理を続ける
                    if( recv.getCMD() != cmd ) {
                        recv.init();
                        continue;
                    }
                    break;
                }
            }
            
            // タイムアウトもしくは、受信電文エラーならば再送要求
            if( recvBuf < 0 || recv.IsError() == true ){
                byte[] reqmsg = new byte[1];
                reqmsg[0] = _NAK;
                Send(reqmsg);       // NAK送信
            }else{
                byte[] reqmsg = new byte[1];
                reqmsg[0] = _ACK;
                Send(reqmsg);       // ACK送信
                break;
            }
        }

        return recv;
    }
    
    /**
     * 実行結果を取得する(待ちなし)
     */
    private int getResponseNoWait(RecvString recv, byte cmd)
    {
        int recvBuf = -1;
        
        // 結果を受信
        _nTimeout = 100;
        for(int ii = 0; ii< _nRetry; ii++){
            // 結果を受信
            
            // 結果を受信
            while( true ){
                recvBuf = Recv();
                if( recvBuf < 0 ){
                    break;
                }
                
                // 受信データを設定
                recv.put((byte)recvBuf);
                
                // 受信電文完成ならば、処理を抜ける
                if( recv.IsComplete() == true ){
                    // 自分のコマンドで無ければ、処理を続ける
                    if( recv.getCMD() != cmd ) {
                        recv.init();
                        continue;
                    }
                    break;
                }
            }

            // 受信電文完成ならば、処理を抜ける
            if( recv.IsComplete() == true ){
                // 自分のコマンドで無ければ、処理を続ける
                if( recv.getCMD() != cmd ) {
                    recv.init();
                }else{
                    // 受信電文エラーならば再送要求
                    if( recv.IsError() == true ){
                        byte[] reqmsg = new byte[1];
                        reqmsg[0] = _NAK;
                        Send(reqmsg);       // NAK送信
                    }else{
                        byte[] reqmsg = new byte[1];
                        reqmsg[0] = _ACK;
                        Send(reqmsg);       // ACK送信
                        break;
                    }
                }
            }
        }

        return recvBuf;
    }
    
    
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="受信処理">                          
    /**
     * 受信電文組み立てクラス
     *
     */
    private class RecvString{
        // 受信データを作成する
        private int _nStat;              // 状態ステータス
        private int _nLen;               // 電文長
        private int _nBCC;               // BCC計算用
        private Vector<Byte> _Buf;
        
        private boolean _Complete;       // 電文完成
        private boolean _Error;
        
        RecvString(){
            // 変数の初期化
            _nStat = 0;
            _nLen  = 0;
            _nBCC  = 0;
            
            _Buf = new Vector<Byte>();

            _Complete = false;
            _Error = false;
        }
        
        /**
         * 電文完成？
         */
        public boolean IsComplete(){
            return _Complete;
        }
        
        /**
         * 受信電文エラー？
         */
        boolean IsError(){
            return _Error;
        }
        
        /**
         * CMD取得
         */
        private byte getCMD(){
            if(_Complete) return _Buf.get(_POS_CMD);
            return 0;
        }
        
        /**
         * STAT取得
         */
        private byte getSTAT(){
            if(_Complete) return _Buf.get(_POS_STAT);
            return 0;
        }
        
        /**
         * 受信データのデータ部分を取得する
         */
        private String getData()
        {
            if(_Complete){
                // データスタート位置からＥＴＸまでを文字列として取得する
                StringBuffer strBuf = new StringBuffer();
                for(int idx = _POS_DATA; idx < _Buf.size() - 1; idx++){
                    strBuf.append((char)(byte)_Buf.get(idx));
                }
                return strBuf.toString();
            }
            return new String();
        }

        /**
         * 受信状態初期化
         */
        private void init(){
            _nStat = 0;
        }
        
        /**
         * 受信電文設定
         */
        private boolean put(byte buf){
            if( _nStat == 0 ){  // STX待ち
                if( buf == _STX ){
                    _nStat = 1;     // 電文取得へ
                    _Complete = false;
                    _Error = false;
                    _Buf.clear();
                    _Buf.add(buf);
                }
            }else if( _nStat == 1 ){
                // 電文取得
                _nBCC ^= buf;
                _Buf.add(buf);
                if( buf == _ETX ){
                    _nStat = 2;     // BCC取得へ
                }
            }else if( _nStat == 2 ){
                _Complete = true;
                if( _nBCC != buf ){
                    // BCCエラー
                    _Error = true;
                    return false;
                }
            }
            return true;
        }
        
        public void debugPrint()
        {
            StringBuffer buf = new StringBuffer();
            buf.append("recv=");
            for(byte msg : _Buf){
                buf.append(String.format("x%02x", msg));
            }
            System.out.println(buf.toString());
        }
    }
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="電文作成関連">                          
    /**
     * サブメッセージなしのメッセージを作成
     */
    private byte[] getMessage(byte cmd)
    {
        Vector<byte[]> subcmd = new Vector<byte[]>();
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * カード書き込みデータ設定電文を取得
     */
    private byte[] getReadMessage()
    {
        if( !_setParam ){
            return getMessage(_CMD_READ);
        }
        
        byte cmd = (byte)_CMD_PARAM_READ;
        Vector<byte[]> subcmd = new Vector<byte[]>();
        subcmd.add(_trac.getBytes());
        subcmd.add(_format.getBytes());
        
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * カード書き込みデータ設定電文を取得
     */
    private byte[] getSetWriteDataMessage(String msg)
    {
        byte cmd = (byte)_CMD_SET_WDATA;
        if( _setParam ){
            if( _format.equals("0") ){
                cmd = (byte)_CMD_SET_WDATA_7;
                
            }else if( _format.equals("4") ){
                cmd = (byte)_CMD_SET_WDATA_R7;
            }
        }
        Vector<byte[]> subcmd = new Vector<byte[]>();
        subcmd.add(msg.getBytes());
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * カード書き込みデータ設定電文を取得
     */
    private byte[] getWriteMessage()
    {
        byte cmd = (byte)_CMD_WRITE;
        Vector<byte[]> subcmd = new Vector<byte[]>();
        subcmd.add("2".getBytes());
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * カード排出電文を取得
     */
    private byte[] getEjectMessage()
    {
        byte cmd = (Byte)_CMD_EJECT;
        Vector<byte[]> subcmd = new Vector<byte[]>();
        subcmd.add("1".getBytes());
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * プリントバッファクラス
     */
    private class PrintBuffer{
        private int line;
        private int pos;
        private int width;
        private int height;
        private String data;
        
        PrintBuffer(int line, int pos, int width, int height, String data){
            this.line = getPrintLine(line, height);
            this.pos = getPrintPos(pos, width);
            this.width = width;
            this.height = height;
            this.data = data;
        }
        
        private int getPrintLine(int Line, int Height)
        {
            int retLine;
            if( Height == 2 ){
                retLine = (Line + 1) * 24 - 1;
            }else{
                retLine = Line * 24 - 1;
            }
            if(retLine < 0) retLine = 0;
            return retLine;
        }

        private int getPrintPos(int Pos, int Width)
        {
            int retPos;

            if( Width == 2 ){
                retPos = ((Pos-1) * 2) * 12;
            }else{
                retPos = (Pos-1) * 12;
            }
            if(retPos < 0) retPos = 0;
            return retPos;
        }
        
        /**
         * 印刷バッファ設定電文を取得
         */
        byte[] getMessage()
        {
            byte cmd = (Byte)_CMD_SET_BUF;
            Vector<Byte> strPrint = new Vector<Byte>();
            Vector<byte[]> subcmd = new Vector<byte[]>();
            subcmd.add("0".getBytes());
            subcmd.add(String.valueOf(pos).getBytes());
            subcmd.add(String.valueOf(line).getBytes());

            if( width > 2 ){
                width = 2;
            }
            if( height > 2 ){
                height = 2;
            }

            if( width > 1 || height > 1 ){
                strPrint.add((byte)_ESC);
                if( width == 2 && height == 2 ){
                    for( byte b : "E22".getBytes() ) strPrint.add(b);
                }else if( width == 2 ){
                    for( byte b : "E12".getBytes() ) strPrint.add(b);
                }else if( height == 2 ){
                    for( byte b : "E21".getBytes() ) strPrint.add(b);
                }
            }

            try {
                byte[] sjisBytes = data.getBytes("Windows-31J");
                for(byte b : sjisBytes) strPrint.add(b);
            } catch (UnsupportedEncodingException e) {
                try {
                    byte[] sjisBytes = data.getBytes("SJIS");
                    for(byte b : sjisBytes) strPrint.add(b);
                } catch (UnsupportedEncodingException ex) {
                    e.printStackTrace();
                }
            }

            if( width > 1 || height > 1 ){
                strPrint.add((byte)_ESC);
                for( byte b : "E11".getBytes() ) strPrint.add(b);
            }
            subcmd.add(VecToCommArray(strPrint));

            return VecToCommArray(makeCommString(cmd, subcmd));
        }
    }
    
    /**
     * プリント電文を取得
     */
    private byte[] getPrintMessage()
    {
        byte cmd = (byte)_CMD_PRINT;
        Vector<byte[]> subcmd = new Vector<byte[]>();
        subcmd.add("0".getBytes());
        subcmd.add("1".getBytes());
        subcmd.add("1".getBytes());
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    // </editor-fold>                        

    // <editor-fold defaultstate="collapsed" desc="エラー関連処理">                          
    private void setLastError(byte Code)
    {
        _LastError = getErrorCode(Code);
    }

    private int getErrorCode(byte Code)
    {
        int ret;
        switch(Code){
        case _STAT_NORMAL:          // 正常
            ret = STAT_NORMAL;
            break;
        case _STAT_NO_CARD:         // カードなし
            ret = STAT_NON_CARD;
            break;
        case _STAT_ERR_CARD:        // カード異常
            ret = STAT_NON_STRIPE;
            break;
        case _STAT_ERR_PARITY:      // パリティーエラー
            ret = STAT_ERR_PARITY;       // モーター異常
            break;
        case _STAT_ERR_MARK:        // 開始符号／終了符号なし
            ret = STAT_ERR_NOMARK;       // 開始終了符号なし
            break;
        case _STAT_ERR_LRC:         // ＬＲＣエラー
            ret = STAT_ERR_LRC;          // ＬＲＣエラー
            break;
        case _STAT_ERR_CHAR:        // 異常キャラクタ
            ret = STAT_ERR_CHAR;        // 異常キャラクター
            break;
        case _STAT_ERR_WRITE:       // 書き込みエラー
            ret = STAT_ERR_WRITE;        // 書き込み
            break;
        case _STAT_ERR_JAM:         // カードジャム
            ret = STAT_ERR_JAM;          // カードジャム
            break;
        case _STAT_ERR_COVER:       // カバーオープン
            ret = STAT_ERR_OPEN;         // カバーオープン
            break;
        case _STAT_ERR_MOTOR:       // カムモータ異常
            ret = STAT_ERR_MOTOR;        // モーター異常
            break;
        case _STAT_ERR_TEMP:        // イレースヘッド異常
            ret = STAT_ERR_HEAD;         // ヘッド異常
            break;
        case _STAT_ERR_EEPROM:      // ＥＥＰＲＯＭエラー
            ret = STAT_ERR_EEPROM;       // EEPROM異常
            break;
        case _STAT_ERR_BUFFER:      // 展開バッファオーバーフロー
            ret = _STAT_ERR_BUFFER;
            break;
        default:
            ret = STAT_ERR_OTHERS;      // 不明なエラー
            break;
        }
        return ret;
    }
    
    // 機能関数
    public int getLastError()
    {
        return _LastError;
    }
    // </editor-fold>                        
    
    /**
     * 初期化コマンド実行
     */
    public boolean Initialize()
    {
        byte[] runcmd = getMessage(_CMD_INITIAL);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 3000;
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }

    /**
     * ステータースを取得
     */
    public boolean Status()
    {
        byte[] runcmd = getMessage(_CMD_STAT);
        boolean ret = false;
        _nTimeout = 1000;
        ret = runCmd(runcmd);
        if( ret == true ){
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( recv.IsError() ){
                ret = false;
            }else{
                // 各センサー状態を記憶
                String str = recv.getData();
                _sensor1 = Integer.valueOf(str.substring(0,1));
                _sensor2 = Integer.valueOf(str.substring(1,2));
                _sensor3 = Integer.valueOf(str.substring(2,3));
                _sensor4 = Integer.valueOf(str.substring(3,4));
                _cover   = Integer.valueOf(str.substring(4,5));
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * 磁気面読み込み
     */
    public boolean Read()                        
    {
        _trac = "";
        _format = "";
        _setParam = false;
        
        // この機種では、ここではなにも処理を行わず、データ取り出し側で行う
        return true;
    }
    
    /**
     * パラメータ付きリード
     */
    public boolean Read(Integer tracParam, Integer tracFormat ){
        //_trac = String.valueOf(tracParam);
        _trac = "2";    // TCP300では、"2"のみ対応
        _format = String.valueOf(tracFormat);
        _setParam = true;
        return true;
    }
    
    /**
     * 読み込みデータ取得
     */
    public int getReadData(StringBuffer RecvData) 
    {
        int ret = -1;   // エラー
        byte[] runcmd;
        runcmd = getReadMessage();
        _nTimeout = 1000;
        
        if( runCmd(runcmd) ){
            _nTimeout = 6000;
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( !recv.IsError() ){
                if( recv.getSTAT() == _STAT_NORMAL ){
                    // データ取得
                    RecvData.append(recv.getData());
                    ret = RecvData.length();
                }
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * カード挿入待ちのキャンセル
     */
    public boolean Cancel()                     
    {
        byte[] runcmd = getMessage(_CMD_CANCEL);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 3000;
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * 書き込みデータを設定
     */
    private boolean setWriteData( String data )
    {
        byte[] runcmd = getSetWriteDataMessage(data);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
        
            // エラーチェック
            if( recv.getSTAT() == _STAT_NORMAL ){
                ret = true;
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        
        return ret;
    }
    
    /**
     * 磁気面書き込み
     */
    public boolean Write(String data)           
    {
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = setWriteData(data);
        if( ret == true ){
            _nTimeout = 1000;
            // 書き込み実行
            byte[] runcmd = getWriteMessage();
            ret = runCmd(runcmd);
            if( ret == true ){
                // 返信電文受信
                RecvString recv = getResponse(getCmd(runcmd));

                // エラーチェック
                if( recv.getSTAT() == _STAT_NORMAL ){
                    ret = true;
                }
                // ステータスセット
                setLastError(recv.getSTAT());
            }
        }
        return ret;
    }
    
    /**
     * カード排出
     */
    public boolean Eject()                       
    {
        
        byte[] runcmd = getEjectMessage();
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 2000;
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * 印刷面プリント
     */
    public boolean Print()
    {
        // プリントバッファクリア要求
        if( !sendClearPrintBuffer() ){
            return false;
        }
        
        // プリントバッファを送信
        for( PrintBuffer buf : printBuffer ){
            if( !sendAddPrintBuffer(buf) ){
                return false;
            }
        }
        
        if( !sendPrint() ){
            return false;
        }
        
        return true;
    }
        
    /**
     * 印刷開始を送信
     */
    private boolean sendPrint(){
        byte[] runcmd = getPrintMessage();
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 10000;
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * プリントバッファにデータを追加する
     */
    public boolean clearPrintBuffer()
    {
        printBuffer.clear();
        return true;
    }
    
    /*
     * 印刷用バッファをクリア
     */
    public boolean sendClearPrintBuffer()
    {
        byte[] runcmd = getMessage(_CMD_CLR_BUF);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * プリントバッファにデータを追加する
     */
    public boolean addPrintBuffer(int Line, int Pos, int Width, int Height, String Data)
    {
        printBuffer.add(new PrintBuffer(Line, Pos, Width, Height, Data));
        return true;
    }
    
    /**
     * プリントデータを送信する
     */
    private boolean sendAddPrintBuffer(PrintBuffer buf)
    {
        byte[] runcmd = buf.getMessage();
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 3000;
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            // エラーチェック
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // ステータスセット
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    
    /**
     * 印刷面をクリア
     */
    public boolean Clear()
    {
        clearPrintBuffer();
        addPrintBuffer(1, 1, 1, "");
        return Print();
    }
    
    /**
     * クリーン
     */
    public boolean Clean()
    {
        byte[] runcmd = getMessage(_CMD_CLEAN);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        
        if( ret == true ){
            // 終了電文受信用変数を初期化
            cleanRecv = new RecvString();
            cleanRecv.init();
        }
        return ret;
    }
    
    /**
     * 読み込みデータ取得
     */
    public boolean isCleannig() 
    {
        boolean ret = true;
        
        // 返信電文受信
        if( getResponseNoWait(cleanRecv, _CMD_CLEAN) >= 0 ){
            // 受信電文完成
            if( cleanRecv.IsComplete() == true ){
                ret = false;
                
                // ステータスセット
                setLastError(cleanRecv.getSTAT());
            }
        }
        
        return ret;
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
}
