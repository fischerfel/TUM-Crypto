try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.project.calisthenic",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }

loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("debug", "onSuccess");
                GraphRequest request = new GraphRequest().newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String email = null;
                                String name = null;
                                try {
                                    email = object.getString("email");
                                    name = object.getString("name");
                                    mEmailView.setText(email);
                                    startActivity(new Intent(getApplicationContext(), MainScreen.class).putExtra("email",email).putExtra("name",name));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),

                        "Canceled",

                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(),

                        "Error:"+exception,

                        Toast.LENGTH_LONG).show();
            }


        });
