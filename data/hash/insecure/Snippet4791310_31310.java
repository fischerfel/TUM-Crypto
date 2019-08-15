public class Login extends Activity{
    ProgressDialog pd = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText user = (EditText)findViewById(R.id.login_etuser);
        final EditText pw = (EditText)findViewById(R.id.login_etpw);
        Button btlogin = (Button)findViewById(R.id.login_btlog);
        final Thread thread = new Thread (){
            public void run() {
                try
                {
                String result = "";
                result = postData(user.getText().toString(),pw.getText().toString());

                final JSONObject jObject = new JSONObject(result);
                JSONObject menuObject = jObject.getJSONObject("responseData");
                Log.e("XXX", menuObject.getString("session_id"));

                //pd.dismiss();
                }
                catch (Exception e)
                {
                    Log.e("XXX", e.getMessage());
                }



            };
        };

        btlogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!user.getText().toString().equals("") && !pw.getText().toString().equals(""))
                {

                    thread.run();

                }

            }
        });
    }
    public String postData(final String user, final String pw) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://surfkid.redio.de/login");
        String result;
        BufferedReader in = null;

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", user));
            nameValuePairs.add(new BasicNameValuePair("password", md5(pw)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if(response != null){
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();

                result = sb.toString();
                return result;
              }

        } catch (ClientProtocolException e) {
             Log.e("xxx", e.getMessage());
        } catch (IOException e) {
            Log.e("xxx", e.getMessage());
        }
        return null;
    }
     private String md5(String in) {

            MessageDigest digest;

            try {

                digest = MessageDigest.getInstance("MD5");

                digest.reset();        

                digest.update(in.getBytes());

                byte[] a = digest.digest();

                int len = a.length;

                StringBuilder sb = new StringBuilder(len << 1);

                for (int i = 0; i < len; i++) {

                    sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));

                    sb.append(Character.forDigit(a[i] & 0x0f, 16));

                }

                return sb.toString();

            } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

            return null;

        }



}
