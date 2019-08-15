private org.apache.axis.message.SOAPEnvelope SendSOAP(String SOAPaction,
        String EndPointURL, String SOAPmessage) {

    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }
    } };

    try {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(getKeyManagers(), trustAllCerts, null);
        SSLContext.setDefault(context);
    } catch (Exception e) {
        logger.fatal(e.toString());
    }


    org.apache.axis.message.SOAPEnvelope resp = null;
    try {
        InputStream input = new ByteArrayInputStream(SOAPmessage.getBytes());
        org.apache.axis.client.Service service = new org.apache.axis.client.Service();

        logger.debug(SOAPmessage);

        Call call = (Call) service.createCall();
        call.setSOAPActionURI(SOAPaction);
        call.setTargetEndpointAddress(new URL(EndPointURL));

        SOAPEnvelope env = new SOAPEnvelope(input);

        resp = call.invoke(env);
    } catch (Exception e) {
        e.printStackTrace();
        logger.fatal("Exception from send soap: " + e.toString());
        e.printStackTrace(System.out);
    }

    return resp;

}
