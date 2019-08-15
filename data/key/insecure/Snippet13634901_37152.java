public static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (int i = 0; i < bytes.length; ++i) {
        sb.append(String.format("%02x", bytes[i]));
    }
    return sb.toString();
}

public static void main(String[] args) {
    String str2 = "5f1fa09364a6ae7e35a090b434f182652ab8dd76:{\"expiration\": 1353759442.0991001, \"channel\": \"dreamhacksc2\", \"user_agent\": \".*";
    Mac localMac;
    try {
        localMac = Mac.getInstance("HmacSHA1");

        localMac.init(new SecretKeySpec("Wd75Yj9sS26Lmhve"
                .getBytes("UTF-8"), localMac.getAlgorithm()));
        byte[] result = localMac.doFinal(str2.getBytes("UTF-8"));
        String hexString = toHexString(result);
        System.out.println(hexString);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalStateException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

}
