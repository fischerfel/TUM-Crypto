SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tmf.getTrustManagers(), null);
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
