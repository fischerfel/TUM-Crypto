public static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

public Cipher createCipher(final int encryptionMode, final Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    final Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(encryptionMode, key);
    return cipher;
}

public byte[] encryptString(final String text, final PrivateKey privateKey) throws GeneralSecurityException, IOException {
    return createCipher(Cipher.ENCRYPT_MODE, privateKey).doFinal(text.getBytes("UTF-8"));
}

public String decryptString(final byte[] msg, final PublicKey publicKey) throws GeneralSecurityException, IOException {
    final byte[] decrypted = createCipher(Cipher.DECRYPT_MODE, publicKey).doFinal(msg);
    return new String(decrypted, "UTF-8");
}

// me 
final PrivateKey privateKey = ... read from file ...
final byte[] msg = encryptString("my-secret-text-that-everybody-can-read-but-only-I-can-generate", privateKey);

// other person
final PublicKey publicKey = ... read from file ...
final String text = decryptString(msg, publicKey));
