public static String decryptAES(String SerializedSystemIni, String ciphertext) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {

    byte[] encryptedPassword1 = new BASE64Decoder().decodeBuffer(ciphertext);
    byte[] salt = null;
    byte[] encryptionKey = null;

    String key = "0xccb97558940b82637c8bec3c770f86fa3a391a56"; //weblogic default key

    char password[] = new char[key.length()];

    key.getChars(0, password.length, password, 0);

    FileInputStream is = new FileInputStream(SerializedSystemIni);
    try {
        salt = readBytes(is);

        int version = is.read();
        if (version != -1) {
            encryptionKey = readBytes(is);
            if (version >= 2) {
                encryptionKey = readBytes(is);
            }
        }
    } catch (IOException e) {

    }

    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWITHSHAAND128BITRC2-CBC");

    PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, 5);

    SecretKey secretKey = keyFactory.generateSecret(pbeKeySpec);

    PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 0);

    Cipher cipher = Cipher.getInstance("PBEWITHSHAAND128BITRC2-CBC");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);
    SecretKeySpec secretKeySpec = new SecretKeySpec(cipher.doFinal(encryptionKey), "AES");

    byte[] iv = new byte[16];
    System.arraycopy(encryptedPassword1, 0, iv, 0, 16);
    byte[] encryptedPassword2 = new byte[16];
    System.arraycopy(encryptedPassword1, 16, encryptedPassword2, 0, 16);

    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    Cipher outCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    outCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

    byte[] cleartext = outCipher.doFinal(encryptedPassword2);

    return new String(cleartext, "UTF-8");

}
