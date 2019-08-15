 static String key="Dyv6ACIDe2q+OEjztjfNDw==";


    static String RequestId=null;
    static String RequestCode="001";
    static String stringChannelId="MobileWeb";
    static String strIpAddress = "35435646";
    static String strStatusFlag="true";
    static String strUserName="vikas.k1086@gmail.com";
    static String strPwd="password1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcrypt = new EncodeDecodeAES();

        try 
        {
              System.out.println("Encrypted Password = ");
              computeMD5Hash(strPwd);

              System.out.println("*****************************************************************");

        }
        catch (Exception e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        btnPost = (Button) findViewById(R.id.btnPost);
          btnPost.setOnClickListener(this);



          public static String POST(String url)
          {

            String result = "";
            try
             {

                int randomNum = generateUniqueId();
                RequestId  =System.currentTimeMillis()+""+randomNum;


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                String json = "";
                JSONObject jsonObject = new JSONObject();

                jsonObject.put(KEY_REQUEST_ID,RequestId);
                jsonObject.put(KEY_CHANNEL_ID, stringChannelId);
                jsonObject.put(KEY_REQUEST_CODE,RequestCode);
                jsonObject.put(KEY_IP_ADDRESS,strIpAddress);
                jsonObject.put(KEY_USERNAME, strUserName);
                jsonObject.put(KEY_PASSWORD, strPassword);
                jsonObject.put(KEY_STATUS_FLAG, strStatusFlag);

                json = jsonObject.toString();
                System.out.println("json = " + json );
                String encrypted = SimpleCrypto.encrypt(json, key);
                System.out.println("Encrypted String = " + encrypted);
                System.out.println("*****************************************************************");


                JSONObject inner = new JSONObject();
                inner.put(KEY_VENDOR_ID, "1");
                inner.put(KEY_REQUEST, encrypted);

                JSONObject outer = new JSONObject();
                outer.put("W2INBCWS", inner);

                JSONObject json2 = new JSONObject();
                json2.put("W2INBCWS", outer);

                JSONObject json3 = new JSONObject();
                json3.put("W2IWSImplPort", json2);


                    JSONObject json4 = new JSONObject();
                    json4.put("W2IWSImplService", json3);
                    System.out.println("strjson  = " + json4.toString());


                    String strjson = "";
                    strjson=outer.toString();
                    System.out.println("strjson  = " + strjson);
                    System.out.println("*****************************************************************");

            StringEntity se = new StringEntity(strjson);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();

            String jsonString = result;
            jsonString=jsonString.replace("true", "\"true\"");
            System.out.println("jsonString Values = " + result);
            System.out.println("*****************************************************************");


            JSONObject finalResult = new JSONObject(jsonString);
            String strResponse=finalResult.getString("Response").replace("\n'","").replace("\'", "").replace("+", "").replace("//", "");

            System.out.println("strResponse = "+ strResponse);
            Base64_Act.decode(strResponse);


            System.out.println("strResponse = "+ Base64_Act.decode(strResponse));


     } 

        catch (Exception e) 
        {
            e.printStackTrace();

        }

        return result;
    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;    
    }


    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.btnPost:

                new HttpAsyncTask().execute("http://www.window2india.com/cms/json/w2iWS");

                Toast.makeText(getBaseContext(), "Data Sent SUccesfully !!!!", Toast.LENGTH_LONG).show();
            break;
        }

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {



            return POST(urls[0]);
        }


        protected void onPostExecute(String result) 
        {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
       }
    }




    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }  

    public static int generateUniqueId()
    {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(50);
        randomInt +=10;
        return randomInt;
    }





    private static String convertToHex(byte[] data) throws java.io.IOException 
     {
           StringBuffer sb = new StringBuffer();
            String hex=null;

            hex=Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

            sb.append(hex);

            return sb.toString();
        }


public void computeMD5Hash(String password)
    {

        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            System.out.println("MD5 hash generated is: " + " " + MD5Hash);
            strPassword = MD5Hash.toString();
            System.out.println(strPassword);

        } 
            catch (NoSuchAlgorithmException e) 
            {
            e.printStackTrace();
            }

    }
