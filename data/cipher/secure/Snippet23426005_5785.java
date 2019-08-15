    // The CMS container
    CMSSignedData cms = new CMSSignedData(bytes);

    // Calculating the digest of the signed attributes
    SignerInformation signerInformation = (SignerInformation) (cms.getSignerInfos().getSigners().iterator().next());
    byte[] derSignedAttributes = signerInformation.getEncodedSignedAttributes();
    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
    byte[] derSignedAttributesHash = sha1.digest(derSignedAttributes);

    // Retrieving the public key from the (single) certificate in the container
    X509CertificateHolder cert = (X509CertificateHolder) cms.getCertificates().getMatches(new Selector() {
        public boolean match(Object arg0) { return true; }
        public Object clone()             { return this; }
    }).iterator().next();
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(cert.getSubjectPublicKeyInfo().getEncoded());
    KeyFactory keyFactory = KeyFactory.getInstance(publicKeySpec.getFormat());
    Key key = keyFactory.generatePublic(publicKeySpec);

    // Decrypting the DigestInfo from the RSA signature
    Cipher asymmetricCipher = Cipher.getInstance("RSA", "BC");
    asymmetricCipher.init(Cipher.DECRYPT_MODE, key);
    byte[] digestInfo = asymmetricCipher.doFinal(signerInformation.getSignature());
    DigestInfo digestInfoObject = new DigestInfo(ASN1Sequence.getInstance(digestInfo));

    System.out.println("Signed Attributes: " + toHex(derSignedAttributes));
    System.out.println("Signed Attributes Hash: " + toHex(derSignedAttributesHash));
    System.out.println("DigestInfo: " + toHex(digestInfo));
    System.out.println("DigestInfo Hash: " + toHex(digestInfoObject.getDigest()));
