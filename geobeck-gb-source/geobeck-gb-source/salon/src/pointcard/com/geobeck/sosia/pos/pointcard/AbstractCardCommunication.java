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
 * �����C�g�J�[�h���[�_���C�^�[�p���N���X
 * @author takeda
 */
//abstract public class AbstractCardCommunication implements SerialPortEventListener{
abstract public class AbstractCardCommunication{
    
    final public static int STAT_NORMAL       = 0;        // ����
    final public static int STAT_NON_CARD     = 11;       // �J�[�h�Ȃ�
    final public static int STAT_NON_STRIPE   = 12;       // �f�[�^�X�g���C�v�Ȃ�
    final public static int STAT_CARD_REMAIN  = 13;       // �}�����J�[�h�c��
    
    // �G���[
    final public static int STAT_ERR_PARITY   = 51;       // ���[�^�[�ُ�
    final public static int STAT_ERR_NOMARK   = 52;       // �J�n�I�������Ȃ�
    final public static int STAT_ERR_LRC      = 53;       // �k�q�b�G���[
    final public static int STAT_ERR_CHAR     = 54;       // �ُ�L�����N�^�[
    final public static int STAT_ERR_WRITE    = 55;       // ��������
    final public static int STAT_ERR_JAM      = 56;       // �J�[�h�W����
    final public static int STAT_ERR_OPEN     = 57;       // �J�o�[�I�[�v��
    final public static int STAT_ERR_READ     = 58;       // �ǂݍ���
    
    // �ً}�G���[
    final public static int STAT_ERR_MOTOR    = 91;       // ���[�^�[�ُ�
    final public static int STAT_ERR_HEAD     = 92;       // �w�b�h�ُ�
    final public static int STAT_ERR_EEPROM   = 93;       // EEPROM�ُ�
    final public static int STAT_ERR_PRINT    = 94;       // �v�����g
    
    // ���̑��G���[
    final public static int STAT_ERR_BUFFER   = 101;      // �W�J�o�b�t�@�I�[�o�[�t���[
    
    
    final public static int STAT_ERR_OTHERS   = 255;      // �s���ȃG���[
    
    // �d������
    final protected static byte _STX = (byte)0x02;       // �e�L�X�g�J�n
    final protected static byte _ETX = (byte)0x03;       // �e�L�X�g�I��
    final protected static byte _ENQ = (byte)0x05;       // �����v��
    final protected static byte _ACK = (byte)0x06;       // ���퉞��
    final protected static byte _NAK = (byte)0x15;       // �ُ퉞��
    final protected static byte _DLE = (byte)0x10;       // ���ۉ���
    final protected static byte _LF  = (byte)0x0A;       // ���s
    final protected static byte _ESC = (byte)0x1B;       // �G�X�P�[�v�V�[�P���X
    
    private SerialPort      _serialPort;
    private InputStream     _in;
    private OutputStream    _out;
    private int             _mode;
    protected int          _nTimeout;       // �^�C���A�E�g
    protected int          _nRetry;         // ���g���C��
    
    private boolean        _bOpen = false;
    
    // ��M�d���o�b�t�@
    Vector<Byte> _vecRecvBuffer = new Vector<Byte>();
    
    // �|�[�g�ݒ�
    private SerialParameters	_para;
    protected int _LastError = 0;
    
    
    /**
     * �R���X�g���N�^
     * @param para �ʐM�p�����[�^
     */
    public AbstractCardCommunication(SerialParameters para)
    {
        this._para = para;
        _nTimeout = 1200;
        _nRetry   = 3;
        _bOpen = false;
    }
    
    /*
     * Vector�z���d���p�z��ɕϊ�����
     * �iVector<Byte> => byte[]�ɕϊ�����j
     */
    /**
     * Byte�^��Vector�z���byte�z��ɕϊ�����
     * @param vecArray Vector������
     * @return byte�z��
     */
    protected byte[] VecToCommArray(Vector<Byte> vecArray)
    {
        byte[] retBuf;
        
        // byte[]�֕ϊ�
        retBuf = new byte[vecArray.size()];
        for(int ii=0; ii<vecArray.size(); ii++){
            retBuf[ii] = vecArray.get(ii);
        }
        
        return retBuf;
    }

    /**
     * �|�[�g�̃I�[�v������
     * @return �����^���s
     */
    public boolean Open( )
    {
        boolean ret = false;
        try{

            System.out.println("�|�C���g�J�[�h�F�|�[�g�I�[�v���F�|�[�g���F" + _para.getPortName());
            
            CommPortIdentifier port = CommPortIdentifier.getPortIdentifier(_para.getPortName());
            _serialPort = (SerialPort)port.open("CardReaderWriter", 30000);
            // �V���A���|�[�g�̃p�����[�^��ݒ�
            _serialPort.setSerialPortParams(_para.getBaudRate(),
                            _para.getDatabits(), _para.getStopbits(), _para.getParity());
            // Flow Control Mode
            _serialPort.setFlowControlMode(_para.getFlowControlIn() | 
                                              _para.getFlowControlOut());
            
            _serialPort.enableReceiveTimeout(100);
            
            // ���̓X�g���[�����擾
            _in = _serialPort.getInputStream();
            // �o�̓X�g���[�����擾
            _out = _serialPort.getOutputStream();
            
            /*
            // �C�x���g���X�i�[�o�^
            _serialPort.addEventListener(this);
            // �V���A���|�[�g���f�[�^����M�����ۂɋ����Ă˂Ɛݒ�
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
     * �|�[�g�N���[�Y
     * @return �����^���s
     */
    public boolean Close()
    {
        boolean ret = false;
        if( !_bOpen ) return true;
        try {
            System.out.println("�|�C���g�J�[�h�F�|�[�g�N���[�Y");
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
     * ��M�o�b�t�@���N���A����
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
    
    // ++++ �|�[�����O�����ɕύX�������ߕs�v 
    /**
     * �V���A���C�x���g����
     * �f�[�^����M�o�b�t�@�Ƀv�[������
     * @param arg0 
     * /
    public void serialEvent(SerialPortEvent arg0) {
        if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            int newData = 0;
    		while (newData != -1){
                try {
                    newData = _in.read();// ���̓X�g���[������ǂݍ���
                    
                    // ��M������΃o�b�t�@�ɒǉ����Ă���
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
     * �f�[�^���M
     * @param msg ���M�f�[�^
     * @return �����^���s
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
                // �o�̓X�g���[���Ƀf�[�^���������݁A�t���b�V��
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
     * �f�[�^��M
     * �f�[�^���P�o�C�g��M
     * @return -1:�^�C���A�E�g
     * �ȊO:��M�o�C�g
     */
    protected int Recv()
    {
        int bufLen, waitTime = 0;
        int byteData = -1;
        final int C_WAIT = 100;
        
        // �^�C���A�E�g�܂ŁA��M���Ď�����
        while( true ){
            try {
                // ���̓X�g���[������ǂݍ���
                int newData = _in.read();// ���̓X�g���[������ǂݍ���
                
                if( newData >= 0 ){
                    // ��M�f�[�^���o�b�t�@������o��
                    byteData = newData;
                    break;
                }
                
                if( waitTime > _nTimeout ){
                    // �^�C���A�E�g
                    break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try{
                waitTime += C_WAIT;
                Thread.sleep(C_WAIT);  // 100ms�҂�
            }catch( InterruptedException ex){
                System.err.println(ex);
            }            
            
            /*
            try{
                synchronized(_vecRecvBuffer){
                     bufLen = _vecRecvBuffer.size();
                }
                if( waitTime > _nTimeout ){
                    // �^�C���A�E�g
                    break;
                }
                if( bufLen == 0 ){
                    waitTime += C_WAIT;
                    Thread.sleep(C_WAIT);  // 100ms�҂�
                }else{
                    // ��M�f�[�^���o�b�t�@������o��
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
     * ���p���l��S�p���l�ɕϊ�����
     * @param num ���p����������
     * @return �S�p����������
     */
    public static String NumberToWideNumber(String num) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            if (c >= '0' && c <= '9') {
                buf.append((char) (c - '0' + '�O'));
            }else if (c == ',') {
                buf.append((char) ('�C'));
            }else if (c == '-') {
                buf.append((char) ('�|'));
            }else if (c == '+') {
                buf.append((char) ('�{'));
            }else{
                buf.append((char) (c));
            }
        }
        return buf.toString();
    }    
    
    // �@�\�֐�
    /**
     * ���������G���[���擾����
     */
    abstract public int getLastError();
    
    /**
     * ����������
     * @return �����^���s
     */
    abstract public boolean Initialize();
    
    /**
     * ��Ԏ擾
     * @return �����^���s
     */
    abstract public boolean Status();
    
    /**
     * ���C�ʓǂݍ���
     * @return �����^���s
     */
    abstract public boolean Read();
    abstract public boolean Read(Integer tracParam, Integer formatParam);
    /**
     * �ǂݍ��݃f�[�^�擾
     * @param data 
     * @return �����^���s
     */
    abstract public int getReadData(StringBuffer data);
    /**
     * �J�[�h�}���҂��̃L�����Z��
     * @return �����^���s
     */
    abstract public boolean Cancel();
    /**
     * ���C�ʏ�������
     * @param data 
     * @return �����^���s
     */
    abstract public boolean Write(String data);
    /**
     * �J�[�h�r�o
     * @return �����^���s
     */
    abstract public boolean Eject();
    /**
     * ����ʃv�����g
     * @return �����^���s
     */
    abstract public boolean Print();
    /**
     * ����ʃN���A
     * @return �����^���s
     */
    abstract public boolean Clear();
    /**
     * �N���[��
     * @return �����^���s
     */
    abstract public boolean Clean();

    // ������e�ݒ�
    /**
     * ����o�b�t�@���N���A
     * @return �����^���s
     */
    abstract public boolean clearPrintBuffer();
    
    /**
     * ����o�b�t�@�փf�[�^��ǉ�
     * @param Line ����s
     * @param Pos ����ʒu  ��A31�̂��̃o�[�W�����ł͖���
     * @param Width ���{�p(1-4)
     * @param Height �c�{�p(1-4)
     * @param data ����f�[�^
     * @return �����^���s
     */
    abstract public boolean addPrintBuffer(int Line, int pos, int Width, int Height, String data);
     
    /**
     * ����o�b�t�@�փf�[�^��ǉ�
     * @param Line ����s
     * @param Width ���{�p(1-4)
     * @param Height �c�{�p(1-4)
     * @param data ����f�[�^
     * @return �����^���s
     */
    public boolean addPrintBuffer(int Line, int Width, int Height, String data)
    {
        return addPrintBuffer(Line, 1, Width, Height, data);
    }

    /**
     * ����o�b�t�@�փf�[�^��ǉ�
     * @param Line ����s
     * @param data ����f�[�^
     * @return �����^���s
     */
    public boolean addPrintBuffer(int Line, String data)
    {
        return addPrintBuffer(Line, 1, 1, 1, data);
    }
    
    /**
     * ����o�b�t�@�փf�[�^��ǉ�
     * @param Line ����s
     * @param Pos ����ʒu  ��A31�̂��̃o�[�W�����ł͖���
     * @param data ����f�[�^
     * @return �����^���s
     */
    public boolean addPrintBuffer(int Line, int Pos, String data)
    {
        return addPrintBuffer(Line, Pos, 1, 1, data);
    }
    
    /**
     * ��s�̍ő啶������Ԃ�
     *
     */
    public int getMaxChars()
    {
        return 24;
    }
    
    /**
     * �N���[�j���O���s�����H
     * @return true ���s��
     */
    abstract public boolean isCleannig();
    
    
    /**
     * �����G���[���b�Z�[�W���擾�i�e�@�틤�ʂƏo����H�j
     */
    public String getLastMessage()
    {
        String ret = "";
        switch( _LastError ) 
        {
        // �y���ȃG���[
        case STAT_NON_CARD:
            ret = "�J�[�h�Ȃ�";
            break;
        case STAT_NON_STRIPE:
            ret = "���C�ʂ��ǂݎ��܂���B";
            break;
        case STAT_CARD_REMAIN:
            ret = "�}�����ɃJ�[�h���c���Ă��܂��B";
            break;
           
            
        // �x���G���[�n
        case STAT_ERR_PARITY:
            ret = "�p���e�B�G���[�ł��B���u�̃N���[�j���O���s���Ă��������B";
            break;
        case STAT_ERR_NOMARK:
            ret = "�J�n�����^�I�������Ȃ��G���[�ł��B���u�̃N���[�j���O���s���Ă��������B";
            break;
        case STAT_ERR_LRC:
            ret = "�k�q�b�G���[�ł��B���u�̃N���[�j���O���s���Ă��������B";
            break;
        case STAT_ERR_CHAR:
            ret = "�ُ�L�����N�^�ł��B���u�̃N���[�j���O���s���Ă��������B";
            break;
        case STAT_ERR_WRITE:
            ret = "�������݂Ɏ��s���܂����B���u�̃N���[�j���O���s���Ă��������B";
            break;
        case STAT_ERR_JAM:
            ret = "�J�[�h�l�܂�ł��B���u���̃J�[�h����菜���Ă��������B";
            break;
        case STAT_ERR_OPEN:
            ret = "�J�o�[���J���Ă��܂��B";
            break;
        case STAT_ERR_READ:
            ret = "�ǂݍ��݂Ɏ��s���܂����B���u�̃N���[�j���O���s���Ă��������B";
            break;
            
        // �ً}�G���[�n
        case STAT_ERR_MOTOR:
            ret = "�J�����[�^�[�ُ�ł��B�T�[�r�X�}���ɘA�����Ă��������B";
            break;
        case STAT_ERR_HEAD:
            ret = "�C���[�X�w�b�h���x�ُ�ł��B�T�[�r�X�}���ɘA�����Ă��������B";
            break;
        case STAT_ERR_EEPROM:
            ret = "�d�d�o�q�n�l�ُ�ł��B�T�[�r�X�}���ɘA�����Ă��������B";
            break;
        case STAT_ERR_BUFFER:
            ret = "�W�J�o�b�t�@�I�[�o�[�t���[�ł��B";
            break;
        case STAT_ERR_PRINT:
            ret = "�v�����^�G���[�ł��B�T�[�r�X�}���ɘA�����Ă��������B";
            break;
        default:
            ret = "�s���ȃG���[�ł��B";
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
