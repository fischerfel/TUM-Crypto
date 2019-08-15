public static void decrypt() throws Exception{
    byte[] modulusBytes = Base64.getDecoder().decode(mod);
    byte[] dByte = Base64.getDecoder().decode(d);

    BigInteger modulus = new BigInteger(1, (modulusBytes));
    BigInteger exponent = new BigInteger(1, (dByte));

    RSAPrivateKeySpec rsaPrivKey = new RSAPrivateKeySpec(modulus, exponent);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey privKey = fact.generatePrivate(rsaPrivKey);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privKey);

    byte[] cipherData = Base64.getDecoder().decode(cipherByte);
    byte[] plainBytes = cipher.doFinal(cipherData);


    System.out.println(new String(plainBytes));
} 
