public static void printHashKey(Context context) 
{ 
    // Add code to print out the key hash 
    try 
    { 
        PackageInfo info = context.getPackageManager().getPackageInfo( com.example.app.BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) 
        { 
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } 
    catch (PackageManager.NameNotFoundException e) 
    { 
        e.printStackTrace(); 
    } 
    catch (NoSuchAlgorithmException e) 
    { 
        e.printStackTrace(); 
    } 
}
