String getSignatureSha1(Context context) {
    PackageManager pm = context.getPackageManager();
    PackageInfo info = null;
    try {
        info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
    } catch (NameNotFoundException e) {
        Log.e(TAG, "Could not find info for " + context.getPackageName());
        return null;
    }

    byte[] signatureBytes = info.signatures[0].toByteArray();
    MessageDigest md = MessageDigest.getInstance("SHA");
    md.update(signatureBytes);
    byte[] digestBytes = md.digest();

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < digestBytes.length; i++) {
        // you can omit this if block if you don't care about the colon separators
        if (i > 0) { 
            builder.append(':');
        }
        builder.append(String.format("%02X", digestBytes[i] & 0xFF));
    }

    return builder.toString();
}
