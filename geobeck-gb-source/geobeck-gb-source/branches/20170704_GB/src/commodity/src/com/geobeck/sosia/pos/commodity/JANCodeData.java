/*
 * JANCodeData.java
 *
 * Created on 2007/04/27, 21:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.commodity;

import java.awt.*;
import java.awt.image.*;
import com.geobeck.sosia.pos.master.product.*;

/**
 *
 * @author katagiri
 */
public class JANCodeData extends MstItem
{
	private	BufferedImage	barcode		=	null;
	
	/** Creates a new instance of JANCodeData */
	public JANCodeData()
	{
	}

	public BufferedImage getBarcode()
	{
		return barcode;
	}

	public void setBarcode(BufferedImage barcode)
	{
		this.barcode = barcode;
	}
	
}
