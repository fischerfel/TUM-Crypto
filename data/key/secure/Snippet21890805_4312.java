private static String EncryptBy16( String str, String theKey) throws Exception
{

    if ( str == null || str.length() > 16)
    {
        throw new NullPointerException();
    }
    int len = str.length();
    byte[] pidBytes = str.getBytes();
    byte[] pidPaddedBytes = new byte[16];

    for ( int x=0; x<16; x++ )
    {
        if ( x<len )
        {
            pidPaddedBytes[x] = pidBytes[x];
        }
        else
        {
            pidPaddedBytes[x] = (byte) 0x0;
        }

    }

    byte[] raw = asBinary( theKey );
    SecretKeySpec myKeySpec = new SecretKeySpec( raw, "AES" );
    Cipher myCipher = Cipher.getInstance( "AES/ECB/NoPadding" );
    cipher.init( Cipher.ENCRYPT_MODE, myKeySpec );
    byte[] encrypted = myCipher.doFinal( pidPaddedBytes );
    return( ByteToString( encrypted ) );
}

public static String Encrypt(String stringToEncrypt, String key) throws Exception
{

    if ( stringToEncrypt == null ){
        throw new NullPointerException();
    }
    String str = stringToEncrypt;

    StringBuffer result = new StringBuffer();
    do{
        String s = str;
        if(s.length() > 16){
            str = s.substring(16);
            s = s.substring(0,16);
        }else {
            str = null;
        }
        result.append(EncryptBy16(s,key));
    }while(str != null);

    return result.toString();
}
