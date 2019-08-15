public class PrepApplication extends AnalyticsApplication {


///
// Constants
///
public static final String TAG = "Prep-";
private static final String PREFERENCES_KEY = "com.youth4work.ibps";
private static final String PACKAGE_NAME = "com.youth4work.ibps";


///
// Static variables.
///
private static PrepApplication sSingleton;

///
// Data members
///
private BaseActivity mActiveBaseActivity;
private String mVersion;
private int mBuildNumber = -1;

///
// Singleton
///
public static PrepApplication singleton() {
    // NOTE: The instance is created by the system.
    return sSingleton;
}

@Override
public void onCreate() {
    super.onCreate();

    sSingleton = this;

    Iconify.with(new FontAwesomeModule());
    FacebookSdk.sdkInitialize(getApplicationContext());
}

///
// Returns the version number.
///
public String getVersionNumber() {
    if (mVersion == null) {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            mVersion = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // do nothing
        }
    }

    return mVersion;
}


///
// Returns the build number.
///
public int getBuildNumber() {
    if (mBuildNumber == -1) {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            mBuildNumber = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // do nothing
        }
    }

    return mBuildNumber;
}

///
// Get Device Token
///
public String getDeviceToken() {

    final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

    final String tmDevice, tmSerial, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

    UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
    return deviceUuid.toString();
}

///
// Set DeviceToken from GCM registration
///
public void setDeviceToken(String deviceToken) {
}

///
// Returns true if this is a debug build.
///
public boolean isDebugBuild() {
    final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    boolean debuggable = false;
    Context ctx = getContext();

    try {
        PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
        Signature signatures[] = pinfo.signatures;

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        for (int i = 0; i < signatures.length; i++) {
            ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
            X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
            debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);

            if (debuggable) {
                break;
            }
        }
    } catch (PackageManager.NameNotFoundException e) {
        // debuggable variable will remain false
    } catch (CertificateException e) {
        // debuggable variable will remain false
    }

    return debuggable;
}


///
// Returns true if only portrait orientation is allowed.
///
public boolean forcePortraitOrientation() {
    return !isDebugBuild();
}


///
// Get the shared App's context. Use this context when an activity's context
// won't do (background tasks, to survive screen rotations, activity isn't easily
// attained etc.)
///
public Context getContext() {
    return getApplicationContext();
}

///
// Returns the active activity.
///
public BaseActivity getActiveBaseActivity() {
    return mActiveBaseActivity;
}

///
// Called to track the active BaseActivity.
///
public void setActiveBaseActivity(BaseActivity activity) {
    mActiveBaseActivity = activity;
}

///
// To get hash key of key store file using which app is signed (either debug or release)
///
@Nullable
public String getHashKey() {
    PackageInfo info;
    String hashKey = null;

    try {
        info = getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_SIGNATURES);

        for (Signature signature : info.signatures) {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            hashKey = new String(Base64.encode(md.digest(), 0));
        }
    } catch (PackageManager.NameNotFoundException e1) {
        hashKey = null;
    } catch (NoSuchAlgorithmException e) {
        hashKey = null;
    } catch (Exception e) {
        hashKey = null;
    }
    return hashKey;
}

}
