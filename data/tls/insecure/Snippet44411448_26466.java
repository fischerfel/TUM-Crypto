StandardWebSocketClient simpleWebSocketClient =
               new StandardWebSocketClient();



SSLContext sslContext = SSLContext.getInstance("TLS");
InputStream is = new FileInputStream("/path_to_cert/certs/localhost.crt");
CertificateFactory cf = CertificateFactory.getInstance("X.509");
X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

System.out.println(caCert.getIssuerX500Principal().getName());
System.out.println(caCert.getSubjectX500Principal().getName());

TrustManagerFactory tmf = TrustManagerFactory
         .getInstance(TrustManagerFactory.getDefaultAlgorithm());
       KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

ks.load(null);

tmf.init(ks);
sslContext.init(null, tmf.getTrustManagers(), null);

Map<String, Object> props = new HashMap<>();
props.put("org.apache.tomcat.websocket.SSL_CONTEXT", sslContext);

simpleWebSocketClient.setUserProperties(props);

List<Transport> transports = new ArrayList<>(1);
transports.add(new WebSocketTransport(simpleWebSocketClient));

SockJsClient sockJsClient = new SockJsClient(transports);
WebSocketStompClient stompClient =
               new WebSocketStompClient(sockJsClient);
stompClient.setMessageConverter(new MappingJackson2MessageConverter());

String url = "wss://localhost:8443/chat";

String plainCredentials= ...;
String userId = ...;
StompSessionHandler sessionHandler = new ChatStompSessionHandler(userId);
String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());
final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
headers.add("Authorization", "Basic " + base64Credentials);
StompSession session = stompClient.connect(url, headers, sessionHandler).get();
