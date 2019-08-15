FileInputStream fis = new FileInputStream(new File("D:/Shashank/Test123.txt"));
        File outfile = new File("D:/Shashank/encTest1234.txt");
        int read;
        if(!outfile.exists())
            outfile.createNewFile();
        File decfile = new File("D:/Shashank/dec123.txt");
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
