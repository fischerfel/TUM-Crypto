private byte[] DecryptFile(byte[] encryptedFileBuffer) {        

    final int iterationCount = 10;

    byte[] dataDecrypted = null;
    SecretKey secKey = null;
    try {
        byte[] salt = "salt1234".getBytes();
        String accessThingy = "Password";
        KeySpec keySpec = new PBEKeySpec(accessThingy.toCharArray(), salt, iterationCount);
        secKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);


        Cipher desCipher;
        // Create the cipher 
        desCipher = Cipher.getInstance(secKey.getAlgorithm());          
        desCipher.init(Cipher.DECRYPT_MODE, secKey,paramSpec);

        dataDecrypted = desCipher.doFinal(encrptedFileBuffer);

    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
    }

    return dataDecrypted;

}
