     protected void prepareConnection(HttpURLConnection connection,
        String httpMethod) throws IOException {

    connection.setFollowRedirects(true);

    HostnameVerifier v = new NullHostnameVerifier();
    ((HttpsURLConnection) connection).setDefaultHostnameVerifier(v);
      ((HttpsURLConnection) connection).setHostnameVerifier(v);



    super.prepareConnection(connection, httpMethod);
 }
