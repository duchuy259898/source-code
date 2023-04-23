/*
 * EnvironmentalSetting.java
 *
 * Created on 2007/02/09, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.comm.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author katagiri
 */
public class EnvironmentalSetting
{
	private	ArrayList<MstSkin>	skinList		=	new ArrayList<MstSkin>();
	private	ArrayList<String>	serialPortList	=	new ArrayList<String>();
	
	/** Creates a new instance of EnvironmentalSetting */
	public EnvironmentalSetting()
	{
		this.loadSkinList();
		this.loadSerialPortList();
	}
	
	private void loadSkinList()
	{
		skinList.clear();
		
		ConnectionWrapper	con	=	SystemInfo.getBaseConnection();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(this.getSkinListSQL());
			
			while(rs.next())
			{
				MstSkin		ms	=	new MstSkin();
				ms.setData(rs);
				skinList.add(ms);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			
		}
	}
	
	private String getSkinListSQL()
	{
		return	"select *\n" +
				"from mst_skin\n" +
				"order by skin_id\n";
	}

	public ArrayList<MstSkin> getSkinList()
	{
		return skinList;
	}

	public void setSkinList(ArrayList<MstSkin> skinList)
	{
		this.skinList = skinList;
	}
	
	private void loadSerialPortList()
	{
		CommPortIdentifier portId;

		Enumeration en = CommPortIdentifier.getPortIdentifiers();

		while(en.hasMoreElements())
		{
                    portId = (CommPortIdentifier) en.nextElement();
			
                    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    {
                        if ( serialPortList.indexOf(portId.getName()) < 0 )
                        {
                            serialPortList.add(portId.getName());
                        }
                    }
		}
	}

	public ArrayList<String> getSerialPortList()
	{
		return serialPortList;
	}

	public void setSerialPortList(ArrayList<String> serialPortList)
	{
		this.serialPortList = serialPortList;
	}
	
    // 2008/09/08 Add pointcard information to parameter list
	public boolean regist(Integer skinID,
        Boolean isUseBarcodeReader,
        Boolean isUsePointcardReader,
        String barcodeReaderPort,
        Boolean isUseCtiReader,
        String ctiReaderPort,
        Integer pointOutputType) {
		boolean		result	=	false;
		
		ConnectionWrapper	con	=	SystemInfo.getBaseConnection();
		
        try {
			con.begin();
			
            try {
                if(con.executeUpdate(this.getRegistSkinSQL(skinID)) == 1) {
                    //if(con.executeUpdate(this.getRegistBarcodeInfoSQL(
                    //		isUseBarcodeReader, barcodeReaderPort)) == 1)
					if(con.executeUpdate(this.getRegistBarcodeInfoSQL(
                        isUseBarcodeReader, isUsePointcardReader, barcodeReaderPort, isUseCtiReader, ctiReaderPort, pointOutputType )) == 1) {
						result	=	true;
					}
				}
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            if(result) {
                con.commit();
            } else {
                con.rollback();
			}
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return	result;
    }

    public boolean registPoinrcardPort( String pointcardPort, Integer pointcardId ) {
        
        boolean result = false;
        
        ConnectionWrapper con = SystemInfo.getBaseConnection();
        
        try {
            con.begin();
            
            try {
                if(con.executeUpdate(this.getRegistPointcardPortSQL(pointcardPort, pointcardId)) == 1) {
                    result = true;
                }
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            if(result) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return	result;
    }

    public boolean registPointDefaultDateType(Integer type) {
        
        boolean result = false;
        
        ConnectionWrapper con = SystemInfo.getBaseConnection();
        
        try {
            con.begin();

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" update mst_mac");
            sql.append(" set");
            sql.append("     point_default_date_type = " + SQLUtil.convertForSQL(type));
            sql.append(" where");
            sql.append("         login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()));
            sql.append("     and mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID()));
            
            try {
                if(con.executeUpdate(sql.toString()) == 1) {
                    result = true;
                }
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            if(result) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return	result;
    }
    
	private String getRegistSkinSQL(Integer skinID)
	{
		return	"update mst_user\n" +
				"set skin_id = " + SQLUtil.convertForSQL(skinID) + "\n" +
				"where login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n";
	}
	
    // 2008/09/08 Add Pointcard information to SQL
	private String getRegistBarcodeInfoSQL(
        Boolean isUseBarcodeReader,
        Boolean isUsePointcardReader,
        String barcodeReaderPort,
        Boolean isUseCtiReader,
        String ctiReaderPort,
        Integer pointOutputType) {
        return	"update mst_mac\n" +
            "set use_barcode_reader = " + SQLUtil.convertForSQL(isUseBarcodeReader) + ",\n" +
            "use_pointcard_reader = " + SQLUtil.convertForSQL(isUsePointcardReader) + ",\n" +
            "barcode_reader_port = " + SQLUtil.convertForSQL(barcodeReaderPort) + ",\n" +
            "use_cti_reader = " + SQLUtil.convertForSQL(isUseCtiReader) + ",\n" +
            "cti_reader_port = " + SQLUtil.convertForSQL(ctiReaderPort) + ",\n" +
            "point_output_type = " + SQLUtil.convertForSQL(pointOutputType) + "\n" +
            "where login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n" +
            "and mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID()) + "\n";

                /*return	"update mst_mac\n" +
				"set use_barcode_reader = " + SQLUtil.convertForSQL(isUseBarcodeReader) + ",\n" +
				"barcode_reader_port = " + SQLUtil.convertForSQL(barcodeReaderPort) + "\n" +
				"where login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n" +
				"and mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID()) + "\n";
                 */
	}
    
    private String getRegistPointcardPortSQL(String port, Integer id) {
        return	"update mst_mac\n" +
            "set pointcard_reader_port = " + SQLUtil.convertForSQL(port) + "\n" +
            ",   pointcard_reader_product_id = " + SQLUtil.convertForSQL(id) + "\n" +
            "where login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n" +
            "and mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID()) + "\n";
    }
    
}
