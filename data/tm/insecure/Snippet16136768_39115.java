   X509TrustManager tm = new X509TrustManager()
   {
     public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException
     {
     }

     public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
     }

     public X509Certificate[] getAcceptedIssuers() {
       return null;
     }
   };
   ctx.init(null, new TrustManager[] { tm }, null);

   SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


   ClientConnectionManager ccm = base.getConnectionManager();

   SchemeRegistry sr = ccm.getSchemeRegistry();

   sr.register(new Scheme("https", 443, ssf));

   return new DefaultHttpClient(ccm, base.getParams());
 }
 catch (NoSuchAlgorithmException nsaex)
 {
   throw new AGException(nsaex, "Not able to get the SSL Context");
 }
 catch (KeyManagementException kmex) {
     throw new AGException(kmex, "Not able to get the SSL Context");
 }
