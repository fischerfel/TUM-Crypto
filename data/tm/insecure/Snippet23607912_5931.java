public class MarklogicDemo {

  public static void main(String[] args) throws Exception {
    URI uri = new URI("xcc://demo:password@localhost:9470/Arg");
    query = "for $x in cts:search(//PLAY,cts:element-word-query(xs:QName(\"LINE\"),\"King\")) return ($x//TITLE)";    
    ContentSource con = 
      ContentSourceFactory.newContentSource(
        "localhost", 9470,
        "demo", "password",
        "Arg", newTrustOptions());
    Session see = con.newSession();
    Request req = see.newAdhocQuery(query);
    ResultSequence rs = see.submitRequest (req);
    System.out.println (rs.asString());
    see.close();
  }

  protected static SecurityOptions newTrustOptions() throws Exception {
    TrustManager[] trust = 
      new TrustManager[] { 
        new X509TrustManager() {
          public void checkClientTrusted(
            X509Certificate[] x509Certificates, 
            String s) throws CertificateException {
          }

          public void checkServerTrusted(
            X509Certificate[] x509Certificates, 
            String s) throws CertificateException {
          }   
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        }
    };
    SSLContext sslContext = SSLContext.getInstance("SSLv3");
    sslContext.init(null, trust, null);
    return new SecurityOptions(sslContext);
  }

}
