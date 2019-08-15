FileInputStream fis = new FileInputStream(new File("D:/Shashank/inputVideo.avi"));
        File outfile = new File("D:/Shashank/encVideo.avi");
        int read;
        if(!outfile.exists())
            outfile.createNewFile();
        File decfile = new File("D:/Shashank/decVideo.avi");
        if(!decfile.exists())
            decfile.createNewFile();
        FileOutputStream fos = new FileOutputStream(outfile);
        FileInputStream encfis = new FileInputStream(outfile);
        FileOutputStream decfos = new FileOutputStream(decfile);
        Cipher encipher = Cipher.getInstance("AES");
        Cipher decipher = Cipher.getInstance("AES");
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //byte key[] = {0x00,0x32,0x22,0x11,0x00,0x00,0x00,0x00,0x00,0x23,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        SecretKey skey = kgen.generateKey();
        //Lgo
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
