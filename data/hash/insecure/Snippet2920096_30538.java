    private String getMD5Digest(byte[] buffer) {
    String resultHash = null;
    try {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] result = new byte[md5.getDigestLength()];
        md5.reset();
        md5.update(buffer);
        result = md5.digest();

        StringBuffer buf = new StringBuffer(result.length * 2);

        for (int i = 0; i < result.length; i++) {
            int intVal = result[i] & 0xff;
            if (intVal < 0x10) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(intVal));
        }

        resultHash = buf.toString();
    } catch (NoSuchAlgorithmException e) {
    }
    return resultHash;
}
