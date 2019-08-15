 byte[] bytes = text.getBytes( "UTF-8" );
 MessageDigest messageDigest = MessageDigest.getInstance( "SHA-256" );
 byte[] hash = messageDigest.digest( bytes );

 StringBuffer hexString = new StringBuffer();
 for( int i = 0; i < hash.length; i++ )
 {
     String hex = Integer.toHexString( 0xff & hash[ i ] );
     if( hex.length() == 1 )
     {
         hexString.append( '0' );
     }
     hexString.append( hex );
 }
