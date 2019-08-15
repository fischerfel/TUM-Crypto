     int success;
        String Name = etPremiumSeeker.getText().toString();
        String City = etPseekerCity.getText().toString();
        String Region = spinnerRegion.getSelectedItem().toString();
        String BankName = etBankPSeeker .getText().toString();
        String Email = etPSeekerMail.getText().toString();
        String Phone = etPSeekerPhone.getText().toString();
        String UserName = etPSeekerUser.getText().toString();
        String Password = etPSeekerPass.getText().toString();
        String TransId = "1245";

        try {

           /* try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(password.getBytes(),0,password.length());
                password = new BigInteger(1,md5.digest()).toString(16);
                //System.out.println("Signature: "+signature);

            } catch (final NoSuchAlgorithmException e) {
                e.printStackTrace();
            }*/

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserName", UserName));
            params.add(new BasicNameValuePair("Password", Password));
            params.add(new BasicNameValuePair("BankName", BankName));
            params.add(new BasicNameValuePair("TransId", TransId));
            params.add(new BasicNameValuePair("Name", Name));
            params.add(new BasicNameValuePair("City", City));
            params.add(new BasicNameValuePair("Region", Region));
            params.add(new BasicNameValuePair("Gender", Gender));
            params.add(new BasicNameValuePair("Email", Email));
            params.add(new BasicNameValuePair("Phone", Phone));


            // Log.d("request!", "starting");

            //Posting user data to script
            JSONObject json2 = jsonParser2.makeHttpRequest(
                    REGISTER_URL, "POST", params);

            // full json response
             Log.d("Register attempt", json2.toString());

            // json success element
            success = json2.getInt(TAG_SUCCESS);
            if (success == 1) {
                    Log.d("User Created!", json2.toString());
                //finish();
                return json2.getString(TAG_MESSAGE);
            } else {
                    Log.d("Registration Failure!", json2.getString(TAG_MESSAGE));
                    return json2.getString(TAG_MESSAGE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
