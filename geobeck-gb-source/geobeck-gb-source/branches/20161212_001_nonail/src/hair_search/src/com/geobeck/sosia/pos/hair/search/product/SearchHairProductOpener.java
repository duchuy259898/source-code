/*
 * SearchHairProductOpener.java
 *
 * Created on 2006/10/06, 17:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.search.product;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.products.*;

/**
 *
 * @author katagiri
 */
public interface SearchHairProductOpener
{
	public void addSelectedProduct(Integer productDivision, Product selectedProduct);

	public void addSelectedCourse(Integer productDivision, Course selectedCourse);

	public void addSelectedConsumptionCourse(Integer productDivision, ConsumptionCourse selectedConsumptionCourse);
}
