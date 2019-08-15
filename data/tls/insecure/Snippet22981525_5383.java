    /**
     * Return the SSLContext for use with our HttpClient or create a new Context
     * if needed.
     * <p>
     * This context uses our {@link NetworkTrustManager}
     * 
     * @return an {@link SSLContext}
     */
    public SSLContext getSSLContext() {

        if (mSSLContextInstance != null)
            return mSSLContextInstance;

        try {
            mSSLContextInstance = SSLContext.getInstance("TLS");
            TrustManager trustManager = new NetworkTrustManager(getKeyStore());
            TrustManager[] tms = new TrustManager[] { trustManager };
            mSSLContextInstance.init(null, tms, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage());
        } catch (KeyManagementException e) {
            Log.e(TAG, e.getMessage());
        }

        return mSSLContextInstance;
    }
