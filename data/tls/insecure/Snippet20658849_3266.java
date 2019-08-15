public SSLContext allowAllSSL() {
            SSLContext context = null; 
            TrustManager[] trustManagers = null;
            try{
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

             KeyStore keyStore = KeyStore.getInstance("pkcs12");
             InputStream in = cntx.getResources().openRawResource(R.raw.client_keystore);
             try {
             keyStore.load(in, "password".toCharArray());
             } catch (CertificateException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             } finally {
             in.close();
             }
             tmf.init(keyStore);


                if (trustManagers == null) { 
                        trustManagers = new TrustManager[] { new FakeX509TrustManager() }; 
                } 

                try { 
                        context = SSLContext.getInstance("SSL"); 
                        context.init(null, tmf.getTrustManagers(), new SecureRandom()); 
                } catch (NoSuchAlgorithmException e) { 
                        e.printStackTrace(); 
                } catch (KeyManagementException e) { 
                        e.printStackTrace(); 
                } 

           HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
           HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
               public boolean verify(String hostname, SSLSession session) {
                    return true;
                  }
                });
            }catch(Exception ex)
            {
                Log.e(TAG,"allowAllSSL failed: "+ex.toString());
            }
           return context;
        } 
