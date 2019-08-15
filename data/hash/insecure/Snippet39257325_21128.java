ExternalSignatureCMSSignedDataGenerator gen = new ExternalSignatureCMSSignedDataGenerator();
ExternalSignatureSignerInfoGenerator signerGenerator = new ExternalSignatureSignerInfoGenerator(
                            CMSSignedDataGenerator.DIGEST_SHA1,
                            CMSSignedDataGenerator.ENCRYPTION_RSA);

//get X509 signer Certificate
FileInputStream fis = new FileInputStream("C:/myCertificate.cer");

CertificateFactory cf = CertificateFactory.getInstance("X.509");
X509Certificate cert = (X509Certificate) cf
        .generateCertificates(fis).iterator().next();

//add certificate for buildSigningCertificateV2Attribute method used in getBytesToSign
signerGenerator.setCertificate(cert);

try {
    // Obtain bytes to sign;
    // note that this implementation includes a timestamp
    // as an authenticated attribute, then bytesToSign is every time
    // different,
    // even if signing the same data.
    // The timestamp should be notified and accepted by the signer along
    // data to sign
    // BEFORE he applies encryption with his private key.
    // The timestamp is used during verification to check that signature
    // time is
    // in signing certificate validity time range.

    //bytes of file to be signed in base64
    String originalFile = "aG9sYQ0KYXNkYXMNCg0KYWZzDQo=";
    CMSProcessable msg = new CMSProcessableByteArray(
            DatatypeConverter.parseBase64Binary(originalFile));

    byte[] bytesToSign = signerGenerator.getBytesToSign(
            PKCSObjectIdentifiers.data, msg, "BC");

    // Digest generation. Digest algorithm must match the one passed to
    // ExternalSignatureSignerInfoGenerator
    // constructor above (SHA1, in this case).
    MessageDigest md = MessageDigest.getInstance("SHA1");
    md.update(bytesToSign);
    byte[] digest = md.digest();

    //sign digest
    Signature sig = Signature.getInstance("SHA1withRSA");

    //add signer private key
    sig.initSign(myPrivateKey);
    sig.update(digest);

    byte[] signedBytes = sig.sign(); // will contain encripted
                                                // digest

    byte[] certBytes = cert.getEncoded(); // will contain DER encoded
                                            // certificate

    // Digest encryption and signer certificate retrieval (using a
    // PKCS11 token, for example)
    // Encryption algorithm must match the one passed to
    // ExternalSignatureSignerInfoGenerator
    // constructor above (RSA, in this case).

    if ((certBytes != null) && (signedBytes != null)) {

        // build java Certificate object.
        // java.security.cert.CertificateFactory cf =
        // java.security.cert.CertificateFactory.getInstance("X.509");
        // java.io.ByteArrayInputStream bais = new
        // java.io.ByteArrayInputStream(certBytes);
        // java.security.cert.X509Certificate javaCert =
        // (java.security.cert.X509Certificate)
        // cf.generateCertificate(bais);

        // pass encrypted digest and certificate to the SignerInfo
        // generator
        signerGenerator.setCertificate(cert);
        signerGenerator.setSignedBytes(signedBytes);

        // pass the signer info generator to the cms generator
        gen.addSignerInf(signerGenerator);

        // generating a cert store with signer certificate into.
        // The store could contain also the root certificate and CRLS
        ArrayList certList = new ArrayList();
        certList.add(cert);
        CertStore store = CertStore.getInstance("Collection",
                new CollectionCertStoreParameters(certList), "BC");
        // pass cert store to the cms generator
        gen.addCertificatesAndCRLs(store);

        // Finally, generate CMS message.
        CMSSignedData s = gen.generate(msg, true);

        //verify data
        CMSVerifier verifier = new CMSVerifier(s);
        verifier.setDebug(true);
        verifier.basicVerify();

        //prints always false :(
        System.out.println("valid?: " +verifier.isIntegrityChecked());

    }
} catch (Exception ex) {
    ex.printStackTrace();
}
