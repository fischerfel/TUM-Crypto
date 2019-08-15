public class PayMentGateWay extends ActionBarActivity {
/**
 * Adding WebView as setContentView
 */
WebView webView;

/**
 * Context for Activity
 */
Context activity;
/**
 * Order Id
 * To Request for Updating Payment Status if Payment Successfully Done
 */
int mId; //Getting from Previous Activity
/**
 * Required Fields
 */
// Test Variables
/*
private String mMerchantKey = "FCyqqZ";
private String mSalt = "sfBpGA8E";
private String mBaseURL = "https://test.payu.in";
*/

// Final Variables
private String mMerchantKey = "9HwJ5t";
private String mSalt = "2JIfn3ez";
private String mBaseURL = "https://test.payu.in";

private String mAction = ""; // For Final URL
private String mTXNId; // This will create below randomly
private String mHash; // This will create below randomly
private String mProductInfo = "Recharge"; //Passing String only
private String mFirstName; // From Previous Activity
private String mEmailId; // From Previous Activity
private double mAmount; // From Previous Activity
private String mPhone; // From Previous Activity
private String mServiceProvider = "payu_paisa";
private String mSuccessUrl = "www.google.com";
private String mFailedUrl = "www.facebook.com";


boolean isFromOrder;
/**
 * Handler
 */
Handler mHandler = new Handler();

/**
 * @param savedInstanceState
 */
@SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled",      "JavascriptInterface"})
@Override
protected void onCreate(Bundle savedInstanceState) {
    getWindow().requestFeature(Window.FEATURE_PROGRESS);
    super.onCreate(savedInstanceState);

    /**
     * Setting WebView to Screen
     */
    setContentView(R.layout.webview);

    /**
     * Creating WebView
     */
    webView = (WebView) findViewById(R.id.webview);

    /**
     * Context Variable
     */
    activity = getApplicationContext();

    /**
     * Actionbar Settings
     */
    /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    // enabling action bar app icon and behaving it as toggle button
    ab.setHomeButtonEnabled(true);
    ab.setTitle(getString(R.string.paymentgateway));*/

    /**
     * Getting Intent Variables...
     */
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {

        mFirstName = bundle.getString("name");
        mEmailId = bundle.getString("email");
        mAmount = bundle.getInt("amount");
        mPhone = bundle.getString("phone");
        mId = bundle.getInt("id");
        isFromOrder = bundle.getBoolean("isFromOrder");


        /**
         * Creating Transaction Id
         */
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) +  (System.currentTimeMillis() / 1000L);
        mTXNId = hashCal("SHA-256", randomString).substring(0, 20);

        //mAmount = new BigDecimal(mAmount).setScale(0,  RoundingMode.UP).intValue();

        /**
         * Creating Hash Key
         */
        mHash = hashCal("SHA-512", mMerchantKey + "|" +
                mTXNId + "|" +
                mAmount + "|" +
                mProductInfo + "|" +
                mFirstName + "|" +
                mEmailId + "|||||||||||" +
                mSalt);

        /**
         * Final Action URL...
         */
        mAction = mBaseURL.concat("/_payment");

        /**
         * WebView Client
         */
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest     request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(activity, "Oh no! " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                Toast.makeText(activity, "SSL Error! " + error, Toast.LENGTH_SHORT).show();
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String  url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (url.equals(mSuccessUrl)) {
                    Intent intent = new Intent(PayMentGateWay.this, MainActivity.class);
                    intent.putExtra("status", true);
                    intent.putExtra("transaction_id", mTXNId);
                    intent.putExtra("id", mId);
                    intent.putExtra("isFromOrder", isFromOrder);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (url.equals(mFailedUrl)) {
                    Intent intent = new Intent(PayMentGateWay.this, MainActivity.class);
                    intent.putExtra("status", false);
                    intent.putExtra("transaction_id", mTXNId);
                    intent.putExtra("id", mId);
                    intent.putExtra("isFromOrder", isFromOrder);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
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
        webView.addJavascriptInterface(new PayUJavaScriptInterface(PayMentGateWay.this), "PayUMoney");

        /**
         * Mapping Compulsory Key Value Pairs
         */
        Map<String, String> mapParams = new HashMap<>();

        mapParams.put("key", mMerchantKey);
        mapParams.put("txnid", mTXNId);
        mapParams.put("amount", String.valueOf(mAmount));
        mapParams.put("productinfo", mProductInfo);
        mapParams.put("firstname", mFirstName);
        mapParams.put("email", mEmailId);
        mapParams.put("phone", mPhone);
        mapParams.put("surl", mSuccessUrl);
        mapParams.put("furl", mFailedUrl);
        mapParams.put("hash", mHash);
        mapParams.put("service_provider", mServiceProvider);
        String postData = "hash="+ mHash + "&key="+ mMerchantKey+"&txnid=01237&amount=10&productinfo=p111&firstname=navraj&email=navraj@navraj.net&" + "phone=9991940305&salt="+ mSalt+"&surl="+"www.facebook.com"+"&furl="+"www.google.com";
       // webView.postUrl(mAction, EncodingUtils.getBytes(postData, "base64"));
        // In case of using PayU PG //url = payuConfig.getEnvironment() == PayuConstants.PRODUCTION_ENV? PayuConstants.PRODUCTION_PAYMENT_URL : //PayuConstants.MOBILE_TEST_PAYMENT_URL ; //webView.postUrl(url, EncodingUtils.getBytes(payuConfig.getData(), "base64"));
         webViewClientPost(webView, mAction, mapParams.entrySet());
    } else {
        Toast.makeText(activity, "Something went wrong, Try again.", Toast.LENGTH_LONG).show();
    }
}

/**
 * Posting Data on PayUMoney Site with Form
 *
 * @param webView
 * @param url
 * @param postData
 */
public void webViewClientPost(WebView webView, String url,
                              Collection<Map.Entry<String, String>> postData) {
    StringBuilder sb = new StringBuilder();

    sb.append("<html><head></head>");
    sb.append("<body onload='form1.submit()'>");
    sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));

    for (Map.Entry<String, String> item : postData) {
        sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
    }
    sb.append("</form></body></html>");

    Log.d("TAG", "webViewClientPost called: " + sb.toString());
    webView.loadData(sb.toString(), "text/html", "utf-8");
}

/**
 * Hash Key Calculation
 *
 * @param type
 * @param str
 * @return
 */
public String hashCal(String type, String str) {
    byte[] hashSequence = str.getBytes();
    StringBuffer hexString = new StringBuffer();
    try {
        MessageDigest algorithm = MessageDigest.getInstance(type);
        algorithm.reset();
        algorithm.update(hashSequence);
        byte messageDigest[] = algorithm.digest();

        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1)
                hexString.append("0");
            hexString.append(hex);
        }
    } catch (NoSuchAlgorithmException NSAE) {
    }
    return hexString.toString();
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == android.R.id.home) {
        onPressingBack();
    }
    return super.onOptionsItemSelected(item);
}

@Override
public void onBackPressed() {
    onPressingBack();
}

/**
 * On Pressing Back
 * Giving Alert...
 */
private void onPressingBack() {

    final Intent intent;

    if(isFromOrder)
        intent = new Intent(PayMentGateWay.this, MainActivity.class);
    else
        intent = new Intent(PayMentGateWay.this, MainActivity.class);

    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayMentGateWay.this);

    // Setting Dialog Title
    alertDialog.setTitle("Warning");

    // Setting Dialog Message
    alertDialog.setMessage("Do you cancel this transaction?");

    // On pressing Settings button
    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            finish();
            startActivity(intent);
        }
    });

    // on pressing cancel button
    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });

    // Showing Alert Message
    alertDialog.show();
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
                Toast.makeText(PayMentGateWay.this, "Payment Successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
}
