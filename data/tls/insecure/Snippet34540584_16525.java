    try {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.load);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            //System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        ssl_context = SSLContext.getInstance("TLS");
        ssl_context.init(null, tmf.getTrustManagers(), null);
    } catch (Exception e) {
        Log.d("test1", "A: " + e);
    }

    Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().setTrustManagers(tmf.getTrustManagers());
    Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().setSSLContext(ssl_context);

    //test SSL
    Ion.getDefault(this).with(this)
            .load("https://na2b.no-ip.com/dragonair/can_app/api/media_list.php")
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null) {
                        Log.d("test1", "B: " + e);
                    } else {
                        Log.d("test1", "result" + result);
                    }
                }
            });
