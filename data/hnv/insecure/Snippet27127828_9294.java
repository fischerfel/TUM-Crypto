public KeyStore initializeTrustStore(Context context)
{
    KeyStore keyStore = null;
    try
    {
        keyStore = KeyStore.getInstance("BKS", BouncyCastleProvider.PROVIDER_NAME);
        InputStream inputStream = context.getResources().openRawResource(R.raw.truststore);
        try
        {
            keyStore.load(inputStream, "password".toCharArray());
        }
        finally
        {
            inputStream.close();
        }
    }


    KeyStore trustStore = loadTrustStore(context);
    SSLContext sslcontext = null;
    CloseableHttpClient httpclient = null;

        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
            .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());
        sslcontext = sslContextBuilder.build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslcontext, new String[] {"TLSv1"}, null,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
        );
        httpclient = HttpClients
            .custom()
            .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .setSSLSocketFactory(sslsf).build();
        CloseableHttpResponse response = httpclient.execute(httpUriRequest);
        try
        {
            HttpEntity entity = response.getEntity();
            //do things with response here
            if(entity != null)
            {
                entity.consumeContent();
            }
        }
        finally
        {
            response.close();
        }
