 if(conn != null && conn.getServerCertificates() != null) {
        Utils.appendLog("Entered Logic to add certificates",false);
        Certificate ca = conn.getServerCertificates()[0];
        Log.i("","adding certificate is: " + ca.getPublicKey().toString());
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        InputStream readStream = new FileInputStream(c.getFilesDir() + "/VidyoRemote/keystore.jks");
        String pass = "Ready4Launch!";
        try{
            keyStore.load(readStream, pass.toCharArray());
        }catch (Exception e){
            Log.e("","error trying to load:" + e.getMessage());
            keyStore.load(null, pass.toCharArray());
        }
        keyStore.setCertificateEntry("ca", ca);
        FileOutputStream fos = new FileOutputStream(c.getFilesDir() + "/VidyoRemote/keystore.jks");
        keyStore.store(fos, pass.toCharArray());
        fos.close();
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
