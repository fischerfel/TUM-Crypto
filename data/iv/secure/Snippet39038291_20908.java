public static byte[] decode(byte[] decrypteSrcBuffer) throws Exception {
    Key deskey = null;
    DESedeKeySpec spec = new DESedeKeySpec(mKeyBytes);
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
    deskey = keyfactory.generateSecret(spec);
    Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
    IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
    cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
    byte[] decryptData = cipher.doFinal(decrypteSrcBuffer);

    return decryptData;
}
