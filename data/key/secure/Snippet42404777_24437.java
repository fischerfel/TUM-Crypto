public static void decrypt(File file, String password) throws Exception
{
    byte[] iv = new byte[16];
    DataInputStream dis = new DataInputStream(new FileInputStream(file));
    dis.readFully(iv);

    SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
    CipherInputStream cis = new CipherInputStream(dis, cipher);

    // Now just read plaintext from `cis` and do whatever you want with it.

    cis.close();
}
