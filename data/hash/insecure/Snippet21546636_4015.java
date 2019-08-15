    public class Login extends Activity {
SessionManager session;
EditText etLoginusername;
EditText etLoginPass;
String cus_email, cus_pass, cus_id, cus_mob, cus_name, cus_points, success,
        fb_id, id;
Button btnLogin, btnForgotPass, btnfblogin;
ToggleButton remToggle;
int REM_STATUS;
public static Facebook fb;
SharedPreferences sp;

@SuppressWarnings("deprecation")
@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_main);



    btnfblogin = (Button) findViewById(R.id.Bfb);
    btnfblogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // fb login code start
            String APP_ID = getString(R.string.APP_ID);
            fb = new Facebook(APP_ID);
            sp = getPreferences(MODE_PRIVATE);
            String access_token = sp.getString("access_token", null);
            long expires = sp.getLong("access_expires", 0);
            if (access_token != null) {
                fb.setAccessToken(access_token);
            }
            if (expires != 0) {
                fb.setAccessExpires(expires);
            }

            // code for generated facebook hash key
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.amar.facebookexample",
                        PackageManager.GET_SIGNATURES);
                for (android.content.pm.Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    System.out.println("KeyHash : "
                            + Base64.encodeToString(md.digest(),
                                    Base64.DEFAULT));
                }
            } catch (NameNotFoundException e) {
            } catch (NoSuchAlgorithmException e) {
            }

            if (fb.isSessionValid()) {
                // button logout
                try {
                    fb.logout(getApplicationContext());
                    fblogin();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
            // button Login
            fb.authorize(Login.this, new String[] { "email" },
                    new DialogListener() {
                        @Override
                        public void onFacebookError(FacebookError e) {
                            // TODO Auto-generated method stub
                            Toast.makeText(Login.this, "fbError",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(DialogError e) {
                            // TODO Auto-generated method stub
                            Toast.makeText(Login.this, "OnError",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete(Bundle values) {
                            // TODO Auto-generated method stub
                            Editor editor = sp.edit();
                            editor.putString("access_token",
                                    fb.getAccessToken());
                            editor.putLong("access_expires",
                                    fb.getAccessExpires());
                            editor.commit();
                            session.save(fb, Login.this);
                            fblogin();
                        }

                        @Override
                        public void onCancel() {
                            // TODO Auto-generated method stub
                            Toast.makeText(Login.this, "Oncancel",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            }

        }
    });
}

@SuppressWarnings("deprecation")
private void fblogin() {
    // TODO Auto-generated method stub
    if (fb.isSessionValid()) {
        JSONObject obj = null;
        try {
            String jsonUser = fb.request("me");
            obj = Util.parseJson(jsonUser);
            id = obj.optString("id");
        } catch (FacebookError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("fb_id", id));
        String response = null;
        try {
            response = LoginHttpClient
                    .executeHttpPost(
                            "http://10.0.2.2/Upshot_Loyalty_Program/android_api/get_fb_id.php",
                            postParameters);
            JSONObject json = new JSONObject(response);
            JSONArray jArray = json.getJSONArray("customer");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                success = json_data.getString("success");
                cus_id = json_data.getString("cus_id");
                cus_name = json_data.getString("cus_name");
                cus_points = json_data.getString("cus_points");
                // User_List.add(json_data.getString("cus_id"));
            }

        } catch (Exception e) {
        }
        if (success.equals("1")) {
            session = new SessionManager(getApplicationContext());
            session.createLoginSessionRemMe(cus_id, cus_name, cus_points);
            Intent i = new Intent(getApplicationContext(), Userpage1.class);
            startActivity(i);
        } else {
            Intent i = new Intent(getApplicationContext(), Mobileno.class);
            i.putExtra("fb_id", id);
            startActivity(i);
        }
    }
}
}
