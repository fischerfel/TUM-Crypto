// Load CAs from an InputStream
                    // (could be from a resource or ByteArrayInputStream or ...)
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
                    AssetManager am = getAssets();
                    InputStream caInput = new BufferedInputStream(am.open("your_cert.crt"));
                    Certificate ca;
                    try {
                        ca = cf.generateCertificate(caInput);
                        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                    } finally {
                        caInput.close();
                    }
                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

               TrustManager[] trustManagers = tmf.getTrustManagers();
final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

TrustManager[] wrappedTrustManagers = new TrustManager[]{
   new X509TrustManager() {
       public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return origTrustmanager.getAcceptedIssuers();
       }

       public void checkClientTrusted(X509Certificate[] certs, String authType) {
           origTrustmanager.checkClientTrusted(certs, authType);
       }

       public void checkServerTrusted(X509Certificate[] certs, String authType) {
           try {
               origTrustmanager.checkServerTrusted(certs, authType);
           } catch (CertificateExpiredException e) {}
       }
   }
};

SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, wrappedTrustManagers, null);
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 




                // Tell the URLConnection to use a SocketFactory from our SSLContext
                AppLog.LogE(getClass().getName(), "Licensing Url is " + request.toString());
                URL url = new URL(request.toString());
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection)url.openConnection();
                urlConnection.setSSLSocketFactory(context.getSocketFactory());
                InputStream in = urlConnection.getInputStream();
                String line = "";
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                while ((line = rd.readLine()) != null) {
                    strResponse += line;
                }
