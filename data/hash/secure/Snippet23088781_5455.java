final String appPackage = "com.example"  // TODO: Add here the package name!!

try {
    final PackageManager pm = getPackageManager();
    final ApplicationInfo ai = pm.getApplicationInfo(appPackage, PackageManager.GET_META_DATA);

    if ( ai != null ) {
        final PackageInfo pi = pm.getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS);

        if ( pi != null ) {
            final Signature[] signatures = pi.signatures;

            if ( (pi.signatures != null) && (pi.signatures.length > 0) ) {
                for ( final Signature signature : signatures ) {
                    if ( signature != null ) {
                        final InputStream certInputStream = new ByteArrayInputStream(signature.toByteArray());
                        final CertificateFactory certFactory;
                        final X509Certificate x509Cert;

                        try {
                            certFactory = CertificateFactory.getInstance("X509");

                            if ( certFactory != null ) {
                                x509Cert = (X509Certificate) certFactory.generateCertificate(certInputStream);

                                if ( x509Cert != null ) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    Log.d(TAG, "Certificate Owner: " + x509Cert.getSubjectDN().toString());
                                    Log.d(TAG, "Certificate Issuer: " + x509Cert.getIssuerDN().toString());
                                    Log.d(TAG, "Certificate Serial Number: " + x509Cert.getSerialNumber().toString());
                                    Log.d(TAG, "Certificate Algorithm: " + x509Cert.getSigAlgName());
                                    Log.d(TAG, "Certificate Version: " + x509Cert.getVersion());
                                    Log.d(TAG, "Certificate OID: " + x509Cert.getSigAlgOID());
                                    Log.d(TAG, "Certificate Valid From: " + dateFormat.format( x509Cert.getNotBefore() ));
                                    Log.d(TAG, "Certificate Valid To: " + dateFormat.format( x509Cert.getNotAfter() ));

                                    try {
                                        final MessageDigest md = MessageDigest.getInstance("SHA-256");
                                        md.update( x509Cert.getEncoded() );

                                        Log.d(TAG, "Certificate SHA-256: " + getHex(md.digest()));
                                    }
                                    catch ( NoSuchAlgorithmException e ) {
                                        //Debug:
                                        Log.e(TAG, "MessageDigest ERROR: " + e.getMessage() + "\n");
                                        //e.printStackTrace();
                                    }
                                }
                            }
                        }
                        catch ( final CertificateException e ) {
                            //Debug:
                            Log.e(TAG, "CertificateFactory ERROR: " + e.getMessage() + "\n");
                            //e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
catch ( final PackageManager.NameNotFoundException e ) {
    //Debug:
    Log.e(TAG, "ApplicationInfo ERROR: " + e.getMessage() + "\n");
    //e.printStackTrace();
}
