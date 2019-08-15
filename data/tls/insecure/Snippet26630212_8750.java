CertificateFactory cf =CertificateFactory.getInstance("X.509");
InputStreamcaInput caInput= new BufferedInputStream(getResources().openRawResource(R.raw.crtsrv));
Certificate ca;
try{
  ca = cf.generateCertificate(caInput);
  System.out.println(((X509Certificate) ca).getSubjectDN());
//This returns: OID.1.2.840.113549.1.9.1=#161C6265726E642E6672697363686D616E6E406F75746C6F6F6B2E636F6D, CN=IP, OU=UNIT, O=ORGANISATION, L=CITY, ST=STATE, C=AU
}finally{
  caInput.close();
}
String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore = KeyStore.getInstance(keyStoreType);
keyStore.load(null,null);
keyStore.setCertificateEntry("cn", ca);
String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);
URL url = new URL(link);
HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
//The statement above throws the exception, look down!

urlConnection.setSSLSocketFactory(context.getSocketFactory());
//After this there is the Outputstreamwriter & Bufferedreader to write
//and read the answer from the server,
//so far there is an exception above!
