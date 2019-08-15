//Call this method for KeyHash  
    String keyHash = getKeyHashForFacebook(SplashScreen.this);
    Log.e("keyHash  ", keyHash);


    public static String getKeyHashForFacebook(Context context) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return "KeyHash:" + context.getApplicationContext().getPackageName() + "=>"
                            + Base64.encodeToString(md.digest(), Base64.DEFAULT);
                }
            } catch (Exception e) {
    e.printStackTrace();

            }
            return "=>";
        }
