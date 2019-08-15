   //Start connection
    URL url = new URL(address);
    HttpURLConnection con  = (HttpURLConnection) url.openConnection();

    //Set HTTP header properties
    con.setConnectTimeout(60000);   
    con.setRequestMethod("POST");
    con.setUseCaches(true);
    con.setRequestProperty("Content-type", "text/xml");
    con.setRequestProperty("Content-Length", Integer.toString(xml.length()));
    con.setRequestProperty("SOAPAction", address);
    con.setDoOutput(true);
    con.setDoInput(true);

    userPass = username + ":" + password;
    byte[] encodeBytes = Base64.encodeBase64(userPass.getBytes());
    String encode = new String(encodeBytes);
    con.setRequestProperty("Authorization", "Basic " + encode);

    //Set up HTTPS
    HttpsURLConnection httpsConnection = (HttpsURLConnection) con;
    try {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore kStore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(new File(ERPGetProperty.erpGetProperty("pathToKeyStore")));
        TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        try {
            kStore.load(fis, keyStorePass);
            kmFactory.init(kStore, keyStorePass);
            tmFactory.init(kStore);

            context.init(kmFactory.getKeyManagers(), tmFactory.getTrustManagers(), new SecureRandom());
            httpsConnection.setSSLSocketFactory(context.getSocketFactory());
            httpsConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

            });

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }

    } catch(Exception e) {
        e.printStackTrace();
    }


    out = httpsConnection.getOutputStream();
