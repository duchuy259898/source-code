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
     * 画像ID
     */
    protected Integer mailimgId = null;
    /**
     * 画像ファイルパス
     */
    protected String imageFilePath = "";
    /**
     * 画像
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
     * 画像ファイルパスを取得する。
     *
     * @return 画像ファイルパス
     */
    public String getImageFilePath() {
        return imageFilePath;
    }

    /**
     * 画像ファイルパスをセットする。
     *
     * @param imageFilePath 画像ファイルパス
     */
    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    /**
     * 画像を取得する。
     *
     * @return 画像
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * 画像をセットする。
     *
     * @param image 画像
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
     * 新しい画像IDをセットする。
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
     * データを読み込む。
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
     * 新しい画像IDを取得するＳＱＬ文を取得する。
     *
     * @return 新しい画像IDを取得するＳＱＬ文
     */
    private String getNewImageIdSQL() {
        return "select coalesce(max(mail_image_id), 0) + 1 as new_image_id\n"
                + "from data_mail_image \n";
    }

    /**
     * 登録処理を行う。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
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
     * 画像データの登録処理を行う。
     *
     * @param con ConnectionWrapper
     * @param isExists データが存在するかどうか
     * @return true - 成功
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
     * 画像のバイト配列を取得する。
     *
     * @param filePath ファイルパス
     * @return 画像のバイト配列
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
