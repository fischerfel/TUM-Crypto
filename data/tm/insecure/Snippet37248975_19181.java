public class GetCertificates {
    static private TrustManager[] trustmgr = new TrustManager[]{new X509TrustManager() {

        private X509Certificate[] certs = null;

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            System.out.println("checkClientTrusted");
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            System.out.println("checkServerTrusted");
        }

        public X509Certificate[] getAcceptedIssuers() {
            System.out.println("getAcceptedIssuers");
            certs = loadCertificatesFromCompanJks("C:/Users/vinod/Desktop/keystore.jks", "mypassword");
            // return new
            // X509Certificate[]{};
            return certs;
        }
    }};

    public void postMessage() {
        try {
            // here I prepare Url to execute and make a call
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static X509Certificate[] loadCertificatesFromCompanJks(String jksPath, String keyStorePassword) {
        try {
            X509Certificate X509Certificate[] = null;
            Certificate[] certs = null;

            ArrayList<X509Certificate> serverCerts = new ArrayList<X509Certificate>();
            FileInputStream is = new FileInputStream(jksPath);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            String password = keyStorePassword;
            keystore.load(is, password.toCharArray());

            Enumeration e = keystore.aliases();
            for (; e.hasMoreElements(); ) {

                String alias = (String) e.nextElement();
                Certificate cert = keystore.getCertificate(alias);
                X509Certificate cert1 = (X509Certificate) cert;
                serverCerts.add(cert1);
            }
            is.close();
            System.out.println("Number of server certificates : " + serverCerts.size());
            X509Certificate = (java.security.cert.X509Certificate[]) serverCerts.toArray();
            return X509Certificate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new GetCertificates().postMessage();
    }
}
