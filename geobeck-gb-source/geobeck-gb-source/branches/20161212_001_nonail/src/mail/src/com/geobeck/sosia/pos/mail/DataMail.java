/*
 * DataMail.java
 *
 * Created on 2006/11/06, 11:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.mail;


import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.util.StringUtil;

/**
 *
 * @author katagiri
 */

public class DataMail extends MstCustomer
{
	private	String	    sendMailAddress	= "";
	private	String	    mailTitle		= "";
	private	String	    mailBody		= "";
        
        private	String[]    managerName		= {"", ""};     

        public void setSendMailAddress(String sendMailAddress) {
            this.sendMailAddress = sendMailAddress;
        }

	
	/** Creates a new instance of DataMail */
	public DataMail()
	{
	}
	
	public DataMail(MstCustomer customer)
	{
		this.setData(customer);
	}
	
	public DataMail(MstCustomer customer, Integer selectedAddress)
	{
		this.setData(customer);
		this.setSendMailAddress(selectedAddress);
	}

	public DataMail(MstCustomer customer, Integer selectedAddress, String mailTitle, String mailBody)
	{
		this.setData(customer);
		this.setSendMailAddress(selectedAddress);
                this.setMailTitle(mailTitle);
                this.setMailBody(mailBody);
	}
        
        public DataMail(MstManager manager)
	{
		this.setData(manager);
                sendMailAddress	=	this.getPCMailAddress();
                
	}
                
	public String getSendMailAddress()
	{
		return sendMailAddress;
	}

	public void setSendMailAddress(Integer selectedAddress)
	{
		switch(selectedAddress)
		{
			case 2:
			case 3:
				sendMailAddress	=	this.getPCMailAddress();
				if(selectedAddress == 2 && ( sendMailAddress == null || sendMailAddress.equals("") ) )
				{
					sendMailAddress	=	this.getCellularMailAddress();
				}
				break;
			case 0:
			case 1:
				sendMailAddress	=	this.getCellularMailAddress();
				if(selectedAddress == 0 && ( sendMailAddress == null || sendMailAddress.equals("") ) )
				{
					sendMailAddress	=	this.getPCMailAddress();
				}
				break;
		}
	}

	public String getMailTitle()
	{
		return StringUtil.replaceInvalidString(mailTitle);
	}

	public void setMailTitle(String mailTitle)
	{
		this.mailTitle = mailTitle;
	}

	public String getMailBody()
	{
		return StringUtil.replaceInvalidString(mailBody);
	}

	public void setMailBody(String mailBody)
	{
		this.mailBody = mailBody;
	}
	
	public String toString()
	{
		return	this.getFullCustomerName();
	}
        
        public void setData( MstManager manager ) {

        //this.setManagerName(0, manager.getManagerName(0) );
        //this.setManagerName(1, manager.getManagerName(1) );
        this.setCustomerName(0, manager.getManagerName(0));
        this.setCustomerName(1, manager.getManagerName(1));
        this.setPCMailAddress(manager.getMailAddress() );
    }
        /**
     * �Ǘ��Җ����擾����B
     * @return �Ǘ��Җ�
     */
    public String[] getManagerName() {
        return managerName;
    }
    
    /**
     * �Ǘ��Җ����擾����B
     * @param index �C���f�b�N�X
     * @return �Ǘ��Җ�
     */
    public String getManagerName(int index) {
        return managerName[index];
    }
    
    /**
     * �Ǘ��Җ����Z�b�g����B
     * @param staffName �Ǘ��Җ�
     */
    public void setManagerName(String[] managerName) {
        this.managerName = managerName;
    }
    
    /**
     * �Ǘ��Җ����Z�b�g����B
     * @param index �C���f�b�N�X
     * @param staffName �Ǘ��Җ�
     */
    public void setManagerName(int index, String managerName) {
        this.managerName[index] = managerName;
    }
    
    /**
     * �Ǘ��҂̃t���l�[�����擾����B
     * @return �t���l�[��
     */
    public String getFullManagerName() {
        return	(managerName[0] == null ? "" : managerName[0])
        +	(managerName[1] == null || managerName[1].equals("") ? "" : "�@" + managerName[1]);
    }
    
}
