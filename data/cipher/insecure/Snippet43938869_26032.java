public static String crypt(String plaintext) throws GinException
{
    return toHexString(crypt(plaintext.getBytes()));
}

private static byte[] crypt(byte[] plaintext) throws GinException
{
    try
    {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plaintext);    
    }
    catch (Exception e)
    {
        throw new GinException(e.getMessage());
    }
}


// Convert a byte array to a hex string.
private static String toHexString ( byte[] b )
{
    StringBuffer sb = new StringBuffer( b.length * 2 );
    for ( int i=0; i<b.length; i++ )
    {
        // look up high nibble char
        sb.append( hexChar [( b[i] & 0xf0 ) >>> 4] ); // fill left with zero bits

        // look up low nibble char
        sb.append( hexChar [b[i] & 0x0f] );
    }
    return sb.toString();
}
