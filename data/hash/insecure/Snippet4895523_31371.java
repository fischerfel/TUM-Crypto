public static String toSHA1(byte[] convertme) {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA-1");
    }
    catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    } 
    return new String(md.digest(convertme));
}
