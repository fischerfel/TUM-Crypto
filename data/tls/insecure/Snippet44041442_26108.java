       System.setProperty("javax.net.debug", "ssl");
           try {
           String configName = "softhsm2.cfg";
           Provider p = new SunPKCS11(configName);
           System.out.println(p.getName());
           Security.addProvider(p);


           // Load the key store
           char[] pin = "5678".toCharArray();
           KeyStore ks = KeyStore.getInstance("PKCS11", p);
           ks.load(null, pin);
           System.out.println(ks.size());
          Enumeration<String> aliases = ks.aliases();
           for(;aliases.hasMoreElements();)
           {
            System.out.println(aliases.nextElement());
           }
           KeyManagerFactory keyManagerFactory =     KeyManagerFactory.getInstance("SunX509");
         //Add to keystore to key manager
           keyManagerFactory.init(ks, pin);  

         //Create the context

           SSLContext context = SSLContext.getInstance("TLS");
           context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            //Create a socket factory
           SSLServerSocketFactory ssf = context.getServerSocketFactory();
            //SSLSocketFactory sf = context.getSocketFactory();
           //Create the socket
            SSLServerSocket s   = (SSLServerSocket) ssf.createServerSocket(8888);
             printServerSocketInfo(s);
             SSLSocket c = (SSLSocket) s.accept();
