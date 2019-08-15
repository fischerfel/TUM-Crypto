//Get Cert
try {
   InputStream CertInputStream = a_sContext.getResources().openRawResource(R.raw.server);

   //Read Cert
   CertificateFactory CertFactory = CertificateFactory.getInstance("X.509");
   Certificate Cert = CertFactory.generateCertificate(CertInputStream);
   String Result = "Ca=" + ((X509Certificate) Cert).getSubjectDN();
   Log.d("test", Result); //This Returns correct Cert information

   //Create a keystore
   KeyStore MyKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
   MyKeyStore.load(null, null);
   MyKeyStore.setCertificateEntry("ca", Cert);

   //Create a TrustManager
   TrustManagerFactory TMF = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
   TMF.init(MyKeyStore);

   //Create a SSLContext
   m_pSSLContext = SSLContext.getInstance("TLS");
   m_pSSLContext.init(null, TMF.getTrustManagers(), null);
   HttpsURLConnection.setDefaultSSLSocketFactory(m_pSSLContext.getSocketFactory());

   //Try and connect to the website
   URL MyURL = new URL(m_sSecureHttp + m_sWebsite + m_sTestPath);
   con = (HttpsURLConnection) MyURL.openConnection();
   con.connect();
   Result = new String();
   BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));;
   while((Line = reader.readLine())!= null) {
        Result += Line;
   }
}
catch (Exception e) { e.printStackTrace(); }
