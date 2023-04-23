/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.product;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static javax.swing.SwingConstants.TOP;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author lvtu
 */
public class CustomTabbedPaneUI  extends BasicTabbedPaneUI {

    private ColorSet selectedColorSet;
	private ColorSet defaultColorSet;
        //private Rectangle shape;
        private Color selectColor = new Color(255, 210, 142);
        private int xp[] = null; 
        private int yp[] = null;

        
	public static ComponentUI createUI(JComponent c) {
		return new CustomTabbedPaneUI();
	}

	public CustomTabbedPaneUI() {

		defaultColorSet = new ColorSet();
		defaultColorSet.topGradColor1 = new Color(245, 245, 245);
		defaultColorSet.topGradColor2 = new Color(202, 202, 202);

		defaultColorSet.bottomGradColor1 = new Color(202, 202, 202);
		defaultColorSet.bottomGradColor2 = new Color(245, 245, 245);

                //this.shape = rec;

	}


	protected void installDefaults() {
		super.installDefaults();
	}

	protected boolean scrollableTabLayoutEnabled() {
		return false;
	}

	protected void paintTabBackground(Graphics g, int tabPlacement,
			int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		Graphics2D g2d = (Graphics2D) g;
		ColorSet colorSet;

		Rectangle rect = rects[tabIndex];

		if (isSelected) {
			colorSet = selectedColorSet;
		} else {
			colorSet = defaultColorSet;
		}

		int width = rect.width;
		int xpos = rect.x;
		if (tabIndex > 0) {
			width--;
			xpos++;
		}
                if (isSelected) {
                    g.setColor(selectColor);
                    g.fill3DRect(x, y-2, w, h+2, isSelected);
                } else {
                    g2d.setPaint(new GradientPaint(xpos, 0, colorSet.bottomGradColor1, xpos,
                                    10, colorSet.bottomGradColor2));
                    g2d.fillRect(xpos, 0+2, width, 10);

                    g2d.setPaint(new GradientPaint(0, 10, colorSet.bottomGradColor1, 0, 21,
                                    colorSet.bottomGradColor2));
                    g2d.fillRect(xpos, 10, width, 11);
                }
	}

	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
            
            Graphics2D g2D = (Graphics2D)g;    
                switch( tabPlacement )
               {
                   case LEFT: 
                   case RIGHT:        
                   case BOTTOM:
                       /* codigo para estos tabs */
                       break;

                   case TOP:
                   default:           
                       xp = new int[]{ x, 
                                       x,
                                       x,
                                       x+w,
                                       x+w,
                                       x+w,
                                       x+w,
                                       x
                       };
                       y = y-1;
                       yp = new int[]{ y+h,
                                       y,
                                       y,
                                       y,
                                       y,
                                       y,
                                       y+h+2,
                                       y+h+2
                       };
                     break;
               }

               if ( isSelected )
               {
                    g2D.setColor( new Color(137,140,149) );
                    g2D.drawPolyline( xp , yp , xp.length - 1 );
               }
               else
               {
                   g2D.setColor( new Color(137,140,149) );
                   g2D.drawPolyline( xp , yp , xp.length );
               }     

             } 

	protected void paintFocusIndicator(Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect, boolean isSelected) {
		// Do nothing
	}

	protected int getTabLabelShiftY(int tabPlacement, int tabIndex,
			boolean isSelected) {
		return 0;
	}

	private class ColorSet {
		Color topGradColor1;
		Color topGradColor2;

		Color bottomGradColor1;
		Color bottomGradColor2;
	}  

}