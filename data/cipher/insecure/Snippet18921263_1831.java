public static String encrypt(String input, String key){
    byte[] crypted = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey);
        crypted = cipher.doFinal(input.getBytes());
    } catch(Exception e) {
        //Log.e("ERR", e.toString());
    }
    return new String(Base64.encode(crypted, Base64.DEFAULT));
}

public static String decrypt(String input, String key){
    byte[] output = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
    } catch(Exception e) {
    //Log.e("ERR", e.toString());
    }
    return new String(output);
}
