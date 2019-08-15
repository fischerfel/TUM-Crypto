public static PrivateKey getPrivateKey(byte[]   encryptedPkcs8, String passwd) throws Exception{

        EncryptedPrivateKeyInfo encryptPKInfo = new EncryptedPrivateKeyInfo(encryptedPkcs8);

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
