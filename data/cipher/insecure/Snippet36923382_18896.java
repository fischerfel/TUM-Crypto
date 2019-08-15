Cipher cipher = null;
try {
    cipher = Cipher.getInstance("AES");
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (NoSuchPaddingException e) {
    e.printStackTrace();
}
SecretKeySpec secretKeySpec = new SecretKeySpec(array, "AES");
try {
    cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
} catch (InvalidKeyException e) {
    e.printStackTrace();
}
byte[] decrypted=new byte[0];
try {
    decrypted=cipher.doFinal(ciphertext.getBytes());
} catch (IllegalBlockSizeException e) {
    e.printStackTrace();
} catch (BadPaddingException e) {
    e.printStackTrace();
}
String plaintext= Base64.encodeToString(decrypted,0);
pt.setText(plaintext);
