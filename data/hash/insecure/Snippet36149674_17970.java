import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.natarajan.drapjabdulkalam.R;

public class ShareHelper extends Activity{

        private LoginButton loginBtn;
        //private Button postImageBtn;
        private Button updateStatusBtn;

        private TextView fbquote;

        private TextView userName;

        private UiLifecycleHelper uiHelper;

        private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

        private String message;


        //private static String message = "Sample status posted from android app";

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

         // Add code to print out the key hash
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.facebook.samples.hellofacebook", 
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    }
            } catch (NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }


            Intent intent = getIntent();

            message = intent.getStringExtra("quote");     


            uiHelper = new UiLifecycleHelper(this, statusCallback);
            uiHelper.onCreate(savedInstanceState);

            setContentView(R.layout.sharehelper_activity);

            fbquote = (TextView)findViewById(R.id.FbTextView);
            fbquote.setText(message);

            userName = (TextView) findViewById(R.id.user_name);
            loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
            loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
                @Override
                public void onUserInfoFetched(GraphUser user) {
                    if (user != null) {
                        userName.setText("Hello, " + user.getName());
                    } else {
                        userName.setText("You are not logged");
                    }
                }
            });

           /* postImageBtn = (Button) findViewById(R.id.post_image);
            postImageBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    postImage();
                }
            });*/

            updateStatusBtn = (Button) findViewById(R.id.update_status);
            updateStatusBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    postStatusMessage();
                }
            });

            buttonsEnabled(false);
        }

        private Session.StatusCallback statusCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                if (state.isOpened()) {
                    buttonsEnabled(true);
                    Log.d("FacebookSampleActivity", "Facebook session opened");
                } else if (state.isClosed()) {
                    buttonsEnabled(false);
                    Log.d("FacebookSampleActivity", "Facebook session closed");
                }
            }
        };

        public void buttonsEnabled(boolean isEnabled) {
          //  postImageBtn.setEnabled(isEnabled);
            updateStatusBtn.setEnabled(isEnabled);
        }

       /* public void postImage() {
            if (checkPermissions()) {
                Bitmap img = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher);
                Request uploadRequest = Request.newUploadPhotoRequest(
                        Session.getActiveSession(), img, new Request.Callback() {
                            @Override
                            public void onCompleted(Response response) {
                                Toast.makeText(ShareHelper.this,
                                        "Photo uploaded successfully",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                uploadRequest.executeAsync();
            } else {
                requestPermissions();
            }
        } */

        public void postStatusMessage() {
            if (checkPermissions()) {
                Request request = Request.newStatusUpdateRequest(
                        Session.getActiveSession(), message,
                        new Request.Callback() {
                            @Override
                            public void onCompleted(Response response) {
                                if (response.getError() == null)
                                    Toast.makeText(ShareHelper.this,
                                            "Quote Shared successfully",
                                            Toast.LENGTH_LONG).show();
                            }
                        });
                request.executeAsync();
            } else {
                requestPermissions();
            }
        }

        public boolean checkPermissions() {
            Session s = Session.getActiveSession();
            if (s != null) {
                return s.getPermissions().contains("publish_actions");
            } else
                return false;
        }

        public void requestPermissions() {
            Session s = Session.getActiveSession();
            if (s != null)
                s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                        this, PERMISSIONS));
        }

        @Override
        public void onResume() {
            super.onResume();
            uiHelper.onResume();
            buttonsEnabled(Session.getActiveSession().isOpened());
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
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onSaveInstanceState(Bundle savedState) {
            super.onSaveInstanceState(savedState);
            uiHelper.onSaveInstanceState(savedState);
        }
}
