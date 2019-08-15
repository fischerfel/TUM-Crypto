            HttpsURLConnection connection = (HttpsURLConnection) (new URL(url))
                .openConnection();
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager tm[] = {new SSLPinningTrustManager()};
        context.init(null, tm, null);
        SSLSocketFactory preferredCipherSuiteSSLSocketFactory = new PreferredCipherSuiteSSLSocketFactory(context.getSocketFactory());
        connection.setSSLSocketFactory(preferredCipherSuiteSSLSocketFactory);
                    connection.connect();
