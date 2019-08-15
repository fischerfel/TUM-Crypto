include java.security.MessageDigest;

// Exception handling not shown

String prehash = ...

final byte[] prehashBytes= prehash.getBytes( "iso-8859-1" );

System.out.println( prehash.length( ) );
System.out.println( prehashBytes.length );

final MessageDigest digester = MessageDigest.getInstance( "MD5" );

digester.update( prehashBytes );

final byte[] digest = digester.digest( );

final StringBuffer hexString = new StringBuffer();

for ( final byte b : digest ) {
    final int intByte = 0xFF & b;

    if ( intByte < 10 )
    {
        hexString.append( "0" );
    }

    hexString.append(
        Integer.toHexString( intByte )
    );
}

System.out.println( hexString.toString( ).toUpperCase( ) );
