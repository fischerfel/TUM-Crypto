        SSLContext ctx = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(getTrustedCert());//todo
        ctx.init(null, tmf.getTrustManagers(), null);

        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {  
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return isInAcceptableHostNameList(arg0);//todo
            }
        });
