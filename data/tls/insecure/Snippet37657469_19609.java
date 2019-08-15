public class CustomTrustCA {
    private static SSLContext mSSLContext = null;

    public static SSLSocketFactory getInstance() {
        if (mSSLContext == null && Init() == null) return null;
        return mSSLContext.getSocketFactory();
    }

    public static SSLContext Init() {
        Certificate ca = null;
        Certificate ca2 = null;
        InputStream caInput = null;
        InputStream caInput2 = null;
        KeyStore keyStore = null;
        TrustManagerFactory tmf = null;
        mSSLContext = null;

        //noinspection TryFinallyCanBeTryWithResources
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            caInput = Application.AppContext.getAssets().open("thawte.cer");
            ca = cf.generateCertificate(caInput);
            caInput2 = Application.AppContext.getAssets().open("thawte2.cer");
            ca2 = cf.generateCertificate(caInput2);
        } catch (Exception e) {
            Logger.Exception(e);
        } finally {
            try {
                if (caInput != null) caInput.close();
                if (caInput2 != null) caInput2.close();
            } catch (Exception ignored) {}
        }

        if (ca == null) return null;
        if (ca2 == null) return null;
        try {
            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            keyStore.setCertificateEntry("ca2", ca2);
        } catch (Exception e) {
            Logger.Exception(e);
        }

        try {
            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (Exception e) {
            Logger.Exception(e);
        }

        if (tmf == null) return null;
        try {
            // Create an SSLContext that uses our TrustManager
            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null, tmf.getTrustManagers(), null);
        } catch (Exception e) {
            Logger.Exception(e);
        }

        return mSSLContext;
    }
}
