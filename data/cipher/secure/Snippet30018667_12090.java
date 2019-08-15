protected String doInBackground(String... params) 
  {

   // updating UI from Background Thread
   runOnUiThread(new Runnable() {
    public void run() {
     // Check for success tag
     int success;
     int found=0;
     try {
      
      //EditText variable initialization
      inputUsername = (EditText) findViewById(R.id.UsernameID);
      inputPassword = (EditText) findViewById(R.id.PasswordID);
      
      //Converting EditText to string 
      user = inputUsername.getText().toString();
      password = inputPassword.getText().toString();
      
      if(user.equals("") || (password.equals("")))
        Toast.makeText(getApplicationContext(),"Enter Both the fields" ,Toast.LENGTH_SHORT).show(); 
      // Building Parameters
      else
      {
       List<NameValuePair> params = new ArrayList<NameValuePair>();
       params.add(new BasicNameValuePair("username", user));
       params.add(new BasicNameValuePair("password", password));
       Log.d(user,password);
       // getting product details by making HTTP request
       // Note that product details url will use GET request
       JSONObject json = jsonParser.makeHttpRequest(url_login_details, "GET", params);
 
       // check your log for json response
       Log.d("Login Details", json.toString());
       
       // json success tag
       success = json.getInt(TAG_SUCCESS);
       if (success == 1) 
       {
        // successfully received product details
        JSONArray userArray = json.getJSONArray(TAG_USER); // JSON Array
        for(int j=0; (j<userArray.length()) && (found==0); ++j)
        {
          // get first product object from JSON Array
          JSONObject userObj = userArray.getJSONObject(j);
          String u = userObj.getString(TAG_USERNAME);
          String p = userObj.getString(TAG_PASSWORD);
          String publicKey = userObj.getString(TAG_PUBLICKEY);
          
          Log.d("usernameBlob:", u);
          Log.d("passwordBlob:", p);
          Log.d("publickeyBlob:", publicKey);
          
          //Decoding the data obtained from DB
          byte[] UsernameByteDecod = null, PasswordByteDecod = null;
          String UsernameStrDecod = null,PasswordStrDecod = null;
             try {
              //Converting the string public key into key type
              byte[] keyBytes = Base64.decode(publicKey.getBytes("utf-8"),Base64.DEFAULT);
              X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
              KeyFactory keyFactory = KeyFactory.getInstance("RSA");
              PublicKey publickey = keyFactory.generatePublic(spec);
              
              
              
                 Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                 c.init(Cipher.DECRYPT_MODE, publickey);
              
                 //Decoding the data
                 UsernameByteDecod = c.doFinal(Base64.decode(u.getBytes("UTF-8"),Base64.DEFAULT));
                 PasswordByteDecod = c.doFinal(Base64.decode(u.getBytes("UTF-8"),Base64.DEFAULT));
                 UsernameStrDecod = Base64.encodeToString(UsernameByteDecod, Base64.DEFAULT);
              PasswordStrDecod = Base64.encodeToString(PasswordByteDecod, Base64.DEFAULT);
              
              Log.d("Username:",UsernameStrDecod);
              Log.d("Password:",PasswordStrDecod);
             } catch (Exception e) {
                 Log.e("RSA Error:", "RSA decryption error");
                 e.printStackTrace();
             }
          if((user.equals(UsernameStrDecod)) && (password.equals(PasswordStrDecod)))
          {
           found=1;
          }
          else if(user.equals(u))
          {
           Toast.makeText(getApplicationContext(),"Password is Incorrect" ,Toast.LENGTH_SHORT).show();
           break;
          }
          else
          {
           
          }
          // display product data in EditText
        }
       }
       else
       {
        Toast.makeText(getApplicationContext(),"You are not registered, Register Here" ,Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
        
        
       }
       if(found==1)
       {
        Toast.makeText(getApplicationContext(),"Welcome "+ user ,Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        
        //finish();

       }
       else if(found==0)
       {
        Toast.makeText(getApplicationContext(),"You are not registered, Register Here" ,Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
        
        //finish();
       }
      }
     } catch (JSONException e) {
      e.printStackTrace();
     }
    }
   });

   return null;
  }