/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 販売・消化コースのList
 *
 * @author s_furukawa
 */
public class ConsumptionCourse {

    protected ConsumptionCourseClass consumptionCourseClass;
    protected Integer contractStatus;
    /**
     * ショップID
     */
    protected Integer shopId;
    /**
     * 伝票番号
     */
    protected Integer slipNo;
    /**
     * コースID
     */
    protected Integer courseId;
    /**
     * コース名
     */
    protected String courseName;
    /**
     * 価格
     */
    protected Long price;
    /**
     * 元の価格
     */
    protected Long basePrice;
    /**
     * 施術時間
     */
    protected Integer operationTime;
    /**
     * 有効期限使用有無
     */
    protected boolean isPraiseTime;
    /**
     * 有効期限
     */
    protected Integer praiseTimeLimit;
    /**
     * 消化回数
     */
    protected Integer num;
    /**
     * 消化した回数
     */
    protected Double consumptionNum;
    /**
     * 残消化回数
     */
    protected Double consumptionRestNum;
    /**
     * 契約店舗ID
     */
    private Integer contractShopId = null;
    /**
     * 契約No
     */
    protected Integer contractNo;
    /**
     * 契約詳細No
     */
    protected Integer contractDetailNo;
    /**
     * スタッフID
     */
    protected Integer staffId;
    /**
     * 仮契約番号
     */
    protected String tmpContractNo;
    /**
     * 購入日時
     */
    protected Date salesDate;
    protected Date delDate;
    //Start add 20121202 lvut 
    protected Date validDate;
    //End add 20121202 lvut 

    /**
     * コンストラクタ
     */
    public ConsumptionCourse() {
    }

    public ConsumptionCourseClass getConsumptionCourseClass() {
        return consumptionCourseClass;
    }

    public void setConsumptionCourseClass(ConsumptionCourseClass consumptionCourseClass) {
        this.consumptionCourseClass = consumptionCourseClass;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    public Long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Long basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isIsPraiseTime() {
        return isPraiseTime;
    }

    public void setPraiseTime(boolean isPraiseTime) {
        this.isPraiseTime = isPraiseTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Integer operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getPraiseTimeLimit() {
        return praiseTimeLimit;
    }

    public void setPraiseTimeLimit(Integer praiseTimeLimit) {
        this.praiseTimeLimit = praiseTimeLimit;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Double getConsumptionNum() {
        return consumptionNum;
    }

    public void setConsumptionNum(Double consumptionNum) {
        this.consumptionNum = consumptionNum;
    }

    public Double getConsumptionRestNum() {
        return this.consumptionRestNum;
    }

    public void setConsumptionRestNum(Double consumptionRestNum) {
        this.consumptionRestNum = consumptionRestNum;
    }

    public Integer getContractDetailNo() {
        return contractDetailNo;
    }

    public void setContractDetailNo(Integer contractDetailNo) {
        this.contractDetailNo = contractDetailNo;
    }

    public Integer getContractShopId() {
        return contractShopId;
    }

    public void setContractShopId(Integer contractShopId) {
        this.contractShopId = contractShopId;
    }

    public Integer getContractNo() {
        return contractNo;
    }

    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

    public String getTmpContractNo() {
        return tmpContractNo;
    }

    public void setTmpContractNo(String tmpContractNo) {
        this.tmpContractNo = tmpContractNo;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public boolean hasConsumptionRest() {
        if (getConsumptionRestNum() > 0) {
            return true;
        }
        return false;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }
    //Start add 20131202 lvut 

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }
    //End add 20131202 lvut 

    /**
     * 販売データを設定する。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setSlipNo(rs.getInt("slip_no"));
        this.setCourseId(rs.getInt("course_id"));
        this.setCourseName(rs.getString("course_name"));
//            this.setDisplaySeq(rs.getInt("display_seq"));
//		this.setBaseDisplaySeq(rs.getInt("base_display_seq"));
        this.setOperationTime(rs.getInt("operation_time"));
        this.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        this.setPraiseTime(rs.getBoolean("is_praise_time"));
        //消化回数
        this.setNum(rs.getInt("num"));
        this.setConsumptionNum(rs.getDouble("consumption_num"));
//            this.setPrice(rs.getLong("product_value"));
//            this.setBasePrice(rs.getLong("price"));
        //価格はコースの販売価格/消化回数をセット
        long price = new BigDecimal(rs.getLong("product_value")).divide(new BigDecimal(getNum().longValue()), 0, RoundingMode.UP).longValue();
        this.setPrice(price);
        long basePrice = new BigDecimal(rs.getLong("price")).divide(new BigDecimal(getNum().longValue()), 0, RoundingMode.UP).longValue();
        this.setBasePrice(basePrice);
        //コース契約履歴の店舗IDをセット
        this.setContractShopId(rs.getInt("shop_id"));
        //コース契約履歴の契約Noをセット
        this.setContractNo(rs.getInt("contract_no"));
        //コース契約履歴の契約詳細Noをセット
        this.setContractDetailNo(rs.getInt("contract_detail_no"));
        this.setDelDate(rs.getDate("delete_date"));


    }

    /**
     * 販売データを設定する。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setDataWithSalesDate(ResultSetWrapper rs) throws SQLException {
        this.setSlipNo(rs.getInt("slip_no"));
        this.setCourseId(rs.getInt("course_id"));
        this.setCourseName(rs.getString("course_name"));
//            this.setDisplaySeq(rs.getInt("display_seq"));
//		this.setBaseDisplaySeq(rs.getInt("base_display_seq"));
        this.setOperationTime(rs.getInt("operation_time"));
        this.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        this.setPraiseTime(rs.getBoolean("is_praise_time"));
        //消化回数
        this.setNum(rs.getInt("num"));
        this.setConsumptionNum(rs.getDouble("consumption_num"));
//            this.setPrice(rs.getLong("product_value"));
//            this.setBasePrice(rs.getLong("price"));
        //価格はコースの販売価格/消化回数をセットz

        //default product_num = 1
        long price = new BigDecimal(rs.getLong("product_value")).divide(new BigDecimal(1), 0, RoundingMode.UP).longValue();
        try {
            price = new BigDecimal(rs.getLong("product_value")).divide(new BigDecimal(getNum().longValue()), 0, RoundingMode.UP).longValue();
        } catch (Exception e) {
        }
        long basePrice = new BigDecimal(rs.getLong("price")).divide(new BigDecimal(1), 0, RoundingMode.UP).longValue();

        this.setPrice(price);
        try {
            basePrice = new BigDecimal(rs.getLong("price")).divide(new BigDecimal(getNum().longValue()), 0, RoundingMode.UP).longValue();
        } catch (Exception e) {
        }
        this.setBasePrice(basePrice);
        //コース契約履歴の店舗IDをセット
        this.setContractShopId(rs.getInt("shop_id"));
        //コース契約履歴の契約Noをセット
        this.setContractNo(rs.getInt("contract_no"));
        //コース契約履歴の契約詳細Noをセット
        this.setContractDetailNo(rs.getInt("contract_detail_no"));
        //購入日時をセット
        this.setSalesDate(rs.getDate("sales_date"));
    }

    /**
     * 伝票番号からコース消化履歴を取得する
     *
     * @param con
     * @param shopId
     * @param slipNo
     * @return
     * @throws SQLException
     */
    public static List<ConsumptionCourse> getConsumptionCourseList(ConnectionWrapper con, Integer shopId, Integer slipNo) throws SQLException {
        List<ConsumptionCourse> list = new ArrayList<ConsumptionCourse>();

        ResultSetWrapper rs = con.executeQuery(getSelectSQL(shopId, slipNo));

        while (rs.next()) {
            ConsumptionCourse consumptionCourse = new ConsumptionCourse();
            consumptionCourse.setData(rs);
            consumptionCourse.setStaffId(rs.getInt("staff_id"));

            ConsumptionCourseClass consumptionCouresClass = new ConsumptionCourseClass();
            consumptionCouresClass.setShopId(shopId);
            consumptionCouresClass.setSlipNo(slipNo);
            consumptionCouresClass.setCourseClassId(rs.getInt("course_class_id"));
            consumptionCouresClass.setCourseClassName(rs.getString("course_class_name"));
            //Luc start add 20150817 #41948
            consumptionCouresClass.setShopCategoryID(rs.getInt("shop_category_id"));
            //Luc end add 20150817 #41948

            consumptionCourse.setConsumptionCourseClass(consumptionCouresClass);
            consumptionCourse.setContractStatus(rs.getInt("contract_status"));

            list.add(consumptionCourse);
        }

        rs.close();

        for (ConsumptionCourse consumptionCourse : list) {
            //IVS NNTUAN START EDIT 20131018
            //rs	=	con.executeQuery(getConsumptionSQL(shopId, consumptionCourse.getContractNo(), consumptionCourse.getContractDetailNo()));
            rs = con.executeQuery(getConsumptionSQL(consumptionCourse.getContractShopId(), consumptionCourse.getContractNo(), consumptionCourse.getContractDetailNo()));
            //IVS NNTUAN END EDIT 20131018
            if (rs.next()) {
                consumptionCourse.setConsumptionRestNum(consumptionCourse.getNum() - rs.getDouble("consumption_num"));
            }
            rs.close();
        }

        return list;
    }

    public static String getSelectSQL(Integer shopId, Integer slipNo) {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dc.shop_id");
        //Start add 20131105 lvut 
        sql.append("     ,dc.contract_status");
        sql.append("     ,dc.delete_date");
        //End add 20131105 lvut 
        sql.append("     ,dcd.slip_no");
        sql.append("     ,dcd.contract_no");
        sql.append("     ,dcd.contract_detail_no");
        sql.append("     ,mcc.course_class_id");
        sql.append("     ,mcc.course_class_name");
        //Luc start add 20150817 #41948
        sql.append("     ,mcc.shop_category_id");
        //Luc start add 20150817 #41948
        sql.append("     ,dc.product_id as course_id");
        sql.append("     ,mc.course_name");
        sql.append("     ,mc.operation_time");
        sql.append("     ,mc.praise_time_limit");
        sql.append("     ,mc.is_praise_time");
        sql.append("     ,dc.product_num as num");
        sql.append("     ,dcd.product_num as consumption_num");
        sql.append("     ,dc.product_value");
        sql.append("     ,dc.product_value as price");
        sql.append("     ,dcd.staff_id");
        sql.append(" from");
        sql.append("     data_contract_digestion dcd");
        sql.append("         inner join data_contract dc");
        sql.append("                 on dcd.contract_shop_id = dc.shop_id");
        sql.append("                and dcd.contract_no = dc.contract_no");
        sql.append("                and dcd.contract_detail_no = dc.contract_detail_no");
        sql.append("         inner join mst_course mc");
        sql.append("                 on dc.product_id = mc.course_id");
        sql.append("         inner join mst_course_class mcc");
        sql.append("                 on mc.course_class_id = mcc.course_class_id");
        sql.append(" where");
        sql.append("         dcd.delete_date is null");
        sql.append("     and dcd.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("     and dcd.slip_no = " + SQLUtil.convertForSQL(slipNo));
        sql.append(" order by");
        sql.append("      dcd.contract_no");
        sql.append("     ,dcd.contract_detail_no");

        return sql.toString();
    }

    /**
     * 契約番号、契約詳細番号から契約したコースの消化回数を取得する
     *
     * @param shopId
     * @param courseId
     * @return
     */
    public static String getConsumptionSQL(Integer shopId, Integer contractNo, Integer contractDetailNo) {
        String sql = "select coalesce(sum(dcd.product_num), 0) as consumption_num\n"
                + "from data_contract dc\n"
                + "inner join data_contract_digestion dcd\n"
                //IVS NNTUAN START EDIT 20131018
                // + "on dc.shop_id = dcd.shop_id and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no\n"
                + "on dc.shop_id = dcd.contract_shop_id and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no\n"
                //IVS NNTUAN START EDIT 20131018
                + "where dc.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                + "and dc.contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
                + "and dc.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n";

        return sql;
    }

    /**
     * 伝票番号からコース消化履歴を取得する
     *
     * @param con
     * @param shopId
     * @param slipNo
     * @return
     * @throws SQLException
     */
    public static boolean existConsumption(ConnectionWrapper con, Integer shopId, Integer contractNo, Integer contractDetailNo) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(existConsumptionSQL(shopId, contractNo, contractDetailNo));

        if (rs.next()) {
            if (rs.getInt("count") > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param shopId
     * @param contractNo
     * @param contractDetailNo
     * @return
     */
    public static String existConsumptionSQL(Integer shopId, Integer contractNo, Integer contractDetailNo) {
        String sql = "select count(*) as count\n"
                + "from data_contract dc\n"
                + "inner join data_contract_digestion dcd\n"
                //IVS NNTUAN START EDIT 20131028
                //+ "on dc.shop_id = dcd.shop_id and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no\n"
                + "on dc.shop_id = dcd.contract_shop_id and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no\n"
                //IVS NNTUAN END EDIT 20131028
                + "where dc.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                + "and dc.contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
                + "and dc.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n";

        return sql;
    }

    public String toString() {
        return getCourseName();
    }

    /**
     *
     * @param con
     * @param shopId
     * @param contractNo
     * @param contractDetail
     * @return
     * @throws SQLException
     */
    public static ConsumptionCourse getConsumptionNum(ConnectionWrapper con, Integer shopId, Integer contractNo, Integer contractDetail) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getConsumptionNumSQL(shopId, contractNo, contractDetail));
        ConsumptionCourse consumptionCourse = new ConsumptionCourse();
        if (rs.next()) {
            consumptionCourse.setPrice(rs.getLong("product_value"));
            consumptionCourse.setNum(rs.getInt("product_num"));
            consumptionCourse.setConsumptionNum(rs.getDouble("consumption_num"));
            consumptionCourse.setConsumptionRestNum(consumptionCourse.getNum() - consumptionCourse.getConsumptionNum());
        }


        return consumptionCourse;
    }

    /**
     *
     * @param shopId
     * @param contractNo
     * @param contractDetailNo
     * @return
     */
    public static String getConsumptionNumSQL(Integer shopId, Integer contractNo, Integer contractDetailNo) {
        String sql = "select dc.product_value, dc.product_num, coalesce(sum(dcd.product_num), 0) as consumption_num\n"
                + "from data_contract dc\n"
                + "left outer join data_contract_digestion dcd\n"
                //IVS NNTUAN START EDIT 20131028
                //+ "on dc.shop_id = dcd.shop_id and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no\n"
                + "on dc.shop_id = dcd.contract_shop_id and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no\n"
                //IVS NNTUAN END EDIT 20131028
                + "where dc.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
                + "and dc.contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
                + "and dc.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n"
                + "group by dc.product_value, dc.product_num";

        return sql;
    }
}
