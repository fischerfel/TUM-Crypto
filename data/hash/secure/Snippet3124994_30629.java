byte[] digest = new byte[0];
MessageDigest md = null;
try{
    md = MessageDigest.getInstance( "SHA-512" );
}
catch( NoSuchAlgorithmException e ) {
    return digest;
}
digest = md.digest( myString.getBytes() );
