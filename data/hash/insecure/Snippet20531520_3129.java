public static void main(String[] args) throws NoSuchAlgorithmException {
        String s = "oshai";
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(s.getBytes(),0,s.length());
        String string = toHexString(m.digest());
        System.out.println(string);
}

public static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for(byte b : bytes) {
        sb.append(String.format("%02x", b));
    }
    return sb.toString();
}
