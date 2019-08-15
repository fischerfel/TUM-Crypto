HttpsURLConnection con = (HttpsURLConnection) req.openConnection();
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, // No Key Manager
             new TrustManager[] { new X509TrustManager()
               {
                 @Override
                 public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                     throws CertificateException
                   {}

                 @Override
                 public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                     throws CertificateException {
                      // check the certs
                 }

                 @Override
                 public X509Certificate[] getAcceptedIssuers()
                   {
                     return null;
                   }

               } }, // TrustManager 
             new java.security.SecureRandom());
con.setSSLSocketFactory(context.getSocketFactory());
