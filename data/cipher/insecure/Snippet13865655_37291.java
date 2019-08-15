private byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    int offset = 0;
    byte[] encrypted;

    while(offset < clear.length()) {
        final byte[] answer = cipher.update(clear, offset, 1024);
        encrypted = Arrays.copyOf( encrypted, encrypted.length + 1024);
        System.arrayCopy(answer, 0, encrypted, offset, 1024);
        offset += 1024;
    }
    encrypted += cipher.doFinal(clear, offset, clear.length() - offset);
    return encrypted;
}
