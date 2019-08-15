static {
    Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
}
// ...

// Crypted input data and the key
String criptedInput = "vsm1/sLWAUxW7JjKT/Amww==";
final String KEY = "jf7746yghndd";

// Decoding base64
byte[] bytesDecoded = Base64.decodeBase64(criptedInput.getBytes());

SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "DES");

Cipher cipher = null;
String result = null;

try {
    cipher = Cipher.getInstance("DES/ECB/PKCS7Padding", "BC");

    // Initialize the cipher for decryption
    cipher.init(Cipher.DECRYPT_MODE, key);

    // Decrypt the text
    byte[] textDecrypted = cipher.doFinal(bytesDecoded);

    result = new String(textDecrypted);

} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (NoSuchProviderException e) {
    e.printStackTrace();
} catch (NoSuchPaddingException e) {
    e.printStackTrace();
} catch (IllegalBlockSizeException e) {
    e.printStackTrace();
} catch (BadPaddingException e) {
    e.printStackTrace();
} catch (InvalidKeyException e) {
    e.printStackTrace();
}
