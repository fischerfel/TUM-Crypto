private static final String HMAC_SHA512 = "HmacSHA512";

private static String toHexString(byte[] bytes) {
    Formatter formatter = new Formatter();
    for (byte b : bytes) {
        formatter.format("%02x", b);
    }
    return formatter.toString();
}

public static String calculateHMAC(String data, String key)
    throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
{
    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
    Mac mac = Mac.getInstance(HMAC_SHA512);
    mac.init(secretKeySpec);
    return toHexString(mac.doFinal(data.getBytes()));
}

public static void main(String[] args) throws Exception {
    String hmac = calculateHMAC("data", "key");
    System.out.println(hmac);
}
