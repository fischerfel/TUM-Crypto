     Override 
public void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState);
 try { 
PackageInfo info = getPackageManager().getPackageInfo( "com.facebook.samples.loginhowto", PackageManager.GET_SIGNATURES); 
for (Signature signature : info.signatures) {
 MessageDigest md = MessageDigest.getInstance("SHA"); 
md.update(signature.toByteArray());
 Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT)); }
 } catch (NameNotFoundException e) {
....
