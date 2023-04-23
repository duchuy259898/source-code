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
	protected	Integer			responseID		=	null;		/** レスポンスID */
	protected	String			responseName		=	null;		/** レスポンス名 */
	protected	String			responseObjectName	=	null;		/** 発行詳細 */
	protected	GregorianCalendar	registDate		=	null;		/** 登録日 */
	protected	Integer			distNum			=	null;		/** 配布数 */
	protected	Integer			recNum			=	null;		/** 回収数 */
	protected	Double			recRate			=	null;		/** 回収率 */
	protected	Double			rankingNo		=	null;		/** 順位付け用データ */

	private	Integer			totalCount	=	null;		/** 件数 */
	private	Integer			newCount	=	null;		/** 新規数 */
	private	Integer			newAmount	=	null;		/** 新規金額 */
	private	Integer			subFixedCount	=	null;		/** 準固定数 */
	private	Integer			subFixedAmount	=	null;		/** 準固定金額 */
	private	Integer			fixedCount	=	null;		/** 固定数 */
	private	Integer			fixedAmount	=	null;		/** 固定金額 */
	private	Integer			maleCount	=	null;		/** 男性数 */
	private	Integer			maleAmount	=	null;		/** 男性金額 */
	private	Integer			femaleCount	=	null;		/** 女性数 */
	private	Integer			femaleAmount	=	null;		/** 女性金額 */
	private	Integer			unknownCount	=	null;		/** 不明数 */
	private	Integer			unknownAmount	=	null;		/** 不明金額 */
	private	Integer			totalAmount	=	null;		/** 売上合計 */
	
	/** Creates a new instance of DataResponseReport */
	public DataResponseReport() {
	}

	/**
	 * レスポンスIDを取得する
	 */
	public Integer getResponseID()
	{
		return this.responseID;
	}
	
	/**
	 * レスポンスIDを設定する
	 */
	public void setResponseID( Integer responseID )
	{
		this.responseID = responseID;
	}
	
	/**
	 * レスポンス名を取得する
	 */
	public String getResponseName()
	{
		return this.responseName;
	}
	
	/**
	 * レスポンス名を設定する
	 */
	public void setResponseName( String responseName )
	{
		this.responseName = responseName;
	}
	
	/**
	 * 発行詳細を取得する
	 */
	public String getResponseObjectName()
	{
		return this.responseObjectName;
	}
	
	/**
	 * 発行詳細を設定する
	 */
	public void setResponseObjectName( String responseObjectName )
	{
		this.responseObjectName = responseObjectName;
	}
	
	/**
	 * 登録日を取得する
	 */
	public GregorianCalendar getRegistDate()
	{
		return this.registDate;
	}
	
	public String getStrRegistDate()
	{
		return String.format( "%1$tY年%1$tm月%1$td日", this.registDate );
	}
	
	/**
	 * 登録日を設定する
	 */
	public void setRegistDate( GregorianCalendar registDate )
	{
		this.registDate = registDate;
	}
	
	/**
	 * 配布数を取得する
	 */
	public Integer getDistNum()
	{
		return this.distNum;
	}
	
	/**
	 * 配布数を設定する
	 */
	public void setDistNum( Integer distNum )
	{
		this.distNum = distNum;
	}
	
	/**
	 * 回収数を取得する
	 */
	public Integer getRecNum()
	{
		return this.recNum;
	}
	
	/**
	 * 回収数を設定する
	 */
	public void setRecNum( Integer recNum )
	{
		this.recNum = recNum;
	}
	
	/**
	 * 回収率を取得する
	 */
	public Double getRecRate()
	{
		return this.recRate;
	}
	
	/**
	 * 回収率を設定する
	 */
	public void setRecRate( Double recRate )
	{
		this.recRate = recRate;
	}
	
	/**
	 * 順位付け用データを取得する
	 */
	public Double getRankingNo()
	{
		return this.rankingNo;
	}
	
	/**
	 * 順位付け用データを設定する
	 */
	public void setRankingNo( Double rankingNo )
	{
		this.rankingNo = rankingNo;
	}
	
	/**
	 * DataReponseReportからデータを生成する
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
	 * データを生成する
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
