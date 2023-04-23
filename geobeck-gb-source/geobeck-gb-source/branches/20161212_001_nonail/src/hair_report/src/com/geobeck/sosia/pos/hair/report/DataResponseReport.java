/*
 * DataResponseReport.java
 *
 * Created on 2007/09/05, 15:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;

/**
 *
 * @author kanemoto
 */
public class DataResponseReport {
	protected	Integer			responseID		=	null;		/** ���X�|���XID */
	protected	String			responseName		=	null;		/** ���X�|���X�� */
	protected	String			responseObjectName	=	null;		/** ���s�ڍ� */
	protected	GregorianCalendar	registDate		=	null;		/** �o�^�� */
	protected	Integer			distNum			=	null;		/** �z�z�� */
	protected	Integer			recNum			=	null;		/** ����� */
	protected	Double			recRate			=	null;		/** ����� */
	protected	Double			rankingNo		=	null;		/** ���ʕt���p�f�[�^ */

	private	Integer			totalCount	=	null;		/** ���� */
	private	Integer			newCount	=	null;		/** �V�K�� */
	private	Integer			newAmount	=	null;		/** �V�K���z */
	private	Integer			subFixedCount	=	null;		/** ���Œ萔 */
	private	Integer			subFixedAmount	=	null;		/** ���Œ���z */
	private	Integer			fixedCount	=	null;		/** �Œ萔 */
	private	Integer			fixedAmount	=	null;		/** �Œ���z */
	private	Integer			maleCount	=	null;		/** �j���� */
	private	Integer			maleAmount	=	null;		/** �j�����z */
	private	Integer			femaleCount	=	null;		/** ������ */
	private	Integer			femaleAmount	=	null;		/** �������z */
	private	Integer			unknownCount	=	null;		/** �s���� */
	private	Integer			unknownAmount	=	null;		/** �s�����z */
	private	Integer			totalAmount	=	null;		/** ���㍇�v */
	
	/** Creates a new instance of DataResponseReport */
	public DataResponseReport() {
	}

	/**
	 * ���X�|���XID���擾����
	 */
	public Integer getResponseID()
	{
		return this.responseID;
	}
	
	/**
	 * ���X�|���XID��ݒ肷��
	 */
	public void setResponseID( Integer responseID )
	{
		this.responseID = responseID;
	}
	
	/**
	 * ���X�|���X�����擾����
	 */
	public String getResponseName()
	{
		return this.responseName;
	}
	
	/**
	 * ���X�|���X����ݒ肷��
	 */
	public void setResponseName( String responseName )
	{
		this.responseName = responseName;
	}
	
	/**
	 * ���s�ڍׂ��擾����
	 */
	public String getResponseObjectName()
	{
		return this.responseObjectName;
	}
	
	/**
	 * ���s�ڍׂ�ݒ肷��
	 */
	public void setResponseObjectName( String responseObjectName )
	{
		this.responseObjectName = responseObjectName;
	}
	
	/**
	 * �o�^�����擾����
	 */
	public GregorianCalendar getRegistDate()
	{
		return this.registDate;
	}
	
	public String getStrRegistDate()
	{
		return String.format( "%1$tY�N%1$tm��%1$td��", this.registDate );
	}
	
	/**
	 * �o�^����ݒ肷��
	 */
	public void setRegistDate( GregorianCalendar registDate )
	{
		this.registDate = registDate;
	}
	
	/**
	 * �z�z�����擾����
	 */
	public Integer getDistNum()
	{
		return this.distNum;
	}
	
	/**
	 * �z�z����ݒ肷��
	 */
	public void setDistNum( Integer distNum )
	{
		this.distNum = distNum;
	}
	
	/**
	 * ��������擾����
	 */
	public Integer getRecNum()
	{
		return this.recNum;
	}
	
	/**
	 * �������ݒ肷��
	 */
	public void setRecNum( Integer recNum )
	{
		this.recNum = recNum;
	}
	
	/**
	 * ��������擾����
	 */
	public Double getRecRate()
	{
		return this.recRate;
	}
	
	/**
	 * �������ݒ肷��
	 */
	public void setRecRate( Double recRate )
	{
		this.recRate = recRate;
	}
	
	/**
	 * ���ʕt���p�f�[�^���擾����
	 */
	public Double getRankingNo()
	{
		return this.rankingNo;
	}
	
	/**
	 * ���ʕt���p�f�[�^��ݒ肷��
	 */
	public void setRankingNo( Double rankingNo )
	{
		this.rankingNo = rankingNo;
	}
	
	/**
	 * DataReponseReport����f�[�^�𐶐�����
	 */
	public void setData( DataResponseReport drr )
	{
		this.setResponseID( drr.getResponseID() );
		this.setResponseName( drr.getResponseName() );
		this.setResponseObjectName( drr.getResponseObjectName() );
		this.setRegistDate( drr.getRegistDate() );
		this.setDistNum( drr.getDistNum() );
		this.setRecNum( drr.getRecNum() );
		this.setRecRate( drr.getRecRate() );
		this.setRankingNo( drr.getRankingNo() );
	}
	
	/**
	 * �f�[�^�𐶐�����
	 */
	public void setData( ResultSetWrapper rs, int sheetType ) throws SQLException
	{
		this.setResponseID( rs.getInt( "response_id" ) );
		this.setResponseName( rs.getString( "response_name" ) );
		if( sheetType != 1 )
		{
			this.setResponseObjectName( rs.getString( "response_object_name" ) );
			this.setRegistDate( rs.getGregorianCalendar( "regist_date" ) );
		}
		this.setDistNum( rs.getInt( "dist_num" ) );
		this.setRecNum( rs.getInt( "rec_num" ) );
		this.setRecRate( rs.getDouble( "recRate" ) );
		this.setRankingNo( rs.getDouble( "ranking_value" ) );
	}
	
	public void setData( ResultSetWrapper rs ) throws SQLException
	{
		this.setResponseID( rs.getInt( "response_id" ) );
		this.setResponseName( rs.getString( "response_name" ) );
		this.setTotalCount( rs.getInt( "total_count" ) );
		this.setNewCount( rs.getInt( "new_count" ) );
		this.setNewAmount( rs.getInt( "new_amount" ) );
		this.setSubFixedCount( rs.getInt( "sub_fixed_count" ) );
		this.setSubFixedAmount( rs.getInt( "sub_fixed_amount" ) );
		this.setFixedCount( rs.getInt( "fixed_count" ) );
		this.setFixedAmount( rs.getInt( "fixed_amount" ) );
		this.setMaleCount( rs.getInt( "male_count" ) );
		this.setMaleAmount( rs.getInt( "male_amount" ) );
		this.setFemaleCount( rs.getInt( "female_count" ) );
		this.setFemaleAmount( rs.getInt( "female_amount" ) );
		this.setUnknownCount( rs.getInt( "unknown_count" ) );
		this.setUnknownAmount( rs.getInt( "unknown_amount" ) );
		this.setTotalAmount( rs.getInt( "total_amount" ) );
	}	

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
	public Integer getNewCount() {
		return newCount;
	}

	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}

	public Integer getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(Integer newAmount) {
		this.newAmount = newAmount;
	}

	public Integer getSubFixedCount() {
		return subFixedCount;
	}

	public void setSubFixedCount(Integer subFixedCount) {
		this.subFixedCount = subFixedCount;
	}

	public Integer getSubFixedAmount() {
		return subFixedAmount;
	}

	public void setSubFixedAmount(Integer subFixedAmount) {
		this.subFixedAmount = subFixedAmount;
	}

	public Integer getFixedCount() {
		return fixedCount;
	}

	public void setFixedCount(Integer fixedCount) {
		this.fixedCount = fixedCount;
	}

	public Integer getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(Integer fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public Integer getMaleCount() {
		return maleCount;
	}

	public void setMaleCount(Integer maleCount) {
		this.maleCount = maleCount;
	}

	public Integer getMaleAmount() {
		return maleAmount;
	}

	public void setMaleAmount(Integer maleAmount) {
		this.maleAmount = maleAmount;
	}

	public Integer getFemaleCount() {
		return femaleCount;
	}

	public void setFemaleCount(Integer femaleCount) {
		this.femaleCount = femaleCount;
	}

	public Integer getFemaleAmount() {
		return femaleAmount;
	}

	public void setFemaleAmount(Integer femaleAmount) {
		this.femaleAmount = femaleAmount;
	}

	public Integer getUnknownCount() {
		return unknownCount;
	}

	public void setUnknownCount(Integer unknownCount) {
		this.unknownCount = unknownCount;
	}

	public Integer getUnknownAmount() {
		return unknownAmount;
	}

	public void setUnknownAmount(Integer unknownAmount) {
		this.unknownAmount = unknownAmount;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
}
