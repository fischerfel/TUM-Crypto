public static class PasswordDeriveBytes{

private final MessageDigest hash;

private final byte[] firstToLastDigest;
private final byte[] outputBuffer;

private int position = 0;

public PasswordDeriveBytes(String password, byte[] salt, int iterations) {
    try {
        this.hash = MessageDigest.getInstance("SHA-1");

        this.hash.update(password.getBytes("UTF-8"));
        this.hash.update(salt);
        this.firstToLastDigest = this.hash.digest();
        // At this point, the Obj-C and Java values are the same
        // this.firstToLastDigest = b8fa3d36....

        for (int i = 1; i < iterations - 1; i++) {
            System.out.println( "  Iterate " + i);
            hash.update(firstToLastDigest);
            hash.digest(firstToLastDigest, 0, firstToLastDigest.length);
        }

        this.outputBuffer = hash.digest(firstToLastDigest);
        // However at this point, they become different
        // Java has outputBuffer = f498e100...
        // Obj-C has outputBuffer = <d7d5fa71...

    } catch (UnsupportedEncodingException|NoSuchAlgorithmException | DigestException e) {
        throw new IllegalStateException("SHA-1 digest should always be available", e);
    }
}
