final RestTemplate restTemplate = new RestTemplate();
restTemplate.setRequestFactory( new SimpleClientHttpRequestFactory() {
    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        if(connection instanceof HttpsURLConnection ){
            ((HttpsURLConnection) connection).setHostnameVerifier(PROMISCUOUS_VERIFIER);
        }
        super.prepareConnection(connection, httpMethod);
    }
});
