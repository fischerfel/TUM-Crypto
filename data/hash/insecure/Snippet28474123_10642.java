private boolean checkAuthorized() throws SecurityException {
    PackageManager pm = getPackageManager();
    try {
        PackageInfo packageInfo = pm.getPackageInfo(pm.getNameForUid(getCallingUid()),
            PackageManager.GET_SIGNATURES);
        Signature[] signatures = packageInfo.signatures;
        byte[] certBytes = signatures[0].toByteArray();
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(
            new ByteArrayInputStream(certBytes));
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] encodedCert = md.digest(cert.getEncoded());
        String hexString = byte2HexFormatted(encodedCert);

        Log.d("public certificate SHA-1: " + hexString);

        String trustedAppName = trustedCerts.get(hexString);
        if (trustedAppName != null) {
            Log.d("Found public certificate SHA-1 for " + trustedAppName);
            return true;
        }
    } catch (Exception e) {
        Log.e(e, "Unable to get certificate from client");
    }

    Log.w("Couldn't find signature in list of trusted certs!");
    /* Crash the calling application if it doesn't catch */
    throw new SecurityException();
}

public static String byte2HexFormatted(byte[] arr) {
    StringBuilder str = new StringBuilder(arr.length * 2);
    for (int i = 0; i < arr.length; i++) {
        String h = Integer.toHexString(arr[i]);
        int l = h.length();
        if (l == 1) h = "0" + h;
        if (l > 2) h = h.substring(l - 2, l);
        str.append(h.toUpperCase());
        if (i < (arr.length - 1)) str.append(':');
    }
    return str.toString();
}
