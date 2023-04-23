package com.geobeck.sosia.pos.hair.product.beans;
/*
 * TotalInventorySubReportBean.java
 *
 * Created on 2008/09/25, 15:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author trino
 */
public class TotalInventorySubReportBean
{
    private int id;
    private String categoryName;
    private Integer initialStockShop;
    private Integer initialStockBusiness;
    private Integer initialStockTotal;
    private Integer incomingStockShop;
    private Integer incomingStockBusiness;
    private Integer incomingStockTotal;
    private Integer outgoingStockShop;
    private Integer outgoingStockBusiness;
    private Integer outgoingStockTotal;
    private Integer registerStockShop;
    private Integer registerStockBusiness;
    private Integer registerStockTotal;
    private Integer existingStockShop;
    private Integer existingStockBusiness;
    private Integer existingStockTotal;
    private Integer excessStockShop;
    private Integer excessStockBusiness;
    private Integer excessStockTotal;
            
    /**
     * Creates a new instance of TotalInventorySubReportBean
     */
    public TotalInventorySubReportBean()
    {
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public Integer getInitialStockShop()
    {
        return initialStockShop;
    }

    public void setInitialStockShop(int initialStockShop)
    {
        this.initialStockShop = initialStockShop;
    }

    public Integer getInitialStockBusiness()
    {
        return initialStockBusiness;
    }

    public void setInitialStockBusiness(int initialStockBusiness)
    {
        this.initialStockBusiness = initialStockBusiness;
    }

    public Integer getInitialStockTotal()
    {
        return initialStockTotal;
    }

    public void setInitialStockTotal(int initialStockTotal)
    {
        this.initialStockTotal = initialStockTotal;
    }

    public void setInitialStockTotal()
    {
        int shopVal = 0;
        int businessVal = 0;
        
        if( this.initialStockBusiness != null )
        {
            businessVal = this.initialStockBusiness.intValue();
        }
        if( this.initialStockShop != null )
        {
            shopVal = this.initialStockShop.intValue();
        }
        this.initialStockTotal = ( shopVal + businessVal );        
    }
    
    public Integer getIncomingStockShop()
    {
        return incomingStockShop;
    }

    public void setIncomingStockShop(int incomingStockShop)
    {
        this.incomingStockShop = incomingStockShop;
    }

    public Integer getIncomingStockBusiness()
    {
        return incomingStockBusiness;
    }

    public void setIncomingStockBusiness(int incomingStockBusiness)
    {
        this.incomingStockBusiness = incomingStockBusiness;
    }

    public Integer getIncomingStockTotal()
    {
        return incomingStockTotal;
    }

    public void setIncomingStockTotal(int incomingStockTotal)
    {
        this.incomingStockTotal = incomingStockTotal;
    }
    
    public void setIncomingStockTotal()
    {
        int shopVal = 0;
        int businessVal = 0;
        
        if( this.incomingStockBusiness != null )
        {
            businessVal = this.incomingStockBusiness.intValue();
        }
        if( this.incomingStockShop != null )
        {
            shopVal = this.incomingStockShop.intValue();
        }
        this.incomingStockTotal = ( shopVal + businessVal );        
    }

    public Integer getOutgoingStockShop()
    {
        return outgoingStockShop;
    }

    public void setOutgoingStockShop(int outgoingStockShop)
    {
        this.outgoingStockShop = outgoingStockShop;
    }

    public Integer getOutgoingStockBusiness()
    {
        return outgoingStockBusiness;
    }

    public void setOutgoingStockBusiness(int outgoingStockBusiness)
    {
        this.outgoingStockBusiness = outgoingStockBusiness;
    }

    public Integer getOutgoingStockTotal()
    {
        return outgoingStockTotal;
    }

    public void setOutgoingStockTotal(int outgoingStockTotal)
    {
        this.outgoingStockTotal = outgoingStockTotal;
    }

    public void setOutgoingStockTotal()
    {
        int shopVal = 0;
        int businessVal = 0;
        
        if( this.outgoingStockBusiness != null )
        {
            businessVal = this.outgoingStockBusiness.intValue();
        }
        if( this.outgoingStockShop != null )
        {
            shopVal = this.outgoingStockShop.intValue();
        }
        this.outgoingStockTotal = ( shopVal + businessVal );        
    }
    
    public Integer getRegisterStockShop()
    {
        return registerStockShop;
    }

    public void setRegisterStockShop(int registerStockShop)
    {
        this.registerStockShop = registerStockShop;
    }
    
    /*
    public void setRegisterStockShop()
    {
        int initVal = 0;
        int inVal = 0;
        int outVal = 0;
        
        if( this.initialStockShop != null )
        {
            initVal = this.initialStockShop.intValue();
        }
        if( this.incomingStockShop != null )
        {
            inVal = this.incomingStockShop.intValue();
        }
        if( this.outgoingStockShop != null )
        {
            outVal = this.outgoingStockShop.intValue();
        }
        
        this.registerStockShop = (( initVal + inVal ) - outVal);
    }
     */

    public Integer getRegisterStockBusiness()
    {
        return registerStockBusiness;
    }

    public void setRegisterStockBusiness(int registerStockBusiness)
    {
        this.registerStockBusiness = registerStockBusiness;
    }
    
    /*
    public void setRegisterStockBusiness()
    {
        int initVal = 0;
        int inVal = 0;
        int outVal = 0;
        
        if( this.initialStockBusiness != null )
        {
            initVal = this.initialStockBusiness.intValue();
        }
        if( this.incomingStockBusiness != null )
        {
            inVal = this.incomingStockBusiness.intValue();
        }
        if( this.outgoingStockBusiness != null )
        {
            outVal = this.outgoingStockBusiness.intValue();
        }
        
        this.registerStockBusiness = (( initVal + inVal ) - outVal);
    }
     */

    public Integer getRegisterStockTotal()
    {
        return registerStockTotal;
    }

    public void setRegisterStockTotal(int registerStockTotal)
    {
        this.registerStockTotal = registerStockTotal;
    }
    
    public void setRegisterStockTotal()
    {
        int shopVal = 0;
        int businessVal = 0;
        
        if( this.registerStockBusiness != null )
        {
            businessVal = this.registerStockBusiness.intValue();
        }
        if( this.registerStockShop != null )
        {
            shopVal = this.registerStockShop.intValue();
        }
        this.registerStockTotal = ( shopVal + businessVal );        
    }

    public Integer getExistingStockShop()
    {
        return existingStockShop;
    }

    public void setExistingStockShop(int existingStockShop)
    {
        this.existingStockShop = existingStockShop;
    }

    public Integer getExistingStockBusiness()
    {
        return existingStockBusiness;
    }

    public void setExistingStockBusiness(int existingStockBusiness)
    {
        this.existingStockBusiness = existingStockBusiness;
    }

    public Integer getExistingStockTotal()
    {
        return existingStockTotal;
    }

    public void setExistingStockTotal(int existingStockTotal)
    {
        this.existingStockTotal = existingStockTotal;
    }
    
    public void setExistingStockTotal()
    {
        int shopVal = 0;
        int businessVal = 0;
        
        if( this.existingStockBusiness != null )
        {
            businessVal = this.existingStockBusiness.intValue();
        }
        if( this.existingStockShop != null )
        {
            shopVal = this.existingStockShop.intValue();
        }
        this.existingStockTotal = ( shopVal + businessVal );        
    }

    public Integer getExcessStockShop()
    {
        return excessStockShop;
    }

    public void setExcessStockShop(int excessStockShop)
    {
        this.excessStockShop = excessStockShop;
    }

    public void setExcessStockShop()
    {
        int registerVal = 0;
        int actualVal = 0;
        
        if( this.registerStockShop != null )
        {
            registerVal = this.registerStockShop.intValue();
        }
        if( this.existingStockShop != null )
        {
            actualVal = this.existingStockShop.intValue();
        }
        this.excessStockShop = ( actualVal - registerVal  );
    }
    
    public Integer getExcessStockBusiness()
    {
        return excessStockBusiness;
    }

    public void setExcessStockBusiness(int excessStockBusiness)
    {
        this.excessStockBusiness = excessStockBusiness;
    }
    
    public void setExcessStockBusiness()
    {
        int registerVal = 0;
        int actualVal = 0;
        
        if( this.registerStockBusiness != null )
        {
            registerVal = this.registerStockBusiness.intValue();
        }
        if( this.existingStockBusiness != null )
        {
            actualVal = this.existingStockBusiness.intValue();
        }
        this.excessStockBusiness = ( actualVal - registerVal );
    }

    public Integer getExcessStockTotal()
    {
        return excessStockTotal;
    }

    public void setExcessStockTotal(int excessStockTotal)
    {
        this.excessStockTotal = excessStockTotal;
    }
    
    public void setExcessStockTotal()
    {
        int shopVal = 0;
        int businessVal = 0;
        
        if( this.excessStockBusiness != null )
        {
            businessVal = this.excessStockBusiness.intValue();
        }
        if( this.excessStockShop != null )
        {
            shopVal = this.excessStockShop.intValue();
        }
        this.excessStockTotal = ( shopVal + businessVal );        
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
}
