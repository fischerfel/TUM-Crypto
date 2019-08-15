public MyCipher() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
    Security.addProvider(new BouncyCastleProvider());
    KeyGenerator keyGen = KeyGenerator.getInstance("DESede", "BC");
    keyGen.init(new SecureRandom());
    SecretKey keySpec = keyGen.generateKey();

    this.sharedKey = keySpec.getEncoded().toString();
    this.encrypter = Cipher.getInstance("DESede/ECB/Nopadding", "BC");
    this.encrypter.init(Cipher.ENCRYPT_MODE, keySpec);
    this.decrypter = Cipher.getInstance("DESede/ECB/Nopadding", "BC");
    this.decrypter.init(Cipher.DECRYPT_MODE, keySpec);
}
