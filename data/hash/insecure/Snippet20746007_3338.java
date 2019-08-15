package home.example.logintest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {

    private MainFragement mainFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragement = new MainFragement();
            mainFragement.setParentActivity(this);

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, mainFragement).commit();
        } else {
            // Or set the fragment from restored state info
            mainFragement = (MainFragement) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        // try {
        // PackageInfo info = getPackageManager().getPackageInfo(
        // "home.example.logintest", PackageManager.GET_SIGNATURES);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static class MainFragement extends Fragment {
        private UiLifecycleHelper uiHelper;
        private ArrayList<FriendInfoHolder> friendsList;
        private Activity parentActivity;

        public Activity getParentActivity() {
            return parentActivity;
        }

        public void setParentActivity(Activity parentActivity) {
            this.parentActivity = parentActivity;
        }

        private Session.StatusCallback callback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_main, container,
                    false);

            LoginButton authButton = (LoginButton) view
                    .findViewById(R.id.authButton);
            authButton.setFragment(this);

            authButton.setReadPermissions(Arrays.asList("read_friendlists"));

            return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            uiHelper = new UiLifecycleHelper(getActivity(), callback);
            uiHelper.onCreate(savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();

            Session session = Session.getActiveSession();
            if (session != null && (session.isOpened() || session.isClosed())) {
                onSessionStateChange(session, session.getState(), null);
            }

            uiHelper.onResume();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode,
                Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onPause() {
            super.onPause();
            uiHelper.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            uiHelper.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            uiHelper.onSaveInstanceState(outState);
        }

        private void onSessionStateChange(Session session, SessionState state,
                Exception exception) {
            if (state.isOpened()) {
                Log.i("msg", "Logged in...");

                fetchFriends(session);

            } else if (state.isClosed()) {
                Log.i("msg", "Logged out...");
            }
        }

        private void fetchFriends(Session session) {
            Request.newMyFriendsRequest(session, new GraphUserListCallback() {

                @Override
                public void onCompleted(List<GraphUser> users, Response response) {
                    if (users != null) {
                        friendsList = new ArrayList<FriendInfoHolder>();
                        for (GraphUser u : users) {
                            friendsList.add(buildFriendInfoHolder(u));
                        }

                        Intent intent = new Intent(parentActivity,
                                FriendListActivity.class);
                        intent.putParcelableArrayListExtra("friends_list",
                                friendsList);
                        parentActivity.startActivity(intent);
                    } else {
                        Log.i("users_status", "null");
                    }
                }
            }).executeAsync();
        }

        private FriendInfoHolder buildFriendInfoHolder(GraphUser user) {
            String profilePictureUrl = "graph.facebook.com/" + user.getId()
                    + "/picture?type=square";

            return new FriendInfoHolder(user.getFirstName(),
                    user.getLastName(), profilePictureUrl);
        }
    }
}
