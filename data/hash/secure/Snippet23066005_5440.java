public static String hashSHA256(String input)
        throws NoSuchAlgorithmException {
    MessageDigest mDigest = MessageDigest.getInstance("SHA-256");

byte[] shaByteArr = mDigest.digest(input.getBytes(Charset.forName("UTF-8")));
    StringBuilder hexStrBuilder = new StringBuilder();
    for (int i = 0; i < shaByteArr.length; i++) {
        hexStrBuilder.append(Integer.toHexString(0xFF & shaByteArr[i]));
    }

    return hexStrBuilder.toString();
}
