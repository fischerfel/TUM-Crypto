PackageInfo info;
try {
    info = getPackageManager().getPackageInfo("YOUR_APP_PACKAGE", PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        String keyhash = new String(Base64.encode(md.digest(), 0));
        //string something is what you should paste as key hash
        Log.e("hash key", keyhash);
    }
} catch (NameNotFoundException e1) {
    Log.e("name not found", e1.toString());
} catch (NoSuchAlgorithmException e) {
    Log.e("no such an algorithm", e.toString());
} catch (Exception e) {
    Log.e("exception", e.toString());
} 
