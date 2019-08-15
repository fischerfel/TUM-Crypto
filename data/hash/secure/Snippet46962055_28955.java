private static void checkPublicKeyHash(String publicKeyHash, X509Certificate paymentProcessingCertificate)
        throws NoSuchAlgorithmException, CertificateException {

    String certHash = Base64.getEncoder().encodeToString(
            MessageDigest.getInstance("SHA-256").digest(
                    paymentProcessingCertificate.getPublicKey().getEncoded()));
    if (!Objects.equals(publicKeyHash, certHash)) {
        throw new DigestException(String.format(
                "publicKeyHash %s doesn't match Payment Processing Certificate hash %s",
                publicKeyHash, certHash));
    }
}
