SSLContext context = SSLContext.getInstance("TLS");
HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
