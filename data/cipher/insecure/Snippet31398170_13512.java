public String encrypt (String str,String key) throws EncryptException {

    try{
        javax.crypto.spec.SecretKeySpec keyspec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "AES");
        javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
        c.init(javax.crypto.Cipher.ENCRYPT_MODE, keyspec);
        byte[] src = str.getBytes("UTF-8");
        byte[] encrypt = c.doFinal(src);
        return new sun.misc.BASE64Encoder().encode(encrypt).replaceAll("\r|\n", "");
    }catch(Exception e){
        throw new EncryptException("Encrypt failed.",e);
    }
}
