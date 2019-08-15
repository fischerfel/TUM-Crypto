private final String DEFAULT_HOST = "edge-mqtt.facebook.com";
private final int DEFAULT_PORT = 443;

public void connect(String protogle) throws Exception {

this.broker =  protogle + "://"+ DEFAULT_HOST + ":" + DEFAULT_PORT;
this.mqttClient = new MqttClient(broker,getMqttClientId() ,new MemoryPersistence() );

MqttConnectOptions connOpts = new MqttConnectOptions();
connOpts.setCleanSession(true);
connOpts.setKeepAliveInterval( MQTT_KEEPALIVE);
connOpts.setUserName( getMqttUsername() );
connOpts.setPassword( getMqttPassword().toCharArray() );
connOpts.setMqttVersion( 3 );
//connOpts.setSocketFactory(getSocketFactory (caCrtFile,crtFile,keyFile,password) );
Logger.w("Connecting to broker: "+broker);
Logger.w("isConnected:"+mqttClient.isConnected());
try {
    IMqttToken cn = mqttClient.connectWithResult(connOpts);
    Logger.w("connected");
}catch (MqttException me){
    System.out.println("reason "+me.getReasonCode());
    System.out.println("msg "+me.getMessage());
    System.out.println("loc "+me.getLocalizedMessage());
    System.out.println("cause "+me.getCause());
    System.out.println("excep "+me);
    return;
}



this.mqttClient.setCallback(new MqttCallback() {
    @Override
    public void connectionLost(Throwable me) {
        Logger.w("Connection lost");
        System.out.println("msg "+me.getMessage());
        System.out.println("loc "+me.getLocalizedMessage());
        System.out.println("cause "+me.getCause());
        System.out.println("excep "+me);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Logger.w("message Arrived");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Logger.w("deliverd--------");
        try {
            MqttDeliveryToken token  = (MqttDeliveryToken) iMqttDeliveryToken;
            String h = token.getMessage().toString();
            Logger.w("deliverd message :"+h);
        } catch (MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
});

public SSLSocketFactory getSocketFactory (final String caCrtFile, final String crtFile, final String keyFile,
                                          final String password) throws Exception
{
    Security.addProvider(new BouncyCastleProvider());

    // load CA certificate
    PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
    X509Certificate caCert = (X509Certificate)reader.readObject();
    reader.close();

    // load client certificate
    reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))));
    X509Certificate cert = (X509Certificate)reader.readObject();
    reader.close();

    // load client private key
    reader = new PEMReader(
            new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFile)))),
            new PasswordFinder() {
                @Override
                public char[] getPassword() {
                    return password.toCharArray();
                }
            }
    );
    KeyPair key = (KeyPair)reader.readObject();
    reader.close();

    // CA certificate is used to authenticate server
    KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
    caKs.load(null, null);
    caKs.setCertificateEntry("ca-certificate", caCert);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(caKs);

    // client key and certificates are sent to server so it can authenticate us
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(null, null);
    ks.setCertificateEntry("certificate", cert);
    ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, password.toCharArray());

    // finally, create SSL socket factory
    SSLContext context = SSLContext.getInstance("TLSv1");
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    return context.getSocketFactory();
}
}
