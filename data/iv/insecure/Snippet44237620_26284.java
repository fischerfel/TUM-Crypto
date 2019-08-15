public static void encryptFile(File file, PublicKey key,
        String transformation) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, IOException,
        InvalidAlgorithmParameterException, NoSuchProviderException {
    Cipher c = Cipher.getInstance(transformation, "SunJCE");
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    SecretKeySpec secretKeySpec = new SecretKeySpec(keyb, "AES");

    c.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

    FileInputStream is = new FileInputStream(file);
    CipherOutputStream os = new CipherOutputStream(new FileOutputStream(
            new File(file.getName() + "_enc")), c);

    copy(is, os);
}

public static void decryptFile(File encryptedFile, File decryptedFile,
        Key privateKey, String transformation) {
    try {
        Cipher c = Cipher.getInstance(transformation, "SunJCE");

        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        byte[] keyb = privateKey.getEncoded();

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyb, "AES");

        c.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
        CipherInputStream is = new CipherInputStream(new FileInputStream(
                encryptedFile), c);
        FileOutputStream os = new FileOutputStream(decryptedFile);

        copy(is, os);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void copy(InputStream is, OutputStream os) {

    try {
        byte[] buf = new byte[1024];
        long total = 0;
        while (true) {
            int r = is.read(buf);
            if (r == -1) {
                break;
            }
            os.write(buf, 0, r);
            total += r;
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
