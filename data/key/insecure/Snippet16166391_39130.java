      public static String decrypt1(Object data, byte[] ivBytes) throws InvalidKeyException,
        InvalidAlgorithmParameterException, IllegalBlockSizeException,
        BadPaddingException, UnsupportedEncodingException {
            byte[] keyBytes = "keyPhrase".getBytes();
            Cipher cipher = null;
            if (ivBytes.length<16) {
                System.out.println("error" + ivBytes.length); 
                //ivBytes = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 97, 98, 99,  100, 101, 102, 103};
            }
        byte[] byteArr = null;
       try {
          SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
          cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
          cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(
                ivBytes));
           if (data instanceof String) {
            byteArr = Base64.decodeBase64(((String) data).getBytes("UTF-8"));
        } 
        byteArr = (cipher.doFinal(byteArr));
    } catch (Exception e) {
        e.printStackTrace();
    }
    //return byteArr;
     return new String(byteArr);
}
