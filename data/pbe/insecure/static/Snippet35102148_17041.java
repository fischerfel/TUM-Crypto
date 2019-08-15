public  String encryptPass(){

    String username="stackoverflow";
    String password="stackoverflow";
    String salt="stackoverflow145";

    /* generate secretkey */
    PBEKeySpec keySpec=new PBEKeySpec(username.toCharArray(),salt.getBytes(),1000,256);
    SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    SecretKey key=keyFactory.generateSecret(keySpec);

    /* IV bytes 16 length */
    byte[] iv={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    IvParameterSpec ivSpec=new IvParameterSpec(iv);

    Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE,key,ivSpec);
    byte[] passBytes=cipher.doFinal(password.getBytes());
    String result=Base64.encodeToString(passBytes,Base64.NO_WRAP);
    return result;
}
