public boolean decryptFile() {

    long millis=Calendar.getInstance().getTimeInMillis();
    try{
        String path=Environment.getExternalStorageDirectory().getAbsolutePath();

    InputStream fis = new FileInputStream(path+"/Download/circus.pbf");
    File outfile = new File(path+"/Download/circus.zip");
    int read = 0;
    if (!outfile.exists())
        outfile.createNewFile();

    FileOutputStream fos = new FileOutputStream(outfile);

    IvParameterSpec ive = new IvParameterSpec(key2.getBytes("UTF-8"));

    SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"),
            "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ive);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();
        Log.e("Decryption:", (Calendar.getInstance().getTimeInMillis() - millis) / (1000 + 0.0) + " sec");
        return true;

}
    catch(IOException ex){
        ex.printStackTrace();
    }
    catch(InvalidAlgorithmParameterException ex){
        ex.printStackTrace();
    }
    catch(NoSuchPaddingException ex){
        ex.printStackTrace();
    }
    catch(InvalidKeyException ex){
        ex.printStackTrace();
    }
    catch(NoSuchAlgorithmException ex){
        ex.printStackTrace();
    }
    return false;
}


public boolean encryptFile()  {
    long millis= Calendar.getInstance().getTimeInMillis();

    // Here you read the cleartext.
    try {

        String path=Environment.getExternalStorageDirectory().getAbsolutePath();
        InputStream fis = new FileInputStream(path+"/Download/circus.zip");

        /*File folder=new File(dir);
        folder.mkdir();*/

        File outfile = new File(path+"/Download/circus.pbf");
        int read = 0;
        if (!outfile.exists())
            outfile.createNewFile();

        FileOutputStream encfos = new FileOutputStream(outfile);


        IvParameterSpec ive = new IvParameterSpec(key2.getBytes("UTF-8"));

        SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"),
                "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ive);

        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(encfos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }

        cos.flush();
        cos.close();
        fis.close();
        Log.e("Encryption:",(Calendar.getInstance().getTimeInMillis()-millis)/(1000+0.0)+" sec");
        return true;
    }
    catch(IOException ex){
        ex.printStackTrace();
    }
    catch(InvalidAlgorithmParameterException ex){
        ex.printStackTrace();
    }
    catch(NoSuchPaddingException ex){
        ex.printStackTrace();
    }
    catch(InvalidKeyException ex){
        ex.printStackTrace();
    }
    catch(NoSuchAlgorithmException ex){
        ex.printStackTrace();
    }
    return false;
}
