PackageInfo pi = packageManager.getPackageInfo(getPackageName());
byte[] certificate = pi.signatures[0].toByteArray();
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] fingerprint = md.digest(certificate);
String hexFingerprint = toHexString(fingerprint);
