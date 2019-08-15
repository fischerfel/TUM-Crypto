public static final long md5(final String input) {
    try {
        // Create MD5
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        // Read in string as an array of bytes.
        byte[] originalBytes = input.getBytes("US-ASCII");
        byte[] encodedBytes = md5.digest(originalBytes);

        long output = 0;
        long multiplier = 1;

        // Create 64 bit integer from the MD5 hash of the input
        for (int i = 0; i < encodedBytes.length; i++) {
            output = output + encodedBytes[i] * multiplier;
            multiplier = multiplier * 255;
        }
        return output;

    } 
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
     catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return 0;
}
