private String getShortenedKey(String key) {
    String shortenedKey=null;
    MessageDigest md = null;
    LogUtils.LOGD(HASH_ALGO, "before key: "+ System.currentTimeMillis());
    try {
        md = MessageDigest.getInstance("MD5");

        md.update(key.getBytes());
        byte[] shortenedBytes = md.digest();
        shortenedKey = Base64.encodeToString(shortenedBytes, Base64.NO_WRAP);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        shortenedKey = key;
    }
    LogUtils.LOGD(HASH_ALGO, "after key: "+ System.currentTimeMillis());

    return shortenedKey;
}
