String encryptedString = … ;  
String decryptedString = null;
SecretKeySpec key = new SecretKeySpec(myKey.getBytes(), "Blowfish");
private static byte[] linebreak = {}; // Remove Base64 encoder default linebreak
private static Base64 coder;
Cipher cipher;
try {
    coder = new Base64(32, linebreak, true);
    cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decrypted = cipher.doFinal(encryptedString.getBytes());
    decryptedString = new String(coder.encode(decrypted));
} [ catch Exceptions … ]
