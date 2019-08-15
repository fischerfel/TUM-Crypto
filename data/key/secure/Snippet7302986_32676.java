public String asHex(byte[] buf) {
    StringBuffer strbuf = new StringBuffer(buf.length * 2);
    int i;
    for (i = 0; i < buf.length; i++) {
        if (((int) buf[i] & 0xff) < 0x10)
            strbuf.append("0");
        strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
    }
    return strbuf.toString();
}

private SecretKeySpec skeySpec;
private Cipher cipher;
private byte[] encrypted;

public String encrypt(String str) throws Exception {
    // Get the KeyGenerator
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128); // 192 and 256 bits may not be available

    // Generate the secret key specs.
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    skeySpec = new SecretKeySpec(raw, "AES");

    // Instantiate the cipher
    cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    encrypted = cipher.doFinal(str.getBytes());
    return asHex(encrypted);
}

public String decrypt(String str) throws Exception {
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] original = cipher.doFinal(encrypted);
    String originalString = new String(original);
    return originalString;
}
