 public void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState);
AssetManager am = getResources().getAssets();
String as = null;
try {
    InputStream is=am.open("sdapk.db");
    as=is.toString();
}catch(IOException e) {
    Log.v("Error_E",""+e);
}
String  res = md5(as);
TextView tv = new TextView(this);
tv.setText(res);
setContentView(tv);
}
public String md5(String s) { 
try { 
    MessageDigest digest = java.security.MessageDigest.getInstance("MD5"); digest.update(s.getBytes()); byte messageDigest[] = digest.digest();

 // Create Hex String
 StringBuffer hexString = new StringBuffer();
 for (int i=0; i<messageDigest.length; i++)
     hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
return hexString.toString();

} catch (NoSuchAlgorithmException e) {
 e.printStackTrace();
}
return "";
}

}
