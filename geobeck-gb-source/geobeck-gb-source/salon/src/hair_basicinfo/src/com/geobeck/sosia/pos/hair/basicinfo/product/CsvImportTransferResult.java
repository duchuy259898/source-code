package com.geobeck.sosia.pos.hair.basicinfo.product;

import com.geobeck.sosia.pos.hair.data.member.DataTransferResult;
import com.geobeck.sosia.pos.hair.data.member.DataTransferResultDetail;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.CheckUtil;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.lang.StringUtils;

/**
 * CsvImportTransferResult
 */
public class CsvImportTransferResult {

        /**ヘッダーレコード*/
        public final int                        HEADER_RECORD          =        1;
        /**ヘッダーレコード*/
        public final int                        DATA_RECORD            =        2;
        /**ヘッダーレコード*/
        public final int                        TRAILER_RECORD         =        8;
        /**ヘッダーレコード*/
        public final int                        END_RECORD             =        9;
        
        List<CsvData>                          csvDataList            =        new ArrayList<CsvData>();
        
        String                                  csvPath                =        StringUtils.EMPTY;
        
        AccountTransferPanel                    transferPanel          =        null ;

    public CsvImportTransferResult(AccountTransferPanel transferPanel, String path) {
        if (!StringUtils.EMPTY.equals(path)) {
            csvPath = path;
        }
        this.transferPanel = transferPanel;
    }
        
        
        
	/**
	 * 入力ストリームを返します
	 */
	private Reader getReader() {
		try {
			FileReader reader = new FileReader( csvPath ) ;
			return reader ;
		} catch ( IOException e ) {
			throw new RuntimeException( e.getMessage() ) ;
		}
	}
	/**
	 * リーダーを実行する
	 */
	public boolean excuteReaderAndCheckImport() {
		// リーダーを実行する
		Reader in = getReader() ;
		CsvDataParser dataParser =  new TransferResultDataParser() ;

		CsvRecordReader reader = new CsvRecordReader( in , dataParser ) ;
		Object recordObject ;
                csvDataList.clear();
                boolean isFinish = true;
                try {
                    if (checkFormatImport()) {
                        try {
                            while ( ( recordObject = reader.readLine() ) != null ) {
                                if (!checkImport((CsvData) recordObject)) {
                                    return false;
                                }
                                String str = recordObject.toString() ;
                                System.out.println( str ) ;
                                csvDataList.add((CsvData) recordObject);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(CsvImportTransferResult.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        isFinish = false;
                    }
                    return isFinish;
                } catch (Exception e) {
                    isFinish = false;
                } finally {
                    if (! isFinish ) {
                        MessageDialog.showMessageDialog(this.transferPanel,
                                "ファイルフォーマットが正しくないです。",
                                this.transferPanel.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                return isFinish;
	}

        /**
         * 請求・結果情報ヘッダからデータをセットする。
         *
         * @param trasfer 請求・結果情報
         */
        public void setDataTransferResult(DataTransferResult trasfer) {

            //2:結果データ取込済
            trasfer.setStatus(2);
            for(DataTransferResultDetail detail : trasfer)
            {
                for (CsvData data : csvDataList) {
                    if (  data.value1 == null ? String.valueOf(this.DATA_RECORD) == null : data.value1.equals(String.valueOf(this.DATA_RECORD)) ) {
                        String accountCustomerNO = data.value12.substring(6, 20);
                        if ( detail.getAccountCustomerNo().equals(accountCustomerNO) ) {
                            Integer resultCode = Integer.parseInt(data.value13);
                            detail.setResultCode(resultCode);
                        }
                    }
                }
            }
        }
        
        /**
	 * フォーマットのインポートを確認する
        * @return 
	 */
	public boolean checkFormatImport() {
		// リーダーを実行する
		Reader in = getReader() ;
		CsvDataParser dataParser =  new TransferResultDataParser() ;

		CsvRecordReader reader = new CsvRecordReader( in , dataParser ) ;
		Object recordObject ;
                ArrayList<Integer> typeList = new ArrayList<Integer>();
		try {
			while ( ( recordObject = reader.readLine() ) != null ) {
                            
                            CsvData data = (CsvData) recordObject;
                            int typeData = Integer.parseInt(data.value1);
                            typeList.add(typeData);
                        }
                        if (!(typeList.size() >= 4 && typeList.contains(HEADER_RECORD) 
                                && typeList.contains(DATA_RECORD) 
                                && typeList.contains(TRAILER_RECORD) 
                                && typeList.contains(END_RECORD))) {
                            
                            return false;
                        }
                } catch ( IOException e ) {
			return false;
                } catch ( NumberFormatException ex ) {
			return false;
		}
                return true;
	}
        /**
         * フォーマットのインポートを確認する
         * @param recordObject
         * @return 
         */
        public boolean checkImport(CsvData recordObject) {
            
            String keyName = StringUtils.EMPTY;
            try {
                
                int typeData = Integer.parseInt(recordObject.value1);
                switch(typeData) {
                    case HEADER_RECORD:
                       keyName = checkInportHeader(recordObject);
                       break;
                     case DATA_RECORD:
                       keyName = checkInportData(recordObject);
                       break;
                     case TRAILER_RECORD:
                       keyName = checkInportTrailer(recordObject);
                       break;
                     case END_RECORD:
                       keyName = checkInportEnd(recordObject);
                       break;
                     default:
                        keyName = "データ区分";
                     break;
                }
            }catch( Exception e) {
                MessageDialog.showMessageDialog(this.transferPanel,
                        "ファイルフォーマットが正しくないです。",
                        this.transferPanel.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if(!StringUtils.isEmpty(keyName))
            {
                MessageDialog.showMessageDialog(this.transferPanel,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, keyName),
                        this.transferPanel.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return	false;
            }
            
            return true;
        }
        
        /**
         * 形式のインポートヘッダーを確認する
         * @param recordObject
         * @return 
         */
        private String checkInportHeader(CsvData recordObject) {
            
            //データ区分
            if (!checkValueFixed(recordObject.value1, "1")) {
                return "データ区分";
            }
            //種別コード
            if (!checkValueFixed(recordObject.value2, "91")) {
                return "種別コード";
            }
            //コード区分
            if (!checkValueFixed(recordObject.value3, "1")) {
                return "コード区分";
            }
            //委託者番号(６桁)
            if (!checkStringLength(recordObject.value4, 6)) {
                return "委託者コード";
            } else if (!CheckUtil.isNumber(recordObject.value4)) {
                return "委託者コード";
            }
            //区分２桁
            if (!checkStringLength(recordObject.value5, 2)) {
                return "区分";
            } else if (!CheckUtil.isNumber(recordObject.value5)) {
                return "区分";
            }
            //２桁
            if (!checkValueFixed(recordObject.value6, "00")) {
                return "委託者コード";
            }
            //委託者名
            if (! CheckUtil.checkStringLength(recordObject.value7, 40)) {
                return "委託者名";
            }
            //振替日
            if (!checkStringLength(recordObject.value8, 4)) {
                return "振替日";
            } else if (CheckUtil.isNumber(recordObject.value8)) {
                int month = Integer.parseInt(recordObject.value8.substring(0, 2));
                String day = recordObject.value8.substring(2, 4);
                if (!CheckUtil.checkRange(month, 1, 12)) {
                    return "振替日";
                } else if (!(checkValueFixed(day, "14") || checkValueFixed(day, "27"))) {
                    return "振替日";
                }
            } else {
                return "振替日";
            }
            
            return StringUtils.EMPTY;
        }
        /**
         * フォーマットインポートデータを確認する
         * @param recordObject
         * @return 
         */
        private String checkInportData(CsvData recordObject) {
            //データ区分
            if (!checkValueFixed(recordObject.value1, "2")) {
                return "データ区分";
            }
            
            //振替銀行番号
            if (!checkStringLength(recordObject.value2, 4)) {
                return "振替銀行番号";
            } else if (!CheckUtil.isNumber(recordObject.value2)) {
                return "振替銀行番号";
            }
            //振替銀行支店番号
            if (!checkStringLength(recordObject.value4, 3)) {
                return "振替銀行支店番号";
            } else if (!CheckUtil.isNumber(recordObject.value4)) {
                return "振替銀行支店番号";
            }
            //預金種別
            if (!(checkValueFixed(recordObject.value7, "1") || checkValueFixed(recordObject.value7, "2"))) {
                return "預金種別";
            }
            //口座番号
            if (!checkStringLength(recordObject.value8, 7)) {
                return "口座番号";
            } else if (!CheckUtil.isNumber(recordObject.value8)) {
                return "口座番号";
            }
            //預金者名
            if (! CheckUtil.checkStringLength(recordObject.value9, 30)) {
                return "預金者名";
            }
            //振替金額
            if (!CheckUtil.checkStringLength(recordObject.value10, 10)) {
                return "振替金額";
            } else if (!CheckUtil.isNumber(recordObject.value10)) {
                return "振替金額";
            }
            //新規コード
            if (!(checkValueFixed(recordObject.value11, "1") || checkValueFixed(recordObject.value11, "0"))) {
                return "新規コード";
            }
            //顧客番号
            if (!checkStringLength(recordObject.value12, 20)) {
                return "顧客番号";
            } else if (!CheckUtil.isNumber(recordObject.value12)) { //TODO: check 委託者番号の上５桁
                return "顧客番号";
            }
            // 振替結果コード
            if (!CheckUtil.isNumber(recordObject.value13)) {
                return "振替結果コード";
            } else if (StringUtils.indexOfAny(recordObject.value13, 
                    new String[]{String.valueOf(DataTransferResultDetail.TRANSFERRED), String.valueOf(DataTransferResultDetail.LACK_OF_FUNDS), 
                        String.valueOf(DataTransferResultDetail.NO_DEPOSIT_TRANSACTIONS), String.valueOf(DataTransferResultDetail.STOP_TRANSFER_REASON), 
                        String.valueOf(DataTransferResultDetail.NO_TRANSFER_REQUEST), String.valueOf(DataTransferResultDetail.TRANSFER_STOP), 
                        String.valueOf(DataTransferResultDetail.OTHER)}) == -1) {
                return "振替結果コード";
            }

            return StringUtils.EMPTY;
        }
                
        private String checkInportTrailer(CsvData recordObject) {
            //データ区分
            if (!checkValueFixed(recordObject.value1, "8")) {
                return "データ区分";
            }
            //請求合計件数
            if (!CheckUtil.checkStringLength(recordObject.value2, 6)) {
                return "請求合計件数";
            } else if (!CheckUtil.isNumber(recordObject.value2)) {
                return "請求合計件数";
            }
            //請求合計金額
            if (!CheckUtil.checkStringLength(recordObject.value3, 12)) {
                return "請求合計金額";
            } else if (!CheckUtil.isNumber(recordObject.value3)) {
                return "請求合計金額";
            }
            //振替済合計件数
            if (!CheckUtil.checkStringLength(recordObject.value4, 6)) {
                return "振替済合計件数";
            } else if (!CheckUtil.isNumber(recordObject.value4)) {
                return "振替済合計件数";
            }
            //請求合計金額
            if (!CheckUtil.checkStringLength(recordObject.value5, 12)) {
                return "振替済合計金額";
            } else if (!CheckUtil.isNumber(recordObject.value5)) {
                return "振替済合計金額";
            }
            //振替不能合計件数
            if (!CheckUtil.checkStringLength(recordObject.value6, 6)) {
                return "振替不能合計件数";
            } else if (!CheckUtil.isNumber(recordObject.value6)) {
                return "振替不能合計件数";
            }
            //振替不能合計金額
            if (!CheckUtil.checkStringLength(recordObject.value7, 12)) {
                return "振替不能合計金額";
            } else if (!CheckUtil.isNumber(recordObject.value7)) {
                return "振替不能合計金額";
            }
            
            return StringUtils.EMPTY;
        }
        
         private String checkInportEnd(CsvData recordObject) {
            
            //データ区分
            if (!checkValueFixed(recordObject.value1, "9")) {
                return "データ区分";
            }
            return StringUtils.EMPTY;
        }
         
        private boolean checkValueFixed(String value, String valueFixed) {
            
            return value.equals(valueFixed);
        }
        
         /**
	 * 文字列の長さをチェックする。
	 * @param value チェックする文字列
	 * @param length 最長の文字数
	 * @return valueの長さがlength以下ならtrue、lengthより長ければfalse
	 */	
	private boolean checkStringLength(String value, int length)
	{
		return	length == value.length();
	}


	private class TransferResultDataParser implements CsvDataParser {

                @Override
		public Object parseFieldValues(String[] recordStrings) {
			CsvData obj = new CsvData() ;
                        obj.value1 = recordStrings[0] ;
                        if (obj.value1 != null && !"".equals(obj.value1) && CheckUtil.isNumber(obj.value1)) {
                            
                            int typeData = Integer.parseInt(obj.value1);
                            
                             switch(typeData) {
                                 case HEADER_RECORD:
                                    obj.value1 = recordStrings[0] ;
                                    obj.value2 = recordStrings[1] ;
                                    obj.value3 = recordStrings[2] ;
                                    obj.value4 = recordStrings[3] ;
                                    obj.value5 = recordStrings[4] ;
                                    obj.value6 = recordStrings[5] ;
                                    obj.value7 = recordStrings[6] ;
                                    obj.value8 = recordStrings[7] ;
                                    break;
                                  case DATA_RECORD:
                                    obj.value1 = recordStrings[0] ;
                                    obj.value2 = recordStrings[1] ;
                                    obj.value3 = recordStrings[2] ;
                                    obj.value4 = recordStrings[3] ;
                                    obj.value5 = recordStrings[4] ;
                                    obj.value6 = recordStrings[5] ;
                                    obj.value7 = recordStrings[6] ;
                                    obj.value8 = recordStrings[7] ;
                                    obj.value9 = recordStrings[8] ;
                                    obj.value10 = recordStrings[9] ;
                                    obj.value11 = recordStrings[10] ;
                                    obj.value12 = recordStrings[11] ;
                                    obj.value13 = recordStrings[12] ;
                                    break;
                                  case TRAILER_RECORD:
                                    obj.value1 = recordStrings[0] ;
                                    obj.value2 = recordStrings[1] ;
                                    obj.value3 = recordStrings[2] ;
                                    obj.value4 = recordStrings[3] ;
                                    obj.value5 = recordStrings[4] ;
                                    obj.value6 = recordStrings[5] ;
                                    obj.value7 = recordStrings[6] ;
                                    break;
                                  case END_RECORD:
                                    obj.value1 = recordStrings[0] ;
                                    break;
                                  default:
                                  break;
                             }
                        }
			return obj ;
		}

	}

	// -----------------------------------------------------------------------------------------
	// CsvData クラス
	// -----------------------------------------------------------------------------------------
	public static class CsvData {
		public String value1 ;
		public String value2 ;
		public String value3 ;
                public String value4 ;
		public String value5 ;
		public String value6 ;
                public String value7 ;
		public String value8 ;
		public String value9 ;
                public String value10 ;
		public String value11 ;
		public String value12 ;
                public String value13 ;
		public String value14 ;
		public String value15 ;

		public String toString() {
			StringBuffer str = new StringBuffer("{");
			str.append("value1=") ;
			str.append(value1) ;
			str.append(" ") ;
			str.append("value2=") ;
			str.append(value2) ;
			str.append(" ") ;
			str.append("value3=") ;
			str.append(value3) ;
                        str.append(" ") ;
			str.append("value4=") ;
			str.append(value4) ;
			str.append(" ") ;
			str.append("value5=") ;
			str.append(value5) ;
                        str.append(" ") ;
			str.append("value6=") ;
			str.append(value6) ;
			str.append(" ") ;
			str.append("value7=") ;
			str.append(value7) ;
                        str.append(" ") ;
			str.append("value8=") ;
			str.append(value8) ;
			str.append(" ") ;
			str.append("value9=") ;
			str.append(value9) ;
                        str.append(" ") ;
			str.append("value10=") ;
			str.append(value10) ;
			str.append(" ") ;
			str.append("value11=") ;
			str.append(value11) ;
                        str.append(" ") ;
			str.append("value12=") ;
			str.append(value12) ;
			str.append(" ") ;
			str.append("value13=") ;
			str.append(value13) ;
                        str.append(" ") ;
			str.append("value14=") ;
			str.append(value14) ;
			str.append(" ") ;
			str.append("value15=") ;
			str.append(value15) ;
			str.append('}');
			String result = str.toString() ;
			return result ;

		}
	}
}
