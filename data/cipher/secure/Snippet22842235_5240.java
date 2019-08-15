public static String decryptRSA(Context mContext, byte[] message) throws Exception { 


    InputStream in = mContext.getResources().openRawResource(R.raw.publicrsakey);
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(org.apache.commons.io.IOUtils.toByteArray(in));

    PublicKey publicKey = 
            KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    final String encryptedString = Base64.encode(cipher.doFinal(message));

    return encryptedString;


}   
