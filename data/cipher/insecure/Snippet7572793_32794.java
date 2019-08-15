public class TripleDES {
private String key;
private byte[] initializationVector;

public TripleDES(String key, byte[] initializationVector)
{
    this.key = key;
    this.initializationVector = initializationVector;
}

public String encryptText(String plainText) throws Exception{
//----  Use specified 3DES key and IV from other source -------------------------
  byte[] plaintext = plainText.getBytes();
  byte[] tdesKeyData = key.getBytes();

  System.out.println("plain text length: " + plaintext.length);
  System.out.println("key length: " + tdesKeyData.length);


  Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
  SecretKeySpec    myKey = new SecretKeySpec(tdesKeyData, "DESede");
  IvParameterSpec ivspec = new IvParameterSpec(initializationVector);

  c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
  byte[] cipherText = c3des.doFinal(plaintext);

  return Base64Coder.encodeString(new String(cipherText));
}

public String decryptText(String encryptedText) throws Exception{
    //----  Use specified 3DES key and IV from other source -------------------
      byte[] enctext = Base64Coder.decode(encryptedText);
      byte[] tdesKeyData = key.getBytes();


      Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
      SecretKeySpec    myKey = new SecretKeySpec(tdesKeyData, "DESede");
      IvParameterSpec ivspec = new IvParameterSpec(initializationVector);

      c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);
      byte[] cipherText = c3des.doFinal(enctext);
      return new String(cipherText);
    }
