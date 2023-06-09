package com.geobeck.sosia.pos.report.bean;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EffectIndicatorAnalysisBean {

	// 新規再来人数
	private Long newReappearance = 0L;
	// 新規入客人数
	private Long newTotal = 0L;
	// 再来人数
	private Long reappearenceTotal = 0L;
	// 入客人数
	private Long countTotal = 0L;
	// 予約がありdata_reservation.next_flag=1の件数
	private int nextTimeReserve = 0;
	// 来店顧客数
	private int comeShopTotal = 0;
	// data_reservation.status=3の件数
	private int reserveSuccess = 0;
	// 当月の予約件数
	private int reserveTotal = 0;

    // 技術客数
    private int technicTotalNum = 0;
    // 商品のみ客
    private int itemTotalNum = 0;
    // 商品購入者
    private int itemBuyNum = 0;
    // 紹介数
    private int introducerNum = 0;
    // 技術売上
    private int technicTotal = 0;
    // 商品売上
    private int itemTotal = 0;
    // 全体割引
    private int allDiscountPrice = 0;
    // 指名客数
    private int chargeNum = 0;
    // 新規客数
    private int newVisitNum = 0;

    // 営業日数
    private int workDayNum = 0;
	// スタッフ数
    private double staffNum = 0;

	// 技術ID人数リスト
    private ArrayList<BusinessReportBean> techIdNumList;

	public int getTechnicTotalNum() {
		return this.technicTotalNum;
	}

	public void setTechnicTotalNum(int technicTotalNum) {
		this.technicTotalNum = technicTotalNum;
	}

	public int getItemTotalNum() {
		return this.itemTotalNum;
	}

	public void setItemTotalNum(int itemTotalNum) {
		this.itemTotalNum = itemTotalNum;
	}

	public int getItemBuyNum() {
		return this.itemBuyNum;
	}

	public void setItemBuyNum(int itemBuyNum) {
		this.itemBuyNum = itemBuyNum;
	}

	public int getIntroducerNum() {
		return this.introducerNum;
	}

	public void setIntroducerNum(int introducerNum) {
		this.introducerNum = introducerNum;
	}

	public int getTechnicTotal() {
		return this.technicTotal;
	}

	public void setTechnicTotal(int technicTotal) {
		this.technicTotal = technicTotal;
	}

	public int getItemTotal() {
		return this.itemTotal;
	}

	public void setItemTotal(int itemTotal) {
		this.itemTotal = itemTotal;
	}

	public int getAllDiscountPrice() {
		return this.allDiscountPrice;
	}

	public void setAllDiscountPrice(int allDiscountPrice) {
		this.allDiscountPrice = allDiscountPrice;
	}

	public int getChargeNum() {
		return this.chargeNum;
	}

	public void setChargeNum(int chargeNum) {
		this.chargeNum = chargeNum;
	}

	public int getNewVisitNum() {
		return this.newVisitNum;
	}

	public void setNewVisitNum(int newVisitNum) {
		this.newVisitNum = newVisitNum;
	}

	public int getReserveSuccess() {
		return this.reserveSuccess;
	}

	public void setReserveSuccess(int reserveSuccess) {
		this.reserveSuccess = reserveSuccess;
	}

	public int getReserveTotal() {
		return this.reserveTotal;
	}

	public void setReserveTotal(int reserveTotal) {
		this.reserveTotal = reserveTotal;
	}

	public int getNextTimeReserve() {
		return this.nextTimeReserve;
	}

	public void setNextTimeReserve(int nextTimeReserve) {
		this.nextTimeReserve = nextTimeReserve;
	}

	public int getComeShopTotal() {
		return this.comeShopTotal;
	}

	public void setComeShopTotal(int comeShopTotal) {
		this.comeShopTotal = comeShopTotal;
	}

	public Long getNewReappearance() {
		return this.newReappearance;
	}

	public void setNewReappearance(Long newReappearance) {
		this.newReappearance = newReappearance;
	}

	public Long getNewTotal() {
		return this.newTotal;
	}

	public void setNewTotal(Long newTotal) {
		this.newTotal = newTotal;
	}

	public Long getReappearenceTotal() {
		return this.reappearenceTotal;
	}

	public void setReappearenceTotal(Long reappearenceTotal) {
		this.reappearenceTotal = reappearenceTotal;
	}

	public Long getCountTotal() {
		return this.countTotal;
	}

	public void setCountTotal(Long countTotal) {
		this.countTotal = countTotal;
	}

	public int getWorkDayNum() {
		return this.workDayNum;
	}

	public void setWorkDayNum(int workDayNum) {
		this.workDayNum = workDayNum;
	}

	public double getStaffNum() {
		return this.staffNum;
	}

	public void setStaffNum(double staffNum) {
		this.staffNum = staffNum;
	}

	public ArrayList<BusinessReportBean> getTechIdNumList() {
		return this.techIdNumList;
	}

	public void setTechIdNumList(ArrayList<BusinessReportBean> techIdNumList) {
		this.techIdNumList = techIdNumList;
	}

	public BigDecimal getNewComeRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.newTotal != null && this.newTotal > 0) {
			bd = new BigDecimal(Double.parseDouble(this.newReappearance.toString()) / Double.parseDouble(this.newTotal.toString()) * 100);
		}
		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("新規再来率:" + this.newReappearance + "/" + this.newTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getExistComeRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.countTotal != null && this.countTotal > 0) {
			bd = new BigDecimal(Double.parseDouble(this.reappearenceTotal.toString()) / Double.parseDouble(this.countTotal.toString()) * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("既存再来率:" + this.reappearenceTotal + "/" + this.countTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getNextTimeReserveRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.comeShopTotal > 0) {
			bd = new BigDecimal((double)this.nextTimeReserve / (double)this.comeShopTotal * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("次回予約率:" + this.nextTimeReserve + "/" + this.comeShopTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getReserveSuccessRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.reserveTotal > 0) {
			bd = new BigDecimal((double)this.reserveSuccess / (double)this.reserveTotal * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("予約成約率:" + this.reserveSuccess + "/" + this.reserveTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public int getTotalNum() {
		int totalNum = this.getTechnicTotalNum() + this.getItemTotalNum();
		System.out.println("総客数=技術客数+商品のみ客:" +  this.getTechnicTotalNum() + "+" + this.getItemTotalNum() + "=" + totalNum);
		return totalNum;
	}

	public BigDecimal getShopSaleRate() {
		BigDecimal bd = BigDecimal.ZERO;
		int totalNum = this.getTotalNum();

		if (totalNum > 0) {
			bd = new BigDecimal((double)this.itemBuyNum / (double)totalNum * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("店販比率:" + this.itemBuyNum + "/" + totalNum + "=" + retVal.toString() + "%");
		return retVal;
	}

	public int getAllTotal() {
		int allTotal = this.getTechnicTotal() + this.getItemTotal() - this.getAllDiscountPrice();
		System.out.println("総売上=技術売上+商品売上-全体割引:" + this.getTechnicTotal() + "+" + this.getItemTotal() + "-" + this.getAllDiscountPrice() + "=" + allTotal);
		return allTotal;
	}

	public BigDecimal getSalesOneStaff() {
		BigDecimal bd = BigDecimal.ZERO;
		double staffNum = this.getStaffNum();

		if (staffNum > 0) {
			bd = new BigDecimal((double)this.getTechnicTotal() / staffNum);
		}

		bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("スタッフ1人当たり売上:" + this.getTechnicTotal() + "/" + staffNum + "=" + bd.toString());
		return bd;
	}
}
