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

        /**�w�b�_�[���R�[�h*/
        public final int                        HEADER_RECORD          =        1;
        /**�w�b�_�[���R�[�h*/
        public final int                        DATA_RECORD            =        2;
        /**�w�b�_�[���R�[�h*/
        public final int                        TRAILER_RECORD         =        8;
        /**�w�b�_�[���R�[�h*/
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
	 * ���̓X�g���[����Ԃ��܂�
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
	 * ���[�_�[�����s����
	 */
	public boolean excuteReaderAndCheckImport() {
		// ���[�_�[�����s����
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
                                "�t�@�C���t�H�[�}�b�g���������Ȃ��ł��B",
                                this.transferPanel.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                return isFinish;
	}

        /**
         * �����E���ʏ��w�b�_����f�[�^���Z�b�g����B
         *
         * @param trasfer �����E���ʏ��
         */
        public void setDataTransferResult(DataTransferResult trasfer) {

            //2:���ʃf�[�^�捞��
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
	 * �t�H�[�}�b�g�̃C���|�[�g���m�F����
        * @return 
	 */
	public boolean checkFormatImport() {
		// ���[�_�[�����s����
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
         * �t�H�[�}�b�g�̃C���|�[�g���m�F����
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
                        keyName = "�f�[�^�敪";
                     break;
                }
            }catch( Exception e) {
                MessageDialog.showMessageDialog(this.transferPanel,
                        "�t�@�C���t�H�[�}�b�g���������Ȃ��ł��B",
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
         * �`���̃C���|�[�g�w�b�_�[���m�F����
         * @param recordObject
         * @return 
         */
        private String checkInportHeader(CsvData recordObject) {
            
            //�f�[�^�敪
            if (!checkValueFixed(recordObject.value1, "1")) {
                return "�f�[�^�敪";
            }
            //��ʃR�[�h
            if (!checkValueFixed(recordObject.value2, "91")) {
                return "��ʃR�[�h";
            }
            //�R�[�h�敪
            if (!checkValueFixed(recordObject.value3, "1")) {
                return "�R�[�h�敪";
            }
            //�ϑ��Ҕԍ�(�U��)
            if (!checkStringLength(recordObject.value4, 6)) {
                return "�ϑ��҃R�[�h";
            } else if (!CheckUtil.isNumber(recordObject.value4)) {
                return "�ϑ��҃R�[�h";
            }
            //�敪�Q��
            if (!checkStringLength(recordObject.value5, 2)) {
                return "�敪";
            } else if (!CheckUtil.isNumber(recordObject.value5)) {
                return "�敪";
            }
            //�Q��
            if (!checkValueFixed(recordObject.value6, "00")) {
                return "�ϑ��҃R�[�h";
            }
            //�ϑ��Җ�
            if (! CheckUtil.checkStringLength(recordObject.value7, 40)) {
                return "�ϑ��Җ�";
            }
            //�U�֓�
            if (!checkStringLength(recordObject.value8, 4)) {
                return "�U�֓�";
            } else if (CheckUtil.isNumber(recordObject.value8)) {
                int month = Integer.parseInt(recordObject.value8.substring(0, 2));
                String day = recordObject.value8.substring(2, 4);
                if (!CheckUtil.checkRange(month, 1, 12)) {
                    return "�U�֓�";
                } else if (!(checkValueFixed(day, "14") || checkValueFixed(day, "27"))) {
                    return "�U�֓�";
                }
            } else {
                return "�U�֓�";
            }
            
            return StringUtils.EMPTY;
        }
        /**
         * �t�H�[�}�b�g�C���|�[�g�f�[�^���m�F����
         * @param recordObject
         * @return 
         */
        private String checkInportData(CsvData recordObject) {
            //�f�[�^�敪
            if (!checkValueFixed(recordObject.value1, "2")) {
                return "�f�[�^�敪";
            }
            
            //�U�֋�s�ԍ�
            if (!checkStringLength(recordObject.value2, 4)) {
                return "�U�֋�s�ԍ�";
            } else if (!CheckUtil.isNumber(recordObject.value2)) {
                return "�U�֋�s�ԍ�";
            }
            //�U�֋�s�x�X�ԍ�
            if (!checkStringLength(recordObject.value4, 3)) {
                return "�U�֋�s�x�X�ԍ�";
            } else if (!CheckUtil.isNumber(recordObject.value4)) {
                return "�U�֋�s�x�X�ԍ�";
            }
            //�a�����
            if (!(checkValueFixed(recordObject.value7, "1") || checkValueFixed(recordObject.value7, "2"))) {
                return "�a�����";
            }
            //�����ԍ�
            if (!checkStringLength(recordObject.value8, 7)) {
                return "�����ԍ�";
            } else if (!CheckUtil.isNumber(recordObject.value8)) {
                return "�����ԍ�";
            }
            //�a���Җ�
            if (! CheckUtil.checkStringLength(recordObject.value9, 30)) {
                return "�a���Җ�";
            }
            //�U�֋��z
            if (!CheckUtil.checkStringLength(recordObject.value10, 10)) {
                return "�U�֋��z";
            } else if (!CheckUtil.isNumber(recordObject.value10)) {
                return "�U�֋��z";
            }
            //�V�K�R�[�h
            if (!(checkValueFixed(recordObject.value11, "1") || checkValueFixed(recordObject.value11, "0"))) {
                return "�V�K�R�[�h";
            }
            //�ڋq�ԍ�
            if (!checkStringLength(recordObject.value12, 20)) {
                return "�ڋq�ԍ�";
            } else if (!CheckUtil.isNumber(recordObject.value12)) { //TODO: check �ϑ��Ҕԍ��̏�T��
                return "�ڋq�ԍ�";
            }
            // �U�֌��ʃR�[�h
            if (!CheckUtil.isNumber(recordObject.value13)) {
                return "�U�֌��ʃR�[�h";
            } else if (StringUtils.indexOfAny(recordObject.value13, 
                    new String[]{String.valueOf(DataTransferResultDetail.TRANSFERRED), String.valueOf(DataTransferResultDetail.LACK_OF_FUNDS), 
                        String.valueOf(DataTransferResultDetail.NO_DEPOSIT_TRANSACTIONS), String.valueOf(DataTransferResultDetail.STOP_TRANSFER_REASON), 
                        String.valueOf(DataTransferResultDetail.NO_TRANSFER_REQUEST), String.valueOf(DataTransferResultDetail.TRANSFER_STOP), 
                        String.valueOf(DataTransferResultDetail.OTHER)}) == -1) {
                return "�U�֌��ʃR�[�h";
            }

            return StringUtils.EMPTY;
        }
                
        private String checkInportTrailer(CsvData recordObject) {
            //�f�[�^�敪
            if (!checkValueFixed(recordObject.value1, "8")) {
                return "�f�[�^�敪";
            }
            //�������v����
            if (!CheckUtil.checkStringLength(recordObject.value2, 6)) {
                return "�������v����";
            } else if (!CheckUtil.isNumber(recordObject.value2)) {
                return "�������v����";
            }
            //�������v���z
            if (!CheckUtil.checkStringLength(recordObject.value3, 12)) {
                return "�������v���z";
            } else if (!CheckUtil.isNumber(recordObject.value3)) {
                return "�������v���z";
            }
            //�U�֍ύ��v����
            if (!CheckUtil.checkStringLength(recordObject.value4, 6)) {
                return "�U�֍ύ��v����";
            } else if (!CheckUtil.isNumber(recordObject.value4)) {
                return "�U�֍ύ��v����";
            }
            //�������v���z
            if (!CheckUtil.checkStringLength(recordObject.value5, 12)) {
                return "�U�֍ύ��v���z";
            } else if (!CheckUtil.isNumber(recordObject.value5)) {
                return "�U�֍ύ��v���z";
            }
            //�U�֕s�\���v����
            if (!CheckUtil.checkStringLength(recordObject.value6, 6)) {
                return "�U�֕s�\���v����";
            } else if (!CheckUtil.isNumber(recordObject.value6)) {
                return "�U�֕s�\���v����";
            }
            //�U�֕s�\���v���z
            if (!CheckUtil.checkStringLength(recordObject.value7, 12)) {
                return "�U�֕s�\���v���z";
            } else if (!CheckUtil.isNumber(recordObject.value7)) {
                return "�U�֕s�\���v���z";
            }
            
            return StringUtils.EMPTY;
        }
        
         private String checkInportEnd(CsvData recordObject) {
            
            //�f�[�^�敪
            if (!checkValueFixed(recordObject.value1, "9")) {
                return "�f�[�^�敪";
            }
            return StringUtils.EMPTY;
        }
         
        private boolean checkValueFixed(String value, String valueFixed) {
            
            return value.equals(valueFixed);
        }
        
         /**
	 * ������̒������`�F�b�N����B
	 * @param value �`�F�b�N���镶����
	 * @param length �Œ��̕�����
	 * @return value�̒�����length�ȉ��Ȃ�true�Alength��蒷�����false
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
	// CsvData �N���X
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
