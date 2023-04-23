/*
 * MstUseProduct.java
 *
 * Created on 2007/01/18, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author katagiri
 */
public class MstUseProduct
{
	private	MstShop		shop				=	new MstShop();
	private Integer		productDivision		=	null;
	private	Integer		productID			=	null;
	/** �Ɩ��p�K���݌� */
	private Integer		useProperStock		=	null;
	/** �X�̗p�K���݌� */
	private	Integer		sellProperStock		=	null;
	/** ���i */
	private	Long		price			=	null;
        //IVS_TMTrong start add 2015/10/20 New request #43515
        private Integer Display_seq=0;
         //IVS_TMTrong end add 2015/10/20 New request #43515
	/** Creates a new instance of MstUseProduct */
	public MstUseProduct()
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

	public Integer getShopID()
	{
		if(shop == null)
		{
			return	null;
		}

		return	shop.getShopID();
	}

	public Integer getProductDivision()
	{
		return productDivision;
	}

	public void setProductDivision(Integer productDivision)
	{
		this.productDivision = productDivision;
	}

	public Integer getProductID()
	{
		return productID;
	}

	public void setProductID(Integer productID)
	{
		this.productID = productID;
	}

	public Integer getUseProperStock()
	{
		return this.useProperStock;
	}

	public void setUseProperStock(Integer useProperStock)
	{
		this.useProperStock = useProperStock;
	}

	public Integer getSellProperStock()
	{
		return this.sellProperStock;
	}

	public void setSellProperStock(Integer sellProperStock)
	{
		this.sellProperStock = sellProperStock;
	}
        
	/**
	 * ���i���擾����B
	 * @return ���i
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * ���i��ݒ肷��B
	 * @param price ���i
	 */
	public void setPrice(Long price)
	{
		this.price = price;
	}
        
        //IVS_TMTrong start add 2015/10/20 New request #43515
        /**
        * @return the Display_seq
        */
        public Integer getDisplay_seq() {
            return Display_seq;
        }

        /**
         * @param Display_seq the Display_seq to set
         */
        public void setDisplay_seq(Integer Display_seq) {
            this.Display_seq = Display_seq;
        }
       //IVS_TMTrong end add 2015/10/20 New request #43515
       
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setShop(new MstShop());
		this.setProductDivision(null);
		this.setProductID(null);
		this.setUseProperStock(null);
		this.setSellProperStock(null);
		this.setPrice(null);
                //IVS_TMTrong start add 2015/10/20 New request #43515
                this.setDisplay_seq(0);
                //IVS_TMTrong end add 2015/10/20 New request #43515
	}

	/**
	 * �}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null ||
				this.getProductDivision() == null ||
				this.getProductID() == null ||
				con == null)
		{
			return	false;
		}

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null ||
				this.getProductDivision() == null ||
				this.getProductID() == null ||
				con == null)
		{
			return	false;
		}

		if(this.isExists(con))
		{
                    //IVS_TMTrong start add 2015/10/20 New request #43515
                    //return	true;
                    return (con.executeUpdate(this.getUpdateSQL())==1);
                    //IVS_TMTrong end add 2015/10/20 New request #43515
		}
		else
		{
			return	(con.executeUpdate(this.getInsertSQL()) == 1);
		}
	}

	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_use_product\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + "\n" +
				"and	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n";
	}

	private String getInsertSQL()
	{
		return	"insert into mst_use_product\n" +
				"(shop_id, product_division, product_id,\n" +
				"insert_date, update_date, delete_date,\n" +
                                //IVS_TMTrong start add 2015/10/20 New request #43515
				"use_proper_stock, sell_proper_stock, price, display_seq)\n" +
                                //IVS_TMTrong end add 2015/10/20 New request #43515
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductDivision()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductID()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null,\n" +
				SQLUtil.convertForSQL(this.getUseProperStock()) + ",\n" +
				SQLUtil.convertForSQL(this.getSellProperStock()) + ",\n" +
				SQLUtil.convertForSQL(this.getPrice()) + ",\n" +
                                //IVS_TMTrong start add 2015/10/20 New request #43515
                                SQLUtil.convertForSQL(this.getDisplay_seq()) + ")\n";
                                //IVS_TMTrong end add 2015/10/20 New request #43515
	}

	private String getDeleteSQL()
	{
		return	"delete from mst_use_product\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + "\n" +
				"and	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n";
	}
        
    //IVS_TMTrong start add 2015/10/20 New request #43515
    private String getUpdateSQL() {
        StringBuilder updateSQL = new StringBuilder();
        updateSQL.append(" update mst_use_product ");
        updateSQL.append(" set display_seq ="+this.getDisplay_seq());
        updateSQL.append(" ,price ="+this.getPrice());
        updateSQL.append(" where shop_id ="+this.getShopID());
        updateSQL.append(" and product_division ="+this.getProductDivision());
        updateSQL.append(" and product_id ="+this.getProductID());  
        return updateSQL.toString();
    }
        
    public boolean isExist_Technic_UseProduct(ConnectionWrapper con, Integer shopIDEnter, Integer productDivisionEnter, Integer productIDEnter) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectIsExist_Technic_UseProduct(shopIDEnter, productDivisionEnter, productIDEnter));

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
        
    public String getSelectIsExist_Technic_UseProduct(Integer shopIDEnter, Integer productDivisionEnter, Integer productIDEnter){
        StringBuilder isExistQuery = new StringBuilder();
        isExistQuery.append(" select * ");
        isExistQuery.append(" from mst_technic a inner join mst_use_product b on a.technic_id = b.product_id ");
        isExistQuery.append(" and b.shop_id ="+shopIDEnter);
        isExistQuery.append(" and b.product_division ="+productDivisionEnter);
        isExistQuery.append(" and b.product_id ="+productIDEnter);
        isExistQuery.append(" and a.technic_id ="+productIDEnter);

        return isExistQuery.toString();
    }
    
     public boolean isExist_Item_UseProduct(ConnectionWrapper con, Integer shopIDEnter, Integer productDivisionEnter, Integer productIDEnter) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectIsExist_Item_UseProduct(shopIDEnter, productDivisionEnter, productIDEnter));

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
        
    public String getSelectIsExist_Item_UseProduct(Integer shopIDEnter, Integer productDivisionEnter, Integer productIDEnter){
        StringBuilder isExistQuery = new StringBuilder();
        isExistQuery.append(" select * ");
        isExistQuery.append(" from mst_item a inner join mst_use_product b on a.item_id = b.product_id ");
        isExistQuery.append(" and b.shop_id ="+shopIDEnter);
        isExistQuery.append(" and b.product_division ="+productDivisionEnter);
        isExistQuery.append(" and b.product_id ="+productIDEnter);
        isExistQuery.append(" and a.item_id ="+productIDEnter);

        return isExistQuery.toString();
    }
    
     public boolean isExist_Course_UseProduct(ConnectionWrapper con, Integer shopIDEnter, Integer productDivisionEnter, Integer productIDEnter) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectIsExist_Course_UseProduct(shopIDEnter, productDivisionEnter, productIDEnter));

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
        
    public String getSelectIsExist_Course_UseProduct(Integer shopIDEnter, Integer productDivisionEnter, Integer productIDEnter){
        StringBuilder isExistQuery = new StringBuilder();
        isExistQuery.append(" select * ");
        isExistQuery.append(" from mst_course a inner join mst_use_product b on a.course_id = b.product_id ");
        isExistQuery.append(" and b.shop_id ="+shopIDEnter);
        isExistQuery.append(" and b.product_division ="+productDivisionEnter);
        isExistQuery.append(" and b.product_id ="+productIDEnter);
        isExistQuery.append(" and a.course_id ="+productIDEnter);

        return isExistQuery.toString();
    }
    //IVS_TMTrong end add 2015/10/20 New request #43515

    //�}�X�^�ꊇ�o�^ add start 2017/01/07
    /**
     * �X�܎g�p�Z�p�E���i�}�X�^�̈ꊇ�o�^
     * @param con �R�l�N�V����
     * @return true:���� / false:�ُ�
     * @throws SQLException 
     */
    public boolean registForBulk(ConnectionWrapper con) throws SQLException {
        if(this.getShopID() == null || this.getProductDivision() == null || this.getProductID() == null || con == null) {
            return false;
        }
        return (con.executeUpdate(this.getInsertSQLForBulk()) == 1);
    }
    
    /**
     * �X�܎g�p�Z�p�E���i�}�X�^�̈ꊇ�o�^�pSQL����Ԃ�
     * @return �ꊇ�o�^�pSQL��
     */
    private String getInsertSQLForBulk() {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into mst_use_product\n");
        sql.append("(\n");
        sql.append("shop_id,\n");
        sql.append("product_division,\n");
        sql.append("product_id,\n");
        sql.append("insert_date,\n");
        sql.append("update_date,\n");
        sql.append("delete_date,\n");
        sql.append("use_proper_stock,\n");
        sql.append("sell_proper_stock,\n");
        sql.append("price,\n");
        sql.append("reserve_flg,\n");
        sql.append("color,\n");
        sql.append("display_seq\n");
        sql.append(") values(\n");
        sql.append(SQLUtil.convertForSQL(this.getShop().getShopID())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getProductDivision())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getProductID())).append(",\n");
        sql.append("current_timestamp,\n");
        sql.append("current_timestamp,\n");
        sql.append("null,\n");
        sql.append("null,\n");
        sql.append("null,\n");
        sql.append(SQLUtil.convertForSQL(this.getPrice())).append(",\n");
        sql.append("0,\n");
        sql.append("null,\n");
        sql.append("(select coalesce(max(display_seq), 0) + 1 from mst_use_product) \n");
        sql.append(")");

        return sql.toString();
    }
    //�}�X�^�ꊇ�o�^ add end 2017/01/07
}
