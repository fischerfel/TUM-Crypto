public synchronized String encode(String password)
        throws NoSuchAlgorithmException, IOException {

    String encodedPassword = null;
    byte[] salt = base64ToByte(saltChars);

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
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

    return returnbyteArray;
}

private String byteToBase64(byte[] bt) {

    BASE64Encoder endecoder = new BASE64Encoder();
    String returnString = endecoder.encode(bt);

    return returnString;
}
