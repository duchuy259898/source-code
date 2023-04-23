/*
 * MstReceiptSetting.java
 *
 * Created on 2007/10/23, 15:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.imageio.stream.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class MstReceiptSetting
{
    private MstShop	shop			= null;
    private String	printerName		= "";
    private String	logoFilePath		= "";
    private ImageIcon	logoImage		= null;
    private Boolean	deleteLogo		= false;
    private String	message			= "";
    private String	footerImageFilePath	= "";
    private ImageIcon	footerImage		= null;
    private Boolean	deleteFooter		= false;
    private Boolean	printReceipt		= false;
    private Integer	receiptSize		= 0;
    private Boolean	printCounselingSheet	= false;
    private String	counselingMenu1		= "";
    private String	counselingMenu2		= "";
    private String	counselingMenu3		= "";
    private String	counselingMenu4		= "";
    private Boolean	printNextInfo		= true;
	
    /** Creates a new instance of MstReceiptSetting */
    public MstReceiptSetting()
    {
    }

    public MstShop getShop()
    {
            return shop;
    }

    public void setShop(MstShop shop)
    {
            this.shop = shop;
    }

    public String getPrinterName()
    {
            return printerName;
    }

    public void setPrinterName(String printerName)
    {
            this.printerName = printerName;
    }

    public String getLogoFilePath()
    {
            return logoFilePath;
    }

    public void setLogoFilePath(String logoFilePath)
    {
            this.logoFilePath = logoFilePath;
    }

    public ImageIcon getLogoImage()
    {
            return logoImage;
    }

    public void setLogoImage(ImageIcon logoImage)
    {
            this.logoImage = logoImage;
    }

    public Boolean isDeleteLogo()
    {
            return deleteLogo;
    }

    public void setDeleteLogo(Boolean deleteLogo)
    {
            this.deleteLogo = deleteLogo;
    }

    public String getMessage()
    {
            return message;
    }

    public void setMessage(String message)
    {
            this.message = message;
    }

    public String getFooterImageFilePath()
    {
            return footerImageFilePath;
    }

    public void setFooterImageFilePath(String footerImageFilePath)
    {
            this.footerImageFilePath = footerImageFilePath;
    }

    public ImageIcon getFooterImage()
    {
            return footerImage;
    }

    public void setFooterImage(ImageIcon footerImage)
    {
            this.footerImage = footerImage;
    }

    public Boolean isDeleteFooter()
    {
            return deleteFooter;
    }

    public void setDeleteFooter(Boolean deleteFooter)
    {
            this.deleteFooter = deleteFooter;
    }

    public Boolean isPrintReceipt()
    {
            return printReceipt;
    }

    public void setPrintReceipt(Boolean printReceipt)
    {
            this.printReceipt = printReceipt;
    }

    public Integer getReceiptSize()
    {
            return receiptSize;
    }

    public void setReceiptSize(Integer receiptSize)
    {
            this.receiptSize = receiptSize;
    }

    public Boolean isPrintCounselingSheet()
    {
            return printCounselingSheet;
    }

    public void setPrintCounselingSheet(Boolean printCounselingSheet)
    {
            this.printCounselingSheet = printCounselingSheet;
    }

    /**
     * @return the counselingMenu1
     */
    public String getCounselingMenu1() {
        return counselingMenu1;
    }

    /**
     * @param counselingMenu1 the counselingMenu1 to set
     */
    public void setCounselingMenu1(String counselingMenu1) {
        this.counselingMenu1 = counselingMenu1;
    }

    /**
     * @return the counselingMenu2
     */
    public String getCounselingMenu2() {
        return counselingMenu2;
    }

    /**
     * @param counselingMenu2 the counselingMenu2 to set
     */
    public void setCounselingMenu2(String counselingMenu2) {
        this.counselingMenu2 = counselingMenu2;
    }

    /**
     * @return the counselingMenu3
     */
    public String getCounselingMenu3() {
        return counselingMenu3;
    }

    /**
     * @param counselingMenu3 the counselingMenu3 to set
     */
    public void setCounselingMenu3(String counselingMenu3) {
        this.counselingMenu3 = counselingMenu3;
    }

    /**
     * @return the counselingMenu4
     */
    public String getCounselingMenu4() {
        return counselingMenu4;
    }

    /**
     * @param counselingMenu4 the counselingMenu4 to set
     */
    public void setCounselingMenu4(String counselingMenu4) {
        this.counselingMenu4 = counselingMenu4;
    }

    /**
     * @return the printNextInfo
     */
    public Boolean isPrintNextInfo() {
        return printNextInfo;
    }

    /**
     * @param printNextInfo the printNextInfo to set
     */
    public void setPrintNextInfo(Boolean printNextInfo) {
        this.printNextInfo = printNextInfo;
    }

    public void load(ConnectionWrapper con) throws SQLException
    {
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            
            this.setPrinterName(rs.getString("printer_name"));

            byte[] imgBytes = rs.getBytes("logo_image");

            if (imgBytes == null) {
                this.setLogoImage(null);
            } else {
                this.setLogoImage(new ImageIcon(imgBytes));
            }

            this.setMessage(rs.getString("message"));

            imgBytes = rs.getBytes("footer_image");
            
            if (imgBytes == null) {
                this.setFooterImage(null);
            } else {
                this.setFooterImage(new ImageIcon(imgBytes));
            }

            this.setPrintReceipt(rs.getBoolean("print_receipt"));

            this.setReceiptSize(rs.getInt("receipt_size"));

            this.setPrintCounselingSheet(rs.getBoolean("print_counseling_sheet"));

            this.setCounselingMenu1(rs.getString("counseling_menu1"));
            this.setCounselingMenu2(rs.getString("counseling_menu2"));
            this.setCounselingMenu3(rs.getString("counseling_menu3"));
            this.setCounselingMenu4(rs.getString("counseling_menu4"));

            this.setPrintNextInfo(rs.getBoolean("print_next_info"));

        } else {

            this.setCounselingMenu1("カット");
            this.setCounselingMenu2("パーマ");
            this.setCounselingMenu3("カラー");
            this.setCounselingMenu4("トリートメント");

        }

        rs.close();
    }
	
    /**
     * レシート設定マスタにデータが存在するかチェックする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException
    {
        if (this.getShop() == null || this.getShop().getShopID() == null) return false;

        if (con == null) return false;

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        return rs.next();
    }

    private String getSelectSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      shop_id");
        sql.append("     ,printer_name");
        sql.append("     ,logo_image");
        sql.append("     ,coalesce((");
        sql.append("         select");
        sql.append("             message");
        sql.append("         from");
        sql.append("             mst_receipt_template");
        sql.append("         where");
        sql.append("             shop_id = mst_receipt_setting.shop_id");
        sql.append("         and current_date between from_date and to_date");
        sql.append("         and delete_date is null");
        sql.append("         limit 1");
        sql.append("      ), message) as message");
        sql.append("     ,footer_image");
        sql.append("     ,print_receipt");
        sql.append("     ,receipt_size");
        sql.append("     ,print_counseling_sheet");
        sql.append("     ,counseling_menu1");
        sql.append("     ,counseling_menu2");
        sql.append("     ,counseling_menu3");
        sql.append("     ,counseling_menu4");
        sql.append("     ,print_next_info");
        sql.append(" from");
        sql.append("     mst_receipt_setting");
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));

        return sql.toString();
    }
	
    public boolean regist(ConnectionWrapper con) throws Exception
    {
        boolean result = false;

        String sql = "";
        PreparedStatement ps = null;

        if (this.isExists(con)) {
            
            sql = "update mst_receipt_setting\n" +
                  "set printer_name = ?,\n";

            if (this.isDeleteLogo()) {
                sql	+=	"logo_image = null,\n";
            } else {
                if (!this.getLogoFilePath().equals("")) {
                    sql	+=	"logo_image = ?,\n";
                }
            }

            sql += "message = ?,\n";

            if (this.isDeleteFooter()) {
                sql += "footer_image = null,\n";
            } else {
                if (!this.getFooterImageFilePath().equals("")) {
                    sql += "footer_image = ?,\n";
                }
            }

            sql	+=	"print_receipt = ?,\n";

            sql	+=	"receipt_size = ?,\n";

            sql	+=	"print_counseling_sheet = ?,\n";

            sql += "counseling_menu1 = ?,\n";
            sql += "counseling_menu2 = ?,\n";
            sql += "counseling_menu3 = ?,\n";
            sql += "counseling_menu4 = ?,\n";

            sql	+= "print_next_info = ?\n";

            sql	+=	"where shop_id = ?\n";

            ps	=	con.prepareStatement(sql);

            int index = 1;

            ps.setString(index, this.getPrinterName());
            index++;

            if (!this.isDeleteLogo() && !this.getLogoFilePath().equals("")) {
                ps.setBytes(index, this.getImageData(this.getLogoFilePath()));
                index++;
            }

            ps.setString(index, this.getMessage());
            index++;

            if (!this.isDeleteFooter() && !this.getFooterImageFilePath().equals("")) {
                ps.setBytes(index, this.getImageData(this.getFooterImageFilePath()));
                index++;
            }

            ps.setBoolean(index, this.isPrintReceipt());
            index++;

            ps.setInt(index, this.getReceiptSize());
            index++;

            ps.setBoolean(index, this.isPrintCounselingSheet());
            index++;

            ps.setString(index, this.getCounselingMenu1());
            index++;
            ps.setString(index, this.getCounselingMenu2());
            index++;
            ps.setString(index, this.getCounselingMenu3());
            index++;
            ps.setString(index, this.getCounselingMenu4());
            index++;

            ps.setBoolean(index, this.isPrintNextInfo());
            index++;

            ps.setInt(index, this.getShop().getShopID());
            
        } else {
            sql	=	"insert into mst_receipt_setting\n" +
                            "(shop_id, printer_name, logo_image,\n" +
                            "message, footer_image, print_receipt, receipt_size, print_counseling_sheet,counseling_menu1,counseling_menu2,counseling_menu3,counseling_menu4, print_next_info)\n" +
                            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";

            ps	=	con.prepareStatement(sql);

            ps.setInt(1, this.getShop().getShopID());

            ps.setString(2, this.getPrinterName());

            if (this.isDeleteLogo() || this.getLogoFilePath().equals("")) {
                ps.setBytes(3, null);
            } else {
                ps.setBytes(3, this.getImageData(this.getLogoFilePath()));
            }

            ps.setString(4, this.getMessage());

            if (this.isDeleteLogo() || this.getFooterImageFilePath().equals("")) {
                ps.setBytes(5, null);
            } else {
                ps.setBytes(5, this.getImageData(this.getFooterImageFilePath()));
            }

            ps.setBoolean(6, this.isPrintReceipt());

            ps.setInt(7, this.getReceiptSize());

            ps.setBoolean(8, this.isPrintCounselingSheet());

            ps.setString(9, this.getCounselingMenu1());
            ps.setString(10, this.getCounselingMenu2());
            ps.setString(11, this.getCounselingMenu3());
            ps.setString(12, this.getCounselingMenu4());

            ps.setBoolean(13, this.isPrintNextInfo());
        }

        if (ps.executeUpdate() == 1) {
            result = true;
        }

        ps.close();

        return result;
    }

    private byte[] getImageData(String filePath) throws Exception
    {
        byte[]	b = null;
        File	file = new File(filePath);
        FileInputStream	fis = new FileInputStream(file);
        FileImageOutputStream fios = new FileImageOutputStream(file);
        b = new byte[(int)file.length()];
        fios.readFully(b);
        fis.close();

        return b;
    }

}
