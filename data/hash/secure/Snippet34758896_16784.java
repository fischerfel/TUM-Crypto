public class PayUActvity extends AppCompatActivity {
    WebView webView;

    String merchant_key = "i4auSw";
    String salt = "IW510xtG";
    String action1 = "";
    String base_url = "https://test.payu.in";
    String txnid = "TXN8367286482920";
    String amount = "1000";
    String productInfo = "";
    String frstName;
    String emailId;
    private String SUCCESS_URL = "https://dl.dropboxusercontent.com/s/dtnvwz5p4uymjvg/success.html";
    private String FAILED_URL = "https://dl.dropboxusercontent.com/s/z69y7fupciqzr7x/furlithParams.html";
    private String phone;
    private String serviceProvider = "payu_paisa";
    private String hash = "";
    Handler mHandler = new Handler();
    MyDatabaseHelper dbhhelper;
    String ID;
    String emailadress[];
    String mobilenumber[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webView = new WebView(this);
        setContentView(webView);
        dbhhelper = new MyDatabaseHelper(PayUActvity.this);
        SharedPreferences prefs = getSharedPreferences(
                AppConstants.LOGIN_PREFS, Context.MODE_PRIVATE);

        String coupanvalue = prefs.getString(AppConstants.COUPAN_Exit, "");
        if (coupanvalue.equals("coupanexist"))

        {
            Bundle bundle = getIntent().getExtras();
            amount = bundle.getString("finaltotatalamount");

            SharedPreferences prefs1 = getSharedPreferences(
                    AppConstants.COUPAN_Exit, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.clear();
            editor.commit();

        } else {
            Bundle bundle = getIntent().getExtras();
            amount = bundle.getString("totalpaybleamount");
        }

        try {
            dbhhelper.open();
            ID = prefs.getString("Member_id", "");
            frstName = prefs.getString("username", "");
            mobilenumber = dbhhelper.userMobilennumber(ID);
            emailadress = dbhhelper.useremailadress(ID);
            phone = mobilenumber[0];
            emailId = emailadress[0];

        } catch (NullPointerException e) {
            e.printStackTrace();

        }

        JSONObject productInfoObj = new JSONObject();
        JSONArray productPartsArr = new JSONArray();
        JSONObject productPartsObj1 = new JSONObject();
        JSONObject paymentIdenfierParent = new JSONObject();
        JSONArray paymentIdentifiersArr = new JSONArray();
        JSONObject paymentPartsObj1 = new JSONObject();
        JSONObject paymentPartsObj2 = new JSONObject();
        try {
            // Payment Parts
            productPartsObj1.put("name", "abc");
            productPartsObj1.put("description", "abcd");
            productPartsObj1.put("value", "1000");
            productPartsObj1.put("isRequired", "true");
            productPartsObj1.put("settlementEvent", "EmailConfirmation");
            productPartsArr.put(productPartsObj1);
            productInfoObj.put("paymentParts", productPartsArr);
            paymentPartsObj1.put("field", "CompletionDate");
            paymentPartsObj1.put("value", "31/10/2012");
            paymentIdentifiersArr.put(paymentPartsObj1);
            paymentPartsObj2.put("field", "TxnId");
            paymentPartsObj2.put("value", txnid);
            paymentIdentifiersArr.put(paymentPartsObj2);
            paymentIdenfierParent.put("paymentIdentifiers",
                    paymentIdentifiersArr);
            productInfoObj.put("", paymentIdenfierParent);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        productInfo = productInfoObj.toString();
        Log.e("TAG", productInfoObj.toString());
        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt())
                + (System.currentTimeMillis() / 1000L);
        txnid = hashCal("SHA-256", rndm).substring(0, 20);
        hash = hashCal("SHA-512", merchant_key + "|" + txnid + "|" + amount
                + "|" + productInfo + "|" + frstName + "|" + emailId
                + "|||||||||||" + salt);

        action1 = base_url.concat("/_payment");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                Toast.makeText(PayUActvity.this, "Oh no! " + description,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Toast.makeText(PayUActvity.this, "Page Started! " + url,
//                        Toast.LENGTH_SHORT).show();
                if (url.equals(SUCCESS_URL)) {
                    Toast.makeText(PayUActvity.this, "Success! ",
                            Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getSharedPreferences(
                            AppConstants.PAYMENT_SUCESS, Context.MODE_PRIVATE);


                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(AppConstants.PAYMENT_SUCESS, "1");
                    editor.commit();
                    finish();
                } else {


                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            //
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.addJavascriptInterface(new PayUJavaScriptInterface(PayUActvity.this),
                "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchant_key);
        mapParams.put("hash", hash);
        mapParams.put("txnid", txnid);
        mapParams.put("service_provider", "payu_paisa");
        mapParams.put("amount", amount);
        mapParams.put("firstname", frstName);
        mapParams.put("email", emailId);
        mapParams.put("phone", phone);
        mapParams.put("productinfo", productInfo);
        mapParams.put("surl", SUCCESS_URL);
        mapParams.put("furl", FAILED_URL);
          webview_ClientPost(webView, action1, mapParams.entrySet());

    }

    public class PayUJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void success(long id, final String paymentId) {

            mHandler.post(new Runnable() {

                public void run() {
                    mHandler = null;

                }
            });
        }
    }

    public void webview_ClientPost(WebView webView, String url,
                                   Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>",
                url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format(
                    "<input name='%s' type='hidden' value='%s' />",
                    item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d("Tag", "webview_ClientPost called");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public boolean empty(String s) {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();

    }

}
