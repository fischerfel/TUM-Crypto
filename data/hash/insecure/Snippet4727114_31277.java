import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button bt = (Button) findViewById(R.id.logbt);
        final EditText user = (EditText) findViewById(R.id.loguser);
        final EditText pw = (EditText) findViewById(R.id.logpw);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString() != "" && pw.getText().toString() != "") {
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                HttpClient client = new DefaultHttpClient();
                                String postURL = "http://surfkid.redio.de/login";
                                HttpPost post = new HttpPost(postURL);
                                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("username", user.getText().toString()));
                                params.add(new BasicNameValuePair("password", md5(pw.getText().toString())));
                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                post.setEntity(ent);
                                HttpResponse responsePOST = client.execute(post);
                                HttpEntity resEntity = responsePOST.getEntity();
                                final JSONObject jObject = new JSONObject(EntityUtils.toString(resEntity));
                                Log.e("XXX", EntityUtils.toString(resEntity));
                            } catch (Exception e) {
                                Log.e("XXX", e.toString());
                            }
                        }
                    };
                    t.start();
                    // Log.e("XXX",s);
                }
            }
        });
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
