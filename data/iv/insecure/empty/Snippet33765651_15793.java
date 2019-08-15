private static String key = "012345678901234567890123";

public static void main(String[] args) throws Exception {
    String text = "test";
    String codedtext = encrypt(text);
    //String decodedtext = decrypt(codedtext);

    System.out.println(new String(codedtext));
    //System.out.println(decodedtext); 
}

public static String encrypt(String message) throws Exception {
    MessageDigest md = MessageDigest.getInstance("md5");
    byte[] digestOfPassword = md.digest(key.getBytes("unicode"));
    byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
    //for (int j = 0, k = 16; j < 8;) {
    //  keyBytes[k++] = keyBytes[j++];
    //}

    SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    byte[] plainTextBytes = message.getBytes();
    byte[] cipherText = cipher.doFinal(plainTextBytes);

    String output = Base64.encode(cipherText);

    return output;
}

public static String decrypt(String message) throws Exception {
    byte[] messageBytes = Base64.decode(message);

    MessageDigest md = MessageDigest.getInstance("md5");
    byte[] digestOfPassword = md.digest(key.getBytes());
    byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
    for (int j = 0, k = 16; j < 8;) {
        keyBytes[k++] = keyBytes[j++];
    }

    SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    decipher.init(Cipher.DECRYPT_MODE, key, iv);

    byte[] plainText = decipher.doFinal(messageBytes);

    return new String(plainText);
}
