CallerRemote remote=IGGetEJB.getEJBAccess3("ejbUser","1","127.0.0.1","8080"); 

  public static CallerRemote getEJBAccess3(String uName, String uPass,String serverHost, String serverPort) {  


     String serverUrl = "http-remoting://" + serverHost + ":" + serverPort; // serverPort обычно 4447  
     Hashtable<String, Object> params = new Hashtable<String, Object>();  
     params.put(Context.PROVIDER_URL, serverUrl);  
     params.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");  
     params.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");   
     params.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "true");   
//    params.put(Context.SECURITY_PRINCIPAL, uName);  //java.naming.security.principal  
//    params.put(Context.SECURITY_CREDENTIALS, uPass);//java.naming.security.credentials  


     Properties clientProp = new Properties();  
     clientProp.put("remote.connections", "default");  
     clientProp.put("remote.connection.default.host", serverHost);  
     clientProp.put("remote.connection.default.port", serverPort);  
     clientProp.put("remote.connection.default.username", uName);  
//    clientProp.put("remote.connection.default.password",uPass);  

    /* try { 
  MessageDigest md = MessageDigest.getInstance("SHA-256"); 
  byte[] passwordBytes = uPass.getBytes(); 
  byte[] hash = md.digest(passwordBytes); 
  String passwordHash = Base64.getEncoder().encodeToString(hash); 
  System.out.println("password hash: "+passwordHash);     
  clientProp.put("remote.connection.default.password", "a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s="); 
  } catch (NoSuchAlgorithmException e1) { 
  // TODO Auto-generated catch block 
  e1.printStackTrace(); 
  }*/  


     clientProp.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "true");  
//    clientProp.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");  
     clientProp.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "true");  

     EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(clientProp);  
     ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(cc);  
     EJBClientContext.setSelector(selector);  
//    EJBClientContext.getCurrent().registerInterceptor(0, new ClientInterceptor());  

     try {  
  InitialContext context = new InitialContext(params);    

  final String jndiName = "/TestRemoteEJBEAR/CallerBean!remote.CallerRemote";  
  CallerRemote remote = (CallerRemote) context.lookup(jndiName);  
// CallerRemote remote = connectEjb(context, jndiName);  
  return remote;  
  } catch (NamingException e) {  
  // TODO Auto-generated catch block  
  e.printStackTrace();  
  }  
  return null;  
  }  
