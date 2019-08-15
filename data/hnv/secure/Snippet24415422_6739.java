PoolingClientConnectionManager cm = new PoolingClientConnectionManager();

// Increase max total connection to 10
cm.setMaxTotal(GlobalConstants.HTTP_CLIENT_MAX_TOTAL_CONNECTIONS);
HttpParams httpParameters = new BasicHttpParams();

int timeoutConnection = CONNECTION_TIMEOUT_MS_DEFAULT;
HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

HostnameVerifier hostnameVerifier = new MyHostnameVerifier();

SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
cm.getSchemeRegistry().register(new ch.boye.httpclientandroidlib.conn.scheme.Scheme("https", 443, socketFactory));
DefaultHttpClient httpClient = new DefaultHttpClient(cm, httpParameters);
