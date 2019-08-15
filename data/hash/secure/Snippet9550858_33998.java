public static byte[] produce(final byte[] data)
        throws NoSuchAlgorithmException{
    MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
    digest.reset();
    digest.update(data);
    byte[] ret = digest.digest(data);
    for (int i = 0; i < HASH_ITERATIONS; i++) {
        digest.reset();
        ret = digest.digest(ret);
    }
    return ret;
}
