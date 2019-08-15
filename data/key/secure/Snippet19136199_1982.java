 static Cipher c;// = Cipher.getInstance("AES/CBC/PKCS5Padding");
private static void Encrypt_File(String infile, String outFile) throws Exception
{
    //String destKey = "123456";
    //byte[] IV = generateRandomBytes(16);

    //byte[] salt = generateRandomBytes(16);
    //Rfc2898DeriveBytes rfc = new Rfc2898DeriveBytes("123456", salt, 1000);
    //SecretKey key = new SecretKeySpec(rfc.getBytes(32), "AES");
    SecretKey key = new SecretKeySpec(generateRandomBytes(32), "AES");
    //if(c == null)
    c = Cipher.getInstance("AES/CBC/PKCS5Padding");

    c.init(Cipher.ENCRYPT_MODE, key);
    FileOutputStream fos = new FileOutputStream(outFile);
    CipherOutputStream cos = new CipherOutputStream(fos, c);
    FileInputStream fis = new FileInputStream(infile);
    try
    {
        int len = 0;
        byte[] buf = new byte[1024*128];
        while((len = fis.read(buf)) != -1) {
            cos.write(buf, 0, len);
            //cos.flush();
        }           
    }
    finally 
    {
        c.doFinal();
        cos.flush();
        cos.close();                
        fos.flush();
        fos.close();
        fis.close();

    }
}
