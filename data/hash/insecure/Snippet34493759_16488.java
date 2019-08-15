public static void showHashKey(Context context) {

// Set Your Package Name                                                                                                                 
String m_PackageName = "com.example";

    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(m_PackageName, PackageManager.GET_SIGNATURES); 
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("HashKey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {
        Log.d("HashKey", e.getMessage());

    } catch (NoSuchAlgorithmException e) {
        Log.d("HashKey", e.getMessage());

    }
}
