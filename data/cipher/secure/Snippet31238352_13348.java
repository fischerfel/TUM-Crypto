private static final String salt = "SaltySalt";

private static byte [] ivBytes = null;

private static byte[] getSaltBytes() throws Exception {
    return salt.getBytes("UTF-8");
}

private static char[] getMasterPassword() {
    return "SuperSecretPassword".toCharArray();
}

private static byte[] getIvBytes() throws Exception {
    if (ivBytes == null) {
        //I don't have the parameters, so I'll generate a dummy encryption to create them
        encrpytString("test");
    }
    return ivBytes;
}

public static String encrpytString (String input) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(getMasterPassword(), getSaltBytes(), 65536,256);
    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    ivBytes = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
    byte[] encryptedTextBytes = cipher.doFinal(input.getBytes("UTF-8"));
    return DatatypeConverter.printBase64Binary(encryptedTextBytes);        
}

public static String decrpytString (String input) throws Exception {
    byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(input);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(getMasterPassword(), getSaltBytes(), 65536, 256);
    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(getIvBytes()));
    byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    return new String(decryptedTextBytes);
}
