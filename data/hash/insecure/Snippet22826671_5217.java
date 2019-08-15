// Its my problem, yuk...
public byte[] md5TheHardWay( String s ) {
    try {
        MessageDigest md = MessageDigest.getInstance( "MD5" );
        // Do stuff
        byte[] result = md.digest( s.getBytes() );
        return result;
    } catch ( NoSuchAlgorithmException e ) {
        // Can't happen...
        e.printStackTrace();
    }
    return null;
}

// Its your problem, yuk...
public byte[] md5ItsYourProblemClient( String s ) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance( "MD5" );
    // Do stuff
    byte[] result = md.digest( s.getBytes() );
    return result;
}

// Its no problem...I like Guava.
public byte[] md5ThroughGuava( String s ) {
    HashFunction md = Hashing.md5();
    HashCode code = md.hashBytes( s.getBytes() );
    return code.asBytes();
}
