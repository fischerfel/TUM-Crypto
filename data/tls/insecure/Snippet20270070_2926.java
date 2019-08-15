public class VerifyKey extends AsyncTask<Void, Void, Void>{

    public static final String CERTIFICATE_TYPE_X_509 = "X.509";
    public static final String CERTIFICATE_ALIAS = "user_desktop";
    public static final String SERVER_URL = "https://192.168.1.111";
    @Override
    protected Void doInBackground(Void... params) {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = null;
        InputStream certificateInputStream = getApplicationContext().getResources().openRawResource(R.raw.user_desktop);
        Certificate certificate = null;
        try {
            cf = CertificateFactory.getInstance(CERTIFICATE_TYPE_X_509);
            certificate = cf.generateCertificate(certificateInputStream);
            Log.d(TAG, "Certificate : " + certificate.toString());
            Log.d(TAG, "Certificate public key : " + certificate.getPublicKey());
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } finally {
            if (certificateInputStream != null) {
                try {
                    certificateInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            if (keyStore != null) {
                keyStore.load(null, null);
                keyStore.setCertificateEntry(CERTIFICATE_ALIAS, certificate);
            } else {
                throw new RuntimeException("KeyStore is null");
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            if (tmf != null) {
                tmf.init(keyStore);
            } else {
                throw new RuntimeException("TrustManagerFactory is null");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");

            TrustManager[] trustManagers = tmf.getTrustManagers();
            sslContext.init(null, trustManagers, null);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = null;
        HttpsURLConnection httpsURLConnection =
                null;
        InputStream in = null;
        try {
            url = new URL(SERVER_URL);
            Log.d(TAG, "URL : "+url.toString());
            httpsURLConnection = (HttpsURLConnection)url.openConnection();
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            Log.d(TAG, "Socket factory : "+socketFactory.toString());
            httpsURLConnection.setSSLSocketFactory(socketFactory);

            in = httpsURLConnection.getInputStream(); //IOException exception gets triggered here

            Toast.makeText(getApplicationContext(), in.toString(), Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (SSLHandshakeException e){
            throw new RuntimeException(e);
        } catch(UnknownHostException e){
            throw new RuntimeException(e);
        } catch (ConnectException e1){
            throw new RuntimeException(e1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
