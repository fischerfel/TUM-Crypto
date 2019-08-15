package com.company;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Main {

    public static void main(String[] args) {
        try {
            Cipher aesEncrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Cipher aesDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");

            SecretKeySpec key = new SecretKeySpec("ABCDEQWERTASDFGA".getBytes(), "AES");

            IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);

            aesDecrypt.init(Cipher.DECRYPT_MODE, key, ivSpec);
            aesEncrypt.init(Cipher.ENCRYPT_MODE, key, ivSpec);

            byte[] message = "Hello".getBytes();

            aesEncrypt.update(message);

            byte[] encrypted = aesEncrypt.doFinal();

            aesDecrypt.update(encrypted);

            byte[] decrypted = aesDecrypt.doFinal();

            System.out.println(new String(decrypted, "UTF-8"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
