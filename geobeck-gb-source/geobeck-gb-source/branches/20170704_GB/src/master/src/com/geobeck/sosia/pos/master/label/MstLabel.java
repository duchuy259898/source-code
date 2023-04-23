/*
 * MstLabel.java
 *
 * Created on 2006/09/08, 17:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.label;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;

/**
 *
 * @author katagiri
 */
public class MstLabel
{
	private	MstLabelMaker	labelMaker			=	new MstLabelMaker();
	private	Integer			labelID				=	null;
	private	String			labelName			=	"";
	private	Integer			rowCount			=	0;
	private	Integer			columnCount			=	0;
	private	MstSheet		sheet				=	new MstSheet();
	private	Double			leftMargin			=	0d;
	private	Double			topMargin			=	0d;
	private	Double			labelWidth			=	0d;
	private	Double			labelHeight			=	0d;
	private	Double			verticalSpacing		=	0d;
	private	Double			horizontalSpacing	=	0d;
	
	/** Creates a new instance of MstLabel */
	public MstLabel()
	{
	}

	public MstLabelMaker getLabelMaker()
	{
		return labelMaker;
	}

	public void setLabelMaker(MstLabelMaker labelMaker)
	{
		this.labelMaker = labelMaker;
	}

	public Integer getLabelID()
	{
		return labelID;
	}

	public void setLabelID(Integer labelID)
	{
		this.labelID = labelID;
	}

	public String getLabelName()
	{
		return labelName;
	}

	public void setLabelName(String labelName)
	{
		this.labelName = labelName;
	}

	public Integer getRowCount()
	{
		return rowCount;
	}

	public void setRowCount(Integer rowCount)
	{
		this.rowCount = rowCount;
	}

	public Integer getColumnCount()
	{
		return columnCount;
	}

	public void setColumnCount(Integer columnCount)
	{
		this.columnCount = columnCount;
	}

	public MstSheet getSheet()
	{
		return sheet;
	}

	public void setSheet(MstSheet sheet)
	{
		this.sheet = sheet;
	}

	public Double getLeftMargin()
	{
		return leftMargin;
	}

	public void setLeftMargin(Double leftMargin)
	{
		this.leftMargin = leftMargin;
	}

	public Double getTopMargin()
	{
		return topMargin;
	}

	public void setTopMargin(Double topMargin)
	{
		this.topMargin = topMargin;
	}

	public Double getLabelWidth()
	{
		return labelWidth;
	}

	public void setLabelWidth(Double labelWidth)
	{
		this.labelWidth = labelWidth;
	}

	public Double getLabelHeight()
	{
		return labelHeight;
	}

	public void setLabelHeight(Double labelHeight)
	{
		this.labelHeight = labelHeight;
	}

	public Double getVerticalSpacing()
	{
		return verticalSpacing;
	}

	public void setVerticalSpacing(Double verticalSpacing)
	{
		this.verticalSpacing = verticalSpacing;
	}

	public Double getHorizontalSpacing()
	{
		return horizontalSpacing;
	}

	public void setHorizontalSpacing(Double horizontalSpacing)
	{
		this.horizontalSpacing = horizontalSpacing;
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstLabelMaker	mlm	=	new MstLabelMaker();
		this.setLabelMaker(mlm);
		this.setLabelName(rs.getString("label_name"));
		this.setRowCount(rs.getInt("row_count"));
		this.setColumnCount(rs.getInt("column_count"));
		MstSheet	ms	=	new MstSheet();
		ms.setSheetID(rs.getInt("sheet_id"));
		ms.setSheetWidth(rs.getDouble("sheet_width"));
		ms.setSheetHeight(rs.getDouble("sheet_height"));
		this.setSheet(ms);
		this.setLeftMargin(rs.getDouble("left_margin"));
		this.setTopMargin(rs.getDouble("top_margin"));
		this.setLabelWidth(rs.getDouble("label_width"));
		this.setLabelHeight(rs.getDouble("label_height"));
		this.setVerticalSpacing(rs.getDouble("vertical_spacing"));
		this.setHorizontalSpacing(rs.getDouble("horizontal_spacing"));
	}
	
	public String toString()
	{
		return	this.getLabelName();
	}
	
}
