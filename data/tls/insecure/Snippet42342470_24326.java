public String MakeSSlCall(String meternum) {
    String message = "";
    FileWriter file = null;
    try {
        file = new FileWriter("C:\\SSLCERT\\ClientJavalog.txt");

    } catch (Exception ee) {
        message = ee.getMessage();
    }
    try {
        file.write("KeyStore Generated\r\n");
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream("C:\\SSLCERT\\NEWAEDCKSSKYE"), "skyebank".toCharArray());
        file.write("KeyStore Generated\r\n");
        Enumeration enumeration = keystore.aliases();
        while (enumeration.hasMoreElements()) {
            String alias = (String) enumeration.nextElement();
            file.write("alias name: " + alias + "\r\n");
            keystore.getCertificate(alias);
            file.write(keystore.getCertificate(alias).toString() + "\r\n");
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(keystore, "skyebank".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);
        file.write("KeyStore Stored\r\n");
        SSLContext context = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        KeyManager[] AllKeysMan = kmf.getKeyManagers();

        file.write("Key Manager Length is " + AllKeysMan.length + "\r\n");

        for (int i = 0; i < AllKeysMan.length; i++) {
            file.write("Key Manager At This Point is " + AllKeysMan[i] + "\r\n");
        }
        context.init(kmf.getKeyManagers(), trustManagers, null);
        SSLSocketFactory f = context.getSocketFactory();
        file.write("About to Connect to Ontech\r\n");
        SSLSocket c = (SSLSocket) f.createSocket("192.168.1.16", 4444);
        file.write("Connection Established to 196.14.30.33 Port: 8462\r\n");
        file.write("About to Start Handshake\r\n");
        c.startHandshake();
        file.write("Handshake Established\r\n");
        file.flush();
        file.close();
        return "Connection Established";
    } catch (Exception e) {
        try {
            file.write("An Error Occured\r\n");
            file.write(e.getMessage() + "\r\n");
            file.flush();
            file.close();
        } catch (Exception eee) {
            message = eee.getMessage();
        }
        return "Connection Failed";
    }
}
}
