public Decrypt(String path, String pathcode) {
        // TODO Auto-generated constructor stub
        filepath = path;
        try {
            fis = new FileInputStream(new File(path));
            this.passcode = pathcode;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static String decrypt() throws IOException, NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException {

        SecretKeySpec sks = new SecretKeySpec(passcode.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int size = fis.available();
        byte[] resdata = new byte[size];
        cis.read(resdata, 0, size);
        String newres = new String(resdata, "UTF-8").trim();
        //write("decrypted_file.xhtml",newres);  
        if(fis!=null)
        {
        fis.close();
        }
        if(cis!=null)
            cis.close();
        return newres;
    }
