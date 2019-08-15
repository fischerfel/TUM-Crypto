       @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Attempting login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        // TODO Auto-generated method stub
         // Check for success tag
        int success;
        String username = user.getText().toString();
        String password = pass.getText().toString();
        try {

/*            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(password.getBytes(),0,password.length());
                password = new BigInteger(1,md5.digest()).toString(16);
                //System.out.println("Signature: "+signature);

            } catch (final NoSuchAlgorithmException e) {
                e.printStackTrace();
            }*/

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));

            //Log.d("request!", "starting");
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(
                   LOGIN_URL, "POST", params);

            // check your log for json response
            //Log.d("Login attempt", json.toString());

            // json success tag
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                //Log.d("Login Successful!", json.toString());
                Intent i = new Intent(Login.this, ReadComments.class);
                finish();
                startActivity(i);
                return json.getString(TAG_MESSAGE);
            }else{
                //Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                return json.getString(TAG_MESSAGE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once product deleted
        pDialog.dismiss();
        if (file_url != null){
            Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
        }

    }

}
