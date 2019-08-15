        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
            .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());
        sslcontext = sslContextBuilder.build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslcontext, new String[] {"TLSv1"}, null,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
        );
        httpclient = HttpClients
            .custom()
            .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .setSSLSocketFactory(sslsf).build();
