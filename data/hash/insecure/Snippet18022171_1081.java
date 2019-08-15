/**
 * Static factory to retrieve a type 3 (name based) {@code UUID} based on
 * the specified byte array.
 *
 * @param  name
 *         A byte array to be used to construct a {@code UUID}
 *
 * @return  A {@code UUID} generated from the specified array
 */
public static UUID nameUUIDFromBytes(byte[] name) {
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException nsae) {
        throw new InternalError("MD5 not supported");
    }
    byte[] md5Bytes = md.digest(name);
    md5Bytes[6]  &= 0x0f;  /* clear version        */
    md5Bytes[6]  |= 0x30;  /* set to version 3     */
    md5Bytes[8]  &= 0x3f;  /* clear variant        */
    md5Bytes[8]  |= 0x80;  /* set to IETF variant  */
    return new UUID(md5Bytes);
}
