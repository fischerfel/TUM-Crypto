// Set system properties
System.setProperty("javax.net.ssl.trustStore", "C:\\Program Files\\Java\\jdk1.8.0_131\\jre\\lib\\security\\cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", KEYSTORE_PASSWORD);
        System.setProperty("javax.net.ssl.trustStoreType","jks");

// Open a secure connection.
        URL url = new URL(ENDPOINT_URL);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

// Set up the connection properties
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod("POST");

// Set up the user authentication portion of the handshake with the private key
        File pKeyFile = new File("C:\\privatekey.pfx");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream keyInput = new FileInputStream(pKeyFile);
        keyStore.load(keyInput, pKeyPassword.toCharArray());
        keyInput.close();
        keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        SSLSocketFactory sockFact = context.getSocketFactory();
        con.setSSLSocketFactory(sockFact);

// Send the request
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(soapMsgXml);
        writer.flush();
        writer.close();

// Check for errors
        int responseCode = con.getResponseCode();
        InputStream inputStream;
        if (responseCode == HttpURLConnection.HTTP_OK) 
        {
            inputStream = con.getInputStream();
        } 
        else 
        {
          inputStream = con.getErrorStream();
        }

// Process the response
        BufferedReader reader;
        String line = null;
        reader = new BufferedReader( new InputStreamReader( inputStream ) );
        while( ( line = reader.readLine() ) != null )
        {
          System.out.println( line );
        }

        inputStream.close();
