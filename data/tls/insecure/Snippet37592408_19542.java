String url = "https://www.coles.com.au";
        String strContent = "";
        HttpURLConnection connection;

        AssetManager assetManager = context.getAssets();
        InputStream caInput = null;

        try{
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            caInput = assetManager.open("thawte_SSL_CA_G2.cer");
            Certificate ca = cf.generateCertificate(caInput);

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            URL urlObj = new URL(url);

            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)urlObj.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            InputStream in = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while(null != (strContent= reader.readLine())){
                System.out.println(strContent);
            }
        }
        catch (IOException e){
             String exception = e.getMessage();
            System.out.println(exception);
        }
