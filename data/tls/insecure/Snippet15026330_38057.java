        CertificateFactory cf = CertificateFactory.getInstance("X.509"); 
        InputStream caInput = new BufferedInputStream(new FileInputStream("/sdcard/cacert.crt")); 
        Certificate ca; 
        try { 
            ca = cf.generateCertificate(caInput); 
        } finally { 
            caInput.close(); 
        } 

        // Create a KeyStore containing our trusted CAs 
        String keyStoreType = KeyStore.getDefaultType(); 
        KeyStore keyStore = KeyStore.getInstance(keyStoreType); 
        keyStore.load(null, null); 
        keyStore.setCertificateEntry("ca", ca); 



        SocketIO.setDefaultSSLSocketFactory(SSLContext.getInstance("SSL"));
        socket = new SocketIO("https://"+this.serverIpAddress+":3000/");
