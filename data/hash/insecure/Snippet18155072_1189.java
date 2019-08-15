public String doEncryptPassword(String s) {
    MessageDigest sha1;
    try {
        sha1 = MessageDigest.getInstance("SHA1");
        byte[] digest = sha1.digest((s).getBytes());
        return bytes2String(digest);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    return s;

}
private static String bytes2String(byte[] bytes) {
    StringBuilder string = new StringBuilder();
    for (byte b : bytes) {
        String hexString = Integer.toHexString(0x00FF & b);
        string.append(hexString.length() == 1 ? "0" + hexString : hexString);
    }
    return string.toString();
}
