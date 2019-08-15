public class DesEncrypter {

private static final String ALGO="AES";
private static final String a="TheBestSecretKey";
private static final byte[] keyValue=a.getBytes();

public byte[] encrypt(byte[] bs) throws Exception{

byte[] key={'h','e','l','l','o','o','o','o','h','e','l','l','o','o','o','o'};
SecretKeySpec skeyspec=new SecretKeySpec(key,"AES");
Log.d("Encrypted Key=  ",key+"");
Cipher c=Cipher.getInstance("AES/ECB/PKCS7Padding");
c.init(Cipher.ENCRYPT_MODE,skeyspec);
byte[] encVal=bs;
Log.d("Encrypted",encVal.toString());
return c.doFinal(encVal);
}

public byte[] decrypt(byte[] encryptedData) throws Exception{
byte[] key={'h','e','l','l','o','o','o','o','h','e','l','l','o','o','o','o'};
Log.d("Decrypted Key=  ",key+"");
SecretKeySpec skeyspec=new SecretKeySpec(key,"AES");
Cipher c=Cipher.getInstance("AES/ECB/PKCS7Padding");
c.init(Cipher.DECRYPT_MODE,skeyspec);
Log.d("Inside Decryption 2 ",encryptedData+"");
byte[] decValue=c.doFinal(encryptedData);
return decValue;
}
}
