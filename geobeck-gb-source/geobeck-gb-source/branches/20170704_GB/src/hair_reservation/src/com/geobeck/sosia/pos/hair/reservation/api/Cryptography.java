/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

/**
 *　Created date:20121201
 * @author ivs
 */
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Cryptography {

    public static final String ALGORITHM = "Blowfish";
    /**
     * 
     * @param key
     * @param text
     * @return Blowfishでデータを暗号化する。
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public static String encrypt(String key, String text) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec sksSpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return new String(Hex.encodeHex(encrypted));
    }
    /**
     * 
     * @param key
     * @param text
     * @return Blowfishでデータを暗号解読する。
     * @throws DecoderException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    //暗号解読
    public static String decrypt(String key, String text) throws DecoderException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encrypted = Hex.decodeHex(text.toCharArray());
        SecretKeySpec sksSpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, sksSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }
}
