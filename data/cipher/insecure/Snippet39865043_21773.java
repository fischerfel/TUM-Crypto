public static String EncryptUrl(String parameters){

    try{
        String encodedStr = "";
        Cipher cipher;
        DESKeySpec keySpec = new DESKeySpec(key.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(keySpec));

        encodedStr = Base64.encodeBase64String(cipher.doFinal(parameters.getBytes("UTF8")));

        try{
            encodedStr = URLEncoder.encode(encodedStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            throw new AssertionError("UTF-8 is unknown");
        }

        return encodedStr;
    }
    catch(Exception ex){
        return null;
    }
}
