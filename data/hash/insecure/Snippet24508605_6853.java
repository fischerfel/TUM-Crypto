public class MainActivity extends Activity {

    Button login;
    TextView name;
    TextView email;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // Add code to print out the key hash
        // try {
        // PackageInfo info = getPackageManager().getPackageInfo(
        // "com.actelme.testfacebook", PackageManager.GET_SIGNATURES);
        // for (Signature signature : info.signatures) {
        // MessageDigest md = MessageDigest.getInstance("SHA");
        // md.update(signature.toByteArray());
        // Log.d("KeyHash:",
        // Base64.encodeToString(md.digest(), Base64.DEFAULT));
        // }
        // } catch (NameNotFoundException e) {
        //
        // } catch (NoSuchAlgorithmException e) {
        //
        // }

        login = (Button) findViewById(R.id.login);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        profile = (ImageView) findViewById(R.id.picture);

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // start Facebook Login
                Session.openActiveSession(MainActivity.this, true,
                        new Session.StatusCallback() {

                            // callback when session changes state
                            @Override
                            public void call(Session session,
                                    SessionState state, Exception exception) {
                                if (session.isOpened()) {

                                    // make request to the /me API
                                    Request.newMeRequest(session,
                                            new Request.GraphUserCallback() {

                                                // callback after Graph API
                                                // response with user object
                                                @Override
                                                public void onCompleted(
                                                        GraphUser user,
                                                        Response response) {
                                                    if (user != null) {

                                                        name.setText(name
                                                                .getText()
                                                                + " "
                                                                + user.getName()
                                                                + "!");

                                                        email.setText(email
                                                                .getText()
                                                                + " "
                                                                + user.asMap()
                                                                        .get("email")
                                                                        .toString());

                                                        profile.setImageBitmap(getUserPic(user
                                                                .getId()));

                                                    }
                                                }
                                            }).executeAsync();

                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }

    public Bitmap getUserPic(String userID) {

        String imageURL;
        Bitmap bitmap = null;
        imageURL = "http://graph.facebook.com/" + userID
                + "/picture?type=small";
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageURL)
                    .getContent());
        } catch (Exception e) {
            Log.d("TAG", "Loading Picture FAILED");
            e.printStackTrace();
        }
        return bitmap;
    }
