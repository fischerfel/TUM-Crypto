   final String URL = "https://mywolla.com/mobwebservices/svalidateuser.php?wsdl";

   String response = null;
            try {

                 HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                   DefaultHttpClient client = new DefaultHttpClient();

                   SchemeRegistry registry = new SchemeRegistry();
                   SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                   socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                   registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                   registry.register(new Scheme("https", socketFactory, 443));
                   SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
                   DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

                   // Set verifier      
                   HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                   // Example send http request

                   HttpPost httpPost = new HttpPost(URL);

                   HttpResponse response1 = httpClient.execute(httpPost);
