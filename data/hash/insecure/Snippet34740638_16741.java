public class AppPreference extends Application {

Context cxt;

private static final String SHARED_APP_PREFERENCE_NAME = "sabakuchapp_pref_key";
public static AppPreference mAppprefernce;
private SharedPreferences pref;
private Editor mEditor;



public AppPreference() {
    super();
    // TODO Auto-generated constructor stub
}

enum SharedPreferenceKey{
    USER_ID, USER_NAME, SERVER_KEY;
}

private AppPreference (Context context){
    pref = context.getSharedPreferences(SHARED_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
    mEditor = pref.edit();
}



public AppPreference(Context cxt, SharedPreferences pref, Editor mEditor) {
    super();
    this.cxt = cxt;
    this.pref = pref;
    this.mEditor = mEditor;
}


public static AppPreference getInstance(Context context){
    if(mAppprefernce == null){
        mAppprefernce = new AppPreference(context);
    }
    return  mAppprefernce;
}

public void setUserID(String id){
    mEditor.putString(SharedPreferenceKey.USER_ID.toString(), id);
    mEditor.commit();
}

public String getUserID(){
    return pref.getString(SharedPreferenceKey.USER_ID.toString(), "");
}

public void setUserName(String name){
    mEditor.putString(SharedPreferenceKey.USER_NAME.toString(), name);
    mEditor.commit();
}

public String getUserName(){
    return pref.getString(SharedPreferenceKey.USER_NAME.toString(), "");
}

public void setServerKey(){

    String original = getUserID()+"_" + getUserName() + "_SBK";
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("MD5");
        md.update(original.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        mEditor.putString(SharedPreferenceKey.SERVER_KEY.toString(), sb.toString());
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        mEditor.putString(SharedPreferenceKey.SERVER_KEY.toString(), "");
    }       
    mEditor.commit();
    return;
}

public String getServerKey(){
    return pref.getString(SharedPreferenceKey.SERVER_KEY.toString(), "");
}
