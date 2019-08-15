    public static String RSAPublicEncryptuion(String text){
    DataInputStream dis = null;
    try {
        File pubKeyFile = new File("public_key.der");
        dis = new DataInputStream(new FileInputStream(pubKeyFile));
        byte[] keyBytes = new byte[(int) pubKeyFile.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String textoEncryptado = new String(cipher.doFinal(text.getBytes()), "UTF-8");
        return textoEncryptado;
    } catch (FileNotFoundException ex) {
        Logger.getLogger(RSAEncrypt.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException ex) {
        Logger.getLogger(RSAEncrypt.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException | BadPaddingException ex) {
        Logger.getLogger(RSAEncrypt.class.getName()).log(Level.SEVERE, null, ex);
    }
   return "Error";
}
