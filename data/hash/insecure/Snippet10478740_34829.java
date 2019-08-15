... someFoo(X509Certificate cert) {
    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    System.out.println("  Subject " + cert.getSubjectDN());
    System.out.println("   Issuer  " + cert.getIssuerDN());
    sha1.update(cert.getEncoded());
    System.out.println("   sha1    " + toHexString(sha1.digest()));
    System.out.println();
  }
