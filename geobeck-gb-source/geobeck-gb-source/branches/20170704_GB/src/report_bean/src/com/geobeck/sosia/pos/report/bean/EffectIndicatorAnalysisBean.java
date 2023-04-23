package com.geobeck.sosia.pos.report.bean;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EffectIndicatorAnalysisBean {

	// �V�K�ė��l��
	private Long newReappearance = 0L;
	// �V�K���q�l��
	private Long newTotal = 0L;
	// �ė��l��
	private Long reappearenceTotal = 0L;
	// ���q�l��
	private Long countTotal = 0L;
	// �\�񂪂���data_reservation.next_flag=1�̌���
	private int nextTimeReserve = 0;
	// ���X�ڋq��
	private int comeShopTotal = 0;
	// data_reservation.status=3�̌���
	private int reserveSuccess = 0;
	// �����̗\�񌏐�
	private int reserveTotal = 0;

    // �Z�p�q��
    private int technicTotalNum = 0;
    // ���i�̂݋q
    private int itemTotalNum = 0;
    // ���i�w����
    private int itemBuyNum = 0;
    // �Љ
    private int introducerNum = 0;
    // �Z�p����
    private int technicTotal = 0;
    // ���i����
    private int itemTotal = 0;
    // �S�̊���
    private int allDiscountPrice = 0;
    // �w���q��
    private int chargeNum = 0;
    // �V�K�q��
    private int newVisitNum = 0;

    // �c�Ɠ���
    private int workDayNum = 0;
	// �X�^�b�t��
    private double staffNum = 0;

	// �Z�pID�l�����X�g
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
		System.out.println("�V�K�ė���:" + this.newReappearance + "/" + this.newTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getExistComeRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.countTotal != null && this.countTotal > 0) {
			bd = new BigDecimal(Double.parseDouble(this.reappearenceTotal.toString()) / Double.parseDouble(this.countTotal.toString()) * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("�����ė���:" + this.reappearenceTotal + "/" + this.countTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getNextTimeReserveRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.comeShopTotal > 0) {
			bd = new BigDecimal((double)this.nextTimeReserve / (double)this.comeShopTotal * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("����\��:" + this.nextTimeReserve + "/" + this.comeShopTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public BigDecimal getReserveSuccessRate() {
		BigDecimal bd = BigDecimal.ZERO;
		if (this.reserveTotal > 0) {
			bd = new BigDecimal((double)this.reserveSuccess / (double)this.reserveTotal * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("�\�񐬖�:" + this.reserveSuccess + "/" + this.reserveTotal + "=" + retVal.toString() + "%");
		return retVal;
	}

	public int getTotalNum() {
		int totalNum = this.getTechnicTotalNum() + this.getItemTotalNum();
		System.out.println("���q��=�Z�p�q��+���i�̂݋q:" +  this.getTechnicTotalNum() + "+" + this.getItemTotalNum() + "=" + totalNum);
		return totalNum;
	}

	public BigDecimal getShopSaleRate() {
		BigDecimal bd = BigDecimal.ZERO;
		int totalNum = this.getTotalNum();

		if (totalNum > 0) {
			bd = new BigDecimal((double)this.itemBuyNum / (double)totalNum * 100);
		}

		BigDecimal retVal = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("�X�̔䗦:" + this.itemBuyNum + "/" + totalNum + "=" + retVal.toString() + "%");
		return retVal;
	}

	public int getAllTotal() {
		int allTotal = this.getTechnicTotal() + this.getItemTotal() - this.getAllDiscountPrice();
		System.out.println("������=�Z�p����+���i����-�S�̊���:" + this.getTechnicTotal() + "+" + this.getItemTotal() + "-" + this.getAllDiscountPrice() + "=" + allTotal);
		return allTotal;
	}

	public BigDecimal getSalesOneStaff() {
		BigDecimal bd = BigDecimal.ZERO;
		double staffNum = this.getStaffNum();

		if (staffNum > 0) {
			bd = new BigDecimal((double)this.getTechnicTotal() / staffNum);
		}

		bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		System.out.println("�X�^�b�t1�l�����蔄��:" + this.getTechnicTotal() + "/" + staffNum + "=" + bd.toString());
		return bd;
	}
}
