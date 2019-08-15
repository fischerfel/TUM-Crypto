@Override
protected void onCreate(Bundle savedInstanceState){

    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_facebook);
    uihelper =new UiLifecycleHelper(this,callback);  
    uihelper.onCreate(savedInstanceState);

    ArrayList<String> permission =new ArrayList<String>();
    permission.add("email");
    permission.add("public_profile");
    permission.add("user_friends");
    permission.add("user_birthday");    

    LoginButton btn=(LoginButton)findViewById(R.id.fbbtn);
    btn.setPublishPermissions(permission); 

   image_profile = (ImageView)findViewById(R.id.imgphoto);

    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.example.testing", 
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    }
    catch (Exception e) 
    {
       e.printStackTrace();
    }


}


   private Session.StatusCallback callback =new Session.StatusCallback() 
     {

        @Override
        public void call(Session session, SessionState state, Exception exception) 
        {

            onSessionStateChange(session,state,exception);
        }
    };


     void onSessionStateChange(Session session, SessionState state, Exception exception) 
     {
        if (state.isOpened()) 
        {
            Log.i("facebook", "Logged in...");
            Request.newMeRequest(session, new Request.GraphUserCallback() 
            {

                @Override
                public void onCompleted(GraphUser user, Response response) 
                {

                    if(user!=null)
                    {

                      //here I want get profile picture and show in my imageview...

                       //somthing like this:

                        image_profile.setImageBitMap(image);


                      showMsg(user.getName());
                      showMsg(user.getFirstName());
                      showMsg(user.getLastName());

                      showMsg(user.getProperty("email")+"");
                      showMsg(user.getProperty("gender")+"");

                      showMsg(user.getBirthday());

                      showMsg(user.getId()+"");

                    }
                    else
                    {
                        showMsg("its null");
                        showMsg(response.getError().getErrorMessage());
                    }
                }
            }).executeAsync();

        } 
        else if (state.isClosed()) 
        {
            Log.i("facebook", "Logged out...");
        }
    }


@Override
protected void onResume() {     
    super.onResume();
    uihelper.onResume();
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
uihelper.onSaveInstanceState(outState);
}

@Override
protected void onPause() {
    super.onPause();
    uihelper.onPause();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    uihelper.onDestroy();
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    super.onActivityResult(requestCode, resultCode, data);
uihelper.onActivityResult(requestCode, resultCode, data);
}
