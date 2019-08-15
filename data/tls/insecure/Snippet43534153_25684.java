private static OkHttpClient.Builder getUnsafeOkHttpClient() {

    try {
        // Create a trust manager that does not validate certificate chains
        X509TrustManager x509TrustManager = new X509TrustManager() {
            public  final String PUB_KEY = "252b4188d34273e3afc79d70766ce4545b835e9ffb76ecae1df0b72bae06850d10649e9dda30adbf3e72d6b2707f25f3da14d1714a3c506dbc50567bb75c0a8731dd514da3353ee0b436db17ad3cbe8ba851bf21032e4458fc7f14cb2f5f3ad1efc704bec684934fdc2485641a7f4f354ea992458055dfd8489f403e8cc04b396a9e3f4ecd60c939f80c525a588fe40a239f7d36a6cef9c6aee1b597a3d3b44b38e16d8fb1226e40cd98e835b133043a5b0764e3b10d4c12af7c0af64c290e97e1788d1d540adafab99f2a25a729268a9630203010001".replace(" ","");

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) {

                if (chain == null) {
                    throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
                }

                if (!(chain.length > 0)) {
                    throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                }

                if (!(null != authType && authType.equalsIgnoreCase("ECDHE_RSA"))) {
                    try {
                        throw new CertificateException("checkServerTrusted: AuthType is not RSA: type = " +authType);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                }

                // Perform customary SSL/TLS checks
                try {
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                    tmf.init((KeyStore) null);
                    for (TrustManager trustManager : tmf.getTrustManagers()) {
                        ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                    }
                } catch (Exception e) {
                    try {
                        throw new CertificateException(e.getMessage());
                    } catch (CertificateException e1) {
                        e1.printStackTrace();
                    }
                }
                // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
                // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
                RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();

                String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);
                // Pin it!
                final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
                if (!expected) {
                    try {
                        throw new CertificateException("checkServerTrusted: Expected public \n key: "
                                + PUB_KEY + ", \n got public key:" + encoded);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        };

        final TrustManager[] trustAllCerts = new TrustManager[] { x509TrustManager };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext
                .getSocketFactory();

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient
                .sslSocketFactory(sslSocketFactory,x509TrustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @SuppressLint("BadHostnameVerifier")
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        boolean ret = false;
                        for (String host : new String[]{"apigw.jiaj.com.cn"}) {
                            if (host.equalsIgnoreCase(hostname)) {
                                ret = true;
                            }
                        }
                        return ret;
                    }
                })
                .certificatePinner(new CertificatePinner.Builder()
                .add("apic.zhg.com.cn","sha256/KArtr8lZCfZixEMIMuqpRt3kZDKAjaX6jK0PeCwxqzc=")
                .build())
             .proxy(Proxy.NO_PROXY)
        ;


        return okHttpClient;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

}
