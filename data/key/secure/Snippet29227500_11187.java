public String hashValue(String message) {
    byte[] hash = toHmacSHA256(message);
    String hashHexed = toHex(hash);
    return hashHexed;
}

private String toHex(byte[] value) {
    String hexed = String.format("%040x", new BigInteger(1, value));
    return hexed;
}

private byte[] toHmacSHA256(String value) {
    byte[] hash = null;
    try {
        SecretKey secretKey = new SecretKeySpec(PRIVATE_KEY.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        hash = mac.doFinal(value.getBytes("UTF-8"));

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return hash;
}
