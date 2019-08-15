try {
// I have a TrustManagerFactory object
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
// I have a KeyStore considering BKS (BOUNCY CASTLE) KeyStore object
KeyStore ks = KeyStore.getInstance("BKS");
// I have configured a inputStream using my TrustStore file as a Raw Resource
inStream = ctx.getApplicationContext().getResources().openRawResource(R.raw.mytruststore);
// I have loaded my Raw Resource into the KeyStore object
ks.load(inStream, null);
inStream.close();
// I have initialiazed my Trust Manager Factory, using my Key Store Object
tmf.init(ks);
// I have created a new SSL Context object
SSLContext sslContext = SSLContext.getInstance("TLS");
// I have initialized my new SSL Context, with the configured Trust Managers found on my Trust Store
sslContext.init(null, tmf.getTrustManagers(), null);
// I have configured a HttpClientStack, using my brand new Socket Context
final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
hurlStack = new HurlStack(null, sslSocketFactory);
} catch (Exception e){
Log.d("Exception:",e.toString());
}
