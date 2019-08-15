private final Cipher cipher;
private final SecretKeySpec key;
private AlgorithmParameterSpec spec;
private String encryptedText, decryptedText;
private String password = "PASSWORD";

public AESCrypt() throws Exception {

    // hash password with SHA-256 and crop the output to 128-bit for key
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(password.getBytes("UTF-8"));
    byte[] keyBytes = new byte[16];
    System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    key = new SecretKeySpec(keyBytes, "AES");
    spec = getIV();
    }

public AlgorithmParameterSpec getIV() {
    AlgorithmParameterSpec ivspec;
    byte[] iv = new byte[cipher.getBlockSize()];
    new SecureRandom().nextBytes(iv);
    ivspec = new IvParameterSpec(iv);
    return ivspec;
    }

public String encrypt(String plainText) throws Exception {      
    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes());
    encryptedText = Base64.encodeToString(encrypted, Base64.DEFAULT);
    return encryptedText;
}

public String decrypt(String cryptedText) throws Exception {
    cipher.init(Cipher.DECRYPT_MODE, key, spec);
    byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
    byte[] decrypted = cipher.doFinal(bytes);
    decryptedText = new String(decrypted, "UTF-8");
    return decryptedText;
}   
