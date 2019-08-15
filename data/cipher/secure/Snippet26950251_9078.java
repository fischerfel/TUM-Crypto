public void decryptFile(String password, String filePath) {
    byte[] decryptedFileData = null;
    byte[] fileData = null;

    try {
        fileData = readFile(filePath);

        byte[] salt = "goodsalt".getBytes("UTF-8");//generateSalt();
        SecretKey key = generateKey(password.toCharArray(), salt);

        byte[] keyData = key.getEncoded();
        SecretKeySpec sKeySpec = new SecretKeySpec(keyData, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec);

        decryptedFileData = cipher.doFinal(fileData);

        saveData(decryptedFileData, filePath);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
