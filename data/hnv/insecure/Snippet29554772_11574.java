    httpClient = new DefaultHttpClient(a, b);
    SSLSocketFactory socketFactory = (SSLSocketFactory) httpClient.getConnectionManager().getSchemeRegistry().get("https").getSchemeSocketFactory();
    socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
