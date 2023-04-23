
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
	// インスタンス変数
	// -----------------------------------------------------------------------------------------
	/**
	 * バーファーリーダー
	 */
	protected BufferedReader reader ;
	/**
	 * CSVデータパーサー
	 */
	protected CsvDataParser dataParser ;

	// -----------------------------------------------------------------------------------------
	// コンストラクタ
	// -----------------------------------------------------------------------------------------
	/**
	 * コンストラクタ
	 * @param reader 入力ストリーム
	 * @param dataParser データパーサー
	 */
	public CsvRecordReader( Reader reader , CsvDataParser dataParser ) {
		setReader( reader ) ;
		setDataParser( dataParser ) ;
	}
	
	// -----------------------------------------------------------------------------------------
	// 公開メソッド
	// -----------------------------------------------------------------------------------------
	/**
	 * データパーサーを返します
	 */
	public CsvDataParser getDataParser() {
		return this.dataParser ; 
	}
	/**
	 * 入力ストリームを返します
	 */
	public Reader getReader() {
		return this.reader ; 
	}
	/**
	 * 読み込み処理を行ないます
	 * 事前条件
	 * 		データパーサーがセットされていること
	 * 		入力ストリームがセットされていること
	 * 事後条件
	 * 		オブジェクトが返ります
	 * @return 読み込み結果のオブジェクト
	 */
	public Object readLine() throws IOException {
		// 事前条件チェック
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
		return parseFieldValues( line )  ; // CSVレコード --> オブジェクト変換 
	}
	/**
	 * データパーサーをセットします
	 * @param dataParser CSVデータパーサー
	 */
	public void setDataParser( CsvDataParser dataParser ) {
		this.dataParser = dataParser ; 
	}
	/**
	 * 入力ストリームをセットします
	 * @param reader 入力ストリーム
	 */
	public void setReader( Reader reader ) {
		this.reader = new BufferedReader( reader ) ;
	}
	
	// -----------------------------------------------------------------------------------------
	// プライベートメソッド
	// -----------------------------------------------------------------------------------------
	/**
	 * 行をパースします
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
