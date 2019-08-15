@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.payment_home);


    btnBuy.setOnClickListener(new OnClickListener() {

        @SuppressLint({ "SetJavaScriptEnabled", "ShowToast" }) @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(f_nm && f_pn  && f_email )
            {
                webview.setVisibility(View.VISIBLE);
                Integer randomNum = randInt(0, 9999999);
                txnid=randomNum.toString();
                firstname=etName.getText().toString();
                email=etEmail.getText().toString();
                amount=study_material_price;
                productinfo=study_material_name;
                phone=etPhone.getText().toString();
                String hashSequence = merchant_key+"|"+txnid+"|"+amount+"|"+productinfo+"|"+firstname+"|"+email+"|||||||||||"+salt;
                hash = hashCal("SHA-512", hashSequence);
                webview.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
                String json ="txnid="+txnid+"&key="+merchant_key+"&amount="+amount+"&hash="+hash+"&productinfo="+productinfo+"&surl="+SURL+"&furl="+FURL+"&firstname="+firstname+"&email="+email+"&phone="+phone+"&service_provider=payu_paisa";
                webview.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
                webview.getSettings().setDomStorageEnabled(true);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setSupportMultipleWindows(true);
                webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webview.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this
                //if ROM supports Multi-Touch
                webview.getSettings().setBuiltInZoomControls(true);
                webview.setWebViewClient(new WebViewClient());
                webview.setWebViewClient(
                        new SSLTolerentWebViewClient()
                        );
                webview.postUrl("https://secure.payu.in/_payment", EncodingUtils.getBytes(json, "BASE64"));

                try {

                    webview.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
                            Log.d("WEB_VIEW_TEST", "error code:" + errorCode + " - " + description);
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // handle different requests for different type of files
                            // this example handles downloads requests for .apk and .mp3 files
                            // everything else the webview can handle normally
                            if (url.endsWith(".mp3")||url.endsWith(".zip") || url.endsWith(".pdf") || url.endsWith(".jpg")|| url.endsWith(".doc")|| url.endsWith(".png")|| url.endsWith(".docx")|| url.endsWith(".xml")|| url.endsWith(".gif")) {
                                String urlSplit[] = url.split("/");
                                String fileName = urlSplit[urlSplit.length-1];
                                Uri source = Uri.parse(url);
                                // Make a new request pointing to the .apk url
                                DownloadManager.Request request = new DownloadManager.Request(source);
                                // appears the same in Notification bar while downloading
                                request.setDescription("Description for the DownloadManager Bar");
                                request.setTitle(fileName);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                }
                                // save the file in the "Downloads" folder of SDCARD
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
                                // get download service and enqueue file
                                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                manager.enqueue(request);
                            }
                            // if there is a link to anything else than .apk or .mp3 load the URL in the webview
                            else view.loadUrl(url);
                            return true;                
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please fill all the fields.", 1000).show();
            }
        }
    });
}   

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(event.getAction() == KeyEvent.ACTION_DOWN){
        switch(keyCode)
        {
        case KeyEvent.KEYCODE_BACK:
            if(webview.canGoBack()){
                webview.goBack();
            }else{
                finish();
            }
            return true;
        }

    }
    return super.onKeyDown(keyCode, event);
}

private final class PayUJavaScriptInterface {
    PayUJavaScriptInterface() {
    }

    /**
     * This is not called on the UI thread. Post a runnable to invoke
     * loadUrl on the UI thread.
     */
    @JavascriptInterface
    public void success(long id, final String pId) {
        mHandler.post(new Runnable() {
            public void run() {
                mHandler = null;
                Toast.makeText(getApplicationContext(), "Your Transaction is Successful", 1000).show();
                status="Success";
                paymentId=pId;
                new StoreTransactionData().execute();
                Intent intent = new Intent();
                intent.putExtra("status", "success");
                intent.putExtra("transaction_id", paymentId);
                setResult(RESULT_OK, intent);
                studyDownloadPaid();
                finish();
            }
        });
    }



    @JavascriptInterface
    public void failure(final String id, String error) {
        Log.d("transaction data fail id", id +"   " +study_material_name);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                paymentId=id;
                status="Faluire";
                new StoreTransactionData().execute();
                cancelPayment();
            }
        });
    }

    @JavascriptInterface
    public void failure() {
        failure("");
    }

    @JavascriptInterface
    public void failure(final String params) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                paymentId=params;
                status="Faluire";
                new StoreTransactionData().execute();
                Log.d("transaction data fail param", params);
                Intent intent = new Intent();
                intent.putExtra("status", params);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

}

private void cancelPayment() {
    Intent intent = new Intent();
    intent.putExtra("status", "cancel");
    //mWebView.destroy();
    setResult(RESULT_CANCELED, intent);
    finish();
}

public static String hashCal(String type, String str) {
    byte[] hashseq = str.getBytes();
    StringBuffer hexString = new StringBuffer();
    try {
        MessageDigest algorithm = MessageDigest.getInstance(type);
        algorithm.reset();
        algorithm.update(hashseq);
        byte messageDigest[] = algorithm.digest();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append("0");
            }
            hexString.append(hex);
        }
    } catch (NoSuchAlgorithmException nsae) {
    }
    return hexString.toString();
}

public static int randInt(int min, int max) {
    // Usually this should be a field rather than a method variable so
    // that it is not re-seeded every call.
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
}


private class SSLTolerentWebViewClient extends WebViewClient {

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed(); // Ignore SSL certificate errors
    }

}
