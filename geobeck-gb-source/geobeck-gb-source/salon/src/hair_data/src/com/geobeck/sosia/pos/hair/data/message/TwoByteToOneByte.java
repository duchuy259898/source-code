/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.message;

/**
 *
 * @author VTBPhuong
 */
public class TwoByteToOneByte {

    /**
     * �S�p�A���t�@�x�b�g�Ɣ��p�A���t�@�x�b�g�Ƃ̕����R�[�h�̍�
     */
    private static final int DIFFERENCE = '�`' - 'A';

    /**
     * �ύX�ΏۑS�p�L���z��
     */
    private static char[] SIGNS2 =
            {
                    '�I',
                    '��',
                    '��',
                    '��',
                    '��',
                    '�i',
                    '�j',
                    '��',
                    '�{',
                    '�C',
                    '?',
                    '�D',
                    '�^',
                    '�F',
                    '�G',
                    '��',
                    '��',
                    '��',
                    '�H',
                    '��',
                    '�m',
                    '�n',
                    '�O',
                    '�Q',
                    '�o',
                    '�b',
                    '�p'
                    };

//    /**
//     * ���C�����\�b�h
//     * @param args
//     */
//    public static void main(String[] args) {
//        TwoByteToOneByte c = new TwoByteToOneByte();
//
//        String result = c.convert("���������F�^�^�������D�����P�R�D������");
//        System.out.println(result);
//
//    }

    /**
     * �ϊ��ΏۑS�p�L�����𔻒肷��B
     * @param pc
     * @return
     */
    private static boolean is2Sign(char pc) {
        for (char c : TwoByteToOneByte.SIGNS2) {
            if (c == pc) {
                return true;
            }
        }
        return false;
    }

    /**
     * ������̃A���t�@�x�b�g�E���l�𔼊p�����ɕϊ�����B
     * @param str
     * @return
     */
    public String convert(String str) {
        char[] cc = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cc) {
            char newChar = c;
            if ((('�`' <= c) && (c <= '�y')) || (('��' <= c) && (c <= '��'))
                    || (('�P' <= c) && (c <= '�X')) || is2Sign(c)) {
                // �ϊ��Ώۂ�char�������ꍇ�ɑS�p�����Ɣ��p�����̍���������
                newChar = (char) (c - TwoByteToOneByte.DIFFERENCE);
            }
            sb.append(newChar);
        }
        return sb.toString();
    }

}
