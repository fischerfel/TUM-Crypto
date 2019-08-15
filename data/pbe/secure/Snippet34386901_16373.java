public static PrivateKey getPrivateKey(String passwd){
    try {

        byte[] encodedPrivateKey = getFileBytes(PRIVATE_KEY_FILE);

        // exception thrown from here
        EncryptedPrivateKeyInfo encryptPKInfo = new EncryptedPrivateKeyInfo(encodedPrivateKey);

        Cipher cipher = Cipher.getInstance(encryptPKInfo.getAlgName());
        PBEKeySpec pbeKeySpec = new PBEKeySpec(passwd.toCharArray());
        SecretKeyFactory secFac = SecretKeyFactory.getInstance(encryptPKInfo.getAlgName());
        Key pbeKey = secFac.generateSecret(pbeKeySpec);
        AlgorithmParameters algParams = encryptPKInfo.getAlgParameters();
        cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);
        KeySpec pkcs8KeySpec = encryptPKInfo.getKeySpec(cipher);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        return kf.generatePrivate(pkcs8KeySpec);
    }
    catch (Exception e){
        e.printStackTrace();
        return null;
    }
}
