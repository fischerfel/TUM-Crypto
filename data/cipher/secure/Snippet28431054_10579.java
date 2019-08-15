public static String encrypt(String salt, String password, byte[] object) throws GeneralSecurityException {
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt.getBytes(), 1000);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());

        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        Cipher encryptionCipher = Cipher.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        byte[] encryptedObject = encryptionCipher.doFinal(object);

    return new String(encryptedObject);
}

public static String decrypt(String encryptedObject, String password, String salt) throws GeneralSecurityException{
    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), 1000);
    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());

        SecretKeyFactory keyFactory
                = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
        SecretKey passwordKey = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
        cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);

        byte[] decryptedObject = cipher.doFinal(encryptedObject.getBytes());

    return new String(decryptedObject);
}
