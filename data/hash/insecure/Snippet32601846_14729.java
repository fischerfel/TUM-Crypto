public void buttonAppClick() {
    final PackageManager pm = getActivity().getPackageManager();
    //get a list of installed apps.
    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
    outputTextView.setText("");
    for (ApplicationInfo packageInfo : packages) {
        try {
            String packageName = packageInfo.packageName;
            outputTextView.append("Apk Path : " + packageInfo.sourceDir + "\n");
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            Signature sig = pi.signatures[0];
            String md5Fingerprint = doFingerprint(sig.toByteArray(), "MD5");
            Log.d(TAG_HOME, "MD5 : " + packageInfo.sourceDir + md5Fingerprint);
            outputTextView.append("MD5 : " + md5Fingerprint + "\n");
            outputTextView.append("\n");
        }
        catch (Exception e) {
            Log.e(TAG_HOME, e.getMessage());
        }
    }
}

protected static String doFingerprint(byte[] certificateBytes, String algorithm)
        throws Exception {
    MessageDigest md = MessageDigest.getInstance(algorithm);
    md.update(certificateBytes);
    byte[] digest = md.digest();

    String toRet = "";
    for (int i = 0; i < digest.length; i++) {
        if (i != 0)
            toRet += ":";
        int b = digest[i] & 0xff;
        String hex = Integer.toHexString(b);
        if (hex.length() == 1)
            toRet += "0";
        toRet += hex;
    }
    return toRet;
}
