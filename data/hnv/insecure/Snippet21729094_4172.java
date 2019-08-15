public class MyAsyncTask1 extends AsyncTask<Object, Integer, String> {

    ResultsListener listener;


    public void setOnResultsListener(ResultsListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Object... params) {
        // TODO Auto-generated method stub
        String res = null;
        DefaultHttpClient responseBody = null;
        if (params.length > 0) {
            String mUrl =  params[0].toString();
            List<NameValuePair> mParams = (List<NameValuePair>) params[1];

            //String pam = "param1="+"dsd"+"&param2="+mobile+"&param3=" + "sds"+"&param4="+"sdsds"+"&param5="+"dsds"+"&param6="+Images;
            //String regx = "[] ";
            //char[] ca = regx.toCharArray();
            //for (char c : ca) {
              //  pam = pam.replace(""+c, "");
               // pam = pam.replace(",", "&");

          //  }

            try{

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);

                 SchemeRegistry registry = new SchemeRegistry();
                    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                    registry.register(new Scheme("https", socketFactory, 443));


                socketFactory.setHostnameVerifier(socketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


                DefaultHttpClient httpclient= new DefaultHttpClient();              
                httpclient.getConnectionManager().getSchemeRegistry().register(registry);
                HttpPost httpget = new HttpPost(mUrl);
                 httpget.setEntity(new UrlEncodedFormEntity(mParams));
            //  System.out.println("executing request " + httpget.getRequestLine());

                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                //System.out.println("----------------------------------------");
                //System.out.println(response.getStatusLine());
                //if (entity != null) {
                //   System.out.println("Response content length:  " + entity.getContentLength());
                //}

                // Print html.
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = in.readLine()) != null) {
                    res=line; 
                    System.out.println(line);
                }
                in.close();

                }catch(Exception e){

                    return res=e.toString();

                }

            }
        return res;
    }
    @Override
    protected void onPostExecute(String result) {
    //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
        if (!listener.equals(null)) {
            listener.onResultsSucceeded(result);
        }
    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);

            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

}
