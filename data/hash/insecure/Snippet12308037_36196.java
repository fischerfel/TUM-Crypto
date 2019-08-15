String f = LauncherFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath(); // path to launcher
java.lang.System.out.println(f);

String launcherHash = "";

try{
  MessageDigest md5  = MessageDigest.getInstance("MD5");
  launcherHash = calculateHash(md5, f);
}catch (Exception e) {
  java.lang.System.out.println(e){
  return;
}
