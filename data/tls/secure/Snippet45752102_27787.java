private static javax.net.ssl.SSLSocketFactory getFactorySimple() 
        throws Exception {
    SSLContext context = SSLContext.getInstance("TLSv1.2");`

    context.init(null, null, null);

    return context.getSocketFactory();

}

String loginurl ="some url";
HttpsURLConnection connection = null;
URL url = new URL(loginURL);

connection = (HttpsURLConnection) url.openConnection();

javax.net.ssl.SSLSocketFactory sslSocketFactory =getFactorySimple();

connection.setSSLSocketFactory(sslSocketFactory);
