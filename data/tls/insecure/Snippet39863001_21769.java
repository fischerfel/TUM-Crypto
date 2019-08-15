SslContextFactory factory = new SslContextFactory(true);
factory.setTrustAll(true);
factory.setValidateCerts(false);
factory.setValidatePeerCerts(false);
factory.setEndpointIdentificationAlgorithm(null);

SSLContext sslContext = factory.getSslContext();
if(null == sslContext) {
    sslContext = SSLContext.getInstance("TLS");
}
TrustManager[] verifiers = new TrustManager[] {...// some dummy trust manager that always passes};
sslContext.init(null, verifiers, null);
factory.setSslContext(sslContext);

HttpClientTransportOverHTTP2 httpClientTransportOverHTTP2
            = new HttpClientTransportOverHTTP2(new HTTP2Client());
HttpClient httpClient = new HttpClient(httpClientTransportOverHTTP2, factory);


Request request = httpClient.POST(destination);
ContentProvider contentProvider = new InputStreamContentProvider(new StringInputStream(payload));
request.content(contentProvider);
ContentResponse response = request.send();
