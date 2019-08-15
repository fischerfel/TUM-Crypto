private class MobHawkCheck extends AsyncTask<String, Void, JSONObject> {

        protected JSONObject doInBackground(String... params) {
            JSONObject json = null;
            try {
                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                // From https://www.washington.edu/itconnect/security/ca/load-der.crt
                InputStream cert = mActivity.getResources().openRawResource(R.raw.mycertificate);
                InputStream caInput = new BufferedInputStream(cert);
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

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url = new URL("https://mysecureurl.com");
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection)url.openConnection();
                urlConnection.setSSLSocketFactory(context.getSocketFactory());

                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                json = new JSONObject(total.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject result) {
            //TODO parse the result JSONObject
        }
    }
