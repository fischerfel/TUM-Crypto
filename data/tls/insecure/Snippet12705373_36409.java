// use local trust store (CA)
TrustManagerFactory tmf;
KeyStore trustedStore = null;
InputStream in = context.getResources().openRawResource(R.raw.mycatruststore); // BKS in res/raw
trustedStore = KeyStore.getInstance("BKS");
trustedStore.load(in, "insertBksPasswordHere".toCharArray());
tmf = TrustManagerFactory.getInstance("X509");
tmf.init(trustedStore);

// load client certificate
KeyStore clientKeyStore = loadClientKeyStore(getApplicationContext());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
kmf.init(clientKeyStore, "insertPasswordHere".toCharArray());

SSLContext context = SSLContext.getInstance("TLS");

// provide client cert - if server requires client cert this will pass
context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;

// connect to url
URL u = new URL("https://10.0.2.2:8888/");
HttpsURLConnection urlConnection = (HttpsURLConnection) u.openConnection();
urlConnection.setSSLSocketFactory(context.getSocketFactory());
urlConnection.setHostnameVerifier(hostnameVerifier);
urlConnection.connect();
System.out.println("Response Code: " + urlConnection.getResponseCode());
