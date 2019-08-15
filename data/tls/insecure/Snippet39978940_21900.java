 CertificateFactory cf = CertificateFactory.getInstance("X.509","BC");

 InputStream caInput = getResources().openRawResource(R.raw.root);  //here "R.raw.root" is a .bks file converted from my CA's .crt file , and a .bks file converted from my server's .crt file , but both throw this exception
 Certificate ca;
 try
{
     ca = cf.generateCertificate(caInput);

} finally

{
      caInput.close();
}

// Create a KeyStore containing our trusted CAs
//String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore = KeyStore.getInstance("BKS");
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(),null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
URL url = new URL("https://172.18.13.178:63123/Home/Test");
HttpsURLConnection urlConnection =(HttpsURLConnection) url.openConnection();
urlConnection.setSSLSocketFactory(context.getSocketFactory());
InputStream in = urlConnection.getInputStream();

//omit other code
}
