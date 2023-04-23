/*
 * DataTargetResultBean.java
 *
 * Created on 2008/09/24, 14:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.basicinfo.company;

/**
 *
 * @author trino
 */
public class DataTargetResultBean
{
    //CONSTANTS:
    public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;
    
    //variables:
    private int shopId;
    private int year;
    private int month;
    private long resultTotal; 
    private long resultTechnic;
    private long resultItem;
    private long resultIn;
    private long resultAvgAmount;
    private long resultNew;
    private long resultK;
    private long resultSET;
    private long resultS;
    private long resultHD;
    private long resultP;
    private long resultSTP;
    private long resultTR;
    private long resultETC;
    private long resultCRM;
    private long resultMON;
    private long targetTotal;
    private long targetTechnic;
    private long targetItem;
    private long targetIn;
    private long targetAvgAmount;
    private long targetNew;
    private long targetK;
    private long targetSET;
    private long targetS;
    private long targetHD;
    private long targetP;
    private long targetSTP;
    private long targetTR;
    private long targetETC;
    private long targetCRM;
    private long targetMON;
    private int openDays;
    
    /** Creates a new instance of DataTargetResultBean */
    public DataTargetResultBean()
    {        
    }
    
    public void resetValues()
    {
        resultTotal = 0; 
        resultTechnic = 0;
        resultItem = 0;
        resultIn = 0;
        resultAvgAmount = 0;
        resultNew = 0;
        resultK = 0;
        resultSET = 0;
        resultS = 0;
        resultHD = 0;
        resultP = 0;
        resultSTP = 0;
        resultTR = 0;
        resultETC = 0;
        resultCRM = 0;
        resultMON = 0;
        targetTotal = 0;
        targetTechnic = 0;
        targetItem = 0;
        targetIn = 0;
        targetAvgAmount = 0;
        targetNew = 0;
        targetK = 0;
        targetSET = 0;
        targetS = 0;
        targetHD = 0;
        targetP = 0;
        targetSTP = 0;
        targetTR = 0;
        targetETC = 0;
        targetCRM = 0;
        targetMON = 0;
        openDays = 0;
    }

    public int getShopId()
    {
        return shopId;
    }

    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public long getResultTotal()
    {
        return resultTotal;
    }
    
    public String getResultTotalStr()
    {
        return String.valueOf( resultTotal );
    }

    public void setResultTotal(long resultTotal)
    {
        this.resultTotal = resultTotal;
    }
    
    public void setResultTotal(String resultTotal)
    {
        if( resultTotal.length() > 0 )
        {
            this.resultTotal = Long.parseLong( resultTotal );
        }
    }

    public long getResultTechnic()
    {
        return resultTechnic;
    }
    
    public String getResultTechnicStr()
    {
        return String.valueOf( resultTechnic );
    }

    public void setResultTechnic(long resultTechnic)
    {
        this.resultTechnic = resultTechnic;
    }
    
    public void setResultTechnic(String resultTechnic)
    {
        if( resultTechnic.length() > 0 )
        {
            this.resultTechnic = Long.parseLong( resultTechnic );
        }
    }

    public long getResultItem()
    {
        return resultItem;
    }
    
    public String getResultItemStr()
    {
        return String.valueOf( resultItem );
    }

    public void setResultItem(long resultItem)
    {
        this.resultItem = resultItem;
    }
    
    public void setResultItem(String resultItem)
    {
        if( resultItem.length() > 0 )
        {
            this.resultItem = Long.parseLong( resultItem );
        }
    }

    public long getResultIn()
    {
        return resultIn;
    }

    public String getResultInStr()
    {
        return String.valueOf( resultIn );
    }
    
    public void setResultIn(long resultIn)
    {
        this.resultIn = resultIn;
    }
    
    public void setResultIn(String resultIn)
    {
        if( resultIn.length() > 0 )
        {
            this.resultIn = Long.parseLong( resultIn );
        }
    }

    public long getResultAvgAmount()
    {
        return resultAvgAmount;
    }
    
    public String getResultAvgAmountStr()
    {
        return String.valueOf( resultAvgAmount );
    }

    public void setResultAvgAmount(long resultAvgAmount)
    {
        this.resultAvgAmount = resultAvgAmount;
    }
    
    public void setResultAvgAmount(String resultAvgAmount)
    {
        if( resultAvgAmount.length() > 0 )
        {
            this.resultAvgAmount = Long.parseLong( resultAvgAmount );
        }
    }

    public long getResultNew()
    {
        return resultNew;
    }
    
    public String getResultNewStr()
    {
        return String.valueOf( resultNew );
    }

    public void setResultNew(long resultNew)
    {
        this.resultNew = resultNew;
    }
    
    public void setResultNew(String resultNew)
    {
        if( resultNew.length() > 0 )
        {
            this.resultNew = Long.parseLong( resultNew );
        }
    }

    public long getResultK()
    {
        return resultK;
    }
    
    public String getResultKStr()
    {
        return String.valueOf( resultK );
    }

    public void setResultK(long resultK)
    {
        this.resultK = resultK;
    }
    
    public void setResultK(String resultK)
    {
        if( resultK.length() > 0 )
        {
            this.resultK = Long.parseLong( resultK );
        }
    }

    public long getResultSET()
    {
        return resultSET;
    }
    
    public String getResultSETStr()
    {
        return String.valueOf( resultSET );
    }

    public void setResultSET(long resultSET)
    {
        this.resultSET = resultSET;
    }
    
    public void setResultSET(String resultSET)
    {
        if( resultSET.length() > 0 )
        {
            this.resultSET = Long.parseLong( resultSET );
        }
    }
    
    public long getResultS()
    {
        return resultS;
    }
    
    public String getResultSStr()
    {
        return String.valueOf( resultS );
    }

    public void setResultS(long resultS)
    {
        this.resultS = resultS;
    }
    
    public void setResultS(String resultS)
    {
        if( resultS.length() > 0 )
        {
            this.resultS = Integer.valueOf( resultS );
        }
    }

    public long getResultHD()
    {
        return resultHD;
    }
    
    public String getResultHDStr()
    {
        return String.valueOf( resultHD );
    }

    public void setResultHD(long resultHD)
    {
        this.resultHD = resultHD;
    }
    
    public void setResultHD(String resultHD)
    {
        if( resultHD.length() > 0 )
        {
            this.resultHD = Long.parseLong( resultHD );
        }
    }

    public long getResultP()
    {
        return resultP;
    }
    
    public String getResultPStr()
    {
        return String.valueOf( resultP );
    }

    public void setResultP(long resultP)
    {
        this.resultP = resultP;
    }
    
    public void setResultP(String resultP)
    {
        if( resultP.length() > 0 )
        {
            this.resultP = Long.parseLong( resultP );
        }
    }

    public long getResultSTP()
    {
        return resultSTP;
    }
    
    public String getResultSTPStr()
    {
        return String.valueOf( resultSTP );
    }

    public void setResultSTP(long resultSTP)
    {
        this.resultSTP = resultSTP;
    }
    
    public void setResultSTP(String resultSTP)
    {
        if( resultSTP.length() > 0 )
        {
            this.resultSTP = Long.parseLong( resultSTP );
        }
    }

    public long getResultTR()
    {
        return resultTR;
    }
    
    public String getResultTRStr()
    {
        return String.valueOf( resultTR );
    }

    public void setResultTR(long resultTR)
    {
        this.resultTR = resultTR;
    }
    
    public void setResultTR(String resultTR)
    {
        if( resultTR.length() > 0 )
        {
            this.resultTR = Long.parseLong( resultTR );
        }
    }

    public long getResultETC()
    {
        return resultETC;
    }
    
    public String getResultETCStr()
    {
        return String.valueOf( resultETC );
    }

    public void setResultETC(long resultETC)
    {
        this.resultETC = resultETC;
    }
   
    public void setResultETC(String resultETC)
    {
        if( resultETC.length() > 0 )
        {
            this.resultETC = Long.parseLong( resultETC );
        }
    }

    public long getResultCRM()
    {
        return resultCRM;
    }
    
    public String getResultCRMStr()
    {
        return String.valueOf( resultCRM );
    }

    public void setResultCRM(long resultCRM)
    {
        this.resultCRM = resultCRM;
    }
    
    public void setResultCRM(String resultCRM)
    {
        if( resultCRM.length() > 0 )
        {
            this.resultCRM = Long.parseLong( resultCRM );
        }
    }

    public long getResultMON()
    {
        return resultMON;
    }
    
    public String getResultMONStr()
    {
        return String.valueOf( resultMON );
    }

    public void setResultMON(long resultMON)
    {
        this.resultMON = resultMON;
    }
    
    public void setResultMON(String resultMON)
    {
        if( resultMON.length() > 0 )
        {
            this.resultMON = Long.parseLong( resultMON );
        }
    }

    public long getTargetTotal()
    {
        return targetTotal;
    }
    
    public String getTargetTotalStr()
    {
        return String.valueOf( targetTotal );
    }

    public void setTargetTotal(long targetTotal)
    {
        this.targetTotal = targetTotal;
    }
    
    public void setTargetTotal(String targetTotal)
    {
        if( targetTotal.length() > 0 )
        {
            this.targetTotal = Long.parseLong( targetTotal );
        }
    }

    public long getTargetTechnic()
    {
        return targetTechnic;
    }
    
    public String getTargetTechnicStr()
    {
        return String.valueOf( targetTechnic );
    }

    public void setTargetTechnic(long targetTechnic)
    {
        this.targetTechnic = targetTechnic;
    }
    
    public void setTargetTechnic(String targetTechnic)
    {
        if( targetTechnic.length() > 0 )
        {
            this.targetTechnic = Long.parseLong( targetTechnic );
        }
    }

    public long getTargetItem()
    {
        return targetItem;
    }
    
    public String getTargetItemStr()
    {
        return String.valueOf( targetItem );
    }

    public void setTargetItem(long targetItem)
    {
        this.targetItem = targetItem;
    }
    
    public void setTargetItem(String targetItem)
    {
        if( targetItem.length() > 0 )
        {
            this.targetItem = Long.parseLong( targetItem );
        }
    }

    public long getTargetIn()
    {
        return targetIn;
    }
    
    public String getTargetInStr()
    {
        return String.valueOf( targetIn );
    }

    public void setTargetIn(long targetIn)
    {
        this.targetIn = targetIn;
    }
    
    public void setTargetIn(String targetIn)
    {
        if( targetIn.length() > 0 )
        {
            this.targetIn = Long.parseLong( targetIn );
        }
    }

    public long getTargetAvgAmount()
    {
        return targetAvgAmount;
    }
    
    public String getTargetAvgAmountStr()
    {
        return String.valueOf( targetAvgAmount );
    }

    public void setTargetAvgAmount(long targetAvgAmount)
    {
        this.targetAvgAmount = targetAvgAmount;
    }
    
    public void setTargetAvgAmount(String targetAvgAmount)
    {
        if( targetAvgAmount.length() > 0 )
        {
            this.targetAvgAmount = Long.parseLong( targetAvgAmount );
        }
    }

    public long getTargetNew()
    {
        return targetNew;
    }
    
    public String getTargetNewStr()
    {
        return String.valueOf( targetNew );
    }

    public void setTargetNew(long targetNew)
    {
        this.targetNew = targetNew;
    }
    
    public void setTargetNew(String targetNew)
    {
        if( targetNew.length() > 0 )
        {
            this.targetNew = Long.parseLong( targetNew );
        }
    }

    public long getTargetK()
    {
        return targetK;
    }
    
    public String getTargetKStr()
    {
        return String.valueOf( targetK );
    }

    public void setTargetK(long targetK)
    {
        this.targetK = targetK;
    }

    public void setTargetK(String targetK)
    {
        if( targetK.length() > 0 )
        {
            this.targetK = Long.parseLong( targetK );
        }
    }

    public long getTargetSET()
    {
        return targetSET;
    }
    
    public String getTargetSETStr()
    {
        return String.valueOf( targetSET );
    }

    public void setTargetSET(long targetSET)
    {
        this.targetSET = targetSET;
    }
    
    public void setTargetSET(String targetSET)
    {
        if( targetSET.length() > 0 )
        {
            this.targetSET = Long.parseLong( targetSET );
        }
    }

    public long getTargetS()
    {
        return targetS;
    }
    
    public String getTargetSStr()
    {
        return String.valueOf( targetS );
    }

    public void setTargetS(long targetS)
    {
        this.targetS = targetS;
    }
    
    public void setTargetS(String targetS)
    {
        if( targetS.length() > 0 )
        {
            this.targetS = Long.parseLong( targetS );
        }
    }

    public long getTargetHD()
    {
        return targetHD;
    }
    
    public String getTargetHDStr()
    {
        return String.valueOf( targetHD );
    }

    public void setTargetHD(long targetHD)
    {
        this.targetHD = targetHD;
    }
    
    public void setTargetHD(String targetHD)
    {
        if( targetHD.length() > 0 )
        {
            this.targetHD = Long.parseLong( targetHD );
        }
    }

    public long getTargetP()
    {
        return targetP;
    }
    
    public String getTargetPStr()
    {
        return String.valueOf( targetP );
    }

    public void setTargetP(long targetP)
    {
        this.targetP = targetP;
    }
    
    public void setTargetP(String targetP)
    {
        if( targetP.length() > 0 )
        {
            this.targetP = Long.parseLong( targetP );
        }
    }

    public long getTargetSTP()
    {
        return targetSTP;
    }
    
    public String getTargetSTPStr()
    {
        return String.valueOf( targetSTP );
    }

    public void setTargetSTP(long targetSTP)
    {
        this.targetSTP = targetSTP;
    }
    
    public void setTargetSTP(String targetSTP)
    {
        if( targetSTP.length() > 0 )
        {
            this.targetSTP = Long.parseLong( targetSTP );
        }
    }

    public long getTargetTR()
    {
        return targetTR;
    }
    
    public String getTargetTRStr()
    {
        return String.valueOf( targetTR );
    }

    public void setTargetTR(long targetTR)
    {
        this.targetTR = targetTR;
    }
    
    public void setTargetTR(String targetTR)
    {
        if( targetTR.length() > 0 )
        {
            this.targetTR = Long.parseLong( targetTR );
        }
    }

    public long getTargetETC()
    {
        return targetETC;
    }
    
    public String getTargetETCStr()
    {
        return String.valueOf( targetETC );
    }

    public void setTargetETC(long targetETC)
    {
        this.targetETC = targetETC;
    }
    
    public void setTargetETC(String targetETC)
    {
        if( targetETC.length() > 0 )
        {
            this.targetETC = Long.parseLong( targetETC );
        }
    }

    public long getTargetCRM()
    {
        return targetCRM;
    }
    
    public String getTargetCRMStr()
    {
        return String.valueOf( targetCRM );
    }

    public void setTargetCRM(long targetCRM)
    {
        this.targetCRM = targetCRM;
    }
    
    public void setTargetCRM(String targetCRM)
    {
        if( targetCRM.length() > 0 )
        {
            this.targetCRM = Long.parseLong( targetCRM );
        }
    }

    public long getTargetMON()
    {
        return targetMON;
    }
    
    public String getTargetMONStr()
    {
        return String.valueOf( targetMON );
    }

    public void setTargetMON(long targetMON)
    {
        this.targetMON = targetMON;
    }
    
    public void setTargetMON(String targetMON)
    {
        if( targetMON.length() > 0 )
        {
            this.targetMON = Long.parseLong( targetMON );
        }
    }

    public int getOpenDays()
    {
        return openDays;
    }
    
    public String getOpenDaysStr()
    {
        return String.valueOf( openDays );
    }

    public void setOpenDays(int openDays)
    {
        this.openDays = openDays;
    }
    
    public void setOpenDays(String openDays)
    {
        if( openDays.length() > 0 )
        {
            this.openDays = Integer.parseInt( openDays );
        }
    }    
}
