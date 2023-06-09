/*
 *
 * Created on 2006/11/06, 9:26
 */
package com.geobeck.sosia.pos.hair.mail;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.filechooser.WildcardFileFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.security.CodeSource;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author lvut
 */
public class TabIconPanel extends javax.swing.JDialog {

    private final double MAX_FILE_SIZE = 90;
    private final String MAX_FILE_SIZE_STR = "90KB";
    private Component opener = null;
    final HashMap<Object, ImageIcon> icons = new HashMap<Object, ImageIcon>();
    final HashMap<Object, String> iconPaths = new HashMap<Object, String>();
    final HashMap<Object, ImageIcon> icons3 = new HashMap<Object, ImageIcon>();
    LinkedHashMap<String, DataImage> imgesMap = new LinkedHashMap<String, DataImage>();
    private ArrayList<DataImage> dataImages = new ArrayList<DataImage>();

    public TabIconPanel(java.awt.Frame parent, boolean modal,
            Component opener, Integer productDivision, MstShop shop, Boolean isDecomo) {
        super(parent, modal);
        this.opener = opener;
        initComponents();
        addMouseCursorChange();
        this.setSize(700, 600);
        this.setTitle("絵文字パレット");
        init();
        //Function delete image emoj tab pending
        tabImage.remove(deldecomoPanel);
        //Function delete image emoj tab pending
        if (!isDecomo) {
            tabImage.remove(deldecomoPanel);
            tabImage.remove(uploadImageaPanel);
        }
        SwingUtil.moveCenter(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabImage = new javax.swing.JTabbedPane();
        emojPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listIcon = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        deldecomoPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listIcon1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        uploadImageaPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listIcon3 = new javax.swing.JList();
        btnUpload = new javax.swing.JButton();
        delImage = new javax.swing.JButton();
        imagePath = new com.geobeck.swing.JTextFieldEx();
        imageSelect = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(null);

        listIcon.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listIcon.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        listIcon.setVisibleRowCount(-1);
        listIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listIconMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                listIconMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(listIcon);

        jLabel1.setText("履歴 ");

        org.jdesktop.layout.GroupLayout emojPanelLayout = new org.jdesktop.layout.GroupLayout(emojPanel);
        emojPanel.setLayout(emojPanelLayout);
        emojPanelLayout.setHorizontalGroup(
            emojPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(emojPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(emojPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .add(emojPanelLayout.createSequentialGroup()
                        .add(jLabel1)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        emojPanelLayout.setVerticalGroup(
            emojPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, emojPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .add(jLabel1)
                .add(18, 18, 18)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 395, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabImage.addTab("標準絵文字", emojPanel);

        listIcon1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listIcon1.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        listIcon1.setVisibleRowCount(-1);
        listIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listIcon1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                listIcon1MouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(listIcon1);

        jButton1.setPreferredSize(new java.awt.Dimension(91, 25));
        jButton1.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));

        jButton1.setBorderPainted(false);

        jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("履歴 ");

        org.jdesktop.layout.GroupLayout deldecomoPanelLayout = new org.jdesktop.layout.GroupLayout(deldecomoPanel);
        deldecomoPanel.setLayout(deldecomoPanelLayout);
        deldecomoPanelLayout.setHorizontalGroup(
            deldecomoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, deldecomoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(deldecomoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(deldecomoPanelLayout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
                .addContainerGap())
        );
        deldecomoPanelLayout.setVerticalGroup(
            deldecomoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, deldecomoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(deldecomoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .add(18, 18, 18)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 397, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabImage.addTab("  装飾文字  ", deldecomoPanel);

        listIcon3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listIcon3.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        listIcon3.setVisibleRowCount(-1);
        listIcon3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listIcon3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                listIcon3MouseEntered(evt);
            }
        });
        jScrollPane4.setViewportView(listIcon3);

        btnUpload.setIcon(SystemInfo.getImageIcon("/button/key/upload_off.jpg"));
        btnUpload.setPressedIcon(SystemInfo.getImageIcon("/button/key/upload_on.jpg"));
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        delImage.setPreferredSize(new java.awt.Dimension(91, 25));
        delImage.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));

        delImage.setBorderPainted(false);

        delImage.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        delImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delImageActionPerformed(evt);
            }
        });

        imagePath.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        imageSelect.setIcon(SystemInfo.getImageIcon("/button/select/select_file_off.jpg"));
        imageSelect.setBorderPainted(false);
        imageSelect.setContentAreaFilled(false);
        imageSelect.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_file_on.jpg"));
        imageSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageSelectActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout uploadImageaPanelLayout = new org.jdesktop.layout.GroupLayout(uploadImageaPanel);
        uploadImageaPanel.setLayout(uploadImageaPanelLayout);
        uploadImageaPanelLayout.setHorizontalGroup(
            uploadImageaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(uploadImageaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(uploadImageaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, uploadImageaPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(uploadImageaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, delImage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, uploadImageaPanelLayout.createSequentialGroup()
                                .add(imagePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(imageSelect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(38, 38, 38)
                                .add(btnUpload, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        uploadImageaPanelLayout.setVerticalGroup(
            uploadImageaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(uploadImageaPanelLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .add(delImage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 338, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(uploadImageaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnUpload, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(uploadImageaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(imageSelect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(imagePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(32, 32, 32))
        );

        tabImage.addTab("     画像     ", uploadImageaPanel);

        getContentPane().add(tabImage);
        tabImage.setBounds(0, 0, 640, 490);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    }//GEN-LAST:event_formComponentShown

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here
    }//GEN-LAST:event_jButton1ActionPerformed

    private void listIcon1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listIcon1MouseEntered
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            setSelectedImgage(listIcon1);

        }
    }//GEN-LAST:event_listIcon1MouseEntered

    private void listIcon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listIcon1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            setSelectedImgage(listIcon1);

        }
    }//GEN-LAST:event_listIcon1MouseClicked

    private void listIconMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listIconMouseEntered
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            setSelectedImgage(listIcon);

        }
    }//GEN-LAST:event_listIconMouseEntered

    private void listIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listIconMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            setSelectedImgage(listIcon);
        }
    }//GEN-LAST:event_listIconMouseClicked

    private void delImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delImageActionPerformed
        // TODO add your handling code here:
        if (listIcon3.getSelectedValue() != null) {
            DataImage img = imgesMap.get(listIcon3.getSelectedValue().toString());
            if (MessageDialog.showYesNoDialog(
                    this,MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE,listIcon3.getSelectedValue().toString()), this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
                return;
            }
            try {
                if (img.deleteImage()) {
                    this.init();
                }
            } catch (Exception ex) {
                Logger.getLogger(TabIconPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_delImageActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        if (this.regist()) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
            this.init();
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    private void imageSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageSelectActionPerformed
        this.selectImage();
    }//GEN-LAST:event_imageSelectActionPerformed

    private void listIcon3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listIcon3MouseEntered
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            setSelectedImgage(listIcon3);

        }
    }//GEN-LAST:event_listIcon3MouseEntered

    private void listIcon3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listIcon3MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            setSelectedImgage(listIcon3);

        }
    }//GEN-LAST:event_listIcon3MouseClicked
    /**
     * 画像選択時の処理
     */
    private void selectImage() {
        String filePath = this.selectImageFile();

        //画像をチェックする
        if (this.checkImage(filePath)) {

            if (filePath.length() > 0) {
                File f = new File(filePath);
                this.showImage(f);
                imagePath.setText(filePath);
            }

        } else {
            imagePath.setText("");
        }
    }

    /**
     * 画像ファイルを開く
     *
     * @return
     */
    private String selectImageFile() {
        JFileChooser jfc = new JFileChooser();
        WildcardFileFilter filter = new WildcardFileFilter("*.jpg;*.gif", "JPG & GIFファイル");
        jfc.setFileFilter(filter);
        jfc.setAcceptAllFileFilterUsed(false);

        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().getAbsolutePath();
        }

        return "";
    }

    /**
     * 画像をチェックする
     *
     * @param filePath 画像ファイルのパス
     * @return true - OK
     */
    private boolean checkImage(String filePath) {
        if (!filePath.equals("")) {
            File f = new File(filePath);
            double bytes = f.length();
            double kilobytes = (bytes / 1024);
            //ファイルが存在する場合
            if (f.exists()) {
                //ファイルサイズをチェック
                if (MAX_FILE_SIZE < kilobytes) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(1201, "画像", MAX_FILE_SIZE_STR),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                return true;
            } else {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(1200,
                        f.getAbsolutePath()),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void showImage(File f) {
        DefaultListModel Model = new DefaultListModel();
        icons3.put(f, new ImageIcon(f.getAbsolutePath()));
        Model.addElement(f);
        listIcon3.setModel(Model);
        listIcon3.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                Component c = super.getListCellRendererComponent(list, value,
                        index, isSelected, cellHasFocus);
                // set icon for cell image
                ImageIcon icon = (ImageIcon) icons3.get(value);
                
                JLabel label =
                        (JLabel) super.getListCellRendererComponent(list,
                        value,
                        index,
                        isSelected,
                        hasFocus());
                label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(15, 15, 0)));
                label.setText("");
                return c;
            }
        });

    }

    private void setSelectedImgage(JList tab) {
        String index = tab.getSelectedValue().toString();
        if (opener instanceof SelectImagesOpener) {
            SelectImagesOpener srp = (SelectImagesOpener) opener;
            srp.addSelectedImage(index, icons, iconPaths,imgesMap);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpload;
    private javax.swing.JButton delImage;
    private javax.swing.JPanel deldecomoPanel;
    private javax.swing.JPanel emojPanel;
    private com.geobeck.swing.JTextFieldEx imagePath;
    private javax.swing.JButton imageSelect;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList listIcon;
    private javax.swing.JList listIcon1;
    private javax.swing.JList listIcon3;
    private javax.swing.JTabbedPane tabImage;
    private javax.swing.JPanel uploadImageaPanel;
    // End of variables declaration//GEN-END:variables
    protected static final String LS = System.getProperty("line.separator");

    /**
     * メール送信画面用FocusTraversalPolicy
     */
    private void init() {
        CodeSource src = TabIconPanel.class.getProtectionDomain().getCodeSource();
        ArrayList<String> list = new ArrayList<String>();
        DataImage img = new DataImage();

        try {
            //load image emoj
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry ze = null;

                while ((ze = zip.getNextEntry()) != null) {
                    String entryName = ze.getName();
                    if (entryName.contains("/icon/") && (entryName.endsWith(".jpg") || entryName.endsWith(".gif"))) {
                        list.add(entryName.substring(37, entryName.length()));
                    }
                }

            }
            //load image decomo
            img.load();
            dataImages.clear();
            dataImages.addAll(img.getDataImages());
        } catch (Exception e) {
        }
        DefaultListModel Model = new DefaultListModel();
        for (int i = 0; i < list.size(); i++) {
            icons.put("[絵" + list.get(i).substring(0, list.get(i).length() - 4) + "]", new ImageIcon(getClass().getResource("icon/" + list.get(i))));
            iconPaths.put("[絵" + list.get(i).substring(0, list.get(i).length() - 4) + "]", "icon/" + list.get(i));
            Model.addElement("[絵" + list.get(i).substring(0, list.get(i).length() - 4) + "]");
        }
        listIcon.setModel(Model);
        listIcon.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value,
                        index, isSelected, cellHasFocus);
                // set icon for cell image
                ImageIcon icon = (ImageIcon) icons.get(value);
                JLabel label =
                        (JLabel) super.getListCellRendererComponent(list,
                        value,
                        index,
                        isSelected,
                        hasFocus());
                label.setIcon(icon);
                label.setText("");
                return c;
            }
        });
        listIcon1.setModel(Model);
        listIcon1.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value,
                        index, isSelected, cellHasFocus);
                // set icon for cell image
                ImageIcon icon = (ImageIcon) icons.get(value);
                JLabel label =
                        (JLabel) super.getListCellRendererComponent(list,
                        value,
                        index,
                        isSelected,
                        hasFocus());
                label.setIcon(icon);
                label.setText("");
                return c;
            }
        });
        //get image from database
        DefaultListModel model = new DefaultListModel();
        if (dataImages.size() > 0) {
            for (DataImage image : dataImages) {
                icons3.put("[画像" + image.getMailimgId() + "]", image.getImage());
                icons.put("[画像" + image.getMailimgId() + "]", image.getImage());
                imgesMap.put("[画像" + image.getMailimgId() + "]", image);
                model.addElement("[画像" + image.getMailimgId() + "]");
            }
        }
        listIcon3.setModel(model);
        listIcon3.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                Component c = super.getListCellRendererComponent(list, value,
                        index, isSelected, cellHasFocus);
                // set icon for cell image
                ImageIcon icon = (ImageIcon) icons3.get(value);
                JLabel label =
                        (JLabel) super.getListCellRendererComponent(list,
                        value,
                        index,
                        isSelected,
                        hasFocus());
                label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(15, 15, 0)));
                label.setText("");
                return c;
            }
        });
        imagePath.setText("");
    }

    /**
     * 登録処理を行う。
     *
     * @return true - 成功、false - 失敗
     */
    private boolean regist() {
        boolean result = false;
        File f = new File(imagePath.getText());
        String extension = f.getPath().substring(f.getPath().lastIndexOf(".") + 1);
        DataImage img = new DataImage();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            con.begin();

            try {
                //入力内容の設定
                img.setImageFilePath(imagePath.getText());
                img.setSize((int) f.length());
                img.setFormat(extension);
                result = img.regist(con);
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            if (result) {
                con.commit();
                SystemInfo.getLogger().log(Level.INFO, "報登録正常終了");
            } else {
                SystemInfo.getLogger().log(Level.INFO, "報登録異常終了");
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    class ImageRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            // for default cell renderer behavior
            Component c = super.getListCellRendererComponent(list, value,
                    index, isSelected, cellHasFocus);
            // set icon for cell image
            ((JLabel) c).setIcon(new ImageIcon((BufferedImage) value));
            ((JLabel) c).setText("");
            return c;
        }
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
    }

    /**
     * aComponent の前にフォーカスを受け取る Component を返します。 aContainer は aComponent
     * のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
     *
     * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
     * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
     * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
     */
    public Component getComponentBefore(Container aContainer, Component aComponent) {


        return listIcon;
    }

    /**
     * トラバーサルサイクルの最初の Component を返します。 このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする
     * Component を判定するために使用します。
     *
     * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
     * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は
     * null
     */
    public Component getFirstComponent(Container aContainer) {
        return listIcon;
    }

    /**
     * トラバーサルサイクルの最後の Component を返します。 このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする
     * Component を判定するために使用します。
     *
     * @param aContainer aContainer - 最後の Component
     * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
     * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は
     * null
     */
    public Component getLastComponent(Container aContainer) {
        return listIcon;
    }

    /**
     * フォーカスを設定するデフォルトコンポーネントを返します。 aContainer
     * をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
     *
     * @param aContainer デフォルトの Component
     * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
     * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component
     * が見つからない場合は null
     */
    public Component getDefaultComponent(Container aContainer) {
        return listIcon;
    }

    /**
     * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。 show() または setVisible(true)
     * の呼び出しで一度ウィンドウが表示されると、 初期化コンポーネントはそれ以降使用されません。
     * 一度別のウィンドウに移ったフォーカスが再び設定された場合、 または、一度非表示状態になったウィンドウが再び表示された場合は、
     * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
     * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
     *
     * @param window 初期コンポーネントが返されるウィンドウ
     * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
     */
    public Component getInitialComponent(Window window) {
        return listIcon;
    }
}
