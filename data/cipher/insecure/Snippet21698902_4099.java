public static final String getAES256(final String text){
    try{
        SecretKeySpec specKey = new SecretKeySpec(Hex.decode(MERCHANT_KEY), "AES");
        Cipher aesCipher =  Cipher.getInstance("AES/ECB/ZeroBytePadding");
        aesCipher.init(Cipher.ENCRYPT_MODE, specKey);
        byte[] result = aesCipher.doFinal(Hex.decode(text));
        return convToHex(result);
    }catch(Exception e){
        e.printStackTrace();
    }
    return null;
}
