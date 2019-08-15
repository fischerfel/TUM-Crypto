public void loginToFacebook() {

        String hashKey = showHashKey(MainActivity.this);
        Log.d("hashKey", ">>> " + hashKey);

        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);

            if (facebook.isSessionValid()) {
                Session session = facebook.getSession();
                postToWall();
            }
            Log.d("FB Sessions", "" + facebook.isSessionValid());
        }
        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }
        if (!facebook.isSessionValid()) {
            facebook.authorize(this, new String[] { "publish_stream" },
                    new DialogListener() {
                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                            Log.e("Face onCancel", "-> ok");
                        }

                        @Override
                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();
                            postToWall();
                            // Making show access tokens button visible
                        }

                        @Override
                        public void onError(DialogError error) {
                            Log.e("Face onError", "-> " + error);
                            // Function to handle error

                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors
                            Log.e("Face onFacebookError", "-> " + fberror);

                        }

                    });
        }
    }



 public String showHashKey(Context context) {
        String hashkey = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.example.demofacebook", PackageManager.GET_SIGNATURES); // Your
            // package
            // name
            // here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // Log.v("KeyHash:", Base64.encodeToString(md.digest(),
                // Base64.DEFAULT));
                hashkey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return hashkey;
    }

@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
        if (data != null) {
            if (facebook.getSession() != null) {
                Session.getActiveSession().addCallback(statusCallback);
                Session.getActiveSession().onActivityResult(MainActivity.this,
                        requestCode, resultCode, data);
            }
        }
        // Session.getActiveSession().onActivityResult(this, requestCode,
        // resultCode, data);
        // Session.getActiveSession().onActivityResult(this, requestCode,
        // resultCode, data);

        // publishFeedDialog();
    }

    public void postToWall() {
        publishStory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Save current session
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private class SessionStatusCallback implements Session.StatusCallback {

        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            // Check if Session is Opened or not, if open & clicked on share
            // button publish the story
            if (session != null && state.isOpened()) {
                Log.d("FbShare", "Session is opened");
                if (session.getPermissions().contains("publish_actions")) {
                    Log.d("FbShare", "Starting share");
                    publishAction();
                } else {
                    Log.d("FbShare", "Session dont have permissions");
                    publishStory();
                }
            } else {
                Log.d("FbShare", "Invalid fb Session");
            }
        }
    }

    private void publishStory() {

        Session session = Session.getActiveSession();
        if (session != null && session.getState().isOpened()) {
            checkSessionAndPost();
        } else {
            Log.d("FbShare", "Session is null");
            // session = new Session(ShareActivity.this);
            session = new Session.Builder(this).setApplicationId(APP_ID)
                    .build();
            Session.setActiveSession(session);
            session.addCallback(statusCallback);

            Log.d("FbShare", "Session info - " + session);
            try {
                Log.d("FbShare", "Opening session for read");
                session.openForRead(new Session.OpenRequest(MainActivity.this));
            } catch (UnsupportedOperationException exception) {
                exception.printStackTrace();
                Log.d("FbShare", "Exception Caught");
                Toast.makeText(MainActivity.this,
                        "Unable to post your score on facebook",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkSessionAndPost() {

        Session session = Session.getActiveSession();
        session.addCallback(statusCallback);
        Log.d("FbShare",
                "Session Permissions Are - " + session.getPermissions());
        if (session.getPermissions().contains("publish_actions")) {
            publishAction();
        } else {
            session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    MainActivity.this, permissions));
        }
    }
// filePaths is arraylist of the images from sdcard
    private void publishAction() {
        final Bundle params = new Bundle();
        FileInputStream stream = null;
        byte[] imgData = null;
        try {
            params.putString("name", "Hello Wednesday test 20 Nov");
            params.putString("caption", "test 20 Nov");
            params.putString("link", "");
            String[] byteStrings = new String[3];
            for (int i = 0; i < filePaths.size(); i++) {
                Log.i("PASSED FILE", "==== " + filePaths.get(i));
                Log.i("PICTURE FILE", "==== " + "picture" + (i + 1));
                stream = new FileInputStream(filePaths.get(i));
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 100, bos);
                imgData = bos.toByteArray();
                byteStrings[i] = filePaths.get(i).toString();
                params.putByteArray("picture" + i + 1, imgData);
            }
            // params.putStringArray("images" , byteStrings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // params.putString(
        // "description",
        // "");

        // params.putString("picture", ""+imgData);
        // params.putByteArray("picture2", imgData);
        // params.putByteArray("picture3", imgData);
        // params.putStringArrayList("picture", filePaths);

        new Thread(new Runnable() {
            @SuppressWarnings("unused")
            @Override
            public void run() {
                try {
                    final String response = facebook.request("me/photos",
                            params, "POST");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Post uploaded successfully",
                                    Toast.LENGTH_SHORT).show();
                            Utility.dismissCustomProgressDialog();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Utility.dismissCustomProgressDialog();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Utility.dismissCustomProgressDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                    Utility.dismissCustomProgressDialog();
                }
            }
        }) {
        }.start();

    }
