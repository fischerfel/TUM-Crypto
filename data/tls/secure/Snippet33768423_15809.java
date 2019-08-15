public void sslCheck() {

        int PLAY_SERVICES_RESOLUTION_REQUEST=9000;
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        //Log.i("log_tag2", String.valueOf(resultCode));
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i("log_tag2","Not good!");
                if (MyAppActivity.instance != null) {
                    Log.i("log_tag2","but can fix");
                    GooglePlayServicesUtil.getErrorDialog(
                            resultCode,
                            MyAppActivity.instance,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                }
            }
        }
        else {
            Log.i("log_tag2","Already good!");
            try {
                ProviderInstaller.installIfNeeded(this);
                Log.i("log_tag", "1");
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
            SSLContext helper=null;
            try {
                helper = SSLContext.getInstance("TLSv1.2");
                Log.i("log_tag","2");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                helper.init(null,null,null);
                Log.i("log_tag", "3");
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            SSLEngine engine = helper.createSSLEngine();
        }
    }
