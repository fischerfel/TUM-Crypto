public void decryptAES() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException{
        FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/encrypted.txt");

        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/decrypted.txt");
        SecretKeySpec sks = new SecretKeySpec("your_key".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC");
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
