public static byte[] encryptAES(byte[] toEncrypt, byte[] key,
                                boolean encrypte) throws Exception {

    Security.addProvider(new BouncyCastleProvider());

    byte[] iv = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                  (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                  (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                  (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

    IvParameterSpec salt = new IvParameterSpec(iv);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

    if (encrypte == false)
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),  salt);
    else
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"),  salt);

    byte[] result = cipher.doFinal(toEncrypt);
    return result;
}
