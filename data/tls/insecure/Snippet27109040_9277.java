String url = "https://organization.company.com/sites/Ateam/_vti_bin/ListData.svc/TableLoadInfo";        DefaultHttpClient client = new DefaultHttpClient();

        NTCredentials credentials = new NTCredentials(username, password, hostname, domain);
        AuthScope scope = new AuthScope(hostname, 443);

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[] { trustmanager }, null);

        SSLSocketFactory sf = new SSLSocketFactory(sslcontext,              SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme https = new Scheme("https", 443, sf);

        client.getConnectionManager().getSchemeRegistry().register(https);
        client.getCredentialsProvider().setCredentials(scope, credentials);

        HttpGet request = new HttpGet(url);
        //request.addHeader("Accept", "application/xml");
        request.addHeader("Accept", "application/json;odata=verbose");

        HttpResponse response = client.execute(request);
