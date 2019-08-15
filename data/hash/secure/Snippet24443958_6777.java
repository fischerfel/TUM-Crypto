public String getThumbPrint(X509Certificate cert) throws NoSuchAlgorithmException, CertificateEncodingException 
{
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] der = cert.getSignature();

    md.update(der);


    byte[] digest = md.digest();

    digest=md.digest(digest);


    return hexify(digest);

}
