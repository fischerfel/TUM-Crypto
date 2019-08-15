String sha1Hash( String toHash )
{
    String hash = null;
    try
    {
        MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
        byte[] bytes = toHash.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        bytes = digest.digest();

        // This is ~55x faster than looping and String.formating()
        hash = bytesToHex( bytes );
    }
    catch( NoSuchAlgorithmException e )
    {
        e.printStackTrace();
    }
    catch( UnsupportedEncodingException e )
    {
        e.printStackTrace();
    }
    return hash;
}

// http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
public static String bytesToHex( byte[] bytes )
{
    char[] hexChars = new char[ bytes.length * 2 ];
    for( int j = 0; j < bytes.length; j++ )
    {
        int v = bytes[ j ] & 0xFF;
        hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
        hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
    }
    return new String( hexChars );
}
