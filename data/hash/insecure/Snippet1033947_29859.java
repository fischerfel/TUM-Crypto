public static String md5( String source ) {
    try {
        MessageDigest md = MessageDigest.getInstance( "MD5" );
        byte[] bytes = md.digest( source.getBytes("UTF-8") );
        return getString( bytes );
    } catch( Exception e )  {
        e.printStackTrace();
        return null;
    }
}

private static String getString( byte[] bytes ) {
    StringBuffer sb = new StringBuffer();
    for( int i=0; i<bytes.length; i++ ) {
        byte b = bytes[ i ];
        sb.append( ( int )( 0x00FF & b ) );
        if( i+1 <bytes.length ) {
            sb.append( "-" );
        }
    }
    return sb.toString();
}
