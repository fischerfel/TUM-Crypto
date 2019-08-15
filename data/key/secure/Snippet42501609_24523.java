    private static void startCrypting(int cipherMode, String key, File inputFile,
                             File outputFile) throws MediaCodec.CryptoException {
    try {
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        CipherOutputStream out = new CipherOutputStream(outputStream, cipher);
        byte[] buffer = new byte[8192];
        int count;
        while ((count = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }

        out.flush();
        out.close();
        outputStream.close();
        inputStream.close();

    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException ex) {
        ex.printStackTrace();
    }
}
