/*
 * UIUtil.java
 *
 * Created on 2008/10/09, 15:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.util.CheckUtil;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author e_mizukawa
 */
public class UIUtil
{
	/** Creates a new instance of UIUtil */
	private UIUtil()
	{
	}

	/**
	 * staffNoに入力した値をcmbboxに反映する
	 * @param staffNo スタッフNoを入力するTextField
	 * @param cmbbox スタッフNoからスタッフを選択するコンボボックス
	 */
	public static void selectStaff(JTextField txtField, JComboBox cmbbox)
	{
		String staffNoValue = txtField.getText();
		if (staffNoValue == null || "".equals(staffNoValue))
		{
			txtField.setText("");
			cmbbox.setSelectedIndex(0);
			return;
		}

		cmbbox.setSelectedIndex(0);
		for(int i = 0; i < cmbbox.getItemCount(); i ++)
		{
			MstStaff	ms	=	(MstStaff) cmbbox.getItemAt(i);
			if(staffNoValue.equals(ms.getStaffNo()))
			{
				cmbbox.setSelectedIndex(i);
				return;
			}
		}

		cmbbox.setSelectedIndex(0);
	}

	/**
	 * cmbboxで選択した値をstaffNoに反映する
	 * @param cmbbox スタッフNoからスタッフを選択するコンボボックス
	 * @param txtField スタッフNoを入力するTextField
	 */
	public static void outputStaff(JComboBox cmbBox, JTextField txtField)
	{
		MstStaff staff = (MstStaff) cmbBox.getSelectedItem();
		if (staff != null && staff.getStaffID() != null)
		{
			txtField.setText(staff.getStaffNo());
		}
		else
		{
			txtField.setText("");
		}
	}

	/**
	 * 
	 */
	public static void selectSupplier(JTextField txtField, JComboBox cmbbox)
	{
		String no = txtField.getText();
		if (no == null || "".equals(no) || !CheckUtil.isNumber(no))
		{
			txtField.setText("");
			cmbbox.setSelectedIndex(0);
			return;
		}

		Integer noValue = new Integer(no);

		for (int i = 0; i < cmbbox.getItemCount(); i++)
		{
			MstSupplier supplier = (MstSupplier) cmbbox.getItemAt(i);
			if (noValue.equals(supplier.getSupplierNo()))
			{
				cmbbox.setSelectedIndex(i);
				return;
			}
		}

		cmbbox.setSelectedIndex(0);
	}

	/**
	 *
	 */
	public static void outputSupplier(JComboBox cmbBox, JTextField txtField)
	{
		MstSupplier supplier = (MstSupplier) cmbBox.getSelectedItem();
		if (supplier != null && supplier.getSupplierNo() != null)
		{
			txtField.setText(supplier.getSupplierNo().toString());
		}
		else
		{
			txtField.setText("");
		}
	}

	/**
	 *
	 */
	public static Integer getSupplierID(JComboBox cmbBox)
	{
		MstSupplier supplier = (MstSupplier) cmbBox.getSelectedItem();
		if (supplier != null && supplier.getSupplierID() != null)
		{
			return supplier.getSupplierID();
		}

		return null;
	}
}
