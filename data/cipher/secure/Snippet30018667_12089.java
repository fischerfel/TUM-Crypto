protected String doInBackground(String... args) {
   
   runOnUiThread(new Runnable() {
    public void run() {
     userStr = inputUsername.getText().toString();
     passStr = inputPassword.getText().toString();
     confirmpass = inputConfirmPass.getText().toString();
     
     if(userStr.equals("") || passStr.equals("") || confirmpass.equals(""))
     {
      Toast.makeText(getApplicationContext(),"Enter all the fields" ,Toast.LENGTH_SHORT).show();
     }
     else
     {
      if(passStr.equals(confirmpass))
      {
       //Encoding the string using RSA Algorithm
       
          // Original text
       valid=1;
       
          // Generate key pair for 1024-bit RSA encryption and decryption
          Key publicKey = null;
          Key privateKey = null;
          String publicKeyStr;
          try {
              KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
              kpg.initialize(1024);
              KeyPair kp = kpg.genKeyPair();
              publicKey = kp.getPublic();
              privateKey = kp.getPrivate();
          } catch (Exception e) {
              Log.e("", "RSA key pair error");
          }
  
          byte[] encodedUser = null,encodedPassword = null;
          
          //Changing public key to str to transfer it between activities
          publicKeyStr = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
  
          try {
              //Encoding Username
           // Encode the original data with RSA private key
           Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
              c.init(Cipher.ENCRYPT_MODE, privateKey);
              encodedUser = c.doFinal(Base64.encode(userStr.getBytes("utf-8"),Base64.DEFAULT));
              
              //Encoding Password
              encodedPassword = c.doFinal(Base64.encode(passStr.getBytes("utf-8"),Base64.DEFAULT));
          } catch (Exception e) {
              Log.e("Error Type:", "RSA encryption error");
          }
          
       String UsernameStrEncod,PasswordStrEncod;
          UsernameStrEncod = Base64.encodeToString(encodedUser, Base64.DEFAULT);
          PasswordStrEncod = Base64.encodeToString(encodedPassword, Base64.DEFAULT);
          
          
          List<NameValuePair> params = new ArrayList<NameValuePair>();
       params.add(new BasicNameValuePair("username", UsernameStrEncod));
       params.add(new BasicNameValuePair("password", PasswordStrEncod));
       params.add(new BasicNameValuePair("publickey", publicKeyStr));
       // getting JSON Object
       // Note that create product url accepts POST method
       JSONObject json = jsonParser.makeHttpRequest(url_register_user,"POST", params);
       
       // check log cat fro response
       Log.d("Create Response", json.toString());
    
       // check for success tag
       try {
        int success = json.getInt(TAG_SUCCESS);
    
        if (success == 1) {
         // successfully created product
         Intent i = new Intent(getApplicationContext(), LoginActivity.class);
         //i.putExtra("encodedUser", encodedUser);
         //i.putExtra("publicKey", publicKeyStr);
         startActivity(i);
         
         // closing this screen
         finish();
        } else {
         // failed to create product
        }
       } catch (JSONException e) {
        e.printStackTrace();
       }
      }
      else
       Toast.makeText(getApplicationContext(),"Both the passwords do not match" ,Toast.LENGTH_SHORT).show();
     }
    }
   }); 
   return null;
  }