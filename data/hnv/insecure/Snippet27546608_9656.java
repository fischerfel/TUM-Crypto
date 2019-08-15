Scn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                        // Open default camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // start the image capture Intent
                startActivityForResult(intent, 100);
            }
 }
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == 100 && resultCode == RESULT_OK) {


                 selectedImage = intent.getData();
                    imageBitmap = (Bitmap) intent.getExtras().get("data");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);

                imageView1.setImageBitmap(imageBitmap);

                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

               submitData(encodedImage);         
           }
}

public void submitData(final  String encodedImage) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Looper.prepare();
                        Log.d("looper", "-->>>>");
                        try 
                        {
                            isAuthorized = isAuthenticated(encodedImage);
                        } 
                        catch (Exception e) 
                        {
                            Log.e("Exception ==> ", e.toString());  
                        }
                        MHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(isAuthorized){
                                        if(AuthenticationResultJSONObject!=null){
                                            String message = AuthenticationResultJSONObject.getString("message");

                                            if(AuthenticationResultJSONObject.getString("status").equalsIgnoreCase("1"))
                                            {
                                                Toast.makeText(ScannerActivity.this,message,Toast.LENGTH_SHORT).show();
                                            }
                                            else if (AuthenticationResultJSONObject.getString("status").equalsIgnoreCase("0"))
                                            {
                                                Toast.makeText(ScannerActivity.this,message,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(ScannerActivity.this, "Loing unsuccessfull, please try again !", Toast.LENGTH_SHORT).show();
                                    }
                                } 
                                catch (Exception e) 
                                {
                                    e.printStackTrace();
                                    Log.e("Exception 146==> ", e.toString());  
                                }
                                finally 
                                {

                                    //progressdialog.dismiss();
                                    //progress.dismissProgressDialog();
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.e("Exception 153==> ", e.toString());  
                    }
                }
            }).start();

        } catch (Exception e) {
            Log.e("Exception 159==> ", e.toString());  
        }
    }
    public boolean isAuthenticated(final  String encodedImage) 
    {
        isAuthorized = false;
        String encoder1 = "";
        final String url="http://bestpeopleservice.com/projects/scanner/api/api/api.php?class=login&method=login_data&json=";


        String finalurl = "{\"data\":{\"qr_code\":\""+username1+"\",\"user_pass\":\""+password1+"\",\"longitude\":\""+longitude1+"\",\"latitude\":\""+latitude1+"\",\"adderss\":\""+adderss1+"\",\"images\":\""+encodedImage+"\"}}";//
        Log.v("267 1111111", "done url -> "+url+finalurl);

        try {
            encoder1 = URLEncoder
                    .encode(finalurl,"UTF-8");
            Log.v("267 222", "done url 2 -> "+url+encoder1);
        } 
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
                try {

                    if (isNetworkConnected()) {

                        AuthenticationResultJSONObject = new JSONObject(doFetchDataFromWebService_Simple_OnlyJSONResponse(url+encoder1));    

                        Log.v("Online", "User json array    ===  "+AuthenticationResultJSONObject);  

                    }else{

                        Toast.makeText(ScannerActivity.this, "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();

                    }
                }
                catch(Exception e){ 
                    e.getMessage();
                }
                finally{
                    if(AuthenticationResultJSONObject!=null){
                        isAuthorized = true;
                    }
                    else
                    {
                        isAuthorized=false;
                    }
                }
        //  }
    //  });
        return isAuthorized;
    }

    // Checking the internet connection
    public boolean isNetworkConnected() {
        ConnectivityManager connectivitymanagar = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanagar.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isConnectedOrConnecting()) {
            return false;
        } else {
            return true;
        }
    }



    public String doFetchDataFromWebService_Simple_OnlyJSONResponse(
            String WebServiceURL) {

        String responseBody = "";
        JSONObject jobj = new JSONObject();
        try {
            HttpGet WSHttpPost = null;
            HttpClient WSHttpClient = null;
            HttpResponse WSHttpResponse = null;

            WSHttpClient = getNewHttpClient();
            WSHttpPost = new HttpGet(WebServiceURL);
            WSHttpResponse = WSHttpClient.execute(WSHttpPost);

            Log.d("resp", ""+WSHttpResponse);
            //responseBody = EntityUtils.toString(WSHttpResponse.getEntity());
            responseBody = EntityUtils.toString(WSHttpResponse.getEntity(), "UTF-8");


            Log.v("response1", "" + responseBody);

        } catch (Exception e) {
            Log.e("Exception2 ==> ", e.toString());
        }
        if (responseBody == null || responseBody.equalsIgnoreCase("null")) {
            return "";
        } else {
            return responseBody;
        }
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

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
}
