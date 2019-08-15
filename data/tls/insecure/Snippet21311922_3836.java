TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);
        LogUtils.log("SSL: did init TrustManagerFactory with trust keyStore");
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

urlConnection.setSSLSocketFactory(context.getSocketFactory());
