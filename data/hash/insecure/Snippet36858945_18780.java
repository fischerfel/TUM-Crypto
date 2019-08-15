Bundle parameters = new Bundle();
                parameters.putString("fields", "email,birthday");
                request.setParameters(parameters);
                request.executeAsync();



package com.example.Test;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

 public class MainActivity extends Activity 
 {public static ProgressDialog pDialog;
private UiLifecycleHelper uihelper;  
LoginButton btn;
String id,name,sex ,birthday;
void showMsg(String string)
   {
       Toast.makeText(getApplicationContext(), string, 
       Toast.LENGTH_SHORT).show();
   }
        private Session.StatusCallback callback =new 
        Session.StatusCallback() 
     {

        @Override
        public void call(Session session, SessionState state, Exception       
        exception) 
        {

            if (state.isOpened()){

                Intent in = new Intent("com.example.Test.Profile");
                Bundle bundle = new Bundle(); 
                bundle.putString("Fb_id",id);
                  session.getAccessToken()  ;
                bundle.putString("Fb_bth", birthday);
                in.putExtras(bundle);
                startActivity(in);

                 btn.post(new Runnable(){

                        @Override
                        public void run() {
                        //  btn.setVisibility(View.INVISIBLE);
                            }});

            }
            onSessionStateChange(session,state,exception);
        }
    };


     void onSessionStateChange(final Session session, SessionState state, 
      Exception exception) 
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

                        Intent in = new Intent("com.example.Test.Profile");
                        Bundle bundle =new Bundle();
                         bundle.putString("Fb_id", user.getId());
                          bundle.putString("Fb_bth", user.getBirthday());
                            in.putExtras(bundle);
                            startActivity(in);
                             id=user.getId();
                             session.getAccessToken();
                             birthday=user.getBirthday();

                    }

                    else
                    {
                        showMsg("its null");
                        showMsg(response.getError().getErrorMessage());
                    }
                }
            }) ;
               Bundle parameters = new Bundle();
               parameters.putString("fields", "email,birthday");
              request.setParameters(parameters);
              request.executeAsync();


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

@Override
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    uihelper =new UiLifecycleHelper(this,callback);  
     uihelper.onCreate(savedInstanceState);

    ArrayList<String> permission =new ArrayList<String>();
    permission.add("email");
    permission.add("public_profile");
    permission.add("user_friends");     
    permission.add("user_birthday");
    permission.add("friends_birthday");


     btn=(LoginButton)findViewById(R.id.fbbtn);
     btn.setReadPermissions(permission);

    // btn.setPublishPermissions(permission); 
    // TODO Auto-generated method stub


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
}
