ApiVersion1Service apiVersion1Service = new ApiVersion1Service(wsdlURL, SERVICE_NAME);
    APIport = apiVersion1Service.getApiVersion1Port();

    // SOAP Header
    BindingProvider bindingProvider = (BindingProvider) APIport;
    Binding binding = bindingProvider.getBinding();
    Map<String, Object> requestContext = bindingProvider.getRequestContext();

    requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL + accountID);
    List<Handler> handlers = binding.getHandlerChain();
    handlers.add(new SOAPAuthenticationHandler(username, password));
    binding.setHandlerChain(handlers);

    // SSL
    SSLContext context = SSLContext.getInstance("SSL");
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
    } };
    context.init(null, trustAllCerts, new SecureRandom());
    SSLSocketFactory sslSocketFactory = context.getSocketFactory(); 
    bindingProvider.getRequestContext().put(/*JAXWSProperties.SSL_SOCKET_FACTORY*/ "com.sun.xml.ws.transport.https.client.SSLSocketFactory" , sslSocketFactory);
