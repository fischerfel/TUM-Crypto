private void getHashKey(String pkgName)
{
    try
    {
        PackageInfo info = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures)
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String hashKey = Base64.encodeBytes(md.digest());
            _hashKey_et.setText(hashKey);
            Log.i("KeyTool", pkgName + " -> hashKey = " + hashKey);
        }
    }
    catch (NameNotFoundException e)
    {
        e.printStackTrace();
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
}
