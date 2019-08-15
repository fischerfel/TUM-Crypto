try
{
    //paste Your package name at the first parameter
    PackageInfo info = getPackageManager().getPackageInfo("PUT_YOUR_PACKAGE_NAME_HERE",
            PackageManager.GET_SIGNATURES);
    for (android.content.pm.Signature signature : info.signatures)
    {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
        Log.e("MY KEY HASH:", sign);
        Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
    }
}
catch (PackageManager.NameNotFoundException e)
{
}
catch (NoSuchAlgorithmException e)
{
}
