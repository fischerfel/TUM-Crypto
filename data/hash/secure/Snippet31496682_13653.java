    MessageDigest md = MessageDigest.getInstance( "SHA-512" );
    md.update( inbytes );
    byte[] aMessageDigest = md.digest();

    String outEncoded = Base64.getEncoder().encodeToString( aMessageDigest );
    return( outEncoded );
