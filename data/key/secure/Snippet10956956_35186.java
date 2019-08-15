static String RSAEncrypt(String pubkey, String plain){
    return encrypt(pubkey,plain,"RSA");
}
static String encrypt(String stringKey, String plain, String algo){
    String enc="failed";
    try{
    byte[] byteKey  = new BASE64Decoder().decodeBuffer(stringKey);
    Key key = new SecretKeySpec(byteKey,algo);

    byte[] data = plain.getBytes();
    Cipher c = Cipher.getInstance(algo);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(data);
    enc = new BASE64Encoder().encode(encVal);
    }catch(Exception e){e.printStackTrace();}

    return enc;
}
