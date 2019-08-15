public String getCheckSum(byte[] buffer) {
    StringBuilder builder = new StringBuilder();
    try {
        MessageDigest complete;
        complete = MessageDigest.getInstance("MD5");
        byte[] rawCheckSum = complete.digest(buffer);
        for(byte b: rawCheckSum) {
            builder.append(Integer.toString( ( b & 0xff ) + 0x100, 16).substring( 1 ));
        }
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
    return builder.toString();
}
