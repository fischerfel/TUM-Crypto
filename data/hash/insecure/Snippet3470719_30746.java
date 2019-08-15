    try {
        MessageDigest md5 = MessageDigest.getInstance( "MD5" );

        byte [] digest = md5.digest( data );

        return digest;
    } catch( java.security.NoSuchAlgorithmException ex ) {
        // Insert error handling here.
    }
