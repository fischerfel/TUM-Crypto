public class SecuredXmppConnectionImpl extends XmppConnectionImpl {

    public SecuredXmppConnectionImpl(XMPPTCPConnectionConfiguration config) {
        super(config);
    }

    /**
     * Returns default configuration for Old SSL port method
     * 
     * @param serviceName
     * @param port
     * @return
     * @throws GeneralSecurityException 
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static XMPPTCPConnectionConfiguration getConfig(String serviceName,
            int port) throws KeyManagementException, NoSuchAlgorithmException {

            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(
                    null, // KeyManager not required
                    new TrustManager[] { new DummyTrustManager() },
                    new java.security.SecureRandom());
            SSLSocketFactory ssf = sslcontext.getSocketFactory();
            return XMPPTCPConnectionConfiguration.builder()
                    .setServiceName(serviceName).setHost(serviceName).setPort(port)
                    .setSocketFactory(ssf).build(); 
    }

    static class DummyTrustManager implements X509TrustManager {

        public boolean isClientTrusted(X509Certificate[] cert) {
            return true;
        }

        public boolean isServerTrusted(X509Certificate[] cert) {
            try {
                cert[0].checkValidity();
                return true;
            } catch (CertificateExpiredException e) {
                return false;
            } catch (CertificateNotYetValidException e) {
                return false;
            }
        }

        public void checkClientTrusted(X509Certificate[] x509Certificates,
                String s) throws CertificateException {
            // Do nothing for now.
        }

        public void checkServerTrusted(X509Certificate[] x509Certificates,
                String s) throws CertificateException {
            // Do nothing for now.
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
