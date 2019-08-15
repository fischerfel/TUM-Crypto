public static String md5( String input ) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(input.getBytes( "UTF-8" ));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append( String.format( "%02x", array[i]));
        }
        return sb.toString();
    } catch ( NoSuchAlgorithmException | UnsupportedEncodingException e) {
        return null;            
    }

}
