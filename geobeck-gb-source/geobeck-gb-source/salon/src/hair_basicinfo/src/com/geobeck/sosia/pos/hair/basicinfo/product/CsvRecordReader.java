
package com.geobeck.sosia.pos.hair.basicinfo.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.ibm.icu.util.StringTokenizer;

/**
 * CsvRecordReader
 * 
 */
public class CsvRecordReader {

	// -----------------------------------------------------------------------------------------
	// �C���X�^���X�ϐ�
	// -----------------------------------------------------------------------------------------
	/**
	 * �o�[�t�@�[���[�_�[
	 */
	protected BufferedReader reader ;
	/**
	 * CSV�f�[�^�p�[�T�[
	 */
	protected CsvDataParser dataParser ;

	// -----------------------------------------------------------------------------------------
	// �R���X�g���N�^
	// -----------------------------------------------------------------------------------------
	/**
	 * �R���X�g���N�^
	 * @param reader ���̓X�g���[��
	 * @param dataParser �f�[�^�p�[�T�[
	 */
	public CsvRecordReader( Reader reader , CsvDataParser dataParser ) {
		setReader( reader ) ;
		setDataParser( dataParser ) ;
	}
	
	// -----------------------------------------------------------------------------------------
	// ���J���\�b�h
	// -----------------------------------------------------------------------------------------
	/**
	 * �f�[�^�p�[�T�[��Ԃ��܂�
	 */
	public CsvDataParser getDataParser() {
		return this.dataParser ; 
	}
	/**
	 * ���̓X�g���[����Ԃ��܂�
	 */
	public Reader getReader() {
		return this.reader ; 
	}
	/**
	 * �ǂݍ��ݏ������s�Ȃ��܂�
	 * ���O����
	 * 		�f�[�^�p�[�T�[���Z�b�g����Ă��邱��
	 * 		���̓X�g���[�����Z�b�g����Ă��邱��
	 * �������
	 * 		�I�u�W�F�N�g���Ԃ�܂�
	 * @return �ǂݍ��݌��ʂ̃I�u�W�F�N�g
	 */
	public Object readLine() throws IOException {
		// ���O�����`�F�b�N
		if ( dataParser == null ) {
			throw new IllegalStateException("dataParser == null") ;
		}
		if ( reader == null ) {
			throw new IllegalStateException("reader == null") ;
		}
		String line = reader.readLine() ;
		if ( line == null ) {
			return null ;
		}
		return parseFieldValues( line )  ; // CSV���R�[�h --> �I�u�W�F�N�g�ϊ� 
	}
	/**
	 * �f�[�^�p�[�T�[���Z�b�g���܂�
	 * @param dataParser CSV�f�[�^�p�[�T�[
	 */
	public void setDataParser( CsvDataParser dataParser ) {
		this.dataParser = dataParser ; 
	}
	/**
	 * ���̓X�g���[�����Z�b�g���܂�
	 * @param reader ���̓X�g���[��
	 */
	public void setReader( Reader reader ) {
		this.reader = new BufferedReader( reader ) ;
	}
	
	// -----------------------------------------------------------------------------------------
	// �v���C�x�[�g���\�b�h
	// -----------------------------------------------------------------------------------------
	/**
	 * �s���p�[�X���܂�
	 */
	private Object parseFieldValues( String line ) {
//		StringTokenizer tokenizer = new StringTokenizer( line , "," ) ;
//		int countTokens = tokenizer.countTokens() ;
//		String[] strings = new String[ countTokens ] ;
//		for ( int i = 0 ; i < countTokens ; i++ ) {
//			strings[i] = tokenizer.nextToken() ;
//		}
                String[] strings = line.split(",");
                
		return dataParser.parseFieldValues( strings ) ;
	}

}
