Attribute signingCertificateAttribute;
MessageDigest dig = MessageDigest.getInstance(DigestAlgorithm().getName(),
    new BouncyCastleProvider());

byte[] certHash = dig.digest(SigningCertificate().getEncoded());

if (DigestAlgorithm() == DigestAlgorithm.SHA1) {
    SigningCertificate sc = new SigningCertificate(new ESSCertID(certHash));

    signingCertificateAttribute = new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificate, new DERSet(sc));

} else {
    ESSCertIDv2 essCert = new ESSCertIDv2(new AlgorithmIdentifier(DigestAlgorithm().getOid()), certHash);
    SigningCertificateV2 scv2 = new SigningCertificateV2(new ESSCertIDv2[] { essCert });

    signingCertificateAttribute =  new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificateV2, new DERSet(scv2));
}
