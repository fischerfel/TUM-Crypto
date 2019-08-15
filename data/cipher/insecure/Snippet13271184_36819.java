public byte[] cryptograph(Key key, byte[] content){
    Cipher cipher;
    byte[] cryptograph = null;
    try {
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cryptograph = cipher.doFinal(content);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return cryptograph;

}
