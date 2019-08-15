SSLContext sc = SSLContext.getInstance("SSL"); 
sc.init(null, trustAllCerts, new java.security.SecureRandom()); 

Map<String, Object> ctxt = ((BindingProvider) wsport ).getRequestContext(); 
ctxt.put(JAXWSProperties.SSL_SOCKET_FACTORY, sc.getSocketFactory()); 
