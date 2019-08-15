public static String getHash(final String msg) {
    StringBuilder sb = new StringBuilder();
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(msg.getBytes());
        byte[] byteData = digest.digest();
        for (byte x : byteData) {
            String str = Integer.toHexString(Byte.toUnsignedInt(x));
            if (str.length() < 2) {
                sb.append('0');
            }
            sb.append(str);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return sb.toString();
}
