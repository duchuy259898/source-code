/*
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.mail;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.imageio.stream.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.ArrayList;
import org.apache.commons.net.util.Base64;

/**
 *
 * @author lvut
 */
public class DataImage {

    /**
     * �摜ID
     */
    protected Integer mailimgId = null;
    /**
     * �摜�t�@�C���p�X
     */
    protected String imageFilePath = "";
    /**
     * �摜
     */
    protected ImageIcon image = null;
    /**
     * size
     */
    protected Integer size = null;
    /**
     * size
     */
    protected String format = null;
    /**
     * get List image
     */
    protected ArrayList<DataImage> dataImages ;
    /**
     * byte data image
     */
    protected String url = "";

    /**
     * Creates a new instance of DataImage
     */
    public DataImage() {
    }

    /**
     * �摜�t�@�C���p�X���擾����B
     *
     * @return �摜�t�@�C���p�X
     */
    public String getImageFilePath() {
        return imageFilePath;
    }

    /**
     * �摜�t�@�C���p�X���Z�b�g����B
     *
     * @param imageFilePath �摜�t�@�C���p�X
     */
    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    /**
     * �摜���擾����B
     *
     * @return �摜
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * �摜���Z�b�g����B
     *
     * @param image �摜
     */
    public void setImage(ImageIcon image) {
        this.image = image;
    }
    /**
     *
     * @return
     */
    public Integer getMailimgId() {
        return mailimgId;
    }

    public void setMailimgId(Integer mailimgId) {
        this.mailimgId = mailimgId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public ArrayList<DataImage> getDataImages() {
        return dataImages;
    }

    public void setDataImages(ArrayList<DataImage> dataImages) {
        this.dataImages = dataImages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    /**
     * �V�����摜ID���Z�b�g����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setNewImageId(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getNewImageIdSQL());

        if (rs.next()) {
            this.setMailimgId(rs.getInt("new_image_id"));
        }

        rs.close();
    }

    /**
     * �f�[�^��ǂݍ��ށB
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void load() throws SQLException {
        ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectSQL());
        ArrayList<DataImage> imgs = new ArrayList<DataImage>();
        while (rs.next()) {
             DataImage img = new DataImage();   
             img.setMailimgId(rs.getInt("mail_image_id"));
             img.setFormat(rs.getString("format"));
             img.setSize(rs.getInt("size"));

            byte[] imgBytes = rs.getBytes("image");

            if (imgBytes == null) {
                img.setImage(null);
            } else {
                img.setImage(new ImageIcon(imgBytes));
                img.setUrl(convertUrl(imgBytes,img.getFormat()));
            }
            imgs.add(img);
        }
        this.setDataImages(imgs);
        rs.close();
    }

    /**
     * �V�����摜ID���擾����r�p�k�����擾����B
     *
     * @return �V�����摜ID���擾����r�p�k��
     */
    private String getNewImageIdSQL() {
        return "select coalesce(max(mail_image_id), 0) + 1 as new_image_id\n"
                + "from data_mail_image \n";
    }

    /**
     * �o�^�������s���B
     *
     * @param con ConnectionWrapper
     * @return true - ����
     * @throws java.sql.SQLException SQLException
     */
    public boolean regist(ConnectionWrapper con) throws Exception {
        setNewImageId(con);
        if (!registDataImage(con)) {
            return false;
        }

        return true;
    }

    /**
     * �摜�f�[�^�̓o�^�������s���B
     *
     * @param con ConnectionWrapper
     * @param isExists �f�[�^�����݂��邩�ǂ���
     * @return true - ����
     * @throws java.sql.SQLException SQLException
     */
    public boolean registDataImage(ConnectionWrapper con) throws Exception {
        boolean result = false;

        String sql = "";
        PreparedStatement ps = null;
        sql = "insert into data_mail_image \n"
                + "(mail_image_id, image, format, size,\n"
                + "create_date, update_date, delete_date)\n"
                + "values(?, ?, "
                + SQLUtil.convertForSQL(this.getFormat()) + ", "
                + SQLUtil.convertForSQL(this.getSize()) + ", "
                + "current_timestamp, current_timestamp, null)\n";

        ps = con.prepareStatement(sql);

        int index = 1;

        ps.setInt(index++, this.getMailimgId());
        ps.setBytes(index, this.getImageData(this.getImageFilePath()));
        if (ps.executeUpdate() == 1) {
            result = true;
        }

        ps.close();

        return result;
    }
    
    public boolean deleteImage() throws Exception {
        
         boolean result = false;
         String sql = "update  data_mail_image \n"
                 + "   set delete_date = current_timestamp\n"
                 + "   where mail_image_id = " +SQLUtil.convertForSQL(this.getMailimgId());
         if(SystemInfo.getConnection().executeUpdate(sql) == 1){
                result = true;
         }
         return result;
    }

    /**
     * �摜�̃o�C�g�z����擾����B
     *
     * @param filePath �t�@�C���p�X
     * @return �摜�̃o�C�g�z��
     * @throws Exception Exception
     */
    private byte[] getImageData(String filePath) throws Exception {
        byte[] b = null;
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        FileImageOutputStream fios = new FileImageOutputStream(file);
        b = new byte[(int) file.length()];
        fios.readFully(b);
        fis.close();

        return b;
    }

    public String convertUrl(byte[] data, String f){
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/");
        sb.append(f).append(";base64,");
        sb.append(Base64.encodeBase64String(data));
        return sb.toString();
    }
    private String getSelectSQL() {
        return "select mail_image_id, image, format, size\n"
                + "from  data_mail_image\n"
                + "where delete_date is null";
    }
}
