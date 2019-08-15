and for debug key useing this approch

try {
PackageInfo info = getPackageManager().getPackageInfo(
"com.samsung.smart_learning",
PackageManager.GET_SIGNATURES);
for (Signature signature : info.signatures) {
MessageDigest md = MessageDigest.getInstance("SHA-1");
md.reset();
md.update(signature.toByteArray());
Log.d("KeyHash:++++++++++++++++++++",
Base64.encodeToString(md.digest(), Base64.DEFAULT));
System.out.println("shivansj_++"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
}
} catch (NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {

}
