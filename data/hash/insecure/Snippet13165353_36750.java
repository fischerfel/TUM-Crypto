public class SHA1 {
    public static final int SHA_DIGEST_LENGTH = 20;

    private MessageDigest md;

    public SHA1() {
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void update(byte[] data) {
        md.update(data);
    }

    public void update(BigNumber bn) {
        md.update(bn.asByteArray());
    }

    public void update(String data) {
        md.update(data.getBytes());
    }

    public byte[] digest() {
        return md.digest();
    }
}
