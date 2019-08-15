        My_Service service=new My_Service();
        MyServicePort servicePort=service.getServicePort();

        BindingProvider bp = (BindingProvider)servicePort;

        // In case of 1-Way SSL, only the Truststore is necessary
        TrustManagerFactory tmf= TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore)null);
        TrustManager[] trustMgr= tmf.getTrustManagers();
        KeyStore trustStore = KeyStore.getInstance("JKS");
        FileInputStream truststoreFile = new FileInputStream("truststore.jks");
        trustStore.load(truststoreFile, "truststore_password".toCharArray());
        truststoreFile.close();
        tmf.init(trustStore);

        // In case of 2-Way SSL, keystore and  truststore are necessary
        KeyManagerFactory kmf= KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init((KeyStore)null,null);
        KeyManager[] keyMgr = kmf.getKeyManagers();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        FileInputStream keystoreFile = new FileInputStream("keystore.jks");
        keyStore.load(keystoreFile, "keystore_password".toCharArray()); 
                keystoreFile.close();
        kmf.init(keyStore, "keystore_password".toCharArray());

        SSLContext context = SSLContext.getInstance("SSL");//"SSLv3"
        context.init(keyMgr, trustMgr, null);
        //context.getSocketFactory();
        bp.getRequestContext().put(JAXWSProperties.SSL_SOCKET_FACTORY, context.getSocketFactory());
        bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,"username");
        bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,"password");
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://xxxxxxx....");

**ERROR IN THIS LINE** //List<MyEntity> list= servicePort.getEntityList();
        for(MyEntity e:list){
            System.out.println(e.getName());
        }
