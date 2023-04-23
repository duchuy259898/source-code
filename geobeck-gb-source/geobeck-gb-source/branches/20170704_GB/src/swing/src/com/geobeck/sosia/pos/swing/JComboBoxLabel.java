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
 * Itemが１つの場合ラベルになるコンボボックス
 * @author katagiri
 */
public class JComboBoxLabel extends JComboBox
{
	
	/**
	 * ラベル
	 */
	private	JLabel		label	=	null;
	
	/**
	 * デフォルトのデータモデルで JComboBox を生成します。
	 * デフォルトのデータモデルは、オブジェクトの空リストです。
	 * addItem を使用して項目を追加します。デフォルトでは、データモデルの最初の項目が選択されます。
	 */
	public JComboBoxLabel()
	{
		super();
		this.initLabel();
	}
	
	/**
	 * 項目を既存の ComboBoxModel から取得する JComboBox を生成します。
	 * ComboBoxModel が提供されるため、このコンストラクタを使用して生成されたコンボボックスは、
	 * デフォルトのコンボボックスモデルを生成せず、挿入、削除、および追加の各メソッドの動作方法に強い影響を与えます。
	 * @param aModel 表示された項目のリストを提供する ComboBoxModel
	 */
	public JComboBoxLabel(ComboBoxModel aModel)
	{
		super(aModel);
		this.initLabel();
	}
	
	/**
	 * 指定された配列に要素を格納する JComboBox を生成します。
	 * デフォルトでは、配列の最初の項目と、それに伴うデータモデルが選択されます。
	 * @param items コンボボックスに挿入するオブジェクトの配列
	 */
	public JComboBoxLabel(Object[] items)
	{
		super(items);
		this.initLabel();
	}
	
	/**
	 * 指定された Vector に要素を格納する JComboBox を生成します。
	 * デフォルトでは、ベクタの最初の項目と、それに伴うデータモデルが選択されます。
	 * @param items コンボボックスに挿入するベクタの配列
	 */
	public JComboBoxLabel(Vector<?> items)
	{
		super(items);
		this.setLayout(null);
		this.initLabel();
	}
	
	/**
	 * ラベルを初期化
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
	 * ラベルを取得する。
	 * @return ラベル
	 */
	public JLabel getLabel()
	{
		return label;
	}

	/**
	 * ラベルを設定する。
	 * @param label ラベル
	 */
	public void setLabel(JLabel label)
	{
		this.label = label;
	}
	
	/**
	 * このコンポーネントを移動し、サイズ変更します。
	 * 左上隅の新しい位置は x および y によって指定され、新しいサイズは width および height によって指定されます。
	 * @param x このコンポーネントの新しい x 座標
	 * @param y このコンポーネントの新しい y 座標
	 * @param width このコンポーネントの新しい width
	 * @param height このコンポーネントの新しい height
	 */
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		label.setBounds(0, 0, width, height);
	}

	/**
	 * 新しい境界の矩形 r に適合するようにこのコンポーネントを移動し、サイズ変更します。
	 * このコンポーネントの新しい位置は r.x および r.y によって指定され、新しいサイズは r.width および r.height によって指定されます。
	 * @param r このコンポーネントの新しい境界の矩形
	 */
	public void setBounds(Rectangle r)
	{
		super.setBounds(r);
		label.setBounds(0, 0, ((Double)r.getWidth()).intValue(), ((Double)r.getHeight()).intValue());
	}
	
	/**
	 * このコンポーネントのサイズを、幅 d.width、高さ d.height に変更します。
	 * @param d このコンポーネントの新しいサイズを指定する寸法
	 */
	public void setSize(Dimension d)
	{
		super.setSize(d);
		label.setSize(d);
	}

	/**
	 * このコンポーネントのサイズを width および height に変更します。
	 * @param width このコンポーネントの新しい幅 (ピクセル単位)
	 * @param height このコンポーネントの新しい高さ (ピクセル単位)
	 */
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		label.setSize(width, height);
	}
	
	/**
	 * 項目を項目リストに追加します。このメソッドは JComboBox が可変データモデルを使用する場合にだけ有効です。
	 * @param anObject リストに追加する Object
	 */
	public void addItem(Object anObject)
	{
		super.addItem(anObject);
		this.setLabelStatus();
	}
	
	/**
	 * 項目リストからすべての項目を削除します。
	 */
	public void removeAllItems()
	{
		super.removeAllItems();
		this.setLabelStatus();
	}
	/**
	 * 項目を項目リストから削除します。このメソッドは JComboBox が可変データモデルを使用する場合にだけ有効です。
	 * @param anObject 項目リストから削除するオブジェクト
	 */
	public void removeItem(Object anObject)
	{
		super.removeItem(anObject);
		this.setLabelStatus();
	}
	/**
	 * anIndex 位置の項目を削除します。このメソッドは JComboBox が可変データモデルを使用する場合にだけ有効です。
	 * @param anIndex 削除する項目のインデックスを指定する整数値。0 はリスト内の最初の項目を示す
	 */
	public void removeItemAt(int anIndex)
	{
		super.removeItemAt(anIndex);
		this.setLabelStatus();
	}
	
	/**
	 * ラベルの状態をセットする。
	 */
	private void setLabelStatus()
	{
		//Itemが１つの場合
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
		//Itemが１つ以外の場合
		else
		{
			this.setEnabled(true);
			label.setVisible(false);
		}
	}
	
	/**
	 * このメソッドは Swing によって呼び出され、コンポーネントを描画します。
	 * @param g ペイント対象の Graphics コンテキスト
	 */
	public void paint(Graphics g)
	{
		//ラベルを表示する場合
		if(label.isVisible())
		{
			label.print(g);
		}
		//ラベルを表示しない場合
		else
		{
			super.paint(g);
		}
	}
}
