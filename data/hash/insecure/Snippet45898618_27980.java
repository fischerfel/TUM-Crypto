public class PasswordDeriveBytes {

    private final MessageDigest hash;

    private final byte[] firstToLastDigest;
    private final byte[] outputBuffer;

    private int position = 0;

    public PasswordDeriveBytes(String password, byte[] salt) {
        try {
            this.hash = MessageDigest.getInstance("SHA-1");

            this.hash.update(password.getBytes(UTF_8));
            this.hash.update(salt);
            this.firstToLastDigest = this.hash.digest();

            final int iterations = 1;//**changed from 100**
            for (int i = 1; i < iterations - 1; i++) {
                hash.update(firstToLastDigest);
                hash.digest(firstToLastDigest, 0, firstToLastDigest.length);
            }

            this.outputBuffer = hash.digest(firstToLastDigest);

        } catch (NoSuchAlgorithmException | DigestException e) {
            throw new IllegalStateException("SHA-1 digest should always be available", e);
        }
    }

    public byte[] getBytes(int requested) {
        if (requested < 1) {
            throw new IllegalArgumentException(
                    "You should at least request 1 byte");
        }

        byte[] result = new byte[requested];

        int generated = 0;

        try {
            while (generated < requested) {
                final int outputOffset = position % outputBuffer.length;
                if (outputOffset == 0 && position != 0) {
                    final String counter = String.valueOf(position / outputBuffer.length);
                    hash.update(counter.getBytes(US_ASCII));
                    hash.update(firstToLastDigest);
                    hash.digest(outputBuffer, 0, outputBuffer.length);
                }

                final int left = outputBuffer.length - outputOffset;
                final int required = requested - generated;
                final int copy = Math.min(left, required);

                System.arraycopy(outputBuffer, outputOffset, result, generated, copy);

                generated += copy;
                position += copy;
            }
        } catch (final DigestException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }
}
