PackageInfo info=getActivity().getPackageManager().getPackageInfo("com.mypackage.myapp", PackageManager.GET_SIGNATURES);
for(Signature signature : info.signatures)
{
  MessageDigest md=MessageDigest.getInstance("SHA");
  md.update(signature.toByteArray());
  Log.d(Constants.TAG, "APP KeyHash: "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
}
