private HttpClient getHttpClient()
{
    HttpParams params = new BasicHttpParams();

    //Set main protocol parameters
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
    HttpProtocolParams.setUseExpectContinue(params, true);

    // Turn off stale checking.  Our connections break all the time anyway, and it's not worth it to pay the penalty of checking every time.
    HttpConnectionParams.setStaleCheckingEnabled(params, false);
    // FIX v2.2.1+ - Set timeout to 30 seconds, seems like 5 seconds was not enough for good communication
    HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
    HttpConnectionParams.setSoTimeout(params, 30 * 1000);
    HttpConnectionParams.setSocketBufferSize(params, 8192);

    // Don't handle redirects -- return them to the caller.  Our code often wants to re-POST after a redirect, which we must do ourselves.
    HttpClientParams.setRedirecting(params, false);

    // Register our own "trust-all" SSL scheme
    SchemeRegistry schReg = new SchemeRegistry();
    try
    {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        TrustAllSSLSocketFactory sslSocketFactory = new TrustAllSSLSocketFactory(trustStore);
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        Scheme sslTrustAllScheme = new Scheme("https", sslSocketFactory, 443);
        schReg.register(sslTrustAllScheme);
    }
    catch (Exception ex)
    {
        LogData.e(LOG_TAG, ex, LogData.Priority.None);
    }

    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params,schReg);
    return new DefaultHttpClient(conMgr, params);
}
