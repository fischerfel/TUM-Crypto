public class Hashes {
    public static byte[] sha256Hash(byte[] input) {
        return hash(input, "SHA-256");
    }

    private static byte[] hash(byte[] input, String algorithm) {
        MessageDigest md=null;
        try {
            md=MessageDigest.getInstance(algorithm);
        } 
        catch (NoSuchAlgorithmException e) {}
        return md.digest(input);
    }
}
