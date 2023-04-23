/*
 * GraphDataList.java
 *
 * Created on 2007/08/27, 10:01
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
public class GraphDataList extends ArrayList<Long>
{
	public static final Color	DEFAULT_COLOR		=	Color.black;
	
	public static final int		SHAPE_NONE			=	0;
	public static final int		SHAPE_CIRCLE		=	1;
	public static final int		SHAPE_SQUARE		=	2;
	public static final int		SHAPE_TRIANGLE		=	3;
	public static final int		SHAPE_STAR			=	4;
	
	private String		name			=	"";
	private	Color		color			=	DEFAULT_COLOR;
	private int			pointShape		=	SHAPE_CIRCLE;
	private boolean		fillShape		=	true;
	private	Stroke		stroke			=	null;
	
	/**
	 * Creates a new instance of GraphDataList
	 */
	public GraphDataList()
	{
		super();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public int getPointShape()
	{
		return pointShape;
	}

	public void setPointShape(int pointShape)
	{
		this.pointShape = pointShape;
	}

	public boolean isFillShape()
	{
		return fillShape;
	}

	public void setFillShape(boolean fillShape)
	{
		this.fillShape = fillShape;
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
	
	public Long getMinValue()
	{
		if(this.size() == 0)
		{
			return	null;
		}
		
		Long	min	=	Long.MAX_VALUE;
		
		for(Long temp : this)
		{
			if(temp < min)
			{
				min	=	temp;
			}
		}
		
		return	min;
	}
	
	public Long getMaxValue()
	{
		if(this.size() == 0)
		{
			return	null;
		}
		
		Long	max	=	Long.MIN_VALUE;
		
		for(Long temp : this)
		{
			if(max < temp)
			{
				max	=	temp;
			}
		}
		
		return	max;
	}
	
	public void drawPoint(Graphics g, int x, int y)
	{
		int[]	xPoints		=	null;
		int[]	yPoints		=	null;
		
		switch(this.getPointShape())
		{
			case SHAPE_NONE:
				break;
			case SHAPE_CIRCLE:
				if(this.isFillShape())
				{
					g.fillOval(x - 5, y - 5, 10, 10);
				}
				else
				{
					g.setColor(Color.white);
					g.fillOval(x - 5, y - 5, 10, 10);
					g.setColor(this.getColor());
					g.drawOval(x - 5, y - 5, 10, 10);
				}
				
				break;
			case SHAPE_SQUARE:
				if(this.isFillShape())
				{
					g.fillRect(x - 5, y - 5, 10, 10);
				}
				else
				{
					g.setColor(Color.white);
					g.fillRect(x - 5, y - 5, 10, 10);
					g.setColor(this.getColor());
					g.drawRect(x - 5, y - 5, 10, 10);
				}
				
				break;
			case SHAPE_TRIANGLE:
				xPoints		=	new int[3];
				yPoints		=	new int[3];
				
				xPoints[0]	=	x;
				xPoints[1]	=	x + 6;
				xPoints[2]	=	x - 6;
				yPoints[0]	=	y - 6;
				yPoints[1]	=	y + 4;
				yPoints[2]	=	y + 4;
				
				if(this.isFillShape())
				{
					g.fillPolygon(xPoints, yPoints, 3);
				}
				else
				{
					g.setColor(Color.white);
					g.drawPolygon(xPoints, yPoints, 3);
					g.setColor(this.getColor());
					g.drawPolygon(xPoints, yPoints, 3);
				}
				
				break;
			case SHAPE_STAR:
				xPoints		=	new int[5];
				yPoints		=	new int[5];
				
				xPoints[0]	=	x;
				xPoints[1]	=	x - 5;
				xPoints[2]	=	x + 6;
				xPoints[3]	=	x - 6;
				xPoints[4]	=	x + 5;
				yPoints[0]	=	y - 5;
				yPoints[1]	=	y + 6;
				yPoints[2]	=	y - 3;
				yPoints[3]	=	y - 3;
				yPoints[4]	=	y + 6;
				
				if(this.isFillShape())
				{
					g.fillPolygon(xPoints, yPoints, 5);
				}
				else
				{
					g.setColor(Color.white);
					g.drawPolygon(xPoints, yPoints, 5);
					g.setColor(this.getColor());
					g.drawPolygon(xPoints, yPoints, 5);
				}
				
				break;
		}
	}
}
