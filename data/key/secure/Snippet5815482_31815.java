public byte[] generateHMac(String secretKey, String data, String algorithm /* e.g. "HmacSHA256" */) {

    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), algorithm);

    try {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(signingKey);

        return mac.doFinal(data.getBytes());
    }
    catch(InvalidKeyException e) {
        throw new IllegalArgumentException("invalid secret key provided (key not printed for security reasons!)");
    }
    catch(NoSuchAlgorithmException e) {
        throw new IllegalStateException("the system doesn't support algorithm " + algorithm, e);
    }
}
