/*
 * GraphLine.java
 *
 * Created on 2007/08/30, 17:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.graph;

import java.awt.*;
import java.util.*;

/**
 *
 * @author katagiri
 */
public class GraphLine
{
	public static final Color	DEFAULT_COLOR		=	Color.black;
	
	public static final int		SHAPE_NONE			=	0;
	public static final int		SHAPE_CIRCLE		=	1;
	public static final int		SHAPE_SQUARE		=	2;
	public static final int		SHAPE_TRIANGLE		=	3;
	public static final int		SHAPE_STAR			=	4;
	
	private String		name			=	"";
	private	Long		value			=	null;
	private	Color		color			=	DEFAULT_COLOR;
	private	Stroke		stroke			=	null;
	
	/** Creates a new instance of GraphLine */
	public GraphLine()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getValue()
	{
		return value;
	}

	public void setValue(Long value)
	{
		this.value = value;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public Stroke getStroke()
	{
		return stroke;
	}

	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}
	
	public boolean isSetStroke()
	{
		return	this.getStroke() != null;
	}
}
