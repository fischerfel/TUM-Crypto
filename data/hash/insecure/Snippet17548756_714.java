public String getCertificateFingerprint() throws NameNotFoundException, CertificateException, NoSuchAlgorithmException {
        PackageManager pm = context.getPackageManager();
        String packageName =context.getPackageName();

        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = null;

        packageInfo = pm.getPackageInfo(packageName, flags);
        Signature[] signatures = packageInfo.signatures;

        byte[] cert = signatures[0].toByteArray();

        InputStream input = new ByteArrayInputStream(cert);

        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X509");

        X509Certificate c = null;
        c = (X509Certificate) cf.generateCertificate(input);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] publicKey = md.digest(c.getPublicKey().getEncoded());

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < publicKey.length; i++) {
            String appendString = Integer.toHexString(0xFF & publicKey[i]);
            if (appendString.length() == 1)
                hexString.append("0");
            hexString.append(appendString);
        }

        return hexString.toString();
    }
