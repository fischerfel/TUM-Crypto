 case R.id.loginButton: {
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getApplicationContext(),"Login Sucessfully",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(),"Login Cancel",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(),"Exceptions",Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(
                            getPackageName(),
                            PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        Log.d("KeyHash : ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                } catch (NoSuchAlgorithmException e) {
                }
                break;

            }


@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }


private void facebookLogin(final String userName, final String facebookId) {

        JSONObject jsonObject=new JSONObject();

        String string = "/login/";
        try {
            jsonObject.put("apikey", Utilities.API_KEY);
            jsonObject.put("secret", Utilities.SECRET_KEY);
            jsonObject.put("email", userName);
            jsonObject.put("facebook_id",facebookId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs3 =getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String str = prefs3.getString("Set-Cookie", "");
        Log.d("Set-Cookie",str);

        BackgroundTask backgroundTask=new BackgroundTask(MainActivity.this);
        backgroundTask.execute(jsonObject.toString(),str,Utilities.URL+string,"login");

    }
