 public class ConnectHttps {
    public static void main(String[] args) throws Exception {
  /*
 *  fix for
 *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
 *       sun.security.validator.ValidatorException:
 *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
 *               unable to find valid certification path to requested target
 */
TrustManager[] trustAllCerts = new TrustManager[] {
   new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

      public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

   }
};

SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
HostnameVerifier allHostsValid = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
};
// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
/*
 * end of the fix
 */

 //send SOAP Request
   URL url = new URL("https://192.168.0.43:7002/Examples/TestService?WSDL");

    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
    envelope.setOutputSoapObject(request); 


    //HttpsTransportSE  androidHttpTransport= new KeepAliveHttpsTransportSE("https://192.168.0.43", 7002, "TestService", 9000);

    try{

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        androidHttpTransport.call(SOAP_ACTION, envelope);   

        SoapPrimitive result = (SoapPrimitive)envelope.getResponse();

        if (envelope.bodyIn instanceof SoapFault) {
            String str= ((SoapFault) envelope.bodyIn).faultstring;

            System.out.println("" +str);

        } else {
            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
            System.out.println("WS"+ ""+ String.valueOf(resultsRequestSOAP));
        }
    }catch(Exception ex)
    {
        System.out.println("Error:" +ex.getMessage());
        ex.printStackTrace();

    }*/



  }
}
