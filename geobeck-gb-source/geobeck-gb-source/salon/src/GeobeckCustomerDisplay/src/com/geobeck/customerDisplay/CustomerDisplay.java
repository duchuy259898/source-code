/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.customerDisplay;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.comm.*;
import java.util.*;
import java.io.*;

/** よくある操作を行う為のコンビニエンスメソッド付き
 *  カスタマディスプレイ制御オブジェクトのクラス. 
 * @author onishi
 */
public class CustomerDisplay{
    
    public static final int CONSOLE_COLS         = 20;
    public static final int CONSOLE_ROWS         = 2;
    public static final byte[] CMD_INIT          = new byte[]{ 0x1B, 0x40 };
    public static final byte[] CMD_JP            = new byte[]{ 0x1B, 0x52, 0x08 };
    public static final byte[] CMD_CUR_OFF       = new byte[]{ 0x1F, 0x43, 0x00 };
    public static final byte[] CMD_MD1           = new byte[]{ 0x1F, 0x01 };
    public static final byte[] CMD_CLR           = new byte[]{ 0x0C };
    public static final byte[] CMD_KANA          = new byte[]{ 0x1B, 0x74, 0x01 };
    public static final byte[] CMD_HOME          = new byte[]{ 0x0B };
    public static final byte[] CMD_PREVIOUS_LINE = new byte[]{ 0x1F, 0x0A };
    public static final byte[] CMD_NEXT_LINE     = new byte[]{ 0x0A };
    public static final byte[] CMD_FORWARD_CHAR  = new byte[]{ 0x09 };
    public static final byte[] CMD_BACKWARD_CHAR = new byte[]{ 0x08 };
    public static final byte[] CMD_LOCATE        = new byte[]{ 0x1F, 0x24 };
    public static final byte[] CMD_CLR_LN        = new byte[]{ 0x18 };
    public static final byte[][] CMD_BRIGHTNESS  = new byte[][]{{ 0x1f, 0x58, 0x01 },
                                                                { 0x1f, 0x58, 0x02 },
                                                                { 0x1f, 0x58, 0x03 },
                                                                { 0x1f, 0x58, 0x04 }};

    private String       name = null;
    private OutputStream  out = null;
    private CommPort     port = null;
    private String   portName = null;
    private int       timeout = 30000;
    private boolean  disabled = false;
    static private HashMap<String,CustomerDisplay> instances = new HashMap<String,CustomerDisplay>();

    private CustomerDisplay(String commPortName){
        this.setPortName(commPortName);
    }
    
    /** 指定されたポートに接続されているカスタマディスプレイオブジェクトを返す
     */
    public static CustomerDisplay getInstance(String commPortName){
        if(!instances.containsKey(commPortName)){
            CustomerDisplay cd = new CustomerDisplay(commPortName);
            instances.put(commPortName, cd);
        }
        return instances.get(commPortName);
    }
    

    /**
     */
    public void setPortName(String name){ this.portName = name; }

    /**
     */
    public OutputStream getOutputStream() throws PortInUseException, IOException{
        if(this.out == null){
            if(this.portName == null) throw new IllegalStateException();
            try{
                CommPortIdentifier idt =
                        CommPortIdentifier.getPortIdentifier(this.portName);
                this.port = idt.open(this.name, this.timeout);
                this.out = this.port.getOutputStream();
                this.putBytes(CMD_INIT);
                this.putBytes(CMD_JP);
                this.putBytes(CMD_KANA);
                this.putBytes(CMD_CUR_OFF);
                this.putBytes(CMD_MD1);
                this.clearScreen();                
            }catch(NoSuchPortException e){
                this.disabled = true;
            }
        }
        return this.out;
    }
    
    /** バイト配列をカスタマディスプレイに送る
     */
    public void putBytes(byte[] bs) throws PortInUseException, IOException{
        if(this.disabled) return;
        OutputStream o = this.getOutputStream();
        if(this.disabled) return;
        o.write(bs);
        o.flush();
    }
    
    /** 与えられた位置に文字列をセットする
     * @param s     表示すべき文字列
     * @param align 'l', 'c', 'r', で現わされる, 左寄せ, 中央揃え, 右寄せの指定
     * @param row   表示するロー
     */
    public void putStr(String s, char align, int row) throws PortInUseException, IOException{
        byte[] bytes = convertToBytes(s);
        int col = 0;
        switch(align)
        {
            case 'r':
                col = CONSOLE_COLS - bytes.length;
                break;
            case 'c':
                col = ( CONSOLE_COLS - bytes.length ) / 2;
                break;
            case 'l':
                break;
            default:
                throw new IllegalArgumentException("Unknown align name:'"+Character.toString(align)+"'.");
        }

        this.moveTo(col, row);
        this.putBytes(bytes);
    }
    
    public void clearLine(int row) throws PortInUseException, IOException{
        this.moveTo(0,row);
        this.putBytes(CMD_CLR_LN);
    }
    
    /** 与えられた位置にカーソルを移動する. 
     */
    public void moveTo(int x, int y) throws PortInUseException, IOException{
        this.putBytes(CMD_LOCATE);
        if(x >= CONSOLE_COLS || y >= CONSOLE_ROWS) throw new IllegalArgumentException();
        byte[] pos = new byte[2];
        pos[0] = (byte)(x + 1);
        pos[1] = (byte)(y + 1);
        this.putBytes(pos);
    }

    /** 画面を消去する. 
     */
    public void clearScreen() throws PortInUseException, IOException{
        this.putBytes(CMD_CLR);
    }
    
    @Override
    protected void finalize() {
        if(this.port != null) this.port.close();
    }
    
    static private byte[] convertToBytes(String s){
        try {
            return s.getBytes("SJIS");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CustomerDisplay.class.getName()).log(Level.SEVERE, null, ex);
            return new byte[]{};
        }
    }
}
