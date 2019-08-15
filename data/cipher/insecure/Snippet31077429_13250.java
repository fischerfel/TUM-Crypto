    public String decrypt(final String encrypted) {
    try {
        final Cipher blowfishECB = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        final SecretKeySpec blowfishKey = new SecretKeySpec(DECRYPT_PASSWORD.getBytes("UTF8"), "Blowfish");
        blowfishECB.init(Cipher.DECRYPT_MODE, blowfishKey);
        final byte[] decryptedBytes = blowfishECB.doFinal(decodeHex(encrypted.toCharArray()));

        // First 4 bytes are garbage according to specification (deletes first 4 bytes)
        final byte[] trimGarbage = Arrays.copyOfRange(decryptedBytes, 4, decryptedBytes.length);
        return new String(trimGarbage);
    } catch (final Exception e) {
        Log.e(TAG, "Failed to decrypt content", e);
        return null;
    }
}
