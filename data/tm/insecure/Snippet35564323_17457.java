public static String getInspectorSchedulesProd(){
    //Local Variable Declaration
    //List<InspectorSchedule> returnValue = null;
    String returnValue = "";
    //String url = "";
    String content = "";
    javax.xml.soap.SOAPConnectionFactory connectionFactory = null;
    javax.xml.soap.SOAPConnection connection = null;
    javax.xml.soap.SOAPMessage soapResponse = null;
    java.io.ByteArrayOutputStream out = null;

    try{
        javax.net.ssl.HttpsURLConnection httpsConnection = null;

        // Create SSL context and trust all certificates
        javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("SSL");
        //content = "Current SSL Provider: " + sslContext.getProvider().getName() + "\n";

        for(java.security.Provider Item : java.security.Security.getProviders()){
            //content += Item.getName() + "\n";
        }

        javax.net.ssl.TrustManager[] trustAll = new javax.net.ssl.TrustManager[] {new TrustAllCertificates()};

        sslContext.init(null, trustAll, new java.security.SecureRandom());

        // Set trust all certificates context to HttpsURLConnection
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Open HTTPS connection
        java.net.URL url = new java.net.URL("https://www.twiddy.com/webservices/rnsportalws1.asmx");
        httpsConnection = (javax.net.ssl.HttpsURLConnection) url.openConnection();

        // Trust all hosts
        httpsConnection.setHostnameVerifier(new TrustAllHosts());

        // Connect
        httpsConnection.connect();

        content = "Content Type: " + httpsConnection.getContentType() + "\nResponse Message:" + httpsConnection.getResponseMessage() + "\nAllow User Interaction:" + httpsConnection.getAllowUserInteraction();

        // Send HTTP SOAP request and get response
        //connection = javax.xml.soap.SOAPConnectionFactory.newInstance().createConnection();

        //soapResponse = connection.call(createGetInspectorsSchedulesSoapMessageProd(), "https://url/portalws1.asmx");

        //Write the results from the request into the output stream
        //soapResponse.writeTo(out);

        //Convert the stream into a string
        //content = new String(out.toByteArray());

        returnValue = content;

        // Close connection
        //connection.close();
        httpsConnection.disconnect();
    }
    catch (Exception ex){
        returnValue = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(ex);
    }

    return  returnValue;
}

private static class TrustAllCertificates implements javax.net.ssl.X509TrustManager {
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {

    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[]{};
    }
}

private static class TrustAllHosts implements javax.net.ssl.HostnameVerifier {
    public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
        return true;
    }
}

private static javax.xml.soap.SOAPMessage createGetInspectorsSchedulesSoapMessageProd() throws Exception{
    //Local Variable Declaration
    String serverURI = "";
    javax.xml.soap.MessageFactory messageFactory = null;
    javax.xml.soap.SOAPMessage soapMessage = null;
    javax.xml.soap.SOAPPart soapPart = null;
    javax.xml.soap.SOAPEnvelope envelope = null;
    javax.xml.soap.SOAPBody soapBody = null;
    javax.xml.soap.SOAPElement soapBodyElem = null;
    javax.xml.soap.SOAPElement soapMethodParam = null;
    javax.xml.soap.MimeHeaders headers = null;

    //Set the Server Uri Namespace
    serverURI = "http://url/PortalWS1";

    //Init the Message Factory
    messageFactory = javax.xml.soap.MessageFactory.newInstance();

    //Init the Message
    soapMessage = messageFactory.createMessage();

    //Init the SOAP-specific portion of a Message
    soapPart = soapMessage.getSOAPPart();

    //Create the envelope that will wrap the message
    envelope = soapPart.getEnvelope();
    envelope.addNamespaceDeclaration("PortalWS1Soap", serverURI);

    //Get an instance of the body of the message
    soapBody = envelope.getBody();

    //Add the Soap Method we wish to call
    soapBodyElem = soapBody.addChildElement("GetInspectorScheds", "PortalWS1Soap");

    //Add the Access Key Parameter
    soapMethodParam = soapBodyElem.addChildElement("strAccessKey", "PortalWS1Soap");
    soapMethodParam.addTextNode("SF!435gFW");

    //Get an instance of the http message headers
    headers = soapMessage.getMimeHeaders();

    //Append our soap Action header to the message
    headers.addHeader("soapAction", "https://url/webservices/portalws1.asmx/GetScheds");

    //Save our chnages
    soapMessage.saveChanges();

    return soapMessage;
}
