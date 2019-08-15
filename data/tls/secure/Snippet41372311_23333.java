SSLContext ctx = null;
String keystoreName = "/Users/user/ec_key/in_keystore";
char[] password = "123456".toCharArray();       //keystore's password

    FileInputStream fIn;
    KeyStore keystore;
    TrustManagerFactory tmf=null;

    try {
        fIn = new FileInputStream(keystoreName);
        keystore = KeyStore.getInstance("JKS");
        keystore.load(fIn, password);               //loading keysotre

        tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());   //TrustManagerFactory.getDefaultAlgorithm()=PKIX
        tmf.init(keystore);

        ctx = SSLContext.getInstance("TLSv1.2");
        // Initial SSLContext
        ctx.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

        fIn.close();

    } catch (FileNotFoundException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (KeyStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (CertificateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (KeyManagementException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

        // create SSLConnectionSocketFactory
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(ctx);

    CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setSSLSocketFactory(factory)
            .disableAutomaticRetries()
            .build();

//execute http method
HttpResponse httpResponse = httpClient.execute(method);
