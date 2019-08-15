public static byte[] encrypt(SecretKey secret, byte[] buffer) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
{
    /* Encrypt the message. */
    cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    byte[] ciphertext = cipher.doFinal(buffer);

    return ciphertext;
}

public static byte[] decrypt(SecretKey secret, byte[] buffer) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
{
    /* Decrypt the message. - use cipher instance created at encrypt */
    cipher.init(Cipher.DECRYPT_MODE, secret);
    byte[] clear = cipher.doFinal(buffer);

    return clear;
}
