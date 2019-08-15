int qos = 1;
String broker = "tcp://xxx.example.in:5633";
String clientId = "JavaExample";
String user = "my_identity"; (my psk identity)
String pass = "6874e899071b909a2268b020ef9ff046cydfernjn90546n546nklkj565"; (my psk key)
MemoryPersistence persistence = new MemoryPersistence();

    MqttAsyncClient sampleClient = new MqttAsyncClient(broker,clientId,persistence);
    sampleClient.setCallback(new SimpleMqttCallBack());
    MqttConnectOptions connOpts = new MqttConnectOptions(); 
    connOpts.setUserName(user);
    connOpts.setCleanSession(false);
    System.out.println("Connecting to broker: " + broker);


    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");    
    sslContext.init(null,null,null);
    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    connOpts.setSocketFactory(socketFactory);   
    System.out.println("SSL Added");


    sampleClient.connect(connOpts);
    sampleClient.subscribe("#", qos);
