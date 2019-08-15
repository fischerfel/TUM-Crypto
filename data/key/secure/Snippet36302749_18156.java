private String generateHMAC( String datas )
{

    //                final Charset asciiCs = Charset.forName( "utf-8" );
    Mac mac;
    String result = "";
    try
    {
      final SecretKeySpec secretKey = new SecretKeySpec( DatatypeConverter.parseHexBinary(PayboxConstants.KEY), "HmacSHA512" );
        mac = Mac.getInstance( "HmacSHA512" );
        mac.init( secretKey );
        final byte[] macData = mac.doFinal( datas.getBytes( ) );
        byte[] hex = new Hex( ).encode( macData );
        result = new String( hex, "ISO-8859-1" );
    }
    catch ( final NoSuchAlgorithmException e )
    {
        AppLogService.error( e );
    }
    catch ( final InvalidKeyException e )
    {
        AppLogService.error( e );
    }
    catch ( UnsupportedEncodingException e )
    {
        AppLogService.error( e );
    }

    return result.toUpperCase( );

}
