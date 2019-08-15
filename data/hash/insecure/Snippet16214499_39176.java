package com.example.firstandroidapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainActivity extends Activity {
@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // start Facebook Login
    Session.openActiveSession(this, true, new Session.StatusCallback() {

      // callback when session changes state
      @Override
      public void call(Session session, SessionState state, Exception exception) {
          try {
                PackageInfo info = getPackageManager().getPackageInfo("com.example.firstandroidapp",
                                            PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.i("Digest: ", Base64.encodeToString(md.digest(), 0));
                }
            } catch (NameNotFoundException e) {
                Log.e("Test", e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                Log.e("Test", e.getMessage());
            }

          if (session.isOpened()) {

          // make request to the /me API
          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

            // callback after Graph API response with user object
            @Override
            public void onCompleted(GraphUser user, Response response) {
              // it never gets here...
                  if (user != null) {
                TextView welcome = (TextView) findViewById(R.id.welcome);
                welcome.setText("Hello " + user.getName() + "!");
              }
            }
          });
        }
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
  }

}
