try{
    FileInputStream fis = new FileInputStream(new File(c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))));
    File outfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/appvideo/1.mp4");

    int read;
    if(!outfile.exists())
        outfile.createNewFile();

    File decfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/appvideo/2.mp4");
    if(!decfile.exists())
        decfile.createNewFile();

    FileOutputStream fos = new FileOutputStream(outfile);
    FileInputStream encfis = new FileInputStream(outfile);
    FileOutputStream decfos = new FileOutputStream(decfile);

    Cipher encipher = Cipher.getInstance("AES");
    Cipher decipher = Cipher.getInstance("AES");

    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecretKey skey = kgen.generateKey();
    encipher.init(Cipher.ENCRYPT_MODE, skey);
    CipherInputStream cis = new CipherInputStream(fis, encipher);
    decipher.init(Cipher.DECRYPT_MODE, skey);
    CipherOutputStream cos = new CipherOutputStream(decfos,decipher);

    while((read = cis.read())!=-1)
    {
        fos.write((char)read);
        fos.flush();
    }
    fos.close();
    while((read=encfis.read())!=-1)
    {
        cos.write(read);
        cos.flush();
    }
    cos.close();

}catch (Exception e) {
    // TODO: handle exceptione
    e.printStackTrace();
}
