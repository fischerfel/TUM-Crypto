public static void encrypt(byte[] file, String password, String fileName, String dir) throws Exception {

    SecureRandom r = new SecureRandom();
    //128 bit IV generated for each file
    byte[] iv = new byte[IV_LENGTH];
    r.nextBytes(iv);
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivspec);

    FileOutputStream fos = new FileOutputStream(dir + fileName);
    fos.write(iv);

    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    // Have to append IV --------

    cos.write(file);

    fos.flush();
    cos.flush();
    cos.close();
    fos.close();
}
