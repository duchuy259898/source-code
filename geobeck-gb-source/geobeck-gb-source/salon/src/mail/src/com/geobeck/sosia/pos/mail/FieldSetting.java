/*
 * FieldSetting.java
 *
 * Created on 2006/09/07, 11:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.mail;

/**
 *
 * @author katagiri
 */
public class FieldSetting
{
	private	float				top			=	0;
	private	float				left		=	0;
	private	float				size		=	0;
	
	/** Creates a new instance of FieldSetting */
	public FieldSetting()
	{
	}

	public float getTop()
	{
		return top;
	}
	
	public float getOptimizedTop()
	{
		return convertMillimeterToDot(148 - top) - size;
	}

	public void setTop(float top)
	{
		this.top = top;
	}

	public float getLeft()
	{
		return left;
	}

	public float getOptimizedLeft()
	{
		return convertMillimeterToDot(left);
	}

	public void setLeft(float left)
	{
		this.left = left;
	}

	public float getSize()
	{
		return size;
	}

	public void setSize(float size)
	{
		this.size = size;
	}
	
	public static float convertMillimeterToDot(float value)
	{
		return	value / 25.4f * 72f;
	}
}
