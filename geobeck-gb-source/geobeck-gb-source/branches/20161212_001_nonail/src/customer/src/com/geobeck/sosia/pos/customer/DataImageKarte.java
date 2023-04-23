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
	 * �ڋq�f�[�^
	 */
	protected	MstCustomer		customer	=	new MstCustomer();
	/**
	 * �摜NO
	 */
	protected	Integer			imageNo		=	null;
	/**
	 * �摜ID
	 */
	protected	Integer			imageId		=	null;
	/**
	 * �摜�t�@�C���p�X
	 */
	protected	String			imageFilePath	=	"";
	/**
	 * �摜
	 */
	protected	ImageIcon		image		=	null;
	/**
	 * �摜�폜�t���O
	 */
	protected	Boolean			deleteImage	=	false;
        /**	
         * ����
         */
        protected       String                  comment         =       "";
	
	/**
     * Creates a new instance of DataImageKarte
     */
	public DataImageKarte()
	{
	}

	/**
	 * �ڋq�f�[�^���擾����B
	 * @return �ڋq�f�[�^
	 */
	public MstCustomer getCustomer()
	{
		return customer;
	}

	/**
	 * �ڋq�f�[�^���Z�b�g����B
	 * @param customer �ڋq�f�[�^
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}

	/**
	 * �摜NO���擾����B
	 * @return �摜NO
	 */
	public Integer getImageNo()
	{
		return imageNo;
	}

	/**
	 * �摜NO���Z�b�g����B
	 * @param imageNo �摜NO
	 */
	public void setImageNo(Integer imageNo)
	{
		this.imageNo = imageNo;
	}

	/**
	 * �摜ID���擾����B
	 * @return �摜ID
	 */
	public Integer getImageId()
	{
		return imageId;
	}

	/**
	 * �摜ID���Z�b�g����B
	 * @param imageId �摜ID
	 */
	public void setImageId(Integer imageId)
	{
		this.imageId = imageId;
	}

	/**
	 * �摜�t�@�C���p�X���擾����B
	 * @return �摜�t�@�C���p�X
	 */
	public String getImageFilePath()
	{
		return imageFilePath;
	}

	/**
	 * �摜�t�@�C���p�X���Z�b�g����B
	 * @param imageFilePath �摜�t�@�C���p�X
	 */
	public void setImageFilePath(String imageFilePath)
	{
		this.imageFilePath = imageFilePath;
	}

	/**
	 * �摜���擾����B
	 * @return �摜
	 */
	public ImageIcon getImage()
	{
		return image;
	}

	/**
	 * �摜���Z�b�g����B
	 * @param image �摜
	 */
	public void setImage(ImageIcon image)
	{
		this.image = image;
	}

	/**
	 * �摜�폜�t���O���擾����B
	 * @return �摜�폜�t���O
	 */
	public Boolean isDeleteImage()
	{
		return deleteImage;
	}

	/**
	 * �摜�폜�t���O���Z�b�g����B
	 * @param deleteImage �摜�폜�t���O
	 */
	public void setDeleteImage(Boolean deleteImage)
	{
		this.deleteImage = deleteImage;
	}
        
	/**
	 * �������擾����B
	 * @return ����
	 */
        public String getComment(){
            return this.comment;
        }

	/**
	 * �������Z�b�g����B
	 * @param comment ����
	 */
        public void setComment(String comment){
            this.comment = comment ;
        }
	
	/**
	 * �V�����摜ID���Z�b�g����B
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
	 * �f�[�^��ǂݍ��ށB
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
	 * �f�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
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
	 * �V�����摜ID���擾����r�p�k�����擾����B
	 * @return �V�����摜ID���擾����r�p�k��
	 */
	private String getNewImageIdSQL()
	{
		return	"select coalesce(max(image_id), 0) + 1 as new_image_id\n" +
				"from data_image\n";
	}
	
	/**
	 * �f�[�^���擾����r�p�k�����擾����B
	 * @return �f�[�^���擾����r�p�k��
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
	 * �o�^�������s���B
	 * @param con ConnectionWrapper
	 * @return true - ����
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
	 * �摜�J���e�f�[�^�̓o�^�������s���B
	 * @param con ConnectionWrapper
	 * @param isExists �f�[�^�����݂��邩�ǂ���
	 * @return true - ����
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
	 * �摜�f�[�^�̓o�^�������s���B
	 * @param con ConnectionWrapper
	 * @param isExists �f�[�^�����݂��邩�ǂ���
	 * @return true - ����
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
	 * �摜�̃o�C�g�z����擾����B
	 * @param filePath �t�@�C���p�X
	 * @return �摜�̃o�C�g�z��
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
