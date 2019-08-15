private static String rc4(String plaintext, int mode, Key key) throws Exception {
    Cipher cipher = Cipher.getInstance("RC4");
    cipher.init(mode, (java.security.Key) key);
    return new String(cipher.doFinal(plaintext.getBytes()));
}

public static String encrypt(String plaintext, Key key) throws Exception {
    return rc4(plaintext, Cipher.ENCRYPT_MODE, key);
}
