java.io.IOException: HTTPS hostname wrong:  should be <XXXX.XXXX.XXX>
    at sun.net.www.protocol.https.HttpsClient.checkURLSpoofing(Unknown Source)
    at sun.net.www.protocol.https.HttpsClient.afterConnect(Unknown Source)
    at sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection.connect(Unknown Source)
    at sun.net.www.protocol.http.HttpURLConnection.getInputStream(Unknown Source)
    at sun.net.www.protocol.https.HttpsURLConnectionImpl.getInputStream(Unknown Source)
    at protocol.URL_Certificate.main(URL_Certificate.java:55)

public class URL_Certificate {
    public static void main(String[] args) {
        String urlPlugin = "https://XXXX.XXXX.XXX/signin";

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] chain,
                        final String authType) {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain,
                        final String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            InputStream input;
            BufferedReader br;
            String line = "";

            // All set up, we can get a resource through https now:
            final URLConnection urlCon1 = new URL(urlPlugin).openConnection();
            // Tell the url connection object to use our socket factory which
            // bypasses security checks
            ((HttpsURLConnection) urlCon1)
                    .setSSLSocketFactory(sslSocketFactory);

            input = urlCon1.getInputStream();
            br = new BufferedReader(new InputStreamReader(input));
            line = "";
            while ((line = br.readLine()) != null) {
                // TODO: Need to Remove
                System.out.println(line);

            }
            br.close();
            input.close();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
