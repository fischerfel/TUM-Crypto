try {
   PackageInfo info = getPackageManager().getPackageInfo("[your package name, e.g. com.yourcompany.yourapp]", PackageManager.GET_SIGNATURES);
   for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("Hash Key:", Base64.encode(md.digest()));
   }
} catch (NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {

}
