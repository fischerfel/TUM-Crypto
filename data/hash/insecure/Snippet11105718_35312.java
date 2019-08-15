public static final String md5(byte[] s) { 
    try { 

        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] digest = m.digest(s);
        String hash = EncodingUtils.getAsciiString(digest, 0, 16);
        Log.i("MD5", "Hash: "+hash);

        return hash;

    } catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
    }
    return "";
}   
