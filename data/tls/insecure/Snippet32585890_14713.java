public static void getSSLConnection() {

        try {
            String jkspath = "C://Users//myuser//Desktop//clientkeystore"
                    ,jkspass="jkspassword",proxy = "proxy.com", port = "8080";

            URL url = new URL("https://TRIED_URL");

            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("http.proxyHost", proxy);
            systemProperties.setProperty("http.proxyPort", port);
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("username",
                            "password".toCharArray());
                }
            });
            HttpsURLConnection urlConnection = (HttpsURLConnection) url
                    .openConnection();
            // Load Trusted Certs into a KeyStore Object
            KeyStore ksCACert = KeyStore.getInstance(KeyStore.getDefaultType());
            ksCACert.load(new FileInputStream(jkspath),
                    jkspass.toCharArray());
            // Initialise a TrustManagerFactory with the CA keyStore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ksCACert);
            // Create new SSLContext using our new TrustManagerFactory
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            // Get a SSLSocketFactory from our SSLContext
            SSLSocketFactory sslSocketFactory = context.getSocketFactory();
            // Set our custom SSLSocketFactory to be used by our
            // HttpsURLConnection instance
            urlConnection.setSSLSocketFactory(sslSocketFactory);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
