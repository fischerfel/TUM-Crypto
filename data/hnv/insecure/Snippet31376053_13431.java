public class IncomingCallService extends Service implements LoginListener,
    BasicConnectionListener, BasicDeviceListener, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener,
    RadioGroup.OnCheckedChangeListener {

private static Handler handler;
public BasicPhone phone;
BasicPhoneActivity call_screen;
SharedPreferences login_details;
Vibrator vibrator;
Ringtone r;
private AlertDialog incomingAlert;
Uri notification;
public static final String LOGIN_DETAILS = "u18_sales_logins_details";
AudioManager am;
Intent intent;
static String Twilio_id = "",
        INCOMING_AUTH_PHP_SCRIPT = "http://example.com/client_generate_token?users_id=";
Runnable newrun;
Activity context;
Context ctx;

@Override
public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
}

@Override
public void onCreate() {
    login_details = getSharedPreferences(LOGIN_DETAILS,
            Context.MODE_PRIVATE);
    if (login_details.contains("twilio_Id")) {
        Twilio_id = login_details.getString("twilio_Id", "");
    }
    phone = BasicPhone.getInstance(getApplicationContext());
    phone.setListeners(this, this, this);
    new IncomingTokenTask().execute();
    onCallHandler();
    Log.d("toekn", BasicPhone.capabilityToken);
}

@Override
public void onDestroy() {
    super.onDestroy();
    if (phone != null) {
        phone.setListeners(null, null, null);
        phone = null;
    }
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    // TODO Auto-generated method stub
    op_id = (String) intent.getExtras().get("operator_id");
    if (null != intent) {
                if (phone.handleIncomingIntent(intent)) {
                    showIncomingAlert();
                }
            }
    return START_STICKY;
}

private void showIncomingAlert() {

    handler.post(new Runnable() {
        @Override
        public void run() {
            if (incomingAlert == null) {
                notification = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                r = RingtoneManager.getRingtone(getApplicationContext(),
                        notification);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                switch (am.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    r.play();
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    long pattern[] = { 0, 500, 200, 300, 500 };
                    vibrator.vibrate(pattern, 0);
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    r.play();
                    break;
                }
                incomingAlert = new AlertDialog.Builder(
                        getApplicationContext())
                        .setTitle(R.string.incoming_call)
                        .setCancelable(false)
                        .setMessage(R.string.incoming_call_message)
                        .setPositiveButton(R.string.answer,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        switch (am.getRingerMode()) {
                                        case AudioManager.RINGER_MODE_SILENT:
                                            r.stop();
                                            break;
                                        case AudioManager.RINGER_MODE_VIBRATE:
                                            vibrator.cancel();
                                            break;
                                        case AudioManager.RINGER_MODE_NORMAL:
                                            r.stop();
                                            break;
                                        }
                                        phone.acceptConnection();
                                        incomingAlert = null;
                                    }
                                })
                        .setNegativeButton(R.string.ignore,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        switch (am.getRingerMode()) {
                                        case AudioManager.RINGER_MODE_SILENT:
                                            r.stop();
                                            break;
                                        case AudioManager.RINGER_MODE_VIBRATE:
                                            vibrator.cancel();
                                            break;
                                        case AudioManager.RINGER_MODE_NORMAL:
                                            r.stop();
                                            break;
                                        }
                                        phone.ignoreIncomingConnection();
                                        incomingAlert = null;
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(
                                            DialogInterface dialog) {
                                        phone.ignoreIncomingConnection();
                                    }
                                }).create();
                incomingAlert.show();
            }
        }
    });
}

private void hideIncomingAlert() {
    handler.post(new Runnable() {
        @Override
        public void run() {

            if (incomingAlert != null) {
                incomingAlert.dismiss();
                incomingAlert = null;
            }
        }
    });
}

public void onCallHandler() {

    // TODO Auto-generated method stub

    handler = new Handler();
    newrun = new Runnable() {

        @Override
        public void run() {
            handler.removeCallbacks(newrun);
            new IncomingTokenTask().execute();
               if (null != intent) {
                if (phone.handleIncomingIntent(intent)) {
                    showIncomingAlert();
                }
            }
            handler.postDelayed(this, 200000);
        }
    };
    handler.postDelayed(newrun, 80000);

}

class IncomingTokenTask extends AsyncTask<Void, Void, Void> {
    String message;
    JSONObject jsonResponse;
    int crash_app;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... params) {
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory
                .getSocketFactory();
        socketFactory
                .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(
                httpclient.getParams(), registry);
        @SuppressWarnings("unused")
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
                httpclient.getParams());
        // Set verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);// Create
                                                                        // new
                                                                        // HTTP
                                                                        // POST
                                                                        // with
                                                                        // URL
                                                                        // to
                                                                        // php
                                                                        // file
                                                                        // as
                                                                        // parameter

        HttpGet httppost = new HttpGet(INCOMING_AUTH_PHP_SCRIPT + Twilio_id);
        try {
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BasicPhone.capabilityToken = EntityUtils
                            .toString(entity);
                    BasicPhone.decodedString = BasicPhone.capabilityToken
                            .replace("\"", "");
                }
            }
        } catch (Exception e) {
            crash_app = 5;
            message = "Something went wrong. Please try again later.";
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    }
}

@Override
public void onCheckedChanged(RadioGroup arg0, int arg1) {
    // TODO Auto-generated method stub

}

@Override
public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
    // TODO Auto-generated method stub

}

@Override
public void onClick(View arg0) {
    // TODO Auto-generated method stub

}

@Override
public void onDeviceStartedListening() {
    // TODO Auto-generated method stub

}

@Override
public void onDeviceStoppedListening(Exception error) {
    // TODO Auto-generated method stub

}

@Override
public void onIncomingConnectionDisconnected() {
    hideIncomingAlert();
}

@Override
public void onConnectionConnecting() {
    // TODO Auto-generated method stub

}

@Override
public void onConnectionConnected() {
    // TODO Auto-generated method stub

}

@Override
public void onConnectionFailedConnecting(Exception error) {
    // TODO Auto-generated method stub

}

@Override
public void onConnectionDisconnecting() {
    // TODO Auto-generated method stub

}

@Override
public void onConnectionDisconnected() {
    // TODO Auto-generated method stub

}

@Override
public void onConnectionFailed(Exception error) {
    // TODO Auto-generated method stub

}

@Override
public void onLoginStarted() {
    // TODO Auto-generated method stub

}

@Override
public void onLoginFinished() {
    // TODO Auto-generated method stub

}

@Override
public void onLoginError(Exception error) {
    // TODO Auto-generated method stub

}

}
