String keyStorePassword = "123456";   
String configPath = "C:\\ssl\\";  
String keyStorePath = configPath + "client.ks";  
KeyStore ks = KeyStore.getInstance("jks");  
String trustStore = configPath + "trust.ts";  
java.lang.System.setProperty("javax.net.ssl.trustStore", trustStore);
java.lang.System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);

            InputStream ksIs = new FileInputStream(keyStorePath);
            try {
                ks.load(ksIs, keyStorePassword.toCharArray());
            } finally {
                if (ksIs != null) {
                    ksIs.close();
                }
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keyStorePassword.toCharArray());

            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            ConnectionFactory factory = new ActiveMQSslConnectionFactory(URL);
            sslContext.init(kmf.getKeyManagers(), trustAllCerts, new SecureRandom());       
            SslContext context = new SslContext();
            context.setSSLContext(sslContext);
            SslContext.setCurrentSslContext(context);
            Connection connection = factory.createConnection(loginName, pwd);
            connection.start();         
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer nonPersistentProducer = session.createProducer(null);
            session.close();
            connection.close();
