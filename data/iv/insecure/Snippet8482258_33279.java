public static String decrypt(byte[] b) throws Exception{

    byte[] key = "12345678".getBytes("UTF-16");
    byte[] iv ="0443".getBytes("UTF-16");
    System.out.println("Length of iv" + iv.length + "key length.." + key.length);
    SecretKey secretKey = new SecretKeySpec(key, "RC2");
    System.out.println("Key size" + secretKey.getEncoded().length);
    Cipher cipher = Cipher.getInstance("RC2/CBC/NoPadding");
    IvParameterSpec initialisationVector = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, initialisationVector);
     byte[] cipherText = cipher.doFinal(b);
        String plainText = new String(cipherText, "UTF-8");
        System.out.println("Decrypted Text :: " + plainText);

    return "";
}
