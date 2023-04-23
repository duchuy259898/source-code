/*
 * StoreShipPanelReportBean.java
 *
 * Created on 2008/09/26, 10:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

/**
 *
 * @author shiera.delusa
 */
public class StoreShipPanelReportBean
{
    //for database access purposes only
    private int categoryId;
    private int brandId;
    
    //REPORT FIELDS:s
    private String categoryName;
    private String brandName;
    private Integer termStartNo;
    private Integer suitableNo;
    
    
    private Integer[] incomingNo = new Integer[31];
    private Integer incomingNoTotal;
    
    private Integer[] additionalNo = new Integer[31];
    private Integer additionalNoTotal;
    
    private Integer[] outgoingNo = new Integer[31];
    private Integer outgoingNoTotal;
    
    private Integer[] remainingNo = new Integer[31];
    private Integer remainingNoTotal;
    
    private Integer termEndNo;
    
    /**
     * Creates a new instance of StoreShipPanelReportBean
     */
    public StoreShipPanelReportBean()
    {
    }
    
    /** 
     * Calculates the total of all incomingNo_* and sets the total to 
     * incomingNoTotal variable 
     */
    public void calcIncomingNoTotal()
    {
        incomingNoTotal = new Integer(0);
        
        for( Integer qty : incomingNo ){
            if( qty != null ){
                incomingNoTotal += qty;
            }
        }
    }
    
    public void setIncomingNo( int index, int inQty ){
        Integer qty = inQty;
        if( qty == 0 ) qty = null;
        if( index >= 0  && index <= 30 ){
            incomingNo[index] = qty;
        }
    }
    
    /** 
     * Calculates the total of all additionalNo_* and sets the total to 
     * addtionalNoTotal variable 
     */
    public void calcAdditionalNoTotal()
    {
        additionalNoTotal = new Integer(0);
        
        for( Integer qty : additionalNo ){
            if( qty != null ){
                additionalNoTotal += qty;
            }
        }
        
    }
    
    public void setAdditionalNo( int index, int inQty ){
        Integer qty = inQty;
        if( qty == 0 ) qty = null;
        if( index >= 0  && index <= 30 ){
            additionalNo[index] = qty;
        }
    }
    
    /** 
     * Calculates the total of all outgoingNo_* and sets the total to 
     * outgoingNoTotal variable 
     */
    public void calcOutgoingNoTotal()
    {
        outgoingNoTotal = new Integer(0);
        
        for( Integer qty : outgoingNo ){
            if( qty != null ){
                outgoingNoTotal += qty;
            }
        }
    }
    
    public void setAllOutgoingNo( int index, int inQty )
    {
        Integer qty = inQty;
        if( qty == 0 ) qty = null;
        if( index >= 0  && index <= 30 ){
            outgoingNo[index] = qty;
        }
    }
    
    public void calcRemainingNo()
    {
        for( int ii = 0; ii < 31; ii++ ){
            int remainQty = 0;
            if( ii == 0 ){
                if( termStartNo != null ) remainQty = termStartNo;
            }else{
                remainQty = remainingNo[ii-1];
            }
            remainingNo[ii] = remainQty;
            if( incomingNo[ii]   != null )remainingNo[ii] += incomingNo[ii];
            if( additionalNo[ii] != null )remainingNo[ii] += additionalNo[ii];
            if( outgoingNo[ii]   != null )remainingNo[ii] -= outgoingNo[ii];
        }
        remainingNoTotal = remainingNo[31-1];
    }
    
    
    /**************************************************************************
     * GENERIC GETTERS AND SETTERS
     **************************************************************************/
    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getBrandName()
    {
        return brandName;
    }

    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }

    public Integer getTermStartNo()
    {
        return termStartNo;
    }

    public void setTermStartNo(int termStartNo)
    {
        this.termStartNo = termStartNo;
    }

    public Integer getSuitableNo()
    {
        return suitableNo;
    }

    public void setSuitableNo(int suitableNo)
    {
        this.suitableNo = suitableNo;
    }

    public Integer getIncomingNo_01()
    {
        return incomingNo[1-1];
    }

    public void setIncomingNo_01(int incomingNo_01)
    {
        this.incomingNo[1-1] = incomingNo_01;
    }

    public Integer getIncomingNo_02()
    {
        return incomingNo[2-1];
    }

    public void setIncomingNo_02(int incomingNo_02)
    {
        this.incomingNo[2-1] = incomingNo_02;
    }

    public Integer getIncomingNo_03()
    {
        return incomingNo[3-1];
    }

    public void setIncomingNo_03(int incomingNo_03)
    {
        this.incomingNo[3-1] = incomingNo_03;
    }

    public Integer getIncomingNo_04()
    {
        return incomingNo[4-1];
    }

    public void setIncomingNo_04(int incomingNo_04)
    {
        this.incomingNo[4-1] = incomingNo_04;
    }

    public Integer getIncomingNo_05()
    {
        return incomingNo[5-1];
    }

    public void setIncomingNo_05(int incomingNo_05)
    {
        this.incomingNo[5-1] = incomingNo_05;
    }

    public Integer getIncomingNo_06()
    {
        return incomingNo[6-1];
    }

    public void setIncomingNo_06(int incomingNo_06)
    {
        this.incomingNo[6-1] = incomingNo_06;
    }

    public Integer getIncomingNo_07()
    {
        return incomingNo[7-1];
    }

    public void setIncomingNo_07(int incomingNo_07)
    {
        this.incomingNo[7-1] = incomingNo_07;
    }

    public Integer getIncomingNo_08()
    {
        return incomingNo[8-1];
    }

    public void setIncomingNo_08(int incomingNo_08)
    {
        this.incomingNo[8-1] = incomingNo_08;
    }

    public Integer getIncomingNo_09()
    {
        return incomingNo[9-1];
    }

    public void setIncomingNo_09(int incomingNo_09)
    {
        this.incomingNo[9-1] = incomingNo_09;
    }

    public Integer getIncomingNo_10()
    {
        return incomingNo[10-1];
    }

    public void setIncomingNo_10(int incomingNo_10)
    {
        this.incomingNo[10-1] = incomingNo_10;
    }

    public Integer getIncomingNo_11()
    {
        return incomingNo[11-1];
    }

    public void setIncomingNo_11(int incomingNo_11)
    {
        this.incomingNo[11-1] = incomingNo_11;
    }

    public Integer getIncomingNo_12()
    {
        return incomingNo[12-1];
    }

    public void setIncomingNo_12(int incomingNo_12)
    {
        this.incomingNo[12-1] = incomingNo_12;
    }

    public Integer getIncomingNo_13()
    {
        return incomingNo[13-1];
    }

    public void setIncomingNo_13(int incomingNo_13)
    {
        this.incomingNo[13-1] = incomingNo_13;
    }

    public Integer getIncomingNo_14()
    {
        return incomingNo[14-1];
    }

    public void setIncomingNo_14(int incomingNo_14)
    {
        this.incomingNo[14-1] = incomingNo_14;
    }

    public Integer getIncomingNo_15()
    {
        return incomingNo[15-1];
    }

    public void setIncomingNo_15(int incomingNo_15)
    {
        this.incomingNo[15-1] = incomingNo_15;
    }

    public Integer getIncomingNo_16()
    {
        return incomingNo[16-1];
    }

    public void setIncomingNo_16(int incomingNo_16)
    {
        this.incomingNo[16-1] = incomingNo_16;
    }

    public Integer getIncomingNo_17()
    {
        return incomingNo[17-1];
    }

    public void setIncomingNo_17(int incomingNo_17)
    {
        this.incomingNo[17-1] = incomingNo_17;
    }

    public Integer getIncomingNo_18()
    {
        return incomingNo[18-1];
    }

    public void setIncomingNo_18(int incomingNo_18)
    {
        this.incomingNo[18-1] = incomingNo_18;
    }

    public Integer getIncomingNo_19()
    {
        return incomingNo[19-1];
    }

    public void setIncomingNo_19(int incomingNo_19)
    {
        this.incomingNo[19-1] = incomingNo_19;
    }

    public Integer getIncomingNo_20()
    {
        return incomingNo[20-1];
    }

    public void setIncomingNo_20(int incomingNo_20)
    {
        this.incomingNo[20-1] = incomingNo_20;
    }

    public Integer getIncomingNo_21()
    {
        return incomingNo[21-1];
    }

    public void setIncomingNo_21(int incomingNo_21)
    {
        this.incomingNo[21-1] = incomingNo_21;
    }

    public Integer getIncomingNo_22()
    {
        return incomingNo[22-1];
    }

    public void setIncomingNo_22(int incomingNo_22)
    {
        this.incomingNo[22-1] = incomingNo_22;
    }

    public Integer getIncomingNo_23()
    {
        return incomingNo[23-1];
    }

    public void setIncomingNo_23(int incomingNo_23)
    {
        this.incomingNo[23-1] = incomingNo_23;
    }

    public Integer getIncomingNo_24()
    {
        return incomingNo[24-1];
    }

    public void setIncomingNo_24(int incomingNo_24)
    {
        this.incomingNo[24-1] = incomingNo_24;
    }

    public Integer getIncomingNo_25()
    {
        return incomingNo[25-1];
    }

    public void setIncomingNo_25(int incomingNo_25)
    {
        this.incomingNo[25-1] = incomingNo_25;
    }

    public Integer getIncomingNo_26()
    {
        return incomingNo[26-1];
    }

    public void setIncomingNo_26(int incomingNo_26)
    {
        this.incomingNo[26-1] = incomingNo_26;
    }

    public Integer getIncomingNo_27()
    {
        return incomingNo[27-1];
    }

    public void setIncomingNo_27(int incomingNo_27)
    {
        this.incomingNo[27-1] = incomingNo_27;
    }

    public Integer getIncomingNo_28()
    {
        return incomingNo[28-1];
    }

    public void setIncomingNo_28(int incomingNo_28)
    {
        this.incomingNo[28-1] = incomingNo_28;
    }

    public Integer getIncomingNo_29()
    {
        return incomingNo[29-1];
    }

    public void setIncomingNo_29(int incomingNo_29)
    {
        this.incomingNo[29-1] = incomingNo_29;
    }

    public Integer getIncomingNo_30()
    {
        return incomingNo[30-1];
    }

    public void setIncomingNo_30(int incomingNo_30)
    {
        this.incomingNo[30-1] = incomingNo_30;
    }

    public Integer getIncomingNo_31()
    {
        return incomingNo[31-1];
    }

    public void setIncomingNo_31(int incomingNo_31)
    {
        this.incomingNo[31-1] = incomingNo_31;
    }

    public Integer getIncomingNoTotal()
    {
        return incomingNoTotal;
    }

    public Integer getAdditionalNo_01()
    {
        return additionalNo[1-1];
    }

    public void setAdditionalNo_01(int additionalNo_01)
    {
        this.additionalNo[1-1] = additionalNo_01;
    }

    public Integer getAdditionalNo_02()
    {
        return additionalNo[2-1];
    }

    public void setAdditionalNo_02(int additionalNo_02)
    {
        this.additionalNo[2-1] = additionalNo_02;
    }

    public Integer getAdditionalNo_03()
    {
        return additionalNo[3-1];
    }

    public void setAdditionalNo_03(int additionalNo_03)
    {
        this.additionalNo[3-1] = additionalNo_03;
    }

    public Integer getAdditionalNo_04()
    {
        return additionalNo[4-1];
    }

    public void setAdditionalNo_04(int additionalNo_04)
    {
        this.additionalNo[4-1] = additionalNo_04;
    }

    public Integer getAdditionalNo_05()
    {
        return additionalNo[5-1];
    }

    public void setAdditionalNo_05(int additionalNo_05)
    {
        this.additionalNo[5-1] = additionalNo_05;
    }

    public Integer getAdditionalNo_06()
    {
        return additionalNo[6-1];
    }

    public void setAdditionalNo_06(int additionalNo_06)
    {
        this.additionalNo[6-1] = additionalNo_06;
    }

    public Integer getAdditionalNo_07()
    {
        return additionalNo[7-1];
    }

    public void setAdditionalNo_07(int additionalNo_07)
    {
        this.additionalNo[7-1] = additionalNo_07;
    }

    public Integer getAdditionalNo_08()
    {
        return additionalNo[8-1];
    }

    public void setAdditionalNo_08(int additionalNo_08)
    {
        this.additionalNo[8-1] = additionalNo_08;
    }

    public Integer getAdditionalNo_09()
    {
        return additionalNo[9-1];
    }

    public void setAdditionalNo_09(int additionalNo_09)
    {
        this.additionalNo[9-1] = additionalNo_09;
    }

    public Integer getAdditionalNo_10()
    {
        return additionalNo[10-1];
    }

    public void setAdditionalNo_10(int additionalNo_10)
    {
        this.additionalNo[10-1] = additionalNo_10;
    }

    public Integer getAdditionalNo_11()
    {
        return additionalNo[11-1];
    }

    public void setAdditionalNo_11(int additionalNo_11)
    {
        this.additionalNo[11-1] = additionalNo_11;
    }

    public Integer getAdditionalNo_12()
    {
        return additionalNo[12-1];
    }

    public void setAdditionalNo_12(int additionalNo_12)
    {
        this.additionalNo[12-1] = additionalNo_12;
    }

    public Integer getAdditionalNo_13()
    {
        return additionalNo[13-1];
    }

    public void setAdditionalNo_13(int additionalNo_13)
    {
        this.additionalNo[13-1] = additionalNo_13;
    }

    public Integer getAdditionalNo_14()
    {
        return additionalNo[14-1];
    }

    public void setAdditionalNo_14(int additionalNo_14)
    {
        this.additionalNo[14-1] = additionalNo_14;
    }

    public Integer getAdditionalNo_15()
    {
        return additionalNo[15-1];
    }

    public void setAdditionalNo_15(int additionalNo_15)
    {
        this.additionalNo[15-1] = additionalNo_15;
    }

    public Integer getAdditionalNo_16()
    {
        return additionalNo[16-1];
    }

    public void setAdditionalNo_16(int additionalNo_16)
    {
        this.additionalNo[16-1] = additionalNo_16;
    }

    public Integer getAdditionalNo_17()
    {
        return additionalNo[17-1];
    }

    public void setAdditionalNo_17(int additionalNo_17)
    {
        this.additionalNo[17-1] = additionalNo_17;
    }

    public Integer getAdditionalNo_18()
    {
        return additionalNo[18-1];
    }

    public void setAdditionalNo_18(int additionalNo_18)
    {
        this.additionalNo[18-1] = additionalNo_18;
    }

    public Integer getAdditionalNo_19()
    {
        return additionalNo[19-1];
    }

    public void setAdditionalNo_19(int additionalNo_19)
    {
        this.additionalNo[19-1] = additionalNo_19;
    }

    public Integer getAdditionalNo_20()
    {
        return additionalNo[20-1];
    }

    public void setAdditionalNo_20(int additionalNo_20)
    {
        this.additionalNo[20-1] = additionalNo_20;
    }

    public Integer getAdditionalNo_21()
    {
        return additionalNo[21-1];
    }

    public void setAdditionalNo_21(int additionalNo_21)
    {
        this.additionalNo[21-1] = additionalNo_21;
    }

    public Integer getAdditionalNo_22()
    {
        return additionalNo[22-1];
    }

    public void setAdditionalNo_22(int additionalNo_22)
    {
        this.additionalNo[22-1] = additionalNo_22;
    }

    public Integer getAdditionalNo_23()
    {
        return additionalNo[23-1];
    }

    public void setAdditionalNo_23(int additionalNo_23)
    {
        this.additionalNo[23-1] = additionalNo_23;
    }

    public Integer getAdditionalNo_24()
    {
        return additionalNo[24-1];
    }

    public void setAdditionalNo_24(int additionalNo_24)
    {
        this.additionalNo[24-1] = additionalNo_24;
    }

    public Integer getAdditionalNo_25()
    {
        return additionalNo[25-1];
    }

    public void setAdditionalNo_25(int additionalNo_25)
    {
        this.additionalNo[25-1] = additionalNo_25;
    }

    public Integer getAdditionalNo_26()
    {
        return additionalNo[26-1];
    }

    public void setAdditionalNo_26(int additionalNo_26)
    {
        this.additionalNo[26-1] = additionalNo_26;
    }

    public Integer getAdditionalNo_27()
    {
        return additionalNo[27-1];
    }

    public void setAdditionalNo_27(int additionalNo_27)
    {
        this.additionalNo[27-1] = additionalNo_27;
    }

    public Integer getAdditionalNo_28()
    {
        return additionalNo[28-1];
    }

    public void setAdditionalNo_28(int additionalNo_28)
    {
        this.additionalNo[28-1] = additionalNo_28;
    }

    public Integer getAdditionalNo_29()
    {
        return additionalNo[29-1];
    }

    public void setAdditionalNo_29(int additionalNo_29)
    {
        this.additionalNo[29-1] = additionalNo_29;
    }

    public Integer getAdditionalNo_30()
    {
        return additionalNo[30-1];
    }

    public void setAdditionalNo_30(int additionalNo_30)
    {
        this.additionalNo[30-1] = additionalNo_30;
    }

    public Integer getAdditionalNo_31()
    {
        return additionalNo[31-1];
    }

    public void setAdditionalNo_31(int additionalNo_31)
    {
        this.additionalNo[31-1] = additionalNo_31;
    }

    public Integer getAdditionalNoTotal()
    {
        return additionalNoTotal;
    }

    public Integer getOutgoingNo_01()
    {
        return outgoingNo[1-1];
    }

    public void setOutgoingNo_01(int outgoingNo_01)
    {
        this.outgoingNo[1-1] = outgoingNo_01;
    }

    public Integer getOutgoingNo_02()
    {
        return outgoingNo[2-1];
    }

    public void setOutgoingNo_02(int outgoingNo_02)
    {
        this.outgoingNo[2-1] = outgoingNo_02;
    }

    public Integer getOutgoingNo_03()
    {
        return outgoingNo[3-1];
    }

    public void setOutgoingNo_03(int outgoingNo_03)
    {
        this.outgoingNo[3-1] = outgoingNo_03;
    }

    public Integer getOutgoingNo_04()
    {
        return outgoingNo[4-1];
    }

    public void setOutgoingNo_04(int outgoingNo_04)
    {
        this.outgoingNo[4-1] = outgoingNo_04;
    }

    public Integer getOutgoingNo_05()
    {
        return outgoingNo[5-1];
    }

    public void setOutgoingNo_05(int outgoingNo_05)
    {
        this.outgoingNo[5-1] = outgoingNo_05;
    }

    public Integer getOutgoingNo_06()
    {
        return outgoingNo[6-1];
    }

    public void setOutgoingNo_06(int outgoingNo_06)
    {
        this.outgoingNo[6-1] = outgoingNo_06;
    }

    public Integer getOutgoingNo_07()
    {
        return outgoingNo[7-1];
    }

    public void setOutgoingNo_07(int outgoingNo_07)
    {
        this.outgoingNo[7-1] = outgoingNo_07;
    }

    public Integer getOutgoingNo_08()
    {
        return outgoingNo[8-1];
    }

    public void setOutgoingNo_08(int outgoingNo_08)
    {
        this.outgoingNo[8-1] = outgoingNo_08;
    }

    public Integer getOutgoingNo_09()
    {
        return outgoingNo[9-1];
    }

    public void setOutgoingNo_09(int outgoingNo_09)
    {
        this.outgoingNo[9-1] = outgoingNo_09;
    }

    public Integer getOutgoingNo_10()
    {
        return outgoingNo[10-1];
    }

    public void setOutgoingNo_10(int outgoingNo_10)
    {
        this.outgoingNo[10-1] = outgoingNo_10;
    }

    public Integer getOutgoingNo_11()
    {
        return outgoingNo[11-1];
    }

    public void setOutgoingNo_11(int outgoingNo_11)
    {
        this.outgoingNo[11-1] = outgoingNo_11;
    }

    public Integer getOutgoingNo_12()
    {
        return outgoingNo[12-1];
    }

    public void setOutgoingNo_12(int outgoingNo_12)
    {
        this.outgoingNo[12-1] = outgoingNo_12;
    }

    public Integer getOutgoingNo_13()
    {
        return outgoingNo[13-1];
    }

    public void setOutgoingNo_13(int outgoingNo_13)
    {
        this.outgoingNo[13-1] = outgoingNo_13;
    }

    public Integer getOutgoingNo_14()
    {
        return outgoingNo[14-1];
    }

    public void setOutgoingNo_14(int outgoingNo_14)
    {
        this.outgoingNo[14-1] = outgoingNo_14;
    }

    public Integer getOutgoingNo_15()
    {
        return outgoingNo[15-1];
    }

    public void setOutgoingNo_15(int outgoingNo_15)
    {
        this.outgoingNo[15-1] = outgoingNo_15;
    }

    public Integer getOutgoingNo_16()
    {
        return outgoingNo[16-1];
    }

    public void setOutgoingNo_16(int outgoingNo_16)
    {
        this.outgoingNo[16-1] = outgoingNo_16;
    }

    public Integer getOutgoingNo_17()
    {
        return outgoingNo[17-1];
    }

    public void setOutgoingNo_17(int outgoingNo_17)
    {
        this.outgoingNo[17-1] = outgoingNo_17;
    }

    public Integer getOutgoingNo_18()
    {
        return outgoingNo[18-1];
    }

    public void setOutgoingNo_18(int outgoingNo_18)
    {
        this.outgoingNo[18-1] = outgoingNo_18;
    }

    public Integer getOutgoingNo_19()
    {
        return outgoingNo[19-1];
    }

    public void setOutgoingNo_19(int outgoingNo_19)
    {
        this.outgoingNo[19-1] = outgoingNo_19;
    }

    public Integer getOutgoingNo_20()
    {
        return outgoingNo[20-1];
    }

    public void setOutgoingNo_20(int outgoingNo_20)
    {
        this.outgoingNo[20-1] = outgoingNo_20;
    }

    public Integer getOutgoingNo_21()
    {
        return outgoingNo[21-1];
    }

    public void setOutgoingNo_21(int outgoingNo_21)
    {
        this.outgoingNo[21-1] = outgoingNo_21;
    }

    public Integer getOutgoingNo_22()
    {
        return outgoingNo[22-1];
    }

    public void setOutgoingNo_22(int outgoingNo_22)
    {
        this.outgoingNo[22-1] = outgoingNo_22;
    }

    public Integer getOutgoingNo_23()
    {
        return outgoingNo[23-1];
    }

    public void setOutgoingNo_23(int outgoingNo_23)
    {
        this.outgoingNo[23-1] = outgoingNo_23;
    }

    public Integer getOutgoingNo_24()
    {
        return outgoingNo[24-1];
    }

    public void setOutgoingNo_24(int outgoingNo_24)
    {
        this.outgoingNo[24-1] = outgoingNo_24;
    }

    public Integer getOutgoingNo_25()
    {
        return outgoingNo[25-1];
    }

    public void setOutgoingNo_25(int outgoingNo_25)
    {
        this.outgoingNo[25-1] = outgoingNo_25;
    }

    public Integer getOutgoingNo_26()
    {
        return outgoingNo[26-1];
    }

    public void setOutgoingNo_26(int outgoingNo_26)
    {
        this.outgoingNo[26-1] = outgoingNo_26;
    }

    public Integer getOutgoingNo_27()
    {
        return outgoingNo[27-1];
    }

    public void setOutgoingNo_27(int outgoingNo_27)
    {
        this.outgoingNo[27-1] = outgoingNo_27;
    }

    public Integer getOutgoingNo_28()
    {
        return outgoingNo[28-1];
    }

    public void setOutgoingNo_28(int outgoingNo_28)
    {
        this.outgoingNo[28-1] = outgoingNo_28;
    }

    public Integer getOutgoingNo_29()
    {
        return outgoingNo[29-1];
    }

    public void setOutgoingNo_29(int outgoingNo_29)
    {
        this.outgoingNo[29-1] = outgoingNo_29;
    }

    public Integer getOutgoingNo_30()
    {
        return outgoingNo[30-1];
    }

    public void setOutgoingNo_30(int outgoingNo_30)
    {
        this.outgoingNo[30-1] = outgoingNo_30;
    }

    public Integer getOutgoingNo_31()
    {
        return outgoingNo[31-1];
    }

    public void setOutgoingNo_31(int outgoingNo_31)
    {
        this.outgoingNo[31-1] = outgoingNo_31;
    }

    public Integer getOutgoingNoTotal()
    {
        return outgoingNoTotal;
    }

    public Integer getRemainingNo_01()
    {
        return remainingNo[1-1];
    }

    public void setRemainingNo_01(int remainingNo_01)
    {
        this.remainingNo[1-1] = remainingNo_01;
    }

    public Integer getRemainingNo_02()
    {
        return remainingNo[2-1];
    }

    public void setRemainingNo_02(int remainingNo_02)
    {
        this.remainingNo[2-1] = remainingNo_02;
    }

    public Integer getRemainingNo_03()
    {
        return remainingNo[3-1];
    }

    public void setRemainingNo_03(int remainingNo_03)
    {
        this.remainingNo[3-1] = remainingNo_03;
    }

    public Integer getRemainingNo_04()
    {
        return remainingNo[4-1];
    }

    public void setRemainingNo_04(int remainingNo_04)
    {
        this.remainingNo[4-1] = remainingNo_04;
    }

    public Integer getRemainingNo_05()
    {
        return remainingNo[5-1];
    }

    public void setRemainingNo_05(int remainingNo_05)
    {
        this.remainingNo[5-1] = remainingNo_05;
    }

    public Integer getRemainingNo_06()
    {
        return remainingNo[6-1];
    }

    public void setRemainingNo_06(int remainingNo_06)
    {
        this.remainingNo[6-1] = remainingNo_06;
    }

    public Integer getRemainingNo_07()
    {
        return remainingNo[7-1];
    }

    public void setRemainingNo_07(int remainingNo_07)
    {
        this.remainingNo[7-1] = remainingNo_07;
    }

    public Integer getRemainingNo_08()
    {
        return remainingNo[8-1];
    }

    public void setRemainingNo_08(int remainingNo_08)
    {
        this.remainingNo[8-1] = remainingNo_08;
    }

    public Integer getRemainingNo_09()
    {
        return remainingNo[9-1];
    }

    public void setRemainingNo_09(int remainingNo_09)
    {
        this.remainingNo[9-1] = remainingNo_09;
    }

    public Integer getRemainingNo_10()
    {
        return remainingNo[10-1];
    }

    public void setRemainingNo_10(int remainingNo_10)
    {
        this.remainingNo[10-1] = remainingNo_10;
    }

    public Integer getRemainingNo_11()
    {
        return remainingNo[11-1];
    }

    public void setRemainingNo_11(int remainingNo_11)
    {
        this.remainingNo[11-1] = remainingNo_11;
    }

    public Integer getRemainingNo_12()
    {
        return remainingNo[12-1];
    }

    public void setRemainingNo_12(int remainingNo_12)
    {
        this.remainingNo[12-1] = remainingNo_12;
    }

    public Integer getRemainingNo_13()
    {
        return remainingNo[13-1];
    }

    public void setRemainingNo_13(int remainingNo_13)
    {
        this.remainingNo[13-1] = remainingNo_13;
    }

    public Integer getRemainingNo_14()
    {
        return remainingNo[14-1];
    }

    public void setRemainingNo_14(int remainingNo_14)
    {
        this.remainingNo[14-1] = remainingNo_14;
    }

    public Integer getRemainingNo_15()
    {
        return remainingNo[15-1];
    }

    public void setRemainingNo_15(int remainingNo_15)
    {
        this.remainingNo[15-1] = remainingNo_15;
    }

    public Integer getRemainingNo_16()
    {
        return remainingNo[16-1];
    }

    public void setRemainingNo_16(int remainingNo_16)
    {
        this.remainingNo[16-1] = remainingNo_16;
    }

    public Integer getRemainingNo_17()
    {
        return remainingNo[17-1];
    }

    public void setRemainingNo_17(int remainingNo_17)
    {
        this.remainingNo[17-1] = remainingNo_17;
    }

    public Integer getRemainingNo_18()
    {
        return remainingNo[18-1];
    }

    public void setRemainingNo_18(int remainingNo_18)
    {
        this.remainingNo[18-1] = remainingNo_18;
    }

    public Integer getRemainingNo_19()
    {
        return remainingNo[19-1];
    }

    public void setRemainingNo_19(int remainingNo_19)
    {
        this.remainingNo[19-1] = remainingNo_19;
    }

    public Integer getRemainingNo_20()
    {
        return remainingNo[20-1];
    }

    public void setRemainingNo_20(int remainingNo_20)
    {
        this.remainingNo[20-1] = remainingNo_20;
    }

    public Integer getRemainingNo_21()
    {
        return remainingNo[21-1];
    }

    public void setRemainingNo_21(int remainingNo_21)
    {
        this.remainingNo[21-1] = remainingNo_21;
    }

    public Integer getRemainingNo_22()
    {
        return remainingNo[22-1];
    }

    public void setRemainingNo_22(int remainingNo_22)
    {
        this.remainingNo[22-1] = remainingNo_22;
    }

    public Integer getRemainingNo_23()
    {
        return remainingNo[23-1];
    }

    public void setRemainingNo_23(int remainingNo_23)
    {
        this.remainingNo[23-1] = remainingNo_23;
    }

    public Integer getRemainingNo_24()
    {
        return remainingNo[24-1];
    }

    public void setRemainingNo_24(int remainingNo_24)
    {
        this.remainingNo[24-1] = remainingNo_24;
    }

    public Integer getRemainingNo_25()
    {
        return remainingNo[25-1];
    }

    public void setRemainingNo_25(int remainingNo_25)
    {
        this.remainingNo[25-1] = remainingNo_25;
    }

    public Integer getRemainingNo_26()
    {
        return remainingNo[26-1];
    }

    public void setRemainingNo_26(int remainingNo_26)
    {
        this.remainingNo[26-1] = remainingNo_26;
    }

    public Integer getRemainingNo_27()
    {
        return remainingNo[27-1];
    }

    public void setRemainingNo_27(int remainingNo_27)
    {
        this.remainingNo[27-1] = remainingNo_27;
    }

    public Integer getRemainingNo_28()
    {
        return remainingNo[28-1];
    }

    public void setRemainingNo_28(int remainingNo_28)
    {
        this.remainingNo[28-1] = remainingNo_28;
    }

    public Integer getRemainingNo_29()
    {
        return remainingNo[29-1];
    }

    public void setRemainingNo_29(int remainingNo_29)
    {
        this.remainingNo[29-1] = remainingNo_29;
    }

    public Integer getRemainingNo_30()
    {
        return remainingNo[30-1];
    }

    public void setRemainingNo_30(int remainingNo_30)
    {
        this.remainingNo[30-1] = remainingNo_30;
    }

    public Integer getRemainingNo_31()
    {
        return remainingNo[31-1];
    }

    public void setRemainingNo_31(int remainingNo_31)
    {
        this.remainingNo[31-1] = remainingNo_31;
    }
    
    public Integer getRemainingNoTotal()
    {
        return remainingNoTotal;
    }

    public Integer getTermEndNo()
    {
        return termEndNo;
    }

    public void setTermEndNo(int termEndNo)
    {
        this.termEndNo = termEndNo;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public int getBrandId()
    {
        return brandId;
    }

    public void setBrandId(int brandId)
    {
        this.brandId = brandId;
    }

    
}
