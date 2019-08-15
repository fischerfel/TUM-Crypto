public static byte[] decryptPassword(byte[] result) throws GeneralSecurityException {
    byte constant = result[0];
    if (constant != 5) {
        throw new IllegalArgumentException();
    }

    byte[] secretKey = new byte[8];
    System.arraycopy(result, 1, secretKey, 0, 8);

    byte[] encryptedPassword = new byte[result.length - 9];
    System.arraycopy(result, 9, encryptedPassword, 0, encryptedPassword.length);

    byte[] iv = new byte[8];
    for (int i = 0; i < iv.length; i++) {
        iv[i] = 0;
    }

    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, "DES"), new IvParameterSpec(iv));
    return cipher.doFinal(encryptedPassword);
}
