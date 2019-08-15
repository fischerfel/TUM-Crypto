public static byte[] computeSignature(String algorithm, byte[] data, byte[] sharedSecret) {
    try {
        SecretKey secretKey = new SecretKeySpec(sharedSecret, algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKey);
        return Base64.encode(mac.doFinal(data));
    } catch (NoSuchAlgorithmException e) {
        // 
    } catch (InvalidKeyException e) {
        //
    }
}
