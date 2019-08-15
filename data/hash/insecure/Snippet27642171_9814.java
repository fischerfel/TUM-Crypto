public class Md5Sum {
    public static void main(final String... args) {
        final String md5sum = md5sum("dbox#service" + "2014-12-24T18:34:49");
        System.out.println("MD5 hash generated is: " + md5sum);
    }
    public static String md5sum2(final String plaintext) {
        try {
            final Formatter formatter = new Formatter();
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(plaintext.getBytes(StandardCharsets.UTF_16LE));
            for (final byte b : digest.digest())
                formatter.format("%02x", b);
            return formatter.toString();
        } catch (final NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
}
