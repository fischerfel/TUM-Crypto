// Build SSL context with own KeyManager / TrustManager
SSLContext sc = SSLContext.getInstance("TLS");

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

KeyStore ks = KeyStore.getInstance("JKS");
String password = "changeit";
ks.load(getClass().getResourceAsStream("/keystore"), password.toCharArray());

kmf.init(ks, password.toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

// Now build webservice client
MyWS_Service service = new MyWS_Service(null, new QName("http://...", "MyWS"));
MyWS port = service.getMyWSSOAP();

BindingProvider bindingProvider = (BindingProvider) port;

// set to use own SSLContext
bindingProvider.getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", sc.getSocketFactory());
// set endpoint
bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://hostname:443/.../...");

// perform request
respObj = port.myRequest(myRequestObj);
