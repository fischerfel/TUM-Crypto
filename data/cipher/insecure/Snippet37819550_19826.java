public static void encrypt(SecretKey secretKey, String filePath){
    try {
        // Here you read the cleartext.
        FileInputStream fis = new FileInputStream(filePath);
        // This stream write the encrypted text. This stream will be wrapped by another stream.
        FileOutputStream fos = new FileOutputStream(filePath);

        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }

        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();

    }catch(IOException e){
        e.printStackTrace();
    }catch (NoSuchAlgorithmException e){
        e.printStackTrace();
    }catch(NoSuchPaddingException e){
        e.printStackTrace();
    }catch(InvalidKeyException e){
        e.printStackTrace();
    }
}
