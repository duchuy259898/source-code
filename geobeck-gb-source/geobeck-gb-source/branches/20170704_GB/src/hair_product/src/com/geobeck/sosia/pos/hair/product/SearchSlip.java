/*
 * SearchSlip.java
 *
 * Created on 2008/09/19, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author s_matsumura
 */
public class SearchSlip extends ArrayList<SlipData> {
	enum SlipType {	ORDER, STORE, SHIP };

	protected MstShop				shop		=	new MstShop();
    
    /**
	 * ‘ÎÛ
	 */
	protected	SlipType             slipType		=	null;
    
    /**
	 * “ú•t
	 */
	protected	java.util.Date[]	searchDate	=	{	null, null	};
    
    /**
	 * “`•[No.
	 */
	protected	Integer[]			slipNO		=	{	null, null	};
    
    /**
	 * d“üæ
	 */
	protected	MstSupplier			supplier		=	new MstSupplier();
    
    
    
    /** Creates a new instance of SearchSlip */
    public SearchSlip() {
    }
    
    //“X•Ü
    public MstShop getShop()
	{
		return shop;
	}

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
    
    //‘ÎÛ‰æ–Ê
    public SlipType getSlipType()
	{
		return slipType;
	}

    public void setSlipType(SlipType slipType)
	{
		this.slipType = slipType;
	}
        
     /**
	 * “ú•t
	 * @return “ú•t
	 */
	public java.util.Date[] getSearchDate()
	{
		return searchDate;
	}

	/**
	 * “ú•t
	 * @param SearchDate “ú•t
	 */
	public void setSearchDate(java.util.Date[] searchDate)
	{
		this.searchDate = searchDate;
	}

	/**
	 * “ú•t
	 * @param index ƒCƒ“ƒfƒbƒNƒX
	 * @return “ú•t
	 */
	public java.util.Date getSearchDate(int index)
	{
		return searchDate[index];
	}

	/**
	 * “ú•t
	 * @param index ƒCƒ“ƒfƒbƒNƒX
	 * @param SearchDate “ú•t
	 */
	public void setSearchDate(int index, Date searchDate)
	{
		this.searchDate[index] = searchDate;
	}
    
    /**
	 * “`•[No.
	 * @return “`•[No.
	 */
	public Integer[] getSlipNO()
	{
		return slipNO;
	}

	/**
	 * “`•[No.
	 * @param slipNO “`•[No.
	 */
	public void setSlipNO(Integer[] slipNO)
	{
		this.slipNO = slipNO;
	}

	/**
	 * “`•[No.
	 * @param index ƒCƒ“ƒfƒbƒNƒX
	 * @return “`•[No.
	 */
	public Integer getSlipNO(int index)
	{
		return slipNO[index];
	}

	/**
	 * “`•[No.
	 * @param index ƒCƒ“ƒfƒbƒNƒX
	 * @param slipNO “`•[No.
	 */
	public void setSlipNO(int index, Integer slipNO)
	{
		this.slipNO[index] = slipNO;
	}
    
    /**
	 * d“üæ
	 * @return d“üæ
	 */
	public MstSupplier getSupplier()
	{
		return supplier;
	}

	/**
	 * d“üæ
	 * @param supplier d“üæ
	 */
	public void setSupplier(MstSupplier supplier)
	{
		this.supplier = supplier;
	}
    
    
    /**
	 * “`•[ƒf[ƒ^‚ğ“Ç‚İ‚ŞB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();

        String strSQL = "";
		switch (slipType)
		{
			case ORDER:
	            strSQL = this.getSelectOrderSQL();
				break;
			case STORE:
	            strSQL = this.getSelectStoreSQL();
				break;
			case SHIP:
				strSQL = this.getSelectShipSQL();
				break;
			default:
				throw new IllegalStateException("slipType is illegal.");
		}

        ResultSetWrapper rs = con.executeQuery(strSQL);
        while(rs.next())
        {
            SlipData sd = new SlipData();
            //IVS_LVTu start edit 2015/10/07 New request #43148
            sd.setData(rs, slipType);
            //IVS_LVTu end edit 2015/10/07 New request #43148
            this.add(sd);
        }               

		rs.close();
	}
    
    /**
	 * ”­’‘‚ğæ“¾‚·‚éSQL
	 * @return ‚r‚p‚k•¶
	 */
	private String getSelectOrderSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" SELECT \n");
            sql.append("     data_slip_order.shop_id \n");
            sql.append("    ,data_slip_order.order_date as slip_date \n");
            sql.append("    ,data_slip_order.slip_no \n");
            sql.append("    ,mst_supplier.supplier_name \n");
            sql.append("    ,mst_supplier.supplier_staff \n");
            sql.append("    ,mst_staff.staff_name1 \n");
            sql.append("    ,mst_staff.staff_name2 \n");
            sql.append(" FROM \n");
            sql.append("     data_slip_order \n");
            sql.append("    ,mst_supplier \n");
            sql.append("    ,mst_staff \n");
            sql.append(" WHERE \n");
            sql.append("     data_slip_order.supplier_id = mst_supplier.supplier_id \n");
            sql.append(" AND \n");
            sql.append("     data_slip_order.staff_id = mst_staff.staff_id \n");
            sql.append(" AND \n");
            sql.append("     data_slip_order.delete_date is null \n");
            sql.append(" AND \n");
            sql.append("     data_slip_order.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + "\n");
            if(searchDate[0] != null){
                sql.append(" AND \n");
                sql.append(SQLUtil.convertForSQLDateOnly(searchDate[0]) + " <= data_slip_order.order_date \n"); 
            }
            if(searchDate[1] != null){
                sql.append(" AND \n");
                sql.append(" data_slip_order.order_date <= " + SQLUtil.convertForSQLDateOnly(searchDate[1]) + "\n");
            }
            if(slipNO[0] != null){
                sql.append(" AND \n");
                sql.append(SQLUtil.convertForSQL(slipNO[0]) + " <= data_slip_order.slip_no \n"); 
            }
            if(slipNO[1] != null){
                sql.append(" AND \n");
                sql.append(" data_slip_order.slip_no <= " + SQLUtil.convertForSQL(slipNO[1]) + "\n");
            }
            if(supplier.getSupplierID() != null){
                sql.append(" AND \n");
                sql.append(" data_slip_order.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + "\n");
            }
            
            sql.append("order by data_slip_order.shop_id, data_slip_order.slip_no");

            return sql.toString();
	}
    
    //IVS_LVTu start edit 2015/10/07 New request #43148
    //“üŒÉ“`•[
    private String getSelectStoreSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" SELECT ");
            sql.append("     data_slip_store.shop_id \n");
            sql.append("    ,data_slip_store.store_date as slip_date \n");
            sql.append("    ,data_slip_store.slip_no \n");
            sql.append("    ,mst_supplier.supplier_name \n");
            sql.append("    ,mst_supplier.supplier_staff \n");
            sql.append("    ,mst_staff.staff_name1 \n");
            sql.append("    ,mst_staff.staff_name2 \n");
            sql.append("    , sum ( case when data_slip_store_detail.item_use_division = 1 then data_slip_store_detail.in_num else 0 end ) as item_num_division1 \n");
            sql.append("    , sum ( case when data_slip_store_detail.item_use_division = 2 then data_slip_store_detail.in_num else 0 end ) as item_num_division2 \n");
            sql.append("    , sum ( coalesce( data_slip_store_detail.in_num*data_slip_store_detail.cost_price,  0  )) as cost_price_total \n");
            sql.append(" FROM \n");
            sql.append("     data_slip_store \n");
            sql.append("    ,mst_supplier \n");
            sql.append("    ,mst_staff \n");
            sql.append("    ,data_slip_store_detail \n");
            sql.append(" WHERE \n");
            sql.append("     data_slip_store.supplier_id = mst_supplier.supplier_id \n");
            sql.append(" AND \n");
            sql.append("     data_slip_store.staff_id = mst_staff.staff_id \n");
            sql.append(" AND \n");
            sql.append("     data_slip_store.delete_date is null \n");
            sql.append(" AND \n");
            sql.append("     data_slip_store.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + "\n");
            if(searchDate[0] != null){
                sql.append(" AND \n");
                sql.append(SQLUtil.convertForSQLDateOnly(searchDate[0]) + " <= data_slip_store.store_date \n"); 
            }
            if(searchDate[1] != null){
                sql.append(" AND \n");
                sql.append(" data_slip_store.store_date <= " + SQLUtil.convertForSQLDateOnly(searchDate[1]) + "\n");
            }
            if(slipNO[0] != null){
                sql.append(" AND \n");
                sql.append(SQLUtil.convertForSQL(slipNO[0]) + " <= data_slip_store.slip_no \n"); 
            }
            if(slipNO[1] != null){
                sql.append(" AND \n");
                sql.append(" data_slip_store.slip_no <= " + SQLUtil.convertForSQL(slipNO[1]) + "\n");
            }
            if(supplier.getSupplierID() != null){
                sql.append(" AND \n");
                sql.append(" data_slip_store.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + "\n");
            }
            
            sql.append("AND \n");
            sql.append("    ship_slip_no is null\n");
            sql.append("AND \n");
            sql.append("    not exists (select data_slip_store.slip_no from data_move_stock where dest_shop_id = data_slip_store.shop_id AND dest_slip_no = data_slip_store.slip_no AND delete_date is null)\n");
            sql.append("    and data_slip_store.shop_id = data_slip_store_detail.shop_id \n");
            sql.append("    and data_slip_store.slip_no = data_slip_store_detail.slip_no \n");

            sql.append("    group by data_slip_store.shop_id, \n");
            sql.append("           data_slip_store.store_date, \n");
            sql.append("           data_slip_store.slip_no, \n");
            sql.append("           mst_supplier.supplier_name, \n");
            sql.append("           mst_supplier.supplier_staff, \n");
            sql.append("           mst_staff.staff_name1, \n");
            sql.append("           mst_staff.staff_name2 \n");
            sql.append("order by data_slip_store.shop_id, data_slip_store.slip_no");

            return sql.toString();
	}
    
    //oŒÉ“`•[
    private String getSelectShipSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" SELECT ");
            sql.append("     data_slip_ship.shop_id \n");
            sql.append("    ,data_slip_ship.ship_date as slip_date \n");
            sql.append("    ,data_slip_ship.slip_no \n");
            sql.append("    ,mst_supplier.supplier_name \n");
            sql.append("    ,mst_supplier.supplier_staff \n");
            sql.append("    ,mst_staff.staff_name1 \n");
            sql.append("    ,mst_staff.staff_name2 \n");
            sql.append("    , sum ( case when data_slip_ship_detail.item_use_division = 1 then data_slip_ship_detail.out_num else 0 end ) as item_num_division1 \n");
            sql.append("    , sum ( case when data_slip_ship_detail.item_use_division = 2 then data_slip_ship_detail.out_num else 0 end ) as item_num_division2 \n");
            sql.append(" FROM \n");
            sql.append("     data_slip_ship \n");
            sql.append("    ,mst_supplier \n");
            sql.append("    ,mst_staff \n");
            sql.append("    ,data_slip_ship_detail \n");
            sql.append(" WHERE \n");
            sql.append("     data_slip_ship.supplier_id = mst_supplier.supplier_id \n");
            sql.append(" AND \n");
            sql.append("     data_slip_ship.staff_id = mst_staff.staff_id \n");
            sql.append(" AND \n");
            sql.append("     data_slip_ship.delete_date is null \n");
            sql.append(" AND \n");
            sql.append("     data_slip_ship.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + "\n");
            
            if(searchDate[0] != null){
                sql.append(" AND \n");
                sql.append(SQLUtil.convertForSQLDateOnly(searchDate[0]) + " <= data_slip_ship.ship_date \n"); 
            }
            if(searchDate[1] != null){
                sql.append(" AND \n");
                sql.append(" data_slip_ship.ship_date <= " + SQLUtil.convertForSQLDateOnly(searchDate[1]) + "\n");
            }
            if(slipNO[0] != null){
                sql.append(" AND \n");
                sql.append(SQLUtil.convertForSQL(slipNO[0]) + " <= data_slip_ship.slip_no \n"); 
            }
            if(slipNO[1] != null){
                sql.append(" AND \n");
                sql.append(" data_slip_ship.slip_no <= " + SQLUtil.convertForSQL(slipNO[1]) + "\n");
            }
            if(supplier.getSupplierID() != null){
                sql.append(" AND \n");
                sql.append(" data_slip_ship.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + "\n");
            }
            
            sql.append("AND \n");
            sql.append("    not exists (select data_slip_ship.slip_no from data_move_stock where src_shop_id = data_slip_ship.shop_id AND src_slip_no = data_slip_ship.slip_no AND delete_date is null)\n");
            sql.append("    and data_slip_ship.shop_id = data_slip_ship_detail.shop_id \n");
            sql.append("    and data_slip_ship.slip_no = data_slip_ship_detail.slip_no \n");
            
            sql.append("    group by data_slip_ship.shop_id \n");
            sql.append("    ,data_slip_ship.ship_date \n");
            sql.append("    ,data_slip_ship.slip_no \n");
            sql.append("    ,mst_supplier.supplier_name \n");
            sql.append("    ,mst_supplier.supplier_staff \n");
            sql.append("    ,mst_staff.staff_name1 \n");
            sql.append("    ,mst_staff.staff_name2 \n");
            sql.append("order by data_slip_ship.shop_id, data_slip_ship.slip_no");

            return sql.toString();
	}
    //IVS_LVTu edit add 2015/10/07 New request #43148
}
