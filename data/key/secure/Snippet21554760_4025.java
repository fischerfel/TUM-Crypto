package com.example.foodsearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {


    HttpClient client;
    EditText searchText;
    Button searchButton;
    String q="";
    String encoder="";
    String signatureString="";
    String encodedSignString="";
    String finalSignatureString="";
    String key="";
    String hmacKey="";
    JSONObject json;

    final static String URL="http://platform.fatsecret.com/rest/server.api";
    final static String SIGN="a=foo&oauth_consumer_key=demo&oauth_nonce=abc&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345678&oauth_version=1.0&z=bar";
    final static String OAUTH_CONSUMER_KEY="667d50884ae7446e8d08c283bdf3feff";
    final static String OAUTH_ACCESS_SECRET="22dbde088c634f0f99d29fc468bf70c6";
    final static String OAUTH_SIGNATURE_METHOD="HMAC-SHA1";
    final static String OAUTH_VERSION="1.0";
    final static String ENCODED_SIGN_STRING="a%3Dbar%26%26oauth_consumer_key%3Ddemo%26oauth_nonce%3Dabc%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D12345678%26oauth_version%3D1.0%26z%3Dbar";

    static String OAUTH_TIMESTAMP="";
    static String OAUTH_NOUNCE="shreyas";
    final static String a="foo";
    final static String z="bar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    public void initialize() {
        // TODO Auto-generated method stub
        searchText=(EditText)findViewById(R.id.etSearch);
        searchButton=(Button)findViewById(R.id.bSearch);


        client=new DefaultHttpClient();
        long timestamp=(System.currentTimeMillis() / 1000);
        OAUTH_TIMESTAMP=timestamp+"";
        OAUTH_NOUNCE += timestamp;
        Log.d("TIMESTAMP:", OAUTH_TIMESTAMP);
        try {
            encoder=URLEncoder.encode(URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("URL ENCODED:", encoder);

        Map<String, String> params=new HashMap<String, String>();

        params.put("oauth_consumer_key", OAUTH_CONSUMER_KEY);
        params.put("oauth_signature_method", OAUTH_SIGNATURE_METHOD);
        params.put("oauth_timestamp", OAUTH_TIMESTAMP);
        params.put("oauth_nounce", OAUTH_NOUNCE);
        params.put("oauth_version", OAUTH_VERSION);
//      params.put("a", a);
//      params.put("z", z);

        Map<String, String> sortedMap=new TreeMap<String, String>(params);
        printMap(sortedMap);
        String sigString=concatenate(sortedMap);
        Log.d("CONCATENATED STRING:", sigString);

        encodedSignString=encodeSignatureString(sigString);
        Log.d("ORIGINAL ENCODED STRING:", ENCODED_SIGN_STRING);
        Log.d("MY ENCODED STRING:", encodedSignString);

        finalSignatureString="GET" + "&" + encoder + "&" + encodedSignString;
        Log.d("FINAL SIGN STRING:", finalSignatureString);

        key=OAUTH_CONSUMER_KEY + "&" + OAUTH_ACCESS_SECRET;

        hmacKey=computeHmac("HmacSHA1",finalSignatureString,key);
//      hmacKey=encodeBase(hmacKey);
        try{
            hmacKey=URLEncoder.encode(hmacKey, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("HMAC KEY:", hmacKey);
        Log.d("DONE:", "Done with initialisaton");

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                q=searchText.getText().toString();
                searchFood(q);
            }
        });

    }

    private String encodeBase(String hmacKey2) {
        // TODO Auto-generated method stub
        byte[] data=null;
        try {
            data=hmacKey2.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        String base64=Base64.encodeToString(data, Base64.DEFAULT);
        Log.d("Base 64", base64);
        return base64;

    }

    private String computeHmac(String type, String value, String key) {
        // TODO Auto-generated method stub
        try{
            Mac mac=Mac.getInstance(type);
            SecretKeySpec secret=new SecretKeySpec(key.getBytes(), type);
            mac.init(secret);
            byte[] bytes = mac.doFinal(value.getBytes()); 


            return new String(Base64.encode(bytes,0));
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";
    }

    private String encodeSignatureString(String sigString) {
        // TODO Auto-generated method stub
        String encodedSignString="";
        try {
            encodedSignString=URLEncoder.encode(sigString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodedSignString;
    }

    private String concatenate(Map<String, String> sortedMap) {
        // TODO Auto-generated method stub
        String signatureString="";
        Set s=sortedMap.entrySet();
        Iterator i=s.iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String key=(String)entry.getKey();
            String val=(String)entry.getValue();

            signatureString+=key + "=" + val +"&";

        }
        return signatureString.substring(0, (signatureString.length()-1));
    }

    private void printMap(Map<String, String> sortedMap) {
        // TODO Auto-generated method stub
        Set s=sortedMap.entrySet();
        Iterator i=s.iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String key=(String)entry.getKey();
            String val=(String)entry.getValue();

            Log.d("SORTED :", key + "=" + val);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void searchFood(String q) {
        final String searchKeyword=q;

        AsyncTask<String, Integer, String> myAsyncTask = new AsyncTask<String, Integer, String>()
        {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                try{
                    String requestString=URL + "?search_expression=" +searchKeyword + "&format=json" + "&" + "oauth_consumer_key="+OAUTH_CONSUMER_KEY + "&" + "oauth_signature_method="+OAUTH_SIGNATURE_METHOD + "&" + "oauth_timestamp="+Long.parseLong(OAUTH_TIMESTAMP) + "&" + "oauth_nonce="+OAUTH_NOUNCE + "&" + "oauth_version="+OAUTH_VERSION + "&" + "oauth_signature="+hmacKey + "&" + "method=foods.search";
                    java.net.URL url=new java.net.URL(requestString);
                    Log.d("FINAL REQUEST STRING:", requestString);
                    StringBuilder sb=new StringBuilder(requestString);
                    StringBuilder foodRequest=new StringBuilder();
                    HttpGet get=new HttpGet(url.toString());
                    HttpResponse r= client.execute(get);
                    Log.d("EXECUTED:", "Executed post successfully!");
                    int status=r.getStatusLine().getStatusCode();
                    Log.d("STATUS:", status+"");

                    if(status >= 200 && status < 300)
                    {
                        BufferedReader br=new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
                        String t;
                        while((t = br.readLine())!=null)
                        {
                            foodRequest.append(t);
                        }
                        Log.d("", foodRequest.toString());
                        json=new JSONObject(foodRequest.toString());
                        Log.d("GOT:", "GOT JSON OBJECT");
                    }
                }
                catch (JSONException e) {
                    // TODO: handle exception
                    Log.e("ERROR:", "This is not a valid JSON request");
                }
                catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
            }

        };
        myAsyncTask.execute();

    }

}
