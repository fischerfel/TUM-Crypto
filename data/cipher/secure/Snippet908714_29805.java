public static byte[] encrypt(String toEncrypt) throws Exception{
    try{
        String plaintext = toEncrypt;
        String key = "01234567890abcde";
        String iv = "fedcba9876543210";

        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE,keyspec,ivspec);
        byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());

        return encrypted;
    } catch(Exception e){
        return null;            // Always must return something
    }
}
