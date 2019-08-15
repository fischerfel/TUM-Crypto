private String getShortenedKey(String key) {
        String shortenedKey=null;
        MessageDigest md = null;
        LogUtils.LOGD(HASH_ALGO, "before key: "+ System.currentTimeMillis());
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            shortenedKey = key;
        }
        LogUtils.LOGD(HASH_ALGO, "after key: "+ System.currentTimeMillis());

        md.update(key.getBytes());
        byte[] shortenedBytes = md.digest();
        shortenedKey = String.valueOf(shortenedBytes);
        return shortenedKey;
    }
