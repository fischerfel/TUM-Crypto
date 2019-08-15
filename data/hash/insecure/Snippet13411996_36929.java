public static String encode(String data) throws Exception {

    /* Check the validity of data */
    if (data == null || data.isEmpty()) {
        throw new IllegalArgumentException("Null value provided for "
                + "MD5 Encoding");
    }

    /* Get the instances for a given digest scheme MD5 or SHA */
    MessageDigest m = MessageDigest.getInstance("MD5");

    /* Generate the digest. Pass in the text as bytes, length to the
     * bytes(offset) to be hashed; for full string pass 0 to text.length()
     */
    m.update(data.getBytes(), 0, data.length());

    /* Get the String representation of hash bytes, create a big integer
     * out of bytes then convert it into hex value (16 as input to
     * toString method)
     */
    String digest = new BigInteger(1, m.digest()).toString(16);

    return digest;
}
