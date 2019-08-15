public static void main(String[] args) throws DecoderException, InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
    //Hex encoding/decoding done with Apache Codec
    byte[] key = "abcdefghabcdefgh".getBytes();
    String text = "Password12345678";

    byte[] encrypted = encrypt(key, text.getBytes());
    byte[] decrypted = decrypt(key, encrypted);

    System.out.println("Text: " + text);
    System.out.println("Encrypted: " + Hex.encodeHexString(encrypted));
    System.out.println("Decrypted: " + new String(decrypted));
}

public static byte[] encrypt(byte[] key, byte[] unencrypted) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException{
    //Set up the cipher and encrypt
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
    byte[] encrypted = cipher.doFinal(unencrypted);

    return encrypted;
    }

public static byte[] decrypt(byte[] key, byte[] encrypted) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException{
    //Decrypt the encrypted text
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
    byte[] decrypted = cipher.doFinal(encrypted);

    return decrypted;
}
