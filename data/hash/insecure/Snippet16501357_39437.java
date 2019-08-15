public static UUID UUID5(UUID namespace, String name) {
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException nsae) {
        throw new InternalError("SHA-1 not supported");
    }

    byte[] namespaceBytes = ByteBuffer.allocate(16).putLong(namespace.getMostSignificantBits()).putLong(namespace.getLeastSignificantBits()).array();
    byte[] nameBytes;
    try {
        nameBytes = name.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
        throw new InternalError("UTF-8 encoding not supported");
    }

    byte[] toHashify = new byte[namespaceBytes.length + nameBytes.length];
    System.arraycopy(namespaceBytes, 0, toHashify, 0, namespaceBytes.length);
    System.arraycopy(nameBytes, 0, toHashify, namespaceBytes.length, nameBytes.length);

    byte[] data = md.digest(toHashify);
    data = Arrays.copyOfRange(data, 0, 16); // Wikipedia says "Note that the 160 bit SHA-1 hash is truncated to 128 bits to make the length work out."

    data[6]  &= 0x0f;  /* clear version        */
    data[6]  |= 0x50;  /* set to version 5     TODO is this the correct way to do it     */
    data[8]  &= 0x3f;  /* clear variant        */
    data[8]  |= 0x80;  /* set to IETF variant  */

    long msb = 0;
    long lsb = 0;
    assert data.length == 16 : "data must be 16 bytes in length";
    for (int i=0; i<8; i++)
        {msb = (msb << 8) | (data[i] & 0xff);}
    for (int i=8; i<16; i++)
        {lsb = (lsb << 8) | (data[i] & 0xff);}

    return new UUID(msb, lsb);
}
