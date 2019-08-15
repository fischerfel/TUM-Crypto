final MqttConnectOptions connnOpt = new MqttConnectOptions();
connnOpt.setCleanSession(false);  
connnOpt.setKeepAliveInterval(2000);
connnOpt.setUserName(user.getUsername());
connnOpt.setPassword(user.getSDPApiDriver().getToken().toCharArray());
connnOpt.setConnectionTimeout(20000); 
String sslCert="/mycert.crt";
System.setProperty("javax.net.ssl.trustStore", sslCert);
System.setProperty("javax.net.ssl.trustStorePassword", "expectBrilliance");
System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
KeyStore ks = KeyStore.getInstance("JKS");
InputStream jksInputStream = this.getClass().getResourceAsStream(sslCert);
ks.load(jksInputStream, "expectBrilliance".toCharArray());  //thrown exception
KeyManagerFactory kmf = 
KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, "expectBrilliance".toCharArray());
TrustManagerFactory tmf = 
TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);
SSLContext sc = SSLContext.getInstance("TLS");
TrustManager[] trustManagers = tmf.getTrustManagers();
sc.init(kmf.getKeyManagers(), trustManagers, null);
SSLSocketFactory ssf = sc.getSocketFactory();
connnOpt.setSocketFactory(ssf);
String locationTopic = "abcd/efgh/" + dongleId + "/events";
final MqttClient client =  new MqttClient(clientDevice.getBrokerUrl(), dongleId,null);
client.connect(connnOpt);
System.out.println("client status : " + client.isConnected());
