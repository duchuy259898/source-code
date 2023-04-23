/*
 * StaffReportBeanProportionally.java
 *
 * Created on 2008/08/31, 19:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;

/**
 *
 * @author saito
 */
public class StaffReportBeanProportionally {
    
    private int ranking;
    private int staffId;
    private String staffName;

    private long totalKingaku;
    private long kingaku1;
    private long kingaku2;
    private long kingaku3;
    private long kingaku4;
    private long kingaku5;
    private long kingaku6;
    private long kingaku7;
    private long kingaku8;
    private long kingaku9;
    private long kingaku10;
    private long kingaku11;
    private long kingaku12;
    private long kingaku13;
    private long kingaku14;
    private long kingaku15;
    private long kingaku16;
    private long kingaku17;

    private double totalPoint;    
    private double point1;
    private double point2;
    private double point3;
    private double point4;
    private double point5;    
    private double point6;
    private double point7;
    private double point8;
    private double point9;
    private double point10;
    private double point11;
    private double point12;    
    private double point13;
    private double point14;
    private double point15;
    private double point16;
    private double point17;    
    
    /** Creates a new instance of StaffReportBeanProportionally */
    public StaffReportBeanProportionally() {
    }
    
    public void initField() {
	for (int i=1 ; i < 18 ; i++) {
	    this.setPoint(i, 0);
	    this.setKingaku(i, 0);
	}
    }
    
    public void setPoint(int no, double point) {
	switch (no) {
	    case 1: this.setPoint1(point); break;
	    case 2: this.setPoint2(point); break;
	    case 3: this.setPoint3(point); break;
	    case 4: this.setPoint4(point); break;
	    case 5: this.setPoint5(point); break;
	    case 6: this.setPoint6(point); break;
	    case 7: this.setPoint7(point); break;
	    case 8: this.setPoint8(point); break;
	    case 9: this.setPoint9(point); break;
	    case 10: this.setPoint10(point); break;
	    case 11: this.setPoint11(point); break;
	    case 12: this.setPoint12(point); break;
	    case 13: this.setPoint13(point); break;
	    case 14: this.setPoint14(point); break;
	    case 15: this.setPoint15(point); break;
	    case 16: this.setPoint16(point); break;
	    case 17: this.setPoint17(point); break;
	}
    }
    
    public void setKingaku(int no, long kingaku) {
	switch (no) {
	    case 1: this.setKingaku1(kingaku); break;
	    case 2: this.setKingaku2(kingaku); break;
	    case 3: this.setKingaku3(kingaku); break;
	    case 4: this.setKingaku4(kingaku); break;
	    case 5: this.setKingaku5(kingaku); break;
	    case 6: this.setKingaku6(kingaku); break;
	    case 7: this.setKingaku7(kingaku); break;
	    case 8: this.setKingaku8(kingaku); break;
	    case 9: this.setKingaku9(kingaku); break;
	    case 10: this.setKingaku10(kingaku); break;
	    case 11: this.setKingaku11(kingaku); break;
	    case 12: this.setKingaku12(kingaku); break;
	    case 13: this.setKingaku13(kingaku); break;
	    case 14: this.setKingaku14(kingaku); break;
	    case 15: this.setKingaku15(kingaku); break;
	    case 16: this.setKingaku16(kingaku); break;
	    case 17: this.setKingaku17(kingaku); break;
	}
    }

    public int getRanking() {
	    return ranking;
    }

    public void setRanking(int ranking) {
	    this.ranking = ranking;
    }

    public int getStaffId() {
	    return staffId;
    }

    public void setStaffId(int staffId) {
	    this.staffId = staffId;
    }
    
    public String getStaffName() {
	    return staffName;
    }

    public void setStaffName(String staffName) {
	    this.staffName = staffName;
    }

    public double getTotalPoint() {
	    return totalPoint;
    }

    public void setTotalPoint(double totalPoint) {
	    this.totalPoint = totalPoint;
    }

    public long getTotalKingaku() {
	    return totalKingaku;
    }

    public void setTotalKingaku(long totalKingaku) {
	    this.totalKingaku = totalKingaku;
    }

    public double getPoint1() {
	    return point1;
    }

    public void setPoint1(double point1) {
	    this.point1 = point1;
    }

    public long getKingaku1() {
	    return kingaku1;
    }

    public void setKingaku1(long kingaku1) {
	    this.kingaku1 = kingaku1;
    }

    public double getPoint2() {
	    return point2;
    }

    public void setPoint2(double point2) {
	    this.point2 = point2;
    }

    public long getKingaku2() {
	    return kingaku2;
    }

    public void setKingaku2(long kingaku2) {
	    this.kingaku2 = kingaku2;
    }

    public double getPoint3() {
	    return point3;
    }

    public void setPoint3(double point3) {
	    this.point3 = point3;
    }

    public long getKingaku3() {
	    return kingaku3;
    }

    public void setKingaku3(long kingaku3) {
	    this.kingaku3 = kingaku3;
    }

    public double getPoint4() {
	    return point4;
    }

    public void setPoint4(double point4) {
	    this.point4 = point4;
    }

    public long getKingaku4() {
	    return kingaku4;
    }

    public void setKingaku4(long kingaku4) {
	    this.kingaku4 = kingaku4;
    }


    public double getPoint5() {
	    return point5;
    }

    public void setPoint5(double point5) {
	    this.point5 = point5;
    }    

    public long getKingaku5() {
	    return kingaku5;
    }

    public void setKingaku5(long kingaku5) {
	    this.kingaku5 = kingaku5;
    }

    public double getPoint6() {
	    return point6;
    }

    public void setPoint6(double point6) {
	    this.point6 = point6;
    }      

    public long getKingaku6() {
	    return kingaku6;
    }

    public void setKingaku6(long kingaku6) {
	    this.kingaku6 = kingaku6;
    }

    public double getPoint7() {
	    return point7;
    }

    public void setPoint7(double point7) {
	    this.point7 = point7;
    }  

    public long getKingaku7() {
	    return kingaku7;
    }

    public void setKingaku7(long kingaku7) {
	    this.kingaku7 = kingaku7;
    }

    public double getPoint8() {
	    return point8;
    }

    public void setPoint8(double point8) {
	    this.point8 = point8;
    }  

    public long getKingaku8() {
	    return kingaku8;
    }

    public void setKingaku8(long kingaku8) {
	    this.kingaku8 = kingaku8;
    }

    public double getPoint9() {
	    return point9;
    }

    public void setPoint9(double point9) {
	    this.point9 = point9;
    }      

    public long getKingaku9() {
	    return kingaku9;
    }

    public void setKingaku9(long kingaku9) {
	    this.kingaku9 = kingaku9;
    }

    public double getPoint10() {
	    return point10;
    }

    public void setPoint10(double point10) {
	    this.point10 = point10;
    }  

    public long getKingaku10() {
	    return kingaku10;
    }

    public void setKingaku10(long kingaku10) {
	    this.kingaku10 = kingaku10;
    }

    public double getPoint11() {
	    return point11;
    }

    public void setPoint11(double point11) {
	    this.point11 = point11;
    }   

    public long getKingaku11() {
	    return kingaku11;
    }

    public void setKingaku11(long kingaku11) {
	    this.kingaku11 = kingaku11;
    }

    public double getPoint12() {
	    return point12;
    }

    public void setPoint12(double point12) {
	    this.point12 = point12;
    }   

    public long getKingaku12() {
	    return kingaku12;
    }

    public void setKingaku12(long kingaku12) {
	    this.kingaku12 = kingaku12;
    }

    public double getPoint13() {
	    return point13;
    }

    public void setPoint13(double point13) {
	    this.point13 = point13;
    }   

    public long getKingaku13() {
	    return kingaku13;
    }

    public void setKingaku13(long kingaku13) {
	    this.kingaku13 = kingaku13;
    }

    public double getPoint14() {
	    return point14;
    }

    public void setPoint14(double point14) {
	    this.point14 = point14;
    }   

    public long getKingaku14() {
	    return kingaku14;
    }

    public void setKingaku14(long kingaku14) {
	    this.kingaku14 = kingaku14;
    }

    public double getPoint15() {
	    return point15;
    }

    public void setPoint15(double point15) {
	    this.point15 = point15;
    } 

    public long getKingaku15() {
	    return kingaku15;
    }

    public void setKingaku15(long kingaku15) {
	    this.kingaku15 = kingaku15;
    }

    public double getPoint16() {
	    return point16;
    }

    public void setPoint16(double point16) {
	    this.point16 = point16;
    }   

    public long getKingaku16() {
	    return kingaku16;
    }

    public void setKingaku16(long kingaku16) {
	    this.kingaku16 = kingaku16;
    }

    public double getPoint17() {
	    return point17;
    }

    public void setPoint17(double point17) {
	    this.point17 = point17;
    }   

    public long getKingaku17() {
	    return kingaku17;
    }

    public void setKingaku17(long kingaku17) {
	    this.kingaku17 = kingaku17;
    }
}

