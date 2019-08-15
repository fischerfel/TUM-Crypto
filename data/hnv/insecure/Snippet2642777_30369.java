public void connect() throws A_WHOLE_BUNCH_OF_EXCEPTIONS {

    HttpPost post = new HttpPost(new URI(PROD_URL));
    post.setEntity(new StringEntity(BODY));

    KeyStore trusted = KeyStore.getInstance("BKS");
    trusted.load(null, "".toCharArray());
    SSLSocketFactory sslf = new SSLSocketFactory(trusted);
    sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme ("https", sslf, 443));
    SingleClientConnManager cm = new SingleClientConnManager(post.getParams(),
            schemeRegistry);

    HttpClient client = new DefaultHttpClient(cm, post.getParams());
    HttpResponse result = client.execute(post);
}
