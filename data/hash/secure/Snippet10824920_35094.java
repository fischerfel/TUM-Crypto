public String encrypt(String plaintext, String salt) throws NoSuchAlgorithmException,
        UnsupportedEncodingException {
    String pass = plaintext + "{" + salt + "}";
    MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);

    messageDigest.reset();
    byte[] btPass = messageDigest.digest(pass.getBytes("UTF-8"));

    for (int i = 0; i < ITERATIONS - 1; i++) {
        messageDigest.reset();
        btPass = messageDigest.digest(btPass);
    }

    String hashedPass = new BigInteger(1, btPass).toString(16);
    if (hashedPass.length() < 32) {
        hashedPass = "0" + hashedPass;
    }

    return hashedPass;
}
