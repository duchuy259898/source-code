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
    
    // �R�}���h
    final static private byte _CMD_INITIAL = (byte)0x10;     // �������R�}���h
    final static private byte _CMD_STATUS  = (byte)0x20;     // �X�e�[�^�X�R�}���h
    final static private byte _CMD_READ    = (byte)0x33;     // ���[�h�R�}���h
    final static private byte _CMD_CANCEL  = (byte)0x40;     // �L�����Z���R�}���h
    final static private byte _CMD_WRITE   = (byte)0x53;     // ���C�g�R�}���h
    final static private byte _CMD_PRINT   = (byte)0x7C;     // �v�����g�R�}���h
    final static private byte _CMD_EJECT   = (byte)0x80;     // �J�[�h�r�o�R�}���h
    final static private byte _CMD_CLEAN   = (byte)0xA0;     // �N���[�j���O�R�}���h
    
    // �ŐV�@����
    private byte RPS1;
    private byte RPS2;
    private byte RPS3;
    
    // ���X�|���X�f�[�^
    final static private byte _RPS1_NONE_CARD   = (byte)0x30;    // ���u���J�[�h�Ȃ�
    final static private byte _RPS1_EXIST_CARD  = (byte)0x31;    // ���u���J�[�h����
    final static private byte _RPS1_STAT_ERR1   = (byte)0x32;    // �ȊO�̃G���[
    final static private byte _RPS1_STAT_ERR2   = (byte)0x33;    // �Z���T�T�ُ̈�
    final static private byte _RPS1_CARD_ERR    = (byte)0x34;    // �r�o�J�[�h�����u�ɂ���
    
    final static private byte _RPS2_COMPLETE    = (byte)0x30;    // ����I��
    final static private byte _RPS2_READ_ERR    = (byte)0x31;    // ���[�h�G���[
    final static private byte _RPS2_WRITE_ERR   = (byte)0x32;    // ���C�g�G���[
    final static private byte _RPS2_JAM_ERR     = (byte)0x33;    // �J�[�h�l�܂�G���[
    final static private byte _RPS2_PRINT_ERR   = (byte)0x35;    // �v�����^�[�G���[
    final static private byte _RPS2_CARD_ERR    = (byte)0x38;   // ���u���̃J�[�h����������ꂽ�G���[
    
    final static private byte _RPS3_CMD_END     = (byte)0x30;    // �R�}���h�I��
    final static private byte _RPS3_CAN_NOT_RUN = (byte)0x32;    // �R�}���h���s�s��
    final static private byte _RPS3_RUNNING     = (byte)0x33;    // �R�}���h���s��
    final static private byte _RPS3_CARD_WAIT   = (byte)0x34;    // �J�[�h�}���҂�
    
    // �d���ʒu���
    final private int _POS_STX  = 0;
    final private int _POS_LEN  = 1;
    final private int _POS_CMD  = 2;
    final private int _POS_RPS1 = 3;
    final private int _POS_RPS2 = 4;
    final private int _POS_RPS3 = 5;
    final private int _POS_DATA = 6;
    
    // ����f�[�^
    private int         _PrintLine = 0;
    private final int _MAX_LINE = 20;
    private StringBuffer[] _PrintData = new StringBuffer[_MAX_LINE];
    private int[]          _PrintFlag = new int[_MAX_LINE];
    
    /**
     * �R���X�g���N�^
     * �e�̃R���X�g���N�^���ďo��
     * @param para �ʐM�p�����[�^�[
     */
    public SanwaA31(SerialParameters para)
    {
        super(para);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Send Strings">                          
    /**
     * �ʐM�d�����쐬����
     */
    private Vector<Byte> makeCommString(Byte cmd, Vector<Byte> subcmd)
    {
        Vector<Byte> veclsbyte = new Vector<Byte>();
        
        veclsbyte.add(_STX);
        veclsbyte.add((byte)0);     // �����͌�ŕύX����
        veclsbyte.add((byte)cmd);
        veclsbyte.add((byte)0);    // RPS1
        veclsbyte.add((byte)0);    // RPS2
        veclsbyte.add((byte)0);    // RPS3
        veclsbyte.addAll(subcmd);
        veclsbyte.add(_ETX);
        
        // LEN�̐ݒ�iCMD�`BCC�܂ł̒����j
        veclsbyte.set(_POS_LEN, (byte)(veclsbyte.size() - 1));
        
        // BCC�̌v�Z
        byte byteBCC = 0;
        byteBCC = 0;
        
        // LEN�`ETX�܂ł�BCC���v�Z
        for(int ii=_POS_LEN; ii< veclsbyte.size(); ii++){
            byteBCC ^= veclsbyte.get(ii);
        }
        veclsbyte.add((byte)byteBCC);

        return veclsbyte;
    }
    
    /**
     * ���s�R�}���h���擾
     */
    private byte getCmd(byte[] cmd)
    {
        return cmd[2];
    }    
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="Comminucate">                          
    /**
     * �R�}���h�����s
     */
    private boolean runCmd(byte[] runcmd)
    {
        boolean ret = false;
        int recvBuf;
        for(int ii = 0; ii< _nRetry; ii++){
            // ��M�o�b�t�@������
            claerRecvBuffer();
            
            // ���M�G���[
            if( !Send(runcmd) ){
                break;
            }
            
            // ���ʂ���M
            while( true ){
                recvBuf = Recv();
                System.out.println(String.format("recv=x%02x", recvBuf));
                if( recvBuf < 0 ||
                    recvBuf == _ACK ||
                    recvBuf == _NAK ){
                    break;
                }
            }
            // ACK�ȊO��M�Ȃ�΁A�đ�
            if( recvBuf == _ACK ) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    
    /**
     * ���s���ʂ��擾����
     */
    private RecvString getResponse(byte cmd)
    {
        int recvBuf;
        RecvString recv = new RecvString();
        
        // ���ʂ���M
        for(int ii = 0; ii< _nRetry; ii++){
            byte[] reqmsg = new byte[1];
            reqmsg[0] = _ENQ;
            Send(reqmsg);       // ENQ���M
            recv.init();

            // ���ʂ���M
            while( true ){
                recvBuf = Recv();
                if( recvBuf < 0 ){
                    break;
                }
                
                // ��M�f�[�^��ݒ�
                recv.put((byte)recvBuf);
                
                // ��M�d�������Ȃ�΁A�����𔲂���
                if( recv.IsComplete() == true ){
                    recv.debugPrint();
                    
                    // �����̃R�}���h�Ŗ�����΁A�����𑱂���
                    if( recv.getCMD() != cmd ) {
                        recv.init();
                        continue;
                    }
                    break;
                }else if( recvBuf == _ENQ ){
                    break;
                }
            }
            // �^�C���A�E�g�������́A�₢���킹�v���A��M�d���G���[�Ȃ�΍đ��v��
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
     * ��M�d���g�ݗ��ăN���X
     *
     */
    private class RecvString{
        // ��M�f�[�^���쐬����
        private int _nStat;              // ��ԃX�e�[�^�X
        private int _nLen;               // �d����
        private int _nBCC;               // BCC�v�Z�p
        private Vector<Byte> _Buf;
        
        private boolean _Complete;       // �d������
        private boolean _Error;
        
        RecvString(){
            // �ϐ��̏�����
            _nStat = 0;
            _nLen  = 0;
            _nBCC  = 0;
            
            _Buf = new Vector<Byte>();

            _Complete = false;
            _Error = false;
        }
        
        /**
         * �d�������H
         */
        public boolean IsComplete(){
            return _Complete;
        }
        
        /**
         * ��M�d���G���[�H
         */
        boolean IsError(){
            return _Error;
        }
        
        /**
         * CMD�擾
         */
        private byte getCMD(){
            if(_Complete) return _Buf.get(_POS_CMD);
            return 0;
        }
        
        /**
         * RPS1�擾
         */
        private byte getRPS1(){
            if(_Complete) return _Buf.get(_POS_RPS1);
            return 0;
        }

        /**
         * RPS2�擾
         */
        private byte getRPS2(){
            if(_Complete) return _Buf.get(_POS_RPS2);
            return 0;
        }

        /**
         * RPS3�擾
         */
        private byte getRPS3(){
            if(_Complete) return _Buf.get(_POS_RPS3);
            return 0;
        }
        
        /**
         * ��M�f�[�^�̃f�[�^�������擾����
         */
        String getData()
        {
            if(_Complete){
                // �f�[�^�X�^�[�g�ʒu����d�s�w�܂ł𕶎���Ƃ��Ď擾����
                StringBuffer strBuf = new StringBuffer();
                for(int idx = _POS_DATA; idx < _Buf.size() - 1; idx++){
                    strBuf.append((char)(byte)_Buf.get(idx));
                }
                return strBuf.toString();
            }
            return new String();
        }

        /**
         * ��M��ԏ�����
         */
        private void init(){
            _nStat = 0;
        }
        
        /**
         * ��M�d���ݒ�
         */
        private boolean put(byte buf){
            if( _nStat == 0 ){  // STX�҂�
                if( buf == _STX ){
                    _nStat = 1;     // LEN�҂���
                    _Complete = false;
                    _Error = false;
                    _Buf.clear();
                    _Buf.add(buf);
                }
            }else if( _nStat == 1 ){
                _nLen = buf;
                _nBCC = buf;
                _nStat = 2;     // ��M�҂���
                _Buf.add(buf);
            }else if( _nStat == 2 ){
                // �d������
                if( _nLen == _Buf.size() - 1 ){
                    _Complete = true;
                    // BCC�`�F�b�N
                    if( _nBCC != buf ){
                        // BCC�G���[
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
    
    /**** �d���쐬�n ***/
    /**
     * ���������ߓd�����擾
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
     * �J�[�h�ǂݍ��ݓd�����擾
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
     * �J�[�h�������ݓd�����擾
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
     * ����d�����擾
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
     * �T�u���b�Z�[�W�Ȃ��̃��b�Z�[�W���쐬
     */
    private byte[] getMessage(byte cmd)
    {
        Vector<Byte> subcmd = new Vector<Byte>();
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    // <editor-fold defaultstate="collapsed" desc="�G���[�֘A����">                          
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
        case _RPS2_COMPLETE:          // ����
            ret = STAT_NORMAL;
            break;
        case _RPS2_CARD_ERR:        // �J�[�h����������ꂽ
            ret = STAT_NON_CARD;       // �J�[�h�Ȃ�
            break;
        case _RPS2_READ_ERR:        // �ǂݍ��݃G���[
//            ret = STAT_ERR_READ;        // �ǂݍ���
            ret = STAT_NON_STRIPE;        // �ǂݍ���
            break;
        case _RPS2_WRITE_ERR:       // �������݃G���[
            ret = STAT_ERR_WRITE;        // ��������
            break;
        case _RPS2_JAM_ERR:         // �J�[�h�W����
            ret = STAT_ERR_JAM;          // �J�[�h�W����
            break;
        case _RPS2_PRINT_ERR:       // �v�����^�G���[
            ret = STAT_ERR_PRINT;        // �v�����^
            break;
        default:
            ret = STAT_ERR_OTHERS;      // �s���ȃG���[
            break;
        }
        return ret;
    }
    
    // �@�\�֐�
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
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            
            // �G���[�`�F�b�N
            if( recv.IsError() ){
                ret = false;
            }else{
                // �X�e�[�^�X�Z�b�g
                setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
            }
        }
        return ret;
    }
    
    /**** ���s�n���� ***/
    /**
     * ���������s
     * @return �����^���s
     */
    public boolean Initialize()
    {
        byte[] runcmd = getInitialMessage();
        boolean ret = false;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            
            // �G���[�`�F�b�N
            if( recv.IsError() || recv.getRPS2() != _RPS2_COMPLETE){
                ret = false;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
        }
        return ret;
    }
    
    /**
     * �f�[�^�ǂݍ��ݎ��s
     * @return �����^���s
     */
    public boolean Read()
    {
        byte[] runcmd = getReadMessage();
        boolean ret = false;
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // �ԐM�d����M
                RecvString recv = getResponse(getCmd(runcmd));

                // �G���[�`�F�b�N
                if( recv.IsError() ){
                    ret = false;
                }

                // �J�[�h�}���҂��ɂȂ�����OK
                if( recv.getRPS2() == _RPS2_COMPLETE && recv.getRPS3() == _RPS3_CARD_WAIT ){
                    ret = true;
                    break;
                }else{
                    ret = false;
                }
                
                // �X�e�[�^�X�Z�b�g
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
                // ����
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
     * @return �����^���s
     */
    public int getReadData(StringBuffer RecvData) {
        int ret = -1;    // �}���҂�
        
        RecvString recv = getResponse(_CMD_READ);
        // �G���[�`�F�b�N
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
            
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
        }
        return ret;
    }
    
    /**
     * �J�[�h�}���҂��L�����Z�����s
     * @return �����^���s
     */
    public boolean Cancel()
    {
        boolean ret = false;
        byte[] runcmd = getMessage(_CMD_CANCEL);
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // �ԐM�d����M
                RecvString recv = getResponse(getCmd(runcmd));

                // �G���[�`�F�b�N
                if( recv.IsError() || recv.getRPS2() != _RPS2_COMPLETE){
                    ret = false;
                }else{
                    ret = true;
                }
                
                if( !recv.IsError() ){
                    // �X�e�[�^�X�Z�b�g
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
                // ����
            }    
        }
        return ret;
    }
    
    /**
     * �f�[�^�������ݎ��s
     * @param msg �������ݕ�����
     * @return �����^���s
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
                    // �ԐM�d����M
                    RecvString recv = getResponse(getCmd(runcmd));

                    // �G���[�`�F�b�N
                    if( recv.IsError() ){
                        ret = false;
                        break;
                    }
                    
                    // �X�e�[�^�X�Z�b�g
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
                        // ����
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
                // ����
            }    
        }
        return ret;
    }
    
    /**
     * �f�[�^������s
     * @return �����^���s
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
        sumbyte += 20;  // ���s����ǉ�
        
        if( sumbyte > 249 ){
            // 2��ɕ����Ĉ󎚂���
            // 1-8
            // 9-20
            nTimes = 2;
        }
        /*
        if( sumbyte > 98 ){
            // 2��ɕ����Ĉ󎚂���
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
                        // �ԐM�d����M
                        RecvString recv = getResponse(getCmd(runcmd));

                        // �G���[�`�F�b�N
                        if( recv.IsError() ){
                            ret = false;
                            break;
                        }
                        
                        // �X�e�[�^�X�Z�b�g
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
                            // ����
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
                    // ����
                }    
            }
            if( ret == false ) break;
        }
        return ret;
    }
    
    /**
     * �J�[�h�r�o���s
     * @return �����^���s
     */
    public boolean Eject()
    {
        boolean ret = false;
        byte[] runcmd = getMessage(_CMD_EJECT);
        
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // �ԐM�d����M
                RecvString recv = getResponse(getCmd(runcmd));

                // �G���[�`�F�b�N
                if( recv.IsError() || recv.getRPS2() != _RPS2_COMPLETE){
                    ret = false;
                }else{
                    ret = true;
                }
                
                // �X�e�[�^�X�Z�b�g
                setLastError(recv.getCMD(), recv.getRPS1(), recv.getRPS2(), recv.getRPS3());
            }

            if( ret == true ) break;
            if( ++nCnt > 3 ){
                break;
            }
            
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){
                // ����
            }    
        }
        return ret;
    }
    
    /**
     * ����o�b�t�@���N���A
     * @return �����^���s
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
     * ����o�b�t�@�փf�[�^��ǉ�
     * @param Line ����s
     * @param Pos ����ʒu  ��A31�̂��̃o�[�W�����ł͖���
     * @param Width ���{�p(1-4)
     * @param Height �c�{�p(1-4)
     * @param data ����f�[�^
     * @return �����^���s
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
     * ����ʂ��N���A
     * @return �����^���s
     */
    public boolean Clear()
    {
        clearPrintBuffer();
        return Print();
    }
    
    /**
     * �N���[��
     * @return �����^���s
     */
    public boolean Clean()
    {
        boolean ret = false;
        byte[] runcmd = getMessage(_CMD_CLEAN);
        int nCnt = 0;
        
        while(true){
            ret = runCmd(runcmd);
            if( ret == true ){
                // �ԐM�d����M
                RecvString recv = getResponse(getCmd(runcmd));

                // �G���[�`�F�b�N
                if( recv.IsError() ){
                    ret = false;
                }

                // �J�[�h�}���҂��ɂȂ�����OK
                if( recv.getRPS2() == _RPS2_COMPLETE && recv.getRPS3() == _RPS3_CARD_WAIT ){
                    ret = true;
                    break;
                }else{
                    ret = false;
                }
                
                // �X�e�[�^�X�Z�b�g
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
                // ����
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
     * ��s�̍ő啶������Ԃ�
     *
     */
    @Override
    public int getMaxChars()
    {
        return 26;
    }
    
}

