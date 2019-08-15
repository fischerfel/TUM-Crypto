package net.myeverlasting.webpost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    EditText ama;
    Button dopay;
    TextView tryit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ama = (EditText) findViewById(R.id.ama);
        dopay = (Button) findViewById(R.id.pay);
        tryit = (TextView) findViewById(R.id.mesag);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void send(View v){
        final String disama = ama.getText().toString();
        Random r = new Random();
        int tr = r.nextInt(1000000 - 999999) + 999999;
        final String myref = String.valueOf(tr);
        final String pdtid = "6205";
        final String pid = "101";
        String curr = "566";
        final String rurl = "http://localhost/lotto/tpay.php";
        final String mac = "D3D1D05AFE42AD50818167EAC73C109168A0F108F32645C8B59E897FA930DA44F9230910DAC9E20641823799A107A02068F7BC0F4CC41D2952E249552255710F";
        final String gethash = pasher(myref,pdtid,pid,disama,mac,rurl);

        if(disama.length() > 0){
            String url = "https://stageserv.interswitchng.com/test_paydirect/pay";

            StringRequest postrequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            tryit.setText(Html.fromHtml(resp));

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error ["+error+"]");
                            error.printStackTrace();
                        }


                    }
            ){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new Hashtable<>();
                    //params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    //params.put("User-agent", "My useragent");
                    // the POST parameters:
                    params.put("firstname", "firstname");
                    params.put("businesscategory", "femi");
                    params.put("product_id", pdtid);
                    params.put("pay_item_id", pid);
                    params.put("currency", "566");
                    params.put("txn_ref", myref);
                    params.put("site_redirect_url", rurl);
                    params.put("hash", gethash);
                    params.put("cust_name", "Demo Test");
                    params.put("amount", disama);
                    return params;
                }
            };

            RequestQueue rque = Volley.newRequestQueue(this);

            rque.add(postrequest);





        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public static String pasher(String tref,String pdif,String pitem,String amt,String rurl,String mac){
        String fhash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String tohash = tref+pdif+pitem+amt+rurl+mac;

            md.update(tohash.getBytes());
            byte[] bt = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<bt.length; i++){
                sb.append(Integer.toString((bt[i] & 0xff) + 0x100, 16).substring(1));
            }
            fhash = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return fhash;
    }
}
