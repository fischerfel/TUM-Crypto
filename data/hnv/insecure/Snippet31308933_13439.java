private AndroidHttpClient addCustomCertificate(AndroidHttpClient client)
    {
        SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();

        try
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            sf = new SSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        }
        catch (Exception t)
        {
            t.printStackTrace();
        }

        client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));

        return client;
    }
