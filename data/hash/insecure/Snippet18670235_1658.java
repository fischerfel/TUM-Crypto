public void getHashKeyForFacebook(Activity activity, String packageName){
    try{
        PackageInfo info = activity.getPackageManager().getPackageInfo(packageName,  PackageManager.GET_SIGNATURES);

        for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (Exception ex){

    }
}
