Log.e("body", body);
            HttpClient httpclient = getNewHttpClient();

            HttpPost httppost = new HttpPost("https://www.demo.com/login.php");

             nameValuePairs = new ArrayList<NameValuePair>();
nameValuePairs.add(new BasicNameValuePair("username", username));
nameValuePairs.add(new BasicNameValuePair("password", password));
httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


            httppost.setEntity(entity);
            response = httpclient.execute(httppost);;


            Log.e("response", response.getStatusLine().toString());
            String aaa = response.getStatusLine().toString();
            Toast.makeText(getApplicationContext(), "Content :"+ aaa, Toast.LENGTH_LONG).show();
            HttpEntity entity1 = response.getEntity();
            is = entity1.getContent();

                //Toast.makeText(getApplicationContext(), "Content :"+ (CharSequence) is, Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }


public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
