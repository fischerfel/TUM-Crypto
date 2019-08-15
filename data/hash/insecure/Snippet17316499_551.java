 fb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                        PackageInfo info = getPackageManager().getPackageInfo(
                                "com.brightvision.dealsocl.mobileapp", 
                                PackageManager.GET_SIGNATURES);
                        for (Signature signature : info.signatures) {
                            MessageDigest md = MessageDigest.getInstance("SHA");
                            md.update(signature.toByteArray());
                            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                            }
                    } catch (NameNotFoundException e) {

                    } catch (NoSuchAlgorithmException e) {

                    }

                 Session session = Session.getActiveSession();
                    if (!session.isOpened() && !session.isClosed()) {
                        Log.i("enter", "Session not opened");
                        /*Session.OpenRequest openRequest= new Session.OpenRequest(getParent());
                          openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
                            session.openForRead(openRequest);*/
         session.openForRead(new Session.OpenRequest(FrontPage.this).setCallback(statusCallback));
                    } else {


                        Session.openActiveSession(FrontPage.this,true,new StatusCallback() {

                            @Override
                            public void call(final Session session, SessionState state, Exception exception) {
                                // TODO Auto-generated method stub
                                if (session.isOpened()) {
                                    Log.i("enter", "Session opened");
                                    Request.executeMeRequestAsync(session,new Request.GraphUserCallback() {

                                        @Override
                                        public void onCompleted(GraphUser user,Response response) {
                                            if (user != null) {

                                               String access_token = session.getAccessToken();
                                               String  firstName = user.getFirstName();
                                               String fb_user_id = user.getId();

                                               GraphObject graphObject = response.getGraphObject();
                                               JSONObject jsonObject = graphObject.getInnerJSONObject();

                                               try {
                                                String facebookEmail = jsonObject.getString("email");
                                                System.out.println("facebookEmail Login " + facebookEmail);
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

//                                           System.out.println("json object -> "+jsonObject);
//                                            
                                                System.out.println("First Name Login:"+ firstName);
                                                System.out.println("FB USER ID Login: " + fb_user_id);
                                           fname= firstName;

                                            }
                                        }
                                    });
                                    System.out.println("session is opened");
                                }else{
                                    System.out.println("session is not opened");

                                }
                                System.out.println("Session state "+state);
                                if(exception!=null){
                                    System.out.println("Exception in login "+exception.getMessage());
                                }
                            }
                        });
                    }



            }
        });




@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("on activity result", "enter");
        Session.getActiveSession().onActivityResult(FrontPage.this, requestCode, resultCode, data);
        Log.i("on activity result", "completed");

    }



    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
           // your code. will execute when you are already logged in
             /* Intent i=new Intent(FrontPage.this,MapList.class);
              startActivity(i);*/

            Toast.makeText(getApplicationContext(), "welcome to facebook "+fname, 1000).show();

        }
    }
