public static byte[] createChecksum(byte[] b){
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");


    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();
    }
    md.update(b,0,b.length);
    byte[] checksum = md.digest();
    return checksum;
}
