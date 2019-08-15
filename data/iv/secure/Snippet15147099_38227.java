public MyCipher() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
    Security.addProvider(new BouncyCastleProvider());
    KeyGenerator keyGen = KeyGenerator.getInstance("DES", "BC");
    keyGen.init(new SecureRandom());
    SecretKey keySpec = keyGen.generateKey();

    this.sharedKey = new String( Base64.encodeBase64URLSafe( keySpec.getEncoded() ) );
    this.encrypter = Cipher.getInstance("DES/CBC/PKCS5Padding", "BC");
    this.encrypter.init(Cipher.ENCRYPT_MODE, keySpec);

    AlgorithmParameters params = this.encrypter.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    this.sharedIV = new String( Base64.encodeBase64URLSafe( iv ) );
    this.decrypter = Cipher.getInstance("DES/CBC/PKCS5Padding", "BC");
    this.decrypter.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
}
