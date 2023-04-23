/*
 * DataImageKarte.java
 *
 * Created on 2008/09/14, 10:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import com.geobeck.sosia.pos.master.customer.MstCustomer;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.imageio.stream.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author saito
 */
public class DataImageKarte
{
	/**
	 * 顧客データ
	 */
	protected	MstCustomer		customer	=	new MstCustomer();
	/**
	 * 画像NO
	 */
	protected	Integer			imageNo		=	null;
	/**
	 * 画像ID
	 */
	protected	Integer			imageId		=	null;
	/**
	 * 画像ファイルパス
	 */
	protected	String			imageFilePath	=	"";
	/**
	 * 画像
	 */
	protected	ImageIcon		image		=	null;
	/**
	 * 画像削除フラグ
	 */
	protected	Boolean			deleteImage	=	false;
        /**	
         * メモ
         */
        protected       String                  comment         =       "";
	
	/**
     * Creates a new instance of DataImageKarte
     */
	public DataImageKarte()
	{
	}

	/**
	 * 顧客データを取得する。
	 * @return 顧客データ
	 */
	public MstCustomer getCustomer()
	{
		return customer;
	}

	/**
	 * 顧客データをセットする。
	 * @param customer 顧客データ
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}

	/**
	 * 画像NOを取得する。
	 * @return 画像NO
	 */
	public Integer getImageNo()
	{
		return imageNo;
	}

	/**
	 * 画像NOをセットする。
	 * @param imageNo 画像NO
	 */
	public void setImageNo(Integer imageNo)
	{
		this.imageNo = imageNo;
	}

	/**
	 * 画像IDを取得する。
	 * @return 画像ID
	 */
	public Integer getImageId()
	{
		return imageId;
	}

	/**
	 * 画像IDをセットする。
	 * @param imageId 画像ID
	 */
	public void setImageId(Integer imageId)
	{
		this.imageId = imageId;
	}

	/**
	 * 画像ファイルパスを取得する。
	 * @return 画像ファイルパス
	 */
	public String getImageFilePath()
	{
		return imageFilePath;
	}

	/**
	 * 画像ファイルパスをセットする。
	 * @param imageFilePath 画像ファイルパス
	 */
	public void setImageFilePath(String imageFilePath)
	{
		this.imageFilePath = imageFilePath;
	}

	/**
	 * 画像を取得する。
	 * @return 画像
	 */
	public ImageIcon getImage()
	{
		return image;
	}

	/**
	 * 画像をセットする。
	 * @param image 画像
	 */
	public void setImage(ImageIcon image)
	{
		this.image = image;
	}

	/**
	 * 画像削除フラグを取得する。
	 * @return 画像削除フラグ
	 */
	public Boolean isDeleteImage()
	{
		return deleteImage;
	}

	/**
	 * 画像削除フラグをセットする。
	 * @param deleteImage 画像削除フラグ
	 */
	public void setDeleteImage(Boolean deleteImage)
	{
		this.deleteImage = deleteImage;
	}
        
	/**
	 * メモを取得する。
	 * @return メモ
	 */
        public String getComment(){
            return this.comment;
        }

	/**
	 * メモをセットする。
	 * @param comment メモ
	 */
        public void setComment(String comment){
            this.comment = comment ;
        }
	
	/**
	 * 新しい画像IDをセットする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewImageId(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewImageIdSQL());
		
		if(rs.next())
		{
			this.setImageId(rs.getInt("new_image_id"));
		}
		
		rs.close();
	}

	/**
	 * データを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
                        this.setImageId(rs.getInt("image_id"));
                        this.setComment(rs.getString("comment"));
                        
			byte[]	imgBytes	=	rs.getBytes("image");
			
			if(imgBytes == null)
			{
				this.setImage(null);
			}
			else
			{
				this.setImage(new ImageIcon(imgBytes));
			}
		}

		rs.close();
	}
	
	/**
	 * データが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getImageId() == null || this.getImageNo() == null ||
                    this.getImageId() < 1 || this.getImageNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
        // start add lvut 20130903
        
        private boolean isExitsImage(ConnectionWrapper con) throws SQLException {
               if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getExitSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
        }
         private String getExitSelectSQL() {
          return "select  image_id \n "
                  + "from data_image \n"
                  + "where delete_date is null \n"
                  + "and image_id = " + SQLUtil.convertForSQL(this.getImageId());
        }
        // end add lvut 20130903
         
	/**
	 * 新しい画像IDを取得するＳＱＬ文を取得する。
	 * @return 新しい画像IDを取得するＳＱＬ文
	 */
	private String getNewImageIdSQL()
	{
		return	"select coalesce(max(image_id), 0) + 1 as new_image_id\n" +
				"from data_image\n";
	}
	
	/**
	 * データを取得するＳＱＬ文を取得する。
	 * @return データを取得するＳＱＬ文
	 */
	private String getSelectSQL()
	{
		return	"select dik.customer_id\n" +
                                ", dik.image_no\n" +
                                ", dik.image_id\n" +
                                ", dik.comment\n" +
                                ", di.image\n" +
				"from data_image_karte dik\n" +
				"left join data_image di\n" +
				"on di.image_id = dik.image_id\n" +
				"and di.delete_date is null\n" +
				"where dik.delete_date is null\n" +
				"and dik.customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + "\n" +
				"and image_no = " + SQLUtil.convertForSQL(this.getImageNo()) + "\n";
	}
	
	/**
	 * 登録処理を行う。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws Exception
	{
                boolean isExeists = this.isExists(con);

                if (!isExeists) {
                    setNewImageId(con);
                }
                
                if (!registDataImageKarte(con, isExeists)) {
                    return false;
                }
                // start add  20130903 lvut 
                
                isExeists = this.isExitsImage(con);
               
                // end add  20130903 lvut 
                if (!registDataImage(con, isExeists)) {
                    return false;
                }

                return true;
        }
	
	/**
	 * 画像カルテデータの登録処理を行う。
	 * @param con ConnectionWrapper
	 * @param isExists データが存在するかどうか
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registDataImageKarte(ConnectionWrapper con, boolean isExists) throws Exception
	{
		boolean	result	=	false;
		
		String	sql	=	"";

		if(isExists)
		{
                    sql	=	"update data_image_karte\n"
                            +	"set\n"
                            +	"image_id = " + SQLUtil.convertForSQL(this.getImageId()) + ",\n"
                            +	"comment = " + SQLUtil.convertForSQL(this.getComment()) + ",\n"
                            +	"update_date = current_timestamp\n"
                            +	"where	customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + "\n"
                            +	"and    image_no = " + SQLUtil.convertForSQL(this.getImageNo()) + "\n";
		}
		else
		{
                    sql	=	"insert into data_image_karte\n" +
                                    "(customer_id, image_no, image_id, comment,\n" +
                                    "insert_date, update_date, delete_date)\n" +
                                    "values(\n" +
                                    SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
                                    SQLUtil.convertForSQL(this.getImageNo()) + ",\n" +
                                    SQLUtil.convertForSQL(this.getImageId()) + ",\n" +
                                    SQLUtil.convertForSQL(this.getComment()) + ",\n" +
                                    "current_timestamp, current_timestamp, null)\n";
		}

                if(con.executeUpdate(sql) == 1){
			result	=	true;
                }
		
		return	result;
	}
	
	/**
	 * 画像データの登録処理を行う。
	 * @param con ConnectionWrapper
	 * @param isExists データが存在するかどうか
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registDataImage(ConnectionWrapper con, boolean isExists) throws Exception
	{
		boolean	result	=	false;
		
		String	sql	=	"";
		PreparedStatement	ps	=	null;

		if(isExists)
		{
                    sql	=	"update data_image\n" +
                                    "set\n" +
                                    "update_date = current_timestamp\n";

                    if(this.isDeleteImage())
                    {
                        sql	+=	",image = null\n";
                    }
                    else
                    {
                        if(!this.getImageFilePath().equals("")) {
                            sql	+=	",image = ?\n";
                        }
                    }

                    sql	+=	"where image_id = ?\n";

                    ps	=	con.prepareStatement(sql);

                    int		index	=	1;

                    if(!this.isDeleteImage() && !this.getImageFilePath().equals(""))
                    {
                            ps.setBytes(index++, this.getImageData(
                                            this.getImageFilePath()));
                    }

                    ps.setInt(index, this.getImageId());
		}
		else
		{
			sql	=	"insert into data_image\n" +
					"(image_id, image,\n" +
					"insert_date, update_date, delete_date)\n" +
					"values(?, ?, current_timestamp, current_timestamp, null)\n";

			ps	=	con.prepareStatement(sql);

                        int		index	=	1;

			ps.setInt(index++, this.getImageId());

			if(this.isDeleteImage() || this.getImageFilePath().equals(""))
			{
				ps.setBytes(index, null);
			}
			else
			{
				ps.setBytes(index, this.getImageData(
						this.getImageFilePath()));
			}
		}

		if(ps.executeUpdate() == 1)
		{
			result	=	true;
		}

		ps.close();
		
		return	result;
	}
	
	/**
	 * 画像のバイト配列を取得する。
	 * @param filePath ファイルパス
	 * @return 画像のバイト配列
	 * @throws Exception Exception
	 */
	private byte[] getImageData(String filePath) throws Exception
	{
		byte[]	b	=	null;
		File	file	=	new File(filePath);
		FileInputStream	fis		=	new FileInputStream(file);
		FileImageOutputStream	fios	=	new FileImageOutputStream(file);
		b	=	new byte[(int)file.length()];
		fios.readFully(b);
		fis.close();
		
		return	b;
	}

   

    

   
}
