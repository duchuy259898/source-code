package com.geobeck.sosia.pos.report.bean;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EffectIndicatorAnalysisBean {

	// V‹KÄ—ˆl”
	private Long newReappearance = 0L;
	// V‹K“ü‹ql”
	private Long newTotal = 0L;
	// Ä—ˆl”
	private Long reappearenceTotal = 0L;
	// “ü‹ql”
	private Long countTotal = 0L;
	// —\–ñ‚ª‚ ‚èdata_reservation.next_flag=1‚ÌŒ”
	private int nextTimeReserve = 0;
	// —ˆ“XŒÚ‹q”
	private int comeShopTotal = 0;
	// data_reservation.status=3‚ÌŒ”
	private int reserveSuccess = 0;
	// “–Œ‚Ì—\–ñŒ”
	private int reserveTotal = 0;

    // ‹Zp‹q”
    private int technicTotalNum = 0;
    // ¤•i‚Ì‚İ‹q
    private int itemTotalNum = 0;
    // ¤•iw“üÒ
    private int itemBuyNum = 0;
    // Ğ‰î”
    private int introducerNum = 0;
    // ‹Zp”„ã
    private int technicTotal = 0;
    // ¤•i”„ã
    private int itemTotal = 0;
    // ‘S‘ÌŠ„ˆø
    private int allDiscountPrice = 0;
    // w–¼‹q”
    private int chargeNum = 0;
    // V‹K‹q”
    private int newVisitNum = 0;

    // ‰c‹Æ“ú”
    private int workDayNum = 0;
	// ƒXƒ^ƒbƒt”
    private double staffNum = 0;

	// ‹ZpIDl”ƒŠƒXƒg
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
		System.out.println("V‹KÄ—ˆ—¦:" + this.newReappearance + "/" + this.newTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getExistComeRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.countTotal != null && this.countTotal > 0) {
			bd = new BigDecimal(Double.parseDouble(this.reappearenceTotal.toString()) / Double.parseDouble(this.countTotal.toString()) * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("Šù‘¶Ä—ˆ—¦:" + this.reappearenceTotal + "/" + this.countTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getNextTimeReserveRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.comeShopTotal > 0) {
			bd = new BigDecimal((double)this.nextTimeReserve / (double)this.comeShopTotal * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("Ÿ‰ñ—\–ñ—¦:" + this.nextTimeReserve + "/" + this.comeShopTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getReserveSuccessRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.reserveTotal > 0) {
			bd = new BigDecimal((double)this.reserveSuccess / (double)this.reserveTotal * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("—\–ñ¬–ñ—¦:" + this.reserveSuccess + "/" + this.reserveTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public int getTotalNum() {
		int totalNum = this.getTechnicTotalNum() + this.getItemTotalNum();
		System.out.println("‘‹q”=‹Zp‹q”+¤•i‚Ì‚İ‹q:" +  this.getTechnicTotalNum() + "+" + this.getItemTotalNum() + "=" + totalNum);
		return totalNum;
	}

	public BigDecimal getShopSaleRate() {
		BigDecimal bd = BigDecimal.ZERO;
		int totalNum = this.getTotalNum();

		if (totalNum > 0) {
			bd = new BigDecimal((double)this.itemBuyNum / (double)totalNum * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("“X”Ì”ä—¦:" + this.itemBuyNum + "/" + totalNum + "=" + retVal.toString() + "%");
		return retVal;
	}

	public int getAllTotal() {
		int allTotal = this.getTechnicTotal() + this.getItemTotal() - this.getAllDiscountPrice();
		System.out.println("‘”„ã=‹Zp”„ã+¤•i”„ã-‘S‘ÌŠ„ˆø:" + this.getTechnicTotal() + "+" + this.getItemTotal() + "-" + this.getAllDiscountPrice() + "=" + allTotal);
		return allTotal;
	}

	public BigDecimal getSalesOneStaff() {
		BigDecimal bd = BigDecimal.ZERO;
		double staffNum = this.getStaffNum();

		if (staffNum > 0) {
			bd = new BigDecimal((double)this.getTechnicTotal() / staffNum);
		}

		bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("ƒXƒ^ƒbƒt1l“–‚½‚è”„ã:" + this.getTechnicTotal() + "/" + staffNum + "=" + bd.toString());
		return bd;
	}
}
