    static public String getPackageFingerPrint( Context ctx ) {
        PackageManager pm = ctx.getPackageManager();
        String packageName = ctx.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = null;

        try {
                packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (NameNotFoundException e) {
                return "";
        }
        Signature[] signatures = packageInfo.signatures;

        byte[] cert = signatures[0].toByteArray();

        InputStream input = new ByteArrayInputStream(cert);

        CertificateFactory cf = null;
        try {
                cf = CertificateFactory.getInstance("X509");


        } catch (CertificateException e) {
                return "";
        }
        X509Certificate c = null;
        try {
                c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
                return "";
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


            return hexString.toString();

        } catch (NoSuchAlgorithmException e1) {
            return "";
        } 
    }
