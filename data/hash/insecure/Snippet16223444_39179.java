public static byte[] createHash( String stringToHash ) throws UnsupportedEncodingException {

    MessageDigest digestor;

    byte[] byteInput = null;

    try {
        digestor = MessageDigest.getInstance("SHA-1");
        digestor.reset();
        byteInput = digestor.digest( stringToHash.getBytes("UTF-8") );
    } catch( NoSuchAlgorithmException e ){};

    return byteInput;

}
