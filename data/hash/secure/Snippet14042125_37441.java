public String encode(String password, String saltKey)
        throws NoSuchAlgorithmException, IOException {

    String encodedPassword = null;
    byte[] salt = base64ToByte(saltKey);

    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    digest.reset();
    digest.update(salt);

    byte[] btPass = digest.digest(password.getBytes("UTF-8"));
    for (int i = 0; i < ITERATION_COUNT; i++) {
        digest.reset();
        btPass = digest.digest(btPass);
    }

    encodedPassword = byteToBase64(btPass);
    return encodedPassword;
}

private byte[] base64ToByte(String str) throws IOException {
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] returnbyteArray = decoder.decodeBuffer(str);
    if (log.isDebugEnabled()) {
        log.debug("base64ToByte(String) - end");
    }
    return returnbyteArray;
}
