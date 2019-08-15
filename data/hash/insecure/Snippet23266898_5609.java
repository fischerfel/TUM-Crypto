public static byte[] createChecksum(byte[] b){
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("MD5");
        md.update(b);
        byte[] checksum = md.digest();
        return checksum;
    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return null;
}
