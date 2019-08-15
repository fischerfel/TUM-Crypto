public class Crypto {
private static final String mEngine = "AES";
private static final String mCryptoEncrypt = "AES/ECB/ZeroBytePadding";
private String mKey;
public Crypto(String key) {
    this.mKey = key;
}
public byte[] cipher(byte[] data, int mode, String crypto)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException {
    SecretKeySpec sks =new SecretKeySpec(mKey.getBytes(Charset
            .forName("UTF-8")), mEngine);

    Cipher c = Cipher.getInstance(crypto);
    c.init(mode, sks);
    return c.doFinal(data);
}

public String encrypt(byte[] data) throws InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException {
    return Base64.encodeToString(cipher(data, Cipher.ENCRYPT_MODE, mCryptoEncrypt), Base64.DEFAULT);
}

}
