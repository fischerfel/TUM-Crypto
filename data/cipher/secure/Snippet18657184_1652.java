public String encrypt_rsa(String original) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    Resources res = getResources();
    InputStream is = res.openRawResource(R.raw.public_key);
    byte[] encodedKey = new byte[is.available()];
    is.read(encodedKey);
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pkPublic = kf.generatePublic(publicKeySpec);

    Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    pkCipher.init(Cipher.ENCRYPT_MODE, pkPublic);
    byte[] encryptedInByte = pkCipher.doFinal(original.getBytes());
    String encryptedInString = new String(Base64Coder.encode(encryptedInByte));
    is.close();

    return encryptedInString;
}
