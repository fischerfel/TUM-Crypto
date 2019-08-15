try
{

    FileInputStream encfis = new FileInputStream(Encrypted_File_Path);
    FileOutputStream decfos = new FileOutputStream(Decrypted_File_Path);

    Cipher decipher = Cipher.getInstance("AES");

    String encode ="8888888888888888";
    byte[] decodedKey = encode.getBytes();
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    decipher.init(Cipher.DECRYPT_MODE, originalKey);
    CipherOutputStream cos = new CipherOutputStream(decfos,decipher);

    int c;
    byte[] d1 = new byte[4096 * 2048];
    while((c = encfis.read(d1)) != -1)
    {
        cos.write(d1, 0, c);
        cos.flush();
    }
    cos.close();
}
catch(Exception e)
{
    e.printStackTrace();
}
