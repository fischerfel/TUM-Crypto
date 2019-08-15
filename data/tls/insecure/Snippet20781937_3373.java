 HttpsTransportSE  androidHttpTransport = new HttpsTransportSE(10.0.2.2, 8181, "/server/?wsdl", 10000);
             ((HttpsServiceConnectionSE) androidHttpTransport.getServiceConnection()).setSSLSocketFactory(trustAllHosts().getSocketFactory());


protected  SSLContext trustAllHosts()
{   
    return allowAllSSL();
}

 public SSLContext allowAllSSL() {
        SSLContext context = null; 
        TrustManager[] trustManagers = null;
        KeyManagerFactory mgrFact;
        try{
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        mgrFact = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

         KeyStore keyStore = KeyStore.getInstance("pkcs12");
         InputStream in = cntx.getResources().openRawResource(R.raw.keystore);
         try {
         keyStore.load(in, "password".toCharArray());
         mgrFact.init(keyStore, "password".toCharArray());
         } catch (CertificateException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         } finally {
         in.close();
         }
         tmf.init(keyStore);


            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() 
            {    
                    @Override 
                    public boolean verify(String hostname, SSLSession session) { 

                            return true; 
                    } 

            }); 


            if (trustManagers == null) { 
                    trustManagers = new TrustManager[] { new FakeX509TrustManager() }; 
            }

            final TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    System.out.println("getAcceptedIssuers");
                     return null;
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    System.out.println("Сведения о сертификате : " +       chain[0].getIssuerX500Principal().getName() + "\n Тип авторизации : " + authType);
                }
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    System.out.println("checkClientTrusted : " + authType);
                }
            } };
            //tmf.getTrustManagers()
            try { 
                    context = SSLContext.getInstance("TLS"); 
                    context.init(mgrFact.getKeyManagers(), trustAllCerts, new SecureRandom()); 
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
