public class UpdateActivity extends Activity {

    private TelephonyManager tm;

    AlertDialog mConfirmAlert = null;
    NetworkTask task;
    ImageView image, text;
    AlertDialog mErrorAlert = null;
    public static ArrayList<String> NameArr = new ArrayList<String>();
    public static ArrayList<String> ValueArr = new ArrayList<String>();
    public static ArrayList<String> nameArr = new ArrayList<String>();
    public static ArrayList<String> ApnArr = new ArrayList<String>();
    public static ArrayList<String> mmscArr = new ArrayList<String>();
    public static ArrayList<String> mmsportArr = new ArrayList<String>();
    public static ArrayList<String> mmsproxyArr = new ArrayList<String>();
    public static ArrayList<String> portArr = new ArrayList<String>();
    public static ArrayList<String> proxyArr = new ArrayList<String>();
    public static int count;
    public AnimationDrawable mTextAnimation = null;
    TextView mUpdatetext;
    public static InputStream stream = null;
    int version;
    public static BigInteger iD1, iD2, mdN1, mdN2;
    BigInteger[] id, mdnId;
    public static String ICCID, MDN;

    public static String caR;

    public static int result;
    private static final String LOG_TAG = "DataSettings";

    public static final String Base_URL = "https://someurl.testsite555.com/REST/phoneSettings";

    public static XmlParserHandlerFinal handler;
    public static int TotalSteps = 8;

    public FileInputStream fis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // instance for xml parser class
        handler = new XmlParserHandlerFinal();
        handler.setContext(this.getBaseContext());
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        int networkType = tm.getNetworkType();
        int phoneType = tm.getPhoneType();
        version = android.os.Build.VERSION.SDK_INT;
        // to get MDN(MCC+MNC) of the provider of the SIM and ICCID (Serial
        // number of the SIM)
        // and to check for the Carrier type
        getImpVariablesForQuery();
        task = new NetworkTask();
        if (phoneType == TelephonyManager.PHONE_TYPE_CDMA
                || (phoneType != TelephonyManager.PHONE_TYPE_GSM
                        && networkType != TelephonyManager.NETWORK_TYPE_GPRS
                        && networkType != TelephonyManager.NETWORK_TYPE_EDGE
                        && networkType != TelephonyManager.NETWORK_TYPE_HSDPA
                        && networkType != TelephonyManager.NETWORK_TYPE_HSPA
                        && networkType != TelephonyManager.NETWORK_TYPE_HSPAP
                        && networkType != TelephonyManager.NETWORK_TYPE_HSUPA
                        && networkType != TelephonyManager.NETWORK_TYPE_UMTS && networkType != TelephonyManager.NETWORK_TYPE_LTE)) {
            // If the phone type is CDMA or
            // the phone phone type is not GSM and the network type is none of
            // the network types indicated in the statement
            // Display incompatibility message
            showAlert(getString(R.string.incomp_sm_dialog));
            // Network type is looked because some tablets have no phone type.
            // We rely on network type in such cases
        } else if (!(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT
                || (tm.getSimOperator())
                        .equals(getString(R.string.numeric_tmo)) || (tm
                    .getSimOperator()).equals(getString(R.string.numeric_att)))) {
            // if SIM is present and is NOT a T-Mo or ATT network SIM,
            // display Error message alert indicating to use SM SIM
            showAlert(getString(R.string.insert_sm_dialog));
        }// No SIM or SIM with T-Mo & ATT MNC MCC present
        else if ((tm.getSimOperator()).equals(getString(R.string.numeric_tmo))
                || (tm.getSimOperator())
                        .equals(getString(R.string.numeric_att))) {
            // Device has T-Mo or ATT network SIM card MCC and MNC correctly
            // populated
            TotalSteps = 6;
            setContentView(R.layout.updating);

//          image = (ImageView) findViewById(R.id.updating_image);
//          text = (ImageView) findViewById(R.id.updating_text);
//
//          text.setBackgroundResource(R.drawable.animation);
//          mTextAnimation = (AnimationDrawable) text.getBackground();

            // AsyncTask to call the web service

            task.execute("");

        }
    }

//  @Override
//  public void onWindowFocusChanged(boolean hasFocus) {
//      super.onWindowFocusChanged(hasFocus);
//      // Animate GO button when corresponding window is in focus
//      Animation a = AnimationUtils.loadAnimation(getBaseContext(),
//              R.anim.image);
//      a.setDuration(1000);
//      a.setInterpolator(new Interpolator() {
//          private final int frameCount = 16;
//
//          @Override
//          public float getInterpolation(float input) {
//              return (float) Math.floor(input * frameCount) / frameCount;
//          }
//      });
//      image.startAnimation(a);
//
//      mTextAnimation.start();
//  }

    private void getImpVariablesForQuery() {

        long d = 1234;
        BigInteger divisor = BigInteger.valueOf(d);
        // to get MDN
        MDN = tm.getLine1Number();
        // MDN = "7862125102";
        if (MDN.equals("")) {
            mdN1 = null;
            mdN2 = null;
        } else {

            Log.d("MDN", MDN);
            BigInteger bInt = new BigInteger(MDN);
            mdnId = bInt.divideAndRemainder(divisor);
            // to retrieve ICCID number of the SIM
            mdN1 = mdnId[1];
            System.out.println("MDN%1234 = " + mdN1);
            mdN2 = mdnId[0];
            System.out.println("MDN/1234 = " + mdN2);

        }
        ICCID = tm.getSimSerialNumber();
        if (ICCID.equals("")) {
            iD1 = null;
            iD2 = null;
        } else {
            Log.d("ICCID", ICCID);

            BigInteger bInteger = new BigInteger(ICCID);
            id = bInteger.divideAndRemainder(divisor);
            iD1 = id[1];
            System.out.println("ICCID%1234 = " + iD1);
            iD2 = id[0];
            System.out.println("ICCID/1234 = " + iD2);
        }
        // Check for the Carrier Type
        if ((tm.getSimOperator()).equals(getString(R.string.numeric_tmo))) {
            caR = "TMO";
        } else if ((tm.getSimOperator())
                .equals(getString(R.string.numeric_att))) {
            caR = "ATT";
        }

    }

    // method to save the ArrayLists from parser
    public static void setArrayList() {
        nameArr = handler.getnameArr();
        ApnArr = handler.getApnArr();
        mmscArr = handler.getMMSCArr();
        mmsproxyArr = handler.getMmscProxyArr();
        mmsportArr = handler.getMmsPortArr();
        proxyArr = handler.getProxyArr();
        portArr = handler.getPortArr();
        count = handler.getCount();
        result = handler.getResult();

    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateActivity.this.finish();
                    }
                });
        mConfirmAlert = builder.create();
        mConfirmAlert.show();
    }

    public ArrayList<String> getnameArr() {

        return nameArr;
    }

    public ArrayList<String> getApnArr() {

        return ApnArr;
    }

    public ArrayList<String> getMMSCArr() {

        return mmscArr;
    }

    public ArrayList<String> getMmscProxyArr() {

        return mmsproxyArr;
    }

    public ArrayList<String> getMmsPortArr() {

        return mmsportArr;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<String> getProxyArr() {

        return proxyArr;
    }

    public ArrayList<String> getPortArr() {

        return portArr;
    }

    // AsyncTask to call web service
    private class NetworkTask extends AsyncTask<String, Integer, InputStream> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected InputStream doInBackground(String... params) {

            try {
                /*
                 * query the server for the data settings put the returned data
                 * settings into values
                 */
                // String charset = "UTF-8";
                // String param1 = "mdn1=";
                // String param2 = "&mdn2=";
                // String param3 = "&car=";
                // String param4 = "&id1=";
                // String param5 = "&id2=";

                // String parameters = param1 + Mdn1 + param2 + Mdn2 + param3
                // + Car + param4 + iD1 + param5 + iD2;
                // String parameters = param1 + Mdn1 + param2 + Mdn2 ;
                // URL = Base_URL + URLEncoder.encode(parameters, charset);

                Log.i("url...", Base_URL);

                stream = getQueryResults(Base_URL);

            } catch (IOException e) {

                Log.v(LOG_TAG, e.toString());
                e.printStackTrace();
            } catch (SAXException e) {

                Log.v(LOG_TAG, e.toString());
                e.printStackTrace();
            } catch (Exception e) {

                Log.v(LOG_TAG, e.toString());
                e.printStackTrace();
            }

            return stream;
        }

        /*
         * Sends a query to server and gets back the parsed results in a bundle
         * urlQueryString - URL for calling the webservice
         */
        protected synchronized InputStream getQueryResults(String urlQueryString)
                throws IOException, SAXException, SSLException,
                SocketTimeoutException, Exception {
            // HttpsURLConnection https = null;
            try {
                // HttpsURLConnection https = null;
                String uri = urlQueryString;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                // BasicNameValuePair username = new
                // BasicNameValuePair("username",
                // "nmundru");
                // BasicNameValuePair password = new
                // BasicNameValuePair("password",
                // "abc123");
                BasicNameValuePair mdn1, mdn2,id1,id2;
                if (MDN.equals("")) {
                    mdn1 = new BasicNameValuePair("mdn1", null);
                    mdn2 = new BasicNameValuePair("mdn2", null);
                } else {
                    mdn1 = new BasicNameValuePair("mdn1", mdN1.toString());
                    mdn2 = new BasicNameValuePair("mdn2", mdN2.toString());

                }

                BasicNameValuePair car = new BasicNameValuePair("car", caR);
                if (ICCID.equals("")) {
                     id1 = new BasicNameValuePair("id1", null);
                     id2 = new BasicNameValuePair("id2", null);
                } else {
                     id1 = new BasicNameValuePair("id1",
                            iD1.toString());
                     id2 = new BasicNameValuePair("id2",
                            iD2.toString());
                }

                // nameValuePairs.add(username);
                // nameValuePairs.add(password);
                nameValuePairs.add(mdn1);
                nameValuePairs.add(mdn2);
                nameValuePairs.add(car);
                nameValuePairs.add(id1);
                nameValuePairs.add(id2);

                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                        nameValuePairs, "ISO-8859-1");
                KeyStore trustStore = KeyStore.getInstance(KeyStore
                        .getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory
                        .getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                        params, registry);

                HttpClient httpClient = new DefaultHttpClient(ccm, params);
                params = httpClient.getParams();
                HttpClientParams.setRedirecting(params, true);

                HttpPost httpPost = new HttpPost(uri);
                httpPost.addHeader("Authorization",
                        getB64Auth("nmundru", "abc123"));

                httpPost.setHeader("Content-Type", "text/plain; charset=utf-8");

                Log.v("httpPost", httpPost.toString());

                httpPost.setEntity(urlEncodedFormEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                System.out.println("response...." + httpResponse.toString());
                Log.v("response...", httpResponse.toString());

                stream = httpResponse.getEntity().getContent();

                // save the InputStream in a file

                try {

                    FileOutputStream fOut = openFileOutput("settings.xml",
                            Context.MODE_WORLD_READABLE);

                    DataInputStream in = new DataInputStream(stream);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                         System.out.println(strLine); //to print the response
                        // in logcat
                        fOut.write(strLine.getBytes());

                    }
                    fOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fis = openFileInput("settings.xml");

            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());

                // e.printStackTrace();
                tryagain();

            } finally {
                // https.disconnect();
            }

            return stream;
        }

        private String getB64Auth(String login, String pass) {
            String source = login + ":" + pass;
            String ret = "Basic "
                    + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE
                            | Base64.NO_WRAP);
            return ret;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPostExecute(InputStream stream) {
            super.onPostExecute(stream);
            // This method is called to parse the response and save the
            // ArrayLists
            success();

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (mErrorAlert != null)
            mErrorAlert.dismiss();
    }

    public void tryagain() {
        // Displaying final layout after failure of pre-ICS automatic settings
        // update
//      setContentView(R.layout.tryagain);
//      String tryAgainText = "";
//      CharSequence styledTryAgainText;
//
//      tryAgainText = String.format(getString(R.string.tryagain_text1),
//              TotalSteps);
//      styledTryAgainText = Html.fromHtml(tryAgainText);
//      TextView tryAgain1 = (TextView) findViewById(R.id.tryagain_text1);
//      tryAgain1.setText(styledTryAgainText);
//
//      tryAgainText = String.format(getString(R.string.tryagain_text2),
//              TotalSteps);
//      styledTryAgainText = Html.fromHtml(tryAgainText);
//      TextView tryAgain2 = (TextView) findViewById(R.id.tryagain_text2);
//      tryAgain2.setText(styledTryAgainText);
//
//      tryAgainText = String.format(getString(R.string.tryagain_text3),
//              TotalSteps);
//      styledTryAgainText = Html.fromHtml(tryAgainText);
//      TextView tryAgain3 = (TextView) findViewById(R.id.tryagain_text3);
//      tryAgain3.setText(styledTryAgainText);

    }

    public void success() {

        // to parse the response
        try {
            handler.getQueryResponse(fis);
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // set method to save the ArryaLists from the parser
        setArrayList();
        Intent i = new Intent(this, ConfigFinalActivity.class);
        startActivity(i);
        finish();

    }

    public int getResult() {

        return result;
    }

}
