public GetCountryResponse find() throws MalformedURLException, GeneralSecurityException, IOException {
    GetCountryRequest request = new GetCountryRequest();
    request.setName("Spain");
    SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

    LayeredConnectionSocketFactory sslSocketFactory = null;
    try {
        sslSocketFactory = new SSLConnectionSocketFactory(SSLContext.getDefault(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    } catch (NoSuchAlgorithmException e) {
    }

    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    FileInputStream instream = new FileInputStream(new File("C:\\client.p12"));
    try {
        keyStore.load(instream, "password".toCharArray());
    } finally {
        instream.close();
    }

    // Trust own CA and all self-signed certs
    SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "password".toCharArray())
            // .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
            .build();
    // Allow TLSv1 protocol only
    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // TODO
    HttpClient httpclient = HttpClients.custom()
            .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) // TODO
            .setSSLSocketFactory(sslsf).addInterceptorFirst(new ContentRemover()).build();

    HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(httpclient);

    WebServiceMessageSender sender = messageSender;

    WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
    webServiceTemplate.setMessageSender(sender);

    GetCountryResponse response = (GetCountryResponse) webServiceTemplate.marshalSendAndReceive(
            "https://localhost:8443/ws/countries.wsdl", request,
            new SoapActionCallback("https://localhost:8443/ws/"));

    return response;
}
