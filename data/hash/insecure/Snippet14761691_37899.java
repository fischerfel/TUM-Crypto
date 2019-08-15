public static String md5(String text) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(text.getBytes());
    return bytesToHex(md.digest());
}

public static String bytesToHex(byte[] b) {
    char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };
    StringBuffer buf = new StringBuffer();
    for (int j = 0; j < b.length; j++) {
        buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
        buf.append(hexDigit[b[j] & 0x0f]);
    }
    return buf.toString();
}
