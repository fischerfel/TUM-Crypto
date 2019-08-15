try {
    byte[] keyBytes = Base64.decode(Constants.RSA_PUBLIC_KEY.getBytes(), Base64.DEFAULT);
    X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(encodedKeySpec) ;
    Cipher cipher = Cipher.getInstance("RSA") ;
    cipher.init(Cipher.DECRYPT_MODE, publicKey) ;
    //
    Log.e(DEBUG_TAG, jwt) ; // received token
    String payload = new String(Base64.decode(jwt, Base64.DEFAULT), "UTF-8") ; // java does UTF16, elixir does UTF8
    Log.e(DEBUG_TAG, payload) ; // base64 decoded token
    byte[] cipherText = cipher.doFinal(payload.getBytes("UTF-8")) ; // decrypt
    String token = new String(Base64.decode(cipherText, Base64.URL_SAFE), "UTF-8") ; // cipher text is urlencoded
    Log.e(DEBUG_TAG, token) ;
    return null ;
} catch (Exception e) {
    e.printStackTrace();
}
