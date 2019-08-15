public static String decrypt() throws Exception{
    try{
        String Base64EncodedText = "iz1qFlQJfs6Ycp+gcc2z4w==";
        String decodedText = com.sun.xml.internal.messaging.saaj.util.Base64.base64Decode(Base64EncodedText);
        String key = "1234567812345678";
        String iv = "1234567812345678";

        javax.crypto.spec.SecretKeySpec keyspec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "AES");
        javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv.getBytes());

        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] decrypted = cipher.doFinal(decodedText.getBytes());

        String str = new String(decrypted);

        return str;

    }catch(Exception e){
        return null;
    }   
}
