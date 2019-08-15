public static String Encrypt(String text){
    try {           
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= KEY.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        String result = Base64.encodeToString(results, 0);
        //result = URLEncoder.encode(result.trim(),"UTF-8");
        return result;

    } catch (Exception e) {
        // TODO: handle exception
        return null;
    }
}   

public static String Decrypt(String text){
    try {           
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= KEY.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        String result = Base64.encodeToString(results, 0);
        //result = URLEncoder.encode(result.trim(),"UTF-8");
        return result;

    } catch (Exception e) {
        // TODO: handle exception
        Log.d("decryption", e.getMessage());
        return null;
    }
}   
