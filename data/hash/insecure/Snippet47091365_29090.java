protected boolean checkFont(String apkname) {
  if (DEBUG) {
    Log.secD("FlipFont", "checkFont - checking apkname" + apkname);
  }
  if ("com.monotype.android.font.foundation".equals(apkname)) {
    return false;
  }
  PackageManager pm = this.mContext.getPackageManager();
  for (int i = 0; i < apkNameList.length; i++) {
    if (apkname != null) {
      if (apkname.equals(apkNameList[i])) {
        this.isCheckPlatformSignatures = pm.checkSignatures("android", apkNameList[i]) == 0;
        this.isCheckReleaseSignatures = Utils.isSignatureMatch(this.mContext, apkNameList[i]);
        Log.i("FontPreviewTablet", "apkname : " + apkname + ", isCheckPlatformSignatures : " + this.isCheckPlatformSignatures + ", isCheckReleaseSignatures : " + this.isCheckReleaseSignatures);
        if (!(this.isCheckPlatformSignatures || this.isCheckReleaseSignatures)) {
          if (apkname.equals("")) {
          }
        }
        return false;
      }
      continue;
    }
  }
  if (DEBUG) {
    Log.secD("FlipFont", "checkFont - check if valid certificate");
  }
  PackageInfo packageInfo = null;
  try {
    packageInfo = this.mFontListAdapter.mPackageManager.getPackageInfo(apkname, 64);
  } catch (Exception e) {
  }
  if (packageInfo != null) {
    Signature[] signatures = packageInfo.signatures;
    byte[] cert = signatures[0].toByteArray();
    try {
      MessageDigest md = MessageDigest.getInstance("SHA");
      md.update(signatures[0].toByteArray());
      if ("T84drf8v3ZMOLvt2SFG/K7ODXgI=".equals(Base64.encodeToString(md.digest(), 0).trim())) {
        if (DEBUG) {
          Log.v("FlipFont", "**Signature is correct**");
        }
        return false;
      }
      if (DEBUG) {
        Log.v("FlipFont", "**Signature is incorrect**");
      }
      return true;
    } catch (Exception e2) {
      e2.printStackTrace();
      InputStream input = new ByteArrayInputStream(cert);
      CertificateFactory cf = null;
      try {
        cf = CertificateFactory.getInstance("X509");
      } catch (CertificateException e3) {
        e3.printStackTrace();
      }
      X509Certificate c = null;
      try {
        c = (X509Certificate) cf.generateCertificate(input);
      } catch (CertificateException e32) {
        e32.printStackTrace();
      }
      if (DEBUG) {
        Log.secD("Example", "APK name: " + apkname);
        if (c != null) {
          Log.secD("Example", "Certificate for: " + c.getSubjectDN());
          Log.secD("Example", "Certificate issued by: " + c.getIssuerDN());
          Log.secD("Example", "The certificate is valid from " + c.getNotBefore() + " to " + c.getNotAfter());
          Log.secD("Example", "Certificate SN# " + c.getSerialNumber());
          Log.secD("Example", "Generated with " + c.getSigAlgName());
        }
      }
      String certIssuedByString = "CN=Ed Platz, OU=Display Imaging, O=Monotype Imanging Inc., L=Woburn, ST=MA, C=US";
      if (c != null && certIssuedByString.equals(c.getIssuerDN().toString())) {
        if (DEBUG) {
          Log.secD("FlipFont", "**Certificate data is correct**");
        }
        return false;
      }
    }
  }
  return true;
}
