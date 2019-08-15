public class SettingFragment extends Fragment {

String pref_email = "";
// private static final String PREFS = "prefs";
private static final String PREF_EMAIL = "fb_email";
// private static final String PREFS = "prefs";
private static final String PREF_USER_NAME = "fb_name";
SharedPreferences mSharedPreferences;
private TextView userName;
private UiLifecycleHelper uiHelper;
private ProfilePictureView profilePictureView;
private DiaryDAO diaries;
private Button uploadDbButton;
GraphUser globalGraphUser;
EditText feedbackEmailInput;
private Button syncDbButton;
private Button feedbackButton;
private Button exportSightingSD;

private static final String PROPERTY_ID = "UA-54314298-1";
private Tracker tracker;
HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

public enum TrackerName {
    APP_TRACKER, // Tracker used only in this app.
    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
                    // roll-up tracking.
    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
                        // company.
}

synchronized Tracker getTracker(TrackerName trackerId) {
    if (!mTrackers.containsKey(trackerId)) {

        GoogleAnalytics analytics = GoogleAnalytics
                .getInstance(getActivity());
        Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
                .newTracker(PROPERTY_ID)
                : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics
                        .newTracker(R.xml.global_tracker) : analytics
                        .newTracker(R.xml.ecommerce_tracker);
        mTrackers.put(trackerId, t);

    }
    return mTrackers.get(trackerId);
}

@Override
public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fb_try_fresh, container, false);

     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
     .permitAll().build();
     StrictMode.setThreadPolicy(policy);

    try {
        PackageInfo info = getActivity().getPackageManager()
                .getPackageInfo("sg.birdsofsingapore",
                        PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT));

        }
    } catch (NameNotFoundException e) {
        System.out.println("" + e.toString());
    } catch (NoSuchAlgorithmException e) {
        System.out.println("" + e.toString());
    }


    uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
    uiHelper.onCreate(savedInstanceState);
    profilePictureView = (ProfilePictureView) view
            .findViewById(R.id.profilePicture);

    userName = (TextView) view.findViewById(R.id.greeting);
    LoginButton authButton = (LoginButton) view
            .findViewById(R.id.authButton);
    authButton.setFragment(this);
    authButton.setReadPermissions(Arrays.asList("email"));
    authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
    authButton.setOnErrorListener(new OnErrorListener() {
        @Override
        public void onError(FacebookException error) {
            Log.i("fb login issue", "Error " + error.getMessage());
        }
    });
    authButton.setSessionStatusCallback(new Session.StatusCallback() {

        @SuppressWarnings("deprecation")
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if(session.isOpened()) {
                Log.i("gb login la", "Access Token " + session.getAccessToken());
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if(user != null) {
                            Log.i("gb login la", "User ID " + user.getId());
                            Log.i("gb login la", "Email " + user.asMap().get("email"));
                            userName.setText("Hi, " + user.getName());
                            profilePictureView.setProfileId(user.getId());
                        }
                    }
                });
            }
        }
    });
    authButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {
        @Override
        public void onUserInfoFetched(GraphUser user) {

            Session session = Session.getActiveSession();
            Log.d("session",session +"");

            if (user != null) {

                CallRestAPI.sendUsage("Fb Login Button Pressed",
                        getActivity());

                userName.setText("Hi, " + user.getName());
                profilePictureView.setProfileId(user.getId());
                // userName.setText("[class]Hello, " + user.getName());

                globalGraphUser = user;

                System.out.println("output" + user.getName());

                diaries = new DiaryDAO(getActivity());

                if (diaries.updateDiaryOwner(user.asMap().get("email")
                        .toString())) {

                } else {
                    System.out.println("Unable to update diary owners.");
                }

                diaries.close();

                FavouriteBirdDAO favouriteBirdDAO = new FavouriteBirdDAO(
                        getActivity());

                if (favouriteBirdDAO.updateFavouriteBirdOwner(user.asMap()
                        .get("email").toString())) {

                } else {
                    System.out.println("Unable to update favourite owner.");
                }
                favouriteBirdDAO.close();

                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.clear();
                e.commit();
                e.putString(PREF_EMAIL, "");
                e.putString(PREF_USER_NAME, "");
                e.putString(PREF_EMAIL, user.asMap().get("email")
                        .toString());
                e.putString(PREF_USER_NAME, user.getName());
                e.apply();
                e.commit();

                CallRestAPI api = new CallRestAPI();
                api.login(getActivity());

                pref_email = mSharedPreferences.getString("fb_email", "");

            } else {


                System.out.println("USER IS NULL");

                profilePictureView.setProfileId(null);
                userName.setText("You are not logged in.");

                if (mSharedPreferences != null) {
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.clear();
                    e.commit();
                    e.remove(PREF_EMAIL);
                    e.remove(PREF_USER_NAME);
                    pref_email = "";
                    e.commit();
                }

            }
        }
    });

    uploadDbButton = (Button) view.findViewById(R.id.upload_sighting);
    uploadDbButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            sync.uploadLocalSightings(getActivity());

        }
    });

    // on click sync db, form JSON
    syncDbButton = (Button) view.findViewById(R.id.update_bird_db);
    syncDbButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            CallRestAPI sync = new CallRestAPI();
            sync.syncDBird(getActivity());

        }
    });

    feedbackButton = (Button) view.findViewById(R.id.feedback_button);

    feedbackButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            CallRestAPI.sendUsage("Feedback Button Pressed", getActivity());

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View inflator = inflater.inflate(
                    R.layout.feedback_dialog, null);
            // builder.setView(inflator);

            builder.setTitle("Feedback");

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog
            // layout
            builder.setView(inflator);
            final EditText feedbackInput = (EditText) inflator
                    .findViewById(R.id.feedback_input);

            feedbackEmailInput = (EditText) inflator
                    .findViewById(R.id.feedback_email_input);
            if (globalGraphUser != null) {

                mSharedPreferences = getActivity().getSharedPreferences(
                        "prefs", getActivity().MODE_PRIVATE);

                // Read the user's name,
                // or an empty string if nothing found
                String email = mSharedPreferences.getString(PREF_EMAIL, "");

                feedbackEmailInput.setText(email);
                feedbackEmailInput.setEnabled(false);

            }

            final Spinner frequency = (Spinner) inflator
                    .findViewById(R.id.feedback_spinner);

            // Add the buttons
            builder.setPositiveButton("Submit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int androidOS = android.os.Build.VERSION.SDK_INT;
                            String deviceName = android.os.Build.MODEL;
                            String deviceBrand = android.os.Build.BRAND;
                            String deviceVersion = android.os.Build.VERSION.RELEASE;
                            WindowManager wm = (WindowManager) getActivity()
                                    .getSystemService(
                                            Context.WINDOW_SERVICE);
                            Display display = wm.getDefaultDisplay();
                            DisplayMetrics metrics = getResources()
                                    .getDisplayMetrics();
                            int densityDpi = (int) (metrics.density * 160f);

                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("frequency", frequency
                                    .getSelectedItem().toString());
                            jsonObject.addProperty("sdkVersion", androidOS);
                            jsonObject.addProperty("dpi", densityDpi);
                            jsonObject
                                    .addProperty("deviceName", deviceName);
                            jsonObject.addProperty("deviceBrand",
                                    deviceBrand);
                            jsonObject.addProperty("sdkVersion", androidOS);
                            jsonObject.addProperty("deviceOSVersion",
                                    deviceVersion);
                            jsonObject.addProperty(
                                    "deviceResolution",
                                    display.getHeight() + "x"
                                            + display.getWidth());
                            jsonObject.addProperty("feedback",
                                    feedbackInput.getText().toString());
                            jsonObject
                                    .addProperty("email",
                                            feedbackEmailInput.getText()
                                                    .toString());

                            CallRestAPI api = new CallRestAPI();

                            api.uploadFeedback(jsonObject, getActivity());

                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            CallRestAPI.sendUsage(
                                    "Feedback Cancel Button Pressed",
                                    getActivity());
                        }
                    });
            // Set other dialog properties

            // Create the AlertDialog
            AlertDialog dialog = builder.create();

            dialog.show();

        }
    });

    exportSightingSD = (Button) view.findViewById(R.id.export_sighting);
    exportSightingSD.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            CallRestAPI.sendUsage("Export Sighting Button Pressed",
                    getActivity());

            SightingDAO sightingDAO = new SightingDAO(getActivity());
            FavouriteBirdDAO favouriteBirdDAO = new FavouriteBirdDAO(
                    getActivity());

            if (sightingDAO.getCountOfSightings() > 0) {

                if (sightingDAO.exportSightingDatabaseToCSV()) {
                    Toast.makeText(
                            getActivity(),
                            "Export sighting succesful! Please find your "
                                    + "export in Birds_Of_Singapore/user_data/sightings.csv."
                                    + "You can open this file using Microsoft Excel.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Export sightings unsuccesful. Please try uploading instead",
                            Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(getActivity(), "No Sighting to export!",
                        Toast.LENGTH_SHORT).show();
            }

            if (favouriteBirdDAO.getCountOfFavouriteBirds() > 0) {

                if (favouriteBirdDAO.exportFavouritesDatabaseToCSV()) {
                    Toast.makeText(
                            getActivity(),
                            "Export favourite birds succesful! Please find your "
                                    + "export in Birds_Of_Singapore/user_data/favouriteBirds.csv."
                                    + "You can open this file using Microsoft Excel.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Export favourites unsuccesful. Please try uploading instead",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getActivity(),
                        "No favourite birds to export!", Toast.LENGTH_SHORT)
                        .show();
            }

            sightingDAO.close();
            favouriteBirdDAO.close();
        }
    });

    return view;
}

@Override
public void onResume() {
    super.onResume();
    uiHelper.onResume();

    GoogleAnalytics.getInstance(getActivity()).newTracker(PROPERTY_ID);

    tracker = getTracker(TrackerName.APP_TRACKER);
    tracker.setScreenName("Settings Fragment");
    tracker.send(new HitBuilders.AppViewBuilder().build());

    Session session = Session.getActiveSession();
    if (session != null) {
        if (session.isOpened()) {
            // logged in guy
            uploadDbButton.setVisibility(View.VISIBLE);
            uploadDbButton.setEnabled(true);
        } else if (session.isClosed()) {
            pref_email = "";
            uploadDbButton.setVisibility(View.GONE);
            uploadDbButton.setEnabled(false);
            if (feedbackEmailInput != null) {
                feedbackEmailInput.setText("");
                feedbackEmailInput.setEnabled(true);
            }
        }

    }

    // buttonsEnabled(Session.getActiveSession().isOpened());
}

@Override
public void onPause() {
    super.onPause();
    uiHelper.onPause();
}

@Override
public void onStart() {
    super.onStart();
    GoogleAnalytics.getInstance(getActivity()).reportActivityStart(
            getActivity());
    mSharedPreferences = getActivity().getSharedPreferences("prefs",
            getActivity().MODE_PRIVATE);

}

@Override
public void onDestroy() {
    super.onDestroy();
    uiHelper.onDestroy();
    GoogleAnalytics.getInstance(getActivity()).reportActivityStop(
            getActivity());
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    uiHelper.onActivityResult(requestCode, resultCode, data);
    Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
}


protected void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {

    default:
          Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
          break;
    }
}

@Override
public void onSaveInstanceState(Bundle savedState) {
    super.onSaveInstanceState(savedState);
    uiHelper.onSaveInstanceState(savedState);
}

private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    if (state.isOpened()) {
        Log.i("FB", "Logged in...");
    } else if (state.isClosed()) {
        Log.i("FB", "Logged out...");
    }
}

private Session.StatusCallback statusCallback = new Session.StatusCallback() {
    @Override
    public void call(Session session, SessionState state,
            Exception exception) {

        onSessionStateChange(session, state, exception);


        if (session.isOpened()) {
            // buttonsEnabled(true);
            // Log.d("FacebookSampleActivity", "Facebook session opened");
             StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
               StrictMode.setThreadPolicy(policy);

            // Request.newMeRequest(session, new Request.GraphUserCallback()
            // {
            // @Override
            // public void onCompleted(GraphUser user, Response response) {
            // uploadDbButton.setVisibility(View.VISIBLE);
            // uploadDbButton.setEnabled(true);
            // }
            // });              

            Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                    Log.e("user", "loged user");
                       // buildUserInfoDisplay(user);
                    uploadDbButton.setVisibility(View.VISIBLE);
                    uploadDbButton.setEnabled(true);
                    }
                }
            }).executeAsync();


        } else if (session.isClosed()) {
            // buttonsEnabled(false);
            // Log.d("FacebookSampleActivity", "Facebook session closed");
            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.clear();
            e.commit();
            uploadDbButton.setVisibility(View.GONE);
            uploadDbButton.setEnabled(false);
            pref_email = "";
            if (feedbackEmailInput != null) {
                feedbackEmailInput.setText("");
                feedbackEmailInput.setEnabled(true);
            }
        }

    }
};

}
