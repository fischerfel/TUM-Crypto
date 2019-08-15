public static String decrypt(String message){
    try {
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(secrKey.getBytes(), "AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(message.getBytes(), Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        String decoded = new String(Base64.decode(decryptedValue, Base64.DEFAULT));
        return decoded;
    }catch(Exception e){
        return null;
    }
}
