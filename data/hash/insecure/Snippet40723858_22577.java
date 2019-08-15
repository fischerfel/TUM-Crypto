private static void getSubjectHash( X509Certificate x509Cert )
{
    try {
        // get the subject principal
        X500Principal x500Princ = x509Cert.getSubjectX500Principal( );

        // create a new principal using canonical name (order, spacing, etc.) and get it in ANS1 DER format
        byte[] newPrincEnc = new X500Principal( x500Princ.getName( X500Principal.CANONICAL ) ).getEncoded( );

        // read it in as an ASN1 Sequence to avoid custom parsing
        ASN1InputStream aIn = new ASN1InputStream( newPrincEnc );
        ASN1Sequence seq = (ASN1Sequence) aIn.readObject( );

        List<byte[]> terms = new ArrayList<>( );
        int finalLen = 0;
        int i = 0;

        // hash the encodables for each term individually and accumulate them in a list
        for ( ASN1Encodable asn1Set : seq.toArray( ) ) {
            byte[] term = ( (ASN1Set) asn1Set ).getEncoded( );
            terms.add( term );
            finalLen += term.length;

            // digest the term
            byte[] hashBytes = truncatedHash( getDigest( term ), 4 );
            printByteArray( String.format( "hash of object at %d:", i++ ), hashBytes );

            System.out.println( "" );
        }


        // hash all terms together in order of appearance
        int j = 0;
        byte[] finalEncForw = new byte[finalLen];
        for ( byte[] term : terms )
            for ( byte b : term )
                finalEncForw[j++] = b;

        // digest and truncate
        byte[] hashBytes = truncatedHash( getDigest( finalEncForw ), 4 );

        printByteArray( "hash of all terms in forward order", hashBytes );
        System.out.println( "" );


        // hash all terms together in reverse order
        j = 0;
        byte[] finalEncRev = new byte[finalLen];
        for ( int k = terms.size( ) - 1; k >= 0; --k )
            for ( byte b : terms.get( k ) )
                finalEncRev[j++] = b;

        // digest and truncate
        hashBytes = truncatedHash( getDigest( finalEncRev ), 4 );

        printByteArray( "hash of all terms in reverse order", hashBytes );
    }
    catch ( Exception ex ) {
        throw new RuntimeException( "uh-oh" );
    }
}

private static byte[] getDigest( byte[] toHash )
{
    MessageDigest md;

    try {
        md = MessageDigest.getInstance( "SHA1" );
    }
    catch ( NoSuchAlgorithmException nsa ) {
        throw new RuntimeException( "no such algorithm" );
    }

    return md.digest( toHash );
}

private static byte[] truncatedHash( byte[] hash, int truncatedLength )
{
    if ( truncatedLength < 1 || hash.length < 1 )
        return new byte[0];

    byte[] result = new byte[truncatedLength];

    for ( int i = 0; i < truncatedLength; ++i )
        result[truncatedLength - 1 - i] = hash[i];

    return result;
}

private static void printByteArray( String name, byte[] bytes )
{
    System.out.println( name + " length=" + String.valueOf( bytes.length ) );
    for ( byte b: bytes ) {
        System.out.print( String.format( "%02X ", Byte.toUnsignedInt( b ) ) );
    }

    System.out.println( );
}
