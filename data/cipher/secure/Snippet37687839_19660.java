import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.takwolf.android.lock9.Lock9View;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;

public class AppCheckServices extends Service {

    public static String currentApp = "";
    public static String previousApp = "";
    ImageView imageView;
    SharedPreference sharedPreference;
    SharedPreferences sharedPreferences;

    List<String> pakageName;
    String currentProfile;
    private Context context = null;
    private Timer timer;
    private WindowManager windowManager;
    private static final String KEY_NAME = "my_key";
    private Dialog dialog;

    FingerprintUiHelper.FingerprintUiHelperBuilder mFingerprintUiHelperBuilder;
    private FingerprintManagerCompat.CryptoObject mCryptoObject;
    private KeyStore mKeyStore;
    private Cipher mCipher;
    private FingerprintUiHelper mFingerprintUiHelper;
    @Inject
    FingerprintManagerCompat mFingerprintManager;
    @Inject
    SharedPreferences mSharedPreferences;

    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            String currentlyActiveProfile = sharedPreferences.getString("ActiveProfile", "");
            if (sharedPreference != null) {
                pakageName = sharedPreference.getLocked(context, currentlyActiveProfile);
            }
            if (isConcernedAppIsInForeground()) {
                if (imageView != null) {
                    imageView.post(new Runnable() {
                        public void run() {
                            if (!currentApp.matches(previousApp)) {
                                showUnlockDialog();
                                previousApp = currentApp;
                            }
                        }
                    });
                }
            } else {
                if (imageView != null) {
                    imageView.post(new Runnable() {
                        public void run() {
                            hideUnlockDialog();
                        }
                    });
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        currentProfile = sharedPreferences.getString("ActiveProfile", null);
        if (currentProfile != null) {
            Log.i("jcheckvalues", currentProfile);
        }
        context = getApplicationContext();
        sharedPreference = new SharedPreference();
        if (sharedPreference != null) {
            pakageName = sharedPreference.getLocked(context, currentProfile);
        }
        timer = new Timer("AppCheckServices");
        timer.schedule(updateTask, 500L, 500L);

        final Tracker t = ((AppLockApplication) getApplication()).getTracker(AppLockApplication.TrackerName.APP_TRACKER);
        t.setScreenName(AppLockConstants.APP_LOCK);
        t.send(new HitBuilders.AppViewBuilder().build());

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        imageView = new ImageView(this);
        imageView.setVisibility(View.GONE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.x = ((getApplicationContext().getResources().getDisplayMetrics().widthPixels) / 2);
        params.y = ((getApplicationContext().getResources().getDisplayMetrics().heightPixels) / 2);
        windowManager.addView(imageView, params);
    }

    void showUnlockDialog() {
        showDialog();
    }

    void hideUnlockDialog() {
        previousApp = "";
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showDialog() {
        if (context == null)
            context = getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String PasswordType = sharedPreferences.getString("PasswordType", "");
        if (PasswordType.equals("pattern")) {
            showPatternDialog(context);
        } else if (PasswordType.equals("pin")) {
//            showPinDialog(context);
        }
    }

    private void showPatternDialog(Context context) {
        ///////////////////////finger print code////////////////////////////
        int i = ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT);
        boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.USE_FINGERPRINT)
                == PackageManager.PERMISSION_GRANTED;
        mFingerprintManager = FingerprintManagerCompat.from(getApplicationContext());
        mFingerprintUiHelperBuilder = new FingerprintUiHelper.FingerprintUiHelperBuilder(mFingerprintManager); // FingerprintUiHelper
        Log.i("CheckPermissionstate", "" + isFingerprintPermissionGranted);

        if (isFingerprintPermissionGranted) {

            if (!mFingerprintManager.isHardwareDetected()) {
                Toast.makeText(this, "Your device does not support Finger Print",
                        Toast.LENGTH_LONG).show();
            } else if (!mFingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "you are good to go", Toast.LENGTH_LONG).show();
                createKey();
                check();
            }
        }
//        ///////////////////////finger print code////////////////////////////
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptsView = layoutInflater.inflate(R.layout.popup_unlock, null);
        Lock9View lock9View = (Lock9View) promptsView.findViewById(R.id.lock_9_view);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String pattern = sharedPreferences.getString("Password", "");
        lock9View.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {
                if (password.matches(pattern)) {
                    dialog.dismiss();
                    AppLockLogEvents.logEvents(AppLockConstants.PASSWORD_CHECK_SCREEN, "Correct Password", "correct_password", "");
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Pattern Try Again", Toast.LENGTH_SHORT).show();
                    AppLockLogEvents.logEvents(AppLockConstants.PASSWORD_CHECK_SCREEN, "Wrong Password", "wrong_password", "");
                }
            }
        });
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(promptsView);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();
    }

    ///finger print method
    private void check() {
        boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.USE_FINGERPRINT)
                == PackageManager.PERMISSION_GRANTED;
        boolean isFingerprintAvailable = mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();

        if (!isFingerprintPermissionGranted || !isFingerprintAvailable) {

        } else if (initCipher()) {

            setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
            FingerprintUiHelper.Callback callback = new FingerprintUiHelper.Callback() {
                @Override
                public void onAuthenticated() {
                    Log.i("checkauthenctication", "true");
                }

                @Override
                public void onError() {
                    Log.i("checkauthenctication", "false");
                }
            };
            mFingerprintUiHelper = mFingerprintUiHelperBuilder.build(
                    callback);
            mFingerprintUiHelper.startListening(mCryptoObject);

            if (mFingerprintUiHelper.isFingerprintAuthAvailable()) {
                Log.i("checkavailability", "if");
            } else {
                Log.i("checkavailability", "else");
            }
        } else {
        }
    }

    private void setCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean initCipher() {
        try {
            if (mKeyStore == null) {
                createKey();
            }
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);

            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    //create key for finger print
    @TargetApi(Build.VERSION_CODES.M)
    private void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);

            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | KeyStoreException
                | CertificateException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
        }
        return START_STICKY;
    }

    public boolean isConcernedAppIsInForeground() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(5);
        if (Build.VERSION.SDK_INT <= 20) {
            if (task.size() > 0) {
                ComponentName componentInfo = task.get(0).topActivity;
                for (int i = 0; pakageName != null && i < pakageName.size(); i++) {
                    if (componentInfo.getPackageName().equals(pakageName.get(i))) {
                        currentApp = pakageName.get(i);
                        return true;
                    }
                }
            }
        } else {
            String mpackageName = manager.getRunningAppProcesses().get(0).processName;
            UsageStatsManager usage = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    runningTask.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (runningTask.isEmpty()) {
                    mpackageName = "";
                } else {
                    mpackageName = runningTask.get(runningTask.lastKey()).getPackageName();
                }
            }
            for (int i = 0; pakageName != null && i < pakageName.size(); i++) {
                if (mpackageName.equals(pakageName.get(i))) {
                    currentApp = pakageName.get(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
        if (imageView != null) {
            windowManager.removeView(imageView);
        }
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}`enter code here`
