private void getShaKey() {
    try {
      Activity activity = this; // or getActivity() if the code is in fragment
      String packageName = activity.getPackageName();
      PackageInfo info = activity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
      for (android.content.pm.Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(signature.toByteArray());
        byte[] digest = md.digest();
        Log.v(LOG_TAG, "KeyHash: " + bytesToHex(digest) + ";" + packageName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String bytesToHex(byte[] bytes) {
    char[] hexArray = "0123456789ABCDEF".toCharArray();
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < hexChars.length; i += 2) {
      sb.append(hexChars[i]);
      sb.append(hexChars[i + 1]);
      if (i < hexChars.length - 2) {
        sb.append(':');
      }
    }
    return sb.toString();
  }
