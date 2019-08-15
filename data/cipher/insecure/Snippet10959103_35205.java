 private void decryption()throws Exception {
    // TODO Auto-generated method stub
    String filePath2 = path + "en/encVideo";

    String filePath3 = path + "de/decVideo";

    File decfile = new File(filePath3);


    if(!decfile.exists())
        decfile.createNewFile();

    File outfile = new File(filePath2);
    int read;

    FileInputStream encfis = new FileInputStream(outfile);
    Cipher decipher = Cipher.getInstance("AES");

    decipher.init(Cipher.DECRYPT_MODE, skey);
    FileOutputStream decfos = new FileOutputStream(decfile);
    CipherOutputStream cos = new CipherOutputStream(decfos,decipher);   

    while((read=encfis.read()) != -1)
    {

        cos.write(read);
        cos.flush();
    }
    cos.close(); 
}
