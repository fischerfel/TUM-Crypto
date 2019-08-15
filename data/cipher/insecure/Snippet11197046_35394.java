 public void decryptFile(String key, String typeFile) throws InvalidKeyException, 
                                                          NoSuchAlgorithmException, 
                                                       InvalidKeySpecException, 
                                                            NoSuchPaddingException, 
                                                                       IOException
   {

       DESKeySpec dks = new DESKeySpec(key.getBytes());
       SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
       SecretKey desKey = skf.generateSecret(dks);
       Cipher pbeCipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

       pbeCipher.init(Cipher.DECRYPT_MODE, desKey);    


        // Decrypt the ciphertext and then print it out.     
        FileInputStream output = null;

        File encryptedFile = new File(Environment.getExternalStorageDirectory() + "/images/Et1.jpg");   
        File decryptedFile = new File(Environment.getExternalStorageDirectory() + "/images/Dt1.jpg");


        try 
        {

        output = new FileInputStream(encryptedFile);
        } 
        catch (FileNotFoundException e) 
        {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }

    CipherInputStream cis = new CipherInputStream(output, pbeCipher);
    BufferedImage input = null;

    try 
    {
        input = ImageIO.read(cis);
    } 
    catch (IOException e) 
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    FileOutputStream out = null;

    try 
    {
        out = new FileOutputStream(decryptedFile);
    } 
    catch (FileNotFoundException e) 
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try 
    {
        ImageIO.write(input,typeFile, out);
    } 
    catch (IOException e) 
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try 
    {
        cis.close();
    } 
    catch (IOException e) 
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


   }
