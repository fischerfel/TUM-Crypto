@Test
public void shouldCheckURI() throws IOException {
try{
 // Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
return null; }
    public void checkClientTrusted(java.security.cert.X509Certificate[] 
certs, String authType) { }
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] 
certs, String authType)throws CertificateException  { }
}};

// Install the all-trusting trust manager
SSLContext sc;
try {
    sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
} catch (Exception e) {
    e.printStackTrace();
}

// Create all-trusting host name verifier
HostnameVerifier allHostsValid = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};
// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
Client client = Client.create();

WebResource webResource = client.resource("https://SSLResfulendpoint");

ClientResponse response = 
webResource.type("application/json").get(ClientResponse.class);

assertEquals(200, response.getStatus());
} catch (Exception e) {
System.out.println(e.getMessage());  
e.printStackTrace();
}

}
