/*
 * JComboBoxLabel.java
 *
 * Created on 2007/01/17, 13:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Item���P�̏ꍇ���x���ɂȂ�R���{�{�b�N�X
 * @author katagiri
 */
public class JComboBoxLabel extends JComboBox
{
	
	/**
	 * ���x��
	 */
	private	JLabel		label	=	null;
	
	/**
	 * �f�t�H���g�̃f�[�^���f���� JComboBox �𐶐����܂��B
	 * �f�t�H���g�̃f�[�^���f���́A�I�u�W�F�N�g�̋󃊃X�g�ł��B
	 * addItem ���g�p���č��ڂ�ǉ����܂��B�f�t�H���g�ł́A�f�[�^���f���̍ŏ��̍��ڂ��I������܂��B
	 */
	public JComboBoxLabel()
	{
		super();
		this.initLabel();
	}
	
	/**
	 * ���ڂ������� ComboBoxModel ����擾���� JComboBox �𐶐����܂��B
	 * ComboBoxModel ���񋟂���邽�߁A���̃R���X�g���N�^���g�p���Đ������ꂽ�R���{�{�b�N�X�́A
	 * �f�t�H���g�̃R���{�{�b�N�X���f���𐶐������A�}���A�폜�A����ђǉ��̊e���\�b�h�̓�����@�ɋ����e����^���܂��B
	 * @param aModel �\�����ꂽ���ڂ̃��X�g��񋟂��� ComboBoxModel
	 */
	public JComboBoxLabel(ComboBoxModel aModel)
	{
		super(aModel);
		this.initLabel();
	}
	
	/**
	 * �w�肳�ꂽ�z��ɗv�f���i�[���� JComboBox �𐶐����܂��B
	 * �f�t�H���g�ł́A�z��̍ŏ��̍��ڂƁA����ɔ����f�[�^���f�����I������܂��B
	 * @param items �R���{�{�b�N�X�ɑ}������I�u�W�F�N�g�̔z��
	 */
	public JComboBoxLabel(Object[] items)
	{
		super(items);
		this.initLabel();
	}
	
	/**
	 * �w�肳�ꂽ Vector �ɗv�f���i�[���� JComboBox �𐶐����܂��B
	 * �f�t�H���g�ł́A�x�N�^�̍ŏ��̍��ڂƁA����ɔ����f�[�^���f�����I������܂��B
	 * @param items �R���{�{�b�N�X�ɑ}������x�N�^�̔z��
	 */
	public JComboBoxLabel(Vector<?> items)
	{
		super(items);
		this.setLayout(null);
		this.initLabel();
	}
	
	/**
	 * ���x����������
	 */
	private void initLabel()
	{
		label	=	new JLabel();
		label.setSize(this.getWidth(), this.getHeight());
		this.add(label, 0);
		label.setBounds(0, 0, this.getWidth(), this.getHeight());
                label.setOpaque(true);
                label.setBackground(new Color(235,235,235));
		label.setVisible(false);
	}

	/**
	 * ���x�����擾����B
	 * @return ���x��
	 */
	public JLabel getLabel()
	{
		return label;
	}

	/**
	 * ���x����ݒ肷��B
	 * @param label ���x��
	 */
	public void setLabel(JLabel label)
	{
		this.label = label;
	}
	
	/**
	 * ���̃R���|�[�l���g���ړ����A�T�C�Y�ύX���܂��B
	 * ������̐V�����ʒu�� x ����� y �ɂ���Ďw�肳��A�V�����T�C�Y�� width ����� height �ɂ���Ďw�肳��܂��B
	 * @param x ���̃R���|�[�l���g�̐V���� x ���W
	 * @param y ���̃R���|�[�l���g�̐V���� y ���W
	 * @param width ���̃R���|�[�l���g�̐V���� width
	 * @param height ���̃R���|�[�l���g�̐V���� height
	 */
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		label.setBounds(0, 0, width, height);
	}

	/**
	 * �V�������E�̋�` r �ɓK������悤�ɂ��̃R���|�[�l���g���ړ����A�T�C�Y�ύX���܂��B
	 * ���̃R���|�[�l���g�̐V�����ʒu�� r.x ����� r.y �ɂ���Ďw�肳��A�V�����T�C�Y�� r.width ����� r.height �ɂ���Ďw�肳��܂��B
	 * @param r ���̃R���|�[�l���g�̐V�������E�̋�`
	 */
	public void setBounds(Rectangle r)
	{
		super.setBounds(r);
		label.setBounds(0, 0, ((Double)r.getWidth()).intValue(), ((Double)r.getHeight()).intValue());
	}
	
	/**
	 * ���̃R���|�[�l���g�̃T�C�Y���A�� d.width�A���� d.height �ɕύX���܂��B
	 * @param d ���̃R���|�[�l���g�̐V�����T�C�Y���w�肷�鐡�@
	 */
	public void setSize(Dimension d)
	{
		super.setSize(d);
		label.setSize(d);
	}

	/**
	 * ���̃R���|�[�l���g�̃T�C�Y�� width ����� height �ɕύX���܂��B
	 * @param width ���̃R���|�[�l���g�̐V������ (�s�N�Z���P��)
	 * @param height ���̃R���|�[�l���g�̐V�������� (�s�N�Z���P��)
	 */
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		label.setSize(width, height);
	}
	
	/**
	 * ���ڂ����ڃ��X�g�ɒǉ����܂��B���̃��\�b�h�� JComboBox ���σf�[�^���f�����g�p����ꍇ�ɂ����L���ł��B
	 * @param anObject ���X�g�ɒǉ����� Object
	 */
	public void addItem(Object anObject)
	{
		super.addItem(anObject);
		this.setLabelStatus();
	}
	
	/**
	 * ���ڃ��X�g���炷�ׂĂ̍��ڂ��폜���܂��B
	 */
	public void removeAllItems()
	{
		super.removeAllItems();
		this.setLabelStatus();
	}
	/**
	 * ���ڂ����ڃ��X�g����폜���܂��B���̃��\�b�h�� JComboBox ���σf�[�^���f�����g�p����ꍇ�ɂ����L���ł��B
	 * @param anObject ���ڃ��X�g����폜����I�u�W�F�N�g
	 */
	public void removeItem(Object anObject)
	{
		super.removeItem(anObject);
		this.setLabelStatus();
	}
	/**
	 * anIndex �ʒu�̍��ڂ��폜���܂��B���̃��\�b�h�� JComboBox ���σf�[�^���f�����g�p����ꍇ�ɂ����L���ł��B
	 * @param anIndex �폜���鍀�ڂ̃C���f�b�N�X���w�肷�鐮���l�B0 �̓��X�g���̍ŏ��̍��ڂ�����
	 */
	public void removeItemAt(int anIndex)
	{
		super.removeItemAt(anIndex);
		this.setLabelStatus();
	}
	
	/**
	 * ���x���̏�Ԃ��Z�b�g����B
	 */
	private void setLabelStatus()
	{
		//Item���P�̏ꍇ
		if(this.getItemCount() == 1)
		{
			this.setEnabled(false);
			if(this.getItemAt(0) == null)
			{
				label.setText("");
			}
			else
			{
				label.setText(this.getItemAt(0).toString());
			}
			label.setVisible(true);
		}
		//Item���P�ȊO�̏ꍇ
		else
		{
			this.setEnabled(true);
			label.setVisible(false);
		}
	}
	
	/**
	 * ���̃��\�b�h�� Swing �ɂ���ČĂяo����A�R���|�[�l���g��`�悵�܂��B
	 * @param g �y�C���g�Ώۂ� Graphics �R���e�L�X�g
	 */
	public void paint(Graphics g)
	{
		//���x����\������ꍇ
		if(label.isVisible())
		{
			label.print(g);
		}
		//���x����\�����Ȃ��ꍇ
		else
		{
			super.paint(g);
		}
	}
}
