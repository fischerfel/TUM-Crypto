public SecretKey getSecretKey(Context context){
    SharedPreferences sharedPreferences = context.getSharedPreferences(DashboardFragment.GENERATED_KEY, Context.MODE_PRIVATE);
    String stringKey = sharedPreferences.getString(DashboardFragment.GENERATED_KEY, null);
    byte[] encodedKey = Base64.decode(stringKey, Base64.DEFAULT);
    return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
}
