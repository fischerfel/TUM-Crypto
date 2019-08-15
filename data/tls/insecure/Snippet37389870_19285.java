        String configName = "d:/config.txt";

        SunPKCS11 sunpkcs11 = new SunPKCS11(configName);
        Security.addProvider(sunpkcs11);
        KeyStore keyStore = null;

        keyStore = KeyStore.getInstance("PKCS11",sunpkcs11);
        keyStore.load(null, pin.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, pin.toCharArray());


        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), null, null);
        SSLContext.setDefault(ctx);
        final SSLSocketFactory factory = ctx.getSocketFactory();
        final SSLSocket socket = (SSLSocket) factory.createSocket("xx.xx.xx.xx", 443);

        socket.startHandshake();   

        PrintWriter out = new PrintWriter(socket.getOutputStream());

        String fileName = "/Login";
        out.print("GET " + fileName + " HTTP/1.0\r\n");
        out.print("\r\n");
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        while ((line = in.readLine()) != null)
          System.out.println(line);
