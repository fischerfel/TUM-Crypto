public class SSLSupported {

    // http://www.ibm.com/support/knowledgecenter/SSFKSJ_8.0.0/com.ibm.mq.dev.doc/q113220_.htm
    static String[] MQ_SUPPORTED = { "SSL_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
            "SSL_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
            "SSL_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
            "SSL_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
            "SSL_ECDHE_ECDSA_WITH_NULL_SHA",
            "SSL_ECDHE_ECDSA_WITH_RC4_128_SHA",
            "SSL_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
            "SSL_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "SSL_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
            "SSL_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
            "SSL_ECDHE_RSA_WITH_NULL_SHA", 
            "SSL_ECDHE_RSA_WITH_RC4_128_SHA",
            "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
            "SSL_RSA_FIPS_WITH_3DES_EDE_CBC_SHA",
            "SSL_RSA_FIPS_WITH_DES_CBC_SHA", 
            "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_RSA_WITH_AES_128_CBC_SHA", 
            "SSL_RSA_WITH_AES_128_CBC_SHA256",
            "SSL_RSA_WITH_AES_128_GCM_SHA256", 
            "SSL_RSA_WITH_AES_256_CBC_SHA",
            "SSL_RSA_WITH_AES_256_CBC_SHA256",
            "SSL_RSA_WITH_AES_256_GCM_SHA384", 
            "SSL_RSA_WITH_DES_CBC_SHA",
            "SSL_RSA_WITH_NULL_MD5", 
            "SSL_RSA_WITH_NULL_SHA",
            "SSL_RSA_WITH_NULL_SHA256", 
            "SSL_RSA_WITH_RC4_128_MD5",
            "SSL_RSA_WITH_RC4_128_SHA" };

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
            }

        } };

        context.init(null, trustAllCerts, new SecureRandom());

        SSLParameters params = context.getSupportedSSLParameters();
        String[] suites = params.getCipherSuites();
        System.out.println("Java version : " + System.getProperty("java.runtime.version"));
        System.out.println("Connecting with " + suites.length + " cipher suites supported:");

        List<String> mqSupported= Arrays.asList(MQ_SUPPORTED);

        for (int i = 0; i < suites.length; i++) 
            if (mqSupported.contains(suites[i])) 
                System.out.println(suites[i]);

    }

}

Java version : 1.7.0_75-b13
Connecting with 63 cipher suites supported:
SSL_RSA_WITH_3DES_EDE_CBC_SHA
SSL_RSA_WITH_RC4_128_SHA
SSL_RSA_WITH_RC4_128_MD5
SSL_RSA_WITH_DES_CBC_SHA
SSL_RSA_EXPORT_WITH_RC4_40_MD5
SSL_RSA_WITH_NULL_SHA
SSL_RSA_WITH_NULL_MD5
