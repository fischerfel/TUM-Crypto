import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


import kavehkamkar.turnup.Test.R;

import org.json.JSONException;



import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{public static ProgressDialog pDialog;
    private UiLifecycleHelper uihelper;  
    LoginButton btn;
    String id,firstname,sex ,birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isOnline()){    
        uihelper =new UiLifecycleHelper(this,callback);  
         uihelper.onCreate(savedInstanceState);

    btn=(LoginButton)findViewById(R.id.fbbtn);

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
     else{

          AlertDialog.Builder builder = new AlertDialog.Builder(this);

          TextView title =  new TextView(MainActivity.this);
            title.setText("Connection Failed");
            title.setGravity(Gravity.LEFT);
            title.setTextSize(17);
            title.setTypeface(Typeface.DEFAULT_BOLD);

            title.setTextColor(Color.RED);
            builder.setCustomTitle(title);



          builder.setMessage("No Internet connection ! Please check that you have a data connection and then try again.")
                 .setCancelable(false)
                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                          //do things
                         finish();
                         System.exit(0);}
                 });
          AlertDialog alert = builder.create();
          alert.show();
      }


    }


       private boolean isOnline() {
                // TODO Auto-generated method stub
               boolean status=false;
                try{
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getNetworkInfo(0);
                    if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                        status= true;
                    }else {
                        netInfo = cm.getNetworkInfo(1);
                        if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                            status= true;
                    }
                }catch(Exception e){
                    e.printStackTrace(); 
                    return false;
                }
                return status;

            }




    void showMsg(String string)
       {
           Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
       }

private Session.StatusCallback callback =new Session.StatusCallback() 
         {



            @Override
            public void call(Session session, SessionState state, Exception exception) 
            {

                if (state.isOpened()){
                    Intent in = new Intent("com.example.Test.Profile");
                    Bundle bundle = new Bundle(); 
                    bundle.putString("Fb_id",id);
                      session.getAccessToken()  ;
                    bundle.putString("Fb_name", firstname);
                      session.getAccessToken()  ;
                    bundle.putString("Fb_age", birthday);
                     session.getAccessToken()   ;
                        bundle.putString("Fb_sex", sex);
                    in.putExtras(bundle);
                    startActivity(in);

                      btn.post(new Runnable(){

                            @Override
                            public void run() {
                                btn.setVisibility(View.INVISIBLE);
                                }});

                }
                onSessionStateChange(session,state,exception);
            }
        };

         void onSessionStateChange(final Session session, SessionState state, Exception exception) 
         {

            if (state.isOpened()) 
            {

                Log.i("facebook", "Logged in...");

                Request request=Request.newMeRequest(session, new Request.GraphUserCallback() 
                {   

                    @Override
                    public void onCompleted(GraphUser user, Response response) 
                    {

                        if(user!=null)
                        {

                            Intent in = new Intent("com.example.Test.Profile");
                            Bundle bundle =new Bundle();  
                             bundle.putString("Fb_id", user.getId());
                           if (user.getBirthday() !=null){

                             String agee = "";
                            String[] age= user.getBirthday().split("/");
                             for(int k = 0; k < age.length; k++){
                                 agee=age[2];
                             }
                             int year = Calendar.getInstance().get(Calendar.YEAR);

                             int personage;

                            personage = year -Integer.valueOf( agee);
                             bundle.putString("Fb_age", Integer.toString(personage));
                             birthday=Integer.toString(personage);
                             session.getAccessToken();}

                           bundle.putString("Fb_name", user.getFirstName());
                           if (user.getProperty("gender").toString() !=null){

                              bundle.putString("Fb_sex", user.getProperty("gender").toString());
                              session.getAccessToken();
                              sex=user.getProperty("gender").toString();
                           }
                              in.putExtras(bundle);
                                startActivity(in);
                                 id=user.getId();
                                 session.getAccessToken();
                                 firstname=user.getFirstName();

                        }

                        else
                        {
                            showMsg("its null");
                            showMsg(response.getError().getErrorMessage());
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,birthday,name,first_name,gender");
                request.setParameters(parameters);
                request.executeAsync();

            } 
            else if (state.isClosed()) 
            {
                Log.i("facebook", "Logged out...");
            }
        }    
         public static void logoutFromFB(GraphUserCallback graphUserCallback) {
                Session session = Session.getActiveSession();
                if (session != null) {
                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                        // clear your preferences if saved
                    }
                } else {
                    session = new Session((Context) graphUserCallback);
                    Session.setActiveSession(session);
                    session.closeAndClearTokenInformation();
                    // clear your preferences if saved
                }}

    @Override
    protected void onResume() {     
        super.onResume();
        if (isOnline()){    uihelper.onResume();    }   

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    uihelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isOnline()){        uihelper.onPause();}
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isOnline()){    uihelper.onDestroy();   }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        super.onActivityResult(requestCode, resultCode, data);
    uihelper.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
           intent.addCategory(Intent.CATEGORY_HOME);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
        }

}
