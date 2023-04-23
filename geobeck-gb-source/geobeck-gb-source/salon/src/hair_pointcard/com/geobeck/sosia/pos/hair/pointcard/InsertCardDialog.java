/*
 * InsertCardDialog.java
 *
 * Created on 2008/09/11, 22:32
 */

package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.CheckUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.pointcard.AbstractCardCommunication;

/**
 *
 * @author  takeda
 */
public class InsertCardDialog extends javax.swing.JDialog implements ActionListener {
    
    private Timer _timer;
    private AbstractCardCommunication _com;
    private String  _RecvCardData = "";
    private boolean     _canceled   = true;
    private boolean     _write = true;
    private String      _customer_id = "";
    private int         _mode;
    
    // ERROR_ID:
    private final static int MACHINE_STATE_ERROR = 20001;   // 接続エラー
    
    // コマンド
    public final static int MODE_READ  = 0;         // 読み込み
    public final static int MODE_PRINT = 1;         // プリント
    public final static int MODE_CLEAR = 2;         // クリア
    public final static int MODE_CLEAN = 3;         // クリーン
    
    // 実行結果
    public final static int STAT_ERROR      = -1;   // エラー
    public final static int STAT_SUCCESS    =  0;   // 成功
    public final static int STAT_CANCELED   =  1;   // キャンセル
    
    private static boolean beforeCheck() {
        if( SystemInfo.getPointcardConnection() == null ) {
            return false;
        }
        return true;
    }
    
    /**
     * 読込用カード挿入待ちダイアログを表示します
     * @return 内部顧客ID
     * @param parent 親フレーム
     */
    static public Integer ShowReadDialog(java.awt.Frame parent){
        if( !beforeCheck() ) return null;
        InsertCardDialog dlg = new InsertCardDialog(parent, true);
        Integer customer_id = dlg.startRead();
        dlg.dispose();
        return customer_id;
    }
    
    static public Integer ShowReadDialog(JDialog parent){
        if( !beforeCheck() ) return null;
        InsertCardDialog dlg = new InsertCardDialog(parent, true);
        Integer customer_id = dlg.startRead();
        dlg.dispose();
        return customer_id;
    }
    
    private Integer startRead() {
        Integer customer_id = null;
        
        SystemInfo.getLogger().log(Level.INFO, "カード挿入待ち");
        _mode = MODE_READ;
        _com.setMode(_mode);
        if( RunCommand() == 0 ){
            try {
                if( CheckUtil.isNumber(_RecvCardData) ){
                    customer_id = Integer.valueOf(_RecvCardData);
                    
                    MstCustomer customer = new MstCustomer(customer_id);
                    try {
                        customer.load(SystemInfo.getConnection());
                        if( customer.getCustomerNo() == null ){
                            // 顧客情報なし
                            MessageDialog.showMessageDialog( this,
                                    "条件に合う顧客がいません。", JOptionPane.ERROR_MESSAGE );
                        }
                    } catch (SQLException ex) {
                        customer_id = null;
                        ex.printStackTrace();
                    }
                    
                }else{
                    MessageDialog.showMessageDialog( this,
                            "カードの情報が不正です。", JOptionPane.ERROR_MESSAGE );
                    
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return customer_id;
    }
    
    /**
     * カード挿入待ちダイアログを表示します
     * @return 0:成功、-1:キャンセル、以外:エラー
     * @param parent 親フレーム
     * @param mode 実行モード 0:カード読み込み、1:カード印刷、2:カードクリア
     * @param customer_id 内部顧客ID
     */
    static public int ShowDialog(java.awt.Frame parent, int mode, String customer_id){
        if( !beforeCheck() ) return -1;
        
        InsertCardDialog dlg = new InsertCardDialog(parent, true);
        
        SystemInfo.getLogger().log(Level.INFO, "カード挿入待ち");
        return dlg.startWait(mode, customer_id);
    }
    
    static public int ShowDialog(JDialog parent, int mode, String customer_id){
        if( !beforeCheck() ) return -1;
        
        InsertCardDialog dlg = new InsertCardDialog(parent, true);
        return dlg.startWait(mode, customer_id);
    }
    
    private int startWait( int mode, String customer_id ) {
        _write = true;
        _mode  = mode;
        _com.setMode(_mode);
        _customer_id = customer_id;
        return RunCommand();
    }
    
    /**
     * カード挿入待ちダイアログを表示します
     * @param parent 親フレーム
     * @param mode   実行モード 0:カード読み込み、1:カード印刷、2:カードクリア
     * @return 0:成功、-1:キャンセル、以外:エラー
     */
    static public int ShowDialog(java.awt.Frame parent, int mode){
        return ShowDialog(parent, mode, "");
    }
    
    static public int ShowDialog(JDialog parent, int mode){
        return ShowDialog(parent, mode, "");
    }
    
    /**
     * コマンド実行
     * @return 0:成功、1:キャンセル、以外:エラー
     */
    private int RunCommand() {
        boolean ret = false;
        // ステータスを確認
        if( !SystemInfo.getPointcardConnection().Status() ){
            this.displayMessage( MACHINE_STATE_ERROR );
            return STAT_ERROR;
        }
        
        if( !StartTimer() ) return STAT_ERROR;
        
        this.setVisible(true);
        if( this._canceled )   return STAT_CANCELED;
        
        // 挿入を確認してから実行するコマンド
        switch(this._mode){
            case MODE_PRINT:     // カード印刷
                ret = _com.Print();
                if(!ret) break;
                if(_write){
                    ret = _com.Write(_customer_id);
                }
                break;
            case MODE_CLEAR:     // カードクリア
                ret = _com.Clear();
                if(!ret) break;
                if(_write){
                    ret = _com.Write("");
                }
                break;
            case MODE_CLEAN:     // カードクリーン
                break;
            case MODE_READ:     // カード印刷
                ret = true;
                break;
            default:
                break;
        }
        if( ret == false ){
            if( _com.getLastError() != _com.STAT_NORMAL ){
                // エラーメッセージを表示
                MessageDialog.showMessageDialog( this,
                        _com.getLastMessage(),
                        JOptionPane.ERROR_MESSAGE );
            }            
            _com.Eject();
            return STAT_ERROR;
        }
        
        _com.Eject();
        return STAT_SUCCESS;
    }
    
    /**
     * Creates new form InsertCardDialog
     * @param parent
     * @param modal
     */
    private InsertCardDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        init();
    }
    
    /**
     * Creates new form InsertCardDialog
     * @param parent
     * @param modal
     */
    private InsertCardDialog(JDialog parent, boolean modal) {
        super(parent, modal);
        addMouseCursorChange();
        init();
    }
    
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnClose);
    }
    
    private void init() {
        initComponents();
        SwingUtil.moveCenter(this);
        
        _com = SystemInfo.getPointcardConnection();
    }
    
    /**
     * 読み込んだデータを受け取る
     * @return 受信データ
     */
    public String getRecvData() {
        return _RecvCardData;
    }
    
    /**
     * 読み込み完了を確認する or 読み込みデータ受信要求を発行する
     * @param e
     */
    public void actionPerformed(ActionEvent e){
        
        if( _mode == MODE_CLEAN ){
            if( !_com.isCleannig() ){
                _timer.stop();
                _canceled = false;
                
                if( _com.getLastError() != _com.STAT_NORMAL ){
                    // エラーメッセージを表示
                    MessageDialog.showMessageDialog( this,
                            _com.getLastMessage(),
                            JOptionPane.ERROR_MESSAGE );
                    Cancel();
                }
                this.setVisible(false);
            }
        }else{
            StringBuffer RecvStr = new StringBuffer();
            if( _com.getReadData(RecvStr) >= 0 ){
                _RecvCardData = RecvStr.toString();
                _timer.stop();
                _canceled = false;
                this.setVisible(false);
                if( _customer_id.equals(RecvStr.toString()) ){
                    // 既に登録されている情報が同じなので書き込み不要にする
                    _write = false;
                }else if( _customer_id.trim().length() > 0 ){
                    if( MessageDialog.showYesNoDialog( this,
                            "挿入されたカードが、精算中の顧客番号と一致しません。\n" +
                            "上書き実行で処理を続行してもよろしいですか？",
                            JOptionPane.QUESTION_MESSAGE ) == JOptionPane.NO_OPTION){
                        Cancel();
                    }
                }
            }else{
                if( _com.getLastError() != _com.STAT_NORMAL &&
                    _com.getLastError() != _com.STAT_NON_CARD ){
                    _timer.stop();
                    
                    if( this._mode != MODE_READ && _com.getLastError() == _com.STAT_NON_STRIPE ){
                        // 磁気情報読み取りエラー
                        if( MessageDialog.showYesNoDialog( this,
                                _com.getLastMessage() + "\n新規カードとして処理を続行してもよろしいですか？",
                                JOptionPane.QUESTION_MESSAGE ) == JOptionPane.YES_OPTION){
                            _canceled = false;
                            this.setVisible(false);
                        }else{
                            Cancel();
                        }
                    }else{
                        // エラーメッセージを表示
                        MessageDialog.showMessageDialog( this,
                                _com.getLastMessage(),
                                JOptionPane.ERROR_MESSAGE );
                        Cancel();
                    }
                }
            }
        }
    }
    
    /**
     * 読み込みタイマーを開始する
     */
    private boolean StartTimer(){
        if( _mode == MODE_CLEAN ){
            _com.Clean();
        }else{
            if( !_com.Read() ){
                // エラーメッセージを表示
                MessageDialog.showMessageDialog( this,
                        _com.getLastMessage(),
                        JOptionPane.ERROR_MESSAGE );
                Cancel();
                return false;
            }
        }
        
        _timer = new Timer(500 , this);
        _timer.start();
        return true;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnClose.setContentAreaFilled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u30dd\u30a4\u30f3\u30c8\u30ab\u30fc\u30c9\u633f\u5165");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("MS UI Gothic", 0, 18));
        jLabel1.setText("\u30ab\u30fc\u30c9\u3092\u633f\u5165\u3057\u3066\u304f\u3060\u3055\u3044");

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if( _timer != null ){
            Cancel();
        }
    }//GEN-LAST:event_formWindowClosing
    
    private void Cancel() {
        _com.Cancel();
        _RecvCardData = "";
        if( _timer != null ){
            _timer.stop();
        }
        _canceled = true;
        this.setVisible(false);
        _com.Eject();
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        Cancel();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void displayMessage( int messageType )
    {
        switch( messageType )
        {
            case MACHINE_STATE_ERROR:
                MessageDialog.showMessageDialog( this, 
                    MessageUtil.getMessage( MACHINE_STATE_ERROR ), 
                    javax.swing.JOptionPane.ERROR_MESSAGE );
                break;
            default:
                break;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    
}
