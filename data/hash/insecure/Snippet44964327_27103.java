private static AssetManager assetManager;
public static List<IAndroidMessageReceiver> receivers;
public static native String GetBase64PublicKey();
public static native boolean IsDebug();
public static native boolean IsFree();
public static native boolean IsPocketBugs();
public static native boolean JavaToNative(byte[] bArr);
public static native void OnActivityResult(int i, int i2);
public static native void OnCreateActivity(Activity activity, AssetManager assetManager, byte[] bArr);
public static native void OnDestroyActivity();
public static native boolean OnKeyEvent(int i, boolean z);
public static native void OnPause();
public static native void OnPauseActivity();
public static native void OnResume();
public static native void OnResumeActivity();
public static native void OnSensorChanged(float[] fArr);
public static native void OnStopActivity();
public static native void OnSurfaceCreated();
public static native void OnTouchEvent(float[] fArr);
public static native void Resize(int i, int i2);
public static native void Step();

public static void OnCreateActivity(Activity activity, byte[] messageData) {
    assetManager = activity.getAssets();
    OnCreateActivity(activity, assetManager, messageData);
}

public static int LoadSound(String filename, SoundPool soundPool) {
    try {
        AssetFileDescriptor fd = assetManager.openFd(filename);
        int result = soundPool.load(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength(), 1);
        return result;
    } catch (IOException e) {
        e.printStackTrace();
        return 0;
    }
}

public static int NativeToJava(byte[] data) {
    AndroidMessage am = AndroidMessage.parse(data);
    for (int i = 0; i < receivers.size(); i++) {
        if (((IAndroidMessageReceiver) receivers.get(i)).onReceive(am)) {
            return am.retValue;
        }
    }
    return am.retValue;
}

public static String CalcUniqueIdentifier(Activity activity) {
    String pseudoId = ((TelephonyManager) activity.getSystemService("phone")).getDeviceId() + Secure.getString(activity.getContentResolver(), "android_id");
    String pseudoIdHash = new String();
    try {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(pseudoId.getBytes(), 0, pseudoId.length());
        byte[] md5Data = m.digest();
        for (byte b : md5Data) {
            int b2 = b & 255;
            if (b2 <= 15) {
                pseudoIdHash = pseudoIdHash + "0";
            }
            pseudoIdHash = pseudoIdHash + Integer.toHexString(b2);
        }
        pseudoIdHash = pseudoIdHash.toLowerCase();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return pseudoIdHash;
}

static {
    System.loadLibrary("App");
    receivers = new ArrayList();
}
