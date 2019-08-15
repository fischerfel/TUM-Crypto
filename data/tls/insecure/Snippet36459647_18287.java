// Trust manager that recognizes you acceptance criteria, that being ignoring handshake errors
        ResourceTrustManager trustManager = new ResourceTrustManager(sslKeyStores);
        TrustManager[] trustManagers = {trustManager};
        // use: org.apache.http.conn.ssl.AllowAllHostnameVerifier, this can be optional depending on your case.
        AllowAllHostnameVerifier resourceHostNameVerifier = new AllowAllHostnameVerifier(); 

        // Install the trust manager and host name verifier
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustManagers, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(resourceHostNameVerifier);
        } catch (Exception e) {
            Log.e(TAG, "Invalid algorithm used while setting trust store:" +e.getMessage());
            throw e;
        }
