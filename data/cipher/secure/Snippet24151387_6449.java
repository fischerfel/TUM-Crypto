public class StoredPrefsHandler {
    public static class SecurePreferencesException extends RuntimeException {
        private static final long serialVersionUID = 3051912281127821578L;

        public SecurePreferencesException(Throwable e) {
            super(e);
        }
    }

    // Stripped down part, more fields unrelevant to the problem have been hidden
    private final String SP_LOCATION = "org.conrogatio.lazyrace.prefs";
    private final String SSP_LOCATION = "org.conrogatio.lazyrace.secureprefs";
    private String LOCATION;
    private Context c;
    private SharedPreferences prefs;
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
private static final String CHARSET = "UTF-8";
    private final Cipher writer;
    private final Cipher reader;
    private final Cipher keyWriter;

    /**
     * This will initialize an instance of the SecurePreferences class
     * 
     * @param context
     *            your current context.
     * @throws SecurePreferencesException
     */
    public StoredPrefsHandler(Context context, boolean secure) throws SecurePreferencesException {
        c = context;
        secured = secure;
        if (secured) {
            LOCATION = SSP_LOCATION;
            deviceId = Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
            try {
                this.writer = Cipher.getInstance(TRANSFORMATION);
                this.reader = Cipher.getInstance(TRANSFORMATION);
                this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);
                initCiphers(deviceId + SSP_SALT);
                this.prefs = c.getSharedPreferences(SSP_LOCATION, Context.MODE_PRIVATE);
            } catch (GeneralSecurityException e) {
                throw new SecurePreferencesException(e);
            } catch (UnsupportedEncodingException e) {
                throw new SecurePreferencesException(e);
            }
        } else {
            LOCATION = SP_LOCATION;
        }
        update();
    }
