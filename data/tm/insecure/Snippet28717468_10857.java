TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };


        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }


        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };


        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://17.91.15.84:8088/login");
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("userid", "user1")); 
        parameters.add(new BasicNameValuePair("password", "user1$"));
                    post.setEntity(new UrlEncodedFormEntity(parameters));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }           
    }
