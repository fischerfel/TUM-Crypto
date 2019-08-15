public class HttpsDisable {


public static void disableCertificateValidation() {
      TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() { return null; }
          public void checkClientTrusted(X509Certificate[] certs, String authType) { }
          public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        }
      };
      try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      } catch (Exception e) {
          e.printStackTrace();
      }
    }


public static void main(String[] args)  {


                //Now you can access an https URL without having the certificate in the truststore
                try {
                 disableCertificateValidation();
                 URL url = new URL("https://162.19.122.114/service.asmx?wsdl");
                 System.out.println("url ================ "+url);

                 String sWebserviceurl = "https://172.17.134.214/service.asmx?wsdl";
                 String sXMLInput = "<KeedAn><PrAge>26</PrAge></KeedAn>";
                    AnalysisServiceService sl = new AnalysisServiceServiceLocator();
                    AnalysisService service =  sl.getAnalysisService(new URL(sWebserviceurl));
                    String in = service.getNeedProdRes(sXMLInput);
                    System.out.println("web service output ----------\n" + in);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

}   
}
