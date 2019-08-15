public static String ComputeSTNodeID(Certificate cert) {
    MessageDigest md = MessageDigest.getInstance("SHA256");
    return BaseEncoding.base32().encode(md.digest(cert.getEncoded())).replaceAll("=", "");
}
