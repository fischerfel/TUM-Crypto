SSLContext sslContext = SSLContext.getInstance( "SSL" );

// set up a TrustManager that trusts everything
sslContext.init( null, new TrustManager[]
    {
        new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted( X509Certificate[] certs, String authType )
            {
                // everything is trusted
            }

            public void checkServerTrusted( X509Certificate[] certs, String authType )
            {
                // everything is trusted
            }
        }
    }, new SecureRandom() );

// this doesn't seem to apply to connections through a proxy
HttpsURLConnection.setDefaultSSLSocketFactory( sslContext.getSocketFactory() );

// setup a hostname verifier that verifies everything
HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier()
{
    public boolean verify( String arg0, SSLSession arg1 )
    {
        return true;
    }
} );
