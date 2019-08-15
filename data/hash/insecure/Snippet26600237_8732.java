public static String MD5Encode(String sourceString) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] bytes = md.digest();
    StringBuffer bf = new StringBuffer(bytes.length * 2);
    for (int i = 0; i < bytes.length; i++) {
        if ((bytes[i] & 0xff) < 0x10) {
            bf.append("0");
        }
        bf.append(Long.toString(bytes[i] & 0xff, 16));
    }
    return bf.toString();
}
