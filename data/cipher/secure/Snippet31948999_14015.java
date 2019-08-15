    private byte[] encryptSalt( String salt ) throws Exception
    {
        byte[] cipheredKey = null;
        try
        {

             String keyString= readKeyFile( enviro.getPublicKeyFile() );
             byte[] pem = pemToDer(keyString); 
             PublicKey publicKey = derToPublicKey(pem);


            //PublicKey publicKey = getPublicKey( enviro.getPublicKeyFile() );
            // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            rsaCipher.init( Cipher.ENCRYPT_MODE, publicKey );
            cipheredKey = rsaCipher.doFinal( salt.getBytes( "UTF-8" ) );//"UTF-8"
            LOG.info( "Cyphered key : " + cipheredKey.toString() );

        }
        catch ( IOException | GeneralSecurityException e )
        {
            e.printStackTrace();
            throw e;
        }

        return cipheredKey;
    }

    static String readKeyFile( String path )
            throws IOException
    {
        String line = null;

        try (BufferedReader br =
                new BufferedReader( new FileReader( path ) ))
        {

            StringBuilder sb = new StringBuilder();
            line = br.readLine();

            while ( line != null )
            {
                sb.append( line );
                sb.append( "\n" );
                line = br.readLine();
            }

            return sb.toString();
        }

    }

    public static byte[] pemToDer( String pemKey ) throws GeneralSecurityException
    {
        String[] parts = pemKey.split( "-----" );
        return DatatypeConverter.parseBase64Binary( parts[ parts.length / 2 ] );
    }

    public static PublicKey derToPublicKey( byte[] asn1key ) throws GeneralSecurityException
    {
        X509EncodedKeySpec spec = new X509EncodedKeySpec( asn1key );
        KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );
        return keyFactory.generatePublic( spec );
    }
