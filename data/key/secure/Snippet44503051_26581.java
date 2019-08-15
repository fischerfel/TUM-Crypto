static Key makeKey() {
    try {
        byte[] key = ENCRYPTION_KEY.getBytes("UTF-8");
        return new SecretKeySpec(key, "AES");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return null;
}
