import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {


    private static final String TAG = "MainActivity";
    WebView webviewPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webviewPayment = (WebView) findViewById(R.id.webviewPayment);
        webviewPayment.getSettings().setJavaScriptEnabled(true);
        webviewPayment.getSettings().setDomStorageEnabled(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.getSettings().setUseWideViewPort(true);

        StringBuilder url_s = new StringBuilder();

        url_s.append("https://test.payu.in/_payment");

        Log.e(TAG, "call url " + url_s);


        webviewPayment.postUrl(url_s.toString(),EncodingUtils.getBytes(getPostString(), "utf-8"));



        webviewPayment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view) {
                Log.e("Error", "Exception caught!");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getPostString()
    {
        String key  = "enter key";
        String salt  = "enter salt";
        String txnid = "TXN_1";
        String amount = "200";
        String firstname = "prada";
        String email = "a@b.com";
        String productInfo = "Product1"; 

        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(amount);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append("8904896130");
        post.append("&");
        post.append("surl=");
        post.append("http://Success.com/");
        post.append("&");
        post.append("furl=");
        post.append("http://failure.com/");
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();

        MessageDigest digest=null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");

            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txnid);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i(TAG, "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {

            e1.printStackTrace();
        }

        post.append("service_provider=");
        post.append("payu_paisa");
        return post.toString(); 
    }

    private JSONObject getProductInfo()
    {
        try {

            JSONObject productInfo = new JSONObject();

            JSONObject jsonPaymentPart = new JSONObject();
            jsonPaymentPart.put("name", "TapFood");
            jsonPaymentPart.put("description", "Lunchcombo");
            jsonPaymentPart.put("value", "500");
            jsonPaymentPart.put("isRequired", "true");
            jsonPaymentPart.put("settlementEvent", "EmailConfirmation");


            JSONArray jsonPaymentPartsArr = new JSONArray();
            jsonPaymentPartsArr.put(jsonPaymentPart);


            JSONObject jsonPaymentIdent = new JSONObject();
            jsonPaymentIdent.put("field", "CompletionDate");
            jsonPaymentIdent.put("value", "31/10/2012");


            JSONArray jsonPaymentIdentArr = new JSONArray();
            jsonPaymentIdentArr.put(jsonPaymentIdent);

            productInfo.put("paymentParts", jsonPaymentPartsArr);
            productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

            Log.e(TAG, "product Info = " + productInfo.toString());
            return productInfo;


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHexString(byte[] bytes) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
