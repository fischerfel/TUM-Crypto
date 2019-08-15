KeyStore ks = KeyStore.getInstance("BKS");
android.content.res.Resources res = <getter for resources>;
InputStream inputStream = res.openRawResources(R.raw.cert); //The converted bks certificate stored in the raw directory
ks.load(inputStream, "password".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);
SSLContext ssc = SSLContext.getInstance("TLS");
ssc.init(null, tmf.getTrustManagers(), null);
... same as above ...
ssls.startHandshake();
