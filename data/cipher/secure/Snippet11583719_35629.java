public static InputStream getUncryptInputStream(InputStream is, String pass, final long dataLen) throws Exception{
        SecretKeySpec key = new SecretKeySpec(getRawKey(pass.getBytes()), "AES");
        Cipher mCipher = Cipher.getInstance("AES/CTR/NoPadding");
        mCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        if(dataLen==-1){
            return new CipherInputStream(is, mCipher);
        }else{
            return new CipherInputStreamWithDataLen(is, mCipher, dataLen);
        }
}

public static OutputStream getCryptOutputStream(OutputStream os, String pass) throws Exception{
    SecretKeySpec key = new SecretKeySpec(getRawKey(pass.getBytes()), "AES");

    Cipher mCipher = Cipher.getInstance("AES/CTR/NoPadding");
    mCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

    return new CipherOutputStream(os, mCipher);

}

private static byte[] getRawKey(byte[] seed) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    kgen.init(128, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
}
