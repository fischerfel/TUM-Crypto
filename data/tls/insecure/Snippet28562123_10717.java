HttpsURLConnection.setDefaultHostnameVerifier(new DummyHostnameVerifier());
        //  Create a TrustManager which wont validate certificate chains start 

        javax.net.ssl.TrustManager[] trustAllCertificates = new javax.net.ssl.TrustManager[1];

        javax.net.ssl.TrustManager tm = new miTM();

        trustAllCertificates[0] = tm;

        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(null, trustAllCertificates, null);
    //  Create a TrustManager which wont validate certificate chains end 
HttpsURLConnection.setDefaultSSLSocketFactory(sslFactory);
