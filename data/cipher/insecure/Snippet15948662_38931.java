String encryptedString = … ;
String decryptedString = null;
SecretKeySpec key = new SecretKeySpec(myKey.getBytes(), "Blowfish");
Cipher cipher;
try {
    cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decrypted = cipher.doFinal(encryptedString.getBytes());
    decryptedString = new String(decrypted, Charset.forName("UTF-8"));
} [ catch Exceptions … ]
