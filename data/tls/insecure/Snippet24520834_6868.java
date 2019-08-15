String message = "Hello World";
try{
    // Load the server keystore
    KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(ctx.getResources().openRawResource(R.raw.mykeystore), "MY_PASSWORD".toCharArray());

    // Create a custom trust manager that accepts the server self-signed certificate
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);

    // Create the SSLContext for the SSLSocket to use
    SSLContext sslctx = SSLContext.getInstance("TLS");
    sslctx.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

    // Create SSLSocketFactory
    SSLSocketFactory factory = sslctx.getSocketFactory();

    // Create socket using SSLSocketFactory
    SSLSocket client = (SSLSocket) factory.createSocket("SERVER_IP_ADDRESS", 8081);

    // Print system information
    System.out.println("Connected to server " + client.getInetAddress() + ": " + client.getPort());

    // Writer and Reader
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

    // Send request to server
    System.out.println("Sending request: "+message);
    writer.write(message);
    writer.newLine();
    writer.flush();

    // Receive response from server
    String response = reader.readLine();
    System.out.println("Received from the Server: "+response);

    // Close connection
    client.close();

    return response;
} catch(Exception e) {
    e.printStackTrace();
}
return "Something went wrong...";
