/*
 * LabelManager.java
 *
 * Created on 2006/09/08, 18:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.label;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;

/**
 *
 * @author katagiri
 */
public class LabelManager
{
	private MstSheets		sheets			=	new MstSheets();
	private MstLabelMakers	labelMakers		=	new MstLabelMakers();
	
	/**
	 * Creates a new instance of LabelManager
	 */
	public LabelManager()
	{
	}

	public MstSheets getSheets()
	{
		return sheets;
	}

	public void setSheets(MstSheets sheets)
	{
		this.sheets = sheets;
	}

	public MstLabelMakers getLabelMakers()
	{
		return labelMakers;
	}

	public void setLabelMakers(MstLabelMakers labelMakers)
	{
		this.labelMakers = labelMakers;
	}
	
	public void init(ConnectionWrapper con) throws SQLException
	{
		sheets.load(con);
		labelMakers.load(con);

		for(MstLabelMaker mlm : labelMakers)
		{
			mlm.loadLabel(con);

			for(MstLabel ml : mlm)
			{
				if(ml.getSheet().getSheetID() != null)
				{
					ml.setSheet(sheets.getSheetByID(ml.getSheet().getSheetID()));
				}
			}
		}
	}
}
