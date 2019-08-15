     private JMXServiceURL url() {
        final String url = String.format( "service:jmx:jmxmp://%s:%s", host(), port() );
        try {

           return new JMXServiceURL( url );

        } catch( Throwable exception ) {
           throw new RuntimeException( String.format( "Failed to create JMX Service URL: %s", url ), exception );
        }
     }

     private Map<String, Object> env() {
        final Map<String, Object> env = new LinkedHashMap<String, Object>();


         try {

             String keystore = "jmx.keystore";

             char keystorepass[] = "12345678".toCharArray();
             char keypassword[] = "12345678".toCharArray();

             KeyStore ks = KeyStore.getInstance("JKS");
             ks.load(new FileInputStream(keystore), keystorepass);
             KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

             kmf.init(ks, keypassword);
             SSLContext ctx = SSLContext.getInstance("TLSv1");
             ctx.init(kmf.getKeyManagers(), null, null);
             SSLSocketFactory ssf = ctx.getSocketFactory();

             env.put("jmx.remote.profiles", "TLS");
             env.put("jmx.remote.tls.socket.factory", ssf);
             env.put("jmx.remote.tls.enabled.protocols", "TLSv1");
             env.put("jmx.remote.tls.enabled.cipher.suites","SSL_RSA_WITH_NULL_MD5");


             env.put("jmx.remote.x.password.file", "password.properties");
             env.put("jmx.remote.x.access.file","access.properties");


         } catch (Exception e) {
             e.printStackTrace();
         }


         return env;
     }

     private MBeanServer server() {
        return ManagementFactory.getPlatformMBeanServer();
     }

     private JMXConnectorServer connector() {
        try {

           ServerProvider.class.getName();
           return JMXConnectorServerFactory.newJMXConnectorServer( url(), env(), server() );

        }catch( Throwable exception ) {
           throw new RuntimeException( "Failed to create JMX connector server factory", exception );
        }
     }
