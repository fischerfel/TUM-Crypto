public static void main(String[] args) throws DecoderException, InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
    //Hex encoding/decoding done with Apache Codec library.
    //key MUST BE 16, 24 or 32 random bytes.
    //Do not reuse this key! Create your own.
    byte[] key = Hex.decodeHex("a3134dfd51c30f6e25343d861320668e".toCharArray());
    String text = "This is some test text.";

    byte[] encrypted = encrypt(key, text.getBytes());
    byte[] decrypted = decrypt(key, encrypted);

    System.out.println("Text: " + text);
    System.out.println("Encrypted: " + Hex.encodeHexString(encrypted));
    System.out.println("Decrypted: " + new String(decrypted));
}

public static byte[] encrypt(byte[] key, byte[] unencrypted) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException{
    //Create an initialization vector
    //SecureRandom is not available on J2ME, so we use Bouncy Castle's DigestRandomGenerator instead.
    DigestRandomGenerator rnd = new DigestRandomGenerator(new SHA1Digest());
    //SecureRandom rnd = new SecureRandom();
    byte[] iv = new byte[16];
    rnd.nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    //Set up the cipher and encrypt
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivSpec);
    byte[] encrypted = cipher.doFinal(unencrypted);

    //Append the encrypted text to the IV
    byte[] output = new byte[iv.length + encrypted.length];
    System.arraycopy(iv, 0, output, 0, iv.length);
    System.arraycopy(encrypted, 0, output, iv.length, encrypted.length);
    return output;
}

public static byte[] decrypt(byte[] key, byte[] encrypted) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException{
    //Separate the IV and encrypted text
    byte[] iv = new byte[16];
    byte[] encryptedText = new byte[encrypted.length - iv.length];
    System.arraycopy(encrypted, 0, iv, 0, iv.length);
    System.arraycopy(encrypted, iv.length, encryptedText, 0, encryptedText.length);

    //Decrypt the encrypted text
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), ivSpec);
    byte[] decrypted = cipher.doFinal(encryptedText);

    return decrypted;
}
