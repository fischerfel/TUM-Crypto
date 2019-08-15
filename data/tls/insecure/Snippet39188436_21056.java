 `private void initSSL() throws Exception {
    Log.d(TAG,"initSSL");
    TrustManager tm = new CustomX509TrustManager();
    SSLContext customSSLContext = SSLContext.getInstance("TLSv1.1");
    customSSLContext.init(null , new TrustManager[] { tm } , null);
    sslSocketFactory = customSSLContext.getSocketFactory();

    HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);

    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            Log.d(TAG, String.format("Verifying %s , sessionId: %s", hostname, Arrays.toString(session.getId())));
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
}


public class CustomX509TrustManager implements X509TrustManager {
    private String TAG = "CustomX509TrustManager";
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        Log.d(TAG, "checkClientTrusted");
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException
    {
        Log.d(TAG, "checkServerTrusted");
        // Here you can verify the servers certificate. (e.g. against one which is stored on mobile device)

        InputStream inStream = null;
        try {
            inStream = getResources().openRawResource(R.raw.goserver_10y);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate ca = (X509Certificate) cf.generateCertificate(inStream);
            inStream.close();

            for (X509Certificate cert : certs) {
                // Verifing by public key
                cert.verify(ca.getPublicKey());
            }
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
            throw new CertificateException(e);
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        Log.d(TAG, "getAcceptedIssuers");
        return null;
    }
}`
