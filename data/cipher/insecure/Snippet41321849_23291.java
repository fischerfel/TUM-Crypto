public class ObscuredSharedPreferences implements SharedPreferences {

    private static final String TAG = "ObscuredSp";
    protected static final String UTF8 = "utf-8";
    private static final char[] SEKRIT = "abc".toCharArray() ; // INSERT A RANDOM PASSWORD HERE.

    protected SharedPreferences delegate;
    protected Context context;

    public ObscuredSharedPreferences(Context context, SharedPreferences delegate) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    public int getInt(String key, int defValue) {
        final String v = delegate.getString(key, null);
        Log.d(TAG, "got int " + v);
        return v!=null ? Integer.parseInt(decrypt(v)) : defValue;
    }

    protected String decrypt(String value){
        try {
            final byte[] bytes = value!=null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes),UTF8);

        } catch( Exception e) {
            throw new RuntimeException(e);
        }
    }
}
