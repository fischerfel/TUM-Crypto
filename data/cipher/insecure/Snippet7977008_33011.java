 public static String encrypt( String content, String password ) throws NoSuchAlgorithmException,
    NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException,
    BadPaddingException
{
    KeyGenerator kgen = KeyGenerator.getInstance( "AES" );
    kgen.init( 128, new SecureRandom( password.getBytes() ) );
    SecretKey secretKey = kgen.generateKey();
    byte[] enCodeFormat = secretKey.getEncoded();
    SecretKeySpec key = new SecretKeySpec( enCodeFormat, "AES" );
    Cipher cipher = Cipher.getInstance( "AES" );
    byte[] byteContent = content.getBytes( "utf-8" );
    cipher.init( Cipher.ENCRYPT_MODE, key );
    byte[] result = cipher.doFinal( byteContent );
    return parseByte2HexStr( result );
}


public static String decrypt( String contents, String password ) throws NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    byte[] content = parseHexStr2Byte( contents );
    KeyGenerator kgen = KeyGenerator.getInstance( "AES" );
    kgen.init( 128, new SecureRandom( password.getBytes() ) );
    SecretKey secretKey = kgen.generateKey();
    byte[] enCodeFormat = secretKey.getEncoded();
    SecretKeySpec key = new SecretKeySpec( enCodeFormat, "AES" );
    Cipher cipher = Cipher.getInstance( "AES" );  
    cipher.init( Cipher.DECRYPT_MODE, key );
    byte[] result = cipher.doFinal( content );
    return new String( result ); 
}


public static String parseByte2HexStr( byte buf[] )
{
    StringBuffer sb = new StringBuffer();
    for( int i = 0; i < buf.length; i++ )
    {
        String hex = Integer.toHexString( buf[i] & 0xFF );
        if( hex.length() == 1 )
        {
            hex = '0' + hex;
        }
        sb.append( hex.toUpperCase() );
    }
    return sb.toString();
}


public static byte[] parseHexStr2Byte( String hexStr )
{
    if( hexStr.length() < 1 )
        return null;
    byte[] result = new byte[ hexStr.length() / 2 ];
    for( int i = 0; i < hexStr.length() / 2; i++ )
    {
        int high = Integer.parseInt( hexStr.substring( i * 2, i * 2 + 1 ), 16 );
        int low = Integer.parseInt( hexStr.substring( i * 2 + 1, i * 2 + 2 ), 16 );
        result[i] = ( byte ) ( high * 16 + low );
    }
    return result;
}
