/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.course;

import com.geobeck.sosia.pos.hair.data.account.DataSalesDetail;
import com.geobeck.sosia.pos.hair.master.product.MstCourse;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.products.Product;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

/**
 * コース契約履歴
 * @author s_furukawa
 */
public class DataContract extends ArrayList<DataContractDigestion>{
	/**
	 * 店舗
	 */
	protected MstShop shop = new MstShop();

	/**
	 * 伝票No.
	 */
	protected Integer slipNo = null;
        /**
         * 契約No
         */
        protected Integer contractNo = null;
        /**
         * 契約詳細No
         */
        protected Integer contractDetailNo = null;
        /**
         * コース
         */
        protected Product product = new Product();
        /**
         * 消化回数
         */
	protected Integer productNum = 0;
        /**
         * 金額
         */
	protected Long productValue = 0l;
	/**
	 * 契約担当スタッフ
	 */
	protected MstStaff staff = null;

        private Date      validDate;
        /**
         * 保証期限
         */
        private Date      limitDate;
        
        private Date      deleteDate;
        //Thanh Add 2013/03/01
        protected Integer contractStatus = null;
        
        protected Integer serviceCharge = 0;
        
        protected double requestValue = 0;
        
        protected String courseName = "";
        
        protected GregorianCalendar cancelDate = null;        
        
        /**
	 * 顧客データ
	 */
	protected	MstCustomer		customer	=	new MstCustomer();
        
        protected Integer verificationFlag = 0;
        
        /**
         * 変更先契約詳細番号
         */
	protected Integer alterContractDetailNo = 0;
        //Thanh Add 2013/03/01

        //Thuyet Add Start 2013/03/05
        /**
         * コースID
         */
	protected Integer productID = 0;
        /**
         * 消化
         */
	protected Double productNum1 = 0.0;
        /**
         * 消化回数
         */
	protected Double productNum2 = 0.0;
	
		/**
		 * 消化殘り
		 */
	protected Double remainingNum = null;
        /**
         * 変更理由
         */
        private String      changeReason = "";
        /**
         * 契約日
         */
        private Date      salesDate;
        //Thuyet Add End 2013/03/05
        // Start add 20130424 nakhoa
        private Integer praiseTimeLimit = 0;
        private Integer courseSecurityTimeLimit = 0;
        // End add 20130424 nakhoa
        
        //IVS_LVTu start add 2015/12/28 Bug #45225
        Integer beforeContractShopID    = null;
        Integer beforeContractNo        = null;

        public Integer getBeforeContractShopID() {
            return beforeContractShopID;
        }

        public void setBeforeContractShopID(Integer beforeContractShopID) {
            this.beforeContractShopID = beforeContractShopID;
        }

        public Integer getBeforeContractNo() {
            return beforeContractNo;
        }

        public void setBeforeContractNo(Integer beforeContractNo) {
            this.beforeContractNo = beforeContractNo;
        }
        //IVS_LVTu end add 2015/12/28 Bug #45225
        
        /**
         * コンストラクタ
         */
        public DataContract()
        {
        }

        public DataContract(DataSalesDetail dataSalesDetail){
            this.shop = dataSalesDetail.getShop();
            this.slipNo = dataSalesDetail.getSlipNo();
            this.product = dataSalesDetail.getProduct();
           // this.productValue = dataSalesDetail.getProductValue() - dataSalesDetail.getDiscountValue();
            // vtbphuong start change 20140710 Request #26770
            if(SystemInfo.getAccountSetting().getDiscountType() == 0 ){
                this.productValue = dataSalesDetail.getProductValue() - dataSalesDetail.getDiscountValue();
            }else{
                // start add 2017/06/19 #16593
                dataSalesDetail.setTaxRate(SystemInfo.getAccountSetting().getTaxRate());
                // end add 2017/06/19 #16593
                Double value = new Double(Math.floor(dataSalesDetail.getDiscountValue() + (dataSalesDetail.getDiscountValue() * dataSalesDetail.getTaxRate())));
                this.productValue = dataSalesDetail.getProductValue() - value.longValue();
            }
             // vtbphuong end change 20140710 Request #26770
            this.staff = dataSalesDetail.getStaff();
        }

        public Integer getContractDetailNo() {
            return contractDetailNo;
        }

        public void setContractDetailNo(Integer contractDetailNo) {
            this.contractDetailNo = contractDetailNo;
        }

        public Integer getContractNo() {
            return contractNo;
        }

        public void setContractNo(Integer contractNo) {
            this.contractNo = contractNo;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Integer getProductNum() {
            return productNum;
        }

        public void setProductNum(Integer productNum) {
            this.productNum = productNum;
        }

        public Long getProductValue() {
            return productValue;
        }

        public void setProductValue(Long productValue) {
            this.productValue = productValue;
        }

        public MstShop getShop() {
            return shop;
        }

        public void setShop(MstShop shop) {
            this.shop = shop;
        }

        public Integer getSlipNo() {
            return slipNo;
        }

        public void setSlipNo(Integer slipNo) {
            this.slipNo = slipNo;
        }

        public MstStaff getStaff() {
            return staff;
        }

        public void setStaff(MstStaff staff) {
            this.staff = staff;
        }
        public Date getValidDate() {
            return validDate;
        }

        public void setValidDate(Date validDate) {
            this.validDate = validDate;
        }

        public Date getLimitDate() {
            return limitDate;
        }

        public void setLimitDate(Date limitDate) {
            this.limitDate = limitDate;
        }

        public Date getDeleteDate() {
            return deleteDate;
        }

        public void setDeleteDate(Date deleteDate) {
            this.deleteDate = deleteDate;
        }
         public Integer getContractStatus() {
            return contractStatus;
        }

        public void setContractStatus(Integer contractStatus) {
            this.contractStatus = contractStatus;
        }
        public Integer getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(Integer serviceCharge) {
            this.serviceCharge = serviceCharge;
        }
        
         public double getRequestValue() {
            return requestValue;
        }      
        
        public void setRequestValue(double requestValue) {
            this.requestValue = requestValue;
        }
        
         public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }   
    
        /**
     * 施術開始時間を取得する
     *
     * @return キャンセル実施期間
     */
    public GregorianCalendar getCancelDate() {
        return cancelDate;
    }

    /**
     * 施術開始時間をセットする
     *
     * @param cancelDate キャンセル実施期間
     */
    public void setCancelDate(GregorianCalendar cancelDate) {
        this.cancelDate = cancelDate;
    }

    public void setCancelDate(java.util.Date cancelDate) {
        if (this.cancelDate == null) {
            this.cancelDate = new GregorianCalendar();
        }

        this.cancelDate.setTime(cancelDate);
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
        
         
         public Integer getVerificationFlag() {
            return verificationFlag;
        }

        public void setVerificationFlag(Integer verificationFlag) {
            this.verificationFlag = verificationFlag;
        }
        
         public Integer getAlterContractDetailNo() {
            return alterContractDetailNo;
        }

        public void setAlterContractDetailNo(Integer alterContractDetailNo) {
            this.alterContractDetailNo = alterContractDetailNo;
        }
        
        //Thanh add 2013/03/01
        
        //Thuyet Add Start 2013/03/05
        public Integer getProductID() {
            return productID;
        }

        public void setProductID(Integer setProductID) {
            this.productID = setProductID;
        }
        public Double getProductNum1() {
            return productNum1;
        }
        public void setProductNum1(Double productNum1) {
            this.productNum1 = productNum1;
        }
        public Double getProductNum2() {
            return productNum2;
        }
        
        public void setRemainingNum(Double remainingNum){
        	this.remainingNum = remainingNum;
        }
        
        public Double getRemainingNum() {
            return remainingNum;
        }

        public void setProductNum2(Double productNum2) {
            this.productNum2 = productNum2;
        }
        /**
	 * 役務残を取得する。
	 * @return 役務残
	 */
	public Double getRemainValue()
	{
            if(getProductNum1() > 0)
            {
		return getProductValue() - (getProductNum2() / getProductNum1()) * getProductValue();
            }
            else
            {
                return 0.0;
            }
	}

        public String getChangeReason()
	{
		return changeReason;
	}

	public void setChangeReason(String changeReason)
	{
		this.changeReason = changeReason;
	}

        public Date getSalesDate() {
            return salesDate;
        }

        public void setSalesDate(Date salesDate) {
            this.salesDate = salesDate;
        }
        //Thuyet Add End 2013/03/05
        // Start add 20130424 nakhoa
        public void setPraiseTimeLimit(Integer praiseTimeLimit){
            this.praiseTimeLimit = praiseTimeLimit;
        }
        
        public Integer getPraiseTimeLimit(){
            return this.praiseTimeLimit;
        }
        
        public void setCourseSecurityTimeLimit(Integer courseSecurityTimeLimit){
            this.courseSecurityTimeLimit = courseSecurityTimeLimit;
        }
        
        public Integer getCourseSecurityTimeLimit(){
            return this.courseSecurityTimeLimit;
        }
        // End add 20130424 nakhoa

        /**
         * コース契約履歴を登録する
         * @param con
         * @return
         * @throws SQLException
         */
      	public boolean regist(ConnectionWrapper con) throws SQLException
	{
            String	sql		=	"";

            if(isExists(con))
            {
                    sql	=	this.getUpdateSQL();
            }
            else
            {
                    sql	=	this.getInsertSQL();
            }

            if(con.executeUpdate(sql) == 1)
            {
                    return	true;
            }
            else
            {
                    return	false;
            }
        }
        /**
         * コース契約履歴を登録する
         * @param con
         * @return
         * @throws SQLException
         */
      	public boolean registDataContract(ConnectionWrapper con) throws SQLException
	{
            String	sql		=	"";

           
            sql	=	this.getInsertDataContractSQL();         

            if(con.executeUpdate(sql) == 1)
            {
                    return	true;
            }
            else
            {
                    return	false;
            }
        }

        /**
         * コース契約履歴を登録する
         * @param con
         * @return
         * @throws SQLException
         */
      	public boolean UpdateDataContract(ConnectionWrapper con) throws SQLException
	{
            String	sql		=	"";


            sql	=	this.getUpdateDataContractSQL();

            if(con.executeUpdate(sql) == 1)
            {
                    return	true;
            }
            else
            {
                    return	false;
            }
        }
        
        /**
         * コース契約履歴を登録する
         * @param con
         * @return
         * @throws SQLException
         */
      	public boolean UpdateContractStatus(ConnectionWrapper con) throws SQLException
	{
            String	sql		=	"";


            sql	=	this.getUpdateContractStatusSQL();

            if(con.executeUpdate(sql) == 1)
            {
                    return	true;
            }
            else
            {
                    return	false;
            }
        }
        
        /**
         * 契約履歴を更新するクエリを返す
         * @return
         */
        public String getUpdateDataContractSQL(){
            String sql = "UPDATE data_contract\n "
                        + "set\n"
                        + "valid_date = " + SQLUtil.convertForSQL(this.getValidDate()) + ", \n"
                        + "limit_date = " + SQLUtil.convertForSQL(this.getLimitDate()) + ", \n"
                        + "staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ", \n"
                        + "change_reason = " + SQLUtil.convertForSQL(this.getChangeReason()) + ", \n"
                        + "update_date = current_timestamp\n"
                        + "where shop_id = " + SQLUtil.convertForSQL(this.shop.getShopID()) + "\n"
                        + "and slip_no = " + SQLUtil.convertForSQL(this.slipNo) + "\n"
                        + "and contract_no = " + SQLUtil.convertForSQL(this.contractNo) + "\n"
                        + "and contract_detail_no = " + SQLUtil.convertForSQL(this.contractDetailNo) + "\n";
            return sql;
        }
        
         public String getUpdateContractStatusSQL(){
            String sql = "UPDATE data_contract\n "
                        + "set\n"
                        + "contract_status = " + SQLUtil.convertForSQL(this.contractStatus) + ", \n"
                        + "update_date = current_timestamp\n"
                        + "where shop_id = " + SQLUtil.convertForSQL(this.shop.getShopID()) + "\n"
                        + "and slip_no = " + SQLUtil.convertForSQL(this.slipNo) + "\n"
                        + "and contract_no = " + SQLUtil.convertForSQL(this.contractNo) + "\n"
                        + "and contract_detail_no = " + SQLUtil.convertForSQL(this.contractDetailNo) + "\n";
            return sql;
        }
         /*
         * コース契約履歴を登録するクエリを返す
         * @return
         */
        private String getInsertSQL()
        {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO data_contract\n");
            sql.append("(shop_id, \n");
            sql.append("slip_no, \n");
            sql.append("contract_no, \n");
            sql.append("contract_detail_no, \n");
            sql.append("product_id, \n");
            sql.append("product_num, \n");
            sql.append("product_value, \n");
            sql.append("staff_id, \n");
            // Start add 20131202 lvut
            sql.append("valid_date, \n");
            sql.append( "limit_date, \n");
            // End add 20131202 lvut
            sql.append( "insert_date, \n");
            sql.append("update_date, \n");
            sql.append("delete_date) \n");
            sql.append("VALUES (\n");
            sql.append(SQLUtil.convertForSQL(this.shop.getShopID())).append(", \n");
            sql.append(SQLUtil.convertForSQL(this.slipNo)).append(", \n");
            sql.append(SQLUtil.convertForSQL(this.contractNo)).append(", \n");
            sql.append(SQLUtil.convertForSQL(this.contractDetailNo)).append(", \n");
            sql.append(SQLUtil.convertForSQL(this.product.getProductID())).append(", \n");
            sql.append(SQLUtil.convertForSQL(this.productNum)).append(", \n");
            sql.append(SQLUtil.convertForSQL(this.productValue)).append(", \n");
            sql.append(SQLUtil.convertForSQL(this.staff.getStaffID())).append(", \n");
            // Start add 20131202 lvut
            if(this.getPraiseTimeLimit().intValue() > 0){
                sql.append("to_date(to_char(date_trunc('day',current_date) + cast(").append(this.getPraiseTimeLimit().toString()).append(" || ' months' as interval),'YYYY/MM/DD HH24:MI'),'YYYY/MM/DD HH24:MI')-1, \n");
            }else{
                sql.append("null, \n");
            }
            if(this.getCourseSecurityTimeLimit().intValue() > 0){
                sql.append(" to_date(to_char(date_trunc('day',current_date) + cast(").append(this.getCourseSecurityTimeLimit().toString()).append(" || ' months' as interval),'YYYY/MM/DD HH24:MI'),'YYYY/MM/DD HH24:MI')-1, \n");
            }else{
                sql.append("null, \n");
            }
            // End add 20131202 lvut
            sql.append("current_timestamp, \n");
            sql.append("current_timestamp, \n");
            sql.append("null)");
            return sql.toString();
        }

        /**
         * コース契約履歴を更新するクエリを返す
         * @return
         */
        public String getUpdateSQL(){
            String sql = "UPDATE data_contract\n "
                        + "set\n"
                        + "product_id = " + SQLUtil.convertForSQL(this.product.getProductID()) + ", \n"
                        + "product_num = " + SQLUtil.convertForSQL(this.productNum) + ", \n"
                        + "product_value = " + SQLUtil.convertForSQL(this.productValue) + ", \n"
                        + "staff_id = "+ SQLUtil.convertForSQL(this.staff.getStaffID()) + ", \n"
                        + "update_date = current_timestamp\n"
                        + "where shop_id = " + SQLUtil.convertForSQL(this.shop.getShopID()) + "\n"
                        + "and slip_no = " + SQLUtil.convertForSQL(this.slipNo) + "\n"
                        + "and contract_no = " + SQLUtil.convertForSQL(this.contractNo) + "\n"
                        + "and contract_detail_no = " + SQLUtil.convertForSQL(this.contractDetailNo) + "\n";
            return sql;
        }

        /**
         * コース情報を取得する
         * @param con
         * @return
         */
        public MstCourse loadCourse(ConnectionWrapper con, Integer courseId) throws SQLException
        {
            MstCourse course = null;
            ResultSetWrapper rs = con.executeQuery(this.getSelectCourseSQL(courseId));

            if(rs.next())
            {
                    course = new MstCourse();
                    course.setData(rs);
            }

            rs.close();

            return course;
        }

        /**
         * コースを取得するクエリを返す
         * @param courseId
         * @return
         */
        private String getSelectCourseSQL(Integer courseId)
        {
		return	"select *\n" +
				"from mst_course\n" +
				"where	course_id = " + SQLUtil.convertForSQL(courseId) + "\n";
        }

        /**
         * 契約Noの最大値を取得する
         * @param con
         * @param shopId
         * @return
         * @throws SQLException
         */
        public Integer loadMaxContractNo(ConnectionWrapper con, Integer shopId) throws SQLException {
            Integer maxContractNo = null;
            ResultSetWrapper rs = con.executeQuery(this.getMaxContractNoSQL(shopId));

            if(rs.next())
            {
                maxContractNo = rs.getInt("max_contract_no");
            }

            rs.close();
            return maxContractNo;
        }

        /**
         * 契約Noの最大値を取得するクエリを返す
         * @param shopId
         * @return
         */
        public String getMaxContractNoSQL(Integer shopId){
            return "select coalesce(max(contract_no), 0) as max_contract_no\n"
                    + "from data_contract\n"
                    + "where shop_id = " + SQLUtil.convertForSQL(shopId) + "\n";
        }

        /**
         * 契約詳細Noの最大値を返す
         * @param con
         * @param shopId
         * @param contractNo
         * @return
         * @throws SQLException
         */
        public Integer loadMaxContractDetailNo(ConnectionWrapper con, Integer shopId, Integer contractNo) throws SQLException {
            Integer maxContractDetailNo = null;
            ResultSetWrapper rs = con.executeQuery(this.getMaxContractDetailNoSQL(shopId, contractNo));

            if(rs.next())
            {
                maxContractDetailNo = rs.getInt("max_contract_detail_no");
            }

            rs.close();
            return maxContractDetailNo;
        }

        /**
         * 契約詳細Noの最大値を取得するクエリを返す
         * @param shopId
         * @param contractNo
         * @return
         */
        public String getMaxContractDetailNoSQL(Integer shopId, Integer contractNo){
            return "select coalesce(max(contract_detail_no), 0) as max_contract_detail_no\n"
                    + "from data_contract\n"
                    + "where shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                    + "and contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n";
        }


      	public boolean loadDataContactDigestion(ConnectionWrapper con, Integer customerId) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(this.getDataContractDigestionSQL(this.getShop().getShopID(), this.contractNo, this.contractDetailNo,customerId));

            while(rs.next()){
                DataContractDigestion dcd = new DataContractDigestion();
                dcd.setData(rs);
                this.add(dcd);
            }

            rs.close();
            return true;
        }

        public String getDataContractDigestionSQL(Integer shopId, Integer contractNo, Integer contractDetailNo,Integer customerId){
            return "select ds.sales_date, dcd.*, coalesce(ms.staff_name1, '') as staff_name1, coalesce(ms.staff_name2, '') as staff_name2\n"
                    + "from\n"
                    + "    data_contract_digestion dcd\n"
                    + "    inner join data_sales ds"
                    //他店コース消化対応 IVS20130412
                    + "    on dcd.shop_id = ds.shop_id and dcd.slip_no = ds.slip_no"
                    //他店コース消化対応 IVS20130412
                    + "    left join mst_staff ms\n"
                    + "    on dcd.staff_id = ms.staff_id\n"
                    + "where ds.delete_date is null\n"
                    //Luc start add 20151117 #44432
                    +" and ds.sales_date is not null "
                    //Luc end add 20151117 #44432
                    //他店コース消化対応 IVS20130412
                    //+ "and dcd.contract_shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                    + "and ds.customer_id = " + SQLUtil.convertForSQL(customerId) + "\n"
                    //他店コース消化対応 IVS20130412
                    + "and dcd.contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
                    + "and dcd.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n"
                    + "order by dcd.insert_date desc";
        }
      	public boolean loadDataContactDigestion(ConnectionWrapper con) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(this.getDataContractDigestionSQL(this.getShop().getShopID(), this.contractNo, this.contractDetailNo));

            while(rs.next()){
                DataContractDigestion dcd = new DataContractDigestion();
                dcd.setData(rs);
                this.add(dcd);
            }

            rs.close();
            return true;
        }


        public String getDataContractDigestionSQL(Integer shopId, Integer contractNo, Integer contractDetailNo){

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      ds.sales_date");
            sql.append("     ,ds.customer_id");
            sql.append("     ,dcd.*");
            sql.append("     ,coalesce(ms.staff_name1, '') as staff_name1");
            sql.append("     ,coalesce(ms.staff_name2, '') as staff_name2");
            sql.append("     ,coalesce(mc.customer_name1, '') as customer_name1");
            sql.append("     ,coalesce(mc.customer_name2, '') as customer_name2");
            sql.append(" from");
            sql.append("     data_contract_digestion dcd");
            sql.append("         inner join data_sales ds");
            sql.append("                 on dcd.shop_id = ds.shop_id");
            sql.append("                and dcd.slip_no = ds.slip_no");
            sql.append("         left join mst_staff ms");
            sql.append("                on dcd.staff_id = ms.staff_id");
            sql.append("         left join mst_customer mc");
            sql.append("                on ds.customer_id = mc.customer_id");
            sql.append(" where");
            sql.append("         ds.delete_date is null");
            //Luc start add 20151117 #44432
            sql.append("         and ds.sales_date is not null " );
            //Luc end add 20151117 #44432
            sql.append("     and dcd.contract_shop_id = " + SQLUtil.convertForSQL(shopId));
            sql.append("     and dcd.contract_no = " + SQLUtil.convertForSQL(contractNo));
            sql.append("     and dcd.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo));
            sql.append(" order by");
            sql.append("     dcd.insert_date desc");

            return sql.toString();
        }
        
        public String toString(){
            if(this.product == null){
                return "";
            }
            return this.product.getProductName();
        }

	/**
	 * 伝票明細データの存在チェックを行う。
	 * @param con ConnectionWrapper
	 * @return true - 存在する。
	 * @throws java.sql.SQLException 例外
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;

		if(con == null)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	/**
	 * 伝票明細データを取得するＳＱＬ文を取得する。
	 * @return 伝票明細データを取得するＳＱＬ文
	 */
	public String getSelectSQL()
	{
		return	"select *\n"
				+ "from data_contract\n"
                                + "where shop_id = " + SQLUtil.convertForSQL(this.shop.getShopID()) + "\n"
                                + "and slip_no = " + SQLUtil.convertForSQL(this.slipNo) + "\n"
                                + "and contract_no = " + SQLUtil.convertForSQL(this.contractNo) + "\n"
                                + "and contract_detail_no = " + SQLUtil.convertForSQL(this.contractDetailNo) + "\n";
	}

      	/**
	 * コース契約情報リストを取得する
	 * @param con ConnectionWrapper
	 * @param shopId 店舗ID
	 * @param slipNo 伝票番号
	 * @return コース契約情報リスト
	 * @throws java.sql.SQLException 例外
	 */
	public static List<DataContract> getDataContractList(ConnectionWrapper con, Integer shopId, Integer slipNo) throws SQLException
	{
                List<DataContract> list = new ArrayList<DataContract>();

                ResultSetWrapper	rs	=	con.executeQuery(DataContract.getSelectSQL(shopId, slipNo));

                while(rs.next()){
                    DataContract dataContract = new DataContract();
                    MstShop shop = new MstShop();
                    shop.setShopID(shopId);
                    dataContract.setShop(shop);
                    dataContract.setSlipNo(slipNo);
                    dataContract.setContractNo(rs.getInt("contract_no"));
                    dataContract.setContractDetailNo(rs.getInt("contract_detail_no"));
                    //Start add 20131105 lvut (hide 精算 when contrac_status =1,2 )
                    dataContract.setContractStatus(rs.getInt("contract_status"));
                    dataContract.setDeleteDate(rs.getDate("delete_date"));
                    //End add 20131105 lvut (hide 精算 when contrac_status =1,2 )
                    Product product = new Product();
                    product.setProductID(rs.getInt("product_id"));
                    dataContract.setProduct(product);
                    dataContract.setProductNum(rs.getInt("product_num"));
                    dataContract.setProductValue(rs.getLong("product_value"));
                    MstStaff staff = new MstStaff();
                    staff.setStaffID(rs.getInt("staff_id"));
                    dataContract.setStaff(staff);
                    
                    list.add(dataContract);
                }

                return list;
        }

        /**
	 * 伝票明細データを取得するＳＱＬ文を取得する。
	 * @return 伝票明細データを取得するＳＱＬ文
	 */
	public static String getSelectSQL(Integer shopId, Integer slipNo)
	{
		return	"select *\n"
				+ "from data_contract\n"
                                + "where shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                                + "and slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
	}
        
        
        /**
	 * コース契約履歴データを論理削除するＳＱＬ文を取得する。
	 * @return コース契約履歴データを論理削除するＳＱＬ文
	 */
	public String getDeleteDataContractLogicallySQL(Integer shopId, Integer slipNo)
	{
		return	"update data_contract\n" +
                        "set delete_date = current_timestamp " +
                        "where shop_id = " + SQLUtil.convertForSQL(shopId) + "\n" +
                        "and slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n" +
                        "and contract_no = " + SQLUtil.convertForSQL(this.getContractNo())+ "\n" +
                        "and contract_detail_no = " + SQLUtil.convertForSQL(this.getContractDetailNo()) + "\n" ;
	}
        
        //An start add 20130307
         public String getUpdateDataContractLogicallySQL()
	{
		return	"update data_contract\n" +
                        "set delete_date = current_timestamp " +
                        "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                        "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
                        "and contract_no = " + SQLUtil.convertForSQL(this.getContractNo()) + "\n" +
                        "and contract_detail_no = " + SQLUtil.convertForSQL(this.getContractDetailNo())+ "\n" ;
	}
         
        public String getUpdateDataContractDigestionSQL()
        {
            return    "update data_contract_digestion\n" +
                         "set delete_date = current_timestamp " +
                        "where contract_shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                        "and contract_no = " + SQLUtil.convertForSQL(this.getContractNo())+ "\n" +
                        "and contract_detail_no = " + SQLUtil.convertForSQL(this.getContractDetailNo())+ "\n" ;
        }
        
        //nhanvt add start 20141007 Request #31292 
         public String getReUpdateDataContractLogicallySQL()
	{
		return	"update data_contract\n" +
                        "set delete_date = null " +
                        "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                        "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
                        "and contract_no = " + SQLUtil.convertForSQL(this.getContractNo()) + "\n" +
                        "and contract_detail_no = " + SQLUtil.convertForSQL(this.getContractDetailNo())+ "\n" ;
	}
        //nhanvt add end 20141007 Request #31292 
        
        public boolean isExitsContract(ConnectionWrapper con) throws SQLException
	{
        if (con == null) {
            return false;
        }
          ResultSetWrapper rs = con.executeQuery(this.getSelectDataContractLogicallySQL());
        if (rs.next()) {
            return true;
        }
        else {
            return false;
        }
	} 
        
       
        public boolean isExitsContractDigestion(ConnectionWrapper con) throws SQLException
	{
        if (con == null) {
            return false;
        }
         
          ResultSetWrapper rs = con.executeQuery(this.getSelectDataContractDigestionSQL());
        if (rs.next()) {
            return true;
        }
        else {
            return false;
        }
	} 
        
         public String getSelectDataContractLogicallySQL()
	{
		return	"Select * from data_contract\n" +
                        "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                        "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
                        "and contract_no = " + SQLUtil.convertForSQL(this.getContractNo())+"\n" +
                        "and contract_detail_no = " + SQLUtil.convertForSQL(this.getContractDetailNo())+ "\n" +
                        "and delete_date  is null"  +  "\n" ;
                        
        }
          public String getSelectDataContractDigestionSQL()
	{
		return	"Select * from  data_contract_digestion\n" +
                        "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                        "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo())+ "\n" +
                        "and contract_no = " + SQLUtil.convertForSQL(this.getContractNo())+ "\n" +
                        "and contract_detail_no = " + SQLUtil.convertForSQL(this.getContractDetailNo())+ "\n" +
                        "and delete_date  is null" +  "\n"  ;
        }

        //An end add 20130307
        /**
	 * データをデータベースから読み込む。
	 */
	public void loadData()
	{
		this.clear();

		ConnectionWrapper con	=	SystemInfo.getConnection();

		try
		{
			ResultSetWrapper rs	=	con.executeQuery(this.getSelectDataContractSQL());

			if(rs.next())
                        {
                                this.setData(rs);
                        }

			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

        /**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException 例外
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
                this.setProductID(rs.getInt("product_id"));
		this.setProductNum1(rs.getDouble("product_num1"));
                this.setProductNum2(rs.getDouble("product_num2"));
		this.setProductValue(rs.getLong("product_value"));
                this.setSalesDate(rs.getDate("sales_date"));
                this.setValidDate(rs.getDate("valid_date"));
		this.setLimitDate(rs.getDate("limit_date"));
             //   this.setDeleteDate(rs.getDate("delete_date"));
		this.setChangeReason(rs.getString("change_reason"));
                MstStaff staff = new MstStaff();
                staff.setStaffName(0, rs.getString("staff_name1"));
                staff.setStaffName(1, rs.getString("staff_name2"));
                staff.setStaffNo(rs.getString("staff_no"));
                staff.setStaffID(rs.getInt("staff_id"));
                setStaff(staff);
	}

        /**
	 * 伝票明細データを取得するＳＱＬ文を取得する。
	 * @return 伝票明細データを取得するＳＱＬ文
	 */
	public String getSelectDataContractSQL()
	{
		String sql =    "select dc.product_id, ms.staff_no, ms.staff_id, coalesce(dc.product_num, 0) as product_num1, coalesce(dcd.product_num, 0) as product_num2, dc.product_value, sales_date,\n"
                                + "coalesce(ms.staff_name1, '') as staff_name1, coalesce(ms.staff_name2, '') as staff_name2, valid_date, limit_date, change_reason\n"
				+ "from data_contract dc\n"
                                + "    left join data_contract_digestion dcd\n"
                                + "    on dc.shop_id = dcd.shop_id and dc.slip_no = dcd.slip_no and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no\n"
                                + "    left join mst_staff ms\n"
                                + "    on dc.staff_id = ms.staff_id\n"
                                + "    inner join data_sales ds "
                                + "    on dc.shop_id = ds.shop_id and dc.slip_no = ds.slip_no \n"
                                + "where dc.shop_id = " + SQLUtil.convertForSQL(this.shop.getShopID()) + "\n"
                                + "and dc.slip_no = " + SQLUtil.convertForSQL(this.slipNo) + "\n"
                                + "and dc.contract_no = " + SQLUtil.convertForSQL(this.contractNo) + "\n"
                                + "and dc.contract_detail_no = " + SQLUtil.convertForSQL(this.contractDetailNo) + "\n";
                return sql;
	}
        
        /**
         * コース契約削除
         * @param con
         * @return
         * @throws SQLException
         */
        public boolean deleteDataContractLogically(ConnectionWrapper con,Integer shopId, Integer slipNo) throws SQLException
        {
            return 0 <= con.executeUpdate(this.getDeleteDataContractLogicallySQL(shopId,slipNo));
        }
        
        /**
         * コース契約履歴を登録するクエリを返す
         * @return
         */
        //Thanh start Add 2013/03
        private String getInsertDataContractSQL()
        {
            String sql = "INSERT INTO data_contract\n " 
                        + "(shop_id, \n"
                        + "slip_no, \n"
                        + "contract_no, \n" 
                        + "contract_detail_no, \n"                       
                        + "product_id, \n"
                        + "product_num, \n"
                        + "product_value, \n"
                        + "staff_id, \n"
                        + "contract_status, \n"
                        + "service_charge, \n"
                        + "request_value, \n"
                        + "valid_date, \n"
                        + "before_contract_shop_id, \n"
                        + "before_contract_no, \n"
                        + "insert_date, \n"
                        + "update_date, \n"
                        + "delete_date) \n"
                        + "VALUES (\n"
                        + SQLUtil.convertForSQL(this.shop.getShopID()) + ", \n"
                        + SQLUtil.convertForSQL(this.slipNo) + ", \n"
                        + SQLUtil.convertForSQL(this.contractNo) + ", \n"
                        + SQLUtil.convertForSQL(this.contractDetailNo) + ", \n"                       
                        + SQLUtil.convertForSQL(this.product.getProductID()) + ", \n"
                        + SQLUtil.convertForSQL(this.productNum) + ", \n"
                        + SQLUtil.convertForSQL(this.productValue) + ", \n"
                        + SQLUtil.convertForSQL(this.staff.getStaffID()) + ", \n"
                        + SQLUtil.convertForSQL(this.contractStatus) + ", \n"
                        + SQLUtil.convertForSQL(this.serviceCharge) + ", \n"
                        + SQLUtil.convertForSQL(this.requestValue) + ", \n"
                        + SQLUtil.convertForSQL(this.validDate) + ", \n"
                        + SQLUtil.convertForSQL(this.beforeContractShopID) + ", \n"
                        + SQLUtil.convertForSQL(this.beforeContractNo) + ", \n"
                        + "current_timestamp, \n"
                        + "current_timestamp, \n"
                        + "null)";
            return sql;
        }
        //Thanh end Add 2013/03
        
}
