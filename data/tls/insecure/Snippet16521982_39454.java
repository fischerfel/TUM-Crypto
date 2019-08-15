SSLServerSocketFactory sslSrvFact = null;  
SSLContext ctx;
KeyManagerFactory kmf;
KeyStore ks;
// Load the self-signed server certificate

char[] passphrase = "hosttest".toCharArray();

ks = KeyStore.getInstance("BKS");
ks.load(SSLActivity.mContext.getResources().openRawResource(R.raw.hosttestcert), passphrase);
kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

kmf.init(ks, passphrase);

// Create a SSLContext with the certificate

ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(),null, null);

sslSrvFact = ctx.getServerSocketFactory();
myServerSocket =(SSLServerSocket)sslSrvFact.createServerSocket(port);
