/*
 * ReceiptData.java
 *
 * Created on 2007/10/23, 15:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author katagiri
 */
public class ReceiptData
{
	private	Integer     dataType            = null;
	private	String      className           = "";
	private	String      name                = "";
	private	Integer     num                 = null;
	private	Long        value               = null;
	private	Long        tax                 = null;
	private	Boolean     taxExcluded         = false;
	private	Boolean     discountTaxExcluded = false;

	private Double consumptionNum = null;
	
	/** Creates a new instance of ReceiptData */
	public ReceiptData()
	{
	}

	public ReceiptData(Integer dataType, String name, Integer num,
			Long value, Long tax, Boolean taxExcluded, Boolean discountTaxExcluded)
	{
            this(dataType, "", name, num, value, tax, taxExcluded, discountTaxExcluded);
        }

	public ReceiptData(Integer dataType, String className, String name, Integer num,
			Long value, Long tax, Boolean taxExcluded, Boolean discountTaxExcluded)
	{
		this.setDataType(dataType);
		this.setClassName(className);
		this.setName(name);
		this.setNum(num);
		this.setValue(value);
		this.setTax(tax);
		this.setTaxExcluded(taxExcluded);
		this.setDiscountTaxExcluded(discountTaxExcluded);
	}

        /**
         * 消化コース用コンストラクタ
         * @param dataType
         * @param className
         * @param name
         * @param consumptionNum
         * @param value
         * @param tax
         * @param taxExcluded
         * @param discountTaxExcluded
         */
	public ReceiptData(Integer dataType, String className, String name, Double consumptionNum,
			Long value, Long tax, Boolean taxExcluded, Boolean discountTaxExcluded)
	{
		this.setDataType(dataType);
		this.setClassName(className);
		this.setName(name);
		this.setConsumptionNum(consumptionNum);
		this.setValue(value);
		this.setTax(tax);
		this.setTaxExcluded(taxExcluded);
		this.setDiscountTaxExcluded(discountTaxExcluded);
	}

	public Integer getDataType()
	{
		return dataType;
	}

	public void setDataType(Integer dataType)
	{
		this.dataType = dataType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getNum()
	{
		return num;
	}

	public void setNum(Integer num)
	{
		this.num = num;
	}

	public Long getValue()
	{
		return value;
	}

	public void setValue(Long value)
	{
		this.value = value;
	}

	public Long getTax()
	{
		return tax;
	}

	public void setTax(Long tax)
	{
		this.tax = tax;
	}
	
	public Long getTaxOffValue()
	{
		//return	this.getValue() - this.getTax();
                Long	taxOffValue		=	0l;
                Double temptaxOffValue ;
                temptaxOffValue	=	(this.getValue() / (1 + SystemInfo.getTaxRate(SystemInfo.getSystemDate())));
                BigDecimal a = new BigDecimal(temptaxOffValue);
                a = a.setScale(3, RoundingMode.HALF_UP);
                taxOffValue = ((Double)Math.ceil(a.doubleValue())).longValue();
                return   taxOffValue;
	}

	public Boolean getTaxExcluded()
	{
		return taxExcluded;
	}

	public void setTaxExcluded(Boolean taxExcluded)
	{
		this.taxExcluded = taxExcluded;
	}

	public Boolean getDiscountTaxExcluded()
	{
		return discountTaxExcluded;
	}

	public void setDiscountTaxExcluded(Boolean discountTaxExcluded)
	{
		this.discountTaxExcluded = discountTaxExcluded;
	}

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    public Double getConsumptionNum() {
        return consumptionNum;
    }

    public void setConsumptionNum(Double consumptionNum) {
        this.consumptionNum = consumptionNum;
    }

}
