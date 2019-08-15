public static void decrypt(byte[] file, String password, String fileName, String dir) throws Exception
{   
    // gets the IV
    int ivIndex = file.length - 16;

    byte[] truncatedFile = Arrays.copyOfRange(file, 0, file.length - 16);

    SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(truncatedFile, ivIndex, 16));

    //IvParameterSpec ivspec = new IvParameterSpec(iv);
    //
    //cipher.init(Cipher.DECRYPT_MODE, keySpec, ivspec);

    FileOutputStream fos = new FileOutputStream(dir + fileName);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    cos.write(file);
    fos.flush();
    cos.flush();
    cos.close();
    fos.close();
}
