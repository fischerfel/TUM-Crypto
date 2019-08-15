public class TestHttps {

  private static TrustManager[] getTrustManagers() throws IOException, GeneralSecurityException {

    String trustStorePassword = "password";

    String alg = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmFact = TrustManagerFactory.getInstance(alg);

    FileInputStream fis = new FileInputStream("C:\\work\\certs\\trust");
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(fis, trustStorePassword.toCharArray());
    fis.close();

    tmFact.init(ks);

    TrustManager[] tms = tmFact.getTrustManagers();
    return tms;
  }

  public static void main(String[] args) throws Exception {

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, getTrustManagers(), null);

    SSLSocketFactory ssf = new SSLSocketFactory(context);

    HttpClient base = new DefaultHttpClient();
    ClientConnectionManager ccm = base.getConnectionManager();
    SchemeRegistry registry = ccm.getSchemeRegistry();
    registry.register(new Scheme("https", 8443, ssf));

    try {

      DefaultHttpClient httpClient = new DefaultHttpClient(ccm, base.getParams());
      HttpGet getRequest = new HttpGet("https://localhost:8443/cafe/");

      HttpResponse response = httpClient.execute(getRequest);

      if (response.getStatusLine().getStatusCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "
            + response.getStatusLine().getStatusCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(
          (response.getEntity().getContent())));
      String output;
      while ((output = br.readLine()) != null) {
        System.out.println(output);
      }
      httpClient.getConnectionManager().shutdown();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
