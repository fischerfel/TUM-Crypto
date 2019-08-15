void printSampleSha1List(Context ctx) {

    List<ApplicationInfo> packages = ctx.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
    for (int i = 0; i < packages.size(); ++i) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(
                    packages.get(i).packageName, PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            for (Signature signature : packageInfo.signatures) {
                // SHA1 the signature
                String sha1 = getSHA1(signature.toByteArray());

                Log.i("Sha1", "name:" + packages.get(i).packageName + ", " + sha1);
                //note sample just checks the first signature
                break;
            }
        }
    }

}

public static String getSHA1(byte[] sig) {
    MessageDigest digest = null;
    try {
        digest = MessageDigest.getInstance("SHA1", "BC");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        e.printStackTrace();
    }
    digest.update(sig);
    byte[] hashtext = digest.digest();
    return bytesToHex(hashtext);
}

//util method to convert byte array to hex string
public static String bytesToHex(byte[] bytes) {
    final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    char[] hexChars = new char[bytes.length * 2];
    int v;
    for (int j = 0; j < bytes.length; j++) {
        v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
