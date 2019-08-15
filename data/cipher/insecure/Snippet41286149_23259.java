public void encrypt(File sourceFile, File targetFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
    try(InputStream inputStream = new FileInputStream(sourceFile)){
        try(OutputStream outputStream = new FileOutputStream(targetFile)){
            byte[] chunk = new byte[8192];
            int chunkLen = 0;
            while ((chunkLen = inputStream.read(chunk)) != -1) {
                byte[] encrytedBytes = cipher.update(chunk);
                outputStream.write(encrytedBytes);
            }
            byte[] finalBytes =  cipher.doFinal();
            if(finalBytes!=null) {
                outputStream.write(finalBytes);
            }
        }
    }
}

public void decrypt(File encryptedFile, File targetFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
    try(InputStream inputStream = new FileInputStream(encryptedFile)){
        try(OutputStream outputStream = new FileOutputStream(targetFile)){
            byte[] chunk = new byte[8192];
            int chunkLen = 0;
            while ((chunkLen = inputStream.read(chunk)) != -1) {
                byte[] decrytedBytes = cipher.update(chunk);
                outputStream.write(decrytedBytes);
            }
            byte[] finalBytes =  cipher.doFinal();
            if(finalBytes!=null) {
                outputStream.write(finalBytes);
            }
        }
    }
}
