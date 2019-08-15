CertificateFactory cf = CertificateFactory.getInstance("X.509");

// Generate the certificate using the certificate file under res/raw/cert.cer
InputStream caInput = new BufferedInputStream(getResources().openRawResource(R.raw.cert));
Certificate ca = cf.generateCertificate(caInput);
caInput.close();

// Create a KeyStore containing our trusted CAs
String keyStoreType = KeyStore.getDefaultType();
KeyStore trusted = KeyStore.getInstance(keyStoreType);
trusted.load(null, null);
trusted.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(trusted);

// Create an SSLContext that uses our TrustManager
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);

SSLSocketFactory sf = context.getSocketFactory();
mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new HurlStack(null, sf));
