package com.geobeck.sosia.pos.hair.basicinfo.product;

/**
 * CSV���R�[�h ==> �I�u�W�F�N�g�ϊ����s�Ȃ��܂�
 */
public interface CsvDataParser {

	// -----------------------------------------------------------------------------------------
	// ���J���\�b�h
	// -----------------------------------------------------------------------------------------
	/**
	 * ���R�[�h�̕����񂩂�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * �Ⴆ�΁A�X�֔ԍ����R�[�h����X�֔ԍ��I�u�W�F�N�g���Ԃ�܂��B
	 * @param recordStrings ���R�[�h������̔z��
	 * @return �I�u�W�F�N�g
	 */
	public Object parseFieldValues( String[] recordStrings ) ; 

}
