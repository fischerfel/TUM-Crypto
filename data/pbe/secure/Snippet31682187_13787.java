private void generateSK(char[] passPhrase, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
    pbeParamSpecKey = new PBEParameterSpec(salt,1000);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(passPhrase);
    SecretKeyFactory secretKeyFactory;

    secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
    secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
}
