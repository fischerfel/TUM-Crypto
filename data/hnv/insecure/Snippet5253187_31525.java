HostnameVerifier hostnameVerifier = 
    org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

DefaultHttpClient dummy = new DefaultHttpClient();

SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

SchemeRegistry registry = new SchemeRegistry();
registry.register(new Scheme("https", socketFactory, 8443));

SingleClientConnManager mgr = new SingleClientConnManager(
    dummy.getParams(), registry);
DefaultHttpClient client = new DefaultHttpClient(mgr, dummy.getParams());

// make connection with 'client' now.
