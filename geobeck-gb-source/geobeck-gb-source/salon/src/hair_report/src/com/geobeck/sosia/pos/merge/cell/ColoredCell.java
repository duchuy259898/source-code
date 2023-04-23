/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.merge.cell;
import java.awt.*;

/**
 *
 * @author Tran Thi Mai Loan
 */
public interface ColoredCell {
  
  public Color getForeground(int row, int column);
  public void setForeground(Color color, int row, int column);
  public void setForeground(Color color, int[] rows, int[] columns);

  public Color getBackground(int row, int column);
  public void setBackground(Color color, int row, int column);
  public void setBackground(Color color, int[] rows, int[] columns);


}