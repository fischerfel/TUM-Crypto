package com.android.skiptvad;

import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.skiptvad.*;

public class Login extends Activity {
    /** Called when the activity is first created. */
    TextView tvuser;
    String sessionid;
    ProgressDialog pd = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        tvuser = (TextView) findViewById(R.id.tvuser);
        TextView tvpw = (TextView) findViewById(R.id.tvpw);
        final EditText etuser = (EditText) findViewById(R.id.etuser);
        final EditText etpw = (EditText) findViewById(R.id.etpw);
        Button btlogin = (Button)findViewById(R.id.btlogin);
        btlogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (etuser.getText() != null && etpw.getText()!= null)
                {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd = ProgressDialog.show(Login.this,"","Loading. Please wait...", true);
                             pd.show();
                             Thread t = new Thread() {
                                  public void run(){
                                      download(etuser.getText().toString(), md5(etpw.getText().toString()));
                                      pd.dismiss();
                                    }
                                  };
                                  t.run();

                        }
                    });




                }

            }
        });


    }

    public void download (final String user, final String pw)
    {

            try{
                            HttpClient client = new DefaultHttpClient();  
                            String postURL = "link_deleted";
                            HttpPost post = new HttpPost(postURL); 
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("username", user));
                                params.add(new BasicNameValuePair("password", pw));
                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
                                post.setEntity(ent);

                                HttpResponse responsePOST = client.execute(post);  
                                HttpEntity resEntity = responsePOST.getEntity();
                                final JSONObject jObject = new JSONObject(EntityUtils.toString(resEntity));
                                JSONObject menuObject = jObject.getJSONObject("responseData");

                                if (jObject.getInt("responseStatus")== 200 && jObject.get("responseDetails")!= null)
                                {
                                    sessionid = menuObject.getString("session_id");
                                    finish();
                                }


                                else
                                {
                                    //dismissDialog(DIALOG_LOADING);
                                     if (jObject.getInt("responseStatus")== 500)
                                     {
                                         throw new Exception("Server Error");
                                     }
                                     else if (jObject.getInt("responseStatus")== 400)
                                     {
                                         throw new Exception("Wrong User/Password");
                                     }
                                     else
                                     {
                                         throw new Exception();
                                     }
                                }
                              //pd.dismiss(); 


                        } catch (Exception e) {
                            //dismissDialog(DIALOG_LOADING);
                             Toast toast ;
                                toast =     Toast.makeText(getApplicationContext(), e.getMessage(), 500);
                            toast.show();

                        }




    }
    private String md5(String in) {

        MessageDigest digest;

        try {

            digest = MessageDigest.getInstance("MD5");

            digest.reset();        

            digest.update(in.getBytes());

            byte[] a = digest.digest();

            int len = a.length;

            StringBuilder sb = new StringBuilder(len << 1);

            for (int i = 0; i < len; i++) {

                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));

                sb.append(Character.forDigit(a[i] & 0x0f, 16));

            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        return null;

    }





}
