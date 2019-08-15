public static void allowAllSSL() 
{
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(final String hostname, final SSLSession session) {
            return true;
        }
    });
    SSLContext context = null;
    try {
        context = SSLContext.getInstance("TLS");
        context.init(null, sTrustManagers, new SecureRandom());
    } catch (final NoSuchAlgorithmException catchException) {
        LoggerFactory.consoleLogger().printStackTrace(catchException);
    } catch (final KeyManagementException catchException) {
        LoggerFactory.consoleLogger().printStackTrace(catchException);
    }
    mFakeFactory = context.getSocketFactory();
    HttpsURLConnection.setDefaultSSLSocketFactory(mFakeFactory);
}
