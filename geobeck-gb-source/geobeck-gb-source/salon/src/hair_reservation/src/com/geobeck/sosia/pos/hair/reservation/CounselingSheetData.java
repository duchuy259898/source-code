/*
 * CounselingSheetData.java
 *
 * Created on 2007/11/14, 10:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation;

import java.sql.*;
import java.util.*;

import net.sf.jasperreports.engine.data.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.hair.data.account.*;
import com.geobeck.sosia.pos.hair.data.reservation.*;

/**
 *
 * @author katagiri
 */
public class CounselingSheetData extends MstCustomer {

    private MstCustomer introducer = null;
    private ArrayList<MstCustomer> introducedCustomers = null;
    private DataSales lastSales = null;
    private DataSales last2ndSales = null;
    private DataSales last3rdSales = null;
    private DataReservation reservation = null;

    /**
     * Creates a new instance of CounselingSheetData
     */
    public CounselingSheetData() {
        super();
        this.setIntroducedCustomers(new ArrayList<MstCustomer>());
    }

    public MstCustomer getIntroducer() {
        return introducer;
    }

    public void setIntroducer(MstCustomer introducer) {
        this.introducer = introducer;
    }

    public String getIntroducerNo() {
        if (this.introducer != null) {
            return this.introducer.getCustomerNo();
        } else {
            return "";
        }
    }

    public String getIntroducerName() {
        if (this.introducer != null) {
            return this.introducer.getFullCustomerName();
        } else {
            return "";
        }
    }

    public ArrayList<MstCustomer> getIntroducedCustomers() {
        return introducedCustomers;
    }

    public void setIntroducedCustomers(ArrayList<MstCustomer> introducedCustomers) {
        this.introducedCustomers = introducedCustomers;
    }

    public DataSales getLastSales() {
        return lastSales;
    }

    public DataSales getLast2ndSales() {
        return last2ndSales;
    }

    public void setLast2ndSales(DataSales last2ndSales) {
        this.last2ndSales = last2ndSales;
    }

    public DataSales getLast3rdSales() {
        return last3rdSales;
    }

    public void setLast3rdSales(DataSales last3rdSales) {
        this.last3rdSales = last3rdSales;
    }

    public void setLastSales(DataSales lastSales) {
        this.lastSales = lastSales;
    }

    public java.util.Date getLastSalesDate() {
        if (this.getLastSales() != null) {
            return this.getLastSales().getSalesDate();
        }

        return null;
    }

    public java.util.Date getLast2ndSalesDate() {
        if (this.getLast2ndSales() != null) {
            return this.getLast2ndSales().getSalesDate();
        }

        return null;
    }

    public java.util.Date getLast3rdSalesDate() {
        if (this.getLast3rdSales() != null) {
            return this.getLast3rdSales().getSalesDate();
        }

        return null;
    }

    public Integer getVisitNum() {
        if (this.getLastSales() != null) {
            return this.getLastSales().getVisitNum();
        }

        return 0;
    }

    public String getLastShopName() {
        if (this.getLastSales() != null) {
            return this.getLastSales().getShop().getShopName();
        }

        return "";
    }

    public String getLast2ndShopName() {
        if (this.getLast2ndSales() != null) {
            return this.getLast2ndSales().getShop().getShopName();
        }

        return "";
    }

    public String getLast3rdShopName() {
        if (this.getLast3rdSales() != null) {
            return this.getLast3rdSales().getShop().getShopName();
        }

        return "";
    }

    public String getLastStaffName() {
        if (this.getLastSales() != null && this.getLastSales().getStaff() != null) {
            return this.getLastSales().getStaff().getFullStaffName();
        }

        return "";
    }

    public String getLast2ndStaffName() {
        if (this.getLast2ndSales() != null && this.getLast2ndSales().getStaff() != null) {
            return this.getLast2ndSales().getStaff().getFullStaffName();
        }

        return "";
    }

    public String getLast3rdStaffName() {
        if (this.getLast3rdSales() != null && this.getLast3rdSales().getStaff() != null) {
            return this.getLast3rdSales().getStaff().getFullStaffName();
        }

        return "";
    }

    public JRBeanCollectionDataSource getLastTechnicList() {
        return this.getLastProductList(0, 1);
    }

    public JRBeanCollectionDataSource getLastItemList() {
        return this.getLastProductList(0, 2);
    }

    public JRBeanCollectionDataSource getLast2ndTechnicList() {
        return this.getLastProductList(1, 1);
    }

    public JRBeanCollectionDataSource getLast2ndItemList() {
        return this.getLastProductList(1, 2);
    }

    public JRBeanCollectionDataSource getLast3rdTechnicList() {
        return this.getLastProductList(2, 1);
    }

    public JRBeanCollectionDataSource getLast3rdItemList() {
        return this.getLastProductList(2, 2);
    }

    public JRBeanCollectionDataSource getLastProductList(int idx, int productDivision) {
        ArrayList<DataSalesDetail> tempdv1 = new ArrayList<DataSalesDetail>();
        ArrayList<DataSalesDetail> tempdv2 = new ArrayList<DataSalesDetail>();

        if (idx == 0) {
            if (this.getLastSales() != null) {
                for (DataSalesDetail dsd : this.getLastSales()) {
                    if (dsd.getProductDivision() == 1) {
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 5) {
                        dsd.getProduct().setProductName(dsd.getCourse().getCourseName());
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 6) {
                        dsd.setProductNum(1);
                        dsd.getProduct().setProductName(dsd.getConsumptionCourse().getCourseName());
                        dsd.getProduct().setPrice(dsd.getConsumptionCourse().getPrice()); 
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 2) {
                        tempdv2.add(dsd);
                    }
                }
            }

        }
        if (idx == 1) {
            if (this.getLast2ndSales() != null) {
                for (DataSalesDetail dsd : this.getLast2ndSales()) {
                     if (dsd.getProductDivision() == 1) {
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 5) {
                        dsd.getProduct().setProductName(dsd.getCourse().getCourseName());
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 6) {
                        dsd.setProductNum(1);
                        dsd.getProduct().setProductName(dsd.getConsumptionCourse().getCourseName());
                        dsd.getProduct().setPrice(dsd.getConsumptionCourse().getPrice()); 
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 2) {
                        tempdv2.add(dsd);
                    }
                }
            }
        }

        if (idx == 2) {
            if (this.getLast3rdSales() != null) {
                for (DataSalesDetail dsd : this.getLast3rdSales()) {
                    if (dsd.getProductDivision() == 1) {
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 5) {
                        dsd.getProduct().setProductName(dsd.getCourse().getCourseName());
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 6) {
                        dsd.setProductNum(1);
                        dsd.getProduct().setProductName(dsd.getConsumptionCourse().getCourseName());
                        dsd.getProduct().setPrice(dsd.getConsumptionCourse().getPrice()); 
                        tempdv1.add(dsd);
                    }
                    if (dsd.getProductDivision() == 2) {
                        tempdv2.add(dsd);
                    }
                }
            }
        }




        int size = 0;
        size = tempdv1.size() - tempdv2.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                tempdv2.add(null);
            }
        } else if (size < 0) {
            for (int i = 0; i < size; i++) {
                tempdv1.add(null);
            }
        }

        if (productDivision == 1) {
            return new JRBeanCollectionDataSource(tempdv1);
        } else {
            return new JRBeanCollectionDataSource(tempdv2);
        }

    }

    public DataReservation getReservation() {
        return reservation;
    }

    public void setReservation(DataReservation reservation) {
        this.reservation = reservation;
    }

    public Integer getReservationNo() {
        if (this.getReservation() != null) {
            return this.getReservation().getReservationNo();
        }

        return null;
    }

    public Boolean getDesignated() {
        if (this.getReservation() != null) {
            return this.getReservation().getDesignated();
        }

        return false;
    }

    public Boolean getNewCustomer() {
        return this.getLastSales() == null;
    }

    public GregorianCalendar getVisitTime() {
        if (this.getReservation() != null) {
            return this.getReservation().getVisitTime();
        }

        return null;
    }

    public GregorianCalendar getStartTime() {
        if (this.getReservation() != null) {
            return this.getReservation().getStartTime();
        }

        return null;
    }

    public GregorianCalendar getLeaveTime() {
        if (this.getReservation() != null) {
            return this.getReservation().getLeaveTime();
        }

        return null;
    }

    public void setData(ResultSetWrapper rs) throws SQLException {
        super.setData(rs);
        this.getJob().setJobName(rs.getString("job_name"));
    }

    public boolean loadLastSales(ConnectionWrapper con, Integer type, java.util.Date salesDate) throws SQLException {
        boolean result = false;

        lastSales = null;
        last2ndSales = null;
        last3rdSales = null;

        ResultSetWrapper rs = con.executeQuery(this.getLastSalesKeySQL(salesDate));

        int i = 0;
        MstShop ms = null;
        while (rs.next()) {
            switch (i) {
                case 0:
                    lastSales = new DataSales(type);

                    ms = new MstShop();
                    ms.setShopID(rs.getInt("shop_id"));
                    lastSales.setShop(ms);
                    lastSales.setSlipNo(rs.getInt("slip_no"));
                    break;
                case 1:
                    last2ndSales = new DataSales(type);

                    ms = new MstShop();
                    ms.setShopID(rs.getInt("shop_id"));
                    last2ndSales.setShop(ms);
                    last2ndSales.setSlipNo(rs.getInt("slip_no"));
                    break;
                case 2:

                    last3rdSales = new DataSales(type);

                    ms = new MstShop();
                    ms.setShopID(rs.getInt("shop_id"));
                    last3rdSales.setShop(ms);
                    last3rdSales.setSlipNo(rs.getInt("slip_no"));
                    break;
            }
            i++;

        }

        rs.close();

        if (lastSales != null && !lastSales.loadAll(con)) {
            result = false;
        } else if (last2ndSales != null && !last2ndSales.loadAll(con)) {
            result = false;
        } else if (last3rdSales != null && !last3rdSales.loadAll(con)) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    private String getLastSalesKeySQL(java.util.Date salesDate) {
        // vtbphuong 20140108 start edit Request #18717
//		return	"select ds.shop_id, ds.slip_no\n" +
//				"from data_sales ds\n" +
//				"where ds.customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n" +
//				"and ds.sales_date = (\n" +
//				"select max(ds.sales_date) as last_sales_date\n" +
//				"from data_sales ds\n" +
//				"where ds.customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n" +
//				"and ds.sales_date < " + SQLUtil.convertForSQLDateOnly(salesDate) + ")\n";
        return "select ds.shop_id, ds.slip_no\n"
                + "from data_sales ds\n"
                + "where ds.customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n"
                + "and ds.delete_date is null\n"
                + "and ds.sales_date in (\n"
                + "                 select ds.sales_date as last_sales_date\n"
                + "                 from data_sales ds\n"
                + "                 where ds.customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n"
                + "                 and ds.sales_date < " + SQLUtil.convertForSQLDateOnly(salesDate)
                 +"                 and ds.delete_date is null  "
                + "                 order by ds.sales_date desc limit 3 )\n"
               
                + " order by ds.sales_date desc \n ";

        // vtbphuong 20140108 end edit Request #18717
    }

    private int getIntoroducedCustomersNum() {
        return introducedCustomers.size();
    }

    public String getIntoroducedCustomerNo1() {
        if (getIntoroducedCustomersNum() > 0) {
            MstCustomer mc = introducedCustomers.get(0);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo2() {
        if (getIntoroducedCustomersNum() > 1) {
            MstCustomer mc = introducedCustomers.get(1);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo3() {
        if (getIntoroducedCustomersNum() > 2) {
            MstCustomer mc = introducedCustomers.get(2);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo4() {
        if (getIntoroducedCustomersNum() > 3) {
            MstCustomer mc = introducedCustomers.get(3);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo5() {
        if (getIntoroducedCustomersNum() > 4) {
            MstCustomer mc = introducedCustomers.get(4);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo6() {
        if (getIntoroducedCustomersNum() > 5) {
            MstCustomer mc = introducedCustomers.get(5);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo7() {
        if (getIntoroducedCustomersNum() > 6) {
            MstCustomer mc = introducedCustomers.get(6);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo8() {
        if (getIntoroducedCustomersNum() > 7) {
            MstCustomer mc = introducedCustomers.get(7);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerNo9() {
        if (getIntoroducedCustomersNum() == 9) {
            MstCustomer mc = introducedCustomers.get(8);
            return mc.getCustomerNo();
        }
        return "";
    }

    public String getIntoroducedCustomerName1() {
        if (getIntoroducedCustomersNum() > 0) {
            MstCustomer mc = introducedCustomers.get(0);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName2() {
        if (getIntoroducedCustomersNum() > 1) {
            MstCustomer mc = introducedCustomers.get(1);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName3() {
        if (getIntoroducedCustomersNum() > 2) {
            MstCustomer mc = introducedCustomers.get(2);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName4() {
        if (getIntoroducedCustomersNum() > 3) {
            MstCustomer mc = introducedCustomers.get(3);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName5() {
        if (getIntoroducedCustomersNum() > 4) {
            MstCustomer mc = introducedCustomers.get(4);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName6() {
        if (getIntoroducedCustomersNum() > 5) {
            MstCustomer mc = introducedCustomers.get(5);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName7() {
        if (getIntoroducedCustomersNum() > 6) {
            MstCustomer mc = introducedCustomers.get(6);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName8() {
        if (getIntoroducedCustomersNum() > 7) {
            MstCustomer mc = introducedCustomers.get(7);
            return mc.getFullCustomerName();
        }
        return "";
    }

    public String getIntoroducedCustomerName9() {
        if (getIntoroducedCustomersNum() == 9) {
            MstCustomer mc = introducedCustomers.get(8);
            return mc.getFullCustomerName();
        } else if (getIntoroducedCustomersNum() > 9) {
            return "‘¼ " + (getIntoroducedCustomersNum() - 8) + "–¼";
        }
        return "";
    }
}
