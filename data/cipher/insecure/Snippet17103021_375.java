public static byte[] enc(byte data[], byte key[]
{
Cipher c = Cipher.getInstance("AES");
SecretKeySpec k =
  new SecretKeySpec(key, "AES");
c.init(Cipher.ENCRYPT_MODE, k);
byte[] encryptedData = c.doFinal(dataToSend);
return encryptedData;
}
