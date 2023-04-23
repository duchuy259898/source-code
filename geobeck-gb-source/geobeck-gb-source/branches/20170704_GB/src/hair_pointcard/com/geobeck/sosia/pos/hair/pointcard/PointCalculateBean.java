/*
 * PointCalculateBean.java
 *
 * Created on 2008/09/08, 14:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.pointcard;

/**
 *
 * @author shiera.delusa
 */
public class PointCalculateBean
{
    /** Class Constants */
    public final static int BASIC_TAX_INCLUDED = 1;
    public final static int BASIC_TAX_EXCLUDED = 0;
    public final static int BASIC_TARGET_TECH_ITEM = 0;
    public final static int BASIC_TARGET_TECH_ONLY = 1;
    public final static int BASIC_TARGET_ITEM_ONLY = 2;
    public final static int FIRST_TIME_VISIT_ENABLED = 1;
    public final static int FIRST_TIME_VISIT_DISABLED = 0;
    public final static int BIRTHDAY_ENABLED = 1;
    public final static int BIRTHDAY_DISABLED = 0;
    public final static int BIRTHDAY_EXACT_DAY = 0;
    public final static int BIRTHDAY_MONTH = 1;
    public final static int BIRTHDAY_BEFORE_AFTER = 2;
    public final static int BASIC_TARGET2_ALL = 0;
    public final static int BASIC_TARGET2_SOSIA = 1;
    
    /** Class Variables */
    private Integer shopid;
    private Integer basicRate;
    private Integer basicPoint;
    private Integer basicRate2;
    private Integer basicPoint2;
    private Integer basicRate3;
    private Integer basicPoint3;
    private Integer basicRate4;
    private Integer basicPoint4;

    private Integer basicTaxType;
    private Integer basicTarget;
    private Integer basicTarget2;

    private Integer visitPoint;

    private Integer firstTimeEnabled;
    private Integer firstTimePoint;
    private Double firstTimeRate;
    private Integer birthdayEnabled;
    private Integer birthdayCond;
    private Integer birthdayRange;
    private Integer birthdayPoint;
    private Double birthdayRate;

    private Integer cashbackPoint;
    private Integer cashbackRate;
    
    /** Creates a new instance of PointCalculateBean */
    public PointCalculateBean()
    {
        this.shopid = null;
        this.basicRate = null;
        this.basicPoint = null;
        this.basicRate2 = null;
        this.basicPoint2 = null;
        this.basicRate3 = null;
        this.basicPoint3 = null;
        this.basicRate4 = null;
        this.basicPoint4 = null;
        this.basicTaxType = null;
        this.basicTarget = null;
        this.basicTarget2 = null;
        this.visitPoint = null;
        this.firstTimeEnabled = null;
        this.firstTimePoint = null;
        this.firstTimeRate = null;
        this.birthdayEnabled = null;
        this.birthdayCond = null;
        this.birthdayRange = null;
        this.birthdayPoint = null;
        this.birthdayRate = null;
        this.cashbackPoint = null;
        this.cashbackRate = null;
    }

    public Integer getShopid()
    {
        return shopid;
    }

    public void setShopid(Integer shopid)
    {
        this.shopid = shopid;
    }

    public Integer getBasicRate()
    {
        return basicRate;
    }

    public void setBasicRate(Integer basicRate)
    {
        this.basicRate = basicRate;
    }

    public Integer getBasicPoint()
    {
        return basicPoint;
    }

    public void setBasicPoint(Integer basicPoint)
    {
        this.basicPoint = basicPoint;
    }

    public Integer getBasicTaxType()
    {
        return basicTaxType;
    }

    public void setBasicTaxType(Integer basicTaxType)
    {
        this.basicTaxType = basicTaxType;
    }

    public Integer getBasicTarget()
    {
        return basicTarget;
    }

    public void setBasicTarget(Integer basicTarget)
    {
        this.basicTarget = basicTarget;
    }
    
    public Integer getBasicTarget2()
    {
        return basicTarget2;
    }

    public void setBasicTarget2(Integer basicTarget2)
    {
        this.basicTarget2 = basicTarget2;
    }

    public Integer getFirstTimeEnabled()
    {
        return firstTimeEnabled;
    }

    public void setFirstTimeEnabled(Integer firstTimeEnabled)
    {
        this.firstTimeEnabled = firstTimeEnabled;
    }

    public Integer getFirstTimePoint()
    {
        return firstTimePoint;
    }

    public void setFirstTimePoint(Integer firstTimePoint)
    {
        this.firstTimePoint = firstTimePoint;
    }

    public Double getFirstTimeRate()
    {
        return firstTimeRate;
    }

    public void setFirstTimeRate(Double firstTimeRate)
    {
        this.firstTimeRate = firstTimeRate;
    }

    public Integer getBirthdayEnabled()
    {
        return birthdayEnabled;
    }

    public void setBirthdayEnabled(Integer birthdayEnabled)
    {
        this.birthdayEnabled = birthdayEnabled;
    }

    public Integer getBirthdayCond()
    {
        return birthdayCond;
    }

    public void setBirthdayCond(Integer birthdayCond)
    {
        this.birthdayCond = birthdayCond;
    }

    public Integer getBirthdayRange()
    {
        return birthdayRange;
    }

    public void setBirthdayRange(Integer birthdayRange)
    {
        this.birthdayRange = birthdayRange;
    }

    public Integer getBirthdayPoint()
    {
        return birthdayPoint;
    }

    public void setBirthdayPoint(Integer birthdayPoint)
    {
        this.birthdayPoint = birthdayPoint;
    }

    public Double getBirthdayRate()
    {
        return birthdayRate;
    }

    public void setBirthdayRate(Double birthdayRate)
    {
        this.birthdayRate = birthdayRate;
    }

    /**
     * @return the basicRate2
     */
    public Integer getBasicRate2() {
        return basicRate2;
    }

    /**
     * @param basicRate2 the basicRate2 to set
     */
    public void setBasicRate2(Integer basicRate2) {
        this.basicRate2 = basicRate2;
    }

    /**
     * @return the basicPoint2
     */
    public Integer getBasicPoint2() {
        return basicPoint2;
    }

    /**
     * @param basicPoint2 the basicPoint2 to set
     */
    public void setBasicPoint2(Integer basicPoint2) {
        this.basicPoint2 = basicPoint2;
    }

    /**
     * @return the basicRate3
     */
    public Integer getBasicRate3() {
        return basicRate3;
    }

    /**
     * @param basicRate3 the basicRate3 to set
     */
    public void setBasicRate3(Integer basicRate3) {
        this.basicRate3 = basicRate3;
    }

    /**
     * @return the basicPoint3
     */
    public Integer getBasicPoint3() {
        return basicPoint3;
    }

    /**
     * @param basicPoint3 the basicPoint3 to set
     */
    public void setBasicPoint3(Integer basicPoint3) {
        this.basicPoint3 = basicPoint3;
    }

    /**
     * @return the basicRate4
     */
    public Integer getBasicRate4() {
        return basicRate4;
    }

    /**
     * @param basicRate4 the basicRate4 to set
     */
    public void setBasicRate4(Integer basicRate4) {
        this.basicRate4 = basicRate4;
    }

    /**
     * @return the basicPoint4
     */
    public Integer getBasicPoint4() {
        return basicPoint4;
    }

    /**
     * @param basicPoint4 the basicPoint4 to set
     */
    public void setBasicPoint4(Integer basicPoint4) {
        this.basicPoint4 = basicPoint4;
    }

    /**
     * @return the visitPoint
     */
    public Integer getVisitPoint() {
        return visitPoint;
    }

    /**
     * @param visitPoint the visitPoint to set
     */
    public void setVisitPoint(Integer visitPoint) {
        this.visitPoint = visitPoint;
    }

    /**
     * @return the cashbackPoint
     */
    public Integer getCashbackPoint() {
        return cashbackPoint;
    }

    /**
     * @param cashbackPoint the cashbackPoint to set
     */
    public void setCashbackPoint(Integer cashbackPoint) {
        this.cashbackPoint = cashbackPoint;
    }

    /**
     * @return the cashbackRate
     */
    public Integer getCashbackRate() {
        return cashbackRate;
    }

    /**
     * @param cashbackRate the cashbackRate to set
     */
    public void setCashbackRate(Integer cashbackRate) {
        this.cashbackRate = cashbackRate;
    }
    
}
