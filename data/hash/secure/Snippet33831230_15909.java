public void setKey() {
    skey = "mykey";
    MessageDigest sha = null;
    try {
        key = skey.getBytes("UTF-8");
        logger.debug("Key length ====> " + key.length);
        sha = MessageDigest.getInstance("SHA-512");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        key[15] = key[0]; // added

        secretKey = new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
}
