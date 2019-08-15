public class DropboxApi extends AsyncTask<Void, Long, Boolean> {
    public String token;
    public DropboxApi(String a) {
        // TODO Auto-generated constructor stub
        token=a;
    }
    String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    public String Sign(String token)// throws Exception
    {
        String uri = "https://api.dropbox.com/1/account/info";
        //Uri json = GetResponse(uri);
        //return ParseJson<Account>(json);
        uri+="?";
        String queryParams = "oauth_consumer_key=2f2y1dyuqhp58ek&oauth_token="+token+"&oauth_nonce=6880853&oauth_timestamp=";

         java.util.Date date= new java.util.Date();
         //System.out.println(new Timestamp(date.getTime()));

         queryParams+=(new Timestamp(date.getTime()).toString())+"&oauth_signature_method=HMAC-SHA1&oauth_version=1.0&oauth_signature=";

         uri+=queryParams;
         Mac mac=null;
        try {
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         Key key=new SecretKeySpec("2f2y1dyuqhp58ek".getBytes(), HMAC_SHA1_ALGORITHM) ;
         try {
            mac.init(key);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         byte[] rawHmac = mac.doFinal(queryParams.getBytes());
         String result=new String(Base64.encode(rawHmac,Base64.DEFAULT));
         uri+=result;
         return uri;


    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO Auto-generated method stub


            HttpClient httpclient = new DefaultHttpClient();   
            HttpPost httpPost = new HttpPost(Sign(token));
            int k=0;
            k=23;
            HttpResponse response;
            try {
                int j=3;
                j=0;
                response = httpclient.execute(httpPost); // the request executes
                Log.d("HTTP","Executed");
        String  responseBody = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch(ConnectTimeoutException e){
            e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            //return null;

        return null;
    }


}
