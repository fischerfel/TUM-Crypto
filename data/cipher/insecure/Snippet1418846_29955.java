public static byte[] encryptDES(byte[] message, byte[] key) {
    byte[] encrypted = new byte[0];
    try{
        Cipher c = Cipher.getInstance("DES");
        c.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key,"DES"));
        encrypted = c.doFinal(message);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return encrypted;
}
