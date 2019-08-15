public static String md5(String text) {
    try {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        digester.update(text.getBytes());
        byte[] md5Bytes = digester.digest();
        String md5Text = null;

        md5Text = bytesToHex(md5Bytes);

        return md5Text;

    }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "";
}

public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
