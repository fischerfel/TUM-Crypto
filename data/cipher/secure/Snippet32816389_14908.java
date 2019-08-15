public static void decryptPublic() throws Exception{

    byte[] modulusBytes = Base64.getDecoder().decode(mod);
    byte[] expBytes = Base64.getDecoder().decode(exp);

    BigInteger modulus = new BigInteger(1, (modulusBytes));
    BigInteger exponent = new BigInteger(1, (expBytes));

    RSAPublicKeySpec pubKey = new RSAPublicKeySpec(modulus, exponent);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PublicKey publicKey = fact.generatePublic(pubKey);
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, publicKey );


    byte[] cipherData = Base64.getDecoder().decode(cipherByte);
    byte[] plainBytes = cipher.doFinal(cipherData);

    System.out.println(new String(plainBytes));
} 
