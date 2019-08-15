private static void signCAdES(byte[] aDocument, PrivateKey aPrivateKey, Certificate[] certChain, String outputPath) {
    try {

        Security.addProvider(new BouncyCastleProvider());
        ArrayList<X509Certificate> certsin = new ArrayList<X509Certificate>();
        for (Certificate certChain1 : certChain) {
            certsin.add((X509Certificate) certChain1);
        }

        X509Certificate signingCertificate= certsin.get(0);

        MessageDigest dig = MessageDigest.getInstance("SHA-1");
        byte[] certHash = dig.digest(signingCertificate.getEncoded());

        ESSCertID essCertid = new ESSCertID(certHash);
        DERSet set = new DERSet(new SigningCertificate(essCertid));

        Attribute certHAttribute = new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificate, set);
        AttributeTable at = getAttributeTableWithSigningCertificateAttribute(certHAttribute);
        CMSAttributeTableGenerator attrGen = new DefaultSignedAttributeTableGenerator(at);

        SignerInfoGeneratorBuilder genBuild = new SignerInfoGeneratorBuilder(new BcDigestCalculatorProvider());
        genBuild.setSignedAttributeGenerator(attrGen);

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ContentSigner shaSigner = new JcaContentSignerBuilder("SHA1withRSA").build(aPrivateKey);
        SignerInfoGenerator sifGen = genBuild.build(shaSigner, new X509CertificateHolder(signingCertificate.getEncoded()));
        gen.addSignerInfoGenerator(sifGen);
        JcaCertStore jcaCertStore = new JcaCertStore(certsin);
        gen.addCertificates(jcaCertStore);

        CMSTypedData msg = new CMSProcessableByteArray(aDocument);
        CMSSignedData sigData = gen.generate(msg, false); // false=detached

        byte[] encoded = sigData.getEncoded();

        ASN1InputStream in = new ASN1InputStream(encoded);
        CMSSignedData sigData2 = new CMSSignedData(new CMSProcessableByteArray(aDocument), in);
        byte[] encoded2 = sigData2.getEncoded();

        FileOutputStream fos = new FileOutputStream(outputPath);
        fos.write(encoded2);
//      fos.write(encoded);
        fos.flush();
        fos.close();
    } catch (CMSException | IOException | OperatorCreationException | CertificateEncodingException ex) {
        log("signCAdES", "Error: " + ex.toString());
    }
}
