String encryptedString = … ;
String decryptedString = null;
SecretKeySpec key = new SecretKeySpec(myKey.getBytes(CHARSET_ISO_8859_1), "Blowfish");
Cipher cipher;
try {
    cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decrypted = cipher.doFinal(encryptedString.getBytes(CHARSET_ISO_8859_1));
    decryptedString = new String(decrypted, CHARSET_ISO_8859_1);
} [ catch Exceptions … ]
