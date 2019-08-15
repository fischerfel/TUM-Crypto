public static byte[] computeSignature(String algorithm, byte[] data, byte[] sharedSecret) {
    try {
        SecretKey secretKey = new SecretKeySpec(Base64.decode(sharedSecret), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKey);
        mac.update(data);
        return Base64.encode(mac.doFinal());
    } catch (NoSuchAlgorithmException e) {
        throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
    } catch (InvalidKeyException e) {
        throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
    }
}
