public void saveProfile(Profile newProfile) {
  try {
    Log.i(C.TAG, newProfile.toString());

    SharedPreferences.Editor editor = prefs.edit();
    String profileJSONfied = new Gson().toJson(newProfile);
    Log.i(C.TAG, profileJSONfied);

    byte[] cleartext = profileJSONfied.getBytes(HTTP.UTF_8);
    Log.i(C.TAG, cleartext.toString());

    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    String encrypedProfile = Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);
    Log.i(C.TAG, encrypedProfile);

    editor.putString(PROFILE, encrypedProfile);
    editor.commit();
    profile = newProfile;
  } catch (Exception e) {
    Log.i(C.TAG, e.getMessage());
  }
}

public Profile loadProfile() {
  try {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, key);

    Log.i(C.TAG, prefs.getString(PROFILE, null));

    // byte[] plainTextProfileBytes = Base64.decode(cipher.doFinal(prefs.getString(PROFILE, null).getBytes(HTTP.UTF_8)), Base64.DEFAULT);
    byte[] plainTextProfileBytes = Base64.decode(prefs.getString(PROFILE, null).getBytes(HTTP.UTF_8), Base64.DEFAULT);
    Log.i(C.TAG, new String(plainTextProfileBytes, HTTP.UTF_8));

    profile = new Gson().fromJson(new String(plainTextProfileBytes, HTTP.UTF_8), PROFILE_TYPE);
    Log.i(C.TAG, profile.toString());

  } catch (Exception e) {
    Log.i(C.TAG, e.getMessage());
  }
  return profile;
}
