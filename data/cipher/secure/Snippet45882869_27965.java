public static String rsadecrypt(byte[] text) {
try {
// get an RSA cipher object and print the provider
final Cipher cipher = Cipher.getInstance("RSA");

// decrypt the text using the private key
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] dectyptedText = cipher.doFinal(text);
return new String(dectyptedText);
} catch (Exception ex) {
ex.printStackTrace();
}
return null;
}
public static byte[] rsaencrypt(String text) {

try {
  // get an RSA cipher object and print the provider
  final Cipher cipher = Cipher.getInstance("RSA");
  // encrypt the plain text using the public key
  cipher.init(Cipher.ENCRYPT_MODE, clientKey);
  byte[] cipherText = cipher.doFinal(text.getBytes());
  return cipherText;
} catch (Exception e) {
  e.printStackTrace();
}
return null;
}
