public static byte[] readFileBytes(String filename) throws IOException
{
    Path path = Paths.get(filename);
    return Files.readAllBytes(path);        
}
  public static PublicKey readPublicKey(String filename) throws     IOException,NoSuchAlgorithmException,InvalidKeySpecException
{
    X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");


    return keyFactory.generatePublic(publicSpec);
}
public static PrivateKey readPrivateKey(String filename) throws IOException,NoSuchAlgorithmException,InvalidKeySpecException
{
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
}
public static byte[] encrypt(PublicKey key,byte[] plaintext) throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeySpecException,IllegalBlockSizeException,BadPaddingException, InvalidKeyException
{
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
    cipher.init(Cipher.ENCRYPT_MODE,key);
    return cipher.doFinal(plaintext);
}
public static byte[] decrypt(PrivateKey key,byte[] ciphertext)throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
    cipher.init(Cipher.DECRYPT_MODE,key);
    return cipher.doFinal(ciphertext);

}
