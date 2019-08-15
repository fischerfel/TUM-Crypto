//This method compares a password received from keyboard with the decrypted password (decrypting output from generatePswdBasedKey(String password))
public static boolean checkPswdBasedKey(String password, String passwordInput){
byte bufferBytes[] = Base64.decode(password);
SecretKey sk = new SecretKeySpec(bufferBytes, 0, bufferBytes.length, "AES"); //Also tried new SecretKeySPec(bufferBytes, "AES");...
Cipher c = Cipher.getInstance(Cifrador.AES_MODE);//AES_MODE = AES/CBC/PKCS5Padding
IvParameterSpec ivParams = new IvParameterSpec(iv);//IV already initialized
c.init(Cipher.DECRYPT_MODE, sk, ivParams);
byte result[] = c.doFinal(bufferBytes);
String resultStr = Base64.encodeToString(result, false); //.encodeToString(byte[] sArr, boolean lineSep)
if(passwordInput.equalsIgnoreCase(resultStr)){
return true;
}
return false;
}
