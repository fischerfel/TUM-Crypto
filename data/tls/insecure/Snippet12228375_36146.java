        httpServer=HttpsServer.create(...);
        ssl=SSLContext.getInstance("TLS");
        ...
        ssl.init(keyFactory.getKeyManagers(),trustFactory.getTrustManagers(),new SecureRandom());
        configurator=new HttpsConfigurator(ssl) {
           public void configure (HttpsParameters params) 
           {
               SSLContext context; 
               SSLParameters sslparams;

               context=getSSLContext();
               sslparams=context.getDefaultSSLParameters();
               sslparams.setNeedClientAuth(true);
               params.setSSLParameters(sslparams);
           }
       }; 
       ((HttpsServer)httpServer).setHttpsConfigurator(configurator);
       ...
       endPoint=getSunWsProvider().createEndPoint(...);
       httpContext=httpServer.createContext(...);
       endPoint.publish(httpContext);
       httpServer.start();
       ...
