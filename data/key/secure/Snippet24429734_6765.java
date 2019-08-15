public static String base64sha256(String data, String secret) {
    String hash = null;
    try {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] res = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        hash = getHex(res);
        hash = Base64.encodeToString(hash.getBytes("UTF-8"), Base64.NO_WRAP);
    } catch (Exception e){}
    return hash;
}

static final String HEXES = "0123456789abcdef";
public static String getHex( byte [] raw ) {
    if ( raw == null ) {
        return null;
    }
    final StringBuilder hex = new StringBuilder( 2 * raw.length );
    for ( final byte b : raw ) {
        hex.append(HEXES.charAt((b & 0xF0) >> 4))
                .append(HEXES.charAt((b & 0x0F)));
    }
    return hex.toString();
}
