    PackageManager packageManager = getPackageManager();
    int flag = PackageManager.GET_SIGNATURES;

    PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, flag);

            byte[] certificates = packageInfo.signatures[0].toByteArray();
            InputStream input = new ByteArrayInputStream(certificates);
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            X509Certificate certificate = (X509Certificate) factory.generateCertificate(input);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] publicKey = messageDigest.digest(certificate.getEncoded());

        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
