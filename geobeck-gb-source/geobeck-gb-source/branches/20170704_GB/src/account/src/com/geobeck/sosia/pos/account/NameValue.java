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
 * ���̂Ƌ��z��ێ�����N���X
 * @author katagiri
 */
public class NameValue<T>
{
	/**
	 * ����
	 */
	protected String	name	=	"";
	/**
	 * ���z
	 */
	protected Long		value	=	0l;

        /** �g���l
         */
        protected T enchancedValue = null;
	
    /**
     * �R���X�g���N�^
     */
    public NameValue(){
		
    }
	
    /**
     * ���́A���z���Z�b�g����R���X�g���N�^
     * @param name ����
     * @param value ���z
     */
    public NameValue(String name, Long value){
        this.setName(name);
        this.setValue(value);
    }
	
    /**
     * ���̂��擾����B
     * @return ����
     */
    public String getName(){
        return name;
    }
	
    /**
     * ���̂��Z�b�g����B
     * @param name ����
     */
    public void setName(String name){
        this.name = name;
    }
	
    /**
     * ���z���擾����B
     * @return ���z
     */
    public Long getValue(){
        return value;
    }
	
    /**
     * ���z���Z�b�g����B
     * @param value ���z
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
