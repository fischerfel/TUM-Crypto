public class CertificateAcceptor {

    public void initializeTrustManager() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, createTrustManager(), new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private TrustManager[] createTrustManager() {

    TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // leave blank to trust all clients
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // leave blank to trust all servers
                    for (X509Certificate c : chain) {
                        System.out.println(c.toString());
                    }
                }

            }
        };
        return trustAllCerts;
    }

}
