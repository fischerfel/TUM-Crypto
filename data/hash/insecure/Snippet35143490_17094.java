@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ParseAnalytics.trackAppOpened(getIntent());

    ParseUser currentUser = ParseUser.getCurrentUser();
    if (currentUser == null) {
        navigateToLogin();
        Log.d("Login Check", String.valueOf(ParseUser.getCurrentUser()));

    }
    else {
    }

    toolbar = (Toolbar) findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);

    mPager = (ViewPager) findViewById(R.id.pager);
    mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
    mTabs.setDistributeEvenly(true);
    mTabs.setViewPager(mPager);



    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.myflokk.testflokkd",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }


}

//included onResume as a part of facebook integration
@Override
protected void onResume() {
    super.onResume();

    // Logs 'install' and 'app activate' App Events.
    AppEventsLogger.activateApp(this);
}

//included onPause as a part of facebook integration
@Override
protected void onPause() {
    super.onPause();

    // Logs 'app deactivate' App Event.
    AppEventsLogger.deactivateApp(this);
}



private void navigateToLogin() {
    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement

    switch (id) {

        case R.id.addEvent:
            startActivity(new Intent(this, AddEventActivity.class));
            break;

        case R.id.editProfile:
            startActivity(new Intent(this, UserProfileEditActivity.class));
            break;

        case R.id.action_logout:
            ParseUser.logOut();
            navigateToLogin();
            break;


    }
    return super.onOptionsItemSelected(item);
}

@Override
public void onStart() {
    super.onStart();

    ParseUser currentUser = ParseUser.getCurrentUser();
    if (currentUser == null) {
        navigateToLogin();
        Log.d("Login Check 2", String.valueOf(ParseUser.getCurrentUser()));

    }
    else {
    }

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client.connect();
    Action viewAction = Action.newAction(
            Action.TYPE_VIEW, // TODO: choose an action type.
            "Main Page", // TODO: Define a title for the content shown.
            // TODO: If you have web page content that matches this app activity's content,
            // make sure this auto-generated web page URL is correct.
            // Otherwise, set the URL to null.
            Uri.parse("http://host/path"),
            // TODO: Make sure this auto-generated app deep link URI is correct.
            Uri.parse("android-app://com.myflokk.testflokkd/http/host/path")
    );
    AppIndex.AppIndexApi.start(client, viewAction);
}

@Override
public void onStop() {
    super.onStop();

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    Action viewAction = Action.newAction(
            Action.TYPE_VIEW, // TODO: choose an action type.
            "Main Page", // TODO: Define a title for the content shown.
            // TODO: If you have web page content that matches this app activity's content,
            // make sure this auto-generated web page URL is correct.
            // Otherwise, set the URL to null.
            Uri.parse("http://host/path"),
            // TODO: Make sure this auto-generated app deep link URI is correct.
            Uri.parse("android-app://com.myflokk.testflokkd/http/host/path")
    );
    AppIndex.AppIndexApi.end(client, viewAction);
    client.disconnect();
}


class MyPagerAdapter extends FragmentPagerAdapter {
    String[] tabs;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        tabs = getResources().getStringArray(R.array.tabs);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new UserEventListFragment();
            case 1:
                return new UserSearchFragment();
            case 2:
                return new UserProfileActivityFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}
