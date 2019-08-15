private static byte[] hmac_sha1(String crypto, byte[] keyBytes, byte[] text) {
Mac hmac = null;

    try {
        hmac = Mac.getInstance(crypto);
        SecretKeySpec macKey =
            new SecretKeySpec(keyBytes, "RAW");
        hmac.init(macKey);
        System.out.println("hmac: "+Arrays.toString(keyBytes));
        return hmac.doFinal(text);
} catch (Exception e) {
    // NOTE. Deviation from reference code.
    // Reference code prints a stack trace here, which is not what we
    // want in a production environment, so instead we rethrow.
    throw new UndeclaredThrowableException(e);
    }

}
