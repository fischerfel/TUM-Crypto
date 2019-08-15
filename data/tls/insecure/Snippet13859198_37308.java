KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(context.getResources().openRawResource(R.raw.gecko_cert_1), "gecko_cert_1".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(ks, "gecko_cert_1".toCharArray());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);


        //request
        URL serverURL = new URL(myurl); 
        HttpsURLConnection conn = (HttpsURLConnection)serverURL.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        //conn.setHostnameVerifier(DO_NOT_VERIFY);
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
