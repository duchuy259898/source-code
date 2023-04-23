/*
 * SanwaA31.java
 *
 * Created on 2008/09/02, 11:27
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
 * 
 * @author takeda
 */
public class SanwaA31 extends AbstractCardCommunication {
    
    // コマンド
    final static private byte _CMD_INITIAL = (byte)0x10;     // 初期化コマンド
    final static private byte _CMD_STATUS  = (byte)0x20;     // ステータスコマンド
    final static private byte _CMD_READ    = (byte)0x33;     // リードコマンド
    final static private byte _CMD_CANCEL  = (byte)0x40;     // キャンセルコマンド
    final static private byte _CMD_WRITE   = (byte)0x53;     // ライトコマンド
    final static private byte _CMD_PRINT   = (byte)0x7C;     // プリントコマンド
    final static private byte _CMD_EJECT   = (byte)0x80;     // カード排出コマンド
    final static private byte _CMD_CLEAN   = (byte)0xA0;     // クリーニングコマンド
    
    // 最新機器状態
    private byte RPS1;
    private byte RPS2;
    private byte RPS3;
    
    // レスポンスデータ
    final static private byte _RPS1_NONE_CARD   = (byte)0x30;    // 装置内カードなし
    final static private byte _RPS1_EXIST_CARD  = (byte)0x31;    // 装置内カードあり
    final static private byte _RPS1_STAT_ERR1   = (byte)0x32;    // 以外のエラー
    final static private byte _RPS1_STAT_ERR2   = (byte)0x33;    // センサ５の異常
    final static private byte _RPS1_CARD_ERR    = (byte)0x34;    // 排出カードが装置にあり
    
    final static private byte _RPS2_COMPLETE    = (byte)0x30;    // 正常終了
    final static private byte _RPS2_READ_ERR    = (byte)0x31;    // リードエラー
    final static private byte _RPS2_WRITE_ERR   = (byte)0x32;    // ライトエラー
    final static private byte _RPS2_JAM_ERR     = (byte)0x33;    // カード詰まりエラー
    final static private byte _RPS2_PRINT_ERR   = (byte)0x35;    // プリンターエラー
    final static private byte _RPS2_CARD_ERR    = (byte)0x38;   // 装置内のカードが抜き去られたエラー
    
    final static private byte _RPS3_CMD_END     = (byte)0x30;    // コマンド終了
    final static private byte _RPS3_CAN_NOT_RUN = (byte)0x32;    // コマンド実行不可
    final static private byte _RPS3_RUNNING     = (byte)0x33;    // コマンド実行中
    final static private byte _RPS3_CARD_WAIT   = (byte)0x34;    // カード挿入待ち
    
    // 電文位置情報
    final private int _POS_STX  = 0;
    final private int _POS_LEN  = 1;
    final private int _POS_CMD  = 2;
    final private int _POS_RPS1 = 3;
    final private int _POS_RPS2 = 4;
    final private int _POS_RPS3 = 5;
    final private int _POS_DATA = 6;
    
    // 印刷データ
    private int         _PrintLine = 0;
    private final int _MAX_LINE = 20;
    private StringBuffer[] _PrintData = new StringBuffer[_MAX_LINE];
    private int[]          _PrintFlag = new int[_MAX_LINE];
    
    /**
     * コンストラクタ
     * 親のコンストラクタを呼出す
     * @param para 通信パラメーター
     */
    public SanwaA31(SerialParameters para)
    {
        super(para);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Send Strings">                          
    /**
     * 通信電文を作成する
     */
    private Vector<Byte> makeCommString(Byte cmd, Vector<Byte> subcmd)
    {
        Vector<Byte> veclsbyte = new Vector<Byte>();
        
        veclsbyte.add(_STX);
        veclsbyte.add((byte)0);     // 長さは後で変更する
        veclsbyte.add((byte)cmd);
        veclsbyte.add((byte)0);    // RPS1
        veclsbyte.add((byte)0);    // RPS2
        veclsbyte.add((byte)0);    // RPS3
        veclsbyte.addAll(subcmd);
        veclsbyte.add(_ETX);
        
        // LENの設定（CMD～BCCまでの長さ）
        veclsbyte.set(_POS_LEN, (byte)(veclsbyte.size() - 1));
        
        // BCCの計算
        byte byteBCC = 0;
        byteBCC = 0;
        
        // LEN～ETXまでのBCCを計算
        for(int ii=_POS_LEN; ii< veclsbyte.size(); ii++){
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
        return cmd[2];
    }    
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="Comminucate">                          
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
            byte[] reqmsg = new byte[1];
            reqmsg[0] = _ENQ;
            Send(reqmsg);       // ENQ送信
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
                }else if( recvBuf == _ENQ ){
                    break;
                }
            }
            // タイムアウトもしくは、問い合わせ要求、受信電文エラーならば再送要求
            if( recvBuf < 0 ||
                recvBuf == _ENQ ||
                recv.IsError() == true ){
                continue;
            }
            break;
        }

        return recv;
    }
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="Recv Strings">                          
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
         * RPS1取得
         */
        private byte getRPS1(){
            if(_Complete) return _Buf.get(_POS_RPS1);
            return 0;
        }

        /**
         * RPS2取得
         */
        private byte getRPS2(){
            if(_Complete) return _Buf.get(_POS_RPS2);
            return 0;
        }

        /**
         * RPS3取得
         */
        private byte getRPS3(){
            if(_Complete) return _Buf.get(_POS_RPS3);
            return 0;
        }
        
        /**
         * 受信データのデータ部分を取得する
         */
        String getData()
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
                    _nStat = 1;     // LEN待ちへ
                    _Complete = false;
                    _Error = false;
                    _Buf.clear();
                    _Buf.add(buf);
                }
            }else if( _nStat == 1 ){
                _nLen = buf;
                _nBCC = buf;
                _nStat = 2;     // 受信待ちへ
                _Buf.add(buf);
            }else if( _nStat == 2 ){
                // 電文完成
                if( _nLen == _Buf.size() - 1 ){
                    _Complete = true;
                    // BCCチェック
                    if( _nBCC != buf ){
                        // BCCエラー
                        _Error = true;
                        return false;
                    }
                }else{
                    _nBCC ^= buf;
                    _Buf.add(buf);
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
    
    /**** 電文作成系 ***/
    /**
     * 初期化命令電文を取得
     */
    private byte[] getInitialMessage()
    {
        Byte cmd = (Byte)_CMD_INITIAL;
        Vector<Byte> subcmd = new Vector<Byte>();
        subcmd.add((byte)0x31);
        subcmd.add((byte)0x31);
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * カード読み込み電文を取得
     */
    private byte[] getReadMessage()
    {
        Byte cmd = (Byte)_CMD_READ;
        Vector<Byte> subcmd = new Vector<Byte>();
        subcmd.add((byte)0x30);
        subcmd.add((byte)0x30);
        subcmd.add((byte)0x30);
        return VecToCommArray(makeCommString(cmd, subcmd));
    }

    /**
     * カード書き込み電文を取得
     */
    private byte[] getWriteMessage(String msg)
    {
        Byte cmd = (Byte)_CMD_WRITE;
        Vector<Byte> subcmd = new Vector<Byte>();
        subcmd.add((byte)0x30);
        subcmd.add((byte)0x30);
        subcmd.add((byte)0x30);
        
        final int C_MAXSIZE = 69+3;
        subcmd.setSize(C_MAXSIZE);
        for(int ii = 3; ii < (msg.length() + 3) && ii < C_MAXSIZE; ii++){
            subcmd.set(ii, (byte)msg.charAt(ii-3));
        }
        for(int ii = msg.length()+3; ii < C_MAXSIZE; ii++){
            subcmd.set(ii, (byte)0);
        }
        
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * 印刷電文を取得
     */
    private byte[] getPrintMessage( int line, int toline )
    {
        Byte cmd = (Byte)_CMD_PRINT;
        Vector<Byte> subcmd = new Vector<Byte>();
        subcmd.add((byte)0x30);
        subcmd.add((byte)0x30);
        subcmd.add((byte)line);
        
        StringBuffer buf = new StringBuffer();
        for( line = line-1; line < toline; line++ ){
            if( _PrintFlag[line] >= 0 ){
                buf.append(_PrintData[line]);
                buf.append("\r");
            }
        }
        String msg = buf.toString();
        
        try {
            byte[] sjisBytes = msg.getBytes("Windows-31J");
            for(int ii = 0; ii < sjisBytes.length; ii++){
                subcmd.add((byte)sjisBytes[ii]);
            }
        } catch (UnsupportedEncodingException e) {
            try {
                byte[] sjisBytes = msg.getBytes("SJIS");
                for(int ii = 0; ii < sjisBytes.length; ii++){
                    subcmd.add((byte)sjisBytes[ii]);
                }
            } catch (UnsupportedEncodingException ex) {
                e.printStackTrace();
            }
        }
        
        
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * サブメッセージなしのメッセージを作成
     */
    private byte[] getMessage(byte cmd)
    {
        Vector<Byte> subcmd = new Vector<Byte>();
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    // <editor-fold defaultstate="collapsed" desc="エラー関連処理">                          
    private void setLastError( byte cmd, byte rps1, byte rps2, byte rps3 )
    {
        this.RPS1 = rps1;
        this.RPS2 = rps2;
        this.RPS3 = rps3;
        
        if( cmd == _CMD_READ && rps1 == _RPS1_CARD_ERR ){
            _LastError = STAT_CARD_REMAIN;
        }else{
            _LastError = getErrorCode(rps2);
        }
    }

    private int getErrorCode(byte Code)
    {
        int ret;
        switch(Code){
        case _RPS2_COMPLETE:          // 正常
            ret = STAT_NORMAL;
            break;
        case _RPS2_CARD_ERR:        // カードが抜き去られた
            ret = STAT_NON_CARD;       // カードなし
            break;
        case _RPS2_READ_ERR:        // 読み込みエラー
//            ret = STAT_ERR_READ;        // 読み込み
            ret = STAT_NON_STRIPE;        // 読み込み
            break;
        case _RPS2_WRITE_ERR:       // 書き込みエラー
            ret = STAT_ERR_WRITE;        // 書き込み
            break;
        case _RPS2_JAM_ERR:         // カードジャム
            ret = STAT_ERR_JAM;          // カードジャム
            break;
        case _RPS2_PRINT_ERR:       // プリンタエラー
            ret = STAT_ERR_PRINT;        // プリンタ
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
    
    public boolean Status()
    {
        byte[] runcmd = getMessage(_CMD_STATUS);
        boolean ret = false;
        ret = runCmd(runcmd);
        if( ret == true ){
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            
            // エラーチェック
            if( recv.IsError() ){
                ret = false;
            }else{
                // ステータスセット
                setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
            }
        }
        return ret;
    }
    
    /**** 実行系命令 ***/
    /**
     * 初期化実行
     * @return 成功／失敗
     */
    public boolean Initialize()
    {
        byte[] runcmd = getInitialMessage();
        boolean ret = false;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            // 返信電文受信
            RecvString recv = getResponse(getCmd(runcmd));
            
            // エラーチェック
            if( recv.IsError() || recv.getRPS2() != _RPS2_COMPLETE){
                ret = false;
            }
            // ステータスセット
            setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
        }
        return ret;
    }
    
    /**
     * データ読み込み実行
     * @return 成功／失敗
     */
    public boolean Read()
    {
        byte[] runcmd = getReadMessage();
        boolean ret = false;
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // 返信電文受信
                RecvString recv = getResponse(getCmd(runcmd));

                // エラーチェック
                if( recv.IsError() ){
                    ret = false;
                }

                // カード挿入待ちになったらOK
                if( recv.getRPS2() == _RPS2_COMPLETE && recv.getRPS3() == _RPS3_CARD_WAIT ){
                    ret = true;
                    break;
                }else{
                    ret = false;
                }
                
                // ステータスセット
                setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
                if( recv.getRPS1() == _RPS1_CARD_ERR ){
                    break;
                }
            }
            
            if( ++nCnt > 3 ){
                break;
            }
            
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){
                // 無視
            }    
        }
        return ret;
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
        
        RecvString recv = getResponse(_CMD_READ);
        // エラーチェック
        if( !recv.IsError() && recv.IsComplete() ){
            if( recv.getRPS3() == _RPS3_RUNNING || 
                recv.getRPS3() == _RPS3_CARD_WAIT ){
                ret = -1;
            }else if( recv.getRPS3() == _RPS3_CMD_END ){
                if( recv.getRPS2() == _RPS2_READ_ERR ){
                    ret = -2;
                }else{
                    RecvData.append(recv.getData());
                    RecvData.setLength(RecvData.toString().trim().length());
                    ret = RecvData.length();
                }
            }
            
            // ステータスセット
            setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
        }
        return ret;
    }
    
    /**
     * カード挿入待ちキャンセル実行
     * @return 成功／失敗
     */
    public boolean Cancel()
    {
        boolean ret = false;
        byte[] runcmd = getMessage(_CMD_CANCEL);
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // 返信電文受信
                RecvString recv = getResponse(getCmd(runcmd));

                // エラーチェック
                if( recv.IsError() || recv.getRPS2() != _RPS2_COMPLETE){
                    ret = false;
                }else{
                    ret = true;
                }
                
                if( !recv.IsError() ){
                    // ステータスセット
                    setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
                }
            }
            
            if( ret == true ) break;
            if( ++nCnt > 3 ){
                break;
            }
            
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){
                // 無視
            }    
        }
        return ret;
    }
    
    /**
     * データ書き込み実行
     * @param msg 書き込み文字列
     * @return 成功／失敗
     */
    public boolean Write(String msg)
    {
        byte[] runcmd = getWriteMessage(msg);
        boolean ret = false;
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                while(true){
                    // 返信電文受信
                    RecvString recv = getResponse(getCmd(runcmd));

                    // エラーチェック
                    if( recv.IsError() ){
                        ret = false;
                        break;
                    }
                    
                    // ステータスセット
                    setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
                    
                    if( recv.getRPS2() == _RPS2_COMPLETE ){
                        ret = true;
                    }else{
                        ret = false;
                        break;
                    }
                    if( recv.getRPS3() == _RPS3_CMD_END ){
                        break;
                    }else if( recv.getRPS3() == _RPS3_CAN_NOT_RUN ){
                        ret = false;
                        break;
                    }
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException ex){
                        // 無視
                    }    

                }
            }
            
            if( ret == true ) break;
            if( ++nCnt > 3 ){
                break;
            }
            
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){
                // 無視
            }    
        }
        return ret;
    }
    
    /**
     * データ印刷実行
     * @return 成功／失敗
     */
    public boolean Print()
    {
        boolean ret = false;
        int nTimes = 1;
        int sumbyte = 0;
        for( int line = 0; line < _MAX_LINE; line++ ){
            if( _PrintFlag[line] > 0 ){
                sumbyte += _PrintFlag[line];
            }
        }
        sumbyte += 20;  // 改行分を追加
        
        if( sumbyte > 249 ){
            // 2回に分けて印字する
            // 1-8
            // 9-20
            nTimes = 2;
        }
        /*
        if( sumbyte > 98 ){
            // 2回に分けて印字する
            // 1-8
            // 9-20
            nTimes = 6;
        }
         */
        
        
        for( int ii = 0; ii < nTimes; ii++  ){
            byte[] runcmd;
            
            if( nTimes == 6 ){
                if( ii == 0 ){
                    runcmd = getPrintMessage(1, 5);
                }else{
                    int pos = ii*3+3;
                    runcmd = getPrintMessage(pos, pos+2);
                }
            }else
            if( nTimes == 2 ){
                if( ii == 0 ){
                    runcmd = getPrintMessage(1, 8);
                }else{
                    runcmd = getPrintMessage(9, 20);
                }
            }else{
                runcmd = getPrintMessage(1, 20);
            }
            int nCnt = 0;

            while(true){
                ret = runCmd(runcmd);
                if( ret == true ){
                    ret = false;
                    while(true){
                        // 返信電文受信
                        RecvString recv = getResponse(getCmd(runcmd));

                        // エラーチェック
                        if( recv.IsError() ){
                            ret = false;
                            break;
                        }
                        
                        // ステータスセット
                        setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
                        
                        if( recv.getRPS2() == _RPS2_COMPLETE ){
                            ret = true;
                        }else{
                            ret = false;
                        }
                        
                        if( recv.getRPS3() == _RPS3_CMD_END ){
                            break;
                        }else if( recv.getRPS3() == _RPS3_CAN_NOT_RUN ){
                            ret = false;
                            break;
                        }
                       try{
                            Thread.sleep(100);
                        }catch (InterruptedException ex){
                            // 無視
                        }    
                    }
                }
                if( ret == true ) break;
                if( ++nCnt > 3 ){
                    break;
                }

                try{
                    Thread.sleep(100);
                }catch (InterruptedException ex){
                    // 無視
                }    
            }
            if( ret == false ) break;
        }
        return ret;
    }
    
    /**
     * カード排出実行
     * @return 成功／失敗
     */
    public boolean Eject()
    {
        boolean ret = false;
        byte[] runcmd = getMessage(_CMD_EJECT);
        
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // 返信電文受信
                RecvString recv = getResponse(getCmd(runcmd));

                // エラーチェック
                if( recv.IsError() || recv.getRPS2() != _RPS2_COMPLETE){
                    ret = false;
                }else{
                    ret = true;
                }
                
                // ステータスセット
                setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
            }

            if( ret == true ) break;
            if( ++nCnt > 3 ){
                break;
            }
            
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){
                // 無視
            }    
        }
        return ret;
    }
    
    /**
     * 印刷バッファをクリア
     * @return 成功／失敗
     */
    public boolean clearPrintBuffer()
    {
        for( int line = 0; line < _MAX_LINE; line++  ){
            _PrintData[line] = new StringBuffer();
            _PrintFlag[line] = 0;
        }
        return true;
    }
    
    /**
     * 印刷バッファへデータを追加
     * @param Line 印刷行
     * @param Pos 印刷位置  ※A31のこのバージョンでは無効
     * @param Width 横倍角(1-4)
     * @param Height 縦倍角(1-4)
     * @param data 印刷データ
     * @return 成功／失敗
     */
    public boolean addPrintBuffer(int Line, int Pos, int Width, int Height, String data)
    {
        int SetLine = Line - 1;
        if( Width > 4 ){
            Width = 4;
        }
        if( Height > 4 ){
            Height = 4;
        }
        SetLine += Height - 1;
        
        if( Height > 1 ) _PrintFlag[SetLine-1] = -1;
        if( Height > 2 ) _PrintFlag[SetLine-2] = -1;
        if( Height > 3 ) _PrintFlag[SetLine-3] = -1;
        
        if( Width > 1 || Height > 1 ){
            _PrintData[SetLine].append((char)0x1B);
            _PrintData[SetLine].append((char)0x73);
            _PrintData[SetLine].append((char)(0x30 + Height));
            _PrintData[SetLine].append((char)(0x30 + Width));
        }
        
        _PrintData[SetLine].append(data);
        
        if( Width > 1 || Height > 1 ){
            _PrintData[SetLine].append((char)0x14);
        }
        
        
        try {
            byte[] sjisBytes = _PrintData[SetLine].toString().getBytes("Windows-31J");
            _PrintFlag[SetLine] = sjisBytes.length;
        } catch (UnsupportedEncodingException e) {
            try {
                byte[] sjisBytes = _PrintData[SetLine].toString().getBytes("SJIS");
                _PrintFlag[SetLine] = sjisBytes.length;
            } catch (UnsupportedEncodingException ex) {
                e.printStackTrace();
                _PrintData[SetLine] = new StringBuffer();
                _PrintFlag[SetLine] = 0;
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
        clearPrintBuffer();
        return Print();
    }
    
    /**
     * クリーン
     * @return 成功／失敗
     */
    public boolean Clean()
    {
        boolean ret = false;
        byte[] runcmd = getMessage(_CMD_CLEAN);
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // 返信電文受信
                RecvString recv = getResponse(getCmd(runcmd));

                // エラーチェック
                if( recv.IsError() ){
                    ret = false;
                }

                // カード挿入待ちになったらOK
                if( recv.getRPS2() == _RPS2_COMPLETE && recv.getRPS3() == _RPS3_CARD_WAIT ){
                    ret = true;
                    break;
                }else{
                    ret = false;
                }
                
                // ステータスセット
                setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
                if( recv.getRPS1() == _RPS1_CARD_ERR ){
                    break;
                }
            }
            
            if( ret == true ) break;
            if( ++nCnt > 3 ){
                break;
            }
            
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){
                // 無視
            }    
        }
        return ret;
    }
    
    public boolean isCleannig()
    {
        boolean ret = Status();
        
        if( this.RPS3 ==  _RPS3_CARD_WAIT ){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 一行の最大文字数を返す
     *
     */
    @Override
    public int getMaxChars()
    {
        return 26;
    }
    
}

