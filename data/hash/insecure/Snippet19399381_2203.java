public static String hashKeyForDisk(String key) {
    String cacheKey;
    try {
        final MessageDigest mDigest = MessageDigest.getInstance("MD5");
        if (key == null) {
            Log.e("TEST","key = null");
            throw new IllegalArgumentException(" key == null");
        }

        if (mDigest == null) {
            Log.e("TEST","mDigest = null");

            return String.valueOf(key.hashCode());
        }
        byte[] bytes = key.getBytes();
        mDigest.update(bytes);
        cacheKey = bytesToHexString(mDigest.digest());
    } catch (NoSuchAlgorithmException e) {
        cacheKey = String.valueOf(key.hashCode());
    }
    return cacheKey;
}
