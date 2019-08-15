package com.test.demo;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class StringEncryptor {

    private static final Logger LOGGER = Logger.getLogger(StringEncryptor.class);

    public static String getEncryptedString(String input){

        if(input == null){
            return null;
        }

        String keyString = "C0BAE23DF8B51807B3E17D21925FADF2";//32 byte string
        byte[] keyValue = DatatypeConverter.parseHexBinary(keyString);
        Key key = new SecretKeySpec(keyValue, "AES");
        Cipher c1 = null;
        try {
            c1 = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            LOGGER.error("error getting cypher", e);
            return null;
        }
        try {
            c1.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            LOGGER.error("error initalising cypher", e);
            return null;
        }

        byte[] encVal = null;
        try {
            encVal = c1.doFinal(input.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.error("error performing encryption", e);
            return null;
        }
        String encryptedValue = Base64.encodeBase64String(encVal);

        return encryptedValue;
    }
}
