/*
 * SelectProductOpener.java
 *
 * Created on 2015/11/13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.customer;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.products.*;

/**
 *
 * @author lvtu
 */
public interface SelectProductOpener
{
	public void addSelectedProduct(Integer productDivision, Product selectedProduct);

	public void addSelectedCourse(Integer productDivision, Course selectedCourse);
}
