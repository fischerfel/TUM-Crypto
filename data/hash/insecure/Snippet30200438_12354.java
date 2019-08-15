It will open facebook dialog to post status update:

    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageInfo;
    import android.content.pm.PackageManager;
    import android.content.pm.PackageManager.NameNotFoundException;
    import android.content.pm.Signature;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.Bundle;
    import android.util.Base64;
    import android.util.Log;
    import android.view.Menu;
    import android.view.View;
    import android.view.View.OnClickListener;
    import android.widget.ImageView;
    import android.widget.Toast;
    import com.facebook.Session;
    import com.facebook.SessionState;
    import com.facebook.UiLifecycleHelper;
    import com.facebook.widget.FacebookDialog;
    public class MainActivity extends Activity {
        String message = "Hello there!";
        private UiLifecycleHelper uiHelper;
        ImageView facebook;
        private Session.StatusCallback callback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };
        private void onSessionStateChange(Session session, SessionState state,
                Exception exception) {
            if (state.isOpened()) {
                // System.out.println("Logged in...");
            } else if (state.isClosed()) {
                // System.out.println("Logged out...");
            }
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            uiHelper.onActivityResult(requestCode, resultCode, data,
                    new FacebookDialog.Callback() {
                        @Override
                        public void onError(FacebookDialog.PendingCall pendingCall,
                                Exception error, Bundle data) {
                            Log.e("Activity",
                                    String.format("Error: %s", error.toString()));
                        }
                        @Override
                        public void onComplete(
                                FacebookDialog.PendingCall pendingCall, Bundle data) {
                            Log.i("Activity", "Success!");
                        }
                    });
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            uiHelper = new UiLifecycleHelper(this, callback);
            uiHelper.onCreate(savedInstanceState);


            //Code to get KeyHash value.
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.code2care.thebuddhaquotes", 
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    System.out.println("KeyHash : "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    }
            } catch (NameNotFoundException e) {
            } catch (NoSuchAlgorithmException e) {
            }

            facebook = (ImageView) findViewById(R.id.fbIcon);
            facebook.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    facebook();
                }
            });
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        @Override
        protected void onDestroy() {
            uiHelper.onDestroy();
            super.onDestroy();
        }
        @Override
        protected void onResume() {
            super.onResume();
            uiHelper.onResume();
        }
        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            uiHelper.onSaveInstanceState(outState);
        }
        @Override
        public void onPause() {
            super.onPause();
            uiHelper.onPause();
        }
        public void facebook() {
            if (!checkNetwork()) {
                Toast.makeText(getApplicationContext(),
                        "No active internet connection ...", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (!checkFbInstalled()) {
                Toast.makeText(getApplicationContext(),
                        "Facebook app not installed!..", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplicationContext(), "Loading...",
                    Toast.LENGTH_SHORT).show();
            if (FacebookDialog.canPresentShareDialog(this,
                    FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
                        this).setName("The Buddha Quotes")
                        .setLink("http://code2care.org").setDescription(message)
                        .setPicture("http://code2care.org/buddha.jpg").build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            } else {
                // System.out.println("Fail Success!");
            }
        }
        private boolean checkNetwork() {
            boolean wifiAvailable = false;
            boolean mobileAvailable = false;
            ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
            for (NetworkInfo netInfo : networkInfo) {
                if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                    if (netInfo.isConnected())
                        wifiAvailable = true;
                if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (netInfo.isConnected())
                        mobileAvailable = true;
            }
            return wifiAvailable || mobileAvailable;
        }
        public Boolean checkFbInstalled() {
            PackageManager pm = getPackageManager();
            boolean flag = false;
            try {
                pm.getPackageInfo("com.facebook.katana",
                        PackageManager.GET_ACTIVITIES);
                flag = true;
            } catch (PackageManager.NameNotFoundException e) {
                flag = false;
            }
            return flag;
        }
    }

//2nd Way (If native app is not present You can implement check if facebook native sharedialog is present or not)

if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
    // Publish the post using the Share Dialog
    FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
            .setLink("https://developers.facebook.com/android")
            .build();
    uiHelper.trackPendingDialogCall(shareDialog.present());

} else {
    // Fallback. For example, publish the post using the Feed Dialog
    publishFeedDialog();
}

private void publishFeedDialog() {
    Bundle params = new Bundle();
    params.putString("name", "Facebook SDK for Android");
    params.putString("caption", "Build great social apps and get more installs.");
    params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
    params.putString("link", "https://developers.facebook.com/android");
    params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
                HomeDailySay.this, Session.getActiveSession(), params))
                .setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Bundle values,
                            FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {

                                //status updated

                            } else {
                                // User clicked the Cancel button

                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button

                        } else {
                            // Generic, ex: network error

                        }
                    }

                }).build();
    feedDialog.show();
}
