private static void myCryptography(){

Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
byte[] input = "Hitesh Dhamshaniya".getBytes();
byte[] keyBytes = "ABCD657865BHNKKK".getBytes();
SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

// encryption pass

cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
ctLength += cipher.doFinal(cipherText, ctLength);
Log.e("==> ", " == > Encode " + Base64.encodeToString(cipherText, Base64.DEFAULT));
String encodedStr = Base64.encodeToString(cipherText, Base64.DEFAULT);
// decryption pass

cipherText = Base64.decode(encodedStr, Base64.DEFAULT);
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
ptLength += cipher.doFinal(plainText, ptLength);
Log.e("==> ", " == > Decoded " + new String(plainText, "UTF-8"));

}
