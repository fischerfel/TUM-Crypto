public static byte[] encrypt(byte[] data, String pubKey64) {

    try {
         byte[] key = Toolkit.base64Decode(pubKey64);
         KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
         RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
         System.out.println("MODE:"+Cipher.ENCRYPT_MODE);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, pbk);

        byte[] encDate = cipher.doFinal(data);
        return encDate;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
