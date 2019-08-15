import javax.crypto.Cipher;
private Cipher myGetCipher() {
    Cipher c = Cipher.getInstance("RC4");
    c.init(Cipher.DECRYPT_MODE, new SecretKeySpec("myPassword".getBytes(), "RC4"));
    return c
}
