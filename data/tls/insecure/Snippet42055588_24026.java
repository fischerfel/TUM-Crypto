@Override
public Response callService(String serviceUrl, Request request) throws FaultMessage {

    if (serviceUrl.contains("https://")) {
        initHttps();
    }
    ((BindingProvider)port).getRequestContext().put(
        BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

    try {
        return port.sendMyRequest(request);
    } catch (Exception e) {
        //(show error)
    }
}

private void initHttps() {
    SSLContext sslCtx;
    try {
        AllTrustingManager tm = new AllTrustingManager();
        sslCtx = SSLContext.getInstance("SSL");
        sslCtx.init(null, new TrustManager[]{tm}, new SecureRandom());
    } catch (KeyManagementException e) {
        //(show error)
    } catch (NoSuchAlgorithmException e) {
        //(show error)
    }

    HostnameVerifier hnv = new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, SSLSession sslSession) {
          return true;
      }
    };

    Map<String, Object> ctxt = ((BindingProvider)port).getRequestContext();

    ctxt.put("com.sun.xml.internal.ws.transport.https.client.hostname.verifier", hnv);
    ctxt.put("com.sun.xml.ws.transport.https.client.hostname.verifier", hnv);

    ctxt.put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", sslCtx.getSocketFactory());
    ctxt.put("com.sun.xml.ws.transport.https.client.SSLSocketFactory", sslCtx.getSocketFactory());

}
private static class AllTrustingManager implements X509TrustManager {

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
}
