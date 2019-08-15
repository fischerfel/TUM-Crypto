private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
private static final int KEY_SIZE = 256;
private static String UTF_8 = "UTF-8";

public static void main(final String[] args) throws Exception {
    System.out.println(decryptAuthorizationString(
            "eGlhV2xNbmdqSFBkbEhkZDNpZ3gwQT09", "CB=Z8#P@0!N2/8$%3K-9C(5S9*FDH+0Z"));
}

private static String decryptAuthorizationString(final String authString,
                                                 final String password) throws UnsupportedEncodingException {
    try {
        // --- check if AES-256 is available
        if (Cipher.getMaxAllowedKeyLength(AES_CBC_PKCS5PADDING) < KEY_SIZE) {
            throw new IllegalStateException("Unlimited crypto files not present in this JRE");
        }

        // --- create cipher
        final Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);

        // --- create the key and initial vector bytes
        final byte[] passwordEncoded = password.getBytes(UTF_8);
        final byte[] ivBytes = "brasilshopsoft07".getBytes(UTF_8);

        // --- init cipher
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(passwordEncoded, "AES"),
                new IvParameterSpec(ivBytes));

        // --- decode & decrypt authentication string
        final byte[] authBytes = Base64.decode(authString);
        final byte[] authBytes2 = Base64.decode(authBytes);
        final byte[] decryptedData = cipher.doFinal(authBytes2);

        // WARNING: may still decrypt to wrong string if
        // authString or password are incorrect -
        // BadPaddingException may *not* be thrown
        return new String(decryptedData, UTF_8);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
        // failure to authenticate
        return null;
    } catch (final GeneralSecurityException e) {
        throw new IllegalStateException(
                "Algorithms or unlimited crypto files not available", e);
    }
}