protected void Encrypt()
{

    byte[] numBytes = {'0','1','2','3','4','5','6','7','8','9','0','1','2','3','4','5','6','7','8','9', '0','1','2','3','4','5','6','7','8','9', '0','1','2','3','4','5','6','7','8','9'};
    byte[] smallCase = {'a','b','c','d','a','b','c','d','a','b','c','d','a','b','c','d'};
    byte[] capitalCase = {'A','B','C','D','A','B','C','D','A','B','C','D','A','B','C','D'};

    try {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1And8BIT");
        KeySpec spec = new PBEKeySpec("junglebook".toCharArray(), "Salt".getBytes(), 65536, 256);
        SecretKey tmp = null;
        tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        /* Encryption cipher initialization. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        Log.d("Encryption" + "iv data :", iv.toString());

        /*Open two Cipher ouput streams to the same encrypted file*/
        FileOutputStream os = new FileOutputStream(sdCard.getAbsolutePath() + "/Notes/sample.encrypted");
        CipherOutputStream cos = new CipherOutputStream(os,cipher);

        FileOutputStream os1 = new FileOutputStream(sdCard.getAbsolutePath() + "/Notes/sample.encrypted");
        CipherOutputStream cos1 = new CipherOutputStream(os1,cipher);

        int offset = 0;
        Log.d("Encryption", "Writing cipher text to output file");
        //Write 16 bytes header data with smallCase array
        cos.write(smallCase, offset, 16);
        // write 40 bytes actual data
        cos.write(numBytes, offset, 40);

        FileOutputStream ivStream = new FileOutputStream(sdCard.getAbsolutePath() + "/Notes/iv.dat");
        if (ivStream != null) {
            Log.d("Encryption", "Writing iv data to output file");
            ivStream.write(iv);
        }
        cos.close();

        // Overwrite header data with capitalCase array data
        cos1.write(capitalCase, offset, 16);
        cos1.close();

        ivStream.close();

    }catch (Exception e) {
        e.printStackTrace();
    }
}

protected void Decrypt()
{
    byte[] dBytes = new byte[200];

    try {

        Log.d("Decryption", "Reading iv data ");
        File f1 = new File(sdCard.getAbsolutePath()+"/Notes/iv.dat");
        byte[] newivtext = new byte[(int)f1.length()];
        FileInputStream readivStream = new FileInputStream(sdCard.getAbsolutePath()+"/Notes/iv.dat");
        if(readivStream != null) {
            readivStream.read(newivtext);
        }

        // Generate the secret key from same password and salt used in encryption
        SecretKeyFactory dfactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1And8BIT");
        KeySpec dspec = new PBEKeySpec("junglebook".toCharArray(), "Salt".getBytes(), 65536, 256);
        SecretKey dtmp = dfactory.generateSecret(dspec);
        SecretKey dsecret = new SecretKeySpec(dtmp.getEncoded(), "AES");

        // Initialize dcipher
        Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher.init(Cipher.DECRYPT_MODE, dsecret, new IvParameterSpec(newivtext));

        FileInputStream inputStream = new FileInputStream(sdCard.getAbsolutePath()+"/Notes/sample.encrypted");
        CipherInputStream cis = new CipherInputStream(inputStream,dcipher);
        FileOutputStream os = new FileOutputStream(sdCard.getAbsolutePath() + "/Notes/sample.decrypted");

        int b = cis.read(dBytes);
        while(b != -1) {
            Log.d("Decryption","Bytes decrypted" + b);
            os.write(dBytes, 0, b);
            b = cis.read(dBytes);
        }
        cis.close();
        os.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
