public void decrypt(String base64String) {
    String keyStorePath = "C:\Key.keystore";
    String storepass = "1234";
    String keypass = "abcd";
    byte[] data = Base64.decode(base64String);
    byte[] cipherData = null;

    keystore = KeyStore.getInstance("JKS");
    keystore.load(new FileInputStream(keyStorePath), storepass.toCharArray());

    RSAPrivateKey privateRSAKey = (RSAPrivateKey) keystore.getKey(alias, keypass.toCharArray());

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateRSAKey);
    cipherData = cipher.doFinal(data);

    System.out.println(new String(cipherData));
}
