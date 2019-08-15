   ....
        SSLContext sslContext = SSLContexts.custom()         
        .loadTrustMaterial((KeyStore)null, new TrustSelfSignedStrategy()) 
               //I had a trust store of my own, and this might not work!
        .build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslcontext,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        CloseableHttpClient httpclient = HttpClients
            .custom()
            .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .setSSLSocketFactory(sslsf).build();
        CloseableHttpResponse response = httpclient.execute(httpUriRequest);
        try {
            HttpEntity entity = response.getEntity();
            //do things
            if(entity != null) {
                entity.consumeContent();
            }
        } finally {
            response.close();
        }
   ....
