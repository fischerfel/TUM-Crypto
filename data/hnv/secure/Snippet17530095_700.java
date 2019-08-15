private HttpsURLConnection openConnection(URL src, URL dest, SSLContext sslContext)
        throws IOException, ProtocolException {
    HttpsURLConnection connection = (HttpsURLConnection) dest.openConnection();
    HttpsHostNameVerifier httpsHostNameVerifier = new HttpsHostNameVerifier();
    connection.setHostnameVerifier(httpsHostNameVerifier);
    connection.setConnectTimeout(CONNECT_TIMEOUT);
    connection.setReadTimeout(READ_TIMEOUT);
    connection.setRequestMethod(POST_METHOD);
    connection.setRequestProperty(CONTENT_TYPE, SoapConstants.CONTENT_TYPE_HEADER);
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setSSLSocketFactory(sslContext.getSocketFactory());
    if ( src!=null ) {
        InetAddress inetAddress = InetAddress.getByName(src.getHost());
        int destPort = dest.getPort();
        if ( destPort <=0 ) 
            destPort=SERVER_HTTPS_PORT;
        int srcPort = src.getPort();
        if ( srcPort <=0 ) 
            srcPort=CLIENT_HTTPS_PORT;
         connectionSocket = connection.getSSLSocketFactory().createSocket(dest.getHost(), destPort, inetAddress, srcPort);
    }
    connection.connect();
    return connection;
}    
