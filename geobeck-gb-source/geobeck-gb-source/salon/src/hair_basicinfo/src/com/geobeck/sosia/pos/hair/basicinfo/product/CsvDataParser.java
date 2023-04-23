package com.geobeck.sosia.pos.hair.basicinfo.product;

/**
 * CSVレコード ==> オブジェクト変換を行ないます
 */
public interface CsvDataParser {

	// -----------------------------------------------------------------------------------------
	// 公開メソッド
	// -----------------------------------------------------------------------------------------
	/**
	 * レコードの文字列からオブジェクトを生成して返します。
	 * 例えば、郵便番号レコードから郵便番号オブジェクトが返ります。
	 * @param recordStrings レコード文字列の配列
	 * @return オブジェクト
	 */
	public Object parseFieldValues( String[] recordStrings ) ; 

}
