public class BillnoxCryptography {

private static final String ALGORITHM = "AES";

public static String encrypt(String seed, String cleartext) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException {
    byte[] rawKey = getRawKey(seed.getBytes());
    byte[] result = encrypt(rawKey, cleartext.getBytes());
    return toHex(result);
}

public static String decrypt(String seed, String encrypted) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException {
    byte[] rawKey = getRawKey(seed.getBytes());
    byte[] enc = toByte(encrypted);
    byte[] result = decrypt(rawKey, enc);
    return new String(result);
}

private static byte[] getRawKey(byte[] seed) {
    seed = Arrays.copyOf(seed, 16);
    SecretKey key = new SecretKeySpec(seed, ALGORITHM);
    byte[] raw = key.getEncoded();
    return raw;
}

// Since the credentials are already secured through shared prefs, we're
// using this as a lightweight solution for obfuscation. Fixing SecureRandom
// to provide cryptographically strong values is outside the scope of this
// application. See
// http://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
@SuppressLint("TrulyRandom")
private static byte[] encrypt(byte[] raw, byte[] clear) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException {
    SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    byte[] encrypted = cipher.doFinal(clear);
    return encrypted;
}

private static byte[] decrypt(byte[] raw, byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}

public static byte[] toByte(String hexString) {
    int len = hexString.length() / 2;
    byte[] result = new byte[len];

    for (int i = 0; i < len; i++) {
        result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
    }

    return result;
}

public static String toHex(byte[] buf) {
    if (buf == null) {
        return "";
    }

    StringBuffer result = new StringBuffer(2 * buf.length);

    for (int i = 0; i < buf.length; i++) {
        appendHex(result, buf[i]);
    }

    return result.toString();
}

private final static String HEX = "0123456789ABCDEF";

private static void appendHex(StringBuffer sb, byte b) {
    sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
}
}


public class BillNoxApp extends Application {



private Activity localActivity;
public static Context mcontext;
private DBHelper db = null;
@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
}




@Override
public void onCreate() {
    super.onCreate();
    this.mcontext = this.getApplicationContext();
    try {
        BillNoxApp.mcontext =this;
        db = new DBHelper(this);
        db.open();
    } catch (Exception e) {
        Log.i("Exception", e + "");
    }

}
public static Context getContext() {

    return mcontext;
}
@Override
public void onLowMemory() {
    super.onLowMemory();
}

@Override
public void onTerminate() {
    db.close();
    super.onTerminate();
}

public DBHelper getDatabase() {
    return db;
}


public void setLocalActivity(Activity localActivity) {
    this.localActivity = localActivity;
}

public Activity getLocalActivity() {
    return this.localActivity;
}




}
