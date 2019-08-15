public class SingletonClient extends DefaultHttpClient {

    private static SingletonClient instance = null;
    private static String userAgentString = "";

    public static synchronized SingletonClient getInstance() {
            if (instance == null) {
                HttpParams httpParameters = new BasicHttpParams();
                ConnManagerParams.setMaxTotalConnections(httpParameters, 100);
                HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
                int timeoutConnection = 40000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 40000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                HttpProtocolParams.setUseExpectContinue(httpParameters, false);
                HttpProtocolParams.setUserAgent(httpParameters, userAgentString + " " + HttpProtocolParams.getUserAgent(httpParameters));

                SchemeRegistry schemeRegistry = new SchemeRegistry();

                SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
                sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schemeRegistry.register(new Scheme("https", sf, 443));

                ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParameters, schemeRegistry);

                instance = new SingletonClient(cm, httpParameters);
            }
            return instance;
        }
}
