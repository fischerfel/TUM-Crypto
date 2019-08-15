try {

            // Things to Note 1 : Bypass default Trust Managers
            TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
            }};

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inn = getAssets().open("keystore.cer");
            Certificate ca;
            try {
                ca = cf.generateCertificate(inn);
                Log.i("TEST", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                inn.close();
            }


            String keyStoreType = "BKS";
            KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, byPassTrustManagers, null);

            // Things to Note 2 : Don't use "localhost" ,instead use IP
            URL url = new URL("https://192.168.56.1:8443/RestHTTPS/JavaCodeGeeks/AuthorService/authors");
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url.openConnection();

            urlConnection.setSSLSocketFactory(context.getSocketFactory());

            // Things to Note 3 : Allow all host
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            Log.i("TEST", urlConnection.getResponseMessage() + "");

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            String str = "";
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                str += current;
            }

            Log.i("TEST", "" + str);

        } catch (Exception e) {
            Log.e("TEST", e.toString());
            e.printStackTrace();
        }
