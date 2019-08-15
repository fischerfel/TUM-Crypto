BasicHttpResponse send(String message) throws KeyStoreException, IOException, NoSuchAlgorithmException, UnsupportedEncodingException, AuthenticationException, CertificateException, KeyManagementException, UnrecoverableKeyException {
    Log.d(context.getString(R.string.app_name), "SOAP: " + message);

    //SSL
    final KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(context.getResources().openRawResource(R.raw.ipex), "xxx".toCharArray());
    final SSLSocketFactory ssl = new SSLSocketFactory(keyStore);
    ssl.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    http.getConnectionManager().getSchemeRegistry().register(new Scheme("https", ssl, 443));

    //SOAP POST
    post.setHeader("Content-Type", "application/soap+xml; charset=utf-8");
    http.modifyRequestToAcceptGzipResponse(post);
    final StringEntity entity = new StringEntity(message.toString(), HTTP.UTF_8);
    entity.setContentType("text/xml");
    post.setEntity(entity);

    //Basic HTTP authentication
    post.setHeader(new BasicScheme().authenticate(credentials, post));

    //Send request
    BasicHttpResponse response = null;
    final byte max = 5;
    for (byte i = 1; response == null || (i <= max && response.getStatusLine().getStatusCode() != HttpStatus.SC_OK); i++) {
        Log.d(context.getString(R.string.app_name), String.format("SOAP: %d/%d", i, max));
        response = (BasicHttpResponse) http.execute(post);
        Log.d(context.getString(R.string.app_name), response.getStatusLine().toString());
    }

    //Unpack response
    final BasicHttpEntity ungzipped = new BasicHttpEntity();
    ungzipped.setContent(http.getUngzippedContent(response.getEntity()));
    response.setEntity(ungzipped);

    Log.v(context.getString(R.string.app_name), "SOAP: return");
    return response;
}
