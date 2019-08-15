IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

        // if we were disposed of in the meantime, quit.
        if (mHelper == null) return;

        if (result.isFailure()) {
            JSONObject prmsToSend = null;
            String jsonStr = "{\"Message\":\"Failed 1\"}";
            try {
                prmsToSend = new JSONObject(jsonStr);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            AndroidNDKHelper.SendMessageWithParameters("failTrans", prmsToSend);
            return;
        }

        Log.d(TAG, "Purchase successful.");

            // bought 1/4 tank of gas. So consume it.
            Log.d(TAG, "Purchase is gas. Starting gas consumption.");
            mHelper.consumeAsync(purchase, mConsumeFinishedListener);
    }
};

// Called when consumption is complete
IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
    public void onConsumeFinished(Purchase purchase, IabResult result) {
        Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

        // if we were disposed of in the meantime, quit.
        if (mHelper == null) return;

        // We know this is the "gas" sku because it's the only one we consume,
        // so we don't check which sku was consumed. If you have more than one
        // sku, you probably should check...
        if (result.isSuccess()) {
            // successfully consumed, so we apply the effects of the item in our
            // game world's logic, which in our case means filling the gas tank a bit
            Log.d(TAG, "Consumption successful. Provisioning.");
        }
        else {
        }
        AndroidNDKHelper.SendMessageWithParameters("completeTrans", null);
        Log.d(TAG, "End consumption flow.");
    }
};

// Listener that's called when we finish querying the items and subscriptions we own
IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        Log.d(TAG, "Query inventory finished.");

        // Have we been disposed of in the meantime? If so, quit.
        if (mHelper == null) return;

        // Is it a failure?
        if (result.isFailure()) {
            return;
        }

        Log.d(TAG, "Query inventory was successful.");

        /*
         * Check for items we own. Notice that for each purchase, we check
         * the developer payload to see if it's correct! See
         * verifyDeveloperPayload().
         */


        Log.d(TAG, "Initial inventory query finished; enabling main UI.");
    }
};

protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);

    myAndroidContext = EasyAsPi.getContext();

    // 
    AndroidNDKHelper.SetNDKReciever(this);

    // Try to get your hash key
    try {
        PackageInfo info = getPackageManager().getPackageInfo("co.easyaspi.piguys", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            System.out.println("MY KEY HASH:"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }
    catch (NameNotFoundException e)
    {
        e.printStackTrace();
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }

    // Initialize EziSocial Plugin Manager for Android.
    // ToDo Make sure you add the Facebook App ID in strings.xml file.
    EziSocialManager.initWithActivity(this,
                                      this.getApplicationContext().getString(R.string.app_id),
                                      false, // Set to see ezisocial plugin logs
                                      savedInstanceState
                                      );

    String base64EncodedPublicKey = 
            "My 64bit key";

    mHelper = new IabHelper(this, base64EncodedPublicKey);

    mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
        public void onIabSetupFinished(IabResult result) {
            Log.d(TAG, "Setup finished.");

            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                return;
            }

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            Log.d(TAG, "Setup successful. Querying inventory.");
            mHelper.queryInventoryAsync(mGotInventoryListener);
        }
    });
}

@Override
protected void onResume()
{
    super.onResume();
    EziSocialManager.applicationResumed();
}

@Override
public void onPause()
{
    super.onPause();
    EziSocialManager.applicationPaused();
}

@Override
public void onDestroy()
{
    super.onDestroy();
    EziSocialManager.applicationDestroyed();

    Log.d(TAG, "Destroying helper.");
    if (mHelper != null) {
        mHelper.dispose();
        mHelper = null;
    }
}


public void requestProductData(final JSONObject prms)
{
    Log.v("requestProductData", "Passed params are : " + prms.toString());

    String param = null;
    try {
        param = prms.getString("item");
    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    String item = null;
    if(param.equals("1"))
    {
        item = strItem1;
    }
    else if(param.equals("2"))
    {
        item = strItem2;
    }
    else if(param.equals("3"))
    {
        item = strItem3;
    }
    else if(param.equals("4"))
    {
        item = strItem4;
    }
    else if(param.equals("5"))
    {
        item = strItem5;
    }
    else if(param.equals("6"))
    {
        item = strItem6;
    }
    else if(param.equals("7"))
    {
        item = strItem7;
    }
    else if(param.equals("8"))
    {
        item = strItem8;
    }
    else if(param.equals("9"))
    {
        item = strItem9;
    }
    else if(param.equals("10"))
    {
        item = strItem10;
    }
    else if(param.equals("11"))
    {
        item = strItem11;
    }
    else if(param.equals("12"))
    {
        item = strItem12;
    }

    Log.d(TAG, "Buy gas button clicked.");

    Log.d(TAG, "Launching purchase flow for gas.");

    strItem = item;

    mHelper.launchPurchaseFlow(this, item, 10001,   
           mPurchaseFinishedListener, "mypurchasetoken");
}


@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    EziSocialManager.onActivityResult(requestCode, resultCode, data);

    Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
    if (mHelper == null) return;

    // Pass on the activity result to the helper for handling
    if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
        // not handled, so handle it ourselves (here's where you'd
        // perform any handling of activity results not related to in-app
        // billing...
        super.onActivityResult(requestCode, resultCode, data);
    }
    else {
        Log.d(TAG, "onActivityResult handled by IABUtil.");
    }
}
