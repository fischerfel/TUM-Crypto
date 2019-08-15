    public static String hashValue(String value) {

    byte[] input = null;

    MessageDigest digest;
    try {
        digest = MessageDigest.getInstance("SHA-512");
        try {
            input = digest.digest(value.getBytes("UTF-8")); 

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    } catch (NoSuchAlgorithmException e1) {
        e1.printStackTrace();
    }

    return input.toString();
}
