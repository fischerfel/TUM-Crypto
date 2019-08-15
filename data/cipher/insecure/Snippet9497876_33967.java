public static void encryptVideos(File fil,File outfile)
{ 
  try{
    FileInputStream fis = new FileInputStream(fil);
    //File outfile = new File(fil2);
    int read;
    if(!outfile.exists())
      outfile.createNewFile();
    FileOutputStream fos = new FileOutputStream(outfile);
    FileInputStream encfis = new FileInputStream(outfile);
    Cipher encipher = Cipher.getInstance("AES");
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    //byte key[] = {0x00,0x32,0x22,0x11,0x00,0x00,0x00,0x00,0x00,0x23,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    SecretKey skey = kgen.generateKey();
    //Lgo
    encipher.init(Cipher.ENCRYPT_MODE, skey);
    CipherInputStream cis = new CipherInputStream(fis, encipher);
    while((read = cis.read())!=-1)
      {
        fos.write(read);
        fos.flush();
      }   
    fos.close();
  }catch (Exception e) {
    // TODO: handle exception
  }
}
