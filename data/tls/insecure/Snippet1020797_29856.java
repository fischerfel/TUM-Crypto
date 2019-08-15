    int port = 2195;
    String hostname = "gateway.sandbox.push.apple.com";


    char []passwKey = "<keystorePassword>".toCharArray();
    KeyStore ts = KeyStore.getInstance("PKCS12");
    ts.load(new FileInputStream("/path/to/apn_keystore/cert.p12"), passwKey);

    KeyManagerFactory tmf = KeyManagerFactory.getInstance("SunX509");
    tmf.init(ts,passwKey);
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(tmf.getKeyManagers(), null, null);
    SSLSocketFactory factory =sslContext.getSocketFactory();
    SSLSocket socket = (SSLSocket) factory.createSocket(hostname,port); // Create the ServerSocket
    String[] suites = socket.getSupportedCipherSuites();
    socket.setEnabledCipherSuites(suites);
    //start handshake

    socket.startHandshake();


    // Create streams to securely send and receive data to the server
    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();



    // Read from in and write to out...
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(0); //The command
    System.out.println("First byte Current size: " + baos.size());

    baos.write(0); //The first byte of the deviceId length    
    baos.write(32); //The deviceId length

    System.out.println("Second byte Current size: " + baos.size());

    String deviceId = "<heaxdecimal representation of deviceId";
    baos.write(hexStringToByteArray(deviceId.toUpperCase()));
    System.out.println("Device ID: Current size: " + baos.size());


    String payload = "{\"aps\":{\"alert\":\"I like spoons also\",\"badge\":14}}";
    System.out.println("Sending payload: " + payload);
    baos.write(0); //First byte of payload length;
    baos.write(payload.length());
    baos.write(payload.getBytes());

    out.write(baos.toByteArray());
    out.flush();

    System.out.println("Closing socket..");
    // Close the socket
    in.close();
    out.close();
