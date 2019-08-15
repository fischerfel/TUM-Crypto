public void calculateInfoHash( ){
try{
    int index = rawData.indexOf("4:info") + 6;
    int end = rawData.length() - 1;

    String info = rawData.substring( index , end );

    MessageDigest md = MessageDigest.getInstance( "SHA" );
    md.update( info.getBytes() );
    byte[] digest = md.digest();

    for ( byte b : digest ) {
    // print byte as 2 hex digits with lead 0. 
    //Separate pairs of digits with space
    //System.out.print( "%" );
    System.out.printf( "%02X", b & 0xff );
        }
    System.out.println( );

}catch( Exception e ) { 
    System.out.println( e.toString() );
}
}
