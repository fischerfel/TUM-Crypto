public static String getSignatureForS3Upload(final String dateStamp, final String policy) {
    byte[] signingKey = null;
    byte[] signature = null;
    String strSignature = null;
    try {
        signingKey = AwsUtill.getSignatureKey(AppConfig.getS3SecretKey(), dateStamp,
                AppConfigShared.getMyAwsS3RegionName(), "s3");
        signature = HmacSHA256(policy, signingKey);
        strSignature = bytesToHex(signature);
    }
    catch (Exception e) {
       // log
    }
    ServerDBLogger.log(Level.INFO, byteArrayToHex(signature));
    ServerDBLogger.log(Level.INFO, bytesToHex(signature));
    return strSignature;

private static byte[] HmacSHA256(final String data, final byte[] key) throws Exception {
    String algorithm = "HmacSHA256";
    Mac mac = Mac.getInstance(algorithm);
    mac.init(new SecretKeySpec(key, algorithm));
    return mac.doFinal(data.getBytes("UTF-8"));
}

private static byte[] getSignatureKey(final String key, final String dateStamp, final String regionName,
        final String serviceName) throws Exception {
    byte[] kSecret = ("AWS4" + key).getBytes("UTF-8");
    byte[] kDate = HmacSHA256(dateStamp, kSecret);
    byte[] kRegion = HmacSHA256(regionName, kDate);
    byte[] kService = HmacSHA256(serviceName, kRegion);
    byte[] kSigning = HmacSHA256("aws4_request", kService);
    return kSigning;
}

final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
public static String bytesToHex(final byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
