public static String decrypt2(final String sEncryptedMessageBase64,
                              final String sSymKeyHex,
                              final String sIvHex)
{
    final byte[] byteEncryptedMessage = Base64.decode(sEncryptedMessageBase64, Base64.DEFAULT);

    try
    {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final SecretKeySpec symKey = new SecretKeySpec(byteSymKeyData, "AES");
        final IvParameterSpec iv = new IvParameterSpec(byteIvData);

        cipher.init(Cipher.DECRYPT_MODE, symKey, iv);
        final byte[] encodedMessage = cipher.doFinal(byteEncryptedMessage);
        final String message = new String(encodedMessage, Charset.forName("UTF-8"));

        return message;
    }
    catch (GeneralSecurityException e) {

        Log.e("%%%%%", e.getMessage());

        throw new IllegalStateException(
                "Unexpected exception during decryption", e);
    }
}
