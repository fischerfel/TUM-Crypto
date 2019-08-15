    public static String decode( String secret ) {
    String retString = "";
    try {
        byte[] kbytes = "jaas is the way".getBytes();
        SecretKeySpec key = new SecretKeySpec( kbytes, "Blowfish" );

        BigInteger n = new BigInteger( secret, 16 );
        byte[] encoding = n.toByteArray();

        Cipher cipher = Cipher.getInstance( "Blowfish" );
        cipher.init( Cipher.DECRYPT_MODE, key );
        byte[] decode = cipher.doFinal( encoding );
        retString = new String( decode );
    } catch (Exception ignore) {
        ignore.printStackTrace();
    }

    return retString;
}
