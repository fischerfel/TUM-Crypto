    package com.abc.some.common.nativeDES;

    import java.io.ByteArrayOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;

public class DESEncrypt {
    public String keyValue = "123456789";
    public static void main(String[] args) {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SomeRequest><OrderNumber>1564578</OrderNumber></SomeRequest>";
        String codedtext ="not encrypted";
        try{
            codedtext = new DESEncrypt().Encrypt1(text);
            //codedtext = new DESEncrypt().encrypt(text);
        }catch (Exception e) {
            System.out.println("Exception in Encryption.. " + e.getMessage());
        }

        System.out.println(codedtext);

    }
    public String Encrypt1(String CXML) {
        try {
            KeySpec myKey = new DESKeySpec(keyValue.getBytes("UTF8"));
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(myKey);
        Cipher ecipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] data = CXML.getBytes("ASCII");

        Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] crypt = ecipher.doFinal(data);
        //String encoded = DatatypeConverter.printBase64Binary(crypt.toString().getBytes("ASCII"));
        //String encoded = DatatypeConverter.printBase64Binary(crypt.getBytes("ASCII"));

        String encoded = DatatypeConverter.printBase64Binary(crypt).toString();

        System.out.println(encoded);

        return encoded;
        } catch (Exception ex) {
        }

        return null;
    }    
}
