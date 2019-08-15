 public static byte[] Encrypt(byte[] a,Key skey) throws Exception {

 // Instantiate the cipher

Cipher cipher = Cipher.getInstance("AES");  
cipher.init(Cipher.ENCRYPT_MODE, skey);

byte[] encrypted =cipher.doFinal(a);
cipher.init(Cipher.DECRYPT_MODE, skey);
byte[] original = cipher.doFinal(encrypted);
String originalString = new String(original);

return encrypted;
}
