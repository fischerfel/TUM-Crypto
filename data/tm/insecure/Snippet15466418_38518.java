// Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
    public X509Certificate[] getAcceptedIssuers(){return null;}
    public void checkClientTrusted(X509Certificate[] certs, String authType){}
    public void checkServerTrusted(X509Certificate[] certs, String authType){}
}};
try {
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
} catch (Exception e) {
    ;
}

Client client = Client.create();

String url = Configuration.getOption(CONFIG_REST_URL) + actionPath;
if (dontSendMode) {
    GGResponse r = new GGResponse();
    r.setUrl(url);
    return r;
}
if (StringUtils.isNotEmpty(proxy_ticket)) {
    if (url.contains("?")) {
        url += "&";
    } else {
        url += "?";
    }
    url += String.format("ticket=%s", proxy_ticket);
}
WebResource.Builder resourceBuilder = client
        .resource(url)
        .accept("application/json");

ClientResponse response = null;
if (method.equalsIgnoreCase("GET")) {
    response = resourceBuilder.get(ClientResponse.class);
}
if (method.equalsIgnoreCase("POST")) {
    response = resourceBuilder.post(ClientResponse.class, json);
}
if (method.equalsIgnoreCase("DELETE")) {
    response = resourceBuilder.delete(ClientResponse.class);
}
if (method.equalsIgnoreCase("PUT")) {
    response = resourceBuilder.put(ClientResponse.class, json);
}

String output = null;
String etagResponse = null;
String error = null;

if (response != null && response.getStatus() == 200) {
    output = response.getEntity(String.class);
} else {
    System.out.println(error);
}
