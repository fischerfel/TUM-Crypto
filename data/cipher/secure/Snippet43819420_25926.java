public static String Decrypt(String privateKeyFilename, String encryptedData) throws IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IOException, NoSuchProviderException
{
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

      String filecontent = readFileAsString(privateKeyFilename);
      if (filecontent != null && !filecontent.trim().isEmpty()) {
         filecontent = filecontent.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", ""); 
    }

    System.out.println(filecontent);

    byte[] modulusBytes = Base64.decode(filecontent);
    byte[] exponentBytes = Base64.decode("OAEP");
    BigInteger modulus = new BigInteger(1, modulusBytes );
    BigInteger exponent = new BigInteger(1, exponentBytes);

    RSAPrivateKeySpec rsaPrivKey = new RSAPrivateKeySpec(modulus, exponent);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey privKey = fact.generatePrivate(rsaPrivKey);

    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding","BC");
    cipher.init(Cipher.DECRYPT_MODE, privKey);

    byte[] base64String = Base64.decode(encryptedData);
    byte[] plainBytes = new String(base64String).getBytes("UTF-8");
    byte[] cipherData = cipher.doFinal(plainBytes);

    System.out.println(cipherData);
    return cipherData.toString();
}
