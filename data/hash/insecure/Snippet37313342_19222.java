private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
public String getEncodeData(String app_secret, String data){
    try{
        byte[] hmc = calculateRFC2104HMAC(Base64.encodeBase64(data.getBytes()), app_secret);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(hmc);
        return new String(Hex.encodeHex(thedigest));

    }catch (Exception ex){
        System.out.print(ex.getMessage().toString());
    }
    return "";
}
public static byte[] calculateRFC2104HMAC(byte[] data, String key)
        throws java.security.SignatureException
{
    try {

        // get an hmac_sha1 key from the raw key bytes
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

        // get an hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);

        // compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(data);
        return rawHmac;

    } catch (Exception e) {
        throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
    }
}
