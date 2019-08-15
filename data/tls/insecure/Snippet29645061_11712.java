SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, null, null);
    SSLSocketFactory factory = context.getSocketFactory();
    httpURLConnection.setSSLSocketFactory(factory);
