HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
           HttpClient client = new DefaultHttpClient();

           SchemeRegistry registry = new SchemeRegistry();
           SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
           socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
           registry.register(new Scheme("https", socketFactory, 443));
           SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
           DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

           HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
           Log.v("URL:", Url[0]);
           HttpPost post = new HttpPost(Url[0]);  
           post.addHeader("Username", Url[1]);
           post.addHeader("Passwort", Url[2]);
           HttpResponse getResponse = httpClient.execute(post); //Wirft Exception
           HttpEntity responseEntity = getResponse.getEntity();
           UserID = Integer.parseInt(responseEntity.getContent().toString());
