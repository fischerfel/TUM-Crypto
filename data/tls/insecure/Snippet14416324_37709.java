            InputStream keystoreContents = null;//Read it from a file, a byte array or whatever floats your boat
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(keystoreContetns, "changeme".toCharArray());
            SSLContext context = SSLContext.getInstance("TLSv1");
            context.init(new KeyManager[] { new MyKeyManager(keystore, "changeme") },
                            new TrustManager[] { new MyTrustManager() }, new SecureRandom());
            SSLSocketFactory sf = new SSLSocketFactory(context);
            Scheme httpsScheme = new Scheme("https", 443, sf);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(httpsScheme);
            ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
            HttpClient client = new DefaultHttpClient(cm);
            HttpPost post = new HttpPost("https://www.someserver.com");
