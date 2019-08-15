public static String md5(String string) {
    byte[] hash;

    try {
        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Huh, MD5 should be supported?", e);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
    }

    StringBuilder hex = new StringBuilder(hash.length * 2);

    for (byte b : hash) {
        int i = (b & 0xFF);
        if (i < 0x10) hex.append('0');
        hex.append(Integer.toHexString(i));
    }

    return hex.toString();
}

public static byte[] rijndael_256(String text, byte[] givenKey) throws DataLengthException, IllegalStateException, InvalidCipherTextException, IOException{
    final int keysize;

    if (givenKey.length <= 192 / Byte.SIZE) {
        keysize = 192;
    } else {
        keysize = 256;
    }

    byte[] keyData = new byte[keysize / Byte.SIZE];
    System.arraycopy(givenKey, 0, keyData, 0, Math.min(givenKey.length, keyData.length));
    KeyParameter key = new KeyParameter(keyData);
    BlockCipher rijndael = new RijndaelEngine(256);
    ZeroBytePadding c = new ZeroBytePadding();
    PaddedBufferedBlockCipher pbbc = new PaddedBufferedBlockCipher(rijndael, c);
    pbbc.init(true, key);

    byte[] plaintext = text.getBytes(Charset.forName("UTF8"));
    byte[] ciphertext = new byte[pbbc.getOutputSize(plaintext.length)];
    int offset = 0;
    offset += pbbc.processBytes(plaintext, 0, plaintext.length, ciphertext, offset);
    offset += pbbc.doFinal(ciphertext, offset);
    return ciphertext;
}


public static String encrypt(String text, String secretKey) throws Exception {


    byte[] givenKey = String.valueOf(md5(secretKey)).getBytes(Charset.forName("ASCII"));

    byte[] encrypted = rijndael_256(text,givenKey);

    return new String(Base64.encodeBase64(encrypted));
}
