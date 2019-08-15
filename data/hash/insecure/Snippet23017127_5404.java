MessageDigest sha;
    try {
        sha = MessageDigest.getInstance("SHA-1");
        byte[] hashOne = sha.digest(etPassword.getText().toString().getBytes());
        server.setPassword(hexEncode(hashOne));
    } catch (NoSuchAlgorithmException e) {
        //
    }

static private String hexEncode(byte[] aInput) {
    StringBuffer result = new StringBuffer();
    char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    for (byte b : aInput) {
        result.append(digits[(b & 0xf0) >> 4]);
        result.append(digits[b & 0x0f]);
    }
    return result.toString();
}
