private byte[] SignByCertificate__Privileged(String str_CertName, String plainData)  {
    X509Certificate signerCertificate = null;
    Signature signatureEngine;
    try {
        signatureEngine = Signature.getInstance("ExternalSHA1WithRSA", iaikPkcs11Provider_.getName());
        tokenKeyStore = KeyStore.getInstance("PKCS11KeyStore", iaikPkcs11Provider_.getName());
        if (tokenKeyStore == null) {
            System.out.println("Got no key store. Ensure that the provider is"
                    + " properly configured and installed.");
        }
        tokenKeyStore.load(null, null); // this call binds the keystore to        
        aliases = tokenKeyStore.aliases();

        while (aliases.hasMoreElements()) {
            String keyAlias = aliases.nextElement().toString();
            if (str_CertName.equals(keyAlias)) {

                Key key = (Key) tokenKeyStore.getKey(keyAlias, null);
                if (key instanceof PrivateKey) {
                    Certificate[] certificateChain;
                    certificateChain = (Certificate[]) tokenKeyStore.getCertificateChain(keyAlias);
                    signerCertificate = (X509Certificate) certificateChain[0];
                    boolean[] keyUsage = signerCertificate.getKeyUsage();
                    if ((keyUsage == null) || keyUsage[0] || keyUsage[1]) { // check for digital signature or non-repudiation, but also accept if none set
                       System.out.println("The signature key is: " + key);

                        System.out.println("The signer certificate is:");
                        System.out.println(signerCertificate.toString());
                        System.out.println("##########");
                        signatureKey_ = (PrivateKey) key;
                        break;
                    }
                }
            }
        }
        MessageDigest digestEngine = MessageDigest.getInstance("SHA-1");

        byte[] dataBuffer = plainData.getBytes();
        digestEngine.update(dataBuffer);
        byte[] contentHash = digestEngine.digest();
        // create the SignedData
        SignedData signedData = new SignedData(dataBuffer, SignedData.IMPLICIT);
        // set the certificates
        signedData.setCertificates(new X509Certificate[]{signerCertificate});
        // create a new SignerInfo
        SignerInfo signerInfo = new SignerInfo(new IssuerAndSerialNumber(signerCertificate), AlgorithmID.sha1, null);
        // define the authenticated attributes
        iaik.asn1.structures.Attribute[] authenticatedAttributes = {
            new Attribute(ObjectID.contentType, new ASN1Object[]{ObjectID.pkcs7_data}),
            new Attribute(ObjectID.signingTime, new ASN1Object[]{new ChoiceOfTime().toASN1Object()}),
            new Attribute(ObjectID.messageDigest, new ASN1Object[]{new OCTET_STRING(contentHash)})
        };
        // set the authenticated attributes
        signerInfo.setAuthenticatedAttributes(authenticatedAttributes);
        // encode the authenticated attributes, which is the data that we must sign
        byte[] toBeSigned = DerCoder.encode(ASN.createSetOf(authenticatedAttributes, true));
        // we can use the digest engine from above
        byte[] hashToBeSigned = digestEngine.digest(toBeSigned);
        // according to PKCS#11 building the DigestInfo structure must be done off-card
        DigestInfo digestInfoEngine = new DigestInfo(AlgorithmID.sha1, hashToBeSigned);
        byte[] toBeEncrypted = digestInfoEngine.toByteArray();
        // initialize for signing
        signatureEngine.initSign((java.security.PrivateKey) signatureKey_);
        signatureEngine.update(toBeEncrypted);
        // sign the data to be signed
        byte[] signatureValue = signatureEngine.sign();
        // set the signature value in the signer info
        signerInfo.setEncryptedDigest(signatureValue);
        // and add the signer info object to the PKCS#7 signed data object
        signedData.addSignerInfo(signerInfo);
        SignerInfo[] signerInfos = signedData.getSignerInfos();
        // verify the signatures
        for (int i = 0; i < signerInfos.length; i++) {
            try {
                // verify the signature for SignerInfo at index i                
                signerCertificate = signedData.verify(i);// Exception.................

                System.out.println("Signature OK from signer with certificate: ");
                System.out.println(signerCertificate);
                System.out.println();

            } catch (SignatureException ex) {
                // if the signature is not OK a SignatureException is thrown
                JOptionPane.showMessageDialog(this, ex);
                System.out.println("Signature ERROR from signer with certificate: ");
                System.out.println(signedData.getCertificate(signerInfos[i]
                        .getIssuerAndSerialNumber()));
                System.out.println();
                ex.printStackTrace();

            }
        }
        ASN1Object obj_ASN1Object = signedData.toASN1Object();
        ASN1 obj_ASN1 = new ASN1(obj_ASN1Object);
        byte[] byte_Array = obj_ASN1.toByteArray();
        return byte_Array;

    } catch (CodingException | IOException | PKCSException| InvalidKeyException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | UnrecoverableKeyException | CertificateException ex) {
        JOptionPane.showMessageDialog(null, ex);
    }
    obj_Dialog.dispose();
    return null;
}
