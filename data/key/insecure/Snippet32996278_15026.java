package com.caja.utilidades;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainClass {

private static final String ALGORITHM = "AES";
private static final String keyValue = "thisisasecretkey";

public static void main(String[] args) throws Exception {
    System.out.println(encrypt("hello world"));
}

public static String encrypt(String valueToEnc) throws Exception {
  Key key = generateKey();
  Cipher cipher = Cipher.getInstance(ALGORITHM);
  cipher.init(Cipher.ENCRYPT_MODE, key);
  byte[] encValue = cipher.doFinal(valueToEnc.getBytes());
  return new String(encValue);
}

private static Key generateKey() throws Exception {
  Key key = new SecretKeySpec(keyValue.getBytes(), ALGORITHM);
  return key;
}


}
