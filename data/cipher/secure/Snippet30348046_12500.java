    public String decrypt(final String encryptedValue, final String secretKey) {

    String decryptedValue = null;

    try {

        final Key key = generateKeyFromString(secretKey);
        final Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        final byte[] decorVal = new BASE64Decoder().decodeBuffer(encryptedValue);
        final byte[] decValue = c.doFinal(decorVal);
        decryptedValue = new String(decValue);
    } catch(Exception ex) {
        System.out.println("The Exception is=" + ex);
    }

    return decryptedValue;
}
