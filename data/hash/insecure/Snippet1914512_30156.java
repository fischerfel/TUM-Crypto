final public class Security {

    synchronized public static String MD5(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte[] digest = md.digest();
            return new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            return "" + msg.hashCode();
        }
    }
}
