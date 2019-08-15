public class SslPoke {

    private static javax.net.ssl.SSLSocketFactory getFactorySimple() throws Exception {
        SSLContext context = SSLContext.getInstance("TLSv1");

        context.init(null, null, null);

        return context.getSocketFactory();

    }

    public static void main(String[] args) {
        System.getProperties().setProperty("javax.net.debug", "ssl");
        System.getProperties().setProperty("https.cipherSuites", "TLS_RSA_WITH_AES_256_CBC_SHA");

        try {
            String urlStr ="https://<your host>:<your port>";
            URL url = new URL(urlStr);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            javax.net.ssl.SSLSocketFactory sslSocketFactory = getFactorySimple();

            connection.setSSLSocketFactory(sslSocketFactory);
            InputStream in = connection.getInputStream();

            while (in.available() > 0) {
                System.out.print(in.read());
            }
            System.out.println("Successfully connected");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
