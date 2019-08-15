public String WriteSignature(String packageName)
{     
   PackageManager pm = this.getPackageManager();
   String sig = ""; 
   PackageInfo pi = null;
   try {
       pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
   } catch (NameNotFoundException e1) {
       e1.printStackTrace();
   }

   try {
       for (Signature signature : pi.signatures) {
           MessageDigest md = MessageDigest.getInstance("SHA");
           md.update(signature.toByteArray());
           sig = Base64.encodeToString(md.digest(), Base64.DEFAULT);
           Log.d(ACTIVITY_TAG, sig);
       }
   } catch (NoSuchAlgorithmException e) {
       e.printStackTrace();
   }

   return sig;
}
