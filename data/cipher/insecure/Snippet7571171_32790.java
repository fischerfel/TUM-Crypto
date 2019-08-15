// Decrypts the given ciphertext with the given password
public String decrypt(String ciphertext, String password)
    throws FailedCryptOperationException {
    String plaintext = "";
    byte[] ciphertext_bytes = decode(ciphertext);

    try {
        byte[] salt = decode(SALT_BASE64);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); //$NON-NLS-1$
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1024, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES"); 
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        plaintext = new String(cipher.doFinal(ciphertext_bytes), TEXT_FORMAT);
    } catch (Exception e) {
        throw new FailedCryptOperationException(e);
    }

    return plaintext;
}

// Does Base64 decoding
public byte[] decode(String text) throws FailedCryptOperationException {
    byte[] res;
    BASE64Decoder       decoder         = new BASE64Decoder();
    try {
        res = decoder.decodeBuffer(text);
    } catch (IOException e) {
        throw new FailedCryptOperationException(e);
    }
    return res;
}
