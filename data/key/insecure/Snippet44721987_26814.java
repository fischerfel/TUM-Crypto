public class ShaUtil {

    public static String run(String msg) {
        Mac sha512_HMAC = null;
        String result = null;
        String key = "someKey";

        try {
            byte[] byteKey = key.getBytes("UTF-8");
            final String HMAC_SHA256 = "HmacSHA512";
            sha512_HMAC = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA256);
            sha512_HMAC.init(keySpec);
            byte[] mac_data = sha512_HMAC.doFinal(msg.getBytes("UTF-8"));
            result = bytesToHex(mac_data);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
            }
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
