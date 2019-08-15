//add bouncy castle to the list of security providers
Security.insertProviderAt(new BouncyCastleProvider(), 1);

//load the trusted CA
KeyStore trusted = KeyStore.getInstance("BKS");
InputStream in = getResources().openRawResource(R.raw.mobilecastore);
trusted.load(in, "password".toCharArray());
in.close();
TrustManagerFactory trust_factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trust_factory.init(trusted);

//load the client keystore
KeyStore client = KeyStore.getInstance(KeyStore.getDefaultType());
InputStream client_in = getResources().openRawResource(R.raw.client);
client.load(client_in, "password2".toCharArray());
client_in.close();
KeyManagerFactory key_factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

key_factory.init(client, "password2".toCharArray());

SSLContext ssl_context = SSLContext.getInstance("SSL"); 
ssl_context.init(key_factory.getKeyManagers(), trust_factory.getTrustManagers(), null);

URL url = new URL("https", IP, 60000, "/cgi-bin/www_sel_jf");
connection = (HttpsURLConnection) url.openConnection();
connection.setSSLSocketFactory(ssl_context.getSocketFactory());
connection.setDoInput(true);
connection.setRequestMethod("GET");
connection.connect();
