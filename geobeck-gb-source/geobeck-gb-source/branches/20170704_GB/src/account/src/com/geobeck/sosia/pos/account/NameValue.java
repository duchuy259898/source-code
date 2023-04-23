/*
 * NameValue.java
 *
 * Created on 2006/05/15, 16:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

/**
 * 名称と金額を保持するクラス
 * @author katagiri
 */
public class NameValue<T>
{
	/**
	 * 名称
	 */
	protected String	name	=	"";
	/**
	 * 金額
	 */
	protected Long		value	=	0l;

        /** 拡張値
         */
        protected T enchancedValue = null;
	
    /**
     * コンストラクタ
     */
    public NameValue(){
		
    }
	
    /**
     * 名称、金額をセットするコンストラクタ
     * @param name 名称
     * @param value 金額
     */
    public NameValue(String name, Long value){
        this.setName(name);
        this.setValue(value);
    }
	
    /**
     * 名称を取得する。
     * @return 名称
     */
    public String getName(){
        return name;
    }
	
    /**
     * 名称をセットする。
     * @param name 名称
     */
    public void setName(String name){
        this.name = name;
    }
	
    /**
     * 金額を取得する。
     * @return 金額
     */
    public Long getValue(){
        return value;
    }
	
    /**
     * 金額をセットする。
     * @param value 金額
     */
    public void setValue(Long value){
        this.value = value;
    }


    public T getEnchancedValue() {
        return enchancedValue;
    }

    public void setEnchancedValue(T enchancedValue) {
        this.enchancedValue = enchancedValue;
    }

}
