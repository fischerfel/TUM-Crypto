public static String encrypt() throws Exception{
try{
    String data = "Test string";
    String key = "1234567812345678";
    String iv = "1234567812345678";

    javax.crypto.spec.SecretKeySpec keyspec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "AES");
    javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv.getBytes());

    javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keyspec, ivspec);
    byte[] encrypted = cipher.doFinal(data.getBytes());

    return new sun.misc.BASE64Encoder().encode(encrypted);

}catch(Exception e){
    return null;
}
