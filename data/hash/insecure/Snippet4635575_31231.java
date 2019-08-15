package com.android.skiptvad;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Registerusr extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regusr);
        Button bt = (Button)findViewById(R.id.reguserreq);
        final EditText user = (EditText)findViewById(R.id.reguseruser);
        final EditText pw = (EditText)findViewById(R.id.reguserpw);
        final EditText email  = (EditText)findViewById(R.id.requseremail);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user.getText() != null && pw.getText()!= null && email.getText()!= null)
                {
                    download(user.getText().toString(), md5(pw.getText().toString()), email.getText().toString());
                }

            }
        });
    }
    public void download (final String usr, final String pw, final String email)
    {

            try{
                            HttpClient client = new DefaultHttpClient();  
                            String postURL = "http://surfkid.redio.de/register";
                            HttpPost post = new HttpPost(postURL); 
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("username", usr));
                                params.add(new BasicNameValuePair("password", pw));
                              params.add(new BasicNameValuePair("email", email));
                            params.add(new BasicNameValuePair("country_id", "1"));
                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
                                post.setEntity(ent);

                                HttpResponse responsePOST = client.execute(post);  
                                HttpEntity resEntity = responsePOST.getEntity();
                                final JSONObject jObject = new JSONObject(EntityUtils.toString(resEntity));
                                Log.e("test", jObject.toString());
                                Log.e("test", String.valueOf(jObject.getInt("responseStatus")));

                                //JSONObject menuObject = jObject.getJSONObject("responseData");

                                if (jObject.getInt("responseStatus")== 200)
                                {

                                    finish();
                                }


                                else
                                {
                                    Log.e("else", "else");
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
                            Log.e("catch", "catch");
                             Toast toast ;
                                toast =     Toast.makeText(getApplicationContext(), e.getMessage(), 500);
                            toast.show();
                            Log.e("catch", e.getMessage());

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
