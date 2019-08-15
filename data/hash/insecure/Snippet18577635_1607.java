public static void getHashes(Activity act) {
    PackageInfo info;
    try {
        info = act.getPackageManager().getPackageInfo("com.my.package.myapp", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String something = new String(Base64.encode(md.digest(), 0));
            //String something = new String(Base64.encodeBytes(md.digest()));
            Log.i("hash key", something);
        }
    } catch (NameNotFoundException e1) {
        Log.e("name not found", e1.toString());
    } catch (NoSuchAlgorithmException e) {
        Log.e("no such an algorithm", e.toString());
    } catch (Exception e) {
        Log.e("exception", e.toString());
    }
}
