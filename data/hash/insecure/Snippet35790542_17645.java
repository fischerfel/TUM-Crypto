/**
 * Hash a constraint name using MD5. Convert the MD5 digest to base 35
 * (full alphanumeric), guaranteeing
 * that the length of the name will always be smaller than the 30
 * character identifier restriction enforced by a few dialects.
 *
 * @param s The name to be hashed.
 *
 * @return String The hashed name.
 */
public String hashedName(String s) {
    try {
        MessageDigest md = MessageDigest.getInstance( "MD5" );
        md.reset();
        md.update( s.getBytes() );
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger( 1, digest );
        // By converting to base 35 (full alphanumeric), we guarantee
        // that the length of the name will always be smaller than the 30
        // character identifier restriction enforced by a few dialects.
        return bigInt.toString( 35 );
    }
    catch ( NoSuchAlgorithmException e ) {
        throw new HibernateException( "Unable to generate a hashed name!", e );
    }
}
