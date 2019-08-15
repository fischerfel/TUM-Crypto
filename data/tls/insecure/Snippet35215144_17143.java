public class APNSDriver {
    public static void main(String[] args) {
        APNSDriver.sendSamplePushNotification();
    }

    static SSLSocketFactory getSSLSocketFactory() {
        try {
            String keystoreFilename = "D:\\Oracle\\IDCS\\MFA\\APNS\\OMA_prereqs_backup\\iOS_prod.p12";
            char[] storepass = "welcome1".toCharArray();
            FileInputStream fis = new FileInputStream(
                    new File(keystoreFilename));

            final KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, storepass);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance("SunX509");
            keyManagerFactory.init(ks, storepass);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance("SunX509");
            trustManagerFactory.init((KeyStore) null);

            // create ssl context
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // setup the HTTPS context and parameters
            sslContext.init(keyManagerFactory.getKeyManagers(),
                    trustManagerFactory.getTrustManagers(), null);

            if (keyManagerFactory != null || trustManagerFactory != null) {
                return sslContext.getSocketFactory();
            }
        } catch (Exception e) {
            System.out.println("Unable to create ssl socket factory");
            e.printStackTrace();
        }
        return HttpsURLConnection.getDefaultSSLSocketFactory();
    }

    private static void sendSamplePushNotification() {
        URL url = null;
        try {
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };

            url = new URL("https://api.development.push.appl.com:443");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    "www-proxy.us.oracle.com", 80));
            HttpsURLConnection conn = (HttpsURLConnection) url
                    .openConnection(proxy);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setHostnameVerifier(hv);
            conn.setSSLSocketFactory(getSSLSocketFactory());
            OutputStream os = conn.getOutputStream();
            ..........
    }
}
