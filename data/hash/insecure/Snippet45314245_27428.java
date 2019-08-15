public static String byteArrayToString(byte[] bytes) {
    StringBuilder buffer = new StringBuilder();
    for (byte b : bytes) {
        buffer.append(String.format(Locale.getDefault(), "%02x", b));
    }
    return buffer.toString();
}

public static String SHA1(String clearString)
{
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(clearString.getBytes("UTF-8"));
        return byteArrayToString(messageDigest.digest());
    } catch (Exception ignored) {
        ignored.printStackTrace();
        return null;
    }
}
