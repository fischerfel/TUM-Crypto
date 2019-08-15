X509Certificate cert = CertManager.getCertificate(number, c);  
MessageDigest sha1 = MessageDigest.getInstance("SHA1");
System.out.println("  Subject " + cert.getSubjectDN());
System.out.println("   Issuer  " + cert.getIssuerDN());
sha1.update(cert.getSubjectDN().getName().getBytes());
String hexString =  bytesToHex(sha1.digest());
System.out.println("   sha1    " + hexString);
System.out.println();
