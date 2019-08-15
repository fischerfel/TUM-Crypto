        NTCredentials credentials = new NTCredentials(username, password, hostname, domain);
        AuthScope scope = new AuthScope(hostname, 443);

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[] { trustmanager }, null);

        SSLSocketFactory sf = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme https = new Scheme("https", 443, sf);

        client.getConnectionManager().getSchemeRegistry().register(https);
        client.getCredentialsProvider().setCredentials(scope, credentials);

        // POST a data
        HttpPost request = new HttpPost(url);

                    request.addHeader("Accept", "application/json;odata=verbose");
        request.addHeader("Content-type", "application/json;odata=verbose");
        // request.addHeader("X-RequestDigest", FormDigestValue);
        request.addHeader("X-HTTP-Method", "POST");
        request.addHeader("If-Match", "*");

        JSONStringer json = (JSONStringer) new JSONStringer().object().key("Table_Name").value("TableName 1")
                .key("Load_Frequency").value("Weekly").key("Cycle").value("CURRENT").endObject();


        StringEntity se = new StringEntity(json.toString());
        request.setEntity(se);

                    HttpResponse response = client.execute(request);
