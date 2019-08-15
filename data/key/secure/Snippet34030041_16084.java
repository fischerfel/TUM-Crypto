public static void test() throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    SecureRandom rnd = new SecureRandom();
    byte[] keyData = new byte[16];
    byte[] iv = new byte[16];
    rnd.nextBytes(keyData);
    rnd.nextBytes(iv);
    SecretKeySpec key = new SecretKeySpec(keyData, "AES");

    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    CipherOutputStream out = new CipherOutputStream(buffer, cipher);

    byte[] plain = "Test1234567890_ABCDEFG".getBytes();
    out.write(plain);
    out.flush();
    out.close();
    byte[] encrypted = buffer.toByteArray();
    System.out.println("Plaintext length: " + plain.length);
    System.out.println("Padding length  : " + (cipher.getBlockSize() - (plain.length % cipher.getBlockSize())));
    System.out.println("Cipher length   : " + encrypted.length);

    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    CipherInputStream in = new CipherInputStream(new ByteArrayInputStream(encrypted), cipher);
    buffer = new ByteArrayOutputStream();
    byte[] b = new byte[100];
    int read;
    while ((read = in.read(b)) >= 0) {
        buffer.write(b, 0, read);
    }
    in.close();

    // prints Test1234567890_ABCDEFG
    System.out.println(new String(buffer.toByteArray()));
}
