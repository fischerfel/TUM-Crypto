public byte[] encTripleDes (String txt, byte [] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException{
    DESedeKeySpec keySpec = new DESedeKeySpec(key);
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
    SecretKey ky = keyfactory.generateSecret(keySpec);

    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, ky);
    return cipher.doFinal(txt.getBytes("UTF-8"));

}
