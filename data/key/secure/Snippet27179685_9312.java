<code>
public static String decrypt(String keyText,String encryptedText) 
{
// generate key 
Key key = new SecretKeySpec(keyText.getBytes(), "AES");
Cipher chiper = Cipher.getInstance("AES");
chiper.init(Cipher.DECRYPT_MODE, key);
byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
byte[] decValue = chiper.doFinal(decordedValue);
String decryptedValue = new String(decValue);
return decryptedValue;
}  
</code>
