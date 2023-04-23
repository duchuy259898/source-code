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
     * 全角アルファベットと半角アルファベットとの文字コードの差
     */
    private static final int DIFFERENCE = 'Ａ' - 'A';

    /**
     * 変更対象全角記号配列
     */
    private static char[] SIGNS2 =
            {
                    '！',
                    '＃',
                    '＄',
                    '％',
                    '＆',
                    '（',
                    '）',
                    '＊',
                    '＋',
                    '，',
                    '?',
                    '．',
                    '／',
                    '：',
                    '；',
                    '＜',
                    '＝',
                    '＞',
                    '？',
                    '＠',
                    '［',
                    '］',
                    '＾',
                    '＿',
                    '｛',
                    '｜',
                    '｝'
                    };

//    /**
//     * メインメソッド
//     * @param args
//     */
//    public static void main(String[] args) {
//        TwoByteToOneByte c = new TwoByteToOneByte();
//
//        String result = c.convert("ｈｔｔｐ：／／ｗｗｗ．ｒａ１３．ｏｒｇ");
//        System.out.println(result);
//
//    }

    /**
     * 変換対象全角記号かを判定する。
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
     * 文字列のアルファベット・数値を半角文字に変換する。
     * @param str
     * @return
     */
    public String convert(String str) {
        char[] cc = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cc) {
            char newChar = c;
            if ((('Ａ' <= c) && (c <= 'Ｚ')) || (('ａ' <= c) && (c <= 'ｚ'))
                    || (('１' <= c) && (c <= '９')) || is2Sign(c)) {
                // 変換対象のcharだった場合に全角文字と半角文字の差分を引く
                newChar = (char) (c - TwoByteToOneByte.DIFFERENCE);
            }
            sb.append(newChar);
        }
        return sb.toString();
    }

}
