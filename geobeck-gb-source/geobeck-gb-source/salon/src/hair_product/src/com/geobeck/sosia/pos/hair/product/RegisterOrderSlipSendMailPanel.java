/*
 * RegistReservationDialog.java
 *
 * Created on 2007/10/05, 11:50
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.hair.data.product.DataSlipOrder;
import com.geobeck.sosia.pos.hair.product.logic.ProductOrderReportLogic;
import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.swing.AbstractImagePanelEx;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.MessageDialog;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author  kanemoto
 */
public class RegisterOrderSlipSendMailPanel extends AbstractImagePanelEx {

        private MstShop shop = null;
        private MstSupplier supplier = null;
        private DataSlipOrder dataSlipOrder = null;

        public RegisterOrderSlipSendMailPanel(MstShop shop, MstSupplier supplier, DataSlipOrder dataSlipOrder) {
            initComponents();
            this.setSize(485, 450);
            this.shop = shop;
            this.supplier = supplier;
            this.dataSlipOrder = dataSlipOrder;
            this.init();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        charge = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        shopName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        sendMailButton = new javax.swing.JButton();
        shopLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        supplierName = new javax.swing.JLabel();
        supplierMail = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        mailBodyScrollPane = new javax.swing.JScrollPane();
        mailBody = new javax.swing.JTextArea();
        mailTitle = new javax.swing.JTextField();

        setFocusCycleRoot(true);
        setFocusTraversalPolicyProvider(true);
        setOpaque(false);

        jPanel1.setOpaque(false);

        shopName.setText("XXXXX店");

        jLabel1.setText("仕入先");

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        sendMailButton.setIcon(SystemInfo.getImageIcon("/button/mail/send_mail_off.jpg"));
        sendMailButton.setBorderPainted(false);
        sendMailButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/send_mail_on.jpg"));
        sendMailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMailButtonActionPerformed(evt);
            }
        });

        shopLabel1.setText("店舗");

        jLabel2.setText("メールアドレス");

        supplierName.setText("XXXXX");

        supplierMail.setText("XXXXX＠XXXX");

        jLabel3.setText("タイトル");

        jLabel4.setText("本文");

        mailBody.setColumns(20);
        mailBody.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        mailBody.setRows(5);
        mailBodyScrollPane.setViewportView(mailBody);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(shopLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(supplierMail, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(shopName, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(sendMailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(11, 11, 11))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(supplierName, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(416, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(mailTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                            .addComponent(mailBodyScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sendMailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(shopName, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(shopLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(supplierMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(3, 3, 3)
                .addComponent(mailTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mailBodyScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void sendMailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMailButtonActionPerformed

            if (MessageDialog.showYesNoDialog(
                    this,
                    supplier.getMailAddress() + " 宛に発注書を送信します。よろしいですか？",
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
            {
                    return;
            }

            // PDF出力
            String fileName = SystemInfo.getLogRoot() + "/発注書.pdf";
            ProductOrderReportLogic.setExportFile(fileName);
            int result = ProductOrderReportLogic.OutputPdfReport2(dataSlipOrder.getShopId(), dataSlipOrder.getSlipNo(), dataSlipOrder.getOrderDate());

            if (result == ProductOrderReportLogic.RESULT_SUCCESS ) {
                // 成功
                sendMail(supplier.getMailAddress(), fileName);

            } else if(result == ProductOrderReportLogic.RESULT_DATA_NOTHNIG ) {

                // データなし
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(4001),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);

            } else if(result == ProductOrderReportLogic.RESULT_ERROR ) {

                // 予期せぬエラー
                MessageDialog.showMessageDialog( this,
                        MessageUtil.getMessage(1099),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE );
            }

            new File(fileName).delete();

	}//GEN-LAST:event_sendMailButtonActionPerformed

	
	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
            this.closePanel();
	}//GEN-LAST:event_closeButtonActionPerformed
												
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup charge;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea mailBody;
    private javax.swing.JScrollPane mailBodyScrollPane;
    private javax.swing.JTextField mailTitle;
    private javax.swing.JButton sendMailButton;
    private javax.swing.JLabel shopLabel1;
    private javax.swing.JLabel shopName;
    private javax.swing.JLabel supplierMail;
    private javax.swing.JLabel supplierName;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * 初期化処理を行う
	 */
	private void init() {
            this.addMouseCursorChange();
            this.setListener();
            this.shopName.setText(shop.getShopName());
            this.supplierName.setText(supplier.getSupplierName());
            this.supplierMail.setText(supplier.getMailAddress());
	}

	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange() {
            SystemInfo.addMouseCursorChange(sendMailButton);
            SystemInfo.addMouseCursorChange(closeButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener() {
            mailTitle.addKeyListener(SystemInfo.getMoveNextField());
            mailTitle.addFocusListener(SystemInfo.getSelectText());
            mailBodyScrollPane.addKeyListener(SystemInfo.getMoveNextField());
            mailBodyScrollPane.addFocusListener(SystemInfo.getSelectText());
	}

	/**
	 * ダイアログを閉じる
	 */
	private void closePanel() {
                if(this.isDialog()) {
                        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
                } else {
                        this.setVisible(false);
                }
	}

        private void sendMail(String mail, String file) {

            Properties objPrp = new Properties();
            // SMTPサーバ名

            //IVS NNTUAN START EDIT 20131011
            //objPrp.put("mail.smtp.host","geobeck.com");
            //// メールセッションを確立
            //Session session = Session.getDefaultInstance(objPrp,null);
            objPrp.put("mail.smtp.host","mail.geobeck.com");
            objPrp.put("mail.smtp.port","587");
            objPrp.put("mail.smtp.starttls.enable","true");
            objPrp.put("mail.smtp.auth","true");
            // メールセッションを確立
            Session session = Session.getDefaultInstance(objPrp, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("test@geobeck.com", "7K2$/T8%vGcr");
                    }
            });
            //IVS NNTUAN END EDIT 20131011            
            
            // 送信メッセージを生成
            MimeMessage objMsg = new MimeMessage(session);

            try {

                // 送信日時
                objMsg.setSentDate(new Date());

                // 送信先（TO）
                objMsg.setRecipients(Message.RecipientType.TO, mail);
                // 送信先（BCC）
                objMsg.setRecipients(Message.RecipientType.BCC, shop.getMailAddress());

                // Fromヘッダ
                InternetAddress objFrm = new InternetAddress(shop.getMailAddress(), shop.getShopName());
                objMsg.setFrom(objFrm);

                // タイトル
                objMsg.setSubject(mailTitle.getText(), "ISO-2022-JP");

                // 本文
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setText(mailBody.getText() , "ISO-2022-JP");

                // 添付ファイル
                MimeBodyPart mbp2 = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(file);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(MimeUtility.encodeWord(fds.getName()));

                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);

                objMsg.setContent(mp);

                // メール送信
                Transport.send(objMsg);

		MessageDialog.showMessageDialog(
                        this,
			"送信しました。",
			this.getTitle(),
			JOptionPane.INFORMATION_MESSAGE);

                this.closePanel();
                
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

}
