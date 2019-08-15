private void processCommonRequest(String url, HashMap<String, String> params) throws Exception {
    URL endpoint = new URL(url);

    //MessageDigest md = MessageDigest.getInstance("MD5"); 

    //System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
    //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

    // Create SOAP connection
    SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
    SOAPConnection connection = scf.createConnection();

    // Create a message from the message factory.
    MessageFactory mf = MessageFactory.newInstance();
    SOAPMessage msg = mf.createMessage();

    // Get the SOAP Part from the message
    SOAPPart soapPart = msg.getSOAPPart();

    // Get the SOAP Envelope from the SOAP Part
    SOAPEnvelope envelope = soapPart.getEnvelope();
    envelope.addNamespaceDeclaration("SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/");
    envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/1999/XMLSchema");
    envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/1999/XMLSchema-instance-instance");
    envelope.addNamespaceDeclaration("tns", "http://api.cj.com");
    envelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
    // Remove empty header from the Envelope
    envelope.getHeader().detachNode();

    // Create a soap body from the Envelope.
    SOAPBody body = envelope.getBody();
    body.addNamespaceDeclaration("soap-env", "http://schemas.xmlsoap.org/soap/encoding/");

    // SOAPBodyElement item = body.addBodyElement(envelope.createName("GeScore") );
    SOAPBodyElement item = body.addBodyElement(envelope.createName(
            "GeScore", "soap-env", "http://schemas.xmlsoap.org/soap/encoding/"));

    for (String keyMap : params.keySet()) {
        addItem(envelope, keyMap, params.get(keyMap), item);
    }

    System.out.println("\nContent of the message: \n"); // FIXME
    msg.writeTo(System.out);

    // Send the SOAP message and get reply
    System.err.println("\nSending message to URL: " + endpoint); // XXX
    SOAPMessage reply = connection.call(msg, endpoint);

  // ... nevermind what later ... .call function throws error ....

    connection.close();
}
