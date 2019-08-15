private static void doCrypto(int cipherMode, String key, File inputFile,
        File outputFile) throws CryptoException {
    try {    
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(cipherMode, secretKey);         
        FileInputStream inputStream = new FileInputStream(inputFile);

        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);            
        inputStream.close();
        outputStream.close();

    } catch(NoSuchPaddingException|NoSuchAlgorithmException|InvalidKeyException | BadPaddingException| IllegalBlockSizeException | IOException ex) {
        throw new CryptoException("Error encrypting/decrypting file",ex);
    }
