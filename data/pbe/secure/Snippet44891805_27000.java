public static String encrypt(String str) {
    try {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedText = cipher.doFinal(str.getBytes("UTF-8"));

        // concatenate salt + iv + ciphertext
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(salt);
        outputStream.write(iv);
        outputStream.write(encryptedText);

        // properly encode the complete ciphertext
        return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public static String decrypt(String str) {
    try {
        byte[] ciphertext = DatatypeConverter.parseBase64Binary(str);
        if (ciphertext.length < 48) {
            return null;
        }
        byte[] salt = Arrays.copyOfRange(ciphertext, 0, 16);
        byte[] iv = Arrays.copyOfRange(ciphertext, 16, 32);
        byte[] ct = Arrays.copyOfRange(ciphertext, 32, ciphertext.length);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        byte[] plaintext = cipher.doFinal(ct);

        return new String(plaintext, "UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
