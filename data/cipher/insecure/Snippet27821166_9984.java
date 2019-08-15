static void encrypt(File file, String pass) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {


        FileInputStream fis = new FileInputStream(file);


        FileOutputStream fos = new FileOutputStream(APPPATH+"/E_"+file.getName());


        SecretKeySpec sks = new SecretKeySpec(pass.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);

        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        int b;
        byte[] d = new byte[8];
        while((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }

        cos.flush();
        cos.close();
        fis.close();


    }



     static void decrypt(File file, String pass) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

            FileInputStream fis = new FileInputStream(file);

            FileOutputStream fos = new FileOutputStream(APPPATH+"/D_"+file.getName());
            SecretKeySpec sks = new SecretKeySpec(pass.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[8];
            while((b = cis.read(d)) != -1) {
                fos.write(d, 0, b);
            }
            fos.flush();
            fos.close();
            cis.close();
        }
