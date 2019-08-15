// PathAssembler.java
private String getMd5Hash(String string) {
    try {
        MessageDigest e = MessageDigest.getInstance("MD5");
        byte[] bytes = string.getBytes();
        e.update(bytes);
        return (new BigInteger(1, e.digest())).toString(32);
    } catch (Exception var4) {
        throw new RuntimeException("Could not hash input string.", var4);
    }
}
