public String doHash(String s, String salt) {
    try {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        if (salt != null && !salt.isEmpty()) {
            md5.update(salt.getBytes());
        }
        byte[] array = md5.digest(s.getBytes());
        StringBuilder hexData = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            hexData.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return hexData.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
