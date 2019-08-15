   public static <T> T buildServerWsdl(String endpointWsdl,final String username,final String password,
        final Class<T> serviceClass,boolean ignoreSSLCertificate,boolean useAuthorizationBasic,Map<String,String> supplierheaders) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException{

    //Controllo wsdlurl
    URL wsdlURL;
    java.io.File wsdlFile = new java.io.File(endpointWsdl);

    if (wsdlFile.exists()) {
        wsdlURL = wsdlFile.toURI().toURL();
    } else {
        wsdlURL = new URL(endpointWsdl);
    }
    System.out.println(wsdlURL);


    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();            
    factory.setServiceClass(serviceClass);          
    factory.setAddress(endpointWsdl);
    //Abilita il loggin in ingresco ed uscita dei messaggi soap!    
    factory.getInInterceptors().add(new LoggingInInterceptor(4*1024));
    factory.getOutInterceptors().add(new LoggingOutInterceptor(4*1024));    

    @SuppressWarnings("unchecked")
    T server = (T) factory.create();            

    // The BindingProvider interface provides access to the protocol binding and
    // to the associated context objects for request and response message processing.
    BindingProvider prov = (BindingProvider)server;
    Binding binding = prov.getBinding(); 
    ((SOAPBinding)binding).setMTOMEnabled(true);
    //Add handlers to the binding jaxb 
    java.util.List<javax.xml.ws.handler.Handler> handlers = binding.getHandlerChain();
    handlers.add(new JaxWsLoggingHandler());
    binding.setHandlerChain(handlers);

    Map<String, Object> req_ctx = prov.getRequestContext();
    req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointWsdl);
    Map<String, List<String>> headers = new HashMap<String, List<String>>();

    if(username != null && password != null){   
        headers.put("Username", Arrays.asList(username));
        headers.put("Password", Arrays.asList(password));
        //headers.put("Content-Type", Arrays.asList("text/xml")); //necessario specificare se si usa schema-core invece di XmlSchema

        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

        if(supplierheaders !=null &&  supplierheaders.size() > 0){
            prov.getRequestContext().putAll(supplierheaders);
            for(Map.Entry<String, String> entry : supplierheaders.entrySet()){
                headers.put(entry.getKey(), Arrays.asList(entry.getValue()));
            }
        }

        Authenticator myAuth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        };
        Authenticator.setDefault(myAuth);                       
    }

    if(useAuthorizationBasic){
        String authorization = new sun.misc.BASE64Encoder().encode((username+":"+password).getBytes());
        headers.put("Authorization", Arrays.asList("Basic " + authorization));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        //MessageContext mctx = wsctx.getMessageContext();
        Map<String, List<String>> http_headers = (HashMap<String, List<String>>) req_ctx.get(MessageContext.HTTP_REQUEST_HEADERS);
        List list = (List) http_headers.get("Authorization");
        if (list == null || list.size() == 0) {
            throw new RuntimeException("Authentication failed! This WS needs BASIC Authentication!");
        }

        String userpass = (String) list.get(0);
        userpass = userpass.substring(5);
        byte[] buf = org.apache.commons.codec.binary.Base64.decodeBase64(userpass.getBytes());
        String credentials = new String(buf);         
        String usernamex = null;
        String passwordx = null;
        int p = credentials.indexOf(":");
        if (p > -1) {
            usernamex = credentials.substring(0, p);
            passwordx = credentials.substring(p+1);
        }   
        else {
            throw new RuntimeException("There was an error while decoding the Authentication!");
        }
        // This should be changed to a DB / Ldap authentication check 
        if (usernamex.equals(username) && passwordx.equals(password)) {              
            //System.out.println("============== Authentication Basic OK =============");
        }
        else {
            throw new RuntimeException("Authentication failed! Wrong username / password!");
        }
    } 
    //Client cl = ClientProxy.getClient(server);
    org.apache.cxf.endpoint.Client cl = org.apache.cxf.frontend.ClientProxy.getClient(server);
    //=============================================================================================
    // Set up WS-Security Encryption
    // Reference: https://ws.apache.org/wss4j/using.html
    Map<String, Object> inProps = new HashMap<String, Object>();      
    //props.put(ConfigurationConstants.EXPAND_XOP_INCLUDE_FOR_SIGNATURE, false);
    //props.put(ConfigurationConstants.EXPAND_XOP_INCLUDE, false);       
    //inProps.put("expandXOPIncludeForSignature", false);
    //inProps.put("expandXOPInclude", false);
    //WSS4JOutInterceptor wss4jOut = new WSS4JOutInterceptor(inProps);

    //ClientProxy.getClient(client).getOutInterceptors().add(wss4jOut);
    //cl.getInInterceptors().add(wss4jOut);
    //cl.getOutInterceptors();
    //==============================================================================================

    HTTPConduit httpConduit = (HTTPConduit) cl.getConduit();

    //disable ssl certificate handshake
    if(ignoreSSLCertificate){
        String targetAddr = httpConduit.getTarget().getAddress().getValue();
        if (targetAddr.toLowerCase().startsWith("https:")) {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {            
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {return new java.security.cert.X509Certificate[0];}
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
            } };

            // Ignore differences between given hostname and certificate hostname
            //HostnameVerifier hv = new HostnameVerifier(){public boolean verify(String hostname, SSLSession session) { return true; }};

            TLSClientParameters tlsParams = new TLSClientParameters();
            tlsParams.setTrustManagers(trustAllCerts);
            tlsParams.setDisableCNCheck(true);
            httpConduit.setTlsClientParameters(tlsParams);

            //SSLContext sc = SSLContext.getInstance("SSL");
            //sc.init(null, trustAllCerts, new SecureRandom());

        }
    }

    AuthorizationPolicy authorizationPolicy = httpConduit.getAuthorization();
    authorizationPolicy.setUserName(username);
    authorizationPolicy.setPassword(password);

    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
    httpClientPolicy.setConnectionTimeout(10000);//10sec
    httpClientPolicy.setReceiveTimeout(60000);

    httpClientPolicy.setContentType("application/soap+xml"); 

    httpConduit.setClient(httpClientPolicy);
    return server;              
}
