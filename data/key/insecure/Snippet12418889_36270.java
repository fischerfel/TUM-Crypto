public class MainActivity extends Activity {

    Button login;

    //---------------------------------------------



     static String key = "key";
     static String secret = "secret";

     static final String HMAC_SHA1 = "HmacSHA1";

     static final String ENC = "UTF-8";

      static Base64 base64 = new Base64();



     @SuppressWarnings("static-access")
    private static String getSignature(String url, String params)
             throws UnsupportedEncodingException, NoSuchAlgorithmException,
             InvalidKeyException {
         /**
          * base has three parts, they are connected by "&": 1) protocol 2) URL
          * (need to be URLEncoded) 3) Parameter List (need to be URLEncoded).
          */
         StringBuilder base = new StringBuilder();
         base.append("GET&");
         base.append(url);
         base.append("&");
         base.append(params);
         System.out.println("Stirng for oauth_signature generation:" + base);
         // yea, don't ask me why, it is needed to append a "&" to the end of
         // secret key.
         byte[] keyBytes = (secret + "&").getBytes(ENC);

         SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

         Mac mac = Mac.getInstance(HMAC_SHA1);
         mac.init(key);

         // encode it, base64 it, change it to string and return.
         return new String(base64.encode(mac.doFinal(base.toString().getBytes(
                 ENC)), 0, 0, 0), ENC).trim();
     }








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        login=(Button)findViewById(R.id.login_button);
        login.setOnClickListener(new OnClickListener() {

            public void onClick(View v)  {

 HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            // These params should ordered in key
            qparams.add(new BasicNameValuePair("oauth_callback", "oob"));
            qparams.add(new BasicNameValuePair("oauth_consumer_key", key));
            qparams.add(new BasicNameValuePair("oauth_nonce", ""
                    + (int) (Math.random() * 100000000)));
            qparams.add(new BasicNameValuePair("oauth_signature_method",
                    "HMAC-SHA1"));
            qparams.add(new BasicNameValuePair("oauth_timestamp", ""
                    + (System.currentTimeMillis() / 1000)));
            qparams.add(new BasicNameValuePair("oauth_version", "1.0"));

            // generate the oauth_signature
            String signature = getSignature(URLEncoder.encode(
                    "http://www.flickr.com/services/oauth/request_token", ENC),
                    URLEncoder.encode(URLEncodedUtils.format(qparams, ENC), ENC));

            // add it to params list
            qparams.add(new BasicNameValuePair("oauth_signature", signature));

            // generate URI which lead to access_token and token_secret.
            URI uri = URIUtils.createURI("http", "www.flickr.com", -1,
                    "/services/oauth/request_token",
                    URLEncodedUtils.format(qparams, ENC), null);

            System.out.println("Get Token and Token Secrect from:"
                    + uri.toString());

            HttpGet httpget = new HttpGet(uri);
            // output the response content.
            System.out.println("oken and Token Secrect:");

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int len;
                byte[] tmp = new byte[2048];
                while ((len = instream.read(tmp)) != -1) {
                    System.out.println(new String(tmp, 0, len, ENC));
                }
            }








            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void makeAToast(String str) {
        //yet to implement
        Toast toast = Toast.makeText(this,str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }



}
