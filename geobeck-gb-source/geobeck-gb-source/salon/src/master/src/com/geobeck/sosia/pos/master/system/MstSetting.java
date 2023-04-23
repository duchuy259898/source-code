/*
 * MstSetting.java
 *
 * Created on 2006/09/05, 9:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.system;

import java.sql.*;

import com.geobeck.sql.*;

/**
 *
 * @author katagiri
 */
public class MstSetting
{
	private boolean shareCustomer	= false;
	private boolean merchandise	= false;
	private boolean itoAnalysis	= false;
        private boolean transferProduct = false;
        private int     webcheck        = 0;
        private Date    rankStartDate   = null;
        private Date    rankEndDate     = null;
        private int     rankTaxType     = 0;
        private boolean usePrepaid = false;
        //IVS_vtnhan start add 20140828 Request #30271
        private boolean useShopCategory = false;
        //IVS_vtnhan start add 20140828 Request #30271
        //IVS start add 2020/04/01 å˚ç¿êUë÷òAåg
        private boolean useAccountTransfer = false;
        //IVS end add 2020/04/01 å˚ç¿êUë÷òAåg
	
	/**
	 * Creates a new instance of MstSetting
	 */
	public MstSetting()
	{
	}

	public boolean isShareCustomer()
	{
            return shareCustomer;
	}

	public void setShareCustomer(boolean shareCustomer)
	{
            this.shareCustomer = shareCustomer;
	}

	public boolean isMerchandise()
	{
            return merchandise;
	}

	public void setMerchandise(boolean merchandise)
	{
            this.merchandise = merchandise;
	}	
	
        public boolean isItoAnalysis() {
            
            return itoAnalysis;
        }

        public void setItoAnalysis(boolean itoAnalysis) {
            
            this.itoAnalysis = itoAnalysis;
        }

        public boolean isTransferProduct() {
            return transferProduct;
        }

        public void setTransferProduct(boolean transferProduct) {
            this.transferProduct = transferProduct;
        }

        public boolean isWebcheck() {
            return webcheck > 0;
        }

        public int getWebcheck() {
            return webcheck;
        }

        public void setWebcheck(int webcheck) {
            this.webcheck = webcheck;
        }

        public Date getRankStartDate() {
            return rankStartDate;
        }

        public void setRankStartDate(Date rankStartDate) {
            this.rankStartDate = rankStartDate;
        }

        public Date getRankEndDate() {
            return rankEndDate;
        }

        public void setRankEndDate(Date rankEndDate) {
            this.rankEndDate = rankEndDate;
        }

        public int getRankTaxType() {
            return rankTaxType;
        }

        public void setRankTaxType(int rankTaxType) {
            this.rankTaxType = rankTaxType;
        }

        public boolean isUsePrepaid() {
            return usePrepaid;
        }

        public void setUsePrepaid(boolean usePrepaid) {
            this.usePrepaid = usePrepaid;
        }
        
        //IVS_vtnhan start add 20140828 Request #30271
         public boolean isUseShopCategory() {
            return useShopCategory;
        }

        public void setUseShopCategory(boolean useShopCategory) {
            this.useShopCategory = useShopCategory;
        }
        //IVS_vtnhan start add 20140828 Request #30271
        //IVS start add 2020/04/01 å˚ç¿êUë÷òAåg
        public boolean isUseAccountTransfer() {
            return useAccountTransfer;
        }

        public void setUseAccountTransfer(boolean useAccountTransfer) {
            this.useAccountTransfer = useAccountTransfer;
        }
        //IVS end add 2020/04/01 å˚ç¿êUë÷òAåg

	public void setData(ResultSetWrapper rs) throws SQLException
	{
            this.setShareCustomer(rs.getBoolean("share_customer"));
            this.setMerchandise(rs.getBoolean("merchandise"));
            this.setItoAnalysis(rs.getBoolean("ito_analysis"));
            this.setTransferProduct(rs.getBoolean("transfer_product"));
            this.setWebcheck(rs.getInt("webcheck"));
            this.setRankStartDate(rs.getDate("rank_start_date"));
            this.setRankEndDate(rs.getDate("rank_end_date"));
            this.setRankTaxType(rs.getInt("rank_tax_type"));
            this.setUsePrepaid(rs.getBoolean("use_prepaid"));
            //IVS_vtnhan start add 20140828 Request #30271
            this.setUseShopCategory(rs.getBoolean("use_shop_category"));
            //IVS_vtnhan end add 20140828 Request #30271
            //IVS start add 2020/04/01 å˚ç¿êUë÷òAåg
            this.setUseAccountTransfer(rs.getBoolean("use_account_transfer"));
            //IVS end add 2020/04/01 å˚ç¿êUë÷òAåg
            
	}
	
	public void load(ConnectionWrapper con) throws SQLException
	{
            ResultSetWrapper rs = con.executeQuery("select * from mst_system");

            if (rs.next()) {
                this.setData(rs);
            }

            rs.close();
	}
        
        //nhanvt start add  20141009 Bug #30993
        public String getVersionSql(ConnectionWrapper con)throws SQLException{
            StringBuilder sqlVersion = new StringBuilder(100);
        
            sqlVersion.append(" select version() as ver ");
            ResultSetWrapper rsVer = new ResultSetWrapper();
            String ver = "";
            rsVer = con.executeQuery(sqlVersion.toString());
            while(rsVer.next())
            {
                ver = rsVer.getString("ver");
            }
            return ver;
        }
        //nhanvt end add  20141009 Bug #30993
  

}
