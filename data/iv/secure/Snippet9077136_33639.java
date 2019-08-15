public static String[] decrypt(String input, String key){

    String iv = input.substring(0, 24);
    String encrypted = input.substring(24);
    try {
        IvParameterSpec ips = new IvParameterSpec(Base64.decodeBase64(iv));
        byte[] keybytes = md5(key);//This isn't final. Don't worry ;)
        byte[] output = null;
        SecretKeySpec skey = new SecretKeySpec(keybytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding"); //Change here
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
