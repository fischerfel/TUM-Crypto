private static String Encrypt(String DecryptedText, String accountName, String apiKey) {
    byte[] EncryptedStr = {};

    if (accountName.length() < 5 || apiKey.length() < 5) {
        System.out.println("Name too short");
        return DatatypeConverter.printBase64Binary(EncryptedStr);
    }
    try {
        byte[] salt = apiKey.getBytes();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec KeySpec = new PBEKeySpec(accountName.toCharArray(), salt, 1000, 256);
        SecretKey key = factory.generateSecret(KeySpec);

        KeySpec VectorSpec = new PBEKeySpec(accountName.toCharArray(), salt, 1000, 128);
        SecretKey vector = (SecretKey) factory.generateSecret(VectorSpec);

        SecretKey secretKey = new SecretKeySpec(key.getEncoded(), "AES");

        byte[] ivKeyBytes = vector.getEncoded();
        byte[] iv = new byte[16]; // Get IV bytes from the
        for (int i = 0; i < iv.length; ++i)
                iv[i] = ivKeyBytes[i];
        IvParameterSpec ivParm = new IvParameterSpec(iv);

        Log("Salt:", false);
        Log(salt, true);
        Log("Key:", false);
        Log(secretKey.getEncoded(), true);
        Log("Vector:", false);
        Log(iv, true);

        // IN C# they are using KeySize=256, Mode=CBC, Padding=PKCS7,
        // PKCS7 padding doesn't exist in java so use PKCS5 and hope it works correctly.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // NOTE: Added ivParam to initialize the IV parameter, w/o the first block is off.
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParm);
        EncryptedStr = cipher.doFinal(DecryptedText.getBytes("UTF-8"));
    } catch (Exception e) {
        System.out.println("Exc:" + e.getMessage());
        e.printStackTrace(System.out);
    }

    return DatatypeConverter.printBase64Binary(EncryptedStr);
}
