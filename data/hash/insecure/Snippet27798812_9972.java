public class PasswordDeriveBytes {

    private final MessageDigest hash;
    private final byte[] initial;
    private final int iterations;

    private byte[] output;
    private int hashnumber = 0;
    private int position = 0;

    public PasswordDeriveBytes(String password, byte[] salt) {
        try {
            this.hash = MessageDigest.getInstance("SHA-1");
            this.initial = new byte[hash.getDigestLength()];
            this.hash.update(password.getBytes(UTF_8));
            this.hash.update(salt);
            this.hash.digest(this.initial, 0, this.initial.length);
            this.iterations = 100;
        } catch (NoSuchAlgorithmException | DigestException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] getBytes(int cb) {
        if (cb < 1)
            throw new IndexOutOfBoundsException("cb");
        byte[] result = new byte[cb];
        int cpos = 0;
        // the initial hash (in reset) + at least one iteration
        int iter = Math.max(1, iterations - 1);
        // start with the PKCS5 key
        if (output == null) {
            // calculate the PKCS5 key
            output = initial;
            // generate new key material
            for (int i = 0; i < iter - 1; i++)
                output = hash.digest(output);
        }
        while (cpos < cb) {
            byte[] output2 = null;
            if (hashnumber == 0) {
                // last iteration on output
                output2 = hash.digest(output);
            } else if (hashnumber < 1000) {
                String n = String.valueOf(hashnumber);
                output2 = new byte[output.length + n.length()];
                for (int j = 0; j < n.length(); j++)
                    output2[j] = (byte) (n.charAt(j));
                System.arraycopy(output, 0, output2, n.length(), output.length);
                // don't update output
                output2 = hash.digest(output2);
            } else {
                throw new SecurityException();
            }
            int rem = output2.length - position;
            int l = Math.min(cb - cpos, rem);
            System.arraycopy(output2, position, result, cpos, l);
            cpos += l;
            position += l;
            while (position >= output2.length) {
                position -= output2.length;
                hashnumber++;
            }
        }
        return result;
    }
}
