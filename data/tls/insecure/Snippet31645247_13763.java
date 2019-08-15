public static void testServerA(List keyManagerFactoryList, List trustManagerFactoryList) {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
    try {

        SSLContext sc = SSLContext.getInstance("TLS"); 

        KeyManagerFactory kmf = (KeyManagerFactory)keyManagerFactoryList.get(0);
        TrustManagerFactory tmf = (TrustManagerFactory)trustManagerFactoryList.get(0);  
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLSocket client = (SSLSocket) f.createSocket("localhost", 7777);
        printSocketInfo(client);
        client.startHandshake();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String content;

        while ((content=reader.readLine())!= null) {

            System.out.println("Client: " + content);
            writer.newLine();
            writer.flush();                 
            writer.close();
            reader.close();
            client.close();             
        }
    } catch (Exception e) {
        System.err.println(e.toString());
    }       
}
