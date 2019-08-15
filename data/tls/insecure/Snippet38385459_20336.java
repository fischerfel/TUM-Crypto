SSLContext ctx = null;
SSLSocketFactory socketFactory = null;
        KeyManagerFactory kmf;
KeyStore ks;
char[] passphrase = "abcd".toCharArray();

ctx = SSLContext.getInstance("TLS");
kmf = KeyManagerFactory.getInstance("SunX509");
ks = KeyStore.getInstance("JKS");

ks.load(new FileInputStream("C:/goahead.jks"), passphrase);

kmf.init(ks, passphrase);

ctx.init(kmf.getKeyManagers(), null, null);

socketFactory = ctx.getSocketFactory();
String endpoint = "https://myurl/goahead";
BindingProvider bindingProvider = (BindingProvider) goSOAP;    //goSOAP is derived from wsdl soap class
bindingProvider.getRequestContext()
.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
bindingProvider.getRequestContext()
.put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", socketFactory); 
