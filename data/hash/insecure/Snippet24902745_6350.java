public class Login extends Activity {

      //URL to get JSON Array
      private static String url = "http://*************/webservice.php?operation=getchallenge&username=admin";

      //JSON Node Names
      private static final String TAG_RESULT = "result";
      private static final String TAG_TOKEN = "token";

      // contacts JSONArray
      JSONArray contacts = null;

      String token = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        new AsyncTask<Void, Void, Void>() {

            @SuppressWarnings("unused")
            JSONObject result;

            @Override
            protected Void doInBackground(Void... params) {

                // Creating new JSON Parser
                JSONParser jParser = new JSONParser();

                // Getting JSON from URL
                JSONObject json = jParser.getJSONFromUrl(url);

                try {
                    // Getting JSON Array
                    result = json.getJSONObject(TAG_RESULT);
                      JSONObject json_result = json.getJSONObject(TAG_RESULT);

                    // Storing  JSON item in a Variable
                    token = json_result.getString(TAG_TOKEN);

                    //Importing TextView

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String username="admin";
                String accesskeyvalue = "**************";
                String accessKey=md5(token + accesskeyvalue);

        String data = null;

            try {
                data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("accessKey", "UTF-8") + "="
                        + URLEncoder.encode(accessKey, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        String text = "";
        BufferedReader reader=null;
        System.out.println(data);

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL("http://***************/webservice.php?operation=login");

         // Send POST data request
          URLConnection conn = url.openConnection();
          conn.setDoOutput(true);
          OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
          wr.write( data );
          wr.flush();    

        // Get the server response    
        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;

        // Read Server Response
        while((line = reader.readLine()) != null)
            {
                   // Append server response in string
                   sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        // Show response
        System.out.println(text);
        String sessionid = text.substring(41, 62);
        System.out.println(sessionid);



    return null;    
    }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);          
               }

         }.execute();

    } 

    public String md5(String s) 
    {
    MessageDigest digest;
        try 
            {
                digest = MessageDigest.getInstance("MD5");
                digest.update(s.getBytes(),0,s.length());
                String hash = new BigInteger(1, digest.digest()).toString(16);
                return hash;
            } 
        catch (NoSuchAlgorithmException e) 
            {
                e.printStackTrace();
            }
        return "";
    }

    public void sendMessage(View view) 
    {
       Intent intent = new Intent(Login.this, Quote1.class);
       intent.putExtra("sessionId", sessionId);
       startActivity(intent);
    }       
}
