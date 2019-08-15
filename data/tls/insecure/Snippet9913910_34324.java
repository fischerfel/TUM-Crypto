HttpClient httpclient = new DefaultHttpClient();
// truststore
KeyStore ts = KeyStore.getInstance("JKS", "SUN");
ts.load(PostService.class.getResourceAsStream("/truststore.jks"), "amber%".toCharArray());
// if you remove me, you've got 'javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated' on missing truststore
if(0 == ts.size()) throw new IOException("Error loading truststore");
// tmf
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ts);
// keystore
KeyStore ks = KeyStore.getInstance("PKCS12", "SunJSSE");
ks.load(PostService.class.getResourceAsStream("/" + certName), certPwd.toCharArray());
// if you remove me, you've got 'javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated' on missing keystore
if(0 == ks.size()) throw new IOException("Error loading keystore");
// kmf
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, certPwd.toCharArray());
// SSL
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
// socket
SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
Scheme sch = new Scheme("https", 8443, socketFactory);
httpclient.getConnectionManager().getSchemeRegistry().register(sch);
// request
HttpMethod get = new GetMethod("https://localhost:8443/foo");
client.executeMethod(get);
IOUtils.copy(get.getResponseBodyAsStream(), System.out);
