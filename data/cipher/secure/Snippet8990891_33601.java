public static String encrypt(String input, String key){

    try {

        byte[] ivBytes = new byte[16];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(ivBytes);

        IvParameterSpec ips = new IvParameterSpec(ivBytes);
        byte[] keybytes = md5(key);//This isn't final. Don't worry ;)
        byte[] crypted = null;
        SecretKeySpec skey = new SecretKeySpec(keybytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey, ips);
        byte[] ptext = input.getBytes("UTF-8");
        crypted = cipher.doFinal(ptext);

        return Base64.encodeBase64String(ivBytes)+Base64.encodeBase64String(crypted);
    }catch(Exception e){
        e.printStackTrace();
    }
    return null;
}

public static String[] decrypt(String input, String key){

    String iv = input.substring(0, 24);
    String encrypted = input.substring(24);
    try {
        IvParameterSpec ips = new IvParameterSpec(Base64.decodeBase64(iv));
        byte[] keybytes = md5(key);//This isn't final. Don't worry ;)
        byte[] output = null;
        SecretKeySpec skey = new SecretKeySpec(keybytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey, ips);
        output = cipher.doFinal(Base64.decodeBase64(encrypted));
        if(output==null){
            throw new Exception();
        }

        return new String[]{new String(output),iv};
    }catch(Exception e){
        e.printStackTrace();
    }
}
