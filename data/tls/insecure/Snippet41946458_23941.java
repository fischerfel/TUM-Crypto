MqttClient client = new MqttClient("ssl://yourbroker:8883", MqttClient.generateClientId(), new MemoryPersistence());

SSLContext sslContext = SSLContext.getInstance("SSL");
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore keyStore = readKeyStore();
trustManagerFactory.init(keyStore);
sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

MqttConnectOptions options = new MqttConnectOptions();
options.setSocketFactory(sslContext.getSocketFactory());

client.connect(options);
