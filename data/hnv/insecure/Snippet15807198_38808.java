    SSLSocketFactory sslSocketFactory = new SSLSocketFactory(keyStore, res.get(KEY_STORE_PASS_CONF), trustStore);
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
