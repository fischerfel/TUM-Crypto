protected Void doInBackground(String... urls) {

    try {

        if(Some condition){
            disableSSLCertificateChecking();
            HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(host + "discovery.jsp");

                SSLSocketFactory sf = (SSLSocketFactory)httpClient.getConnectionManager()
                        .getSchemeRegistry().getScheme("https").getSocketFactory();
                sf.setHostnameVerifier(new AllowAllHostnameVerifier());                    
                HttpResponse response = null;

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {                                              
                }

                //making POST request.
                try {                  
                    response = httpClient.execute(httpPost);
                }
