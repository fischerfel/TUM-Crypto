public static String encrypt(String input, String key){
    IvParameterSpec ips = new IvParameterSpec("sixteenbyteslong".getBytes());
    try {
        key = md5(key);
    } catch (NoSuchAlgorithmException e1) {
        e1.printStackTrace();
    }
    byte[] crypted = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey, ips);
        crypted = cipher.doFinal(input.getBytes());
    }catch(Exception e){
        System.out.println(e.toString());
    }
    return new String(Base64.encodeBase64(crypted));
}

public static String decrypt(String input, String key){
    IvParameterSpec ips = new IvParameterSpec("sixteenbyteslong".getBytes());
    try {
        key = md5(key);
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    byte[] output = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey,ips);
        output = cipher.doFinal(Base64.decodeBase64(input));
    }catch(Exception e){
        System.out.println(e.toString());
    }
    return new String(output);
}
