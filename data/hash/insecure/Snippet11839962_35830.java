String hexMEID = "A0000000002329";
MessageDigest mDigest = MessageDigest.getInstance( "SHA1" );

byte[] input = new byte[ 7 ]; // MEIDs are only 7 bytes

// Now copy the bytes from BigInteger skipping the extra byte added by it
System.arraycopy( new BigInteger( hexMEID, 16 ).toByteArray(), 1, input, 0, 7 );

// Get the SHA-1 bytes
byte[] result = mDigest.digest( input );

// Build the SHA-1 String
StringBuilder sb = new StringBuilder();
for ( int i = 0; i < result.length; i++ )
{
    String hex = Integer.toHexString( 0xFF & result[ i ] );
    if ( hex.length() == 1 )
    {
        sb.append( '0' );
    }
    sb.append( hex );
}

String sha1 = sb.toString();
// Grab the last 6 characters of the SHA-1 hash
String lastSix = sha1.substring( sha1.length() - 6 );
// And prepend '80', now you have the ESN
System.out.println( "80" + lastSix );
// Will print 8051f1ab which is exactly what you want
