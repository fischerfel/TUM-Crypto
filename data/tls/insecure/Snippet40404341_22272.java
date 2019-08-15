private SSLSocketFactory newSslSocketFactory() {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = getApplicationContext().getResources().openRawResource(R.raw.muip);
            try {
                // Initialize the keystore with the provided trusted certificates
                // Provide the password of the keystore
                trusted.load(in, KEYSTORE_PASSWORD.toCharArray());
            } finally {
                in.close();
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);



            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory sf = new NoSSLv3Factory(context.getSocketFactory());
            HttpsURLConnection.setDefaultSSLSocketFactory(sf);





            return sf;
        } catch (Throwable e) {
            Log.e(TAG, "newSslSocketFactory: "+ e.getMessage(),e );
            throw new AssertionError(e);
        }
    }
