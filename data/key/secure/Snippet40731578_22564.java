void verifySignature(String[] pieces, String algorithm) 
    throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

    Mac hmac = Mac.getInstance(algorithm);
    hmac.init(new SecretKeySpec(decoder.decodeBase64(secret), algorithm));
    byte[] sig = hmac.doFinal(
        new StringBuilder(pieces[0]).append(".").append(pieces[1]).toString().getBytes());

    if (!Arrays.equals(sig, decoder.decodeBase64(pieces[2]))) {
        throw new SignatureException("signature verification failed");
    }
}
