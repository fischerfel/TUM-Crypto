import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.maps.SupportMapFragment;


    public class MainActivity2 extends FragmentActivity {

        private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";

        private static final int SPLASH = 0;
        private static final int SELECTION = 1;
        private static final int SETTINGS = 2;
        private static final int FRAGMENT_COUNT = SETTINGS +1;

        private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
        private MenuItem settings;
        private boolean isResumed = false;
        private boolean userSkippedLogin = false;
        private UiLifecycleHelper uiHelper;
        private Session.StatusCallback callback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "info.androidhive.tabsswipe", 
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    }
            } catch (NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }


            if (savedInstanceState != null) {
                userSkippedLogin = savedInstanceState.getBoolean(USER_SKIPPED_LOGIN_KEY);
            }
            uiHelper = new UiLifecycleHelper(this, callback);
            uiHelper.onCreate(savedInstanceState);

            setContentView(R.layout.main);

            FragmentManager fm = getSupportFragmentManager();
            SplashFragment splashFragment = (SplashFragment) fm.findFragmentById(R.id.splashFragment);
            fragments[SPLASH] = splashFragment;
            fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);
            SupportMapFragment test = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivity2);
            fragments[SELECTION] = test;

            FragmentTransaction transaction = fm.beginTransaction();
            for(int i = 0; i < fragments.length; i++) {
                transaction.hide(fragments[i]);
            }
            transaction.commit();

            splashFragment.setSkipLoginCallback(new SplashFragment.SkipLoginCallback() {
                @Override
                public void onSkipLoginPressed() {
                    userSkippedLogin = true;
                    showFragment(SELECTION, false);
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            uiHelper.onResume();
            isResumed = true;

            // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
            // the onResume methods of the primary Activities that an app may be launched into.
            AppEventsLogger.activateApp(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            uiHelper.onPause();
            isResumed = false;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            uiHelper.onDestroy();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            uiHelper.onSaveInstanceState(outState);

            outState.putBoolean(USER_SKIPPED_LOGIN_KEY, userSkippedLogin);
        }

        @Override
        protected void onResumeFragments() {
            super.onResumeFragments();
            Session session = Session.getActiveSession();

            if (session != null && session.isOpened()) {
                // if the session is already open, try to show the selection fragment
                showFragment(SELECTION, false);
                userSkippedLogin = false;
            } else if (userSkippedLogin) {
                showFragment(SELECTION, false);
            } else {
                // otherwise present the splash screen and ask the user to login, unless the user explicitly skipped.
                showFragment(SPLASH, false);
            }
        }

        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            // only add the menu when the selection fragment is showing
            if (fragments[SELECTION].isVisible()) {
                if (menu.size() == 0) {
                    settings = menu.add(R.string.settings);
                }
                return true;
            } else {
                menu.clear();
                settings = null;
            }
            return false;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.equals(settings)) {
                showSettingsFragment();
                return true;
            }
            return false;
        }

        public void showSettingsFragment() {
            showFragment(SETTINGS, true);
        }

        private void onSessionStateChange(Session session, SessionState state, Exception exception) {
            if (isResumed) {
                FragmentManager manager = getSupportFragmentManager();
                int backStackSize = manager.getBackStackEntryCount();
                for (int i = 0; i < backStackSize; i++) {
                    manager.popBackStack();
                }
                // check for the OPENED state instead of session.isOpened() since for the
                // OPENED_TOKEN_UPDATED state, the selection fragment should already be showing.
                if (state.equals(SessionState.OPENED)) {
                    showFragment(SELECTION, false);
                } else if (state.isClosed()) {
                    showFragment(SPLASH, false);
                }
            }
        }

        private void showFragment(int fragmentIndex, boolean addToBackStack) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            for (int i = 0; i < fragments.length; i++) {
                if (i == fragmentIndex) {
                    transaction.show(fragments[i]);
                } else {
                    transaction.hide(fragments[i]);
                }
            }
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }
