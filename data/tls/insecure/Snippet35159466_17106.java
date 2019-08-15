String serverUrl = Constants.BASE_URL + mAction;
        if (mParams != null) {
            serverUrl += "?" + mParams;
        }
        Log.d("usm_serverUrl",serverUrl);
        try {

            //-----------------------------------------------------------------------

            // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput  = ctx.getResources().openRawResource(R.raw.route2school_be);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                Log.d("usm_ca","ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

// Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            //--------------------------------------------------

            URL url = new URL(serverUrl);


            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(context.getSocketFactory());

              conn.setRequestProperty("User-Agent", "application/json");
              conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String response = IOUtils.toString(in, "UTF-8");
            Log.d("debug", "get-url: " + serverUrl + "\n post-response: " + response);
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
