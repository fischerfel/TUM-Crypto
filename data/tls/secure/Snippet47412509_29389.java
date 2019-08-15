protected void checkTls() {
    if (android.os.Build.VERSION.SDK_INT < 21) {
        try {
            ProviderInstaller.installIfNeededAsync(this, new ProviderInstaller.ProviderInstallListener() {
                @Override
                public void onProviderInstalled() {
                    SSLContext sslContext = null;
                    try {
                        sslContext = SSLContext.getInstance("TLSv1.2");
                        sslContext.init(null, null, null);
                        SSLEngine engine = sslContext.createSSLEngine();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onProviderInstallFailed(int i, Intent intent) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
