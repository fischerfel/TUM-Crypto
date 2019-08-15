protected String doInBackground(String... args) {
                try {

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                DefaultHttpClient client = new DefaultHttpClient();
                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
                DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());



                HttpPost post = new HttpPost("https://test-api.smart-trial.dk/api");
                List <NameValuePair> nvps = new ArrayList <NameValuePair>(2);
                nvps.add(new BasicNameValuePair("path[pathwayId]","5566e151817a62021b1ea809"));
                nvps.add(new BasicNameValuePair("formData[firstname]","Name"));
                nvps.add(new BasicNameValuePair("formData[lastname]","XXX"));
                nvps.add(new BasicNameValuePair("formData[email]","example@mail.com"));

                 post.addHeader("Referer" ,"https://myurl.com/api");
                 post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));


                //DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(post);
                HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        // get the response content as a string
                        String stringResponse = EntityUtils.toString(entity);

                        Log.d("Response", stringResponse);
                    }
