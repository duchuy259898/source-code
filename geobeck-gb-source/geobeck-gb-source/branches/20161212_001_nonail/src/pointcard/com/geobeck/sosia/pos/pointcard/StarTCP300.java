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
 * Star�����������C�g�J�[�h���[�_�[���C�^�[����p�N���X
 * @author takeda
 */
public class StarTCP300 extends AbstractCardCommunication {
    
    // <editor-fold defaultstate="collapsed" desc="�ʐM�d���p�萔">                          
    // �R�}���h
    final private static byte _CMD_INITIAL     = (byte)0x5F;   // �������R�}���h
    final private static byte _CMD_READ        = (byte)0x26;   // ���[�h�R�}���h
    final private static byte _CMD_PARAM_READ  = (byte)0x28;   // ���[�h�R�}���h�i�p�����[�^�[�t���j
    final private static byte _CMD_SET_WDATA_R7 = (byte)0x36;   // ���C�g�g���b�N�f�[�^�ݒ�R�}���h
    final private static byte _CMD_SET_WDATA_7  = (byte)0x39;   // ���C�g�g���b�N�f�[�^�ݒ�R�}���h
    final private static byte _CMD_SET_WDATA    = (byte)0x3C;   // ���C�g�g���b�N�f�[�^�ݒ�R�}���h
    final private static byte _CMD_WRITE        = (byte)0x32;   // ���C�g�R�}���h
    
    final private static byte _CMD_CLR_BUF     = (byte)0x49;   // �v�����g�o�b�t�@�N���A
    final private static byte _CMD_SET_BUF     = (byte)0x41;   // �v�����g�o�b�t�@�ݒ�
    final private static byte _CMD_PRINT       = (byte)0x46;   // �v�����g�R�}���h(�����^�󎚁^�r�o)
    final private static byte _CMD_EJECT       = (byte)0x50;   // �J�[�h�r�o�R�}���h
    final private static byte _CMD_CANCEL      = (byte)0x54;   // �L�����Z���R�}���h
    final private static byte _CMD_CLEAN       = (byte)0x52;   // �N���[�j���O�R�}���h
    final private static byte _CMD_STAT        = (byte)0x59;   // �X�e�[�^�X�v���R�}���h
    
    // �X�e�[�^�X
    final private static byte _STAT_NORMAL     = (byte)0x20;   // ����
    final private static byte _STAT_NO_CARD    = (byte)0x22;   // �J�[�h�Ȃ�
    final private static byte _STAT_ERR_CARD   = (byte)0x23;   // �J�[�h�ُ�
    
    final private static byte _STAT_ERR_PARITY = (byte)0x31;   // �p���e�B�[�G���[
    final private static byte _STAT_ERR_MARK   = (byte)0x32;   // �J�n�����^�I�������Ȃ�
    final private static byte _STAT_ERR_LRC    = (byte)0x33;   // �k�q�b�G���[
    final private static byte _STAT_ERR_CHAR   = (byte)0x34;   // �ُ�L�����N�^
    final private static byte _STAT_ERR_WRITE  = (byte)0x37;   // �������݃G���[
    final private static byte _STAT_ERR_JAM    = (byte)0x38;   // �J�[�h�W����
    final private static byte _STAT_ERR_COVER  = (byte)0x40;   // �J�o�[�I�[�v��
    final private static byte _STAT_ERR_MOTOR  = (byte)0x42;   // �J�����[�^�ُ�
    final private static byte _STAT_ERR_TEMP   = (byte)0x43;   // �C���[�X�w�b�h�ُ�
    final private static byte _STAT_ERR_EEPROM = (byte)0x44;   // �d�d�o�q�n�l�G���[
    final private static byte _STAT_ERR_BUFFER = (byte)0x51;   // �d�d�o�q�n�l�G���[
    
        
    // �d���ʒu���
    final private static int _POS_STX  = 0;
    final private static int _POS_CMD  = 1;
    final private static int _POS_STAT = 2;
    final private static int _POS_DATA = 3;
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="�����o�ϐ�">                          
    // �擾�X�e�[�^�X���
    private Integer _sensor1 = 0;
    private Integer _sensor2 = 0;
    private Integer _sensor3 = 0;
    private Integer _sensor4 = 0;
    private Integer _cover   = 0;
    
    // ���C�ʃp�����[�^�[
    private boolean _setParam = false;
    private String _trac = "";
    private String _format = "";
    
    // �N���[�j���擾�p
    RecvString cleanRecv;
   
    // ����v�[���f�[�^
    ArrayList<PrintBuffer> printBuffer = new ArrayList<PrintBuffer>();
    // </editor-fold>                        
    
    /** Creates a new instance of StarTCP300 */
    public StarTCP300(SerialParameters para)
    {
        super(para);
    }

    // <editor-fold defaultstate="collapsed" desc="���M�d���쐬����">                          
    /**
     * �ʐM�d�����쐬����
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
        if( subcmd.size() >0 ){ // �Ō�J���}���폜
            veclsbyte.setSize(veclsbyte.size()-1);
        }
        veclsbyte.add(_ETX);
        
        // BCC�̌v�Z
        byte byteBCC = 0;
        byteBCC = 0;
        
        // CMD�`ETX�܂ł�BCC���v�Z
        for(int ii=_POS_CMD; ii< veclsbyte.size(); ii++){
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
        return cmd[_POS_CMD];
    }    
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="�ʐM����">                          
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
                }
            }
            
            // �^�C���A�E�g�������́A��M�d���G���[�Ȃ�΍đ��v��
            if( recvBuf < 0 || recv.IsError() == true ){
                byte[] reqmsg = new byte[1];
                reqmsg[0] = _NAK;
                Send(reqmsg);       // NAK���M
            }else{
                byte[] reqmsg = new byte[1];
                reqmsg[0] = _ACK;
                Send(reqmsg);       // ACK���M
                break;
            }
        }

        return recv;
    }
    
    /**
     * ���s���ʂ��擾����(�҂��Ȃ�)
     */
    private int getResponseNoWait(RecvString recv, byte cmd)
    {
        int recvBuf = -1;
        
        // ���ʂ���M
        _nTimeout = 100;
        for(int ii = 0; ii< _nRetry; ii++){
            // ���ʂ���M
            
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
                    // �����̃R�}���h�Ŗ�����΁A�����𑱂���
                    if( recv.getCMD() != cmd ) {
                        recv.init();
                        continue;
                    }
                    break;
                }
            }

            // ��M�d�������Ȃ�΁A�����𔲂���
            if( recv.IsComplete() == true ){
                // �����̃R�}���h�Ŗ�����΁A�����𑱂���
                if( recv.getCMD() != cmd ) {
                    recv.init();
                }else{
                    // ��M�d���G���[�Ȃ�΍đ��v��
                    if( recv.IsError() == true ){
                        byte[] reqmsg = new byte[1];
                        reqmsg[0] = _NAK;
                        Send(reqmsg);       // NAK���M
                    }else{
                        byte[] reqmsg = new byte[1];
                        reqmsg[0] = _ACK;
                        Send(reqmsg);       // ACK���M
                        break;
                    }
                }
            }
        }

        return recvBuf;
    }
    
    
    // </editor-fold>                        
    
    // <editor-fold defaultstate="collapsed" desc="��M����">                          
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
         * STAT�擾
         */
        private byte getSTAT(){
            if(_Complete) return _Buf.get(_POS_STAT);
            return 0;
        }
        
        /**
         * ��M�f�[�^�̃f�[�^�������擾����
         */
        private String getData()
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
                    _nStat = 1;     // �d���擾��
                    _Complete = false;
                    _Error = false;
                    _Buf.clear();
                    _Buf.add(buf);
                }
            }else if( _nStat == 1 ){
                // �d���擾
                _nBCC ^= buf;
                _Buf.add(buf);
                if( buf == _ETX ){
                    _nStat = 2;     // BCC�擾��
                }
            }else if( _nStat == 2 ){
                _Complete = true;
                if( _nBCC != buf ){
                    // BCC�G���[
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
    
    // <editor-fold defaultstate="collapsed" desc="�d���쐬�֘A">                          
    /**
     * �T�u���b�Z�[�W�Ȃ��̃��b�Z�[�W���쐬
     */
    private byte[] getMessage(byte cmd)
    {
        Vector<byte[]> subcmd = new Vector<byte[]>();
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * �J�[�h�������݃f�[�^�ݒ�d�����擾
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
     * �J�[�h�������݃f�[�^�ݒ�d�����擾
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
     * �J�[�h�������݃f�[�^�ݒ�d�����擾
     */
    private byte[] getWriteMessage()
    {
        byte cmd = (byte)_CMD_WRITE;
        Vector<byte[]> subcmd = new Vector<byte[]>();
        subcmd.add("2".getBytes());
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * �J�[�h�r�o�d�����擾
     */
    private byte[] getEjectMessage()
    {
        byte cmd = (Byte)_CMD_EJECT;
        Vector<byte[]> subcmd = new Vector<byte[]>();
        subcmd.add("1".getBytes());
        return VecToCommArray(makeCommString(cmd, subcmd));
    }
    
    /**
     * �v�����g�o�b�t�@�N���X
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
         * ����o�b�t�@�ݒ�d�����擾
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
     * �v�����g�d�����擾
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

    // <editor-fold defaultstate="collapsed" desc="�G���[�֘A����">                          
    private void setLastError(byte Code)
    {
        _LastError = getErrorCode(Code);
    }

    private int getErrorCode(byte Code)
    {
        int ret;
        switch(Code){
        case _STAT_NORMAL:          // ����
            ret = STAT_NORMAL;
            break;
        case _STAT_NO_CARD:         // �J�[�h�Ȃ�
            ret = STAT_NON_CARD;
            break;
        case _STAT_ERR_CARD:        // �J�[�h�ُ�
            ret = STAT_NON_STRIPE;
            break;
        case _STAT_ERR_PARITY:      // �p���e�B�[�G���[
            ret = STAT_ERR_PARITY;       // ���[�^�[�ُ�
            break;
        case _STAT_ERR_MARK:        // �J�n�����^�I�������Ȃ�
            ret = STAT_ERR_NOMARK;       // �J�n�I�������Ȃ�
            break;
        case _STAT_ERR_LRC:         // �k�q�b�G���[
            ret = STAT_ERR_LRC;          // �k�q�b�G���[
            break;
        case _STAT_ERR_CHAR:        // �ُ�L�����N�^
            ret = STAT_ERR_CHAR;        // �ُ�L�����N�^�[
            break;
        case _STAT_ERR_WRITE:       // �������݃G���[
            ret = STAT_ERR_WRITE;        // ��������
            break;
        case _STAT_ERR_JAM:         // �J�[�h�W����
            ret = STAT_ERR_JAM;          // �J�[�h�W����
            break;
        case _STAT_ERR_COVER:       // �J�o�[�I�[�v��
            ret = STAT_ERR_OPEN;         // �J�o�[�I�[�v��
            break;
        case _STAT_ERR_MOTOR:       // �J�����[�^�ُ�
            ret = STAT_ERR_MOTOR;        // ���[�^�[�ُ�
            break;
        case _STAT_ERR_TEMP:        // �C���[�X�w�b�h�ُ�
            ret = STAT_ERR_HEAD;         // �w�b�h�ُ�
            break;
        case _STAT_ERR_EEPROM:      // �d�d�o�q�n�l�G���[
            ret = STAT_ERR_EEPROM;       // EEPROM�ُ�
            break;
        case _STAT_ERR_BUFFER:      // �W�J�o�b�t�@�I�[�o�[�t���[
            ret = _STAT_ERR_BUFFER;
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
    
    /**
     * �������R�}���h���s
     */
    public boolean Initialize()
    {
        byte[] runcmd = getMessage(_CMD_INITIAL);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 3000;
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }

    /**
     * �X�e�[�^�[�X���擾
     */
    public boolean Status()
    {
        byte[] runcmd = getMessage(_CMD_STAT);
        boolean ret = false;
        _nTimeout = 1000;
        ret = runCmd(runcmd);
        if( ret == true ){
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( recv.IsError() ){
                ret = false;
            }else{
                // �e�Z���T�[��Ԃ��L��
                String str = recv.getData();
                _sensor1 = Integer.valueOf(str.substring(0,1));
                _sensor2 = Integer.valueOf(str.substring(1,2));
                _sensor3 = Integer.valueOf(str.substring(2,3));
                _sensor4 = Integer.valueOf(str.substring(3,4));
                _cover   = Integer.valueOf(str.substring(4,5));
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * ���C�ʓǂݍ���
     */
    public boolean Read()                        
    {
        _trac = "";
        _format = "";
        _setParam = false;
        
        // ���̋@��ł́A�����ł͂Ȃɂ��������s�킸�A�f�[�^���o�����ōs��
        return true;
    }
    
    /**
     * �p�����[�^�t�����[�h
     */
    public boolean Read(Integer tracParam, Integer tracFormat ){
        //_trac = String.valueOf(tracParam);
        _trac = "2";    // TCP300�ł́A"2"�̂ݑΉ�
        _format = String.valueOf(tracFormat);
        _setParam = true;
        return true;
    }
    
    /**
     * �ǂݍ��݃f�[�^�擾
     */
    public int getReadData(StringBuffer RecvData) 
    {
        int ret = -1;   // �G���[
        byte[] runcmd;
        runcmd = getReadMessage();
        _nTimeout = 1000;
        
        if( runCmd(runcmd) ){
            _nTimeout = 6000;
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( !recv.IsError() ){
                if( recv.getSTAT() == _STAT_NORMAL ){
                    // �f�[�^�擾
                    RecvData.append(recv.getData());
                    ret = RecvData.length();
                }
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * �J�[�h�}���҂��̃L�����Z��
     */
    public boolean Cancel()                     
    {
        byte[] runcmd = getMessage(_CMD_CANCEL);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 3000;
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * �������݃f�[�^��ݒ�
     */
    private boolean setWriteData( String data )
    {
        byte[] runcmd = getSetWriteDataMessage(data);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
        
            // �G���[�`�F�b�N
            if( recv.getSTAT() == _STAT_NORMAL ){
                ret = true;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        
        return ret;
    }
    
    /**
     * ���C�ʏ�������
     */
    public boolean Write(String data)           
    {
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = setWriteData(data);
        if( ret == true ){
            _nTimeout = 1000;
            // �������ݎ��s
            byte[] runcmd = getWriteMessage();
            ret = runCmd(runcmd);
            if( ret == true ){
                // �ԐM�d����M
                RecvString recv = getResponse(getCmd(runcmd));

                // �G���[�`�F�b�N
                if( recv.getSTAT() == _STAT_NORMAL ){
                    ret = true;
                }
                // �X�e�[�^�X�Z�b�g
                setLastError(recv.getSTAT());
            }
        }
        return ret;
    }
    
    /**
     * �J�[�h�r�o
     */
    public boolean Eject()                       
    {
        
        byte[] runcmd = getEjectMessage();
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 2000;
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * ����ʃv�����g
     */
    public boolean Print()
    {
        // �v�����g�o�b�t�@�N���A�v��
        if( !sendClearPrintBuffer() ){
            return false;
        }
        
        // �v�����g�o�b�t�@�𑗐M
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
     * ����J�n�𑗐M
     */
    private boolean sendPrint(){
        byte[] runcmd = getPrintMessage();
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 10000;
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * �v�����g�o�b�t�@�Ƀf�[�^��ǉ�����
     */
    public boolean clearPrintBuffer()
    {
        printBuffer.clear();
        return true;
    }
    
    /*
     * ����p�o�b�t�@���N���A
     */
    public boolean sendClearPrintBuffer()
    {
        byte[] runcmd = getMessage(_CMD_CLR_BUF);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    /**
     * �v�����g�o�b�t�@�Ƀf�[�^��ǉ�����
     */
    public boolean addPrintBuffer(int Line, int Pos, int Width, int Height, String Data)
    {
        printBuffer.add(new PrintBuffer(Line, Pos, Width, Height, Data));
        return true;
    }
    
    /**
     * �v�����g�f�[�^�𑗐M����
     */
    private boolean sendAddPrintBuffer(PrintBuffer buf)
    {
        byte[] runcmd = buf.getMessage();
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        if( ret == true ){
            _nTimeout = 3000;
            // �ԐM�d����M
            RecvString recv = getResponse(getCmd(runcmd));
            // �G���[�`�F�b�N
            if( recv.IsError() || recv.getSTAT() != _STAT_NORMAL){
                ret = false;
            }
            // �X�e�[�^�X�Z�b�g
            setLastError(recv.getSTAT());
        }
        return ret;
    }
    
    
    /**
     * ����ʂ��N���A
     */
    public boolean Clear()
    {
        clearPrintBuffer();
        addPrintBuffer(1, 1, 1, "");
        return Print();
    }
    
    /**
     * �N���[��
     */
    public boolean Clean()
    {
        byte[] runcmd = getMessage(_CMD_CLEAN);
        boolean ret = false;
        _nTimeout = 1000;
        
        ret = runCmd(runcmd);
        
        if( ret == true ){
            // �I���d����M�p�ϐ���������
            cleanRecv = new RecvString();
            cleanRecv.init();
        }
        return ret;
    }
    
    /**
     * �ǂݍ��݃f�[�^�擾
     */
    public boolean isCleannig() 
    {
        boolean ret = true;
        
        // �ԐM�d����M
        if( getResponseNoWait(cleanRecv, _CMD_CLEAN) >= 0 ){
            // ��M�d������
            if( cleanRecv.IsComplete() == true ){
                ret = false;
                
                // �X�e�[�^�X�Z�b�g
                setLastError(cleanRecv.getSTAT());
            }
        }
        
        return ret;
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
}
