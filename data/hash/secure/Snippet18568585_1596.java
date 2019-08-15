private byte[] signCAdES(byte[] aDocument, PrivateKey aPrivateKey, Certificate[] certChain)
        throws GeneralSecurityException {
    byte[] digitalSignature = null;
    try {

        Security.addProvider(new BouncyCastleProvider());
        ArrayList<X509Certificate> certsin = new ArrayList<X509Certificate>();
        for (int i = 0; i < certChain.length; i++) {
            certsin.add((X509Certificate) certChain[i]);
        }
        X509Certificate cert = certsin.get(0);
        //Nel nuovo standard di firma digitale e' richiesto l'hash del certificato di sottoscrizione:
        String digestAlgorithm = "SHA-256";
        String digitalSignatureAlgorithmName = "SHA256withRSA";
        MessageDigest sha = MessageDigest.getInstance(digestAlgorithm);
        byte[] digestedCert = sha.digest(cert.getEncoded());

        //Viene ora create l'attributo ESSCertID versione 2 cosi come richiesto nel nuovo standard:
        AlgorithmIdentifier aiSha256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
        ESSCertIDv2 essCert1 = new ESSCertIDv2(aiSha256, digestedCert);
        ESSCertIDv2[] essCert1Arr = {essCert1};
        SigningCertificateV2 scv2 = new SigningCertificateV2(essCert1Arr);
        Attribute certHAttribute = new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificateV2, new DERSet(scv2));

        //Aggiungiamo l'attributo al vettore degli attributi da firmare:
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(certHAttribute);
        AttributeTable at = new AttributeTable(v);
        CMSAttributeTableGenerator attrGen = new DefaultSignedAttributeTableGenerator(at);

        //Creaiamo l'oggetto che firma e crea l'involucro attraverso le librerie di Bouncy Castle:
        SignerInfoGeneratorBuilder genBuild = new SignerInfoGeneratorBuilder(new BcDigestCalculatorProvider());
        genBuild.setSignedAttributeGenerator(attrGen);

        //Si effettua la firma con l'algoritmo SHA256withRSA che crea l'hash e lo firma con l'algoritmo RSA:
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ContentSigner shaSigner = new JcaContentSignerBuilder("SHA256withRSA").build(aPrivateKey);
        SignerInfoGenerator sifGen = genBuild.build(shaSigner, new X509CertificateHolder(cert.getEncoded()));
        gen.addSignerInfoGenerator(sifGen);
        X509CollectionStoreParameters x509CollectionStoreParameters = new X509CollectionStoreParameters(certsin);
        JcaCertStore jcaCertStore = new JcaCertStore(certsin);
        gen.addCertificates(jcaCertStore);

        CMSTypedData msg = new CMSProcessableByteArray(aDocument);
        CMSSignedData sigData = gen.generate(msg, false); // false=detached

        byte[] encoded = sigData.getEncoded();

        FileOutputStream fos = new FileOutputStream("H:\\prova2.txt.p7m");
        fos.write(attach(aDocument, encoded));
        fos.flush();
        fos.close();
        digitalSignature = encoded;


    } catch (CMSException ex) {
        ex.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    } catch (OperatorCreationException ex) {
        ex.printStackTrace();
    }
    return digitalSignature;
}
