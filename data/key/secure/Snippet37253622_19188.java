 public String decrypt(byte key[], String encrypted)
            throws GeneralSecurityException {
        if (key.length != 32 || key.length != 48 || key.length != 64) {
            throw new IllegalArgumentException("Invalid key size.");
        }
    byte[] ciphertextBytes = Base64.decodeBase64(encrypted.getBytes());

    // Need to find the IV length here. I am using 16 here

    IvParameterSpec iv = new IvParameterSpec(ciphertextBytes, 0, 16);
    ciphertextBytes = Arrays.copyOfRange(ciphertextBytes, 16,
            ciphertextBytes.length);

    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
    byte[] original = cipher.doFinal(ciphertextBytes);

    // remove zero bytes at the end
    int lastLength = original.length;
    for (int i = original.length - 1; i > original.length - 16; i--) {
        if (original[i] == (byte) 0) {
            lastLength--;
        } else {
            break;
        }
    }

    return new String(original, 0, lastLength);
}
