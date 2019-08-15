/**
 * This method computes a hash of the provided {@code string}.
 * <p>
 * The algorithm in use by this method is as follows:
 * <ol>
 *    <li>Compute the MD5 value of {@code string}.</li>
 *    <li>Truncate leading zeros (i.e., treat the MD5 value as a number).</li>
 *    <li>Convert to base 36 (the characters {@code 0-9a-z}).</li>
 * </ol>
 */
private String getHash(String string) {
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = string.getBytes();
        messageDigest.update(bytes);
        return new BigInteger(1, messageDigest.digest()).toString(36);
    } catch (Exception e) {
        throw new RuntimeException("Could not hash input string.", e);
    }
}
