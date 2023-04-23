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
    private final static int MACHINE_STATE_ERROR = 20001;   // �ڑ��G���[
    
    // �R�}���h
    public final static int MODE_READ  = 0;         // �ǂݍ���
    public final static int MODE_PRINT = 1;         // �v�����g
    public final static int MODE_CLEAR = 2;         // �N���A
    public final static int MODE_CLEAN = 3;         // �N���[��
    
    // ���s����
    public final static int STAT_ERROR      = -1;   // �G���[
    public final static int STAT_SUCCESS    =  0;   // ����
    public final static int STAT_CANCELED   =  1;   // �L�����Z��
    
    private static boolean beforeCheck() {
        if( SystemInfo.getPointcardConnection() == null ) {
            return false;
        }
        return true;
    }
    
    /**
     * �Ǎ��p�J�[�h�}���҂��_�C�A���O��\�����܂�
     * @return �����ڋqID
     * @param parent �e�t���[��
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
        
        SystemInfo.getLogger().log(Level.INFO, "�J�[�h�}���҂�");
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
                            // �ڋq���Ȃ�
                            MessageDialog.showMessageDialog( this,
                                    "�����ɍ����ڋq�����܂���B", JOptionPane.ERROR_MESSAGE );
                        }
                    } catch (SQLException ex) {
                        customer_id = null;
                        ex.printStackTrace();
                    }
                    
                }else{
                    MessageDialog.showMessageDialog( this,
                            "�J�[�h�̏�񂪕s���ł��B", JOptionPane.ERROR_MESSAGE );
                    
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return customer_id;
    }
    
    /**
     * �J�[�h�}���҂��_�C�A���O��\�����܂�
     * @return 0:�����A-1:�L�����Z���A�ȊO:�G���[
     * @param parent �e�t���[��
     * @param mode ���s���[�h 0:�J�[�h�ǂݍ��݁A1:�J�[�h����A2:�J�[�h�N���A
     * @param customer_id �����ڋqID
     */
    static public int ShowDialog(java.awt.Frame parent, int mode, String customer_id){
        if( !beforeCheck() ) return -1;
        
        InsertCardDialog dlg = new InsertCardDialog(parent, true);
        
        SystemInfo.getLogger().log(Level.INFO, "�J�[�h�}���҂�");
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
     * �J�[�h�}���҂��_�C�A���O��\�����܂�
     * @param parent �e�t���[��
     * @param mode   ���s���[�h 0:�J�[�h�ǂݍ��݁A1:�J�[�h����A2:�J�[�h�N���A
     * @return 0:�����A-1:�L�����Z���A�ȊO:�G���[
     */
    static public int ShowDialog(java.awt.Frame parent, int mode){
        return ShowDialog(parent, mode, "");
    }
    
    static public int ShowDialog(JDialog parent, int mode){
        return ShowDialog(parent, mode, "");
    }
    
    /**
     * �R�}���h���s
     * @return 0:�����A1:�L�����Z���A�ȊO:�G���[
     */
    private int RunCommand() {
        boolean ret = false;
        // �X�e�[�^�X���m�F
        if( !SystemInfo.getPointcardConnection().Status() ){
            this.displayMessage( MACHINE_STATE_ERROR );
            return STAT_ERROR;
        }
        
        if( !StartTimer() ) return STAT_ERROR;
        
        this.setVisible(true);
        if( this._canceled )   return STAT_CANCELED;
        
        // �}�����m�F���Ă�����s����R�}���h
        switch(this._mode){
            case MODE_PRINT:     // �J�[�h���
                ret = _com.Print();
                if(!ret) break;
                if(_write){
                    ret = _com.Write(_customer_id);
                }
                break;
            case MODE_CLEAR:     // �J�[�h�N���A
                ret = _com.Clear();
                if(!ret) break;
                if(_write){
                    ret = _com.Write("");
                }
                break;
            case MODE_CLEAN:     // �J�[�h�N���[��
                break;
            case MODE_READ:     // �J�[�h���
                ret = true;
                break;
            default:
                break;
        }
        if( ret == false ){
            if( _com.getLastError() != _com.STAT_NORMAL ){
                // �G���[���b�Z�[�W��\��
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
     * �ǂݍ��񂾃f�[�^���󂯎��
     * @return ��M�f�[�^
     */
    public String getRecvData() {
        return _RecvCardData;
    }
    
    /**
     * �ǂݍ��݊������m�F���� or �ǂݍ��݃f�[�^��M�v���𔭍s����
     * @param e
     */
    public void actionPerformed(ActionEvent e){
        
        if( _mode == MODE_CLEAN ){
            if( !_com.isCleannig() ){
                _timer.stop();
                _canceled = false;
                
                if( _com.getLastError() != _com.STAT_NORMAL ){
                    // �G���[���b�Z�[�W��\��
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
                    // ���ɓo�^����Ă����񂪓����Ȃ̂ŏ������ݕs�v�ɂ���
                    _write = false;
                }else if( _customer_id.trim().length() > 0 ){
                    if( MessageDialog.showYesNoDialog( this,
                            "�}�����ꂽ�J�[�h���A���Z���̌ڋq�ԍ��ƈ�v���܂���B\n" +
                            "�㏑�����s�ŏ����𑱍s���Ă���낵���ł����H",
                            JOptionPane.QUESTION_MESSAGE ) == JOptionPane.NO_OPTION){
                        Cancel();
                    }
                }
            }else{
                if( _com.getLastError() != _com.STAT_NORMAL &&
                    _com.getLastError() != _com.STAT_NON_CARD ){
                    _timer.stop();
                    
                    if( this._mode != MODE_READ && _com.getLastError() == _com.STAT_NON_STRIPE ){
                        // ���C���ǂݎ��G���[
                        if( MessageDialog.showYesNoDialog( this,
                                _com.getLastMessage() + "\n�V�K�J�[�h�Ƃ��ď����𑱍s���Ă���낵���ł����H",
                                JOptionPane.QUESTION_MESSAGE ) == JOptionPane.YES_OPTION){
                            _canceled = false;
                            this.setVisible(false);
                        }else{
                            Cancel();
                        }
                    }else{
                        // �G���[���b�Z�[�W��\��
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
     * �ǂݍ��݃^�C�}�[���J�n����
     */
    private boolean StartTimer(){
        if( _mode == MODE_CLEAN ){
            _com.Clean();
        }else{
            if( !_com.Read() ){
                // �G���[���b�Z�[�W��\��
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
