CertificateFactory cf = CertificateFactory.getInstance("X.509");
// load ca                  
InputStream caInput = getActivity().getResources().getAssets().open("qa_upld.cer");
Certificate ca;
try {
    ca = cf.generateCertificate(caInput);
    System.out.println("ca = " + ((X509Certificate) ca).getSubjectDN());
} finally {
    caInput.close();
}
// Create a KeyStore 
KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
kmf.init(keyStore, null);
KeyManager[] keyManagers = kmf.getKeyManagers(); 

// Create a TrustManager that trusts the CAs in our KeyStore
//TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
//tmf.init(keyStore); 
// Using trustmanager not working for some reason..

// Create an SSLContext that uses our TrustManager
    // Out of 3 possibilities, (keymanager,trustmanager),(null,trustmanager) 
    // and (keymanager,null) only the following works
SSLContext context = SSLContext.getInstance("TLS");
context.init(keyManagers, null, null);
