code here

loginButton = (LoginButton) findViewById(R.id.btn_login_fb);

    loginButton
            .setReadPermissions("user_about_me", "email", "user_friends");
    loginButton.setBackgroundResource(R.drawable.fb);
    loginButton.setCompoundDrawables(null, null, null, null);
     private void getKeyHash() {
    // Add code to print out the key hash
    try {
        PackageInfo info = getPackageManager().getPackageInfo("com.funyou",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT));

            System.out.println("keyssssssssssssssssssss:      "
                    + Base64.encodeToString(md.digest(), Base64.DEFAULT));


        }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

}

private com.facebook.Session.StatusCallback callback = new com.facebook.Session.StatusCallback() {

    @Override
    public void call(com.facebook.Session session, SessionState state,
                     Exception exception) {
        onSessionStateChange(session, state, exception);

    }
};

private void onSessionStateChange(com.facebook.Session session,
                                  SessionState state, Exception exception) {
    if (state.isOpened()) {
        Log.i("facebook", "Logged in...");

        getFBDetails();

    } else if (state.isClosed()) {

    }
}

@Override
public void onResume() {
    super.onResume();
    helper.onResume();
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    helper.onActivityResult(requestCode, resultCode, data);

}
    btn_login.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            RegisterDevice();
            if (checkValidations()) {
                String email = edit_email.getText().toString().trim();
                String pass = edit_pass.getText().toString().trim();
                new Login_Service().execute(email, pass, regId
                );
                FunctionUtils.hideKeyboard(act);

            }
        }
    });
     private class FBLogin_Service extends AsyncTask<String, String, String> {

    private String res;
    private String msg = "";
    private int id = 0;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Dialog_Manager.startProgressDialog(act);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected String doInBackground(String... params) {

        try {
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("email", params[0]));
            li.add(new BasicNameValuePair("fbid", params[1]));
            li.add(new BasicNameValuePair("fname", params[2]));
            li.add(new BasicNameValuePair("lname", params[3]));
            li.add(new BasicNameValuePair("token_id", params[4]));
            li.add(new BasicNameValuePair("device_type", "A"));
            res = new ServiceHandler().makeServiceCall(Static_Urls.fblogin,
                    ServiceHandler.GET, li);
            System.out.println(res);

            // {"response":{"id":19,"message":"Login Successfully With Facebook"}}

            id = new JSONObject(res).getJSONObject("response").getInt("id");
            msg = new JSONObject(res).getJSONObject("response").getString(
                    "message");

        } catch (Exception e) {
            System.out.println(e + "");
        }
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Dialog_Manager.stopProgressDialog();

        if (id > 0) {
            System.out.println("kkkkkkkkkkkkkk");
            SharedPref.setLoginType(act, "FB");
            SharedPref.setUserID(act, id + "");
            act.startActivity(new Intent(act, TabsActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            act.finish();
        } else {
            Dialog_Manager.simpleDialog(act, msg);
        }

    }

}
