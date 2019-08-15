    public static String RSAPublicEncryptuion(String text){
    try {
        String pubKeyFile = "";
        byte[] keyBytes = new byte[pubKeyFile.length()];
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String textoEncryptado = new String(cipher.doFinal(text.getBytes()));
        return textoEncryptado;
    } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException ex) {
        Logger.getLogger(RSAEncrypt.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException | BadPaddingException ex) {
        Logger.getLogger(RSAEncrypt.class.getName()).log(Level.SEVERE, null, ex);
    }
   return "";
}
