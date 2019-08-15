  InputStream inputStream = null;
  DataOutputStream dStream = null;
            int responseCode;
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");

                Certificate ca;
                InputStream caInput = mApplicationContext.getResources()
                        .openRawResource(R.raw.ca);
                try {
                    ca = cf.generateCertificate(caInput);
                    Log.d(TAG, "ca=" + ((X509Certificate) ca).getSubjectDN());
                } finally {
                    try {
                        caInput.close();
                    } catch (IOException ioe) {
                        Log.e(TAG, "Exception happened while closing the stream", ioe);
                    }
                }

                // Create a KeyStore containing our trusted CAs
                String trustStoreType = KeyStore.getDefaultType();
                KeyStore trustStore = KeyStore
                        .getInstance(trustStoreType);
                trustStore.load(null, null);
                trustStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our
                // KeyStore
                String tmfAlgorithm = TrustManagerFactory
                        .getDefaultAlgorithm();
                final TrustManagerFactory tmf = TrustManagerFactory
                        .getInstance(tmfAlgorithm);
                tmf.init(trustStore);

                InputStream keyInput = mApplicationContext.getResources()
                        .openRawResource(R.raw.admin);
                KeyStore keystore = KeyStore.getInstance("PKCS12");
                keystore.load(keyInput, CA_PASSWORD.toCharArray());

                KeyManagerFactory kmf = KeyManagerFactory
                        .getInstance(KeyManagerFactory
                                .getDefaultAlgorithm());
                kmf.init(keystore, CA_PASSWORD.toCharArray());

                // Create an SSLContext that uses our TrustManager
                SSLContext sslCtx = SSLContext.getInstance("TLS");
                sslCtx.init(kmf.getKeyManagers(),
                        tmf.getTrustManagers(), null);

                URL url = new URL(urlStr);
                HttpsURLConnection conn = (HttpsURLConnection) url
                        .openConnection();
                conn.setDoOutput(true);
               conn.setHostnameVerifier(new HostnameVerifier() {

                    @SuppressLint("BadHostnameVerifier")
                    @Override
                    public boolean verify(String hostname,
                                          SSLSession session) {

                        /*
                         * As the certificate is self signed
                         */
                        return true;

                    }
                });

                // Tell the URLConnection to use a SocketFactory from our SSLContext
                conn.setSSLSocketFactory(sslCtx
                        .getSocketFactory());
                conn.setRequestProperty("Content-Type", appContentType);

                conn.setRequestMethod("POST");
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);

                String paramStr = getPostFormParamString(params);
                Log.d(TAG, "connection post param String: " + paramStr  );
                dStream = new DataOutputStream(
                        conn.getOutputStream());
                dStream.writeBytes(paramStr);

                responseCode = conn.getResponseCode();
