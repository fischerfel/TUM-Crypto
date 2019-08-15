public class Testing {
static WebTarget webTarget = null;

public static void main(String args[]) throws Exception {
    webTarget = createClient();

    WebTarget webTargetWithQueryParam = webTarget.queryParam("Version", "1").queryParam("Connection", "gOpAK52by09i305CMqJsnzzD4paQd1KG%2BVgdCBJw9h%0D%0AeuAxY2");
    Invocation.Builder invocationBuilder = webTargetWithQueryParam.request(MediaType.APPLICATION_XML);
    invocationBuilder.header("Host", "stagingtenant").header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_TYPE)
    .accept("text/xml");

    Response response = invocationBuilder.post(Entity.xml(new File("..\sample.xml")));
    if (response.getStatus() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus()+"=="+response.getStatusInfo());
    }
    String output = response.getStatusInfo().toString();
    System.out.println(output);
}

public static Client initClient(ClientConfig config) throws NoSuchAlgorithmException, KeyManagementException {

    SSLContext ctx = SSLContext.getInstance("SSL");
      TrustManager certs = 
              new X509TrustManager(){
                  public X509Certificate[] getAcceptedIssuers(){ return new X509Certificate[0];}
                  public void checkClientTrusted(X509Certificate[] certs, String authType){}
                  public void checkServerTrusted(X509Certificate[] certs, String authType){}
              };

        ctx.init(null, new TrustManager[]{certs}, new SecureRandom());
        return ClientBuilder.newBuilder()
            .sslContext(ctx)
            .withConfig((javax.ws.rs.core.Configuration) config)
            .hostnameVerifier(new TrustAllHostNameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })

            .build();
}

public  static class TrustAllHostNameVerifier implements HostnameVerifier {
public boolean verify(String hostname, SSLSession session) {
    return true;
}
}

  public static WebTarget createClient() throws KeyManagementException, NoSuchAlgorithmException{
      ClientConfig clientConfig = new ClientConfig();
      Client client = initClient(clientConfig);
      client.register(new LoggingFilter());
      HttpAuthenticationFeature feature = HttpAuthenticationFeature.universalBuilder().credentialsForDigest("username", "password").build();
      client.register(feature);
      WebTarget webTarget = client.target("https://stagingtenant/apc/dig/ingestion/transcript");
      return webTarget;
  }
}
