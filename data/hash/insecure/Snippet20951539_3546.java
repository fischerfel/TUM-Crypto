    PackageInfo packageInfo = null;
    try {
        packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), PackageManager.GET_SIGNATURES);
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }
    Signature[] signatures = packageInfo.signatures;
    byte[] cert = signatures[0].toByteArray();
    InputStream input = new ByteArrayInputStream(cert);

    CertificateFactory cf = null;
    try {
        cf = CertificateFactory.getInstance("X509");
    } catch (CertificateException e) {
        e.printStackTrace();
    }
    X509Certificate c = null;
    try {
        c = (X509Certificate) cf.generateCertificate(input);
    } catch (CertificateException e) {
        e.printStackTrace();
    }
    try {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] publicKey = md.digest(c.getPublicKey().getEncoded());

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<publicKey.length;i++) {
            String appendString = Integer.toHexString(0xFF & publicKey[i]);
            if(appendString.length()==1)hexString.append("0");
            hexString.append(appendString);
        }
        Log.d("SHA1", "Cer: " + hexString.toString());

    } catch (NoSuchAlgorithmException e1) {
        e1.printStackTrace();
    }
