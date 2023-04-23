/*
 * VisitKarteDialog.java
 *
 * Created on 2017/03/29, 12:00
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.account.PrintQR;
import com.geobeck.sosia.pos.swing.AbstractImagePanelEx;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import static com.geobeck.swing.SwingUtil.moveAnchor;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.mozilla.browser.MozillaInitialization;
import org.mozilla.browser.MozillaPanel;


/***
 * QR�R�[�h��ʕ\���_�C�A���O
 * @author GB
 * 
 */
public class VisitKarteDialog extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx  {
    
    VisitKarteSetting vks = new VisitKarteSetting();
    
    private MozillaPanel mozPanel =    null;
    
    private static JDialog dialog	= null;
    
    private Integer  karteQrId   =  0;
    
    private static boolean useDefaultBrowserFlag = false;
    
    /**
     * Creates new form VisitKarteDialog
     * @param custimerID
     */
    public VisitKarteDialog(Integer custimerID) {
        
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                initComponents();
                this.setTitle("�񎟌��R�[�h�\��");

                this.setSize(460, 390);

                this.qrDisplay(custimerID);
            }finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        
    }
    
    /*
     * �_�C�A���O�Ƃ��ĕ\��
     */
    public static boolean ShowDialog(Frame owner, Integer custimerID, AbstractImagePanelEx p) {
        
        VisitKarteDialog vkp =  new VisitKarteDialog(custimerID);

        if(!useDefaultBrowserFlag) {
            openAnchorDialog(owner, true, vkp, "�񎟌��R�[�h�\��", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);
            vkp.dispose();
        }
        
        return true;
    }
    
    public static void openAnchorDialog(Frame owner, boolean modal, JPanel panel, String title, int anchor ) {
		dialog	=	new JDialog(owner, modal);
		
		dialog.setTitle(title);
		setPanelToDialog(dialog, panel);
		moveAnchor( dialog, anchor );
                
                // �_�C�A���O�T�C�Y�Œ�
                dialog.setResizable(false);
                
		dialog.setVisible(true);
    }
	
    private static void setPanelToDialog(JDialog dialog, JPanel panel) {
            dialog.setSize(panel.getWidth() + 4, panel.getHeight() + 32);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(dialog.getContentPane());
            dialog.getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    
    
    /**
     * QR�R�[�h��ʂ�\��
     * @param customerID 
     */
    private void qrDisplay(Integer customerID) {
        
        try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                String strUri = "http://cws01.sosia-pos.com/karte/qrcode/";

                // data_karte_qr�Ƀf�[�^�o�^&�V�[�P���XID���擾
                Integer karteQrID = vks.registDataKarteQR(customerID);
                
                // URI�쐬
                if(karteQrID > 0) {

                    // �X�L�[�}��
                    String shemaName = vks.getSchemaName();
                    
                    strUri = strUri + shemaName + "/" + karteQrID.toString();
                    
                    if(customerID != 0) {
                        // �ڋqID
                        strUri = strUri + "/" + customerID.toString();
                    }
                    
                    // �V�[�P���XID�ݒ�
                    this.setKarteQrID(karteQrID);
                    
                }else {
                    MessageDialog.showMessageDialog(this,
                        "�񎟌��R�[�h�̕\���Ɏ��s���܂����B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
             

                mozPanel = new MozillaPanel(MozillaPanel.VisibilityMode.FORCED_HIDDEN, MozillaPanel.VisibilityMode.FORCED_HIDDEN);
                setLayout(null);

                if (MozillaInitialization.getStatus().equals(MozillaInitialization.InitStatus.INITIALIZED)) {

                    // MozPanel
                    mozPanel.setUpdateTitle(false);
                    mozPanel.setBounds(0, 0, 460, 340);
                    add(mozPanel);
                    
                    // ����{�^����ݒu����p�l��
                    JPanel panel = new JPanel();
                    panel.setLayout(null);
                    panel.setBounds(0, 340, 460, 50);
                    panel.setBackground(Color.WHITE);
                    add(panel);

                    // ����{�^��
                    JButton printButton = new JButton();
                    printButton.setIcon(SystemInfo.getImageIcon("/button/common/print_off.jpg"));
                    printButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/print_on.jpg"));
                    printButton.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            printButtonActionPerformed();
                        }
                    });
                    printButton.setBounds(340, 10, 91, 25);
                    panel.add(printButton);
                    
                    
                    setBounds(0, 0, 460, 390);
                    setVisible(true);

                    // URL�Ǎ�
                    mozPanel.load(strUri);

                } else {

                    // �f�t�H���g�u���E�U���N������QR�R�[�h�\��
                    if(!this.qrDefaultBrowserDisplay(strUri)) {
                            MessageDialog.showMessageDialog(this,
                                "�u���E�U���擾�ł��܂���ł����B\n�񎟌��R�[�h�̕\���Ɏ��s���܂����B",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                    }
                    
                    this.useDefaultBrowserFlag = true;

                }
                      
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * �f�t�H���g�̃u���E�U���N������QR�R�[�h��ʂ�\��
     * @param strUri
     * @return true:���� false:���s
     */
    private boolean qrDefaultBrowserDisplay(String strUri) {
        
            boolean flag = false;
        
            if(!Desktop.isDesktopSupported()) {
                return flag;
            }
            
            Desktop desktop = Desktop.getDesktop();
            
            if(!desktop.isSupported(Desktop.Action.BROWSE)) {
                return flag;
            }
            
            try {
                
                URI uri = new URI(strUri);
                desktop.browse(uri);
                
                flag = true;
                
            }catch(  URISyntaxException | IOException e) {
                flag = false;
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            return flag;
    }

    
    /**
     * QR�R�[�h�̉�ʃL���v�`�����擾
     * @return BufferedImage
     */
    private BufferedImage printScreen() {
        
        BufferedImage printImage = null;
                
        try{
            Robot robot = new Robot();
            //Point point = new Point((int)dialog.getLocation().getX()+105, (int)dialog.getLocation().getY()+88);
            Point point = new Point((int)dialog.getLocation().getX()+105, (int)dialog.getLocation().getY()+64);
            //Dimension screenSize = new Dimension(250, 250);
            Dimension screenSize = new Dimension(260, 260);
            Rectangle rect = new Rectangle(point, screenSize);
            printImage = robot.createScreenCapture(rect);
            
        }catch(AWTException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return printImage;
    }
    
    
    /**
     * �v�����g�A�E�g
     */
    private void printButtonActionPerformed() {                                            
        PrintQR pq = new PrintQR();
        //�v�����^��񂪑��݂��Ȃ��ꍇ�͏����𔲂���
        if(!pq.canPrint()) {
                MessageDialog.showMessageDialog(this,
                    "�v�����^�̎擾�Ɏ��s���܂����B",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
        }
        
        // �������
        pq.print(getQrImageToByte(), this.getKarteQrID(), null, null);
    }

    
    /**
     * �摜��ImageIcon�Ŏ擾
     * @return ImageIcon
     */
    private ImageIcon getQrImageToByte() {
        
        ImageIcon qrImage;
        
        BufferedImage textstream = printScreen();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try{
            ImageIO.write(textstream, "png", baos);
            //ImageIO.write(textstream, "png", new File("sampleQR.png")); //test�p
        }catch(IOException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        byte[] qrImageByteArray = baos.toByteArray();
        qrImage = new ImageIcon(qrImageByteArray);
        
        return qrImage;
    }
    
    /**
     * �������ꂽ�J���eQRID�i�V�[�P���XID�j�擾
     * @return �J���eQRID
     */
    public Integer getKarteQrID() {
        return this.karteQrId;
    }

    /**
     * �������ꂽ�J���eQRID�i�V�[�P���XID�j��ݒ�
     * @param karteQrId 
     */
    public void setKarteQrID(Integer karteQrId) {
        this.karteQrId = karteQrId;
    }
    
    /**
     * �_�C�A���O��j��
     */
    private void dispose() {
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).dispose();
        }
    }
    
    
 
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}