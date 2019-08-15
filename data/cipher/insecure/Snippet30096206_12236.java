String str=message+username;
byte[] byte1= str.getBytes("UTF8");
byte[] byte2= encrypt(pubk, byte1);

public byte[] encrypt(PublicKey key, byte[] plaintext)
{
Cipher cipher = Cipher.getInstance("AES");   
cipher.init(Cipher.ENCRYPT_MODE, key);  
return cipher.doFinal(plaintext);
}
