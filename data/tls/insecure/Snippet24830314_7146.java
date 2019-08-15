public static void createJsonRpcClient(Context ctx) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException, KeyManagementException {
      if(instance==null) {          
            //http://stackoverflow.com/questions/7615645
            Properties props=System.getProperties();
            props.put("jsse.enableSNIExtension", "false");
            //Configurando la conexion SSL
            SSLContext sc=SSLContext.getInstance("TLS");
            KeyStore ks = KeyStore.getInstance("PKCS12");
            AssetManager manager=ctx.getAssets();
            ks.load(manager.open("www.example.com.p12"), 
                      "Password".toCharArray());
            TrustManagerFactory tf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tf.init(ks);
            sc.init(null, tf.getTrustManagers(), null);
            //Configurando la autenticacion HTTP
            Authenticator auth=new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {                  
                    return new PasswordAuthentication("user", 
                            "pass".toCharArray());
                }
            };
            Authenticator.setDefault(auth);
            instance=new JsonRpcHttpClient(new URL("https://www.example.com/" +
                    "dbconnector/index.php"));
            instance.setSslContext(sc);
        }       
    }

    /**
     * 
     * @return La instancia del cliente JSON-RPC
     */
    public static JsonRpcHttpClient getJsonRpcClient() {        
        return instance;
    }
