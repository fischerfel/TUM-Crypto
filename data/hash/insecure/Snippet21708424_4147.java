/** Constant to identify your sharedPref */
public static final String MY_SHARED_PREFERENCES    = "mySharedPrefInstance";
public static final String USER_TAG                 = "sharedPrefsUserTag";

// Get shared preferences
SharedPreferences sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, MODE_PRIVATE);

// Check if sharedPreferences already contain this user
if (sharedPref.getString(USER_TAG) == textView.gettext()) {
    // The user is already saved
} else {
    // Save a string obj
sharedPref.edit().putString(USER_TAG, editTextUser.getText());
// Save password too
sharedPref.edit().putString(PWD_TAG, editTextPwd.getText());

// Maybe, you would to encrypt the pwd (I recommend this) 
// So, for example:
sharedPref.edit().putString(PWD_TAG, MD5( editTextPwd.getText() ) );

// Commit changes
sharedPref.commit();

}

/** Encrypt params string with MD5 algorithm */
public static String MD5(String md5) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) { }
    return null;
}
