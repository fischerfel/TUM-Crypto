public static byte[] encrypt(SecretKey secret, byte[] buffer) throws GeneralSecurityException
{
    /* Encrypt the message. */
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

    SecureRandom rng = new SecureRandom();
    byte[] ivData = new byte[cipher.getBlockSize()];
    rng.nextBytes(ivData);

    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivData));
    byte[] ciphertext = cipher.doFinal(buffer);

    return Arrays.concatenate(ivData, ciphertext);
}

public static byte[] decrypt(SecretKey secret, byte[] buffer) throws GeneralSecurityException
{
    /* Decrypt the message. - use cipher instance created at encrypt */
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

    int n = cipher.getBlockSize();
    byte[] ivData = Arrays.copyOf(buffer, n);

    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivData));
    byte[] clear = cipher.doFinal(buffer, n, buffer.length - n);

    return clear;
}
