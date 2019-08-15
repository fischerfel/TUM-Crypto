public class CheckLicenseService extends IntentService {

    public CheckLicenseService() {
        super("CheckLicenseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final String userEmail = FieldEdgeApplication
                .getSharedPreferences().getString(FieldEdgeConstant.SharedPreferenceKey.KEY_USER_EMAIL, null);
        final String slug = FieldEdgeApplication
                .getSharedPreferences().getString(FieldEdgeConstant.SharedPreferenceKey.KEY_SLUG_NAME, null);

        if (userEmail != null && slug != null) {
            checkAppLicense(userEmail, slug);
        } else {
            startDialogActivity(FieldEdgeApplication.getInstance().getResources().getString(R.string.alert_licensing_err));
        }

    }

    public void checkAppLicense(final String emailId, final String slugName) {

        if (Utils.isNetworkAvailable(FieldEdgeApplication.getInstance())) {

            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {

                    String strResponse = "";

                    try {

                        StringBuilder request = new StringBuilder();
                        request.append(getString(R.string.licensing_server_api));
                        request.append(FieldEdgeConstant.ApiSymbol.AMPERSAND);
                        request.append(FieldEdgeConstant.ApiMethodParamsName.PARAM_EMAIL);
                        request.append(FieldEdgeConstant.ApiSymbol.EQUAL);
                        request.append(emailId);

                        request.append(FieldEdgeConstant.ApiSymbol.AMPERSAND);
                        request.append(FieldEdgeConstant.ApiMethodParamsName.PARAM_KEY);
                        request.append(FieldEdgeConstant.ApiSymbol.EQUAL);
                        request.append(FieldEdgeConstant.API_LICENSE_KEY);

                        request.append(FieldEdgeConstant.ApiSymbol.AMPERSAND);
                        request.append(FieldEdgeConstant.ApiMethodParamsName.PARAM_SLUG);
                        request.append(FieldEdgeConstant.ApiSymbol.EQUAL);
                        request.append(slugName);

                        // Load CAs from an InputStream
                        // (could be from a resource or ByteArrayInputStream or ...)
                        CertificateFactory cf = CertificateFactory.getInstance("X.509");
                        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
                        AssetManager am = getAssets();
                        InputStream caInput = new BufferedInputStream(am.open("fieldedgeapp_com.crt"));
                        Certificate ca;
                        try {
                            ca = cf.generateCertificate(caInput);
                            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                        } finally {
                            caInput.close();
                        }

                        // Create a KeyStore containing our trusted CAs
                        String keyStoreType = KeyStore.getDefaultType();
                        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                        keyStore.load(null, null);
                        keyStore.setCertificateEntry("ca", ca);

                        // Create a TrustManager that trusts the CAs in our KeyStore
                        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                        tmf.init(keyStore);

                        // Create an SSLContext that uses our TrustManager
                        SSLContext context = SSLContext.getInstance("TLS");
                        context.init(null, tmf.getTrustManagers(), null);

                        // Tell the URLConnection to use a SocketFactory from our SSLContext
                        AppLog.LogE(getClass().getName(), "Licensing Url is " + request.toString());
                        URL url = new URL(request.toString());
                        HttpsURLConnection urlConnection =
                                (HttpsURLConnection)url.openConnection();
                        urlConnection.setSSLSocketFactory(context.getSocketFactory());
                        InputStream in = urlConnection.getInputStream();
                        String line = "";
                        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                        while ((line = rd.readLine()) != null) {
                            strResponse += line;

                            Log.e("Str Response "," == "+strResponse);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e("Licence Api Exception", " == " + ex);
                    }

                    return strResponse;
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    AppLog.LogE(getClass().getName(), "Licensing Url Response in service " + response);
                    handleLicensingResponse(response);
                }
            }.execute();

        } else {
            AppLog.LogE(getClass().getName(), "Internet issue while checking licensing in service");
        }

    }

    public void handleLicensingResponse(String response) {

        try {

            if (response != null) {

                final Gson gson = new Gson();
                LicensingResponse licensingResponse = gson.fromJson(response, LicensingResponse.class);

                if (licensingResponse == null) {
                    FileHandler.logResponse("licensingResponse is null ", "No Exception");
//                    startDialogActivity(FieldEdgeApplication.getInstance().getResources().getString(R.string.alert_licensing_err));
                } else {
//                    if false then show message to user and take move user to login page.
                    if (!licensingResponse.success) {
                        if (licensingResponse.message != null && licensingResponse.message.trim().length() > 0) {
                            startDialogActivity(licensingResponse.message);
                        } else {
                            FileHandler.logResponse(licensingResponse.message, null);
                            startDialogActivity(FieldEdgeApplication.getInstance().getResources().getString(R.string.alert_licensing_err));
                        }

                    } else {
//                        if true then do nothing and allowing them to use application.
                    }
                }

            } else {
                FileHandler.logResponse("Response is null", "No Exception");
//                startDialogActivity(FieldEdgeApplication.getInstance().getResources().getString(R.string.alert_licensing_err));
            }

        } catch (Exception ex) {
            FileHandler.logResponse(response, Utils.exceptionToString(ex));
//            startDialogActivity(FieldEdgeApplication.getInstance().getResources().getString(R.string.alert_licensing_err));
        }

    }

    /**
     * method to start login activity
     */
    public void startDialogActivity(String message) {
        Intent dialogIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(FieldEdgeConstant.BundleKeys.LICENSING_ERR_MSG, message);
        dialogIntent.putExtras(bundle);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        dialogIntent.setClass(this, FieldEdgeDialogActivity.class);
        startActivity(dialogIntent);
    }

}
