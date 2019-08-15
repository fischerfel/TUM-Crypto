    static public byte[] dec(byte data[]){

    byte[] ret = null;

    SecretKeyFactory keyFac;

    keyFac = SecretKeyFactory.getInstance("DESede");

    DESedeKeySpec keySpec = new DESedeKeySpec(passwd.getBytes());
    SecretKey secKey = keyFac.generateSecret(keySpec);
    Cipher cipher = Cipher.getInstance("DESede");
    cipher.init(Cipher.DECRYPT_MODE, secKey);

    ret = cipher.doFinal(data);
    return ret;
}
