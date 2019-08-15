public class MD5Converter {

    private final static String ALGORITHM = "MD5";
    private final static String ENCODING = "UTF-8";

    public static String encryptToMd5(String normal) {
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance(ALGORITHM);
            digest.reset();
            digest.update(normal.getBytes(ENCODING));
            byte[] hash = digest.digest();
            return byteToHex(hash); //This is on you res to implement this 
        } catch (NoSuchAlgorithmException e) {
            final String err = "No such digest algorithm \"" + ALGORITHM + "\"";
            log.fatal(err, e);
            throw new IllegalStateException(err, e);
        } catch (UnsupportedEncodingException e) {
            final String err = "Unsupported encoding \"" + ENCODING + "\"";
            log.fatal(err, e);
            throw new IllegalStateException(err, e);
            }
        }
}
