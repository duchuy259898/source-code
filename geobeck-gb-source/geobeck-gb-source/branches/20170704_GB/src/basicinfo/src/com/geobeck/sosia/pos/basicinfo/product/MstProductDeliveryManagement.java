/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.data.account.DataSalesDetail;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 *
 * @author nakhoa
 */
public class MstProductDeliveryManagement {
    
    protected   DataSalesDetail         dataSalesDetail     =   new DataSalesDetail();
    
    protected   String                  productName         =   "";
    
    protected   Integer                 productNumP         =   null;
    
    protected   Integer                 productNumUse       =   null;
    /*
     * 受渡商品詳細No.
     */
    protected   Integer                 pickupProductNo     =   null;
    /**
    * 顧客
    */
    protected	MstCustomer		customer            =	new MstCustomer();
    
    protected   java.util.Date          salesDate           =   null;
    
    protected   java.util.Date          startDate           =   null;
    protected   java.util.Date          endDate             =   null;
    /**
     * 処理内容
     */
    protected   Integer                 status              =   null;
    /**
     * スタッフ
     */
    protected MstStaff staff = new MstStaff();
    protected Integer shopID = null; 
    protected String shopIDList = null;
    protected   java.util.Date  deliverDate = null;
    protected   Integer                 delivertNum         =   null;
    protected String postalCode = "";
    protected String[] address = {"", "", "", ""};
    protected MstShop shop = new MstShop();
    protected   String                  hour         =   "";
    protected   String                  minute         =   "";
    
    
    
    protected Double salesNum = null;
    protected Double usedNum = null;
    protected Double price = null;
    protected Double amount = null;
    protected Double pickNum = null;
    protected Double SumDeliverNum = null;
    protected Double ProductNumD =null;
    protected Double SumHandSend =null;

    public Double getSumDeliverNum() {
        return SumDeliverNum;
    }

    public void setSumDeliverNum(Double SumDeliverNum) {
        this.SumDeliverNum = SumDeliverNum;
    }

    public Double getProductNumD() {
        return ProductNumD;
    }

    public void setProductNumD(Double ProductNumD) {
        this.ProductNumD = ProductNumD;
    }

    public Double getSumHandSend() {
        return SumHandSend;
    }

    public void setSumHandSend(Double SumHandSend) {
        this.SumHandSend = SumHandSend;
    }
    
    
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
    
    
     public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPickNum() {
        return pickNum;
    }

    public void setPickNum(Double pick_num) {
        this.pickNum = pick_num;
    }
    public Double getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Double usedNum) {
        this.usedNum = usedNum;
    }
     public Double getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Double salesNum) {
        this.salesNum = salesNum;
    }
    
   
    
      public String getPostalCode() {
        return postalCode;
    }

    /**
     * 郵便番号をセットする。
     *
     * @param postalCode 郵便番号
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = null;

        if (postalCode != null) {
            if (postalCode.matches("[0-9]{7}")) {
                this.postalCode = postalCode;
            }
        }
    }

    /**
     * 郵便番号を取得する。
     *
     * @return 郵便番号
     */
    public String getFormatedPostalCode() {
        if (postalCode == null || postalCode.equals("")) {
            return "";
        } else {
            return (postalCode.length() < 4 ? postalCode
                    : postalCode.subSequence(0, 3) + "-" + postalCode.substring(3, 7));
        }
    }

    /**
     * 住所を取得する。
     *
     * @return 住所
     */
    public String[] getAddress() {
        return address;
    }

    /**
     * 住所を取得する。
     *
     * @param index インデックス
     * @return 住所
     */
    public String getAddress(int index) {
        return address[index] == null ? "" : address[index];
    }

    /**
     * 住所をセットする。
     *
     * @param address 住所
     */
    public void setAddress(String[] address) {
        this.address = address;
    }

    /**
     * 住所をセットする。
     *
     * @param index インデックス
     * @param address 住所
     */
    public void setAddress(int index, String address) {
        this.address[index] = address != null ? address : "";
    }

    public String getFullAddress() {
        return (address[0] == null ? "" : address[0])
                + (address[1] == null ? "" : address[1])
                + (address[2] == null ? "" : address[2])
                + (address[3] == null ? "" : address[3]);
    }
    public Integer getDelivertNum() {
        return delivertNum;
    }

    public void setDelivertNum(Integer delivertNum) {
        this.delivertNum = delivertNum;
    }
    
    
     public java.util.Date getDeliverDate()
    {
            return deliverDate;
    }


    public void setDeliverDate(java.util.Date deliverDate)
    {
            this.deliverDate = deliverDate;
    }        
    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
     public String getShopIDList() {
        return shopIDList;
    }

    public void setShopIDList(String shopIDList) {
        this.shopIDList = shopIDList;
    }
    public void setStartDate(java.util.Date startDate)
    {
            this.startDate = startDate;
    }
    
    public void setEndDate(java.util.Date endDate)
    {
            this.endDate = endDate;
    }
    
    public Integer getProductNumUse() {
        return productNumUse;
    }

    public void setProductNumUse(Integer productNumUse) {
        this.productNumUse = productNumUse;
    }
    
    public void setProductNum(Integer productNum) {
        this.productNumP = productNum;
    }

    public Integer getProductNum() {
        return productNumP;
    }
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

        
    /**
     * 伝票詳細を取得する。
     * @return 伝票詳細
     */
    public DataSalesDetail getSalesDetail() {
        return dataSalesDetail;
    }

    /**
     * 伝票詳細をセットする。
     * @param dataSalesDetail 伝票詳細
     */
    public void setDataSalesDetail(DataSalesDetail dataSalesDetail) {
        this.dataSalesDetail = dataSalesDetail;
    }
    
    /**
    * 顧客を取得する。
    * @return 顧客
    */
    public MstCustomer getCustomer()
    {
            return customer;
    }

    /**
     * 顧客をセットする。
     * @param customer 顧客
     */
    public void setCustomer(MstCustomer customer)
    {
            this.customer = customer;
    }
    
    /**
    * 売上日を取得する。
    * @return 売上日
    */
    public java.util.Date getSalesDate()
    {
            return salesDate;
    }

    /**
     * 売上日をセットする。
     * @param salesDate 売上日
     */
    public void setSalesDate(java.util.Date salesDate)
    {
            this.salesDate = salesDate;
    }
        
    /**
     * 処理内容
     *
     * @return 処理内容
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 処理内容
     *
     * @param status 処理内容
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * スタッフ
     *
     * @return スタッフ
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * スタッフ
     *
     * @param staff スタッフ
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }
    
    /**
     * 受渡商品詳細No.
     *
     * @return 受渡商品詳細No.
     */
    public Integer getPickupProductNo() {
        return pickupProductNo;
    }

    /**
     * 受渡商品詳細No.
     *
     * @param pickupProductNo 受渡商品詳細No.
     */
    public void setPickupProductNo(Integer pickupProductNo) {
        this.pickupProductNo = pickupProductNo;
    }
    
    public ArrayList<MstProductDeliveryManagement> load(ConnectionWrapper con,int flag) throws Exception {
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(flag));
        ArrayList<MstProductDeliveryManagement> list = new ArrayList<MstProductDeliveryManagement>();
        MstProductDeliveryManagement dpp = new MstProductDeliveryManagement();
        while (rs.next()) {
            dpp = new MstProductDeliveryManagement();
            dpp.setData(rs);
            list.add(dpp);
        }
        return list;
    }
    
    /**
     * 
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean updateStatus(ConnectionWrapper con,Integer status) throws SQLException {
        if(!isExists(con)){
            return false;
        }
        if (con.executeUpdate(getUpdateStatusSQL(status)) == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getSalesDetail() == null || this.getPickupProductNo() == null) {
            return false;
        }
        if (con == null) {
            return false;
        }
        ResultSetWrapper rs = con.executeQuery(this.getSelectOneSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * ResultSetWrapperからデータを取得する。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        DataSalesDetail dsd = new DataSalesDetail();
        dsd.setSlipNo(rs.getInt("slip_no"));
        dsd.setSlipDetailNo(rs.getInt("slip_detail_no"));
        MstShop shop = new MstShop();
        shop.setShopID(rs.getInt("shop_id"));
        dsd.setShop(shop);
        this.setDataSalesDetail(dsd);
        
        this.setProductNum(rs.getInt("product_num"));
        this.setPickupProductNo(rs.getInt("pickup_product_no"));
        
        this.setStatus(rs.getInt("status"));
        this.setSalesDate(rs.getDate("sales_date"));
        this.setProductName(rs.getString("product_name"));
        MstCustomer cus = new MstCustomer();
        cus.setCustomerID(rs.getInt("customer_id"));
        cus.setCustomerName(0, rs.getString("customer_name1"));
        cus.setCustomerName(1, rs.getString("customer_name2"));
        cus.setCustomerNo(rs.getString("customer_no"));
        this.setCustomer(cus);
        //MstStaff ms = new MstStaff();
        //ms.setStaffID(rs.getInt("staff_id"));
        //this.setStaff(ms);
        
    }
    
       
    private String getSelectOneSQL() {
        return "select *\n"
                + "from data_pickup_product \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getSalesDetail().getShop().getShopID()) + "\n"
                + "     and slip_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipNo()) + "\n"
                + "     and slip_detail_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipDetailNo()) + "\n"
                //Luc start delete 20130606
                +"     and pickup_product_no = "+ SQLUtil.convertForSQL(this.getPickupProductNo())
                //Luc end delete 20130606
                //Luc start add 20130606
                + "     and delete_date is null";
                
                //Luc end add 2030606
    }
    
    public String getSelectSQL(Integer flag) {
        StringBuilder sql = new StringBuilder();

        sql.append("    select sd.shop_id, \n");
        sql.append(" sd.slip_no,\n");
        sql.append(" sd.slip_detail_no,\n");
        sql.append("pp.pickup_product_no,\n");
        sql.append(" sa.sales_date,\n");
        sql.append("p.product_name,\n");
        sql.append("sd.product_num,\n");
        sql.append("sd.product_value,\n");
        sql.append("pp.status,\n");
        sql.append("sa.staff_id,\n");
        sql.append("c.customer_id,c.customer_name1, c.customer_name2,c.customer_no\n");
        sql.append("from data_sales sa\n");
        sql.append("inner join data_sales_detail sd on sa.shop_id = sd.shop_id and sa.slip_no = sd.slip_no\n");
        sql.append("inner join view_mst_product p on sd.product_id = p.product_id and p.product_division = sd.product_division\n");
        sql.append("inner join mst_customer c on c.customer_id = sa.customer_id \n");
        sql.append("inner join  data_pickup_product pp on pp.shop_id = sa.shop_id and pp.slip_no = sd.slip_no and pp.slip_detail_no = sd.slip_detail_no \n ");
        sql.append("where  sd.product_division = 2 \n");
        if(customer != null){
            sql.append("\n");
            sql.append( " AND   sa.customer_id = ").append(SQLUtil.convertForSQL(customer.getCustomerID()));
            sql.append("\n");
        }
         if(flag == 1){
            sql.append("AND pp.status = 1  \n");
        }else{
            if(flag == 2){
                sql.append(" AND pp.status = 2 \n");
            }else{
                sql.append(" AND (pp.status = 1 or pp.status = 2) \n");
            }
        }
         DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder strDate = new StringBuilder();
        if(this.startDate != null){
            strDate.append(df.format(this.startDate));
            strDate.append(" 0:0:0");
            sql.append( "AND   sa.sales_date >= ").append(SQLUtil.convertForSQL(strDate.toString()));
            sql.append("\n");
        }
        if(this.endDate != null){
            strDate = new StringBuilder();
            strDate.append(df.format(this.endDate));
            strDate.append(" 23:59:59");
            sql.append( "AND   sa.sales_date <= ").append(SQLUtil.convertForSQL(strDate.toString()));
            sql.append("\n");
        }
        if(SystemInfo.getCurrentShop().getShopID() != 0)
        {
            sql.append("AND sa.shop_id =" + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
        }
        sql.append( "\n  AND sa.delete_date is null AND pp.delete_date is null  ");
        sql.append( "\n   order by sd.shop_id,sd.slip_no,sd.slip_detail_no DESC ");
        sql.append( "\n   limit 1000 ");
         
        
        return sql.toString();
    }
    
    /**
     * 顧客No.を更新するUpdate文を取得する。
     *
     * @return Update文
     */
    private String getUpdateStatusSQL(Integer status) {
      //    return "update data_pickup_product\n"
//                + "set\n"
//                + "product_num = " + SQLUtil.convertForSQL(productNum) + ",\n"
//                + "operation_date = current_timestamp\n" + ",\n"
//                + " status = " + SQLUtil.convertForSQL(status) + ",\n"
//                + " staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n"
//                + "insert_date = current_timestamp,\n"
//                + "update_date = current_timestamp\n"
//                + "where shop_id = " + SQLUtil.convertForSQL(this.getSalesDetail().getShop().getShopID()) + "\n"
//                + "     and slip_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipNo()) + "\n"
//                + "     and slip_detail_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipDetailNo()) + "\n"
//                //Luc start delete 20130606
//                //+ "     and pickup_product_no = "+ SQLUtil.convertForSQL(this.getPickupProductNo());
//                //Luc end delete 20130606
//                //Luc start add 20130606
//                + "     and delete_date is null ";
//                //Luc end add 20130606
        
                String sql = "update data_pickup_product\n"
                + "set\n"
                + " status = " + SQLUtil.convertForSQL(status) + ",\n"
                + "update_date = current_timestamp \n "
                + "where shop_id = " + SQLUtil.convertForSQL(this.getSalesDetail().getShop().getShopID()) + "\n"
                + "     and slip_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipNo()) + "\n"
                + "     and slip_detail_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipDetailNo()) + "\n"
               
                + "     and pickup_product_no = "+ SQLUtil.convertForSQL(this.getPickupProductNo()) ;
                return sql;
               
                
    }
    //VTAn Start add  20140906
    
     public boolean updateStatusDelivery(ConnectionWrapper con) throws SQLException {
        if (con.executeUpdate(getUpdateStatusDeliverySQL()) == 1) {
            return true;
        } else {
            return false;
        }
    }
        
    private String getUpdateStatusDeliverySQL() {
                String sql = "update data_pickup_product\n"
                + "set\n"
                + "delivery_date =" + SQLUtil.convertForSQL(this.getDeliverDate()) + ",\n"
                + "status = " + SQLUtil.convertForSQL(this.getStatus()) + ",\n"
                + "post_code =" + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n"
                + "address1 =" + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n"
                + "address2 =" + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n"
                + "address3 =" + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n"
                + "address4 =" + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n"
                + "delivery_num =" + SQLUtil.convertForSQL(this.getDelivertNum()) + ",\n"       
                + "update_date = current_timestamp \n "
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + "     and slip_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipNo()) + "\n"
                + "     and slip_detail_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipDetailNo()) + "\n"
                + "     and pickup_product_no = " + SQLUtil.convertForSQL(this.getPickupProductNo());
                return sql;
               
                
    }
      public ArrayList<MstProductDeliveryManagement> loadDeliver(ConnectionWrapper con, int flag) throws Exception {
        ResultSetWrapper rs = con.executeQuery(this.getSelectDeliverSQL(flag));
        ArrayList<MstProductDeliveryManagement> list = new ArrayList<MstProductDeliveryManagement>();
        MstProductDeliveryManagement dpp = new MstProductDeliveryManagement();
        while (rs.next()) {
            dpp = new MstProductDeliveryManagement();
            dpp.setDataDeliver(rs);
            list.add(dpp);
        }
        return list;
    }
    
       public void setDataDeliver(ResultSetWrapper rs) throws SQLException {
         
        DataSalesDetail dsd = new DataSalesDetail();
        dsd.setSlipNo(rs.getInt("slip_no"));
        dsd.setSlipDetailNo(rs.getInt("slip_detail_no"));
        MstShop shop = new MstShop();
        this.setShopID(rs.getInt("shop_id"));
        shop.setShopName(rs.getString("shop_name"));
        dsd.setShop(shop);
        // Thanh start add 2014/07/01
        MstCustomer mc = new MstCustomer();
        mc.setCustomerID(rs.getInt("customer_id"));
        mc.setCustomerNo(rs.getString("customer_no"));
        mc.setCustomerName(0,rs.getString("customer_name1"));
        mc.setCustomerName(1,rs.getString("customer_name2"));
        this.setCustomer(mc);
        // Thanh end add 2014/07/01
        this.setDataSalesDetail(dsd);
        this.setPickupProductNo(rs.getInt("pickup_product_no"));
        this.setDelivertNum(rs.getInt("delivery_num"));
        this.setStatus(rs.getInt("status"));
        this.setProductName(rs.getString("item_name"));   
        this.setPostalCode(rs.getString("post_code"));
        this.setAddress(0, rs.getString("address1"));
        this.setAddress(1, rs.getString("address2"));
        this.setAddress(2, rs.getString("address3"));
        this.setAddress(3, rs.getString("address4"));
        this.setUsedNum(rs.getDouble("sales_num") - rs.getDouble("pick_num") - rs.getDouble("delivery_num") ); 
        this.setPrice(rs.getDouble("price"));
        this.setAmount(this.getPrice() * this.getUsedNum());
        this.setPickNum(rs.getDouble("pick_num"));
        this.setSalesNum(rs.getDouble("sales_num"));       
        this.setDeliverDate(rs.getTimestamp("delivery_date"));
 
    }
       
       public void setDataSum(ResultSetWrapper rs) throws SQLException {
        DataSalesDetail dsd = new DataSalesDetail();
        dsd.setSlipNo(rs.getInt("slip_no"));
        dsd.setSlipDetailNo(rs.getInt("slip_detail_no"));
        MstShop shop = new MstShop();
        shop.setShopID(rs.getInt("shop_id"));
        dsd.setShop(shop);
        this.setDataSalesDetail(dsd);   
        this.setProductNumD(rs.getDouble("product_num"));
        this.setSumDeliverNum(rs.getDouble("delivery_num_sum"));       
        this.setSumHandSend(rs.getDouble("hand_send"));
 
    }
       
    public String getSelectSumNumSQL() {
        StringBuilder sql = new StringBuilder();
  
        sql.append("SELECT dsd.shop_id , dsd.slip_no , dsd.slip_detail_no, dsd.product_num \n");
        sql.append(", sum(COALESCE(dpk.product_num,0)) :: INT as hand_send ,sum(COALESCE(dpk.delivery_num,0)) :: INT as delivery_num_sum \n");
        sql.append("FROM data_pickup_product dpk \n");
        sql.append("inner join data_sales_detail dsd on dsd.shop_id = dpk.shop_id and dsd.slip_no = dpk.slip_no and dsd.slip_detail_no = dpk.slip_detail_no\n");
        sql.append("inner join data_sales ds on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no \n");

        sql.append("where dsd.product_division IN (2, 4) \n");
        if (this.getShopID() != null) {
            if (this.getShopID() != 0) {
                sql.append(" and ds.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n");
            } else {
                sql.append(" and ds.shop_id in  (" + this.getShopIDList() + ")\n");
            }
        }
        sql.append("and dsd.slip_no = " + SQLUtil.convertForSQL(this.getSalesDetail().getSlipNo()) + "\n");
        sql.append("and dsd.slip_detail_no =" + SQLUtil.convertForSQL(this.getSalesDetail().getSlipDetailNo()) + "\n");   
        sql.append("and dpk.pickup_product_no not in (" + this.getPickupProductNo() + ")\n");
        sql.append("and ds.sales_date is not null\n");
        sql.append("group by dsd.shop_id , dsd.slip_no, dsd.slip_detail_no, dsd.product_num \n");
        sql.append("order by dsd.shop_id , dsd.slip_no , dsd.slip_detail_no \n");
        return sql.toString();
    }

    public  void loadSum(ConnectionWrapper con) throws SQLException{    
        ResultSetWrapper rs = con.executeQuery(this.getSelectSumNumSQL());
        MstProductDeliveryManagement dpp = new MstProductDeliveryManagement(); 
        if(rs.next()) {

            this.setDataSum(rs);
        }
        rs.close();
    }
    
    public String getSelectDeliverSQL(Integer flag) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ms.shop_name ,\n");
        // Thanh start add 2014/07/01
        sql.append("mc.customer_id ,\n");
        sql.append("mc.customer_no ,\n");
        sql.append("mc.customer_name1 ,\n");
        sql.append("mc.customer_name2 ,\n");
        // Thanh end add 2014/07/01
        sql.append("pp.delivery_date ,\n");
        sql.append("mi.item_name ,\n");
        sql.append("dsd.product_num as sales_num,\n");
        sql.append("pp.status ,\n");
        sql.append("pp.post_code ,\n");
        sql.append("pp.address1 ,\n");
        sql.append("pp.address2 ,\n");
        sql.append("pp.address3 ,\n");
        sql.append("pp.address4 ,\n");
        sql.append("pp.shop_id ,\n");
        sql.append("pp.slip_no ,\n");
        sql.append("pp.pickup_product_no ,\n");
        sql.append("pp.slip_detail_no ,\n");
        sql.append("wp.price ,\n");
        // Thanh start edit 2014/06/26
        //sql.append("sum(COALESCE(pp.product_num,0)) :: INT as pick_num,\n");
        sql.append("COALESCE(( select sum(product_num) from  data_pickup_product \n");
        sql.append("where shop_id = dsd.shop_id and slip_no = dsd.slip_no AND slip_detail_no = dsd.slip_detail_no ),0) \n");
        sql.append("as pick_num,\n"); 
        // Thanh end edit 2014/06/26
        sql.append("sum(COALESCE(pp.delivery_num,0)) :: INT as delivery_num\n");
        sql.append("FROM data_sales_detail dsd \n");
        sql.append("INNER JOIN mst_shop ms ON ms.shop_id = dsd.shop_id \n");
        sql.append("INNER JOIN view_mst_product wp on wp.product_division = dsd.product_division and wp.product_id = dsd.product_id \n ");
        sql.append("LEFT  JOIN data_pickup_product pp ON pp.shop_id = dsd.shop_id \n");
        sql.append("AND pp.slip_no = dsd.slip_no AND pp.slip_detail_no = dsd.slip_detail_no \n");
        sql.append("inner join data_sales ds on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no \n");
        // Thanh start add 2014/07/01
        sql.append("inner join mst_customer mc on mc.customer_id = ds.customer_id \n");
        // Thanh end add 2014/07/01
        sql.append("INNER JOIN mst_item mi ON mi.item_id = dsd.product_id\n");
        sql.append("WHERE dsd.product_division IN(2, 4)\n");
        if (this.getShopID() != null) {
            if (this.getShopID() != 0) {
                sql.append(" and pp.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
            } else {
                sql.append(" and pp.shop_id in  (" + this.getShopIDList() + ")\n");
            }
        }

         DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder strDate = new StringBuilder();
        if(this.startDate != null){
            strDate.append(df.format(this.startDate));
            strDate.append(" 0:0:0");
            sql.append( " AND pp.delivery_date >= ").append(SQLUtil.convertForSQL(strDate.toString()));
            sql.append("\n");
        }
        if(this.endDate != null){
            strDate = new StringBuilder();
            strDate.append(df.format(this.endDate));
            strDate.append(" 23:59:59");
            sql.append( " AND pp.delivery_date <=  ").append(SQLUtil.convertForSQL(strDate.toString()));
            sql.append("\n");
        }
         if(flag == 1){
            sql.append("AND pp.status = 1  \n");
        }else{
            if(flag == 2){
                sql.append(" AND pp.status = 2 \n");
            }else{
                sql.append(" AND (pp.status = 1 or pp.status = 2) \n");
            }
        }
        sql.append("AND delivery_num > 0 \n");
        sql.append("and ds.sales_date is not null and ds.delete_date is null\n");
        sql.append("AND dsd.delete_date IS NULL AND pp.delete_date IS NULL \n");
        sql.append("GROUP BY  	ms.shop_name	\n");	
        // Thanh start add 2014/07/01
        sql.append(",mc.customer_id \n");
        sql.append(",mc.customer_no \n");
        sql.append(",mc.customer_name1 \n");
        sql.append(",mc.customer_name2 \n");
        // Thanh end add 2014/07/01
	sql.append(",wp.price \n");																					
        sql.append(",pp.delivery_date,mi.item_name	\n");																				
        sql.append(",dsd.product_num,pp.status	\n");																				
        sql.append(",pp.post_code	\n");																				
        sql.append(",pp.address1\n");																					
        sql.append(",pp.address2	\n");																				
        sql.append(",pp.address3	\n");																				
        sql.append(",pp.address4	\n");																				
        sql.append(",pp.shop_id		\n");																			
        sql.append(",pp.slip_no		\n");																			
        sql.append(",pp.pickup_product_no \n");																					
        sql.append(",pp.slip_detail_no	\n");																				
        sql.append(",pp.delivery_num \n");
        sql.append(",dsd.slip_no	\n");
        sql.append(",dsd.slip_detail_no    \n");     
        sql.append(",dsd.shop_id    \n");  
        sql.append("ORDER BY dsd.slip_no ASC,\n");
        sql.append("pp.pickup_product_no ASC,\n");
        sql.append("dsd.slip_detail_no ASC \n");     

        return sql.toString();
    }
    
    
    //VTAn End add 20140906



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MstProductDeliveryManagement other = (MstProductDeliveryManagement) obj;
        if (this.dataSalesDetail != other.dataSalesDetail && (this.dataSalesDetail == null || !this.dataSalesDetail.equals(other.dataSalesDetail))) {
            return false;
        }
        if (this.pickupProductNo != other.pickupProductNo && (this.pickupProductNo == null || !this.pickupProductNo.equals(other.pickupProductNo))) {
            return false;
        }
        if (this.shopID != other.shopID && (this.shopID == null || !this.shopID.equals(other.shopID))) {
            return false;
        }
        return true;
    }
}
