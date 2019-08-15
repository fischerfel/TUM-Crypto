public class NukeSSLCerts {
public static void nuke() {
    try {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                        return myTrustedAnchors;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        try {
                            certs[0].checkValidity();
                        } catch (Exception e) {
                            throw new CertificateException("Certificate not valid or trusted.");
                        }
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        try {
                            certs[0].checkValidity();
                        } catch (Exception e) {
                            throw new CertificateException("Certificate not valid or trusted.");
                        }
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    } catch (Exception e) {
    }
}
