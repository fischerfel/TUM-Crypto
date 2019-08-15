public String getMd5Hash(String str) {
    try {
        byte[] array = MessageDigest.getInstance("MD5").digest(str.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("Something went really wrong.");
    }
    return null;
}
