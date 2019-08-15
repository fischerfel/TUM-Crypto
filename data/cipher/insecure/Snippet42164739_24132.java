package org.springframework.security.util;

import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.SpringSecurityException;
import org.springframework.util.Assert;

public final class EncryptionUtils
{
  public static byte[] stringToByteArray(String input)
  {
    Assert.hasLength(input, "Input required");
    try
    {
      return input.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException fallbackToDefault) {}
    return input.getBytes();
  }

  public static String byteArrayToString(byte[] byteArray)
  {
    Assert.notNull(byteArray, "ByteArray required");
    Assert.isTrue(byteArray.length > 0, "ByteArray cannot be empty");
    try
    {
      return new String(byteArray, "UTF8");
    }
    catch (UnsupportedEncodingException e) {}
    return new String(byteArray);
  }

  private static byte[] cipher(String key, byte[] passedBytes, int cipherMode)
    throws EncryptionUtils.EncryptionException
  {
    try
    {
      KeySpec keySpec = new DESedeKeySpec(stringToByteArray(key));
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
      Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
      SecretKey secretKey = keyFactory.generateSecret(keySpec);
      cipher.init(cipherMode, secretKey);
      return cipher.doFinal(passedBytes);
    }
    catch (Exception e)
    {
      throw new EncryptionException(e.getMessage(), e);
    }
  }

  public static String encrypt(String key, String inputString)
    throws EncryptionUtils.EncryptionException
  {
    isValidKey(key);
    byte[] cipherText = cipher(key, stringToByteArray(inputString), 1);
    return byteArrayToString(Base64.encodeBase64(cipherText));
  }

  public static byte[] encrypt(String key, byte[] inputBytes)
    throws EncryptionUtils.EncryptionException
  {
    isValidKey(key);
    return Base64.encodeBase64(cipher(key, inputBytes, 1));
  }

  public static String decrypt(String key, String inputString)
    throws EncryptionUtils.EncryptionException
  {
    Assert.hasText(key, "A key is required to attempt decryption");
    byte[] cipherText = cipher(key, Base64.decodeBase64(stringToByteArray(inputString)), 2);
    return byteArrayToString(cipherText);
  }

  public static byte[] decrypt(String key, byte[] inputBytes)
    throws EncryptionUtils.EncryptionException
  {
    Assert.hasText(key, "A key is required to attempt decryption");
    return cipher(key, Base64.decodeBase64(inputBytes), 2);
  }

  private static void isValidKey(String key)
  {
    Assert.hasText(key, "A key to perform the encryption is required");
    Assert.isTrue(key.length() >= 24, "Key must be at least 24 characters long");
  }

  public static class EncryptionException
    extends SpringSecurityException
  {
    private static final long serialVersionUID = 1L;

    public EncryptionException(String message, Throwable t)
    {
      super(t);
    }

    public EncryptionException(String message)
    {
      super();
    }
  }
}
