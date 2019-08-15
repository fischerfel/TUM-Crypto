package com.cipher;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO Auto-generated method stub
String s="You are doing encryption at deep level";
SecureRandom sr=SecureRandom.getInstance("SHA1PRNG");
sr.setSeed(s.getBytes());
byte[] k=new byte[128/8];
sr.nextBytes(k);
SecretKeySpec spec=new SecretKeySpec(k,"AES");
byte[] iv={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
IvParameterSpec ivs=new IvParameterSpec(iv);
Cipher cps=Cipher.getInstance("AES/CBC/PKCS5Padding");
cps.init(Cipher.ENCRYPT_MODE,spec,ivs);
byte[] iv2=cps.doFinal(s.getBytes());
System.out.println("En"+iv2);
Cipher cpr=Cipher.getInstance("AES/CBC/PKCS5Padding");
cpr.init(Cipher.DECRYPT_MODE, spec,ivs);
byte[] iv3=cpr.doFinal(iv2);
String ds=new String(iv3);
System.out.println(ds);


    }

}
