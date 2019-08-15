    @Override
public byte[] sign(InputStream content) throws IOException {
    // testSHA1WithRSAAndAttributeTable
    try {
        MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
        List<Certificate> certList = new ArrayList<Certificate>();
        CMSTypedData msg = new CMSProcessableByteArray(IOUtils.toByteArray(content));

        certList.add(certificate);

        Store certs = new JcaCertStore(certList);

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

        Attribute attr = new Attribute(CMSAttributes.messageDigest,
                new DERSet(new DEROctetString(md.digest(IOUtils.toByteArray(content)))));

        ASN1EncodableVector v = new ASN1EncodableVector();

        v.add(attr);

        SignerInfoGeneratorBuilder builder = new SignerInfoGeneratorBuilder(new BcDigestCalculatorProvider())
                .setSignedAttributeGenerator(new DefaultSignedAttributeTableGenerator(new AttributeTable(v)));

        AlgorithmIdentifier sha1withRSA = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(certificate.getEncoded());
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);

        gen.addSignerInfoGenerator(builder.build(
                new BcRSAContentSignerBuilder(sha1withRSA,
                        new DefaultDigestAlgorithmIdentifierFinder().find(sha1withRSA))
                                .build(PrivateKeyFactory.createKey(privateKey.getEncoded())),
                new JcaX509CertificateHolder(cert)));

        gen.addCertificates(certs);

        CMSSignedData s = gen.generate(new CMSAbsentContent(), false);
        return new CMSSignedData(msg, s.getEncoded()).getEncoded();

    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new IOException(e);
    }

}
