public void encryptFile(String password, String filePath) {
    byte[] encryptedFileData = null;
    byte[] fileData = null;

    try {
        fileData = readFile(filePath);//method provided below

        // 64 bit salt for testing only
        byte[] salt = "goodsalt".getBytes("UTF-8");
        SecretKey key = generateKey(password.toCharArray(), salt);//method provided below

        byte[] keyData = key.getEncoded();
        SecretKeySpec sKeySpec = new SecretKeySpec(keyData, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);

        encryptedFileData = cipher.doFinal(fileData);

        saveData(encryptedFileData, filePath);//method provided below
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
