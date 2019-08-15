public AESCrypt(String password) throws Exception {
    // hash password with SHA-256 and crop the output to 128-bit for key
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(password.getBytes("UTF-8"));
    byte[] keyBytes = new byte[32];
    System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
    cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    //cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
    //cipher = Cipher.getInstance("AES/CBC/NoPadding");
    key = new SecretKeySpec(keyBytes, "AES");
    spec = getIV();
}

public AlgorithmParameterSpec getIV() {
    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
    IvParameterSpec ivParameterSpec;
    ivParameterSpec = new IvParameterSpec(iv);

    return ivParameterSpec;
}

public String encrypt(String plainText) throws Exception {
    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
    String encryptedText = new String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8");
    System.out.println("Encrypt Data" + encryptedText);
    return encryptedText;
}
