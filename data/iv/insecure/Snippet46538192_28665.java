private final static byte[] iv = new byte[16];
private static final String TRANSFORMATION = "AES/CBC/NoPadding";

public static CipherOutputStream cryptOutputStream(SecretKey key,OutputStream os) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
        CipherOutputStream out = new CipherOutputStream(os, cipher);
        return out;
}

public static CipherInputStream decryptInputStream(SecretKey key,InputStream os) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
        CipherInputStream out = new CipherInputStream(os, cipher);
        return out;
}
