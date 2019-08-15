public class SSLCertificate {
    public static void disableSSLVerifier() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public static HttpClient getTestHttpClient(int port) {
        try {

            // Create all-trusting host name verifier
            X509HostnameVerifier allHostsValid = new X509HostnameVerifier() {

                public void verify(String host, SSLSocket ssl) throws IOException {
                    //Todo: missing implementation
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                    //Todo: missing implementation
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                    //Todo: missing implementation
                }

                public boolean verify(String s, SSLSession sslSession) {
                    return true;  //Todo: missing implementation
                }
            };
            org.apache.http.conn.ssl.SSLSocketFactory sf = new org.apache.http.conn.ssl.SSLSocketFactory(new TrustStrategy(){
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }, allHostsValid);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", port, sf));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(registry);
            return new DefaultHttpClient(ccm);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

}
