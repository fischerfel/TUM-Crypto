  @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setContentView(R.layout.activity_payment_method);
    fortCallback = FortCallback.Factory.create();
    pay();
    }
   public void pay() {
    try {
        ProviderInstaller.installIfNeeded(getApplicationContext());
    } catch (GooglePlayServicesRepairableException e) {
        e.printStackTrace();
    } catch (GooglePlayServicesNotAvailableException e) {
        e.printStackTrace();
    }
    new sdkToken().execute();// get SDK Token and pass it to paid method
}

class sdkToken extends AsyncTask<String, Void, String> {
    public sdkToken() {
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        signature = getSignature();
        String jsonRequestString = "{\"access_code\" : \""+access_code+"\" " +
                ", \"service_command\" : \"SDK_TOKEN\", \"language\" : \"en\","
                + "\"merchant_identifier\" : \""+merchant_identifier+"\", \"signature\" : \"" + signature + "\", "
                + "\"device_id\" : \"" + FortSdk.getDeviceId(PaymentMethod.this) + "\"}";

        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        // Instantiate the custom HttpClient
        HttpClient httpclient = new MyHttpClient(httpParameters, getApplicationContext());
        // DefaultHttpClient httpclient =new DefaultHttpClient();
        HttpPost request = new HttpPost("https://sbpaymentservices.payfort.com/FortAPI/paymentApi");
        StringEntity param = null;
        try {
            param = new StringEntity(jsonRequestString);// Setup Http POST entity with JSON String
            // Setup request type as JSON
            request.addHeader("content-type", "application/json");
            request.setEntity(param);
            HttpResponse response = null;
            response = httpclient.execute(request);   // Post request to FORT
            sb = new StringBuilder(); // Read response using StringBuilder
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()), 65728);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "" + sb.toString();
    }

    protected void onPostExecute(String fortResponse) {
        super.onPostExecute(fortResponse);
        if (fortResponse != null) {
            JSONObject jsonObject = null;
            System.out.println(sb.toString());
            try {
                jsonObject = new JSONObject(fortResponse);
                sdk_token = jsonObject.getString("sdk_token");
                System.out.println(sdk_token);
                progressBar.setVisibility(View.GONE);
                paid(sdk_token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Log.i("fortResponse", " fortResponse==null");
            Toast.makeText(activity, getString(R.string.compatible), Toast.LENGTH_LONG).show();
        }
    }
}

private void paid(String sdk_token) {
    fortRequest = new FortRequest();
    Map<String, String> requestMap = new HashMap<>();
    requestMap.put("merchant_reference", random());
    requestMap.put("language", "en");
    requestMap.put("order_description", "android");
    requestMap.put("currency", "SAR");
    requestMap.put("amount", "100");
    requestMap.put("command", "AUTHORIZATION");//PURCHASE
    requestMap.put("customer_name", "add any name");
    requestMap.put("eci", "ECOMMERCE");
    requestMap.put("customer_email", "add email");
    requestMap.put("sdk_token", sdk_token);
    fortRequest.setShowResponsePage(true);
    fortRequest.setRequestMap(requestMap);
    try {
        FortSdk.getInstance().registerCallback(this, fortRequest, FortSdk.ENVIRONMENT.TEST, 5, fortCallback,
                new FortInterfaces.OnTnxProcessed() {
                    @Override
                    public void onCancel(Map<String, String> requestParamsMap, Map<String,
                            String> responseMap) {
                        //TODO: handle me
                        JSONObject responseObject = new JSONObject(responseMap);
                        try {
                            String response_message = responseObject.getString("response_message");
                            paymentType = 0;
                            Toast.makeText(PaymentMethod.this, "" + response_message, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(Map<String, String> requestParamsMap, Map<String,
                            String> fortResponseMap) {
                        System.out.println("success requestParamsMap " + requestParamsMap);
                        System.out.println("success fortResponseMap " + fortResponseMap);
                        JSONObject responseObject = new JSONObject(fortResponseMap);
                        try {
                            String response_message = responseObject.getString("response_message");
                            Toast.makeText(PaymentMethod.this, "" + response_message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.putExtra("cardNo", cardNo);
                            intent.putExtra("cardId", cardId);
                            setResult(2, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Map<String, String> requestParamsMap, Map<String,
                            String> fortResponseMap) {
                        System.out.println("requestParamsMap " + requestParamsMap);
                        System.out.println("fortResponseMap " + fortResponseMap);
                        JSONObject responseObject = new JSONObject(fortResponseMap);
                        try {
                            String response_message = responseObject.getString("response_message");
                            Toast.makeText(PaymentMethod.this, response_message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PaymentMethod.this, PayFail.class);
                            intent.putExtra("cardNo", cardNo);
                            intent.putExtra("cardType", cardType);
                            intent.putExtra("cardPersonName", cardPersonName);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    } catch (Exception e) {
        e.printStackTrace();
    }

}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
        try {
            fortCallback.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Log.i("exception", e.getMessage());
        }
}

private String getSignature() {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA-256");
        String text = "sadafdfeeeeaccess_code="+access_code+"
                + "device_id=" + FortSdk.getDeviceId(PaymentMethod.this)
                + "language=enmerchant_identifier="+merchant_identifier"+service_command=SDK_TOKENsadafdfeeee";
        md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed

    } catch (Exception e) {
        e.printStackTrace();
    }
    byte[] digest = md.digest();
    signature = String.format("%064x", new java.math.BigInteger(1, digest));
    return signature;
}

public static String random() {
    SecureRandom secureRandom = new SecureRandom();
    return new BigInteger(40, secureRandom).toString(32);
}
