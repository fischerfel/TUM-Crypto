                    try
                {
                    encrypt_text.setText(decrypt(filecopy1,text_cipher));

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), e.toString(),Toast.LENGTH_LONG).show();
                    System.out.println("Exception is>>"+e);
                }

    public static String decrypt(String filePath1, String encrypted) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    InputStream stream = new FileInputStream(filePath1);
    byte[] encodedKey1 = new byte[stream.available()];
    stream.read(encodedKey1);
    X509EncodedKeySpec publicKeySpec1 = new X509EncodedKeySpec(encodedKey1);
    KeyFactory kf1 = KeyFactory.getInstance("RSA");
    PublicKey pkPublic1 = kf1.generatePublic(publicKeySpec1);

    Cipher pkCipher1 = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    pkCipher1.init(Cipher.DECRYPT_MODE, pkPublic1);

    byte[] decoded = Base64Coder.decode(encrypted);
    byte[] decryptedInByte = pkCipher1.doFinal(decoded);
    return new String(decryptedInByte);
    // return encrypted;
}
