// Set key for SSL connection
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
    config.setKeystoreType("AndroidCAStore");
    config.setKeystorekeyPath(null);
} else {
    config.setKeystoreType("BKS");
    String keyPath = System.getProperty("javax.net.ssl.trustStore");
    if (keyPath == null)
        keyPath = System.getProperty("java.home") + File.separator + "etc"
                + File.separator + "security" + File.separator + "certs.bks";
        config.setKeystorekeyPath(keyPath);
    }
}

// Now set custom SSL to configuration
try {
    SSLContext ssl = SSLContext.getInstance("TLS");
    ssl.init(null, new TrustManager[]{new TLSUtils.AcceptAllTrustManager()}, null);
    ssl.getServerSessionContext().setSessionTimeout(10 * 1000);
    config.setCustomSSLContext(ssl);
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (KeyManagementException e) {
    e.printStackTrace();
}

config.setSecurityMode(Connectionconfig.SecurityMode.required);
// config is type of XMPPTCPConnectionConfiguration
